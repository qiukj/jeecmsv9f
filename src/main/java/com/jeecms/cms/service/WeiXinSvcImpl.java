package com.jeecms.cms.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecms.cms.Constants;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.web.CmsThreadVariable;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileUpload;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.plug.weixin.entity.Weixin;
import com.jeecms.plug.weixin.entity.WeixinMenu;

/**
 * @author Tom
 */
@Service
public class WeiXinSvcImpl implements WeiXinSvc {
	private static final Logger log = LoggerFactory.getLogger(WeiXinSvcImpl.class);
	//微信token地址key
	public static final String TOKEN_KEY="weixin.address.token";
	//微信公众号关注用户地址key
	public static final String USERS_KEY="weixin.address.users";
	//微信发送消息地址key
	public static final String SEND_KEY="weixin.address.send";
	//微信上传地址key
	public static final String UPLOAD_KEY="weixin.address.upload";
	//微信创建菜单key
	public static final String MENU_KEY="weixin.address.menu";
	//微信上传临时图文素材
	public static final String UPLOAD_NEWS="weixin.address.uploadnews";
	//群发消息
	public static final String SEND_ALL_MESSAGE="weixin.address.sendAllMessage";
	//微信图文消息上传图片地址
	public static final String UPLOAD_IMG_URL="weixin.address.uploadimg";
	//微信新增永久素材接口地址
	public static final String ADD_NEWS="weixin.address.addNews";
	//微信永久素材图片上传接口地址
	public static final String UPLOAD_MATERIAL_IMG_URL="weixin.address.addMaterial";
	//每次抽取关注号数量
	public static final Integer USERS_QUERY_MAX=10000;
	
