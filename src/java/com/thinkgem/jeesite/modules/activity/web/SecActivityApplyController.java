/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.web;

import java.util.Date;
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
import com.thinkgem.jeesite.modules.activity.entity.SecActivityApply;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityCollection;
import com.thinkgem.jeesite.modules.activity.service.SecActivityApplyService;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 活动应用Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secActivityApply")
public class SecActivityApplyController extends BaseController {

	@Autowired
	private SecActivityApplyService secActivityApplyService;
	
	@ModelAttribute
	public SecActivityApply get(@RequestParam(required=false) String id) {
		SecActivityApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secActivityApplyService.get(id);
		}
		if (entity == null){
			entity = new SecActivityApply();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secActivityApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecActivityApply secActivityApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecActivityApply> page = secActivityApplyService.findPage(new Page<SecActivityApply>(request, response), secActivityApply); 
		model.addAttribute("page", page);
		return "modules/activity/secActivityApplyList";
	}

	@RequiresPermissions("activity:secActivityApply:view")
	@RequestMapping(value = "form")
	public String form(SecActivityApply secActivityApply, Model model) {
		model.addAttribute("secActivityApply", secActivityApply);
		return "modules/activity/secActivityApplyForm";
	}

	@RequiresPermissions("activity:secActivityApply:edit")
	@RequestMapping(value = "save")
	public String save(SecActivityApply secActivityApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secActivityApply)){
			return form(secActivityApply, model);
		}
		secActivityApplyService.save(secActivityApply);
		addMessage(redirectAttributes, "保存活动应用成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityApply/?repage";
	}
	
	@RequiresPermissions("activity:secActivityApply:edit")
	@RequestMapping(value = "delete")
	public String delete(SecActivityApply secActivityApply, RedirectAttributes redirectAttributes) {
		secActivityApplyService.delete(secActivityApply);
		addMessage(redirectAttributes, "删除活动应用成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secActivityApply/?repage";
	}

	/**
	 * 报名活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "applyActivity", method = RequestMethod.GET , produces = "application/json")
	public String  applyActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		String activityId = request.getParameter("activityId");
		
		
		
		String userId = request.getParameter("userId");
		
		try {
			 
				SecActivityApply applySelect = new SecActivityApply();
				applySelect.setUserId(userId);
				applySelect.setActivityId(activityId);
				List<SecActivityApply>  list = secActivityApplyService.findList(applySelect);
				SecActivityApply apply;
				if(list!=null&&list.size()>0) {
					apply = list.get(0);
					apply.setSignType("0");
					User user = new User();
					user.setId(userId);
					apply.setUser(user);
					apply.setSignDate(new Date());
				}else { 
					apply = new SecActivityApply();
					apply.setActivityId(activityId);
					User user = new User();
					user.setId(userId);
					apply.setSignType("0");
					apply.setIsEvaluate("0");
					apply.setSignDate(new Date());
					apply.setUser(user);
				}
			
				secActivityApplyService.save(apply);
			 
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "报名成功");
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		
		return JSONUtils.toJSONString(map); 
	}
	
	/**
	 * 参与报名活动
	 * @param secActivity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "joinActivity", method = RequestMethod.GET , produces = "application/json")
	public String  joinActivity(HttpServletRequest request) {
		Map<String, Object> map =  new HashMap<>();
		try {
			secActivityApplyService.joinActivity(map,request);
			map.put("status", "1");
			map.put("code", "1");
			map.put("description", "成功");
		} catch (Exception e) {
			map.put("status", "-1");
			map.put("code", "1");
			map.put("description", "失败");
		}
		
		
		return JSONUtils.toJSONString(map); 
	}
	
}