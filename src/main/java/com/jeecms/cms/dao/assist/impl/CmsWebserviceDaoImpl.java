package com.jeecms.cms.dao.assist.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.cms.dao.assist.CmsWebserviceDao;
import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class CmsWebserviceDaoImpl extends HibernateBaseDao<CmsWebservice, Integer> implements CmsWebserviceDao {
	public Pagination getPage(int pageNo, int pageSize) {
		String hql="from CmsWebservice bean";
		Finder f=Finder.create(hql);
		return find(f, pageNo, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	public List<CmsWebservice> getList(String type){
		String hql="from CmsWebservice bean where bean.type=:type";
		Finder f =Finder.create(hql).setParam("type", type);
		f.setCacheable(true);
		return find(f);
	}

	public CmsWebservice findById(Integer id) {
		CmsWebservice entity = get(id);
		return entity;
	}

	public CmsWebservice save(CmsWebservice bean) {
		getSession().save(bean);
		return bean;
	}

	public CmsWebservice deleteById(Integer id) {
		CmsWebservice entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<CmsWebservice> getEntityClass() {
		return CmsWebservice.class;
	}
}