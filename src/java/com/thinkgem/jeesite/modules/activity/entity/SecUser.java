/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户基本信息表Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecUser extends DataEntity<SecUser> {
	
	private static final long serialVersionUID = 1L;
	private String mobile;		// mobile
	private String nickname;		// nickname
	private String wxSign;		// wx_sign
	private String wxOpenid;		// wx_openid
	private String avatar;		// avatar
	private String userType;		// user_type
	private String isRealName;		// is_real_name
	private String status;		// status
	private Date registerDate;		// register_date
	
	public SecUser() {
		super();
	}

	public SecUser(String id){
		super(id);
	}

	@Length(min=0, max=15, message="mobile长度必须介于 0 和 15 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=100, message="nickname长度必须介于 0 和 100 之间")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Length(min=0, max=200, message="wx_sign长度必须介于 0 和 200 之间")
	public String getWxSign() {
		return wxSign;
	}

	public void setWxSign(String wxSign) {
		this.wxSign = wxSign;
	}
	
	@Length(min=0, max=100, message="wx_openid长度必须介于 0 和 100 之间")
	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}
	
	@Length(min=0, max=1000, message="avatar长度必须介于 0 和 1000 之间")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Length(min=0, max=10, message="user_type长度必须介于 0 和 10 之间")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@Length(min=0, max=10, message="is_real_name长度必须介于 0 和 10 之间")
	public String getIsRealName() {
		return isRealName;
	}

	public void setIsRealName(String isRealName) {
		this.isRealName = isRealName;
	}
	
	@Length(min=0, max=10, message="status长度必须介于 0 和 10 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
}