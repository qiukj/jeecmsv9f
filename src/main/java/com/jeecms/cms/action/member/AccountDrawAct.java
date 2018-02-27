package com.jeecms.cms.action.member;

import static com.jeecms.cms.Constants.TPLDIR_MEMBER;
import static com.jeecms.common.page.SimplePage.cpn;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.entity.assist.CmsAccountDraw;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.manager.assist.CmsAccountDrawMng;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;

/**
 * 账户提现
 */
@Controller
public class AccountDrawAct {
	private static final Logger log = LoggerFactory.getLogger(AccountDrawAct.class);

	public static final String MEMBER_ACCOUNT_DRAW = "tpl.memberAccountDraw";
	public static final String MEMBER_ACCOUNT_DRAW_LIST = "tpl.memberAccountDrawList";

	@RequestMapping(value = "/member/draw_list.jspx")
	public String drawList(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if(user.getUserAccount()==null){
			WebErrors errors=WebErrors.create(request);
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		Pagination pagination=accountDrawMng.getPage(user.getId(),null,null,null,
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("userAccount",user.getUserAccount());
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ACCOUNT_DRAW_LIST);
	}
	
	@RequestMapping(value = "/member/draw_del.jspx")
	public String drawDel(Integer[] ids,Integer pageNo,
			String nextUrl,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		WebErrors errors = validateDelete(ids, site, user, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		accountDrawMng.deleteByIds(ids);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	
	/**
	 * 提现申请输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/draw.jspx", method = RequestMethod.GET)
	public String drawInput(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		Double appliedSum=accountDrawMng.getAppliedSum(user.getId());
		model.addAttribute("userAccount", user.getUserAccount());
		model.addAttribute("minDrawAmount",config.getMinDrawAmount());
		if(user.getUserAccount()!=null){
			model.addAttribute("maxDrawAmount",user.getUserAccount().getContentNoPayAmount()-appliedSum);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ACCOUNT_DRAW);
	}
	

	/**
	 * 提现申请提交页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/draw.jspx", method = RequestMethod.POST)
	public String drawSubmit(Double drawAmout,String applyAcount,
			String nextUrl,HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		WebErrors errors=WebErrors.create(request);
		if(user.getUserAccount()==null){
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		if(drawAmout!=null){
			CmsConfigContentCharge config=configContentChargeMng.getDefault();
			if(drawAmout>user.getUserAccount().getContentNoPayAmount()){
				errors.addErrorCode("error.userAccount.balanceNotEnough");
			}
			if(drawAmout<config.getMinDrawAmount()){
				errors.addErrorCode("error.userAccount.drawLessMinAmount",config.getMinDrawAmount());
			}
			if(errors.hasErrors()){
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		accountDrawMng.draw(user, drawAmout, applyAcount);
		log.info("update CmsUserExt success. id={}", user.getId());
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	
	private WebErrors validateDelete(Integer[] ids, CmsSite site, CmsUser user,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldOpt(errors, site, user, ids)) {
			return errors;
		}
		return errors;
	}
	
	private boolean vldOpt(WebErrors errors, CmsSite site, CmsUser user,
			Integer[] ids) {
		for (Integer id : ids) {
			if (errors.ifNull(id, "id", true)) {
				return true;
			}
			CmsAccountDraw d = accountDrawMng.findById(id);
			// 数据不存在
			if (errors.ifNotExist(d, CmsAccountDraw.class, id, true)) {
				return true;
			}
			// 非本用户数据
			if (!d.getDrawUser().getId().equals(user.getId())) {
				errors.noPermission(CmsAccountDraw.class, id, true);
				return true;
			}
			// 提现申请状态是申请成功待支付和提现成功
			if (d.getApplyStatus()==CmsAccountDraw.CHECKED_SUCC
					||d.getApplyStatus()==CmsAccountDraw.DRAW_SUCC) {
				errors.addErrorCode("error.account.draw.hasChecked");
				return true;
			}
		}
		return false;
	}
	
	@Autowired
	private CmsAccountDrawMng accountDrawMng;
	@Autowired
	private CmsConfigContentChargeMng configContentChargeMng;
}
