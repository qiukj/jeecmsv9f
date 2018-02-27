package com.jeecms.cms.service;

import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jeecms.cms.Constants;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;


@Component
public class ContentPushListener extends ContentListenerAbstract {
	private static final Logger log = LoggerFactory.getLogger(ContentPushListener.class);
	//百度主动推送链接提交地址key
	public static final String LINK_KEY="baidu.linksubmit.address";
	
	//百度主动推送链接提交Host
	public static final String LINK_HOST="baidu.linksubmit.host";
	
	//百度主动推送链接准入密钥
	public static final String BAIDU_TOKEN="bdToken";
		
	@Override
	public void afterChange(Content content, Map<String, Object> map) {
		// TODO Auto-generated method stub
		super.afterChange(content, map);
	}

	@Override
	public void afterDelete(Content content) {
		// TODO Auto-generated method stub
		super.afterDelete(content);
	}

	@Override
	public void afterSave(Content content) {
		// TODO Auto-generated method stub
		String domain= content.getSite().getDomain();
		if (content.getSite().getConfig().getAttr().containsKey("bdToken")&&content.getSite().getConfig().getAttr().containsKey("isBdSubmit")) {			
			String bdToken= content.getSite().getConfig().getAttr().get("bdToken");
			String isBdSubmit=content.getSite().getConfig().getAttr().get("isBdSubmit");
			if (isBdSubmit.equals("true")) {			
				pushPost(content.getUrl(),domain,bdToken);
			}
		}
		
	}


	@Override
	public Map<String, Object> preChange(Content content) {
		// TODO Auto-generated method stub
		return super.preChange(content);
	}

	@Override
	public void preDelete(Content content) {
		// TODO Auto-generated method stub
		super.preDelete(content);
	}

	@Override
	public void preSave(Content content) {
		// TODO Auto-generated method stub
		super.preSave(content);
	}

	/** 
     * 百度链接实时推送 
     * @param PostUrl 
     * @param Parameters 
     * @return 
     */  
	public String pushPost(String parameters,String domain, String bdToken){
		   String linkSubmitUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),LINK_KEY);	
		   String host=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),LINK_HOST);
		   linkSubmitUrl+="?site="+domain+"&token="+bdToken;	
		   String result="";	
		   HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	       //HttpClient  
	       CloseableHttpClient client = httpClientBuilder.build();  
	       client = (CloseableHttpClient) wrapClient(client);	       
	       Map<String, String> msg=new HashMap<>();
	       HttpPost post = new HttpPost(linkSubmitUrl);	
	     //发送请求参数  
	       try
	       {
	           StringEntity s = new StringEntity(parameters,"utf-8");	          
	           s.setContentType("application/json");
	           post.setEntity(s);  
	           post.setHeader("Host", host);
	           post.setHeader("User-Agent", "curl/7.12.1");
	           post.setHeader("Content-Type", "text/plain");      
	           HttpResponse res = client.execute(post);
               HttpEntity entity = res.getEntity();
               String str=EntityUtils.toString(entity, "utf-8");
               result=str;
               log.info("baidu link submit result -> "+result);
	       }
	       catch (Exception e)
	       {
	    	   result=null;
	    	   e.printStackTrace();
	       }
	       
	       return result;
	}
	
	private static  HttpClient wrapClient(HttpClient base) {
	    try {
	        SSLContext ctx = SSLContext.getInstance("TLSv1");
	        X509TrustManager tm = new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] xcs,
	                    String string) throws CertificateException {
	            }
	
	            public void checkServerTrusted(X509Certificate[] xcs,
	                    String string) throws CertificateException {
	            }
	
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };
	        ctx.init(null, new TrustManager[] { tm }, null);
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	        return httpclient;
	       
	    } catch (Exception ex) {
	        return null;
	    }
	}
	@Autowired
	private RealPathResolver realPathResolver;

	
}
