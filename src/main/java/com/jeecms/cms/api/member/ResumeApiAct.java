package com.jeecms.cms.api.member;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsJobApply;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.assist.CmsJobApplyMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserResume;
import com.jeecms.core.manager.CmsUserResumeMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ResumeApiAct {
	
	/**
	 * 简历查看
	 * @param appId appId
	 * @param sessionKey 会话标识
	 */
	@RequestMapping(value = "/resume/get")
	public void resumeGet(
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		String code=ResponseCode.API_CODE_NOT_FOUND;
		CmsUser user=CmsUtils.getUser(request);
		CmsUserResume resume=user.getUserResume();
		if(resume!=null){
			body= resume.convertToJson().toString();
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 简历修改
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识 必选
	 * @param resumeName 建立名称  必选
	 * @param targetWorknature 期望工作性质 非必选
	 * @param targetWorkplace 期望工作地点 非必选
	 * @param targetCategory 期望职位类别 非必选
	 * @param targetSalary 期望月薪 非必选
	 * @param eduSchool  毕业学校 非必选
	 * @param eduGraduation  毕业时间 非必选
	 * @param eduBack  学历 非必选
	 * @param eduDiscipline 专业 非必选
	 * @param recentCompany  最近工作公司名称 非必选
	 * @param companyIndustry  最近公司所属行业 非必选
	 * @param companyScale  公司规模 非必选
	 * @param jobName 职位名称 非必选
	 * @param jobCategory 职位类别 非必选
	 * @param jobStart 工作起始时间 非必选
	 * @param subordinates 下属人数 非必选
	 * @param jobDescription 工作描述 非必选
	 * @param selfEvaluation 自我评价 非必选
	 */
	@SignValidate
	@RequestMapping(value = "/resume/update")
	public void resumeUpdate(
			String resumeName,String targetWorknature,String targetWorkplace,
			String targetCategory,String targetSalary,String eduSchool,
			String eduGraduation,String eduBack,String eduDiscipline,
			String recentCompany,String companyIndustry,String companyScale,
			String jobName,String jobCategory,String jobStart,
			String subordinates,String jobDescription,String selfEvaluation,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user=CmsUtils.getUser(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,resumeName);
		if(!errors.hasErrors()){
			boolean update=true;
			CmsUserResume resume=user.getUserResume();
			if(resume==null){
				update=false;
			}else{
				resume=new CmsUserResume();
			}
			if(StringUtils.isNotBlank(companyIndustry)){
				resume.setCompanyIndustry(companyIndustry);
			}
			if(StringUtils.isNotBlank(companyScale)){
				resume.setCompanyScale(companyScale);
			}
			if(StringUtils.isNotBlank(eduBack)){
				resume.setEduBack(eduBack);
			}
			if(StringUtils.isNotBlank(eduDiscipline)){
				resume.setEduDiscipline(eduDiscipline);
			}
			if(StringUtils.isNotBlank(eduGraduation)){
				resume.setEduGraduation(DateUtils.parseDayStrToDate(eduGraduation));
			}
			if(StringUtils.isNotBlank(eduSchool)){
				resume.setEduSchool(eduSchool);
			}
			if(StringUtils.isNotBlank(jobCategory)){
				resume.setJobCategory(jobCategory);
			}
			if(StringUtils.isNotBlank(jobDescription)){
				resume.setJobDescription(jobDescription);
			}
			if(StringUtils.isNotBlank(jobName)){
				resume.setJobName(jobName);
			}
			if(StringUtils.isNotBlank(jobStart)){
				resume.setJobStart(DateUtils.parseDayStrToDate(jobStart));
			}
			if(StringUtils.isNotBlank(recentCompany)){
				resume.setRecentCompany(recentCompany);
			}
			if(StringUtils.isNotBlank(resumeName)){
				resume.setResumeName(resumeName);
			}
			if(StringUtils.isNotBlank(selfEvaluation)){
				resume.setSelfEvaluation(selfEvaluation);
			}
			if(StringUtils.isNotBlank(subordinates)){
				resume.setSubordinates(subordinates);
			}
			if(StringUtils.isNotBlank(targetCategory)){
				resume.setTargetCategory(targetCategory);
			}
			if(StringUtils.isNotBlank(targetSalary)){
				resume.setTargetSalary(targetSalary);
			}
			if(StringUtils.isNotBlank(targetWorknature)){
				resume.setTargetWorknature(targetWorknature);
			}
			if(StringUtils.isNotBlank(targetWorkplace)){
				resume.setTargetWorkplace(targetWorkplace);
			}
			if(update){
				resume.setId(user.getId());
				cmsUserResumeMng.update(resume, user);
			}else{
				cmsUserResumeMng.save(resume, user);
			}
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 职位申请API
	 * @param contentId 内容ID 必选
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识 必选
	 * @param contentId 职位ID
	 */
	@SignValidate
	@RequestMapping(value = "/resume/apply")
	public void jobApply(Integer contentId,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user=CmsUtils.getUser(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,contentId);
		if(!errors.hasErrors()){
			Content c=contentMng.findById(contentId);
			if(c!=null){
				if(user.getUserResume()!=null){
					if(user.hasApplyToday(contentId)){
						message="\"today has apply\"";
					}else{
						CmsJobApply jobApply=new CmsJobApply();
						jobApply.setApplyTime(Calendar.getInstance().getTime());
						jobApply.setContent(c);
						jobApply.setUser(user);
						jobApply=jobApplyMng.save(jobApply);
						body="{\"id\":"+"\""+jobApply.getId()+"\"}";
						message=Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}else{
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}
			}else{
				message=Constants.API_MESSAGE_CONTENT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 取消职位申请API
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识 必选
	 * @param ids 申请的id 逗号,分隔  必选
	 */
	@SignValidate
	@RequestMapping(value = "/resume/cancelApply")
	public void jobCancelApply(
			String ids,HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		errors=ApiValidate.validateRequiredParams(request,errors,ids);
		if(!errors.hasErrors()){
			Integer[] intIds=ArrayUtils.parseStringToArray(ids);
			jobApplyMng.deleteByIds(intIds);
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 我的申请列表api
	 * @param siteId 站点id 非必选 默认当前站
	 * @param appId appid 必选
	 * @param sessionKey 会话标识 必选
	 * @param first 非必选 默认0
	 * @param count 非必选 默认10
	 */
	@RequestMapping(value = "/resume/myapplys")
	public void jobMyApply(Integer siteId,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(siteId==null){
			siteId=CmsUtils.getSiteId(request);
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		CmsUser user=CmsUtils.getUser(request);
		List<CmsJobApply> list=jobApplyMng.getList(
				user.getId(),null,siteId,true,null,first, count);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
			}
		}
		String body=jsonArray.toString();
		String message=Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private CmsUserResumeMng cmsUserResumeMng;
	@Autowired
	private CmsJobApplyMng jobApplyMng;
	@Autowired
	private ContentMng contentMng;
}
