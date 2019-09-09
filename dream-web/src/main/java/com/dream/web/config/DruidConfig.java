package com.dream.web.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidConfig {
    public static DataSource getDS() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/ds");
		ds.setUsername("root");
		ds.setPassword("111111");
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		return ds;
    }
    
    public static DataSourceTransactionManager primaryTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(getDS());
    }
}

