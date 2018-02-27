package com.jeecms.cms.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.service.ContentListenerAbstract;
import com.jeecms.common.util.ZXingCode;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfigAttr;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.CmsConfigMng;

@Component
public class ContentCodeListener extends ContentListenerAbstract {
	/**
	 * 是否已审核
	 */
	private static final String IS_CHECKED = "isChecked";

	@Override
	public void afterSave(Content content) {
		if (content.isChecked()) {
			createCodeImg(content);
		}
	}

	@Override
	public Map<String, Object> preChange(Content content) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IS_CHECKED, content.isChecked());
		return map;
	}

	@Override
	public void afterChange(Content content, Map<String, Object> map) {
		boolean curr = content.isChecked();
		boolean pre = (Boolean) map.get(IS_CHECKED);
		if (pre && !curr) {
			delCodeImg(content);
		} else if (!pre && curr) {
			createCodeImg(content);
		} else if (pre && curr) {
			createCodeImg(content);
		}
	}

	@Override
	public void afterDelete(Content content) {
		delCodeImg(content);
	}
	
	private void createCodeImg(Content content){
		ZXingCode zp =  ZXingCode.getInstance();
		//尺寸暂定100*100,生成的地址是手机url(带域名端口)
		CmsConfigAttr attr=cmsConfigMng.get().getConfigAttr();
		Integer codeImgWidth=attr.getCodeImgWidth();
		Integer codeImgHeight=attr.getCodeImgHeight();
		BufferedImage bim = zp.getQRCODEBufferedImage(content.getMobileUrlWhole(), BarcodeFormat.QR_CODE, codeImgWidth, codeImgHeight,
				zp.getDecodeHintType());
		File file=new File(realPathResolver.get(content.getCodeImg()));
		File parent=new File(file.getParent());
		if(!parent.exists()){
			parent.mkdirs();
		}
		if(file.exists()){
			file.delete();
		}
		try {
			ImageIO.write(bim, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CmsSite site=content.getSite();
		//配置了ftp需要上传二维码图片，方便集群下处理图片
		if (site.getUploadFtp() != null) {
			Ftp ftp = site.getUploadFtp();
			try {
				ftp.storeByFilename(content.getCodeImg(), new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void delCodeImg(Content content){
		File file=new File(realPathResolver.get(content.getCodeImg()));
		if(file.exists()){
			file.delete();
		}
	}
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsConfigMng cmsConfigMng;
}
