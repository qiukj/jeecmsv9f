package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsAcquisition;
import com.jeecms.common.util.DateUtils;

public class CmsAcquisition extends BaseCmsAcquisition {
	private static final long serialVersionUID = 1L;
	/**
	 * 动态页翻页页号
	 */
	public static final String PAGE = "[page]";
	/**
	 * 停止状态
	 */
	public static final int STOP = 0;
	/**
	 * 采集状态
	 */
	public static final int START = 1;
	/**
	 * 暂停状态
	 */
	public static final int PAUSE = 2;

	public static enum AcquisitionResultType {
		SUCCESS, TITLESTARTNOTFOUND, TITLEENDNOTFOUND, CONTENTSTARTNOTFOUND, CONTENTENDNOTFOUND,VIEWSTARTNOTFOUND,VIEWENDNOTFOUND,AUTHORSTARTNOTFOUND,AUTHORENDNOTFOUND,ORIGINSTARTNOTFOUND,ORIGINENDNOTFOUND,DESCRISTARTNOTFOUND,DESCRIENDNOTFOUND, RELEASESTARTNOTFOUND,RELEASEENDNOTFOUND,VIEWIDSTARTNOTFOUND,VIEWIDENDNOTFOUND,TITLEEXIST, URLEXIST, UNKNOW
	}
	
