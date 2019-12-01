package com.palace.dream.simple.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.palace.dream.simple.db.SimpleTemplate;

public class SZS {
	SimpleTemplate sim;
	
	@Test
	public void download() throws Exception {
		sim = new SimpleTemplate(dataSource());
		int total = 60*10000;
		int pageSize = 500;
		int page = total / pageSize;
		for(int i =0;i<page;i++) {
			int m = i * pageSize;
			download(m,pageSize);
		}
	}
	public void download(int m,int size) {
		String sql = " select * from  ";
	}
    public static DataSource dataSource() throws SQLException {
    	String url = "jdbc:mysql://127.0.0.1:3306/dream_mysql?useUnicode=true&autoReconnect=true";
        DruidDataSource druid = new DruidDataSource();
        druid.setDriverClassName("com.mysql.jdbc.Driver");
        druid.setUsername("root");
        druid.setPassword("111111");
        druid.setUrl(url);
        return druid;
    }
}
