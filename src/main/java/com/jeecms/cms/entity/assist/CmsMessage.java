package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsMessage;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;



public class CmsMessage extends BaseCmsMessage {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getMsgTitle())) {
			json.put("msgTitle", getMsgTitle());
		}else{
			json.put("msgTitle", "");
		}
		if (StringUtils.isNotBlank(getMsgContent())) {
			json.put("msgContent", getMsgContent());
		}else{
			json.put("msgContent", "");
		}
		if (getSendTime()!=null) {
			json.put("sendTime", DateUtils.parseDateToTimeStr(getSendTime()));
		}else{
			json.put("sendTime", "");
		}
		if (getMsgStatus()!=null) {
			json.put("msgStatus", getMsgStatus());
		}else{
			json.put("msgStatus", "");
		}
		if (getMsgBox()!=null) {
			json.put("msgBox", getMsgBox());
		}else {
			json.put("msgBox", "");
		}
		if(getMsgReceiverUser()!=null&&StringUtils.isNotBlank(getMsgReceiverUser().getUsername())){
			json.put("msgReceiverUserName", getMsgReceiverUser().getUsername());
		}else{
			json.put("msgReceiverUserName", "");
		}
		if (getMsgReceiverUser()!=null&&getMsgReceiverUser().getId()!=null) {
			json.put("msgReceiverId", getMsgReceiverUser().getId());
		}else{
			json.put("msgReceiverId", "");
		}
		if(getMsgSendUser()!=null&&StringUtils.isNotBlank(getMsgSendUser().getUsername())){
			json.put("msgSendUserUserName", getMsgSendUser().getUsername());
		}else{
			json.put("msgSendUserUserName", "");
		}
		if(getMsgSendUser()!=null&&getMsgSendUser().getId()!=null){
			json.put("msgSendUserId", getMsgSendUser().getId());
		}else{
			json.put("msgSendUserId", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsMessage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsMessage (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsMessage (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsUser msgReceiverUser,
		com.jeecms.core.entity.CmsUser msgSendUser,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String msgTitle,
		java.lang.Boolean msgStatus,
		java.lang.Integer msgBox) {

		super (
			id,
			msgReceiverUser,
			msgSendUser,
			site,
			msgTitle,
			msgStatus,
			msgBox);
	}
	public String getTitleHtml() {
		return StrUtils.txt2htm(getMsgTitle());
	}
	public String getContentHtml() {
		return StrUtils.txt2htm(getMsgContent());
	}


/*[CONSTRUCTOR MARKER END]*/


}