package com.palace.dream.simple.db.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.palace.dream.simple.db.SimpleTemplate;

@Configuration
public class TemplateConfig {
	
	@Bean(name = "simpleTemplate0")
    JdbcTemplate getJdbcTemplate(@Qualifier("dataSource0") DataSource dataSource) {
    	return new SimpleTemplate(dataSource);
    }
	
}
