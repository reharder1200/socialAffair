/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.web;

import java.util.HashMap;
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
import com.thinkgem.jeesite.modules.activity.entity.SecPlace;
import com.thinkgem.jeesite.modules.activity.service.SecPlaceService;

/**
 * 地铁线路Controller
 * @author zgq
 * @version 2020-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secPlace")
public class SecPlaceController extends BaseController {

	@Autowired
	private SecPlaceService secPlaceService;
	
	@ModelAttribute
	public SecPlace get(@RequestParam(required=false) String id) {
		SecPlace entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secPlaceService.get(id);
		}
		if (entity == null){
			entity = new SecPlace();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secPlace:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecPlace secPlace, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecPlace> page = secPlaceService.findPage(new Page<SecPlace>(request, response), secPlace); 
		model.addAttribute("page", page);
		return "modules/activity/secPlaceList";
	}

	@RequiresPermissions("activity:secPlace:view")
	@RequestMapping(value = "form")
	public String form(SecPlace secPlace, Model model) {
		model.addAttribute("secPlace", secPlace);
		return "modules/activity/secPlaceForm";
	}

	@RequiresPermissions("activity:secPlace:edit")
	@RequestMapping(value = "save")
	public String save(SecPlace secPlace, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secPlace)){
			return form(secPlace, model);
		}
		secPlaceService.save(secPlace);
		addMessage(redirectAttributes, "保存创建地铁线路成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secPlace/?repage";
	}
	
	@RequiresPermissions("activity:secPlace:edit")
	@RequestMapping(value = "delete")
	public String delete(SecPlace secPlace, RedirectAttributes redirectAttributes) {
		secPlaceService.delete(secPlace);
		addMessage(redirectAttributes, "删除创建地铁线路成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secPlace/?repage";
	}
	
	/**
	 * 获取商圈或地铁
	 */
	@ResponseBody
	@RequestMapping(value = "placeList", method = RequestMethod.GET , produces = "application/json")
	public String  placeList(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		secPlaceService.placeList(map,request);
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 获取商圈或地铁子项
	 */
	@ResponseBody
	@RequestMapping(value = "placeList2", method = RequestMethod.GET , produces = "application/json")
	public String  placeList2(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		secPlaceService.placeList2(map,request);
		return JSONUtils.toJSONString(map); 
	}

}