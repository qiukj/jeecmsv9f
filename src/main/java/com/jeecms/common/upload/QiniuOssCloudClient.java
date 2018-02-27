package com.jeecms.common.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.jeecms.core.entity.CmsOss;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuOssCloudClient {
	private static final Logger log = LoggerFactory.getLogger(QiniuOssCloudClient.class);
	public static final  String RESULT_KEY="key";
	public static final  String RESULT_HASE="hash";

	public static Map<String,String> uploadFileByLocal(CmsOss oss, String ossPath, String localPath) {
		Map<String,String>result=new HashMap<String, String>();
		UploadManager uploadManager = getUploadManager();
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = localPath;
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = ossPath;
		String upToken = getAuthToken(oss);
		if (StringUtils.isNotBlank(ossPath)) {
			if(ossPath.startsWith("/")){
				key = ossPath.substring(1);
			}
			try {
				Response response = uploadManager.put(localFilePath, key, upToken);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				result.put(RESULT_KEY, putRet.key);
				result.put(RESULT_HASE, putRet.hash);
			} catch (QiniuException ex) {
				Response r = ex.response;
				log.error(r.toString());
				try {
					log.error(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		}
		return result;
	}

	public static  Map<String,String> uploadFileByByte(CmsOss oss, String ossPath, byte[] content) {
		Map<String,String>result=new HashMap<String, String>();
		UploadManager uploadManager = getUploadManager();
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = ossPath;
		String upToken = getAuthToken(oss);
		if (StringUtils.isNotBlank(ossPath)) {
			/// u/cms/www/201712/xxxxx.jpg 截成u/cms/www/201712/xxxxx.jpg,否则返回URL会无法访问到文件
			if(ossPath.startsWith("/")){
				key = ossPath.substring(1);
			}
			try {
				Response response = uploadManager.put(content, key, upToken);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				result.put(RESULT_KEY, putRet.key);
				result.put(RESULT_HASE, putRet.hash);
			} catch (QiniuException ex) {
				Response r = ex.response;
				log.error(r.toString());
				try {
					log.error(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		}
		
		
		return result;
	}

	public static  Map<String,String> uploadFileByInputStream(CmsOss oss, String ossPath, InputStream inputStream) {
		Map<String,String>result=new HashMap<String, String>();
		UploadManager uploadManager = getUploadManager();
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = ossPath;
		String upToken = getAuthToken(oss);
		if (StringUtils.isNotBlank(ossPath)) {
			try {
				/// u/cms/www/201712/xxxxx.jpg 截成u/cms/www/201712/xxxxx.jpg,否则返回URL会无法访问到文件
				if(ossPath.startsWith("/")){
					key = ossPath.substring(1);
				}
				Response response = uploadManager.put(inputStream, key, upToken, null, null);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				result.put(RESULT_KEY, putRet.key);
				result.put(RESULT_HASE, putRet.hash);
			} catch (QiniuException ex) {
				Response r = ex.response;
				log.error(r.toString());
				try {
					log.error(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		}
		return result;
	}

	public static UploadManager getUploadManager() {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		return uploadManager;
	}

	public static String getAuthToken(CmsOss oss) {
		// ...生成上传凭证，然后准备上传
		String accessKey = oss.getAppKey();
		String secretKey = oss.getSecretId();
		String bucket = oss.getBucketName();
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		return upToken;
	}

	public static byte[] toByteArray(InputStream input) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		try {
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output.toByteArray();
	}

	public static void main(String[] args) {
		File file = new File("D:\\test\\1.jpg");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CmsOss oss = new CmsOss("APP_ID", "J5P8Bq4l2FPHwAqQU1aP24JlcflZTVaulm4-wgK3",
				"Sv0AmRjs4vEWGk7POWV3XNNSoWsBs2aLO3DT6T6M", "jeecms", "BUCKET_AREA", 
				"","http://p0zoghccu.bkt.clouddn.com",(byte) 3);
		// uploadFileByInputStream(oss, "/u/cms/www/3.jpg", fileInput);
		byte[] content = toByteArray(fileInput);
		System.out.println(uploadFileByByte(oss, "/u/cms/www/byte2.jpg", content));;
	}

}
