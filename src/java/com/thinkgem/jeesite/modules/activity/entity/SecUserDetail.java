/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户实名信息表Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecUserDetail extends DataEntity<SecUserDetail> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// user_id
	private String name;		// name
	private String sex;		// sex
	private String age;		// age
	private String phone;		// phone
	private String email;		// email
	private String certificateType;		// certificate_type
	private String certificateNum;		// certificate_num
	private String address;		// address
	private String reserverName;		// reserver_name
	private String reserverMobile;		// reserver_mobile
	private String selectUserId;
	
	public SecUserDetail() {
		super();
	}

	public SecUserDetail(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=100, message="name长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=2, message="sex长度必须介于 0 和 2 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=4, message="age长度必须介于 0 和 4 之间")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	@Length(min=0, max=15, message="phone长度必须介于 0 和 15 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=100, message="email长度必须介于 0 和 100 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=2, message="certificate_type长度必须介于 0 和 2 之间")
	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	
	@Length(min=0, max=100, message="certificate_num长度必须介于 0 和 100 之间")
	public String getCertificateNum() {
		return certificateNum;
	}

	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	
	@Length(min=0, max=300, message="address长度必须介于 0 和 300 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=100, message="reserver_name长度必须介于 0 和 100 之间")
	public String getReserverName() {
		return reserverName;
	}

	public void setReserverName(String reserverName) {
		this.reserverName = reserverName;
	}
	
	@Length(min=0, max=15, message="reserver_mobile长度必须介于 0 和 15 之间")
	public String getReserverMobile() {
		return reserverMobile;
	}

	public void setReserverMobile(String reserverMobile) {
		this.reserverMobile = reserverMobile;
	}

	public String getSelectUserId() {
		return selectUserId;
	}

	public void setSelectUserId(String selectUserId) {
		this.selectUserId = selectUserId;
	}
	
}