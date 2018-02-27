package com.jeecms.cms.dao.assist;

import java.util.List;

import com.jeecms.cms.entity.assist.CmsVoteTopic;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface CmsVoteTopicDao {
	public Pagination getPage(Integer siteId,Short statu, int pageNo, int pageSize);
	
	public List<CmsVoteTopic> getList(Boolean def,Integer siteId,Integer first, int count);

	public CmsVoteTopic getDefTopic(Integer siteId);

	public CmsVoteTopic findById(Integer id);

	public CmsVoteTopic save(CmsVoteTopic bean);

	public CmsVoteTopic updateByUpdater(Updater<CmsVoteTopic> updater);

	public CmsVoteTopic deleteById(Integer id);
}