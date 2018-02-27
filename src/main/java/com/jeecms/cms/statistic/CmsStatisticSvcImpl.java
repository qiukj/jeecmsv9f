package com.jeecms.cms.statistic;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static com.jeecms.cms.statistic.CmsStatistic.JOIN;
import static com.jeecms.cms.statistic.CmsStatistic.TIMEPATTERN;
import static com.jeecms.cms.statistic.CmsStatistic.MEMBER;
import static com.jeecms.cms.statistic.CmsStatistic.CONTENT;
import static com.jeecms.cms.statistic.CmsStatistic.COMMENT;
import static com.jeecms.cms.statistic.CmsStatistic.GUESTBOOK;
import static com.jeecms.cms.statistic.CmsStatistic.TODAY;
import static com.jeecms.cms.statistic.CmsStatistic.YESTERDAY;
import static com.jeecms.cms.statistic.CmsStatistic.THISWEEK;
import static com.jeecms.cms.statistic.CmsStatistic.THISMONTH;
import static com.jeecms.cms.statistic.CmsStatistic.THISYEAR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.statistic.CmsStatistic.CmsStatisticModel;
import com.jeecms.cms.statistic.CmsStatistic.TimeRange;
import com.jeecms.common.util.DateFormatUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.springmvc.MessageResolver;

@Service
@Transactional(readOnly = true)
public class CmsStatisticSvcImpl implements CmsStatisticSvc {
	public List<CmsStatistic> statisticByModel(int type,
			CmsStatisticModel statisticModel, Integer year, Integer month,
			Integer day, Date begin,Date end,Map<String, Object> restrictions) {
		Calendar calendar;
		if (month == null) {
			month = 0;
		} else {
			month = month - 1;
		}
		if (day == null) {
			day = 1;
		}
		if (year == null) {
			calendar = new GregorianCalendar();
		} else {
			calendar = new GregorianCalendar(year, month, day);
		}
		Date now=Calendar.getInstance().getTime();
		if(begin==null){
			begin=DateUtils.getStartDate(now);
		}
		if(end==null){
			end=DateUtils.getFinallyDate(now);
		}
		return statisticByModel(type, statisticModel, calendar,begin,end, restrictions);
	}

	public long statistic(int type, TimeRange timeRange,
			Map<String, Object> restrictions) {
		switch (type) {
		case MEMBER: {
			return dao.memberStatistic(timeRange);
		}
		case CONTENT: {
			return dao.contentStatistic(timeRange, restrictions);
		}
		case COMMENT: {
			return dao.commentStatistic(timeRange, restrictions);
		}
		case GUESTBOOK: {
			return dao.guestbookStatistic(timeRange, restrictions);
		}
		}
		return 0;
	}
	
	public List<Object[]> statisticMemberByTarget(
			Integer target,Date timeBegin,Date timeEnd){
		return dao.statisticMemberByTarget(target, timeBegin, timeEnd);
	}
	
	public List<Object[]> statisticContentByTarget(Integer target,
			Date timeBegin,Date timeEnd,Map<String, Object> restrictions){
		return dao.statisticContentByTarget(target, timeBegin, timeEnd, restrictions);
	}
	
	public List<Object[]> statisticCommentByTarget(
			Integer target,Integer siteId,Boolean isReplyed,Date timeBegin,Date timeEnd){
		return dao.statisticCommentByTarget(target,siteId,isReplyed, timeBegin, timeEnd);
	}
	
	public List<Object[]> statisticGuestbookByTarget(Integer target,Integer siteId,
			 Boolean isReplyed,Date timeBegin,Date timeEnd){
		return dao.statisticGuestbookByTarget(target,siteId,isReplyed, timeBegin, timeEnd);
	}

