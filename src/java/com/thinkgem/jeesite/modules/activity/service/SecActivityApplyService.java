/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityApply;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.activity.dao.SecActivityApplyDao;

/**
 * 活动应用Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecActivityApplyService extends CrudService<SecActivityApplyDao, SecActivityApply> {

	public SecActivityApply get(String id) {
		return super.get(id);
	}
	
	public List<SecActivityApply> findList(SecActivityApply secActivityApply) {
		return super.findList(secActivityApply);
	}
	
	public Page<SecActivityApply> findPage(Page<SecActivityApply> page, SecActivityApply secActivityApply) {
		return super.findPage(page, secActivityApply);
	}
	
	@Transactional(readOnly = false)
	public void save(SecActivityApply secActivityApply) {
		super.save(secActivityApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecActivityApply secActivityApply) {
		super.delete(secActivityApply);
	}

	@Transactional(readOnly = false)
	public void joinActivity(Map<String, Object> map, HttpServletRequest request) {
		 
		String activityId = request.getParameter("activityId");
		String userId = request.getParameter("userId");
		String isFlag = request.getParameter("isFlag");
		
		SecActivityApply applySelect = new SecActivityApply();
		applySelect.setUserId(userId);
		applySelect.setActivityId(activityId);
		List<SecActivityApply>  list = this.findList(applySelect);
		SecActivityApply apply = null;
		if(list!=null&&list.size()>0) {
			apply = list.get(0);
			apply.setSignType(isFlag);
			User user = new User();
			user.setId(userId);
			apply.setUser(user);
			apply.setSignDate(new Date());
			
			save(apply);
		}
	
		
	}
	
}