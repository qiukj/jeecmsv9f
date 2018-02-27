package com.jeecms.cms.api.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestChannel {

	public static void main(String[] args) {
		//testSaveChannel();
		//testUpdateChannel();
		//createSign();
		testCopyChannel();
	}
	
	private static String createSign(){
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		param.put("contentId", "2133103");
		param.put("txt", "123123123123123123123");
		param.put("appId", "4505818634615678");
		param.put("sessionKey", "70ff7c1ed6523a47b5225cd375bc05d506aaa348eb90cadbc91e813f8120fe1d0766e8ada1be8285463ce3a6757d099c");
		param.put("nonce_str", "1483085875800nJ3489");
		//param.put("https", "0");
		String sign=PayUtil.createSign(param, appKey);
		System.out.println(sign);
		return sign;
	}
	
	private static String testSaveChannel(){
		String url="http://192.168.0.173:8080/jeecmsv8/channel/save.jspx?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("siteId="+1);
		paramBuff.append("&parentId="+75);
		paramBuff.append("&name="+"APITEST");
		paramBuff.append("&path="+"APITEST");
		paramBuff.append("&title="+"APITESTtitle");
		paramBuff.append("&keywords="+"APITESTkeywords");
		paramBuff.append("&desc=adfsdf");
		paramBuff.append("&priority="+12);
		paramBuff.append("&display="+true);
		paramBuff.append("&modelId="+1);
		paramBuff.append("&titleImg="+"titleImg");
		paramBuff.append("&contentImg="+"contentImg");
		paramBuff.append("&finalStep="+1);
		paramBuff.append("&afterCheck="+1);
		paramBuff.append("&tplChannel="+"/default/channel/news.html");
		paramBuff.append("&tplMobileChannel="+"/mobile/channel/news.html");
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
		return res;
	}
	
	private static String testUpdateChannel(){
		String url="http://192.168.0.173:8080/jeecmsv8/channel/update.jspx?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("siteId="+1);
		paramBuff.append("&channelId="+95);
		paramBuff.append("&parentId="+75);
		paramBuff.append("&name="+"APITEST1");
		paramBuff.append("&path="+"APITEST1");
		paramBuff.append("&title="+"APITESTtitle1");
		paramBuff.append("&keywords="+"APITESTkeywords1");
		paramBuff.append("&desc=adfsdf1");
		paramBuff.append("&priority="+10);
		paramBuff.append("&display="+false);
		paramBuff.append("&modelId="+1);
		paramBuff.append("&titleImg="+"titleImg1");
		paramBuff.append("&contentImg="+"contentImg2");
		paramBuff.append("&finalStep="+1);
		paramBuff.append("&afterCheck="+1);
		paramBuff.append("&tplChannel="+"/default/channel/news.html");
		paramBuff.append("&tplMobileChannel="+"/mobile/channel/news.html");
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
		return res;
	}
	
	private static String testCopyChannel(){
		String url="http://192.168.0.173:8080/jeecmsv9/api/admin/channel/copy?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("channelIds=75,76,77,95,96");
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
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String appId="1580387213331704";
	private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
	private static String aesKey="S9u978Q31NGPGc5H";
	private static String ivKey="X83yESM9iShLxfwS";
	private static String sessionKey="32C85883D9BEFA87AE028DB3401B66CF";
	//private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
}
