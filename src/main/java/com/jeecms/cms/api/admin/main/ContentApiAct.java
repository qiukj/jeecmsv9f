package com.jeecms.cms.api.admin.main;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeecms.common.upload.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.entity.main.CmsTopic;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentExt;
import com.jeecms.cms.entity.main.ContentRecord.ContentOperateType;
import com.jeecms.cms.entity.main.ContentTxt;
import com.jeecms.cms.entity.main.ContentType;
import com.jeecms.cms.entity.main.Content.ContentStatus;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.manager.assist.CmsFileMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.cms.manager.main.CmsTopicMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.cms.manager.main.ContentTypeMng;
import com.jeecms.cms.service.ImageSvc;
import com.jeecms.cms.service.WeiXinSvc;
import com.jeecms.cms.staticpage.ContentStatusChangeThread;
import com.jeecms.cms.staticpage.FtpDeleteThread;
import com.jeecms.cms.staticpage.exception.ContentNotCheckedException;
import com.jeecms.cms.staticpage.exception.GeneratedZeroStaticPageException;
import com.jeecms.cms.staticpage.exception.StaticPageNotOpenException;
import com.jeecms.cms.staticpage.exception.TemplateNotFoundException;
import com.jeecms.cms.staticpage.exception.TemplateParseException;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.FtpMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.plug.weixin.entity.Weixin;
import net.sf.json.JSONObject;

