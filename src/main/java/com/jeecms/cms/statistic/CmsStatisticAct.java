package com.jeecms.cms.statistic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.manager.assist.CmsSiteAccessCountMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessPagesMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessStatisticMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.statistic.CmsStatistic.CmsStatisticModel;
import com.jeecms.cms.statistic.CmsStatistic.TimeRange;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.util.CmsUtils;

import static com.jeecms.cms.statistic.CmsStatistic.MEMBER;
import static com.jeecms.cms.statistic.CmsStatistic.CONTENT;
import static com.jeecms.cms.statistic.CmsStatistic.COMMENT;
import static com.jeecms.cms.statistic.CmsStatistic.GUESTBOOK;
import static com.jeecms.cms.statistic.CmsStatistic.SITEID;
import static com.jeecms.cms.statistic.CmsStatistic.STATUS;
import static com.jeecms.cms.statistic.CmsStatistic.ISREPLYED;
import static com.jeecms.cms.statistic.CmsStatistic.USERID;
import static com.jeecms.cms.statistic.CmsStatistic.CHANNELID;

import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_DAY;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_MONTH;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEAR;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEARS;
import static com.jeecms.common.page.SimplePage.cpn;

import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_ALL;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_SOURCE;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_LINK;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_AREA;

@Controller
public class CmsStatisticAct {
	
