package com.thinkgem.jeesite.modules.activity.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.activity.entity.PayInfo;
import com.thinkgem.jeesite.modules.activity.entity.SecPay;
import com.thinkgem.jeesite.modules.activity.entity.SecPayRefund;
import com.thinkgem.jeesite.modules.activity.entity.WxPayResponesModel;
import com.thinkgem.jeesite.modules.activity.service.SecPayRefundService;

/**
 * 统一下单接口步骤： 1.根据订单内容，构建微信订单信息 2.对订单信息进行签名
 * 3.将签名加入微信订单信息，将订单对象内的信息转化为xml格式数据，发送至微信服务器
 * 4.解析微信服务器返回的数据(微信服务器返回xml数据)，主要获取其中的prepay_id，nonceStr
 * 5.对五个字段进行签名后，连同参与签名的字段和签名返回给调用此接口的客户端（五个字段：appId,timeStamp,nonceStr,prepay_id,signType区分大小写，不能写错）
 * 6.微信服务器进行签名验证后，允许小程序拉起支付，用户输入密码后，支付成功。
 * 
 * @param outTradeNo 商户订单号：此订单号目的是获取商户系统的订单数据，并根据微信订单字段要求，构建微信订单信息(实际上只要成功构建正确的微信订单信息（payInfo），不管你怎么获取信息都行)
 *                   注：商户订单由后台人员自己构建，这里
 * @return
 * @throws Exception
 */
public class WxPayUtil {
	
	private static final Logger log = LoggerFactory.getLogger(WxPayUtil.class);
	
	/**
	 * 统一下单接口步骤： 1.根据订单内容，构建微信订单信息 2.对订单信息进行签名
	 * 3.将签名加入微信订单信息，将订单对象内的信息转化为xml格式数据，发送至微信服务器
	 * 4.解析微信服务器返回的数据(微信服务器返回xml数据)，主要获取其中的prepay_id，nonceStr
	 * 5.对五个字段进行签名后，连同参与签名的字段和签名返回给调用此接口的客户端（五个字段：appId,timeStamp,nonceStr,prepay_id,signType区分大小写，不能写错）
	 * 6.微信服务器进行签名验证后，允许小程序拉起支付，用户输入密码后，支付成功。
	 * 
	 * @param secPay 商户订单由后台人员自己构建，根据微信订单字段要求，构建微信订单信息(实际上只要成功构建正确的微信订单信息（payInfo），不管你怎么获取信息都行)
	 * @return
	 * @throws Exception
	 */
	public static void unifiedorder(SecPay secPay,HttpServletRequest request,Map<String, Object> mapResult)
			throws Exception {

		/**
		 * 根据商户订单构建微信订单
		 */
		PayInfo payInfo = new PayInfo(secPay, request);

		/**
		 * 构建完订单对象，对订单信息进行签名
		 */
		Map<String, Object> map = CommonUtils.object2Map(payInfo);
		String sign = CommonUtils.createSign(Global.getConfig("APP_KEY"), map);

		/**
		 * 将签名sign写入微信订单信息
		 */
		payInfo.setSign(sign);

		/**
		 * 将订单信息转化为xml格式
		 */
		String xml = CommonUtils.objectToXML(payInfo);

		/**
		 * 发送至微信支付服务器
		 */
		String result = CommonUtils.sentHttpRequest(Global.getConfig("URL_UNIFIED_ORDER"), xml);

		/**
		 * 将返回结果解析成map
		 */
		Map<String, Object> resultMap = CommonUtils.parseXml(result);
		//resultMap.forEach((k, v) -> System.out.println("key:" + k + "\tvalue" + v));
		/**
		 * 获取map中的数据，并根据返回结果进行处理
		 */
		if (resultMap.get("return_code").equals("SUCCESS")) { // return_codef返回SUCCESS为通信成功
			if (resultMap.get("result_code").equals("SUCCESS")) { // result_code返回SUCCESS为获取prepay_id成功

				/**
				 * 将获取的数据封装到对象中，签名后连同签名返回给接口调用的终端
				 */
				String prepay_id = (String) resultMap.get("prepay_id");
				log.info("prepay_id=" + prepay_id);

				/**
				 * 获取其他参数， appId,nonceStr,package,signType,timeStamp 获取值并进行再次签名
				 */
				String nonceStr = (String) resultMap.get("nonce_str");
				log.info("微信返回的随机字符串：" + nonceStr);
				String timeStamp = DateUtil.localdatetimeTimestamp();

				/**
				 * 对准备返回的参数进行封装，方便调用签名算法签名，WxPayResponesModel是用来封装参数的对象
				 */
				/*WxPayResponesModel wxPayResponesModel = new WxPayResponesModel();
				wxPayResponesModel.setAppId(Global.getConfig("APP_ID"));
				wxPayResponesModel.setNonceStr(nonceStr);
				wxPayResponesModel.setPrepay_id("prepay_id="+prepay_id);
				wxPayResponesModel.setSignType("MD5");
				wxPayResponesModel.setTimeStamp(timeStamp);*/
				
				Map<String, Object> wxPayResponesModel = new HashMap<>();
				wxPayResponesModel.put("appId",Global.getConfig("APP_ID"));
				wxPayResponesModel.put("nonceStr",nonceStr);
				wxPayResponesModel.put("package","prepay_id="+prepay_id);
				wxPayResponesModel.put("signType","MD5");
				wxPayResponesModel.put("timeStamp",timeStamp);

				/**
				 * 对参数再次签名
				 */
				//Map<String, Object> SignMap = CommonUtils.object2Map(wxPayResponesModel);
				String signAgain = CommonUtils.createSign(Global.getConfig("APP_KEY"), wxPayResponesModel);
				log.info("微信返回的签名:" + resultMap.get("sign"));
				log.info("再次签名：" + signAgain);
				mapResult.put("appId", Global.getConfig("APP_ID"));
				mapResult.put("timeStamp", timeStamp);
				mapResult.put("nonceStr", nonceStr);
				mapResult.put("package", "prepay_id="+prepay_id);
				mapResult.put("signType", "MD5");
				mapResult.put("sign", signAgain);
				mapResult.put("wxResponse", "SUCCESS");//微信响应ok
			}
		}else{
			System.out.println(resultMap.get("return_msg"));//打印错误信息
			mapResult.put("wxResponse", "FAIL");//微信响应失败
		}
	}

