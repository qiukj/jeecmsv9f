package com.jeecms.plug.weixin.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.plug.weixin.entity.base.BaseWeixin;



public class Weixin extends BaseWeixin {
	private static final long serialVersionUID = 1L;
	
	public static final Integer TENCENT_WX_SUCCESS= 200;
	public static final Integer TENCENT_WX_GET_TOKEN_ERROR = 400;
	public static final Integer TENCENT_WX_MENU_APP_ERROR = 401;
	public static final Integer TENCENT_WX_MESSAGE_ERROR = 402;
	public static final Integer TENCENT_WX_UPLOAD_CONTENT_ERROR = 403;
	public static final Integer TENCENT_WX_UPLOAD_CONTENT_LESS = 405;
	public static final Integer TENCENT_WX_SUCCESS_RETURN_CODE= 0;
	
	public JSONObject convertToJson() {
		JSONObject json = new JSONObject();
		if (getId() != null) {
			json.put("id", getId());
		} else {
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getWelcome())) {
			json.put("welcome", getWelcome());
		} else {
			json.put("welcome", "");
		}
		if (StringUtils.isNotBlank(getPic())) {
			json.put("pic", getPic());
		} else {
			json.put("pic", "");
		}
		json.put("wxAppSecret", "");
		json.put("wxToken", "");
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Weixin () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Weixin (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}