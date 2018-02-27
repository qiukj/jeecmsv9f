package com.jeecms.cms.staticpage;

import static com.jeecms.common.web.Constants.UTF8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.manager.assist.CmsKeywordMng;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateSimpleDao;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.SimplePage;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.Constants;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.FtpMng;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.core.web.util.URLHelper;
import com.jeecms.core.web.util.URLHelper.PageInfo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Repository
public class StaticPageDaoImpl extends HibernateSimpleDao implements
		StaticPageDao {
	public int channelStatic(HttpServletRequest request, HttpServletResponse response,
			Integer siteId, Integer channelId, boolean containChild,
			Configuration conf, Map<String, Object> data)
			throws IOException, TemplateException {
		Finder finder = Finder.create("select bean from Channel bean");
		if (channelId != null) {
			if (containChild) {
				finder.append(",Channel parent where").append(
						" bean.lft between parent.lft and parent.rgt").append(
						" and parent.site.id=bean.site.id").append(
						" and parent.id=:channelId");
				finder.setParam("channelId", channelId);
			} else {
				finder.append(" where bean.id=:channelId");
				finder.setParam("channelId", channelId);
			}
		} else if (siteId != null) {
			finder.append(" where bean.site.id=:siteId");
			finder.setParam("siteId", siteId);
		}
		Session session = getSession();
		int totalCount=countQueryResult(finder);
		if(request!=null&&response!=null){
			sessionProvider.setAttribute(request, response, 
					com.jeecms.cms.api.Constants.STATIC_TOTAL_COUNT_KEY, totalCount);
		}
		ScrollableResults channels = finder.createQuery(session).setCacheMode(
				CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		int count = 0;
		CmsSite site;
		Channel c;
		String filename;
		int quantity, totalPage;
		boolean mobileStaticSync=false;
		ExecutorService es= null;
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		while (channels.next()) {
			if (++count % 20 == 0) {
				session.clear();
			}
			c = (Channel) channels.get(0);
			site = c.getSite();
			mobileStaticSync=site.getMobileStaticSync();
			FrontUtils.frontData(data, site, null, null, null);
			// 如果是外部链接或者不需要生产静态页，则不生成
			if (!StringUtils.isBlank(c.getLink()) || !c.getStaticChannel()) {
				continue;
			}
			// 没有内容或者有子栏目，则只生成一页
			int childs = childsOfChannel(c.getId());
			if (!c.getModel().getHasContent()) {
				totalPage = 1;
			} else {
				if (c.getListChild()) {
					quantity = childs;
				} else {
					if(!c.getListChild() && childs > 0){
						quantity=contentsOfParentChannel(c.getId());
					}else{
						quantity = contentsOfChannel(c.getId());
					}
				}
				if (quantity <= 0) {
					totalPage = 1;
				} else {
					totalPage = (quantity - 1) / c.getPageSize() + 1;
				}
			}
			//初始化线程池
			if(site.getPageSync()&&es==null){
				es=Executors.newFixedThreadPool(Constants.DISTRIBUTE_THREAD_COUNT);
			}
			totalPage=totalPage>10?com.jeecms.cms.api.Constants.MAX_STATIC_COUNT:totalPage;
			for (int i = 1; i <= totalPage; i++) {
				filename = c.getStaticFilename(i);
				createChannelPage(es,site,conf, data, c, filename, i,false);
				//手机静态页页面
				if(mobileStaticSync){
					filename = c.getMobileStaticFilename(i);
					createChannelPage(es,site,conf, data, c, filename, i,true);
				}
			}
			if(request!=null&&response!=null){
				sessionProvider.setAttribute(request, response, 
						com.jeecms.cms.api.Constants.STATIC_CURR_COUNT_KEY, count);
			}
		}
		if(es!=null){
			es.shutdown();
		}
		return count;
	}
	
	

	public void channelStatic(Channel c, boolean firstOnly, Configuration conf,
			Map<String, Object> data) throws IOException, TemplateException {
		// 如果是外部链接或者不需要生产静态页，则不生成
		if (!StringUtils.isBlank(c.getLink()) || !c.getStaticChannel()) {
			return;
		}
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		// 没有内容或者有子栏目，则只生成一页
		int childs = childsOfChannel(c.getId());
		int quantity, totalPage;
		if (firstOnly || !c.getModel().getHasContent()
				|| (!c.getListChild() && childs > 0)) {
			totalPage = 1;
		} else {
			if (c.getListChild()) {
				quantity = childs;
			} else {
				quantity = contentsOfChannel(c.getId());
			}
			if (quantity <= 0) {
				totalPage = 1;
			} else {
				totalPage = (quantity - 1) / c.getPageSize() + 1;
			}
		}
		String filename;
		CmsSite site = c.getSite();
		boolean mobileStaticSync=site.getMobileStaticSync();
		FrontUtils.frontData(data, site, null, null, null);
		ExecutorService es=null;
		if(site.getPageSync()){
			es=Executors.newFixedThreadPool(Constants.DISTRIBUTE_THREAD_COUNT);
		}
		totalPage=totalPage>10?com.jeecms.cms.api.Constants.MAX_STATIC_COUNT:totalPage;
		for (int i = 1; i <= totalPage; i++) {
			filename = c.getStaticFilename(i);
			createChannelPage(es,site,conf, data, c, filename, i,false);
			//手机静态页页面
			if(mobileStaticSync){
				filename = c.getMobileStaticFilename(i);
				createChannelPage(es,site,conf, data, c, filename, i,true);
			}
		}
		if(es!=null){
			es.shutdown();
		}
	}

	public int contentsOfChannel(Integer channelId) {
		String hql = "select count(*) from Content bean"
				+ " join bean.channels as channel"
				+ " where channel.id=:channelId and bean.status="
				+ ContentCheck.CHECKED;
		Query query = getSession().createQuery(hql);
		query.setParameter("channelId", channelId);
		return ((Number) query.iterate().next()).intValue();
	}
	
	public int contentsOfParentChannel(Integer channelId) {
		String hql = "select count(*) from Content bean"
				+ " join bean.channel channel,Channel parent"
				+ "  where channel.lft between parent.lft and parent.rgt and channel.site.id=parent.site.id and parent.id=:parentId and bean.status="
				+ ContentCheck.CHECKED;
		Query query = getSession().createQuery(hql);
		query.setParameter("parentId", channelId);
		return ((Number) query.iterate().next()).intValue();
	}

	public int childsOfChannel(Integer channelId) {
		String hql = "select count(*) from Channel bean"
				+ " where bean.parent.id=:channelId";
		Query query = getSession().createQuery(hql);
		query.setParameter("channelId", channelId);
		return ((Number) query.iterate().next()).intValue();
	}

	public int contentStatic(
			HttpServletRequest request, HttpServletResponse response, Integer siteId,
			Integer channelId, Date start, Configuration conf, Map<String, Object> data) throws IOException,
			TemplateException {
		Finder f = Finder.create("select bean from Content bean");
		if (channelId != null) {
			f.append(" join bean.channel node,Channel parent");
			f.append(" where node.lft between parent.lft and parent.rgt");
			f.append(" and parent.id=:channelId");
			f.append(" and node.site.id=parent.site.id");
			f.setParam("channelId", channelId);
		} else if (siteId != null) {
			f.append(" where bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		} else {
			f.append(" where 1=1");
		}
		if (start != null) {
			f.append(" and bean.sortDate>=:start");
			f.setParam("start", start);
		}
		f.append(" and bean.status=" + ContentCheck.CHECKED);
		f.setCacheable(true);
		Session session = getSession();
		int totalCount=countQueryResult(f);
		if(request!=null&&response!=null){
			sessionProvider.setAttribute(request, response, 
					com.jeecms.cms.api.Constants.STATIC_TOTAL_COUNT_KEY, totalCount);	
		}
		ScrollableResults contents = f.createQuery(session).setCacheMode(
				CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		int count = 0;
		int totalPage;
		Content c;
		Channel chnl;
		CmsSite site;
		Template tpl,mobileTpl;
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		ExecutorService es=null;
		while (contents.next()) {
			c = (Content) contents.get(0);
			chnl = c.getChannel();
			// 如果是外部链接或者不生成静态页面，则不生成
			if (!StringUtils.isBlank(c.getLink()) || !chnl.getStaticContent()) {
				continue;
			}
			// 如果不需要生成静态页面，则不生成
			/*
			if(!c.getNeedRegenerate()){
				continue;
			}
			*/
			site = c.getSite();
			//初始化线程池
			if(site.getPageSync()&&es==null){
				es=Executors.newFixedThreadPool(Constants.DISTRIBUTE_THREAD_COUNT);
			}
			CmsModel model=modelMng.findById(c.getModel().getId());
			tpl = conf.getTemplate(c.getTplContentOrDef(model));
			mobileTpl=conf.getTemplate(c.getMobileTplContentOrDef(model));
			FrontUtils.frontData(data, site, null, null, null);
			data.put("content", c);
			data.put("channel", c.getChannel());
			totalPage = c.getPageCount();
			totalPage=totalPage>10?com.jeecms.cms.api.Constants.MAX_STATIC_COUNT:totalPage;
			for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
				createContentPage(es,data, tpl,false, c, pageNo);
				//手机静态页页面
				if(site.getMobileStaticSync()){
					createContentPage(es,data, mobileTpl,true, c, pageNo);
				}
			}
			c.setNeedRegenerate(false);
			if (++count % 20 == 0) {
				session.flush();
				session.clear();
			}
			if(request!=null&&response!=null){
				sessionProvider.setAttribute(request, response, 
						com.jeecms.cms.api.Constants.STATIC_CURR_COUNT_KEY, count);
			}
		}
		if(es!=null){
			es.shutdown();
		}
		return count;
	}
	
	public boolean contentStatic(Content c, Configuration conf,
			Map<String, Object> data) throws IOException, TemplateException {
		// 如果是外部链接或者不生成静态页面，则不生成
		Channel chnl = c.getChannel();
		if (!StringUtils.isBlank(c.getLink()) || !chnl.getStaticContent()) {
			return false;
		}
		// 如果不需要生成静态页面，则不生成
		/* 是否需要生成静态页这里判断过于简单话，模板变换 站点名称等参数变换均需重新生成
		if(!c.getNeedRegenerate()){
			return false;
		}
		*/
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		int totalPage;
		CmsSite site;
		Template tpl,mobileTpl;
		site = c.getSite();
		CmsModel model=modelMng.findById(c.getModel().getId());
		tpl = conf.getTemplate(c.getTplContentOrDef(model));
		mobileTpl=conf.getTemplate(c.getMobileTplContentOrDef(model));
		FrontUtils.frontData(data, site, null, null, null);
		data.put("content", c);
		data.put("channel", chnl);
		totalPage = c.getPageCount();
		ExecutorService es= null;
		if(site.getPageSync()){
			es=Executors.newFixedThreadPool(Constants.DISTRIBUTE_THREAD_COUNT);
		}
		totalPage=totalPage>10?com.jeecms.cms.api.Constants.MAX_STATIC_COUNT:totalPage;
		for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
			createContentPage(es,data, tpl,false, c, pageNo);
			//手机静态页页面
			if(site.getMobileStaticSync()){
				createContentPage(es,data, mobileTpl,true, c, pageNo);
			}
		}
		if(es!=null){
			es.shutdown();
		}
		c.setNeedRegenerate(false);
		return true;
	}
	
	private void createContentPage(ExecutorService es,Map<String, Object> data,Template tpl,boolean mobile,Content c,Integer pageNo) throws TemplateException, IOException{
		String url, real;
		File file, parent;
		CmsSite site;
		PageInfo info;
		Writer out = null;
		site = c.getSite();
		Ftp syncPageFtp=null;
		syncPageFtp=site.getSyncPageFtp();
		if(syncPageFtp!=null){
			syncPageFtp=ftpMng.findById(syncPageFtp.getId());
		}
		String txt = c.getTxtByNo(pageNo);
		// 内容加上关键字
		txt = cmsKeywordMng.attachKeyword(site.getId(), txt);
		Paginable pagination = new SimplePage(pageNo, 1, c.getPageCount());
		data.put("pagination", pagination);
		url = c.getUrlStatic(pageNo);
		info = URLHelper.getPageInfo(url.substring(url.lastIndexOf("/")),
				null);
		FrontUtils.putLocation(data, url);
		FrontUtils.frontPageData(pageNo, info.getHref(), info
				.getHrefFormer(), info.getHrefLatter(), data);
		data.put("title", c.getTitleByNo(pageNo));
		data.put("txt", txt);
		data.put("pic", c.getPictureByNo(pageNo));
		if(mobile){
			real = realPathResolver.get(c.getMobileStaticFilename(pageNo));
		}else{
			real = realPathResolver.get(c.getStaticFilename(pageNo));
		}
		file = new File(real);
		if (pageNo == 1) {
			parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
		}
		try {
			// FileWriter不能指定编码确实是个问题，只能用这个代替了。
			out = new OutputStreamWriter(new FileOutputStream(file), UTF8);
			tpl.process(data, out);
			log.info("create static file: {}", file.getAbsolutePath());
		} finally {
			if (out != null) {
				out.close();
			}
		}
		String filename;
		if(mobile){
			filename=c.getMobileStaticFilename(pageNo);
		}else{
			filename=c.getStaticFilename(pageNo);
		}
		if(es!=null&&syncPageFtp!=null){
			if(es.isTerminated()){
				es= Executors.newFixedThreadPool(Constants.DISTRIBUTE_THREAD_COUNT);
			}
			es.execute(new DistributionThread(syncPageFtp,filename, new FileInputStream(file)));
		}
	}
	
	private void createChannelPage(ExecutorService es,CmsSite site,Configuration conf, Map<String, Object> data,
			Channel c,String filename,Integer page,boolean mobile) throws IOException, TemplateException{
		PageInfo info;
		Writer out = null;
		Template tpl;
		String real;
		File f, parent;
		real = realPathResolver.get(filename.toString());
		f = new File(real);
		parent = f.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		if(mobile){
			tpl = conf.getTemplate(c.getMobileTplChannelOrDef());
		}else{
			tpl = conf.getTemplate(c.getTplChannelOrDef());
		}
		String urlStatic = c.getUrlStatic(page);
		info = URLHelper.getPageInfo(filename.substring(filename
				.lastIndexOf("/")), null);
		FrontUtils.frontPageData(page, info.getHref(), info
				.getHrefFormer(), info.getHrefLatter(), data);
		FrontUtils.putLocation(data, urlStatic);
		data.put("channel", c);
		Ftp syncPageFtp=null;
		syncPageFtp=site.getSyncPageFtp();
		if(syncPageFtp!=null){
			syncPageFtp=ftpMng.findById(syncPageFtp.getId());
		}
		try {
			// FileWriter不能指定编码确实是个问题，只能用这个代替了。
			out = new OutputStreamWriter(new FileOutputStream(f), UTF8);
			tpl.process(data, out);
			log.info("create static file: {}", f.getAbsolutePath());
		} finally {
			if (out != null) {
				out.close();
			}
		}
		if(es!=null&&syncPageFtp!=null){
			if(es.isTerminated()){
				es= Executors.newFixedThreadPool(Constants.DISTRIBUTE_THREAD_COUNT);
			}
			es.execute(new DistributionThread(syncPageFtp,filename, new FileInputStream(f)));
		}
	}

	private CmsKeywordMng cmsKeywordMng;
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsModelMng modelMng;
	@Autowired
	private FtpMng ftpMng;
	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	public void setCmsKeywordMng(CmsKeywordMng cmsKeywordMng) {
		this.cmsKeywordMng = cmsKeywordMng;
	}

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
	}
}
