package com.jeecms.cms.entity.assist;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsComment;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;

public class CmsComment extends BaseCmsComment {
	private static final long serialVersionUID = 1L;

	public String getText() {
		return getCommentExt().getText();
	}

	public String getTextHtml() {
		return StrUtils.txt2htm(getText());
	}

	public String getReply() {
		return getCommentExt().getReply();
	}

	public String getReplayHtml() {
		return StrUtils.txt2htm(getReply());
	}

	public String getIp() {
		return getCommentExt().getIp();
	}

	public void init() {
		short zero = 0;
		if (getDowns() == null) {
			setDowns(zero);
		}
		if (getUps() == null) {
			setUps(zero);
		}
		if(getReplyCount()==null){
			setReplyCount(0);
		}
		if (getChecked() == null) {
			setChecked((short) 0);
		}
		if (getRecommend() == null) {
			setRecommend(false);
		}
		if (getCreateTime() == null) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
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
		if (getCreateTime()!=null) {
			json.put("createTime", DateUtils.parseDateToTimeStr(getCreateTime()));
		}else{
			json.put("createTime", "");
		}
		if(getReplayTime()!=null){
			json.put("replayTime",DateUtils.parseDateToTimeStr(getReplayTime()));
		}else{
			json.put("replayTime", "");
		}
		if (getUps()!=null) {
			json.put("ups", getUps());
		}else{
			json.put("ups", "");
		}
		if (getDowns()!=null) {
			json.put("downs", getDowns());
		}else{
			json.put("downs", "");
		}
		if (getRecommend()!=null) {
			json.put("recommend", getRecommend());
		}else{
			json.put("recommend", "");
		}
		if (getChecked()!=null) {
			json.put("checked", getChecked());
		}else{
			json.put("checked", "");
		}
		if (getReplyCount()!=null) {
			json.put("replyCount", getReplyCount());
		}else{
			json.put("replyCount", "");
		}
		if (getReplayUser()!=null&&StringUtils.isNotBlank(getReplayUser().getUsername())) {
			json.put("replayerUsername", getReplayUser().getUsername());
		}else{
			json.put("replayerUsername", "");
		}
		if (getReplayUser()!=null&&getReplayUser().getId()!=null) {
			json.put("replayerId", getReplayUser().getId());
		}else{
			json.put("replayerId", "");
		}
		if(getCommentUser()!=null&&getCommentUser().getId()!=null){
			json.put("commenterId", getCommentUser().getId());
		}else{
			json.put("commenterId", "");
		}
		if(getCommentUser()!=null&&StringUtils.isNotBlank(getCommentUser().getUsername())){
			json.put("commenterUsername", getCommentUser().getUsername());
		}else{
			json.put("commenterUsername", "");
		}
		if (getCommentExt()!=null&&StringUtils.isNotBlank(getCommentExt().getIp())) {
			json.put("ip", getIp());
		}else{
			json.put("ip", "");
		}
		if (getCommentExt()!=null&&StringUtils.isNotBlank(getCommentExt().getText())) {
			json.put("text", getText());
		}else{
			json.put("text", "");
		}
		if (getCommentExt()!=null&&StringUtils.isNotBlank(getCommentExt().getReply())) {
			json.put("reply", getReply());
		}else{
			json.put("reply", "");
		}
		if (getScore()!=null) {
			json.put("score", getScore());
		}else{
			json.put("score", "");
		}
		if(getParent()!=null&&getParent().getId()!=null){
			json.put("parentId", getParent().getId());
		}else{
			json.put("parentId", "");
		}
		if (getContent()!=null&&getContent().getId()!=null) {
			json.put("contentId", getContent().getId());
		}else{
			json.put("contentId", "");
		}
		if (getContent()!=null&&StringUtils.isNotBlank(getContent().getTitle())) {
			json.put("contentTitle", getContent().getTitle());
		}else{
			json.put("contentTitle", "");
		}
		if (getContent()!=null&&getContent().getChannel()!=null&&getContent().getChannel().getId()!=null) {
			json.put("channelId", getContent().getChannel().getId());
		}else{
			json.put("channelId", "");
		}
		if (getContent()!=null&&getContent().getChannel()!=null&&StringUtils.isNotBlank(getContent().getChannel().getName())) {
			json.put("channelName", getContent().getChannel().getName());
		}else{
			json.put("channelName", "");
		}
		if (getContent()!=null&&StringUtils.isNotBlank(getContent().getUrl())) {
			json.put("contentURL", getContent().getUrl());
		}else{
			json.put("contentURL", "");
		}
		return json;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsComment () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsComment (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsComment (
		java.lang.Integer id,
		com.jeecms.cms.entity.main.Content content,
		com.jeecms.core.entity.CmsSite site,
		java.util.Date createTime,
		java.lang.Short ups,
		java.lang.Short downs,
		java.lang.Boolean recommend,
		java.lang.Short checked) {

		super (
			id,
			content,
			site,
			createTime,
			ups,
			downs,
			recommend,
			checked);
	}

	/* [CONSTRUCTOR MARKER END] */

}