package com.jeecms.core.entity;

import static com.jeecms.cms.Constants.RES_PATH;
import static com.jeecms.cms.Constants.TPL_BASE;
import static com.jeecms.cms.Constants.TPLDIR_INDEX;
import static com.jeecms.cms.Constants.TPL_SUFFIX;
import static com.jeecms.cms.Constants.UPLOAD_PATH;
import static com.jeecms.cms.Constants.LIBRARY_PATH;
import static com.jeecms.common.web.Constants.ADMIN_SUFFIX;
import static com.jeecms.common.web.Constants.INDEX;
import static com.jeecms.common.web.Constants.SPT;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsSite;

public class CmsSite extends BaseCmsSite {
	private static final long serialVersionUID = 1L;
	public static final String PV_TOTAL="pvTotal";
	public static final String VISITORS="visitors";
	public static final String DAY_PV_TOTAL="dayPvTotal";
	public static final String DAY_VISITORS="dayVisitors";
	public static final String WEEK_PV_TOTAL="weekPvTotal";
	public static final String WEEK_VISITORS="weekVisitors";
	public static final String MONTH_PV_TOTAL="monthPvTotal";
	public static final String MONTH_VISITORS="monthVisitors";
	
	public static final String CONTENT_TOTAL="contentTotal";
	public static final String COMMENT_TOTAL="commentTotal";
	public static final String GUESTBOOK_TOTAL="guestbookTotal";
	public static final String MEMBER_TOTAL="memberTotal";
	

	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getShortName())) {
			json.put("shortName", getShortName());
		}else{
			json.put("shortName", "");
		}
		if (StringUtils.isNotBlank(getKeywords())) {
			json.put("keywords", getKeywords());
		}else{
			json.put("keywords", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getDomain())) {
			json.put("domain", getDomain());
		}else{
			json.put("domain", "");
		}
		if (StringUtils.isNotBlank(getPath())) {
			json.put("path",getPath() );
		}else{
			json.put("path", "");
		}
		if (StringUtils.isNotBlank(getDomainAlias())) {
			json.put("domainAlias", getDomainAlias() );
		}else{
			json.put("domainAlias", "");
		}
		if (StringUtils.isNotBlank(getDomainRedirect())) {
			json.put("domainRedirect",getDomainRedirect() );
		}else{
			json.put("domainRedirect", "");
		}
		if (getRelativePath()!=null) {
			json.put("relativePath",getRelativePath() );
		}else{
			json.put("relativePath", "");
		}
		if (StringUtils.isNotBlank(getProtocol())) {
			json.put("protocol", getProtocol());
		}else{
			json.put("protocol", "");
		}
		if (StringUtils.isNotBlank(getDynamicSuffix())) {
			json.put("dynamicSuffix", getDynamicSuffix());
		}else{
			json.put("dynamicSuffix", "");
		}
		if (StringUtils.isNotBlank(getStaticSuffix())) {
			json.put("staticSuffix", getStaticSuffix());
		}else{
			json.put("staticSuffix", "");
		}
		if (StringUtils.isNotBlank(getStaticDir())) {
			json.put("staticDir", getStaticDir());
		}else{
			json.put("staticDir", "");
		}
		if (getIndexToRoot()!=null) {
			json.put("indexToRoot", getIndexToRoot());
		}else{
			json.put("indexToRoot", "");
		}
		if (StringUtils.isNotBlank(getStaticMobileDir())) {
			json.put("staticMobileDir", getStaticMobileDir());
		}else{
			json.put("staticMobileDir", "");
		}
		if (getMobileStaticSync()!=null) {
			json.put("mobileStaticSync", getMobileStaticSync());
		}else{
			json.put("mobileStaticSync", "");
		}
		if (getResouceSync()!=null) {
			json.put("resouceSync", getResouceSync());
		}else{
			json.put("resouceSync", "");
		}
		if (getPageSync()!=null) {
			json.put("pageSync", getPageSync());
		}else{
			json.put("pageSync", "");
		}
		if (getSyncPageFtp()!=null&&getSyncPageFtp().getId()!=null) {
			json.put("syncPageFtpId", getSyncPageFtp().getId());
		}else{
			json.put("syncPageFtpId", "");
		}
		if (getStaticIndex()!=null) {
			json.put("staticIndex", getStaticIndex());
		}else{
			json.put("staticIndex", "");
		}
		int tplPathLength = getTplPath().length();
		String tplIndex = getTplIndex();
		if (!StringUtils.isBlank(tplIndex)) {
			tplIndex = tplIndex.substring(tplPathLength);
		}
		if (StringUtils.isNotBlank(tplIndex)) {
			json.put("tplIndex", tplIndex);
		}else{
			json.put("tplIndex", "");
		}
		if (StringUtils.isNotBlank(getLocaleAdmin())) {
			json.put("localeAdmin", getLocaleAdmin());
		}else{
			json.put("localeAdmin", "");
		}
		if (StringUtils.isNotBlank(getLocaleFront())) {
			json.put("localeFront", getLocaleFront());
		}else{
			json.put("localeFront", "");
		}
		if (getUploadFtp()!=null&&getUploadFtp().getId()!=null) {
			json.put("uploadFtpId", getUploadFtp().getId());
		}else{
			json.put("uploadFtpId", "");
		}
		if (getResycleOn()!=null) {
			json.put("resycleOn", getResycleOn());
		}else{
			json.put("resycleOn", "");
		}
		if (getAfterCheck()!=null) {
			json.put("afterCheck", getAfterCheck());
		}else{
			json.put("afterCheck", "");
		}
		json.put("master", "");
		if (getUploadOss()!=null&&getUploadOss().getId()!=null) {
			json.put("ossId", getUploadOss().getId());
		}else{
			json.put("ossId", "");
		}
		if (StringUtils.isNotBlank(getTplSolution())) {
			json.put("tplSolution", getTplSolution());
		}else{
			json.put("tplSolution", "");
		}
		if (StringUtils.isNotBlank(getTplMobileSolution())) {
			json.put("tplMobileSolution", getTplMobileSolution());
		}else{
			json.put("tplMobileSolution", "");
		}
		json.put("parentId", "");
		return json;
	}
	
	/**
	 * 返回首页模板
	 * @return
	 */
	public String getTplIndexOrDef() {
		String tpl = getTplIndex();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getTplIndexDefault();
		}
	}
	
	/**
	 * 返回手机首页模板
	 * @return
	 */
	public String getMobileTplIndexOrDef() {
		StringBuilder t = new StringBuilder();
		t.append(getMobileSolutionPath()).append("/");
		t.append(TPLDIR_INDEX).append("/");
		t.append(TPLDIR_INDEX);
		t.append(TPL_SUFFIX);
		return t.toString();
	}
	
	/**
	 * 返回首页默认模板(类似/WEB-INF/t/cms/www/default/index/index.html)
	 * @return
	 */
	private String getTplIndexDefault() {
		StringBuilder t = new StringBuilder();
		t.append(getTplIndexPrefix(TPLDIR_INDEX));
		t.append(TPL_SUFFIX);
		return t.toString();
	}
	
	/**
	 * 返回完整前缀(类似/WEB-INF/t/cms/www/default/index/index)
	 * @param prefix
	 * @return
	 */
	public String getTplIndexPrefix(String prefix) {
		StringBuilder t = new StringBuilder();
		t.append(getSolutionPath()).append("/");
		t.append(TPLDIR_INDEX).append("/");
		if (!StringUtils.isBlank(prefix)) {
			t.append(prefix);
		}
		return t.toString();
	}
	
	/**
	 * 获得节点列表。从父节点到自身。
	 * 
	 * @return
	 */
	public List<CmsSite> getNodeList() {
		LinkedList<CmsSite> list = new LinkedList<CmsSite>();
		CmsSite node = this;
		return list;
	}

	/**
	 * 获得节点列表ID。从父节点到自身。
	 * 
	 * @return
	 */
	public Integer[] getNodeIds() {
		List<CmsSite> sites = getNodeList();
		Integer[] ids = new Integer[sites.size()];
		int i = 0;
		for (CmsSite c : sites) {
			ids[i++] = c.getId();
		}
		return ids;
	}
	
	/**
	 * 获得深度
	 * 
	 * @return 第一层为0，第二层为1，以此类推。
	 */
	public int getDeep() {
		int deep = 0;
		return deep;
	}

	/**
	 * 获得站点url
	 * 
	 * @return
	 */
	public String getUrl() {
		StringBuilder url = new StringBuilder();
		if (getStaticIndex()) {
			url.append(getUrlStatic());
			if (!getIndexToRoot()) {
				if (!StringUtils.isBlank(getStaticDir())) {
					url.append(getStaticDir());
				}
			}
			url.append(SPT).append(INDEX).append(getStaticSuffix());
		} else {
			url.append(getUrlDynamic());
		}
		return url.toString();
		/*
		if (getStaticIndex()) {
			return getUrlStatic();
		} else {
			return getUrlDynamic();
		}
		*/
	}
	
	public String getHttpsUrl() {
		StringBuilder url = new StringBuilder();
		if (getStaticIndex()) {
			url.append(getHttpsUrlStatic());
			if (!getIndexToRoot()) {
				if (!StringUtils.isBlank(getStaticDir())) {
					url.append(getStaticDir());
				}
			}
			url.append(SPT).append(INDEX).append(getStaticSuffix());
		} else {
			url.append(getHttpsUrlDynamic());
		}
		return url.toString();
	}
	public String getAdminUrl() {
		StringBuilder url = new StringBuilder();
			url.append(getUrlDynamic());
			url.append(ADMIN_SUFFIX);
		return url.toString();
	}

	/**
	 * 获得完整路径。在给其他网站提供客户端包含时也可以使用。
	 * 
	 * @return
	 */
	public String getUrlWhole() {
		if (getStaticIndex()) {
			return getUrlBuffer(false, true, false).append("/").toString();
		} else {
			return getUrlBuffer(true, true, false).append("/").toString();
		}
	}

	public String getUrlDynamic() {
		return getUrlBuffer(true, null, false).append("/").toString();
	}

	public String getUrlStatic() {
		return getUrlBuffer(false, null, true).append("/").toString();
	}
	
	public String getHttpsUrlDynamic() {
		return getHttpsUrlBuffer(true, null, false).append("/").toString();
	}

	public String getHttpsUrlStatic() {
		return getHttpsUrlBuffer(false, null, true).append("/").toString();
	}
	
	public String getUrlPrefix() {
		StringBuilder url = new StringBuilder();
		url.append(getProtocol()).append(getDomain());
		if (getPort() != null) {
			url.append(":").append(getPort());
		}
		return url.toString();
	}
	
	public String getUrlPrefixWithNoDefaultPort() {
		StringBuilder url = new StringBuilder();
		url.append(getProtocol()).append(getDomain());
		if (getPort() != null&&getPort() != 80) {
			url.append(":").append(getPort());
		}
		return url.toString();
	}
	
	
	public String getSafeUrlPrefix() {
		StringBuilder url = new StringBuilder();
		url.append("https://").append(getDomain());
		if (getPort() != null&&getPort()!=80&&getPort()!=443) {
			url.append(":").append(getPort());
		}
		return url.toString();
	}

	public StringBuilder getUrlBuffer(boolean dynamic, Boolean whole,
			boolean forIndex) {
		boolean relative = whole != null ? !whole : getRelativePath();
		String ctx = getContextPath();
		StringBuilder url = new StringBuilder();
		if (!relative) {
			url.append(getProtocol()).append(getDomain());
			if (getPort() != null&&getPort()!=80) {
				url.append(":").append(getPort());
			}
		}
		if (!StringUtils.isBlank(ctx)) {
			url.append(ctx);
		}
		if (dynamic) {
			String servlet = getServletPoint();
			if (!StringUtils.isBlank(servlet)) {
				url.append(servlet);
			}
		} else {
			if (!forIndex) {
				String staticDir = getStaticDir();
				if (!StringUtils.isBlank(staticDir)) {
					url.append(staticDir);
				}
			}
		}
		return url;
	}
	
	public StringBuilder getHttpsUrlBuffer(boolean dynamic, Boolean whole,
			boolean forIndex) {
		boolean relative = whole != null ? !whole : getRelativePath();
		String ctx = getContextPath();
		StringBuilder url = new StringBuilder();
		if (!relative) {
			url.append("https://").append(getDomain());
			if (getPort() != null&&getPort()!=80) {
				url.append(":").append(getPort());
			}
		}
		if (!StringUtils.isBlank(ctx)) {
			url.append(ctx);
		}
		if (dynamic) {
			String servlet = getServletPoint();
			if (!StringUtils.isBlank(servlet)) {
				url.append(servlet);
			}
		} else {
			if (!forIndex) {
				String staticDir = getStaticDir();
				if (!StringUtils.isBlank(staticDir)) {
					url.append(staticDir);
				}
			}
		}
		return url;
	}
	
	public StringBuilder getMobileUrlBuffer(boolean dynamic, Boolean whole,
			boolean forIndex) {
		boolean relative = whole != null ? !whole : getRelativePath();
		String ctx = getContextPath();
		StringBuilder url = new StringBuilder();
		if (!relative) {
			url.append(getProtocol()).append(getDomain());
			if (getPort() != null) {
				url.append(":").append(getPort());
			}
		}
		if (!StringUtils.isBlank(ctx)) {
			url.append(ctx);
		}
		if (dynamic) {
			String servlet = getServletPoint();
			if (!StringUtils.isBlank(servlet)) {
				url.append(servlet);
			}
		} else {
			if (!forIndex) {
				String staticDir = getStaticMobileDir();
				if (!StringUtils.isBlank(staticDir)) {
					url.append(staticDir);
				}
			}
		}
		return url;
	}

	/**
	 * 获得模板路径。如：/WEB-INF/t/cms/www
	 * 
	 * @return
	 */
	public String getTplPath() {
		return TPL_BASE + "/" + getPath();
	}

	/**
	 * 获得模板方案路径。如：/WEB-INF/t/cms/www/default
	 * 
	 * @return
	 */
	public String getSolutionPath() {
		return TPL_BASE + "/" + getPath() + "/" + getTplSolution();
	}
	
	public String getMobileSolutionPath() {
		return TPL_BASE + "/" + getPath() + "/" + getTplMobileSolution();
	}

	/**
	 * 获得模板资源路径。如：/r/cms/www
	 * 
	 * @return
	 */
	public String getResPath() {
		return RES_PATH + "/" + getPath();
	}

	/**
	 * 获得上传路径。如：/u/jeecms/path
	 * 
	 * @return
	 */
	public String getUploadPath() {
		return UPLOAD_PATH + getPath();
	}
	/**
	 * 获得文库路径。如：/wenku/path
	 * 
	 * @return
	 */
	public String getLibraryPath() {
		return LIBRARY_PATH + getPath();
	}

	/**
	 * 获得上传访问前缀。
	 * 
	 * 根据配置识别上传至数据、FTP和本地
	 * 
	 * @return
	 */
	public String getUploadBase() {
		CmsConfig config = getConfig();
		String ctx = config.getContextPath();
		if (config.getUploadToDb()) {
			if (!StringUtils.isBlank(ctx)) {
				return ctx + config.getDbFileUri();
			} else {
				return config.getDbFileUri();
			}
		} else if (getUploadFtp() != null) {
			return getUploadFtp().getUrl();
		} else {
			if (!StringUtils.isBlank(ctx)) {
				return ctx;
			} else {
				return "";
			}
		}
	}

	public String getServletPoint() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getServletPoint();
		} else {
			return null;
		}
	}

	public String getContextPath() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getContextPath();
		} else {
			return null;
		}
	}

	public Integer getPort() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getPort();
		} else {
			return null;
		}
	}

	public String getDefImg() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getDefImg();
		} else {
			return null;
		}
	}

	public String getLoginUrl() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getLoginUrl();
		} else {
			return null;
		}
	}

	public String getProcessUrl() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getProcessUrl();
		} else {
			return null;
		}
	}

	public int getUsernameMinLen() {
		return getConfig().getMemberConfig().getUsernameMinLen();
	}

	public int getPasswordMinLen() {
		return getConfig().getMemberConfig().getPasswordMinLen();
	}
	
	public Boolean getMark(){
		return getConfig().getMarkConfig().getOn();
	}
	
	public String getNewPic(){
		return getConfig().getConfigAttr().getPictureNew();
	}
	
	public Long getPvTotal(){
		String pv=getAttr().get(PV_TOTAL);
		if(StringUtils.isNotBlank(pv)){
			return Long.decode(pv);
		}else{
			return 0l;
		}
	}
	
	public Long getVisitorTotal(){
		String visitorNum=getAttr().get(VISITORS);
		if(StringUtils.isNotBlank(visitorNum)){
			return Long.decode(visitorNum);
		}else{
			return 0l;
		}
	}
	
	public Long getDayPvTotal(){
		String pv=getAttr().get(DAY_PV_TOTAL);
		if(StringUtils.isNotBlank(pv)){
			return Long.decode(pv);
		}else{
			return 0l;
		}
	}
	
	public Long getDayVisitorTotal(){
		String visitorNum=getAttr().get(DAY_VISITORS);
		if(StringUtils.isNotBlank(visitorNum)){
			return Long.decode(visitorNum);
		}else{
			return 0l;
		}
	}
	
	public Long getWeekPvTotal(){
		String pv=getAttr().get(WEEK_PV_TOTAL);
		if(StringUtils.isNotBlank(pv)){
			return Long.decode(pv);
		}else{
			return 0l;
		}
	}
	
	public Long getWeekVisitorTotal(){
		String visitorNum=getAttr().get(WEEK_VISITORS);
		if(StringUtils.isNotBlank(visitorNum)){
			return Long.decode(visitorNum);
		}else{
			return 0l;
		}
	}
	
	public Long getMonthPvTotal(){
		String pv=getAttr().get(MONTH_PV_TOTAL);
		if(StringUtils.isNotBlank(pv)){
			return Long.decode(pv);
		}else{
			return 0l;
		}
	}
	
	public Long getMonthVisitorTotal(){
		String visitorNum=getAttr().get(MONTH_VISITORS);
		if(StringUtils.isNotBlank(visitorNum)){
			return Long.decode(visitorNum);
		}else{
			return 0l;
		}
	}
	
	public Integer getContentTotal(){
		String contentTotal=getAttr().get(CONTENT_TOTAL);
		if(StringUtils.isNotBlank(contentTotal)){
			return Integer.decode(contentTotal);
		}else{
			return 0;
		}
	}
	
	public Integer getCommentTotal(){
		String commentTotal=getAttr().get(COMMENT_TOTAL);
		if(StringUtils.isNotBlank(commentTotal)){
			return Integer.decode(commentTotal);
		}else{
			return 0;
		}
	}
	
	public Integer getGuestbookTotal(){
		String guestbookTotal=getAttr().get(GUESTBOOK_TOTAL);
		if(StringUtils.isNotBlank(guestbookTotal)){
			return Integer.decode(guestbookTotal);
		}else{
			return 0;
		}
	}
	
	public Integer getMemberTotal(){
		String memberTotal=getAttr().get(MEMBER_TOTAL);
		if(StringUtils.isNotBlank(memberTotal)){
			return Integer.decode(memberTotal);
		}else{
			return 0;
		}
	}
	
	public String getWxAppkey(){
		String wxAppkey=getAttr().get(com.jeecms.core.Constants.WEIXIN_APPKEY);
		return wxAppkey;
	}
	
	public String getWxAppSecret(){
		String wxAppSecret=getAttr().get(com.jeecms.core.Constants.WEIXIN_APPSECRET);
		return wxAppSecret;
	}
	
	public String getWxToken(){
		String wxToken=getAttr().get(com.jeecms.core.Constants.WEIXIN_TOKEN);
		return wxToken;
	}
	
	public static Integer[] fetchIds(Collection<CmsSite> sites) {
		if (sites == null) {
			return null;
		}
		Integer[] ids = new Integer[sites.size()];
		int i = 0;
		for (CmsSite s : sites) {
			ids[i++] = s.getId();
		}
		return ids;
	}
	
	public String getBaseDomain() {
		String domain = getDomain();
		if (domain.indexOf(".") > -1) {
			return domain.substring(domain.indexOf(".") + 1);
		}
		return domain;
	}

	public void init() {
		if (StringUtils.isBlank(getProtocol())) {
			setProtocol("http://");
		}
		if (StringUtils.isBlank(getTplSolution())) {
			//默认路径名作为方案名
			setTplSolution(getPath());
		   //setTplSolution(DEFAULT);
		}
		if (StringUtils.isBlank(getTplMobileSolution())) {
			//默认路径名作为方案名
			setTplMobileSolution(getPath());
		}
		if (getFinalStep() == null) {
			byte step = 2;
			setFinalStep(step);
		}
		if (StringUtils.isBlank(getShortName())) {
			if (StringUtils.isNotBlank(getName())) {
				setShortName(getName());
			}else{
				setShortName("");
			}
		}
		//新增默认赋值
		if (getRelativePath()==null) {
			setRelativePath(false);
		}
		if (getIndexToRoot()==null) {
			setIndexToRoot(false);
		}
		if (getMobileStaticSync()==null) {
			setMobileStaticSync(false);
		}
		if (getResouceSync()==null) {
			setResouceSync(false);
		}
		if (getPageSync()==null) {
			setPageSync(false);
		}
		if (getStaticIndex()==null) {
			setStaticIndex(false);
		}
		if (getResycleOn()==null) {
			setResycleOn(false);
		}
		if (getFinalStep()==null) {
			setFinalStep((byte) 3);
		}
		if (getAfterCheck()==null) {
			setAfterCheck((byte) 3);
		}
		if (StringUtils.isBlank(getDynamicSuffix())) {
			setDynamicSuffix(".jhtml");
		}
		if (StringUtils.isBlank(getStaticSuffix())) {
			setStaticSuffix(".html");
		}
		if (StringUtils.isBlank(getLocaleAdmin())) {
			setLocaleAdmin("zh_CN");
		}
		if (StringUtils.isBlank(getLocaleFront())) {
			setLocaleFront("zh_CN");
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsSite () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSite (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSite (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsConfig config,
		java.lang.String domain,
		java.lang.String path,
		java.lang.String name,
		java.lang.String protocol,
		java.lang.String dynamicSuffix,
		java.lang.String staticSuffix,
		java.lang.Boolean indexToRoot,
		java.lang.Boolean staticIndex,
		java.lang.String localeAdmin,
		java.lang.String localeFront,
		java.lang.String tplSolution,
		java.lang.Byte finalStep,
		java.lang.Byte afterCheck,
		java.lang.Boolean relativePath,
		java.lang.Boolean resycleOn) {

		super (
			id,
			config,
			domain,
			path,
			name,
			protocol,
			dynamicSuffix,
			staticSuffix,
			indexToRoot,
			staticIndex,
			localeAdmin,
			localeFront,
			tplSolution,
			finalStep,
			afterCheck,
			relativePath,
			resycleOn);
	}

	/* [CONSTRUCTOR MARKER END] */

}