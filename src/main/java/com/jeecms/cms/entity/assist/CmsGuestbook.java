package com.jeecms.cms.entity.assist;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsGuestbook;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;

public class CmsGuestbook extends BaseCmsGuestbook {
	private static final long serialVersionUID = 1L;

	public String getTitleHtml() {
		return StrUtils.txt2htm(getTitle());
	}

	public String getContentHtml() {
		return StrUtils.txt2htm(getContent());
	}

	public String getReplyHtml() {
		return StrUtils.txt2htm(getReply());
	}

	public String getTitle() {
		CmsGuestbookExt ext = getExt();
		if (ext != null) {
			return ext.getTitle();
		} else {
			return null;
		}
	}

	public String getContent() {
		CmsGuestbookExt ext = getExt();
		if (ext != null) {
			return ext.getContent();
		} else {
			return null;
		}
	}

	public String getReply() {
		CmsGuestbookExt ext = getExt();
		if (ext != null) {
			return ext.getReply();
		} else {
			return null;
		}
	}

	public String getEmail() {
		CmsGuestbookExt ext = getExt();
		if (ext != null) {
			return ext.getEmail();
		} else {
			return null;
		}
	}

	public String getPhone() {
		CmsGuestbookExt ext = getExt();
		if (ext != null) {
			return ext.getPhone();
		} else {
			return null;
		}
	}

	public String getQq() {
		CmsGuestbookExt ext = getExt();
		if (ext != null) {
			return ext.getQq();
		} else {
			return null;
		}
	}

	public void init() {
		if (getChecked() == null) {
			setChecked((short)0);
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
		if(getMember()!=null&&StringUtils.isNotBlank(getMember().getUsername())){
			json.put("memberUsername", getMember().getUsername());
		}else{
			json.put("memberUsername", "");
		}
		if(getMember()!=null&&getMember().getId()!=null){
			json.put("memberId", getMember().getId());
		}else{
			json.put("memberId", "");
		}
		if(getAdmin()!=null&&StringUtils.isNotBlank(getAdmin().getUsername())){
			json.put("adminUsername", getAdmin().getUsername());
		}else{
			json.put("adminUsername", "");
		}
		if(getAdmin()!=null&&getAdmin().getId()!=null){
			json.put("adminId", getAdmin().getId());
		}else{
			json.put("adminId", "");
		}
		if (StringUtils.isNotBlank(getIp())) {
			json.put("ip", getIp());
		}else{
			json.put("ip", "");
		}
		if (getCtg()!=null&&StringUtils.isNotBlank(getCtg().getName())) {
			json.put("ctgName", getCtg().getName());
		}else{
			json.put("ctgName", "");
		}
		if (getCtg()!=null&&getCtg().getId()!=null) {
			json.put("ctgId", getCtg().getId());
		}else{
			json.put("ctgId", "");
		}
		if (getContent()!=null) {
			json.put("content", getContent());
		}else{
			json.put("content", "");
		}
		if (getReply()!=null) {
			json.put("reply", getReply());
		}else{
			json.put("reply", "");
		}
		if (StringUtils.isNotBlank(getEmail())) {
			json.put("email", getEmail());
		}else{
			json.put("email", "");
		}
		if (StringUtils.isNotBlank(getPhone())) {
			json.put("phone", getPhone());
		}else{
			json.put("phone", "");
		}
		if (StringUtils.isNotBlank(getQq())) {
			json.put("qq", getQq());
		}else{
			json.put("qq", "");
		}
		return json;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsGuestbook () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsGuestbook (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsGuestbook (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		com.jeecms.cms.entity.assist.CmsGuestbookCtg ctg,
		java.lang.String ip,
		java.util.Date createTime,
		java.lang.Short checked,
		java.lang.Boolean recommend) {

		super (
			id,
			site,
			ctg,
			ip,
			createTime,
			checked,
			recommend);
	}

	/* [CONSTRUCTOR MARKER END] */

}