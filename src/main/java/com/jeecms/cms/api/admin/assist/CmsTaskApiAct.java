package com.jeecms.cms.api.admin.assist;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsTask;
import com.jeecms.cms.manager.assist.CmsTaskMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsTaskApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsTaskApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/list")
	public void list(Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(CmsUtils.getSiteId(request), pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsTask> list = (List<CmsTask>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0; i<list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/task/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsTask bean = null;
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsTask();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init(site, user);
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/task/save")
	public void save(CmsTask bean,HttpServletRequest request,HttpServletResponse response) throws ClassNotFoundException, ParseException, SchedulerException{
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),bean.getEnable(),bean.getJobClass());
		if (!errors.hasErrors()) {
			bean.init(site, user);
			if (bean.getSite()==null) {
				bean.setSite(CmsUtils.getSite(request));
			}
			Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
			UUID uuid=UUID.randomUUID();
			bean.setTaskCode(uuid.toString());
			//静态化任务添加站点id参数
			String siteId= CmsUtils.getSite(request).getId().toString();
			if(bean.getType().equals(CmsTask.TASK_STATIC_INDEX)){
				attrs.put(CmsTask.TASK_PARAM_SITE_ID,siteId);
				bean.setAttr(attrs);
			}else if(bean.getType().equals(CmsTask.TASK_STATIC_CHANNEL)||bean.getType().equals(CmsTask.TASK_STATIC_CONTENT)){
				attrs.put(CmsTask.TASK_PARAM_SITE_ID, siteId);
			}
			bean.setAttr(attrs);
			bean = manager.save(bean);
			//启用则启动任务
			if(bean.getEnable()){
				startTask(bean,uuid.toString());
			}
			log.info("save CmsTask id={}", bean.getId());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code =ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping("/task/update")
	public void update(CmsTask bean,HttpServletRequest request,HttpServletResponse response) throws SchedulerException, ClassNotFoundException, ParseException{
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getName(),bean.getEnable(),bean.getJobClass());
		if (!errors.hasErrors()) {
			CmsTask task = manager.findById(bean.getId());
			if (task==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				if(bean.getExecycle().equals(CmsTask.EXECYCLE_DEFINE)){
					bean.setCronExpression(null);
				}else if(bean.getExecycle().equals(CmsTask.EXECYCLE_CRON)){
					bean.setIntervalUnit(null);
				}
				Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
				//静态化任务添加站点id参数
				String siteId= CmsUtils.getSite(request).getId().toString();
				if(bean.getType().equals(CmsTask.TASK_STATIC_INDEX)){
					attr.put(CmsTask.TASK_PARAM_SITE_ID,siteId);
				}else if(bean.getType().equals(CmsTask.TASK_STATIC_CHANNEL)||bean.getType().equals(CmsTask.TASK_STATIC_CONTENT)){
					attr.put(CmsTask.TASK_PARAM_SITE_ID, siteId);
				}
				//结束之前的任务，开始新任务调度
				String oldTaskCode=manager.findById(bean.getId()).getTaskCode();
				endTask(oldTaskCode);
				UUID uuid=UUID.randomUUID();
				bean.setTaskCode(uuid.toString());
				bean = manager.update(bean,attr);
				if(bean.getEnable()){
					startTask(bean,uuid.toString());
				}
				log.info("update CmsTask id={}.", bean.getId());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/task/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response) throws SchedulerException{
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors,request, idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				try {
					CmsTask[] beans = manager.deleteByIds(idArr);
					for (CmsTask bean : beans) {
						//删除结束任务
						endTask(bean.getTaskCode());
						log.info("delete CmsTask id={}", bean.getId());
					}
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					message = Constants.API_MESSAGE_DELETE_ERROR;
					code = ResponseCode.API_CODE_DELETE_ERROR;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors,HttpServletRequest request,Integer[] idArr){
		CmsSite site = CmsUtils.getSite(request);
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				vldExist(idArr[i],site.getId(), errors);
			}
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsTask entity = manager.findById(id);
		if(errors.ifNotExist(entity, CmsTask.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	/**
	 * 开始任务调度
	 * @param task 任务
	 * @param taskCode 任务名称
	 * @throws ParseException
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	private void startTask(CmsTask task,String taskCode) throws ParseException, SchedulerException, ClassNotFoundException{
		String cronExpress=manager.getCronExpressionFromDB(task.getId());
		if(cronExpress.indexOf("null")==-1){
			JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
			jobDetailFactoryBean.setName(taskCode);
			jobDetailFactoryBean.setGroup(Scheduler.DEFAULT_GROUP);
			jobDetailFactoryBean.setJobClass(getClassByTask(task.getJobClass()));
			//任务需要参数attr属性 
			jobDetailFactoryBean.setJobDataMap(getJobDataMap(task.getAttr()));
			jobDetailFactoryBean.afterPropertiesSet();
			CronTriggerFactoryBean cronTriggerFactoryBean=new CronTriggerFactoryBean();
			cronTriggerFactoryBean.setBeanName(taskCode);
			cronTriggerFactoryBean.setCronExpression(cronExpress);
			cronTriggerFactoryBean.setGroup(Scheduler.DEFAULT_GROUP);
			cronTriggerFactoryBean.setName("cron_" + taskCode);
			cronTriggerFactoryBean.afterPropertiesSet();
			scheduler.scheduleJob(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject()); 
		}
	}
	
	/**
	 * 结束任务调度
	 * @param taskName 
	 */
	private void endTask(String taskName) throws SchedulerException{
		//scheduler.deleteJob(taskName, Scheduler.DEFAULT_GROUP);
		scheduler.deleteJob(JobKey.jobKey(taskName, Scheduler.DEFAULT_GROUP));
	}
	
	
	private Class<?> getClassByTask(String taskClassName) throws ClassNotFoundException{
		return Class.forName(taskClassName);
	}
	
	private JobDataMap getJobDataMap(Map<String,String> params){
		JobDataMap jdm=new JobDataMap();
		Set<String>keySet=params.keySet();
		Iterator<String>it=keySet.iterator();
		while(it.hasNext()){
			String key=it.next();
			jdm.put(key, params.get(key));
		}
		return jdm;
	}
	
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private CmsTaskMng manager;
}
