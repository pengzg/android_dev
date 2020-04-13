package com.xdjd.utils;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �?验密码�?�手机号、验证码�?
 * @author NIT
 *
 */
public class Validation {
	public static boolean isPhoneNum(String str) {
		Pattern p = Pattern
				.compile("^1[0-9]{10}$");
//((13[0-9])|(16[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}
		Matcher m = p.matcher(str);

		return m.matches();
	}

	public static boolean isPassword(String str) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isUsername(String str) {
		String patternStr = "^[\\w\\u4e00-\\u9fa5]{3,20}$";// ����?
		Pattern p = Pattern.compile(patternStr);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isCaptcha(String captcha) {
		Pattern p = Pattern.compile("^[0-9A-Za-z]{4,6}$");
		Matcher m = p.matcher(captcha);
		return m.matches();
	}

	/**
	 * 正则验证是否是数字
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	/**
	 * 验证是否是数字且必须大于0
	 * true 是数字且大于0
	 */
	public static boolean isNumericAndExceedZero(String str){
		try {
			Double dou = Double.valueOf(str);
			if (dou>0){
				return true;
			}else{
				return false;
			}
		}catch (NumberFormatException e){
			return false;
		}
	}

}
