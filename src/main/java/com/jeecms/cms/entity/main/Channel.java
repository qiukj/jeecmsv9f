package com.jeecms.cms.entity.main;

import static com.jeecms.common.web.Constants.INDEX;
import static com.jeecms.common.web.Constants.SPT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseChannel;
import com.jeecms.cms.staticpage.StaticPageUtils;
import com.jeecms.common.hibernate4.HibernateTree;
import com.jeecms.common.hibernate4.PriorityComparator;
import com.jeecms.common.hibernate4.PriorityInterface;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;

/**
 * 栏目实体类
 */
public class Channel extends BaseChannel implements HibernateTree<Integer>, PriorityInterface, Cloneable {
	private static final long serialVersionUID = 1L;

	public JSONObject convertToJson(Integer https, boolean showChild, boolean showTxt, List<CmsModel> modelList) {
		JSONObject json = new JSONObject();
		if (getId() != null) {
			json.put("id", getId());
		} else {
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getPath())) {
			json.put("path", getPath());
		} else {
			json.put("path", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		} else {
			json.put("priority", "");
		}
		json.put("hasContent", getHasContent());
		json.put("display", getDisplay());
		json.put("deep", getDeep());
		
		//ext对象
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		} else {
			json.put("name", "");
		}

		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		} else {
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(getKeywords())) {
			json.put("keywords", getKeywords());
		} else {
			json.put("keywords", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		} else {
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getLink())) {
			json.put("link", getLink());
		} else {
			json.put("link", "");
		}
		json.put("finalStep", getFinalStep());
		json.put("afterCheck", getAfterCheck());
		json.put("staticChannel", getStaticChannel());
		json.put("staticContent", getStaticContent());
		json.put("accessByDir", getAccessByDir());
		json.put("listChild", getListChild());
		json.put("pageSize", getPageSize());
		if (StringUtils.isNotBlank(getChannelRule())) {
			json.put("channelRule", getChannelRule());
		} else {
			json.put("channelRule", "");
		}
		if (StringUtils.isNotBlank(getContentRule())) {
			json.put("contentRule", getContentRule());
		} else {
			json.put("contentRule", "");
		}
		// 当前模板，去除基本路径
		int tplPathLength = 0;
		if (getSite()!=null&&getSite().getTplPath()!=null) {
			tplPathLength = getSite().getTplPath().length();
		}
		String tplChannel = getTplChannel();
		String tplMobileChannel = getMobileTplChannel();
		if (StringUtils.isNotBlank(tplChannel)) {
			tplChannel = tplChannel.substring(tplPathLength);
			json.put("tplChannel", tplChannel);
		} else {
			json.put("tplChannel", "");
		}
		if (StringUtils.isNotBlank(tplMobileChannel)) {
			tplMobileChannel = tplMobileChannel.substring(tplPathLength);
			json.put("tplMobileChannel", tplMobileChannel);
		} else {
			json.put("tplMobileChannel", "");
		}
		json.put("titleImgWidth", getTitleImgWidth());
		json.put("titleImgHeight", getTitleImgHeight());
		json.put("contentImgWidth", getContentImgWidth());
		json.put("contentImgHeight", getContentImgHeight());
		
		json.put("commentControl", getCommentControl());
		json.put("allowUpdown", getAllowUpdown());
		json.put("allowShare", getAllowShare());
		json.put("allowScore", getAllowScore());
		json.put("blank", getBlank());
		if (https == com.jeecms.cms.api.Constants.URL_HTTP) {
			if (getId()!=null&&!getId().equals(0)) {
				json.put("url", getUrl());
			}else{
				json.put("url", "");
			}
			if (getSite()!=null&&StringUtils.isNotBlank(getSite().getUrl())) {
				json.put("siteUrl", getSite().getUrl());
			}else{
				json.put("siteUrl", "");
			}
		} else {
			if (getId()!=null&&!getId().equals(0)) {
				json.put("url", getHttpsUrl());
			}else{
				json.put("url", "");
			}
			if (getSite()!=null && StringUtils.isNotBlank(getSite().getHttpsUrl())) {
				json.put("siteUrl", getSite().getHttpsUrl());
			}else {
				json.put("siteUrl", "");
			}
			
		}
		if (StringUtils.isNotBlank(getTitleImg())) {
			json.put("titleImg", getTitleImg());
		} else {
			json.put("titleImg", "");
		}
		if (StringUtils.isNotBlank(getContentImg())) {
			json.put("contentImg", getContentImg());
		} else {
			json.put("contentImg", "");
		}
		json.put("hasTitleImg", getHasTitleImg());
		json.put("hasContentImg", getHasContentImg());
		if(showTxt){
			//文本对象
			if (StringUtils.isNotBlank(getTxt())) {
				json.put("txt", getTxt());
			} else {
				json.put("txt", "");
			}
			if (StringUtils.isNotBlank(getTxt1())) {
				json.put("txt1", getTxt1());
			} else {
				json.put("txt1", "");
			}
			if (StringUtils.isNotBlank(getTxt2())) {
				json.put("txt2", getTxt2());
			} else {
				json.put("txt2", "");
			}
			if (StringUtils.isNotBlank(getTxt3())) {
				json.put("txt3", getTxt3());
			} else {
				json.put("txt3", "");
			}
		}
		if (getChild() != null) {
			json.put("childCount", getChild().size());
		} else {
			json.put("childCount", "");
		}
		
		Set<CmsGroup> groups= getViewGroups();
		JSONArray viewGroupIds=new JSONArray();
		JSONArray contriGroupIds=new JSONArray();
		JSONArray modelsJsonArray=new JSONArray();
		JSONArray selfModelsJsonArray=new JSONArray();
		JSONArray tpls=new JSONArray();
		JSONArray mtpls=new JSONArray();
		if (groups!=null&&groups.size()>0) {
			Iterator<CmsGroup>vewGroupIt=groups.iterator();
			int i=0;
			while(vewGroupIt.hasNext()){
				viewGroupIds.put(i++, vewGroupIt.next().getId());
			}
		}
		if (getContriGroups()!=null) {
			Iterator<CmsGroup>contriGroupIt=getContriGroups().iterator();
			int c=0;
			while(contriGroupIt.hasNext()){
				contriGroupIds.put(c++, contriGroupIt.next().getId());
			}
		}
		// 当前模板，去除基本路径
		List<ChannelModel>channelModels=getChannelModels();
		if(modelList!=null&&modelList.size()>0){
			for(int m=0;m<modelList.size();m++){
				boolean hasPush=false;
				if (channelModels != null) {
					for(int j=0;j<channelModels.size();j++){
						if (channelModels.get(j).getModel().getId().equals(modelList.get(m).getId())) {
							selfModelsJsonArray.put(m,channelModels.get(j).getModel().convertToJson());
							modelsJsonArray.put(m,channelModels.get(j).getModel().convertToJson());
							String tpl = channelModels.get(j).getTplContent();
							if (StringUtils.isNotBlank(tpl)) {
								tpls.put(m,tpl.substring(tplPathLength));
							} else {
								tpls.put(m,"");
							}
							String mobileTplContent = channelModels.get(j).getTplMoibleContent();
							if (StringUtils.isNotBlank(mobileTplContent)) {
								mtpls.put(m,mobileTplContent.substring(tplPathLength));
							} else {
								mtpls.put(m,"");
							}
							hasPush=true;
							break;
						}	
					}
				}
				if(!hasPush){
					modelsJsonArray.put(m,"");
					tpls.put(m,"");
					mtpls.put(m,"");
				}
			}
		}else{
			if (channelModels != null) {
				for(int j=0;j<channelModels.size();j++){
					modelsJsonArray.put(j,channelModels.get(j).getModel().convertToJson());
				}
			}
			List<String>modelTplList=getModelTpls();
			for(int j=0;j<modelTplList.size();j++){
				tpls.put(j,modelTplList.get(j));
			}
			
			List<String>mobileModelTplList=getMobileModelTpls();
			for(int j=0;j<mobileModelTplList.size();j++){
				mtpls.put(j,mobileModelTplList.get(j));
			}
		}
		
		json.put("viewGroupIds", viewGroupIds);
		json.put("contriGroupIds", contriGroupIds);
		json.put("models", modelsJsonArray);
		json.put("selfModels", selfModelsJsonArray);
		json.put("tpls", tpls);
		json.put("mtpls", mtpls);
		json.put("nodeIds", getNodeIds());
		if (getSite()!=null && StringUtils.isNotBlank(getSite().getName())) {
			json.put("siteName", getSite().getName());
		}else{
			json.put("siteName", "");
		}
		if (getSite()!=null && getSite().getId()!=null) {
			json.put("siteId", getSite().getId());
		}else{
			json.put("siteId", "");
		}
		if (getModel()!=null && StringUtils.isNotBlank(getModel().getName())) {
			json.put("model", getModel().getName());
		}else{
			json.put("model", "");
		}
		if (getModel()!=null && getModel().getId()!=null) {
			json.put("modelId", getModel().getId());
		}else{
			json.put("modelId", "");
		}
		if (getParent() != null) {
			json.put("parentId", getParent().getId());
			json.put("parentName", getParent().getName());
			json.put("parentUrl", getParent().getUrl());
			json.put("parentTxt", getParent().getTxt());
			json.put("parentPath", getParent().getPath());
			json.put("parentTitle", getParent().getTitle());
		}else{
			json.put("parentId", "");
			json.put("parentName", "");
			json.put("parentUrl", "");
			json.put("parentTxt", "");
			json.put("parentPath", "");
			json.put("parentTitle","");
		}

		if (getTopChannel() != null) {
			json.put("topId", getTopChannel().getId());
			json.put("topName", getTopChannel().getName());
			json.put("topUrl", getTopChannel().getUrl());
			json.put("topTxt", getTopChannel().getTxt());
			json.put("topPath", getTopChannel().getPath());
			json.put("topTitle", getTopChannel().getTitle());
		}else{
			json.put("topId", "");
			json.put("topName","");
			json.put("topUrl", "");
			json.put("topTxt","");
			json.put("topPath","");
			json.put("topTitle", "");
		}
		json.put("workflowId", "");
		json.put("workflowName", "");
		CmsModel model=getModel();
		Map<String,String>attr=getAttr();
		if (attr!=null) {
			for(String key:attr.keySet()){
				CmsModelItem item=model.findModelItem(key, true);
				//多选需要传递数组方便前端处理
				if (item!=null) {
					if(item.getDataType().equals(CmsModelItem.DATA_TYPE_CHECKBOX)){
						String[]attrValArray=null;
						JSONArray jsonArray=new JSONArray();
						if(StringUtils.isNotBlank(attr.get(key))){
							attrValArray=attr.get(key).split(",");
							if(attrValArray!=null){
								for(int k=0;k<attrValArray.length;k++){
									jsonArray.put(k,attrValArray[k]);
								}
							}
						}
						json.put("attr_"+key, jsonArray);
					}else{
						json.put("attr_"+key, attr.get(key));
					}
				}
			}
		}
		//展示用
		if (getChannelCount()!=null && getChannelCount().getViews()!=null) {
			json.put("views", getChannelCount().getViews());
		}else{
			json.put("views", "");
		}
		if (getChannelCount()!=null && getChannelCount().getViewsMonth()!=null) {
			json.put("viewsMonth", getChannelCount().getViewsMonth());
		}else{
			json.put("viewsMonth", "");
		}
		if (getChannelCount()!=null && getChannelCount().getViewsWeek()!=null) {
			json.put("viewsWeek", getChannelCount().getViewsWeek());
		}else{
			json.put("viewsWeek", "");
		}
		if (getChannelCount()!=null && getChannelCount().getViewsDay()!=null) {
			json.put("viewsDay", getChannelCount().getViewsDay());
		}else{
			json.put("viewsDay", "");
		}
		if(showChild){
			getChildJson(json, https, getChild(), showChild);
		}
		return json;
	}
	
	private JSONObject getChildJson(JSONObject parent,
			Integer https,Set<Channel> channels,boolean showChild){
		JSONArray childArray=new JSONArray();
		Iterator<Channel>it=channels.iterator();
		int i=0;
		while(it.hasNext()) {
			Channel c = it.next();
			childArray.put(i++,c.convertToJson(https, showChild, false, null));
			if(c.getChild()!=null&&c.getChild().size()>0){
				getChildJson(c.convertToJson(https, showChild, false, null), 
						https, c.getChild(), showChild);
			}
		}
		parent.put("child", childArray);
		return parent;
	}

	public Object clone() {  
		 Channel c = null;
         try{
              c = (Channel)super.clone();
              c.setChild(null);
              c.setViewGroups(null);
              c.setContriGroups(null);
              c.setUsers(null);
              c.setChannelTxtSet(null);
              c.setChannelCountSet(null);
              c.setChannelModels(null);
              c.setAttr(new HashMap<String,String>(getAttr()));
          }catch(CloneNotSupportedException e) {
              e.printStackTrace();
          }
          return c;
    }

	/**
	 * 审核后内容修改方式
	 */
	public static enum AfterCheckEnum {
		/**
		 * 不能修改，不能删除。
		 */
		CANNOT_UPDATE,
		/**
		 * 可以修改，可以删除。 修改后文章的审核级别将退回到修改人级别的状态。如果修改人的级别高于当前文章的审核级别，那么文章审核级别将保持不变。
		 */
		BACK_UPDATE,
		/**
		 * 可以修改，可以删除。 修改后文章保持原状态。
		 */
		KEEP_UPDATE
	}

	/**
	 * 获得URL地址
	 * 
	 * @return
	 */
	public String getUrl() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticChannel()) {
			return getUrlStatic(null, 1);
		} else if (!StringUtils.isBlank(getSite().getDomainAlias())) {
			return getUrlDynamic(null);
		} else {
			return getUrlDynamic(true);
		}
	}

	public String getHttpsUrl() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticChannel()) {
			return getHttpsUrlStatic(false, 1);
		} else if (!StringUtils.isBlank(getSite().getDomainAlias())) {
			return getHttpsUrlDynamic(null);
		} else {
			return getHttpsUrlDynamic(true);
		}
	}

	public String getMobileUrl() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticChannel()) {
			return getMobileUrlStatic(false, 1);
		} else {
			// return getUrlDynamic(null);
			// 此处共享了别站信息需要绝句路径，做了更改 于2012-7-26修改
			return getUrlDynamic(true);
		}
	}

	public String getUrlWhole() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticChannel()) {
			return getUrlStatic(true, 1);
		} else {
			return getUrlDynamic(true);
		}
	}

	/**
	 * 获得静态URL地址
	 * 
	 * @return
	 */
	public String getUrlStatic() {
		return getUrlStatic(null, 1);
	}

	public String getUrlStatic(int pageNo) {
		return getUrlStatic(null, pageNo);
	}

	public String getUrlStatic(Boolean whole, int pageNo) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getUrlBuffer(false, whole, false);
		String filename = getStaticFilenameByRule();
		if (!StringUtils.isBlank(filename)) {
			if (pageNo > 1) {
				int index = filename.indexOf(".", filename.lastIndexOf("/"));
				if (index != -1) {
					url.append(filename.substring(0, index));
					url.append("_").append(pageNo);
					url.append(filename.substring(index));
				} else {
					url.append("_").append(pageNo);
				}
			} else {
				if (getAccessByDir()) {
					url.append(filename.substring(0, filename.lastIndexOf("/") + 1));
				} else {
					url.append(filename);
				}
			}
		} else {
			// 默认静态页面访问路径
			url.append(SPT).append(getPath());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
				url.append(site.getStaticSuffix());
			} else {
				if (getHasContent()) {
					url.append(SPT);
				} else {
					url.append(site.getStaticSuffix());
				}
			}
		}
		return url.toString();
	}

	public String getHttpsUrlStatic(Boolean whole, int pageNo) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getHttpsUrlBuffer(false, whole, false);
		String filename = getStaticFilenameByRule();
		if (!StringUtils.isBlank(filename)) {
			if (pageNo > 1) {
				int index = filename.indexOf(".", filename.lastIndexOf("/"));
				if (index != -1) {
					url.append(filename.substring(0, index));
					url.append("_").append(pageNo);
					url.append(filename.substring(index));
				} else {
					url.append("_").append(pageNo);
				}
			} else {
				if (getAccessByDir()) {
					url.append(filename.substring(0, filename.lastIndexOf("/") + 1));
				} else {
					url.append(filename);
				}
			}
		} else {
			// 默认静态页面访问路径
			url.append(SPT).append(getPath());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
				url.append(site.getStaticSuffix());
			} else {
				if (getHasContent()) {
					url.append(SPT);
				} else {
					url.append(site.getStaticSuffix());
				}
			}
		}
		return url.toString();
	}

	public String getMobileUrlStatic(Boolean whole, int pageNo) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getMobileUrlBuffer(false, whole, false);
		String filename = getStaticFilenameByRule();
		if (!StringUtils.isBlank(filename)) {
			if (pageNo > 1) {
				int index = filename.indexOf(".", filename.lastIndexOf("/"));
				if (index != -1) {
					url.append(filename.substring(0, index));
					url.append("_").append(pageNo);
					url.append(filename.substring(index));
				} else {
					url.append("_").append(pageNo);
				}
			} else {
				if (getAccessByDir()) {
					url.append(filename.substring(0, filename.lastIndexOf("/") + 1));
				} else {
					url.append(filename);
				}
			}
		} else {
			// 默认静态页面访问路径
			url.append(SPT).append(getPath());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
				url.append(site.getStaticSuffix());
			} else {
				if (getHasContent()) {
					url.append(SPT);
				} else {
					url.append(site.getStaticSuffix());
				}
			}
		}
		return url.toString();
	}

	public String getStaticFilename(int pageNo) {
		CmsSite site = getSite();
		StringBuilder url = new StringBuilder();
		String staticDir = site.getStaticDir();
		if (!StringUtils.isBlank(staticDir)) {
			url.append(staticDir);
		}
		String filename = getStaticFilenameByRule();
		if (!StringUtils.isBlank(filename)) {
			int index = filename.indexOf(".", filename.lastIndexOf("/"));
			if (pageNo > 1) {
				if (index != -1) {
					url.append(filename.substring(0, index)).append("_").append(pageNo)
							.append(filename.substring(index));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态页面访问路径
			url.append(SPT).append(getPath());
			String suffix = site.getStaticSuffix();
			if (getHasContent()) {
				url.append(SPT).append(INDEX);
				if (pageNo > 1) {
					url.append("_").append(pageNo);
				}
				url.append(suffix);
			} else {
				if (pageNo > 1) {
					url.append("_").append(pageNo);
				}
				url.append(suffix);
			}
		}
		return url.toString();
	}

	public String getMobileStaticFilename(int pageNo) {
		CmsSite site = getSite();
		StringBuilder url = new StringBuilder();
		String staticDir = site.getStaticMobileDir();
		if (!StringUtils.isBlank(staticDir)) {
			url.append(staticDir);
		}
		String filename = getStaticFilenameByRule();
		if (!StringUtils.isBlank(filename)) {
			int index = filename.indexOf(".", filename.lastIndexOf("/"));
			if (pageNo > 1) {
				if (index != -1) {
					url.append(filename.substring(0, index)).append("_").append(pageNo)
							.append(filename.substring(index));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态页面访问路径
			url.append(SPT).append(getPath());
			String suffix = site.getStaticSuffix();
			if (getHasContent()) {
				url.append(SPT).append(INDEX);
				if (pageNo > 1) {
					url.append("_").append(pageNo);
				}
				url.append(suffix);
			} else {
				if (pageNo > 1) {
					url.append("_").append(pageNo);
				}
				url.append(suffix);
			}
		}
		return url.toString();
	}

	public String getStaticFilenameByRule() {
		String rule = getChannelRule();
		if (StringUtils.isBlank(rule)) {
			return null;
		}
		CmsModel model = getModel();
		String url = StaticPageUtils.staticUrlRule(rule, model.getId(), model.getPath(), getId(), getPath(), null,
				null);
		return url;
	}

	/**
	 * 获得动态URL地址
	 * 
	 * @return
	 */
	public String getUrlDynamic() {
		return getUrlDynamic(null);
	}

	public String getUrlDynamic(Boolean whole) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getUrlBuffer(true, whole, false);
		url.append(SPT).append(getPath());
		if (getHasContent()) {
			url.append(SPT).append(INDEX);
		}
		url.append(site.getDynamicSuffix());
		return url.toString();
	}

	public String getHttpsUrlDynamic(Boolean whole) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getHttpsUrlBuffer(true, whole, false);
		url.append(SPT).append(getPath());
		if (getHasContent()) {
			url.append(SPT).append(INDEX);
		}
		url.append(site.getDynamicSuffix());
		return url.toString();
	}

	/**
	 * 获得节点列表。从父节点到自身。
	 * 
	 * @return
	 */
	public List<Channel> getNodeList() {
		LinkedList<Channel> list = new LinkedList<Channel>();
		Channel node = this;
		while (node != null) {
			list.addFirst(node);
			node = node.getParent();
		}
		return list;
	}

	/**
	 * 获得节点列表ID。从父节点到自身。
	 * 
	 * @return
	 */
	public Integer[] getNodeIds() {
		List<Channel> channels = getNodeList();
		Integer[] ids = new Integer[channels.size()];
		int i = 0;
		for (Channel c : channels) {
			ids[i++] = c.getId();
		}
		return ids;
	}

	/**
	 * 获得深度
	 * 
	 * @return 第一层为0，第二层为1，以此类推。
	 */
	public int getDeep() {
		int deep = 0;
		Channel parent = getParent();
		while (parent != null) {
			deep++;
			parent = parent.getParent();
		}
		return deep;
	}

	public Channel getTopChannel() {
		Channel parent = getParent();
		while (parent != null) {
			if (parent.getParent() != null) {
				parent = parent.getParent();
			} else {
				break;
			}
		}
		return parent;
	}

	/**
	 * 获取栏目下总浏览量
	 * 
	 * @return
	 */
	public int getViewTotal() {
		Integer totalView = 0;
		List<Channel> list = new ArrayList<Channel>();
		addChildToList(list, this, true);
		for (Channel c : list) {
			totalView += c.getChannelCount().getViews();
		}
		return totalView;
	}

	/**
	 * 
	 * @return
	 */
	public int getViewsDayTotal() {
		Integer totalView = 0;
		List<Channel> list = new ArrayList<Channel>();
		addChildToList(list, this, true);
		for (Channel c : list) {
			totalView += c.getChannelCount().getViewsDay();
		}
		return totalView;
	}

	public int getViewsMonthTotal() {
		Integer totalView = 0;
		List<Channel> list = new ArrayList<Channel>();
		addChildToList(list, this, true);
		for (Channel c : list) {
			totalView += c.getChannelCount().getViewsMonth();
		}
		return totalView;
	}

	public int getViewsWeekTotal() {
		Integer totalView = 0;
		List<Channel> list = new ArrayList<Channel>();
		addChildToList(list, this, true);
		for (Channel c : list) {
			totalView += c.getChannelCount().getViewsWeek();
		}
		return totalView;
	}

	public int getContentTotal() {
		ChannelCount c = getChannelCount();
		return c.getContentTotal();
	}

	public int getContentDay() {
		ChannelCount c = getChannelCount();
		return c.getContentDay();
	}

	public int getContentMonth() {
		ChannelCount c = getChannelCount();
		return c.getContentMonth();
	}

	public int getContentWeek() {
		ChannelCount c = getChannelCount();
		return c.getContentWeek();
	}

	public int getContentYear() {
		ChannelCount c = getChannelCount();
		return c.getContentYear();
	}

	private static void addChildToList(List<Channel> list, Channel channel, boolean hasContentOnly) {
		list.add(channel);
		Set<Channel> child = channel.getChild();
		for (Channel c : child) {
			if (hasContentOnly) {
				if (c.getHasContent()) {
					addChildToList(list, c, hasContentOnly);
				}
			} else {
				addChildToList(list, c, hasContentOnly);
			}
		}
	}

	/**
	 * 获得栏目终审级别
	 * 
	 * @return
	 */
	public Byte getFinalStepExtends() {
		Byte step = getFinalStep();
		if (step == null) {
			Channel parent = getParent();
			if (parent == null) {
				return getSite().getFinalStep();
			} else {
				return parent.getFinalStepExtends();
			}
		} else {
			return step;
		}
	}

	public Byte getAfterCheck() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getAfterCheck();
		} else {
			return null;
		}
	}

	/**
	 * 获得审核后修改方式的枚举值。如果该值为null则取父级栏目，父栏目为null则取站点相关设置。
	 * 
	 * @return
	 */
	public AfterCheckEnum getAfterCheckEnum() {
		Byte after = getChannelExt().getAfterCheck();
		Channel channel = getParent();
		// 如果为null，则查找父栏目。
		while (after == null && channel != null) {
			after = channel.getAfterCheck();
			channel = channel.getParent();
		}
		// 如果依然为null，则查找站点设置
		if (after == null) {
			after = getSite().getAfterCheck();
		}
		if (after == 1) {
			return AfterCheckEnum.CANNOT_UPDATE;
		} else if (after == 2) {
			return AfterCheckEnum.BACK_UPDATE;
		} else if (after == 3) {
			return AfterCheckEnum.KEEP_UPDATE;
		} else {
			// 默认为不可改、不可删
			return AfterCheckEnum.CANNOT_UPDATE;
		}
	}

	/**
	 * 获得列表用于下拉选择。条件：有内容的栏目。
	 * 
	 * @return
	 */
	public List<Channel> getListForSelect(Set<Channel> rights, boolean hasContentOnly) {
		return getListForSelect(rights, null, hasContentOnly);
	}

	public List<Channel> getListForSelect(Set<Channel> rights, Channel exclude, boolean hasContentOnly) {
		List<Channel> list = new ArrayList<Channel>((getRgt() - getLft()) / 2);
		addChildToList(list, this, rights, exclude, hasContentOnly);
		return list;
	}

	/**
	 * 获得列表用于下拉选择。条件：有内容的栏目。
	 * 
	 * @param topList
	 *            顶级栏目
	 * @return
	 */
	public static List<Channel> getListForSelect(List<Channel> topList, Set<Channel> rights, boolean hasContentOnly) {
		return getListForSelect(topList, rights, null, hasContentOnly);
	}

	public static List<Channel> getListForSelect(List<Channel> topList, Set<Channel> rights, Channel exclude,
			boolean hasContentOnly) {
		List<Channel> list = new ArrayList<Channel>();
		for (Channel c : topList) {
			addChildToList(list, c, rights, exclude, hasContentOnly);
		}
		return list;
	}

	/**
	 * 递归将子栏目加入列表。条件：有内容的栏目。
	 * 
	 * @param list
	 *            栏目容器
	 * @param channel
	 *            待添加的栏目，且递归添加子栏目
	 * @param rights
	 *            有权限的栏目，为null不控制权限。
	 */
	private static void addChildToList(List<Channel> list, Channel channel, Set<Channel> rights, Channel exclude,
			boolean hasContentOnly) {
		if ((rights != null && !rights.contains(channel)) || (exclude != null && exclude.equals(channel))) {
			return;
		}
		list.add(channel);
		Set<Channel> child = channel.getChild();
		for (Channel c : child) {
			if (hasContentOnly) {
				if (c.getHasContent()) {
					addChildToList(list, c, rights, exclude, hasContentOnly);
				}
			} else {
				addChildToList(list, c, rights, exclude, hasContentOnly);
			}
		}
	}

	public String getTplChannelOrDef() {
		String tpl = getTplChannel();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			String sol = getSite().getSolutionPath();
			return getModel().getTplChannel(sol, true);
		}
	}

	public String getMobileTplChannelOrDef() {
		String tpl = getMobileTplChannel();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			String sol = getSite().getMobileSolutionPath();
			return getModel().getTplChannel(sol, true);
		}
	}

	public String getTplContentOrDef(CmsModel contentModel) {
		String tpl = getModelTpl(contentModel);
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			String sol = getSite().getSolutionPath();
			return contentModel.getTplContent(sol, true);
		}
	}

	public String getMobileTplContentOrDef(CmsModel contentModel) {
		String tpl = getModelMobileTpl(contentModel);
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			String sol = getSite().getMobileSolutionPath();
			return contentModel.getTplContent(sol, true);
		}
	}

	public Integer[] getUserIds() {
		Set<CmsUser> users = getUsers();
		return CmsUser.fetchIds(users);
	}

	public void addToViewGroups(CmsGroup group) {
		Set<CmsGroup> groups = getViewGroups();
		if (groups == null) {
			groups = new TreeSet<CmsGroup>(new PriorityComparator());
			setViewGroups(groups);
		}
		groups.add(group);
		group.getViewChannels().add(this);
	}

	public void addToContriGroups(CmsGroup group) {
		Set<CmsGroup> groups = getContriGroups();
		if (groups == null) {
			groups = new TreeSet<CmsGroup>(new PriorityComparator());
			setContriGroups(groups);
		}
		groups.add(group);
		group.getContriChannels().add(this);
	}

	public void addToUsers(CmsUser user) {
		Set<CmsUser> set = getUsers();
		if (set == null) {
			set = new TreeSet<CmsUser>(new PriorityComparator());
			setUsers(set);
		}
		set.add(user);
		user.addToChannels(this);
	}

	public void addToChannelModels(CmsModel model, String tpl, String mtpl) {
		List<ChannelModel> list = getChannelModels();
		if (list == null) {
			list = new ArrayList<ChannelModel>();
			setChannelModels(list);
		}
		ChannelModel cm = new ChannelModel();
		cm.setTplContent(tpl);
		cm.setTplMoibleContent(mtpl);
		cm.setModel(model);
		list.add(cm);
	}

	public List<ChannelModel> getChannelModelsExtend() {
		List<ChannelModel> list = getChannelModels();
		// 没有配置栏目模型默认父栏目配置
		if (list == null || list.size() <= 0) {
			Channel parent = getParent();
			if (parent == null) {
				return null;
			} else {
				return parent.getChannelModelsExtend();
			}
		} else {
			return list;
		}
	}

	public List<CmsModel> getModels() {
		List<ChannelModel> list = getChannelModelsExtend();
		if (list == null) {
			return null;
		}
		List<CmsModel> models = new ArrayList<CmsModel>();
		for (ChannelModel cm : list) {
			models.add(cm.getModel());
		}
		return models;
	}

	public List<CmsModel> getModels(List<CmsModel> allModels) {
		List<ChannelModel> list = getChannelModelsExtend();
		// 顶层栏目没有配置默认所有可用模型
		if (list == null) {
			return allModels;
		}
		List<CmsModel> models = new ArrayList<CmsModel>();
		for (ChannelModel cm : list) {
			models.add(cm.getModel());
		}
		return models;
	}

	public List<String> getModelIds() {
		List<String> ids = new ArrayList<String>();
		List<CmsModel> models = getModels();
		if (models != null) {
			for (CmsModel model : models) {
				ids.add(model.getId().toString());
			}
		}
		return ids;
	}
	
	public String getModelIdStr() {
		List<String> ids=getModelIds();
		StringBuffer buff=new StringBuffer();
		for(String id:ids){
			buff.append(id+",");
		}
		return buff.toString();
	}

	public List<String> getModelTpls() {
		List<ChannelModel> list = getChannelModelsExtend();
		List<String> tpls = new ArrayList<String>();
		// 当前模板，去除基本路径
		int tplPathLength = getSite().getTplPath().length();
		if (list != null) {
			for (ChannelModel cm : list) {
				String tpl = cm.getTplContent();
				if (StringUtils.isNotBlank(tpl)) {
					tpls.add(tpl.substring(tplPathLength));
				} else {
					tpls.add("");
				}
				/*
				 * if(StringUtils.isNotBlank(tpl)){
				 * tpls.add(tpl.substring(tplPathLength)); }
				 */
			}
		}
		return tpls;
	}

	public String[] getModelTplStrs() {
		List<String> list = getModelTpls();
		String[] tpls = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			tpls[i] = list.get(i);
		}
		return tpls;
	}
	
	public String getModelTplStr(){
		List<String> list = getModelTpls();
		StringBuffer buff=new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			buff.append(list.get(i)+",");
		}
		return buff.toString();
	}

	public List<String> getMobileModelTpls() {
		List<ChannelModel> list = getChannelModelsExtend();
		List<String> tpls = new ArrayList<String>();
		// 当前模板，去除基本路径
		int tplPathLength = getSite().getTplPath().length();
		if (list != null) {
			for (ChannelModel cm : list) {
				String tpl = cm.getTplMoibleContent();
				if (StringUtils.isNotBlank(tpl)) {
					tpls.add(tpl.substring(tplPathLength));
				} else {
					tpls.add("");
				}
				/*
				 * if(StringUtils.isNotBlank(tpl)){
				 * tpls.add(tpl.substring(tplPathLength)); }
				 */
			}
		}
		return tpls;
	}

	public String[] getMobileModelTplStrs() {
		List<String> list = getMobileModelTpls();
		String[] tpls = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			tpls[i] = list.get(i);
		}
		return tpls;
	}
	
	public String getMobileModelTplStr(){
		List<String> list = getMobileModelTpls();
		StringBuffer buff=new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			buff.append(list.get(i)+",");
		}
		return buff.toString();
	}

	public String getModelTpl(CmsModel model) {
		List<ChannelModel> list = getChannelModelsExtend();
		if (list != null) {
			for (ChannelModel cm : list) {
				if (cm.getModel().equals(model)) {
					return cm.getTplContent();
				}
			}
		}
		return null;
	}

	public String getModelMobileTpl(CmsModel model) {
		List<ChannelModel> list = getChannelModelsExtend();
		if (list != null) {
			for (ChannelModel cm : list) {
				if (cm.getModel().equals(model)) {
					return cm.getTplMoibleContent();
				}
			}
		}
		return null;
	}


	public void init() {
		if (getPriority() == null) {
			setPriority(10);
		}
		if (getDisplay() == null) {
			setDisplay(true);
		}
	}

	public String getName() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getName();
		} else {
			return null;
		}
	}

	public Boolean getStaticChannel() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getStaticChannel();
		} else {
			return null;
		}
	}

	public Boolean getStaticContent() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getStaticContent();
		} else {
			return null;
		}
	}

	public Boolean getAccessByDir() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getAccessByDir();
		} else {
			return null;
		}
	}

	public Boolean getListChild() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getListChild();
		} else {
			return null;
		}
	}

	public Integer getPageSize() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getPageSize();
		} else {
			return null;
		}
	}

	public String getChannelRule() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getChannelRule();
		} else {
			return null;
		}
	}

	public String getContentRule() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getContentRule();
		} else {
			return null;
		}
	}

	public Byte getFinalStep() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getFinalStep();
		} else {
			return null;
		}
	}

	public String getLink() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getLink();
		} else {
			return null;
		}
	}

	public String getTplChannel() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTplChannel();
		} else {
			return null;
		}
	}

	public String getMobileTplChannel() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTplMobileChannel();
		} else {
			return null;
		}
	}

	public String getTplContent() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTplContent();
		} else {
			return null;
		}
	}

	public Boolean getHasTitleImg() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getHasTitleImg();
		} else {
			return null;
		}
	}

	public Boolean getHasContentImg() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getHasContentImg();
		} else {
			return null;
		}
	}

	public Integer getTitleImgWidth() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTitleImgWidth();
		} else {
			return null;
		}
	}

	public Integer getTitleImgHeight() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTitleImgHeight();
		} else {
			return null;
		}
	}

	public Integer getContentImgWidth() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getContentImgWidth();
		} else {
			return null;
		}
	}

	public Integer getContentImgHeight() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getContentImgHeight();
		} else {
			return null;
		}
	}

	public String getTitleImg() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTitleImg();
		} else {
			return null;
		}
	}

	public String getContentImg() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getContentImg();
		} else {
			return null;
		}
	}

	public String getTitle() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getTitle();
		} else {
			return null;
		}
	}

	public String getKeywords() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getKeywords();
		} else {
			return null;
		}
	}

	public String getDescription() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getDescription();
		} else {
			return null;
		}
	}

	public Integer getCommentControl() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getCommentControl();
		} else {
			return null;
		}
	}

	public Boolean getAllowUpdown() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getAllowUpdown();
		} else {
			return null;
		}
	}

	public Boolean getAllowShare() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getAllowShare();
		} else {
			return null;
		}
	}

	public Boolean getAllowScore() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getAllowScore();
		} else {
			return null;
		}
	}

	public Boolean getBlank() {
		ChannelExt ext = getChannelExt();
		if (ext != null) {
			return ext.getBlank();
		} else {
			return null;
		}
	}

	/**
	 * 获得栏目内容
	 * 
	 * @return
	 */
	public String getTxt() {
		ChannelTxt txt = getChannelTxt();
		if (txt != null) {
			return txt.getTxt();
		} else {
			return null;
		}
	}

	/**
	 * 获得栏目内容1
	 * 
	 * @return
	 */
	public String getTxt1() {
		ChannelTxt txt = getChannelTxt();
		if (txt != null) {
			return txt.getTxt1();
		} else {
			return null;
		}
	}

	/**
	 * 获得栏目内容2
	 * 
	 * @return
	 */
	public String getTxt2() {
		ChannelTxt txt = getChannelTxt();
		if (txt != null) {
			return txt.getTxt2();
		} else {
			return null;
		}
	}

	/**
	 * 获得栏目内容3
	 * 
	 * @return
	 */
	public String getTxt3() {
		ChannelTxt txt = getChannelTxt();
		if (txt != null) {
			return txt.getTxt3();
		} else {
			return null;
		}
	}

	public ChannelTxt getChannelTxt() {
		Set<ChannelTxt> set = getChannelTxtSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}

	public ChannelCount getChannelCount() {
		Set<ChannelCount> set = getChannelCountSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}

	/**
	 * 每个站点各自维护独立的树结构
	 * 
	 * @see HibernateTree#getTreeCondition()
	 */
	public String getTreeCondition() {
		return "bean.site.id=" + getSite().getId();
	}

	/**
	 * @see HibernateTree#getParentId()
	 */
	public Integer getParentId() {
		Channel parent = getParent();
		if (parent != null) {
			return parent.getId();
		} else {
			return null;
		}
	}

	/**
	 * @see HibernateTree#getLftName()
	 */
	public String getLftName() {
		return DEF_LEFT_NAME;
	}

	/**
	 * @see HibernateTree#getParentName()
	 */
	public String getParentName() {
		return DEF_PARENT_NAME;
	}

	/**
	 * @see HibernateTree#getRgtName()
	 */
	public String getRgtName() {
		return DEF_RIGHT_NAME;
	}

	public static Integer[] fetchIds(Collection<Channel> channels) {
		if (channels == null) {
			return null;
		}
		Integer[] ids = new Integer[channels.size()];
		int i = 0;
		for (Channel c : channels) {
			ids[i++] = c.getId();
		}
		return ids;
	}

	public Integer[] getViewGroupIds() {
		Set<CmsGroup> groups = getViewGroups();
		if (groups == null) {
			return null;
		}
		Integer[] ids = new Integer[groups.size()];
		int i = 0;
		for (CmsGroup c : groups) {
			ids[i++] = c.getId();
		}
		return ids;
	}
	
	public String getViewGroupIdsStr() {
		StringBuffer buff=new StringBuffer();
		Set<CmsGroup> groups = getViewGroups();
		if (groups != null) {
			for (CmsGroup c : groups) {
				buff.append(c.getId()+",");
			}
		}
		return buff.toString();
	}

	public Integer[] getContriGroupIds() {
		Set<CmsGroup> groups = getContriGroups();
		if (groups == null) {
			return null;
		}
		Integer[] ids = new Integer[groups.size()];
		int i = 0;
		for (CmsGroup c : groups) {
			ids[i++] = c.getId();
		}
		return ids;
	}
	
	public String getContriGroupIdsStr() {
		StringBuffer buff=new StringBuffer();
		Set<CmsGroup> groups = getContriGroups();
		if (groups != null) {
			for (CmsGroup c : groups) {
				buff.append(c.getId()+",");
			}
		}
		return buff.toString();
	}

	public Integer[] getModelIntIds() {
		List<String> modelIds = getModelIds();
		if (modelIds == null) {
			return null;
		}
		Integer[] ids = new Integer[modelIds.size()];
		int i = 0;
		for (String c : modelIds) {
			ids[i++] = Integer.parseInt(c);
		}
		return ids;
	}

	public void removeViewGroup(CmsGroup group) {
		Set<CmsGroup> viewGroups = getViewGroups();
		viewGroups.remove(group);
	}

	public void removeContriGroup(CmsGroup group) {
		Set<CmsGroup> contriGroups = getContriGroups();
		contriGroups.remove(group);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Channel() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Channel(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Channel(java.lang.Integer id, com.jeecms.core.entity.CmsSite site, com.jeecms.cms.entity.main.CmsModel model,
			java.lang.Integer lft, java.lang.Integer rgt, java.lang.Integer priority, java.lang.Boolean hasContent,
			java.lang.Boolean display) {

		super(id, site, model, lft, rgt, priority, hasContent, display);
	}

	/* [CONSTRUCTOR MARKER END] */

}