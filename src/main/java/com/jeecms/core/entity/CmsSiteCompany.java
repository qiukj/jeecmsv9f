package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsSiteCompany;



public class CmsSiteCompany extends BaseCmsSiteCompany {
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
		if (StringUtils.isNotBlank(getScale())) {
			json.put("scale", getScale());
		}else{
			json.put("scale", "");
		}
		if (StringUtils.isNotBlank(getNature())) {
			json.put("nature", getNature());
		}else{
			json.put("nature", "");
		}
		if (StringUtils.isNotBlank(getIndustry())) {
			json.put("industry", getIndustry());
		}else{
			json.put("industry", "");
		}
		if (StringUtils.isNotBlank(getContact())) {
			json.put("contact", getContact());
		}else{
			json.put("contact", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getAddress())) {
			json.put("address", getAddress());
		}else{
			json.put("address", "");
		}
		if (getLongitude()!=null) {
			json.put("longitude", getLongitude());
		}else{
			json.put("longitude", "");
		}
		if (getLatitude()!=null) {
			json.put("latitude", getLatitude());
		}else{
			json.put("latitude", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsSiteCompany () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSiteCompany (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSiteCompany (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/


}