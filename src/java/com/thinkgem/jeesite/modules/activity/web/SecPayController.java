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
import com.thinkgem.jeesite.modules.activity.entity.SecPay;
import com.thinkgem.jeesite.modules.activity.service.SecPayService;

/**
 * 活动支付Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secPay")
public class SecPayController extends BaseController {

	@Autowired
	private SecPayService secPayService;
	
	@ModelAttribute
	public SecPay get(@RequestParam(required=false) String id) {
		SecPay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secPayService.get(id);
		}
		if (entity == null){
			entity = new SecPay();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secPay:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecPay secPay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecPay> page = secPayService.findPage(new Page<SecPay>(request, response), secPay); 
		model.addAttribute("page", page);
		return "modules/activity/secPayList";
	}

	@RequiresPermissions("activity:secPay:view")
	@RequestMapping(value = "form")
	public String form(SecPay secPay, Model model) {
		model.addAttribute("secPay", secPay);
		return "modules/activity/secPayForm";
	}

	@RequiresPermissions("activity:secPay:edit")
	@RequestMapping(value = "save")
	public String save(SecPay secPay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secPay)){
			return form(secPay, model);
		}
		secPayService.save(secPay);
		addMessage(redirectAttributes, "保存活动支付成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secPay/?repage";
	}
	
	@RequiresPermissions("activity:secPay:edit")
	@RequestMapping(value = "delete")
	public String delete(SecPay secPay, RedirectAttributes redirectAttributes) {
		secPayService.delete(secPay);
		addMessage(redirectAttributes, "删除活动支付成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secPay/?repage";
	}
	
	/**
	 * 下订单、生成订单  
	 */
	

}