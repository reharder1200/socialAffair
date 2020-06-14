/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.web;
 

import java.security.Security;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.net.aso.MD5;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.activity.entity.SecOrder;
import com.thinkgem.jeesite.modules.activity.entity.SecPay;
import com.thinkgem.jeesite.modules.activity.entity.SecPayRefund;
import com.thinkgem.jeesite.modules.activity.entity.SecRefund;
import com.thinkgem.jeesite.modules.activity.service.SecPayRefundService;
import com.thinkgem.jeesite.modules.activity.service.SecRefundService;
import com.thinkgem.jeesite.modules.activity.utils.CommonUtils;

/**
 * 退款表Controller
 * @author 黄亮亮
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/secRefund")
public class SecRefundController extends BaseController {

	@Autowired
	private SecRefundService secRefundService;
	@Autowired
	private SecPayRefundService secPayRefundService;
	
	@ModelAttribute
	public SecRefund get(@RequestParam(required=false) String id) {
		SecRefund entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = secRefundService.get(id);
		}
		if (entity == null){
			entity = new SecRefund();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:secRefund:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecRefund secRefund, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SecRefund> page = secRefundService.findPage(new Page<SecRefund>(request, response), secRefund); 
		model.addAttribute("page", page);
		return "modules/activity/secRefundList";
	}

	@RequiresPermissions("activity:secRefund:view")
	@RequestMapping(value = "form")
	public String form(SecRefund secRefund, Model model) {
		model.addAttribute("secRefund", secRefund);
		return "modules/activity/secRefundForm";
	}

	@RequiresPermissions("activity:secRefund:edit")
	@RequestMapping(value = "save")
	public String save(SecRefund secRefund, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, secRefund)){
			return form(secRefund, model);
		}
		secRefundService.save(secRefund);
		addMessage(redirectAttributes, "保存退款表成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secRefund/?repage";
	}
	
	@RequiresPermissions("activity:secRefund:edit")
	@RequestMapping(value = "delete")
	public String delete(SecRefund secRefund, RedirectAttributes redirectAttributes) {
		secRefundService.delete(secRefund);
		addMessage(redirectAttributes, "删除退款表成功");
		return "redirect:"+Global.getAdminPath()+"/activity/secRefund/?repage";
	}

	/**
	 * 生成退款订单  
	 * @param secActivity(必须包含退款订单id,活动id)
	 * @param secRefund(必须包含消费金额（totalFee,前台不用处理，格式正常如：22.22）)
	 * @param secUser(必须包含用户微信名:wxOpenid)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createOrder")
	public String createOrder(SecRefund secRefund,HttpServletRequest request) {
		
		boolean result = true;//指示是否正常返回查询结果
		Map<String, Object> map =  new HashMap<>();
		if(secRefund.getOrderId()!=null &&secRefund.getActivityId()!=null && secRefund.getTotalFee() != null && secRefund.getOpenid()!=null){
			SecPayRefund secPayRefund = secRefundService.createOrder_refund(secRefund);
			if(secPayRefund!=null){
				secRefundService.post_refund_order(secPayRefund, request, map);//发送退款请求
			}else{
				result = false;
			}
		}else{
			result = false;
		}
		map.put("result", result);
		return  JSONUtils.toJSONString(map);
	}
	
	/**
	 * 退款成功的回调
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
		final String PAY_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[退款信息有误！]]></return_msg></xml>";
		
		/**
		 * 将获取的xml格式的字符串解析成map对象
		 */
		System.out.println("退款回调信息："+wxNotify);
		Map<String, Object> map = CommonUtils.parseXml(wxNotify);
		String result_code = (String) map.get("result_code");
		System.out.println(result_code);
		
		/**
		 * 判断result_code是否为SUCCESS,支付成功微信返回的信息中result_code为SUCCESS,接下去是订单信息，微信返回的信息具体看微信小程序支付文档
		 * 微信返回支付结果通知地址：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_7&index=8
		 */
		if (result_code.replaceAll("<![CDATA[1]]>", "1").equals("SUCCESS")) {
			
			//获得加密信息
			String req_info = (String) map.get("req_info");
			
			//解密
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] str_decode = decoder.decode(req_info);
			String mchStr = CommonUtils.getMD5((String)Global.getConfig("APP_KEY"));
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");  //为了使用此解密算法，需要替换系统jdk环境下的jre1.8-lib-security下的local_policy.jar和US_export_policy.jar文件，同时导入bcprov-jdk15on-1.49.jar包
	        SecretKeySpec key = new SecretKeySpec(mchStr.toLowerCase().getBytes(), "AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);  
	        byte[] doFinal = cipher.doFinal(str_decode);
	        String resultStr = new String(doFinal,"utf-8"); 
	        Map<String, Object> resultMap = CommonUtils.parseXml(resultStr);
			
			/**
			 * 步骤要点：微信服务器会返回我们之前写入商户订单号，我们自己通过订单号查找我们系统对应那张订单，进行对应信息比较
			 * 这里比较金额相同且用户id，如果微信返回的金额和用户id与我们数据库一致，我们就认为支付成功，返会PAY_SUCCESS给微信服务器
			 * 然后微信服务器就会给我们的商户账号转账
			 */
			SecPayRefund secPayRefundFromWechat = (SecPayRefund) CommonUtils.map2Object(resultMap, SecPayRefund.class);
			SecPayRefund secPayRefundLocal = secPayRefundService.getByOutRefundNo(secPayRefundFromWechat.getOut_refund_no());
			SecRefund secRefundLocal = secRefundService.getByRefundId(secPayRefundFromWechat.getOut_refund_no());
			/**
			 * 比对数据,如果金额相同且退款订单号相同，则返回PAY_SUCCESS，否则返回PAY_FAIL
			 */
			if (secPayRefundFromWechat.getRefund_fee() == secPayRefundLocal.getRefund_fee() && secPayRefundFromWechat.getOut_refund_no().equals(secPayRefundLocal.getOut_refund_no())) {
				if(secRefundLocal.getRefundStatus().equals(SecRefund.REFUND_STATUS_COMPLETE)){
					//订单已处理过
					
				}else{
					//更新退款单信息
					secRefundLocal.setRefundStatus((SecRefund.REFUND_STATUS_COMPLETE));
					secRefundService.save(secRefundLocal);
					
					//更新退款订单表信息
					secPayRefundLocal.setSuccess_time(secPayRefundFromWechat.getSuccess_time());
					if(secPayRefundFromWechat.getSettlement_refund_fee()!=null){
						secPayRefundLocal.setSettlement_refund_fee(secPayRefundFromWechat.getSettlement_refund_fee().toString());
					}
					secPayRefundLocal.setRefund_recv_accout(secPayRefundFromWechat.getRefund_recv_accout());
					secPayRefundLocal.setRefund_account(secPayRefundFromWechat.getRefund_account());
					secPayRefundLocal.setRefund_request_source(secPayRefundFromWechat.getRefund_request_source());
					if(resultMap.get("refund_status").toString().equals("SUCCESS")){
						secPayRefundLocal.setRefundStatus(SecPayRefund.REFUND_STATUS_SUCCESS);
					}else if(resultMap.get("refund_status").toString().equals("CHANGE")){
						secPayRefundLocal.setRefundStatus(SecPayRefund.REFUND_STATUS_CHANGE);
					}else if(resultMap.get("refund_status").toString().equals("REFUNDCLOSE")){
						secPayRefundLocal.setRefundStatus(SecPayRefund.REFUND_STATUS_REFUNDCLOSE);
					}
					secPayRefundService.save(secPayRefundLocal);
				}
				return PAY_SUCCESS;
			}
			return PAY_FAIL;
		}
		return PAY_FAIL;
	}
}