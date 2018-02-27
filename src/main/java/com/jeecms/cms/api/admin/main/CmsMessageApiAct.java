package com.jeecms.cms.api.admin.main;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsMessage;
import com.jeecms.cms.entity.assist.CmsReceiverMessage;
import com.jeecms.cms.manager.assist.CmsMessageMng;
import com.jeecms.cms.manager.assist.CmsReceiverMessageMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsMessageApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsMessageApiAct.class);
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/message/list")
	public void list(Integer pageNo, String title, Date sendBeginTime,
			Date sendEndTime, Boolean status, Integer box,Integer pageSize,
			HttpServletRequest request, HttpServletResponse response){
		CmsUser user = CmsUtils.getUser(request);
		Pagination pagination = null;
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (box==null) {
			box = 0 ;
		}
		if (box.equals(0)) {
			pagination = receiverMessageMng.getPage(null, null, user
					.getId(), title, sendBeginTime, sendEndTime, status, box,
					false, pageNo, pageSize);
		} else if (box.equals(1)) {
			// 发件箱
			pagination = messageMng.getPage(null, user.getId(), null,
					title, sendBeginTime, sendEndTime, status, box, false,
					pageNo, pageSize);
		}else if (box.equals(2)) {
			//草稿箱
			pagination = messageMng.getPage(null, user.getId(), null,
					title, sendBeginTime, sendEndTime, status, box, false,
					pageNo, pageSize);
		} else if (box.equals(3)) {
			// 垃圾箱(可能从收件箱或者从发件箱转过来)
			pagination = receiverMessageMng.getPage(null, user.getId(),
					user.getId(), title, sendBeginTime, sendEndTime, status,
					box, false, pageNo, pageSize);
		}
		int totalCount = pagination.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (box.equals(1)||box.equals(2)) {
			List<CmsMessage> list = (List<CmsMessage>) pagination.getList();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
		}else{
			List<CmsReceiverMessage> list = (List<CmsReceiverMessage>) pagination.getList();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 消息
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping("/message/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsMessage bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsMessage();
			}else{
				bean = messageMng.findById(id);
			}
			if (bean!=null) {
				body = bean.convertToJson().toString();
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
	 * 发送消息
	 * @param bean
	 * @param username
	 * @param groupId
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/message/send")
	public void send(CmsMessage bean,String username,Integer groupId,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getMsgTitle());
		if (!errors.hasErrors()) {
			CmsSite site = CmsUtils.getSite(request);
			CmsUser user = CmsUtils.getUser(request);
			Date now = new Date();
			CmsReceiverMessage receiverMessage = new CmsReceiverMessage();
			CmsUser msgReceiverUser = userMng.findByUsername(username);
			if (msgReceiverUser==null&&(groupId==null||groupId.equals(-1))) {
				message = Constants.API_MESSAGE_PARAM_ERROR;
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				if (msgReceiverUser!=null) {
					messageInfoSet(bean, receiverMessage, user, msgReceiverUser, 
							now, site, request);
				}
				if (groupId != null && !groupId.equals(-1)) {
					List<CmsUser> users;
					CmsUser tempUser;
					CmsMessage tempMsg;
					CmsReceiverMessage tempReceiverMsg;
					if (groupId.equals(0)) {
						// 所有未禁用会员
						users = userMng.getList(null, null, null, null, CmsUser.USER_STATU_CHECKED, false,
								null);
						if (users != null && users.size() > 0) {
							for (int i = 0; i < users.size(); i++) {
								tempUser = users.get(i);
								tempMsg = new CmsMessage();
								tempMsg.setMsgTitle(bean.getMsgTitle());
								tempMsg.setMsgContent(bean.getMsgContent());
								tempReceiverMsg = new CmsReceiverMessage();
								if (msgReceiverUser != null) {
									if (!tempUser.equals(msgReceiverUser)) {
										messageInfoSet(tempMsg, tempReceiverMsg, user,
												tempUser, now, site, request);
									}
								} else {
									messageInfoSet(tempMsg, tempReceiverMsg, user,
											tempUser, now, site, request);
								}
							}
						}
					} else {
						// 非禁用的会员
						users = userMng.getList(null, null, null, groupId,
								CmsUser.USER_STATU_CHECKED,false, null);
						if (users != null && users.size() > 0) {
							for (int i = 0; i < users.size(); i++) {
								tempUser = users.get(i);
								tempMsg = new CmsMessage();
								tempMsg.setMsgTitle(bean.getMsgTitle());
								tempMsg.setMsgContent(bean.getMsgContent());
								tempReceiverMsg = new CmsReceiverMessage();
								if (msgReceiverUser != null) {
									if (!tempUser.equals(msgReceiverUser)) {
										messageInfoSet(tempMsg, tempReceiverMsg, user,
												tempUser, now, site, request);
									}
								} else {
									messageInfoSet(tempMsg, tempReceiverMsg, user,
											tempUser, now, site, request);
								}
							}
						}
					}
				}
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 保存草稿
	 * @param bean
	 * @param username
	 * @param pageNo
	 * @param title
	 * @param sendBeginTime
	 * @param sendEndTime
	 * @param status
	 * @param box
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/message/save")
	public void save(CmsMessage bean, String username,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,username, bean.getMsgTitle());
		if (!errors.hasErrors()) {
			CmsSite site = CmsUtils.getSite(request);
			CmsUser user = CmsUtils.getUser(request);
			bean.setMsgBox(2);
			bean.setMsgSendUser(user);
			CmsUser msgReceiverUser = userMng.findByUsername(username);
			if (msgReceiverUser==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.setMsgReceiverUser(msgReceiverUser);
				bean.setMsgStatus(false);
				// 作为草稿和发件箱的区别
				bean.setSendTime(null);
//				bean.setSendTime(new Date());
				bean.setSite(site);
				messageMng.save(bean);
				CmsReceiverMessage receiverMessage = new CmsReceiverMessage(bean);
				receiverMessage.setMsgBox(2);
				receiverMessage.setMessage(bean);
				// 接收端（有一定冗余）
				receiverMessageMng.save(receiverMessage);
				cmsLogMng.operating(request, "cmsMessage.log.save", "id="
						+ bean.getId() + ";title=" + bean.getMsgTitle());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 选择发送
	 * @param id
	 * @param pageNo
	 * @param title
	 * @param sendBeginTime
	 * @param sendEndTime
	 * @param status
	 * @param box
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/message/tosend")
	public void toSend(Integer id,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			CmsMessage bean = messageMng.findById(id);
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.setMsgBox(1);
				bean.setSendTime(new Date());
				messageMng.update(bean);
				Set<CmsReceiverMessage> receiverMessageSet = bean.getReceiverMsgs();
				Iterator<CmsReceiverMessage> it = receiverMessageSet.iterator();
				CmsReceiverMessage receiverMessage;
				while (it.hasNext()) {
					receiverMessage = it.next();
					receiverMessage.setMsgBox(0);
					receiverMessage.setSendTime(new Date());
					receiverMessage.setMessage(bean);
					// 接收端（有一定冗余）
					receiverMessageMng.update(receiverMessage);
				}
				log.info("member CmsMessage send CmsMessage success. id={}", bean
						.getId());
				cmsLogMng.operating(request, "cmsMessage.log.send", "id="
						+ bean.getId() + ";title=" + bean.getMsgTitle());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改消息接口
	 * @param bean
	 * @param response
	 * @param request
	 */
	@SignValidate
	@RequestMapping("/message/update")
	public void update(CmsMessage bean,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getMsgTitle());
		if (!errors.hasErrors()) {
			bean = messageMng.update(bean);
			// 更新发送表的信息，收件表的信息同步更新
			Set<CmsReceiverMessage> receiverMessageSet = bean.getReceiverMsgs();
			Iterator<CmsReceiverMessage> it = receiverMessageSet.iterator();
			CmsReceiverMessage receiverMessage;
			while (it.hasNext()) {
				receiverMessage = it.next();
				receiverMessage.setMsgContent(bean.getContentHtml());
				receiverMessage.setMsgReceiverUser(bean.getMsgReceiverUser());
				receiverMessage.setMsgTitle(bean.getMsgTitle());
				receiverMessage.setMessage(bean);
				// 接收端（有一定冗余）
				receiverMessageMng.update(receiverMessage);
			}
			log.info("member CmsMessage update CmsMessage success. id={}", bean
					.getId());
			cmsLogMng.operating(request, "cmsMessage.log.update", "id="
					+ bean.getId() + ";title=" + bean.getMsgTitle());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 阅读消息
	 * @param id
	 * @param box
	 * @param request
	 * @param response
	 */
	@RequestMapping("/message/read")
	public void read(Integer id,Integer msgBox,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id,msgBox);
		if (!errors.hasErrors()) {
			CmsUser user = CmsUtils.getUser(request);
			if (msgBox.equals(1)) {
				CmsMessage msg = messageMng.findById(id);
				if (msg==null) {
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}else{
					body = msg.convertToJson().toString();
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				CmsReceiverMessage receiverMessage = receiverMessageMng.findById(id);
				if (receiverMessage!=null) {
					if (receiverMessage.getMsgReceiverUser()!=null&&
							receiverMessage.getMsgReceiverUser().equals(user)) {
						receiverMessage.setMsgStatus(true);
						receiverMessageMng.update(receiverMessage);
					}
					body = receiverMessage.convertToJson().toString();
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 任意消息详情
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping("/message/forward")
	public void forward(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			CmsReceiverMessage bean = receiverMessageMng.findById(id);
			CmsMessage msg =null;
			if (bean!=null) {
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				msg = messageMng.findById(id);
				if (msg!=null) {
					body = msg.convertToJson().toString();
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 回复消息详情
	 * @param id
	 * @param response
	 * @param request
	 */
	@RequestMapping("/message/reply")
	public void reply(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			CmsReceiverMessage bean = receiverMessageMng.findById(id);
			if (bean!=null) {
				body = bean.convertToJson().toString();
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
	 * 垃圾箱
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/message/trash")
	public void trash(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			CmsUser user = CmsUtils.getUser(request);
			Integer[] idArr = StrUtils.getInts(ids);
			CmsReceiverMessage receiverMessage;
			CmsMessage msg;
			for (Integer i = 0; i < idArr.length; i++) {
				msg = messageMng.findById(idArr[i]);
				receiverMessage = receiverMessageMng.findById(idArr[i]);
				if (msg != null && msg.getMsgSendUser().equals(user)) {
					// 清空发信表的同时复制该数据到收信表（收信人未空）
					receiverMessage=new CmsReceiverMessage();
					receiverMessage.setMsgBox(3);
					receiverMessage.setMsgContent(msg.getMsgContent());
					receiverMessage.setMsgSendUser(msg.getMsgSendUser());
					receiverMessage.setMsgReceiverUser(msg.getMsgReceiverUser());
					receiverMessage.setMsgStatus(msg.getMsgStatus());
					receiverMessage.setMsgTitle(msg.getMsgTitle());
					receiverMessage.setSendTime(msg.getSendTime());
					receiverMessage.setSite(msg.getSite());
					receiverMessage.setMessage(msg);
					// 接收端（有一定冗余）
					receiverMessageMng.save(receiverMessage);
					/*
					// 清空该发件对应的收件关联关系
					Set<CmsReceiverMessage> receiverMessages = message
							.getReceiverMsgs();
					CmsReceiverMessage tempReceiverMessage;
					if (receiverMessages != null && receiverMessages.size() > 0) {
						it = receiverMessages.iterator();
						while (it.hasNext()) {
							tempReceiverMessage= it.next();
							tempReceiverMessage.setMessage(null);
							receiverMessageMng.update(tempReceiverMessage);
						}
					}
					*/
					msg.setMsgBox(3);
					messageMng.update(msg);
					//messageMng.deleteById(ids[i]);
					cmsLogMng.operating(request, "cmsMessage.log.trash", "id="
							+ msg.getId() + ";title="
							+ msg.getMsgTitle());
				}
				if (receiverMessage != null
						&& receiverMessage.getMsgReceiverUser().equals(user)) {
					receiverMessage.setMsgBox(3);
					receiverMessageMng.update(receiverMessage);
					cmsLogMng.operating(request, "cmsMessage.log.trash", "id="
							+ receiverMessage.getId() + ";title="
							+ receiverMessage.getMsgTitle());
				}
				log.info("member CmsMessage trash CmsMessage success. id={}",
						idArr[i]);
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 移除消息
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/message/revert")
	public void revert(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			CmsUser user = CmsUtils.getUser(request);
			Integer[] idArr = StrUtils.getInts(ids);
			CmsReceiverMessage receiverMessage;
			CmsMessage bean;
			for (Integer i = 0; i < idArr.length; i++) {
				receiverMessage = receiverMessageMng.findById(idArr[i]);
				// 收件箱
				if (receiverMessage != null
						&& receiverMessage.getMsgReceiverUser().equals(user)) {
					receiverMessage.setMsgBox(0);
					receiverMessageMng.update(receiverMessage);
					cmsLogMng.operating(request, "cmsMessage.log.revert", "id="
							+ receiverMessage.getId() + ";title=" + receiverMessage.getMsgTitle());
				}
				//发件箱移除过来
				if (receiverMessage != null
						&& receiverMessage.getMsgSendUser().equals(user)){
					bean=receiverMessage.getMessage();
					bean.setMsgBox(1);
					messageMng.update(bean);
					receiverMessageMng.deleteById(receiverMessage.getId());
				}
				log.info("member CmsMessage revert CmsMessage success. id={}",
						idArr[i]);
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/message/empty")
	public void empty(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			CmsReceiverMessage receiverMessage;
			Integer[] idArr = StrUtils.getInts(ids);
			for (Integer i = 0; i < idArr.length; i++) {
				// 清空收到的站内信
				receiverMessage = receiverMessageMng.findById(idArr[i]);
				if (receiverMessage != null) {
					receiverMessageMng.deleteById(idArr[i]);
				}
				CmsMessage msg=receiverMessage.getMessage();
				if(msg!=null&&msg.getReceiverMsgs().size()<=0){
					messageMng.deleteById(msg.getId());
				}
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/message/unread")
	public void findUnread(HttpServletRequest request,HttpServletResponse response){
		CmsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		List<CmsReceiverMessage> list = receiverMessageMng
				.getList(site.getId(), null, user.getId(), null, null,
						null, false, 0, false,0,500);
		int count = 0;
		if (list!=null&&list.size()>0) {
			count = list.size();
		}
		String body = "{\"count\":"+count+"}";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private void messageInfoSet(CmsMessage message,
			CmsReceiverMessage receiverMessage, CmsUser sendUser,
			CmsUser receiverUser, Date sendTime, CmsSite site,
			HttpServletRequest request) {
		message.setMsgBox(1);
		message.setMsgSendUser(sendUser);
		message.setMsgReceiverUser(receiverUser);
		message.setMsgStatus(false);
		message.setSendTime(sendTime);
		message.setSite(site);
		messageMng.save(message);
		receiverMessage.setMsgBox(0);
		receiverMessage.setMsgContent(message.getMsgContent());
		receiverMessage.setMsgSendUser(sendUser);
		receiverMessage.setMsgReceiverUser(receiverUser);
		receiverMessage.setMsgStatus(false);
		receiverMessage.setMsgTitle(message.getMsgTitle());
		receiverMessage.setSendTime(sendTime);
		receiverMessage.setSite(site);
		receiverMessage.setMessage(message);
		// 接收端（有一定冗余）
		receiverMessageMng.save(receiverMessage);
		log.info("member CmsMessage send CmsMessage success. id={}", message
				.getId());
		cmsLogMng.operating(request, "cmsMessage.log.send", "id="
				+ message.getId() + ";title=" + message.getMsgTitle());
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsMessageMng messageMng;
	@Autowired
	private CmsReceiverMessageMng receiverMessageMng;
	@Autowired
	private CmsUserMng userMng;
}
