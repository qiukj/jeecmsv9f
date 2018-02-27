package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsSiteAccessPages;
import com.jeecms.common.util.DateUtils;



public class CmsSiteAccessPages extends BaseCmsSiteAccessPages {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getAccessPage())) {
			json.put("accessPage", getAccessPage());
		}else{
			json.put("accessPage", "");
		}
		if (StringUtils.isNotBlank(getSessionId())) {
			json.put("sessionId", getSessionId());
		}else{
			json.put("sessionId", "");
		}
		if(getAccessTime()!=null){
			json.put("accessTime", DateUtils.parseToShortTimeStr(getAccessTime()));
		}else{
			json.put("accessTime", "");
		}
		if(getAccessDate()!=null){
			json.put("accessDate", DateUtils.parseDateToDateStr(getAccessDate()));
		}else{
			json.put("accessDate", "");
		}
		if(getVisitSecond()!=null){
			json.put("visitSecond",getVisitSecond());
		}else{
			json.put("visitSecond", "");
		}
		if(getPageIndex()!=null){
			json.put("pageIndex",getPageIndex());
		}else{
			json.put("pageIndex", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsSiteAccessPages () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSiteAccessPages (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSiteAccessPages (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String accessPage,
		java.lang.String sessionId,
		java.util.Date accessTime,
		java.util.Date accessDate,
		java.lang.Integer visitSecond,
		java.lang.Integer pageIndex) {

		super (
			id,
			site,
			accessPage,
			sessionId,
			accessTime,
			accessDate,
			visitSecond,
			pageIndex);
	}

/*[CONSTRUCTOR MARKER END]*/


}