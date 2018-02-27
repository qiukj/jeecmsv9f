package com.jeecms.cms.manager.main.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.dao.main.ChannelDao;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.ChannelCount;
import com.jeecms.cms.entity.main.ChannelExt;
import com.jeecms.cms.entity.main.ChannelTxt;
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.manager.main.ChannelCountMng;
import com.jeecms.cms.manager.main.ChannelExtMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.ChannelTxtMng;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.cms.service.ChannelDeleteChecker;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserSite;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.CmsUserMng;

@Service
@Transactional
public class ChannelMngImpl implements ChannelMng {
	@Transactional(readOnly = true)
	public List<Channel> getTopList(Integer siteId, boolean hasContentOnly) {
		return dao.getTopList(siteId, hasContentOnly, false, true);
	}

	@Transactional(readOnly = true)
	public List<Channel> getTopListByRigth(Integer userId, Integer siteId,
			boolean hasContentOnly) {
		CmsUser user = cmsUserMng.findById(userId);
		CmsUserSite us = user.getUserSite(siteId);
		if (us.getAllChannel()) {
			return getTopList(siteId, hasContentOnly);
		} else {
			return dao.getTopListByRigth(userId, siteId, hasContentOnly);
		}
	}

	@Transactional(readOnly = true)
	public List<Channel> getTopListForTag(Integer siteId, boolean hasContentOnly) {
		return dao.getTopList(siteId,hasContentOnly, true, true);
	}

	@Transactional(readOnly = true)
	public Pagination getTopPageForTag(Integer siteId, boolean hasContentOnly,
			int pageNo, int pageSize) {
		return dao.getTopPage(siteId, hasContentOnly, false, true, pageNo,
				pageSize);
	}

	@Transactional(readOnly = true)
	public List<Channel> getChildList(Integer parentId, boolean hasContentOnly) {
		return dao.getChildList(parentId, hasContentOnly, false, true);
	}

	@Transactional(readOnly = true)
	public List<Channel> getChildListByRight(Integer userId, Integer siteId,
			Integer parentId, boolean hasContentOnly) {
		CmsUser user = cmsUserMng.findById(userId);
		CmsUserSite us = user.getUserSite(siteId);
		if (us.getAllChannel()) {
			return getChildList(parentId, hasContentOnly);
		} else {
			return dao.getChildListByRight(userId, parentId, hasContentOnly);
		}
	}

	@Transactional(readOnly = true)
	public List<Channel> getChildListForTag(Integer parentId,
			boolean hasContentOnly) {
		return dao.getChildList(parentId, hasContentOnly, true, true);
	}
	
	@Transactional(readOnly = true)
	public List<Channel> getBottomList(Integer siteId,boolean hasContentOnly){
		return dao.getBottomList(siteId,hasContentOnly);
	}

	@Transactional(readOnly = true)
	public Pagination getChildPageForTag(Integer parentId,
			boolean hasContentOnly, int pageNo, int pageSize) {
		return dao.getChildPage(parentId, hasContentOnly, true, true, pageNo,
				pageSize);
	}

	@Transactional(readOnly = true)
	public Channel findById(Integer id) {
		Channel entity = dao.findById(id);
		return entity;
	}

	@Transactional(readOnly = true)
	public Channel findByPath(String path, Integer siteId) {
		return dao.findByPath(path, siteId, false);
	}

	@Transactional(readOnly = true)
	public Channel findByPathForTag(String path, Integer siteId) {
		return dao.findByPath(path, siteId, true);
	}

	public Channel save(Channel bean, ChannelExt ext, ChannelTxt txt,
			Integer[] viewGroupIds, Integer[] contriGroupIds,
			Integer[] userIds, Integer siteId, Integer parentId,
			Integer modelId,Integer[]modelIds,
			String[] tpls,String[] mtpls,boolean isCopy) {
		if (parentId != null) {
			bean.setParent(findById(parentId));
		}else{
			bean.setParent(null);
		}
		CmsSite site=cmsSiteMng.findById(siteId);
		bean.setSite(site);
		CmsModel model = cmsModelMng.findById(modelId);
		bean.setModel(model);
		bean.setHasContent(model.getHasContent());
		bean.init();
		bean=dao.save(bean);
		channelExtMng.save(ext, bean);
		channelTxtMng.save(txt, bean);
		channelCountMng.save(new ChannelCount(), bean);
		CmsGroup g;
		if (viewGroupIds != null && viewGroupIds.length > 0) {
			for (Integer gid : viewGroupIds) {
				g = cmsGroupMng.findById(gid);
				bean.addToViewGroups(g);
			}
		}
		if (contriGroupIds != null && contriGroupIds.length > 0) {
			for (Integer gid : contriGroupIds) {
				g = cmsGroupMng.findById(gid);
				bean.addToContriGroups(g);
			}
		}
		if (modelIds != null && modelIds.length > 0) {
			for (int i=0;i<modelIds.length;i++) {
				CmsModel m = cmsModelMng.findById(modelIds[i]);
				bean.addToChannelModels(m, tpls[i],mtpls[i]);
			}
		}
		CmsUser u;
		if (userIds != null && userIds.length > 0) {
			for (Integer uid : userIds) {
				u = cmsUserMng.findById(uid);
				bean.addToUsers(u);
			}
		}
		return bean;
	}
	
