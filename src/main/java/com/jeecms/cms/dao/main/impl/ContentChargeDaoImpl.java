package com.jeecms.cms.dao.main.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.cms.dao.main.ContentChargeDao;
import com.jeecms.cms.entity.main.ContentCharge;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;

@Repository
public class ContentChargeDaoImpl extends HibernateBaseDao<ContentCharge, Integer>
		implements ContentChargeDao {
	
	public List<ContentCharge> getList(String contentTitle,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,Integer first,Integer count){
		Finder finder=getFinder(contentTitle,authorUserId,
				buyTimeBegin,buyTimeEnd,orderBy);
		if(first!=null){
			finder.setFirstResult(first);
		}
		if(count!=null){
			finder.setMaxResults(count);
		}
		return find(finder);
	}
	
	public Pagination getPage(String contentTitle,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,
			int pageNo,int pageSize){
		Finder finder=getFinder(contentTitle,authorUserId,
				buyTimeBegin,buyTimeEnd,orderBy);
		return find(finder, pageNo, pageSize);
	}
	
	private Finder getFinder(String contentTitle,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy){
		String hql="select bean from ContentCharge bean where 1=1 ";
		Finder finder=Finder.create(hql);
		if(StringUtils.isNotBlank(contentTitle)){
			finder.append(" and bean.content.contentExt.title like :title")
			.setParam("title", "%"+contentTitle+"%");
		}
		if(authorUserId!=null){
			if(authorUserId==0){
				//未找到用户情况下不显示任何记录
				finder.append(" and 1!=1 ");
			}else{
				finder.append(" and bean.content.user.id=:authorUserId")
				.setParam("authorUserId", authorUserId);
			}
		}
		if(buyTimeBegin!=null){
			finder.append(" and bean.lastBuyTime>=:buyTimeBegin")
			.setParam("buyTimeBegin", DateUtils.getStartDate(buyTimeBegin));
		}
		if(buyTimeEnd!=null){
			finder.append(" and bean.lastBuyTime<=:buyTimeEnd")
			.setParam("buyTimeEnd", DateUtils.getFinallyDate(buyTimeEnd));
		}
		finder.append(" and (bean.chargeAmount>0 or bean.totalAmount>0)");
		if(orderBy==1){
			finder.append(" order by bean.totalAmount desc ");
		}else if(orderBy==2){
			finder.append(" order by bean.totalAmount asc ");
		}else if(orderBy==3){
			finder.append(" order by bean.yearAmount desc ");
		}else if(orderBy==4){
			finder.append(" order by bean.yearAmount asc ");
		}else if(orderBy==5){
			finder.append(" order by bean.monthAmount desc ");
		}else if(orderBy==6){
			finder.append(" order by bean.monthAmount asc ");
		}else if(orderBy==7){
			finder.append(" order by bean.dayAmount desc ");
		}else if(orderBy==8){
			finder.append(" order by bean.dayAmount asc ");
		}else if(orderBy==9){
			finder.append(" order by bean.chargeAmount desc ");
		}else if(orderBy==10){
			finder.append(" order by bean.chargeAmount asc ");
		}
		return finder;
	}
	
	public ContentCharge findById(Integer id) {
		ContentCharge entity = get(id);
		return entity;
	}

	public ContentCharge save(ContentCharge bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<ContentCharge> getEntityClass() {
		return ContentCharge.class;
	}
}