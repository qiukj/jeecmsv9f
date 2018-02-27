package com.jeecms.cms.api.admin.assist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsSiteAccessCountHour;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.manager.assist.CmsSiteAccessCountHourMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessCountMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessPagesMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessStatisticMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.statistic.CmsStatistic;
import com.jeecms.cms.statistic.CmsStatistic.CmsStatisticModel;
import com.jeecms.cms.statistic.CmsStatistic.TimeRange;
import com.jeecms.cms.statistic.workload.CmsWorkLoadStatisticSvc;
import com.jeecms.cms.statistic.CmsStatisticSvc;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.MapUtil;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.util.CmsUtils;

import static com.jeecms.cms.statistic.CmsStatistic.SITEID;
import static com.jeecms.cms.statistic.CmsStatistic.STATUS;
import static com.jeecms.cms.statistic.CmsStatistic.ISREPLYED;
import static com.jeecms.cms.statistic.CmsStatistic.USERID;
import static com.jeecms.cms.statistic.CmsStatistic.CHANNELID;

import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_DAY;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_MONTH;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEAR;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEARS;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_SECTION;
import static com.jeecms.common.page.SimplePage.cpn;

import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_ALL;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_SOURCE;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_LINK;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_AREA;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_KEYWORD;

@Controller
public class CmsStatisticApiAct {
	