	public static SecPayRefund refundOrder(SecPayRefund secPayRefund,
			HttpServletRequest request, Map<String, Object> mapResult) throws Exception {
		/**
		 * 根据商户订单构建微信退款订单
		 */
		PayInfo payInfo = new PayInfo(secPayRefund, request);

		/**
		 * 构建完订单对象，对订单信息进行签名
		 */
		Map<String, Object> map = CommonUtils.object2Map(payInfo);
		String sign = CommonUtils.createSign(Global.getConfig("APP_KEY"), map);

		/**
		 * 将签名sign写入微信订单信息
		 */
		payInfo.setSign(sign);

		/**
		 * 将订单信息转化为xml格式
		 */
		String xml = CommonUtils.objectToXML(payInfo);

		/**
		 * 发送至微信支付服务器
		 */
		String result = CommonUtils.doRefund(Global.getConfig("URL_REFUND"), xml);

		/**
		 * 将返回结果解析成map
		 */
		Map<String, Object> resultMap = CommonUtils.parseXml(result);
		//resultMap.forEach((k, v) -> System.out.println("key:" + k + "\tvalue" + v));
		/**
		 * 获取map中的数据，并根据返回结果进行处理
		 */
		if (resultMap.get("return_code").equals("SUCCESS")) { // return_codef返回SUCCESS为通信成功
			if (resultMap.get("result_code").equals("SUCCESS")) { // result_code返回SUCCESS为获取prepay_id成功
				mapResult.put("wxResponse", "SUCCESS");//微信响应ok
				/**
				 * 将获取的数据封装到对象中
				 */
				SecPayRefund sePayRefundFromWechat = (SecPayRefund)CommonUtils.map2Object(resultMap, SecPayRefund.class);
				return sePayRefundFromWechat;
			}else{
				mapResult.put("wxResponse", "UNSUCCESS");//微信响应ok
				System.out.println(resultMap.get("err_code").toString() + resultMap.get("err_code_des").toString());
				SecPayRefund sePayRefundFromWechat = (SecPayRefund)CommonUtils.map2Object(resultMap, SecPayRefund.class);
				return sePayRefundFromWechat;
			}
		}else{
			System.out.println(resultMap.get("return_msg"));//打印错误信息
			mapResult.put("wxResponse", "FAIL");//微信响应失败
			mapResult.put("failReason", resultMap.get("return_msg").toString());//微信响应失败
			return null;
		}
		
	}
}
