/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.activity.entity.SecPayRefund;
import com.thinkgem.jeesite.modules.activity.entity.SecRefund;
import com.thinkgem.jeesite.modules.activity.utils.WxPayUtil;
import com.thinkgem.jeesite.modules.activity.dao.SecRefundDao;

/**
 * 退款表Service
 * @author 张高旗
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecRefundService extends CrudService<SecRefundDao, SecRefund> {

	@Autowired
	private SecPayRefundService secPayRefundService;
	
	public SecRefund get(String id) {
		return super.get(id);
	}
	
	public List<SecRefund> findList(SecRefund secRefund) {
		return super.findList(secRefund);
	}
	
	public Page<SecRefund> findPage(Page<SecRefund> page, SecRefund secRefund) {
		return super.findPage(page, secRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(SecRefund secRefund) {
		super.save(secRefund);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecRefund secRefund) {
		super.delete(secRefund);
	}

	@Transactional(readOnly = false)
	public SecPayRefund createOrder_refund(SecRefund secRefund) {
		/* 构建微信退款订单时 */
		SecPayRefund secPayRefund = new SecPayRefund();
		String outRefundNo = IdGen.randomBase62(32);//生成唯一的退款订单号
		secPayRefund.setTotal_fee(new Double(Double.parseDouble(secRefund.getTotalFee())*100).intValue());//发送到微信后台的总金额单位是分，必须为整数
		secPayRefund.setRefund_fee(new Double(Double.parseDouble(secRefund.getTotalFee())*100).intValue());//发送到微信后台的总金额单位是分，必须为整数
		secPayRefund.setOut_trade_no(secRefund.getOrderId());
		secPayRefund.setOut_refund_no(outRefundNo);
		secPayRefund.setRefundStatus(SecPayRefund.REFUND_STATUS_WAIT);//待退款状态
		secPayRefund.setOpenid(secPayRefund.getOpenid());//获取微信小程序用户的openid并添加进退款订单中
		secPayRefundService.save(secPayRefund);
		
		/*更新退款订单号*/
		secRefund.setOutRefundNo(outRefundNo);
		save(secRefund);
		
		return secPayRefund;
	}
	
	@Transactional(readOnly = false)
	public void post_refund_order(SecPayRefund secPayRefund, HttpServletRequest request,
			Map<String, Object> map) {
		try {
			SecPayRefund secPayRefundFromWechat = WxPayUtil.refundOrder(secPayRefund, request,map);
			
			if(secPayRefundFromWechat!=null){
				if(map.get("wxResponse").toString().equals("SUCCESS")){
					secPayRefundFromWechat.setId(secPayRefund.getId());
					secPayRefundFromWechat.setIsNewRecord(false);
					secPayRefundFromWechat.setRefundStatus(SecPayRefund.REFUND_STATUS_APPLY_SUCCESS);
					secPayRefundService.save(secPayRefundFromWechat);
				}else if(map.get("wxResponse").toString().equals("UNSUCCESS")){
					secPayRefund.setErr_code(secPayRefundFromWechat.getErr_code());
					secPayRefund.setErr_code_des(secPayRefundFromWechat.getErr_code_des());
					secPayRefund.setRefundStatus(SecPayRefund.REFUND_STATUS_APPLY_ERROR);
					secPayRefundService.save(secPayRefund);
				}else if(map.get("wxResponse").toString().equals("FAIL")){
					secPayRefund.setErr_code_des(map.get("failReason").toString());
					secPayRefund.setRefundStatus(SecPayRefund.REFUND_STATUS_APPLY);
					secPayRefundService.save(secPayRefund);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public SecRefund getByRefundId(String out_refund_no) {
		// TODO Auto-generated method stub
		return dao.getByRefundId(out_refund_no);
	}
	
}