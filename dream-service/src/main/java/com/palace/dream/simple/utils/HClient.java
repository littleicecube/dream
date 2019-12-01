package com.palace.dream.simple.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.dream.common.Result;

public class HClient {


    private static final int CONNECT_TIMEOUT = 10*1000;// 设置连接建立的超时时间为10s
    private static final int SOCKET_TIMEOUT = 100;
    private static final int MAX_CONN = 300; // 最大连接数
    private static final int Max_PRE_ROUTE = 300;
    private static final int MAX_ROUTE = 300;
    private static CloseableHttpClient httpClient; // 发送请求的客户端单例
    private static PoolingHttpClientConnectionManager manager; //连接池管理类
    private static ScheduledExecutorService monitorExecutor;

    static {
    	httpClient = HttpClientBuilder.create().build();
    }
    public static byte[] get(String url) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			response = httpClient.execute(httpGet);
	        if (response.getStatusLine().getStatusCode() == 200) {
	            response.getEntity().writeTo(baos);
	            return baos.toByteArray();
	        }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
    }
    private static void setRequestConfig(HttpRequestBase httpRequestBase){
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();

        httpRequestBase.setConfig(requestConfig);
    }
}
