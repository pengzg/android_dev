package com.xdjd.view.calendarview;

import java.text.SimpleDateFormat;

/**
 * @author: wangjihsan
 * @类   说   明:	
 * @version 1.0
 * @创建时间：2015-3-2 下午4:21:36
 * 
 */
public class StringUtil {

	
	public static int compare_date(String date1, String date2) {
		SimpleDateFormat df1 = null;
		SimpleDateFormat df2 = null;
		if (date1.contains("-")) {
			df1 = new SimpleDateFormat("yyyy-MM-dd");
		} else if (date1.contains("/")) {
			df1 = new SimpleDateFormat("yyyy/MM/dd");
		}
		if (date2.contains("-")) {
			df2 = new SimpleDateFormat("yyyy-MM-dd");
		} else if (date1.contains("/")) {
			df2 = new SimpleDateFormat("yyyy/MM/dd");
		}
		try {
			java.util.Date d1 = df1.parse(date1);
			java.util.Date d2 = df2.parse(date2);

			if (d1.before(d2)) {
				return 1;
			} else {
				return -1;
			}
			// if (d1.getTime() > d2.getTime()) {
			// return 1;
			// } else if (d1.getTime() < d2.getTime()) {
			//
			// return -1;
			// } else {
			// return 0;
			// }
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;

	}
	
}
