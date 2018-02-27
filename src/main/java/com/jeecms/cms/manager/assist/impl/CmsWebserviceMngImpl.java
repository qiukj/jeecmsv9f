package com.jeecms.cms.manager.assist.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.dao.assist.CmsWebserviceDao;
import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.cms.entity.assist.CmsWebserviceParam;
import com.jeecms.cms.manager.assist.CmsWebserviceMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUserExt;

@Service
@Transactional
public class CmsWebserviceMngImpl implements CmsWebserviceMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<CmsWebservice> getList(String type){
		return dao.getList(type);
	}
	
	@Transactional(readOnly = true)
	public boolean hasWebservice(String type){
		if(getList(type).size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Transactional(readOnly = true)
	public CmsWebservice findById(Integer id) {
		CmsWebservice entity = dao.findById(id);
		return entity;
	}
	
	public void callWebService(String operate,Map<String,String>params){
		List<CmsWebservice>list=getList(operate);
		for(CmsWebservice s:list){
			callWebService(s, params);
		}
	}
	
	public String callWebService(CmsWebservice webservice,Map<String,String>paramsValues){
		String endpoint =webservice.getAddress();
		org.apache.axis.client.Service service = new org.apache.axis.client.Service(); 
		Call call;
		String res=null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint); // 为Call设置服务的位置
			call.setOperationName(new QName(webservice.getTargetNamespace(), webservice.getOperate()));
			List<CmsWebserviceParam>params=webservice.getParams();
			Object[]values=new Object[params.size()];
			for(int i=0;i<params.size();i++){
				CmsWebserviceParam p=params.get(i);
				String defaultValue=p.getDefaultValue();
				String pValue=paramsValues.get(p.getParamName());
				if(StringUtils.isBlank(pValue)){
					values[i]=defaultValue;
				}else{
					values[i]=pValue;
				}
			}
			res= (String) call.invoke(values);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return res;
	}
	
	public void callWebService(String admin,String username,String password,String email,CmsUserExt userExt,String operate){
		if(hasWebservice(operate)){
			Map<String,String>paramsValues=new HashMap<String, String>();
			paramsValues.put("username", username);
			if(StringUtils.isNotBlank(password)){
				paramsValues.put("password", password);
			}else{
				paramsValues.put("password", "");
			}
			paramsValues.put("admin", admin);
			if(StringUtils.isNotBlank(email)){
				paramsValues.put("email", email);
			}
			if(StringUtils.isNotBlank(userExt.getRealname())){
				paramsValues.put("realname", userExt.getRealname());
			}
			if(userExt.getGender()!=null){
				paramsValues.put("sex", userExt.getGender().toString());
			}
			if(StringUtils.isNotBlank(userExt.getMobile())){
				paramsValues.put("tel",userExt.getMobile());
			}
			callWebService(operate, paramsValues);
		}
	}

	public CmsWebservice save(CmsWebservice bean,String[] paramName, String[] defaultValue) {
		bean=dao.save(bean);
		// 保存参数
		bean.getParams().clear();
		if (paramName != null && paramName.length > 0) {
			for (int i = 0, len = paramName.length; i < len; i++) {
				if (!StringUtils.isBlank(paramName[i])) {
					bean.addToParams(paramName[i], defaultValue[i]);
				}
			}
		}
		return bean;
	}

	public CmsWebservice update(CmsWebservice bean,String[] paramName, String[] defaultValue) {
		Updater<CmsWebservice> updater = new Updater<CmsWebservice>(bean);
		CmsWebservice entity = dao.updateByUpdater(updater);
		entity.getParams().clear();
		if (paramName != null && paramName.length > 0) {
			for (int i = 0, len = paramName.length; i < len; i++) {
				if (!StringUtils.isBlank(paramName[i])) {
					entity.addToParams(paramName[i], defaultValue[i]);
				}
			}
		}
		return entity;
	}

	public CmsWebservice deleteById(Integer id) {
		CmsWebservice bean = dao.deleteById(id);
		return bean;
	}
	
	public CmsWebservice[] deleteByIds(Integer[] ids) {
		CmsWebservice[] beans = new CmsWebservice[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private CmsWebserviceDao dao;

	@Autowired
	public void setDao(CmsWebserviceDao dao) {
		this.dao = dao;
	}
}