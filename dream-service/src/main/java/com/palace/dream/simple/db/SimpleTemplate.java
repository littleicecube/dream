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
	public Map<String,Object> queryForMap(String sql,Object ...objects){
		List<Map<String,Object>> mapList = this.queryForList(sql,objects);
		if(mapList == null || mapList.isEmpty()) {
			return null;
		}
		return mapList.get(0);
	}
	public long insert(String table,Map<String, Object> map) {
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(table).append(" ( ");
		
		StringBuilder sbValue = new StringBuilder(" values(");
		for(Map.Entry<String,Object> entry : map.entrySet()) {
			sb.append("`").append(entry.getKey().trim()).append("`").append(",");
			sbValue.append("?,");
		}
		sbValue.deleteCharAt(sbValue.length() - 1).append(")");
		String sql = sb.deleteCharAt(sb.length()-1).append(")") + sbValue.toString();
		return insert(sql, map.values().toArray(new Object[map.size()]));
	}
	
	public long insert(String sql,Object ...objects) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(int i=0;i<objects.length;i++) {
            	ps.setObject(i+1,objects[1]);
            }
            return ps;
        };
        int i = this.update(psc);
        if(i < 1) {
        	throw new RuntimeException("数据添加失败:"+sql);
        }
        return keyHolder.getKey().longValue();
	}
	
}
