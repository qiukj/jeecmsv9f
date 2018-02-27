package com.jeecms.cms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.common.file.FileNameUtils;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsOss;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.DbFileMng;

/**
 * @author Tom
 */
public class ImageSvcImpl implements ImageSvc {
	public String crawlImg(String imgUrl,CmsSite  site) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	    CloseableHttpClient client = httpClientBuilder.build();  
		String outFileName="";
		String fileUrl="";
		try{
			if(validUrl(imgUrl)){
				URI uri=new URI(imgUrl);
				HttpGet httpget = new HttpGet(uri);
				HttpResponse response = client.execute(httpget);
				InputStream is = null;
				HttpEntity entity = null;
				int resCode=response.getStatusLine().getStatusCode();
				if(resCode==HttpServletResponse.SC_OK){
					entity = response.getEntity();
					is = entity.getContent();
					String ctx = site.getContextPath();
					String ext=FileNameUtils.getFileSufix(imgUrl);
					if (site.getConfig().getUploadToDb()) {
						String dbFilePath = site.getConfig().getDbFileUri();
						fileUrl = dbFileMng.storeByExt(site.getUploadPath(), ext, is);
						// 加上访问地址
						fileUrl = ctx + dbFilePath + fileUrl;
						if(ctx!=null&&StringUtils.isNotBlank(ctx)){
							fileUrl = ctx + dbFilePath + fileUrl;
						}else{
							fileUrl = dbFilePath + fileUrl;
						}
					} else if (site.getUploadFtp() != null) {
						Ftp ftp = site.getUploadFtp();
						String ftpUrl = ftp.getUrl();
						fileUrl = ftp.storeByExt(site.getUploadPath(), ext, is);
						// 加上url前缀
						fileUrl = ftpUrl + fileUrl;
					} else if (site.getUploadOss() != null) {
						CmsOss oss = site.getUploadOss();
						fileUrl = oss.storeByExt(site.getUploadPath(), ext,is);
					}else {
						OutputStream os = null;
						//outFileName=UploadUtils.generateFilename(site.getUploadPath(), FileNameUtils.getFileSufix(imgUrl));
						//File outFile=new File(realPathResolver.get(outFileName));
						String fileName=UploadUtils.generateRamdonFilename(FileNameUtils.getFileSufix(imgUrl));
						File outFile = new File(realPathResolver.get(site.getUploadPath()),fileName);
						UploadUtils.checkDirAndCreate(outFile.getParentFile());
						outFileName=site.getUploadPath()+fileName;
						os = new FileOutputStream(outFile);
						IOUtils.copy(is, os);
						// 加上部署路径
						if(ctx!=null&&StringUtils.isNotBlank(ctx)){
							fileUrl = ctx + outFileName;
						}else{
							fileUrl = outFileName;
						}
						os.flush();
						os.close();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
       }
		return fileUrl;
	}
	
	public String crawlImg(String imgUrl,String ctx,boolean uploadToDb,String dbFileUri,Ftp ftp,
			CmsOss oss,String uploadPath) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	    CloseableHttpClient client = httpClientBuilder.build();  
		String outFileName="";
		String fileUrl="";
		try{
			if(validUrl(imgUrl)){
				HttpGet httpget = new HttpGet(new URI(imgUrl));
				HttpResponse response = client.execute(httpget);
				InputStream is = null;
				OutputStream os = null;
				HttpEntity entity = null;
				entity = response.getEntity();
				is = entity.getContent();
				String ext=FileNameUtils.getFileSufix(imgUrl);
				if (uploadToDb) {
					String dbFilePath = dbFileUri;
					fileUrl = dbFileMng.storeByExt(uploadPath, ext, is);
					// 加上访问地址
					fileUrl = ctx + dbFilePath + fileUrl;
					if(ctx!=null&&StringUtils.isNotBlank(ctx)){
						fileUrl = ctx + dbFilePath + fileUrl;
					}else{
						fileUrl = dbFilePath + fileUrl;
					}
				} else if (ftp!=null) {
					String ftpUrl = ftp.getUrl();
					fileUrl = ftp.storeByExt(uploadPath, ext, is);
					// 加上url前缀
					fileUrl = ftpUrl + fileUrl;
				} else if (oss != null) {
					fileUrl = oss.storeByExt(uploadPath, ext,is);
				} else {
					outFileName=UploadUtils.generateFilename(uploadPath, FileNameUtils.getFileSufix(imgUrl));
					File outFile=new File(realPathResolver.get(outFileName));
					UploadUtils.checkDirAndCreate(outFile.getParentFile());
					os = new FileOutputStream(outFile);
					IOUtils.copy(is, os);
					// 加上部署路径
					if(ctx!=null&&StringUtils.isNotBlank(ctx)){
						fileUrl = ctx + outFileName;
					}else{
						fileUrl = outFileName;
					}
				}
			}
		}catch (Exception e) {
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
       }
		return fileUrl;
	}
	
	private boolean  validUrl(String imgUrl){
		URL url;
		boolean isImage=true;
		try {
			url = new URL(imgUrl);
			URLConnection urlConnection = url.openConnection();
			InputStream inputStream = urlConnection.getInputStream();
			isImage=ImageUtils.isImage(inputStream);
			inputStream.close();
		} catch (Exception e) {
			isImage= false;
		}
		return isImage;
	}
	@Autowired
	protected DbFileMng dbFileMng;
	@Autowired
	protected FileRepository fileRepository;
	@Autowired
	private RealPathResolver realPathResolver;
}
