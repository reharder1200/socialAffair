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
import com.thinkgem.jeesite.modules.activity.entity.SecImage;
import com.thinkgem.jeesite.modules.activity.service.SecImageService;

/**
 * 保存图片Controller
 * @author 张高旗
 * @version 2020-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secImage")
public class SecImageController extends BaseController {

	@Autowired
	private SecImageService secImageService;
	
	@ModelAttribute
	public SecImage get(@RequestParam(required=false) String id) {
		SecImage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secImageService.get(id);
		}
		if (entity == null){
			entity = new SecImage();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secImage:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecImage secImage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecImage> page = secImageService.findPage(new Page<SecImage>(request, response), secImage); 
		model.addAttribute("page", page);
		return "modules/activity/secImageList";
	}

	@RequiresPermissions("activity:secImage:view")
	@RequestMapping(value = "form")
	public String form(SecImage secImage, Model model) {
		model.addAttribute("secImage", secImage);
		return "modules/activity/secImageForm";
	}

	@RequiresPermissions("activity:secImage:edit")
	@RequestMapping(value = "save")
	public String save(SecImage secImage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secImage)){
			return form(secImage, model);
		}
		secImageService.save(secImage);
		addMessage(redirectAttributes, "保存保存图片成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secImage/?repage";
	}
	
	@RequiresPermissions("activity:secImage:edit")
	@RequestMapping(value = "delete")
	public String delete(SecImage secImage, RedirectAttributes redirectAttributes) {
		secImageService.delete(secImage);
		addMessage(redirectAttributes, "删除保存图片成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secImage/?repage";
	}

}