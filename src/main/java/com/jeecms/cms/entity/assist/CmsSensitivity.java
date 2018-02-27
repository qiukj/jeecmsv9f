package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsSensitivity;



public class CmsSensitivity extends BaseCmsSensitivity {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getSearch())) {
			json.put("search", getSearch());
		}else{
			json.put("search", "");
		}
		if (StringUtils.isNotBlank(getReplacement())) {
			json.put("replacement", getReplacement());
		}else{
			json.put("replacement", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsSensitivity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSensitivity (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSensitivity (
		java.lang.Integer id,
		java.lang.String search,
		java.lang.String replacement) {

		super (
			id,
			search,
			replacement);
	}

/*[CONSTRUCTOR MARKER END]*/


}