package com.palace.dream.simple.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.palace.dream.simple.bean.Tables;

@Repository
public class UserDao extends BaseDao{
	
	private String tableName = Tables.TABLE_USER;
	
	public long addUser(Map<String,Object> map){
		return getDefaultTemplate().insert(tableName, map);
	}
}
