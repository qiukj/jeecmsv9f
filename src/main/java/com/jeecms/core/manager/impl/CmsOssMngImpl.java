package com.jeecms.core.manager.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.CmsOssDao;
import com.jeecms.core.entity.CmsOss;
import com.jeecms.core.manager.CmsOssMng;

@Service
@Transactional
public class CmsOssMngImpl implements CmsOssMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<CmsOss> getList(){
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public CmsOss findById(Integer id) {
		CmsOss entity = dao.findById(id);
		return entity;
	}

	public CmsOss save(CmsOss bean) {
		dao.save(bean);
		return bean;
	}

	public CmsOss update(CmsOss bean) {
		Updater<CmsOss> updater = new Updater<CmsOss>(bean);
		if(StringUtils.isBlank(bean.getSecretId())){
			updater.exclude("secretId");
		}
		if(StringUtils.isBlank(bean.getAppKey())){
			updater.exclude("appKey");
		}
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public CmsOss deleteById(Integer id) {
		CmsOss bean = dao.deleteById(id);
		return bean;
	}
	
	public CmsOss[] deleteByIds(Integer[] ids) {
		CmsOss[] beans = new CmsOss[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private CmsOssDao dao;

	@Autowired
	public void setDao(CmsOssDao dao) {
		this.dao = dao;
	}
}