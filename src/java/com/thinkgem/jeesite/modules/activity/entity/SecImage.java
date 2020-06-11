/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 保存图片Entity
 * @author 张高旗
 * @version 2020-04-05
 */
public class SecImage extends DataEntity<SecImage> {
	
	private static final long serialVersionUID = 1L;
	private String imgPath;		// 图片地址
	private String activityId;		// 活动ID
	
	public SecImage() {
		super();
	}

	public SecImage(String id){
		super(id);
	}

	@Length(min=0, max=500, message="图片地址长度必须介于 0 和 500 之间")
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	@Length(min=0, max=32, message="活动ID长度必须介于 0 和 32 之间")
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
}