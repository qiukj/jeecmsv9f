package com.jeecms.cms.api.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestTopic {

	public static void main(String[] args) {
		//testSaveTopic();
		testUpdateTopic();
	}
	
	private static String testSaveTopic(){
		String url="http://192.168.0.173:8080/jeecmsv8/topic/save.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("channelId="+75);
		paramBuff.append("&name="+"topictest");
		paramBuff.append("&shortName="+"shortName");
		paramBuff.append("&keywords="+"topictestkeywords");
		paramBuff.append("&desc=adfsdf");
		paramBuff.append("&priority="+12);
		paramBuff.append("&recommend="+true);
		String titleImg=TestContent.testUpload("d:\\test\\2.jpg", "image");
		String contentImg=TestContent.testUpload("d:\\test\\2.jpg", "image");
		paramBuff.append("&titleImg="+titleImg);
		paramBuff.append("&contentImg="+contentImg);
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
		System.out.println(url);
		System.out.println(param);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateTopic(){
		String url="http://192.168.0.173:8080/jeecmsv8/topic/update.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+5);
		paramBuff.append("&channelId="+94);
		paramBuff.append("&name="+"topictest1");
		paramBuff.append("&shortName="+"shortName1");
		paramBuff.append("&keywords="+"topictestkeywords1");
		paramBuff.append("&desc=adfsdf1");
		paramBuff.append("&priority="+10);
		paramBuff.append("&recommend="+false);
		String titleImg=TestContent.testUpload("d:\\test\\2.jpg", "image");
		String contentImg=TestContent.testUpload("d:\\test\\2.jpg", "image");
		paramBuff.append("&titleImg="+titleImg);
		paramBuff.append("&contentImg="+contentImg);
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
		System.out.println(url);
		System.out.println(param);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String appId="111111";
	private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
}
