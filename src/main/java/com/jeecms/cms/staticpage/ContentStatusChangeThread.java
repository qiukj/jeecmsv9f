package com.jeecms.cms.staticpage;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.cms.service.ContentListener;
import com.jeecms.cms.web.ApplicationContextUtil;

public class ContentStatusChangeThread extends Thread {
	
	public static Short OPERATE_ADD=1;
	public static Short OPERATE_UPDATE=2;
	public static Short OPERATE_DEL=3;

	private Content content;
	//=1保存  =2修改 =3删除
	private Short operateType;
	private List<ContentListener> listenerList;
	private List<Map<String, Object>> mapList;

	public ContentStatusChangeThread(Content content,Short operateType
			,List<ContentListener> listenerList,List<Map<String, Object>> mapList) {
		super();
		this.content=content;
		this.operateType=operateType;
		this.listenerList=listenerList;
		this.mapList=mapList;
	}
	

	@Override
	public void run() {
			HibernateTransactionManager transactionManager = (HibernateTransactionManager) ApplicationContextUtil.getBean("transactionManager");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
			TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
			try {
				//逻辑代码
				if(operateType==1){
					afterSave(content);
				}else if(operateType==2){
					ContentMng contentMng=(ContentMng) ApplicationContextUtil.getBean("contentMng");
					content=contentMng.findById(content.getId());
					afterChange(content, mapList);
				}else if(operateType==3){
					afterDelete(content);
				}
				
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
			}
	}
	
	private void afterSave(Content content) {
		if (listenerList != null) {
			for (ContentListener listener : listenerList) {
				listener.afterSave(content);
			}
		}
	}
	
	private void afterChange(Content content, List<Map<String, Object>> mapList) {
		if (listenerList != null) {
			Assert.notNull(mapList);
			Assert.isTrue(mapList.size() == listenerList.size());
			int len = listenerList.size();
			ContentListener listener;
			for (int i = 0; i < len; i++) {
				listener = listenerList.get(i);
				listener.afterChange(content, mapList.get(i));
			}
		}
	}
	
	private void afterDelete(Content content) {
		if (listenerList != null) {
			for (ContentListener listener : listenerList) {
				listener.afterDelete(content);
			}
		}
	}
}
