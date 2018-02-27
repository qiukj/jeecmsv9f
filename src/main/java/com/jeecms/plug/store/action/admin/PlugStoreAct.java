package com.jeecms.plug.store.action.admin;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.plug.store.entity.PlugStoreConfig;
import com.jeecms.plug.store.entity.StorePlug;
import com.jeecms.plug.store.manager.PlugStoreConfigMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.encoder.Md5PwdEncoder;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.session.SessionProvider;

@Controller
public class PlugStoreAct {
	private static final Logger log = LoggerFactory.getLogger(PlugStoreAct.class);
	
	private static final String STORE_LOGIN="store_login";

	@RequiresPermissions("store:center")
	@RequestMapping("/store/v_center.do")
	public String list(Integer productType,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		//当变换了查询条件或者首次访问
		if(totalCount==null||productType!=null){
			totalCount=getPlugTotal(productType);
		}
		int pageSize= CookieUtils.getPageSize(request);
		Pagination p = new Pagination(cpn(pageNo),pageSize,totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());
		}else{
			p.setList(getPlugs(productType,pageSize*(cpn(pageNo)-1),pageSize));
		}
		String plugUrlPrefix=manager.getDefault().getServerUrl()+"/plug";
		model.addAttribute("pagination",p);
		model.addAttribute("pageNo",p.getPageNo());
		model.addAttribute("plugUrlPrefix",plugUrlPrefix);
		model.addAttribute("productType",productType);
		return "store/storeConfig/list";
	}
	
	
	
	@RequiresPermissions("store:v_config")
	@RequestMapping("/store/v_config.do")
	public String config(HttpServletRequest request, ModelMap model) {
		Boolean is_login=(Boolean) session.getAttribute(request, STORE_LOGIN);
		if(is_login!=null&&is_login){
			model.addAttribute("plugStoreConfig", manager.getDefault());
			return "store/storeConfig/config";
		}else{
			return "store/storeConfig/login";
		}
	}
	
	@RequiresPermissions("store:o_login")
	@RequestMapping("/store/o_login.do")
	public String o_login(String password,ModelMap model,
			HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		if(pwdEncoder.encodePassword(password).equals(manager.getDefault().getPassword())){
			session.setAttribute(request, response, STORE_LOGIN, true);
			return config(request, model);
		}else{
			return "store/storeConfig/login_error";
		}
	}
	
	@RequiresPermissions("store:config_update")
	@RequestMapping("/store/config_update.do")
	public String update(PlugStoreConfig bean,
			String password,Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean,password);
		log.info("update PlugStoreConfig id={}.", bean.getId());
		return config(request, model);
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
	
	private List<StorePlug> getPlugs(Integer productType,Integer first,Integer count){
		String serverUrl=manager.getDefault().getServerUrl();
		String url = serverUrl+"/json/plug_list.jspx?productId=1"
				+ "&first="+first+"&count="+count;
		if(productType!=null){
			url+="&productType="+productType;
		}
		String json = HttpClientUtil.getInstance().get(url);
		List<StorePlug>list=new ArrayList<StorePlug>();
		try {
			JSONArray jarray=new JSONArray(json);
			for(int i=0;i<jarray.length();i++){
				JSONObject jobject=(JSONObject) jarray.get(i);
				StorePlug plug=new StorePlug();
				plug.setId(jobject.getInt("id"));
				plug.setChargeAmount(jobject.getDouble("chargeAmount"));
				plug.setShortDesc(jobject.getString("shortDesc"));
				plug.setIsCharge(jobject.getBoolean("isCharge"));
				plug.setProductId(jobject.getInt("productId"));
				plug.setProductName(jobject.getString("productName"));
				plug.setReleaseDate(jobject.getString("releaseDate"));
				plug.setTitle(jobject.getString("title"));
				plug.setType(jobject.getInt("type"));
				list.add(plug);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id", true)) {
			return true;
		}
		PlugStoreConfig entity = manager.findById(id);
		if(errors.ifNotExist(entity, PlugStoreConfig.class, id, true)) {
			return true;
		}
		return false;
	}
	
	private Integer totalCount;
	
	@Autowired
	private PlugStoreConfigMng manager;
	@Autowired
	private SessionProvider session;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
}