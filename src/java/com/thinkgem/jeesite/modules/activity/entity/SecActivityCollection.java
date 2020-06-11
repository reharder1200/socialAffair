/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 活动收藏Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecActivityCollection extends DataEntity<SecActivityCollection> {
	
	private static final long serialVersionUID = 1L;
	private String activityId;		// activity_id
	private User user;		// user_id
	private String userId;
	
	public SecActivityCollection() {
		super();
	}

	public SecActivityCollection(String id){
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}