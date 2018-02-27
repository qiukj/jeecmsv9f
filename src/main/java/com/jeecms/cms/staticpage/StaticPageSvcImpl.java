package com.jeecms.cms.staticpage;

import static com.jeecms.common.web.Constants.UTF8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.web.Constants;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.FtpMng;
import com.jeecms.core.web.util.FrontUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
@Transactional
public class StaticPageSvcImpl  implements StaticPageSvc, InitializingBean {
	private Logger log = LoggerFactory.getLogger(StaticPageSvcImpl.class);

	public int content(HttpServletRequest request, HttpServletResponse response,
			Integer siteId, Integer channelId, Date start)
			throws IOException, TemplateException {
		long time = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		int count = staticPageDao.contentStatic(request, response, siteId, channelId,
				start, conf, data);
		time = System.currentTimeMillis() - time;
		log.info("create content page count {}, in {} ms", count, time);
		return count;
	}

	public boolean content(Content content) throws IOException, TemplateException {
		Map<String, Object> data = new HashMap<String, Object>();
		long time = System.currentTimeMillis();
		boolean generated = staticPageDao.contentStatic(content, conf, data);
		time = System.currentTimeMillis() - time;
		log.info("create content page in {} ms", time);
		return generated;
	}
	
	public void contentRelated(Integer contentId) throws IOException,
	TemplateException{
		Content content=contentMng.findById(contentId);
		contentRelated(content);
	}

	public void contentRelated(Content content) throws IOException,
			TemplateException {
		content(content);
		Channel channel = content.getChannel();
		while (channel != null) {
			channel(channel, true);
			channel = channel.getParent();
		}
		//新增副栏目生成
		Set<Channel> channels=content.getChannelsWithoutMain();
		for(Channel c:channels){
			channel(c, true);
		}
		if (content.getSite().getStaticIndex()) {
			index(content.getSite());
		}
	}

