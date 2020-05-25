package com.palace.dream.simple.service;

import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JSSH {
	
	Session session;
	static Map<String,Object> map = new HashMap();
	public static JSSH es(String remoteHost,int remotePort) {
		String key = "es";
		JSSH jssh = (JSSH) map.get(key);
		if(jssh == null ) {
			int sshPort = 22;
			String sshHost = "154.8.233.122";
			String sshUserName = "rd";
			String sshPassword ="Bnth27E!kcpYUAdh";
			int localPort = remotePort;
			jssh = new JSSH(sshHost,sshPort,sshUserName,sshPassword,remoteHost,remotePort,localPort);
			map.put(key,jssh);
		}
		return jssh;
 	}
    public JSSH(String sshHost,int sshPort,String sshUserName,String sshPassword,String remoteHost,int remotePort,int localPort){
        JSch jsch = new JSch();
        try {
	        session = jsch.getSession(sshUserName, sshHost, sshPort);
	        session.setPassword(sshPassword);
	        // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.connect();
	        // 打印SSH服务器版本信息
	        System.err.println(session.getServerVersion());
	        // 设置SSH本地端口转发,本地转发到远程
	        int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);
	        System.err.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
        }catch(Exception e) {
        	throw new RuntimeException("",e);
        }
    }
	
}
