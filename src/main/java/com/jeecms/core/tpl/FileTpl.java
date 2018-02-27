package com.jeecms.core.tpl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.file.FileWrap.FileComparator;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.Constants;

public class FileTpl implements Tpl {
	private File file;
	// 应用的根目录
	private String root;
	private List<FileTpl> child;

	public FileTpl(File file, String root) {
		this.file = file;
		this.root = root;
	}

	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("name", getName());
		json.put("path", getPath());
		json.put("filename", getFilename());
		if(StringUtils.isNotBlank(getSource())){
			json.put("source", getSource());
		}else{
			json.put("source", "");
		}
		json.put("length", getLength());
		json.put("lastModifiedDate", DateUtils.parseDateToTimeStr(getLastModifiedDate()));
		json.put("size", getSize());
		json.put("isDirectory", isDirectory());
		return json;
	}
	
	public JSONObject convertToTreeJson(FileTpl ob) 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("name", ob.getFilename());
		json.put("path", ob.getName());
		json.put("isDirectory", ob.isDirectory());
		List<FileTpl>childs=ob.getChild();
		if(childs.size()>0){
			JSONArray childJson=new JSONArray();
			for(int i=0;i<childs.size();i++){
				FileTpl f=childs.get(i);
				JSONObject obj=new JSONObject();
				obj=convertToTreeJson(f);
				childJson.put(i, obj);
			}
			json.put("child", childJson);
		}else{
			json.put("child", "");
		}
		return json;
	}
	
	/**
	 * 获得下级目录
	 * 
	 * 如果没有指定下级目录，则返回文件夹自身的下级文件，并运用FileFilter。
	 * 
	 * @return
	 */
	public List<FileTpl> getChild() {
		if (this.child != null) {
			return this.child;
		}
		File[] files;
		files = getFile().listFiles();
		List<FileTpl> list = new ArrayList<FileTpl>();
		if (files != null) {
			Arrays.sort(files, new FileComparator());
			for (File f : files) {
				FileTpl fw = new FileTpl(f, root);
				list.add(fw);
			}
		}
		return list;
	}
	
	
	public String getName() {
		String ap = file.getAbsolutePath().substring(root.length());
		ap = ap.replace(File.separatorChar, '/');
		// 在resin里root的结尾是带'/'的，这样会导致getName返回的名称不以'/'开头。
		if (!ap.startsWith("/")) {
			ap = "/" + ap;
		}
		return ap;
	}

	public String getPath() {
		String name = getName();
		return name.substring(0, name.lastIndexOf('/'));
	}

	public String getFilename() {
		return file.getName();
	}

	public String getSource() {
		if (file.isDirectory()) {
			return null;
		}
		try {
			return FileUtils.readFileToString(this.file, Constants.UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public long getLastModified() {
		return file.lastModified();
	}

	public Date getLastModifiedDate() {
		return new Timestamp(getLastModified());
	}

	public long getLength() {
		return file.length();
	}

	public int getSize() {
		return (int) (getLength() / 1024) + 1;
	}

	public boolean isDirectory() {
		return file.isDirectory();
	}
	
	/**
	 * 获得被包装的文件
	 * 
	 * @return
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * 指定下级目录
	 * 
	 * @param child
	 */
	public void setChild(List<FileTpl> child) {
		this.child = child;
	}
}
