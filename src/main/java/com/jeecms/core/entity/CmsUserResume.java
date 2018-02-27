package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.DateUtils;
import com.jeecms.core.entity.base.BaseCmsUserResume;



public class CmsUserResume extends BaseCmsUserResume {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getResumeName())) {
			json.put("resumeName", getResumeName());
		}else{
			json.put("resumeName", "");
		}
		if (StringUtils.isNotBlank(getTargetWorknature())) {
			json.put("targetWorknature", getTargetWorknature());
		}else{
			json.put("targetWorknature", "");
		}
		if (StringUtils.isNotBlank(getTargetWorkplace())) {
			json.put("targetWorkplace", getTargetWorkplace());
		}else{
			json.put("targetWorkplace", "");
		}
		if (StringUtils.isNotBlank(getTargetCategory())) {
			json.put("targetCategory", getTargetCategory());
		}else{
			json.put("targetCategory", "");
		}
		if (StringUtils.isNotBlank(getTargetSalary())) {
			json.put("targetSalary", getTargetSalary());
		}else{
			json.put("targetSalary", "");
		}
		if (StringUtils.isNotBlank(getEduSchool())) {
			json.put("eduSchool", getEduSchool());
		}else{
			json.put("eduSchool", "");
		}
		if(getEduGraduation()!=null){
			json.put("eduGraduation", DateUtils.parseDateToDateStr(getEduGraduation()));
		}else{
			json.put("eduGraduation", "");
		}
		if (StringUtils.isNotBlank(getEduBack())) {
			json.put("eduBack", getEduBack());
		}else{
			json.put("eduBack", "");
		}
		if (StringUtils.isNotBlank(getEduDiscipline())) {
			json.put("eduDiscipline", getEduDiscipline());
		}else{
			json.put("eduDiscipline", "");
		}
		if (StringUtils.isNotBlank(getRecentCompany())) {
			json.put("recentCompany", getRecentCompany());
		}else{
			json.put("recentCompany", "");
		}
		if (StringUtils.isNotBlank(getCompanyIndustry())) {
			json.put("companyIndustry", getCompanyIndustry());
		}else{
			json.put("companyIndustry", "");
		}
		if (StringUtils.isNotBlank(getCompanyScale())) {
			json.put("companyScale", getCompanyScale());
		}else{
			json.put("companyScale", "");
		}
		if (StringUtils.isNotBlank(getJobName())) {
			json.put("jobName", getJobName());
		}else{
			json.put("jobName","");
		}
		if(getJobStart()!=null){
			json.put("jobStart", DateUtils.parseDateToDateStr(getJobStart()));
		}else{
			json.put("jobStart", "");
		}
		if (StringUtils.isNotBlank(getJobCategory())) {
			json.put("jobCategory", getJobCategory());
		}else{
			json.put("jobCategory", "");
		}
		if (StringUtils.isNotBlank(getSubordinates())) {
			json.put("subordinates", getSubordinates());
		}else{
			json.put("subordinates", "");
		}
		if (StringUtils.isNotBlank(getJobDescription())) {
			json.put("jobDescription", getJobDescription());
		}else{
			json.put("jobDescription", "");
		}
		if (StringUtils.isNotBlank(getSelfEvaluation())) {
			json.put("selfEvaluation", getSelfEvaluation());
		}else{
			json.put("selfEvaluation", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsUserResume () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsUserResume (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsUserResume (
		java.lang.Integer id,
		java.lang.String resumeName) {

		super (
			id,
			resumeName);
	}

/*[CONSTRUCTOR MARKER END]*/


}