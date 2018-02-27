package com.jeecms.cms.task.job;

import java.io.IOException;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jeecms.cms.entity.assist.CmsTask;
import com.jeecms.cms.staticpage.StaticPageSvc;

import freemarker.template.TemplateException;

/**
 * @Description 首页静态化的job类
 * @author tom
 */
public class IndexStaticJob extends QuartzJobBean {
	private static final Logger log = LoggerFactory.getLogger(IndexStaticJob.class);

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			SchedulerContext schCtx = context.getScheduler().getContext();
			JobDataMap jdm = context.getJobDetail().getJobDataMap();
			// 获取Spring中的上下文
			ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
			this.staticPageSvc = (StaticPageSvc) appCtx.getBean("staticPageSvc");
			this.siteId = Integer.parseInt((String) jdm.get(CmsTask.TASK_PARAM_SITE_ID));
		} catch (SchedulerException e1) {
			// TODO 尚未处理异常
			e1.printStackTrace();
		}
		staticIndex();
	}

	public void staticIndex() {
		log.info("static index  page");
		try {
			staticPageSvc.index(siteId);
		} catch (IOException e) {
			log.error("static index error!", e);
		} catch (TemplateException e) {
			log.error("static index error!", e);
		}
	}

	private StaticPageSvc staticPageSvc;
	private Integer siteId;
}
