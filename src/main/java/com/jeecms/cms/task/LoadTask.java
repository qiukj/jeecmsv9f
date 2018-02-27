package com.jeecms.cms.task;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.jeecms.cms.entity.assist.CmsTask;
import com.jeecms.cms.manager.assist.CmsTaskMng;

/**
 * @author Tom
 */
public class LoadTask{
	/**
	 * 系统初始加载任务
	 */
	public void loadTask(){
		List<CmsTask>tasks=taskMng.getList();
		if(tasks.size()>0){
			for (int i = 0; i < tasks.size(); i++) {
				CmsTask task=tasks.get(i);
				//任务开启状态 执行任务调度
				if(task.getEnable()){
					try {
						JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
						//设置任务名称
						if(StringUtils.isNotBlank(task.getTaskCode())){
							jobDetailFactoryBean.setName(task.getTaskCode());
						}else{
							UUID uuid=UUID.randomUUID();
							jobDetailFactoryBean.setName(uuid.toString());
							task.setTaskCode(uuid.toString());
							taskMng.update(task, task.getAttr());
						}
						jobDetailFactoryBean.setJobClass(getClassByTask(task.getJobClass()));
						//任务需要参数attr属性 
						jobDetailFactoryBean.setJobDataMap(getJobDataMap(task.getAttr()));
						jobDetailFactoryBean.setGroup(Scheduler.DEFAULT_GROUP);
						jobDetailFactoryBean.afterPropertiesSet();
						
						CronTriggerFactoryBean cronTriggerFactoryBean=new CronTriggerFactoryBean();
						cronTriggerFactoryBean.setBeanName("cron_" + i);
						cronTriggerFactoryBean.setCronExpression(taskMng.getCronExpressionFromDB(task.getId()));
						cronTriggerFactoryBean.setGroup(Scheduler.DEFAULT_GROUP);
						cronTriggerFactoryBean.setName("cron_" + i);
						cronTriggerFactoryBean.setDescription("");
						cronTriggerFactoryBean.afterPropertiesSet();
						//调度任务
						scheduler.scheduleJob(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject()); 
					} catch (SchedulerException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param params 任务参数
	 * @return
	 */
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
	
	/**
	 * 
	 * @param taskClassName 任务执行类名
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Class<?> getClassByTask(String taskClassName) throws ClassNotFoundException{
		return Class.forName(taskClassName);
	}
	@Autowired
	private CmsTaskMng taskMng;
	@Autowired
	private Scheduler scheduler;
}
