package com.thinkgem.jeesite.modules.activity.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.activity.entity.PayInfo;
import com.thoughtworks.xstream.XStream;

/**
 * 发送给微信服务器的数据需要特定格式和要求，此工具类是调用过程需要用到的方法
 * @author llhuang
 */
public class CommonUtils {

	/**
	 * 1.微信支付调起的接口都是需要向微信支付服务器发送xml格式的数据作为通知
	 * 2.服务器验证信息无误时根据业务情况返回数据。因此需要一个将信息转换成xml格式的方法
	 * 3.PayInfo对象封装了要发送到微信服务器上的订单数据信息(具体哪些信息看微信小程序支付开发文档)
	 * 4.xstream提供了将对象转化为xml格式的数据，使用之前需要实例化xstream，这里是对PayInfo对象转xml数据的方法
	 * 5.如果遇到xstream实例化失败的问题，注意xstream的jar包版本与dom4j包的版本匹配问题.
	 */
	
	private static XStream xstream = new XStream();
	public static String payInfoToXML(PayInfo payInfo) {
		xstream.alias("xml", payInfo.getClass());
		return xstream.toXML(payInfo);
	}
	
	/**
	 * 将对象转化为xml数据格式
	 * @param t
	 * @return
	 */
	public static <T> String objectToXML(T t) {
			xstream.alias("xml", t.getClass());
			String xml=xstream.toXML(t); 
			return xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
		}

	/**
	 * 对对象数据进行签名
	 *  微信支付過程中有多处地方需要对传送或者返回的数据进行签名，签名规则
	 * https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=4_3
	 * @param key
	 * @param parameters
	 * @return
	 * @throws Exception 
	 */
	public static String createSign(String key, Map<String, Object> parameters) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (!(parameters instanceof SortedMap<?, ?>)) {
			parameters = new TreeMap<String, Object>(parameters);
		}
		Set<?> es = parameters.entrySet();
		Iterator<?> it = es.iterator();	
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = getMD5(sb.toString().trim()).toUpperCase();
		return sign;
	}
	/**
	 * 对字符串进行MD5加密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String getMD5(String str) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("utf-8"));
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}
	/**
	 * 将对象转化为 map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> object2Map(Object obj) {
		Map<String, Object> map = new HashMap<>();
		if (obj==null) {
			return map;
		}
		Class<?> clazz=obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
		for (Field field : fields) {
			if(!field.getName().equals("serialVersionUID")){
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 將map转化为实体对象
	 * @param args
	 */
	public static Object map2Object(Map<String, Object> map, Class<?> clazz) {

		if (map == null) {
			return null;
		}
		Object obj = null;
		try {
			obj = clazz.newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				field.setAccessible(true);
				if(map.get(field.getName()) !=null){
					field.set(obj, map.get(field.getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 將xml格式的数据解析为map,用于将微信服务器返回的数据进行解析
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> parseXml(String xml) throws Exception {

		Map<String, Object> map = new HashMap<String,Object>();
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		for (Element e : elementList)
			map.put(e.getName(), e.getText());
		return map;
	}
	/**
	 * 获取客户端的ip，订单字段中有个数据需要获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	/**
	 * 以post请求的方式向指定url发送xml格式的消息,如要发送get请求将代码中HttpPost对象改为HttpGet
	 * @param url
	 * @param xml
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sentHttpRequest(String url,String xml) throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpClient=HttpClients.createDefault();
		HttpPost httpPost=new HttpPost(url);
		HttpEntity httpEntity = new StringEntity(xml);
		httpPost.setEntity(httpEntity);;
		try (CloseableHttpResponse httpResponse=httpClient.execute(httpPost)){
			if (httpResponse.getStatusLine().getStatusCode()==200) {
				System.out.println("访问成功！");
			}
			httpPost.setHeader("Content-Type","text/xml");
			httpPost.setHeader("charset","utf-8");
			httpResponse.setHeader("Content-Type","text/html");
			httpResponse.setHeader("charset","utf-8");
			HttpEntity entity=httpResponse.getEntity();
			InputStream iStream=entity.getContent();
			String result = new BufferedReader(new InputStreamReader(iStream,"utf-8"))
					  .lines().parallel().collect(Collectors.joining("\n"));
			
			return result;
		
		} catch (Exception e) {
			return "连接异常!";
		}finally{
			httpClient.close();
		}
	}
	
	public static String doRefund(String url,String xml) throws Exception {
		//指定读取证书格式为PKCS12(注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的)
		KeyStore keyStore = KeyStore.getInstance("PKCS12"); 
		// 指定证书路径
		String fileName = Global.getConfig("CERT_FILE"); //文件名
		
		//读取本机存放的PKCS12证书文件 
		//比如安装在D:/pkcs12/apiclient_cert.p12情况下，就可以写成如下语句
		//FileInputStream instream = new FileInputStream(new File("D:/pkcs12/apiclient_cert.p12")); 
		FileInputStream instream = new FileInputStream(new File(fileName));
		
		try { 
			//指定PKCS12的密码(商户ID) 
			keyStore.load(instream, Global.getConfig("MCH_ID").toCharArray()); 
		} finally { 
			instream.close(); 
		} 
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, Global.getConfig("MCH_ID").toCharArray()).build(); 
		//指定TLS版本 
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslcontext,new String[] { "TLSv1"},null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER); 
		//设置httpclient的SSLSocketFactory 
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		String result = null;
		try{
			HttpPost httpost = new HttpPost(url); // 设置响应头信息
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			HttpEntity httpEntity = new StringEntity(xml);
			httpost.setEntity(httpEntity);
			CloseableHttpResponse response = httpclient.execute(httpost);
			HttpEntity entity=response.getEntity();
			InputStream iStream=entity.getContent();
			result = new BufferedReader(new InputStreamReader(iStream,"utf-8"))
					  .lines().parallel().collect(Collectors.joining("\n"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			httpclient.close();
		}
		return result;
	}
	 
	
	
	public static void main(String[] args) {
		try{
			Security.addProvider(new BouncyCastleProvider());
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] str_decode = decoder.decode("T87GAHG17TGAHG1TGHAHAHA1Y1CIOA9UGJH1GAHV871HAGAGQYQQPOOJMXNBCXBVNMNMAJAA");
			String mchStr = CommonUtils.getMD5("10000100");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");  
	        SecretKeySpec key = new SecretKeySpec(mchStr.toLowerCase().getBytes(), "AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);  
	        byte[] doFinal = cipher.doFinal(str_decode);
	        String resultStr = new String(doFinal,"utf-8"); 
	        Map<String, Object> resultMap = CommonUtils.parseXml(resultStr);
	        System.out.println(resultMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
