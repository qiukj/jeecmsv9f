package com.jeecms.cms.api.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.upload.FileUpload;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestContent {

	public static void main(String[] args) {
		//testSaveContent();
		//testUpdateContent();
		//testSaveContentDoc();
		//testUpdateContentDoc();
		//testDelContent();
		//testRecycleContent();
		//testRejectContent();
		//testCheckContent();
		//testUpContent();
		//testDownContent();
		//testBuyContent();
	}
	
	private static String testSaveContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/save.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("siteId="+1);
		paramBuff.append("&channelId="+94);
		paramBuff.append("&modelId="+1);
		paramBuff.append("&title="+"APITEST");
		paramBuff.append("&author="+"author");
		paramBuff.append("&desc="+"desae");
		paramBuff.append("&txt=adfsdf");
		paramBuff.append("&tagStr="+"tagStr");
		paramBuff.append("&mediaType=FLV");
		paramBuff.append("&charge="+0);
		paramBuff.append("&chargeAmount="+0d);
		paramBuff.append("&isDoc="+false);
		String mediaPath="";

		String mediaLocalPath="d:\\test\\1.mp4";
		String picLocalPath="d:\\test\\1.jpg";
		String attachLocalPath="d:\\test\\1.zip";
		mediaPath=testUpload(mediaLocalPath,"vedio");
		
		String contentImg=testUpload(picLocalPath,"image");
		String titleImg=testUpload(picLocalPath,"image");
		String typeImg=testUpload(picLocalPath,"image");
		
		String[] attachmentPaths=new String[1];
		attachmentPaths[0]=testUpload(attachLocalPath, "attach");
		String attachmentPath="";
		for(int i=0;i<attachmentPaths.length;i++){
			attachmentPath+=attachmentPaths[i]+",";
		}
		String[] attachmentNames=new String[1];
		attachmentNames[0]="1.zip";
		String attachmentName="";
		for(int i=0;i<attachmentNames.length;i++){
			attachmentName+=attachmentNames[i]+",";
		}
		String[] attachmentFilenames=new String[1];
		attachmentFilenames[0]="1.zip";
		String attachmentFilename="";
		for(int i=0;i<attachmentFilenames.length;i++){
			attachmentFilename+=attachmentFilenames[i]+",";
		}
		String[] picPaths=new String[1];
		picPaths[0]=testUpload(picLocalPath, "image");
		String picPath="";
		for(int i=0;i<picPaths.length;i++){
			picPath+=picPaths[i]+",";
		}
		
		String[] picDescs=new String[1];
		picDescs[0]="1.jpg";
		
		String picDesc="";
		for(int i=0;i<picDescs.length;i++){
			picDesc+=picDescs[i]+",";
		}
		paramBuff.append("&mediaPath="+mediaPath);
		paramBuff.append("&attachmentPaths="+attachmentPath);
		paramBuff.append("&attachmentNames="+attachmentName);
		paramBuff.append("&attachmentFilenames="+attachmentFilename);
		paramBuff.append("&picPaths="+picPath);
		paramBuff.append("&picDescs="+picDesc);
		
		paramBuff.append("&contentStatus="+1);
		paramBuff.append("&typeId="+2);
		paramBuff.append("&contentImg="+contentImg);
		paramBuff.append("&titleImg="+titleImg);
		paramBuff.append("&typeImg="+typeImg);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/update.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+214);
		paramBuff.append("&channelId="+94);
		paramBuff.append("&modelId="+1);
		paramBuff.append("&title="+"APITEST1");
		paramBuff.append("&author="+"author1");
		paramBuff.append("&desc="+"desae1");
		paramBuff.append("&txt=adfsdf1");
		paramBuff.append("&tagStr="+"tagStr1");
		paramBuff.append("&mediaType=FLV");
		paramBuff.append("&charge="+0);
		paramBuff.append("&chargeAmount="+1d);
		paramBuff.append("&isDoc="+false);
		String mediaPath="";

		String mediaLocalPath="d:\\test\\1.mp4";
		String picLocalPath="d:\\test\\1.jpg";
		String attachLocalPath="d:\\test\\1.zip";
		mediaPath=testUpload(mediaLocalPath,"vedio");
		
		String contentImg=testUpload(picLocalPath,"image");
		String titleImg=testUpload(picLocalPath,"image");
		String typeImg=testUpload(picLocalPath,"image");
		
		String[] attachmentPaths=new String[1];
		attachmentPaths[0]=testUpload(attachLocalPath, "attach");
		String attachmentPath="";
		for(int i=0;i<attachmentPaths.length;i++){
			attachmentPath+=attachmentPaths[i]+",";
		}
		String[] attachmentNames=new String[1];
		attachmentNames[0]="1.zip";
		String attachmentName="";
		for(int i=0;i<attachmentNames.length;i++){
			attachmentName+=attachmentNames[i]+",";
		}
		String[] attachmentFilenames=new String[1];
		attachmentFilenames[0]="1.zip";
		String attachmentFilename="";
		for(int i=0;i<attachmentFilenames.length;i++){
			attachmentFilename+=attachmentFilenames[i]+",";
		}
		String[] picPaths=new String[1];
		picPaths[0]=testUpload(picLocalPath, "image");
		String picPath="";
		for(int i=0;i<picPaths.length;i++){
			picPath+=picPaths[i]+",";
		}
		
		String[] picDescs=new String[1];
		picDescs[0]="1.jpg";
		
		String picDesc="";
		for(int i=0;i<picDescs.length;i++){
			picDesc+=picDescs[i]+",";
		}
		paramBuff.append("&mediaPath="+mediaPath);
		paramBuff.append("&attachmentPaths="+attachmentPath);
		paramBuff.append("&attachmentNames="+attachmentName);
		paramBuff.append("&attachmentFilenames="+attachmentFilename);
		paramBuff.append("&picPaths="+picPath);
		paramBuff.append("&picDescs="+picDesc);
		paramBuff.append("&contentStatus="+1);
		paramBuff.append("&typeId="+2);
		paramBuff.append("&contentImg="+contentImg);
		paramBuff.append("&titleImg="+titleImg);
		paramBuff.append("&typeImg="+typeImg);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testDelContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/del.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"209,210");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testRecycleContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/recycle.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"209,210");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testCheckContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/check.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"209,210,");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testRejectContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/reject.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"209,210");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/up.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+"130");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testDownContent(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/down.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+"130");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testBuyContent(){
		//购买API地址
		//String url="http://192.168.0.173:8080/jeecmsv8/content/buy.jspx";
		//打赏API地址
		String url="http://192.168.0.173:8080/jeecmsv8/content/reward.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+"130");
		//微信订单
		paramBuff.append("&outOrderNum="+"4007812001201611180077904421");
		paramBuff.append("&orderType="+1);
		//其他商户下微信订单
		//paramBuff.append("&outOrderNum="+"4007122001201611200264619374");
		//paramBuff.append("&orderType="+1);
		//支付宝
		//paramBuff.append("&outOrderNum="+"2016102021001004030258989684");
		//paramBuff.append("&orderType="+2);
		//错误的流水号
		//paramBuff.append("&outOrderNum="+"2016112921001001960284133036");
		//paramBuff.append("&orderType="+2);
		//未成功支付订单号
		//paramBuff.append("&outOrderNum="+"2016120721001004960221434294");
		//paramBuff.append("&orderType="+2);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testSaveContentDoc(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/save.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("siteId="+1);
		paramBuff.append("&channelId="+81);
		paramBuff.append("&modelId="+9);
		paramBuff.append("&title="+"doctest");
		paramBuff.append("&author="+"doctest");
		paramBuff.append("&desc="+"desae");
		paramBuff.append("&txt=doctest");
		paramBuff.append("&tagStr="+"tagStr");
		paramBuff.append("&charge="+0);
		paramBuff.append("&chargeAmount="+0d);
		paramBuff.append("&isDoc="+true);

		String docLocalPath="d:\\test\\1.doc";
		String picLocalPath="d:\\test\\1.jpg";
		
		String typeImg=testUpload(picLocalPath,"image");
		
		String docPath=testUploadDoc(docLocalPath);
		
		paramBuff.append("&docPath="+docPath);
		paramBuff.append("&downNeed="+1);
		paramBuff.append("&isOpen="+true);
		paramBuff.append("&docSuffix="+"doc");
		paramBuff.append("&contentStatus="+1);
		paramBuff.append("&typeId="+2);
		paramBuff.append("&typeImg="+typeImg);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateContentDoc(){
		String url="http://192.168.0.173:8080/jeecmsv8/content/update.jspx";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+213);
		paramBuff.append("&channelId="+81);
		paramBuff.append("&modelId="+9);
		paramBuff.append("&title="+"doctest1");
		paramBuff.append("&author="+"doctest1");
		paramBuff.append("&desc="+"desae1");
		paramBuff.append("&txt=doctest1");
		paramBuff.append("&tagStr="+"tagStr1");
		paramBuff.append("&charge="+0);
		paramBuff.append("&chargeAmount="+0d);
		paramBuff.append("&isDoc="+true);

		String docLocalPath="d:\\test\\1.doc";
		String picLocalPath="d:\\test\\1.jpg";
		
		String typeImg=testUpload(picLocalPath,"image");
		
		String docPath=testUploadDoc(docLocalPath);
		
		paramBuff.append("&docPath="+docPath);
		paramBuff.append("&downNeed="+10);
		paramBuff.append("&isOpen="+false);
		paramBuff.append("&docSuffix="+"doc");
		paramBuff.append("&contentStatus="+2);
		paramBuff.append("&typeId="+2);
		paramBuff.append("&typeImg="+typeImg);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	
	public static String testUpload(String local,String type){
		String url="http://192.168.0.173:8080/jeecmsv8/upload/o_upload.jspx?";
		FileUpload fileUpload = new FileUpload();
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		paramBuff.append("siteId="+1);
		paramBuff.append("&mark="+false);
		paramBuff.append("&type="+type);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		String result= fileUpload.uploadFileWithHttpMime(1, type, false,
				url, local, appId, nonce_str, sign);
		JSONObject json;
		String uploadPath="";
		try {
			json = new JSONObject(result);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
				uploadPath=(String) bodyJson.get("uploadPath");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uploadPath;
	}
	
	private static String testUploadDoc(String local){
		String url="http://192.168.0.173:8080/jeecmsv8/upload/o_upload_doc.jspx?";
		FileUpload fileUpload = new FileUpload();
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		paramBuff.append("siteId="+1);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		String result= fileUpload.uploadFileWithHttpMime(1, null, null,
				url, local, appId, nonce_str, sign);
		JSONObject json;
		String uploadPath="";
		try {
			json = new JSONObject(result);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
				uploadPath=(String) bodyJson.get("uploadPath");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uploadPath;
	}
	
	private static String appId="111111";
	private static String appKey="Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi";
	private static String sessionKey="B5F7E21EAB2C6FE119DD9AC5E38187A5";
	private static String aesKey="S9u978Q31NGPGc5H";
	private static String ivKey="X83yESM9iShLxfwS";
}
