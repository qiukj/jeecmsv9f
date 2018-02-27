package com.jeecms.plug.weixin.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.weixin.dao.WeixinDao;
import com.jeecms.plug.weixin.entity.Weixin;
import com.jeecms.plug.weixin.manager.WeixinMng;

@Service
@Transactional
public class WeixinMngImpl implements WeixinMng {
	
	@Transactional(readOnly=true)
	public Pagination getPage(Integer siteId,int pageNo,int pageSize){
		return dao.getPage(siteId, pageNo, pageSize);
	}
	
	@Transactional(readOnly=true)
	public Weixin findById(Integer id){
		return dao.findById(id);
	}
	
	@Transactional(readOnly=true)
	public Weixin find(Integer siteId){
		return dao.find(siteId);
	}
	
	public Weixin save(Weixin bean){
		return dao.save(bean);
	}
	
	public Weixin update(Weixin bean){
		Updater<Weixin> updater = new Updater<Weixin>(bean);
		return dao.updateByUpdater(updater);
	}
	
	public Weixin deleteById(Integer id){
		return dao.deleteById(id);
	}
	
	public Weixin[] delete(Integer[] ids){
		Weixin[] beans = new Weixin[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private WeixinDao dao;

}
