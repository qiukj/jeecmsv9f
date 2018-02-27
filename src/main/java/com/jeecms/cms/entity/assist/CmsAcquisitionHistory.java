package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsAcquisitionHistory;



public class CmsAcquisitionHistory extends BaseCmsAcquisitionHistory {
	private static final long serialVersionUID = 1L;

	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getChannelUrl())) {
			json.put("channelUrl", getChannelUrl());
		}else{
			json.put("channelUrl", "");
		}
		if (StringUtils.isNotBlank(getContentUrl())) {
			json.put("contentUrl", getContentUrl());
		}else{
			json.put("contentUrl", "");
		}
		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (getAcquisition()!=null && StringUtils.isNotBlank(getAcquisition().getName())) {
			json.put("acqName", getAcquisition().getName());
		}else{
			json.put("acqName", "");
		}
		if (getAcquisition()!=null && getAcquisition().getChannel()!=null &&
				StringUtils.isNotBlank(getAcquisition().getChannel().getName())) {
			json.put("acqChannelName", getAcquisition().getChannel().getName());
		}else{
			json.put("acqChannelName", "");
		}
		if (getAcquisition()!=null && getAcquisition().getType()!=null &&
				StringUtils.isNotBlank(getAcquisition().getType().getName())) {
			json.put("acqTypeName", getAcquisition().getType().getName());
		}else{
			json.put("acqTypeName", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsAcquisitionHistory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsAcquisitionHistory (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsAcquisitionHistory (
		java.lang.Integer id,
		java.lang.String channelUrl,
		java.lang.String contentUrl) {

		super (
			id,
			channelUrl,
			contentUrl);
	}

/*[CONSTRUCTOR MARKER END]*/


}