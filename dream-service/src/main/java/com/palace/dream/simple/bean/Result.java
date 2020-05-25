package com.palace.dream.simple.bean;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Result {

	public static int CODE_DEL = 1;
	public static int CODE_SUCCESS = 0;
	public static int CODE_ERROR = -1;
	public static String SIMPLE_KEY = "simpleKey";
	public static String MSG_SUCCESS = "success";
	public static String MSG_ERROR = "error";
	
	private int code;
	private String msg = "";
	private Object data;
	
	public Result(int code,String msg,Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public static Result succ() {
		return new Result(CODE_SUCCESS,MSG_SUCCESS,null);
	}
	public static Result succ(Object data) {
		Result result = new Result(CODE_SUCCESS,MSG_SUCCESS,new HashMap<String,Object>(4));
		result.put(SIMPLE_KEY,data);
		return result;
	}
	public static Result succ(int code,Object data) {
		Result result = new Result(code,MSG_SUCCESS,new HashMap<String,Object>(4));
		result.put(SIMPLE_KEY,data);
		return result;
	}
	public static Result err(String msg) {
		return new Result(CODE_ERROR,msg,null);
	}
	public static Result err(int code,String msg) {
		return new Result(code,msg,null);
	}
	public Result put(String key,Object value) {
		return this;
	}
	public Map<String,Object> getMap(){
		return (Map<String,Object>)this.data;
	}
	public boolean isSucc() {
		return code == CODE_SUCCESS;
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
	 
	public Result setMap(Map<String,Object> map ) {
		this.data = map;
		return this;
	}
	
	public String getString() {
		return (String)this.data;
	}
	public Integer getInt() {
		return (Integer)this.data;
	}
	public Long getLong() {
		return (Long)this.data;
	}
	public double getDouble() {
		return (Double)this.data;
	}
}
