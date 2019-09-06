package com.dream.db;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class SimpleTemplate extends JdbcTemplate{
	
	public SimpleTemplate(DataSource ds) {
		super(ds);
	}
	
	@Override
	public Map<String, Object> queryForMap(String sql){
		try {
			return super.queryForMap(sql);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
