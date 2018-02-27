package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsAccountPay;
import com.jeecms.common.util.DateUtils;



public class CmsAccountPay extends BaseCmsAccountPay {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getDrawNum())) {
			json.put("drawNum", getDrawNum());
		}else{
			json.put("drawNum", "");
		}
		if (StringUtils.isNotBlank(getPayAccount())) {
			json.put("payAccount", getPayAccount());
		}else{
			json.put("payAccount", "");
		}
		if (StringUtils.isNotBlank(getDrawAccount())) {
			json.put("drawAccount", getDrawAccount());
		}else{
			json.put("drawAccount", "");
		}
		if (getPayTime()!=null) {
			json.put("payTime", DateUtils.parseDateToDateStr(getPayTime()));
		}else{
			json.put("payTime", "");
		}
		if (StringUtils.isNotBlank(getWeixinNum())) {
			json.put("weixinNum", getWeixinNum());
		}else{
			json.put("weixinNum", "");
		}
		if (StringUtils.isNotBlank(getAlipayNum())) {
			json.put("alipayNum", getAlipayNum());
		}else{
			json.put("alipayNum", "");
		}
		if (getPayUser()!=null&&getPayUser().getId()!=null) {
			json.put("payUserId", getPayUser().getId());
		}else{
			json.put("payUserId", "");
		}
		if (getPayUser()!=null&&StringUtils.isNotBlank(getPayUser().getUsername())) {
			json.put("payUserName", getPayUser().getUsername());
		}else{
			json.put("payUserName", "");
		}
		if (getDrawUser()!=null&&getDrawUser().getId()!=null) {
			json.put("drawUserId", getDrawUser().getId());
		}else{
			json.put("drawUserId", "");
		}
		if (getDrawUser()!=null&&StringUtils.isNotBlank(getDrawUser().getUsername())) {
			json.put("drawUserName", getDrawUser().getUsername());
		}else{
			json.put("drawUserName", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsAccountPay () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsAccountPay (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsAccountPay (
		java.lang.Long id,
		com.jeecms.core.entity.CmsUser payUser,
		com.jeecms.core.entity.CmsUser drawUser,
		java.lang.String drawNum,
		java.lang.String payAccount,
		java.lang.String drawAccount,
		java.util.Date payTime) {

		super (
			id,
			payUser,
			drawUser,
			drawNum,
			payAccount,
			drawAccount,
			payTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}