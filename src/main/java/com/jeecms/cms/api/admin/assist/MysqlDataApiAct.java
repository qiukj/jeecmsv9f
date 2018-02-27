package com.jeecms.cms.api.admin.assist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import com.jeecms.cms.entity.back.CmsField;
import com.jeecms.cms.manager.assist.CmsMysqlDataBackMng;
import com.jeecms.cms.manager.assist.CmsResourceMng;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.util.Zipper.FileEntry;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class MysqlDataApiAct {
	private static String SUFFIX = "sql";
	private static String SPLIT = "`";
	private static String BR = "\r\n";
	private static String SLASH="/";
	private static String SPACE = " ";
	private static String BRANCH = ";";
	private static String INSERT_INTO = " INSERT INTO ";
	private static String VALUES = "VALUES";
	private static String LEFTBRACE = "(";
	private static String RIGHTBRACE = ")";
	private static String QUOTES = "'";
	private static String COMMA = ",";
	private static String DISABLEFOREIGN = "SET FOREIGN_KEY_CHECKS = 0;\r\n";
	private static String ABLEFOREIGN = "SET FOREIGN_KEY_CHECKS = 1;\r\n";
	private static String dbXmlFileName = "/WEB-INF/config/jdbc.properties";
	private static String backup_table;
	private static final String INVALID_PARAM = "template.invalidParams";
	private static final Logger log = LoggerFactory.getLogger(CmsResourceApiAct.class);
	
	@RequestMapping("/mysql/data/list")
	public void list(HttpServletRequest request, HttpServletResponse response){
		List<String> tables = new ArrayList<String>();
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		try {
			tables = dataBackMng.listTabels(dataBackMng.getDefaultCatalog());
			JSONArray jsonArray = new JSONArray();
			if (tables!=null&&tables.size()>0) {
				for (int i = 0; i < tables.size(); i++) {
					jsonArray.put(i,tables.get(i));
				}
			}
			body = jsonArray.toString();
		} catch (SQLException e) {
			message = Constants.API_MESSAGE_SQL_ERROR;
			code = ResponseCode.API_CODE_SQL_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/mysql/data/listfields")
	public void listfiled(String tablename,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, tablename);
		if (!errors.hasErrors()) {
			List<CmsField> list = dataBackMng.listFields(tablename);
			JSONArray jsonArray = new JSONArray();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
			body = jsonArray.toString();
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/mysql/data/listDataBases")
	public void listDataBases(HttpServletRequest request,HttpServletResponse response){
		List<String> list = dataBackMng.listDataBases();
		JSONArray jsonArray = new JSONArray();
		if (list!=null &&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/mysql/data/defaultCatalog")
	public void listDataBase(HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		try {
			String defaultCatalog = dataBackMng.getDefaultCatalog();
			body = "{\"defaultCatalog\":\""+defaultCatalog+"\"}";
		} catch (SQLException e) {
			message = Constants.API_MESSAGE_SQL_ERROR;
			code = ResponseCode.API_CODE_SQL_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/mysql/data/files")
	public void files(HttpServletRequest request,HttpServletResponse response){
		List<FileWrap> list = resourceMng.listFile(com.jeecms.cms.Constants.BACKUP_PATH, false);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	@SignValidate
	@RequestMapping("/mysql/data/revert")
	public void revert(String filename,String db,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, filename,db);
		if (!errors.hasErrors()) {
			String backpath = realPathResolver.get(com.jeecms.cms.Constants.BACKUP_PATH);
			String backFilePath = backpath + SLASH +filename;
			String sql;
			try {
				sql = readFile(backFilePath);
				//还原暂时没做备份提示。
				dataBackMng.executeSQL("use "+SPLIT+db+SPLIT+BR);
				dataBackMng.executeSQL(sql);
				String defaultCatalog=dataBackMng.getDefaultCatalog();
				if(!defaultCatalog.equals(db)){
					String dbXmlPath = realPathResolver.get(dbXmlFileName);
					dbXml(dbXmlPath, defaultCatalog,db);
				}
				result = true;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}catch (IOException e) {
				message = Constants.API_MESSAGE_FILE_NOT_FOUNT;
				code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
			}catch (Exception e) {
				message = Constants.API_MESSAGE_DB_REVERT_ERROR;
				code = ResponseCode.API_CODE_DB_REVERT_ERROR;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/mysql/data/backup")
	public void backup(String tableNames,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, tableNames);
		if (!errors.hasErrors()) {
			String[] tableNameArr = tableNames.split(",");
			String backpath = realPathResolver.get(com.jeecms.cms.Constants.BACKUP_PATH);
			File backDirectory = new File(backpath);
			if (!backDirectory.exists()) {
				backDirectory.mkdir();
			}
			DateUtils dateUtils = DateUtils.getDateInstance();
			String backFilePath = backpath + SLASH+ dateUtils.getNowString() + "."
					+ SUFFIX;
			File file=new File(backFilePath);
			Thread thread =new DateBackupTableThread(file,tableNameArr);
			thread.start();
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/mysql/data/delete")
	public void delete(String names,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, names);
		if (!errors.hasErrors()) {
			String[] nameArr = names.split(",");
			errors = validateDelete(nameArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				try {
					int count = resourceMng.delete(nameArr);
					log.info("delete Resource count: {}", count);
					for (String name : nameArr) {
						log.info("delete Resource name={}", name);
						cmsLogMng.operating(request, "resource.log.delete", "filename="
								+ name);
					}
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					message = Constants.API_MESSAGE_DELETE_ERROR;
					code = ResponseCode.API_CODE_DELETE_ERROR;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/mysql/data/rename")
	public void rename(String origName, String distName,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors= ApiValidate.validateRequiredParams(request, errors, origName,distName);
		if (!errors.hasErrors()) {
			String orig = com.jeecms.cms.Constants.BACKUP_PATH +"/"+ origName;
			String dist = com.jeecms.cms.Constants.BACKUP_PATH +"/"+ distName;
			errors = fileExist(errors,orig,dist);
			if (!errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
			}else{
				resourceMng.rename(orig, dist);
				log.info("name Resource from {} to {}", orig, dist);
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors fileExist(WebErrors errors,String...filePath){
		if (filePath!=null) {
			for (int i = 0; i < filePath.length; i++) {
				File file = new File(filePath[i]);
				if (!file.exists()) {
					errors.addErrorString(Constants.API_MESSAGE_FILE_NOT_FOUNT);
					return errors;
				}
			}
		}
		return errors;
	}
	
	@RequestMapping("/mysql/data/export")
	public void export(String names,HttpServletRequest request,HttpServletResponse response){
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, names);
		if (!errors.hasErrors()) {
			String[] nameArr = names.split(",");
			if(validate(nameArr, request)){
				errors.addErrorString(INVALID_PARAM);
			}
			if (!errors.hasErrors()) {
				String backName="back";
				if(nameArr!=null&&nameArr.length>0&&nameArr[0]!=null){
					backName=nameArr[0].substring(nameArr[0].indexOf(com.jeecms.cms.Constants.BACKUP_PATH)
							+com.jeecms.cms.Constants.BACKUP_PATH.length()+1);
				}
				List<FileEntry> fileEntrys = new ArrayList<FileEntry>();
				response.setContentType("application/x-download;charset=UTF-8");
				response.addHeader("Content-disposition", "filename="
						+ backName+".zip");
				for(String filename:nameArr){
					File file=new File(realPathResolver.get(filename));
					fileEntrys.add(new FileEntry("", "", file));
				}
				try {
					// 模板一般都在windows下编辑，所以默认编码为GBK
					Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
				} catch (IOException e) {
					log.error("export db error!", e);
				}
			}
		}
	}
	
	@RequestMapping("/mysql/data/origName")
	public void getOrigName(String name,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, name);
		if (!errors.hasErrors()) {
			String origName = name.substring(com.jeecms.cms.Constants.BACKUP_PATH.length());
			body = "{\"origName\":\""+origName+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/mysql/data/progress")
	public void getProgress(HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		if (StringUtils.isNotBlank(backup_table)) {
			body = "{\"tablename\":\""+backup_table+"\"}";
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private boolean validate(String[] names,HttpServletRequest request) {
		if(names!=null&&names.length>0){
			for(String name:names){
				//导出阻止非法获取其他目录文件
				if (!name.contains(com.jeecms.cms.Constants.BACKUP_PATH)
						||name.contains("../")||name.contains("..\\")) {
					return true;
				}
			}
		}else{
			return true;
		}
		return false;
	}
	
	private WebErrors validateDelete(String[] names,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(names, "names", true);
		if(names!=null&&names.length>0){
			for(String name:names){
				//导出阻止非法获取其他目录文件
				if (!name.contains("/WEB-INF/backup/")||name.contains("../")||name.contains("..\\")) {
					errors.addErrorString(INVALID_PARAM);
				}
			}
		}else{
			errors.addErrorString(INVALID_PARAM);
		}
		for (String id : names) {
			vldExist(id, errors);
		}
		return errors;
	}
	
	private boolean vldExist(String name, WebErrors errors) {
		if (errors.ifNull(name, "name", false)) {
			return true;
		}
		return false;
	}
	
	private class DateBackupTableThread extends Thread{
		private File file;
		private String[] tablenames;
		public DateBackupTableThread(File file, String[] tablenames) {
			super();
			this.file = file;
			this.tablenames = tablenames;
		}
		public void run() {
			FileOutputStream out;
			OutputStreamWriter writer=null;
			try {
				out = new FileOutputStream(file);
				writer = new OutputStreamWriter(out, "utf8");
				writer.write(com.jeecms.cms.Constants.ONESQL_PREFIX + DISABLEFOREIGN);
				for (int i=0;i<tablenames.length;i++) {
					backup_table=tablenames[i];
					backupTable(writer,tablenames[i]);
				}
				writer.write(com.jeecms.cms.Constants.ONESQL_PREFIX + ABLEFOREIGN);
				backup_table="";
				writer.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		private   String backupTable(OutputStreamWriter writer,String tablename) throws IOException {
			writer.write(createOneTableSql(tablename));
			writer.flush();
			return tablename;
		}

		private String createOneTableSql(String tablename) {
			StringBuffer buffer = new StringBuffer();
			Object[] oneResult;
			buffer.append(com.jeecms.cms.Constants.ONESQL_PREFIX + "DROP TABLE IF EXISTS "
					+ tablename + BRANCH + BR);
			buffer.append(com.jeecms.cms.Constants.ONESQL_PREFIX
					+ dataBackMng.createTableDDL(tablename) + BRANCH + BR
					+ com.jeecms.cms.Constants.ONESQL_PREFIX);
			List<Object[]> results = dataBackMng.createTableData(tablename);
			for (int i = 0; i < results.size(); i++) {
				// one insert sql
				oneResult = results.get(i);
				buffer.append(createOneInsertSql(oneResult, tablename));
			}
			return buffer.toString();
		}

		private String createOneInsertSql(Object[] oneResult, String tablename) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(com.jeecms.cms.Constants.ONESQL_PREFIX + INSERT_INTO + SPLIT + tablename
					+ SPLIT + SPACE + VALUES + LEFTBRACE);
			for (int j = 0; j < oneResult.length; j++) {
				if (oneResult[j] != null) {
					if (oneResult[j] instanceof Date) {
						buffer.append(QUOTES + oneResult[j] + QUOTES);
					} else if (oneResult[j] instanceof String) {
						buffer.append(QUOTES
								+ StrUtils.replaceKeyString((String) oneResult[j])
								+ QUOTES);
					} else if (oneResult[j] instanceof Boolean) {
						if ((Boolean) oneResult[j]) {
							buffer.append(1);
						} else {
							buffer.append(0);
						}
					} else {
						buffer.append(oneResult[j]);
					}
				} else {
					buffer.append(oneResult[j]);
				}
				buffer.append(COMMA);
			}
			buffer = buffer.deleteCharAt(buffer.lastIndexOf(COMMA));
			buffer.append(RIGHTBRACE + BRANCH + BR);
			return buffer.toString();
		}
	}
	
	@SuppressWarnings("deprecation")
	public  void dbXml(String fileName, String oldDbHost,String dbHost) throws Exception {
		String s = FileUtils.readFileToString(new File(fileName));
		s = StringUtils.replace(s, oldDbHost, dbHost);
		FileUtils.writeStringToFile(new File(fileName), s);
	}
	
	private  String readFile(String filename) throws IOException {
	    File file =new File(filename);
	    if(filename==null || filename.equals(""))
	    {
	    	return Constants.API_MESSAGE_PARAM_REQUIRED;
	    }
	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
	    int r = bufferedInputStream.read( bytes );
	    if (r != len)
	     // throw new IOException("<@s.m 'db.filereaderror'/>");
	    bufferedInputStream.close();
	    return new String(bytes,"utf-8");
	}
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsMysqlDataBackMng dataBackMng;
	@Autowired
	private CmsResourceMng resourceMng;
	@Autowired
	private CmsLogMng cmsLogMng;
}
