package com.jeecms.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



public class HttpClientUtil {
	
	private static class SingletonHolder{
        private final static  HttpClientUtil INSTANCE=new HttpClientUtil();
    }   
	
    private HttpClientUtil(){}
    
    public static HttpClientUtil getInstance(){
        return SingletonHolder.INSTANCE;
    }
	
	public  String get(String url){
		CharsetHandler handler = new CharsetHandler("UTF-8");
		 CloseableHttpClient client = null;
		try {
			HttpGet httpget = new HttpGet(new URI(url));
	        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	        client= httpClientBuilder.build();  
		    client = (CloseableHttpClient) wrapClient(client);
			return client.execute(httpget, handler);
		} catch (Exception e) {
			//e.printStackTrace();
			return "";
		}finally {
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
       }
	}
    
    public static String postParams(String url, Map<String, String>params)
	   {
		
	      //创建HttpClientBuilder  
		  HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	      //HttpClient  
	      CloseableHttpClient client = httpClientBuilder.build();  
	       HttpPost post = new HttpPost(url);
	       CloseableHttpResponse res = null;
	       try
	       {

	    	  List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
	    	  Set<String> keySet=params.keySet();
	    	  for(String key:keySet){
	    		  nvps.add(new BasicNameValuePair(key, params.get(key)));  
	    	  } 
	    	  post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	           res = client.execute(post);
	           HttpEntity entity = res.getEntity();
	           return EntityUtils.toString(entity, "utf-8");
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       } finally {
				try {
					res.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	       }
		return "";
	 }
    
	public static String post(String url, String params,String contentType)
	   {
		
	      //创建HttpClientBuilder  
		  HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	      //HttpClient  
	      CloseableHttpClient client = httpClientBuilder.build();  
	      client = (CloseableHttpClient) wrapClient(client);
	      
	      
	       HttpPost post = new HttpPost(url);
	       CloseableHttpResponse res = null;
	       try
	       {
	           StringEntity s = new StringEntity(params,"UTF-8");
	           if(StringUtils.isBlank(contentType)){
	        	   s.setContentType("application/json");
	           }
	           s.setContentType(contentType);
	           s.setContentEncoding("utf-8");
	           post.setEntity(s);
	           res = client.execute(post);
	           HttpEntity entity = res.getEntity();
	           return EntityUtils.toString(entity, "utf-8");
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       } finally {
				try {
					res.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	       }
		return "";
	 }
	
	public static String post(String urlStr,String xmlInfo) {
		String line1 = "";  
		try {  
			URL url = new URL(urlStr);  
			URLConnection con = url.openConnection();  
			con.setDoOutput(true);  
			//con.setRequestProperty("Pragma:", "no-cache");  
			con.setRequestProperty("Cache-Control", "no-cache");  
			con.setRequestProperty("Content-Type", "text/xml"); 
		
			OutputStreamWriter out = new OutputStreamWriter(con  
			.getOutputStream());           
			out.write(new String(xmlInfo.getBytes("utf-8")));  
			out.flush();  
			out.close();  
			BufferedReader br = new BufferedReader(new InputStreamReader(con  
			.getInputStream()));  
			String line = "";  
			for (line = br.readLine(); line != null; line = br.readLine()) {  
				line1+=line;  
			} 
			return new String(line1.getBytes(),"utf-8");
			} catch (MalformedURLException e) {  
				e.printStackTrace();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}
			return null;  
		}
	
	private class CharsetHandler implements ResponseHandler<String> {
		private String charset;

		public CharsetHandler(String charset) {
			this.charset = charset;
		}

		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (!StringUtils.isBlank(charset)) {
					return EntityUtils.toString(entity, charset);
				} else {
					return EntityUtils.toString(entity);
				}
			} else {
				return null;
			}
		}
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

}
