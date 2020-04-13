package com.xdjd.utils;


import android.util.Log;

import com.xdjd.storebox.base.BaseConfig;


/**
 * Log输出工具类
 */
public class LogUtils {

	/** 是否允许输出log */
	private static boolean DEBUG_MODE = BaseConfig.DEBUGLEVEL;

	public static void e(String tag, String message) {
		if (DEBUG_MODE)
			Log.e(tag, message);
	}

	public static void tagE(String message) {
		if (DEBUG_MODE)
			Log.e("TAG", message);
	}

	public static void e(String tag, String message, Exception e) {
		if (DEBUG_MODE)
			Log.e(tag, message, e);
	}

	public static void w(String tag, String message) {
		if (DEBUG_MODE)
			Log.w(tag, message);
	}

	public static void i(String tag, String message) {
		if (DEBUG_MODE)
			Log.i(tag, message);
	}

	public static void d(String tag, String message) {
		if (DEBUG_MODE)
			Log.d(tag, message);
	}

	public static void v(String tag, String message) {
		if (DEBUG_MODE)
			Log.v(tag, message);
	}


}
