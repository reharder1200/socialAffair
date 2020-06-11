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
import com.thinkgem.jeesite.modules.activity.entity.SecActivityAlarm;
import com.thinkgem.jeesite.modules.activity.service.SecActivityAlarmService;

/**
 * 活动提醒Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secActivityAlarm")
public class SecActivityAlarmController extends BaseController {

	@Autowired
	private SecActivityAlarmService secActivityAlarmService;
	
	@ModelAttribute
	public SecActivityAlarm get(@RequestParam(required=false) String id) {
		SecActivityAlarm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secActivityAlarmService.get(id);
		}
		if (entity == null){
			entity = new SecActivityAlarm();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secActivityAlarm:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecActivityAlarm secActivityAlarm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecActivityAlarm> page = secActivityAlarmService.findPage(new Page<SecActivityAlarm>(request, response), secActivityAlarm); 
		model.addAttribute("page", page);
		return "modules/activity/secActivityAlarmList";
	}

	@RequiresPermissions("activity:secActivityAlarm:view")
	@RequestMapping(value = "form")
	public String form(SecActivityAlarm secActivityAlarm, Model model) {
		model.addAttribute("secActivityAlarm", secActivityAlarm);
		return "modules/activity/secActivityAlarmForm";
	}

	@RequiresPermissions("activity:secActivityAlarm:edit")
	@RequestMapping(value = "save")
	public String save(SecActivityAlarm secActivityAlarm, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secActivityAlarm)){
			return form(secActivityAlarm, model);
		}
		secActivityAlarmService.save(secActivityAlarm);
		addMessage(redirectAttributes, "保存活动提醒成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityAlarm/?repage";
	}
	
	@RequiresPermissions("activity:secActivityAlarm:edit")
	@RequestMapping(value = "delete")
	public String delete(SecActivityAlarm secActivityAlarm, RedirectAttributes redirectAttributes) {
		secActivityAlarmService.delete(secActivityAlarm);
		addMessage(redirectAttributes, "删除活动提醒成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityAlarm/?repage";
	}

}