	public Channel copy(Integer cid,String solution, 
			String mobileSolution, Integer siteId,
			Map<String, String> pathMap){
		Channel c=findById(cid);
		Channel channel=new Channel();
		if(c!=null){
			ChannelExt ext=new ChannelExt();
			ChannelTxt channelTxt=new ChannelTxt();
			channel=(Channel) c.clone();
			ext=(ChannelExt) c.getChannelExt().clone();
			if(c.getChannelTxt()!=null){
				channelTxt=(ChannelTxt) c.getChannelTxt().clone();
			}
			String path=c.getPath();
			//判断栏目路径是否重复，重复则创建新的栏目路径,需要存储对应关系，方便建立子栏目的对应关系结构
			String newpath=getChannelPath(path, siteId);
			pathMap.put(path, newpath);
			channel.setPath(newpath);
			Integer parentId=null;
			//有上层栏目，则新复制的栏目上层需要查找路径同上层栏目路径，新的栏目parent不是当前栏目的父栏目
			//新栏目的路径从pathMap中获取
			if(c.getParent()!=null){
				Channel newParent=findByPath(
						pathMap.get(c.getParent().getPath()), siteId);
				if(newParent!=null){
					parentId=newParent.getId();
				}
			}
			String oldSolution=c.getSite().getTplSolution();
			String oldMobileSolution=c.getSite().getTplMobileSolution();
			if(StringUtils.isNotBlank(ext.getTplChannel())
					&&StringUtils.isNotBlank(solution)){
				ext.setTplChannel(ext.getTplChannel().replace(oldSolution, solution));
			}
			if(StringUtils.isNotBlank(ext.getTplContent())
					&&StringUtils.isNotBlank(solution)){
				ext.setTplContent(ext.getTplContent().replace(oldSolution, solution));
			}
			if(StringUtils.isNotBlank(ext.getTplMobileChannel())
					&&StringUtils.isNotBlank(mobileSolution)){
				ext.setTplMobileChannel(ext.getTplMobileChannel().replace(oldMobileSolution, mobileSolution));
			}
			channel.setChannelExt(ext);
			ext.setChannel(channel);
			channelTxt.setChannel(channel);
			CmsSite site=cmsSiteMng.findById(siteId);
			
			// 加上模板前缀
			String tplPath = site.getTplPath();
			String[]tpls=c.getModelTplStrs();
			String[]mtpls=c.getMobileModelTplStrs();
			if(tpls!=null&&tpls.length>0){
				for(int t=0;t<tpls.length;t++){
					if (!StringUtils.isBlank(tpls[t])) {
						tpls[t]=tplPath+tpls[t];
						if(StringUtils.isNotBlank(solution)){
							tpls[t]=tpls[t].replace(oldSolution, solution);
						}
					}
				}
			}
			if(mtpls!=null&&mtpls.length>0){
				for(int t=0;t<mtpls.length;t++){
					if (!StringUtils.isBlank(mtpls[t])) {
						mtpls[t]=tplPath+mtpls[t];
						if(StringUtils.isNotBlank(mobileSolution)){
							mtpls[t]=mtpls[t].replace(oldMobileSolution, mobileSolution);
						}
					}
				}
			}
			channel=save(channel, ext, channelTxt,
					c.getViewGroupIds(), c.getContriGroupIds(),
					c.getUserIds(), siteId ,parentId, c.getModel().getId(),
					c.getModelIntIds(),tpls,mtpls,true);
		}
		return channel;
	}

