package com.jeecms.cms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.jeecms.common.util.ParseURLKeyword.getKeyword;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jeecms.cms.entity.assist.CmsSiteAccess;
import com.jeecms.cms.entity.assist.CmsSiteAccessCountHour;
import com.jeecms.cms.entity.assist.CmsSiteAccessPages;
import com.jeecms.cms.manager.assist.CmsSiteAccessCountHourMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessCountMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessMng;
import com.jeecms.cms.manager.assist.CmsSiteAccessPagesMng;
import com.jeecms.common.ipseek.IpSeekUtils;
import com.jeecms.common.util.DateFormatUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.UserAgentUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.util.CmsUtils;

import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_BAIDU;
import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_GOOGLE;
import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_YAHOO;
import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_BING;
import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_SOGOU;
import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_SOSO;
import static com.jeecms.cms.entity.assist.CmsSiteAccess.ENGINE_SO;

import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_ALL;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_SOURCE;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_ENGINE;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_LINK;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_KEYWORD;
import static com.jeecms.cms.entity.assist.CmsSiteAccessStatistic.STATISTIC_AREA;

@Service
public class CmsSiteFlowCacheImpl implements CmsSiteFlowCache, DisposableBean {
	private final String VISIT_COUNT="visitCount";
	private final String LAST_VISIT_TIME="lastVisitTime";
//	private final String CACHE_KEY_PV_WEEK="pvWeek_";
//	private final String CACHE_KEY_PV_MONTH="pvMonth_";
//	private final String CACHE_KEY_VISITOR_WEEK="visitorWeek_";
//	private final String CACHE_KEY_VISITOR_MONTH="visitorMonth_";
//	private final String CACHE_KEY_PV_DAY="pvDay_";
//	private final String CACHE_KEY_PV_TOTAL="pvTotal_";
//	private final String CACHE_KEY_VISITOR_DAY="visitorDay_";
//	private final String CACHE_KEY_VISITOR_TOTAL="visitorTotal_";
	private final String CACHE_KEY_SPLIT="_";
	private Logger log = LoggerFactory.getLogger(CmsSiteFlowCacheImpl.class);

		
	public Long[] flow(HttpServletRequest request,  String page, String referer) {
		String ip = RequestUtils.getIpAddr(request);
		CmsSite site=CmsUtils.getSite(request);
		String brower = UserAgentUtils.getBrowserInfo(request);
		String operatingSystem = UserAgentUtils.getClientOS(request);
		Date nowTime = DateFormatUtils.parseTime(Calendar.getInstance().getTime());
		Date nowDate = DateUtils.getStartDate(Calendar.getInstance().getTime());
		HttpSession session=request.getSession();
		String sessionId =session.getId();
		Integer visitCount=(Integer) session.getAttribute(VISIT_COUNT);
		Date lastVisitTime=(Date) session.getAttribute(LAST_VISIT_TIME);
		CmsSiteAccess access = null;
		CmsSiteAccess lastAccess = findLastAccess(site.getId());
		CmsSiteAccessPages accessPage;
		boolean firstVisitToday=false;
		boolean newVisitor=false;
		if(visitCount==null){
			visitCount=0;
			lastVisitTime=Calendar.getInstance().getTime();
			access=visitAccess(request, ip, sessionId, page, referer,brower,operatingSystem);
			//最新访问的时间比当前日期要早
			if(lastAccess==null||
					(lastAccess.getAccessDate()!=null&&
					lastAccess.getAccessDate().before(nowDate))){
				firstVisitToday=true;
			}
			newVisitor=true;
		}else{
			access=findAccess(sessionId);
			if(access==null){
				access=visitAccess(request, ip, sessionId, page, referer,brower,operatingSystem);
				newVisitor=true;
			}
			access=updateAccess(access, page, visitCount+1, DateUtils.getSecondBetweenDate(access.getAccessTime(), nowTime));
		}
		accessPage=visitPages(site,page, sessionId, visitCount, lastVisitTime);
		visitCount+=1;
		session.setAttribute(VISIT_COUNT, visitCount);
		session.setAttribute(LAST_VISIT_TIME, Calendar.getInstance().getTime());
		accessCache.put(new Element(sessionId, access));
		accessPageCache.put(new Element(sessionId+visitCount, accessPage));
		lastAccessCache.put(new Element(site.getId(),access));
		//当天第一次访问统计昨日数据
		if(firstVisitToday){
			Thread thread = new StatisticThread(site.getId());
			thread.start();
		}
		return totalCache(site, newVisitor);
	}
	
