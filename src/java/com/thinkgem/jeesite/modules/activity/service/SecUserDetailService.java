/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.entity.SecUser;
import com.thinkgem.jeesite.modules.activity.entity.SecUserDetail;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.activity.dao.SecUserDetailDao;

/**
 * 用户实名信息表Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecUserDetailService extends CrudService<SecUserDetailDao, SecUserDetail> {

	@Autowired
	private SecUserService secUserService;
	
	public SecUserDetail get(String id) {
		return super.get(id);
	}
	
	public List<SecUserDetail> findList(SecUserDetail secUserDetail) {
		return super.findList(secUserDetail);
	}
	
	public Page<SecUserDetail> findPage(Page<SecUserDetail> page, SecUserDetail secUserDetail) {
		return super.findPage(page, secUserDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(SecUserDetail secUserDetail) {
		super.save(secUserDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecUserDetail secUserDetail) {
		super.delete(secUserDetail);
	}
	
	@Transactional(readOnly = false)
	public void updateUser(Map<String, String> map, HttpServletRequest request) {
		    	
		//获取前台传值  实名表
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		String phone = request.getParameter("phone");
//		String email = request.getParameter("email");
		//默认身份证
		String certificateType = request.getParameter("certificateType");
		
		String certificateNum = request.getParameter("IDCard");
		String address = request.getParameter("address");
		String reserverName = request.getParameter("linkMan");
		String reserverMobile = request.getParameter("reserverMobile");
		//获取前台传值  基本表
		String mobile = request.getParameter("mobile");
		String nickname = request.getParameter("nickName");
		String wx_sign = request.getParameter("wxSign");
		
		//查询是否已经编辑基础信息
		String userId = request.getParameter("userId");
		SecUserDetail secUserDetail = new SecUserDetail();
		secUserDetail.setSelectUserId(userId);
		List<SecUserDetail> list = this.findList(secUserDetail);
		
		//查询基础用户
		 
		SecUser secUser = secUserService.get(userId);
		
		if(list!=null&&list.size()>0) {
			SecUserDetail secUserDetail1 = list.get(0);
			secUserDetail1.setName(name);
			secUserDetail1.setSex(sex);
			secUserDetail1.setAge(age);
			secUserDetail1.setPhone(phone);
//			secUserDetail1.setEmail(email);
			secUserDetail1.setCertificateType(certificateType);
			secUserDetail1.setCertificateNum(certificateNum);
			secUserDetail1.setAddress(address);
			secUserDetail1.setReserverName(reserverName);
			secUserDetail1.setReserverMobile(reserverMobile);
			User user = new User();
			user.setId(userId);
			secUserDetail1.setUser(user);
			this.save(secUserDetail1);
			
			secUser.setMobile(mobile);
			secUser.setNickname(nickname);
			secUser.setWxSign(wx_sign);
			secUserService.save(secUser);
			
		}else {
			SecUserDetail userDetail = new SecUserDetail();
 
			userDetail.setName(name);
			userDetail.setSex(sex);
			userDetail.setAge(age);
			userDetail.setPhone(phone);
//			userDetail.setEmail(email);
			userDetail.setCertificateType(certificateType);
			userDetail.setCertificateNum(certificateNum);
			userDetail.setAddress(address);
			userDetail.setReserverName(reserverName);
			userDetail.setReserverMobile(reserverMobile);
			User user = new User();
			user.setId(userId);
			userDetail.setUser(user);
			this.save(userDetail);
			
			secUser.setMobile(mobile);
			secUser.setNickname(nickname);
			secUser.setWxSign(wx_sign);
			secUserService.save(secUser);
			
		}
		
	}

	public void showUser(Map<String, Object> map, HttpServletRequest request) {
		
		//查询是否已经编辑基础信息
		String userId = request.getParameter("userId");
		SecUserDetail secUserDetail = new SecUserDetail();
		secUserDetail.setSelectUserId(userId);
		List<SecUserDetail> list = this.findList(secUserDetail);
		
		//查询基础用户
		SecUser secUser = secUserService.get(userId);
		
		if(list!=null) {
			Map<String, String> userMap =  new HashMap<>();
			SecUserDetail detail =  list.get(0);
			userMap.put("name", detail.getName());
			userMap.put("sex", detail.getSex());
			userMap.put("age", detail.getAge());
			userMap.put("phone", detail.getPhone());
			userMap.put("certificateType", detail.getCertificateType());
			userMap.put("IDCard", detail.getCertificateNum());
			userMap.put("address", detail.getAddress());
			userMap.put("reserverName", detail.getReserverName());
			userMap.put("reserverMobile", detail.getReserverMobile());
			userMap.put("mobile", secUser.getMobile());
			userMap.put("nickName", secUser.getNickname());
			userMap.put("wxSign", secUser.getWxSign());
			map.put("user", userMap);
		}
	}

	public void showBrieflyUser(Map<String, Object> map, HttpServletRequest request) {
				
		String userId = request.getParameter("userId");
		//查询基础用户
		SecUser secUser = secUserService.get(userId);
		Map<String, String> userMap =  new HashMap<>();
		userMap.put("nickname", secUser.getNickname());
		userMap.put("avatar", secUser.getAvatar());
		map.put("user", userMap);
	}
	
}