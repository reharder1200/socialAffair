/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.activity.entity.SecActivity;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityApply;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityBrowse;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityCollection;
import com.thinkgem.jeesite.modules.activity.entity.SecImage;
import com.thinkgem.jeesite.modules.activity.service.SecActivityApplyService;
import com.thinkgem.jeesite.modules.activity.service.SecActivityBrowseService;
import com.thinkgem.jeesite.modules.activity.service.SecActivityCollectionService;
import com.thinkgem.jeesite.modules.activity.service.SecActivityService;
import com.thinkgem.jeesite.modules.activity.service.SecImageService;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 活动信息Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secActivity")
public class SecActivityController extends BaseController {

	@Autowired
	private SecActivityService secActivityService;
	@Autowired
	private SecImageService secImageService;
	
	
	@ModelAttribute
	public SecActivity get(@RequestParam(required=false) String id) {
		SecActivity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secActivityService.get(id);
		}
		if (entity == null){
			entity = new SecActivity();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secActivity:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecActivity secActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecActivity> page = secActivityService.findPage(new Page<SecActivity>(request, response), secActivity); 
		model.addAttribute("page", page);
		return "modules/activity/secActivityList";
	}

	@RequiresPermissions("activity:secActivity:view")
	@RequestMapping(value = "form")
	public String form(SecActivity secActivity, Model model) {
		model.addAttribute("secActivity", secActivity);
		return "modules/activity/secActivityForm";
	}

