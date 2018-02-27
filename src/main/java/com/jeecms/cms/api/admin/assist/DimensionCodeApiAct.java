package com.jeecms.cms.api.admin.assist;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.zxing.BarcodeFormat;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.common.util.ZXingCode;
import com.jeecms.common.util.ZxingLogoConfig;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class DimensionCodeApiAct {
	private final static String DEMENSION_CODE_IMG_NAME="/demension.png";
	
	@RequestMapping("/dimensioncode/create")
	public void createImg(String content,String logoPicPath
			,String logoWord,Integer fontSize,Integer size,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		if(size==null){
			size=100;
		}
		if(fontSize==null){
			fontSize=10;
		} 
		String logoPic=null;
		errors = ApiValidate.validateRequiredParams(request, errors, content);
		if (!errors.hasErrors()) {
			if(StringUtils.isNotBlank(logoPicPath)){
				if(StringUtils.isNotBlank(site.getContextPath())
						&&logoPicPath.startsWith(site.getContextPath())){
					logoPicPath=logoPicPath.substring(site.getContextPath().length());
				}
				logoPic=realPathResolver.get(logoPicPath);
			}
			response.setContentType("image/png; charset=utf-8"); 
			try {
				ZXingCode zp = ZXingCode.getInstance();

				BufferedImage bim = zp.getQRCODEBufferedImage(content, BarcodeFormat.QR_CODE, size, size,
						zp.getDecodeHintType());
				if(StringUtils.isNotBlank(logoPicPath)){
					zp.addLogoQRCode(bim, new File(logoPic), new ZxingLogoConfig());
				}else if(StringUtils.isNotBlank(logoWord)){
					zp.addLogoWordQRCode(bim, logoWord, fontSize,new ZxingLogoConfig());
				}
				String tempFileName=DEMENSION_CODE_IMG_NAME;
				File file=new File(realPathResolver.get(tempFileName));
				ImageIO.write(bim, "png", file);
				if(StringUtils.isNotBlank(site.getContextPath())){
					tempFileName=site.getContextPath()+tempFileName;
				}
				body = "{\"url\":\""+tempFileName+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				message = Constants.API_MESSAGE_CREATE_ERROR;
				code = ResponseCode.API_CODE_CALL_FAIL;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private RealPathResolver realPathResolver;
}
