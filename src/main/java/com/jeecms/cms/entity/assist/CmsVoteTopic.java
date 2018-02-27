package com.jeecms.cms.entity.assist;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsVoteTopic;
import com.jeecms.common.util.DateUtils;

public class CmsVoteTopic extends BaseCmsVoteTopic {
	private static final long serialVersionUID = 1L;

	public void init() {
		if (getTotalCount() == null) {
			setTotalCount(0);
		}
		if (getMultiSelect() == null) {
			setMultiSelect(1);
		}
		if (getDef() == null) {
			setDef(false);
		}
		if (getDisabled() == null) {
			setDisabled(false);
		}
		if (getRestrictMember() == null) {
			setRestrictMember(false);
		}
		if (getRestrictIp() == null) {
			setRestrictIp(false);
		}
		if (getRestrictCookie() == null) {
			setRestrictCookie(true);
		}
		if(getVoteDay()==null){
			setVoteDay(0);
		}
		if (getLimitWeiXin()==null) {
			setLimitWeiXin(false);
		}
	}
	
	public JSONObject convertToJson(Boolean showSub) 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if(getStartTime()!=null){
			json.put("startTime",DateUtils.parseDateToTimeStr(getStartTime()));
		}else{
			json.put("startTime", "");
		}
		if(getEndTime()!=null){
			json.put("endTime",DateUtils.parseDateToTimeStr(getEndTime()));
		}else{
			json.put("endTime", "");
		}
		if (getRepeateHour()!=null) {
			json.put("repeateHour", getRepeateHour());
		}else{
			json.put("repeateHour", "");
		}
		if (getTotalCount()!=null) {
			json.put("totalCount", getTotalCount());
		}else{
			json.put("totalCount", "");
		}
		if (getRestrictMember()!=null) {
			json.put("restrictMember", getRestrictMember());
		}else{
			json.put("restrictMember", "");
		}
		if (getRestrictIp()!=null) {
			json.put("restrictIp", getRestrictIp());
		}else{
			json.put("restrictIp", "");
		}
		if (getRestrictCookie()!=null) {
			json.put("restrictCookie", getRestrictCookie());
		}else{
			json.put("restrictCookie", "");
		}
		if (getDisabled()!=null) {
			json.put("disabled", getDisabled());
		}else{
			json.put("disabled", "");
		}
		if (getDef()!=null) {
			json.put("def", getDef());
		}else{
			json.put("def", "");
		}
		if (getLimitWeiXin()!=null) {
			json.put("limitWeiXin", getLimitWeiXin());
		}else{
			json.put("limitWeiXin", "");
		}
		if (getVoteDay()!=null) {
			json.put("voteDay", getVoteDay());
		}else{
			json.put("voteDay", "");
		}
		if (showSub) {
			JSONArray subtopics=new JSONArray();
			Set<CmsVoteSubTopic> subtoics=getSubtopics();
			if(subtoics!=null&&subtoics.size()>0){
				Iterator<CmsVoteSubTopic> subTopicIt=subtoics.iterator();
				int i=0;
				while(subTopicIt.hasNext()){
					JSONObject subTopicJson=new JSONObject();
					CmsVoteSubTopic sub=subTopicIt.next();
					Set<CmsVoteItem>items=sub.getVoteItems();
					if(items!=null&&items.size()>0){
						Iterator<CmsVoteItem> itemIt=items.iterator();
						JSONArray itemsArray=new JSONArray();
						int j=0;
						while(itemIt.hasNext()){
							CmsVoteItem item=itemIt.next();
							JSONObject itemJson=new JSONObject();
							if (item.getId()!=null) {
								itemJson.put("id", item.getId());
							}else{
								itemJson.put("id", "");
							}
							itemJson.put("percent", item.getPercent());
							if (StringUtils.isNotBlank(item.getTitle())) {
								itemJson.put("title", item.getTitle());
							}else{
								itemJson.put("title", "");
							}
							if (item.getVoteCount()!=null) {
								itemJson.put("voteCount", item.getVoteCount());
							}else{
								itemJson.put("voteCount", "");
							}
							if (item.getPriority()!=null) {
								itemJson.put("priority", item.getPriority());
							}else{
								itemJson.put("priority", "");
							}
							if(StringUtils.isNotBlank(item.getPicture())){
								itemJson.put("picture", item.getPicture());
							}else{
								itemJson.put("picture", "");
							}
							itemsArray.put(j++,itemJson);
						}
						subTopicJson.put("voteItems", itemsArray);
					}else{
						subTopicJson.put("voteItems", "");
					}
					Set<CmsVoteReply>replys=sub.getVoteReplys();
					if(replys!=null&&replys.size()>0){
						Iterator<CmsVoteReply> replyIt=replys.iterator();
						JSONArray replysArray=new JSONArray();
						int j=0;
						while(replyIt.hasNext()){
							CmsVoteReply reply=replyIt.next();
							JSONObject replyJson=new JSONObject();
							if (reply.getId()!=null) {
								replyJson.put("id", reply.getId());
							}else{
								replyJson.put("id", "");
							}
							if (StringUtils.isNotBlank(reply.getReply())) {
								replyJson.put("reply", reply.getReply());
							}else{
								replyJson.put("reply", "");
							}
							replysArray.put(j++,replyJson);
						}
						subTopicJson.put("voteReplys", replysArray);
					}else{
						subTopicJson.put("voteReplys", "");
					}
					if (StringUtils.isNotBlank(sub.getTitle())) {
						subTopicJson.put("title", sub.getTitle());
					}else{
						subTopicJson.put("title", "");
					}
					if (sub.getType()!=null) {
						subTopicJson.put("type", sub.getType());
					}else{
						subTopicJson.put("type", "");
					}
					if (sub.getPriority()!=null) {
						subTopicJson.put("priority", sub.getPriority());
					}else{
						subTopicJson.put("priority", "");
					}
					if (sub.getId()!=null) {
						subTopicJson.put("id", sub.getId());
					}else{
						subTopicJson.put("id", "");
					}
					subtopics.put(i++, subTopicJson);
				}
			}
			json.put("subtopics", subtopics);
		}
		json.put("voteStatus", getStatus());
		
