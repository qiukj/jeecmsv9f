package com.jeecms.plug.weixin.manager.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.weixin.dao.WeixinMenuDao;
import com.jeecms.plug.weixin.entity.WeixinMenu;
import com.jeecms.plug.weixin.manager.WeixinMenuMng;

@Service
@Transactional
public class WeixinMenuMngImpl implements WeixinMenuMng {
	
	@Transactional(readOnly=true)
	public Pagination getPage(Integer siteId,Integer parentId,int pageNo,int pageSize){
		return dao.getPage(siteId,parentId,pageNo,pageSize);
	}
	
	@Transactional(readOnly=true)
	public List<WeixinMenu> getList(Integer siteId,Integer count){
		return dao.getList(siteId,count);
	}
	
	public String getMenuJsonString(Integer siteId){
		List<WeixinMenu> menus = getList(siteId,100);
		return getMenuJsonString(menus);
	}
	
	private String getMenuJsonString(List<WeixinMenu> menus) {
		String strJson = "{" +
				"\"button\":[";
				
		for (int i = 0; i < menus.size(); i++) {
			strJson = strJson + "{	";
			WeixinMenu menu = menus.get(i);
			if(menu.getChild().size()>0){
				strJson = strJson +
						"\"name\":\""+menu.getName()+"\","+
				        "\"sub_button\":[";
						Set<WeixinMenu> sets = menu.getChild();
						Iterator<WeixinMenu> iter = sets.iterator();
						int no = 1;
						while(iter.hasNext()){
							if(no>5){
								break;
							}else{
								if(no==1){
									strJson = strJson + "{";
								}else{
									strJson = strJson + ",{";
								}
								WeixinMenu child = iter.next();
								if(child.getType().equals("click")){
									strJson = strJson + 
											"\"type\":\"click\","+
											"\"name\":\""+child.getName()+"\","+
											"\"key\":\""+child.getKey()+"\"}";
								}else{
									strJson = strJson + 
											"\"type\":\"view\","+
											"\"name\":\""+child.getName()+"\","+
											"\"url\":\""+child.getUrl()+"\"}";
								}
								no++;
							}
						}
				strJson = strJson+"]";
			}else if(menu.getType().equals("click")){
				strJson = strJson + 
						"\"type\":\"click\","+
						"\"name\":\""+menu.getName()+"\","+
						"\"key\":\""+menu.getKey()+"\"";
			}else{
				strJson = strJson + 
						"\"type\":\"view\","+
						"\"name\":\""+menu.getName()+"\","+
						"\"url\":\""+menu.getUrl()+"\"";
			}
			if(i==menus.size()-1){
				strJson = strJson + "}";
			}else{
				strJson = strJson + "},";
			}
		}
		strJson = strJson + "]}";
        return strJson;
	}
	
	@Transactional(readOnly=true)
	public WeixinMenu findById(Integer id){
		return dao.findById(id);
	}
	
	public WeixinMenu save(WeixinMenu bean){
		return dao.save(bean);
	}
	
	public WeixinMenu update(WeixinMenu bean){
		Updater<WeixinMenu> updater = new Updater<WeixinMenu>(bean);
		return dao.updateByUpdater(updater);
	}
	
	public WeixinMenu deleteById(Integer id){
		return dao.deleteById(id);
	}

	public WeixinMenu[] deleteByIds(Integer[] ids){
		WeixinMenu[] beans = new WeixinMenu[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private WeixinMenuDao dao;
}
