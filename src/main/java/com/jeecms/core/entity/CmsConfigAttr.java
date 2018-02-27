package com.jeecms.core.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

public class CmsConfigAttr {
	public CmsConfigAttr() {
	}

	public CmsConfigAttr(Map<String, String> attr) {
		this.attr = attr;
	}

	private Map<String, String> attr;

	public Map<String, String> getAttr() {
		if (attr == null) {
			attr = new HashMap<String, String>();
		}
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}


	public static final String PICTURENEW = "new_picture";
	public static final String DAYNEW = "day";
	public static final String PREVIEW = "preview";
	public static final String QQ_ENABLE = "qqEnable";
	public static final String QQ_ID = "qqID";
	public static final String QQ_KEY = "qqKey";
	public static final String SINA_ENABLE = "sinaEnable";
	public static final String SINA_ID = "sinaID";
	public static final String SINA_KEY = "sinaKey";
	public static final String QQWEBO_ENABLE = "qqWeboEnable";
	public static final String QQWEBO_ID = "qqWeboID";
	public static final String QQWEBO_KEY = "qqWeboKey";
	public static final String WEIXIN_ENABLE = "weixinEnable";
	public static final String WEIXIN_ID = "weixinID";
	public static final String WEIXIN_KEY = "weixinKey";
	public static final String SSO_ENABLE = "ssoEnable";
	public static final String FLOW_SWITCH = "flowSwitch";
	public static final String CONTENT_FRESH_MINUTE = "contentFreshMinute";
	public static final String CODE_IMG_WIDTH = "codeImgWidth";
	public static final String CODE_IMG_HEIGHT = "codeImgHeight";
	
	public static final String WEIXIN_APP_ID = "weixinAppId";
	public static final String WEIXIN_APP_SECRET = "weixinAppSecret";
	public static final String WEIXIN_LOGIN_ID = "weixinLoginId";
	public static final String WEIXIN_LOGIN_SECRET = "weixinLoginSecret";
	
	public static final String COMMENT_OPEN = "commentOpen";
	public static final String GUESTBOOK_OPEN = "guestbookOpen";
	public static final String GUESTBOOK_NEED_LOGIN = "guestbookNeedLogin";
	public static final String GUESTBOOK_DAY_LIMIT = "guestbookDayLimit";
	public static final String COMMENT_DAY_LIMIT = "commentDayLimit";
	
	public String getPictureNew() {
		return getAttr().get(PICTURENEW);
	}
	
	public int getDayNew() {
		String day=getAttr().get(DAYNEW);
		if(StringUtils.isNotBlank(day)){
			return Integer.parseInt(day);
		}else{
			return 0;
		}
	}
	
	public Boolean getCommentOpen() {
		String enable = getAttr().get(COMMENT_OPEN);
		return !"false".equals(enable);
	}
	
	public void setCommentOpen(boolean commentOpen) {
		getAttr().put(COMMENT_OPEN, String.valueOf(commentOpen));
	}
	
	public Boolean getGuestbookOpen() {
		String enable = getAttr().get(GUESTBOOK_OPEN);
		return !"false".equals(enable);
	}
	
	public void setGuestbookOpen(boolean guestBookOpen) {
		getAttr().put(GUESTBOOK_OPEN, String.valueOf(guestBookOpen));
	}
	
	public Boolean getGuestbookNeedLogin() {
		String enable = getAttr().get(GUESTBOOK_NEED_LOGIN);
		return !"false".equals(enable);
	}
	
	public void setGuestbookNeedLogin(boolean guestBookNeedLogin) {
		getAttr().put(GUESTBOOK_NEED_LOGIN, String.valueOf(guestBookNeedLogin));
	}
	
	public Integer getGuestbookDayLimit() {
		String dayLimit= getAttr().get(GUESTBOOK_DAY_LIMIT);
		if(StringUtils.isBlank(dayLimit)){
			return 0;
		}else{
			if(StringUtils.isNumeric(dayLimit)){
				return Integer.valueOf(dayLimit);
			}else{
				return 0;
			}
		}
	}
	
	public void setGuestbookDayLimit(Integer guestBookDayLimit) {
		getAttr().put(GUESTBOOK_DAY_LIMIT, guestBookDayLimit.toString());
	}
	
	public Integer getCommentDayLimit() {
		String dayLimit= getAttr().get(COMMENT_DAY_LIMIT);
		if(StringUtils.isBlank(dayLimit)){
			return 0;
		}else{
			if(StringUtils.isNumeric(dayLimit)){
				return Integer.valueOf(dayLimit);
			}else{
				return 0;
			}
		}
	}
	
