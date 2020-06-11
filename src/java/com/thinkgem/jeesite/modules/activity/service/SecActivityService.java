/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.thinkgem.jeesite.modules.activity.entity.SecActivityBrowse;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityCollection;
import com.thinkgem.jeesite.modules.activity.entity.SecEvaluate;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.activity.dao.SecActivityDao;

/**
 * 活动信息Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecActivityService extends CrudService<SecActivityDao, SecActivity> {
	
	@Autowired
	private SecActivityBrowseService secActivityBrowseService;
	@Autowired
	private SecActivityApplyService secActivityApplyService;
	@Autowired
	private SecActivityCollectionService secActivityCollectionService;
	@Autowired
	private SecEvaluateService secEvaluateService;
	

	public SecActivity get(String id) {
		return super.get(id);
	}
	
	public List<SecActivity> findList(SecActivity secActivity) {
		return super.findList(secActivity);
	}
	
	public Page<SecActivity> findPage(Page<SecActivity> page, SecActivity secActivity) {
		return super.findPage(page, secActivity);
	}
	
	@Transactional(readOnly = false)
	public void save(SecActivity secActivity) {
		super.save(secActivity);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecActivity secActivity) {
		super.delete(secActivity);
	}

	/**
	 * 待参加活动
	 * @param secActivity
	 * @return
	 */
	public void jionActivity(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		String userId =  request.getParameter("userId");
		String pageNo =  request.getParameter("pageNo");
		
		
		
		if(userId!=null&&!"".equals(userId)) {
					
		Page<SecActivity> page = new Page<>();
		page.setCount(10);
		page.setPageNo(Integer.parseInt(pageNo));
		page.setOrderBy("a.begin_date DESC");
		
		SecActivity secActivity = new  SecActivity();
		Map sqlMap = new HashMap<>();
		String lat = request.getParameter("latitude");
		String lon = request.getParameter("longitude");
		
		sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+lat+" * PI() / 180 - activity_lat * PI() / 180 "+
		" ) / 2 ),  2  ) + COS("+lat+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+lon+" * PI() / 180 - activity_lon * PI() / 180"+
				"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
	 
		sqlMap.put("where", " apply.id is not null and apply.user_id = '"+userId+"' and apply.sign_type='0' ");
		secActivity.setSqlMap(sqlMap);
		
	 	Page  pageBean = this.findPage(page, secActivity);
		List<SecActivity> list =  pageBean.getList();
	 	List<Map> listMap = new ArrayList<>();
	 	for (SecActivity secActivity2 : list) {
	 		Map< String, String> mapBean = new HashMap<>();
	 		mapBean.put("id",  secActivity2.getId());
	 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
	 		mapBean.put("title", secActivity2.getTitle());
	 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
	 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
	 		mapBean.put("postion", secActivity2.getJuli()+"米");
	 		mapBean.put("activityDescription", secActivity2.getActivityDescription());
	 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
	 		mapBean.put("num","15" );
	 		mapBean.put("isCollection", "1" );
	 		listMap.add(mapBean);
		}
	 	
	 	map.put("page", listMap);
	 	map.put("pageSize", pageBean.getPageSize());
	 	map.put("pageNo", pageBean.getPageNo());
		}else {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "用户信息不符合");
		}
	}

	/**
	 * 待评价活动	
	 * @param map
	 * @param request
	 */
	public void evaluateActivity(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String userId =  request.getParameter("userId");
		String pageNo =  request.getParameter("pageNo");
		if(userId!=null&&!"".equals(userId)) {
					
		Page<SecActivity> page = new Page<>();
		page.setCount(10);
		page.setPageNo(Integer.parseInt(pageNo));
		page.setOrderBy("a.begin_date DESC");
		
		SecActivity secActivity = new  SecActivity();
		Map sqlMap = new HashMap<>();
		String lat = request.getParameter("latitude");
		String lon = request.getParameter("longitude");
		
		sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+lat+" * PI() / 180 - activity_lat * PI() / 180 "+
		" ) / 2 ),  2  ) + COS("+lat+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+lon+" * PI() / 180 - activity_lon * PI() / 180"+
				"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
	 
		sqlMap.put("where", " evaluate.id is null and apply.user_id = '"+userId+"' and a.activity_status='4' ");
		secActivity.setSqlMap(sqlMap);
		
	 	Page  pageBean = this.findPage(page, secActivity);
		List<SecActivity> list =  pageBean.getList();
	 	List<Map> listMap = new ArrayList<>();
	 	for (SecActivity secActivity2 : list) {
	 		Map< String, String> mapBean = new HashMap<>();
	 		mapBean.put("id",  secActivity2.getId());
	 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
	 		mapBean.put("title", secActivity2.getTitle());
	 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
	 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
	 		mapBean.put("postion", secActivity2.getJuli()+"米");
	 		mapBean.put("activityDescription", secActivity2.getActivityDescription());
	 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
	 		mapBean.put("num","15" );
	 		mapBean.put("isCollection", "1" );
	 		listMap.add(mapBean);
		}
	 	
	 	map.put("page", listMap);
	 	map.put("pageSize", pageBean.getPageSize());
	 	map.put("pageNo", pageBean.getPageNo());
		}else {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "用户信息不符合");
		}
	}
	
	/*
	 * 已创建活动
	 */
	public void createActivityList(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
				String userId =  request.getParameter("userId");
				String pageNo =  request.getParameter("pageNo");
				if(userId!=null&&!"".equals(userId)) {
							
				Page<SecActivity> page = new Page<>();
				page.setCount(10);
				page.setPageNo(Integer.parseInt(pageNo));
				page.setOrderBy("a.begin_date DESC");
				
				SecActivity secActivity = new  SecActivity();
				Map sqlMap = new HashMap<>();
				String lat = request.getParameter("latitude");
				String lon = request.getParameter("longitude");
				
				sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+lat+" * PI() / 180 - activity_lat * PI() / 180 "+
				" ) / 2 ),  2  ) + COS("+lat+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+lon+" * PI() / 180 - activity_lon * PI() / 180"+
						"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
			 
				sqlMap.put("where", " a.create_by = '"+userId+"'");
				secActivity.setSqlMap(sqlMap);
				
			 	Page  pageBean = this.findPage(page, secActivity);
				List<SecActivity> list =  pageBean.getList();
			 	List<Map> listMap = new ArrayList<>();
			 	for (SecActivity secActivity2 : list) {
			 		Map< String, String> mapBean = new HashMap<>();
			 		mapBean.put("id",  secActivity2.getId());
			 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
			 		mapBean.put("title", secActivity2.getTitle());
			 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
			 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
			 		mapBean.put("postion", secActivity2.getJuli()+"米");
			 		mapBean.put("activityDescription", secActivity2.getActivityDescription());
			 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
			 		mapBean.put("num","15" );
			 		mapBean.put("isCollection", "1" );
			 		listMap.add(mapBean);
				}
			 	
			 	map.put("page", listMap);
			 	map.put("pageSize", pageBean.getPageSize());
			 	map.put("pageNo", pageBean.getPageNo());
				}else {
					map.put("status", "-1");
					map.put("code", "1");
					map.put("description", "用户信息不符合");
				}
		
	}

	/**
	 * 已参与
	 * @param map
	 * @param request
	 */
	public void yJionActivity(Map<String, Object> map, HttpServletRequest request) {

		String userId =  request.getParameter("userId");
		String pageNo =  request.getParameter("pageNo");
		if(userId!=null&&!"".equals(userId)) {
					
		Page<SecActivity> page = new Page<>();
		page.setCount(10);
		page.setPageNo(Integer.parseInt(pageNo));
		page.setOrderBy("a.begin_date DESC");
		
		SecActivity secActivity = new  SecActivity();
		Map sqlMap = new HashMap<>();
		String lat = request.getParameter("latitude");
		String lon = request.getParameter("longitude");
		
		sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+lat+" * PI() / 180 - activity_lat * PI() / 180 "+
		" ) / 2 ),  2  ) + COS("+lat+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+lon+" * PI() / 180 - activity_lon * PI() / 180"+
				"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
	 
		sqlMap.put("where", " apply.id is not null and apply.user_id = '"+userId+"' and apply.sign_type='2' ");
		secActivity.setSqlMap(sqlMap);
		
	 	Page  pageBean = this.findPage(page, secActivity);
		List<SecActivity> list =  pageBean.getList();
	 	List<Map> listMap = new ArrayList<>();
	 	for (SecActivity secActivity2 : list) {
	 		Map< String, String> mapBean = new HashMap<>();
	 		mapBean.put("id",  secActivity2.getId());
	 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
	 		mapBean.put("title", secActivity2.getTitle());
	 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
	 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
	 		mapBean.put("postion", secActivity2.getJuli()+"米");
	 		mapBean.put("activityDescription", secActivity2.getActivityDescription());
	 		
	 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
	 		mapBean.put("num","15" );
	 		mapBean.put("isCollection", "1" );
	 		listMap.add(mapBean);
		}
	 	
	 	map.put("page", listMap);
	 	map.put("pageSize", pageBean.getPageSize());
	 	map.put("pageNo", pageBean.getPageNo());
		}else {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "用户信息不符合");
		}
	}
	
	/**
	 * 已关注
	 * @param map
	 * @param request
	 */
	public void collectionActivity(Map<String, Object> map, HttpServletRequest request) {

		String userId =  request.getParameter("userId");
		String pageNo =  request.getParameter("pageNo");
		if(userId!=null&&!"".equals(userId)) {
					
		Page<SecActivity> page = new Page<>();
		page.setCount(10);
		page.setPageNo(Integer.parseInt(pageNo));
		page.setOrderBy("a.begin_date DESC");
		
		SecActivity secActivity = new  SecActivity();
		Map sqlMap = new HashMap<>();
		String lat = request.getParameter("latitude");
		String lon = request.getParameter("longitude");
		
		sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+lat+" * PI() / 180 - activity_lat * PI() / 180 "+
		" ) / 2 ),  2  ) + COS("+lat+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+lon+" * PI() / 180 - activity_lon * PI() / 180"+
				"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
	 
		sqlMap.put("where", " collection.id is not null and collection.user_id = '"+userId+"' ");
		secActivity.setSqlMap(sqlMap);
		
	 	Page  pageBean = this.findPage(page, secActivity);
		List<SecActivity> list =  pageBean.getList();
	 	List<Map> listMap = new ArrayList<>();
	 	for (SecActivity secActivity2 : list) {
	 		Map< String, String> mapBean = new HashMap<>();
	 		mapBean.put("id",  secActivity2.getId());
	 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
	 		mapBean.put("title", secActivity2.getTitle());
	 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
	 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
	 		mapBean.put("postion", secActivity2.getJuli()+"米");
	 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
	 		mapBean.put("activityDescription", secActivity2.getActivityDescription());
	 		mapBean.put("num","15" );
	 		mapBean.put("isCollection", "1" );
	 		listMap.add(mapBean);
		}
	 	
	 	map.put("page", listMap);
	 	map.put("pageSize", pageBean.getPageSize());
	 	map.put("pageNo", pageBean.getPageNo());
		}else {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "用户信息不符合");
		}
	}

	@Transactional(readOnly = false)
	public void detailActivity(Map<String, Object> map, HttpServletRequest request) {

	
		String id = request.getParameter("sid");
		
		String userId =  request.getParameter("userId");
		
		SecActivityBrowse secActivityBrowse = new SecActivityBrowse();
		secActivityBrowse.setActivityId(id);
		secActivityBrowse.setCreateDate(new Date());
		secActivityBrowseService.save(secActivityBrowse);
		
		SecActivityBrowse secActivityBrowseSelect = new SecActivityBrowse();
		secActivityBrowseSelect.setActivityId(id);
		List<SecActivityBrowse> list = secActivityBrowseService.findList(secActivityBrowseSelect);
		//报名人数
		SecActivityApply secActivityApply = new SecActivityApply();
		secActivityApply.setActivityId(id);
		List<SecActivityApply>  applyList =  secActivityApplyService.findList(secActivityApply);
		//是否报名
		SecActivityApply secIsActivityApply = new SecActivityApply();
		secIsActivityApply.setActivityId(id);
		secIsActivityApply.setUserId(userId);
		List<SecActivityApply>  applyIsList =  secActivityApplyService.findList(secIsActivityApply);
		//是否关注
		SecActivityCollection collection = new SecActivityCollection();
		collection.setActivityId(id);
		collection.setUserId(userId);
		List<SecActivityCollection> collectionList =  secActivityCollectionService.findList(collection);
		
		//是否评价
		SecEvaluate secEvaluate = new SecEvaluate();
		secEvaluate.setActivityId(id);
		secEvaluate.setUserId(userId);
		List<SecEvaluate> secEvaluateList = secEvaluateService.findList(secEvaluate);
		
		SecActivity secActivity = this.get(id);
		Map<String, Object>  detailMap =  new HashMap<>();
		if(secActivity!=null) {
			detailMap.put("id", secActivity.getId());
			detailMap.put("title", secActivity.getTitle());
			detailMap.put("ImagePath", "https://joinjay.com/socialAffair"+secActivity.getImageIds());
			detailMap.put("chargeAmount", secActivity.getChargeAmount());
			detailMap.put("num", applyList.size());
			detailMap.put("browseSize", list.size());
			detailMap.put("locationDetatil", secActivity.getLocationDetatil());
			detailMap.put("activityDescription",secActivity.getActivityDescription());
	 		
			detailMap.put("createDate", DateUtils.formatDateTime(secActivity.getCreateDate()));
			detailMap.put("wx", secActivity.getContactWx());
			detailMap.put("phone", secActivity.getContactMobile());
			
			if(secEvaluateList!=null&&secEvaluateList.size()>0) {
				detailMap.put("isEvaluate", "1");
			}else {
				detailMap.put("isEvaluate", "0");
			}
			
			
			if(secActivity.getContactMobile()!=null&&!"".equals(secActivity.getContactMobile())) {
				detailMap.put("Phone", secActivity.getContactMobile());
	 		}else {
	 			detailMap.put("wxPhone", "");
	 		}
			
			detailMap.put("description", secActivity.getActivityDescription());
			
			
			if(applyIsList!=null&&applyIsList.size()>0) {
				if(applyIsList.get(0).getSignType().equals("0")) {
					detailMap.put("isApply", "1");
				}else {
					detailMap.put("isApply", "0");
				}
				
			}else {
				detailMap.put("isApply", "0");
			}
			
			if(collectionList!=null&&collectionList.size()>0) {
				detailMap.put("isCollection", "1");
			}else {
				detailMap.put("isCollection", "0");
			}
		}
		map.put("info", detailMap);
		
		
		SecActivity activity = new SecActivity();
		Page<SecActivity> page = new Page<>();
		page.setCount(10);
		page.setPageNo(1);
		
		User user = new User();
		user.setId(request.getParameter("userId"));
		secActivity.setCreateBy(user);
		secActivity.setRemarks(request.getParameter("userId"));
	 	Page  pageBean = this.findPage(page, activity);
		
	 	List<SecActivity> historyList =  pageBean.getList();
	 	List<Map> listMap = new ArrayList<>();
	 	for (SecActivity secActivity2 : historyList) {
	 		Map< String, String> mapBean = new HashMap<>();
	 		mapBean.put("id", secActivity2.getId());
	 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
	 		mapBean.put("title", secActivity2.getTitle());
	 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
	 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
	 		mapBean.put("postion", secActivity2.getJuli()+"米");
	 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
	 		mapBean.put("num","15" );
	 		mapBean.put("isCollection", "0" );
	 		mapBean.put("activityDescription", secActivity2.getActivityDescription());
	 		mapBean.put("wx", secActivity2.getContactWx());
	 		if(secActivity2.getContactMobile()!=null&&!"".equals(secActivity2.getContactMobile())) {
	 			mapBean.put("Phone", secActivity2.getContactMobile());
	 		}else {
	 			mapBean.put("wxPhone", "");
	 		}
	 		
	 		listMap.add(mapBean);
		}
	 	map.put("historyList", listMap);
		
	}

	@Transactional(readOnly = false)
	public void endActivity(Map<String, Object> map, HttpServletRequest request) {
		 
		String activityId = request.getParameter("activityId");
		String userId = request.getParameter("userId");
		
		SecActivity activity = get(activityId);
		activity.setActivityStatus("4");
		save(activity);		
		
	}

	@Transactional(readOnly = false)
	public void cancelActivity(Map<String, Object> map, HttpServletRequest request) {
		String activityId = request.getParameter("activityId");
		String userId = request.getParameter("userId");
		
		SecActivity activity = get(activityId);
		activity.setActivityStatus("1");
		save(activity);		
	}
	
}