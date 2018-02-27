package com.jeecms.cms.manager.assist.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.dao.assist.CmsWebserviceCallRecordDao;
import com.jeecms.cms.entity.assist.CmsWebserviceCallRecord;
import com.jeecms.cms.manager.assist.CmsWebserviceAuthMng;
import com.jeecms.cms.manager.assist.CmsWebserviceCallRecordMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class CmsWebserviceCallRecordMngImpl implements CmsWebserviceCallRecordMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public CmsWebserviceCallRecord findById(Integer id) {
		CmsWebserviceCallRecord entity = dao.findById(id);
		return entity;
	}
	
	public CmsWebserviceCallRecord save(String clientUsername,String serviceCode){
		CmsWebserviceCallRecord record=new CmsWebserviceCallRecord();
		record.setAuth(bbsWebserviceAuthMng.findByUsername(clientUsername));
		record.setRecordTime(Calendar.getInstance().getTime());
		record.setServiceCode(serviceCode);
		return save(record);
	}

	public CmsWebserviceCallRecord save(CmsWebserviceCallRecord bean) {
		dao.save(bean);
		return bean;
	}

	public CmsWebserviceCallRecord update(CmsWebserviceCallRecord bean) {
		Updater<CmsWebserviceCallRecord> updater = new Updater<CmsWebserviceCallRecord>(bean);
		CmsWebserviceCallRecord entity = dao.updateByUpdater(updater);
		return entity;
	}

	public CmsWebserviceCallRecord deleteById(Integer id) {
		CmsWebserviceCallRecord bean = dao.deleteById(id);
		return bean;
	}
	
	public CmsWebserviceCallRecord[] deleteByIds(Integer[] ids) {
		CmsWebserviceCallRecord[] beans = new CmsWebserviceCallRecord[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Autowired
	private CmsWebserviceAuthMng bbsWebserviceAuthMng;
	private CmsWebserviceCallRecordDao dao;

	@Autowired
	public void setDao(CmsWebserviceCallRecordDao dao) {
		this.dao = dao;
	}
}