	/**
	 * 统计当前流量信息入统计表
	 */
	private class StatisticThread extends Thread{
		private Integer siteId;
		
		public StatisticThread(Integer siteId) {
			this.siteId = siteId;
		}

		public void run() {
			Date today=Calendar.getInstance().getTime();
			//统计最近
			CmsSiteAccess latestBefore=cmsSiteAccessMng.findRecentAccess(today, this.siteId);
			if(latestBefore!=null){
				Date recent = DateUtils.getStartDate(latestBefore.getAccessDate());
				//每日总流量统计
				cmsSiteAccessMng.statisticByProperty(STATISTIC_ALL, recent, this.siteId);
				//地区统计
				cmsSiteAccessMng.statisticByProperty(STATISTIC_AREA, recent, this.siteId);
				//来源统计
				cmsSiteAccessMng.statisticByProperty(STATISTIC_SOURCE, recent, this.siteId);
				//搜索引擎统计
				cmsSiteAccessMng.statisticByProperty(STATISTIC_ENGINE, recent, this.siteId);
				//外部链接统计
				cmsSiteAccessMng.statisticByProperty(STATISTIC_LINK, recent, this.siteId);
				//关键词统计
				cmsSiteAccessMng.statisticByProperty(STATISTIC_KEYWORD, recent, this.siteId);
				//访问页数情况统计
				cmsSiteAccessCountMng.statisticCount(recent, this.siteId);
				//统计时间段数据
				siteAccessCountHourMng.statisticCount(recent, siteId);
				//清除以往数据
				Date d=DateUtils.getStartDate(today);
				cmsSiteAccessMng.clearByDate(d);
				cmsSiteAccessPagesMng.clearByDate(d);
			}
		}
	}
	
