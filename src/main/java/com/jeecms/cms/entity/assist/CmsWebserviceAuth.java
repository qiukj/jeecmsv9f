package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsWebserviceAuth;



public class CmsWebserviceAuth extends BaseCmsWebserviceAuth {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getUsername())) {
			json.put("username", getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(getPassword())) {
			json.put("password", getPassword());
		}else{
			json.put("password", "");
		}
		if (StringUtils.isNotBlank(getSystem())) {
			json.put("system", getSystem());
		}else{
			json.put("system", "");
		}
		json.put("enable", getEnable());
		return json;
	}

	public boolean getEnable() {
		return super.isEnable();
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsWebserviceAuth () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsWebserviceAuth (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsWebserviceAuth (
		java.lang.Integer id,
		java.lang.String username,
		java.lang.String password,
		java.lang.String system,
		boolean enable) {

		super (
			id,
			username,
			password,
			system,
			enable);
	}

/*[CONSTRUCTOR MARKER END]*/


}