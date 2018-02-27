package com.jeecms.cms.dao.main.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.main.ContentBuyDao;
import com.jeecms.cms.entity.main.ContentBuy;

@Repository
public class ContentBuyDaoImpl extends HibernateBaseDao<ContentBuy, Long> implements ContentBuyDao {
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId,
			Short payMode,int pageNo, int pageSize) {
		Finder f=createFinder(orderNum, buyUserId, authorUserId, payMode);
		Pagination page = find(f, pageNo, pageSize);
		return page;
	}
	
	public List<ContentBuy> getList(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Integer first, Integer count){
		Finder f=createFinder(orderNum, buyUserId, authorUserId, payMode);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	public Pagination getPageByContent(Integer contentId,
			Short payMode,int pageNo, int pageSize){
		Finder f=createFinder(contentId, payMode);
		Pagination page = find(f, pageNo, pageSize);
		return page;
	}
	
	public List<ContentBuy> getListByContent(Integer contentId,
			Short payMode,Integer first, Integer count){
		Finder f=createFinder(contentId, payMode);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	private Finder createFinder(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode){
		String hql="from ContentBuy bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(orderNum)){
			f.append(" and bean.orderNumber like:num").setParam("num", "%"+orderNum+"%");
		}
		if(buyUserId!=null){
			f.append(" and bean.buyUser.id=:buyUserId").setParam("buyUserId", buyUserId);
		}
		if(authorUserId!=null){
			f.append(" and bean.authorUser.id=:authorUserId").setParam("authorUserId", authorUserId);
		}
		if(payMode!=null&&payMode!=0){
			f.append(" and bean.chargeReward=:payMode")
			.setParam("payMode", payMode);
		}
		f.append(" order by bean.buyTime desc");
		f.setCacheable(true);
		return f;
	}
	
	private Finder createFinder(Integer contentId,Short payMode){
		String hql="from ContentBuy bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(contentId!=null){
			f.append(" and bean.content.id=:contentId").setParam("contentId", contentId);
		}
		if(payMode!=null&&payMode!=0){
			f.append(" and bean.chargeReward=:payMode")
			.setParam("payMode", payMode);
		}
		f.append(" order by bean.buyTime desc");
		f.setCacheable(true);
		return f;
	}

	public ContentBuy findById(Long id) {
		ContentBuy entity = get(id);
		return entity;
	}
	
	public ContentBuy findByOrderNumber(String orderNumber){
		String hql="from ContentBuy bean where bean.orderNumber=:orderNumber";
		Finder finder=Finder.create(hql).setParam("orderNumber", orderNumber);
		List<ContentBuy>list=find(finder);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public ContentBuy findByOutOrderNum(String orderNum,Integer payMethod){
		String hql;
		if(payMethod==ContentBuy.PAY_METHOD_WECHAT){
			hql="from ContentBuy bean where bean.orderNumWeiXin=:orderNum";
		}else{
			hql="from ContentBuy bean where bean.orderNumAliPay=:orderNum";
		}
		Finder finder=Finder.create(hql).setParam("orderNum", orderNum);
		List<ContentBuy>list=find(finder);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public ContentBuy find(Integer buyUserId,Integer contentId){
		String hql="from ContentBuy bean where bean.content.id=:contentId "
				+ "and bean.buyUser.id=:buyUserId";
		Finder finder=Finder.create(hql).setParam("contentId", contentId)
				.setParam("buyUserId", buyUserId);
		finder.setCacheable(true);
		List<ContentBuy>list=find(finder);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public ContentBuy save(ContentBuy bean) {
		getSession().save(bean);
		return bean;
	}

	public ContentBuy deleteById(Long id) {
		ContentBuy entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ContentBuy> getEntityClass() {
		return ContentBuy.class;
	}
}