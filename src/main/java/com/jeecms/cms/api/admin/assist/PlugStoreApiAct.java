package com.jeecms.cms.api.admin.assist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.plug.store.manager.PlugStoreConfigMng;

@Controller
public class PlugStoreApiAct {
	
	@RequestMapping("/plug_store/list")
	public void list(Integer productType,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize ==null) {
			pageSize = 10;
		}
		//当变换了查询条件或者首次访问
		if(totalCount==null||productType!=null){
			totalCount=getPlugTotal(productType);
		}
		
	}
	
	private Integer getPlugTotal(Integer productType){
		String serverUrl=manager.getDefault().getServerUrl();
		String url = serverUrl+"/json/plug_sum.jspx?productId=1";
		if(productType!=null){
			url+="&productType="+productType;
		}
		String total = "0";
		String result=HttpClientUtil.getInstance().get(url);
		if(StringUtils.isNotBlank(result)){
			total=result;
		}
		return Integer.parseInt(total);
	}
	
	private Integer totalCount;
	@Autowired
	private PlugStoreConfigMng manager;
}
