package com.jeecms.cms.api.member;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.common.image.ImageScale;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsOss;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.MarkConfig;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.DbFileMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

import ch.qos.logback.core.net.SyslogOutputStream;
import net.sf.json.JSONObject;

@Controller
public class UploadApiAct {
	private static final Logger log = LoggerFactory.getLogger(UploadApiAct.class);
	
	/**
	 * 文件上传API
	 * @param siteId 站点id 非必选 默认当前站
	 * @param mark 水印 true有 false 无  非必选 默认则系统默认配置
	 * @param type 上传类型  图片image 视频vedio  附件attach 必选
	 * @param file 文件 必选
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 */
	@SignValidate
	@RequestMapping("/upload/o_upload")
	public void upload(
			Integer siteId,Boolean mark,String type,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			String uploadPath,HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(StringUtils.isBlank(type)){
			type="image";
		}
		WebErrors errors = validate(type,file, request,uploadPath);
		if (errors.hasErrors()) {
			message=errors.getErrors().get(0);
			log.info(message);
			code = ResponseCode.API_CODE_UPLOAD_ERROR;
		}else{
			CmsSite site=CmsUtils.getSite(request);
			String filename="";
			if(StringUtils.isNotBlank(uploadPath)){
				filename=uploadPath;
				if(!uploadPath.endsWith("/")){
					filename+="/";
				}
				filename+=file.getOriginalFilename();
			}
			if(siteId!=null){
				site=siteMng.findById(siteId);
			}
			MarkConfig conf = site.getConfig().getMarkConfig();
			if (mark == null) {
				mark = conf.getOn();
			}
			String origName = file.getOriginalFilename();
			Long fileSize = file.getSize()/1024;//单位KB
			String ext = FilenameUtils.getExtension(origName).toLowerCase(
					Locale.ENGLISH);
			try {
				String fileUrl;
				if (site.getConfig().getUploadToDb()) {
					String dbFilePath = site.getConfig().getDbFileUri();
					if (mark&&type.equals("image")) {
						File tempFile = mark(file, conf);
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = dbFileMng.storeByFilename(filename, new FileInputStream(tempFile));
						}else{
							fileUrl = dbFileMng.storeByExt(site.getUploadPath(),
									ext, new FileInputStream(tempFile));
						}
						tempFile.delete();
					} else {
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = dbFileMng.storeByFilename(filename,file.getInputStream());
						}else{
							fileUrl = dbFileMng.storeByExt(site.getUploadPath(),
									ext, file.getInputStream());
						}
					}
					// 加上访问地址
					fileUrl = request.getContextPath() + dbFilePath + fileUrl;
				} else if (site.getUploadFtp() != null) {
					Ftp ftp = site.getUploadFtp();
					String ftpUrl = ftp.getUrl();
					if (mark&&type.equals("image")) {
						File tempFile = mark(file, conf);
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = ftp.storeByFilename(filename, new FileInputStream(tempFile));
						}else{
							fileUrl = ftp.storeByExt(site.getUploadPath(), ext,
									new FileInputStream(tempFile));
						}
						tempFile.delete();
					} else {
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = ftp.storeByFilename(filename,file.getInputStream());
						}else{
							fileUrl = ftp.storeByExt(site.getUploadPath(), ext, file
									.getInputStream());
						}
					}
					// 加上url前缀
					fileUrl = ftpUrl + fileUrl;
				}else if (site.getUploadOss() != null) {
					CmsOss oss = site.getUploadOss();
					if (mark&&type.equals("image")) {
						File tempFile = mark(file, conf);
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl=oss.storeByFilename(filename, new FileInputStream(tempFile));
						}else{
							fileUrl=oss.storeByExt(site.getUploadPath(), ext, new FileInputStream(tempFile));
						}
						tempFile.delete();
					} else {
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = oss.storeByFilename(filename, file.getInputStream());
						}else{
							fileUrl = oss.storeByExt(site.getUploadPath(), ext, file.getInputStream());
						}
					}
				} else {
					if (mark&&type.equals("image")) {
						File tempFile = mark(file, conf);
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = fileRepository.storeByFilename(filename, tempFile);
						}else{
							fileUrl = fileRepository.storeByExt(site.getUploadPath(), ext,
									tempFile);
						}
						tempFile.delete();
					} else {
						if(StringUtils.isNotBlank(uploadPath)){
							fileUrl = fileRepository.storeByFilename(filename,file);
						}else{
							fileUrl = fileRepository.storeByExt(site.getUploadPath(), ext,
									file);
						}
					}
					String ctx = request.getContextPath();
					if(StringUtils.isNotBlank(ctx)){
						fileUrl = ctx + fileUrl;
					}
				}
				JSONObject json = new JSONObject();
				json.put("uploadPath", fileUrl);
				json.put("fileName", origName);
				json.put("fileSize", fileSize);
				body=json.toString();
				message=Constants.API_MESSAGE_SUCCESS;
			}catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				message=Constants.API_MESSAGE_UPLOAD_ERROR;
				code=ResponseCode.API_CODE_UPLOAD_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 文库文档上传API
	 * @param siteId 站点id 非必选 默认当前站
	 * @param file 文件 必选
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 */
	@SignValidate
	@RequestMapping("/upload/o_upload_doc")
	public void uploadDoc(
			Integer siteId,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors = validateDoc(file, request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors);
		if (errors.hasErrors()) {
			message=errors.getErrors().get(0);
			code = ResponseCode.API_CODE_UPLOAD_ERROR;
		}else{
			CmsSite site = CmsUtils.getSite(request);
			if(siteId!=null){
				site=siteMng.findById(siteId);
			}
			String origName = file.getOriginalFilename();
			String ext = FilenameUtils.getExtension(origName).toLowerCase(
					Locale.ENGLISH);
			try {
				String fileUrl;
				fileUrl = fileRepository.storeByExt(site.getLibraryPath(), ext,
						file);
				body="{\"uploadPath\":"+"\""+fileUrl+"\"}";
				message=Constants.API_MESSAGE_SUCCESS;
			}catch (Exception e) {
				e.printStackTrace();
				message=Constants.API_MESSAGE_UPLOAD_ERROR;
				code = ResponseCode.API_CODE_UPLOAD_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validate(String type,MultipartFile file,
			HttpServletRequest request,String uploadPath) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		CmsUser user=CmsUtils.getUser(request);
		if (file == null) {
			errors.addErrorString("imageupload_error_noFileToUpload");
			return errors;
		}
		String filename=file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(filename);
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			errors.addErrorString("upload_error_filename");
			return errors;
		}
		if(StringUtils.isNotBlank(uploadPath)){
			if(!uploadPath.startsWith(site.getUploadPath())||uploadPath.contains("../")||
					uploadPath.contains("..\\")||
					uploadPath.contains("..\\")||uploadPath.contains("../")){
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			}
		}
		if(type.equals("image")){
			//图片校验
			if (!ImageUtils.isValidImageExt(ext)) {
				errors.addErrorString("imageupload_error_notSupportExt");
				return errors;
			}
			try {
				if (!ImageUtils.isImage(file.getInputStream())) {
					errors.addErrorString("imageupload_error_notImage");
					return errors;
				}
			} catch (IOException e) {
				errors.addErrorString("imageupload_error_ioError");
				return errors;
			}
		}
		int fileSize = (int) (file.getSize() / 1024);
		//非允许的后缀
		if(!user.isAllowSuffix(ext)){
			errors.addErrorString("upload.error.invalidsuffix");
			return errors;
		}
		//超过附件大小限制
		if(!user.isAllowMaxFile((int)(file.getSize()/1024))){
			errors.addErrorString("upload.error.toolarge");
			return errors;
		}
		//超过每日上传限制
		if (!user.isAllowPerDay(fileSize)) {
			long laveSize=user.getGroup().getAllowPerDay()-user.getUploadSize();
			if(laveSize<0){
				laveSize=0;
			}
			errors.addErrorString("upload.error.dailylimit");
		}
		return errors;
	}
	
	private WebErrors validateDoc(MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorString("imageupload_error_noFileToUpload");
			return errors;
		}
		String filename=file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(filename);
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			errors.addErrorString("upload_error_filename");
			return errors;
		}
		CmsGroup defGroup=groupMng.getRegDef();
		//非允许的后缀
		if(!defGroup.isAllowSuffix(ext)){
			errors.addErrorString("upload_error_invalidsuffix");
			return errors;
		}
		return errors;
	}

	private File mark(MultipartFile file, MarkConfig conf) throws Exception {
		String path = System.getProperty("java.io.tmpdir");
		File tempFile = new File(path, String.valueOf(System
				.currentTimeMillis()));
		file.transferTo(tempFile);
		boolean imgMark = !StringUtils.isBlank(conf.getImagePath());
		if (imgMark) {
			File markImg = new File(realPathResolver.get(conf.getImagePath()));
			imageScale.imageMark(tempFile, tempFile, conf.getMinWidth(), conf
					.getMinHeight(), conf.getPos(), conf.getOffsetX(), conf
					.getOffsetY(), markImg);
		} else {
			imageScale.imageMark(tempFile, tempFile, conf.getMinWidth(), conf
					.getMinHeight(), conf.getPos(), conf.getOffsetX(), conf
					.getOffsetY(), conf.getContent(), Color.decode(conf
					.getColor()), conf.getSize(), conf.getAlpha());
		}
		return tempFile;
	}

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private DbFileMng dbFileMng;
	@Autowired
	private ImageScale imageScale;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsSiteMng siteMng;
	@Autowired
	private CmsGroupMng groupMng;
}

