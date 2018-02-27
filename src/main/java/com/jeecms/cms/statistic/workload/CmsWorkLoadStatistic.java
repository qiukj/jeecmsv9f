package com.jeecms.cms.statistic.workload;

import java.text.NumberFormat;
import java.util.Date;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.core.entity.CmsUser;

/**
 * @author Tom
 */
public class CmsWorkLoadStatistic {
	public static final String PERCENTSIGN = "%";
	public static final String TIMEPATTERN = "HH:mm:ss";
	public static final double COEFFICIENT = 0.8;
	/**
	 * 
	 * 统计模式
	 * 
	 */
	public static enum CmsWorkLoadStatisticGroup {
		day,week,month,	year
	}
	public static enum CmsWorkLoadStatisticDateKind {
		release,check
	}
	
	public String getPercent() {
		return NumberFormat.getPercentInstance().format(
				count / (total == 0 ? 1.0 : total + 0.0));
	}

	public String getBarWidth() {
		return (int) ((Integer.parseInt(getPercent().replace(PERCENTSIGN, ""))) * COEFFICIENT)
				+ PERCENTSIGN;
	}

	
	public CmsWorkLoadStatistic() {
		super();
	}

	public CmsWorkLoadStatistic(Channel channel, Date date, Long count) {
		super();
		this.channel = channel;
		this.date = date;
		this.count = count;
	}

	public CmsWorkLoadStatistic(Channel channel, CmsUser author,
			CmsUser reviewer, Long count) {
		super();
		this.channel = channel;
		this.author = author;
		this.reviewer = reviewer;
		this.count = count;
	}

	public CmsWorkLoadStatistic(CmsUser author, CmsUser reviewer, Date date,
			Long count) {
		super();
		this.author = author;
		this.reviewer = reviewer;
		this.date = date;
		this.count = count;
	}

	public CmsWorkLoadStatistic(String description,Channel channel, CmsUser author,
			CmsUser reviewer, Date date, Long count,Long total) {
		super();
		this.channel = channel;
		this.author = author;
		this.reviewer = reviewer;
		this.date = date;
		this.count = count;
		this.total=total;
		this.description=description;
	}

	private Channel channel;
	private CmsUser author;
	private CmsUser reviewer;
	private Date date;
	private Long count;
	private Long total;
	private String description;


	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public CmsUser getAuthor() {
		return author;
	}

	public void setAuthor(CmsUser author) {
		this.author = author;
	}

	public CmsUser getReviewer() {
		return reviewer;
	}

	public void setReviewer(CmsUser reviewer) {
		this.reviewer = reviewer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
