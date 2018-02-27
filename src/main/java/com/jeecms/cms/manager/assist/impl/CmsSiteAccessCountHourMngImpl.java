package com.jeecms.cms.manager.assist.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.cms.dao.assist.CmsSiteAccessCountHourDao;
import com.jeecms.cms.dao.assist.CmsSiteAccessDao;
import com.jeecms.cms.entity.assist.CmsSiteAccessCount;
import com.jeecms.cms.entity.assist.CmsSiteAccessCountHour;
import com.jeecms.cms.manager.assist.CmsSiteAccessCountHourMng;

@Service
@Transactional
public class CmsSiteAccessCountHourMngImpl implements CmsSiteAccessCountHourMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<CmsSiteAccessCountHour> getList(Date date){
		return dao.getList(date);
	}
	
	public void statisticCount(Date date, Integer siteId){
		List<Object[]> statisTicData = cmsAccessDao.statisticByDayGroupByHour(date, siteId);
		CmsSite site=siteMng.findById(siteId);
		for (Object[]  d: statisTicData) {
			CmsSiteAccessCountHour bean = new CmsSiteAccessCountHour();
			bean.setSite(site);
			bean.setAccessDate(date);
			Long pv = (Long) d[0];
			Long ip = (Long) d[1];
			Long visitor = (Long) d[2];
			Integer hour = (Integer) d[3];
			bean.setHourUv(visitor);
			bean.setHourPv(pv);
			bean.setHourIp(ip);
			bean.setAccessHour(hour);
			save(bean);
		}
	}

	@Transactional(readOnly = true)
	public CmsSiteAccessCountHour findById(Integer id) {
		CmsSiteAccessCountHour entity = dao.findById(id);
		return entity;
	}

	public CmsSiteAccessCountHour save(CmsSiteAccessCountHour bean) {
		dao.save(bean);
		return bean;
	}

	
	private CmsSiteAccessCountHourDao dao;
	@Autowired
	private CmsSiteAccessDao  cmsAccessDao;
	@Autowired
	private CmsSiteMng siteMng;

	@Autowired
	public void setDao(CmsSiteAccessCountHourDao dao) {
		this.dao = dao;
	}
}