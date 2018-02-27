package com.jeecms.cms.api.member;


import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeecms.cms.ueditor.define.AppInfo;
import com.jeecms.cms.ueditor.define.BaseState;
import com.jeecms.cms.ueditor.define.MultiState;
import com.jeecms.cms.ueditor.PathFormat;
import com.jeecms.cms.service.ImageSvc;
import com.jeecms.cms.ueditor.define.State;
import com.jeecms.cms.ueditor.hunter.ImageHunter;
import com.jeecms.cms.ueditor.upload.StorageManager;
import com.jeecms.common.image.ImageScale;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.ueditor.LocalizedMessages;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.ueditor.Utils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsOss;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.MarkConfig;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.DbFileMng;
import com.jeecms.core.web.util.CmsUtils;

/**
 * ueditor服务器端接口
 * 
 * 为了更好、更灵活的处理ueditor上传，重新实现ueditor服务器端接口。
//@CrossOrigin(origins = "*")
 */
@Controller
public class UeditorApiAct {

	private static final Logger log = LoggerFactory.getLogger(UeditorApiAct.class);

	// 状态
	private static final String STATE = "state";
	// 上传成功
	private static final String SUCCESS = "SUCCESS";
	// URL
	private static final String URL = "url";
	// 文件原名
	private static final String ORIGINAL = "original";
	// TITLE
	private static final String TITLE = "title";
	
	@RequestMapping(value = "/ueditor/upload")
	public void upload(
			@RequestParam(value = "Type", required = false) String typeStr,
			Boolean mark,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		responseInit(response);
		if (Utils.isEmpty(typeStr)) {
			typeStr = "File";
		}
		if(mark==null){
			mark=false;
		}
		JSONObject json = new JSONObject();
		JSONObject ob = validateUpload(request, typeStr);
		if (ob == null) {
			json = doUpload(request, typeStr, mark);
		} else {
			json = ob;
		}
		//ResponseUtils.renderJson(response, json.toString());
		ResponseUtils.renderText(response, json.toString());
	}
	
