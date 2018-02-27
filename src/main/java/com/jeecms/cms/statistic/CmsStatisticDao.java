package com.jeecms.cms.statistic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeecms.cms.statistic.CmsStatistic.TimeRange;

public interface CmsStatisticDao {
	public long memberStatistic(TimeRange timeRange);
	
	public List<Object[]> statisticMemberByTarget(Integer target,
			Date timeBegin,Date timeEnd);

	public long contentStatistic(TimeRange timeRange,
			Map<String, Object> restrictions);
	
	public List<Object[]> statisticContentByTarget(Integer target,
			Date timeBegin,Date timeEnd,Map<String, Object> restrictions);
	
	public List<Object[]> statisticCommentByTarget(
			Integer target,Integer siteId,Boolean isReplyed,Date timeBegin,Date timeEnd);


	public List<Object[]> statisticGuestbookByTarget(Integer target,Integer siteId,
			 Boolean isReplyed,Date timeBegin,Date timeEnd);

	public long commentStatistic(TimeRange timeRange,
			Map<String, Object> restrictions);

	public long guestbookStatistic(TimeRange timeRange,
			Map<String, Object> restrictions);
	
	
}
