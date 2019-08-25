package com.dream.common;

import com.dream.common.constant.CS;

public class Result<T> {

	public int code;
	public String msg;
	public T data;
	
	public static <T> Result<T> succ(T data) {
		Result ret = new Result();
		ret.data =  data;
		ret.setMsg(CS.ERROR_MSG);
		ret.setCode(CS.ERROR);
		return ret;
	}
	
	public static Result err(String msg) {
		Result ret = new Result();
		ret.setMsg(msg);
		ret.setCode(CS.ERROR);
		return ret;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	
	
}