		return json;
	}
	
	/**
	 * 获取投票状态 1未开始 2进行中 3已结束
	 */
	public Byte getStatus(){
		Date currentTime = new Date();
		if (getStartTime()!=null) {
			if (currentTime.before(getStartTime())) {//未开始
				return 1;
			}else {
				if (getEndTime()==null) {//进行中(无结束时间)
					return 2;
				}else{
					if (currentTime.getTime()<getEndTime().getTime()) {
						return 2;//进行中
					}else{
						return 3;//已结束
					}
				}
			}
		}else{
			if (getEndTime()==null) {//进行中(无结束时间)
				return 2;
			}else{
				if (currentTime.getTime()<getEndTime().getTime()) {
					return 2;//进行中
				}else{
					return 3;//已结束
				}
			}
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsVoteTopic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsVoteTopic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsVoteTopic (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String title,
		java.lang.Integer totalCount,
		java.lang.Integer multiSelect,
		java.lang.Boolean restrictMember,
		java.lang.Boolean restrictIp,
		java.lang.Boolean restrictCookie,
		java.lang.Boolean disabled,
		java.lang.Boolean def) {

		super (
			id,
			site,
			title,
			totalCount,
			multiSelect,
			restrictMember,
			restrictIp,
			restrictCookie,
			disabled,
			def);
	}
	public void addToSubTopics(CmsVoteSubTopic subTopic){
		Set<CmsVoteSubTopic>subTopics=getSubtopics();
		if(subTopics==null){
			subTopics=new HashSet<CmsVoteSubTopic>();
			setSubtopics(subTopics);
		}
		subTopics.add(subTopic);
	}

	/* [CONSTRUCTOR MARKER END] */

}