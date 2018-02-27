package com.jeecms.cms.statistic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeecms.cms.statistic.CmsStatistic.CmsStatisticModel;
import com.jeecms.cms.statistic.CmsStatistic.TimeRange;

public interface CmsStatisticSvc {
	public List<CmsStatistic> statisticByModel(int type,
			CmsStatisticModel statisticModel, Integer year, Integer month,
			Integer day,Date begin,Date end, Map<String, Object> restrictions);
	
	public long statistic(int type, TimeRange timeRange,
			Map<String, Object> restrictions);
	
	public List<Object[]> statisticMemberByTarget(Integer target,
			Date timeBegin,Date timeEnd);
	
	public List<Object[]> statisticContentByTarget(Integer target,
			Date timeBegin,Date timeEnd,Map<String, Object> restrictions);
	
	public List<Object[]> statisticCommentByTarget(Integer target,Integer siteId,
			 Boolean isReplyed,Date timeBegin,Date timeEnd);
	
	public List<Object[]> statisticGuestbookByTarget(Integer target,Integer siteId,
			 Boolean isReplyed,Date timeBegin,Date timeEnd);
	
}