	public Long[] totalCache(CmsSite site,boolean newVisitor) {
		Long pvTotal=site.getPvTotal();
		Long visitorTotal=site.getVisitorTotal();
		Long dayPvTotal=site.getDayPvTotal();
		Long dayVisitorTotal=site.getDayVisitorTotal();
		Long weekPvTotal=site.getWeekPvTotal();
		Long weekVisitorTotal=site.getWeekVisitorTotal();
		Long monthPvTotal=site.getMonthPvTotal();
		Long monthVisitorTotal=site.getMonthVisitorTotal();
		Element pvCache = pvTotalCache.get(CmsSite.PV_TOTAL+CACHE_KEY_SPLIT+site.getId());
		Element pvWeekCache = pvTotalCache.get(CmsSite.WEEK_PV_TOTAL+CACHE_KEY_SPLIT+site.getId());
		Element pvMonthCache = pvTotalCache.get(CmsSite.MONTH_PV_TOTAL+CACHE_KEY_SPLIT+site.getId());
		Long pv,weekPv,monthPv;
		if (pvCache != null) {
			pv = (Long) pvCache.getObjectValue() + 1;
		} else {
			pv = 1l;
		}
		if (pvWeekCache != null) {
			weekPv = (Long) pvWeekCache.getObjectValue() + 1;
		} else {
			weekPv = 1l;
		}
		if (pvMonthCache != null) {
			monthPv = (Long) pvMonthCache.getObjectValue() + 1;
		} else {
			monthPv = 1l;
		}
		Element dayPvCache = dayPvTotalCache.get(CmsSite.DAY_PV_TOTAL+CACHE_KEY_SPLIT+site.getId());
		Long dayPv;
		if (dayPvCache != null) {
			dayPv = (Long) dayPvCache.getObjectValue() + 1;
		} else {
			dayPv = 1l;
		}
		Long visitor,weekVisitor,monthVisitor;
		Element visitorCache = visitorTotalCache.get(CmsSite.VISITORS+CACHE_KEY_SPLIT+site.getId());
		Element visitorWeekCache = visitorTotalCache.get(CmsSite.WEEK_VISITORS+CACHE_KEY_SPLIT+site.getId());
		Element visitorMonthCache = visitorTotalCache.get(CmsSite.MONTH_VISITORS+CACHE_KEY_SPLIT+site.getId());
		if (visitorCache != null) {
			if(newVisitor){
				visitor = (Long) visitorCache.getObjectValue() + 1;
			}else{
				visitor = (Long) visitorCache.getObjectValue();
			}
		}else{
			if(newVisitor){
				visitor=1l;
			}else{
				visitor = 0l;
			}
		}
		if (visitorWeekCache != null) {
			if(newVisitor){
				weekVisitor = (Long) visitorWeekCache.getObjectValue() + 1;
			}else{
				weekVisitor = (Long) visitorWeekCache.getObjectValue();
			}
		}else{
			if(newVisitor){
				weekVisitor=1l;
			}else{
				weekVisitor = 0l;
			}
		}
		if (visitorMonthCache != null) {
			if(newVisitor){
				monthVisitor = (Long) visitorMonthCache.getObjectValue() + 1;
			}else{
				monthVisitor = (Long) visitorMonthCache.getObjectValue();
			}
		}else{
			if(newVisitor){
				monthVisitor=1l;
			}else{
				monthVisitor = 0l;
			}
		}
		Long dayVisitor;
		Element dayVisitorCache = dayVisitorTotalCache.get(CmsSite.DAY_VISITORS+CACHE_KEY_SPLIT+site.getId());
		if (dayVisitorCache != null) {
			if(newVisitor){
				dayVisitor = (Long) dayVisitorCache.getObjectValue() + 1;
			}else{
				dayVisitor = (Long) dayVisitorCache.getObjectValue();
			}
		}else{
			if(newVisitor){
				dayVisitor=1l;
			}else{
				dayVisitor = 0l;
			}
		}
		pvTotalCache.put(new Element(CmsSite.PV_TOTAL+CACHE_KEY_SPLIT+site.getId(), pv));
		visitorTotalCache.put(new Element(CmsSite.VISITORS+CACHE_KEY_SPLIT+site.getId(), visitor));
		dayPvTotalCache.put(new Element(CmsSite.DAY_PV_TOTAL+CACHE_KEY_SPLIT+site.getId(), dayPv));
		dayVisitorTotalCache.put(new Element(CmsSite.DAY_VISITORS+CACHE_KEY_SPLIT+site.getId(), dayVisitor));
		pvTotalCache.put(new Element(CmsSite.WEEK_PV_TOTAL+CACHE_KEY_SPLIT+site.getId(), weekPv));
		pvTotalCache.put(new Element(CmsSite.MONTH_PV_TOTAL+CACHE_KEY_SPLIT+site.getId(), monthPv));
		visitorTotalCache.put(new Element(CmsSite.WEEK_VISITORS+CACHE_KEY_SPLIT+site.getId(), weekVisitor));
		visitorTotalCache.put(new Element(CmsSite.MONTH_VISITORS+CACHE_KEY_SPLIT+site.getId(), monthVisitor));
		refreshToDB();
		return new Long[] { pv +pvTotal, visitor+visitorTotal,
				dayPv+dayPvTotal, dayVisitor+dayVisitorTotal,
				weekPv+weekPvTotal,weekVisitor+weekVisitorTotal,
				monthPv+monthPvTotal,monthVisitor+monthVisitorTotal};
	}
	
	private CmsSiteAccess visitAccess(HttpServletRequest request,String ip, String sessionId, String page, String referer,String browser,String operatingSystem){
		CmsSite site =CmsUtils.getSite(request);
		String accessSource=getSource(request, referer);
		CmsSiteAccess bean=new CmsSiteAccess();
		Date now=Calendar.getInstance().getTime();
		bean.setAccessDate(now);
		bean.setAccessSource(accessSource);
		if(accessSource.equals(getMessage(request,"cmsAccess.externallink"))){
			bean.setExternalLink(getRefererWebSite(referer));
		}
		if(enterFromEngine(request, referer)){
			bean.setEngine(getEngine(request, referer));
		}
		bean.setAccessTime(now);
		bean.setIp(ip);
		bean.setArea(IpSeekUtils.getIpProvinceBySina(ip));
		bean.setBrowser(browser);
		bean.setEntryPage(page);
		bean.setKeyword(getKeyword(referer));
		bean.setLastStopPage(page);
		bean.setOperatingSystem(operatingSystem);
		bean.setSessionId(sessionId);
		bean.setSite(site);
		bean.setVisitPageCount(1);
		bean.setVisitSecond(0);
		return bean;
	}
	
	private CmsSiteAccess updateAccess(CmsSiteAccess bean,String lastStopPage,int visitPageCount,Integer visitSecond){
		bean.setLastStopPage(lastStopPage);
		bean.setVisitPageCount(visitPageCount);
		bean.setVisitSecond(visitSecond);
		return bean;
	}
	
