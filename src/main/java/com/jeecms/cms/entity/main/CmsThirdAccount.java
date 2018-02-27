package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseCmsThirdAccount;



public class CmsThirdAccount extends BaseCmsThirdAccount {
	private static final long serialVersionUID = 1L;
	
	public static final String QQ_KEY="openId";
	public static final String SINA_KEY="uid";
	public static final String QQ_PLAT="QQ";
	public static final String SINA_PLAT="SINA";
	public static final String WEIXIN_PLAT="WEIXIN";
	public static final String QQ_WEBO_KEY="weboOpenId";
	public static final String QQ_WEBO_PLAT="QQWEBO";
	public static final String WEIXIN_KEY="weixinOpenId";
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getAccountKey())) {
			json.put("accountKey", getAccountKey());
		}else{
			json.put("accountKey", "");
		}
		if (StringUtils.isNotBlank(getUsername())) {
			json.put("username", getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(getSource())) {
			json.put("source", getSource());
		}else{
			json.put("source", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getRealname())) {
			json.put("userRealName", getUser().getRealname());
		}else{
			json.put("userRealName", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getIntro())) {
			json.put("userIntro", getUser().getIntro());
		}else{
			json.put("userIntro", "");
		}
		if (getUser()!=null&&getUser().getGender()!=null) {
			json.put("userGender", getUser().getGender());
		}else{
			json.put("userGender", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsThirdAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsThirdAccount (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsThirdAccount (
		java.lang.Long id,
		java.lang.String accountKey,
		java.lang.String username,
		java.lang.String source) {

		super (
			id,
			accountKey,
			username,
			source);
	}

/*[CONSTRUCTOR MARKER END]*/


}