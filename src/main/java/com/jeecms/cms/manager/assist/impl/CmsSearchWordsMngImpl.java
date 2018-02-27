package com.jeecms.cms.manager.assist.impl;

import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.cms.dao.assist.CmsSearchWordsDao;
import com.jeecms.cms.entity.assist.CmsSearchWords;
import com.jeecms.cms.manager.assist.CmsSearchWordsMng;
import com.jeecms.cms.web.CmsThreadVariable;

@Service
@Transactional
public class CmsSearchWordsMngImpl implements CmsSearchWordsMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Integer siteId,String name,Integer recommend,
			Integer orderBy,int pageNo, int pageSize) {
		Pagination page = dao.getPage(siteId,name,recommend,orderBy,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<CmsSearchWords> getList(Integer siteId,String name,
			Integer recommend,Integer orderBy,Integer first,
			Integer count,boolean cacheable){
		return dao.getList(siteId,name,recommend,orderBy,first,count,cacheable);
	}

	@Transactional(readOnly = true)
	public CmsSearchWords findById(Integer id) {
		CmsSearchWords entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public CmsSearchWords findByName(String name) {
		CmsSearchWords entity = dao.findByName(name);
		return entity;
	}

	public CmsSearchWords save(CmsSearchWords bean) {
		dao.save(bean);
		return bean;
	}

	public CmsSearchWords update(CmsSearchWords bean) {
		Updater<CmsSearchWords> updater = new Updater<CmsSearchWords>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public CmsSearchWords deleteById(Integer id) {
		CmsSearchWords bean = dao.deleteById(id);
		return bean;
	}

	public CmsSearchWords[] deleteByIds(Integer[] ids) {
		CmsSearchWords[] beans = new CmsSearchWords[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@SuppressWarnings("unchecked")
	public int freshCacheToDB(Ehcache cache) {
		int count = 0;
		List<String> list = cache.getKeys();
		for (String word : list) {
			Element element = cache.get(word);
			if (element == null) {
				return count;
			}
			count=(Integer) element.getObjectValue();
			CmsSearchWords searchWord=findByName(word);
			if(searchWord!=null){
				searchWord.setHitCount(count+searchWord.getHitCount());
				update(searchWord);
			}else{
				searchWord=new CmsSearchWords();
				searchWord.setHitCount(count);
				searchWord.setName(word);
				searchWord.setNameInitial(ChineseCharToEn.getAllFirstLetter(word));
				searchWord.setPriority(CmsSearchWords.DEFAULT_PRIORITY);
				searchWord.setRecommend(false);
				searchWord.setSite(CmsThreadVariable.getSite());
				save(searchWord);
			}
		}
		return count;
	}

	private CmsSearchWordsDao dao;

	@Autowired
	public void setDao(CmsSearchWordsDao dao) {
		this.dao = dao;
	}
}