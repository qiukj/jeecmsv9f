package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsFriendlink;

public class CmsFriendlink extends BaseCmsFriendlink {
	private static final long serialVersionUID = 1L;

	public void init() {
		if (getPriority() == null) {
			setPriority(10);
		}
		if (getViews() == null) {
			setViews(0);
		}
		if (getEnabled() == null) {
			setEnabled(true);
		}
		blankToNull();
	}

	public void blankToNull() {
		if (StringUtils.isBlank(getLogo())) {
			setLogo(null);
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
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getDomain())) {
			json.put("domain", getDomain());
		}else{
			json.put("domain", "");
		}
		if(StringUtils.isNotBlank(getLogo())){
			json.put("logo", getLogo());
		}else{
			json.put("logo", "");
		}
		if (StringUtils.isNotBlank(getEmail())) {
			json.put("email", getEmail());
		}else{
			json.put("email", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (getViews()!=null) {
			json.put("views", getViews());
		}else{
			json.put("views", "");
		}
		if (getEnabled()!=null) {
			json.put("enabled", getEnabled());
		}else{
			json.put("enabled", "");
		}
		if (getCategory()!=null&&StringUtils.isNotBlank(getCategory().getName())) {
			json.put("categoryName", getCategory().getName());
		}else{
			json.put("categoryName", "");
		}
		if (getCategory()!=null&&getCategory().getId()!=null) {
			json.put("categoryId", getCategory().getId());
		}else{
			json.put("categoryId", "");
		}
		return json;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsFriendlink() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsFriendlink(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsFriendlink(java.lang.Integer id,
			com.jeecms.cms.entity.assist.CmsFriendlinkCtg category,
			com.jeecms.core.entity.CmsSite site, java.lang.String name,
			java.lang.String domain, java.lang.Integer views,
			java.lang.Integer priority, java.lang.Boolean enabled) {

		super(id, category, site, name, domain, views, priority, enabled);
	}

	/* [CONSTRUCTOR MARKER END] */

}