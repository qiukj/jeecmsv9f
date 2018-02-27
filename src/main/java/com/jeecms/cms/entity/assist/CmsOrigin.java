package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsOrigin;



public class CmsOrigin extends BaseCmsOrigin {
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
		if (getRefCount()!=null) {
			json.put("refCount", getRefCount());
		}else{
			json.put("refCount", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsOrigin () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsOrigin (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsOrigin (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer refCount) {

		super (
			id,
			name,
			refCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}