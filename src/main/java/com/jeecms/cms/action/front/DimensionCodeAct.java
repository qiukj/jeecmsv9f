package com.jeecms.cms.action.front;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.zxing.BarcodeFormat;
import com.jeecms.common.util.ZXingCode;

/**
 * 二维码Action
 */
@Controller
public class DimensionCodeAct {

	@RequestMapping("/special/o_create_dimensioncode.jspx")
	public void createCodeImg(String content,
			Integer fontSize,Integer size, 
			HttpServletRequest request,
			HttpServletResponse response) {
		if(StringUtils.isNotBlank(content)){
				if(size==null){
					size=100;
				}
				if(fontSize==null){
					fontSize=10;
				} 
				response.setContentType("image/png"); 
				try {

					ZXingCode zp =  ZXingCode.getInstance();
					BufferedImage bim = zp.getQRCODEBufferedImage(content, BarcodeFormat.QR_CODE, size, size,
							zp.getDecodeHintType());
					ImageIO.write(bim, "png", response.getOutputStream());
				} catch (Exception e) {
					//e.printStackTrace();
				}
		}
	}
}
