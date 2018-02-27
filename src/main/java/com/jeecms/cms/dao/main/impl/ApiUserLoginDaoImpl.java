package com.jeecms.cms.dao.main.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.main.ApiUserLoginDao;
import com.jeecms.cms.entity.main.ApiUserLogin;

@Repository
public class ApiUserLoginDaoImpl extends HibernateBaseDao<ApiUserLogin, Long> implements ApiUserLoginDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public void clearByDate(Date end){
		String hql="delete from ApiUserLogin bean where bean.activeTime<=:end";
		Query query = getSession().createQuery(hql).setParameter("end", end);
		query.executeUpdate();
	}
	
	public List<ApiUserLogin> getList(Date end,int first, int count){
		Finder f=Finder.create("from  ApiUserLogin bean ");
		if(end!=null){
			f.append("where bean.loginTime<=:end").setParam("end", end);
		}
		f.setFirstResult(first);
		f.setMaxResults(count);
		f.setCacheable(true);
		return find(f);
	}

	public ApiUserLogin findById(Long id) {
		ApiUserLogin entity = get(id);
		return entity;
	}
	
	public ApiUserLogin findUserLogin(String username,String sessionKey){
		String hql="from ApiUserLogin bean where 1=1";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.username=:username").setParam("username", username);
		}
		if(StringUtils.isNotBlank(sessionKey)){
			f.append(" and bean.sessionKey=:sessionKey").setParam("sessionKey", sessionKey);
		}
		List<ApiUserLogin>li=find(f);
		if(li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}

	public ApiUserLogin save(ApiUserLogin bean) {
		getSession().save(bean);
		return bean;
	}

	public ApiUserLogin deleteById(Long id) {
		ApiUserLogin entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ApiUserLogin> getEntityClass() {
		return ApiUserLogin.class;
	}
}