	@RequestMapping(value = "/ueditor/getRemoteImage")
	public void getRemoteImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] list = request.getParameterValues("source[]");
		CmsSite site=CmsUtils.getSite(request);
		State state = new ImageHunter(imgSvc,CmsUtils.getSite(request)).captureByApi(site.getUrlPrefix(),list);
		/*
		JsonObject json=new JsonObject();
		JSONObject jobj=new JSONObject(state);
		for(String s:jobj.keySet()){
			json.add(s, JsonElement);
		}
		*/
		ResponseUtils.renderJson(response, state.toJSONString());
	}
	
	/**
	 * 在线图片管理（选择最近或站点图片）
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/ueditor/imageManager")
	public void imageManager(Integer picNum,Boolean insite,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		State state=listFile(request, getStartIndex(request));
		ResponseUtils.renderText(response, state.toJSONString());
	}
	

	@RequestMapping(value = "/ueditor/scrawlImage")
	public void scrawlImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		State state = scrawlImage(request.getParameter("upfile"),CmsUtils.getSite(request));;
		ResponseUtils.renderText(response, state.toString());
	}
	

	private JSONObject doUpload(HttpServletRequest request, String typeStr,Boolean mark) throws Exception {
		ResourceType type = ResourceType.getDefaultResourceType(typeStr);
		JSONObject result = new JSONObject();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// We upload just one file at the same time
			MultipartFile uplFile = multipartRequest.getFileMap().entrySet()
					.iterator().next().getValue();
			// Some browsers transfer the entire source path not just the
			// filename
			String filename = FilenameUtils.getName(uplFile
					.getOriginalFilename());
			log.debug("Parameter NewFile: {}", filename);
			String ext = FilenameUtils.getExtension(filename);
			if (type.isDeniedExtension(ext)) {
				result.put(STATE, LocalizedMessages
						.getInvalidFileTypeSpecified(request));
				return result;
			}
			if (type.equals(ResourceType.IMAGE)
					&& !ImageUtils.isImage(uplFile.getInputStream())) {
				result.put(STATE, LocalizedMessages
						.getInvalidFileTypeSpecified(request));
				return result;
			}
			String fileUrl;
			CmsSite site = CmsUtils.getSite(request);
			CmsUser user = CmsUtils.getUser(request);
			MarkConfig conf = site.getConfig().getMarkConfig();
			if (mark == null) {
				mark = conf.getOn();
			}
			boolean isImg = type.equals(ResourceType.IMAGE);
			if (site.getConfig().getUploadToDb()) {
				if (mark && isImg) {
					File tempFile = mark(uplFile, conf);
					fileUrl = dbFileMng.storeByExt(site.getUploadPath(), ext,
							new FileInputStream(tempFile));
					tempFile.delete();
				} else {
					fileUrl = dbFileMng.storeByExt(site.getUploadPath(), ext,
							uplFile.getInputStream());
				}
				// 加上访问地址
				String dbFilePath = site.getConfig().getDbFileUri();
				fileUrl = request.getContextPath() + dbFilePath + fileUrl;
			} else if (site.getUploadFtp() != null) {
				Ftp ftp = site.getUploadFtp();
				if (mark && isImg) {
					File tempFile = mark(uplFile, conf);
					fileUrl = ftp.storeByExt(site.getUploadPath(), ext,
							new FileInputStream(tempFile));
					tempFile.delete();
				} else {
					fileUrl = ftp.storeByExt(site.getUploadPath(), ext, uplFile
							.getInputStream());
				}
				// 加上url前缀
				fileUrl = ftp.getUrl() + fileUrl;
			}else if (site.getUploadOss() != null) {
				CmsOss oss = site.getUploadOss();
				if (mark && isImg) {
					File tempFile = mark(uplFile, conf);
					fileUrl=oss.storeByExt(site.getUploadPath(), ext, new FileInputStream(tempFile));
					tempFile.delete();
				} else {
					fileUrl = oss.storeByExt(site.getUploadPath(), ext, uplFile.getInputStream());
				}
			} else {
				if (mark && isImg) {
					File tempFile = mark(uplFile, conf);
					fileUrl = fileRepository.storeByExt(site.getUploadPath(),
							ext, tempFile);
					tempFile.delete();
				} else {
					fileUrl = fileRepository.storeByExt(site.getUploadPath(),
							ext, uplFile);
				}
				// 加上部署路径和域名（前后台分离部署需要）
				fileUrl = site.getUrlPrefix()+request.getContextPath() + fileUrl;
			}
			cmsUserMng.updateUploadSize(user.getId(), Integer.parseInt(String
					.valueOf(uplFile.getSize() / 1024)));
			//需要给页面参数(参考ueditor的/jsp/imageUp.jsp)
			result.put(URL, fileUrl);
			result.put(ORIGINAL, filename);
			result.put("type", ext);
			//result.put("type", "." + ext);
			result.put(STATE, SUCCESS);
			result.put(TITLE, filename);
			//result.put(FILETYPE, "." + ext);
			return result;
		} catch (IOException e) {
			result.put(STATE, LocalizedMessages
					.getFileUploadWriteError(request));
			return result;
		}
	}

	public State listFile (HttpServletRequest request,int index ) {
		CmsSite site=CmsUtils.getSite(request);
		String uploadPath=site.getUploadPath();
		File dir = new File(realPathResolver.get(uploadPath));
		State state = null;
		if ( !dir.exists() ) {
			return new BaseState( false, AppInfo.NOT_EXIST );
		}
		
		if ( !dir.isDirectory() ) {
			return new BaseState( false, AppInfo.NOT_DIRECTORY );
		}
		
		Collection<File> list = FileUtils.listFiles( dir, null, true );
		
		if ( index < 0 || index > list.size() ) {
			state = new MultiState( true );
		} else {
			Object[] fileList = Arrays.copyOfRange( list.toArray(), index, index + 20);
			state = getState(realPathResolver.get("/"),site.getContextPath(),fileList );
		}
		
		state.putInfo( "start", index );
		state.putInfo( "total", list.size() );
		
		return state;
		
	}
	
	public int getStartIndex (HttpServletRequest request) {
		String start = request.getParameter( "start" );
		try {
			return Integer.parseInt( start );
		} catch ( Exception e ) {
			return 0;
		}
	}
	
	private State getState ( String rootPath,String contextPath,Object[] files ) {
		
		MultiState state = new MultiState( true );
		BaseState fileState = null;
		
		File file = null;
		
		for ( Object obj : files ) {
			if ( obj == null ) {
				break;
			}
			file = (File)obj;
			fileState = new BaseState( true );
			fileState.putInfo( "url", PathFormat.format( getPath( rootPath,contextPath,file ) ) );
			state.addState( fileState );
		}
		return state;
	}
	
	private String getPath ( String rootPath ,String contextPath,File file) {
		String path = file.getAbsolutePath();
		if(StringUtils.isNotBlank(contextPath)){
			return contextPath+path.replace( rootPath, "/" );
		}else{
			return path.replace( rootPath, "/" );
		}
		
	}

	private void responseInit(HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		//response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
	}

	private JSONObject validateUpload(HttpServletRequest request, String typeStr)
			throws JSONException {
		// TODO 是否允许上传
		JSONObject result = new JSONObject();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile uplFile = multipartRequest.getFileMap().entrySet()
				.iterator().next().getValue();
		String filename = FilenameUtils.getName(uplFile.getOriginalFilename());
		int fileSize = (int) (uplFile.getSize() / 1024);
		String ext = FilenameUtils.getExtension(filename).toLowerCase(
				Locale.ENGLISH);
		CmsUser user = CmsUtils.getUser(request);
		// 非允许的后缀
		if (!user.isAllowSuffix(ext)) {
			result.put(STATE, LocalizedMessages
					.getInvalidFileSuffixSpecified(request));
			return result;
		}
		// 超过附件大小限制
		if (!user.isAllowMaxFile((int) (uplFile.getSize() / 1024))) {
			result.put(STATE, LocalizedMessages.getInvalidFileToLargeSpecified(
					request, filename, user.getGroup().getAllowMaxFile()));
			return result;
		}
		// 超过每日上传限制
		if (!user.isAllowPerDay(fileSize)) {
			long laveSize = user.getGroup().getAllowPerDay()
					- user.getUploadSize();
			if (laveSize < 0) {
				laveSize = 0;
			}
			result.put(STATE, LocalizedMessages
					.getInvalidUploadDailyLimitSpecified(request, String
							.valueOf(laveSize)));
			return result;
		}
		if (!ResourceType.isValidType(typeStr)) {
			result.put(STATE, LocalizedMessages
					.getInvalidResouceTypeSpecified(request));
			return result;
		}
		return null;
	}
	
	private File mark(File file, MarkConfig conf) throws Exception {
		String path = System.getProperty("java.io.tmpdir");
		File tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
		FileUtils.copyFile(file, tempFile);
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
	
	public  State scrawlImage(String content,CmsSite site) {
		
		byte[] data = decode(content);
		
		String suffix = "jpg";
		
		String savePath = site.getContextPath()+site.getUploadPath()+"/temp.jpg";
		
		String physicalPath = (String) realPathResolver.get(savePath);

		State storageState = StorageManager.saveBinaryFile(data, physicalPath);
		
		File file=new File(physicalPath);
		FileInputStream fileInputStream;

		String fileUrl="";
		try {
			fileInputStream = new FileInputStream(file);
			
			Boolean mark = null;
			MarkConfig conf = site.getConfig().getMarkConfig();
			if (mark == null) {
				mark = conf.getOn();
			}
		
			if (site.getConfig().getUploadToDb()) {
				if (mark) {
					File tempFile = mark(file, conf);
					fileUrl = dbFileMng.storeByExt(site.getUploadPath(), suffix,
							new FileInputStream(tempFile));
					tempFile.delete();
				} else {
					fileUrl = dbFileMng.storeByExt(site.getUploadPath(), suffix,
							fileInputStream);
				}
				// 加上访问地址
				String dbFilePath = site.getConfig().getDbFileUri();
				fileUrl = site.getContextPath() + dbFilePath + fileUrl;
			} else if (site.getUploadFtp() != null) {
				Ftp ftp = site.getUploadFtp();
				if (mark) {
					File tempFile = mark(file, conf);
					fileUrl = ftp.storeByExt(site.getUploadPath(), suffix,new FileInputStream(tempFile));
					tempFile.delete();
				} else {
					fileUrl = ftp.storeByExt(site.getUploadPath(), suffix, fileInputStream);
				}
				// 加上url前缀
				fileUrl = ftp.getUrl() + fileUrl;
			}else if (site.getUploadOss() != null) {
				CmsOss oss = site.getUploadOss();
				if (mark) {
					File tempFile = mark(file, conf);
					fileUrl=oss.storeByExt(site.getUploadPath(), suffix, new FileInputStream(tempFile));
					tempFile.delete();
				} else {
					fileUrl = oss.storeByExt(site.getUploadPath(), suffix, fileInputStream);
				}
			} else {
				if (mark) {
					File tempFile = mark(file, conf);
					fileUrl = fileRepository.storeByExt(site.getUploadPath(),
							suffix, tempFile);
					tempFile.delete();
				} else {
					fileUrl = fileRepository.storeByExt(site.getUploadPath(),
							suffix, file);
				}
				// 加上部署路径
				fileUrl = site.getContextPath() + fileUrl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (storageState.isSuccess()) {
			storageState.putInfo("url", fileUrl);
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}
	
	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private FileRepository fileRepository;
	private DbFileMng dbFileMng;
	private ImageScale imageScale;
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private ImageSvc imgSvc;

	@Autowired
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	@Autowired
	public void setDbFileMng(DbFileMng dbFileMng) {
		this.dbFileMng = dbFileMng;
	}

	@Autowired
	public void setImageScale(ImageScale imageScale) {
		this.imageScale = imageScale;
	}

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
	}

}
