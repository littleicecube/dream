package com.dream.db;

import java.util.HashMap;

public class CMap extends HashMap<String, Object>{
	
	public Long getLong(String key) {
		return (Long) this.get(key);
	}
	public Integer getInt(String key) {
		return (Integer) this.get(key);
	}
	public String getString(String key) {
		return (String) this.get(key);
	}
	public Boolean getBool(String key) {
		return (Boolean) this.get(key);
	}
	public Character getChar(String key) {
		return (Character) this.get(key);
	}
	public Double getDouble(String key) {
		return (Double) this.get(key);
	}
	public Float getFloat(String key) {
		return (Float) this.get(key);
	}
}
