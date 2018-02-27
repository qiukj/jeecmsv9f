package com.jeecms.cms.statistic;

import static com.jeecms.cms.statistic.CmsStatistic.SITEID;
import static com.jeecms.cms.statistic.CmsStatistic.ISREPLYED;
import static com.jeecms.cms.statistic.CmsStatistic.USERID;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_DAY;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_WEEK;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_MONTH;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEAR;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEARS;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YESTERDAY;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_SECTION;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_AREA;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_ENGINE;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_KEYWORD;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_LINK;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_SOURCE;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_TARGET_IP;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_TARGET_PV;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_TARGET_VISITORS;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_TARGET_VISITSECOND;

import static com.jeecms.cms.statistic.CmsStatistic.CHANNELID;
import static com.jeecms.cms.statistic.CmsStatistic.STATUS;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jeecms.cms.statistic.CmsStatistic.TimeRange;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateSimpleDao;

@Repository
public class CmsStatisticDaoImpl extends HibernateSimpleDao implements
		CmsStatisticDao {
	public long memberStatistic(TimeRange timeRange) {
		Finder f = createCacheableFinder("select count(*) from CmsUser bean where 1=1");
		if (timeRange != null) {
			if(timeRange.getBegin()!=null){
				f.append(" and bean.registerTime >= :begin");
				f.setParam("begin", timeRange.getBegin());
			}
			if(timeRange.getEnd()!=null){
				f.append(" and bean.registerTime < :end");
				f.setParam("end", timeRange.getEnd());
			}
		}
		f.append(" and bean.admin is false");
		return (Long) find(f).iterator().next();
	}
	
	public List<Object[]> statisticMemberByTarget(Integer target,Date timeBegin,Date timeEnd){
		String hql = "";
		if(target==STATISTIC_BY_DAY||target==STATISTIC_BY_YESTERDAY){
			hql="select count(bean.id),HOUR(bean.registerTime) from CmsUser bean where 1=1 ";
		}else if(target==STATISTIC_BY_MONTH){
			hql="select count(bean.id),DAY(bean.registerTime) from CmsUser bean  where 1=1  ";
		}else if(target==STATISTIC_BY_YEAR){
			hql="select count(bean.id),MONTH(bean.registerTime) from CmsUser bean  where 1=1 ";
		}else if(target==STATISTIC_BY_SECTION){
			hql="select count(bean.id),bean.registerTime from CmsUser bean  where 1=1 ";
		}else if(target==STATISTIC_BY_YEARS){
			hql="select count(bean.id),YEAR(bean.registerTime) from CmsUser bean  where 1=1 ";
		}
		Finder f = Finder.create(hql);
		if(timeBegin!=null){
			f.append(" and bean.registerTime>=:timeBegin").setParam("timeBegin", timeBegin);
		}
		if(timeEnd!=null){
			f.append(" and bean.registerTime<=:timeEnd").setParam("timeEnd", timeEnd);
		}
		f.append(" and bean.admin=false");
		if(target==STATISTIC_BY_DAY||target==STATISTIC_BY_YESTERDAY){
			f.append(" group by HOUR(bean.registerTime) order by HOUR(bean.registerTime) asc");
		}else if(target==STATISTIC_BY_MONTH){
			f.append(" group by DAY(bean.registerTime) order by DAY(bean.registerTime) asc");
		}else if(target==STATISTIC_BY_YEAR){
			f.append(" group by MONTH(bean.registerTime) order by MONTH(bean.registerTime) asc");
		}else if(target==STATISTIC_BY_YEARS){
			f.append(" group by YEAR(bean.registerTime) order by YEAR(bean.registerTime) asc");
		}else if(target==STATISTIC_BY_SECTION){
			f.append(" group by bean.registerTime order by bean.registerTime asc");
		}
		return find(f);
	}

	public long contentStatistic(TimeRange timeRange,
			Map<String, Object> restrictions) {
		Finder f = createCacheableFinder("select count(bean.id) from Content bean");
		Integer userId = (Integer) restrictions.get(USERID);
		Integer channelId = (Integer) restrictions.get(CHANNELID);
		Byte status = (Byte) restrictions.get(STATUS);
		if (channelId != null) {
			f.append(" join bean.channel channel,Channel parent");
			f.append(" where channel.lft between parent.lft and parent.rgt");
			f.append(" and channel.site.id=parent.site.id");
			f.append(" and parent.id=:parentId");
			f.setParam("parentId", channelId);
		} else {
			f.append(" where bean.site.id=:siteId").setParam("siteId",
					restrictions.get(SITEID));
		}
		if (timeRange != null) {
			if(timeRange.getBegin()!=null){
				f.append(" and bean.sortDate >= :begin");
				f.setParam("begin", timeRange.getBegin());
			}
			if(timeRange.getEnd()!=null){
				f.append(" and bean.sortDate < :end");
				f.setParam("end", timeRange.getEnd());
			}
			
		}
		if (userId != null) {
			f.append(" and bean.user.id=:userId").setParam("userId", userId);
		}
		if(status!=null){
			f.append(" and bean.status=:status").setParam("status", status);
		}
		return (Long) find(f).iterator().next();
	}
	
	public List<Object[]> statisticContentByTarget(Integer target,
			Date timeBegin,Date timeEnd,Map<String, Object> restrictions){
		String hql = "";
		if(target==STATISTIC_BY_DAY){
			hql="select count(bean.id),hour(ext.releaseDate) from Content bean join bean.contentExt ext ";
		}else if(target==STATISTIC_BY_MONTH){
			hql="select count(bean.id),day(ext.releaseDate) from Content bean  join bean.contentExt ext ";
		}else if(target==STATISTIC_BY_YEAR){
			hql="select count(bean.id),month(ext.releaseDate) from Content bean join bean.contentExt ext ";
		}
		Finder f = createCacheableFinder(hql);
		Integer channelId = (Integer) restrictions.get(CHANNELID);
		Byte status = (Byte) restrictions.get(STATUS);
		if (channelId != null) {
			f.append(" join bean.channel channel,Channel parent");
			f.append(" where channel.lft between parent.lft and parent.rgt");
			f.append(" and channel.site.id=parent.site.id");
			f.append(" and parent.id=:parentId");
			f.setParam("parentId", channelId);
		} else {
			f.append(" where bean.site.id=:siteId").setParam("siteId",
					restrictions.get(SITEID));
		}
		if(timeBegin!=null){
			f.append(" and ext.releaseDate>=:timeBegin").setParam("timeBegin", timeBegin);
		}
		if(timeEnd!=null){
			f.append(" and ext.releaseDate<=:timeEnd").setParam("timeEnd", timeEnd);
		}
		if(status!=null){
			f.append(" and bean.status=:status").setParam("status", status);
		}
		if(target==STATISTIC_BY_DAY){
			f.append(" group by hour(ext.releaseDate) order by hour(ext.releaseDate) asc");
		}else if(target==STATISTIC_BY_MONTH){
			f.append(" group by day(ext.releaseDate) order by day(ext.releaseDate) asc");
		}else if(target==STATISTIC_BY_YEAR){
			f.append(" group by month(ext.releaseDate) order by month(ext.releaseDate) asc");
		}
		return find(f);
	}

	public long commentStatistic(TimeRange timeRange,
			Map<String, Object> restrictions) {
		Finder f = createCacheableFinder("select count(*) from CmsComment bean where bean.site.id=:siteId");
		f.setParam("siteId", restrictions.get(SITEID));
		if (timeRange != null) {
			if(timeRange.getBegin()!=null){
				f.append(" and bean.createTime >= :begin");
				f.setParam("begin", timeRange.getBegin());
			}
			if(timeRange.getEnd()!=null){
				f.append(" and bean.createTime < :end");
				f.setParam("end", timeRange.getEnd());
			}
		}
		Boolean isReplyed = (Boolean) restrictions.get(ISREPLYED);
		if (isReplyed != null) {
			if (isReplyed) {
				f.append(" and bean.replayTime is not null");
			} else {
				f.append(" and bean.replayTime is null");
			}
		}
		Short checked = (Short) restrictions.get(STATUS);
		if(checked!=null){
			f.append(" and bean.checked=:status").setParam("status", checked);
		}
		return (Long) find(f).iterator().next();
	}
	
	public List<Object[]> statisticCommentByTarget(Integer target,Integer siteId,
			Boolean isReplyed,Date timeBegin,Date timeEnd){
		String hql = "";
		if(target==STATISTIC_BY_DAY){
			hql="select count(bean.id),HOUR(bean.createTime) from CmsComment bean where 1=1 ";
		}else if(target==STATISTIC_BY_MONTH){
			hql="select count(bean.id),DAY(bean.createTime) from CmsComment bean  where 1=1  ";
		}else if(target==STATISTIC_BY_YEAR){
			hql="select count(bean.id),MONTH(bean.createTime) from CmsComment bean  where 1=1 ";
		}else if(target==STATISTIC_BY_YEARS){
			hql="select count(bean.id),YEAR(bean.createTime) from CmsComment bean  where 1=1 ";
		}
		Finder f = Finder.create(hql);
		f.setCacheable(true);
		if(timeBegin!=null){
			f.append(" and bean.createTime>=:timeBegin").setParam("timeBegin", timeBegin);
		}
		if(timeEnd!=null){
			f.append(" and bean.createTime<=:timeEnd").setParam("timeEnd", timeEnd);
		}
		if(siteId!=null){
			f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		}
		if (isReplyed != null) {
			if (isReplyed) {
				f.append(" and bean.replayTime is not null");
			} else {
				f.append(" and bean.replayTime is null");
			}
		}
		if(target==STATISTIC_BY_DAY){
			f.append(" group by HOUR(bean.createTime) order by HOUR(bean.createTime) asc");
		}else if(target==STATISTIC_BY_MONTH){
			f.append(" group by DAY(bean.createTime) order by DAY(bean.createTime) asc");
		}else if(target==STATISTIC_BY_YEAR){
			f.append(" group by MONTH(bean.createTime) order by MONTH(bean.createTime) asc");
		}else if(target==STATISTIC_BY_YEARS){
			f.append(" group by YEAR(bean.createTime) order by YEAR(bean.createTime) asc");
		}
		return find(f);
	}

	public long guestbookStatistic(TimeRange timeRange,
			Map<String, Object> restrictions) {
		Finder f = createCacheableFinder("select count(*) from CmsGuestbook bean where bean.site.id=:siteId");
		f.setParam("siteId", restrictions.get(SITEID));
		if (timeRange != null) {
			if(timeRange.getBegin()!=null){
				f.append(" and bean.createTime >= :begin");
				f.setParam("begin", timeRange.getBegin());
			}
			if(timeRange.getEnd()!=null){
				f.append(" and bean.createTime < :end");
				f.setParam("end", timeRange.getEnd());
			}
		}
		Boolean isReplyed = (Boolean) restrictions.get(ISREPLYED);
		if (isReplyed != null) {
			if (isReplyed) {
				f.append(" and bean.replayTime is not null");
			} else {
				f.append(" and bean.replayTime is null");
			}
		}
		Short checked = (Short) restrictions.get(STATUS);
		if(checked!=null){
			f.append(" and bean.checked=:status").setParam("status", checked);
		}
		return (Long) find(f).iterator().next();
	}
	
	public List<Object[]> statisticGuestbookByTarget(Integer target,Integer siteId,
			Boolean isReplyed,Date timeBegin,Date timeEnd){
		String hql = "";
		if(target==STATISTIC_BY_DAY){
			hql="select count(bean.id),HOUR(bean.createTime) from CmsGuestbook bean where 1=1 ";
		}else if(target==STATISTIC_BY_MONTH){
			hql="select count(bean.id),DAY(bean.createTime) from CmsGuestbook bean  where 1=1  ";
		}else if(target==STATISTIC_BY_YEAR){
			hql="select count(bean.id),MONTH(bean.createTime) from CmsGuestbook bean  where 1=1 ";
		}else if(target==STATISTIC_BY_YEARS){
			hql="select count(bean.id),YEAR(bean.createTime) from CmsGuestbook bean  where 1=1 ";
		}
		Finder f = Finder.create(hql);
		f.setCacheable(true);
		if(timeBegin!=null){
			f.append(" and bean.createTime>=:timeBegin").setParam("timeBegin", timeBegin);
		}
		if(timeEnd!=null){
			f.append(" and bean.createTime<=:timeEnd").setParam("timeEnd", timeEnd);
		}
		if(siteId!=null){
			f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		}
		if (isReplyed != null) {
			if (isReplyed) {
				f.append(" and bean.replayTime is not null");
			} else {
				f.append(" and bean.replayTime is null");
			}
		}
		if(target==STATISTIC_BY_DAY){
			f.append(" group by HOUR(bean.createTime) order by HOUR(bean.createTime) asc");
		}else if(target==STATISTIC_BY_MONTH){
			f.append(" group by DAY(bean.createTime) order by DAY(bean.createTime) asc");
		}else if(target==STATISTIC_BY_YEAR){
			f.append(" group by MONTH(bean.createTime) order by MONTH(bean.createTime) asc");
		}else if(target==STATISTIC_BY_YEARS){
			f.append(" group by YEAR(bean.createTime) order by YEAR(bean.createTime) asc");
		}
		return find(f);
	}

	private Finder createCacheableFinder(String hql) {
		Finder finder = Finder.create(hql);
		finder.setCacheable(true);
		return finder;
	}

}
