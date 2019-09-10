package com.dream.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class DB extends JdbcTemplate{

	SimpleTemplate defaultTemplate;
	
	public DB() {
		DataSource ds = DSConfig.getDS();
		defaultTemplate = new SimpleTemplate(ds);
	}
	
	public long insert(String sql,Object ...args) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		getWriteTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				for(int i=0;i<args.length;i++) {
					ps.setObject(i+1, args[i]);
				}
				return ps;
			}
		});
		return holder.getKey().longValue();
	}
	
	public int update(String sql,Object ...args) {
		return getWriteTemplate().update(sql, args);
	}
	public List<Map<String,Object>>  queryForList(String sql){
		return getReadTemplate().queryForList(sql);
	}
	public List<Map<String,Object>>  queryForList(String sql,Object ...args){
		return getReadTemplate().queryForList(sql,args);
	}
	public CMap queryCMap(String sql) {
		return null;
	}
	public Map<String,Object> queryForMap(String sql){
		return getReadTemplate().queryForMap(sql);
	}
	public Map<String,Object> queryForMap(String sql,Object ...args){
		return getReadTemplate().queryForMap(sql,args);
	}
	
	public SimpleTemplate getReadTemplate() {
		return defaultTemplate;
	}
	public SimpleTemplate getWriteTemplate() {
		return defaultTemplate;
	}
	
	public class MapRowMapper implements RowMapper<Map<String, Object>> {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			Map<String, Object> mapOfColumnValues = createColumnMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				String column = JdbcUtils.lookupColumnName(rsmd, i);
				mapOfColumnValues.putIfAbsent(getColumnKey(column), getColumnValue(rs, i));
			}
			return mapOfColumnValues;
		}

		/**
		 * Create a Map instance to be used as column map.
		 * <p>By default, a linked case-insensitive Map will be created.
		 * @param columnCount the column count, to be used as initial
		 * capacity for the Map
		 * @return the new Map instance
		 * @see org.springframework.util.LinkedCaseInsensitiveMap
		 */
		protected Map<String, Object> createColumnMap(int columnCount) {
			return new LinkedCaseInsensitiveMap<>(columnCount);
		}

		/**
		 * Determine the key to use for the given column in the column Map.
		 * @param columnName the column name as returned by the ResultSet
		 * @return the column key to use
		 * @see java.sql.ResultSetMetaData#getColumnName
		 */
		protected String getColumnKey(String columnName) {
			return columnName;
		}

		/**
		 * Retrieve a JDBC object value for the specified column.
		 * <p>The default implementation uses the {@code getObject} method.
		 * Additionally, this implementation includes a "hack" to get around Oracle
		 * returning a non standard object for their TIMESTAMP datatype.
		 * @param rs is the ResultSet holding the data
		 * @param index is the column index
		 * @return the Object returned
		 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue
		 */
		@Nullable
		protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
			return JdbcUtils.getResultSetValue(rs, index);
		}

	}
}
