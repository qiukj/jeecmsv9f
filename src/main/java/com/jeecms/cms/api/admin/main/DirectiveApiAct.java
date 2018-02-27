package com.jeecms.cms.api.admin.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsDirectiveTpl;
import com.jeecms.cms.manager.assist.CmsDirectiveTplMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class DirectiveApiAct {
	public static final String ENCODING = "UTF-8";
	private final static String CHANNEL="channel";
	private final static String COMMENT="comment";
	private final static String TOPIC="topic";
	private final static String VOTE="vote";
	private final static String GUESTBOOK="guestbook";
	private final static String ADVERTISE="advertise";
	private final static String LINK="link";
	private final static String TAG="tag";
	private final static String CONTENT="content";
	public static final String LIST_PREFIX = "l_";
	public static final String PAGE_PREFIX = "p_";
	public static final String SINGLE_PREFIX = "s_";
	public static final String CHANNEL_PREFIX = "c_";
	public static final String SYSTEM_TPL_PREFIX = "s_";
	public static final String CUSTOM_TPL_PREFIX = "c_";
	public static final String TPL_SUFFIX = ".txt";
	public static final String HasContent="hasContent";
	public static final String COUNT="count";
	public static final String TextLen="textLen";
	public static final String DescLen="descLen";
	public static final String TitleLen="titleLen";
	public static final String ContentLen="contentLen";
	public static final String CHECKED="checked";
	public static final String RECOMMEND="recommend";
	public static final String ORDERBY="orderBy";
	public static final String CHANNEL_ID="channelId";
	public static final String CHANNEL_PATH="channelPath";
	public static final String NULL="null";
	public static final Integer LIST_COUNT=100;
	
	
	public static final String ID="id";
	public static final String CTG_ID="ctgId";
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/directive/list")
	public void list(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsDirectiveTpl> list = (List<CmsDirectiveTpl>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/directive/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message =Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsDirectiveTpl bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsDirectiveTpl();
			}else{
				bean = manager.findById(id);
			}
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/directive/save")
	public void save(String name,String description,String module,
			HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message =Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, name,module);
		if (!errors.hasErrors()) {
			CmsDirectiveTpl bean=new CmsDirectiveTpl();
			bean.setCode(getDirectiveTpl(module, request));
			bean.setName(name);
			bean.setDescription(description);
			bean.setUser(CmsUtils.getUser(request));
			manager.save(bean);
			cmsLogMng.operating(request, "CmsDirectiveTpl.log.save", "id="
					+ bean.getId() + ";name=" + bean.getName());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/directive/code")
	public void getCode(String module,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message =Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, module);
		if (!errors.hasErrors()) {
			body = "{\"code\":\""+getDirectiveTpl(module, request)+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/directive/update")
	public void update(CmsDirectiveTpl bean,HttpServletResponse response,
			HttpServletRequest request){
		String body = "\"\"";
		String message =Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName());
		if (!errors.hasErrors()) {
			if (manager.findById(bean.getId())==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean = manager.update(bean);
				cmsLogMng.operating(request, "CmsDirectiveTpl.log.update", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/directive/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message =Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors,idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				try {
					CmsDirectiveTpl[] beans = manager.deleteByIds(idArr);
					for (CmsDirectiveTpl bean : beans) {
						cmsLogMng.operating(request, "CmsDirectiveTpl.log.delete", "id="
								+ bean.getId() + ";name=" + bean.getName());
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
	
	private WebErrors validateDelete(WebErrors errors, Integer[] idArr) {
		if (idArr!=null) {
			
			for (int i = 0; i < idArr.length; i++) {
				if (idArr[i]==null) {
					errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
					return errors;
				}
				CmsDirectiveTpl tpl = manager.findById(idArr[i]);
				if (tpl==null) {
					errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
					return errors;
				}
			}
		}
		return errors;
	}
	
	private String getDirectiveTpl(String module,HttpServletRequest request){
		Map<String,Object>params=RequestUtils.getQueryParams(request);
		String filename="";
		Map<String,String>value=new HashMap<String, String>();
		if(StringUtils.isNotBlank(module)){
			if(module.equals(CHANNEL)){
				filename=getChannelTpl(params);
				value=getChannelValue(params);
			}else if(module.equals(COMMENT)){
				filename=getCommonTpl(params, COMMENT);
				value=getCommentValue(params);
			}else if(module.equals(TOPIC)){
				filename=getCommonTpl(params, TOPIC);
				value=getTopicValue(params);
			}else if(module.equals(VOTE)){
				filename=getVoteTpl(params);
				value=getVoteValue(params);
			}else if(module.equals(GUESTBOOK)){
				filename=getCommonTpl(params, GUESTBOOK);
				value=getGuestbookValue(params);
			}else if(module.equals(ADVERTISE)){
				filename=getCommonTpl(params, ADVERTISE);
				value=getAdvertiseValue(params);
			}else if(module.equals(LINK)){
				filename=getLinkTpl(params);
				value=getLinkValue(params);
			}else if(module.equals(TAG)){
				filename=getCommonTpl(params,TAG);
				value=getTagValue(params);
			}else if(module.equals(CONTENT)){
				filename=getContentTpl(params);
				value=getContentValue(params);
			}
		}
		String directive=readTpl(new File(realPathResolver.get(filename)), value);
		return directive;
	}
	
	private String getChannelTpl(Map<String,Object>params){
		String filename="";
		String listType=(String) params.get("listType");
		String singleType=(String) params.get("singleType");
		Boolean list=getBooleanParam(params, "list");
		Boolean channel=getBooleanParam(params, "channel");
		filename+=com.jeecms.cms.Constants.DIRECTIVE_TPL_PATH+CHANNEL+"/";
		if(list){
			filename+=LIST_PREFIX;
			if(channel){
				filename+=CHANNEL_PREFIX;
			}else{
			}
			filename+=listType;
		}else{
			filename+=SINGLE_PREFIX;
			if(channel){
				filename+=CHANNEL_PREFIX;
			}else{
			}
			filename+=singleType;
		}
		filename+=TPL_SUFFIX;
		return filename;
	}
	
	private Map<String,String> getChannelValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String cid=(String)params.get("channelId");
		Boolean list=getBooleanParam(params, "list");
		Boolean channel=getBooleanParam(params, "channel");
		Boolean hasContent=getBooleanParam(params, "hasContent");
		Integer channelId=1;
		if(StringUtils.isNotBlank(cid)){
			channelId=Integer.parseInt(cid);
		}
		if(list){
			if(!channel){
				value.put(ID, channelId.toString());
			}
			value.put(HasContent, hasContent.toString());
		}else{
			if(!channel){
				value.put(ID, channelId.toString());
			}
		}
		return value;
	}
	
	private String getCommonTpl(Map<String,Object>params,String module){
		String filename="";
		Boolean page=getBooleanParam(params, "page");
		filename+=com.jeecms.cms.Constants.DIRECTIVE_TPL_PATH+module+"/";
		if(page){
			filename+=PAGE_PREFIX;
		}else{
			filename+=LIST_PREFIX;
		}
		filename+=TPL_SUFFIX;
		return filename;
	}
	
	private Map<String,String> getCommentValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String textLen=(String)params.get("textLen");
		String c=(String)params.get("count");
		Boolean recommend=getBooleanParam(params, "recommend");
		Boolean checked=getBooleanParam(params, "checked");
		Boolean orderBy=getBooleanParam(params, "orderBy");
		value.put(TextLen, textLen);
		value.put(COUNT, c);
		if(recommend==null){
			value.put(RECOMMEND, NULL);
		}else{
			value.put(RECOMMEND, recommend.toString());
		}
		if(checked==null){
			value.put(CHECKED, NULL);
		}else{
			value.put(CHECKED, checked.toString());
		}
		if(orderBy){
			value.put(ORDERBY, "1");
		}else{
			value.put(ORDERBY, "0");
		}
		return value;
	}
	
	private Map<String,String> getTopicValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String descLen=(String)params.get("descLen");
		String c=(String)params.get("count");
		Boolean recommend=getBooleanParam(params, "recommend");
		value.put(DescLen, descLen);
		value.put(COUNT, c);
		if(recommend==null){
			value.put(RECOMMEND, NULL);
		}else{
			value.put(RECOMMEND, recommend.toString());
		}
		return value;
	}
	
	private String getVoteTpl(Map<String,Object>params){
		String filename="";
		Boolean list=getBooleanParam(params, "list");
		filename+=com.jeecms.cms.Constants.DIRECTIVE_TPL_PATH+VOTE+"/";
		if(list){
			filename+=LIST_PREFIX;
		}else{
			filename+=SINGLE_PREFIX;
		}
		filename+=TPL_SUFFIX;
		return filename;
	}
	
	private Map<String,String> getVoteValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String c=(String)params.get("count");
		String v=(String)params.get("voteId");
		value.put(COUNT, c);
		value.put(ID, v);
		return value;
	}
	
	private Map<String,String> getGuestbookValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String c=(String)params.get("count");
		String ctgId=(String)params.get("ctgId");
		Boolean recommend=getBooleanParam(params, "recommend");
		Boolean checked=getBooleanParam(params, "checked");
		String titleLen=(String)params.get("titleLen");
		String contentLen=(String)params.get("contentLen");
		
		if(recommend==null){
			value.put(RECOMMEND, NULL);
		}else{
			value.put(RECOMMEND, recommend.toString());
		}
		if(checked==null){
			value.put(CHECKED, NULL);
		}else{
			value.put(CHECKED, checked.toString());
		}
		if(ctgId.equals("0")){
			value.put(CTG_ID, NULL);
		}else{
			value.put(CTG_ID, ctgId);
		}
		value.put(COUNT, c);
		
		value.put(TitleLen, titleLen);
		value.put(ContentLen, contentLen);
		return value;
	}
	
	private Map<String,String> getAdvertiseValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		Boolean page=getBooleanParam(params, "page");
		String aid=(String)params.get("aid");
		String sid=(String)params.get("sid");
		if(page!=null){
			if(page){
				value.put(ID, sid);
			}else{
				value.put(ID,aid);
			}
		}else{
			value.put(ID, sid);
		}
		return value;
	}
	
	private String getLinkTpl(Map<String,Object>params){
		return com.jeecms.cms.Constants.DIRECTIVE_TPL_PATH+LINK+"/"+LIST_PREFIX+TPL_SUFFIX;
	}
	
	private Map<String,String> getLinkValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String id=(String)params.get("id");
		value.put(ID, id);
		return value;
	}
	
	private Map<String,String> getTagValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String c=(String)params.get("count");
		value.put(COUNT, c);
		return value;
	}
	
	private String getContentTpl(Map<String,Object>params){
		String filename="";
		String type=(String) params.get("type");
		String singleType=(String) params.get("singleType");
		Boolean sysTpl=getBooleanParam(params, "sys");
		String tpl=(String) params.get("tpl");
		filename+=com.jeecms.cms.Constants.DIRECTIVE_TPL_PATH+CONTENT+"/";
		if(type.equals("single")){
			filename+=SINGLE_PREFIX+singleType;
		}else if(type.equals("page")){
			filename+=PAGE_PREFIX;
		}else if(type.equals("ids")){
			//ids或者list
			filename+=LIST_PREFIX+"i_";
		}else {
			filename+=LIST_PREFIX;
		}
		if(!(type.equals("single")||type.equals("ids"))){
			if(sysTpl){
				filename+=SYSTEM_TPL_PREFIX;
				filename+=tpl;
			}else{
				filename+=CUSTOM_TPL_PREFIX;
			}
		}
		filename+=TPL_SUFFIX;
		return filename;
	}
	
	
	private Map<String,String> getContentValue(Map<String,Object>params){
		Map<String,String>value=new HashMap<String, String>();
		String type=(String) params.get("type");
		Boolean sysTpl=getBooleanParam(params, "sys");
		String tpl=(String) params.get("tpl");
		if(type.equals("single")){
			String id=(String)params.get(ID);
			value.put(ID, id);
		}else if(type.equals("ids")){
			//ids
			String ids=(String)params.get("ids");
			String titleLen=(String)params.get("idsTitLen");
			String idsDateFormat=(String)params.get("idsDateFormat");
			value.put("ids", ids);
			value.put(TitleLen, titleLen);
			value.put("dateFormat", idsDateFormat);
		}else {
			String tagId=getStringParam(params, TAG);
			String topicID=getStringParam(params, TOPIC);
			String channelId=getStringParam(params, CHANNEL_ID);
			String channelPath=getStringParam(params, CHANNEL_PATH);
			String channelOption=getStringParam(params, "channelOption");
			String typeId=getStringsParam(params, "typeId");
			String recommend=getStringParam(params, "recommend");
			String image=getStringParam(params, "image");
			String shownew=getStringParam(params, "new");
			String title=getStringParam(params, "title");
			String orderBy=getStringParam(params, "orderBy");
			String titLen=getStringParam(params, "titLen");
			String showDesc=getStringParam(params, "showDesc");
			String descLen=getStringParam(params, "descLen");
			String target=getStringParam(params, "target");
			String dateFormat=getStringParam(params, "dateFormat");
			String count=getStringParam(params, COUNT);
			value.put("tagId", tagId);
			value.put("topicId", topicID);
			value.put("channelId", channelId);
			value.put("channelPath", channelPath);
			if(tagId!=null){
				value.put("topicId", NULL);
				value.put("channelId", NULL);
				value.put("channelPath", NULL);
			}
			if(topicID!=null){
				value.put("channelId", NULL);
				value.put("channelPath", NULL);
			}
			if(channelId!=null){
				value.put("channelPath", NULL);
			}
			value.put("channelOption", channelOption);
			value.put("typeId", typeId);
			value.put("title", title);
			value.put("orderBy", orderBy);
			value.put("titLen", titLen);
			value.put("descLen", descLen);
			value.put("dateFormat", dateFormat);
			value.put(RECOMMEND, recommend);
			value.put("image", image);
			value.put("target", target);
			value.put("count", count);
			value.put("new", shownew);
			value.put("showDesc", showDesc);
		}
		if(!(type.equals("single")||type.equals("ids"))){
			String styleList=(String) params.get("tpl"+tpl);
			String showTitleStyle=getStringParam(params,"showTitleStyle");
			String useShortTitle=getStringParam(params,"useShortTitle");
			if(sysTpl){
				value.put("styleList", styleList);
				value.put("showTitleStyle", showTitleStyle);
				value.put("useShortTitle", useShortTitle);
				if(tpl.equals("1")||tpl.equals("2")){
					//普通列表
					String lineHeight=getStringParam(params, "lineHeight");
					String headMarkImg=getStringParam(params, "headMarkImg");
					String headMark=getStringParam(params, "headMark");
					String bottomLine=getStringParam(params, "bottomLine");
					String datePosition=getStringParam(params, "datePosition");
					String ctgForm=getStringParam(params, "ctgForm");
					String picWidth=getStringParam(params, "picWidth");
					String picHeight=getStringParam(params, "picHeight");
					String rightPadding=getStringParam(params, "rightPadding");
					String picFloat=getStringParam(params, "picFloat");
					String view=getStringParam(params, "view");
					String viewTitle=getStringParam(params, "viewTitle");
					if(styleList.equals("1")||styleList.equals("3")){
						//文字 列表
						value.put("lineHeight", lineHeight);
						value.put("headMarkImg", headMarkImg);
						value.put("headMark", headMark);
						value.put("bottomLine", bottomLine);
						value.put("datePosition", datePosition);
						value.put("ctgForm", ctgForm);
						value.put("picWidth", NULL);
						value.put("picHeight", NULL);
						value.put("rightPadding", NULL);
						value.put("picFloat", NULL);
					}else if(styleList.equals("2")||styleList.equals("4")){
						//图文列表
						value.put("picWidth", picWidth);
						value.put("picHeight", picHeight);
						value.put("rightPadding", rightPadding);
						value.put("picFloat", picFloat);
						value.put("lineHeight", NULL);
						value.put("headMarkImg", NULL);
						value.put("headMark", NULL);
						value.put("bottomLine", NULL);
						value.put("datePosition", NULL);
						value.put("ctgForm", NULL);
					}
					if(styleList.equals("3")){
						//带点击率的文字列表
						value.put("view", view);
						value.put("viewTitle", viewTitle);
					}else{
						value.put("view", NULL);
						value.put("viewTitle", NULL);
					}
					//滚动列表
					if(tpl.equals("2")){
						String rollDisplayHeight=getStringParam(params, "rollDisplayHeight");
						String rollLineHeight=getStringParam(params, "rollLineHeight");
						String rollCols=getStringParam(params, "rollCols");
						String rollSpeed=getStringParam(params, "rollSpeed");
						String rollSleepTime=getStringParam(params, "rollSleepTime");
						String rollRows=getStringParam(params, "rollRows");
						String rollSpan=getStringParam(params, "rollSpan");
						String isSleep=getStringParam(params, "isSleep");
						value.put("rollDisplayHeight", rollDisplayHeight);
						value.put("rollLineHeight", rollLineHeight);
						value.put("rollCols", rollCols);
						value.put("rollSpeed", rollSpeed);
						value.put("rollSleepTime", rollSleepTime);
						value.put("rollRows", rollRows);
						value.put("rollSpan", rollSpan);
						value.put("isSleep", isSleep);
					}
				}else if(tpl.equals("3")){
					//flash焦点
					String flashWidth=getStringParam(params, "flashWidth");
					String flashHeight=getStringParam(params, "flashHeight");
					String textHeight=getStringParam(params, "textHeight");
					value.put("flashWidth", flashWidth);
					value.put("flashHeight", flashHeight);
					value.put("textHeight", textHeight);
				}
			}
		}
		return value;
	}
	
	private Boolean getBooleanParam(Map<String,Object>params,String name){
		Boolean booValue;
		String value=(String) params.get(name);
		if(StringUtils.isNotBlank(value)){
			if(value.equals("true")){
				booValue=true;
			}else if(value.equals("all")){
				booValue=null;
			}else {
				booValue=false;
			}
		}else{
			booValue=null;
		}
		return booValue;
	}
	
	private String getStringParam(Map<String,Object>params,String name){
		String value=(String) params.get(name);
		if(StringUtils.isBlank(value)||value.equals("all")){
			return null;
		}
		return value;
	}
	
	private String getStringsParam(Map<String,Object>params,String name){
		Object valueObject= params.get(name);
		if(valueObject!=null){
			if(valueObject instanceof String){
				return (String) valueObject;
			}else{
				String[] values=(String[]) valueObject;
				String value="";
				for(String v:values){
					value+=v+",";
				}
				return value;
			}
		}else{
			return null;
		}
	}
	
	private String readTpl(File tpl,Map<String,String>prop) {
		String content = null;
		try {
			content = FileUtils.readFileToString(tpl, ENCODING);
			Set<String> ps = prop.keySet();
			for (Object o : ps) {
				String key = (String) o;
				String value = prop.get(key);
				if(value==null||StringUtils.isNotBlank(value)&&value.equals(NULL)){
					content = content.replaceAll(key+"='\\#\\{" + key + "\\}'", "");
				}else{
					content = content.replaceAll("\\#\\{" + key + "\\}", value);
				}
				
			}
		} catch (IOException e) {
		}
		return content;

	}
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsDirectiveTplMng manager;
	@Autowired
	private CmsLogMng cmsLogMng;
}
