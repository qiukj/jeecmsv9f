package com.jeecms.common.ipseek;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tom
 * 提供淘宝api和百度api查询ip所在省份
 * 淘宝api 支持10qps，百度api 每个key支持100万次/天 
 * 使用百度api需要申请密钥并调整api.properties中的api.baidu.ak参数
 * 为使用方便首选淘宝api
 */
public class IpSeekUtils {
	/**
	 * 查询ip所在省份，返回空值ip在国外或者发生错误
	 * @param ip
	 * @return
	 */
	@Deprecated
	public static String getIpProvinceByTaobao(String ip){
		String api=PropertiesLoader.getTaobaoApi();
		String result=getIpAddress(api,ip,null, null);
		JSONObject json;
		String province="";
		if(StringUtils.isNotBlank(result)&&result.startsWith("{")){
			try {
				json = new JSONObject(result);
				if((Integer)json.get("code")==1){
					return "";
				}
				JSONObject data= (JSONObject) json.get("data");
				province=(String) data.get("region");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return province;
	}
	
	public static String getIpProvinceByBaidu(String ip){
		String api=PropertiesLoader.getBaiduApi();
		String ak=PropertiesLoader.getBaiduApiAk();
		String result=getIpAddress(api,ip,ak, null);
		JSONObject json;
		String province="";
		if(StringUtils.isNotBlank(result)){
			try {
				json = new JSONObject(result);
				if((Integer)json.get("status")!=0){
					return "";
				}
				JSONObject content= (JSONObject) json.get("content");
				JSONObject addressDetail= (JSONObject)content.get("address_detail");
				province=(String) addressDetail.get("province");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return province;
	}
	
	public static String getIpProvinceBySina(String ip){
		String api=PropertiesLoader.getSinaApi();
		String result=getIpAddress(api,ip,null, "&format=json");
		JSONObject json;
		String province="";
		if(StringUtils.isNotBlank(result)){
			try {
				json = new JSONObject(result);
				Object proObj=json.get("province");
				if(proObj!=null){
					province= (String)proObj;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return province;
	}
	
	private  static String getIpAddress(String api,String ip,String ak, String moreParams){
		String apiAddress=api+"?ip="+ip;
		if(StringUtils.isNotBlank(ak)){
			apiAddress+="&ak="+ak;
		}
		if(StringUtils.isNotBlank(moreParams)){
			apiAddress+=moreParams;
		}
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	     //HttpClient  
	    CloseableHttpClient client = httpClientBuilder.build();  
        //创建get请求方式对象  
        HttpGet get = new HttpGet(apiAddress);  
		String result="";
		try {
            CloseableHttpResponse response = client.execute(get);  
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();  
                if (entity != null) {   
                    //实体内容以字符串类型输出  
                    String httpStr = EntityUtils.toString(entity, "UTF-8");
                    result=httpStr;
                }  
                //确保实体内容被完全关闭  
                EntityUtils.consume(entity);                      
            }  
			//result=new String(data);
		} catch (IOException e) {
			//e.printStackTrace();
		}finally {  
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }    
		return result;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//美国ip 需要获取country region获取为空
	//	String result=getIpAddressByTaobao("216.35.169.232");
		//北京ip
	//	String result=getIpAddress("218.30.64.194");
	//	String result=getIpAddress("59.53.63.18");
		String ip="218.30.64.194";
		ip="218.26.84.5";
	}

}
