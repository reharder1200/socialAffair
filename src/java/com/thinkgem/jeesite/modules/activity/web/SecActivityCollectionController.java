/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityCollection;
import com.thinkgem.jeesite.modules.activity.service.SecActivityCollectionService;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 活动收藏Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secActivityCollection")
public class SecActivityCollectionController extends BaseController {

	@Autowired
	private SecActivityCollectionService secActivityCollectionService;
	
	@ModelAttribute
	public SecActivityCollection get(@RequestParam(required=false) String id) {
		SecActivityCollection entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secActivityCollectionService.get(id);
		}
		if (entity == null){
			entity = new SecActivityCollection();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secActivityCollection:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecActivityCollection secActivityCollection, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecActivityCollection> page = secActivityCollectionService.findPage(new Page<SecActivityCollection>(request, response), secActivityCollection); 
		model.addAttribute("page", page);
		return "modules/activity/secActivityCollectionList";
	}

	@RequiresPermissions("activity:secActivityCollection:view")
	@RequestMapping(value = "form")
	public String form(SecActivityCollection secActivityCollection, Model model) {
		model.addAttribute("secActivityCollection", secActivityCollection);
		return "modules/activity/secActivityCollectionForm";
	}

	@RequiresPermissions("activity:secActivityCollection:edit")
	@RequestMapping(value = "save")
	public String save(SecActivityCollection secActivityCollection, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secActivityCollection)){
			return form(secActivityCollection, model);
		}
		secActivityCollectionService.save(secActivityCollection);
		addMessage(redirectAttributes, "保存活动收藏成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityCollection/?repage";
	}
	
	@RequiresPermissions("activity:secActivityCollection:edit")
	@RequestMapping(value = "delete")
	public String delete(SecActivityCollection secActivityCollection, RedirectAttributes redirectAttributes) {
		secActivityCollectionService.delete(secActivityCollection);
		addMessage(redirectAttributes, "删除活动收藏成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityCollection/?repage";
	}

	/**
	 * 关注\取消活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "collectionActivity", method = RequestMethod.GET , produces = "application/json")
	public String  collectionActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		
		try {
			
		
		String activityId = request.getParameter("activityId");
		
		String isCollection = request.getParameter("isCollection");
		
		String userId = request.getParameter("userId");
		
		if(isCollection.equals("1")) {
			
			SecActivityCollection collection = new SecActivityCollection();
			collection.setActivityId(activityId);
			User user = new User();
			user.setId(userId);
			collection.setUser(user);
			secActivityCollectionService.save(collection);
		}else {
			SecActivityCollection collectionSelect = new SecActivityCollection();
			collectionSelect.setUserId(userId);
			collectionSelect.setActivityId(activityId);
			List<SecActivityCollection>  list = secActivityCollectionService.findList(collectionSelect);
			for (SecActivityCollection secActivityCollection : list) {
				secActivityCollectionService.delete(secActivityCollection);
			}
		}
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "收藏成功");
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "收藏失败");
		}
		return JSONUtils.toJSONString(map); 
	}
}