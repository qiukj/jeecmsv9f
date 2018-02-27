package com.jeecms.plug.weixin.dao.impl;

import java.util.List;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.weixin.dao.WeixinMenuDao;
import com.jeecms.plug.weixin.entity.WeixinMenu;

public class WeixinMenuDaoImpl extends HibernateBaseDao<WeixinMenu, Integer> implements WeixinMenuDao {
	
	public Pagination getPage(Integer siteId,Integer parentId,int pageNo,int pageSize){
		Finder f = Finder.create("select bean from WeixinMenu bean where 1=1");
		if(parentId!=null){
			f.append(" and bean.parent.id=:parentId");
			f.setParam("parentId", parentId);
		}else{
			f.append(" and bean.parent is null");
		}
		f.append(" and bean.site.id=:siteId");
		f.setParam("siteId", siteId);
		f.append(" order by bean.id desc");
		f.setCacheable(true);
		return find(f,pageNo,pageSize);
	}
	
	public List<WeixinMenu> getList(Integer siteId,Integer count){
		Finder f = Finder.create("select bean from WeixinMenu bean where 1=1");
		f.append(" and bean.parent is null");
		if(siteId!=null){
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" order by bean.id desc ");
		f.setCacheable(true);
		f.setMaxResults(count);
		return find(f);
	}
	
	public WeixinMenu findById(Integer id){
		return get(id);
	}
	
	public WeixinMenu save(WeixinMenu bean){
		getSession().save(bean);
		return bean;
	}
	
	public WeixinMenu deleteById(Integer id){
		WeixinMenu entity = get(id);
		if(entity!=null){
			getSession().delete(entity);
			return entity;
		}
		return null;
	}

	@Override
	protected Class<WeixinMenu> getEntityClass() {
		return WeixinMenu.class;
	}

}
