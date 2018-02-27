package com.jeecms.cms.manager.main;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentExt;
import com.jeecms.cms.entity.main.ContentTxt;
import com.jeecms.cms.entity.main.Content.ContentStatus;
import com.jeecms.cms.entity.main.ContentRecord.ContentOperateType;
import com.jeecms.cms.service.ContentListener;
import com.jeecms.cms.staticpage.exception.ContentNotCheckedException;
import com.jeecms.cms.staticpage.exception.GeneratedZeroStaticPageException;
import com.jeecms.cms.staticpage.exception.StaticPageNotOpenException;
import com.jeecms.cms.staticpage.exception.TemplateNotFoundException;
import com.jeecms.cms.staticpage.exception.TemplateParseException;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;

public interface ContentMng {
	public Pagination getPageByRight(Integer share,String title, Integer typeId,
			Integer currUserId,Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId,Integer userId, int orderBy, int pageNo,
			int pageSize);
	
	public Pagination getPageCountByRight(Integer share,String title, Integer typeId,
			Integer currUserId,Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId,
			Integer channelId,Integer userId, int orderBy, int pageNo,
			int pageSize);
	
	
	public Pagination getPageBySite(String title, Integer typeId,Integer currUserId,Integer inputUserId,boolean topLevel,
			boolean recommend,ContentStatus status, Integer siteId,int orderBy, int pageNo,int pageSize);
	
	public Pagination getPageCountBySite(String title, Integer typeId,Integer currUserId,Integer inputUserId,boolean topLevel,
			boolean recommend,ContentStatus status, Integer siteId,int orderBy, int pageNo,int pageSize);

	/**
	 * 获得文章分页。供会员中心使用。
	 * 
	 * @param title
	 *            文章标题
	 * @param channelId
	 *            栏目ID
	 * @param siteId
	 *            站点ID
	 * @param memberId
	 *            会员ID
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页大小
	 * @return 文章分页对象
	 */
	public Pagination getPageForMember(String title, Integer channelId,
			Integer siteId, Integer modelId,Integer memberId, int pageNo, int pageSize);
	
	public List<Content> getListForMember(String title, Integer channelId,
			Integer siteId, Integer modelId,Integer memberId, int first, int count);
	

	/**
	 * 根据内容ID数组获取文章列表
	 * 
	 * @param ids
	 * @param orderBy
	 * @return
	 */
	public List<Content> getListByIdsForTag(Integer[] ids, int orderBy);
	
	/**
	 * 查询固顶级别大于topLevel 且到期内容
	 * @param topLevel 固顶级别
	 * @param expiredDay 固顶到期日期
	 * @return
	 */
	public  List<Content> getExpiredTopLevelContents(byte topLevel,Date expiredDay);
	
	/**
	 * 查询到了归档日期内容
	 * @param pigeonholeDay
	 * @return
	 */
	public  List<Content> getPigeonholeContents(Date pigeonholeDay);

	public Content getSide(Integer id, Integer siteId, Integer channelId,
			boolean next);

	public Pagination getPageBySiteIdsForTag(Integer[] siteIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title, Map<String,String[]>attr,int orderBy,int pageNo, int pageSize);

	public List<Content> getListBySiteIdsForTag(Integer[] siteIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr,int orderBy, Integer first, Integer count);

	public Pagination getPageByChannelIdsForTag(Integer[] channelIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title, Map<String,String[]>attr,int orderBy, int option, int pageNo,int pageSize);

	public List<Content> getListByChannelIdsForTag(Integer[] channelIds,
			Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr,int orderBy, int option, Integer first, Integer count);

	public Pagination getPageByChannelPathsForTag(String[] paths,
			Integer[] siteIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title, Map<String,String[]>attr,int orderBy,int pageNo, int pageSize);

	public List<Content> getListByChannelPathsForTag(String[] paths,
			Integer[] siteIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title,Map<String,String[]>attr,int orderBy, Integer first, Integer count);

	public Pagination getPageByTopicIdForTag(Integer topicId,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Boolean titleImg, Boolean recommend, String title,Map<String,String[]>attr,int orderBy, int pageNo,
			int pageSize);

	public List<Content> getListByTopicIdForTag(Integer topicId,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Boolean titleImg, Boolean recommend, String title,Map<String,String[]>attr,int orderBy, Integer first,
			Integer count);

