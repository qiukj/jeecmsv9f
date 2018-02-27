package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseContentType;

public class ContentType extends BaseContentType {
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
		if (getImgWidth()!=null) {
			json.put("imgWidth", getImgWidth());
		}else{
			json.put("imgWidth", "");
		}
		if (getImgHeight()!=null) {
			json.put("imgHeight", getImgHeight());
		}else{
			json.put("imgHeight", "");
		}
		if (getHasImage()!=null) {
			json.put("hasImage", getHasImage());
		}else{
			json.put("hasImage", "");
		}
		if (getDisabled()!=null) {
			json.put("disabled", getDisabled());
		}else{
			json.put("disabled", "");
		}
		return json;
	}
	
	public void init(){
		if (getHasImage()==null) {
			setHasImage(false);
		}
		if (getDisabled()==null) {
			setDisabled(false);
		}
		if (getImgHeight()==null) {
			setImgHeight(139);
		}
		if (getImgWidth()==null) {
			setImgWidth(139);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public ContentType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ContentType (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ContentType (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Boolean hasImage,
		java.lang.Boolean disabled) {

		super (
			id,
			name,
			hasImage,
			disabled);
	}

	/* [CONSTRUCTOR MARKER END] */

}