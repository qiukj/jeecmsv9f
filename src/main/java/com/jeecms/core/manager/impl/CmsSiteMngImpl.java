package com.jeecms.core.manager.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.manager.assist.CmsResourceMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.core.dao.CmsSiteDao;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsSiteCompany;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsOssMng;
import com.jeecms.core.manager.CmsSiteCompanyMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.CmsUserSiteMng;
import com.jeecms.core.manager.FtpMng;

@Service
@Transactional
public class CmsSiteMngImpl implements CmsSiteMng {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSiteMngImpl.class);

	@Transactional(readOnly = true)
	public List<CmsSite> getList() {
		return dao.getList(false);
	}
	
	@Transactional(readOnly = true)
	public List<CmsSite> getListFromCache() {
		return dao.getList(true);
	}
	
	@Transactional(readOnly = true)
	public boolean hasRepeatByProperty(String property){
		return (getList().size()-dao.getCountByProperty(property))>0?true:false;
	}
	
	@Transactional(readOnly = true)
	public CmsSite findByDomain(String domain) {
		return dao.findByDomain(domain);
	}

	@Transactional(readOnly = true)
	public CmsSite findById(Integer id) {
		CmsSite entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public CmsSite update(CmsSite site) {
		Updater<CmsSite> updater = new Updater<CmsSite>(site);
		site = dao.updateByUpdater(updater);
		return site;
	}
	
	public CmsSite save(CmsSite currSite, CmsUser currUser, CmsSite bean,
			Integer uploadFtpId,Integer syncPageFtpId) throws IOException {
		if (uploadFtpId != null) {
			bean.setUploadFtp(ftpMng.findById(uploadFtpId));
		}
		if(syncPageFtpId!=null){
			bean.setSyncPageFtp(ftpMng.findById(syncPageFtpId));
		}
		bean.getConfig().getId();
		bean.init();
		dao.save(bean);
		// 复制本站模板
		cmsResourceMng.copyTplAndRes(currSite, bean);
		// 处理管理员
		cmsUserMng.addSiteToUser(currUser, bean, bean.getFinalStep());
		//保存站点相关公司信息
		CmsSiteCompany company=new CmsSiteCompany();
		company.setName(bean.getName());
		siteCompanyMng.save(bean,company);
		return bean;
	}

	public CmsSite update(CmsSite bean, Integer uploadFtpId,Integer syncPageFtpId,Integer ossId) {
		CmsSite entity = findById(bean.getId());
		if (uploadFtpId != null) {
			entity.setUploadFtp(ftpMng.findById(uploadFtpId));
		} else {
			entity.setUploadFtp(null);
		}
		if (syncPageFtpId != null) {
			entity.setSyncPageFtp(ftpMng.findById(syncPageFtpId));
		} else {
			entity.setSyncPageFtp(null);
		}
		if (ossId!=null) {
			entity.setUploadOss(ossMng.findById(ossId));
		}else{
			entity.setUploadOss(null);
		}
		Updater<CmsSite> updater = new Updater<CmsSite>(bean);
		entity = dao.updateByUpdater(updater);
		return entity;
	}

	public void updateTplSolution(Integer siteId, String solution,String mobileSol) {
		CmsSite site = findById(siteId);
		if(StringUtils.isNotBlank(solution)){
			site.setTplSolution(solution);
		}
		if(StringUtils.isNotBlank(mobileSol)){
			site.setTplMobileSolution(mobileSol);
		}
	}
	
	public void updateRefers(Integer siteId, Integer[] referIds){
		CmsSite site = findById(siteId);
		Set<CmsSite>refers=site.getRefers();
		refers.clear();
		for(Integer referId:referIds){
			refers.add(findById(referId));
		}
		site.setRefers(refers);
	}
	
	public void updateAttr(Integer siteId,Map<String,String>attr){
		CmsSite site = findById(siteId);
		site.getAttr().putAll(attr);
	}
	
	public void updateAttr(Integer siteId,Map<String,String>...attrs){
		CmsSite site = findById(siteId);
		for(Map<String,String>m:attrs){
			site.getAttr().putAll(m);
		}
	}

	public CmsSite deleteById(Integer id) {
		// 删除用户、站点关联
		cmsUserSiteMng.deleteBySiteId(id);
		CmsSite bean = dao.findById(id);
		bean.getRefers().clear();
		bean.getToRefers().clear();
		dao.deleteById(id);
		log.info("delete site "+id);
		// 删除模板
		/*
		try {
			cmsResourceMng.delTplAndRes(bean);
		} catch (IOException e) {
			log.error("delete Template and Resource fail!", e);
		}
		*/
		return bean;
	}

	public CmsSite[] deleteByIds(Integer[] ids) {
		CmsSite[] beans = new CmsSite[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private CmsUserMng cmsUserMng;
	private CmsUserSiteMng cmsUserSiteMng;
	private CmsResourceMng cmsResourceMng;
	private FtpMng ftpMng;
	private CmsSiteDao dao;
	@Autowired
	private CmsOssMng ossMng;
	@Autowired
	private CmsSiteCompanyMng siteCompanyMng;

	@Autowired
	public void setCmsUserMng(CmsUserMng cmsUserMng) {
		this.cmsUserMng = cmsUserMng;
	}

	@Autowired
	public void setCmsUserSiteMng(CmsUserSiteMng cmsUserSiteMng) {
		this.cmsUserSiteMng = cmsUserSiteMng;
	}

	@Autowired
	public void setCmsResourceMng(CmsResourceMng cmsResourceMng) {
		this.cmsResourceMng = cmsResourceMng;
	}

	@Autowired
	public void setFtpMng(FtpMng ftpMng) {
		this.ftpMng = ftpMng;
	}

	@Autowired
	public void setDao(CmsSiteDao dao) {
		this.dao = dao;
	}

	@Autowired
	public CmsOssMng getOssMng() {
		return ossMng;
	}

	@Autowired
	public void setOssMng(CmsOssMng ossMng) {
		this.ossMng = ossMng;
	}
}