package com.jeecms.cms.dao.assist.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.assist.CmsSiteAccessCountHourDao;
import com.jeecms.cms.entity.assist.CmsSiteAccessCountHour;

@Repository
public class CmsSiteAccessCountHourDaoImpl extends HibernateBaseDao<CmsSiteAccessCountHour, Integer> implements CmsSiteAccessCountHourDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<CmsSiteAccessCountHour> getList(Date date){
		String hql="from  CmsSiteAccessCountHour bean ";
		Finder f=Finder.create(hql);
		if(date!=null){
			f.append(" where bean.accessDate=:date").setParam("date", date);
		}
		f.setCacheable(true);
		return find(f);
	}

	public CmsSiteAccessCountHour findById(Integer id) {
		CmsSiteAccessCountHour entity = get(id);
		return entity;
	}

	public CmsSiteAccessCountHour save(CmsSiteAccessCountHour bean) {
		getSession().save(bean);
		return bean;
	}

	public CmsSiteAccessCountHour deleteById(Integer id) {
		CmsSiteAccessCountHour entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<CmsSiteAccessCountHour> getEntityClass() {
		return CmsSiteAccessCountHour.class;
	}
}