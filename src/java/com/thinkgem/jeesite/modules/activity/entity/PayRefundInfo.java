package com.thinkgem.jeesite.modules.activity.entity;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.IdGen;

public class PayRefundInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String appid;       //小程序appid

	private String mch_id;      //开通微信支付服务会得到一个商户号，需要到微信商户平台上查看
	
	private String sign;        //签名,将数据发送至微信服务器时需要对订单内容进行签名，详情参考微信文档
	
	private String out_trade_no;//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯
	
	private String notify_url;  //通知地址,异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	
	private int total_fee;      //订单总金额，单位为分
	
	private int refund_fee;      //退款总金额，单位为分
	
	private String out_refund_no;//商户系统内部退款单号

	private String nonce_str;   //随机生成32位以内的字符串 
	
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public int getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(int refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}


	//退款构造函数
	public PayRefundInfo(SecPayRefund secPayRefund,HttpServletRequest request) {
		this.setTotal_fee(Integer.parseInt(secPayRefund.getTotal_fee())); // 原订单总金额
		this.setRefund_fee(Integer.parseInt(secPayRefund.getRefund_fee())); // 原订单总金额
		this.setOut_trade_no(secPayRefund.getOut_trade_no());// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一
		this.setOut_refund_no(secPayRefund.getOut_refund_no());
		this.setAppid(Global.getConfig("APP_ID"));
		this.setMch_id(Global.getConfig("MCH_ID")); // 微信支付分配的商户号
		this.setNonce_str(IdGen.randomBase62(32)); // 随机字符串，长度要求在32位以内。推荐随机数生成算法
		this.setNotify_url(Global.getConfig("URL_NOTIFY_REFUND")); // 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	}
}
