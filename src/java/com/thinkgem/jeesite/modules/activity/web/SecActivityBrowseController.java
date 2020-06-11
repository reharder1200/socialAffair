/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityBrowse;
import com.thinkgem.jeesite.modules.activity.service.SecActivityBrowseService;

/**
 * 浏览记录Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secActivityBrowse")
public class SecActivityBrowseController extends BaseController {

	@Autowired
	private SecActivityBrowseService secActivityBrowseService;
	
	@ModelAttribute
	public SecActivityBrowse get(@RequestParam(required=false) String id) {
		SecActivityBrowse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secActivityBrowseService.get(id);
		}
		if (entity == null){
			entity = new SecActivityBrowse();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secActivityBrowse:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecActivityBrowse secActivityBrowse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecActivityBrowse> page = secActivityBrowseService.findPage(new Page<SecActivityBrowse>(request, response), secActivityBrowse); 
		model.addAttribute("page", page);
		return "modules/activity/secActivityBrowseList";
	}

	@RequiresPermissions("activity:secActivityBrowse:view")
	@RequestMapping(value = "form")
	public String form(SecActivityBrowse secActivityBrowse, Model model) {
		model.addAttribute("secActivityBrowse", secActivityBrowse);
		return "modules/activity/secActivityBrowseForm";
	}

	@RequiresPermissions("activity:secActivityBrowse:edit")
	@RequestMapping(value = "save")
	public String save(SecActivityBrowse secActivityBrowse, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secActivityBrowse)){
			return form(secActivityBrowse, model);
		}
		secActivityBrowseService.save(secActivityBrowse);
		addMessage(redirectAttributes, "保存浏览记录成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityBrowse/?repage";
	}
	
	@RequiresPermissions("activity:secActivityBrowse:edit")
	@RequestMapping(value = "delete")
	public String delete(SecActivityBrowse secActivityBrowse, RedirectAttributes redirectAttributes) {
		secActivityBrowseService.delete(secActivityBrowse);
		addMessage(redirectAttributes, "删除浏览记录成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityBrowse/?repage";
	}

}