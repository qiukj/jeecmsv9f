package com.jeecms.cms.manager.main.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.dao.main.ApiRecordDao;
import com.jeecms.cms.entity.main.ApiInfo;
import com.jeecms.cms.entity.main.ApiRecord;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiInfoMng;
import com.jeecms.cms.manager.main.ApiRecordMng;

@Service
@Transactional
public class ApiRecordMngImpl implements ApiRecordMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ApiRecord findById(Long id) {
		ApiRecord entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ApiRecord findBySign(String sign,String appId) {
		ApiRecord entity = dao.findBySign(sign,appId);
		return entity;
	}
	
	public ApiRecord callApiRecord(String ip,String appId,String apiUrl,String sign){
		ApiRecord record =new ApiRecord();
		record.setCallIp(ip);
		record.setCallTime(Calendar.getInstance().getTime());
		record.setCallTimeStamp(System.currentTimeMillis());
		record.setApiAccount(apiAccountMng.findByAppId(appId));
		record.setSign(sign);
		ApiInfo info=apiInfoMng.findByUrl(apiUrl);
		if(info!=null){
			record.setApiInfo(info);
			statisCallCount(info);
		}
		record=save(record);
		return record;
				
	}
	
	private void statisCallCount(ApiInfo info) {
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if(info.getLastCallTime()!=null){
			last.setTime(info.getLastCallTime());
			int currDay = curr.get(Calendar.DAY_OF_YEAR);
			int lastDay = last.get(Calendar.DAY_OF_YEAR);
			if (currDay != lastDay) {
				int currWeek = curr.get(Calendar.WEEK_OF_YEAR);
				int lastWeek = last.get(Calendar.WEEK_OF_YEAR);
				int currMonth = curr.get(Calendar.MONTH);
				int lastMonth = last.get(Calendar.MONTH);
				if (currWeek != lastWeek) {
					info.setCallWeekCount(0);
				}
				if (currMonth != lastMonth) {
					info.setCallMonthCount(0);
				}
				info.setCallDayCount(0);
			}
			info.setCallDayCount(info.getCallDayCount()+1);
			info.setCallWeekCount(info.getCallWeekCount()+1);
			info.setCallMonthCount(info.getCallMonthCount()+1);
			info.setCallTotalCount(info.getCallTotalCount()+1);
		}else{
			info.setCallDayCount(1);
			info.setCallWeekCount(1);
			info.setCallMonthCount(1);
			info.setCallTotalCount(1);
		}
		info.setLastCallTime(curr.getTime());
		apiInfoMng.update(info);
	}

	public ApiRecord save(ApiRecord bean) {
		dao.save(bean);
		return bean;
	}

	public ApiRecord update(ApiRecord bean) {
		Updater<ApiRecord> updater = new Updater<ApiRecord>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ApiRecord deleteById(Long id) {
		ApiRecord bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiRecord[] deleteByIds(Long[] ids) {
		ApiRecord[] beans = new ApiRecord[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ApiRecordDao dao;
	@Autowired
	private ApiInfoMng apiInfoMng;
	@Autowired
	private ApiAccountMng apiAccountMng;

	@Autowired
	public void setDao(ApiRecordDao dao) {
		this.dao = dao;
	}
}