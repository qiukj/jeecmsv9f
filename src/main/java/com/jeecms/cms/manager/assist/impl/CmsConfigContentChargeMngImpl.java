package com.jeecms.cms.manager.assist.impl;

import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.security.encoder.Md5PwdEncoder;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.cms.dao.assist.CmsConfigContentChargeDao;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;

@Service
@Transactional
public class CmsConfigContentChargeMngImpl implements CmsConfigContentChargeMng {
	
	@Transactional(readOnly = true)
	public CmsConfigContentCharge findById(Integer id) {
		CmsConfigContentCharge entity = dao.findById(id);
		return entity;
	}
	
	public CmsConfigContentCharge getDefault() {
		return findById(1);
	}

	public CmsConfigContentCharge update(CmsConfigContentCharge bean,
			String payTransferPassword,Map<String,String> keys,Map<String,String>fixVal) {
		Updater<CmsConfigContentCharge> updater = new Updater<CmsConfigContentCharge>(bean);
		for(Entry<String, String> att:keys.entrySet()){
			if(StringUtils.isBlank(att.getValue())){
				updater.exclude(att.getKey());
			}
		}
		if(StringUtils.isBlank(payTransferPassword)){
			updater.exclude("payTransferPassword");
		}else{
			bean.setPayTransferPassword(pwdEncoder.encodePassword(payTransferPassword));;
		}
		if(fixVal!=null){
			cmsConfigMng.updateRewardFixAttr(fixVal);
		}
		bean = dao.updateByUpdater(updater);
		return bean;
	}
	
	public CmsConfigContentCharge afterUserPay(Double payAmout){
		CmsConfigContentCharge config=getDefault();
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if(config.getLastBuyTime()!=null){
			last.setTime(config.getLastBuyTime());
			int currDay = curr.get(Calendar.DAY_OF_YEAR);
			int lastDay = last.get(Calendar.DAY_OF_YEAR);
			int currYear=curr.get(Calendar.YEAR);
			int lastYear=last.get(Calendar.YEAR);
			int currMonth = curr.get(Calendar.MONTH);
			int lastMonth = last.get(Calendar.MONTH);
			if(lastYear!=currYear){
				config.setCommissionYear(0d);
				config.setCommissionMonth(0d);
				config.setCommissionDay(0d);
			}else{
				if(currMonth!=lastMonth){
					config.setCommissionMonth(0d);
					config.setCommissionDay(0d);
				}else{
					if (currDay != lastDay) {
						config.setCommissionDay(0d);
					}
				}
			}
		}
		config.setCommissionDay(config.getCommissionDay()+payAmout);
		config.setCommissionMonth(config.getCommissionMonth()+payAmout);
		config.setCommissionYear(config.getCommissionYear()+payAmout);
		config.setCommissionTotal(config.getCommissionTotal()+payAmout);
		config.setLastBuyTime(curr.getTime());
		return config;
	}


	private CmsConfigContentChargeDao dao;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Autowired
	private CmsConfigMng cmsConfigMng;

	@Autowired
	public void setDao(CmsConfigContentChargeDao dao) {
		this.dao = dao;
	}
}