	@RequiresPermissions("statistic:member:v_list")
	@RequestMapping("/statistic/member/v_list.do")
	public String memberList(String queryModel, Integer year, Integer month,
			Integer day,Date begin,Date end,HttpServletRequest request, ModelMap model) {
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId=CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date weekBegin=DateUtils.getSpecficWeekStart(now, 0);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange weekTimeRange=TimeRange.getInstance(weekBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		TimeRange totalTimeRange=TimeRange.getInstance(null, now);
		if(begin==null){
			begin=DateUtils.getStartDate(monthBegin);
		}else{
			begin=DateUtils.getStartDate(begin);
		}
		if(end==null){
			end=DateUtils.getFinallyDate(now);
		}else{
			end=DateUtils.getFinallyDate(end);
		}
		List<CmsStatistic> list = cmsStatisticSvc.statisticByModel(MEMBER,
				statisticModel, year, month, day,begin,end, null);
		putCommonData(statisticModel, list, year, month, day, model);
		
		long memberDayCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, dayTimeRange, restrictions);
		long memberWeekCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, weekTimeRange, restrictions);
		long memberMonthCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, monthTimeRange, restrictions);
		long memberYearCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, yearTimeRange, restrictions);
		long memberTotalCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, totalTimeRange, restrictions);
		
		List<Object[]> dayMemberList,monthMemberList,yearMemberList,yearsMemberList;
		if(statisticModel.equals(CmsStatisticModel.month)){
			monthMemberList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_MONTH, monthBegin, now);
			model.addAttribute("monthMemberList", monthMemberList);
		}else if(statisticModel.equals(CmsStatisticModel.section)){
			monthMemberList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_MONTH, begin, end);
			model.addAttribute("monthMemberList", monthMemberList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			dayMemberList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_DAY, dayBegin, now);
			model.addAttribute("dayMemberList", dayMemberList);
		}else if(statisticModel.equals(CmsStatisticModel.years)){
			yearsMemberList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_YEARS, null, now);
			model.addAttribute("yearsMemberList", yearsMemberList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			yearMemberList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_YEAR, yearBegin, now);
			model.addAttribute("yearMemberList", yearMemberList);
		}
		model.addAttribute("memberDayCount", memberDayCount);
		model.addAttribute("memberWeekCount", memberWeekCount);
		model.addAttribute("memberMonthCount", memberMonthCount);
		model.addAttribute("memberYearCount", memberYearCount);
		model.addAttribute("memberTotalCount", memberTotalCount);
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		return "statistic/member/list";
	}

	@RequiresPermissions("statistic:content:v_list")
	@RequestMapping("/statistic/content/v_list.do")
	public String contentList(HttpServletRequest request, String queryModel,
			Integer channelId, Integer year, Integer month, Integer day,
			ModelMap model) {
		String queryInputUsername = RequestUtils.getQueryParam(request,
				"queryInputUsername");
		Integer queryInputUserId = null;
		if (!StringUtils.isBlank(queryInputUsername)) {
			CmsUser u = cmsUserMng.findByUsername(queryInputUsername);
			if (u != null) {
				queryInputUserId = u.getId();
			} else {
				// 用户名不存在，清空。
				queryInputUsername = null;
			}
		}
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId = CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		restrictions.put(USERID, queryInputUserId);
		restrictions.put(CHANNELID, channelId);
		restrictions.put(STATUS, ContentCheck.CHECKED);
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		List<CmsStatistic> list = cmsStatisticSvc.statisticByModel(CONTENT,
				statisticModel, year, month, day,null,null, restrictions);
		List<Channel> topList = channelMng.getTopList(siteId, true);
		List<Channel> channelList = Channel.getListForSelect(topList, null,
				true);
		putCommonData(statisticModel, list, year, month, day, model);
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date weekBegin=DateUtils.getSpecficWeekStart(now, 0);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange weekTimeRange=TimeRange.getInstance(weekBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange totalTimeRange=TimeRange.getInstance(null, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		long releaseDayCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, dayTimeRange, restrictions);
		long releaseWeekCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, weekTimeRange, restrictions);
		long releaseMonthCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, monthTimeRange, restrictions);
		long releaseYearCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, yearTimeRange, restrictions);
		long releaseTotalCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, totalTimeRange, restrictions);
		List<Object[]> dayList,monthList,yearList;
		if(statisticModel.equals(CmsStatisticModel.month)){
			monthList=cmsStatisticSvc.statisticContentByTarget(STATISTIC_BY_MONTH, monthBegin, now,restrictions);
			model.addAttribute("monthList", monthList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			dayList=cmsStatisticSvc.statisticContentByTarget(STATISTIC_BY_DAY, dayBegin, now,restrictions);
			model.addAttribute("dayList", dayList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			yearList=cmsStatisticSvc.statisticContentByTarget(STATISTIC_BY_YEAR, yearBegin, now,restrictions);
			model.addAttribute("yearList", yearList);
		}
		model.addAttribute("releaseDayCount", releaseDayCount);
		model.addAttribute("releaseWeekCount", releaseWeekCount);
		model.addAttribute("releaseMonthCount", releaseMonthCount);
		model.addAttribute("releaseYearCount", releaseYearCount);
		model.addAttribute("releaseTotalCount", releaseTotalCount);
		model.addAttribute("queryInputUsername", queryInputUsername);
		model.addAttribute("channelList", channelList);
		model.addAttribute("channelId", channelId);
		return "statistic/content/list";
	}
	
	
	

	@RequiresPermissions("statistic:comment:v_list")
	@RequestMapping("/statistic/comment/v_list.do")
	public String commentList(HttpServletRequest request, String queryModel,
			Integer year, Integer month, Integer day, Boolean isReplyed,
			ModelMap model) {
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId = CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		restrictions.put(ISREPLYED, isReplyed);
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		List<CmsStatistic> list = cmsStatisticSvc.statisticByModel(COMMENT,
				statisticModel, year, month, day, null,null,restrictions);
		putCommonData(statisticModel, list, year, month, day, model);
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date weekBegin=DateUtils.getSpecficWeekStart(now, 0);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange weekTimeRange=TimeRange.getInstance(weekBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange totalTimeRange=TimeRange.getInstance(null, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		long dayCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, dayTimeRange, restrictions);
		long weekCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, weekTimeRange, restrictions);
		long monthCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, monthTimeRange, restrictions);
		long yearCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, yearTimeRange, restrictions);
		long totalCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, totalTimeRange, restrictions);
		List<Object[]> dayList,monthList,yearList;
		if(statisticModel.equals(CmsStatisticModel.month)){
			monthList=cmsStatisticSvc.statisticCommentByTarget(STATISTIC_BY_MONTH,
					siteId,isReplyed,monthBegin, now);
			model.addAttribute("monthList", monthList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			dayList=cmsStatisticSvc.statisticCommentByTarget(STATISTIC_BY_DAY,
					siteId,isReplyed,dayBegin, now);
			model.addAttribute("dayList", dayList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			yearList=cmsStatisticSvc.statisticCommentByTarget(STATISTIC_BY_YEAR,
					siteId,isReplyed,yearBegin, now);
			model.addAttribute("yearList", yearList);
		}
		model.addAttribute("isReplyed", isReplyed);
		model.addAttribute("dayCount", dayCount);
		model.addAttribute("weekCount", weekCount);
		model.addAttribute("monthCount", monthCount);
		model.addAttribute("yearCount", yearCount);
		model.addAttribute("totalCount", totalCount);
		return "statistic/comment/list";
	}

	@RequiresPermissions("statistic:guestbook:v_list")
	@RequestMapping("/statistic/guestbook/v_list.do")
	public String guestbookList(HttpServletRequest request, String queryModel,
			Integer year, Integer month, Integer day, Boolean isReplyed,
			ModelMap model) {
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId = CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		restrictions.put(ISREPLYED, isReplyed);
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		List<CmsStatistic> list = cmsStatisticSvc.statisticByModel(GUESTBOOK,
				statisticModel, year, month, day,null,null, restrictions);
		putCommonData(statisticModel, list, year, month, day, model);
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date weekBegin=DateUtils.getSpecficWeekStart(now, 0);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange weekTimeRange=TimeRange.getInstance(weekBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange totalTimeRange=TimeRange.getInstance(null, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		long dayCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, dayTimeRange, restrictions);
		long weekCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, weekTimeRange, restrictions);
		long monthCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, monthTimeRange, restrictions);
		long yearCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, yearTimeRange, restrictions);
		long totalCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, totalTimeRange, restrictions);
		List<Object[]> dayList,monthList,yearList;
		if(statisticModel.equals(CmsStatisticModel.month)){
			monthList=cmsStatisticSvc.statisticGuestbookByTarget(STATISTIC_BY_MONTH,
					siteId,isReplyed,monthBegin, now);
			model.addAttribute("monthList", monthList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			dayList=cmsStatisticSvc.statisticGuestbookByTarget(STATISTIC_BY_DAY,
					siteId,isReplyed,dayBegin, now);
			model.addAttribute("dayList", dayList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			yearList=cmsStatisticSvc.statisticGuestbookByTarget(STATISTIC_BY_YEAR,
					siteId,isReplyed,yearBegin, now);
			model.addAttribute("yearList", yearList);
		}
		model.addAttribute("dayCount", dayCount);
		model.addAttribute("weekCount", weekCount);
		model.addAttribute("monthCount", monthCount);
		model.addAttribute("yearCount", yearCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("isReplyed", isReplyed);
		return "statistic/guestbook/list";
	}
	
	@RequiresPermissions("statistic:channel:v_list")
	@RequestMapping("/statistic/channel/v_list.do")
	public String channelList(Integer channelLevel,String view, 
			HttpServletRequest request,ModelMap model) {
		Integer siteId=CmsUtils.getSiteId(request);
		List<Channel>list;
		if(channelLevel==null){
			channelLevel=1;
		}
		if(StringUtils.isBlank(view)){
			view="view";
		}
		if(channelLevel.equals(1)){
			//顶层栏目
			list=channelMng.getTopList(siteId, false);
		}else{
			//底层栏目
			list=channelMng.getBottomList(siteId, false);
		}
		//view比较的列
		Collections.sort(list, new ListChannelComparator(view));
		model.addAttribute("list", list);
		model.addAttribute("channelLevel", channelLevel);
		model.addAttribute("view", view);
		return "statistic/channel/list";
	}

	@RequiresPermissions("flow:pv:v_list")
	@RequestMapping("/flow/pv/v_list.do")
	public String pageViewList(Integer flag,Date year,Date begin,Date end
			,HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		//flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
		if(flag==null){
			flag=4;
		}
		//默认一个月
		if(begin==null&&end==null){
			end=DateUtils.getFinallyDate(calendar.getTime());
			begin=DateUtils.getSpecficMonthStart(end, 0);
		}
		List<Object[]> list;
		if(flag==1){
			//本月
			list=cmsAccessStatisticMng.statistic(begin, end, siteId, STATISTIC_ALL,null);
		}else if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
			list=cmsAccessStatisticMng.statistic(begin, end, siteId, STATISTIC_ALL,null);
		}else if(flag==2){
			//选择年度统计
			if(year==null){
				year=calendar.getTime();
			}
			calendar.setTime(year);
			list=cmsAccessStatisticMng.statisticByYear(calendar.get(Calendar.YEAR), siteId,STATISTIC_ALL,null,true,null);
		}else{
			//今日数据统计(按小时)
			list=cmsAccessMng.statisticToday(siteId,null);
		}
		model.addAttribute("flag", flag);
		model.addAttribute("year", calendar.get(Calendar.YEAR));
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("list", list);
		return "statistic/pv/list";
	}
	
	@RequiresPermissions("flow:source:v_list")
	@RequestMapping("/flow/source/v_list.do")
	public String sourceList(String type,Integer flag,Integer target,
			Date year,Date begin,Date end,
			HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		List<String>columnValues;
		if(StringUtils.isBlank(type)){
			type=STATISTIC_SOURCE;
		}
		if(flag==null){
			flag=2;
		}
		//展示指标(0-pv,1-IP,2-访客数 3-访问时长)
		if(target==null){
			target=0;
		}
		String property=type;
		if(flag==0){
			if(type.equals(STATISTIC_SOURCE)){
				property="accessSource";
			}else if(type.equals(STATISTIC_LINK)){
				property="externalLink";
			}
			columnValues=cmsAccessMng.findPropertyValues(property, siteId);
		}else{
			columnValues=cmsAccessStatisticMng.findStatisticColumnValues(begin, end, siteId, property);
		}
		//默认一个月
		if(begin==null&&end==null){
			end=calendar.getTime();
			begin=DateUtils.getSpecficMonthStart(end, 0);
		}
		Map<String,List<Object[]>>resultMap=new HashMap<String, List<Object[]>>();
		Map<String,Long>totalMap=new HashMap<String, Long>();
		//flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
		if(flag==1){
			//选择日期统计
			for(String v:columnValues){
				resultMap.put(v, cmsAccessStatisticMng.statisticByTarget(begin, end, siteId,target,type,v));
			}
		}else if(flag==2){
			//选择年度统计
			if(year==null){
				year=calendar.getTime();
			}
			calendar.setTime(year);
			for(String v:columnValues){
				resultMap.put(v, cmsAccessStatisticMng.statisticByYearByTarget(calendar.get(Calendar.YEAR), siteId,target,type,v));
			}
		}else if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
			for(String v:columnValues){
				resultMap.put(v, cmsAccessStatisticMng.statisticByTarget(begin, end, siteId,target,type,v));
			}
		}else{
			//今日数据统计(按小时)
			for(String v:columnValues){
				resultMap.put(v, cmsAccessMng.statisticTodayByTarget(siteId, target, type, v));
			}
		}
		for(String columnValue:columnValues){
			List<Object[]> li=resultMap.get(columnValue);
			Long total=0l;
			for(Object[]array:li){
				total+=(Long)array[0];
			}
			totalMap.put(columnValue, total);
		}
		model.addAttribute("flag", flag);
		model.addAttribute("type", type);
		model.addAttribute("target", target);
		model.addAttribute("year", calendar.get(Calendar.YEAR));
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("keys", columnValues);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("totalMap", totalMap);
		return "statistic/source";
	}
	
	@RequiresPermissions("flow:area:v_list")
	@RequestMapping("/flow/area/v_list.do")
	public String areaList(Integer flag, Integer target,Date year,
			Date begin,Date end,HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		List<String>areas;
		if(flag==null){
			flag=4;
		}
		//展示指标(0-pv,1-IP,2-访客数 3-访问时长)
		if(target==null){
			target=0;
		}
		if(flag==0){
			areas=cmsAccessMng.findPropertyValues(STATISTIC_AREA, siteId);
		}else{
			areas=cmsAccessStatisticMng.findStatisticColumnValues(begin, end, siteId, STATISTIC_AREA);
		}
		//默认一个月
		if(begin==null&&end==null){
			end=calendar.getTime();
			begin=DateUtils.getSpecficMonthStart(end, 0);
		}
		Map<String,Object[]>areaCounts=new LinkedHashMap<String, Object[]>();
		//列表数据排序后结果
		Map<String,Object[]>areaCountMap=new LinkedHashMap<String, Object[]>();
		//饼图数据map
		Map<String,Long>totalMap=new LinkedHashMap<String, Long>();
		//flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
		for(String area:areas){
			List<Object[]>li;
			if(flag==1){
				//选择当月统计
				li=cmsAccessStatisticMng.statisticTotal(begin, end, siteId, STATISTIC_AREA, area, target);
			}else if(flag==2){
				//选择今年统计
				if(year==null){
					year=calendar.getTime();
				}
				calendar.setTime(year);
				li=cmsAccessStatisticMng.statisticByYear(calendar.get(Calendar.YEAR), siteId, STATISTIC_AREA, area, false, target);
			}else if(flag==3){
				//区间统计
				if(begin!=null){
					begin=DateUtils.getStartDate(begin);
				}
				if(end!=null){
					end=DateUtils.getFinallyDate(end);
				}
				li=cmsAccessStatisticMng.statisticTotal(begin, end, siteId, STATISTIC_AREA, area, target);
			}else{
				//今日数据统计(按小时)
				li=cmsAccessMng.statisticToday(siteId, area);
			}
			if(li.size()>0){
				areaCounts.put(area, li.get(0));
			}
		}
		ArrayList<Entry<String,Object[]>> l = new ArrayList<Entry<String,Object[]>>(areaCounts.entrySet());  
		Collections.sort(l, new MapComparator(target));
		Long otherTotal=0l;
		for(int i=0;i<l.size();i++){
			Entry<String,Object[]> e=l.get(i);
			Object[]array=e.getValue();
			Long targetValue=0l;
			if(target==0){
				targetValue=(Long) array[0];
			}else if(target==1){
				targetValue=(Long) array[1];
			}else if(target==2){
				targetValue=(Long) array[2]; 
			}else{
				targetValue=(Long) array[3];  
			}
			if(targetValue==null){
				targetValue=0l;
			}
			totalMap.put(e.getKey(),targetValue);
			/*
			//饼图只留十条数据
			if(i<9){
				totalMap.put(e.getKey(),targetValue);
			}else{
				otherTotal+=targetValue;
				totalMap.put(getMessage(request, "cmsAccess.area.other"), otherTotal);
			}
			*/
			areaCountMap.put(e.getKey(), array);
		}
		model.addAttribute("flag", flag);
		model.addAttribute("target", target);
		model.addAttribute("year", calendar.get(Calendar.YEAR));
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("areaCountMap", areaCountMap);
		model.addAttribute("totalMap", totalMap);
		return "statistic/area";
	}
	
	@RequiresPermissions("flow:visitor:v_list")
	@RequestMapping("/flow/visitor/v_list.do")
	public String visitorsGroupByPage(Integer flag,Date year,
			Date begin,Date end,HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		if(flag==null){
			flag=4;
		}
		//默认一个月
		if(begin==null&&end==null){
			end=calendar.getTime();
			begin=DateUtils.getSpecficMonthStart(end, 0);
		}
		List<Object[]> li;
		//flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
		if(flag==1){
			//选择当月统计
			li=cmsAccessCountMng.statisticVisitorCountByDate(siteId, begin, end);
		}else if(flag==2){
			//选择年度统计
			if(year==null){
				year=calendar.getTime();
			}
			calendar.setTime(year);
			li=cmsAccessCountMng.statisticVisitorCountByYear(siteId, calendar.get(Calendar.YEAR));
		}if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
			li=cmsAccessCountMng.statisticVisitorCountByDate(siteId, begin, end);
		}else{
			//今日数据统计
			li=cmsAccessMng.statisticVisitorCount(DateUtils.getStartDate(new Date()), siteId);
		}
		List<Object[]>result=listOrder(li);
		Collections.sort(result, new ListComparator());
		model.addAttribute("flag", flag);
		model.addAttribute("year", calendar.get(Calendar.YEAR));
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("result",result );
		return "statistic/visitor";
	}
	
	@RequiresPermissions("flow:pages:v_list")
	@RequestMapping("/flow/pages/v_list.do")
	public String pages(Integer orderBy,Integer pageNo,HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		Pagination pagination=cmsAccessPagesMng.findPages(siteId, orderBy, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("pagination",pagination);
		return "statistic/pages";
	}
	
	@RequiresPermissions("flow:enterpage:v_list")
	@RequestMapping("/flow/enterpage/v_list.do")
	public String enterPages(Integer orderBy,Integer pageNo,HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		Pagination pagination=cmsAccessMng.findEnterPages(siteId, orderBy, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("pagination",pagination);
		return "statistic/enterpages";
	}
	
	private List<Object[]>listOrder(List<Object[]>li){
		List<Object[]> result=new ArrayList<Object[]>();
		Long fiveAbove=0l,tenAbove=0l,twentyabove=0l,fifty=0l;
		for(Object[]o:li){
			Long visitor=(Long) o[0];
			Integer pageCount=(Integer) o[1];
			if(pageCount<5){
				result.add(o);
			}else if(pageCount>=5&&pageCount<=10){
				fiveAbove+=visitor;
			}else if(pageCount>10&&pageCount<=20){
				tenAbove+=visitor;
			}else if(pageCount>20&&pageCount<=50){
				twentyabove+=visitor;
			}else if(pageCount<50){
				fifty+=visitor;
			}
		}
		if(fiveAbove>0){
			Object[]o=new Object[2];
			o[0]=fiveAbove;
			o[1]="5-10";
			result.add(o);
		}
		if(tenAbove>0){
			Object[]o=new Object[2];
			o[0]=tenAbove;
			o[1]="11-20";
			result.add(o);
		}
		if(twentyabove>0){
			Object[]o=new Object[2];
			o[0]=twentyabove;
			o[1]="21-50";
			result.add(o);
		}
		if(fifty>0){
			Object[]o=new Object[2];
			o[0]=fifty;
			o[1]="50+";
			result.add(o);
		}
		return result;
	}
	
	private class MapComparator implements Comparator<Map.Entry<String, Object[]>> {
		private Integer target;
		public MapComparator(Integer target) {
			this.target = target;
		}
		public int compare(Map.Entry<String, Object[]> o1, Map.Entry<String, Object[]> o2) {
			Object[]o1Value=o1.getValue();
			Object[]o2Value=o2.getValue();
			if(o2Value!=null&&o1Value!=null){
				if(target==0){
					if(o2Value[0]!=null&&o1Value[0]!=null){
						Long a=(Long)o2Value[0]-(Long)o1Value[0];
						return a.intValue(); 
					}
				}else if(target==1){
					if(o2Value[1]!=null&&o1Value[1]!=null){
						Long a=(Long)o2Value[1]-(Long)o1Value[1];
						return a.intValue();  
					}
				}else if(target==2){
					if(o2Value[2]!=null&&o1Value[2]!=null){
						Long a=(Long)o2Value[2]-(Long)o1Value[2];
						return a.intValue(); 
					}
				}else{
					if(o2Value[3]!=null&&o1Value[3]!=null){
						Long a=(Long)o2Value[3]-(Long)o1Value[3];
						return a.intValue();
					}
				}
			}
			return 0;
		}  
	}
	
	private class ListComparator implements Comparator<Object[]> {
		public int compare(Object[] o1, Object[] o2) {
			Long a=(Long)o2[0]-(Long)o1[0];
			return a.intValue();  
		}  
	}
	
	private class ListChannelComparator implements Comparator<Channel> {
		private String comparaField;
		public ListChannelComparator(String comparaField) {
			super();
			this.comparaField = comparaField;
		}
		public int compare(Channel c1, Channel c2) {
			Integer a=0;
			if(comparaField.equals("view")){
				a=c2.getViewTotal()-c1.getViewTotal();
			}else if(comparaField.equals("viewDay")){
				a=c2.getViewsDayTotal()-c1.getViewsDayTotal();
			}else if(comparaField.equals("viewMonth")){
				a=c2.getViewsMonthTotal()-c1.getViewsMonthTotal();
			}else if(comparaField.equals("viewWeek")){
				a=c2.getViewsWeekTotal()-c1.getViewsWeekTotal();
			}
			return a;  
		}  
	}


	private CmsStatisticModel getStatisticModel(String queryModel) {
		if (!StringUtils.isBlank(queryModel)) {
			return CmsStatisticModel.valueOf(queryModel);
		}
		return CmsStatisticModel.year;
	}

	private void putCommonData(CmsStatisticModel statisticModel,
			List<CmsStatistic> list, Integer year, Integer month, Integer day,
			ModelMap model) {
		model.addAttribute("list", list);
		model.addAttribute("total", getTotal(list));
		model.addAttribute("statisticModel", statisticModel.name());
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
	}

	private Long getTotal(List<CmsStatistic> list) {
		return list.size() > 0 ? list.iterator().next().getTotal() : 0L;
	}
	
	private  String getMessage(HttpServletRequest request, String key,
			Object... args) {
		return MessageResolver.getMessage(request, key, args);
	}

	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsStatisticSvc cmsStatisticSvc;
	@Autowired
	private CmsSiteAccessMng cmsAccessMng;
	@Autowired
	private CmsSiteAccessPagesMng cmsAccessPagesMng;
	@Autowired
	private CmsSiteAccessCountMng cmsAccessCountMng;
	@Autowired
	private CmsSiteAccessStatisticMng cmsAccessStatisticMng;
}
