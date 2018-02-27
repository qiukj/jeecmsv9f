package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.common.util.DateUtils;
import com.jeecms.core.entity.base.BaseCmsLog;

public class CmsLog extends BaseCmsLog {
	private static final long serialVersionUID = 1L;
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	public static final int OPERATING = 3;
	
	public static final String LOGIN_SUCCESS_TITLE = "login success";
	public static final String LOGIN_FAILURE_TITLE = "login failure";

	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getCategory()!=null) {
			json.put("category", getCategory());
		}else{
			json.put("category", "");
		}
		if (getTime()!=null) {
			json.put("time", DateUtils.parseDateToDateStr(getTime()));
		}else{
			json.put("time", "");
		}
		if (StringUtils.isNotBlank(getIp())) {
			json.put("ip", getIp());
		}else{
			json.put("ip", "");
		}
		if (StringUtils.isNotBlank(getUrl())) {
			json.put("url", getUrl());
		}else{
			json.put("url", "");
		}
		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(getContent())) {
			json.put("content", getContent());
		}else{
			json.put("content", "");
		}
		if (getUser()!=null&&getUser().getId()!=null) {
			json.put("userId", getUser().getId());
		}else{
			json.put("userId", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getUsername())) {
			json.put("userName", getUser().getUsername());
		}else{
			json.put("userName","");
		}
		return json;
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsLog () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsLog (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsLog (
		java.lang.Integer id,
		java.lang.Integer category,
		java.util.Date time) {

		super (
			id,
			category,
			time);
	}

	/* [CONSTRUCTOR MARKER END] */

}