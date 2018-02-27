package com.jeecms.cms.manager.assist;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jeecms.cms.entity.assist.CmsVoteItem;
import com.jeecms.cms.entity.assist.CmsVoteSubTopic;
import com.jeecms.cms.entity.assist.CmsVoteTopic;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;

public interface CmsVoteTopicMng {
	public Pagination getPage(Integer siteId,Short statu, int pageNo, int pageSize);
	
	public List<CmsVoteTopic> getList(Boolean def,Integer siteId,
			Integer first,int count);

	public CmsVoteTopic findById(Integer id);

	public CmsVoteTopic getDefTopic(Integer siteId);

	public CmsVoteTopic vote(Integer topicId,Integer[]subIds, List<Integer[]> itemIds,String[]replys, CmsUser user,
			String ip, String cookie);

	public CmsVoteTopic save(CmsVoteTopic bean, Set<CmsVoteSubTopic> subTopics,Map<Integer,Set<CmsVoteItem>>items);
	
	public CmsVoteTopic save(CmsVoteTopic bean, Set<CmsVoteSubTopic> subTopics);
	
	public CmsVoteTopic save(CmsVoteTopic bean);

	public CmsVoteTopic update(CmsVoteTopic bean);
	
	public CmsVoteTopic update(CmsVoteTopic bean, Set<CmsVoteSubTopic> subTopics,Map<Integer,Set<CmsVoteItem>>items);
	
	public void updatePriority(Integer[] id,Integer defId,Boolean[] disabled,Integer siteId);

	public CmsVoteTopic deleteById(Integer id);

	public CmsVoteTopic[] deleteByIds(Integer[] ids);
}