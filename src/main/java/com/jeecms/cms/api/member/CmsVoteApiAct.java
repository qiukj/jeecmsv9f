package com.jeecms.cms.api.member;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.action.front.AbstractVote;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsVoteTopic;
import com.jeecms.cms.entity.main.ApiRecord;
import com.jeecms.cms.manager.main.ApiRecordMng;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsVoteApiAct extends AbstractVote{
	/**
	 * 投票API
	 * @param siteId 站点ID 非必选 默认当前站
	 * @param voteId 投票ID 必选 
	 * @param subIds 调查题目ID 逗号,分隔  必选 
	 * @param itemIds 投票的调查题目选择性子项id  逗号,分隔  必选 
	 * @param subTxtIds  投票的调查题目选文本性项id  非必选 
	 * @param replys 投票的调查题目选文本性项回复内容  非必选 
	 * @param sessionKey 会话标志   非必选
	 * @param appId   appid 必选
	 * @param nonce_str 随机数 必选
	 * @param sign 签名 必选
	 */
	@RequestMapping(value = "/vote/save")
	public void cmsVoteSave(
			Integer siteId,
			Integer voteId,String subIds,
			String itemIds,String subTxtIds,String replys, 
			String sessionKey,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user = CmsUtils.getUser(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, appId,
				nonce_str,sign,voteId,subIds,itemIds);
		if(!errors.hasErrors()){
			CmsVoteTopic voteTopic=cmsVoteTopicMng.findById(voteId);
			if(voteTopic!=null){
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
				}else{
					Integer[]intSubIds=ArrayUtils.parseStringToArray(subIds);
					Integer[]intSubTxtIds=ArrayUtils.parseStringToArray(subTxtIds);
					String[]reply = null;
					if(StringUtils.isNotBlank(replys)){
						reply=replys.split(Constants.API_ARRAY_SPLIT_STR);
					}
					CmsVoteTopic vote=voteByApi(user, voteId, intSubIds, 
							parseStringToArrayList(itemIds),intSubTxtIds,
							reply, request, response, model);
					if(vote!=null){
						body="{\"id\":"+"\""+vote.getId()+"\"}";
						message=Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}else{
						Object voteResult=model.get("status");
						body = "{\"voteResult\":\""+voteResult+"\"}";
						message = Constants.API_MESSAGE_PARAM_ERROR;
						code = ResponseCode.API_CODE_PARAM_ERROR;
					}
				}
			}else{
				message=Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	public static List<Integer[]>parseStringToArrayList(String ids){
		if(StringUtils.isNotBlank(ids)){
			List<Integer[]>li=new ArrayList<Integer[]>();
			String[] listArray=ids.split(Constants.API_LIST_SPLIT_STR);
			for(String array:listArray){
				Integer[]intArray=ArrayUtils.parseStringToArray(array);
				li.add(intArray);
			}
			return li;
		}else{
			return null;
		}
	}
	
	@Autowired
	private ApiRecordMng apiRecordMng;
}
