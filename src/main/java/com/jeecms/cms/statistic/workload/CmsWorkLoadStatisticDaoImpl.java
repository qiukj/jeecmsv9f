package com.jeecms.cms.statistic.workload;


import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_DAY;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_MONTH;
import static com.jeecms.cms.statistic.CmsStatistic.STATISTIC_BY_YEAR;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.cms.statistic.workload.CmsWorkLoadStatistic.CmsWorkLoadStatisticDateKind;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateSimpleDao;

@Repository
public class CmsWorkLoadStatisticDaoImpl extends HibernateSimpleDao implements CmsWorkLoadStatisticDao {
	public Long statistic(Integer channelId,
			Integer reviewerId, Integer authorId, 
			Date beginDate, Date endDate,CmsWorkLoadStatisticDateKind dateKind) {
		String hql="select count(*) from Content bean";
		if (reviewerId!=null) {
			hql+=" join bean.contentCheckSet check";
		}
		if (channelId != null) {
			hql+=" join bean.channel channel,Channel parent";
			hql+=" where channel.lft between parent.lft and parent.rgt";
			hql+=" and channel.site.id=parent.site.id";
			hql+=" and parent.id=:parentId";
		}else {
			hql+=" where 1=1";
		}
		if (reviewerId!=null) {
			hql+=" and check.reviewer.id=:reviewerId";
		}
		if(authorId!=null){
			hql+=" and bean.user.id=:authorId";
		}
		if(dateKind==CmsWorkLoadStatisticDateKind.release){
			if(beginDate!=null){
				hql+=" and  bean.contentExt.releaseDate>=:beginDate";
			}
			if(endDate!=null){
				hql+=" and  bean.contentExt.releaseDate<=:endDate";
			}
		}else{
			if(beginDate!=null){
				hql+=" and  check.checkDate>=:beginDate";
			}
			if(endDate!=null){
				hql+=" and  check.checkDate<=:endDate";
			}
		}
		Query query = getSession().createQuery(hql);
		if(channelId!=null){
			query.setParameter("parentId", channelId);
		}
		if(reviewerId!=null){
			query.setParameter("reviewerId", reviewerId);
		}
		if(authorId!=null){
			query.setParameter("authorId", authorId);
		}
		if(beginDate!=null){
			query.setParameter("beginDate", beginDate);
		}
		if(endDate!=null){
			query.setParameter("endDate", endDate);
		}
		return (Long) query.uniqueResult();
	}
	
	public List<Object[]> statisticByTarget(Integer target,
			Integer channelId,Integer reviewerId, 
			Integer authorId, Date beginDate, Date endDate){
		String hql="";
		if(target==STATISTIC_BY_DAY){
			hql="select count(bean.id),HOUR(bean.contentExt.releaseDate) from Content bean ";
		}else if(target==STATISTIC_BY_MONTH){
			hql="select count(bean.id),DAY(bean.contentExt.releaseDate) from Content bean  ";
		}else if(target==STATISTIC_BY_YEAR){
			hql="select count(bean.id),MONTH(bean.contentExt.releaseDate) from Content bean ";
		}
		Finder f = Finder.create(hql);
		f.setCacheable(true);
		if (reviewerId!=null) {
			f.append(" join bean.contentCheckSet check");
		}
		if (channelId != null) {
			f.append(" join bean.channel channel,Channel parent");
			f.append(" where channel.lft between parent.lft and parent.rgt");
			f.append(" and channel.site.id=parent.site.id");
			f.append(" and parent.id=:parentId").setParam("parentId", channelId);
		}else {
			f.append(" where 1=1");
		}
		if (reviewerId!=null) {
			f.append(" and check.reviewer.id=:reviewerId").setParam("reviewerId", reviewerId);
			if(beginDate!=null){
				f.append(" and  check.checkDate>=:beginDate").setParam("beginDate", beginDate);
			}
			if(endDate!=null){
				f.append(" and  check.checkDate<=:endDate").setParam("endDate", endDate);
			}
		}else{
			if(beginDate!=null){
				f.append(" and  bean.contentExt.releaseDate>=:beginDate").setParam("beginDate", beginDate);
			}
			if(endDate!=null){
				f.append(" and  bean.contentExt.releaseDate<=:endDate").setParam("endDate", endDate);;
			}
		}
		if(authorId!=null){
			f.append(" and bean.user.id=:authorId").setParam("authorId", authorId);;
		}
		if(target==STATISTIC_BY_DAY){
			f.append(" group by HOUR(bean.contentExt.releaseDate) order by HOUR(bean.contentExt.releaseDate) asc");
		}else if(target==STATISTIC_BY_MONTH){
			f.append(" group by DAY(bean.contentExt.releaseDate) order by DAY(bean.contentExt.releaseDate) asc");
		}else if(target==STATISTIC_BY_YEAR){
			f.append(" group by MONTH(bean.contentExt.releaseDate) order by MONTH(bean.contentExt.releaseDate) asc");
		}
		return find(f);
	}
	

}
