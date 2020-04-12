package com.palace.dream.simple.db;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class SimpleTemplate extends JdbcTemplate{
	
	public SimpleTemplate(DataSource dataSource) {
		super(dataSource);
	}
	public Map<String,Object> queryForMap(String sql){
		List<Map<String,Object>> mapList = this.queryForList(sql);
		if(mapList == null || mapList.isEmpty()) {
			return null;
		}
		return mapList.get(0);
	}
	public Map<String,Object> queryForMap(String sql,Object ...objects){
		List<Map<String,Object>> mapList = this.queryForList(sql,objects);
		if(mapList == null || mapList.isEmpty()) {
			return null;
		}
		return mapList.get(0);
	}
	public int update(String tbName,Map<String,Object> map,Object ...objects) {
		StringBuilder sb = new StringBuilder("update ");
		sb.append(tbName).append(" set ");
		Object[] params = null;
		if(objects != null) {
			params = new Object[map.size() + (objects.length)/2];
		}else {
			params = new Object[map.size()];
		}
		int counter = 0;
		for(Map.Entry<String,Object> entry : map.entrySet()) {
			sb.append(" `").append(entry.getKey()).append("` = ?,");
			params[counter++] = entry.getValue();
		}
		sb.deleteCharAt(sb.length() - 1);
		if(objects != null) {
			sb.append(" where ");
			for(int i=0;i<objects.length;) {
				sb.append(" `").append(objects[i]).append("` =? and");
				params[counter++] = objects[i+1];
				i = i+2;
			}
		}
		sb.delete(sb.length()-3,sb.length());
		String sql = sb.toString();
		return update(sql,params);
	}
	public long insert(String table,Map<String, Object> map) {
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(table).append(" ( ");
		int c = 0;
		Object[] objects = new Object[map.size()];
		StringBuilder sbValue = new StringBuilder(" values(");
		for(Map.Entry<String,Object> entry : map.entrySet()) {
			sb.append("`").append(entry.getKey().trim()).append("`").append(",");
			sbValue.append("?,");
			objects[c++] = entry.getValue();
		}
		sbValue.deleteCharAt(sbValue.length() - 1).append(")");
		String sql = sb.deleteCharAt(sb.length()-1).append(")") + sbValue.toString();
		return insert(sql, objects);
	}
	public long insert(String sql,Object ...objects) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(int i=0;i<objects.length;i++) {
            	ps.setObject(i+1,objects[i]);
            }
            return ps;
        };
        int i = this.update(psc,keyHolder);
        if(i < 1) {
        	throw new RuntimeException("数据添加失败:"+sql);
        }
        return keyHolder.getKey().longValue();
	}
	
	public List<Map<String,Object>> getSchem(String dbName,String tbName){
		String sql = "SELECT * from  information_schema.COLUMNS where TABLE_SCHEMA = ? and TABLE_NAME =? ";
		return this.queryForList(sql,dbName,tbName);
	}
	public void printTable(String dbName,String tbName) {
		for(Map<String,Object> map : getSchem(dbName,tbName)) {
			String columnName = (String) map.get("COLUMN_NAME");
			String line = "public static String "+columnName+" = \""+columnName.trim()+"\";";
			System.err.println(line);
		}
	}
	
}