	private CmsSiteAccess findAccess(String sessionId){
		Element accessElement=accessCache.get(sessionId);
		if(accessElement!=null){
			return (CmsSiteAccess) accessElement.getObjectValue();
		}else{
			CmsSiteAccess access=cmsSiteAccessMng.findAccessBySessionId(sessionId);
			return access;
		}
	}
	

	
	private CmsSiteAccess findLastAccess(Integer siteId){
		Element accessElement=lastAccessCache.get(siteId);
		CmsSiteAccess lastAccess =null;
		if(accessElement!=null){
			lastAccess=(CmsSiteAccess) accessElement.getObjectValue();
		}
		return lastAccess;
		 
	}
	
	private CmsSiteAccessPages visitPages(CmsSite site,String page,String sessionId,Integer hasVisitCount,Date lastVisitTime) {
		CmsSiteAccessPages bean = new CmsSiteAccessPages();
		Date time = DateFormatUtils.parseTime(Calendar.getInstance().getTime());
		Date date = DateUtils.getStartDate(Calendar.getInstance().getTime());
		bean.setAccessPage(page);
		bean.setAccessTime(time);
		bean.setAccessDate(date);
		bean.setSite(site);
		bean.setSessionId(sessionId);
		//设置当前访问时间0，设置上次时间
		bean.setVisitSecond(0);
		bean.setPageIndex(hasVisitCount+1);
		//accessPageCache key为sessionid+访问页面顺序
		String prePageKey=sessionId+hasVisitCount;
		Element pageElement=accessPageCache.get(prePageKey);
		//修改上个页面的访问时间(更新缓存)
		CmsSiteAccessPages prePage=null;
		String prePageCacheKey;
		if(pageElement==null){
			prePage=cmsSiteAccessPagesMng.findAccessPage(sessionId, hasVisitCount);
			prePageCacheKey=sessionId+hasVisitCount;
		}else{
			prePage=(CmsSiteAccessPages) pageElement.getObjectValue();
			prePageCacheKey=(String) pageElement.getObjectKey();
		}
		if(prePage!=null){
			prePage.setVisitSecond(DateUtils.getSecondBetweenDate(prePage.getAccessTime(), time));
			accessPageCache.put(new Element(prePageCacheKey,prePage));
		}
		return bean;
	}
	
	
	private void refreshToDB() {
		long time = System.currentTimeMillis();
		if (time > refreshTime + interval) {
			refreshTime = time;
			
			freshSiteAttrCacheToDB(pvTotalCache,visitorTotalCache,dayPvTotalCache,dayVisitorTotalCache);
			
			int accessCount = freshAccessCacheToDB(accessCache);
			int pagesCount = freshAccessPagesCacheToDB(accessPageCache);
			// 清除缓存
			pvTotalCache.removeAll();
			visitorTotalCache.removeAll();
			dayPvTotalCache.removeAll();
			dayVisitorTotalCache.removeAll();
			accessCache.removeAll();
			accessPageCache.removeAll();
			log.info("refresh cache access to DB: {}", accessCount);
			log.info("refresh cache pages to DB: {}", pagesCount);
		}
	}
	
	private int freshAccessCacheToDB(Ehcache cache) {
		int count = 0;
		List<String> list = cache.getKeys();
		for (String key : list) {
			Element element = cache.get(key);
			if (element == null) {
				return count;
			}
			CmsSiteAccess access = (CmsSiteAccess) element.getObjectValue();
			if(access!=null){
				cmsSiteAccessMng.saveOrUpdate(access);
			}
		}
		return count;
	}
	
	private int freshAccessPagesCacheToDB(Ehcache cache){
		int count = 0;
		List<String> list = cache.getKeys();
		for (String key : list) {
			Element element = cache.get(key);
			if (element == null) {
				return count;
			}
			CmsSiteAccessPages page = (CmsSiteAccessPages) element.getObjectValue();
			if (page.getId() == null&& page.getSessionId() != null) {
				if(page.getAccessDate()==null){
					page.setAccessDate(Calendar.getInstance().getTime());
				}
				cmsSiteAccessPagesMng.save(page);
			}else{
				cmsSiteAccessPagesMng.update(page);
			}
		}
		return count;
	}
	
