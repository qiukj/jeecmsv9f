package com.jeecms.cms.api.admin.assist;


import static com.jeecms.cms.statistic.CmsStatistic.SITEID;
import static com.jeecms.cms.statistic.CmsStatistic.STATUS;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.statistic.CmsStatistic;
import com.jeecms.cms.statistic.CmsStatisticSvc;
import com.jeecms.cms.statistic.CmsStatistic.TimeRange;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.util.CmsUtils;
/**
 * 全局数据统计
 * @author tom
 * @date 2017年11月27日
 */
@Controller
public class CmsGolbalStatisticApiAct {
	
	/**
	 * @param request
	 * @param response
	 */
	@RequestMapping("/global/statistic")
	public void statistic(
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		Map<String, Object> restrictions = new HashMap<String, Object>();
		CmsSite site = CmsUtils.getSite(request);
		restrictions.put(SITEID, site.getId());
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		Integer contentTotal=site.getContentTotal();
		Integer commentTotal=site.getCommentTotal();
		Integer guestbookTotal=site.getGuestbookTotal();
		Integer memberTotal=site.getMemberTotal();
		Runtime runtime = Runtime.getRuntime();
		CmsUser user=CmsUtils.getUser(request);
		long freeMemoery = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long usedMemory = totalMemory - freeMemoery;
		long maxMemory = runtime.maxMemory();
		long useableMemory = maxMemory - totalMemory + freeMemoery;
		long memberToday=0;
		long contentDayUncheckCount=0;
		long contentDayTotalCount=0;
		long commentDayTotalCount=0;
		long commentDayUncheckCount=0;
		long guestbookDayTotalCount=0;
		long guestbookDayUncheckTotalCount=0;
		contentDayTotalCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, dayTimeRange, restrictions);
		restrictions.put(STATUS, ContentCheck.CHECKING);
		contentDayUncheckCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, dayTimeRange, restrictions);
		restrictions.put(STATUS, null);
		commentDayTotalCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, dayTimeRange, restrictions);
		restrictions.put(STATUS, (short)0);
		commentDayUncheckCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, dayTimeRange, restrictions);
		restrictions.put(STATUS, null);
		guestbookDayTotalCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, dayTimeRange, restrictions);
		restrictions.put(STATUS, (short)0);
		guestbookDayUncheckTotalCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, dayTimeRange, restrictions);
		memberToday=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, dayTimeRange, null);
		JSONObject json=new JSONObject();
		json.put("contentTotal", contentTotal);
		json.put("contentDayTotalCount", contentDayTotalCount);
		json.put("contentDayUncheckCount", contentDayUncheckCount);
		json.put("commentDayTotalCount", commentDayTotalCount);
		json.put("commentDayUncheckCount", commentDayUncheckCount);
		json.put("commentTotal", commentTotal);
		json.put("guestbookDayTotalCount", guestbookDayTotalCount);
		json.put("guestbookDayUncheckTotalCount", guestbookDayUncheckTotalCount);
		json.put("guestbookTotal", guestbookTotal);
		json.put("memberTotal", memberTotal);
		json.put("memberToday", memberToday);
		json.put("usedMemory", StrUtils.retainTwoDecimal(usedMemory/1024/1024.0));
		json.put("maxMemory", StrUtils.retainTwoDecimal(maxMemory/1024/1024.0));
		json.put("useableMemory", StrUtils.retainTwoDecimal(useableMemory/1024/1024.0));
		json.put("lastLoginTime", DateUtils.parseDateToTimeStr(user.getLastLoginTime()));
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private CmsStatisticSvc cmsStatisticSvc;
}
