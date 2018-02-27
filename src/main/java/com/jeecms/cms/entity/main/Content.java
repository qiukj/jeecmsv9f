package com.jeecms.cms.entity.main;

import static com.jeecms.common.web.Constants.SPT;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.Constants;
import com.jeecms.cms.entity.assist.CmsComment;
import com.jeecms.cms.entity.assist.CmsScoreRecord;
import com.jeecms.cms.entity.main.Channel.AfterCheckEnum;
import com.jeecms.cms.entity.main.base.BaseContent;
import com.jeecms.cms.staticpage.StaticPageUtils;
import com.jeecms.cms.web.CmsThreadVariable;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.web.ContentInterface;


public class Content extends BaseContent implements ContentInterface {
	private static final long serialVersionUID = 1L;
	//内容信息返回简化格式
	public static final int CONTENT_INFO_SIMPLE=1;
	//内容信息返回更全
	public static final int CONTENT_INFO_WHOLE=0;
	/**
	 * 内容查询模式 1 共享他站内容
	 */
	public static final Integer CONTENT_QUERY_SHARE=1;
	/**
	 * 本站内容
	 */
	public static final Integer CONTENT_QUERY_NOT_SHARE=0;
	/**
	 * 查询数据
	 */
	public static final Integer QUERY_DATA=1;
	/**
	 * 查询分页数据
	 */
	public static final Integer QUERY_PAGE=0;
	/**
	 * 查询分页数据和数据
	 */
	public static final Integer QUERY_TOTAL=2;
	
	/**
	 * 删除副栏目
	 */
	public static final Integer CONTENT_CHANNEL_DEL=1;
	/**
	 * 添加副栏目
	 */
	public static final Integer CONTENT_CHANNEL_ADD=0;
	/**
	 * 状态
	 */
	public enum ContentStatus {
		/**
		 * 所有
		 */
		all,
		/**
		 * 草稿
		 */
		draft,
		/**
		 * 待审核
		 */
		prepared,
		/**
		 * 已审
		 */
		passed,
		/**
		 * 终审
		 */
		checked,
		/**
		 * 退回
		 */
		rejected,
		/**
		 * 回收
		 */
		recycle,
		/**
		 * 投稿
		 */
		contribute,
		/**
		 * 归档
		 */
		pigeonhole
	};
	
	public static int DATA_CONTENT=0;

	private DateFormat df = new SimpleDateFormat("/yyyyMMdd");

	public Boolean getStaticContent() {
		Channel channel = getChannel();
		if (channel != null) {
			return channel.getStaticContent();
		} else {
			return null;
		}
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
		if (getStaticContent()) {
			return getUrlStatic(null, 1);
		} else if(!StringUtils.isBlank(getSite().getDomainAlias())){
			return getUrlDynamic(null);
		}else{
			return getUrlDynamic(true);
		}
	}
	