	@Transactional(readOnly = true)
	public void deleteContent(Content content) throws IOException, TemplateException {
		String real;
		File file;
		int totalPage = content.getPageCount();
		for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
			real = realPathResolver.get(content.getStaticFilename(pageNo));
			file = new File(real);
			file.delete();
			if(content.getSite().getMobileStaticSync()){
				real = realPathResolver.get(content.getMobileStaticFilename(pageNo));
				file = new File(real);
				file.delete();
			}
		}
		Channel channel = content.getChannel();
		while (channel != null) {
			channel(channel, true);
			channel = channel.getParent();
		}
		if (content.getSite().getStaticIndex()) {
			index(content.getSite());
		}
	}

	@Transactional(readOnly = true)
	public int channel(HttpServletRequest request, HttpServletResponse response, Integer siteId, Integer channelId, boolean containChild)
			throws IOException, TemplateException {
		long time = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		int count = staticPageDao.channelStatic(request, response,
				siteId, channelId, containChild, conf, data);
		time = System.currentTimeMillis() - time;
		log.info("create channel page count {}, in {} ms", count, time);
		return count;
	}

	@Transactional(readOnly = true)
	public void channel(Channel channel, boolean firstOnly) throws IOException,
			TemplateException {
		Map<String, Object> data = new HashMap<String, Object>();
		long time = System.currentTimeMillis();
		staticPageDao.channelStatic(channel, firstOnly, conf, data);
		time = System.currentTimeMillis() - time;
		log.info("create channel page in {} ms", time);
	}

	@Transactional(readOnly = true)
	public void deleteChannel(Channel channel) {
		// 如果是外部链接或者不需要生产静态页，则不删除
		if (!StringUtils.isBlank(channel.getLink())
				|| !channel.getStaticChannel()) {
			return;
		}
		// 没有内容或者有子栏目，则只删除一页
		int childs = staticPageDao.childsOfChannel(channel.getId());
		int quantity, totalPage;
		if (!channel.getModel().getHasContent()
				|| (!channel.getListChild() && childs > 0)) {
			totalPage = 1;
		} else {
			if (channel.getListChild()) {
				quantity = childs;
			} else {
				quantity = staticPageDao.contentsOfChannel(channel.getId());
			}
			if (quantity <= 0) {
				totalPage = 1;
			} else {
				totalPage = (quantity - 1) / channel.getPageSize() + 1;
			}
		}
		String real, filename;
		File f;
		for (int i = 1; i <= totalPage; i++) {
			filename = channel.getStaticFilename(i);
			real = realPathResolver.get(filename.toString());
			f = new File(real);
			f.delete();
			if(channel.getSite().getMobileStaticSync()){
				filename = channel.getMobileStaticFilename(i);
				real = realPathResolver.get(filename.toString());
				f = new File(real);
				f.delete();
			}
		}
	}
	
	public void index(Integer siteId) throws IOException, TemplateException{
		CmsSite site=siteMng.findById(siteId);
		index(site);
	}

	public void index(CmsSite site) throws IOException, TemplateException {
		Map<String, Object> data = new HashMap<String, Object>();
		FrontUtils.frontData(data, site, null, site.getUrlStatic(), null);
		/*
		String tpl = FrontUtils.getTplPath(tplMessageSource, site
				.getLocaleAdmin(), site.getSolutionPath(), TPLDIR_INDEX,
				TPL_INDEX);
		*/
		String tpl=site.getTplIndexOrDef();
		index(site, tpl, data,false);
		if(site.getMobileStaticSync()){
			String mobileTpl=site.getMobileTplIndexOrDef();
			index(site, mobileTpl, data,true);
		}
	}

	@Transactional(readOnly = true)
	public void index(CmsSite site, String tpl, Map<String, Object> data,boolean mobile)
			throws IOException, TemplateException {
		long time = System.currentTimeMillis();
		File f ;
		if(mobile){
			f = new File(getMobileIndexPath(site));
		}else{
		    f = new File(getIndexPath(site));
		}
		File parent = f.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		Writer out = null;
		try {
			// FileWriter不能指定编码确实是个问题，只能用这个代替了。
			out = new OutputStreamWriter(new FileOutputStream(f), UTF8);
			Template template = conf.getTemplate(tpl);
			template.process(data, out);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		if(site.getPageSync()){
			distributeIndexHtml(site, f, mobile);
		}
		time = System.currentTimeMillis() - time;
		log.info("create index page, in {} ms", time);
	}

	@Transactional(readOnly = true)
	public boolean deleteIndex(CmsSite site) {
		File f ;
		f = new File(getIndexPath(site));
		if(site.getMobileStaticSync()){
			File mobileFile = new File(getMobileIndexPath(site));
			mobileFile.delete();
		}
		return f.delete();
	}
	
	private String getIndexPath(CmsSite site) {
		StringBuilder pathBuff = new StringBuilder();
		if (!site.getIndexToRoot()) {
			if (!StringUtils.isBlank(site.getStaticDir())) {
				pathBuff.append(site.getStaticDir());
			}
		}
		pathBuff.append("/").append(Constants.INDEX).append(
				site.getStaticSuffix());
		return realPathResolver.get(pathBuff.toString());
	}
	
	private String getMobileIndexPath(CmsSite site) {
		StringBuilder pathBuff = new StringBuilder();
		boolean indexToRoot=site.getIndexToRoot();
		String indexName;
		if (!indexToRoot) {
			if (!StringUtils.isBlank(site.getStaticMobileDir())) {
				pathBuff.append(site.getStaticMobileDir());
			}
			indexName=Constants.INDEX;
		}else{
			indexName=Constants.INDEX_MOBILE;
		}
		pathBuff.append("/").append(indexName).append(
				site.getStaticSuffix());
		return realPathResolver.get(pathBuff.toString());
	}
	
	private void distributeIndexHtml(CmsSite site,File f,boolean mobile) throws FileNotFoundException{
		if(site.getSyncPageFtp()!=null){
			Ftp syncPageFtp=ftpMng.findById(site.getSyncPageFtp().getId());
			String filename;
			if(mobile){
				filename=site.getStaticMobileDir()+"/index.html";
			}else{
				filename=site.getStaticDir()+"/index.html";
			}
			Thread thread = new Thread(new DistributionThread(syncPageFtp,filename, new FileInputStream(f)));  
			thread.start();
		}
	}

	private MessageSource tplMessageSource;
	private RealPathResolver realPathResolver;
	private StaticPageDao staticPageDao;
	private Configuration conf;
	@Autowired
	private FtpMng ftpMng;
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private CmsSiteMng siteMng;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(conf, "freemarker configuration cannot be null!");
		Assert.notNull(tplMessageSource,
				"tplMessageSource configuration cannot be null!");
	}

	public void setFreeMarkerConfigurer(
			FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	public void setTplMessageSource(MessageSource tplMessageSource) {
		this.tplMessageSource = tplMessageSource;
	}

	@Autowired
	public void setStaticPageDao(StaticPageDao staticPageDao) {
		this.staticPageDao = staticPageDao;
	}

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
	}


}
