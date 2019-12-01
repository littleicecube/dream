package com.palace.dream.simple.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.palace.dream.simple.dao.SimpleDao;

@Service
public class SimpleService {

	@Resource
	private SimpleDao simpleDao;
	
}
