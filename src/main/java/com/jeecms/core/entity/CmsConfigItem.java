package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsConfigItem;



public class CmsConfigItem extends BaseCmsConfigItem {
	//会员注册模型
	public static final int CATEGORY_REGISTER=1;
	//留言模型
	public static final int CATEGORY_GUESTBOOK=2;
	private static final long serialVersionUID = 1L;
	public boolean getRequired(){
		return isRequired();
	}
	
	public JSONObject convertToJsonList(){
		JSONObject json = convertToJsonCommon();
		if (getDataType()!=null) {
			Integer type = getDataType();
			if (type.equals(6)||type.equals(7)||type.equals(8)) {
				JSONArray jsonArray = new JSONArray();
				if (StringUtils.isNotBlank(getOptValue())) {
					String value = getOptValue();
					String[] split = value.split(",");
					for(int i = 0 ; i< split.length;i++){
						jsonArray.put(i,split[i]);
					}
				}
				json.put("optValue", jsonArray.toString());
			}else{
				if (StringUtils.isNotBlank(getOptValue())) {
					json.put("optValue", getOptValue());
				}else{
					json.put("optValue", "");
				}
			}
		}
		return json;
	}
	
	public JSONObject convertToJsonGet(){
		JSONObject json = convertToJsonCommon();
		if (StringUtils.isNotBlank(getOptValue())) {
			json.put("optValue", getOptValue());
		}else{
			json.put("optValue", "");
		}
		return json;
	}
	
	private JSONObject convertToJsonCommon(){
		JSONObject json = new JSONObject();
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
		if (getDataType()!=null) {
			json.put("dataType", getDataType());
		}else{
			json.put("dataType", "");
		}
		if (getCategory()!=null) {
			json.put("category", getCategory());
		}else{
			json.put("category", "");
		}
		json.put("required", getRequired());
		if (StringUtils.isNotBlank(getField())) {
			json.put("field", getField());
		}else{
			json.put("field", "");
		}
		if (StringUtils.isNotBlank(getLabel())) {
			json.put("label", getLabel());
		}else{
			json.put("label", "");
		}
		if (StringUtils.isNotBlank(getSize())) {
			json.put("size", getSize());
		}else{
			json.put("size", "");
		}
		if (StringUtils.isNotBlank(getRows())) {
			json.put("rows", getRows());
		}else{
			json.put("rows", "");
		}
		if (StringUtils.isNotBlank(getCols())) {
			json.put("cols", getCols());
		}else{
			json.put("cols", "");
		}
		if (StringUtils.isNotBlank(getHelp())) {
			json.put("help", getHelp());
		}else{
			json.put("help", "");
		}
		if (StringUtils.isNotBlank(getHelpPosition())) {
			json.put("helpPosition", getHelpPosition());
		}else{
			json.put("helpPosition", "");
		}
		if (StringUtils.isNotBlank(getDefValue())) {
			json.put("defValue", getDefValue());
		}else{
			json.put("defValue", "");
		}
		return json;
	}
	
	public void init() {
		if (getPriority() == null) {
			setPriority(10);
		}
	}

	// 将字符串字段全部设置为非null，方便判断。
	public void emptyToNull() {
		if (StringUtils.isBlank(getDefValue())) {
			setDefValue(null);
		}
		if (StringUtils.isBlank(getOptValue())) {
			setOptValue(null);
		}
		if (StringUtils.isBlank(getSize())) {
			setSize(null);
		}
		if (StringUtils.isBlank(getRows())) {
			setRows(null);
		}
		if (StringUtils.isBlank(getCols())) {
			setCols(null);
		}
		if (StringUtils.isBlank(getHelp())) {
			setHelp(null);
		}
		if (StringUtils.isBlank(getHelpPosition())) {
			setHelpPosition(null);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsConfigItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsConfigItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsConfigItem (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsConfig config,
		java.lang.String field,
		java.lang.String label,
		java.lang.Integer priority,
		java.lang.Integer dataType,
		boolean required,
		java.lang.Integer category) {

		super (
			id,
			config,
			field,
			label,
			priority,
			dataType,
			required,
			category);
	}

/*[CONSTRUCTOR MARKER END]*/


}