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
import com.thinkgem.jeesite.modules.activity.entity.SecUserDetail;
import com.thinkgem.jeesite.modules.activity.service.SecUserDetailService;

/**
 * 用户实名信息表Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secUserDetail")
public class SecUserDetailController extends BaseController {

	@Autowired
	private SecUserDetailService secUserDetailService;
	
	@ModelAttribute
	public SecUserDetail get(@RequestParam(required=false) String id) {
		SecUserDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secUserDetailService.get(id);
		}
		if (entity == null){
			entity = new SecUserDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secUserDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecUserDetail secUserDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecUserDetail> page = secUserDetailService.findPage(new Page<SecUserDetail>(request, response), secUserDetail); 
		model.addAttribute("page", page);
		return "modules/activity/secUserDetailList";
	}

	@RequiresPermissions("activity:secUserDetail:view")
	@RequestMapping(value = "form")
	public String form(SecUserDetail secUserDetail, Model model) {
		model.addAttribute("secUserDetail", secUserDetail);
		return "modules/activity/secUserDetailForm";
	}

	@RequiresPermissions("activity:secUserDetail:edit")
	@RequestMapping(value = "save")
	public String save(SecUserDetail secUserDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secUserDetail)){
			return form(secUserDetail, model);
		}
		secUserDetailService.save(secUserDetail);
		addMessage(redirectAttributes, "保存用户实名信息表成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secUserDetail/?repage";
	}
	
	@RequiresPermissions("activity:secUserDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(SecUserDetail secUserDetail, RedirectAttributes redirectAttributes) {
		secUserDetailService.delete(secUserDetail);
		addMessage(redirectAttributes, "删除用户实名信息表成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secUserDetail/?repage";
	}
	
	/**
	 * 保存微信信息
	 * @param wxSign
	 * @param wxOpenid
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateUser", method = RequestMethod.GET , produces = "application/json")
	public String  updateUser( HttpServletRequest request) {
		Map<String, String> map =  new HashMap<>();
		try {
			secUserDetailService.updateUser(map,request);
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		return JSONUtils.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "showUser", method = RequestMethod.GET , produces = "application/json")
	public String  showUser( HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secUserDetailService.showUser(map,request);
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		return  JSONUtils.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "showBrieflyUser", method = RequestMethod.GET , produces = "application/json")
	public String  showBrieflyUser( HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secUserDetailService.showBrieflyUser(map,request);
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		return  JSONUtils.toJSONString(map);
	}

}