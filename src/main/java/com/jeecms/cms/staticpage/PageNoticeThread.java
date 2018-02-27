package com.jeecms.cms.staticpage;

import java.util.List;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.service.ContentListener;
import com.jeecms.cms.web.ApplicationContextUtil;

public class PageNoticeThread extends Thread {

	private Content content;
	private List<ContentListener> listenerList;

	public PageNoticeThread(Content content,
			List<ContentListener> listenerList) {
		super();
		this.content=content;
		this.listenerList=listenerList;
	}
	

	@Override
	public void run() {
			HibernateTransactionManager transactionManager = (HibernateTransactionManager) ApplicationContextUtil.getBean("transactionManager");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
			TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
			try {
				//逻辑代码
				if (listenerList != null) {
					for (ContentListener listener : listenerList) {
						listener.afterSave(content);
					}
				}
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
			}
	}
}
