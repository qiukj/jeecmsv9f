package com.jeecms.plug.weixin.entity;

import org.apache.commons.lang.StringUtils;

import com.jeecms.plug.weixin.entity.base.BaseWeixinMenu;

import net.sf.json.JSONObject;



public class WeixinMenu extends BaseWeixinMenu {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getType())) {
			json.put("type", getType());
		}else{
			json.put("type", "");
		}
		if (StringUtils.isNotBlank(getUrl())) {
			json.put("url", getUrl());
		}else{
			json.put("url", "");
		}
		if (StringUtils.isNotBlank(getKey())) {
			json.put("key", getKey());
		}else{
			json.put("key", "");
		}
		return json ;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public WeixinMenu () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public WeixinMenu (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}