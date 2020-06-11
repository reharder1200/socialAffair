/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 订单表Entity
 * @author 张高旗
 * @version 2020-04-03
 */
public class SecOrder extends DataEntity<SecOrder> {
	
	private static final long serialVersionUID = 1L;
	private String orderId;		// order_id
	private String orderType;		// order_type
	private String payType;		// pay_type
	private String payStatus;		// pay_status
	private String totalAmount;		// total_amount
	
	/*订单类型*/
	public static final String ORDER_TYPE_DEPOSIT = "0";//押金
	public static final String ORDER_TYPE_SIGN = "1";//报名
	
	/*支付方式*/
	public static final String PAY_TYPE_WECHAT = "0";//微信
	public static final String PAY_TYPE_ALIPAY = "1";//支付宝
	public static final String PAY_TYPE_CARD = "2";//银行卡
	public static final String PAY_TYPE_OTHER = "3";//其他
	
	/*支付状态*/
	public static final String PAY_STATUS_TOPAY = "0";//待支付（预支付）
	public static final String PAY_STATUS_PAID = "1";//已支付
	
	public SecOrder() {
		super();
	}

	public SecOrder(String id){
		super(id);
	}

	@Length(min=0, max=100, message="order_id长度必须介于 0 和 100 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=0, max=2, message="order_type长度必须介于 0 和 2 之间")
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@Length(min=0, max=2, message="pay_type长度必须介于 0 和 2 之间")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	@Length(min=0, max=2, message="pay_status长度必须介于 0 和 2 之间")
	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}