	public Channel update(Channel bean, ChannelExt ext, ChannelTxt txt,
			Integer[] viewGroupIds, Integer[] contriGroupIds,
			Integer[] userIds, Integer parentId, Map<String, String> attr, Integer modelId,
			Integer[]modelIds,String[] tpls,String[] mtpls) {
		// 更新主表
		Updater<Channel> updater = new Updater<Channel>(bean);
		bean = dao.updateByUpdater(updater);
		// 更新父栏目
		Channel parent;
		if (parentId != null) {
			parent = findById(parentId);
		} else {
			parent = null;
		}
		if(modelId!=null){
			CmsModel model = cmsModelMng.findById(modelId);
			bean.setModel(model);
			bean.setHasContent(model.getHasContent());
		}
		bean.setParent(parent);
		// 更新扩展表
		channelExtMng.update(ext);
		// 更新文本表
		channelTxtMng.update(txt, bean);
		// 更新属性表
		Map<String, String> attrOrig = bean.getAttr();
		attrOrig.clear();
		attrOrig.putAll(attr);
		// 更新浏览会员组关联
		for (CmsGroup g : bean.getViewGroups()) {
			g.getViewChannels().remove(bean);
		}
		bean.getViewGroups().clear();
		if (viewGroupIds != null && viewGroupIds.length > 0) {
			CmsGroup g;
			for (Integer gid : viewGroupIds) {
				g = cmsGroupMng.findById(gid);
				bean.addToViewGroups(g);
			}
		}
		// 更新投稿会员组关联
		for (CmsGroup g : bean.getContriGroups()) {
			g.getContriChannels().remove(bean);
		}
		bean.getContriGroups().clear();
		if (contriGroupIds != null && contriGroupIds.length > 0) {
			CmsGroup g;
			for (Integer gid : contriGroupIds) {
				g = cmsGroupMng.findById(gid);
				bean.addToContriGroups(g);
			}
		}
		bean.getChannelModels().clear();
		if (modelIds != null && modelIds.length > 0) {
			for (int i=0;i<modelIds.length;i++) {
				CmsModel m = cmsModelMng.findById(modelIds[i]);
				bean.addToChannelModels(m, tpls[i],mtpls[i]);
			}
		}
		// 更新管理员关联
		for (CmsUser u : bean.getUsers()) {
			u.getChannels().remove(bean);
		}
		bean.getUsers().clear();
		if (userIds != null && userIds.length > 0) {
			CmsUser u;
			for (Integer uid : userIds) {
				u = cmsUserMng.findById(uid);
				bean.addToUsers(u);
			}
		}
		return bean;
	}

	public Channel deleteById(Integer id) {
		Channel entity = dao.findById(id);
		for (CmsGroup group : entity.getViewGroups()) {
			group.getViewChannels().remove(entity);
		}
		for (CmsGroup group : entity.getContriGroups()) {
			group.getContriChannels().remove(entity);
		}
		entity = dao.deleteById(id);
		return entity;
	}

	public Channel[] deleteByIds(Integer[] ids) {
		Channel[] beans = new Channel[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public String checkDelete(Integer id) {
		String msg = null;
		for (ChannelDeleteChecker checker : deleteCheckerList) {
			msg = checker.checkForChannelDelete(id);
			if (msg != null) {
				return msg;
			}
		}
		return msg;
	}

	public Channel[] updatePriority(Integer[] ids, Integer[] priority) {
		int len = ids.length;
		Channel[] beans = new Channel[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}
	
	public void initWorkFlow(Integer workflowId){
		dao.initWorkFlow(workflowId);
	}

	private List<ChannelDeleteChecker> deleteCheckerList;

	public void setDeleteCheckerList(
			List<ChannelDeleteChecker> deleteCheckerList) {
		this.deleteCheckerList = deleteCheckerList;
	}
	
	private String getChannelPath(String path,Integer siteId){
		Channel findChannel=findByPath(path, siteId);
		if(findChannel!=null){
			path=path+RandomUtils.nextInt();
			return getChannelPath(path, siteId);
		}
		return path;
	}

	private CmsSiteMng cmsSiteMng;
	private CmsModelMng cmsModelMng;
	private ChannelExtMng channelExtMng;
	private ChannelTxtMng channelTxtMng;
	private ChannelCountMng channelCountMng;
	private CmsUserMng cmsUserMng;
	private CmsGroupMng cmsGroupMng;
	private ChannelDao dao;

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}

	@Autowired
	public void setCmsModelMng(CmsModelMng cmsModelMng) {
		this.cmsModelMng = cmsModelMng;
	}

	@Autowired
	public void setChannelExtMng(ChannelExtMng channelExtMng) {
		this.channelExtMng = channelExtMng;
	}

	@Autowired
	public void setChannelTxtMng(ChannelTxtMng channelTxtMng) {
		this.channelTxtMng = channelTxtMng;
	}

	@Autowired
	public void setChannelCountMng(ChannelCountMng channelCountMng) {
		this.channelCountMng = channelCountMng;
	}

	@Autowired
	public void setCmsUserMng(CmsUserMng cmsUserMng) {
		this.cmsUserMng = cmsUserMng;
	}

	@Autowired
	public void setCmsGroupMng(CmsGroupMng cmsGroupMng) {
		this.cmsGroupMng = cmsGroupMng;
	}
	
	@Autowired
	public void setDao(ChannelDao dao) {
		this.dao = dao;
	}

}