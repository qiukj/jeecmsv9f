package com.jeecms.cms.action.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.jeecms.cms.entity.assist.CmsVoteItem;
import com.jeecms.cms.entity.assist.CmsVoteTopic;
import com.jeecms.cms.manager.assist.CmsSensitivityMng;
import com.jeecms.cms.manager.assist.CmsVoteRecordMng;
import com.jeecms.cms.manager.assist.CmsVoteSubTopicMng;
import com.jeecms.cms.manager.assist.CmsVoteTopicMng;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.entity.CmsUser;

public abstract class AbstractVote {
	/**
	 * 投票cookie前缀
	 */
	public static final String VOTE_COOKIE_PREFIX = "_vote_cookie_";
	
	//投票ID不能为空
	public static final int VOTE_STATUS_ID_NULL = 1;
	//投票项不能为空
	public static final int VOTE_STATUS_ITEM_NULL = 2;
	//投票主题不存在
	public static final int VOTE_STATUS_NOT_FOUND = 100;
	//投票项不合法
	public static final int VOTE_STATUS_ITEM_ILLEGAL = 101;
	//需要登录才能投票
	public static final int VOTE_STATUS_NEED_LOGIN = 501;
	//投票主题已经关闭
	public static final int VOTE_STATUS_CLOSED =200;
	//投票还没有开始
	public static final int VOTE_STATUS_NOT_BEGIN =202;
	//投票已经结束
	public static final int VOTE_STATUS_ENDED =203;
	//规定时间内，同一会员不能重复投票
	public static final int VOTE_STATUS_LIMIT_USER_REPEAT =204;
	//规定时间内，同一IP不能重复投票
	public static final int VOTE_STATUS_LIMIT_IP_REPEAT  =205;
	//规定时间内，同一COOKIE不能重复投票
	public static final int VOTE_STATUS_LIMIT_COOKIE_REPEAT  =206;
	//回复内容含有敏感词
	public static final int VOTE_STATUS_HAS_SENSITIVE = 10;
	
	protected CmsVoteTopic vote(CmsUser user,
			Integer voteId,Integer[]subIds,String[]reply, 
			HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		String ip = RequestUtils.getIpAddr(request);
		String cookieName = VOTE_COOKIE_PREFIX + voteId;
		Cookie cookie = CookieUtils.getCookie(request, cookieName);
		String cookieValue;
		if (cookie != null && !StringUtils.isBlank(cookie.getValue())) {
			cookieValue = cookie.getValue();
		} else {
			cookieValue = null;
		}
		List<Integer[]>itemIds=getItemIdsParam(request, subIds);
		Integer[]subTxtIds = null;
		if(reply!=null&&reply.length>0){
			subTxtIds=new Integer[reply.length];
			List<Integer>subTxtIdList=new ArrayList<Integer>();
       		for(int i=0;i<itemIds.size();i++){
       			if(itemIds.get(i)==null){
       				subTxtIdList.add(subIds[i]);
       			}
       		}
			//投票文本选项和题目id数组对应相同大小
       		subTxtIds=(Integer[]) subTxtIdList.toArray(subTxtIds);
		}
		CmsVoteTopic vote=null;
		if (!validateSubmit(voteId,subIds,itemIds, reply,user, ip, cookieValue, model)) {
			if (cookieValue == null) {
				// 随机cookie
				cookieValue = StringUtils.remove(UUID.randomUUID().toString(),
						"-");
				// 写cookie
				CookieUtils.addCookie(request, response, cookieName,
						cookieValue, Integer.MAX_VALUE, null);
			}
			vote= cmsVoteTopicMng.vote(voteId,subTxtIds,itemIds,reply,
					user, ip,cookieValue);
			model.addAttribute("status", 0);
			model.addAttribute("vote", vote);
		}
		return vote;
	}
	
	protected CmsVoteTopic voteByApi(CmsUser user,
			Integer voteId,Integer[]subIds,
			List<Integer[]>itemIds,Integer[]subTxtIds,
			String[]reply, HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		String ip = RequestUtils.getIpAddr(request);
		String cookieName = VOTE_COOKIE_PREFIX + voteId;
		Cookie cookie = CookieUtils.getCookie(request, cookieName);
		String cookieValue;
		if (cookie != null && !StringUtils.isBlank(cookie.getValue())) {
			cookieValue = cookie.getValue();
		} else {
			cookieValue = null;
		}
		CmsVoteTopic vote=null;
		if (!validateSubmit(voteId,subIds,itemIds,reply, user, ip, cookieValue, model)) {
			if (cookieValue == null) {
				// 随机cookie
				cookieValue = StringUtils.remove(UUID.randomUUID().toString(),
						"-");
				// 写cookie
				CookieUtils.addCookie(request, response, cookieName,
						cookieValue, Integer.MAX_VALUE, null);
			}
			vote= cmsVoteTopicMng.vote(voteId,subTxtIds,itemIds,reply,
					user, ip,cookieValue);
			model.addAttribute("status", 0);
			model.addAttribute("vote", vote);
		}
		return vote;
	}
	
	protected List<Integer[]> getItemIdsParam(HttpServletRequest request,
			Integer[] subIds){
		List<Integer[]>itemIds=new ArrayList<Integer[]>();
		for(Integer subId:subIds){
			itemIds.add(getSubItemIdsParam(request, subId));
		}
		return itemIds;
	}
	