	public String getHttpsUrl() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticContent()) {
			return getHttpsUrlStatic(false, 1);
		} else if(!StringUtils.isBlank(getSite().getDomainAlias())){
			return getHttpsUrlDynamic(null);
		}else{
			return getHttpsUrlDynamic(true);
		}
	}
	
	public String getMobileUrl() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticContent()) {
			return getMobileUrlStatic(false, 1);
		} else {
//			return getUrlDynamic(null);
			//此处共享了别站信息需要绝句路径，做了更改 于2012-7-26修改
			return getUrlDynamic(true);
		}
	}

	public String getUrlWhole() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticContent()) {
			return getUrlStatic(true, 1);
		} else {
			return getUrlDynamic(true);
		}
	}
	
	public String getMobileUrlWhole() {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		if (getStaticContent()) {
			return getMobileUrlStatic(true, 1);
		} else {
			return getUrlDynamic(true);
		}
	}

	public String getUrlStatic() {
		return getUrlStatic(null, 1);
	}

	public String getUrlStatic(int pageNo) {
		return getUrlStatic(null, pageNo);
	}
	
	public String getSoureUrl(){
		String sourceUrl=getUrl();
		StringBuilder url = new StringBuilder();
		if(!sourceUrl.startsWith(getSite().getProtocol())){
			CmsSite site=getSite();
			url.append(site.getProtocol()).append(site.getDomain());
			if (site.getPort() != null) {
				url.append(":").append(site.getPort());
			}
			url.append(sourceUrl);
			sourceUrl=url.toString();
		}
		return sourceUrl;
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
					url.append(filename.subSequence(0, index)).append("_");
					url.append(pageNo).append(
							filename.subSequence(index, filename.length()));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态路径
			url.append(SPT).append(getChannel().getPath());
			url.append(df.format(getReleaseDate()));
			url.append(SPT).append(getId());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
			}
			url.append(site.getStaticSuffix());

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
					url.append(filename.subSequence(0, index)).append("_");
					url.append(pageNo).append(
							filename.subSequence(index, filename.length()));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态路径
			url.append(SPT).append(getChannel().getPath());
			url.append(df.format(getReleaseDate()));
			url.append(SPT).append(getId());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
			}
			url.append(site.getStaticSuffix());

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
					url.append(filename.subSequence(0, index)).append("_");
					url.append(pageNo).append(
							filename.subSequence(index, filename.length()));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态路径
			url.append(SPT).append(getChannel().getPath());
			url.append(df.format(getReleaseDate()));
			url.append(SPT).append(getId());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
			}
			url.append(site.getStaticSuffix());

		}
		return url.toString();
	}

	public String getUrlDynamic() {
		return getUrlDynamic(null);
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
					url.append(filename.substring(0, index));
					url.append("_").append(pageNo);
					url.append(filename.substring(index));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态路径
			url.append(SPT).append(getChannel().getPath());
			url.append(df.format(getReleaseDate()));
			url.append(SPT).append(getId());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
			}
			url.append(site.getStaticSuffix());
		}
		return url.toString();
	}
	
	//获取手机静态页面文件名
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
					url.append(filename.substring(0, index));
					url.append("_").append(pageNo);
					url.append(filename.substring(index));
				} else {
					url.append(filename).append("_").append(pageNo);
				}
			} else {
				url.append(filename);
			}
		} else {
			// 默认静态路径
			url.append(SPT).append(getChannel().getPath());
			url.append(df.format(getReleaseDate()));
			url.append(SPT).append(getId());
			if (pageNo > 1) {
				url.append("_").append(pageNo);
			}
			url.append(site.getStaticSuffix());
		}
		return url.toString();
	}

	public String getStaticFilenameByRule() {
		Channel channel = getChannel();
		CmsModel model = channel.getModel();
		String rule = channel.getContentRule();
		if (StringUtils.isBlank(rule)) {
			return null;
		}
		String url = StaticPageUtils.staticUrlRule(rule, model.getId(), model
				.getPath(), channel.getId(), channel.getPath(), getId(),
				getReleaseDate());
		return url;
	}

	public String getUrlDynamic(Boolean whole) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getUrlBuffer(true, whole, false);
		url.append(SPT).append(getChannel().getPath());
		url.append(SPT).append(getId()).append(site.getDynamicSuffix());
		return url.toString();
	}
	
	public String getHttpsUrlDynamic(Boolean whole) {
		if (!StringUtils.isBlank(getLink())) {
			return getLink();
		}
		CmsSite site = getSite();
		StringBuilder url = site.getHttpsUrlBuffer(true, whole, false);
		url.append(SPT).append(getChannel().getPath());
		url.append(SPT).append(getId()).append(site.getDynamicSuffix());
		return url.toString();
	}

	public Set<Channel> getChannelsWithoutMain() {
		Set<Channel> set = new HashSet<Channel>(getChannels());
		set.remove(getChannel());
		return set;
	}

	public void setContentTxt(ContentTxt txt) {
		Set<ContentTxt> set = getContentTxtSet();
		if (set == null) {
			set = new HashSet<ContentTxt>();
			setContentTxtSet(set);
		}
		if (!set.isEmpty()) {
			set.clear();
		}
		set.add(txt);
	}

	public void setContentCheck(ContentCheck check) {
		Set<ContentCheck> set = getContentCheckSet();
		if (set == null) {
			set = new HashSet<ContentCheck>();
			setContentCheckSet(set);
		}
		if (!set.isEmpty()) {
			set.clear();
		}
		set.add(check);
	}
	
	public void setContentCharge(ContentCharge charge) {
		Set<ContentCharge> set = getContentChargeSet();
		if (set == null) {
			set = new HashSet<ContentCharge>();
			setContentChargeSet(set);
		}
		if (!set.isEmpty()) {
			set.clear();
		}
		set.add(charge);
	}

	public void addToChannels(Channel channel) {
		Set<Channel> channels = getChannels();
		if (channels == null) {
			channels = new HashSet<Channel>();
			setChannels(channels);
		}
		channels.add(channel);
	}
	
	public void removeSelfAddToChannels(Channel channel) {
		Set<Channel> channels = getChannels();
		if (channels == null) {
			channels = new HashSet<Channel>();
			setChannels(channels);
		}
		channels.remove(getChannel());
		channels.add(channel);
	}

	public void addToTopics(CmsTopic topic) {
		Set<CmsTopic> topics = getTopics();
		if (topics == null) {
			topics = new HashSet<CmsTopic>();
			setTopics(topics);
		}
		topics.add(topic);
	}

	public void addToGroups(CmsGroup group) {
		Set<CmsGroup> groups = getViewGroups();
		if (groups == null) {
			groups = new HashSet<CmsGroup>();
			setViewGroups(groups);
		}
		groups.add(group);
	}

	public void addToAttachmemts(String path, String name, String filename) {
		List<ContentAttachment> list = getAttachments();
		if (list == null) {
			list = new ArrayList<ContentAttachment>();
			setAttachments(list);
		}
		ContentAttachment ca = new ContentAttachment(path, name, 0);
		if (!StringUtils.isBlank(filename)) {
			ca.setFilename(filename);
		}
		list.add(ca);
	}

	public void addToPictures(String path, String desc) {
		List<ContentPicture> list = getPictures();
		if (list == null) {
			list = new ArrayList<ContentPicture>();
			setPictures(list);
		}
		ContentPicture cp = new ContentPicture();
		cp.setImgPath(path);
		cp.setDescription(desc);
		list.add(cp);
	}
	
	public void addToRewardFixs(Double fixVal) {
		List<ContentRewardFix> list = getRewardFixs();
		if (list == null) {
			list = new ArrayList<ContentRewardFix>();
			setRewardFixs(list);
		}
		ContentRewardFix rewardFix = new ContentRewardFix();
		rewardFix.setFixVal(fixVal);
		list.add(rewardFix);
	}

	public String getTagStr() {
		List<ContentTag> tags = getTags();
		if (tags != null && tags.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (ContentTag tag : tags) {
				if(tag!=null&&tag.getName()!=null){
					sb.append(tag.getName()).append(',');
				}
			}
			return sb.substring(0, sb.length() - 1);
		} else {
			return null;
		}
	}

	/**
	 * 是否草稿
	 * 
	 * @return
	 */
	public boolean isDraft() {
		return ContentCheck.DRAFT == getStatus();
	}

	/**
	 * 是否终审通过
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return ContentCheck.CHECKED == getStatus();
	}

	public Set<CmsGroup> getViewGroupsExt() {
		Set<CmsGroup> set = getViewGroups();
		if (set != null && set.size() > 0) {
			return set;
		} else {
			return getChannel().getViewGroups();
		}
	}

	public String getTplContentOrDef(CmsModel model) {
		String tpl = getTplContent();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getChannel().getTplContentOrDef(model);
		}
	}
	
	public String getMobileTplContentOrDef(CmsModel model) {
		String tpl = getMobileTplContent();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getChannel().getMobileTplContentOrDef(model);
		}
	}

	/**
	 * 是否有审核后的编辑权限。从CmsThread中获得当前用户。
	 * 
	 * @return
	 */
	public boolean isHasUpdateRight() {
		CmsUser user = CmsThreadVariable.getUser();
		if (user == null) {
			throw new IllegalStateException("CmsUser not found in CmsThread");
		}
		return isHasUpdateRight(user);
	}

	/**
	 * 是否有审核后的编辑权限
	 * 
	 * @param user
	 * @return
	 */
	public boolean isHasUpdateRight(CmsUser user) {
		AfterCheckEnum after = getChannel().getAfterCheckEnum();
		if (AfterCheckEnum.CANNOT_UPDATE == after) {
			CmsSite site = getSite();
			Byte userStep = user.getCheckStep(site.getId());
			Byte channelStep = getChannel().getFinalStepExtends();
			boolean checked = getStatus() == ContentCheck.CHECKED;
			// 如果内容审核级别大于用户审核级别，或者内容已经审核且用户审核级别小于栏目审核级别。
			if (getCheckStep() > userStep
					|| (checked && userStep < channelStep)) {
				return false;
			} else {
				return true;
			}
		} else if (AfterCheckEnum.BACK_UPDATE == after
				|| AfterCheckEnum.KEEP_UPDATE == after) {
			return true;
		} else {
			throw new RuntimeException("AfterCheckEnum '" + after
					+ "' did not handled");
		}
	}

	/**
	 * 是否有审核后的删除权限。从CmsThread中获得当前用户。
	 * 
	 * @return
	 */
	public boolean isHasDeleteRight() {
		CmsUser user = CmsThreadVariable.getUser();
		if (user == null) {
			throw new IllegalStateException("CmsUser not found in CmsThread");
		}
		return isHasDeleteRight(user);
	}

	/**
	 * 是否有审核后的删除权限
	 * 
	 * @param user
	 * @return
	 */
	public boolean isHasDeleteRight(CmsUser user) {
		AfterCheckEnum after = getChannel().getAfterCheckEnum();
		if (AfterCheckEnum.CANNOT_UPDATE == after) {
			CmsSite site = getSite();
			Byte userStep = user.getCheckStep(site.getId());
			Byte channelStep = getChannel().getFinalStepExtends();
			boolean checked = getStatus() == ContentCheck.CHECKED;
			// 如果内容审核级别大于用户审核级别，或者内容已经审核且用户审核级别小于栏目审核级别。
			if (getCheckStep() > userStep
					|| (checked && userStep < channelStep)) {
				return false;
			} else {
				return true;
			}
		} else if (AfterCheckEnum.BACK_UPDATE == after
				|| AfterCheckEnum.KEEP_UPDATE == after) {
			return true;
		} else {
			throw new RuntimeException("AfterCheckEnum '" + after
					+ "' did not handled");
		}
	}

	public void init() {
		short zero = 0;
		byte bzero = 0;
		if (getViewsDay() == null) {
			setViewsDay(0);
		}
		if (getCommentsDay() == null) {
			setCommentsDay(zero);
		}
		if (getDownloadsDay() == null) {
			setDownloadsDay(zero);
		}
		if (getUpsDay() == null) {
			setUpsDay(zero);
		}
		if (getHasTitleImg() == null) {
			setHasTitleImg(false);
		}
		if (getRecommend() == null) {
			setRecommend(false);
		}
		if (getSortDate() == null) {
			setSortDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getTopLevel() == null) {
			setTopLevel(bzero);
		}
		// 保存后立即生成静态化，如果这些值为null，则需要在模板中增加判断，使模板编写变得复杂。
		if (getChannels() == null) {
			setChannels(new HashSet<Channel>());
		}
		if (getTopics() == null) {
			setTopics(new HashSet<CmsTopic>());
		}
		if (getViewGroups() == null) {
			setViewGroups(new HashSet<CmsGroup>());
		}
		if (getTags() == null) {
			setTags(new ArrayList<ContentTag>());
		}
		if (getPictures() == null) {
			setPictures(new ArrayList<ContentPicture>());
		}
		if (getAttachments() == null) {
			setAttachments(new ArrayList<ContentAttachment>());
		}
		if(getScore()==null){
			setScore(0);
		}
		if(getRecommendLevel()==null){
			setRecommendLevel(bzero);
		}
		if (getStatus()==null) {
			setStatus((byte)0);
		}
	}

	public int getPageCount() {
		int txtCount = getTxtCount();
		/*图片集合应该特殊处理，不能作为文章本身分页依据
		if (txtCount <= 1) {
			List<ContentPicture> pics = getPictures();
			if (pics != null) {
				int picCount = pics.size();
				if (picCount > 1) {
					return picCount;
				}
			}
		}
		*/
		return txtCount;
	}

	public int getTxtCount() {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTxtCount();
		} else {
			return 1;
		}
	}

	public ContentPicture getPictureByNo(int pageNo) {
		List<ContentPicture> list = getPictures();
		if (pageNo >= 1 && list != null && list.size() >= pageNo) {
			return list.get(pageNo - 1);
		} else {
			return null;
		}
	}

	public String getTxtByNo(int pageNo) {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTxtByNo(pageNo);
		} else {
			return null;
		}
	}

	public String getTitleByNo(int pageNo) {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTitleByNo(pageNo);
		} else {
			return getTitle();
		}
	}

	public String getStitle() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getStitle();
		} else {
			return null;
		}
	}

	public String getTitle() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTitle();
		} else {
			return null;
		}
	}

	public String getShortTitle() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getShortTitle();
		} else {
			return null;
		}
	}

	public String getDescription() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getDescription();
		} else {
			return null;
		}
	}

	public String getAuthor() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getAuthor();
		} else {
			return null;
		}
	}

	public String getOrigin() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getOrigin();
		} else {
			return null;
		}
	}

	public String getOriginUrl() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getOriginUrl();
		} else {
			return null;
		}
	}

	public Date getReleaseDate() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getReleaseDate();
		} else {
			return null;
		}
	}
	
	public Date getTopLevelDate() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTopLevelDate();
		} else {
			return null;
		}
	}
	
	public Date getPigeonholeDate() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getPigeonholeDate();
		} else {
			return null;
		}
	}

	public String getMediaPath() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getMediaPath();
		} else {
			return null;
		}
	}

	public String getMediaType() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getMediaType();
		} else {
			return null;
		}
	}

	public String getTitleColor() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTitleColor();
		} else {
			return null;
		}
	}

	public Boolean getBold() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getBold();
		} else {
			return null;
		}
	}

	public String getTitleImg() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTitleImg();
		} else {
			return null;
		}
	}

	public String getContentImg() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getContentImg();
		} else {
			return null;
		}
	}

	public String getTypeImg() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTypeImg();
		} else {
			return null;
		}
	}
	
	public String getTypeImgWhole(){
		if (!StringUtils.isBlank(getTypeImg())) {
			CmsSite site=getSite();
			return site.getProtocol()+site.getDomain()+":"+site.getPort()+getTypeImg();
		} else {
			return getTitle();
		}
	}
	
	public String getTitleImgWhole(){
		if (!StringUtils.isBlank(getTitleImg())) {
			CmsSite site= getSite();
			return site.getProtocol()+site.getDomain()+":"+site.getPort()+getTitleImg();
		} else {
			return getTitle();
		}
	}
	
	public String getContentImgWhole(){
		if (!StringUtils.isBlank(getContentImg())) {
			CmsSite site= getSite();
			return site.getProtocol()+site.getDomain()+":"+site.getPort()+getContentImgWhole();
		} else {
			return getTitle();
		}
	}
	

	public String getLink() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getLink();
		} else {
			return null;
		}
	}

	public String getTplContent() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTplContent();
		} else {
			return null;
		}
	}
	
	public String getMobileTplContent() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getTplMobileContent();
		} else {
			return null;
		}
	}
	
	public Boolean getNeedRegenerate() {
		ContentExt ext = getContentExt();
		if (ext != null) {
			return ext.getNeedRegenerate();
		} else {
			return null;
		}
	}
	
	public void setNeedRegenerate(Boolean isNeed) {
		ContentExt ext = getContentExt();
		if (ext != null) {
			ext.setNeedRegenerate(isNeed);
		}
	}

	public String getTxt() {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTxt();
		} else {
			return null;
		}
	}

	public String getTxt1() {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTxt1();
		} else {
			return null;
		}
	}

	public String getTxt2() {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTxt2();
		} else {
			return null;
		}
	}

	public String getTxt3() {
		ContentTxt txt = getContentTxt();
		if (txt != null) {
			return txt.getTxt3();
		} else {
			return null;
		}
	}

	public Integer getViews() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getViews();
		} else {
			return null;
		}
	}
	
	public Integer getViewsMonth() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getViewsMonth();
		} else {
			return null;
		}
	}
	public Integer getViewsWeek() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getViewsWeek();
		} else {
			return null;
		}
	}
	public Integer getViewDay() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getViewsDay();
		} else {
			return null;
		}
	}

	public Integer getCommentsCount() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getComments();
		} else {
			return null;
		}
	}
	
	public Integer getCommentsCheckedNum() {
		Set<CmsComment> comments = getComments();
		int num=0;
		if (comments != null) {
			for(CmsComment comment:comments){
				if(comment.getChecked()==1){
					num++;
				}
			}
			return num;
		} else {
			return 0;
		}
	}
	
	public boolean hasCommentUser(CmsUser user){
		Set<CmsComment>comments=getComments();
		if(comments==null){
			return false;
		}
		Iterator<CmsComment>it=comments.iterator();
		while(it.hasNext()){
			CmsComment comment=it.next();
			if(comment.getCommentUser()!=null&&comment.getCommentUser().equals(user)){
				return true;
			}
		}
		return false;
	}

	public Integer getUps() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getUps();
		} else {
			return null;
		}
	}

	public Integer getDowns() {
		ContentCount count = getContentCount();
		if (count != null) {
			return count.getDowns();
		} else {
			return null;
		}
	}
	
	public Byte getCheckStep() {
		ContentCheck check = getContentCheck();
		if (check != null) {
			return check.getCheckStep();
		} else {
			return null;
		}
	}

	public String getCheckOpinion() {
		ContentCheck check = getContentCheck();
		if (check != null) {
			return check.getCheckOpinion();
		} else {
			return null;
		}
	}

	public Boolean getRejected() {
		ContentCheck check = getContentCheck();
		if (check != null) {
			return check.getRejected();
		} else {
			return null;
		}
	}

	public ContentTxt getContentTxt() {
		Set<ContentTxt> set = getContentTxtSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}

	public ContentCheck getContentCheck() {
		Set<ContentCheck> set = getContentCheckSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}
	
	public String getDesc() {
		return getDescription();
	}

	public String getCtgName() {
		return getChannel().getName();
	}

	public String getCtgUrl() {
		return getChannel().getUrl();
	}

	public String getImgUrl() {
		return getTitleImg();
	}

	public String getImgUrl2() {
		return getTypeImg();
	}

	public String getStit() {
		String stit = getShortTitle();
		if (!StringUtils.isBlank(stit)) {
			return stit;
		} else {
			return getTit();
		}
	}

	public String getTit() {
		return getTitle();
	}

	public String getTitCol() {
		return getTitleColor();
	}

	public Integer getSiteId() {
		return getSite().getId();
	}
	
	public String getSiteName() {
		return getSite().getName();
	}

	public String getSiteUrl() {
		return getSite().getUrl();
	}
	
	public String getCompanyName(){
		return getSite().getSiteCompany().getName();
	}
	
	public String getCompanyAddr(){
		return getSite().getSiteCompany().getAddress();
	}
	
	public String getCompanyScale(){
		return getSite().getSiteCompany().getScale();
	}
	
	public String getCompanyNature(){
		return getSite().getSiteCompany().getNature();
	}
	
	public String getCompanyIndustry(){
		return getSite().getSiteCompany().getIndustry();
	}
	
	public String getCompanyDesc(){
		return getSite().getSiteCompany().getDescription();
	}
	
	public String getCompanyContact(){
		return getSite().getSiteCompany().getContact();
	}
	
	public Integer[]getChannelIds(){
		Set<Channel>channels=getChannels();
		return Channel.fetchIds(channels);
	}
	
	public Integer[]getChannelIdsWithoutChannel(){
		Set<Channel>channels=getChannels();
		channels.remove(getChannel());
		return Channel.fetchIds(channels);
	}
	
	public Integer[]getTopicIds(){
		Set<CmsTopic>topics=getTopics();
		return CmsTopic.fetchIds(topics);
	}
	
	public Integer[]getViewGroupIds(){
		Set<CmsGroup>groups =getViewGroups();
		return CmsGroup.fetchIds(groups);
	}
	
	public String[]getAttachmentPaths(){
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		String[]attachmentPaths=new String[attList.size()];
		for(int i=0;i<attachmentPaths.length;i++){
			attachmentPaths[i]=attList.get(i).getPath();
		}
		return attachmentPaths;
	}
	
	public String getAttachmentPathStr() {
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<attList.size();i++){
			sb.append(attList.get(i).getPath()).append(',');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String getAttachmentPathStr(String prefix) {
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<attList.size();i++){
			sb.append(prefix).append(attList.get(i).getPath()).append(',');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String[]getAttachmentNames(){
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		String[]attachmentNames=new String[attList.size()];
		for(int i=0;i<attachmentNames.length;i++){
			attachmentNames[i]=attList.get(i).getName();
		}
		return attachmentNames;
	}
	
	public String getAttachmentNameStr() {
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<attList.size();i++){
			sb.append(attList.get(i).getName()).append(',');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String[]getAttachmentFileNames(){
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		String[]attachmentFileNames=new String[attList.size()];
		for(int i=0;i<attachmentFileNames.length;i++){
			attachmentFileNames[i]=attList.get(i).getFilename();
		}
		return attachmentFileNames;
	}
	
	public String getAttachmentFileNameStr() {
		List<ContentAttachment>attList=getAttachments();
		if(attList==null||attList.size()<=0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<attList.size();i++){
			sb.append(attList.get(i).getFilename()).append(',');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String[]getPicPaths(){
		List<ContentPicture>pics=getPictures();
		if(pics==null||pics.size()<=0){
			return null;
		}
		String[]picPaths=new String[pics.size()];
		for(int i=0;i<picPaths.length;i++){
			picPaths[i]=pics.get(i).getImgPath();
		}
		return picPaths;
	}
	
	public String getPicPathStr() {
		List<ContentPicture>pics=getPictures();
		if(pics==null||pics.size()<=0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<pics.size();i++){
			sb.append(pics.get(i).getImgPath()).append(',');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String getPicPathStr(String prefix) {
		List<ContentPicture>pics=getPictures();
		if(pics==null||pics.size()<=0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<pics.size();i++){
			sb.append(prefix).append(pics.get(i).getImgPath()).append(',');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String[]getPicDescs(){
		List<ContentPicture>pics=getPictures();
		if(pics==null||pics.size()<=0){
			return null;
		}
		String[]picDescs=new String[pics.size()];
		for(int i=0;i<picDescs.length;i++){
			picDescs[i]=pics.get(i).getDescription();
		}
		return picDescs;
	}
	
	public String getPicDescStr() {
		List<ContentPicture>pics=getPictures();
		if(pics==null||pics.size()<=0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<pics.size();i++){
			String desc=pics.get(i).getDescription();
			if(StringUtils.isNotBlank(desc)){
				sb.append(desc).append(',');
			}else{
				sb.append("").append(',');
			}
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public String[]getTagArray(){
		List<ContentTag>tags=getTags();
		if(tags==null||tags.size()<=0){
			return null;
		}
		String[]tagArrar=new String[tags.size()];
		for(int i=0;i<tagArrar.length;i++){
			tagArrar[i]=tags.get(i).getName();
		}
		return tagArrar;
	}
	
	public ContentCharge getContentCharge() {
		Set<ContentCharge> set = getContentChargeSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}
	
	public boolean getCharge() {
		ContentCharge c=getContentCharge();
		return c!=null&&c.getChargeAmount()>0&&c.getChargeReward().equals(ContentCharge.MODEL_CHARGE);
	}
	
	public Short getChargeModel() {
		ContentCharge c=getContentCharge();
		if(c==null){
			return ContentCharge.MODEL_FREE;
		}else{
			return c.getChargeReward();
		}
	}
	
	public Double getChargeAmount() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getChargeAmount();
		}else{
			return 0d;
		}
	}
	
	public Boolean getRewardPattern() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getRewardPattern();
		}else{
			return false;
		}
	}
	
	public Double getRewardRandomMax() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getRewardRandomMax();
		}else{
			return 0d;
		}
	}
	
	public Double getRewardRandomMin() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getRewardRandomMin();
		}else{
			return 0d;
		}
	}
	
	public Double[] getRewardFixValues() {
		Double[] fixs=null;
		List<ContentRewardFix>list= getRewardFixs();
		if(list!=null&&list.size()>0){
			fixs=new Double[list.size()];
			for(int i=0;i<list.size();i++){
				fixs[i]=list.get(i).getFixVal();
			}
		}
		return fixs;
	}
	
	public Double getDayAmount() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getDayAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getMonthAmount() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getMonthAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getYearAmount() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getYearAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getTotalAmount() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getTotalAmount();
		}else{
			return 0d;
		}
	}
	
	public Date getLastBuyTime() {
		ContentCharge charge= getContentCharge();
		if(charge!=null){
			return charge.getLastBuyTime();
		}else{
			return null;
		}
	}
	
	public Double getScoreAvg() {
		Integer scoreTotal=0;
		if(getScoreRecordSet()!=null){
			for(CmsScoreRecord r:getScoreRecordSet()){
				scoreTotal+=r.getCount();
			}
		}
		if(scoreTotal==0){
			return 0.0;
		}else{
			return getScore()*1.0/scoreTotal;
		}
	}

	public boolean isTitBold() {
		return getBold();
	}

	public Date getDate() {
		return getReleaseDate();
	}

	public Boolean getTarget() {
		return null;
	}
	
	public boolean getNew(){
		Date releaseDate=getReleaseDate();
		Date today=Calendar.getInstance().getTime();
		int between=DateUtils.getDaysBetweenDate(releaseDate, today);
		Integer dayNew=getSite().getConfig().getConfigAttr().getDayNew();
		if(dayNew==0){
			return false;
		}else{
			return dayNew-between>0?true:false;
		}
	}
	
	public Content cloneWithoutSet() {  
        Content content = new Content();  
        setSortDate(getSortDate());
        content.setTopLevel(getTopLevel());
        content.setHasTitleImg(getHasTitleImg());
        content.setRecommend(getRecommend());
        content.setStatus(getStatus());
        content.setViewsDay(getViewDay());
        content.setCommentsDay(getCommentsDay());
        content.setDownloadsDay(getDownloadsDay());
        content.setUpsDay(getUpsDay());
        content.setType(getType());
        content.setSite(getSite());
        content.setUser(getUser());
        content.setChannel(getChannel());
        content.setModel(getModel());
        Map<String,String>attrs=getAttr();
        if(attrs!=null&&!attrs.isEmpty()){
        	Map<String,String>newAttrs=new HashMap<String, String>();
        	String key;
            Set<String>keyset=attrs.keySet();
            Iterator<String>keyIt=keyset.iterator();
            while(keyIt.hasNext()){
            	key=keyIt.next();
            	newAttrs.put(key, attrs.get(key));
            }
            content.setAttr(newAttrs);
        }
        content.setContentExt(getContentExt());
        return content;  
    }  
	
	public void clear(){
		getCollectUsers().clear();
	}
	
	public String getCodeImg(){
		return getSite().getUploadPath()+Constants.CODE_IMG_PATH+getId()+".png";
	}
	
	public String getCodeImgUrl(){
		CmsSite site= getSite();
		String codeImg=site.getUploadPath()+Constants.CODE_IMG_PATH+getId()+".png";
		if(site.getUploadFtp()!=null){
			Ftp ftp = site.getUploadFtp();
			return ftp.getUrl()+codeImg;
		}else{
			if(StringUtils.isNotBlank(site.getContextPath())){
				codeImg=site.getContextPath()+codeImg;
			}
			return codeImg;
		}
	}
	
	public JSONObject convertToJson(Integer format,Integer https,
			boolean hasCollect,boolean isList, boolean txtImgWhole) 
			throws JSONException{
		JSONObject json=new JSONObject();
		CmsSite site=getSite();
		Ftp uploadFtp=site.getUploadFtp();
		boolean uploadToFtp=false;
		if(uploadFtp!=null){
			uploadToFtp=true;
		}
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		String urlPrefix="";
		if(getChannel()!=null){
			if(https==com.jeecms.cms.api.Constants.URL_HTTP){
				if (StringUtils.isNotBlank(getUrl())) {
					json.put("url", getUrl());
				}else{
					json.put("url", "");
				}
				urlPrefix=site.getUrlPrefixWithNoDefaultPort();
			}else{
				if (StringUtils.isNotBlank(getHttpsUrl())) {
					json.put("url", getHttpsUrl());
				}else{
					json.put("url", "");
				}
				urlPrefix=site.getSafeUrlPrefix();
			}
		}else{
			json.put("url", "");
		}
		if (getRecommend()!=null) {
			json.put("recommend", getRecommend());
		}else{
			json.put("recommend", "");
		}
		json.put("status", getStatus());
		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getAuthor())) {
			json.put("author", getAuthor());
		}else{
			json.put("author", "");
		}
		if (getReleaseDate()!=null) {
			json.put("releaseDate", DateUtils.parseDateToTimeStr(getReleaseDate()));
		}else{
			json.put("releaseDate", "");
		}
		if (getChannel()!=null&&StringUtils.isNotBlank(getChannel().getName())) {
			json.put("channelName", getChannel().getName());
		}else{
			json.put("channelName", "");
		}
		if (getChannel()!=null&&getChannel().getId()!=null) {
			json.put("channelId", getChannel().getId());
		}else{
			json.put("channelId", "");
		}
		if (getTopLevel()!=null) {
			json.put("topLevel", getTopLevel());
		}else{
			json.put("topLevel", "");
		}
		if (getPigeonholeDate()!=null) {
			json.put("pigeonholeDate", DateUtils.parseDateToDateStr(getPigeonholeDate()));
		}else{
			json.put("pigeonholeDate", "");
		}
		json.put("draft", isDraft());
		if(StringUtils.isNotBlank(getMediaPath())){
			json.put("mediaPath", getMediaPath());
		}else{
			json.put("mediaPath", "");
		}
		if(StringUtils.isNotBlank(getTitleImg())){
			json.put("titleImg", getTitleImg());
		}else{
			json.put("titleImg", "");
		}
		if(StringUtils.isNotBlank(getContentImg())){
			json.put("contentImg", getContentImg());
		}else{
			json.put("contentImg", "");
		}
		if(StringUtils.isNotBlank(getTypeImg())){
			json.put("typeImg", getTypeImg());
		}else{
			json.put("typeImg", "");
		}
		JSONArray attachJsonArray = new JSONArray();
		if (getAttachments()!=null && getAttachments().size()>0) {
			List<ContentAttachment> list = getAttachments();
			for (int i = 0; i < list.size(); i++) {
				JSONObject attJson=new JSONObject();
				attJson.put("attachmentNames", list.get(i).getName());
				attJson.put("attachmentPaths", list.get(i).getPath());
				attachJsonArray.put(i,attJson);
			}
		}
		json.put("attachArr", attachJsonArray);
		JSONArray picArr = new JSONArray();
		if (getPictures()!=null && getPictures().size()>0) {
			List<ContentPicture> list = getPictures();
			for (int i = 0; i < list.size(); i++) {
				JSONObject picJson = new JSONObject();
				if (StringUtils.isNotBlank(list.get(i).getImgPath())) {
					picJson.put("picPaths", list.get(i).getImgPath());
				}else{
					picJson.put("picPaths", "");
				}
				if (StringUtils.isNotBlank(list.get(i).getDescription())) {
					picJson.put("picDescs", list.get(i).getDescription());
				}else{
					picJson.put("picDescs", "");
				}
				picArr.put(i,picJson);
			}
		}
		json.put("picArr", picArr);
		if (getViews()!=null) {
			json.put("views", getViews());
		}else{
			json.put("views", "");
		}
		if (getContentCount()!=null && getCommentsCount()!=null) {
			json.put("comments", getCommentsCount());
		}else{
			json.put("comments", "");
		}
		if (getContentCount()!=null && getCommentsDay()!=null) {
			json.put("commentsDay", getCommentsDay());
		}else{
			json.put("commentsDay", "");
		}
		if (getContentCount()!=null&&getContentCount().getCommentsMonth()!=null) {
			json.put("commentsMonth", getContentCount().getCommentsMonth());
		}else{
			json.put("commentsMonth", "");
		}
		if (getContentCount()!=null&&getContentCount().getCommentsWeek()!=null) {
			json.put("commentsWeek", getContentCount().getCommentsWeek());
		}else{
			json.put("commentsWeek", "");
		}
		if (getContentCount()!=null&&getContentCount().getDownloads()!=null) {
			json.put("downloads", getContentCount().getDownloads());
		}else{
			json.put("downloads", "");
		}
		if (getDownloadsDay()!=null) {
			json.put("downloadsDay", getDownloadsDay());
		}else{
			json.put("downloadsDay", "");
		}
		if (getContentCount()!=null&&getContentCount().getDownloadsMonth()!=null) {
			json.put("downloadsMonth", getContentCount().getDownloadsMonth());
		}else{
			json.put("downloadsMonth", "");
		}
		if (getContentCount()!=null&&getContentCount().getDownloadsWeek()!=null) {
			json.put("downloadsWeek", getContentCount().getDownloadsWeek());
		}else{
			json.put("downloadsWeek", "");
		}
		if ( getContentCount()!=null&&getContentCount().getDowns()!=null) {
			json.put("downs", getContentCount().getDowns());
		}else{
			json.put("downs", "");
		}
		if (getContentCount()!=null&&getContentCount().getUps()!=null) {
			json.put("ups", getContentCount().getUps());
		}else{
			json.put("ups", "");
		}
		if (getUpsDay()!=null) {
			json.put("upsDay", getUpsDay());
		}else{
			json.put("upsDay", "");
		}
		if (getContentCount()!=null&&getContentCount().getUpsMonth()!=null) {
			json.put("upsMonth", getContentCount().getUpsMonth());
		}else{
			json.put("upsMonth", "");
		}
		if (getContentCount()!=null&&getContentCount().getUpsWeek()!=null) {
			json.put("upsWeek", getContentCount().getUpsWeek());
		}else{
			json.put("upsWeek", "");
		}
		if (getContentCount()!=null&&getViewsMonth()!=null) {
			json.put("viewsMonth", getViewsMonth());
		}else{
			json.put("viewsMonth", "");
		}
		if (getContentCount()!=null&&getViewsWeek()!=null) {
			json.put("viewsWeek", getViewsWeek());
		}else{
			json.put("viewsWeek", "");
		}
		if (getContentCount()!=null&&getViewDay()!=null) {
			json.put("viewsDay", getViewsDay());
		}else{
			json.put("viewsDay", "");
		}
		if (getContentCharge()!=null&&getChargeAmount()!=null) {
			json.put("chargeAmount", getChargeAmount());
		}else{
			json.put("chargeAmount", "");
		}
		if(getContentCharge()!=null){
			json.put("charge", getContentCharge().getChargeReward());
		}else{
			json.put("charge", 0);
		}
		json.put("hasCollect", hasCollect);
		if (!isList) {//列表不显示内容
			if(StringUtils.isNotBlank(getTxt())){
				String txt=getTxt();
				if(txtImgWhole){
					txt=replaceTxt(txt, uploadToFtp, urlPrefix);
				}
				json.put("txt", txt);
			}else{
				json.put("txt", "");
			}
			if(format.equals(CONTENT_INFO_WHOLE)){
				if (StringUtils.isNotBlank(getTxt1())) {
					if(txtImgWhole){
						json.put("txt1", replaceTxt(getTxt1(), uploadToFtp, urlPrefix));
					}else{
						json.put("txt1", getTxt1());
					}
				}else{
					json.put("txt1", "");
				}
				if (StringUtils.isNotBlank(getTxt2())) {
					if(txtImgWhole){
						json.put("txt2", replaceTxt(getTxt2(), uploadToFtp, urlPrefix));
					}else{
						json.put("txt2", getTxt2());
					}
				}else{
					json.put("txt2", "");
				}
				if (StringUtils.isNotBlank(getTxt3())) {
					if(txtImgWhole){
						json.put("txt3", replaceTxt(getTxt2(), uploadToFtp, urlPrefix));
					}else{
						json.put("txt3", getTxt3());
					}
				}else{
					json.put("txt3", "");
				}
				if (StringUtils.isNotBlank(getTagStr())) {
					json.put("tagStr", getTagStr());
				}else{
					json.put("tagStr", "");
				}
				if (getSortDate()!=null) {
					json.put("sortDate", DateUtils.parseDateToTimeStr(getSortDate()));
				}else{
					json.put("sortDate", "");
				}
				if (getHasTitleImg()!=null) {
					json.put("hasTitleImg", getHasTitleImg());
				}else{
					json.put("hasTitleImg", "");
				}
				if (getScore()!=null) {
					json.put("score", getScore());
				}else{
					json.put("score", "");
				}
				if (getRecommendLevel()!=null) {
					json.put("recommendLevel", getRecommendLevel());
				}else{
					json.put("recommendLevel", "");
				}
				if (StringUtils.isNotBlank(getShortTitle())) {
					json.put("shortTitle", getShortTitle());
				}else{
					json.put("shortTitle", "");
				}
				if (StringUtils.isNotBlank(getOrigin())) {
					json.put("origin", getOrigin());
				}else{
					json.put("origin", "");
				}
				if (StringUtils.isNotBlank(getOriginUrl())) {
					json.put("originUrl", getOriginUrl());
				}else{
					json.put("originUrl", "");
				}
				if (StringUtils.isNotBlank(getMediaType())) {
					json.put("mediaType", getMediaType());
				}else{
					json.put("mediaType", "");
				}
				if (StringUtils.isNotBlank(getTitleColor())) {
					json.put("titleColor", getTitleColor());
				}else{
					json.put("titleColor", "");
				}
				if (getBold()!=null) {
					json.put("bold", getBold());
				}else{
					json.put("bold", "");
				}
				if (StringUtils.isNotBlank(getLink())) {
					json.put("link", getLink());
				}else{
					json.put("link", "");
				}
				CmsModel model=getModel();
				Map<String,String>attr=getAttr();
				for(String key:attr.keySet()){
					CmsModelItem item=model.findModelItem(key, false);
					//多选需要传递数组方便前端处理
					if (item!=null) {
						if(item.getDataType()!=null&&item.getDataType().equals(CmsModelItem.DATA_TYPE_CHECKBOX)){
							String[]attrValArray=null;
							JSONArray jsonArray=new JSONArray();
							if(StringUtils.isNotBlank(attr.get(key))){
								attrValArray=attr.get(key).split(",");
								if(attrValArray!=null){
									for(int i=0;i<attrValArray.length;i++){
										jsonArray.put(i,attrValArray[i]);
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
			JSONArray topicArray = new JSONArray();
			if (getTopics()!=null && getTopics().size()>0) {
				Set<CmsTopic> set = getTopics();
				int index = 0 ;
				for (CmsTopic topic : set) {
					topicArray.put(index,topic.getId());
					index++;
				}
			}
			json.put("topicIds", topicArray);
			JSONArray channelArray = new JSONArray();
			if (getChannels()!=null&& getChannels().size()>0) {
				Set<Channel> set = getChannels();
				int index = 0 ;
				for (Channel channel : set) {
					channelArray.put(index,channel.getId());
					index++;
				}
			}
			json.put("channelIds", channelArray);
			JSONArray groupArray = new JSONArray();
			if (getViewGroups()!=null && getViewGroups().size()>0) {
				Set<CmsGroup> set = getViewGroups();
				int index = 0;
				for (CmsGroup group : set) {
					groupArray.put(index,group.getId());
					index++;
				}
			}
			json.put("viewGroupIds", groupArray);
			if (getRewardPattern()!=null) {
				json.put("rewardPattern", getRewardPattern());
			}else{
				json.put("rewardPattern", "");
			}
			JSONArray rewardFix = new JSONArray();
			if (getRewardFixs()!=null) {
				List<ContentRewardFix> list = getRewardFixs();
				for (int i = 0; i < list.size(); i++) {
					rewardFix.put(i,list.get(i).getFixVal());
				}
			}
			json.put("rewardFix", rewardFix);
			if (getRewardRandomMax()!=null) {
				json.put("rewardRandomMax", getRewardRandomMax());
			}else{
				json.put("rewardRandomMax", "");
			}
			if (getRewardRandomMin()!=null) {
				json.put("rewardRandomMin", getRewardRandomMin());
			}else{
				json.put("rewardRandomMin", "");
			}
		}
		if (getType()!=null&&getType().getId()!=null) {
			json.put("typeId", getType().getId());
		}else{
			json.put("typeId", "");
		}
		if (getType()!=null&&StringUtils.isNotBlank(getType().getName())) {
			json.put("typeName", getType().getName());
		}else{
			json.put("typeName", "");
		}
		if (StringUtils.isNotBlank(getSiteName())) {
			json.put("siteName", getSiteName());
		}else{
			json.put("siteName", "");
		}
		if (getSiteId()!=null) {
			json.put("siteId", getSiteId());
		}else{
			json.put("siteId", "");
		}
		if (StringUtils.isNotBlank(getSiteUrl())) {
			json.put("siteUrl", getSiteUrl());
		}else{
			json.put("siteUrl", "");
		}
		if (getChannel()!=null) {
			if (StringUtils.isNotBlank(getCtgUrl())) {
				json.put("channelUrl",getCtgUrl());
			}else{
				json.put("channelUrl","");
			}
		}else{
			json.put("channelUrl","");
		}
		if (getModel()!=null&&StringUtils.isNotBlank(getModel().getName())) {
			json.put("modelName", getModel().getName());
		}else{
			json.put("modelName", "");
		}
		if (getModel()!=null&&getModel().getId()!=null) {
			json.put("modelId", getModel().getId());
		}else{
			json.put("modelId", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getUsername())) {
			json.put("userName", getUser().getUsername());
		}else{
			json.put("userName", "");
		}
		if (getUser()!=null&&getUser().getId()!=null) {
			json.put("userId", getUser().getId());
		}else{
			json.put("userId", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getRealname())) {
			json.put("realname", getUser().getRealname());
		}else{
			json.put("realname", "");
		}
		if (StringUtils.isNotBlank(getCheckOpinion())) {
			json.put("checkOpinion", getCheckOpinion());
		}else{
			json.put("checkOpinion", "");
		}
		if (getRejected()!=null) {
			json.put("rejected", getRejected());
		}else{
			json.put("rejected", "");
		}
		if (StringUtils.isNotBlank(getStitle())) {
			json.put("stitle", getStitle());
		}else{
			json.put("stitle", "");
		}
		if (getTopLevelDate()!=null) {
			json.put("topLevelDate", DateUtils.parseDateToDateStr(getTopLevelDate()));
		}else{
			json.put("topLevelDate", "");
		}
		String tplPath = site.getTplPath();
		if (StringUtils.isNotBlank(getTplContent())) {
			json.put("tplContent", getTplContent().substring(tplPath.length()));
		}else{
			json.put("tplContent", "");
		}
		if (StringUtils.isNotBlank(getMobileTplContent())) {
			json.put("tplMobileContent", getMobileTplContent().substring(tplPath.length()));
		}else{
			json.put("tplMobileContent", "");
		}
		return json;
	}
	
	private String replaceTxt(String txt,boolean uploadToFtp,String urlPrefix){
		if(StringUtils.isNotBlank(txt)){
			//替换图片地址
			List<String>imgUrls=ImageUtils.getImageSrc(txt);
			for(String img:imgUrls){
				String imgRealUrl=img;
				if(!uploadToFtp){
					if(!img.startsWith("http://")&&!img.startsWith("https://")){
						imgRealUrl= urlPrefix+img;
					}
				}
				txt=txt.replace(img, imgRealUrl);
			}
			//替换视频地址
			List<String>videoUrls=StrUtils.getVideoSrc(txt);
			for(String videoUrl:videoUrls){
				String videoRealUrl=videoUrl;
				if(!uploadToFtp){
					if(!videoUrl.startsWith("http://")&&!videoUrl.startsWith("https://")){
						videoRealUrl= urlPrefix+videoUrl;
					}
				}
				txt=txt.replace(videoUrl, videoRealUrl);
			}
			//过滤html标记和转换图片视频地址为占位符+地址+占位符,小程序已经调整，API可调整
			//txt=StrUtils.trimHtml2Txt(txt);
		}
		return txt;
	}
	
	

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Content() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Content(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Content(java.lang.Integer id,
			com.jeecms.core.entity.CmsSite site, java.util.Date sortDate,
			java.lang.Byte topLevel, java.lang.Boolean hasTitleImg,
			java.lang.Boolean recommend, java.lang.Byte status,
			java.lang.Integer viewsDay, java.lang.Short commentsDay,
			java.lang.Short downloadsDay, java.lang.Short upsDay) {

		super(id, site, sortDate, topLevel, hasTitleImg, recommend, status,
				viewsDay, commentsDay, downloadsDay, upsDay);
	}

	/* [CONSTRUCTOR MARKER END] */

}