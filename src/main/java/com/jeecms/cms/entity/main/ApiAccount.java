package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;

import com.jeecms.cms.entity.main.base.BaseApiAccount;

import net.sf.json.JSONObject;



public class ApiAccount extends BaseApiAccount {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getAppId())) {
			json.put("setAppId", getAppId());
		}else{
			json.put("setAppId", "");
		}
		if (StringUtils.isNotBlank(getAppKey())) {
			json.put("appKey", getAppKey());
		}else{
			json.put("appKey", "");
		}
		if (StringUtils.isNotBlank(getAesKey())) {
			json.put("aesKey", getAesKey());
		}else{
			json.put("aesKey", "");
		}
		if (StringUtils.isNotBlank(getIvKey())) {
			json.put("ivKey", getIvKey());
		}else{
			json.put("ivKey", "");
		}
		if (getDisabled()!=null) {
			json.put("disabled", getDisabled());
		}else{
			json.put("disabled", "");
		}
		if (getAdmin()!=null) {
			json.put("admin", getAdmin());
		}else{
			json.put("admin", "");
		}
		if (getLimitSingleDevice()!=null) {
			json.put("limitSingleDevice", getLimitSingleDevice());
		}else{
			json.put("limitSingleDevice", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiAccount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiAccount (
		java.lang.Integer id,
		java.lang.String appId,
		java.lang.String appKey,
		java.lang.Boolean disabled) {

		super (
			id,
			appId,
			appKey,
			disabled);
	}

	public void init() {
		if (getDisabled()==null) {
			setDisabled(false);
		}
		if (getAdmin()==null) {
			setAdmin(false);
		}
		if (getLimitSingleDevice()==null) {
			setLimitSingleDevice(false);
		}
	}

/*[CONSTRUCTOR MARKER END]*/


}