	protected Integer[] getSubItemIdsParam(HttpServletRequest request,Integer subId){
		String[] paramValues=request.getParameterValues("itemIds"+subId);
		return com.jeecms.common.util.ArrayUtils.convertStrArrayToInt(paramValues);
	}
	

	protected boolean validateSubmit(Integer topicId,Integer[]subIds, 
			List<Integer[]>itemIds,String[]replys,
			CmsUser user, String ip, String cookie, ModelMap model) {
		// 投票ID不能为空
		if (topicId == null) {
			model.addAttribute("status", VOTE_STATUS_ID_NULL);
			return true;
		}
		// 投票项不能为空
		if (subIds==null||subIds.length<=0||itemIds == null || itemIds.size() <= 0) {
			model.addAttribute("status", VOTE_STATUS_ITEM_NULL);
			return true;
		}
		// 非文本选项 投票项不能为空
		if(itemIds.size()==subIds.length){
			for(int i=0;i<subIds.length;i++){
				if(!cmsVoteSubTopicMng.findById(subIds[i]).getIsText()){
					if(itemIds.get(i)==null){
						model.addAttribute("status", VOTE_STATUS_ITEM_NULL);
						return true;
					}
				}
			}
		}else{
			model.addAttribute("status", VOTE_STATUS_ITEM_NULL);
			return true;
		}
		boolean hasSensitive=cmsSensitivityMng.haveSensitivity(replys);
		if(hasSensitive){
			model.addAttribute("status", VOTE_STATUS_HAS_SENSITIVE);
			return true;
		}
		CmsVoteTopic topic = cmsVoteTopicMng.findById(topicId);
		// 投票主题不存在
		if (topic == null) {
			model.addAttribute("status", VOTE_STATUS_NOT_FOUND);
			return true;
		}
		// 投票项不合法
		List<Integer>itemTotalIds=new ArrayList<Integer>();
		for(Integer[]ids:itemIds){
			if(ids!=null&&ids.length>0){
				for(Integer id:ids){
					itemTotalIds.add(id);
				}
			}
		}
		boolean contains;
		for (Integer itemId : itemTotalIds) {
			contains = false;
			for (CmsVoteItem item : topic.getItems()) {
				if (item.getId().equals(itemId)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				model.addAttribute("status", VOTE_STATUS_ITEM_ILLEGAL);
				return true;
			}
		}

		// 需要登录才能投票
		if (topic.getRestrictMember() && user == null) {
			model.addAttribute("status", VOTE_STATUS_NEED_LOGIN);
			return true;
		}

		// 投票主题已经关闭
		if (topic.getDisabled()) {
			model.addAttribute("status", VOTE_STATUS_CLOSED);
			return true;
		}
		/*多题目取消下面限制
		// 投票的选项个数大于允许的个数
		if (itemIds.length > topic.getMultiSelect()) {
			model.addAttribute("status", 201);
			return true;
		}
		*/
		long now = System.currentTimeMillis();
		// 投票还没有开始
		Date start = topic.getStartTime();
		if (start != null && now < start.getTime()) {
			model.addAttribute("status", VOTE_STATUS_NOT_BEGIN);
			model.addAttribute("startTime", start);
			return true;
		}
		// 投票已经结束
		Date end = topic.getEndTime();
		if (end != null && now > end.getTime()) {
			model.addAttribute("status", VOTE_STATUS_ENDED);
			model.addAttribute("endTime", end);
			return true;
		}
		Integer hour = topic.getRepeateHour();
		if (hour == null || hour > 0) {
			Date vtime;
			// 规定时间内，同一会员不能重复投票
			if (topic.getRestrictMember()) {
				vtime = cmsVoteRecordMng.lastVoteTimeByUserId(user.getId(),
						topicId);
				if ((hour == null&&vtime!=null)||(hour != null&&vtime.getTime() + hour * 60 * 60 * 1000 > now)) {
					model.addAttribute("status", VOTE_STATUS_LIMIT_USER_REPEAT);
					return true;
				}
			}
			// 规定时间内，同一IP不能重复投票
			if (topic.getRestrictIp()) {
				vtime = cmsVoteRecordMng.lastVoteTimeByIp(ip, topicId);
				if ((hour == null&&vtime!=null)||(hour != null&&vtime.getTime() + hour * 60 * 60 * 1000 > now)) {
					model.addAttribute("status", VOTE_STATUS_LIMIT_IP_REPEAT);
					return true;
				}
			}
			// 规定时间内，同一COOKIE不能重复投票
			if (topic.getRestrictCookie() && cookie != null) {
				vtime = cmsVoteRecordMng.lastVoteTimeByCookie(cookie, topicId);
				if ((hour == null&&vtime!=null)||(hour != null&&vtime.getTime() + hour * 60 * 60 * 1000 > now)) {
					model.addAttribute("status", VOTE_STATUS_LIMIT_COOKIE_REPEAT);
					return true;
				}
			}
		}
		return false;
	}
	@Autowired
	protected CmsVoteTopicMng cmsVoteTopicMng;
	@Autowired
	protected CmsVoteSubTopicMng cmsVoteSubTopicMng;
	@Autowired
	protected CmsVoteRecordMng cmsVoteRecordMng;
	@Autowired
	protected CmsSensitivityMng cmsSensitivityMng;
}
