package com.dream.common;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyListener {

	public static void main(String args[]) {
        try{
            ServerSocket server=null;
            try{
                //创建一个ServerSocket在端口4700监听客户请求
                server=new ServerSocket(4700);
            }catch(Exception e){
                e.printStackTrace();//出错，打印出错信息
            }
            Socket socket=null;
            try{
            	System.err.println("start acc");
                //使用accept()阻塞等待客户请求，有客户
                socket=server.accept();//请求到来则产生一个Socket对象，并继续执行
            }catch(Exception e){
                e.printStackTrace();//出错，打印出错信息
            }
            System.err.println("cnn");
            InputStream ins = socket.getInputStream();
            byte[] arr = new byte[1024];
            int len = -1;
            while((len = ins.read(arr)) != -1){
            	String str = new String(arr,0,len);
            	System.out.println(str);
            }
            
        }catch(Exception e){
            e.printStackTrace();//出错，打印出错信息
        }
    }
}
