package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseCmsModelItem;

public class CmsModelItem extends BaseCmsModelItem {
	private static final long serialVersionUID = 1L;
	
	public static final Integer DATA_TYPE_STRING = 1;
	public static final Integer DATA_TYPE_INTEGER = 2;
	public static final Integer DATA_TYPE_FLOAT = 3;
	public static final Integer DATA_TYPE_TEXTAREA = 4;
	public static final Integer DATA_TYPE_DATE = 5;
	public static final Integer DATA_TYPE_SELECT = 6;
	public static final Integer DATA_TYPE_CHECKBOX = 7;
	public static final Integer DATA_TYPE_RADIO = 8;
	public static final Integer DATA_TYPE_ATTACHMENT = 9;
	public static final Integer DATA_TYPE_PICTRUE = 10;
	
	public JSONObject convertToJsonList(){
		JSONObject json = commonJson();
		if (getDataType()!=null) {
			if (getDataType().equals(6)||getDataType().equals(7)||getDataType().equals(8)) {
				JSONArray jsonArray = new JSONArray();
				String op = getOptValue();
				if (StringUtils.isNotBlank(op)) {
					String[] split = op.split(",");
					for (int i = 0; i < split.length; i++) {
						jsonArray.put(i,split[i]);
					}
				}
				json.put("optValue", jsonArray);
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
	
	public JSONObject convertToJson(){
		JSONObject json = commonJson();
		if (StringUtils.isNotBlank(getOptValue())) {
			json.put("optValue", getOptValue());
		}else{
			json.put("optValue", "");
		}
		return json;
	}
	
	//get/list共用json
	private JSONObject commonJson(){
		JSONObject json= new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
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
		if (StringUtils.isNotBlank(getDefValue())) {
			json.put("defValue", getDefValue());
		}else{
			json.put("defValue", "");
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
		if (getSingle()!=null) {
			json.put("single", getSingle());
		}else{
			json.put("single", "");
		}
		if (getChannel()!=null) {
			json.put("channel", getChannel());
		}else{
			json.put("channel", "");
		}
		if (getCustom()!=null) {
			json.put("custom", getCustom());
		}else{
			json.put("custom", "");
		}
		if (getDisplay()!=null) {
			json.put("display", getDisplay());
		}else{
			json.put("display", "");
		}
		if (getRequired()!=null) {
			json.put("required", getRequired());
		}else{
			json.put("required", "");
		}
		if (getImageWidth()!=null) {
			json.put("imageWidth", getImageWidth());
		}else{
			json.put("imageWidth", "");
		}
		if (getImageHeight()!=null) {
			json.put("imageHeight", getImageHeight());
		}else{
			json.put("imageHeight", "");
		}
		return json;
	}

	public void init() {
		if (getPriority() == null) {
			setPriority(10);
		}
		if (getChannel()==null) {
			setChannel(true);
		}
		if (getCustom() == null) {
			setCustom(true);
		}
		if (getDisplay() == null) {
			setDisplay(true);
		}
		if (getSingle()==null) {
			setSingle(true);
		}
		if (getRequired()==null) {
			setRequired(false);
		}
		if (getDataType()==null) {
			setDataType(1);
		}
		if (StringUtils.isBlank(getRows())) {
			setRows("3");
		}
		if (StringUtils.isBlank(getCols())) {
			setCols("30");
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

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsModelItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsModelItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsModelItem (
		java.lang.Integer id,
		com.jeecms.cms.entity.main.CmsModel model,
		java.lang.String field,
		java.lang.String label,
		java.lang.Integer dataType,
		java.lang.Boolean single,
		java.lang.Boolean channel,
		java.lang.Boolean custom,
		java.lang.Boolean display) {

		super (
			id,
			model,
			field,
			label,
			dataType,
			single,
			channel,
			custom,
			display);
	}

	/* [CONSTRUCTOR MARKER END] */

}