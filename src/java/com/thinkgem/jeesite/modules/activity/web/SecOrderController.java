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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.activity.entity.MerchantOrder;
import com.thinkgem.jeesite.modules.activity.entity.SecActivity;
import com.thinkgem.jeesite.modules.activity.entity.SecOrder;
import com.thinkgem.jeesite.modules.activity.entity.SecPay;
import com.thinkgem.jeesite.modules.activity.entity.SecUser;
import com.thinkgem.jeesite.modules.activity.service.SecOrderService;
import com.thinkgem.jeesite.modules.activity.service.SecPayService;
import com.thinkgem.jeesite.modules.activity.utils.CommonUtils;
import com.thinkgem.jeesite.modules.activity.utils.HttpUtils;

/**
 * 订单表Controller
 * @author 张高旗
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secOrder")
public class SecOrderController extends BaseController {

	@Autowired
	private SecOrderService secOrderService;
	@Autowired
	private SecPayService secPayService;
	
	@ModelAttribute
	public SecOrder get(@RequestParam(required=false) String id) {
		SecOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secOrderService.get(id);
		}
		if (entity == null){
			entity = new SecOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecOrder secOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecOrder> page = secOrderService.findPage(new Page<SecOrder>(request, response), secOrder); 
		model.addAttribute("page", page);
		return "modules/activity/secOrderList";
	}

	@RequiresPermissions("activity:secOrder:view")
	@RequestMapping(value = "form")
	public String form(SecOrder secOrder, Model model) {
		model.addAttribute("secOrder", secOrder);
		return "modules/activity/secOrderForm";
	}

	@RequiresPermissions("activity:secOrder:edit")
	@RequestMapping(value = "save")
	public String save(SecOrder secOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secOrder)){
			return form(secOrder, model);
		}
		secOrderService.save(secOrder);
		addMessage(redirectAttributes, "保存订单表成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secOrder/?repage";
	}
	
	@RequiresPermissions("activity:secOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(SecOrder secOrder, RedirectAttributes redirectAttributes) {
		secOrderService.delete(secOrder);
		addMessage(redirectAttributes, "删除订单表成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secOrder/?repage";
	}
	
	/**
	 * 生成押金订单  
	 * @param secActivity(必须包含活动id)
	 * @param secOrder(必须包含订单类型（0 押金,1 报名）、消费金额（前台不用处理，格式正常如：22.22）)
	 * @param secUser(必须包含用户微信名:wxOpenid)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createOrder")
	public String createOrder_deposit(SecActivity secActivity,SecOrder secOrder,SecUser secUser,HttpServletRequest request) {
		
		boolean result = true;//指示是否正常返回查询结果
		
		Map<String, Object> map =  new HashMap<>();
		if(secActivity.getId()!=null && secOrder.getTotalAmount() != null && secUser.getWxOpenid()!=null){
			secOrder.setActivityId(secActivity.getId());
			secOrder.setOpenid(secUser.getWxOpenid());
			secOrder.setPayStatus(SecOrder.PAY_STATUS_TOPAY);//待支付状态
			List<SecOrder> existList = secOrderService.findList(secOrder);
			if(existList!=null && existList.size()>0){//避免针对同一个活动重复下单
				result = false;
				map.put("error_message", "当前用户当前活动已存在待支付订单信息！");
			}else{
				if(secOrder.getOrderType()!=null && secOrder.getOrderType().equals(SecOrder.ORDER_TYPE_DEPOSIT)){//押金订单
					secOrderService.createOrder_deposit(secActivity,secOrder,secUser,request,map);
				}else if(secOrder.getOrderType()!=null && secOrder.getOrderType().equals(SecOrder.ORDER_TYPE_SIGN)){//报名订单
					secOrderService.createOrder_sign(secActivity,secOrder,secUser,request,map);
				}else{
					result = false;
				}
			}
		}else{
			result = false;
		}
		map.put("result", result);
		return  JSONUtils.toJSONString(map);
	}
	
	/**
	 * 用户支付后，微信会将支付结果发送至我们的小程序系统服务器，我们需要接收信息，比对支付数据，并告知微信服务器支付数据是否正确
	 * 微信会重复发送通知，所以要对已经处理过的通知做判断，防止重复操作数据
	 * 
	 * @param wxNotify
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("notify")
	public String getWxRespones(@RequestBody String wxNotify) throws Exception {

		/**
		 * 定义返回数据常量，要么支付成功，返回PAY_SUCCESS，要么支付失败，返回PAY_FAIL
		 */
		final String PAY_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		final String PAY_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[订单信息有误！]]></return_msg></xml>";
		
		/**
		 * 将获取的xml格式的字符串解析成map对象
		 */
		System.out.println("微信支付回调信息："+wxNotify);
		Map<String, Object> map = CommonUtils.parseXml(wxNotify);
		String result_code = (String) map.get("result_code");
		
		/**
		 * 判断result_code是否为SUCCESS,支付成功微信返回的信息中result_code为SUCCESS,接下去是订单信息，微信返回的信息具体看微信小程序支付文档
		 * 微信返回支付结果通知地址：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_7&index=8
		 */
		if (result_code.replaceAll("<![CDATA[1]]>", "1").equals("SUCCESS")) {
			
			/**
			 * 步骤要点：微信服务器会返回我们之前写入商户订单号，我们自己通过订单号查找我们系统对应那张订单，进行对应信息比较
			 * 这里比较金额相同且用户id，如果微信返回的金额和用户id与我们数据库一致，我们就认为支付成功，返会PAY_SUCCESS给微信服务器
			 * 然后微信服务器就会给我们的商户账号转账
			 */
			SecPay secPayFromWechat = (SecPay) CommonUtils.map2Object(map, SecPay.class);
			SecPay secPayLocal = secPayService.getByOutTradeNo(secPayFromWechat.getOut_trade_no());
			SecOrder secOrderLocal = secOrderService.getByOrderId(secPayFromWechat.getOut_trade_no());
			/**
			 * 比对数据,如果金额相同且用户id相同，则返回PAY_SUCCESS，否则返回PAY_FAIL
			 */
			if (secPayFromWechat.getTotal_fee() == secPayLocal.getTotal_fee() && secPayLocal.getOpenid().equals(secPayFromWechat.getOpenid())) {
				if(secPayLocal.getPayStatus().equals(SecPay.PAY_STATUS_CONFIRM)){
					//订单已处理过
					
				}else{
					//更新订单表信息
					secOrderLocal.setPayStatus(SecOrder.PAY_STATUS_PAID);//已完成支付
					secOrderService.save(secOrderLocal);
					
					//更新支付表信息
					secPayLocal.setAppid(secPayFromWechat.getAppid());
					secPayLocal.setMch_id(secPayFromWechat.getMch_id());
					secPayLocal.setDevice_info(secPayFromWechat.getDevice_info());
					secPayLocal.setNonce_str(secPayFromWechat.getNonce_str());
					secPayLocal.setSign(secPayFromWechat.getSign());
					secPayLocal.setSign_type(secPayFromWechat.getSign_type());
					secPayLocal.setIs_subscribe(secPayFromWechat.getIs_subscribe());
					secPayLocal.setTrade_type(secPayFromWechat.getTrade_type());
					secPayLocal.setBank_type(secPayFromWechat.getBank_type());
					if(secPayFromWechat.getSettlement_total_fee()!=null){
						secPayLocal.setSettlement_total_fee(secPayFromWechat.getSettlement_total_fee().toString());
					}
					secPayLocal.setFee_type(secPayFromWechat.getFee_type());
					if(secPayFromWechat.getCash_fee()!=null){
						secPayLocal.setCash_fee(secPayFromWechat.getCash_fee().toString());
					}
					secPayLocal.setCash_fee_type(secPayFromWechat.getCash_fee_type());
					if(secPayFromWechat.getCoupon_fee()!=null){
						secPayLocal.setCash_fee(secPayFromWechat.getCoupon_fee().toString());
					}
					secPayLocal.setCoupon_count(secPayFromWechat.getCoupon_count());
					secPayLocal.setTransaction_id(secPayFromWechat.getTransaction_id());
					secPayLocal.setTime_end(secPayFromWechat.getTime_end());
					secPayLocal.setUpdateDate(new Date());
					secPayLocal.setPayStatus(SecPay.PAY_STATUS_CONFIRM);
					secPayService.save(secPayLocal);
				}
				return PAY_SUCCESS;
			}
			return PAY_FAIL;
		}
		return PAY_FAIL;
	}

}