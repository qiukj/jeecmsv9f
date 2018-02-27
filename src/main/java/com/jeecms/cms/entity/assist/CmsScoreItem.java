package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsScoreItem;



public class CmsScoreItem extends BaseCmsScoreItem {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getScore()!=null) {
			json.put("score", getScore());
		}else{
			json.put("score", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getImagePath())) {
			json.put("imagePath", getImagePath());
		}else{
			json.put("imagePath", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		if (getGroup()!=null&&StringUtils.isNotBlank(getGroup().getName())) {
			json.put("groupName", getGroup().getName());
		}else{
			json.put("groupName", "");
		}
		if (getGroup()!=null&&getGroup().getId()!=null) {
			json.put("groupId", getGroup().getId());
		}else{
			json.put("groupId", "");
		}
		return json;
	}
	
	public void init(){
		if (getPriority()==null) {
			setPriority(10);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsScoreItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsScoreItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsScoreItem (
		java.lang.Integer id,
		com.jeecms.cms.entity.assist.CmsScoreGroup group,
		java.lang.String name,
		java.lang.Integer score,
		java.lang.Integer priority) {

		super (
			id,
			group,
			name,
			score,
			priority);
	}

/*[CONSTRUCTOR MARKER END]*/


}