	private void freshSiteAttrCacheToDB(Ehcache... caches) {
		CmsConfig config = cmsConfigMng.get();
		//清除日访问、周访问、月访问信息
		clearFlowInfo(config);
		for(Ehcache cache:caches){
			List<String> list = cache.getKeys();
			Map<String,String>attr=new HashMap<String, String>();
		//	CmsSite site=CmsThreadVariable.getSite();
			for (String key : list) {
				Element element = cache.get(key);
				String[]str=key.split(CACHE_KEY_SPLIT);
				String property=str[0];
				Integer siteId=Integer.parseInt(str[1]);
				CmsSite site=cmsSiteMng.findById(siteId);
				Long total = 0l;
				if(key.startsWith(CmsSite.PV_TOTAL)){
					total= (Long) element.getObjectValue()+site.getPvTotal();
				}else if(key.startsWith(CmsSite.VISITORS)){
					total = (Long) element.getObjectValue()+site.getVisitorTotal();
				}else if(key.startsWith(CmsSite.DAY_PV_TOTAL)){
					total = (Long) element.getObjectValue()+site.getDayPvTotal();
				}else if(key.startsWith(CmsSite.DAY_VISITORS)){
					total = (Long) element.getObjectValue()+site.getDayVisitorTotal();
				}else if(key.startsWith(CmsSite.WEEK_PV_TOTAL)){
					total = (Long) element.getObjectValue()+site.getWeekPvTotal();
				}else if(key.startsWith(CmsSite.WEEK_VISITORS)){
					total = (Long) element.getObjectValue()+site.getWeekVisitorTotal();
				}else if(key.startsWith(CmsSite.MONTH_PV_TOTAL)){
					total = (Long) element.getObjectValue()+site.getMonthPvTotal();
				}else if(key.startsWith(CmsSite.MONTH_VISITORS)){
					total = (Long) element.getObjectValue()+site.getMonthVisitorTotal();
				}else{
					//Never
				}
				attr.put(property, total.toString());
				cmsSiteMng.updateAttr(siteId, attr);
			}
		}
	}
	
	private void clearFlowInfo(CmsConfig config) {
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		last.setTime(config.getFlowClearTime());
		int currDay = curr.get(Calendar.DAY_OF_YEAR);
		int lastDay = last.get(Calendar.DAY_OF_YEAR);
		
		Map<String,String>attr=new HashMap<String, String>();
		if (currDay != lastDay) {
			int currWeek = curr.get(Calendar.WEEK_OF_YEAR);
			int lastWeek = last.get(Calendar.WEEK_OF_YEAR);
			int currMonth = curr.get(Calendar.MONTH);
			int lastMonth = last.get(Calendar.MONTH);
			attr.put(CmsSite.DAY_PV_TOTAL, "0");
			attr.put(CmsSite.DAY_VISITORS, "0");
			cmsConfigMng.updateFlowClearTime(curr.getTime());
			if(currWeek != lastWeek){
				attr.put(CmsSite.WEEK_PV_TOTAL, "0");
				attr.put(CmsSite.WEEK_VISITORS, "0");
			}
			if(currMonth != lastMonth){
				attr.put(CmsSite.MONTH_PV_TOTAL, "0");
				attr.put(CmsSite.MONTH_VISITORS, "0");
			}
		} 
		List<CmsSite>siteList=cmsSiteMng.getListFromCache();
		for(CmsSite site:siteList){
			cmsSiteMng.updateAttr(site.getId(), attr);
		}
	}
	
	/**
	 * 销毁BEAN时，缓存入库。
	 */
	public void destroy() throws Exception {
		int accessCount = freshAccessCacheToDB(accessCache);
		int pagesCount = freshAccessPagesCacheToDB(accessPageCache);
		freshSiteAttrCacheToDB(pvTotalCache,visitorTotalCache,dayPvTotalCache,dayVisitorTotalCache);
		log.info("Bean destroy.refresh cache access to DB: {}", accessCount);
		log.info("Bean destroy.refresh cache pages to DB: {}", pagesCount);
	}

	
	private  String getRefererWebSite(String referer){
		if(StringUtils.isBlank(referer)){
			return "";
		}
		int start = 0, i = 0, count = 3;
		while (i < count && start != -1) {
			start = referer.indexOf('/', start + 1);
			i++;
		}
		/*
		if (start <= 0) {
			throw new IllegalStateException(
					"referer website uri not like 'http://.../...' pattern: "
							+ referer);
		}
		*/
		if (start > 0) {
			return referer.substring(0, start);
		}else{
			return "";
		}
	}
	