	private List<CmsStatistic> statisticByModel(int type,
			CmsStatisticModel statisticModel, Calendar calendar,
			Date begin,Date end,
			Map<String, Object> restrictions) {
		switch (statisticModel) {
		case day: {
			return statisticByDay(type, calendar, restrictions);
		}
		case week:
		{
			return statisticByWeek(type, calendar, restrictions);
		}
		case month: {
			return statisticByMonth(type, calendar, restrictions);
		}
		case year: {
			return statisticByYear(type, calendar, restrictions);
		}
		case section:{
			return statisticBySection(type, begin, end, restrictions);
		}
		}
		return new ArrayList<CmsStatistic>();
	}

	private List<CmsStatistic> statisticByDay(int type, Calendar calendar,
			Map<String, Object> restrictions) {
		calendar = clearTime(calendar);
		List<CmsStatistic> list = new ArrayList<CmsStatistic>();
		long total = 0, count = 0;
		Date begin, end;
		Calendar clone = (Calendar) calendar.clone();
		total = statistic(type, getTimeRange(TODAY, clone), restrictions);
		for (int i = 0; i < 24; i++) {
			calendar.set(HOUR_OF_DAY, i);
			begin = calendar.getTime();
			calendar.set(HOUR_OF_DAY, i + 1);
			end = calendar.getTime();
			count = statistic(type, TimeRange.getInstance(begin, end), restrictions);
			CmsStatistic bean = new CmsStatistic(format(i), count, total);
			list.add(bean);
		}
		return list;
	}

	private List<CmsStatistic> statisticByWeek(int type, Calendar calendar,
			Map<String, Object> restrictions) {
		calendar = clearTime(calendar);
		flush(calendar);
		List<CmsStatistic> list = new ArrayList<CmsStatistic>();
		long total = 0, count = 0;
		Date begin, end;
		Calendar clone = (Calendar) calendar.clone();
		total = statistic(type, getTimeRange(THISWEEK, clone), restrictions);
		for (int i = 1; i <= 7; i++) {
			calendar.set(DAY_OF_WEEK, i);
			begin = calendar.getTime();
			if (i == 7) {
				calendar.add(DAY_OF_WEEK, 1);
			} else {
				calendar.set(DAY_OF_WEEK, i + 1);
			}
			end = calendar.getTime();
			count = statistic(type, TimeRange.getInstance(begin, end), restrictions);
			CmsStatistic bean = new CmsStatistic(String.valueOf(i), count,
					total);
			list.add(bean);
		}
		return list;
	}

	private List<CmsStatistic> statisticByMonth(int type, Calendar calendar,
			Map<String, Object> restrictions) {
		List<CmsStatistic> list = new ArrayList<CmsStatistic>();
		int year = getYear(calendar);
		int month = getMonth(calendar);
		long total = 0, count = 0;
		int day = 1, days;
		Date begin, end;
		calendar = new GregorianCalendar(year, month, day);
		total = statistic(type, getTimeRange(THISMONTH, (Calendar) calendar.clone()), restrictions);
		Calendar clone = (Calendar) calendar.clone();
		clone.set(MONTH, month + 1);
		end = clone.getTime();
		clone.add(DATE, -1);
		days = getDay(clone);
		for (int i = 1; i <= days; i++) {
			calendar.set(DATE, i);
			begin = calendar.getTime();
			calendar.set(DATE, i + 1);
			end = calendar.getTime();
			count = statistic(type, TimeRange.getInstance(begin, end), restrictions);
			CmsStatistic bean = new CmsStatistic(String.valueOf(i), count,
					total);
			list.add(bean);
		}
		return list;
	}
	
