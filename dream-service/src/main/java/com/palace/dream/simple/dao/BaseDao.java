package com.palace.dream.simple.dao;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;

import com.palace.dream.simple.db.SimpleTemplate;

public class BaseDao {

	@Resource
	@Qualifier("simpleTemplate0")
	private SimpleTemplate simpleTemplate0;
	
	public SimpleTemplate getDefaultTemplate() {
		return simpleTemplate0;
	}
}
