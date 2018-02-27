package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseApiInfo;



public class ApiInfo extends BaseApiInfo {
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
		if (StringUtils.isNotBlank(getUrl())) {
			json.put("url", getUrl());
		}else{
			json.put("url", "");
		}
		if (StringUtils.isNotBlank(getCode())) {
			json.put("code", getCode());
		}else{
			json.put("code", "");
		}
		if (getLimitCallDay()!=null) {
			json.put("limitCallDay", getLimitCallDay());
		}else{
			json.put("limitCallDay", "");
		}
		if (getCallTotalCount()!=null) {
			json.put("callTotalCount", getCallTotalCount());
		}else{
			json.put("callTotalCount", "");
		}
		if (getCallMonthCount()!=null) {
			json.put("callMonthCount", getCallMonthCount());
		}else{
			json.put("callMonthCount", "");
		}
		if (getCallWeekCount()!=null) {
			json.put("callWeekCount", getCallWeekCount());
		}else{
			json.put("callWeekCount", "");
		}
		if (getCallDayCount()!=null) {
			json.put("callDayCount", getCallDayCount());
		}else{
			json.put("callDayCount", "");
		}
		json.put("disabled", getDisabled());
		return json;
	}
	
	public void init(){
		if(getCallDayCount()==null){
			setCallDayCount(0);
		}
		if(getCallMonthCount()==null){
			setCallMonthCount(0);
		}
		if(getCallWeekCount()==null){
			setCallWeekCount(0);
		}
		if(getCallTotalCount()==null){
			setCallTotalCount(0);
		}
		if (getLimitCallDay()==null) {
			setLimitCallDay(0);
		}
	}
	public boolean getDisabled () {
		return super.isDisabled();
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiInfo () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiInfo (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiInfo (
		java.lang.Integer id,
		java.lang.String url,
		java.lang.String code,
		boolean disabled,
		java.lang.Integer callTotalCount,
		java.lang.Integer callMonthCount,
		java.lang.Integer callWeekCount,
		java.lang.Integer callDayCount) {

		super (
			id,
			url,
			code,
			disabled,
			callTotalCount,
			callMonthCount,
			callWeekCount,
			callDayCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}