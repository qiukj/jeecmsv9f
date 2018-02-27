package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseContentTag;

public class ContentTag extends BaseContentTag {
	private static final long serialVersionUID = 1L;

	public void init() {
		if (getCount() == null) {
			setCount(0);
		}
	}
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getCount()!=null) {
			json.put("count", getCount());
		}else{
			json.put("count", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		return json;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public ContentTag () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ContentTag (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ContentTag (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer count) {

		super (
			id,
			name,
			count);
	}

	/* [CONSTRUCTOR MARKER END] */

}