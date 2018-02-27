package com.jeecms.cms.dao.assist.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.assist.CmsJobApplyDao;
import com.jeecms.cms.entity.assist.CmsJobApply;

@Repository
public class CmsJobApplyDaoImpl extends HibernateBaseDao<CmsJobApply, Integer>
		implements CmsJobApplyDao {
	public Pagination getPage(Integer userId, Integer contentId,
			Integer siteId, boolean cacheable,String title, int pageNo, int pageSize) {
		Finder f=createFinder(userId, contentId, siteId, cacheable,title);
		Pagination page = find(f, pageNo, pageSize);
		return page;
	}
	
	public List<CmsJobApply> getList(Integer userId,Integer contentId,Integer siteId,
			boolean cacheable,String title,Integer first, Integer count){
		Finder f=createFinder(userId, contentId, siteId, cacheable,title);
		return find(f);
	}
	
	
	public CmsJobApply findById(Integer id) {
		CmsJobApply entity = get(id);
		return entity;
	}

	public CmsJobApply save(CmsJobApply bean) {
		getSession().save(bean);
		return bean;
	}

	public CmsJobApply deleteById(Integer id) {
		CmsJobApply entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	private Finder createFinder(Integer userId,Integer contentId,Integer siteId,
			boolean cacheable,String title){
		Finder f = Finder.create("from CmsJobApply apply where 1=1 ");
		if (userId != null) {
			f.append(" and apply.user.id=:userId").setParam("userId", userId);
		}
		if (contentId != null) {
			f.append(" and apply.content.id=:contentId").setParam("contentId",
					contentId);
		}
		if (siteId != null) {
			f.append(" and apply.content.site.id=:siteId").setParam("siteId",
					siteId);
		}
		if (StringUtils.isNotBlank(title)) {
			f.append(" and apply.content.contentExt.title like :title").setParam("title", "%"+title+"%");
		}
		f.append(" order by apply.applyTime desc");
		f.setCacheable(cacheable);
		return f;
	}
	
	@Override
	protected Class<CmsJobApply> getEntityClass() {
		return CmsJobApply.class;
	}
}