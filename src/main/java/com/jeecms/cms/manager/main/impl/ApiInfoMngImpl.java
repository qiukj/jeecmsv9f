package com.jeecms.cms.manager.main.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.main.ApiInfoDao;
import com.jeecms.cms.entity.main.ApiInfo;
import com.jeecms.cms.manager.main.ApiInfoMng;

@Service
@Transactional
public class ApiInfoMngImpl implements ApiInfoMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public ApiInfo findByUrl(String url) {
		ApiInfo entity = dao.findByUrl(url);
		return entity;
	}

	@Transactional(readOnly = true)
	public ApiInfo findById(Integer id) {
		ApiInfo entity = dao.findById(id);
		return entity;
	}

	public ApiInfo save(ApiInfo bean) {
		dao.save(bean);
		return bean;
	}

	public ApiInfo update(ApiInfo bean) {
		Updater<ApiInfo> updater = new Updater<ApiInfo>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ApiInfo deleteById(Integer id) {
		ApiInfo bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiInfo[] deleteByIds(Integer[] ids) {
		ApiInfo[] beans = new ApiInfo[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ApiInfoDao dao;

	@Autowired
	public void setDao(ApiInfoDao dao) {
		this.dao = dao;
	}
}