	@RequiresPermissions("activity:secActivity:edit")
	@RequestMapping(value = "save")
	public String save(SecActivity secActivity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secActivity)){
			return form(secActivity, model);
		}
		secActivityService.save(secActivity);
		addMessage(redirectAttributes, "保存活动信息成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivity/?repage";
	}
	
	@RequiresPermissions("activity:secActivity:edit")
	@RequestMapping(value = "delete")
	public String delete(SecActivity secActivity, RedirectAttributes redirectAttributes) {
		secActivityService.delete(secActivity);
		addMessage(redirectAttributes, "删除活动信息成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivity/?repage";
	}
	
	/**
	 * 上传图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "putImage")
	public String  putImage( HttpServletRequest request,@RequestParam(value = "file", required = false) MultipartFile file) {
		
		Map<String, String> map =  new HashMap<>();
		
		String savePath = null;
		
	    savePath = request.getRealPath("\\upload") + "\\" + DateUtils.getDate()+ "\\";
	    File filePath = new File(savePath);
	    if (!filePath.exists()) {
	    	filePath.mkdirs();
        }
	    
	    try {
	    	   if(!file.isEmpty()) {
	               logger.info("成功获取照片");
	               String fileName = file.getOriginalFilename();
	               String path = null;
	               String type = null;
	               type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
	               logger.info("图片初始名称为：" + file.getOriginalFilename() + " 类型为：" + type);
	               if (type != null) {
	                	   
	                      logger.info("存放图片文件的路径:" + savePath);
	                      String xffileName = new Date().getTime()+file.getName();
	   					  file.transferTo(new File(savePath+xffileName+".png"));
	                      SecImage secImage = new SecImage();
	                      secImage.setImgPath("/upload/"+DateUtils.getDate()+"/"+xffileName+".png");
	                      secImageService.save(secImage);
	                      
	                       map.put("status", "1");
	                       map.put("code", "1");
	                       map.put("imageId",secImage.getId());
	                       map.put("description", "成功");
	   					 
	                       logger.info("文件成功上传到指定目录下");
	                   
	               }else {
	                   logger.info("文件类型为空");
	                   return "error";
	               }
	           }else {
	        	   map.put("status", "-1");
		   		   map.put("code", "1");
		   		   map.put("description", "没有找到相对应的文件");
	           }
		} catch (Exception e) {
				map.put("status", "-1");
	   		   map.put("code", "1");
	   		   map.put("description", "程序发生错误");
		}
	    
 
	    
		return JSONUtils.toJSONString(map);
	}
	
	/**
	 * 创建活动
	 */
	@ResponseBody
	@RequestMapping(value = "createActivity", method = RequestMethod.GET , produces = "application/json")
	public String  createActivity(HttpServletRequest request,SecActivity secActivity) {
		
		Map<String, String> map =  new HashMap<>();
		
		secActivity.setTitle( request.getParameter("title"));
		secActivity.setType( request.getParameter("type"));
		String beginDate = request.getParameter("beginDate");
		if(beginDate!=null&&!"".equals(beginDate)) {
			secActivity.setBeginDate(DateUtils.parseDate( beginDate));
		}else {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "开始时间不能为空");
			
			return  JSONUtils.toJSONString(map);
		}
		
		String endDate = request.getParameter("endDate");
		if(endDate!=null&&!"".equals(endDate)) {
			secActivity.setEndDate(DateUtils.parseDate(endDate) );
		}else {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "结束时间不能为空");
			
			return  JSONUtils.toJSONString(map);
		}
		
		secActivity.setActivityLat( request.getParameter("activityLat"));
		
		secActivity.setActivityLon(request.getParameter("activityLon"));
		secActivity.setLocationDetatil( request.getParameter("locationDetatil"));
		secActivity.setMinPeople( request.getParameter("minPeople"));
		
		secActivity.setMaxPeople( request.getParameter("maxPeople"));
		
		secActivity.setActivityDescription( request.getParameter("activityDescription"));
		secActivity.setChargeAmount( request.getParameter("chargeAmount"));
		secActivity.setContactWx( request.getParameter("contactWx"));
		secActivity.setContactMobile( request.getParameter("contactMobile"));
		
		request.getParameter("yzm");
		
		secActivity.setRemarks( request.getParameter("userId"));
		User user = new User();
		user.setId(request.getParameter("userId"));
		secActivity.setCreateBy(user);
		
		String imageIds = request.getParameter("imageIds");
		
		secActivity.setActivityStatus("0");
		
		try {
			
			secActivityService.save(secActivity);
			
			if(imageIds!=null ) {
					SecImage imgBean = secImageService.get(imageIds);
					if(imgBean!=null) {
						imgBean.setActivityId(secActivity.getId());
						secImageService.save(imgBean);
					}
				 
			}
			
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "成功");
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
			// TODO: handle exception
		}
		
		return JSONUtils.toJSONString(map);
	}
	
	/**
	 * 获取热门活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "hotActivity", method = RequestMethod.GET , produces = "application/json")
	public String  hotActivity(HttpServletRequest request,String pageNo) {
		Map<String, Object> map =  new HashMap<>();
		
		try {
			Page<SecActivity> page = new Page<>();
			page.setCount(10);
			page.setPageNo(Integer.parseInt(pageNo));
//			page.setOrderBy(" ");
			SecActivity secActivity = new SecActivity();
			secActivity.setActivityLat(request.getParameter("activityLat"));
			secActivity.setActivityLon(request.getParameter("activityLon"));
		 	Map sqlMap = new HashMap<>();
			sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+secActivity.getActivityLat()+" * PI() / 180 - activity_lat * PI() / 180 "+
					" ) / 2 ),  2  ) + COS("+secActivity.getActivityLat()+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+secActivity.getActivityLon()+" * PI() / 180 - activity_lon * PI() / 180"+
							"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
			sqlMap.put("where", " a.activity_status='0' ");	
			secActivity.setSqlMap(sqlMap);
			
			Page<SecActivity>  pageBean = secActivityService.findPage(page, secActivity);
		 	List<SecActivity>  list = pageBean.getList();
		 	
		 	List<Map> listMap = new ArrayList<>();
		 	for (SecActivity secActivity2 : list) {
		 		Map< String, String> mapBean = new HashMap<>();
		 		mapBean.put("id",  secActivity2.getId());
		 		mapBean.put("ImagePath", "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
		 		mapBean.put("title", secActivity2.getTitle());
		 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
		 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
		 		mapBean.put("postion", secActivity2.getJuli()+"米");
		 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
		 		mapBean.put("activityDescription",secActivity2.getActivityDescription());
		 		
		 		mapBean.put("num","15" );
		 		mapBean.put("isCollection", "1" );
		 		listMap.add(mapBean);
			}
		 	
		 	map.put("page", listMap);
		 	map.put("pageSize", pageBean.getPageSize());
		 	map.put("pageNo", pageBean.getPageNo());
		 	
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		return JSONUtils.toJSONString(map);
	}
	
	/**
	 * 获取即将开始活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "startActivity", method = RequestMethod.GET , produces = "application/json")
	public String  startActivity(HttpServletRequest request,String pageNo) {
		Map<String, Object> map =  new HashMap<>();
		
		try {
			Page<SecActivity> page = new Page<>();
			page.setCount(10);
			page.setPageNo(Integer.parseInt(pageNo));
			page.setOrderBy("a.begin_date DESC");
			
					
			SecActivity secActivity = new  SecActivity();
			secActivity.setActivityLat(request.getParameter("activityLat"));
			secActivity.setActivityLon(request.getParameter("activityLon"));
			Map sqlMap = new HashMap<>();
			sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+secActivity.getActivityLat()+" * PI() / 180 - activity_lat * PI() / 180 "+
					" ) / 2 ),  2  ) + COS("+secActivity.getActivityLat()+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+secActivity.getActivityLon()+" * PI() / 180 - activity_lon * PI() / 180"+
							"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
			sqlMap.put("where", "  a.activity_status='0'  ");
			secActivity.setSqlMap(sqlMap);
					
		 	Page  pageBean = secActivityService.findPage(page, secActivity);
		 	
		 	List<SecActivity> list =  pageBean.getList();
		 	List<Map> listMap = new ArrayList<>();
		 	for (SecActivity secActivity2 : list) {
		 		Map< String, String> mapBean = new HashMap<>();
		 		mapBean.put("id",  secActivity2.getId());
		 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
		 		mapBean.put("title", secActivity2.getTitle());
		 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
		 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
		 		mapBean.put("activityDescription",secActivity2.getActivityDescription());
		 		
		 		mapBean.put("postion", secActivity2.getJuli()+"米");
		 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
		 		mapBean.put("num","15" );
		 		mapBean.put("isCollection", "1" );
		 		listMap.add(mapBean);
			}
		 	
		 	map.put("page", listMap);
		 	map.put("pageSize", pageBean.getPageSize());
		 	map.put("pageNo", pageBean.getPageNo());
		 	
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		return JSONUtils.toJSONString(map);
	}
	
	/**
	 * 获取最近活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "LatelyActivity", method = RequestMethod.GET , produces = "application/json")
	public String  LatelyActivity(HttpServletRequest request,String pageNo ) {
		Map<String, Object> map =  new HashMap<>();
		SecActivity secActivity = new SecActivity();
		try {
			secActivity.setActivityLat(request.getParameter("activityLat"));
			secActivity.setActivityLon(request.getParameter("activityLon"));
			Map sqlMap = new HashMap<>();
			
			sqlMap.put("column", " , ROUND( 6378.138 * 2 * ASIN( SQRT(  POW(  SIN(  (    "+secActivity.getActivityLat()+" * PI() / 180 - activity_lat * PI() / 180 "+
			" ) / 2 ),  2  ) + COS("+secActivity.getActivityLat()+" * PI() / 180) * COS(activity_lat * PI() / 180) * POW( SIN( ("+secActivity.getActivityLon()+" * PI() / 180 - activity_lon * PI() / 180"+
					"  ) / 2  ),    2    )  ) ) * 1000 ) AS juli ");
			sqlMap.put("where", " a.activity_status='0' ");
			secActivity.setSqlMap(sqlMap);
			 
			
			Page<SecActivity> page = new Page<>();
			page.setCount(10);
			page.setPageNo(Integer.parseInt(pageNo));
			page.setOrderBy("juli ASC");
		 	Page  pageBean = secActivityService.findPage(page, secActivity);
			
		 	List<SecActivity> list =  pageBean.getList();
		 	List<Map> listMap = new ArrayList<>();
		 	for (SecActivity secActivity2 : list) {
		 		Map< String, String> mapBean = new HashMap<>();
		 		mapBean.put("id",  secActivity2.getId());
		 		mapBean.put("ImagePath",  "https://joinjay.com/socialAffair"+secActivity2.getImageIds());
		 		mapBean.put("title", secActivity2.getTitle());
		 		mapBean.put("locationDetatil", secActivity2.getLocationDetatil());
		 		mapBean.put("activityDescription",secActivity2.getActivityDescription());
		 		
		 		mapBean.put("chargeAmount", secActivity2.getChargeAmount());
		 		mapBean.put("postion", secActivity2.getJuli()+"米");
		 		mapBean.put("beginDate", DateUtils.pastHour( secActivity2.getBeginDate())+"小时" );
		 		mapBean.put("num","15" );
		 		mapBean.put("isCollection", "1" );
		 		listMap.add(mapBean);
			}
		 	
		 	map.put("page",listMap);
		 	map.put("pageSize", pageBean.getPageSize());
		 	map.put("pageNo", pageBean.getPageNo());
		 	
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		return JSONUtils.toJSONString(map);
	}
	
	
	/**
	 * 获取活动详情
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "detailActivity", method = RequestMethod.GET , produces = "application/json")
	public String  detailActivity(HttpServletRequest request) {
		
			Map<String, Object> map =  new HashMap<>();
			
		try {
			secActivityService.detailActivity(map,request);
			
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
	
		
		return JSONUtils.toJSONString(map); 
	}
	/**
	 * 待参加活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "jionActivity", method = RequestMethod.GET , produces = "application/json")
	public String  jionActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secActivityService.jionActivity(map,request);
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
	
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 待评价活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "evaluateActivity", method = RequestMethod.GET , produces = "application/json")
	public String  evaluateActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secActivityService.evaluateActivity(map,request);
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
	
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 已参与活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "yJionActivity", method = RequestMethod.GET , produces = "application/json")
	public String  yJionActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secActivityService.yJionActivity(map,request);
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
	
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 已创建活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createActivityList", method = RequestMethod.GET , produces = "application/json")
	public String  createActivityList(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secActivityService.createActivityList(map,request);
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
	
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 已关注活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "collectionActivity", method = RequestMethod.GET , produces = "application/json")
	public String  collectionActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secActivityService.collectionActivity(map,request);
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
	
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 结束活动
	 */
	@ResponseBody
	@RequestMapping(value = "endActivity", method = RequestMethod.GET , produces = "application/json")
	public String  endActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		secActivityService.endActivity(map,request);
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 取消活动
	 */
	@ResponseBody
	@RequestMapping(value = "cancelActivity", method = RequestMethod.GET , produces = "application/json")
	public String  disActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		secActivityService.cancelActivity(map,request);
		return JSONUtils.toJSONString(map); 
	}
}