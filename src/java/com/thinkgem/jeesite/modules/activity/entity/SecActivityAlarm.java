/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 活动提醒Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecActivityAlarm extends DataEntity<SecActivityAlarm> {
	
	private static final long serialVersionUID = 1L;
	private String activityId;		// activity_id
	private User user;		// user_id
	private Date alarmDate;		// alarm_date
	
	public SecActivityAlarm() {
		super();
	}

	public SecActivityAlarm(String id){
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}
	
}