package com.dream.common.http;

public class HResult {
    public static int CODE_SUCC = 0;
    public static int CODE_ERR = -1;

    private int code = 0;
    private String msg;
    private String data;

    public static HResult err(int code, String msg){
        return new HResult(CODE_ERR,msg,"");
    }
    public static HResult err(String msg){
        return new HResult(CODE_ERR,msg,"");
    }
    public static HResult succ(String data){
        return new HResult(CODE_SUCC,"succ",data);
    }
    public HResult(){}
    public HResult(int code, String msg, String data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public boolean isSucc(){
        return this.code == CODE_SUCC;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
    
}
