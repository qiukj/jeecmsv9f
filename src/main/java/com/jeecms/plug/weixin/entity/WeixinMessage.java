package com.jeecms.plug.weixin.entity;

import org.apache.commons.lang.StringUtils;

import com.jeecms.plug.weixin.entity.base.BaseWeixinMessage;

import net.sf.json.JSONObject;



public class WeixinMessage extends BaseWeixinMessage {
	private static final long serialVersionUID = 1L;
	public static final int CONTENT_ONLY=2;
	public static final int CONTENT_WITH_KEY=1;
	public static final int CONTENT_WITH_IMG=0;
	public java.lang.Boolean getWelcome (){
		return super.isWelcome();
	}
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getNumber())) {
			json.put("number", getNumber());
		}else{
			json.put("number", "");
		}
		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(getPath())) {
			json.put("path", getPath());
		}else{
			json.put("path", "");
		}
		if (StringUtils.isNotBlank(getUrl())) {
			json.put("url", getUrl());
		}else{
			json.put("url", "");
		}
		if (StringUtils.isNotBlank(getContent())) {
			json.put("content", getContent());
		}else{
			json.put("content", "");
		}
		if (getWelcome()!=null) {
			json.put("welcome", getWelcome());
		}else{
			json.put("welcome", "");
		}
		if (getType()!=null) {
			json.put("type", getType());
		}else{
			json.put("type", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public WeixinMessage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public WeixinMessage (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}