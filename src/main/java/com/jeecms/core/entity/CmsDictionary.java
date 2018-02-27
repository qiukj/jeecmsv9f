package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsDictionary;



public class CmsDictionary extends BaseCmsDictionary {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getValue())) {
			json.put("value", getValue());
		}else{
			json.put("value", "");
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
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsDictionary () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsDictionary (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsDictionary (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String value,
		java.lang.String type) {

		super (
			id,
			name,
			value,
			type);
	}

/*[CONSTRUCTOR MARKER END]*/


}