	public Map<String, String> getToken() {
		String tokenGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),TOKEN_KEY);
		String appid="";
		String secret="";
		CmsSite site=CmsThreadVariable.getSite();
		Map<String, String> msg=new HashMap<>();		
		if(site!=null){
			appid=site.getAttr().get(com.jeecms.core.Constants.WEIXIN_APPKEY);
			secret=site.getAttr().get(com.jeecms.core.Constants.WEIXIN_APPSECRET);
		}
		JSONObject tokenJson=new JSONObject();
		if(StringUtils.isNotBlank(appid)&&StringUtils.isNotBlank(secret)){
			tokenGetUrl+="&appid="+appid+"&secret="+secret;
			tokenJson=getUrlResponse(tokenGetUrl);	
		    if (tokenJson.has("errmsg")) {
				msg.put("errmsg",(String)tokenJson.get("errmsg"));
			}else{
				msg.put("errmsg","");
			}	    
			try {	
				msg.put("token", (String) tokenJson.get("access_token"));
				return msg;
			} catch (JSONException e) {
				msg.put("token", null);
				return msg;
			}
		}else{
			msg.put("token", null);
			return msg;
		}
	}

	public Set<String> getUsers(String access_token) {
		String usersGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),USERS_KEY);
		usersGetUrl+="?access_token="+access_token;
		JSONObject data=getUrlResponse(usersGetUrl);
		Set<String>openIds=new HashSet<String>();
		Integer total=0,count=0;
		try {
			total=(Integer) data.get("total");
			count=(Integer) data.get("count");
			//总关注用户数超过默认一万
			if(count<total){
				openIds.addAll(getUsers(openIds,usersGetUrl, access_token, (String)data.get("next_openid")));
			}else{
				//有关注者 json才有data参数
				if(count>0){
					JSONObject openIdData=(JSONObject) data.get("data");
					JSONArray openIdArray= (JSONArray) openIdData.get("openid");
					for(int i=0;i<openIdArray.length();i++){
						openIds.add((String) openIdArray.get(i));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openIds;
	}

	
	public String  uploadFile(String access_token,String filePath,String type){
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),UPLOAD_KEY);
		String url = sendGetUrl+"?access_token=" + access_token;
		String result = null;
		String mediaId="";
		FileUpload fileUpload = new FileUpload();
		try {
			result = fileUpload.uploadFile(url,filePath, type);
			log.info("upload content result->"+result);
			if(result.startsWith("{")&&result.contains("media_id")){
				JSONObject json=new JSONObject(result);
				mediaId=json.getString("media_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return mediaId;
	}
	

	public void sendText(String access_token,String content) {
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),SEND_KEY);
        String url = sendGetUrl+"?access_token=" + access_token;
		Set<String> openIds=getUsers(access_token);
		content=filterCharacters(content);
	    //发送给所有关注者消息
		for(String openId:openIds){
		    String strJson = "{\"touser\" :\""+openId+"\",";
	        strJson += "\"msgtype\":\"text\",";
	        strJson += "\"text\":{";
	        strJson += "\"content\":\""+content+"\"";
	        strJson += "}}";
		    post(url, strJson,"application/json");
		}
	}
	
	public void sendContent(String access_token,String title, String description, String url,
			String picurl) {
		String sendUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),SEND_KEY);
        sendUrl = sendUrl+"?access_token=" + access_token;
		Set<String> openIds=getUsers(access_token);
		if(description==null){
			description="";
		}
		title=filterCharacters(title);
		description=filterCharacters(description);
	    //发送给所有关注者消息
		for(String openId:openIds){
		    String strJson = "{\"touser\" :\""+openId+"\",";
	        strJson += "\"msgtype\":\"news\",";
	        strJson += "\"news\":{";
	        strJson += "\"articles\": [{";
	        strJson +="\"title\":\""+title+"\",";    
	        strJson +="\"description\":\""+description+"\",";  
	        strJson +="\"url\":\""+url+"\",";  
	        strJson +="\"picurl\":\""+picurl+"\"";  
	        strJson += "}]}}";
		    post(sendUrl, strJson,"application/json");
		}
	}

	public void sendVedio(String access_token,String title, String description, String media_id) {
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),SEND_KEY);
        String url = sendGetUrl+"?access_token=" + access_token;
		Set<String> openIds=getUsers(access_token);
		if(description==null){
			description="";
		}
		title=filterCharacters(title);
		description=filterCharacters(description);
	    //发送给所有关注者消息
		for(String openId:openIds){
		    String strJson = "{\"touser\" :\""+openId+"\",";
	        strJson += "\"msgtype\":\"video\",";
	        strJson += "\"video\":{";
	        strJson += "\"media_id\":\""+media_id+"\",";
	        strJson += "\"title\":\""+title+"\",";
	        strJson += "\"description\":\""+description+"\"";
	        strJson += "}}";
		    post(url, strJson,"application/json");
		}
	}
	
	private  Set<String> getUsers(Set<String>openIds,String url,String access_token,String next_openid) {
		JSONObject data=getUrlResponse(url);
		try {
			Integer count=(Integer) data.get("count");
			String nextOpenId=(String) data.get("next_openid");
			if(count>0){
				JSONObject openIdData=(JSONObject) data.get("data");
				JSONArray openIdArray= (JSONArray) openIdData.get("openid");
				for(int i=0;i<openIdArray.length();i++){
					openIds.add((String) openIdArray.get(i));
				}
			}
			if(StringUtils.isNotBlank(nextOpenId)){
				return getUsers(openIds,url, access_token, nextOpenId);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openIds;
	}
	
	/**
	 * 创建自定义菜单
	 */
	public Map<String, String> createMenu(String menus){
		int status=Weixin.TENCENT_WX_SUCCESS;
		Map<String, String> msg=weixinTokenCache.getToken();
		String token=msg.get("token");
		if(StringUtils.isNotBlank(token)){
			String createMenuUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),MENU_KEY);
			String url = createMenuUrl+"?access_token=" + token;
			Map<String, String> menuCodeRes=getWeiXinMenuCode(url, menus,"application/json");
			status=Integer.parseInt(menuCodeRes.get("weixinMenuCode"));	
			if(StringUtils.isBlank(msg.get("errmsg"))){
				msg.remove("errmsg");
				msg.put("errmsg",menuCodeRes.get("errmsg"));			
			}		
			msg.put("status",""+status+"");
		}else{
			status=Weixin.TENCENT_WX_GET_TOKEN_ERROR;
			msg.put("status",""+status+"");
		}
		return msg;
	}
	
	
	/**
	 * 群发
	 */
	public Map<String, String> sendTextToAllUser(Content[] beans){
		int status=Weixin.TENCENT_WX_SUCCESS;
		Map<String, String> msg=null;
		msg=weixinTokenCache.getToken();	
		String token=msg.get("token");		
		if(StringUtils.isNotBlank(token)){
			//上传内容到微信
			String articalUploadUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),UPLOAD_NEWS);
			String url = articalUploadUrl+"?access_token=" + token;
			
			String[] str = articalUpload(token, beans);
			Integer contentCount=0;
			contentCount=Integer.parseInt(str[1]);
			if(contentCount>0){
			     HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
				      //HttpClient  
			     CloseableHttpClient client= httpClientBuilder.build();  
			     client = (CloseableHttpClient) wrapClient(client);
			       HttpPost post = new HttpPost(url);
			       try{
			           StringEntity s = new StringEntity(str[0],"utf-8");
			           s.setContentType("application/json");
			           post.setEntity(s);
			           HttpResponse res = client.execute(post);
			           HttpEntity entity = res.getEntity();
			           String contentString=EntityUtils.toString(entity, "utf-8");
			           JSONObject 	json=new JSONObject(contentString);
			           //输出返回消息			   
			           if(contentString.contains("errmsg")){
			        	   Integer startIndex=contentString.indexOf("errmsg")+9;
				           Integer endIndex=contentString.indexOf("\"", startIndex);
			        	   msg.put("errmsg",contentString.substring(startIndex, endIndex));
			           }	
			           String media_id="";
			           if(StringUtils.isNotBlank(contentString)&&
			        		   contentString.contains("media_id")){
			        	   media_id  = (String) json.get("media_id");
			           }
			           log.info("send json->"+json);
			           if(StringUtils.isNotBlank(media_id)){
			        	   String sendAllMessageUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),SEND_ALL_MESSAGE);
				   		   String url_send = sendAllMessageUrl+"?access_token=" + token;
				   		   String str_send = "{\"filter\":{\"is_to_all\":true},\"mpnews\":{\"media_id\":\""+media_id+"\"},\"msgtype\":\"mpnews\"}";
				   		   post(url_send, str_send,"application/json");
				   		   msg.put("status",""+status+"");
			           }else{
			        	   log.error("not has media");
			        	   status=Weixin.TENCENT_WX_UPLOAD_CONTENT_ERROR;
			        	   msg.put("status",""+status+"");
			           }
			       }catch (Exception e){
			    	   e.printStackTrace();
			       }
			}else{
				status=Weixin.TENCENT_WX_UPLOAD_CONTENT_LESS;
				msg.put("status",""+status+"");
			}
		}else{
			status=Weixin.TENCENT_WX_GET_TOKEN_ERROR;
			msg.put("status",""+status+"");
		}
		return msg;
	}
	
	
	
	private String  uploadImg(String access_token,String filePath){
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JEECMS_CONFIG)),UPLOAD_IMG_URL);
        String url = sendGetUrl+"?access_token=" + access_token;
		String result = null;
		String mediaId="";
		FileUpload fileUpload = new FileUpload();
		try {
			result = fileUpload.uploadFile(url,filePath, null);
			if(StringUtils.isNotBlank(result)&&result.startsWith("{")){
				JSONObject json=new JSONObject(result);
				mediaId=json.getString("url");
			}
		} catch (Exception e) {
		//	e.printStackTrace();
			log.error(e.getMessage());
		}
		return mediaId;
	}
	
	private String contentHtmlProc(String token,Content bean){
		CmsConfig config=cmsConfigMng.get();
		String txt=bean.getTxt();
		CmsSite site=bean.getSite();
		Ftp ftp= site.getUploadFtp();
		List<String>imgUrls=ImageUtils.getImageSrc(txt);
		for(String img:imgUrls){
			String imgRealUrl="";
			if(img.startsWith(site.getProtocol())){
				//外链图片(先抓取到本地)
				imgRealUrl=imgSvc.crawlImg(img, config.getContextPath(), config.getUploadToDb(),
						config.getDbFileUri(), ftp, site.getUploadOss(),site.getUploadPath());
			}else{
				imgRealUrl=img;
			}
			if(StringUtils.isNotBlank(bean.getSite().getContextPath())){
				imgRealUrl=imgRealUrl.substring(bean.getSite().getContextPath().length());
			}
			//上传图片到微信
			imgRealUrl= uploadImg(token, realPathResolver.get(imgRealUrl));
			if(StringUtils.isNotBlank(imgRealUrl)){
				txt=txt.replace(img, imgRealUrl);
			}
		}
		//html标签双引号需要注意
		txt=txt.replaceAll("\"", "\'"); 
		return txt;
	}
	
	private String[] articalUpload(String token,Content[] beans){
		Integer count=0;
		String str = "{\"articles\":[";
				for(int i=0;i<beans.length;i++){
					Content bean = beans[i];
					String author =bean.getAuthor();
					if(StringUtils.isBlank(author)){
						author=bean.getSiteName();
					}
					String txt=contentHtmlProc(token, bean);
					String sourceUrl=bean.getSoureUrl();
					String mediaId ="";
					if(!StringUtils.isBlank(bean.getTypeImg())){
						String typeImg=bean.getTypeImg();
						String contextPath=bean.getSite().getConfig().getContextPath();
						if(StringUtils.isNotBlank(contextPath)&&typeImg.startsWith(contextPath)){
							typeImg=realPathResolver.get(typeImg.substring(contextPath.length()));
						}else{
							typeImg=realPathResolver.get(typeImg);
						}
						mediaId = uploadFile(token,typeImg, "image");
						String desc="";
						if(StringUtils.isNotBlank(bean.getDescription())){
							desc=bean.getDescription();
						}
						if(StringUtils.isNotBlank(mediaId)){
							str = str+"{"+
			                        "\"thumb_media_id\":\""+mediaId+"\","+
			                        "\"author\":\""+author+"\","+
									 "\"title\":\""+bean.getTitle()+"\","+
									 "\"content_source_url\":\""+sourceUrl+"\","+
									 "\"content\":\""+txt+"\","+
									 "\"digest\":\""+desc+"\","
									 +"\"show_cover_pic\":\"0\""+"}";
							if(i!=beans.length-1){
								str = str+",";
							}
							count++;
						}
					}
				}
		str = str +"]}";	
		String[]result=new String[2];
		result[0]=str;
		result[1]=count.toString();
		return result;
	}
	
	
	
	private  JSONObject getUrlResponse(String url){
		CharsetHandler handler = new CharsetHandler("UTF-8");
		try {
			HttpGet httpget = new HttpGet(new URI(url));
			 HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	        //HttpClient  
	        CloseableHttpClient client = httpClientBuilder.build();  
	        client = (CloseableHttpClient) wrapClient(client);
			return new JSONObject(client.execute(httpget, handler));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	   
	
	private Map<String, String> getWeiXinMenuCode(String url, String json,String contentType)
	   {
		   Integer weixinMenuCode=Weixin.TENCENT_WX_SUCCESS;
		   HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	       //HttpClient  
	       CloseableHttpClient client = httpClientBuilder.build();  
	       client = (CloseableHttpClient) wrapClient(client);	       
	       Map<String, String> msg=new HashMap<>();
	       HttpPost post = new HttpPost(url);
	       try
	       {
	           StringEntity s = new StringEntity(json,"utf-8");
	           if(StringUtils.isBlank(contentType)){
	        	   s.setContentType("application/json");
	           }
	           s.setContentType(contentType);
	           post.setEntity(s);
	           HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            String str=EntityUtils.toString(entity, "utf-8");
            if(StringUtils.isNotBlank(str)){
            	JSONObject jObj=new JSONObject(str);
            	weixinMenuCode=(Integer) jObj.get("errcode");
            	if (jObj.has("errmsg")) {
            		msg.put("errmsg", (String)jObj.get("errmsg"));
				}else{
					msg.put("errmsg",null);
				}
            	msg.put("weixinMenuCode", ""+weixinMenuCode+"");
            }         
            log.info("send to weixin response-> "+str);
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	       return msg;
	   }
	
	private  void post(String url, String json,String contentType)
	   {
		   HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	       //HttpClient  
	       CloseableHttpClient client = httpClientBuilder.build();  
	       client = (CloseableHttpClient) wrapClient(client);
	       HttpPost post = new HttpPost(url);
	       try
	       {
	           StringEntity s = new StringEntity(json,"utf-8");
	           if(StringUtils.isBlank(contentType)){
	        	   s.setContentType("application/json");
	           }
	           s.setContentType(contentType);
	           post.setEntity(s);
	           HttpResponse res = client.execute(post);
               HttpEntity entity = res.getEntity();
               String str=EntityUtils.toString(entity, "utf-8");
               log.info("send to weixin response-> "+str);
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	   }
	
	private String filterCharacters(String txt){
		if(StringUtils.isNotBlank(txt)){
			txt=txt.replace("&ldquo;", "“").replace("&rdquo;", "”").replace("&nbsp;", " ");
		}
		return txt;
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
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private WeixinTokenCache weixinTokenCache;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private ImageSvc imgSvc;

}