	@RequestMapping("/statistic/member/list")
	public void memberList(String queryModel, Date begin,Date end,
			HttpServletRequest request, HttpServletResponse response) {
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId=CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		Date now=Calendar.getInstance().getTime();
		Date yesterdayBegin=DateUtils.getSpecficDateStart(now, -1);
		Date yesterdayEnd=DateUtils.getSpecficDateEnd(now, -1);
		Date dayBegin=DateUtils.getStartDate(now);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange yesterdayTimeRange=TimeRange.getInstance(yesterdayBegin, yesterdayEnd);
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
		long totalCount=0l;
		
		List<Object[]> statisticList = new ArrayList<Object[]>();
		List<Object[]>data=new ArrayList<Object[]>();
		if(statisticModel.equals(CmsStatisticModel.month)){
		    totalCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, monthTimeRange, restrictions);
			statisticList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_MONTH, monthBegin, now);
			data=getCompleteDataByMonth(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.section)){
			statisticList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_SECTION, begin, end);
			totalCount=0l;
			for(Object[]obj:statisticList){
				totalCount+=(Long)obj[0];
			}
			data=getCompleteDataBySection(statisticList, begin, end);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, dayTimeRange, restrictions);
			statisticList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_DAY, dayBegin, now);
			data=getCompleteDataByDay(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.yesterday)){
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, yesterdayTimeRange, restrictions);
			statisticList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_DAY, yesterdayBegin,yesterdayEnd);
			data=getCompleteDataByDay(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.years)){
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, totalTimeRange, restrictions);
			statisticList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_YEARS, null, now);
			data=statisticList;
		}else if(statisticModel.equals(CmsStatisticModel.year)){
		    totalCount=cmsStatisticSvc.statistic(CmsStatistic.MEMBER, yearTimeRange, restrictions);
			statisticList=cmsStatisticSvc.statisticMemberByTarget(STATISTIC_BY_YEAR, yearBegin, now);
			data=getCompleteDataByYear(statisticList);
		}
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("data", data);
		json.put("totalCount", totalCount);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@RequestMapping("/statistic/content/list")
	public void contentList(HttpServletRequest request, HttpServletResponse response,
			String queryModel,Integer channelId) {
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
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		List<Object[]> statisticList=new ArrayList<Object[]>();
		List<Object[]>data=new ArrayList<Object[]>();
		long totalCount=0l;
		if(statisticModel.equals(CmsStatisticModel.month)){
			statisticList=cmsStatisticSvc.statisticContentByTarget(STATISTIC_BY_MONTH, monthBegin, now,restrictions);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, monthTimeRange, restrictions);
			data=getCompleteDataByMonth(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			statisticList=cmsStatisticSvc.statisticContentByTarget(STATISTIC_BY_DAY, dayBegin, now,restrictions);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, dayTimeRange, restrictions);
			data=getCompleteDataByDay(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			statisticList=cmsStatisticSvc.statisticContentByTarget(STATISTIC_BY_YEAR, yearBegin, now,restrictions);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.CONTENT, yearTimeRange, restrictions);
			data=getCompleteDataByYear(statisticList);
		}
		
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("data", data);
		json.put("totalCount", totalCount);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@RequestMapping("/statistic/comment/list")
	public void commentList(String queryModel,Boolean isReplyed,
			HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId = CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		restrictions.put(ISREPLYED, isReplyed);
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		List<Object[]> statisticList=new ArrayList<Object[]>();
		List<Object[]>data=new ArrayList<Object[]>();
		long totalCount=0l;
		if(statisticModel.equals(CmsStatisticModel.month)){
			statisticList=cmsStatisticSvc.statisticCommentByTarget(STATISTIC_BY_MONTH,
					siteId,isReplyed,monthBegin, now);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, monthTimeRange, restrictions);
			data=getCompleteDataByMonth(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			statisticList=cmsStatisticSvc.statisticCommentByTarget(STATISTIC_BY_DAY,
					siteId,isReplyed,dayBegin, now);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, dayTimeRange, restrictions);
			data=getCompleteDataByDay(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			statisticList=cmsStatisticSvc.statisticCommentByTarget(STATISTIC_BY_YEAR,
					siteId,isReplyed,yearBegin, now);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.COMMENT, yearTimeRange, restrictions);
			data=getCompleteDataByYear(statisticList);
		}
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("data", data);
		json.put("totalCount", totalCount);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@RequestMapping("/statistic/guestbook/list")
	public void guestbookList(String queryModel,Boolean isReplyed,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> restrictions = new HashMap<String, Object>();
		Integer siteId = CmsUtils.getSiteId(request);
		restrictions.put(SITEID, siteId);
		restrictions.put(ISREPLYED, isReplyed);
		CmsStatisticModel statisticModel = getStatisticModel(queryModel);
		Date now=Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		TimeRange dayTimeRange=TimeRange.getInstance(dayBegin, now);
		TimeRange monthTimeRange=TimeRange.getInstance(monthBegin, now);
		TimeRange yearTimeRange=TimeRange.getInstance(yearBegin, now);
		List<Object[]> statisticList=new ArrayList<Object[]>();
		List<Object[]>data=new ArrayList<Object[]>();
		long totalCount=0l;
		if(statisticModel.equals(CmsStatisticModel.month)){
			statisticList=cmsStatisticSvc.statisticGuestbookByTarget(STATISTIC_BY_MONTH,
					siteId,isReplyed,monthBegin, now);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, monthTimeRange, restrictions);
			data=getCompleteDataByMonth(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.day)){
			statisticList=cmsStatisticSvc.statisticGuestbookByTarget(STATISTIC_BY_DAY,
					siteId,isReplyed,dayBegin, now);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, dayTimeRange, restrictions);
			data=getCompleteDataByDay(statisticList);
		}else if(statisticModel.equals(CmsStatisticModel.year)){
			statisticList=cmsStatisticSvc.statisticGuestbookByTarget(STATISTIC_BY_YEAR,
					siteId,isReplyed,yearBegin, now);
			totalCount=cmsStatisticSvc.statistic(CmsStatistic.GUESTBOOK, yearTimeRange, restrictions);
			data=getCompleteDataByYear(statisticList);
		}
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("data", data);
		json.put("totalCount", totalCount);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/statistic/channel/list")
	public void channelList(Integer channelLevel,String view, 
			HttpServletRequest request, HttpServletResponse response) {
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
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONArray jsonArray=new JSONArray();
		for(int i=0;i<list.size();i++){
			JSONObject json=new JSONObject();
			json=list.get(i).convertToJson(Constants.URL_HTTP, false,false, null);
			jsonArray.put(i, json);
		}
		body = jsonArray.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@RequestMapping("/flow/pv/list")
	public void pageViewList(Integer flag,Date year,Date begin,Date end
			,Date statisDay,
			HttpServletRequest request, HttpServletResponse response) {
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		Date now =calendar.getTime();
		//flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计 5某天小时数据统计
		if(flag==null){
			flag=4;
		}
		//默认一个月
		if(begin==null&&end==null){
			end=DateUtils.getFinallyDate(calendar.getTime());
			begin=DateUtils.getSpecficMonthStart(end, 0);
		}
		List<Object[]> list=new ArrayList<Object[]>();
		if(flag==1){
			//本月统计
			int days=DateUtils.getDaysBetweenDate(DateUtils.getSpecficMonthStart(now, 0), now);
			List<Object[]>data=new ArrayList<Object[]>();
			list=cmsAccessStatisticMng.statistic(begin, end, siteId, STATISTIC_ALL,null);
			for(int i=days;i>=0;i--){
				Date d=DateUtils.getSpecficDateStart(now, -i);
				boolean includeInQueryList=false;
				for(Object[] obj:list){
					Date queryDate=(Date) obj[4];
					if(DateUtils.isInDate(d, queryDate)){
						data.add(obj);
						includeInQueryList=true;
						break;
					}
				}
				if(!includeInQueryList){
					Object[]obj=new Object[5];
					obj[0]=0l;obj[1]=0l;obj[2]=0l;obj[3]=0l;obj[4]=d;
					data.add(obj);
				}
			}
			//转换日期
			for(Object[] obj:data){
				Date d=(Date) obj[4];
				obj[4]=DateUtils.getMonthDayStr(d);
			}
			list=data;
		}else if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
			int days=DateUtils.getDaysBetweenDate(begin, end);
			List<Object[]>data=new ArrayList<Object[]>();
			list=cmsAccessStatisticMng.statistic(begin, end, siteId, STATISTIC_ALL,null);
			//for(int i=days+2;i>1;i--){
			for(int i=days;i>=0;i--){
				Date d=DateUtils.getSpecficDateStart(end, -i);
				boolean includeInQueryList=false;
				for(Object[] obj:list){
					Date queryDate=(Date) obj[4];
					if(DateUtils.isInDate(d, queryDate)){
						data.add(obj);
						includeInQueryList=true;
						break;
					}
				}
				if(!includeInQueryList){
					Object[]obj=new Object[5];
					obj[0]=0l;obj[1]=0l;obj[2]=0l;obj[3]=0l;obj[4]=d;
					data.add(obj);
				}
			}
			//转换日期
			for(Object[] obj:data){
				Date d=(Date) obj[4];
				obj[4]=DateUtils.getMonthDayStr(d);
			}
			list=data;
		}else if(flag==2){
			//选择年度统计
			if(year==null){
				year=calendar.getTime();
			}
			calendar.setTime(year);
			int months=12;
			List<Object[]>data=new ArrayList<Object[]>();	
			list=cmsAccessStatisticMng.statisticByYear(calendar.get(Calendar.YEAR), siteId,STATISTIC_ALL,null,true,null);
			for(int i=1;i<=months;i++){
				boolean includeInQuery=false;
				for(Object[]obj:list){
					if(obj[4].equals(i)){
						data.add(obj);
						includeInQuery=true;
						break;
					}
				}
				if(!includeInQuery){
					Object[]obj=new Object[5];
					obj[0]=0l;obj[1]=0l;obj[2]=0l;obj[3]=0l;obj[4]=i;
					data.add(obj);
				}
			}
			list=data;
		}else if(flag==4){
			//今日数据统计(按小时)
			int hours=24;
			List<Object[]>data=new ArrayList<Object[]>();	
			list=cmsAccessMng.statisticToday(siteId,null);
			for(int i=0;i<hours;i++){
				Object[]hour=new Object[5];
				boolean includeInQuery=false;
				for(Object[]obj:list){
					if(obj[4].equals(i)){
						hour=obj;
						includeInQuery=true;
						break;
					}
				}
				if(!includeInQuery){
					hour[0]=0l;hour[1]=0l;hour[2]=0l;hour[3]=0l;
					hour[4]=i;
				}
				data.add(hour);
			}
			list=data;
		}else if(flag==6){
			//昨日数据统计
			int hours=24;
			statisDay=DateUtils.getSpecficDateStart(now, -1);
			List<Object[]>data=new ArrayList<Object[]>();	
			List<CmsSiteAccessCountHour>hourCounts=
					siteAccessCountHourMng.getList(statisDay);
			for(int i=0;i<hours;i++){
				Object[]hour=new Object[5];
				boolean includeInQuery=false;
				for(CmsSiteAccessCountHour obj:hourCounts){
					if(obj.getAccessHour().equals(i)){
						hour[0]=obj.getHourPv();
						hour[1]=obj.getHourIp();
						hour[2]=obj.getHourUv();
						hour[4]=obj.getAccessHour();
						includeInQuery=true;
						break;
					}
				}
				if(!includeInQuery){
					hour[0]=0l;hour[1]=0l;hour[2]=0l;
					hour[4]=i;
				}
				//平均访问时长，此处虚拟，无用，方便前端统一处理hour参数
				hour[3]=0l;
				data.add(hour);
			}
			list=data;
		}else if(flag==5){
			//某日小时数据统计(按小时)
			int hours=24;
			List<Object[]>data=new ArrayList<Object[]>();	
			if(statisDay!=null){
				List<CmsSiteAccessCountHour>hourCounts=
						siteAccessCountHourMng.getList(statisDay);
				for(int i=0;i<hours;i++){
					Object[]hour=new Object[5];
					boolean includeInQuery=false;
					for(CmsSiteAccessCountHour obj:hourCounts){
						if(obj.getAccessHour().equals(i)){
							hour[0]=obj.getHourPv();
							hour[1]=obj.getHourIp();
							hour[2]=obj.getHourUv();
							hour[4]=obj.getAccessHour();
							includeInQuery=true;
							break;
						}
					}
					if(!includeInQuery){
						hour[0]=0l;hour[1]=0l;hour[2]=0l;
						hour[4]=i;
					}
					//平均访问时长，此处虚拟，无用，方便前端统一处理hour参数
					hour[3]=0l;
					data.add(hour);
				}
				list=data;
			}
		}
		Long pvTotal=0l,ipTotal=0l,visitorTotal=0l;
		for(Object[]obj :list){
			pvTotal+=(Long) obj[0];
			ipTotal+=(Long) obj[1];
			visitorTotal+=(Long) obj[2];
		}
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("list", list);
		json.put("pvTotal", pvTotal);
		json.put("ipTotal", ipTotal);
		json.put("visitorTotal", visitorTotal);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	/**
	 * @param type 
	 * @param flag flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
	 * @param target 展示指标(0-pv,1-IP,2-访客数 3-访问时长)
	 * @param year
	 * @param begin
	 * @param end
	 * @param orderBy 排序 0时间升序   1指标值倒序 ，默认0
	 */
	@RequestMapping("/flow/source/list")
	public void sourceList(String type,Integer flag,Integer target,
			Date year,Date begin,Date end,Integer orderBy,Integer count,
			HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		Date now=calendar.getTime();
		List<String>columnValues;
		if(flag==null){
			flag=2;
		}
		//默认一个月
		if(begin==null){
			begin=DateUtils.getSpecficMonthStart(now, 0);
		}
		if(end==null){
			end=calendar.getTime();
		}
		//今年
		if(flag==2){
			begin=DateUtils.getSpecficYearStart(now, 0);
			end=calendar.getTime();
		}else if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
		}else if(flag==1){
			//本月
			begin=DateUtils.getSpecficMonthStart(now, 0);
			end=calendar.getTime();
		}else{
			begin=DateUtils.getStartDate(now);
			end=calendar.getTime();
		}
		if(StringUtils.isBlank(type)){
			type=STATISTIC_SOURCE;
		}
		if(count==null){
			count=10;
		}
		//展示指标(0-pv,1-IP,2-访客数 3-访问时长)
		if(target==null){
			target=0;
		}
		if(orderBy==null){
			orderBy=CmsStatistic.ORDER_BY_TIME_DESC;
		}
		String property=type;
		if(flag==4){
			if(type.equals(STATISTIC_SOURCE)){
				property="accessSource";
			}else if(type.equals(STATISTIC_LINK)){
				property="externalLink";
			}
			columnValues=cmsAccessMng.findPropertyValues(property, siteId);
		}else{
			columnValues=cmsAccessStatisticMng.findStatisticColumnValues(begin, end, siteId, property);
		}
		Map<String,List<Object[]>>resultMap=new HashMap<String, List<Object[]>>();
		Map<String,Long>totalMap=new HashMap<String, Long>();
		//flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
		if(flag==1){
			//本月统计
			int days=DateUtils.getDaysBetweenDate(DateUtils.getSpecficMonthStart(now, 0), now);
			for(String v:columnValues){
				List<Object[]>data=new ArrayList<Object[]>();
				List<Object[]>list=cmsAccessStatisticMng.statisticByTarget(begin, end, siteId,target,type,v);
				for(int i=days;i>=0;i--){
					Date d=DateUtils.getSpecficDateStart(now, -i);
					boolean includeInQueryList=false;
					for(Object[] obj:list){
						Date queryDate=(Date) obj[1];
						if(DateUtils.isInDate(d, queryDate)){
							data.add(obj);
							includeInQueryList=true;
							break;
						}
					}
					if(!includeInQueryList){
						Object[]obj=new Object[2];
						obj[0]=0l;
						obj[1]=d;
						data.add(obj);
					}
				}
				//转换日期
				for(Object[] obj:data){
					Date d=(Date) obj[1];
					 obj[1]=DateUtils.getMonthDayStr(d);
				}
				if(orderBy.equals(CmsStatistic.ORDER_BY_DATA_DESC)){
					data=ArrayUtils.sortListDesc(data, target);
				}
				resultMap.put(v, data);
			}
		}else if(flag==2){
			//选择年度统计
			if(year==null){
				year=calendar.getTime();
			}
			calendar.setTime(year);
			int months=12;
			for(String v:columnValues){
				List<Object[]>data=new ArrayList<Object[]>();	
				List<Object[]>list=cmsAccessStatisticMng.statisticByYearByTarget(calendar.get(Calendar.YEAR), siteId,target,type,v);
				for(int i=1;i<=months;i++){
					boolean includeInQuery=false;
					for(Object[]obj:list){
						if(obj[1].equals(i)){
							data.add(obj);
							includeInQuery=true;
							break;
						}
					}
					if(!includeInQuery){
						Object[]obj=new Object[2];
						obj[0]=0l;
						obj[1]=i;
						data.add(obj);
					}
				}
				if(orderBy.equals(CmsStatistic.ORDER_BY_DATA_DESC)){
					data=ArrayUtils.sortListDesc(data, target);
				}
				resultMap.put(v,data);
			}
		}else if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
			int days=DateUtils.getDaysBetweenDate(begin, end);
			for(String v:columnValues){
				List<Object[]>data=new ArrayList<Object[]>();
				List<Object[]>list=cmsAccessStatisticMng.statisticByTarget(begin, end, siteId,target,type,v);
				for(int i=days;i>=0;i--){
					Date d=DateUtils.getSpecficDateStart(end, -i);
					boolean includeInQueryList=false;
					for(Object[] obj:list){
						Date queryDate=(Date) obj[1];
						if(DateUtils.isInDate(d, queryDate)){
							data.add(obj);
							includeInQueryList=true;
							break;
						}
					}
					if(!includeInQueryList){
						Object[]obj=new Object[2];
						obj[0]=0l;
						obj[1]=d;
						data.add(obj);
					}
				}
				//转换日期
				for(Object[] obj:data){
					Date d=(Date) obj[1];
					 obj[1]=DateUtils.getMonthDayStr(d);
				}
				if(orderBy.equals(CmsStatistic.ORDER_BY_DATA_DESC)){
					data=ArrayUtils.sortListDesc(data, target);
				}
				resultMap.put(v, data);
			}
		}else{
			//今日数据统计(按小时)
			int hours=24;
			for(String v:columnValues){
				List<Object[]>data=new ArrayList<Object[]>();	
				List<Object[]>list=cmsAccessMng.statisticTodayByTarget(siteId, target, type, v);
				for(int i=0;i<hours;i++){
					Object[]hour=new Object[2];
					boolean includeInQuery=false;
					for(Object[]obj:list){
						if(obj[1].equals(i)){
							hour=obj;
							includeInQuery=true;
							break;
						}
					}
					if(!includeInQuery){
						hour[0]=0l;
						hour[1]=i;
					}
					data.add(hour);
				}
				if(orderBy.equals(CmsStatistic.ORDER_BY_DATA_DESC)){
					data=ArrayUtils.sortListDesc(data, target);
				}
				resultMap.put(v, data);
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
		totalMap=MapUtil.sortMapByLongValue(totalMap);
		int k=0;
		Map<String,List<Object[]>>map=new HashMap<String,List<Object[]>>();
		Map<String,Long>totalMapResult=new HashMap<String, Long>();
		List<String>values=new ArrayList<String>();
		if(type.equals(STATISTIC_LINK)||type.equals(STATISTIC_KEYWORD)){
			Set<String>keySet=totalMap.keySet();
			Iterator<String>it=keySet.iterator();
			while(it.hasNext()){
				String key=it.next();
				if(k++<count){
					map.put(key, resultMap.get(key));
					values.add(key);
					totalMapResult.put(key, totalMap.get(key));
				}
			}
		}else{
			map=resultMap;
			values=columnValues;
			totalMapResult=totalMap;
		}
		JSONObject json=new JSONObject();
		json.put("keys", values);
		json.put("resultMap", map);
		json.put("totalMap", totalMapResult);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * @param flag flag 1 按本月统计 2年度统计 3区间统计 4当前日小时统计
	 * @param target 展示指标(0-pv,1-IP,2-访客数 3-访问时长)
	 * @param year  年度统计
	 * @param begin  区间查询开始时间
	 * @param end  区间查询结束时间
	 */
	@RequestMapping("/flow/area/list")
	public void areaList(Integer flag, Integer target,Date year,
			Date begin,Date end,HttpServletRequest request,HttpServletResponse response) {
		Integer siteId = CmsUtils.getSiteId(request);
		Calendar calendar=Calendar.getInstance();
		List<String>areas = new ArrayList<String>();
		if(flag==null){
			flag=4;
		}
		//展示指标(0-pv,1-IP,2-访客数 3-访问时长)
		if(target==null){
			target=0;
		}
		//默认一个月
		if(begin==null&&end==null){
			end=calendar.getTime();
			begin=DateUtils.getSpecficMonthStart(end, 0);
		}
		if(flag==4){
			//当日统计
			areas=cmsAccessMng.findPropertyValues(STATISTIC_AREA, siteId);
		}else if(flag==2){
			//年度统计 
			if(year==null){
				year=calendar.getTime();
			}
			calendar.setTime(year);
			begin=DateUtils.getSpecficYearStart(calendar.getTime(), 0);
			end=DateUtils.getSpecficYearEnd(calendar.getTime(), 0);
		}else if(flag==3){
			//区间统计
			if(begin!=null){
				begin=DateUtils.getStartDate(begin);
			}
			if(end!=null){
				end=DateUtils.getFinallyDate(end);
			}
		}
		//flag 4当日数据统计
		if(flag!=4){
			areas=cmsAccessStatisticMng.findStatisticColumnValues(begin, end, siteId, STATISTIC_AREA);
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
		Long totalCount = 0l;
//		Long otherTotal=0l;
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
			totalCount+=targetValue;
		}
		JSONObject json=new JSONObject();
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		json.put("areaCountMap", areaCountMap);
		json.put("totalMap", totalMap);
		json.put("totalCount", totalCount);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/flow/visitor/list")
	public void visitorsGroupByPage(Integer flag,
			Date begin,Date end,
			HttpServletRequest request, HttpServletResponse response) {
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
			//选择今年统计
			li=cmsAccessCountMng.statisticVisitorCountByYear(siteId, calendar.get(Calendar.YEAR));
		}else if(flag==3){
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
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("data", result);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/flow/pages/list")
	public void pages(Integer orderBy,Integer pageNo,
			HttpServletRequest request,HttpServletResponse response) {
		Integer siteId = CmsUtils.getSiteId(request);
		Pagination pagination=cmsAccessPagesMng.findPages(siteId, orderBy, cpn(pageNo), CookieUtils.getPageSize(request));
		int totalCount = pagination.getTotalCount();
		List<Object[]> list = (List<Object[]>) pagination.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i));
			}
		}
		JSONObject json=new JSONObject();
		json.put("data", jsonArray.toString());
		json.put("totalCount",totalCount);
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/flow/enterpage/list")
	public void enterPages(Integer orderBy,Integer pageNo,
			HttpServletRequest request, HttpServletResponse response) {
		Integer siteId = CmsUtils.getSiteId(request);
		Pagination pagination=cmsAccessMng.findEnterPages(siteId, orderBy, cpn(pageNo), CookieUtils.getPageSize(request));
		int totalCount = pagination.getTotalCount();
		List<Object[]> list = (List<Object[]>) pagination.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i));
			}
		}
		JSONObject json=new JSONObject();
		json.put("data", jsonArray.toString());
		json.put("totalCount",totalCount);
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 工作量统计
	 * @param channelId
	 * @param reviewerId
	 * @param authorId
	 * @param queryModel
	 * @return
	 */
	@RequestMapping("/statistic/workload/list")
	public void statisticWorkload(
			Integer channelId, Integer reviewerId, Integer authorId,
			String queryModel,
			HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(queryModel)) {
			queryModel = "year";
		}
		if(reviewerId != null && reviewerId.equals(0)){
			reviewerId=null;
		}
		if (authorId != null && authorId.equals(0)) {
			authorId = null;
		}
		Date now = Calendar.getInstance().getTime();
		Date dayBegin=DateUtils.getStartDate(now);
		Date monthBegin=DateUtils.getSpecficMonthStart(now, 0);
		Date yearBegin=DateUtils.getSpecficYearStart(now, 0);
		List<Object[]> statisticList=new ArrayList<Object[]>();
		List<Object[]>data=new ArrayList<Object[]>();
		long totalCount=0l;
		if(queryModel.equals(CmsStatisticModel.month.toString())){
			statisticList=workloadStatisticSvc.statisticByTarget(STATISTIC_BY_MONTH, channelId, reviewerId, authorId, monthBegin, now);
			data=getCompleteDataByMonth(statisticList);
		}else if(queryModel.equals(CmsStatisticModel.day.toString())){
			statisticList=workloadStatisticSvc.statisticByTarget(STATISTIC_BY_DAY,channelId, reviewerId, authorId, dayBegin, now);
			data=getCompleteDataByDay(statisticList);
		}else if(queryModel.equals(CmsStatisticModel.year.toString())){
			statisticList=workloadStatisticSvc.statisticByTarget(STATISTIC_BY_YEAR,channelId, reviewerId, authorId, yearBegin, now);
			data=getCompleteDataByYear(statisticList);
		}
		for(Object[]obj:statisticList){
			totalCount+=(Long)obj[0];
		}
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		JSONObject json=new JSONObject();
		json.put("data", data);
		json.put("totalCount", totalCount);
		body = json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
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
	
	private List<Object[]>getCompleteDataByMonth(List<Object[]>statisticList){
		List<Object[]>data=new ArrayList<Object[]>();
		Date now=Calendar.getInstance().getTime();
		int days=DateUtils.getDaysBetweenDate(DateUtils.getSpecficMonthStart(now, 0), now);
		for(int i=1;i<=days+1;i++){
			boolean includeInQueryList=false;
			for(Object[] obj:statisticList){
				Integer queryDate=(Integer) obj[1];
				if(queryDate==i){
					data.add(obj);
					includeInQueryList=true;
					break;
				}
			}
			if(!includeInQueryList){
				Object[]obj=new Object[2];
				obj[0]=0l;obj[1]=i;
				data.add(obj);
			}
		}
		return data;
	}
	
	private List<Object[]>getCompleteDataByDay(List<Object[]>statisticList){
		List<Object[]>data=new ArrayList<Object[]>();
		int hours=24;
		for(int i=0;i<hours;i++){
			Object[]hour=new Object[2];
			boolean includeInQuery=false;
			for(Object[]obj:statisticList){
				if(obj[1].equals(i)){
					hour=obj;
					includeInQuery=true;
					break;
				}
			}
			if(!includeInQuery){
				hour[0]=0l;;
				hour[1]=i;
			}
			data.add(hour);
		}
		return data;
	}
	
	private List<Object[]>getCompleteDataByYear(List<Object[]>statisticList){
		List<Object[]>data=new ArrayList<Object[]>();
		int months=12;
		for(int i=1;i<=months;i++){
			boolean includeInQuery=false;
			for(Object[]obj:statisticList){
				if(obj[1].equals(i)){
					data.add(obj);
					includeInQuery=true;
					break;
				}
			}
			if(!includeInQuery){
				Object[]obj=new Object[2];
				obj[0]=0l;obj[1]=i;
				data.add(obj);
			}
		}
		return data;
	}
	
	private List<Object[]>getCompleteDataBySection(List<Object[]>statisticList,Date begin,Date end){
		int days=DateUtils.getDaysBetweenDate(begin, end);
		List<Object[]>data=new ArrayList<Object[]>();
		for(int i=days;i>=0;i--){
			Date d=DateUtils.getSpecficDateStart(end, -i);
			boolean includeInQueryList=false;
			for(Object[] obj:statisticList){
				Date queryDate=(Date) obj[1];
				if(DateUtils.isInDate(d, queryDate)){
					Object[]o=new Object[2];
					o[0]=obj[0];o[1]=d;
					data.add(o);
					includeInQueryList=true;
					break;
				}
			}
			if(!includeInQueryList){
				Object[]obj=new Object[2];
				obj[0]=0l;obj[1]=d;
				data.add(obj);
			}
		}
		//转换日期
		for(Object[] obj:data){
			Date time=(Date) obj[1];
			obj[1]=DateUtils.getMonthDayStr(time);
		}
		return data;
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

	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsStatisticSvc cmsStatisticSvc;
	@Autowired
	private CmsSiteAccessMng cmsAccessMng;
	@Autowired
	private CmsWorkLoadStatisticSvc workloadStatisticSvc;
	@Autowired
	private CmsSiteAccessPagesMng cmsAccessPagesMng;
	@Autowired
	private CmsSiteAccessCountMng cmsAccessCountMng;
	@Autowired
	private CmsSiteAccessStatisticMng cmsAccessStatisticMng;
	@Autowired
	private CmsSiteAccessCountHourMng siteAccessCountHourMng;
}
