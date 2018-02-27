package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseMarkConfig;



public class MarkConfig extends BaseMarkConfig {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){

		JSONObject json = new JSONObject();
		if (getOn()!=null) {
			json.put("on", getOn());
		}else{
			json.put("on", "");
		}
		if (getMinWidth()!=null) {
			json.put("minWidth", getMinWidth());
		}else{
			json.put("minWidth", "");
		}
		if (getMinHeight()!=null) {
			json.put("minHeight", getMinHeight());
		}else{
			json.put("minHeight", "");
		}
		if (StringUtils.isNotBlank(getImagePath())) {
			json.put("imagePath", getImagePath());
		}else{
			json.put("imagePath", "");
		}
		if (StringUtils.isNotBlank(getContent())) {
			json.put("content", getContent());
		}else{
			json.put("content", "");
		}
		if (getSize()!=null) {
			json.put("size", getSize());
		}else{
			json.put("size", "");
		}
		if (StringUtils.isNotBlank(getColor())) {
			json.put("color", getColor());
		}else{
			json.put("color", "");
		}
		if (getAlpha()!=null) {
			json.put("alpha", getAlpha());
		}else{
			json.put("alpha", "");
		}
		if (getPos()!=null) {
			json.put("pos", getPos());
		}else{
			json.put("pos", "");
		}
		if (getOffsetX()!=null) {
			json.put("offsetX", getOffsetX());
		}else{
			json.put("offsetX", "");
		}
		if (getOffsetY()!=null) {
			json.put("offsetY", getOffsetY());
		}else{
			json.put("offsetY", "");
		}
		return json;
	
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MarkConfig () {
		super();
	}

	/**
	 * Constructor for required fields
	 */
	public MarkConfig (
		java.lang.Boolean on,
		java.lang.Integer minWidth,
		java.lang.Integer minHeight,
		java.lang.String content,
		java.lang.Integer size,
		java.lang.String color,
		java.lang.Integer alpha,
		java.lang.Integer pos,
		java.lang.Integer offsetX,
		java.lang.Integer offsetY) {

		super (
			on,
			minWidth,
			minHeight,
			content,
			size,
			color,
			alpha,
			pos,
			offsetX,
			offsetY);
	}

/*[CONSTRUCTOR MARKER END]*/


}