	public void setCommentDayLimit(Integer commentDayLimit) {
		getAttr().put(COMMENT_DAY_LIMIT, commentDayLimit.toString());
	}
	
	public Boolean getSsoEnable() {
		String enable = getAttr().get(SSO_ENABLE);
		return !"false".equals(enable);
	}
	
	public Boolean getFlowSwitch() {
		String flowSwitch = getAttr().get(FLOW_SWITCH);
		return !"false".equals(flowSwitch);
	}
	
	
	public void setPictureNew(String path) {
		getAttr().put(PICTURENEW, path);
	}
	
	public void setDayNew(Integer day) {
		getAttr().put(DAYNEW, day.toString());
	}
	
	public Boolean getPreview() {
		String preview = getAttr().get(PREVIEW);
		return !"false".equals(preview);
	}

	/**
	 * 设置是否开启内容预览
	 * 
	 * @param preview
	 */
	public void setPreview(boolean preview) {
		getAttr().put(PREVIEW, String.valueOf(preview));
	}
	
	public Boolean getQqEnable() {
		String enable = getAttr().get(QQ_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getQqID() {
		return getAttr().get(QQ_ID);
	}
	
	public String getQqKey() {
		return getAttr().get(QQ_KEY);
	}
	
	public Boolean getSinaEnable() {
		String enable = getAttr().get(SINA_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getSinaID() {
		return getAttr().get(SINA_ID);
	}
	
	public String getSinaKey() {
		return getAttr().get(SINA_KEY);
	}
	
	public Boolean getQqWeboEnable() {
		String enable = getAttr().get(QQWEBO_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getQqWeboID() {
		return getAttr().get(QQWEBO_ID);
	}
	
	public String getQqWeboKey() {
		return getAttr().get(QQWEBO_KEY);
	}
	
	public Boolean getWeixinEnable() {
		String enable = getAttr().get(WEIXIN_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getWeixinID() {
		return getAttr().get(WEIXIN_ID);
	}
	
	public Integer getContentFreshMinute() {
		return Integer.parseInt(getAttr().get(CONTENT_FRESH_MINUTE));
	}
	
	public String getWeixinKey() {
		return getAttr().get(WEIXIN_KEY);
	}
	
	public void setQqEnable(boolean enable) {
		getAttr().put(QQ_ENABLE, String.valueOf(enable));
	}
	
	public void setQqID(String id) {
		getAttr().put(QQ_ID, id);
	}
	
	public void setQqKey(String key) {
		getAttr().put(QQ_KEY, key);
	}
	
	
	public void setSinaEnable(boolean enable) {
		getAttr().put(SINA_ENABLE, String.valueOf(enable));
	}
	
	public void setFlowSwitch(boolean flowSwitch) {
		getAttr().put(FLOW_SWITCH, String.valueOf(flowSwitch));
	}
	
	public void setContentFreshMinute(Integer minute) {
		getAttr().put(CONTENT_FRESH_MINUTE, String.valueOf(minute));
	}
	
	public void setSinaID(String id) {
		getAttr().put(SINA_ID,id);
	}
	
	public void setSinaKey(String key) {
		getAttr().put(SINA_KEY,key);
	}
	
	public void setQqWeboEnable(boolean enable) {
		getAttr().put(QQWEBO_ENABLE, String.valueOf(enable));
	}
	
	public void setQqWeboID(String id) {
		getAttr().put(QQWEBO_ID, id);
	}
	
	public void setQqWeboKey(String key) {
		getAttr().put(QQWEBO_KEY, key);
	}
	
	public void setWeixinEnable(boolean enable) {
		getAttr().put(WEIXIN_ENABLE, String.valueOf(enable));
	}
	
	public void setWeixinID(String id) {
		getAttr().put(WEIXIN_ID, id);
	}
	
	public void setWeixinKey(String key) {
		getAttr().put(WEIXIN_KEY, key);
	}
	
	public int getCodeImgWidth() {
		String width=getAttr().get(CODE_IMG_WIDTH);
		if(StringUtils.isNotBlank(width)){
			return Integer.parseInt(width);
		}else{
			return 100;
		}
	}
	
	public void setCodeImgWidth(Integer width) {
		getAttr().put(CODE_IMG_WIDTH, width.toString());
	}
	
	public int getCodeImgHeight() {
		String height=getAttr().get(CODE_IMG_HEIGHT);
		if(StringUtils.isNotBlank(height)){
			return Integer.parseInt(height);
		}else{
			return 100;
		}
	}
	
	public void setCodeImgHeight(Integer height) {
		getAttr().put(CODE_IMG_HEIGHT, height.toString());
	}
	
	public String getWeixinAppId() {
		return getAttr().get(WEIXIN_APP_ID);
	}
	
	public void setWeixinAppId(String weixinAppId) {
		getAttr().put(WEIXIN_APP_ID, weixinAppId);
	}
	public String getWeixinAppSecret() {
		return getAttr().get(WEIXIN_APP_SECRET);
	}
	
	public void setWeixinAppSecret(String weixinAppSecret) {
		getAttr().put(WEIXIN_APP_SECRET, weixinAppSecret);
	}
	
	public String getWeixinLoginId() {
		return getAttr().get(WEIXIN_LOGIN_ID);
	}
	
	public void setWeixinLoginId(String weixinAppId) {
		getAttr().put(WEIXIN_LOGIN_ID, weixinAppId);
	}
	public String getWeixinLoginSecret() {
		return getAttr().get(WEIXIN_LOGIN_SECRET);
	}
	
	public void setWeixinLoginSecret(String weixinAppSecret) {
		getAttr().put(WEIXIN_LOGIN_SECRET, weixinAppSecret);
	}
	
	public JSONObject attrToJson(){
		JSONObject json = new JSONObject();
		json.put("dayNew", getDayNew());
		if (StringUtils.isNotBlank(getPictureNew())) {
			json.put("pictureNew", getPictureNew());
		}else{
			json.put("pictureNew", "");
		}
		if (getPreview()!=null) {
			json.put("preview", getPreview());
		}else{
			json.put("preview", "");
		}
		if (getFlowSwitch()!=null) {
			json.put("flowSwitch", getFlowSwitch());
		}else{
			json.put("flowSwitch", "");
		}
		if (getAttr()!=null) {
			if(getAttr().containsKey("bdToken")){
				json.put("bdToken", getAttr().get("bdToken"));
			}else{
				json.put("bdToken", "");		
			}
			if(getAttr().containsKey("isBdSubmit")){
				json.put("isBdSubmit", getAttr().get("isBdSubmit"));
			}else{
				json.put("isBdSubmit", "false");		
			}
		}else{
			json.put("bdToken", "");	
			json.put("isBdSubmit", "false");	
		}
		json.put("codeImgWidth", getCodeImgWidth());
		json.put("codeImgHeight", getCodeImgHeight());
		if (StringUtils.isNotBlank(getWeixinAppId())) {
			json.put("weixinAppId", getWeixinAppId());
		}else{
			json.put("weixinAppId", "");
		}
		if (StringUtils.isNotBlank(getWeixinAppSecret())) {
			json.put("weixinAppSecret", getWeixinAppSecret());
		}else{
			json.put("weixinAppSecret", "");
		}
		if (StringUtils.isNotBlank(getWeixinLoginId())) {
			json.put("weixinLoginId", getWeixinLoginId());
		}else{
			json.put("weixinLoginId", "");
		}
		if (StringUtils.isNotBlank(getWeixinLoginSecret())) {
			json.put("weixinLoginSecret", getWeixinLoginSecret());
		}else{
			json.put("weixinLoginSecret", "");
		}
		if (getContentFreshMinute()!=null) {
			json.put("contentFreshMinute", getContentFreshMinute());
		}else{
			json.put("contentFreshMinute", "");
		}
		return json;
	}
	
	public JSONObject apiToJson(){
		JSONObject json = new JSONObject();
		if (getQqEnable()!=null) {
			json.put("qqEnable", getQqEnable());
		}else{
			json.put("qqEnable", "");
		}
		if (StringUtils.isNotBlank(getQqID())) {
			json.put("qqID", getQqID());
		}else{
			json.put("qqID", "");
		}
		if (StringUtils.isNotBlank(getQqKey())) {
			json.put("qqKey", getQqKey());
		}else{
			json.put("qqKey", "");
		}
		if (getSinaEnable()!=null) {
			json.put("sinaEnable", getSinaEnable());
		}else{
			json.put("sinaEnable", "");
		}
		if (StringUtils.isNotBlank(getSinaID())) {
			json.put("sinaID", getSinaID());
		}else{
			json.put("sinaID", "");
		}
		if (StringUtils.isNotBlank(getSinaKey())) {
			json.put("sinaKey", getSinaKey());
		}else{
			json.put("sinaKey", "");
		}
		if (getQqWeboEnable()!=null) {
			json.put("qqWeboEnable", getQqWeboEnable());
		}else{
			json.put("qqWeboEnable", "");
		}
		if (StringUtils.isNotBlank(getQqWeboID())) {
			json.put("qqWeboID", getQqWeboID());
		}else{
			json.put("qqWeboID", "");
		}
		if (StringUtils.isNotBlank(getQqWeboKey())) {
			json.put("qqWeboKey", getQqWeboKey());
		}else{
			json.put("qqWeboKey", "");
		}
		return json;
	}
}
