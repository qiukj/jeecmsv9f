package com.jeecms.cms.api.front;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.lucene.LuceneContentSvc;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class SearchApiAct {
	
	/**
	 * 全文搜索API
	 * @param siteId 站点ID 非必选 默认当前站
	 * @param channelId 栏目ID  非必选
	 * @param format 格式 非必选  1简化 2全   默认1
	 * @param q 关键词  必选
	 * @param workplace 工作地点   非必选
	 * @param category 分类  非必选
	 * @param queryBeginDate 开始日期("2016-10-10")   非必选
	 * @param queryEndDate 结束日期("2016-11-10")  非必选
	 * @param first 开始    非必选 默认0
	 * @param count 数量  非必选 默认10
	 */
	@RequestMapping(value = "/content/search")
	public void search(Integer siteId,Integer channelId,Integer format,
			String q,String workplace,String category,String queryBeginDate,
			String queryEndDate,Integer first,Integer count,Integer https,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		if(format==null){
			format=Content.CONTENT_INFO_SIMPLE;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if(siteId==null){
			siteId=CmsUtils.getSiteId(request);
		}
		List<Content> list = null;
		Date startDate=null,endDate=null;
		if(StringUtils.isNotBlank(queryBeginDate)){
			startDate=DateUtils.parseDayStrToDate(queryBeginDate);
			if(startDate!=null){
				startDate=DateUtils.getStartDate(startDate);
			}
		}
		if(StringUtils.isNotBlank(queryEndDate)){
			endDate =DateUtils.parseDayStrToDate(queryEndDate);
			if(endDate!=null){
				endDate=DateUtils.getFinallyDate(endDate);
			}
		}
		try {
			String path = realPathResolver.get(com.jeecms.cms.Constants.LUCENE_PATH);
			list = luceneContentSvc.searchList(path, q,category,workplace,
					siteId, channelId,startDate, endDate, first, count);
		} catch (Exception e) {
			//throw new RuntimeException(e);
		}
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(format,https,false,true, false));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private LuceneContentSvc luceneContentSvc;
	@Autowired
	private RealPathResolver realPathResolver;
}

