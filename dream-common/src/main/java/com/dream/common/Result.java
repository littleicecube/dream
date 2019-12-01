package com.dream.common;

public class Result {

	public static int CODE_SUCCESS = 0;
	public static int CODE_ERROR = -1;
	public static String MSG_SUCCESS = "success";
	public static String MSG_ERROR = "error";
	
	private int code;
	private String msg;
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
		return new Result(CODE_SUCCESS,MSG_SUCCESS,data);
	}
	
	public static Result err(String msg) {
		return new Result(CODE_ERROR,msg,null);
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
}
