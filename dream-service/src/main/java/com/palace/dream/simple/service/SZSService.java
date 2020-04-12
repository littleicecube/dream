package com.palace.dream.simple.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.collections4.MapUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.palace.dream.simple.db.SimpleTemplate;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SZSService {

	LinkedBlockingQueue<Map<String, Object>> queue = new LinkedBlockingQueue(100);
	ExecutorService service = Executors.newFixedThreadPool(32);
	SimpleTemplate runner;
	AtomicLong counter = new AtomicLong();

	public SZSService() {
		DruidDataSource druid = new DruidDataSource();
		String url ="jdbc:mysql://localhost:3306/szs?useUnicode=true&autoReconnect=true&characterEncoding=UTF-8&socketTimeout=30000&connectTimeout=3000&allowMultiQueries=true";
		String driverClassName = "com.mysql.jdbc.Driver";
		String username= "root";
		String password = "111111";
		druid.setDriverClassName(driverClassName);
		druid.setUsername(username);
		druid.setPassword(password);
		druid.setUrl(url);
		runner = new SimpleTemplate(druid);
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						String sql =" SELECT i.lUserId,u.strName,u.strIdentity,i.strBorrowerFrontURL,i.strBorrowerBackURL " + 
								"from userInfo u join idInfo i on u.lBorrowerId = i.lUserId " + 
								"where i.nState = 0 " + 
								"limit 10,100 ";
						List<Map<String,Object>> mapList = runner.queryForList(sql);
						if(mapList.isEmpty()) {
							return;
						}
						for(Map<String,Object> map : mapList) {
							try {
								queue.put(map);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		service.submit(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Map<String, Object> map = queue.take();
						handler(map);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void handler(Map<String, Object> map) {
				String frontUrl = MapUtils.getString(map, "strBorrowerFrontURL");
				String backUrl = MapUtils.getString(map, "strBorrowerBackURL");
				String name =  MapUtils.getString(map, "strName").trim();
				String id =  MapUtils.getString(map, "strIdentity");
				id = DFAES.dec(id);
				String frontName = id+"-"+name+"-front";
				String backName =  id+"-"+name+"-back";
				downLoad(frontUrl,id+"-"+name,frontName+".jpg");
				downLoad(backUrl,id+"-"+name,backName+".jpg");
				long lId = MapUtils.getLongValue(map,"lUserId");
				String sql = "update idInfo set frontName = ? ,backName = ? ,nState = 1 where lUserId = ? ";
				runner.update(sql,frontName,backName,lId);
				long ll = counter.getAndIncrement();
				System.err.println(id+","+name+","+ll);
			}
		});
	}

	String path = "/data/df/";
	OkHttpClient client = new OkHttpClient();
	public void downLoad(String url, String name,String fileName) {
		File file = new File(path + name);
		file.mkdirs();
		file = new File(path + name + "/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Request req = new Request.Builder().url(url).build();
		FileOutputStream out = null;
		try {
			Response res = client.newCall(req).execute();
			byte[] bytes = res.body().bytes();
			out = new FileOutputStream(file);
			out.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String url = "http://file.dafy.com.cn/img/20150916/8c70e3608067423cbd9f08868b800ad2.jpg";
		new SZSService().start();
	}
}

/**
 * 
 * CREATE TABLE `userInfo` ( `lUserId` bigint(20) NOT NULL,
 * `strBorrowerFrontURL` varchar(255) COLLATE utf8_bin NOT NULL,
 * `strBorrowerBackURL` varchar(255) COLLATE utf8_bin NOT NULL, `backName`
 * varchar(125) COLLATE utf8_bin NOT NULL DEFAULT '', `frontName` varchar(125)
 * COLLATE utf8_bin NOT NULL DEFAULT '', `strStartTime` varchar(255) COLLATE
 * utf8_bin NOT NULL, `strEndTime` varchar(255) COLLATE utf8_bin NOT NULL,
 * `strBorrowerSelfURL` varchar(255) COLLATE utf8_bin NOT NULL,
 * `lBorrowIntentId` bigint(20) NOT NULL DEFAULT '0' COMMENT '借款意向编号', `nScore`
 * int(11) NOT NULL DEFAULT '0' COMMENT '借款意向编号', `nState` int(11) NOT NULL
 * DEFAULT '0' COMMENT '人脸比对状态：10:待执行;20:执行中;100:成功', `dtUpdateTime` datetime
 * DEFAULT NULL, `dtAtoUpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
 * ON UPDATE CURRENT_TIMESTAMP, `strResult` varchar(1024) COLLATE utf8_bin
 * DEFAULT NULL, `nBorrowMode` int(11) NOT NULL DEFAULT '0', PRIMARY KEY
 * (`lUserId`), KEY `idx_nState` (`nState`) USING HASH, KEY `index_intent`
 * (`lBorrowIntentId`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
 * 
 */
