package com.jeecms.plug.weixin.dao.impl;

import java.util.List;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.weixin.dao.WeixinMessageDao;
import com.jeecms.plug.weixin.entity.WeixinMessage;

public class WeixinMessageDaoImpl extends HibernateBaseDao<WeixinMessage, Integer> implements WeixinMessageDao {
	
	public Pagination getPage(Integer siteId,int pageNo,int pageSize){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.welcome=false").setParam("siteId", siteId);
		return find(f,pageNo,pageSize);
	}
	
	public List<WeixinMessage> getList(Integer siteId){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.welcome=false order by bean.number");
		f.setParam("siteId", siteId);
		return find(f);
	}
	
	public WeixinMessage getWelcome(Integer siteId){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.welcome=true order by bean.number");
		f.setParam("siteId", siteId);
		List<WeixinMessage> lists = find(f);
		if(lists!=null && lists.size()>0){
			return lists.get(0);
		}
		return null;
	}
	
	public WeixinMessage findByNumber(String number,Integer siteId){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.number=:number order by bean.id desc");
		f.setParam("number", number);
		f.setParam("siteId", siteId);
		List<WeixinMessage> lists = find(f);
		if(lists!=null && lists.size()>0){
			return lists.get(0);
		}
		return null;
	}
	
	public WeixinMessage findById(Integer id){
		return get(id);
	}
	
	public WeixinMessage save(WeixinMessage bean){
		getSession().save(bean);
		return bean;
	}
	
	public WeixinMessage deleteById(Integer id){
		WeixinMessage entity = get(id);
		if(entity!=null){
			getSession().delete(entity);
			return entity;
		}
		return null;
	}

	@Override
	protected Class<WeixinMessage> getEntityClass() {
		return WeixinMessage.class;
	}

}
