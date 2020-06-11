/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.cmd.SaveAttachmentCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.activity.entity.MerchantOrder;
import com.thinkgem.jeesite.modules.activity.entity.SecActivity;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityApply;
import com.thinkgem.jeesite.modules.activity.entity.SecOrder;
import com.thinkgem.jeesite.modules.activity.entity.SecPay;
import com.thinkgem.jeesite.modules.activity.entity.SecUser;
import com.thinkgem.jeesite.modules.activity.utils.HttpUtils;
import com.thinkgem.jeesite.modules.activity.utils.WxPayUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sun.net.www.http.HttpClient;

import com.thinkgem.jeesite.modules.activity.dao.SecOrderDao;

/**
 * 订单表Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecOrderService extends CrudService<SecOrderDao, SecOrder> {

	@Autowired
	SecActivityService secActivityService = new SecActivityService();
	@Autowired
	SecUserService secUserService = new SecUserService();
	@Autowired
	SecPayService secPayService = new SecPayService();
	@Autowired
	SecActivityApplyService secActivityApplyService = new SecActivityApplyService();
	
	public SecOrder get(String id) {
		return super.get(id);
	}
	
	public List<SecOrder> findList(SecOrder secOrder) {
		return super.findList(secOrder);
	}
	
	public Page<SecOrder> findPage(Page<SecOrder> page, SecOrder secOrder) {
		return super.findPage(page, secOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(SecOrder secOrder) {
		super.save(secOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecOrder secOrder) {
		super.delete(secOrder);
	}

	/**
	 * 创建押金订单
	 */
	@Transactional(readOnly = false)
	public void createOrder_deposit(SecActivity secActivity,SecOrder secOrder,SecUser secUser,HttpServletRequest request, Map<String, Object> map) {
		/* 创建一个订单，状态为预支付 */
		SecOrder secOrderNew = new SecOrder();
		String orderId = IdGen.randomBase62(32);//生成唯一的订单号
		secOrderNew.setOrderId(orderId);
		secOrderNew.setOrderType(SecOrder.ORDER_TYPE_DEPOSIT);
		secOrderNew.setPayType(SecOrder.PAY_TYPE_WECHAT);//默认微信支付
		secOrderNew.setPayStatus(SecOrder.PAY_STATUS_TOPAY);//支付状态：待支付
		secOrderNew.setTotalAmount(secOrder.getTotalAmount());//总金额
		save(secOrderNew);

		/*活动记录表更新押金订单号*/
		SecActivity secActivity_init = secActivityService.get(secActivity.getId());
		secActivity_init.setOrderId(orderId);
		secActivityService.save(secActivity_init);
		
		/* 构建微信订单时需要上传商品基本信息 */
		SecPay secPay = new SecPay();
		secPay.setAttach("starter_pay");//备注：随便写
		secPay.setBody("deposit_pay");//商品描述
		secPay.setDetail("starter_pay_deposit");//商品详情
		secPay.setTotal_fee(new Double(Double.parseDouble(secOrder.getTotalAmount())*100).intValue());//发送到微信后台的总金额单位是分，必须为整数
		secPay.setOut_trade_no(orderId);
		secPay.setPayStatus(SecPay.PAY_STATUS_WAIT);//支付状态
		secPay.setOpenid(secUser.getWxOpenid());//获取当前微信小程序用户的openid并添加进商户订单中
		secPayService.save(secPay);
		
	    try {
	    	WxPayUtil.unifiedorder(secPay, request,map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建报名订单
	 */
	@Transactional(readOnly = false)
	public void createOrder_sign(SecActivity secActivity,SecOrder secOrder,SecUser secUser,HttpServletRequest request, Map<String, Object> map) {
		/* 创建一个订单，状态为预支付 */
		SecOrder secOrderNew = new SecOrder();
		String orderId = IdGen.randomBase62(32);//生成唯一的订单号
		secOrderNew.setOrderId(orderId);
		secOrderNew.setOrderType(SecOrder.ORDER_TYPE_SIGN);
		secOrderNew.setPayType(SecOrder.PAY_TYPE_WECHAT);//默认微信支付
		secOrderNew.setPayStatus(SecOrder.PAY_STATUS_TOPAY);//支付状态：待支付
		secOrderNew.setTotalAmount(secOrder.getTotalAmount());//总金额
		save(secOrderNew);
		
		/*活动申请表更新报名订单号*/
		SecActivityApply secActivityApplyCondition = new SecActivityApply();
		secActivityApplyCondition.setActivityId(secActivity.getId());
		secActivityApplyCondition.setUserId(secUser.getId());
		List<SecActivityApply> secActivityApplyList = secActivityApplyService.findList(secActivityApplyCondition);
		if(secActivityApplyList!=null && secActivityApplyList.size()>0){
			SecActivityApply secActivityApply = secActivityApplyList.get(0);
			secActivityApply.setOrderId(orderId);
			secActivityApplyService.save(secActivityApply);
		}
		
		/* 构建微信订单时需要上传商品基本信息 */
		SecPay secPay = new SecPay();
		secPay.setAttach("candidate_pay");//备注：随便写
		secPay.setBody("sign_pay");//商品描述
		secPay.setDetail("candidate_pay_sign");//商品详情
		secPay.setTotal_fee(new Double(Double.parseDouble(secOrder.getTotalAmount())*100).intValue());//发送到微信后台的总金额单位是分，必须为整数
		secPay.setOut_trade_no(orderId);
		secPay.setPayStatus(SecPay.PAY_STATUS_WAIT);//支付状态
		secPay.setOpenid(secUser.getWxOpenid());//获取当前微信小程序用户的openid并添加进商户订单中
		secPayService.save(secPay);
		
	    try {
	    	WxPayUtil.unifiedorder(secPay, request,map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	
	public static void main(String[] args) {
		//createOrder(null,null);
		String string = "123.98";
		System.out.print(new Double(Double.parseDouble(string)*100).intValue());
	}

	public SecOrder getByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return dao.getByOrderId(orderId);
	}
	
}