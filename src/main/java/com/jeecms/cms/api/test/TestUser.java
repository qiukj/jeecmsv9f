package com.jeecms.cms.api.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestUser {

	public static void main(String[] args) {
		testLogin();
		//testGetUserStatus();
		//testSaveUser();
		//testUpdateUser();
		//testPasswdEdit();
		//testUserGet();
		//testLogout();
	}
	
	private static String testLogin(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/front/user/login?";
		StringBuffer paramBuff=new StringBuffer();
		String password="password";
		paramBuff.append("username="+"bianji");
		try {
			paramBuff.append("&aesPassword="+AES128Util.encrypt(password, aesKey,ivKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		JSONObject json;
		try {
			json = new JSONObject(res);
			String sessionKey=(String) json.get("body");
			try {
				String descryptKey=AES128Util.decrypt(sessionKey, aesKey,ivKey);
				System.out.println(descryptKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static String testLogout(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/member/user/logout?";
		StringBuffer paramBuff=new StringBuffer();
		String sessionKey="99F5ECE1B602D82FD6500D2C344E603C";
		paramBuff.append("username="+"admin");
		try {
			paramBuff.append("&sessionKey="+AES128Util.encrypt(sessionKey, aesKey,ivKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println(res);
		return res;
	}
	
	private static String testGetUserStatus(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/front/user/getStatus?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"admin");
		try {
			paramBuff.append("&sessionKey="+AES128Util.encrypt(sessionKey, aesKey,ivKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println(res);
		JSONObject json;
		try {
			json = new JSONObject(res);
			String message=(String) json.get("message");
			System.out.println(message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
	
	private static String testUserGet(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/member/user/get";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"admin");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
	//	url=url+paramBuff.toString();
		//String res=HttpClientUtil.getInstance().get(url);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		JSONObject json;
		try {
			json = new JSONObject(res);
			/*
			String sessionKey=(String) json.get("body");
			try {
				String descryptKey=AES128Util.decrypt(sessionKey, aesKey);
				System.out.println(descryptKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			System.out.println(json.get("body"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
	
	private static String testSaveUser(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/member/user/add?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"test1112");
		paramBuff.append("&email="+"test1@qq.com");
		paramBuff.append("&loginPassword="+"password");
		paramBuff.append("&realname="+"realname");
		paramBuff.append("&gender="+true);
		paramBuff.append("&birthdayStr="+"1982-05-09");
		paramBuff.append("&phone=0791-88888888");
		paramBuff.append("&mobile=13888888888");
		paramBuff.append("&qq=123456");
		paramBuff.append("&userImg=/user/1.png");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateUser(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/member/user/edit?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"test1112");
		paramBuff.append("&realname="+"realname1");
		paramBuff.append("&gender="+false);
		paramBuff.append("&birthdayStr="+"1983-06-10");
		paramBuff.append("&phone=0791-77777777");
		paramBuff.append("&mobile=13899999999");
		paramBuff.append("&qq=1234561");
		paramBuff.append("&userImg=/user/2.png");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testPasswdEdit(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/member/user/pwd?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"test1112");
		paramBuff.append("&origPwd="+"password");
		paramBuff.append("&newPwd=123456");
		paramBuff.append("&email="+"112@qq.com");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}

	private static String appId="1580387213331704";
	private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
	private static String sessionKey="B5F7E21EAB2C6FE119DD9AC5E38187A5";
	private static String aesKey="S9u978Q31NGPGc5H";
	private static String ivKey="X83yESM9iShLxfwS";
	
//	private static String appId="0735152688870283";
//	private static String appKey="1BGxXqljhc7HBwDT72Ez87Horqmj8wSy";
//	private static String sessionKey="395315729A3293E58061187399F0E5B7";
//	private static String aesKey="3FyLIyQ6MCY7gJUS";
//	private static String ivKey="toa2ce0K2HYtZTxb";

}