	private List<CmsStatistic> statisticBySection(int type, Date begin,Date end,
			Map<String, Object> restrictions) {
		List<CmsStatistic> list = new ArrayList<CmsStatistic>();
		long total = 0, count = 0;
		int beginDay = 0, days;
		TimeRange timeRanger=TimeRange.getInstance(begin, end);
		total = statistic(type, timeRanger, restrictions);
		days = DateUtils.getDaysBetweenDate(begin, end);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(begin);
		Date b;
		for (int i = beginDay; i <= days; i++) {
			b = DateUtils.getSpecficDateStart(begin, i);
			end = DateUtils.getSpecficDateStart(b, 1);
			count = statistic(type, TimeRange.getInstance(b, end), restrictions);
			calendar.setTime(b);
			CmsStatistic bean = new CmsStatistic(String.valueOf(calendar.get(MONTH)+1)+","+calendar.get(DATE), count,
					total);
			list.add(bean);
		}
		return list;
	}

	private List<CmsStatistic> statisticByYear(int type, Calendar calendar,
			Map<String, Object> restrictions) {
		List<CmsStatistic> list = new ArrayList<CmsStatistic>();
		int year = getYear(calendar);
		long total = 0, count = 0;
		int day = 1, month = 0;
		Date begin, end;
		calendar = new GregorianCalendar(year, month, day);
		Calendar clone = (Calendar) calendar.clone();
		total = statistic(type, getTimeRange(THISYEAR, clone), restrictions);
		for (int i = 0; i < 12; i++) {
			calendar.set(MONTH, i);
			begin = calendar.getTime();
			calendar.set(MONTH, i + 1);
			end = calendar.getTime();
			count = statistic(type, TimeRange.getInstance(begin, end), restrictions);
			CmsStatistic bean = new CmsStatistic(String.valueOf(i + 1), count,
					total);
			list.add(bean);
		}
		return list;
	}
	
	@Autowired
	private CmsStatisticDao dao;

	

	private String format(int time) {
		Calendar calendar = clearTime(new GregorianCalendar());
		calendar.set(HOUR_OF_DAY, time);
		String begin, end;
		begin = DateFormatUtils.format(calendar.getTime(), TIMEPATTERN);
		calendar.add(HOUR_OF_DAY, 1);
		end = DateFormatUtils.format(calendar.getTime(), TIMEPATTERN);
		return begin + JOIN + end;
	}

	private int getYear(Calendar calendar) {
		return calendar.get(YEAR);
	}

	private int getMonth(Calendar calendar) {
		return calendar.get(MONTH);
	}

	private int getDay(Calendar calendar) {
		return calendar.get(DATE);
	}

	private Calendar clearTime(Calendar calendar) {
		return new GregorianCalendar(getYear(calendar), getMonth(calendar),
				getDay(calendar));
	}

	private void flush(Calendar calendar) {
		calendar.getTime();
	}
	
	// 获取今日、昨日、本周、本月时间范围
	private TimeRange getTimeRange(int type, Calendar calendar) {
		calendar = clearTime(calendar);
		Date begin, end;
		switch (type) {
		case TODAY: {
			begin = calendar.getTime();
			calendar.add(DATE, 1);
			end = calendar.getTime();
			return TimeRange.getInstance(begin, end);
		}
		case YESTERDAY: {
			calendar.add(DATE, -1);
			begin = calendar.getTime();
			calendar.add(DATE, 1);
			end = calendar.getTime();
			return TimeRange.getInstance(begin, end);
		}
		case THISWEEK: {
			flush(calendar);
			calendar.set(DAY_OF_WEEK, 1);
			begin = calendar.getTime();
			calendar.add(DAY_OF_WEEK, 7);
			end = calendar.getTime();
			return TimeRange.getInstance(begin, end);
		}
		case THISMONTH: {
			int month = calendar.get(MONTH);
			calendar.set(DATE, 1);
			begin = calendar.getTime();
			calendar.set(MONTH, month + 1);
			end = calendar.getTime();
			return TimeRange.getInstance(begin, end);
		}
		case THISYEAR:{
			int year = calendar.get(YEAR);
			calendar.set(MONTH, 0);
			calendar.set(DATE, 1);
			begin = calendar.getTime();
			calendar.set(YEAR, year + 1);
			end = calendar.getTime();
			return TimeRange.getInstance(begin, end);
		}
		}
		return null;
	}
}
