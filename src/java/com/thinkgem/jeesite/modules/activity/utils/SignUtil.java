package com.thinkgem.jeesite.modules.activity.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Random;

import sun.misc.BASE64Encoder;

public class SignUtil {

	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String numberChar = "0123456789";

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */

	public static String generateString(int length) {

		StringBuffer sb = new StringBuffer();

		Random random = new Random();

		for (int i = 0; i < length; i++) {

			sb.append(allChar.charAt(random.nextInt(allChar.length())));

		}

		return sb.toString();

	}

	/**
	 * 返回一个定长的随机纯字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */

	public static String generateMixString(int length) {

		StringBuffer sb = new StringBuffer();

		Random random = new Random();

		for (int i = 0; i < length; i++) {

			sb.append(allChar.charAt(random.nextInt(letterChar.length())));

		}

		return sb.toString();

	}

	/**
	 * 返回一个定长的随机纯小写字母字符串(只包含小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */

	public static String generateLowerString(int length) {

		return generateMixString(length).toLowerCase();

	}

	/**
	 * 返回一个定长的随机纯大写字母字符串(只包含大写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */

	public static String generateUpperString(int length) {

		return generateMixString(length).toUpperCase();

	}

	public static String Sign(String str) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = messageDigest.digest(str.getBytes(Charset
					.forName("UTF-8")));

			BASE64Encoder en = new BASE64Encoder();
			String MD5String = en.encode(md5Bytes);
			System.out.println(MD5String.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		Sign("1245");

		/*
		 * System.out.println(generateString(32));
		 * 
		 * System.out.println(generateMixString(32));
		 * 
		 * System.out.println(generateLowerString(32));
		 * 
		 * System.out.println(generateUpperString(32));
		 */
	}
}
