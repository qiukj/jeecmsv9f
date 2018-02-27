package com.jeecms.common.upload;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author Tom
 */
public class FileUpload {
	/**
	* 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
	* 
	* @param url 请求地址 form表单url地址
	* @param filePath 文件在服务器保存路径
	* @return String 正确上传返回media_id
	* @throws IOException
	*/
	
	/** 微信上传文件接口  */
	public  String uploadFile(String url,String filePath,String type) throws Exception {
		 File file = new File(filePath);
		 String result = null;
	        if (!file.exists() || !file.isFile()) {
	            return "文件路径错误";
	        }
	        /**
	         * 第一部分
	         */
	        if(StringUtils.isNotBlank(type)){
	        	url = url+"&type="+type;
	        }
	        URL urlObj = new URL(url);
	        HttpURLConnection con = null;
	        
	        //解决HTTPS
	        trustAllHttpsCertificates();
	        HostnameVerifier hv = new HostnameVerifier() {
	            public boolean verify(String urlHostName, SSLSession session) {
	                return true;
	            }
	        };
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			con=(HttpURLConnection) urlObj.openConnection();
			
	        /**
	         * 设置关键值
	         */
	        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
	        con.setDoInput(true);
	        con.setDoOutput(true);
	        con.setUseCaches(false); // post方式不能使用缓存
	        // 设置请求头信息
	        con.setRequestProperty("Connection", "Keep-Alive");
	        con.setRequestProperty("Charset", "UTF-8");
	
	        // 设置边界
	        String BOUNDARY = "----------" + System.currentTimeMillis();
	        con.setRequestProperty("content-type", "multipart/form-data; boundary=" + BOUNDARY);
	        
	        //con.setRequestProperty("Content-Type", "multipart/mixed; boundary=" + BOUNDARY);
	        //con.setRequestProperty("content-type", "text/html");
	        // 请求正文信息
	
	        // 第一部分：
	        StringBuilder sb = new StringBuilder();
	        sb.append("--"); // ////////必须多两道线
	        sb.append(BOUNDARY);
	        sb.append("\r\n");
	        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
	                + file.getName() + "\"\r\n");
	        sb.append("Content-Type:application/octet-stream\r\n\r\n");
	        byte[] head = sb.toString().getBytes("utf-8");
	        // 获得输出流
	        OutputStream out = new DataOutputStream(con.getOutputStream());
	        out.write(head);
	        
	        // 文件正文部分
	        DataInputStream in = new DataInputStream(new FileInputStream(file));
	        int bytes = 0;
	        byte[] bufferOut = new byte[1024];
	        while ((bytes = in.read(bufferOut)) != -1) {
	            out.write(bufferOut, 0, bytes);
	        }
	        in.close();
	        // 结尾部分
	        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
	        out.write(foot);
	        out.flush();
	        out.close();
	        /**
	         * 读取服务器响应，必须读取,否则提交不成功
	         */
	       // con.getResponseCode();

	        /**
	         * 下面的方式读取也是可以的
	         */

	         try {

	         // 定义BufferedReader输入流来读取URL的响应
	        	 StringBuffer buffer = new StringBuffer();
		         BufferedReader reader = new BufferedReader(new InputStreamReader(
		         con.getInputStream(),"UTF-8"));
		         String line = null;
		         while ((line = reader.readLine()) != null) {
		            buffer.append(line);
		         }
		         if(result==null){
					result = buffer.toString();
				}
		         return buffer.toString();
	         } catch (Exception e) {
	        	 e.printStackTrace();
	         }
	         return result;
	}
	
	
	/** 
	 * @param fileName 图片路径 
	 */  
	public static String uploadFileWithHttpMime(
			Integer siteId,String type,Boolean mark,
			String url,String filePath,
			String appId,String nonce_str,String sign) {  
	    // 实例化post提交方式  
	    HttpPost post = new HttpPost(url);  
	    //创建HttpClientBuilder  
	    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
       //HttpClient  
        CloseableHttpClient httpClient = httpClientBuilder.build(); 
        
	    StringBuffer buffer  = new StringBuffer();  ;
	    // 添加json参数  
	    try {  
	        MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
	        //builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式  
	        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式  
	        File file = new File(filePath);  
	        builder.addBinaryBody("uploadFile", file);  
	        builder.addTextBody("siteId", siteId.toString());
	        if(mark!=null){
	        	builder.addTextBody("mark", mark.toString());
	        }
	        if(StringUtils.isNotBlank(type)){
	        	 builder.addTextBody("type", type);
	        }
	        builder.addTextBody("appId", appId);
	        builder.addTextBody("nonce_str", nonce_str);
	        builder.addTextBody("sign", sign);
	        
	        HttpEntity entity = builder.build();// 生成 HTTP POST 实体        
	        post.setEntity(entity);//设置请求参数  
	        HttpResponse response = httpClient.execute(post);// 发起请求 并返回请求的响应  
	        HttpEntity resEntity=response.getEntity();
	        InputStream is = resEntity.getContent();  
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
	       
	        String temp;  
	  
	        while ((temp = reader.readLine()) != null) {  
	            buffer.append(temp);  
	        }  
	  
	    } catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	    } catch (ClientProtocolException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } catch (IllegalStateException e) {  
	        e.printStackTrace();  
	    }
		return buffer.toString();  
	  
	}  
	
	
	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}
	
	static class miTM implements javax.net.ssl.TrustManager,
		javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}
		
		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}
		
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
		
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "d:/mv1.jpg";
		String token="Jdr_B5dQzbWlmmTAlMxbpOZiUfe100laWKeNjRgqfYAJ2GkgCdbQCQO4gAA6e0qd7uYM8fhhzx9ehQBCHlQvKQ";
		String result = null;
		FileUpload fileUpload = new FileUpload();
		result = fileUpload.uploadFile(token, filePath, "image");
	}
}
