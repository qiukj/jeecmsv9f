package com.jeecms.core.entity;

import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsUserSite;

public class CmsUserSite extends BaseCmsUserSite {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getCheckStep()!=null) {
			json.put("checkStep", getCheckStep());
		}else{
			json.put("checkStep", "");
		}
		if (getAllChannel()!=null) {
			json.put("allChannel", getAllChannel());
		}else{
			json.put("allChannel", "");
		}
		return json;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsUserSite () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsUserSite (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsUserSite (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsUser user,
		com.jeecms.core.entity.CmsSite site,
		java.lang.Byte checkStep,
		java.lang.Boolean allChannel) {

		super (
			id,
			user,
			site,
			checkStep,
			allChannel);
	}

	/* [CONSTRUCTOR MARKER END] */

}