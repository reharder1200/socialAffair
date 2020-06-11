/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.activity.entity.SecActivity;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityApply;
import com.thinkgem.jeesite.modules.activity.entity.SecEvaluate;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.activity.dao.SecActivityApplyDao;
import com.thinkgem.jeesite.modules.activity.dao.SecActivityDao;
import com.thinkgem.jeesite.modules.activity.dao.SecEvaluateDao;

/**
 * 活动Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecEvaluateService extends CrudService<SecEvaluateDao, SecEvaluate> {


	
	@Autowired
	private SecActivityApplyDao  secActivityApplyDao;
	
	@Autowired
	private SecActivityDao secActivityDao;
	
	public SecEvaluate get(String id) {
		return super.get(id);
	}
	
	public List<SecEvaluate> findList(SecEvaluate secEvaluate) {
		return super.findList(secEvaluate);
	}
	
	public Page<SecEvaluate> findPage(Page<SecEvaluate> page, SecEvaluate secEvaluate) {
		return super.findPage(page, secEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void save(SecEvaluate secEvaluate) {
		super.save(secEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecEvaluate secEvaluate) {
		super.delete(secEvaluate);
	}

	@Transactional(readOnly = false)
	public void evaluateActivity(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String activityId = request.getParameter("activityId");
		String userId = request.getParameter("userId");
		
		//获取评价内容
		String content = request.getParameter("content");
		String activityScore = request.getParameter("activityScore");
		String organizerScore = request.getParameter("organizerScore");
		
		SecEvaluate secEvaluate = new SecEvaluate();
		secEvaluate.setActivityId(activityId);
		User user = new User();
		user.setId(userId);
		secEvaluate.setUser(user);
		secEvaluate.setContent(content);
		secEvaluate.setActivityScore(activityScore);
		secEvaluate.setOrganizerScore(organizerScore);
		save(secEvaluate);
	}

	public void evaluateAList(Map<String, Object> map, HttpServletRequest request) {
		//获取活动ID
		String activityId = request.getParameter("activityId");
		String userId = request.getParameter("userId");
		String pageNo = request.getParameter("pageNo");
		
		SecActivityApply applySelect = new SecActivityApply();
		applySelect.setActivityId(activityId);
		applySelect.setUserId(userId);
		List<SecActivityApply> applyList = secActivityApplyDao.findList(applySelect);
		
		SecActivity secActivity =secActivityDao.get(activityId);
		Map infoMap = new HashMap<String, Object>();
		infoMap.put("title", secActivity.getTitle());
		infoMap.put("activityDescription", secActivity.getActivityDescription());
		infoMap.put("locationDetatil", secActivity.getLocationDetatil());
		infoMap.put("joinNum", applyList.size());
		infoMap.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity.getImageIds());
		String stateString = "报名中";
		if(secActivity.getActivityStatus()!=null&&!secActivity.getActivityStatus().equals("")) {
			switch (secActivity.getActivityStatus()) {
			case "0":
				stateString =  "报名中";
				break;
			case "1":
				stateString =  "已取消";
				break;
			case "2":
				stateString =  "进行中";
				break;
			case "3":
				stateString =  "纠纷处理";
				break;
			case "4":
				stateString =  "已结束";
				break;
			default:
				stateString =  "报名中";
				break;
			}
		}
		 
		infoMap.put("state",stateString);
		infoMap.put("chargeAmount",secActivity.getChargeAmount());
		
		map.put("info", infoMap);
		SecEvaluate secEvaluate = new SecEvaluate();
		secEvaluate.setActivityId(activityId);
		Page<SecEvaluate> page = new Page<>();
		page.setPageNo(Integer.parseInt(pageNo));
		page.setPageSize(10);
		Page pageBean = findPage(page, secEvaluate);
		List<SecEvaluate> list = pageBean.getList();
		List<Map> mapList = new ArrayList<>();
		for (SecEvaluate secEvaluate2 : list) {
			Map<String,Object> mapBean = new HashMap<>();
			mapBean.put("name", "上帝视角的我");
			mapBean.put("content", secEvaluate2.getContent());
			mapBean.put("activityScore", secEvaluate2.getActivityScore());
			mapBean.put("organizerScore", secEvaluate2.getOrganizerScore());
			mapBean.put("createDate", DateUtils.formatDate(secEvaluate2.getCreateDate()));
			mapList.add(mapBean);
		}
		
		map.put("evaluateList", mapList);
		
	}
	
}