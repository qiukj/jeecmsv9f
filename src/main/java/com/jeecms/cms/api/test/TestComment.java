package com.jeecms.cms.api.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestComment {

	public static void main(String[] args) {
		testCommentSave();
		//testCommentMyList();
	}
	
	private static String testCommentMyList(){
		String url="http://192.168.0.173:8080/jeecmsv8/comment/mylist.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("first="+0);
		paramBuff.append("&count="+5);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testCommentSave(){
		String url="http://192.168.0.173:8080/jeecmsv8/comment/save.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("contentId="+27);
		paramBuff.append("&text="+"测试评论");
		paramBuff.append("&parentId="+23);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String appId="111111";
	private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
	private static String sessionKey="B5F7E21EAB2C6FE119DD9AC5E38187A5";
	private static String aesKey="S9u978Q31NGPGc5H";
	private static String ivKey="X83yESM9iShLxfwS";
}