@Controller
public class ContentApiAct {
	private static final Logger log = LoggerFactory.getLogger(ContentApiAct.class);
	@Autowired
	private ContentMng contentMng;
	@RequestMapping("/content/tree")
	public void tree(String root, Integer https,HttpServletRequest request,
			HttpServletResponse response){
		boolean isRoot;
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		List<Channel> list;
		Integer siteId = CmsUtils.getSiteId(request);
		Integer userId = CmsUtils.getUserId(request);
		CmsUser user=CmsUtils.getUser(request);
		if(user.getUserSite(siteId).getAllChannel()){
			if (isRoot) {
				list = channelMng.getTopList( siteId, true);
			} else {
				list = channelMng.getChildList(Integer.parseInt(root), true);
			}
		}else{
			if (isRoot) {
				list = channelMng.getTopListByRigth(userId, siteId, true);
			} else {
				list = channelMng.getChildListByRight(userId,siteId,Integer
						.parseInt(root), true);
			}
		}
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https, false,false, null));
			}
		}
		String body=jsonArray.toString();
		String message=Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
		
	}
	
	/**
	 * 内容列表
	 * @param queryTitle 查询条件-标题
	 * @param queryShare 查询条件-共享内容
	 * @param queryStatus  查询条件-内容状态(
	 * 							本站内容all 投稿contribute 
	 * 							草稿draft 待审prepared 已审passed 终审checked 
	 * 							退回rejected 归档pigeonhole)
	 * @param queryTypeId 查询条件-类型(普通1 图文2 焦点3 头条4)
	 * @param queryTopLevel 查询条件-固顶
	 * @param queryRecommend 查询条件-推荐
	 * @param queryOrderBy 查询条件-排序 0-23
	 * @param cid 查询条件-板块编号
	 * @param pageNo
	 * @param pageSize
	 * @param format 返回完整数据(等于0或为空时)
	 * @param https 
	 * @param hasCollect
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/content/list")
	public void list(String queryTitle,Integer queryShare,String queryStatus, Integer queryTypeId,
			Boolean queryTopLevel, Boolean queryRecommend,
			Integer queryOrderBy, Integer cid, Integer pageNo,Integer pageSize,
			Integer format,Integer https,Boolean hasCollect,
			HttpServletRequest request,HttpServletResponse response){
		queryTitle = StringUtils.trim(queryTitle);
		String queryInputUsername = RequestUtils.getQueryParam(request,
				"queryInputUsername");
		queryInputUsername = StringUtils.trim(queryInputUsername);
		if (format==null) {
			format=0;
		}
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (hasCollect==null) {
			hasCollect=false;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (queryTopLevel == null) {
			queryTopLevel = false;
		}
		if (queryRecommend == null) {
			queryRecommend = false;
		}
		if (queryOrderBy == null) {
			queryOrderBy = 4;
		}
		if(queryShare==null){
			queryShare=0;
		}
		if (cid!=null && cid.equals(0)) {
			cid =null;
		}
		ContentStatus contentStatus;
		if (!StringUtils.isBlank(queryStatus)) {
			contentStatus = ContentStatus.valueOf(queryStatus);
		} else {
			contentStatus = ContentStatus.all;
		}
		Integer queryInputUserId = null;
		if (!StringUtils.isBlank(queryInputUsername)) {
			CmsUser u = cmsUserMng.findByUsername(queryInputUsername);
			if (u != null) {
				queryInputUserId = u.getId();
			} else {
				// 用户名不存在，清空。
				//queryInputUsername = null;
				queryInputUserId=null;
			}
		}else{
			queryInputUserId=0;
		}
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		CmsUser user = CmsUtils.getUser(request);
		Integer userId = user.getId();
//		byte currStep = user.getCheckStep(siteId);
		Pagination page = manager.getPageByRight(queryShare,queryTitle, 
				queryTypeId,user.getId(),queryInputUserId, queryTopLevel,
				queryRecommend, contentStatus, user.getCheckStep(siteId), siteId, cid, userId,
				queryOrderBy, pageNo,pageSize);
		List<Content> list = (List<Content>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null && list.size()>0) {
			for(int i = 0 ; i<list.size();i++){
				jsonArray.put(i,list.get(i).convertToJson(format, https, hasCollect,true, false));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 内容分页
	 * @param queryShare
	 * @param queryStatus
	 * @param queryTypeId
	 * @param queryTopLevel
	 * @param queryRecommend
	 * @param queryOrderBy
	 * @param cid
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 */
	@RequestMapping("/content/page")
	public void page(String queryTitle,Integer queryShare,String queryStatus, Integer queryTypeId,
			Boolean queryTopLevel, Boolean queryRecommend,
			Integer queryOrderBy, Integer cid, Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		queryTitle = StringUtils.trim(queryTitle);
		String queryInputUsername = RequestUtils.getQueryParam(request,
				"queryInputUsername");
		queryInputUsername = StringUtils.trim(queryInputUsername);
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (queryTopLevel == null) {
			queryTopLevel = false;
		}
		if (queryRecommend == null) {
			queryRecommend = false;
		}
		if (queryOrderBy == null) {
			queryOrderBy = 4;
		}
		if(queryShare==null){
			queryShare=0;
		}
		ContentStatus contentStatus;
		if (!StringUtils.isBlank(queryStatus)) {
			contentStatus = ContentStatus.valueOf(queryStatus);
		} else {
			contentStatus = ContentStatus.all;
		}
		Integer queryInputUserId = null;
		if (!StringUtils.isBlank(queryInputUsername)) {
			CmsUser u = cmsUserMng.findByUsername(queryInputUsername);
			if (u != null) {
				queryInputUserId = u.getId();
			} else {
				// 用户名不存在，清空。
				//queryInputUsername = null;
				queryInputUserId=null;
			}
		}else{
			queryInputUserId=0;
		}
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		CmsUser user = CmsUtils.getUser(request);
		Integer userId = user.getId();
		Pagination p = manager.getPageCountByRight(queryShare,queryTitle, 
				queryTypeId,user.getId(),queryInputUserId, queryTopLevel,
				queryRecommend, contentStatus, user.getCheckStep(siteId), siteId, cid, userId,
				queryOrderBy, pageNo, pageSize);
		JSONObject json=new JSONObject();
		json.put("pageNo", p.getPageNo());
		json.put("pageSize", p.getPageSize());
		json.put("totalCount", p.getTotalCount());
		json.put("totalPage", p.getTotalPage());
		json.put("firstPage", p.isFirstPage());
		json.put("lastPage", p.isLastPage());
		json.put("prePage", p.getPrePage());
		json.put("nextPage", p.getNextPage());
		String body = json.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 内容详情
	 * @param id 内容编号
	 * @param format
	 * @param https
	 * @param hasCollect
	 * @param request
	 * @param response
	 */
	@RequestMapping("/content/get")
	public void get(Integer id,Integer format,Integer https,Boolean hasCollect,
			Boolean txtImgWhole,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		Content bean = null;
		if (format==null) {
			format=0;
		}
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (hasCollect==null) {
			hasCollect=false;
		}
		if(txtImgWhole==null){
			txtImgWhole=false;
		}
		if (id!=null) {
			if (id.equals(0)) {
				bean = new Content();
				if (bean.getSite()==null) {
					bean.setSite(CmsUtils.getSite(request));
				}
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				body = bean.convertToJson(format, https, hasCollect,false, txtImgWhole).toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 查看内容
	 * @param id 内容编号
	 * @param request
	 * @param response
	 */
	@RequestMapping("/content/view")
	public void view(Integer id,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			Content bean = manager.findById(id);
			if (bean!=null) {
				body = createViewJson(bean).toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 发表内容
	 * @param bean
	 * @param ext
	 * @param txt
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/save")
	public void save(Content bean,ContentExt ext,ContentTxt txt,
			Boolean copyimg,String channelIds,String topicIds,String viewGroupIds,
			String attachmentPaths,String attachmentNames,
			String picPaths,String picDescs,Integer channelId, Integer typeId, 
			String tagStr, Boolean draft,Integer cid, Integer modelId,Short charge,
			Double chargeAmount,Boolean rewardPattern,Double rewardRandomMin,Double rewardRandomMax,
			String rewardFix,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ext.getTitle(),typeId,modelId,channelId);
		if (!errors.hasErrors()) {
			errors = validateSave(channelId,modelId,typeId, request);
			// 加上模板前缀
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				CmsSite site = CmsUtils.getSite(request);
				bean.setSite(site);
				CmsUser user = CmsUtils.getUser(request);
				String tplPath = site.getTplPath();
				if (!StringUtils.isBlank(ext.getTplContent())) {
					ext.setTplContent(tplPath + ext.getTplContent());
				}
				if (!StringUtils.isBlank(ext.getTplMobileContent())) {
					ext.setTplMobileContent(tplPath + ext.getTplMobileContent());
				}
				bean.setAttr(RequestUtils.getRequestMap(request, "attr_"));
				String[] tagArr = StrUtils.splitAndTrim(tagStr, ",", MessageResolver
						.getMessage(request, "content.tagStr.split"));
				if(txt!=null&&copyimg!=null&&copyimg){
					txt=copyContentTxtImg(txt, site);
				}
				Integer[] channelArr = StrUtils.getInts(channelIds);
				Integer[] topicArr = StrUtils.getInts(topicIds);
				Integer[] viewGroupArr = StrUtils.getInts(viewGroupIds);
				String[] pathArr = getStringArr(attachmentPaths);
				String[] nameArr = getStringArr(attachmentNames);
				String[] filenameArr = nameArr;
				String[] picPathArr = getStringArr(picPaths);
				int picLength=0;
				if(picPathArr!=null){
					picLength=picPathArr.length;
				}
				String[] picDesArr = getPicDescStringArr(picDescs,picLength);
				Double[] rewardArr = getDoubleArr(rewardFix);
				bean.init();
				if (modelId!=null) {
					bean.setModel(modelMng.findById(modelId));
				}


				if(!ext.getTitle().equals("123")){
						bean = manager.save(bean, ext, txt, channelArr,topicArr, viewGroupArr, tagArr,
						pathArr, nameArr, filenameArr, picPathArr,picDesArr, channelId,
						typeId, draft, false,charge,
						chargeAmount,rewardPattern, rewardRandomMin, rewardRandomMax,
						 rewardArr,user,false);
				}else{
					try {
						FileUtil.uploadImgLW(null,"jiaju","1");
					} catch (Exception e) {
						e.printStackTrace();
					}
//					ext = new ContentExt();
//					ext.setTitle("1111111111");
//					ext.setTypeImg("/u/cms/");
//					user = new CmsUser();
//					user = cmsUserMng.findByUsername("admin");
//					String[] array = new String[1];
//					String[] pics=null;//pictures.toArray(array);
//					bean = new Content();
//					site = new CmsSite();
//					site.setDomain("localhost");
//					site.setPath("www");
//					site.setIndexToRoot(false);
//					site.setStaticIndex(false);
//					site.setTplSolution("default");
//					site.setFinalStep((byte) 3);
//					site.setAfterCheck((byte)3);
//					site.setRelativePath(false);
//					site.setResycleOn(true);
//
//
//					bean.setSite(site);
//					contentMng.savePY(bean,ext,null,null,null,null,null,null,null,null,pics,null,76,2,false,null,null,null,false,0.0,10.0,null,user,true);

				}
				fileMng.updateFileByPaths(pathArr,picPathArr,ext.getMediaPath(),ext.getTitleImg(),ext.getTypeImg(),ext.getContentImg(),true,bean);
				log.info("save Content id={}", bean.getId());
				cmsLogMng.operating(request, "content.log.save", "id=" + bean.getId()
						+ ";title=" + bean.getTitle());
				afterContentStatusChange(bean, null,ContentStatusChangeThread.OPERATE_ADD);
				body = easyJson(bean).toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改内容
	 * @param bean
	 * @param ext
	 * @param tagStr
	 * @param txt
	 * @param copyimg
	 * @param chargeAmount
	 * @param rewardPattern
	 * @param rewardRandomMin
	 * @param rewardRandomMax
	 * @param rewardFix
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/update")
	public void update(Content bean,ContentExt ext, String tagStr, ContentTxt txt,Integer modelId,
			Boolean copyimg,String channelIds, String topicIds, String viewGroupIds,
			String attachmentPaths, String attachmentNames, String picPaths,String picDescs,
			Integer channelId, Integer typeId, Boolean draft,
			Integer cid,String oldattachmentPaths,String oldpicPaths,
			String oldTitleImg,String oldContentImg,String oldTypeImg,
			Short charge,Double chargeAmount,
			Boolean rewardPattern,Double rewardRandomMin,
			Double rewardRandomMax,String rewardFix,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			message = errors.getErrors().get(0);
		}else{
			// 加上模板前缀
			CmsSite site = CmsUtils.getSite(request);
			CmsUser user = CmsUtils.getUser(request);
			String tplPath = site.getTplPath();
			if (!StringUtils.isBlank(ext.getTplContent())) {
				ext.setTplContent(tplPath + ext.getTplContent());
			}
			if (!StringUtils.isBlank(ext.getTplMobileContent())) {
				ext.setTplMobileContent(tplPath + ext.getTplMobileContent());
			}
			String[] tagArr = StrUtils.splitAndTrim(tagStr, ",", MessageResolver
					.getMessage(request, "content.tagStr.split"));
			Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
			if(txt!=null&&copyimg!=null&&copyimg){
				txt=copyContentTxtImg(txt, site);
			}
			List<Map<String, Object>>list=manager.preChange(manager.findById(bean.getId()));
			Integer[] channelArr = StrUtils.getInts(channelIds);
			Integer[] topicArr = StrUtils.getInts(topicIds);
			Integer[] viewGroupArr = StrUtils.getInts(viewGroupIds);
			String[] pathArr = getStringArr(attachmentPaths);
			String[] nameArr = getStringArr(attachmentNames);
			String[] picPathArr = getStringArr(picPaths);
			int picLength=0;
			if(picPathArr!=null){
				picLength=picPathArr.length;
			}
			String[] picDesArr = getPicDescStringArr(picDescs,picLength);
			String[] oldattachmentPathArr = getStringArr(oldattachmentPaths);
			String[] oldpicPathArr = getStringArr(oldpicPaths);
			Double[] rewardArr = getDoubleArr(rewardFix);
			if (modelId!=null) {
				bean.setModel(modelMng.findById(modelId));
			}
			bean = manager.update(bean, ext, txt,tagArr, channelArr, topicArr, viewGroupArr,
					pathArr, nameArr, nameArr,
					picPathArr, picDesArr, attr, channelId, typeId,
					draft, charge, chargeAmount,rewardPattern,
					rewardRandomMin, rewardRandomMax,
					rewardArr,user,false);
			afterContentStatusChange(bean, list,ContentStatusChangeThread.OPERATE_UPDATE);
			//处理之前的附件有效性
			fileMng.updateFileByPaths(oldattachmentPathArr,oldpicPathArr,null,oldTitleImg,oldTypeImg,oldContentImg,false,bean);
			//处理更新后的附件有效性
			fileMng.updateFileByPaths(pathArr,picPathArr,ext.getMediaPath(),ext.getTitleImg(),ext.getTypeImg(),ext.getContentImg(),true,bean);
			log.info("update Content id={}.", bean.getId());
			cmsLogMng.operating(request, "content.log.update", "id=" + bean.getId()
					+ ";title=" + bean.getTitle());
			body = easyJson(bean).toString();
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 删除内容
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				Content[] beans;
				// 是否开启回收站
				if (site.getResycleOn()) {
					Map<Integer,List<Map<String, Object>>>map=new HashMap<Integer, List<Map<String, Object>>>();
					for(Integer id:idArr){
						List<Map<String, Object>>list=manager.preChange(manager.findById(id));
						map.put(id, list);
					}
					beans = manager.cycle(CmsUtils.getUser(request),idArr);
					for (Content bean : beans) {
						afterContentStatusChange(bean, map.get(bean.getId()),ContentStatusChangeThread.OPERATE_UPDATE);
						log.info("delete to cycle, Content id={}", bean.getId());
					}
				} else {
					Map<Integer,List<Map<String, Object>>>map=new HashMap<Integer, List<Map<String, Object>>>();
					for(Integer id:idArr){
						Content c=manager.findById(id);
						//处理附件
						manager.updateFileByContent(c, false);
						List<Map<String, Object>>list=manager.preChange(manager.findById(c.getId()));
						map.put(id, list);
					}
					beans = manager.deleteByIds(idArr);
					
					//静态页与ftp删除	
					String real="";
					String ftpPath="";
					String pcFtpPath="";					
					Ftp syncPageFtp=null;
					syncPageFtp=site.getSyncPageFtp();
					if(syncPageFtp!=null){
						syncPageFtp=ftpMng.findById(syncPageFtp.getId());
					}						
					for (Content bean : beans) {
					//删除静态页
						int totalPage = bean.getPageCount();
						for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
							//判断是否手机模板						
							if(site.getMobileStaticSync()){
								real = realPathResolver.get(bean.getMobileStaticFilename(pageNo));								
							}else{
								real = realPathResolver.get(bean.getStaticFilename(pageNo));				
							}
							File f=new File(real);		
							if(f.exists()){
								f.delete();
							}						
							deleteStatic(site,bean,pageNo,ftpPath,pcFtpPath,syncPageFtp);																
						}
						log.info("delete Content id={}", bean.getId());
						afterContentStatusChange(bean, map.get(bean.getId()),ContentStatusChangeThread.OPERATE_DEL);
						cmsLogMng.operating(request, "content.log.delete", "id="
								+ bean.getId() + ";title=" + bean.getTitle());
					}
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 审核
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/check")
	public void check(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsUser user = CmsUtils.getUser(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				Map<Integer,List<Map<String, Object>>>map=new HashMap<Integer, List<Map<String, Object>>>();
				for(Integer id:idArr){
					List<Map<String, Object>>list=manager.preChange(manager.findById(id));
					map.put(id, list);
				}
				Content[] beans = manager.check(idArr, user);
				for (Content bean : beans) {
					afterContentStatusChange(bean,map.get(bean.getId()),
							ContentStatusChangeThread.OPERATE_UPDATE);
					log.info("check Content id={}", bean.getId());
				}
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 生成静态页
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/static")
	public void contentStatic(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			code = ResponseCode.API_CODE_CALL_FAIL;
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
			}else{
				try {
					Content[] beans = manager.contentStatic(CmsUtils.getUser(request),idArr);
					for (Content bean : beans) {
						log.info("static Content id={}", bean.getId());
					}
					result = true;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (TemplateNotFoundException e) {
//					message =  errors.getMessage(e.getMessage(),
//							new Object[] { e.getErrorTitle(), e.getGenerated() });
					message = Constants.API_MESSAGE_TEMPLATE_NOT_FOUNT;
				} catch (TemplateParseException e) {
//					message =  errors.getMessage(e.getMessage(),
//							new Object[] { e.getErrorTitle(), e.getGenerated() });
					message = Constants.API_MESSAGE_TEMPLATE_PARESE_ERROR;
				} catch (GeneratedZeroStaticPageException e) {
					message =  errors.getMessage(e.getMessage(), e.getGenerated());
				} catch (StaticPageNotOpenException e) {
//					message =  errors.getMessage(e.getMessage(),
//							new Object[] { e.getErrorTitle(), e.getGenerated() });
					message = Constants.API_MESSAGE_STATIC_PAGE_NOT_OPEN;
				} catch (ContentNotCheckedException e) {
//					message =  errors.getMessage(e.getMessage(),
//							new Object[] { e.getErrorTitle(), e.getGenerated() });
					message = Constants.API_MESSAGE_CONTENT_NOT_CHECKED;
				}
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 退回
	 * @param ids
	 * @param rejectOpinion
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/reject")
	public void reject(String ids,String rejectOpinion,
			Byte rejectStep,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				Map<Integer,List<Map<String, Object>>>map=new HashMap<Integer, List<Map<String, Object>>>();
				for(Integer id:idArr){
					List<Map<String, Object>>list=manager.preChange(manager.findById(id));
					map.put(id, list);
				}
				if(rejectStep==null){
					rejectStep=CmsUtils.getUser(request).getCheckStep(CmsUtils.getSiteId(request));
				}
				Content[] beans =manager.reject(idArr, user,rejectStep, rejectOpinion);
				for (Content bean : beans) {
					afterContentStatusChange(bean, map.get(bean.getId()),
							ContentStatusChangeThread.OPERATE_UPDATE);
					log.info("reject Content id={}", bean.getId());
				}
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 提交
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/submit")
	public void submit(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsUser user = CmsUtils.getUser(request);
		boolean result = false;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				try {
					Content[] beans = manager.submit(idArr, user);
					for (Content bean : beans) {
						log.info("submit Content id={}", bean.getId());
					}
					result =true;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 移动
	 * @param ids
	 * @param channelId
	 * @param response
	 * @param request
	 */
	@SignValidate
	@RequestMapping("/content/move")
	public void move(String ids,Integer channelId,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids,channelId);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				Channel channel = channelMng.findById(channelId);
				if (channel!=null) {
					for(Integer contentId:idArr){
						Content bean=manager.findById(contentId);
						if(bean!=null&&channel!=null){
							bean.removeSelfAddToChannels(channelMng.findById(channelId));
							bean.setChannel(channel);
							manager.update(CmsUtils.getUser(request), bean, ContentOperateType.move);
						}
					}
					result = true;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 复制
	 * @param ids
	 * @param channelId
	 * @param siteId
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/copy")
	public void copy(String ids,Integer channelId,Integer siteId,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsUser user=CmsUtils.getUser(request);
		boolean result = false;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,channelId);
		if (!errors.hasErrors()) {
			Channel channel = channelMng.findById(channelId);
			if (channel==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				Integer[] idArr = StrUtils.getInts(ids);
				errors = validateContent(errors, idArr);
				if (errors.hasErrors()) {
					message = errors.getErrors().get(0);
					code = ResponseCode.API_CODE_CALL_FAIL;
				}else{
					for(Integer contentId:idArr){
						Content bean=manager.findById(contentId);
						Content beanCopy= new Content();
						ContentExt extCopy=new ContentExt();
						ContentTxt txtCopy=new ContentTxt();
						beanCopy=bean.cloneWithoutSet();
						beanCopy.setChannel(channel);
						//复制到别站
						if(siteId!=null){
							beanCopy.setSite(siteMng.findById(siteId));
						}
						boolean draft=false;
						if(bean.getStatus().equals(ContentCheck.DRAFT)){
							draft=true;
						}
						BeanUtils.copyProperties(bean.getContentExt(), extCopy);
						if(bean.getContentTxt()!=null){
							BeanUtils.copyProperties(bean.getContentTxt(), txtCopy);
						}
						manager.save(beanCopy, extCopy, txtCopy, null, bean.getTopicIds(),
								bean.getViewGroupIds(), bean.getTagArray(), 
								bean.getAttachmentPaths(), bean.getAttachmentNames(),
								bean.getAttachmentFileNames(),bean.getPicPaths(),
								bean.getPicDescs(), channelId,
								bean.getType().getId(), draft, false,bean.getChargeModel(),
								bean.getChargeAmount(),bean.getRewardPattern(),
								bean.getRewardRandomMin(),bean.getRewardRandomMax(),
								bean.getRewardFixValues(),user,
								false);
						afterContentStatusChange(bean,null,ContentStatusChangeThread.OPERATE_ADD);
					}
					result = true;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 引用
	 * @param ids
	 * @param channelId
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/refer")
	public void refer(String ids,Integer channelId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,channelId);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				for(Integer contentId:idArr){
					manager.updateByChannelIds(contentId, new Integer[]{channelId},Content.CONTENT_CHANNEL_ADD);
				}
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 保存固顶等级
	 * @param ids
	 * @param topLevel
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/priority")
	public void priority(String ids,String topLevel,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids,topLevel);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				Byte[] bytes = splitToByte(topLevel);
				errors = validatePriority(errors, idArr,bytes);
				if (!errors.hasErrors()) {
					for(int i=0;i<idArr.length;i++){
						Content c=manager.findById(idArr[i]);
						c.setTopLevel(bytes[i]);
						manager.update(c);
					}
					log.info("update content priority.");
					result = true;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = errors.getErrors().get(0);
					code = ResponseCode.API_CODE_PARAM_ERROR;
				}
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 归档
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/pigeonhole")
	public void pigeonhole(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors= WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				for(int i=0;i<idArr.length;i++){
					Content c=manager.findById(idArr[i]);
					List<Map<String, Object>>list=manager.preChange(c);
					c.setStatus(ContentCheck.PIGEONHOLE);
					afterContentStatusChange(c, list,ContentStatusChangeThread.OPERATE_UPDATE);
					manager.update(CmsUtils.getUser(request), c, ContentOperateType.pigeonhole);
				}
				log.info("update CmsFriendlink priority.");
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 出档
	 * @param ids
	 * @param response
	 * @param request
	 */
	@SignValidate
	@RequestMapping("/content/unpigeonhole")
	public void unpigeonhole(String ids,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		WebErrors errors= WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				for(int i=0;i<idArr.length;i++){
					Content c=manager.findById(idArr[i]);
					List<Map<String, Object>>list=manager.preChange(c);
					c.setStatus(ContentCheck.CHECKED);
					manager.update(CmsUtils.getUser(request), c, ContentOperateType.reuse);
					afterContentStatusChange(c, list,ContentStatusChangeThread.OPERATE_UPDATE);
				}
				log.info("update CmsFriendlink priority.");
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 推荐、取消推荐
	 * @param ids
	 * @param isRecommend true推荐 false取消推荐
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/recommend")
	public void recommend(String ids,Byte level,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		WebErrors errors= WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,level);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				for (int i = 0; i < idArr.length; i++) {
					Content content = manager.findById(idArr[i]);
					if (level>0) {
						content.setRecommend(true);
					}else{
						content.setRecommend(false);
					}
					if (level<=0) {
						level = 0;
					}
					content.setRecommendLevel(level);
					manager.update(content);
				}
				log.info("update CmsFriendlink recommend.");
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 推送至主题
	 * @param ids
	 * @param topicIds
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/content/send_to_topic")
	public void sendToTopic(String ids,String topicIds,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, ids,topicIds);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			Integer[] topicArr = StrUtils.getInts(topicIds);
			errors = validateSendToTopic(errors,request,idArr,topicArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				for(Integer contentId:idArr){
					manager.addContentToTopics(contentId,topicArr);
				}
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/content/send_to_weixin")
	public void sendToWeixin(String ids,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		Map<String, String> msg=null;
		JSONObject json = new JSONObject();
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		Integer wxCode=null;
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			if (errors.hasErrors()||idArr==null||idArr.length<=0) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				Content[] beans = new Content[idArr.length];
				for(int i=0;i<idArr.length;i++){
					Integer contentId=idArr[i];
					beans[i] = manager.findById(contentId);
				}
				// 判断正文是否存在	
				Boolean flag=true;
				for(Content bean:beans){
					if(StringUtils.isBlank(bean.getTxt())){
						flag=false;
						break;
					}
				}
				if(flag){	
					msg=weiXinSvc.sendTextToAllUser(beans);
					wxCode=Integer.parseInt(msg.get("status"));
					if(wxCode.equals(Weixin.TENCENT_WX_SUCCESS)){
						result = true;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						//成功发送状态码无价值
						wxCode=null;
					}else{
						code = "\""+wxCode+"\"";
						message = Constants.API_MESSAGE_SEND_TO_WEXIN_ERROR;
					}
					if (StringUtils.isNotBlank(msg.get("errmsg"))) {
						message=msg.get("errmsg");
					}
					json.put("wxCode", wxCode);
				}else{
					result = false;
					message=Constants.API_MESSAGE_NOT_CONTENT_ERROR;
					code="202";
				}	
					
			}
		}
		json.put("result", result);
		body = json.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code,wxCode);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/content/cycle_recycle")
	public void cycleRecycle(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				Map<Integer,List<Map<String, Object>>>map=new HashMap<Integer, List<Map<String, Object>>>();
				for(Integer id:idArr){
					List<Map<String, Object>>list=manager.preChange(manager.findById(id));
					map.put(id, list);
				}
				Content[] beans = manager.recycle(idArr);
				for (Content bean : beans) {
					afterContentStatusChange(bean,map.get(bean.getId()),
							ContentStatusChangeThread.OPERATE_UPDATE);
					log.info("delete Content id={}", bean.getId());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/content/cycle_delete")
	public void cycleDelete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateContent(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				for(Integer id:idArr){
					Content c=manager.findById(id);
					//处理附件
					manager.updateFileByContent(c, false);
				}
				Map<Integer,List<Map<String, Object>>>map=new HashMap<Integer, List<Map<String, Object>>>();
				for(Integer id:idArr){
					List<Map<String, Object>>list=manager.preChange(manager.findById(id));
					map.put(id, list);
				}
				Content[] beans = manager.deleteByIds(idArr);
				//静态页与ftp删除	
				String real="";
				String ftpPath="";
				String pcFtpPath="";
				Ftp syncPageFtp=null;
				syncPageFtp=site.getSyncPageFtp();
				if(syncPageFtp!=null){
					syncPageFtp=ftpMng.findById(syncPageFtp.getId());
				}						
				//删除静态页
				int count=0;				
				for (Content bean : beans) {	
					count+=1;
					//判断是否手机模板						
					if(site.getMobileStaticSync()){
						real = realPathResolver.get(bean.getMobileStaticFilename(count));								
					}else{
						real = realPathResolver.get(bean.getStaticFilename(count));				
					}
					File f=new File(real);		
					if(f.exists()){
						f.delete();
					}						
					deleteStatic(site,bean,count,ftpPath,pcFtpPath,syncPageFtp);	
					afterContentStatusChange(bean,map.get(bean.getId()),
							ContentStatusChangeThread.OPERATE_DEL);
					log.info("delete Content id={}", bean.getId());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateContent(WebErrors errors,Integer[] arr,HttpServletRequest request){
		CmsSite site = CmsUtils.getSite(request);
		for (Integer id : arr) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}
	
	private WebErrors validateContent(WebErrors errors,Integer[] arr){
		if (arr!=null) {
			for (int i = 0; i < arr.length; i++) {
				Content content = manager.findById(arr[i]);
				if (content==null) {
					errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
					return errors;
				}
			}
		}
		return errors;
	}
	
	private WebErrors validateSendToTopic(WebErrors errors,HttpServletRequest request,Integer[] arr1,Integer[] arr2){
		CmsSite site = CmsUtils.getSite(request);
		if (arr1!=null) {
			for (Integer id : arr1) {
				vldExist(id, site.getId(), errors);
			}
		}
		if (arr2!=null) {
			for(Integer topicId:arr2){
				vldTopicExist(topicId, site.getId(), errors);
			}
		}
		return errors;
	}
	
	private boolean vldTopicExist(Integer topicId, Integer siteId, WebErrors errors) {
		if (errors.ifNull(topicId, "topicId", false)) {
			return true;
		}
		CmsTopic entity = topicMng.findById(topicId);
		if (errors.ifNotExist(entity, Content.class, topicId, false)) {
			return true;
		}
		return false;
	}
	
	private WebErrors validatePriority(WebErrors errors,Integer[] arr1,Byte[] arr2){
		if (arr1!=null&&arr2!=null) {
			if (arr1.length!=arr2.length) {
				errors.addError(Constants.API_MESSAGE_PARAM_ERROR);
				return errors;
			}
		}
		return errors;
	}
	
	private Byte[] splitToByte(String str){
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] split = str.split(",");
		Byte[] bytes = new Byte[split.length];
		for(int i = 0 ; i < split.length; i++){
			bytes[i] = Byte.parseByte(split[i]);
		}
		return bytes;
	}
	
	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids", false);
		if(ids!=null&&ids.length>0){
			for (Integer id : ids) {
				Content content = manager.findById(id);
				// TODO 是否有编辑的数据权限。
				// 是否有审核后删除权限。
				if (!content.isHasDeleteRight()) {
					errors.addErrorString("content.error.afterCheckDelete");
					return errors;
				}
			}
		}
		return errors;
	}
	
	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		Content content = manager.findById(id);
		// TODO 是否有编辑的数据权限。
		// 是否有审核后更新权限。
		if (!content.isHasUpdateRight()) {
			errors.addErrorString("content.error.afterCheckUpdate");
			return errors;
		}
		return errors;
	}
	
	private void afterContentStatusChange(Content content,
			List<Map<String, Object>>list,Short operate){
		ContentStatusChangeThread afterThread = new ContentStatusChangeThread(
				content,operate,
				manager.getListenerList(),
				list);
		afterThread.start();
	}
	
	private Double[] getDoubleArr(String str){
		if (StringUtils.isBlank(str)) {
			return null;
		}else{
			String[] split = str.split(",");
			Double[] newArr = new Double[split.length];
			for(int i= 0; i<split.length;i++){
				newArr[i] = Double.parseDouble(split[i]);
			}
			return newArr;
		}
	}
	
	private String[] getStringArr(String str){
		if (StringUtils.isBlank(str)) {
			return null;
		}else{
			return str.split(",");
		}
	}
	
	private String[] getPicDescStringArr(String str,int length){
		String[] desc=new String[length];
		if (StringUtils.isBlank(str)) {
			return desc ;
		}else{
			return str.split(",",-1);
		}
		
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		Content entity = manager.findById(id);
		if (errors.ifNotExist(entity, Content.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	private JSONObject createViewJson(Content bean){
		JSONObject json = new JSONObject();
		if (bean.getId()!=null) {
			json.put("id", bean.getId());
		}else{
			json.put("id", "");
		}
		if (bean.getSiteId()!=null) {
			json.put("siteId", bean.getSiteId());
		}else{
			json.put("siteId", "");
		}
		if (StringUtils.isNotBlank(bean.getUrlDynamic())) {
			json.put("urlDynamic", bean.getUrlDynamic());
		}else{
			json.put("urlDynamic", "");
		}
		if (StringUtils.isNotBlank(bean.getTit())) {
			json.put("title", bean.getTit());
		}else{
			json.put("title", "");
		}
		if (bean.getReleaseDate()!=null) {
			json.put("releaseDate", DateUtils.parseDateToTimeStr(bean.getReleaseDate()));
		}else{
			json.put("releaseDate", "");
		}
		if (StringUtils.isNotBlank(bean.getTxt())) {
			json.put("txt", bean.getTxt());
		}else{
			json.put("txt", "");
		}
		if (StringUtils.isNotBlank(bean.getOrigin())) {
			json.put("origin", bean.getOrigin());
		}else{
			json.put("origin", "");
		}
		if (bean.getUser()!=null&&StringUtils.isNotBlank(bean.getUser().getUsername())) {
			json.put("username", bean.getUser().getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(bean.getAuthor())) {
			json.put("author", bean.getAuthor());
		}else{
			json.put("author", "");
		}
		if (bean.getViews()!=null) {
			json.put("views", bean.getViews());
		}else{
			json.put("views", "");
		}
		return json;
	}
	
	private WebErrors validateSave(Integer channelId,Integer modelId,Integer typeId,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifNull(channelId, "channelId", false)) {
			return errors;
		}
		Channel channel = channelMng.findById(channelId);
		if (errors.ifNotExist(channel, Channel.class, channelId, false)) {
			return errors;
		}
		if (channel.getChild().size() > 0) {
			errors.addErrorString("content.error.notLeafChannel");
		}
		//所选发布内容模型不在栏目模型范围内
		if(modelId!=null){
			CmsModel m= modelMng.findById(modelId);
			if(errors.ifNotExist(m, CmsModel.class, modelId, false)){
				return errors;
			}
			//默认没有配置的情况下modelIds为空 则允许添加
			if(channel.getModelIds().size()>0&&!channel.getModelIds().contains(modelId.toString())){
				errors.addErrorString("channel.modelError");
			}
		}
		if (typeId!=null) {
			ContentType type = typeMng.findById(typeId);
			if (type==null) {
				//
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			}
		}
		return errors;
	}
	
	private ContentTxt copyContentTxtImg(ContentTxt txt,CmsSite site){
		if(StringUtils.isNotBlank(txt.getTxt())){
			txt.setTxt(copyTxtHmtlImg(txt.getTxt(), site));
		}
		if(StringUtils.isNotBlank(txt.getTxt1())){
			txt.setTxt1(copyTxtHmtlImg(txt.getTxt1(), site));
		}	
		if(StringUtils.isNotBlank(txt.getTxt2())){
			txt.setTxt2(copyTxtHmtlImg(txt.getTxt2(), site));
		}
		if(StringUtils.isNotBlank(txt.getTxt3())){
			txt.setTxt3(copyTxtHmtlImg(txt.getTxt3(), site));
		}
		return txt;
	}
	
	private JSONObject easyJson(Content bean){
		JSONObject json = new JSONObject();
		if (bean.getId()!=null) {
			json.put("id", bean.getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(bean.getUrl())) {
			json.put("url", bean.getUrl());
		}else{
			json.put("url", "");
		}
		return json;
	}
	
	private String copyTxtHmtlImg(String txtHtml,CmsSite site){
		List<String>imgUrls=ImageUtils.getImageSrc(txtHtml);
		for(String img:imgUrls){
			txtHtml=txtHtml.replace(img, imageSvc.crawlImg(img,site));
		}
		return txtHtml;
	}
	
	private void deleteStatic(CmsSite site, Content bean,Integer pageNo,String ftpPath,
			String pcFtpPath,Ftp syncPageFtp){
		
		if(syncPageFtp!=null&&bean.getChannel().getStaticChannel()){
			if(site.getMobileStaticSync()){							
				ftpPath=bean.getSite().getSyncPageFtp().getPath();
				ftpPath+=bean.getMobileStaticFilename(pageNo);			
				new Thread(new FtpDeleteThread(syncPageFtp,ftpPath)).start();
			}				
			pcFtpPath=bean.getSite().getSyncPageFtp().getPath();
			pcFtpPath+=bean.getStaticFilename(pageNo);								
			new Thread(new FtpDeleteThread(syncPageFtp,pcFtpPath)).start();
			
		}
		
	}
	
	
	@Autowired
	private ContentTypeMng typeMng;
	@Autowired
	private CmsModelMng modelMng;
	@Autowired
	private CmsTopicMng topicMng;
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private ContentMng manager;
	@Autowired
	private CmsFileMng fileMng;
	@Autowired
	private CmsSiteMng siteMng;
	@Autowired
	private ImageSvc imageSvc;
	@Autowired
	private WeiXinSvc weiXinSvc;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private FtpMng ftpMng;
}
