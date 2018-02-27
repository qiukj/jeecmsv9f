package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;

import com.jeecms.cms.entity.assist.base.BaseCmsDirectiveTpl;

import net.sf.json.JSONObject;



public class CmsDirectiveTpl extends BaseCmsDirectiveTpl {
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
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getCode())) {
			json.put("code", getCode());
		}else{
			json.put("code", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getUsername())) {
			json.put("username", getUser().getUsername());
		}else{
			json.put("username", "");
		}
		
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsDirectiveTpl () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsDirectiveTpl (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsDirectiveTpl (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsUser user,
		java.lang.String name) {

		super (
			id,
			user,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/


}