	private  String getSource(HttpServletRequest request,String referer){
		CmsSite site=CmsUtils.getSite(request);
		if(StringUtils.isBlank(referer)){
			return getMessage(request,"cmsAccess.directaccess");
		}
		if(enterFromEngine(request, referer)){
			return getMessage(request,"cmsAccess.engine");
		}else{
			String refererWebSite=getRefererWebSite(referer);
			String refererWebDomain="";
			if(StringUtils.isNotBlank(refererWebDomain)){
				refererWebDomain=refererWebSite.substring(refererWebSite.indexOf('/')+2);
			}
			if(refererWebDomain.indexOf(':')!=-1){
				refererWebDomain=refererWebDomain.substring(0, refererWebDomain.indexOf(':'));
			}
			//本站域名直接访问
			if(site.getDomain().equals(refererWebDomain)||(StringUtils.isNotBlank(site.getDomainAlias())&&site.getDomainAlias().contains(refererWebDomain))){
				return getMessage(request,"cmsAccess.directaccess");
			}else{
				return getMessage(request,"cmsAccess.externallink");
			}
		}
	}
	
	/**
	 * 只支持常用的搜索引擎
	 * @param request
	 * @param referer
	 * @return
	 */
	private boolean enterFromEngine(HttpServletRequest request,String referer){
		if(StringUtils.isBlank(referer)){
			return false;
		}
		if(referer.indexOf(ENGINE_BAIDU)!=-1){
			return true;
		}else if(referer.indexOf(ENGINE_GOOGLE)!=-1){
			return true;
		}else if(referer.indexOf(ENGINE_YAHOO)!=-1){
			return true;
		}else if(referer.indexOf(ENGINE_BING)!=-1){
			return true;
		}else if(referer.indexOf(ENGINE_SOGOU)!=-1){
			return true;
		}else if(referer.indexOf(ENGINE_SOSO)!=-1){
			return true;
		}else if(referer.indexOf(ENGINE_SO)!=-1){
			return true;
		}
		return false;
	}
	
	private  String getEngine(HttpServletRequest request,String referer){
		if(StringUtils.isBlank(referer)){
			return "";
		}
		if(referer.indexOf(ENGINE_BAIDU)!=-1){
			return getMessage(request,"cmsSearch.engine.baidu");
		}else if(referer.indexOf(ENGINE_GOOGLE)!=-1){
			return getMessage(request,"cmsSearch.engine.google");
		}else if(referer.indexOf(ENGINE_YAHOO)!=-1){
			return getMessage(request,"cmsSearch.engine.yahoo");
		}else if(referer.indexOf(ENGINE_BING)!=-1){
			return getMessage(request,"cmsSearch.engine.bing");
		}else if(referer.indexOf(ENGINE_SOGOU)!=-1){
			return getMessage(request,"cmsSearch.engine.sogou");
		}else if(referer.indexOf(ENGINE_SOSO)!=-1){
			return getMessage(request,"cmsSearch.engine.soso");
		}else if(referer.indexOf(ENGINE_SO)!=-1){
			return getMessage(request,"cmsSearch.engine.so");
		}
		return "";
	}
	
	
	private  String getMessage(HttpServletRequest request, String key,
			Object... args) {
		return MessageResolver.getMessage(request, key, args);
	}
	
	// 间隔时间
	private int interval = 1 * 60 * 1000*10; //  10分钟
	// 最后刷新时间
	private long refreshTime = System.currentTimeMillis();
	
	/**
	 * 刷新间隔时间
	 * 
	 * @param interval
	 *            单位秒
	 */
	public void setInterval(int interval) {
		this.interval = interval * 1000;
	}
	
	@Autowired
	private CmsSiteMng cmsSiteMng;
	@Autowired
	private CmsSiteAccessMng cmsSiteAccessMng;
	@Autowired
	private CmsSiteAccessPagesMng cmsSiteAccessPagesMng;
	@Autowired
	private CmsSiteAccessCountMng cmsSiteAccessCountMng;
	@Autowired
	private CmsSiteAccessCountHourMng siteAccessCountHourMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;

	@Autowired 
	@Qualifier("cmsAccessCache")
	private Ehcache accessCache;
	@Autowired 
	@Qualifier("cmsLastAccessCache")
	private Ehcache lastAccessCache;
	@Autowired 
	@Qualifier("cmsAccessPageCache")
	private Ehcache accessPageCache;
	@Autowired 
	@Qualifier("cmsPvTotalCache")
	private Ehcache pvTotalCache;
	@Autowired 
	@Qualifier("cmsVisitorTotalCache")
	private Ehcache visitorTotalCache;
	@Autowired 
	@Qualifier("cmsDayPvTotalCache")
	private Ehcache dayPvTotalCache;
	@Autowired 
	@Qualifier("cmsDayVisitorTotalCache")
	private Ehcache dayVisitorTotalCache;

}
