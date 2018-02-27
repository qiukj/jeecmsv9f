package com.jeecms.cms.entity.back;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.back.base.BaseCmsField;

public class CmsField extends BaseCmsField {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsField() {
		super();
	}

	/* [CONSTRUCTOR MARKER END] */
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getFieldType())) {
			json.put("fieldType", getFieldType());
		}else{
			json.put("fieldType", "");
		}
		if (StringUtils.isNotBlank(getFieldDefault())) {
			json.put("fieldDefault", getFieldDefault());
		}else{
			json.put("fieldDefault", "");
		}
		if (StringUtils.isNotBlank(getFieldProperty())) {
			json.put("fieldProperty", getFieldProperty());
		}else{
			json.put("fieldProperty", "");
		}
		if (StringUtils.isNotBlank(getComment())) {
			json.put("comment", getComment());
		}else{
			json.put("comment", "");
		}
		if (StringUtils.isNotBlank(getNullable())) {
			json.put("nullable", getNullable());
		}else{
			json.put("nullable", "");
		}
		if (StringUtils.isNotBlank(getExtra())) {
			json.put("extra", getExtra());
		}else{
			json.put("extra", "");
		}
		if (StringUtils.isNotBlank(getLength())) {
			json.put("length", getLength());
		}else{
			json.put("length", "");
		}
		return json;
	}

}