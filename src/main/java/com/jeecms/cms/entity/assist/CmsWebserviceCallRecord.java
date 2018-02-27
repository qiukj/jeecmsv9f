package com.jeecms.cms.entity.assist;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsWebserviceCallRecord;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.springmvc.MessageResolver;



public class CmsWebserviceCallRecord extends BaseCmsWebserviceCallRecord {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(HttpServletRequest request){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getServiceCode())) {
			json.put("serviceCode", MessageResolver.getMessage(request, "cmsWebservice.call."+getServiceCode()));
		}else{
			json.put("serviceCode", "");
		}
		if (getRecordTime()!=null) {
			json.put("recordTime", DateUtils.parseDateToDateStr(getRecordTime()));
		}else{
			json.put("recordTime", "");
		}
		if (getAuth()!=null&&StringUtils.isNotBlank(getAuth().getUsername())) {
			json.put("authUsername", getAuth().getUsername());
		}else{
			json.put("authUsername", "");
		}
		if (getAuth()!=null&&StringUtils.isNotBlank(getAuth().getSystem())) {
			json.put("authSystem", getAuth().getSystem());
		}else{
			json.put("authSystem", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsWebserviceCallRecord () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsWebserviceCallRecord (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsWebserviceCallRecord (
		java.lang.Integer id,
		com.jeecms.cms.entity.assist.CmsWebserviceAuth auth,
		java.lang.String serviceCode,
		java.util.Date recordTime) {

		super (
			id,
			auth,
			serviceCode,
			recordTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}