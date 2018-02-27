package com.jeecms.common.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
 
/**
 * Http工具类
 * 
 * @author TOM
 *
 */
public class HttpRequestUtil {
 
    public static final String REQUEST_TYPE_GET = "get";
    public static final String REQUEST_TYPE_POST = "post";
     
    /**
     * 模拟发出Http请求
     * 
     * @param uri 请求资源,如：http://www.baidu.com/,注意严谨的格式
     * @param params 请求参数
     * @param type 请求方式,目前只支持get/post
     * @param encoding 网页编码
     * 
     * @return HttpResponseBody
     * 
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static String request(String uri, Map<String,String> params, String type, String encoding) throws ClientProtocolException, IOException{
        String result = "";
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	      //HttpClient  
	    CloseableHttpClient httpClient = httpClientBuilder.build();  
        HttpResponse httpResponse = null;
         
        // GET方式请求
        if(HttpRequestUtil.REQUEST_TYPE_GET.equals(type)){
            // 加入请求参数
            if(params != null ){
                if(uri.indexOf("?") != -1){
                    uri += "&";
                }else{
                    uri += "?";
                }
                for(String key : params.keySet()){
                    uri += key +"="+params.get(key) +"&";
                }
            }
            HttpGet httpGet = new HttpGet(uri);
            httpResponse = httpClient.execute(httpGet);
             
        // POST方式请求
        }else if(HttpRequestUtil.REQUEST_TYPE_POST.equals(type)){
            HttpPost httpPost = new HttpPost(uri);
            // 加入请求参数
            if(params != null ){
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for(String key : params.keySet()){
                    if(key != null){
                        paramList.add(new BasicNameValuePair(key, params.get(key)));
                    }
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"UTF-8");
                httpPost.setEntity(entity);
            }
            httpResponse = httpClient.execute(httpPost);
        }
         
        // 获取返回内容
        HttpEntity entity = httpResponse.getEntity();
        if(entity!=null){
            InputStream is = entity.getContent();
            int l ;
            byte[] buff = new byte[9192];
            while( (l = is.read(buff)) != -1){
                result += new String(buff, 0, l, encoding);
            }
        }
        return result;
    }
}
