package com.dream.common.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;


public class HClient {
	private static int CONNECT_TIMEOUT = 10 * 1000; //连接建立时间
	private static int CONNECT_SOCKET_TIMEOUT = 60 * 1000; //数据传输过程中数据包之间间隔的最大时间
	private static int CONNECTION_REQUEST_TIMEOUT = 10*100;//获取连接超时时间
	private static int CONNECTION_MAX_TOTAL = 300;
	private static int CONNECTION_MAX_PER_ROUTE = 100;

	static volatile CloseableHttpClient httpClient;
	public static void init(int connectTimeout,int connectionSocketTimeout,
							int connectionRequestTimeout,int connectionMaxTotal,
							int connectionMaxPerRoute) {
		CONNECT_TIMEOUT = connectTimeout;
		CONNECT_SOCKET_TIMEOUT = connectionSocketTimeout;
		CONNECTION_REQUEST_TIMEOUT =connectionRequestTimeout;
		CONNECTION_MAX_TOTAL = connectionMaxTotal;
		CONNECTION_MAX_PER_ROUTE = connectionMaxPerRoute;
	}
	public static CloseableHttpClient getClient(){
		if(httpClient == null ) {
			synchronized (HClient.class) {
				if(httpClient == null) {
					httpClient = HttpClients.custom()
			                .setConnectionManager(getConnectionManager())
			                .setKeepAliveStrategy(getStrategy())
			                .setRetryHandler(new DefaultHttpRequestRetryHandler(0,false))
			                .setDefaultRequestConfig(getRequestConfig())
			                .build();
				}
			}
			
		}
		return httpClient;
	}
	
	public static PoolingHttpClientConnectionManager getConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(CONNECTION_MAX_TOTAL);
		connectionManager.setDefaultMaxPerRoute(CONNECTION_MAX_PER_ROUTE);
		return connectionManager;
	}
	public static RequestConfig getRequestConfig() {
		return RequestConfig.custom()
			.setConnectTimeout(CONNECT_TIMEOUT)
			.setSocketTimeout(CONNECT_SOCKET_TIMEOUT)
			.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
			.build();
			
	}
	public static ConnectionKeepAliveStrategy getStrategy() {
		ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
		    @Override
		    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		        HeaderElementIterator it = new BasicHeaderElementIterator
		            (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		        while (it.hasNext()) {
		            HeaderElement he = it.nextElement();
		            String param = he.getName();
		            String value = he.getValue();
		            if (value != null && param.equalsIgnoreCase
		               ("timeout")) {
		                return Long.parseLong(value) * 1000;
		            }
		        }
		        return 60 * 1000;
		    }
		};
		return myStrategy;
	}
	
	

    public static HResult postPrint(String url,Object ...objects){
        HResult hResult = post(url, objects);
        String data = hResult.getData();
        if(data != null){
            try {
               data = JSONObject.toJSONString(JSONObject.parse(data),true);
            }catch (Exception e){
            }
        }
        Map<String, Object> map =new HashMap<String, Object>();
        System.err.println("response:");
        System.err.println(data);
        return hResult;
    }


    public static HResult post(String url, Object ...objects){
        Map<String,Object> map = new HashMap<String, Object>();
        if(objects != null ) {
            if(objects.length == 1){
                Object obj = objects[0];
                if(obj instanceof  Map){
                    return post(url,(Map<String,Object>)obj);
                }else{
                    return HResult.err("参数个数错误");
                }
            }
            for (int i = 0; i < objects.length; ) {
                map.put((String) objects[i], objects[i + 1]);
                i += 2;
            }
        }
        return post(url,map);
    }
    
    public static HResult post(String url, Map<String,Object> map){
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE,"application/json");
        CloseableHttpResponse response = null;
        try {
            String json = JSONObject.toJSONString(map);
            StringEntity se = new StringEntity(json);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            httpPost.setEntity(se);
            response = getClient().execute(httpPost);
            if(response == null){
                return HResult.err("响应结果为null");
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if( statusCode != HttpStatus.SC_OK){
                return HResult.err(statusCode,"");
            }
            HttpEntity entity = response.getEntity();
            return HResult.succ(EntityUtils.toString(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return HResult.err(e.getMessage());
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
