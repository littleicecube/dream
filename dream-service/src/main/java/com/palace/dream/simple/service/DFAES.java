package com.palace.dream.simple.service;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.MapUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class DFAES {

	  private Cipher cipherEncode = null;
	  private Cipher cipherDecode = null;
	  static DFAES a=new DFAES();
	  static {
		a.init("DECSECURITYKEYABCDEFG", "EiJPWIgQQDgoJXlRy91SZncpdZgwQEHi");
	  }
	  
	  public static synchronized String dec(String str){
		  return a.decode(str);
	  }
	  public static String enc(String str){
		  return a.encode(str);
	  }

	  static String[] keys=new String[]{"strBorrowerLoginId","strIdentify","strPhoneNO"};
	  public static Map<String,Object> decMap(Map<String,Object> map){
		  for(String key : keys){
			  if(map!=null){
				  String str = MapUtils.getString(map, key);
				  if(str!=null&&!str.trim().isEmpty()){
					  map.put(key, dec(str));
				  }
			  }
		  }
		  return map;
	  }
	  
	  
	  
	  public void init(String strKey, String strIV)
	  {
	    if (strKey.length() < 16)
	    {
	      throw new RuntimeException("Key length must be at least 16");
	    }
	    if (strIV.length() < 16)
	    {
	      throw new RuntimeException("IV length must be at least 16");
	    }

	    try
	    {
	      strKey = strKey.substring(0, 16);
	      strIV = strIV.substring(0, 16);

	      byte[] keyBytes = strKey.getBytes("utf-8");
	      Security.addProvider(new BouncyCastleProvider());
	      SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	      byte[] bysIV = strKey.getBytes("utf-8");
	      IvParameterSpec iv = new IvParameterSpec(bysIV);

	      this.cipherEncode = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
	      this.cipherEncode.init(1, key, iv);
	      this.cipherDecode = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
	      this.cipherDecode.init(2, key, iv);
	    }
	    catch (Exception e)
	    {
	      throw new RuntimeException(e);
	    }
	  }

	  public byte[] encode(byte[] bysContent)
	  {
	    if (this.cipherEncode == null)
	    {
	      return bysContent;
	    }

	    try
	    {
	      byte[] bysResult = (byte[])null;
	      synchronized (this.cipherEncode)
	      {
	        bysResult = this.cipherEncode.doFinal(bysContent);
	      }

	      return bysResult;
	    }
	    catch (Exception e)
	    {
	      throw new RuntimeException(e);
	    }
	  }

	  public String encode(String strText)
	  {
	    if (this.cipherEncode == null)
	    {
	      return strText;
	    }

	    try
	    {
	      byte[] byteContent = strText.getBytes("utf-8");
	      byte[] bysResult = (byte[])null;
	      synchronized (this.cipherEncode)
	      {
	        bysResult = this.cipherEncode.doFinal(byteContent);
	      }
	      return Base64.encodeBase64String(bysResult);
	    }
	    catch (Exception e)
	    {
	      throw new RuntimeException(e);
	    }
	  }

	  public byte[] decode(byte[] bysEncoded)
	  {
	    if (this.cipherEncode == null)
	    {
	      return bysEncoded;
	    }

	    try
	    {
	      byte[] bysResult = (byte[])null;
	      synchronized (this.cipherDecode)
	      {
	        bysResult = this.cipherDecode.doFinal(bysEncoded);
	      }

	      return bysResult;
	    }
	    catch (Exception e)
	    {
	      throw new RuntimeException(e);
	    }
	  }

	  public String decode(String strEncoded)
	  {
	    if (this.cipherEncode == null)
	    {
	      return strEncoded;
	    }
	    byte[] bysEncoded = Base64.decodeBase64(strEncoded);
	    try
	    {
	      byte[] bysResult = (byte[])null;
	      synchronized (this.cipherDecode)
	      {
	        bysResult = this.cipherDecode.doFinal(bysEncoded);
	      }

	      return new String(bysResult, "utf-8");
	    }
	    catch (Exception e)
	    {
	      throw new RuntimeException(e);
	    }
	  }
	  
	public static void main(String[] args) {
		String str ="gJCtXCEihd/wrvSjbXhfRuH/VruBXrqiwL5XKs7eBA8=5";
		str = "gJCtXCEihd/wrvSjbXhfRuH/VruBXrqiwL5XKs7eBA8=";
		DFAES a=new DFAES();
		a.init("DECSECURITYKEYABCDEFG", "EiJPWIgQQDgoJXlRy91SZncpdZgwQEHi");
		String rr = a.decode(str);
		System.out.println(a.decode("53GwczWguyJo2R6/wI9STrXcwscWw+OzrWBncIZ1Xro="));
		
		
	}
	
	public static void mainaa(String[] args) {
		Map<String,String> map =new HashMap();
		String[] ar = rrmsg.split("@@@");
		for(String ss : ar) {
			String[] aar = ss.split("####");
			map.put(aar[0].trim(), aar[1].trim());
		}
	}
	
	public static String rrmsg ="";
}