	public static enum AcquisitionRepeatCheckType{
		NONE, TITLE, URL
	}

	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (getStartTime()!=null) {
			json.put("startTime", DateUtils.parseDateToTimeStr(getStartTime()));
		}else{
			json.put("startTime", "");
		}
		if (getEndTime()!=null) {
			json.put("endTime", DateUtils.parseDateToTimeStr(getEndTime()));
		}else{
			json.put("endTime", "");
		}
		if (getStatus()!=null) {
			json.put("status", getStatus());
		}else{
			json.put("status", "");
		}
		if (getCurrNum()!=null) {
			json.put("currNum", getCurrNum());
		}else{
			json.put("currNum", "");
		}
		if (getCurrItem()!=null) {
			json.put("currItem", getCurrItem());
		}else{
			json.put("currItem", "");
		}
		if (getTotalItem()!=null) {
			json.put("totalItem", getTotalItem());
		}else{
			json.put("totalItem", "");
		}
		if (getPauseTime()!=null) {
			json.put("pauseTime", getPauseTime());
		}else{
			json.put("pauseTime", "");
		}
		if (getImgAcqu()!=null) {
			json.put("imgAcqu", getImgAcqu());
		}else{
			json.put("imgAcqu", "");
		}
		if (StringUtils.isNotBlank(getPageEncoding())) {
			json.put("pageEncoding", getPageEncoding());
		}else{
			json.put("pageEncoding", "");
		}
		if (StringUtils.isNotBlank(getPlanList())) {
			json.put("planList", getPlanList());
		}else{
			json.put("planList", "");
		}
		if (StringUtils.isNotBlank(getDynamicAddr())) {
			json.put("dynamicAddr", getDynamicAddr());
		}else{
			json.put("dynamicAddr", "");
		}
		if (getDynamicStart()!=null) {
			json.put("dynamicStart", getDynamicStart());
		}else{
			json.put("dynamicStart", "");
		}
		if (getDynamicEnd()!=null) {
			json.put("dynamicEnd", getDynamicEnd());
		}else{
			json.put("dynamicEnd", "");
		}
		if (StringUtils.isNotBlank(getLinksetStart())) {
			json.put("linksetStart", getLinksetStart());
		}else{
			json.put("linksetStart", "");
		}
		if (StringUtils.isNotBlank(getLinksetEnd())) {
			json.put("linksetEnd", getLinksetEnd());
		}else{
			json.put("linksetEnd", "");
		}
		if (StringUtils.isNotBlank(getLinkStart())) {
			json.put("linkStart", getLinkStart());
		}else{
			json.put("linkStart", "");
		}
		if (StringUtils.isNotBlank(getLinkEnd())) {
			json.put("linkEnd", getLinkEnd());
		}else{
			json.put("linkEnd", "");
		}
		if (StringUtils.isNotBlank(getTitleStart())) {
			json.put("titleStart", getTitleStart());
		}else{
			json.put("titleStart", "");
		}
		if (StringUtils.isNotBlank(getTitleEnd())) {
			json.put("titleEnd", getTitleEnd());
		}else{
			json.put("titleEnd", "");
		}
		if (StringUtils.isNotBlank(getKeywordsStart())) {
			json.put("keywordsStart", getKeywordsStart());
		}else{
			json.put("keywordsStart", "");
		}
		if (StringUtils.isNotBlank(getKeywordsEnd())) {
			json.put("keywordsEnd", getKeywordsEnd());
		}else{
			json.put("keywordsEnd", "");
		}
		if (StringUtils.isNotBlank(getDescriptionStart())) {
			json.put("descriptionStart", getDescriptionStart());
		}else{
			json.put("descriptionStart", "");
		}
		if (StringUtils.isNotBlank(getDescriptionEnd())) {
			json.put("descriptionEnd", getDescriptionEnd());
		}else{
			json.put("descriptionEnd", "");
		}
		if (StringUtils.isNotBlank(getContentStart())) {
			json.put("contentStart", getContentStart());
		}else{
			json.put("contentStart", "");
		}
		if (StringUtils.isNotBlank(getContentEnd())) {
			json.put("contentEnd", getContentEnd());
		}else{
			json.put("contentEnd", "");
		}
		if (StringUtils.isNotBlank(getPaginationStart())) {
			json.put("paginationStart", getPaginationStart());
		}else{
			json.put("paginationStart", "");
		}
		if (StringUtils.isNotBlank(getPaginationEnd())) {
			json.put("paginationEnd", getPaginationEnd());
		}else{
			json.put("paginationEnd", "");
		}
		if (StringUtils.isNotBlank(getViewStart())) {
			json.put("viewStart", getViewStart());
		}else{
			json.put("viewStart", "");
		}
		if (StringUtils.isNotBlank(getViewEnd())) {
			json.put("viewEnd", getViewEnd());
		}else{
			json.put("viewEnd", "");
		}
		if (StringUtils.isNotBlank(getViewIdStart())) {
			json.put("viewIdStart", getViewIdStart());
		}else{
			json.put("viewIdStart", "");
		}
		if (StringUtils.isNotBlank(getViewIdEnd())) {
			json.put("viewIdEnd", getViewIdEnd());
		}else{
			json.put("viewIdEnd", "");
		}
		if (StringUtils.isNotBlank(getViewLink())) {
			json.put("viewLink", getViewLink());
		}else{
			json.put("viewLink", "");
		}
		if (StringUtils.isNotBlank(getReleaseTimeStart())) {
			json.put("releaseTimeStart", getReleaseTimeStart());
		}else{
			json.put("releaseTimeStart", "");
		}
		if (StringUtils.isNotBlank(getReleaseTimeEnd())) {
			json.put("releaseTimeEnd", getReleaseTimeEnd());
		}else{
			json.put("releaseTimeEnd", "");
		}
		if (StringUtils.isNotBlank(getReleaseTimeFormat())) {
			json.put("releaseTimeFormat", getReleaseTimeFormat());
		}else{
			json.put("releaseTimeFormat", "");
		}
		if (StringUtils.isNotBlank(getAuthorStart())) {
			json.put("authorStart", getAuthorStart());
		}else{
			json.put("authorStart", "");
		}
		if (StringUtils.isNotBlank(getAuthorEnd())) {
			json.put("authorEnd", getAuthorEnd());
		}else{
			json.put("authorEnd", "");
		}
		if (StringUtils.isNotBlank(getOriginStart())) {
			json.put("originStart", getOriginStart());
		}else{
			json.put("originStart", "");
		}
		if (StringUtils.isNotBlank(getOriginEnd())) {
			json.put("originEnd", getOriginEnd());
		}else{
			json.put("originEnd", "");
		}
		if (StringUtils.isNotBlank(getContentPrefix())) {
			json.put("contentPrefix", getContentPrefix());
		}else{
			json.put("contentPrefix", "");
		}
		if (StringUtils.isNotBlank(getImgPrefix())) {
			json.put("imgPrefix", getImgPrefix());
		}else{
			json.put("imgPrefix", "");
		}
		if (getQueue()!=null) {
			json.put("queue", getQueue());
		}else{
			json.put("queue", "");
		}
		if (StringUtils.isNotBlank(getOriginAppoint())) {
			json.put("originAppoint", getOriginAppoint());
		}else{
			json.put("originAppoint", "");
		}
		if (getType()!=null&& getType().getId()!=null) {
			json.put("typeId", getType().getId());
		}else{
			json.put("typeId", "");
		}
		if (getChannel()!=null && getChannel().getId()!=null) {
			json.put("channelId", getChannel().getId());
		}else{
			json.put("channelId", "");
		}
		return json;
	}
	
	/**
	 * 是否停止
	 * 
	 * @return
	 */
	public boolean isStop() {
		int status = getStatus();
		return status == 0;
	}

	/**
	 * 是否暂停
	 * 
	 * @return
	 */
	public boolean isPuase() {
		int status = getStatus();
		return status == 2;
	}

	/**
	 * 是否中断。停止和暂停都需要中断。
	 * 
	 * @return
	 */
	public boolean isBreak() {
		int status = getStatus();
		return status == 0 || status == 2;
	}

	public String[] getPlans() {
		String plan = getPlanList();
		if (!StringUtils.isBlank(plan)) {
			return StringUtils.split(plan);
		} else {
			return new String[0];
		}
	}

	public String[] getAllPlans() {
		String[] plans = getPlans();
		Integer start = getDynamicStart();
		Integer end = getDynamicEnd();
		if (!StringUtils.isBlank(getDynamicAddr()) && start != null
				&& end != null && start >= 0 && end >= start) {
			int plansLen = plans.length;
			String[] allPlans = new String[plansLen + end - start + 1];
			for (int i = 0; i < plansLen; i++) {
				allPlans[i] = plans[i];
			}
			for (int i = 0, len = end - start + 1; i < len; i++) {
				allPlans[plansLen + i] = getDynamicAddrByNum(start + i);
			}
			return allPlans;
		} else {
			return plans;
		}
	}

	public String getDynamicAddrByNum(int num) {
		return StringUtils.replace(getDynamicAddr(), PAGE, String.valueOf(num));
	}

	public int getTotalNum() {
		int count = 0;
		Integer start = getDynamicStart();
		Integer end = getDynamicEnd();
		if (!StringUtils.isBlank(getDynamicAddr()) && start != null
				&& end != null) {
			count = end - start + 1;
		}
		if (!StringUtils.isBlank(getPlanList())) {
			count += getPlans().length;
		}
		return count;
	}

	public void init() {
		if (getStatus() == null) {
			setStatus(STOP);
		}
		if (getCurrNum() == null) {
			setCurrNum(0);
		}
		if (getCurrItem() == null) {
			setCurrItem(0);
		}
		if (getTotalItem() == null) {
			setTotalItem(0);
		}
		if (getPauseTime() == null) {
			setPauseTime(0);
		}
		if(getQueue()==null){
			setQueue(0);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsAcquisition () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsAcquisition (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsAcquisition (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsUser user,
		com.jeecms.cms.entity.main.ContentType type,
		com.jeecms.core.entity.CmsSite site,
		com.jeecms.cms.entity.main.Channel channel,
		java.lang.String name,
		java.lang.Integer status,
		java.lang.Integer currNum,
		java.lang.Integer currItem,
		java.lang.Integer totalItem,
		java.lang.Integer pauseTime,
		java.lang.String pageEncoding,
		java.lang.Integer queue) {

		super (
			id,
			user,
			type,
			site,
			channel,
			name,
			status,
			currNum,
			currItem,
			totalItem,
			pauseTime,
			pageEncoding,
			queue);
	}

	/* [CONSTRUCTOR MARKER END] */

}