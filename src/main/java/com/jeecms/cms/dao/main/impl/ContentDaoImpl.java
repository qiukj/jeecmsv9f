package com.jeecms.cms.dao.main.impl;

import static com.jeecms.cms.entity.main.Content.ContentStatus.all;
import static com.jeecms.cms.entity.main.Content.ContentStatus.checked;
import static com.jeecms.cms.entity.main.Content.ContentStatus.draft;
import static com.jeecms.cms.entity.main.Content.ContentStatus.passed;
import static com.jeecms.cms.entity.main.Content.ContentStatus.prepared;
import static com.jeecms.cms.entity.main.Content.ContentStatus.recycle;
import static com.jeecms.cms.entity.main.Content.ContentStatus.rejected;
import static com.jeecms.cms.entity.main.Content.ContentStatus.contribute;
import static com.jeecms.cms.entity.main.Content.ContentStatus.pigeonhole;

import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_START;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_END;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_LIKE;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_IN;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_EQ;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_GT;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_GTE;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_LT;
import static com.jeecms.cms.action.directive.abs.AbstractContentDirective.PARAM_ATTR_LTE;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeecms.cms.dao.main.ContentDao;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.entity.main.Content.ContentStatus;
import com.jeecms.cms.service.ContentQueryFreshTimeCache;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class ContentDaoImpl extends HibernateBaseDao<Content, Integer>
		implements ContentDao {
	
	public Pagination getPage(Integer share,String title, Integer typeId,Integer currUserId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,Integer modelId,
			Integer channelId,int orderBy, int pageNo, int pageSize) {
		return getPageData(Content.QUERY_DATA, share, title, typeId, 
				currUserId, inputUserId, topLevel, recommend, status,
				checkStep, siteId, modelId, channelId, orderBy, pageNo, pageSize);
	}
	
	public Pagination getPageCount(Integer share,String title, Integer typeId,Integer currUserId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,Integer modelId,
			Integer channelId,int orderBy, int pageNo, int pageSize) {
		return getPageData(Content.QUERY_PAGE, share, title, typeId, 
				currUserId, inputUserId, topLevel, recommend, status,
				checkStep, siteId, modelId, channelId, orderBy, pageNo, pageSize);
	}
	
	public Pagination getPageForMember(Integer share,String title,
			Integer typeId,Integer currUserId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,Integer modelId,
			Integer channelId,int orderBy, int pageNo, int pageSize){
		return getPageData(Content.QUERY_TOTAL, share, title, typeId, 
				currUserId, inputUserId, topLevel, recommend, status,
				checkStep, siteId, modelId, channelId, orderBy, pageNo, pageSize);
	}

	//只能管理自己的数据不能审核他人信息，工作流相关表无需查询
	public Pagination getPageBySelf(Integer share,String title, Integer typeId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId, Integer userId, int orderBy, int pageNo,
			int pageSize) {
		return getPageDataBySelf(Content.QUERY_DATA, share, title, typeId, inputUserId,
				topLevel, recommend, status, checkStep, siteId, channelId,
				userId, orderBy, pageNo, pageSize);
	}
	
	public Pagination getPageCountBySelf(Integer share,String title, Integer typeId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId, Integer userId, int orderBy, int pageNo,
			int pageSize) {
		return getPageDataBySelf(Content.QUERY_PAGE, share, title, typeId, inputUserId,
				topLevel, recommend, status, checkStep, siteId, channelId,
				userId, orderBy, pageNo, pageSize);
	}

	public Pagination getPageByRight(Integer share,String title, Integer typeId,
			Integer currUserId,Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId, Integer userId, boolean selfData, int orderBy,
			int pageNo,int pageSize) {
		return getPageDataByRight(Content.QUERY_DATA,share, title, typeId, currUserId,
				inputUserId, topLevel, recommend, status, checkStep,
				siteId, channelId, userId, selfData, orderBy,
				pageNo, pageSize);
	}
	
	public Pagination getPageCountByRight(Integer share,String title, Integer typeId,
			Integer currUserId,Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId, Integer userId, boolean selfData, int orderBy,
			int pageNo,int pageSize) {
		return getPageDataByRight(Content.QUERY_PAGE,share, title, typeId, currUserId,
				inputUserId, topLevel, recommend, status, checkStep,
				siteId, channelId, userId, selfData, orderBy,
				pageNo, pageSize);
	}
	
	public List<Content> getList(String title, Integer typeId,Integer currUserId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,Integer modelId,
			Integer channelId,int orderBy, int first, int count) {
		Finder f = Finder.create("select  bean from Content bean ");
		if (rejected == status||prepared == status|| passed == status) {
			f.append("  join bean.contentCheckSet check ");
		}
		if (channelId != null) {
			f.append(" join bean.channel channel,Channel parent");
			f.append(" where (channel.lft between parent.lft and parent.rgt");
			f.append(" and channel.site.id=parent.site.id");
			f.append(" and parent.id=:parentId)");
			f.setParam("parentId", channelId);
		} else if (siteId != null) {
			f.append(" where (bean.site.id=:siteId)");
			f.setParam("siteId", siteId);
		} else {
			f.append(" where 1=1");
		}
		if (prepared == status) {
			f.append(" and check.checkStep<:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (passed == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (rejected == status) {
			//退回只有本级可以查看
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=true");
			f.setParam("checkStep", checkStep);
		}
		if(modelId!=null){
			f.append(" and bean.model.id=:modelId").setParam("modelId", modelId);
		}
		appendQuery(f, title, typeId, inputUserId, status, topLevel, recommend);
		appendOrder(f, orderBy);
		f.setFirstResult(first);
		f.setMaxResults(count);
		return find(f);
	}
	
	private Pagination getPageData(Integer queryMode,Integer share,String title,
			Integer typeId,Integer currUserId,Integer inputUserId, 
			boolean topLevel, boolean recommend,ContentStatus status, 
			Byte checkStep, Integer siteId,Integer modelId,
			Integer channelId,int orderBy, int pageNo, int pageSize){
		Finder f = Finder.create("select bean from Content bean ");
		if (rejected == status||prepared == status|| passed == status) {
			f.append("  join bean.contentCheckSet check ");
		}
		if (channelId != null) {
			//共享内容
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel,Channel parent");
				f.append(" where (channel.lft between parent.lft and parent.rgt");
				f.append(" and channel.site.id=parent.site.id");
				f.append(" and parent.id=:parentId and bean.site.id!=:siteId)");
				f.setParam("parentId", channelId).setParam("siteId", siteId);
			}else{
				f.append(" join bean.channel channel,Channel parent");
				f.append(" where (channel.lft between parent.lft and parent.rgt");
				f.append(" and channel.site.id=parent.site.id");
				f.append(" and parent.id=:parentId)");
				f.setParam("parentId", channelId);
			}
		} else if (siteId != null) {
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel ");
				f.append(" where (bean.site.id!=:siteId and channel.site.id=:siteId)");
				f.setParam("siteId", siteId);
			}else{
				f.append(" where bean.site.id=:siteId");
				f.setParam("siteId", siteId);
			}
		} else {
			f.append(" where 1=1");
		}
		if (prepared == status) {
			f.append(" and check.checkStep<:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (passed == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (rejected == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=true");
			f.setParam("checkStep", checkStep);
		}
		if(modelId!=null){
			f.append(" and bean.model.id=:modelId").setParam("modelId", modelId);
		}
		f.setCacheable(true);
		appendQuery(f, title, typeId, inputUserId, status, topLevel, recommend);
		appendOrder(f, orderBy);
		if(queryMode!=null&&queryMode.equals(Content.QUERY_PAGE)){
			return findBigDataPage(f, pageNo, pageSize);
		}else if(queryMode!=null&&queryMode.equals(Content.QUERY_TOTAL)){
			return find(f, pageNo, pageSize);
		}else{
			return findBigData(f, pageNo, pageSize);
		}
	}
	
	private Pagination getPageDataBySelf(Integer queryMode,Integer share,String title, Integer typeId,
			Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId, Integer userId, int orderBy, int pageNo,
			int pageSize){
		Finder f = Finder.create("select  bean from Content bean");
		if (prepared == status || passed == status || rejected == status) {
			f.append(" join bean.contentCheckSet check");
		}
		if (channelId != null) {
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel,Channel parent");
				f.append(" where channel.lft between parent.lft and parent.rgt");
				f.append(" and channel.site.id=parent.site.id");
				f.append(" and parent.id=:parentId and bean.site.id!=:siteId");
				f.setParam("parentId", channelId);
			}else{
				f.append(" join bean.channel channel,Channel parent");
				f.append(" where channel.lft between parent.lft and parent.rgt");
				f.append(" and channel.site.id=parent.site.id");
				f.append(" and parent.id=:parentId");
				f.setParam("parentId", channelId);
			}
		}else if (siteId != null) {
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel ");
				f.append(" where bean.site.id!=:siteId and channel.site.id=:siteId");
				f.setParam("siteId", siteId);
			}else{
				f.append(" where bean.site.id=:siteId");
				f.setParam("siteId", siteId);
			}
		} else {
			f.append(" where 1=1");
		}
		f.append(" and bean.user.id=:userId");
		f.setParam("userId", userId);
		if (prepared == status) {
			f.append(" and check.checkStep<:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (passed == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (rejected == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=true");
			f.setParam("checkStep", checkStep);
		}
		appendQuery(f, title, typeId, inputUserId, status, topLevel, recommend);
		if (prepared == status) {
			f.append(" order by check.checkStep desc,bean.id desc");
		} else {
			appendOrder(f, orderBy);
		}
		f.setCacheable(true);
		if(queryMode!=null&&queryMode.equals(Content.QUERY_PAGE)){
			return findBigDataPage(f, pageNo, pageSize);
		}else if(queryMode!=null&&queryMode.equals(Content.QUERY_TOTAL)){
			return find(f, pageNo, pageSize);
		}else{
			return findBigData(f, pageNo, pageSize);
		}
	}
	
	private Pagination getPageDataByRight(Integer queryMode,
			Integer share,String title, Integer typeId,
			Integer currUserId,Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId, Integer userId, boolean selfData, int orderBy,
			int pageNo,int pageSize){
		Finder f = Finder.create("select  bean from Content bean ");
		if (rejected == status||prepared == status|| passed == status) {
			f.append("  join bean.contentCheckSet check ");
		}
		if (channelId != null) {
			//共享内容
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel ");
			}else{
				f.append(" join bean.channel channel ");
			}
			f.append(",Channel parent");
			f.append(" where (channel.lft between parent.lft and parent.rgt");
			f.append(" and channel.site.id=parent.site.id");
			f.append(" and parent.id=:parentId");
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" and bean.site.id!=:siteId and channel.site.id=:siteId").setParam("siteId", siteId);
			}
			f.append(")" );
			f.setParam("parentId", channelId);
		} else if (siteId != null) {
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel ");
			}else{
				f.append(" join bean.channel channel ");
			}
			f.append(" where 1=1 ");
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" and bean.site.id!=:siteId and channel.site.id=:siteId");
				f.setParam("siteId", siteId);
			}else{
				f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
			}
		} else {
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" join bean.channels channel ");
			}else{
				f.append(" join bean.channel channel ");
			}
			f.append(" where 1=1 ");
			if(share!=null&&share.equals(Content.CONTENT_QUERY_SHARE)){
				f.append(" and bean.site.id!=channel.site.id");
			}else{
				f.append(" and bean.site.id=channel.site.id");
			}
		}
		if (selfData) {
			// userId前面已赋值
			f.append(" and bean.user.id=:userId");
			f.setParam("userId", userId);
		}
		if (prepared == status) {
			f.append(" and check.checkStep<:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (passed == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=false");
			f.setParam("checkStep", checkStep);
		} else if (rejected == status) {
			f.append(" and check.checkStep=:checkStep");
			f.append(" and check.rejected=true");
			f.setParam("checkStep", checkStep);
		}
		appendQuery(f, title, typeId, inputUserId, status, topLevel, recommend);
		appendOrder(f, orderBy);
		f.setCacheable(true);
		if(queryMode!=null&&queryMode.equals(Content.QUERY_PAGE)){
			return findBigDataPage(f, pageNo, pageSize);
		}else if(queryMode!=null&&queryMode.equals(Content.QUERY_TOTAL)){
			return find(f, pageNo, pageSize);
		}else{
			return findBigData(f, pageNo, pageSize);
		}
	}
	

	public Pagination getPageForCollection(Integer siteId, Integer memberId, int pageNo, int pageSize){
		Finder f=createCollectFinder(siteId, memberId);
		return find(f, pageNo, pageSize);
	}
	
	public List<Content> getListForCollection(Integer siteId, Integer memberId, 
			Integer first, Integer count){
		Finder f=createCollectFinder(siteId, memberId);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	@SuppressWarnings("unchecked")
	public  List<Content> getExpiredTopLevelContents(byte topLevel,Date expiredDay){
		String hql = "from  Content bean where bean.status=:status and bean.topLevel>:topLevel and bean.contentExt.topLevelDate<:topLevelDate";
		Finder f=Finder.create(hql).setParam("status", ContentCheck.CHECKED).setParam("topLevel", topLevel).setParam("topLevelDate", expiredDay);
		return find(f);
	}
	
	@SuppressWarnings("unchecked")
	public  List<Content> getPigeonholeContents(Date pigeonholeDay){
		String hql = "from  Content bean where bean.status=:status and bean.contentExt.pigeonholeDate<:pigeonholeDate";
		Finder f=Finder.create(hql).setParam("status", ContentCheck.CHECKED).setParam("pigeonholeDate", pigeonholeDay);
		return find(f);
	}

	private void appendQuery(Finder f, String title, Integer typeId,
			Integer inputUserId, ContentStatus status, boolean topLevel,
			boolean recommend) {
		if (!StringUtils.isBlank(title)) {
			f.append(" and bean.contentExt.title like :title  escape '/'");
			title=title.replaceAll("%","/%");
			title=title.replaceAll("_","/_");
			f.setParam("title", "%" + title + "%");
		}
		if (typeId != null) {
			f.append(" and bean.type.id=:typeId");
			f.setParam("typeId", typeId);
		}
		if (inputUserId != null&&inputUserId!=0) {
			f.append(" and bean.user.id=:inputUserId");
			f.setParam("inputUserId", inputUserId);
		}else{
			//输入了没有的用户名的情况
			if(inputUserId==null){
				f.append(" and 1!=1");
			}
		}
		if (topLevel) {
			f.append(" and bean.topLevel>0");
		}
		if (recommend) {
			f.append(" and bean.recommend=true");
		}
		if (draft == status) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.DRAFT);
		}if (contribute == status) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.CONTRIBUTE);
		} else if (checked == status) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.CHECKED);
		} else if (prepared == status ) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.CHECKING);
		} else if (rejected == status ) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.REJECT);
		}else if (passed == status) {
			f.append(" and (bean.status=:checking or bean.status=:checked)");
			f.setParam("checking", ContentCheck.CHECKING);
			f.setParam("checked", ContentCheck.CHECKED);
		} else if (all == status) {
			f.append(" and bean.status<>:status");
			f.setParam("status", ContentCheck.RECYCLE);
		} else if (recycle == status) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.RECYCLE);
		} else if (pigeonhole == status) {
			f.append(" and bean.status=:status");
			f.setParam("status", ContentCheck.PIGEONHOLE);
		} else {
			// never
		}
	}
	

	public Content getSide(Integer id, Integer siteId, Integer channelId,
			boolean next, boolean cacheable) {
		Finder f = Finder.create("from Content bean where 1=1");
		if (channelId != null) {
			f.append(" and bean.channel.id=:channelId");
			f.setParam("channelId", channelId);
		} else if (siteId != null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		if (next) {
			f.append(" and bean.id>:id");
			f.setParam("id", id);
			f.append(" and bean.status=" + ContentCheck.CHECKED);
			f.append(" order by bean.id asc");
		} else {
			f.append(" and bean.id<:id");
			f.setParam("id", id);
			f.append(" and bean.status=" + ContentCheck.CHECKED);
			f.append(" order by bean.id desc");
		}
		Query query = f.createQuery(getSession());
		query.setCacheable(cacheable).setMaxResults(1);
		return (Content) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Content> getListByIdsForTag(Integer[] ids, int orderBy) {
		Finder f = Finder.create("from Content bean where bean.id in (:ids)");
		f.setParamList("ids", ids);
		appendOrder(f, orderBy);
		f.setCacheable(true);
		return find(f);
	}

	public Pagination getPageBySiteIdsForTag(Integer[] siteIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title, Map<String,String[]>attr,int orderBy,int pageNo, int pageSize) {
		Finder f = bySiteIds(siteIds, typeIds, titleImg, recommend, title,
				attr,orderBy);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<Content> getListBySiteIdsForTag(Integer[] siteIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr,int orderBy, Integer first, Integer count) {
		Finder f = bySiteIds(siteIds, typeIds, titleImg, recommend, title,attr,orderBy);
		if (first != null) {
			f.setFirstResult(first);
		}
		if (count != null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public Pagination getPageByChannelIdsForTag(Integer[] channelIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr, int orderBy, int option, int pageNo,int pageSize) {
		Finder f = byChannelIds(channelIds, typeIds, titleImg, recommend,
				title, attr,orderBy,option);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<Content> getListByChannelIdsForTag(Integer[] channelIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr,int orderBy, int option, Integer first, Integer count) {
		Finder f = byChannelIds(channelIds, typeIds, titleImg, recommend,
				title, attr,orderBy,option);
		if (first != null) {
			f.setFirstResult(first);
		}
		if (count != null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public Pagination getPageByChannelPathsForTag(String[] paths,
			Integer[] siteIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title, Map<String,String[]>attr,int orderBy,int pageNo, int pageSize) {
		Finder f = byChannelPaths(paths, siteIds, typeIds, titleImg, recommend,
				title,attr,orderBy);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<Content> getListByChannelPathsForTag(String[] paths,
			Integer[] siteIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title,Map<String,String[]>attr,int orderBy, Integer first, Integer count) {
		Finder f = byChannelPaths(paths, siteIds, typeIds, titleImg, recommend,
				title, attr,orderBy);
		if (first != null) {
			f.setFirstResult(first);
		}
		if (count != null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public Pagination getPageByTopicIdForTag(Integer topicId,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Boolean titleImg, Boolean recommend, String title, Map<String,String[]>attr,int orderBy,int pageNo,
			int pageSize) {
		Finder f = byTopicId(topicId, siteIds, channelIds, typeIds, titleImg,
				recommend, title, attr,orderBy);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<Content> getListByTopicIdForTag(Integer topicId,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Boolean titleImg, Boolean recommend, String title,Map<String,String[]>attr,int orderBy, Integer first,
			Integer count) {
		Finder f = byTopicId(topicId, siteIds, channelIds, typeIds, titleImg,
				recommend, title,attr,orderBy);
		if (first != null) {
			f.setFirstResult(first);
		}
		if (count != null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public Pagination getPageByTagIdsForTag(Integer[] tagIds,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Integer excludeId, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr, int orderBy,int pageNo, int pageSize) {
		Finder f = byTagIds(tagIds, siteIds, channelIds, typeIds, excludeId,
				titleImg, recommend, title,attr,orderBy);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<Content> getListByTagIdsForTag(Integer[] tagIds,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Integer excludeId, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr,int orderBy, Integer first, Integer count) {
		Finder f = byTagIds(tagIds, siteIds, channelIds, typeIds, excludeId,
				titleImg, recommend, title, attr,orderBy);
		if (first != null) {
			f.setFirstResult(first);
		}
		if (count != null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	private Finder bySiteIds(Integer[] siteIds, Integer[] typeIds,
			Boolean titleImg, Boolean recommend, String title, Map<String,String[]>attr,int orderBy) {
		Finder f = Finder.create("select  bean from Content bean where 1=1");
		//f.append(" join bean.contentExt as ext where 1=1");
		if (titleImg != null) {
			f.append(" and bean.hasTitleImg=:titleImg");
			f.setParam("titleImg", titleImg);
		}
		if (recommend != null) {
			f.append(" and bean.recommend=:recommend");
			f.setParam("recommend", recommend);
		}
		appendReleaseDate(f);
		appendTypeIds(f, typeIds);
		appendSiteIds(f, siteIds);
		f.append(" and bean.status=" + ContentCheck.CHECKED);
		if (!StringUtils.isBlank(title)) {
			f.append(" and bean.contentExt.title like :title");
			f.setParam("title", "%" + title + "%");
		}
		appendAttr(f, attr);
		appendOrder(f, orderBy);
		return f;
	}

	private Finder byChannelIds(Integer[] channelIds, Integer[] typeIds,
			Boolean titleImg, Boolean recommend, String title, Map<String,String[]>attr,int orderBy,int option) {
		Finder f = Finder.create();
		int len = channelIds.length;
		// 如果多个栏目
		if (option == 0 || len > 1) {
			f.append("select  bean from Content bean ");
			//f.append(" join bean.contentExt as ext");
			if (len == 1) {
				f.append(" where (bean.channel.id=:channelId)");
				f.setParam("channelId", channelIds[0]);
			} else {
				f.append(" where (bean.channel.id in (:channelIds))");
				f.setParamList("channelIds", channelIds);
			}
		} else if (option == 1) {
			// 包含子栏目
			f.append("select  bean from Content bean");
			//f.append(" join bean.contentExt as ext");
			f.append(" join bean.channel node,Channel parent");
			f.append(" where (node.lft between parent.lft and parent.rgt");
			f.append(" and bean.site.id=parent.site.id");
			f.append(" and parent.id=:channelId)");
			f.setParam("channelId", channelIds[0]);
		} else if (option == 2) {
			// 包含副栏目
			f.append("select  bean from Content bean");
			//f.append(" join bean.contentExt as ext");
			f.append(" join bean.channels as channel");
			f.append(" where (channel.id=:channelId)");
			f.setParam("channelId", channelIds[0]);
		}else {
			throw new RuntimeException("option value must be 0 or 1 or 2.");
		}
		/*
		else if(option == 3){
			f.append(" select  bean from Content bean");
			f.append(" join bean.channels as channel,Channel parent ");
			f.append(" where (channel.lft  between parent.lft and parent.rgt");
			f.append(" and bean.site.id=parent.site.id");
			f.append(" and parent.id=:channelId)");
			f.setParam("channelId", channelIds[0]);
		}
		*/
		if (titleImg != null) {
			f.append(" and bean.hasTitleImg=:titleImg");
			f.setParam("titleImg", titleImg);
		}
		if (recommend != null) {
			f.append(" and bean.recommend=:recommend");
			f.setParam("recommend", recommend);
		}
		appendReleaseDate(f);
		appendTypeIds(f, typeIds);
		f.append(" and bean.status=" + ContentCheck.CHECKED);
		if (!StringUtils.isBlank(title)) {
			f.append(" and bean.contentExt.title like :title");
			f.setParam("title", "%" + title + "%");
		}
		appendAttr(f, attr);
		appendOrder(f, orderBy);
		return f;
	}

	public Finder byChannelPaths(String[] paths, Integer[] siteIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title, Map<String,String[]>attr,int orderBy) {
		Finder f = Finder.create();
		f.append("select  bean from Content bean join bean.channel channel ");
		//f.append(" join bean.contentExt as ext");
		int len = paths.length;
		if (len == 1) {
			f.append(" where (channel.path=:path)").setParam("path", paths[0]);
		} else {
			f.append(" where (channel.path in (:paths))");
			f.setParamList("paths", paths);
		}
		if (siteIds != null) {
			len = siteIds.length;
			if (len == 1) {
				f.append(" and (channel.site.id=:siteId)");
				f.setParam("siteId", siteIds[0]);
			} else if (len > 1) {
				f.append(" and (channel.site.id in (:siteIds))");
				f.setParamList("siteIds", siteIds);
			}
		}
		if (titleImg != null) {
			f.append(" and bean.hasTitleImg=:titleImg");
			f.setParam("titleImg", titleImg);
		}
		if (recommend != null) {
			f.append(" and bean.recommend=:recommend");
			f.setParam("recommend", recommend);
		}
		appendReleaseDate(f);
		appendTypeIds(f, typeIds);
		f.append(" and bean.status=" + ContentCheck.CHECKED);
		if (!StringUtils.isBlank(title)) {
			f.append(" and bean.contentExt.title like :title");
			f.setParam("title", "%" + title + "%");
		}
		appendAttr(f, attr);
		appendOrder(f, orderBy);
		return f;
	}

	private Finder byTopicId(Integer topicId, Integer[] siteIds,
			Integer[] channelIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title, Map<String,String[]>attr,int orderBy) {
		Finder f = Finder.create();
		f.append("select bean from Content bean join bean.topics topic");
		//f.append(" join bean.contentExt as ext");
		f.append(" where topic.id=:topicId").setParam("topicId", topicId);
		if (titleImg != null) {
			f.append(" and bean.hasTitleImg=:titleImg");
			f.setParam("titleImg", titleImg);
		}
		if (recommend != null) {
			f.append(" and bean.recommend=:recommend");
			f.setParam("recommend", recommend);
		}
		appendReleaseDate(f);
		appendTypeIds(f, typeIds);
		appendChannelIds(f, channelIds);
		appendSiteIds(f, siteIds);
		f.append(" and bean.status=" + ContentCheck.CHECKED);
		if (!StringUtils.isBlank(title)) {
			f.append(" and bean.contentExt.title like :title");
			f.setParam("title", "%" + title + "%");
		}
		appendAttr(f, attr);
		appendOrder(f, orderBy);
		return f;
	}

	private Finder byTagIds(Integer[] tagIds, Integer[] siteIds,
			Integer[] channelIds, Integer[] typeIds, Integer excludeId,
			Boolean titleImg, Boolean recommend, String title, Map<String,String[]>attr,int orderBy) {
		Finder f = Finder.create();
		int len = tagIds.length;
		if (len == 1) {
			f.append("select bean from Content bean join bean.tags tag");
			//f.append(" join bean.contentExt as ext");
			f.append(" where tag.id=:tagId").setParam("tagId", tagIds[0]);
		} else {
			f.append("select bean from Content bean");
			//f.append(" join bean.contentExt as ext");
			f.append(" join bean.tags tag");
			f.append(" where tag.id in(:tagIds)");
			f.setParamList("tagIds", tagIds);
		}
		if (titleImg != null) {
			f.append(" and bean.hasTitleImg=:titleImg");
			f.setParam("titleImg", titleImg);
		}
		if (recommend != null) {
			f.append(" and bean.recommend=:recommend");
			f.setParam("recommend", recommend);
		}
		appendReleaseDate(f);
		appendTypeIds(f, typeIds);
		appendChannelIds(f, channelIds);
		appendSiteIds(f, siteIds);
		if (excludeId != null) {
			f.append(" and bean.id<>:excludeId");
			f.setParam("excludeId", excludeId);
		}
		f.append(" and bean.status=" + ContentCheck.CHECKED);
		if (!StringUtils.isBlank(title)) {
			f.append(" and bean.contentExt.title like :title");
			f.setParam("title", "%" + title + "%");
		}
		appendAttr(f, attr);
		appendOrder(f, orderBy);
		return f;
	}

	private void appendReleaseDate(Finder f) {
		f.append(" and bean.contentExt.releaseDate<:currentDate");
		//f.append(" and bean.sortDate<:currentDate");
		//f.setParam("currentDate", new Date());
		f.setParam("currentDate", contentQueryFreshTimeCache.getTime());
	}

	private void appendTypeIds(Finder f, Integer[] typeIds) {
		int len;
		if (typeIds != null) {
			len = typeIds.length;
			if (len == 1) {
				f.append(" and bean.type.id=:typeId");
				f.setParam("typeId", typeIds[0]);
			} else if (len > 1) {
				f.append(" and bean.type.id in (:typeIds)");
				f.setParamList("typeIds", typeIds);
			}
		}
	}

	private void appendChannelIds(Finder f, Integer[] channelIds) {
		int len;
		if (channelIds != null) {
			len = channelIds.length;
			if (len == 1) {
				f.append(" and bean.channel.id=:channelId");
				f.setParam("channelId", channelIds[0]);
			} else if (len > 1) {
				f.append(" and bean.channel.id in (:channelIds)");
				f.setParamList("channelIds", channelIds);
			}
		}
	}

	private void appendSiteIds(Finder f, Integer[] siteIds) {
		int len;
		if (siteIds != null) {
			len = siteIds.length;
			if (len == 1) {
				f.append(" and (bean.site.id=:siteId)");
				f.setParam("siteId", siteIds[0]);
			} else if (len > 1) {
				f.append(" and (bean.site.id in (:siteIds))");
				f.setParamList("siteIds", siteIds);
			}
		}
	}
	

	private void appendAttr(Finder f, Map<String,String[]>attr){
		if(attr!=null&&!attr.isEmpty()){
			Set<String>keys=attr.keySet();
			Iterator<String>keyIterator=keys.iterator();
			while(keyIterator.hasNext()){
				String key=keyIterator.next();
				String[] mapValue=attr.get(key);
				String value=mapValue[0],operate=mapValue[1];
				if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
					if(operate.equals(PARAM_ATTR_EQ)){
						f.append(" and bean.attr[:k"+key+"]=:v"+key).setParam("k"+key, key).setParam("v"+key, value);
					}else if(operate.equals(PARAM_ATTR_START)){
						f.append(" and bean.attr[:k"+key+"] like :v"+key).setParam("k"+key, key).setParam("v"+key, value+"%");
					}else if(operate.equals(PARAM_ATTR_END)){
						f.append(" and bean.attr[:k"+key+"] like :v"+key).setParam("k"+key, key).setParam("v"+key, "%"+value);
					}else if(operate.equals(PARAM_ATTR_LIKE)){
						f.append(" and bean.attr[:k"+key+"] like :v"+key).setParam("k"+key, key).setParam("v"+key, "%"+value+"%");
					}else if(operate.equals(PARAM_ATTR_IN)){
						if(StringUtils.isNotBlank(value)){
							f.append(" and bean.attr[:k"+key+"] in (:v"+key+")").setParam("k"+key, key);
							f.setParamList("v"+key, value.split(","));
						}
					}
					else {
						//取绝对值比较大小
						Float floatValue=Float.valueOf(value);
						if(operate.equals(PARAM_ATTR_GT)){
							if(floatValue>=0){
								f.append(" and (bean.attr[:k"+key+"]>=0 and abs(bean.attr[:k"+key+"])>:v"+key+")").setParam("k"+key, key).setParam("v"+key, floatValue);
							}else{
								f.append(" and ((bean.attr[:k"+key+"]<0 and abs(bean.attr[:k"+key+"])<:v"+key+") or bean.attr[:k"+key+"]>=0)").setParam("k"+key, key).setParam("v"+key, -floatValue);
							}
					 	}else if(operate.equals(PARAM_ATTR_GTE)){
					 		if(floatValue>=0){
								f.append(" and (abs(bean.attr[:k"+key+"])>=:v"+key+" and bean.attr[:k"+key+"]>=0)").setParam("k"+key, key).setParam("v"+key, floatValue);
							}else{
								f.append(" and ((abs(bean.attr[:k"+key+"])<=:v"+key+" and bean.attr[:k"+key+"]<0) or bean.attr[:k"+key+"]>=0)").setParam("k"+key, key).setParam("v"+key, -floatValue);
							}
						}else if(operate.equals(PARAM_ATTR_LT)){
							if(floatValue>=0){
								f.append(" and ((abs(bean.attr[:k"+key+"])<:v"+key+" and bean.attr[:k"+key+"]>=0) or bean.attr[:k"+key+"]<=0)").setParam("k"+key, key).setParam("v"+key, floatValue);
							}else{
								f.append(" and ((abs(bean.attr[:k"+key+"])>:v"+key+" and bean.attr[:k"+key+"]<0) or bean.attr[:k"+key+"]>=0)").setParam("k"+key, key).setParam("v"+key, -floatValue);
							}
						}else if(operate.equals(PARAM_ATTR_LTE)){
							if(floatValue>=0){
								f.append(" and ((abs(bean.attr[:k"+key+"])<=:v"+key+" and bean.attr[:k"+key+"]>=0) or bean.attr[:k"+key+"]<=0)").setParam("k"+key, key).setParam("v"+key, floatValue);
							}else{
								f.append(" and ((abs(bean.attr[:k"+key+"])>=:v"+key+" and bean.attr[:k"+key+"]<0) or bean.attr[:k"+key+"]>=0)").setParam("k"+key, key).setParam("v"+key, -floatValue);
							}
						}
					}
				}
			}
		}
	}
	
	
	private void appendOrder(Finder f, int orderBy) {
		switch (orderBy) {
		case 1:
			// ID升序
			f.append(" order by bean.id asc");
			break;
		case 2:
			// 发布时间降序
			f.append(" order by bean.sortDate desc");
			break;
		case 3:
			// 发布时间升序
			f.append(" order by bean.sortDate asc");
			break;
		case 4:
			// 固顶级别降序、发布时间降序
			f.append(" order by bean.topLevel desc, bean.sortDate desc");
			break;
		case 5:
			// 固顶级别降序、发布时间升序
			f.append(" order by bean.topLevel desc, bean.sortDate asc");
			break;
		case 6:
			// 日访问降序
			f.append(" and bean.contentCount.viewsDay>-1 ");
			f.append(" order by bean.contentCount.viewsDay desc");
			//f.append(", bean.id desc");
			break;
		case 7:
			// 周访问降序
			f.append(" and bean.contentCount.viewsWeek>-1 ");
			f.append(" order by bean.contentCount.viewsWeek desc");
			//f.append(", bean.id desc");
			break;
		case 8:
			// 月访问降序
			f.append(" and bean.contentCount.viewsMonth>-1 ");
			f.append(" order by bean.contentCount.viewsMonth desc");
			//f.append(", bean.id desc");
			break;
		case 9:
			// 总访问降序
			f.append(" and bean.contentCount.views>-1 ");
			f.append(" order by bean.contentCount.views desc");
			//f.append(", bean.id desc");
			break;
		case 10:
			// 日评论降序
			f.append(" order by bean.commentsDay desc");
			f.append(", bean.id desc");
			break;
		case 11:
			// 周评论降序
			f.append(" and bean.contentCount.commentsWeek>-1 ");
			f.append(" order by bean.contentCount.commentsWeek desc");
			//f.append(", bean.id desc");
			break;
		case 12:
			// 月评论降序
			f.append(" and bean.contentCount.commentsMonth>-1 ");
			f.append(" order by bean.contentCount.commentsMonth desc");
			//f.append(", bean.id desc");
			break;
		case 13:
			// 总评论降序
			f.append(" and bean.contentCount.comments>-1 ");
			f.append(" order by bean.contentCount.comments desc");
			//f.append(", bean.id desc");
			break;
		case 14:
			// 日下载降序
			f.append(" order by bean.downloadsDay desc");
			f.append(", bean.id desc");
			break;
		case 15:
			// 周下载降序
			f.append(" and bean.contentCount.downloadsWeek>-1 ");
			f.append(" order by bean.contentCount.downloadsWeek desc");
			//f.append(", bean.id desc");
			break;
		case 16:
			// 月下载降序
			f.append(" and bean.contentCount.downloadsMonth>-1 ");
			f.append(" order by bean.contentCount.downloadsMonth desc");
			//f.append(", bean.id desc");
			break;
		case 17:
			// 总下载降序
			f.append(" and bean.contentCount.downloads>-1 ");
			f.append(" order by bean.contentCount.downloads desc");
			//f.append(", bean.id desc");
			break;
		case 18:
			// 日顶降序
			f.append(" order by bean.upsDay desc");
			f.append(", bean.id desc");
			break;
		case 19:
			// 周顶降序
			f.append(" and bean.contentCount.upsWeek>-1 ");
			f.append(" order by bean.contentCount.upsWeek desc");
			//f.append(", bean.id desc");
			break;
		case 20:
			// 月顶降序
			f.append(" and bean.contentCount.upsMonth>-1 ");
			f.append(" order by bean.contentCount.upsMonth desc");
			//f.append(", bean.id desc");
			break;
		case 21:
			// 总顶降序
			f.append(" and bean.contentCount.ups>-1 ");
			f.append(" order by bean.contentCount.ups desc");
			//f.append(", bean.id desc");
			break;
		case 22:
			// 推荐级别降序、发布时间降序
			f.append(" order by bean.recommendLevel desc, bean.sortDate desc");
			break;
		case 23:
			// 推荐级别升序、发布时间降序
			f.append(" order by bean.recommendLevel asc, bean.sortDate desc");
			break;
		default:
			// 默认： ID降序
			f.append(" order by bean.id desc");
		}
	}

	public int countByChannelId(int channelId) {
		String hql = "select count(*) from Content bean"
				+ " join bean.channel channel,Channel parent"
				+ " where channel.lft between parent.lft and parent.rgt"
				+ " and channel.site.id=parent.site.id"
				+ " and parent.id=:parentId";
		Query query = getSession().createQuery(hql);
		query.setParameter("parentId", channelId);
		return ((Number) (query.iterate().next())).intValue();
	}

	public Content findById(Integer id) {
		Content entity = get(id);
		return entity;
	}

	public Content save(Content bean) {
		getSession().save(bean);
		return bean;
	}

	public Content deleteById(Integer id) {
		Content entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	/**不知为何要重写，此处先注释
	protected Pagination find(Finder finder, int pageNo, int pageSize) {
		int totalCount = countQueryResult(finder);
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());
			return p;
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		List list = query.list();
		p.setList(list);
		return p;
	}
	protected int countQueryResult(Finder finder) {
		Query query = getSession().createQuery(finder.getRowCountHql());
		finder.setParamsToQuery(query);
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		return ((Number) query.iterate().next()).intValue();
	} 
	*/
	
	private Finder createCollectFinder(Integer siteId, Integer memberId){
		Finder f = Finder.create("select bean from Content bean join bean.collectUsers user where user.id=:userId").setParam("userId", memberId);
		if (siteId != null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" and bean.status<>:status");
		f.setParam("status", ContentCheck.RECYCLE);
		return f;
	}

	@Override
	protected Class<Content> getEntityClass() {
		return Content.class;
	}
	@Autowired
	private ContentQueryFreshTimeCache contentQueryFreshTimeCache;
}