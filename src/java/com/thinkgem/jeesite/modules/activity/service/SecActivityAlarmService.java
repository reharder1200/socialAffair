/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityAlarm;
import com.thinkgem.jeesite.modules.activity.dao.SecActivityAlarmDao;

/**
 * 活动提醒Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecActivityAlarmService extends CrudService<SecActivityAlarmDao, SecActivityAlarm> {

	public SecActivityAlarm get(String id) {
		return super.get(id);
	}
	
	public List<SecActivityAlarm> findList(SecActivityAlarm secActivityAlarm) {
		return super.findList(secActivityAlarm);
	}
	
	public Page<SecActivityAlarm> findPage(Page<SecActivityAlarm> page, SecActivityAlarm secActivityAlarm) {
		return super.findPage(page, secActivityAlarm);
	}
	
	@Transactional(readOnly = false)
	public void save(SecActivityAlarm secActivityAlarm) {
		super.save(secActivityAlarm);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecActivityAlarm secActivityAlarm) {
		super.delete(secActivityAlarm);
	}
	
}