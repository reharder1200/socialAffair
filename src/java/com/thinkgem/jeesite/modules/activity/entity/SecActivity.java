/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 活动信息Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecActivity extends DataEntity<SecActivity> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// title
	private String type;		// type
	private Date beginDate;		// begin_date
	private Date endDate;		// end_date
	private String minPeople;		// min_people
	private String maxPeople;		// max_people
	private String province;		// province
	private String city;		// city
	private String district;		// district
	private String locationDetatil;		// location_detatil
	private String activityLat;		// activity_lat
	private String activityLon;		// activity_lon
	private String chargeType;		// charge_type
	private String chargeAmount;		// charge_amount
	private String activityDescription;		// activity_description
	private String picSaveUrl;		// pic_save_url
	private String contactWx;		// contact_wx
	private String contactMobile;		// contact_mobile
	private String activityStatus;		// activity_status
	private String depositStatus;		// deposit_status
	private String orderId;		// order_id
	private String activityStarter;		// activity_starter
	
	private String juli;
	
	private String imageIds;
	
	public SecActivity() {
		super();
	}

	public SecActivity(String id){
		super(id);
	}

	@Length(min=0, max=50, message="title长度必须介于 0 和 50 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=2, message="type长度必须介于 0 和 2 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=0, max=15, message="min_people长度必须介于 0 和 15 之间")
	public String getMinPeople() {
		return minPeople;
	}

	public void setMinPeople(String minPeople) {
		this.minPeople = minPeople;
	}
	
	@Length(min=0, max=15, message="max_people长度必须介于 0 和 15 之间")
	public String getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(String maxPeople) {
		this.maxPeople = maxPeople;
	}
	
	@Length(min=0, max=20, message="province长度必须介于 0 和 20 之间")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=20, message="city长度必须介于 0 和 20 之间")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=20, message="district长度必须介于 0 和 20 之间")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	@Length(min=0, max=100, message="location_detatil长度必须介于 0 和 100 之间")
	public String getLocationDetatil() {
		return locationDetatil;
	}

	public void setLocationDetatil(String locationDetatil) {
		this.locationDetatil = locationDetatil;
	}
	
	@Length(min=0, max=200, message="activity_lat长度必须介于 0 和 200 之间")
	public String getActivityLat() {
		return activityLat;
	}

	public void setActivityLat(String activityLat) {
		this.activityLat = activityLat;
	}
	
	@Length(min=0, max=200, message="activity_lon长度必须介于 0 和 200 之间")
	public String getActivityLon() {
		return activityLon;
	}

	public void setActivityLon(String activityLon) {
		this.activityLon = activityLon;
	}
	
	@Length(min=0, max=2, message="charge_type长度必须介于 0 和 2 之间")
	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	
	@Length(min=0, max=20, message="charge_amount长度必须介于 0 和 20 之间")
	public String getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	@Length(min=0, max=500, message="activity_description长度必须介于 0 和 500 之间")
	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
	
	@Length(min=0, max=1000, message="pic_save_url长度必须介于 0 和 1000 之间")
	public String getPicSaveUrl() {
		return picSaveUrl;
	}

	public void setPicSaveUrl(String picSaveUrl) {
		this.picSaveUrl = picSaveUrl;
	}
	
	@Length(min=0, max=100, message="contact_wx长度必须介于 0 和 100 之间")
	public String getContactWx() {
		return contactWx;
	}

	public void setContactWx(String contactWx) {
		this.contactWx = contactWx;
	}
	
	@Length(min=0, max=20, message="contact_mobile长度必须介于 0 和 20 之间")
	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	
	@Length(min=0, max=2, message="activity_status长度必须介于 0 和 2 之间")
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	@Length(min=0, max=2, message="deposit_status长度必须介于 0 和 2 之间")
	public String getDepositStatus() {
		return depositStatus;
	}

	public void setDepositStatus(String depositStatus) {
		this.depositStatus = depositStatus;
	}
	
	@Length(min=0, max=20, message="order_id长度必须介于 0 和 20 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=0, max=100, message="activity_starter长度必须介于 0 和 100 之间")
	public String getActivityStarter() {
		return activityStarter;
	}

	public void setActivityStarter(String activityStarter) {
		this.activityStarter = activityStarter;
	}

	public String getImageIds() {
		return imageIds;
	}

	public void setImageIds(String imageIds) {
		this.imageIds = imageIds;
	}

	public String getJuli() {
		return juli;
	}

	public void setJuli(String juli) {
		this.juli = juli;
	}
	
}