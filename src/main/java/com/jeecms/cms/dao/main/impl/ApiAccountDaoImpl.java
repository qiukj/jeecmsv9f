package com.jeecms.cms.dao.main.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.main.ApiAccountDao;
import com.jeecms.cms.entity.main.ApiAccount;

@Repository
public class ApiAccountDaoImpl extends HibernateBaseDao<ApiAccount, Integer> implements ApiAccountDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<ApiAccount> getList(int first, int count){
		String hql="from ApiAccount bean";
		Finder f=Finder.create(hql);
		List<ApiAccount>li=find(f);
		return  li;
	}
	
	public ApiAccount findByAppId(String appId){
		String hql="from ApiAccount bean where bean.appId=:appId";
		Finder f=Finder.create(hql).setParam("appId", appId);
		List<ApiAccount>li=find(f);
		if(li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}
	
	public ApiAccount findAdmin(){
		String hql="from ApiAccount bean where bean.admin=true";
		Finder f=Finder.create(hql);
		List<ApiAccount>li=find(f);
		if(li!=null&&li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}

	public ApiAccount findById(Integer id) {
		ApiAccount entity = get(id);
		return entity;
	}

	public ApiAccount save(ApiAccount bean) {
		getSession().save(bean);
		return bean;
	}

	public ApiAccount deleteById(Integer id) {
		ApiAccount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ApiAccount> getEntityClass() {
		return ApiAccount.class;
	}
}