package com.jeecms.cms.api.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestSearch {

	public static void main(String[] args) {
		testSearchContent();
	}
	
	private static String testSearchContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/search.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("siteId="+1);
		paramBuff.append("&channelId="+79);
		paramBuff.append("&q="+"双休");
		paramBuff.append("&queryBeginDate="+"2016-09-26");
		paramBuff.append("&queryEndDate="+"2016-10-10");
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
	
	private static String appId="111111";
	private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
}