	public Pagination getPageByTagIdsForTag(Integer[] tagIds,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Integer excludeId, Boolean titleImg, Boolean recommend,
			String title, Map<String,String[]>attr,int orderBy,int pageNo, int pageSize);

	public List<Content> getListByTagIdsForTag(Integer[] tagIds,
			Integer[] siteIds, Integer[] channelIds, Integer[] typeIds,
			Integer excludeId, Boolean titleImg, Boolean recommend,
			String title,Map<String,String[]>attr,int orderBy, Integer first, Integer count);

	public Content findById(Integer id);

	public Content save(Content bean, ContentExt ext, ContentTxt txt,Integer[] channelIds,
			Integer[] topicIds, Integer[] viewGroupIds, String[] tagArr,
			String[] attachmentPaths, String[] attachmentNames,
			String[] attachmentFilenames, String[] picPaths,
			String[] picDescs, Integer channelId, Integer typeId,
			Boolean draft, Boolean contribute,Short charge,
			Double chargeAmount,Boolean rewardPattern,
			Double rewardRandomMin,Double rewardRandomMax,
			Double[] rewardFix,CmsUser user,
			boolean forMember);
	public Content savePY(Content bean, ContentExt ext, ContentTxt txt,Integer[] channelIds,
						Integer[] topicIds, Integer[] viewGroupIds, String[] tagArr,
						String[] attachmentPaths, String[] attachmentNames,
						String[] attachmentFilenames, String[] picPaths,
						String[] picDescs, Integer channelId, Integer typeId,
						Boolean draft, Boolean contribute,Short charge,
						Double chargeAmount,Boolean rewardPattern,
						Double rewardRandomMin,Double rewardRandomMax,
						Double[] rewardFix,CmsUser user,
						boolean forMember);

	public Content save(Content bean, ContentExt ext, ContentTxt txt,Integer channelId,
			Integer typeId,Boolean draft, CmsUser user, boolean forMember);

	public Content update(Content bean, ContentExt ext, ContentTxt txt,String[] tagArr,
			Integer[] channelIds, Integer[] topicIds, Integer[] viewGroupIds,
			String[] attachmentPaths, String[] attachmentNames,
			String[] attachmentFilenames, String[] picPaths,
			String[] picDescs, Map<String, String> attr, Integer channelId,
			Integer typeId, Boolean draft, Short charge,
			Double chargeAmount,Boolean rewardPattern,
			Double rewardRandomMin,Double rewardRandomMax,
			Double[] rewardFix,CmsUser user,
			boolean forMember);
	
	public Content update(Content bean);
	
	public Content update(CmsUser user,Content bean,ContentOperateType operate);
	
	public Content updateByChannelIds(Integer contentId,Integer[]channelIds,Integer operate);
	
	public Content addContentToTopics(Integer contentId,Integer[]topicIds);

	public Content check(Integer id, CmsUser user);

	public Content[] check(Integer[] ids, CmsUser user);
	
	public Content submit(Integer id, CmsUser user);
	
	public Content[] submit(Integer[] ids, CmsUser user);
	
	public Content reject(Integer id, CmsUser user, Byte step, String opinion);

	public Content[] reject(Integer[] ids, CmsUser user,Byte step, String opinion);

	public Content cycle(CmsUser user,Integer id);

	public Content[] cycle(CmsUser user,Integer[] ids);

	public Content recycle(Integer id);

	public Content[] recycle(Integer[] ids);

	public Content deleteById(Integer id);

	public Content[] deleteByIds(Integer[] ids);
	
	public Content[] contentStatic(CmsUser user,Integer[] ids)
			throws TemplateNotFoundException, TemplateParseException,
			GeneratedZeroStaticPageException, StaticPageNotOpenException,
			ContentNotCheckedException;
	
	public Pagination getPageForCollection(Integer siteId, Integer memberId, int pageNo, int pageSize);
	
	public List<Content> getListForCollection(Integer siteId, Integer memberId, Integer first, Integer count);
	
	public void updateFileByContent(Content bean,Boolean valid);
	
	public List<ContentListener> getListenerList();
	
	public List<Map<String, Object>> preChange(Content content);
	
}