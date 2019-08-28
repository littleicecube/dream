package com.dream.common;

import java.util.HashMap;
import java.util.Map;

public class Ret {
	
	private static final int SUCCESS_CODE = 0;
	private static final int ERROR_CODE = -1;
	
    private int code = 0;
    private String msg = "";
    private Map<String,Object> data;
    private Object simple ;
    
    public static Ret err(String msg) {
    	return new Ret(ERROR_CODE,msg);
    }
    public static Ret succ() {
    	return new Ret(SUCCESS_CODE,"success");
    }
    public static Ret succ(Object obj) {
    	Ret ret = new Ret(SUCCESS_CODE,"success");
    	ret.simple = obj;
    	return ret;
    }
    public static Ret succ(String msg) {
    	return new Ret(SUCCESS_CODE,msg);
    }
    public static Ret succ(Object obj,String msg) {
    	return new Ret(SUCCESS_CODE,msg).setSim(obj);
    }
    
    public Ret() {}
    
    public Ret(int code ,String msg) {
    	this.code = code;
    	this.msg = msg;
    }
    public String getStr(String key) {
    	return (String) this.data.get(key);
    }
    public String getSimStr() {
    	return (String) this.simple;
    }
    public Long getLong(String key) {
    	return (Long) this.data.get(key);
    }
    public Long getSimLong() {
    	return (Long)this.simple;
    }
    public Integer getInt(String key) {
    	return (Integer) this.data.get(key);
    }
    public Integer getSimInt() {
    	return (Integer)this.simple;
    }
    public Float getFloat(String key) {
    	return (Float) this.data.get(key);
    }
    public Float getSimFloat() {
    	return (Float) this.simple;
    }
    public Double getDouble(String key) {
    	return (Double) this.data.get(key);
    }
    public Double getSimDouble() {
    	return (Double)this.simple;
    }
    public Ret setSim(Object obj) {
    	 this.simple = obj;
    	 return this;
    }
    public Ret put(String key,Object value) {
    	if(this.data == null ) {
    		this.data = new HashMap<String, Object>();
    	}
    	this.data.put(key, value);
    	return this;
    }
    public boolean isSucc() {
    	return this.code == SUCCESS_CODE;
    }
    public boolean isErr() {
    	return this.code == ERROR_CODE;
    }
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
    public Object getSimple() {
    	return this.simple;
    }
}
