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
import com.thinkgem.jeesite.modules.activity.entity.SecEvaluate;
import com.thinkgem.jeesite.modules.activity.service.SecEvaluateService;

/**
 * 活动Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secEvaluate")
public class SecEvaluateController extends BaseController {

	@Autowired
	private SecEvaluateService secEvaluateService;
	
	@ModelAttribute
	public SecEvaluate get(@RequestParam(required=false) String id) {
		SecEvaluate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secEvaluateService.get(id);
		}
		if (entity == null){
			entity = new SecEvaluate();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secEvaluate:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecEvaluate secEvaluate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecEvaluate> page = secEvaluateService.findPage(new Page<SecEvaluate>(request, response), secEvaluate); 
		model.addAttribute("page", page);
		return "modules/activity/secEvaluateList";
	}

	@RequiresPermissions("activity:secEvaluate:view")
	@RequestMapping(value = "form")
	public String form(SecEvaluate secEvaluate, Model model) {
		model.addAttribute("secEvaluate", secEvaluate);
		return "modules/activity/secEvaluateForm";
	}

	@RequiresPermissions("activity:secEvaluate:edit")
	@RequestMapping(value = "save")
	public String save(SecEvaluate secEvaluate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secEvaluate)){
			return form(secEvaluate, model);
		}
		secEvaluateService.save(secEvaluate);
		addMessage(redirectAttributes, "保存活动成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secEvaluate/?repage";
	}
	
	@RequiresPermissions("activity:secEvaluate:edit")
	@RequestMapping(value = "delete")
	public String delete(SecEvaluate secEvaluate, RedirectAttributes redirectAttributes) {
		secEvaluateService.delete(secEvaluate);
		addMessage(redirectAttributes, "删除活动成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secEvaluate/?repage";
	}

	/**
	 * 评论活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "evaluateActivity", method = RequestMethod.GET , produces = "application/json")
	public String  evaluateActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		
		secEvaluateService.evaluateActivity(map,request);
		map.put("status", "1");
		map.put("code", "1");
		map.put("description", "成功");
		
		return JSONUtils.toJSONString(map); 
	}
	/**
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "evaluateAList", method = RequestMethod.GET , produces = "application/json")
	public String  evaluateAList(HttpServletRequest request) {
		
		Map<String, Object> map =  new HashMap<>();
		secEvaluateService.evaluateAList(map,request);
		map.put("status", "1");
		map.put("code", "1");
		map.put("description", "成功");
		
		return JSONUtils.toJSONString(map); 
	}
}