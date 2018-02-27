package com.jeecms.cms.dao.main.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.main.ContentRecordDao;
import com.jeecms.cms.entity.main.ContentRecord;

@Repository
public class ContentRecordDaoImpl extends HibernateBaseDao<ContentRecord, Long> implements ContentRecordDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public List<ContentRecord> getListByContentId(Integer contentId) {
		String hql=" select bean from ContentRecord bean where bean.content.id=:contentId";
		Finder f=Finder.create(hql).setParam("contentId", contentId);
		f.setCacheable(true);
		List<ContentRecord>list=find(f);
		return list;
	}

	public ContentRecord findById(Long id) {
		ContentRecord entity = get(id);
		return entity;
	}

	public ContentRecord save(ContentRecord bean) {
		getSession().save(bean);
		return bean;
	}

	public ContentRecord deleteById(Long id) {
		ContentRecord entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ContentRecord> getEntityClass() {
		return ContentRecord.class;
	}
}