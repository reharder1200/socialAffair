/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 活动Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecEvaluate extends DataEntity<SecEvaluate> {
	
	private static final long serialVersionUID = 1L;
	private String activityId;		// activity_id
	private User user;		// user_id
	private String content;		// content
	private String activityScore;		// activity_score
	private String organizerScore;		// organizer_score
	private String isShow;		// is_show
	private String showOrder;		// show_order
	private String userId;
	
	public SecEvaluate() {
		super();
	}

	public SecEvaluate(String id){
		super(id);
	}

	@Length(min=0, max=100, message="activity_id长度必须介于 0 和 100 之间")
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=500, message="content长度必须介于 0 和 500 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=10, message="activity_score长度必须介于 0 和 10 之间")
	public String getActivityScore() {
		return activityScore;
	}

	public void setActivityScore(String activityScore) {
		this.activityScore = activityScore;
	}
	
	@Length(min=0, max=10, message="organizer_score长度必须介于 0 和 10 之间")
	public String getOrganizerScore() {
		return organizerScore;
	}

	public void setOrganizerScore(String organizerScore) {
		this.organizerScore = organizerScore;
	}
	
	@Length(min=0, max=2, message="is_show长度必须介于 0 和 2 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	@Length(min=0, max=10, message="show_order长度必须介于 0 和 10 之间")
	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}