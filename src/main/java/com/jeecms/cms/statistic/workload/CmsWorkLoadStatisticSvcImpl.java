package com.jeecms.cms.statistic.workload;

import static com.jeecms.cms.statistic.CmsStatistic.JOIN;
import static com.jeecms.cms.statistic.CmsStatistic.TIMEPATTERN;
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MONTH;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.statistic.CmsStatistic;
import com.jeecms.cms.statistic.CmsStatistic.TimeRange;
import com.jeecms.cms.statistic.workload.CmsWorkLoadStatistic.CmsWorkLoadStatisticDateKind;
import com.jeecms.cms.statistic.workload.CmsWorkLoadStatistic.CmsWorkLoadStatisticGroup;
import com.jeecms.common.util.DateFormatUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;

@Service
@Transactional(readOnly = true)
public class CmsWorkLoadStatisticSvcImpl implements CmsWorkLoadStatisticSvc {
	public List<CmsWorkLoadStatistic> statistic(Integer channelId,
			Integer reviewerId, Integer authorId, Date beginDate, Date endDate,
			CmsWorkLoadStatisticGroup group, CmsWorkLoadStatisticDateKind kind) {
		Long count;
		CmsWorkLoadStatistic bean;
		Channel channel = null;
		CmsUser author = null;
		CmsUser reviewer = null;
		Date begin = beginDate;
		if (channelId != null) {
			channel = channelMng.findById(channelId);
		}
		if (reviewerId != null) {
			reviewer = userMng.findById(reviewerId);
		}
		if (authorId != null) {
			author = userMng.findById(authorId);
		}
		List<CmsWorkLoadStatistic> list = new ArrayList<CmsWorkLoadStatistic>();
		Long total=0l;
		total=dao.statistic(channelId, reviewerId, authorId, beginDate, endDate,
				kind);
		//年统计
		if(group==CmsWorkLoadStatisticGroup.year){
			begin=DateUtils.getSpecficYearStart(beginDate, 0);
			Date bTime,eTime;
			for (int i = 0; i < 12; i++) {
				bTime = DateUtils.getSpecficMonthStart(begin, i);
				eTime=DateUtils.getSpecficMonthEnd(begin, i);
				count = dao.statistic(channelId, reviewerId, authorId, bTime, eTime, kind);
				bean = new CmsWorkLoadStatistic(String.valueOf(i+1),channel, author, reviewer, bTime, count,total);
				list.add(bean);
			}
		}else if(group==CmsWorkLoadStatisticGroup.month){
			int days=DateUtils.getDaysBetweenDate(beginDate, endDate);
			begin=DateUtils.getSpecficMonthStart(beginDate, 0);
			Date bTime,eTime;
			for (int i = 0; i < days; i++) {
				bTime = DateUtils.getSpecficDateStart(begin, i);
				eTime=DateUtils.getSpecficDateEnd(begin, i);
				count = dao.statistic(channelId, reviewerId, authorId, bTime, eTime, kind);
				bean = new CmsWorkLoadStatistic(String.valueOf(i+1),channel, author, reviewer, bTime, count,total);
				list.add(bean);
			}
		}else if(group==CmsWorkLoadStatisticGroup.day){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(DateUtils.getStartDate(beginDate));
			Date bTime,eTime;
			for (int i = 0; i < 24; i++) {
				calendar.set(HOUR_OF_DAY, i);
				bTime = calendar.getTime();
				calendar.set(HOUR_OF_DAY, i + 1);
				eTime = calendar.getTime();
				count = dao.statistic(channelId, reviewerId, authorId, bTime, eTime, kind);
				bean = new CmsWorkLoadStatistic(format(i),channel, author, reviewer, bTime, count,total);
				list.add(bean);
			}
		}
		return list;
	}
	
	public List<Object[]> statisticByTarget(Integer target,
			Integer channelId,Integer reviewerId, 
			Integer authorId, Date beginDate, Date endDate){
		return dao.statisticByTarget(target,channelId,
				reviewerId,authorId,beginDate,endDate);
	}

	private Date getNextDate(CmsWorkLoadStatisticGroup group, Date date,
			int amount) {
		Date result = null;
		if (group == CmsWorkLoadStatisticGroup.year) {
			result = DateUtils.getSpecficYearStart(date, amount);
		} else if (group == CmsWorkLoadStatisticGroup.month) {
			result = DateUtils.getSpecficMonthStart(date, amount);
		} else if (group == CmsWorkLoadStatisticGroup.week) {
			result = DateUtils.getSpecficWeekStart(date, amount);
		} else if (group == CmsWorkLoadStatisticGroup.day) {
			result = DateUtils.getSpecficDateStart(date, amount);
		}
		return result;
	}
	
	private String format(int time) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(DateUtils.getStartDate(calendar.getTime()));
		calendar.set(HOUR_OF_DAY, time);
		String begin, end;
		begin = DateFormatUtils.format(calendar.getTime(), TIMEPATTERN);
		calendar.add(HOUR_OF_DAY, 1);
		end = DateFormatUtils.format(calendar.getTime(), TIMEPATTERN);
		return begin + JOIN + end;
	}

	@Autowired
	private CmsWorkLoadStatisticDao dao;
	@Autowired
	private CmsUserMng userMng;
	@Autowired
	private ChannelMng channelMng;
}
