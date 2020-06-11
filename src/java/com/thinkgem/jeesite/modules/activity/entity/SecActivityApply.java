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
 * 活动应用Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecActivityApply extends DataEntity<SecActivityApply> {
	
	private static final long serialVersionUID = 1L;
	private String activityId;		// activity_id
	private User user;		// user_id
	private Date signDate;		// 报名时间
	private String signType;		// 报名状态(0已报名 1已取消)
	private String isEvaluate;		// 是否评价(0否 1是)
	private String userId;
	private String orderId;	//报名订单号
	
	public SecActivityApply() {
		super();
	}

	public SecActivityApply(String id){
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
	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	@Length(min=0, max=2, message="sign_type长度必须介于 0 和 2 之间")
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	
	@Length(min=0, max=2, message="is_evaluate长度必须介于 0 和 2 之间")
	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}