package com.jeecms.core.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.jeecms.cms.api.Constants;
import com.jeecms.common.upload.AliOssCloudClient;
import com.jeecms.common.upload.QCoscloudClient;
import com.jeecms.common.upload.QiniuOssCloudClient;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.core.entity.base.BaseCmsOss;



public class CmsOss extends BaseCmsOss {
	private static final long serialVersionUID = 1L;
	
	public static final byte OSS_TYPE_Q_CLOUD = 1;
	
	public static final byte OSS_TYPE_QINIU_CLOUD = 3;
	
	public static final byte OSS_TYPE_ALI_CLOUD = 2;
	
	public static final int OSS_UPLOAD_SUCCESS = 1;
	
	public static final int OSS_UPLOAD_FAIL = 0;
	
	public enum OSSTYPE {
		/**
		 * 腾讯云cos
		 */
		QCLOUD,
		/**
		 * 七牛云oss
		 */
		QINIUCLOUD,
		/**
		 * 阿里云oss
		 */
		ALICLOUD,
		/**
		 * 百度云
		 */
		BAIDUCLOUD
	};
	

	public String storeByExt(String path, String ext, InputStream in)
			throws IOException {
		String remoteFileName = UploadUtils.generateFilename(path, ext);
		String fileOssUrl=store(remoteFileName, in);
		return fileOssUrl;
	}
	
	public String storeByFilename(String filename, InputStream in)
			throws IOException {
		String fileOssUrl=store(filename,in);
		return fileOssUrl;
	}
	
	private String store(String remote, InputStream in) {
		String result="";
		String fileUrl="";
		if(getOssType().equals(OSS_TYPE_Q_CLOUD)){
			result=QCoscloudClient.uploadFileByInputStream(this, remote, in);
			if(StringUtils.isNotBlank(result)&&result.startsWith("{")){
				JSONObject json=new JSONObject(result);
				Object code=json.get("code");
				if(code!=null&&code.equals(0)){
					fileUrl=json.getJSONObject("data").getString("source_url");
				}
			}
		}else if(getOssType().equals(OSS_TYPE_ALI_CLOUD)){
			boolean succ=AliOssCloudClient.uploadFileByInputStream(this, remote, in);
			if(succ){
				//阿里云存储accessDomain设置不带http://
				fileUrl="http://"+getBucketName()+"."+this.getAccessDomain()+remote;
			}
		}else if(getOssType().equals(OSS_TYPE_QINIU_CLOUD)){
			Map<String,String>map=QiniuOssCloudClient.uploadFileByInputStream(this, remote, in);
			if(StringUtils.isNotBlank(map.get(QiniuOssCloudClient.RESULT_KEY))){
				//七牛云存存储accessDomain得设置带http://
				fileUrl=this.getAccessDomain()+remote;
			}
		}
		return fileUrl;
	}
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getOssAppId())) {
			json.put("ossAppId", getOssAppId());
		}else{
			json.put("ossAppId", "");
		}
		if (StringUtils.isNotBlank(getSecretId())) {
			json.put("secretId", getSecretId());
		}else{
			json.put("secretId", "");
		}
		if (StringUtils.isNotBlank(getAppKey())) {
			json.put("appKey", getAppKey());
		}else{
			json.put("appKey", "");
		}
		if (StringUtils.isNotBlank(getBucketName())) {
			json.put("bucketName", getBucketName());
		}else{
			json.put("bucketName", "");
		}
		if (StringUtils.isNotBlank(getBucketArea())) {
			json.put("bucketArea", getBucketArea());
		}else{
			json.put("bucketArea", "");
		}
		if (StringUtils.isNotBlank(getEndPoint())) {
			json.put("endPoint", getEndPoint());
		}else{
			json.put("endPoint", "");
		}
		if (StringUtils.isNotBlank(getAccessDomain())) {
			json.put("accessDomain", getAccessDomain());
		}else{
			json.put("accessDomain", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (getOssType()!=null) {
			json.put("ossType", getOssType());
		}else{
			json.put("ossType", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsOss () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsOss (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsOss(String ossAppId, String secretId, 
			String appKey, String bucketName, String bucketArea,
			String endPoint, String accessDomain, Byte ossType) {
		super(ossAppId, secretId, appKey, bucketName, 
				bucketArea,endPoint,accessDomain, ossType);
	}

/*[CONSTRUCTOR MARKER END]*/


}