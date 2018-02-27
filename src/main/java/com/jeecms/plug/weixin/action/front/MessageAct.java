package com.jeecms.plug.weixin.action.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.plug.weixin.entity.WeixinMessage;
import com.jeecms.plug.weixin.manager.WeixinMessageMng;
import com.jeecms.plug.weixin.manager.WeixinMng;

@Controller
public class MessageAct {
	private static final Logger log = LoggerFactory
			.getLogger(MessageAct.class);
	
	/**
	 * 微信开发者验证URL
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException 
	 */
	@RequestMapping(value = "/sendMessage.jspx")
	public void weixin(String signature,String timestamp,
			String nonce,String echostr,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) throws IOException {
	       	//开发者验证填写TOKEN值
			CmsSite site=CmsUtils.getSite(request);
			//"myjcywangluoweixin"
			String token = site.getWxToken();
	        Object[] tmpArr=new Object[]{token,timestamp,nonce};
	        Arrays.sort(tmpArr);
	        String str=tmpArr[0].toString()+tmpArr[1].toString()+tmpArr[2].toString();
	        String tmpStr = DigestUtils.shaHex(str);
	        if(tmpStr.equals(signature)){
	            // 调用核心业务类接收消息、处理消息
	            processRequest(echostr,request,response);
	        }else{
	        	log.error("false");
	        }
	        
	}
	
	
	private String processRequest(String echostr,HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		String postStr = readStreamParameter(request.getInputStream()) ;
		Document document=null;
		try{
			if(postStr!=null && !postStr.trim().equals("")){
				document = DocumentHelper.parseText(postStr);
			}
        }catch(Exception e){  
            e.printStackTrace();  
        }
		if(null==document){  
			response.getWriter().write(echostr);
            return null;  
        }
		CmsSite site = CmsUtils.getSite(request);
		Element root=document.getRootElement();
        String fromUsername = root.elementText("FromUserName");  //取得发送者
        String toUsername = root.elementText("ToUserName");  //取得接收者
        String userMsgType  = root.elementText("MsgType");
        
        String keyword =root.elementTextTrim("Content");
        String time = new Date().getTime()+"";  
        
        // 默认返回的文本消息内容  
        String respContent = "no body"; 
        String welcome=weixinMng.find(CmsUtils.getSiteId(request)).getWelcome();
        if(userMsgType.equals("event")){
        	// 事件类型  
            String eventType = root.elementText("Event"); 
        	// 订阅  
            if (eventType.equals("subscribe")) {  
                respContent = welcome;  
                respContent = text(respContent, fromUsername, toUsername, time);
                send(respContent, response);
                return null;
            }  
            // 取消订阅  
            else if (eventType.equals("unsubscribe")) {  
                // TODO 取消订阅后用户再收不到公众号发送的消息，因此不须要回复消息  
            	return null;
            } 
            // 自定义菜单点击事件  
            // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
            String eventKey = root.elementText("EventKey");  
            //返回自定义回复的定义
            autoReply(eventKey, fromUsername, toUsername, time, site, request, response);
            return null;
        }  
        //回复内容
        if(keyword!=null){
        	keyword = keyword.trim();
        }
        if(keyword!=null && userMsgType.equals("text")){
        	autoReply(keyword, fromUsername, toUsername, time, site, request, response);
        }
		return null;
	}
	
	private void autoReply(String keyword,String fromUsername,String toUsername, String time,CmsSite site,HttpServletRequest request,HttpServletResponse response) throws IOException{
		WeixinMessage entity = weixinMessageMng.findByNumber(keyword,site.getId());
    	if(entity!=null){
    		String text = contentWithImgUseMessage(entity, fromUsername, toUsername, time, request);
    		send(text, response);
    	}else{
    		entity = weixinMessageMng.getWelcome(site.getId());
    		if(entity!=null){
    			StringBuffer buffer = new StringBuffer();
    			String textTpl ="";
    			//内容+关键字 标题 提示
    			if(entity.getType().equals(WeixinMessage.CONTENT_WITH_KEY)){
    				buffer.append(entity.getContent()).append("\n");
            		List<WeixinMessage> lists = weixinMessageMng.getList(site.getId());
                	for (int i = 0; i < lists.size(); i++) {
        				buffer.append("  【"+lists.get(i).getNumber()+"】"+lists.get(i).getTitle()).append("\n");
        			}
                	textTpl=text(buffer.toString(), fromUsername, toUsername, time);
    			}else if(entity.getType().equals(WeixinMessage.CONTENT_ONLY)){
    				//仅限内容
    				buffer.append(entity.getContent()).append("\n");
    				textTpl=text(buffer.toString(), fromUsername, toUsername, time);
    			}else if(entity.getType().equals(WeixinMessage.CONTENT_WITH_IMG)){
    				//图文类型（图片 标题 文字 链接组成）
    				textTpl=contentWithImgUseMessage(entity, fromUsername, toUsername, time, request);
    			}
        		send(textTpl, response);
    		}
    	}
	}
	
	
	//从输入流读取post参数  
    private String readStreamParameter(ServletInputStream in){  
        StringBuilder buffer = new StringBuilder();  
        BufferedReader reader=null;  
        try{  
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));  
            String line=null;  
            while((line = reader.readLine())!=null){  
                buffer.append(line);  
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(null!=reader){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return buffer.toString();  
    } 
    
    private String contentWithImgUseMessage(WeixinMessage entity,String fromUsername,String toUsername,String time,HttpServletRequest request){
    	CmsSite site = CmsUtils.getSite(request);
    	String path = site.getDomain();
    	String textTpls = text(fromUsername, toUsername, time, entity.getTitle(), entity.getContent(), "http://"+path+entity.getPath(), entity.getUrl());
    	return textTpls;
    }
    
    
    private String text(String fromUsername,String toUsername,String time,String title,String desc,String img,String url){
    	String textTpls = "<xml>"+  
                "<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+  
                "<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+  
                "<CreateTime>"+time+"</CreateTime>"+  
                "<MsgType><![CDATA[news]]></MsgType>"+  
                "<ArticleCount>1</ArticleCount>"+ 
                "<Articles>"+ 
                "<item>"+ 
                "<Title><![CDATA["+title+"]]></Title>"+ 
                "<Description><![CDATA["+desc+"]]></Description>"+ 
                "<PicUrl><![CDATA["+img+"]]></PicUrl>"+ 
                "<Url><![CDATA["+url+"]]></Url>"+ 
                "</item>"+
                "</Articles>"+ 
                "</xml>";
    	return textTpls;
    }
    
    private String text(String str,String fromUsername,String toUsername,String time){
    	String textTpls = "<xml>"+  
                "<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+  
                "<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+  
                "<CreateTime>"+time+"</CreateTime>"+  
                "<MsgType><![CDATA[text]]></MsgType>"+  
                "<Content><![CDATA["+str+"]]></Content>"+ 
                "</xml>";
    	
    	return textTpls;
    }
    
    private void send(String textTpl,HttpServletResponse response) throws IOException{
    	String type="text/xml;charset=UTF-8";
    	response.setContentType(type);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
    	response.getWriter().write(textTpl);
    }
    
	@Autowired
	private WeixinMessageMng weixinMessageMng;
	@Autowired
	private WeixinMng weixinMng;
}
