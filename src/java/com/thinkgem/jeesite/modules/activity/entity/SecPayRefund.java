package com.thinkgem.jeesite.modules.activity.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 活动退款信息记录Entity
 * @author 黄亮亮
 * @version 2020-06-06
 */
public class SecPayRefund extends DataEntity<SecPayRefund> {
	private static final long serialVersionUID = 1L;
	private String openid;		// 用户标识
	private String out_trade_no;		// 商户订单号
	private String out_refund_no;		// 商户退款单号
	private String refund_id;		//微信退款单号
	private String appid;		// 微信分配的小程序ID
	private String mch_id;		// 微信支付分配的商户号
	private String nonce_str;		// 随机字符串
	private String sign;		// 签名
	private String sign_type;		// 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
	private Integer total_fee;		// 订单总金额，单位为分
	private Integer refund_fee;		// 退款总金额，单位为分
	private String refund_desc;		//退款原因
	private Integer settlement_total_fee;		// 应结订单金额=订单金额-非充值代金券金额，应结订单金额									&lt;/td&gt;									&lt;td nowrap&gt;										&lt;input type=
	private Integer settlement_refund_fee;		// 应结退款金额							&lt;/td&gt;									&lt;td nowrap&gt;										&lt;input type=
	private String refund_fee_type;		// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private String fee_type;		// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private Integer cash_fee;		// 现金支付金额订单现金支付金额，
	private Integer cash_refund_fee;		// 现金退款金额
	private String cash_fee_type;		// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private Integer coupon_refund_fee;		// 代金券退款金额									&lt;/td&gt;									&lt;td nowrap&gt;										&lt;input type=
	private String coupon_refund_count;		// 退款代金券使用数量
	private String transaction_id;		// 微信支付订单号
	private String refundStatus;	//退款状态
	
	private Date success_time;	//退款成功时间
	private String refund_recv_accout;	//取当前退款单的退款入账方
	private String refund_account;	//退款资金来源
	private String refund_request_source;	//退款发起来源
	
	
	private String err_code;//错误代码
	private String err_code_des;//错误描述
	
	public static final String REFUND_STATUS_WAIT = "0";	//已创建待退款
	public static final String REFUND_STATUS_APPLY = "1";	//申请退款失败
	public static final String REFUND_STATUS_APPLY_SUCCESS = "2";	//申请退款成功
	public static final String REFUND_STATUS_APPLY_ERROR = "3";	//申请退款异常
	public static final String REFUND_STATUS_SUCCESS = "4";	//退款成功
	public static final String REFUND_STATUS_CHANGE = "5";	//退款异常
	public static final String REFUND_STATUS_REFUNDCLOSE = "6";	//退款关闭
	
	public SecPayRefund() {
		super();
	}

	public SecPayRefund(String id){
		super(id);
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = Integer.parseInt(total_fee);
	}

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = Integer.parseInt(refund_fee);
	}

	public String getRefund_desc() {
		return refund_desc;
	}

	public void setRefund_desc(String refund_desc) {
		this.refund_desc = refund_desc;
	}

	public Integer getSettlement_total_fee() {
		return settlement_total_fee;
	}

	public void setSettlement_total_fee(String settlement_total_fee) {
		this.settlement_total_fee = Integer.parseInt(settlement_total_fee);
	}

	public Integer getSettlement_refund_fee() {
		return settlement_refund_fee;
	}

	public void setSettlement_refund_fee(String settlement_refund_fee) {
		this.settlement_refund_fee = Integer.parseInt(settlement_refund_fee);
	}

	public String getRefund_fee_type() {
		return refund_fee_type;
	}

	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(String cash_fee) {
		this.cash_fee = Integer.parseInt(cash_fee);
	}

	public Integer getCash_refund_fee() {
		return cash_refund_fee;
	}

	public void setCash_refund_fee(String cash_refund_fee) {
		this.cash_refund_fee = Integer.parseInt(cash_refund_fee);
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public Integer getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(String coupon_refund_fee) {
		this.coupon_refund_fee = Integer.parseInt(coupon_refund_fee);
	}

	public String getCoupon_refund_count() {
		return coupon_refund_count;
	}

	public void setCoupon_refund_count(String coupon_refund_count) {
		this.coupon_refund_count = coupon_refund_count;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public Date getSuccess_time() {
		return success_time;
	}

	public void setSuccess_time(Date success_time) {
		this.success_time = success_time;
	}

	public String getRefund_recv_accout() {
		return refund_recv_accout;
	}

	public void setRefund_recv_accout(String refund_recv_accout) {
		this.refund_recv_accout = refund_recv_accout;
	}

	public String getRefund_account() {
		return refund_account;
	}

	public void setRefund_account(String refund_account) {
		this.refund_account = refund_account;
	}

	public String getRefund_request_source() {
		return refund_request_source;
	}

	public void setRefund_request_source(String refund_request_source) {
		this.refund_request_source = refund_request_source;
	}

	
}
