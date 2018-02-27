package com.jeecms.cms.task.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.manager.main.ContentMng;

/**
 * @Description 每日任务(内容相关1检查到期置顶,2检查到期归档任务)
 * @author tom
 */
public class ContentDayJob{
	private static final Logger log = LoggerFactory.getLogger(ContentDayJob.class);
	
	public void execute() {
		resetExpiredTopLevel();
		pigeonholeContent();
	}
	
	//重置到期置顶内容
	private void resetExpiredTopLevel(){
		List<Content>contents=contentMng.getExpiredTopLevelContents((byte) 0, new Date());
		for(Content c:contents){
			c.setTopLevel((byte) 0);
			contentMng.update(c);
		}
		log.info("Expired Content TopLevel Job success!");
	}
	
	//内容归档
	private void pigeonholeContent(){
		List<Content>contents=contentMng.getPigeonholeContents(new Date());
		for(Content c:contents){
			c.setStatus(ContentCheck.PIGEONHOLE);
			contentMng.update(c);
		}
		log.info("Pigeonhole Content Job success!");
	}
	
	@Autowired
	private ContentMng contentMng;
}
