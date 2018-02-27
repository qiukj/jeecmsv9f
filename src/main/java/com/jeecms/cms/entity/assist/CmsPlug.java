package com.jeecms.cms.entity.assist;

import com.jeecms.cms.entity.assist.base.BaseCmsPlug;



public class CmsPlug extends BaseCmsPlug {
	private static final long serialVersionUID = 1L;
	
	public Boolean getUsed(){
		return isUsed();
	}
	
	public Boolean getFileConflict () {
		return isFileConflict();
	}
	
	public boolean getCanInstall(){
		//未使用 且(文件未冲突或者是修复类)
		if(!getUsed()&&(!getFileConflict()||getPlugRepair())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getCanUnInstall(){
		//使用中的修复类插件和未使用的插件 不能卸载
		if((getUsed()&&getPlugRepair())||!getUsed()){
			return false;
		}else{
			return true;
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsPlug () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsPlug (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsPlug (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String path,
		java.util.Date uploadTime,
		boolean fileConflict,
		boolean used) {

		super (
			id,
			name,
			path,
			uploadTime,
			fileConflict,
			used);
	}

/*[CONSTRUCTOR MARKER END]*/


}