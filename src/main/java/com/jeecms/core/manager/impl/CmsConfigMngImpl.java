package com.jeecms.core.manager.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.service.ContentQueryFreshTimeCache;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.core.dao.CmsConfigDao;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsConfigAttr;
import com.jeecms.core.entity.MarkConfig;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.manager.CmsConfigMng;

@Service
@Transactional
public class CmsConfigMngImpl implements CmsConfigMng {
	@Transactional(readOnly = true)
	public CmsConfig get() {
		CmsConfig entity = dao.findById(1);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public Integer getContentFreshMinute() {
		CmsConfig entity = get();
		return entity.getContentFreshMinute();
	}

	public void updateCountCopyTime(Date d) {
		dao.findById(1).setCountCopyTime(d);
	}

	public void updateCountClearTime(Date d) {
		dao.findById(1).setCountClearTime(d);
	}
	
	public void updateFlowClearTime(Date d) {
		dao.findById(1).setFlowClearTime(d);
	}
	
	public void updateChannelCountClearTime(Date d) {
		dao.findById(1).setChannelCountClearTime(d);
	}


	public CmsConfig update(CmsConfig bean) {
		Updater<CmsConfig> updater = new Updater<CmsConfig>(bean);
		CmsConfig entity = dao.updateByUpdater(updater);
		entity.blankToNull();
		return entity;
	}

	public MarkConfig updateMarkConfig(MarkConfig mark) {
		get().setMarkConfig(mark);
		return mark;
	}

	public void updateMemberConfig(MemberConfig memberConfig) {
		get().getAttr().putAll(memberConfig.getAttr());
	}

	public void updateConfigAttr(CmsConfigAttr configAttr) {
		Map<String,String>attrMap=configAttr.getAttr();
		if(StringUtils.isBlank(attrMap.get(CmsConfigAttr.WEIXIN_APP_ID))){
			attrMap.remove(CmsConfigAttr.WEIXIN_APP_ID);
		}
		if(StringUtils.isBlank(attrMap.get(CmsConfigAttr.WEIXIN_APP_SECRET))){
			attrMap.remove(CmsConfigAttr.WEIXIN_APP_SECRET);
		}
		if(StringUtils.isBlank(attrMap.get(CmsConfigAttr.WEIXIN_LOGIN_ID))){
			attrMap.remove(CmsConfigAttr.WEIXIN_LOGIN_ID);
		}
		if(StringUtils.isBlank(attrMap.get(CmsConfigAttr.WEIXIN_LOGIN_SECRET))){
			attrMap.remove(CmsConfigAttr.WEIXIN_LOGIN_SECRET);
		}
		get().getAttr().putAll(attrMap);
		contentQueryFreshTimeCache.setInterval(getContentFreshMinute());
	}
	
	public void updateSsoAttr(Map<String,String> ssoAttr){
		Map<String,String> oldAttr=get().getAttr();
		Iterator<String> keys = oldAttr.keySet().iterator();
	    String key = null;
	    while (keys.hasNext()) {
		    key = (String) keys.next();
		    if (key.startsWith("sso_")){
		      keys.remove();
		     }
	    }
		oldAttr.putAll(ssoAttr);
	}
	
	public void updateRewardFixAttr(Map<String,String> fixAttr){
		Map<String,String> oldAttr=get().getAttr();
		Iterator<String> keys = oldAttr.keySet().iterator();
	    String key = null;
	    while (keys.hasNext()) {
		    key = (String) keys.next();
		    if (key.startsWith(CmsConfig.REWARD_FIX_PREFIX)){
		      keys.remove();
		     }
	    }
		oldAttr.putAll(fixAttr);
	}

	private CmsConfigDao dao;
	@Autowired
	private ContentQueryFreshTimeCache contentQueryFreshTimeCache;

	@Autowired
	public void setDao(CmsConfigDao dao) {
		this.dao = dao;
	}
}