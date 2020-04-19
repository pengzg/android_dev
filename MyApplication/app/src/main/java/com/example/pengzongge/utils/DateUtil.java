package com.example.pengzongge.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * 日期工具类
 *
 * @author
 *
 */
public class DateUtil {

    public static void main(String[] args) throws Exception {

        // System.out.println(compareDate2("2015-12-15", "2015-12-15"));
        String ts2 = "2019-04-17 16:32:57";

//		Date date = DateUtil.parseString2Date3(ts2);
//		String ts1 = DateUtil.getDateToMintesString("10", 2);
//		System.out.println(ts1);
//		System.out.println(ts2);
        // System.out.println(ts2.substring(11, 16));
        // String ts = getCurrentDateToString();
        // String ts1 = DateUtil.getDayBeforeHour(24);
        // String ts1 = DateUtil.getDateAfter(ts,10 );
        // System.out.println(ts);
        // System.out.println(ts1);
        // System.out.println(DateUtil.getDate000000(ts));
        // System.out.println(getDayBeforeHour(24));
        // System.out.println(getDayBeforeHour(48));
        // System.out.println(getDayBeforeHour(72));
        // System.out.println(getInterval_ff("2018-05-02 11:12:12",
        // DateUtil.getCurrentDateToString2()));

        // System.out.println(DateUtil.getDateAfterToString(new Date(),-1,1));

        // System.out.println(DateUtil.getDateAfterToString2(new Date(),-1,1));
        // System.out.println(DateUtil.getMondayPlus());
        // int day = DateUtil.getMondayPlus();
        // System.out.println(DateUtil.getDateAfterToString2(new Date(),day,1));

        // System.out.println(DateUtil.getCurrentWorkFirstDay());
        // System.out.println(DateUtil.getCurrentMonthFirstDay());
//		String dateStr = "dafdaf";
//		System.out.println(DateUtil.getWeekOfDate(dateStr));
//		System.out.println(DateUtil.getDateAfterToString2(DateUtil.getCurrentDateToString2(),24,4 ));
//		System.out.println(DateUtil.parseString2Date3(ts2));
//		System.out.println(DateUtil.compareDate( DateUtil.getCurrentDateToString2(),"2019-09-23 10:34:01"));
//		System.out.println(DateUtil.compareDate( "2019-09-23 10:34:01","2019-09-23 10:34:01"));
//		System.out.println(DateUtil.compareDate("2019-09-23 10:34:01", DateUtil.getCurrentDateToString2()));
//		System.out.println(DateUtil.compareDate2( DateUtil.getCurrentDateToString2(),"2019-09-23 10:34:01"));
//		System.out.println(DateUtil.compareDate2( "2019-09-23 10:34:01","2019-09-23 10:34:01"));
//		System.out.println(DateUtil.compareDate2("2019-09-23 10:34:01", DateUtil.getCurrentDateToString2()));
//		System.out.println(DateUtil.getIntervalDays("2020-02-18", DateUtil.getCurrentDateToString2()));
        System.out.println(DateUtil.getDate000000(DateUtil.getDateAfterToString2(DateUtil.getCurrentDateToString2(), (-1)*1, 1).substring(0, 10)));
    }

    /**
     * 日期转字符串
     *
     * @param date
     *            日期
     * @param pattern
     *            格式
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, "yyyy-MM-dd hh:mm:ss");
    }

    public static final short TYPE_DAY = 1;
    public static final short TYPE_WEEK = 2;
    public static final short TYPE_MONTH = 3;
    public static final short TYPE_YEAR = 4;

    public static Date parseString2Date(String date) throws Exception {
        SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd");
        return fmtrq.parse(date);
    }

    public static Date parseString2Date3(String date) throws Exception {
        SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return fmtrq.parse(date);
    }

    public static Date parseString2Date2(String date) {
        SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return fmtrq.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date getCurrentDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(fmt.format(new Date()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date getCurrentDate2() throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.parse(fmt.format(new Date()));
    }

    public static String getCurrentDateToString() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(new Date());
    }

    public static String getCurrentMonthToString() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");
        return fmt.format(new Date());
    }

    public static String getCurrentDateToString1() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(new Date());
    }

    public static String getCurrentDateToString2() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(new Date());
    }

    public static String getCurrentDateToString3() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        return fmt.format(new Date());
    }

    public static String getCurrentDateToString5() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return fmt.format(new Date());
    }

    public static String getCurrentDateToString6() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
        return fmt.format(new Date());
    }

    /**
     * 返回年/月/日格式的字符串
     *
     * @return
     */
    public static String getCurrentDateToString4() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        return fmt.format(new Date());
    }

    public static String getDateToString2(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(date);
    }

    public static String getDateToString3(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return fmt.format(date);
    }

    public static String getDateToString1(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);
    }

    public static String getDateToString4(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        return fmt.format(date);
    }

    public static String getDateAfter(String strdate, int day) {
        try {
            Date date = getDateAfter(parseString2Date(strdate), day, 1);
            return getDateToString1(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("日期转换出错！");
        }
    }

    public static String[] getYearAndMonth(String date) {
        String[] item = new String[2];
        item[0] = date.substring(0, 4);
        item[1] = date.substring(5, 7);
        return item;

    }

    public static Date getDateAfter(Date d, int datenum, int type) throws Exception {

        Calendar now = Calendar.getInstance();
        now.setTime(d);
        switch (type) {
            case 1:
                now.add(Calendar.DATE, datenum);
                break;
            case 2:
                now.add(Calendar.DATE, datenum * 7);
                break;
            case 3:
                now.add(Calendar.MONTH, datenum);
                break;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return parseString2Date(fmt.format(now.getTime()).toString());

    }

    public static String getDateAfterToString(Date d, int datenum, int type) throws Exception {

        Calendar now = Calendar.getInstance();
        now.setTime(d);
        switch (type) {
            case 1:
                now.add(Calendar.DATE, datenum);
                break;
            case 2:
                now.add(Calendar.DATE, datenum * 7);
                break;
            case 3:
                now.add(Calendar.MONTH, datenum);
                break;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(now.getTime()).toString();

    }

    public static String getDateAfterToString2(String time, int datenum, int type) {
        String ts1 = "";
        Date date;
        try {
            date = DateUtil.parseString2Date2(time);

            ts1 = DateUtil.getDateAfterToString2(date, datenum, type);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ts1;
    }

    public static String getDateAfterToString2(Date d, int datenum, int type) throws RuntimeException {

        Calendar now = Calendar.getInstance();
        now.setTime(d);
        switch (type) {
            case 1:
                now.add(Calendar.DATE, datenum);
                break;
            case 2:
                now.add(Calendar.DATE, datenum * 7);
                break;
            case 3:
                now.add(Calendar.MONTH, datenum);
                break;
            case 4:
                now.add(Calendar.HOUR, datenum);
                break;
            case 5:
                now.add(Calendar.MINUTE, datenum);
                break;
            case 6:
                now.add(Calendar.SECOND, datenum);
                break;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        return fmt.format(now.getTime()).toString();

    }

    public static String getDateAfterToString3(Date d, int datenum, int type) throws Exception {

        Calendar now = Calendar.getInstance();
        now.setTime(d);
        switch (type) {
            case 1:
                now.add(Calendar.DATE, datenum);
                break;
            case 2:
                now.add(Calendar.DATE, datenum * 7);
                break;
            case 3:
                now.add(Calendar.MONTH, datenum);
                break;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(now.getTime()).toString() + " 23:59:59 ";

    }

    public static Date getLastDayOfWeek(Date date, int num) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + num);
        return c.getTime();
    }

    public static String getDateAfter2String(Date d, int month) throws Exception {

        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + month);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(now.getTime()).toString();

    }

    public static int getIntervalDays(String startday, String endday) {
        Date startdate;
        Date enddate;
        try {
            startdate = parseString2Date(startday);
            enddate = parseString2Date(endday);
            return getIntervalDays(startdate, enddate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("日期相比较出错！");
        }
    }

    public static String getIntervalStr(String startday, String endday) {
        try {
            int i = getInterval_ff(startday, endday);
            int day = i / (60 * 24);
            if (day > 0 && day < 28) {
                return day + "天前";
            }
            if (day > 0 && day > 28) {
                return startday.substring(0, 10);
            }
            int hour = i / 60;
            if (hour > 0) {
                return hour + "小时前";
            }
            if (i < 5) {
                return "刚刚";
            }
            return i + "分钟前";

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("日期相比较出错！");
        }
    }

    public static int getInterval_ff(String startday, String endday) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(startday));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(endday));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 60);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("日期相比较出错！");
        }
    }

    public static int getIntervalDays(Date startday, Date endday) {
        // 确保startday在endday之前
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        // 分别得到两个时间的毫秒数
        long sl = startday.getTime();
        long el = endday.getTime();

        long ei = el - sl;
        // 根据毫秒数计算间隔天数
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    public static String getMaxDate(String datetime) {
        Date mindate = null;
        Calendar maxdate = null;
        if (datetime != null && datetime.length() > 0) {
            if (datetime.length() >= 8) {
                DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    mindate = fm.parse(datetime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                maxdate = Calendar.getInstance();
                maxdate.setTime(mindate);
                maxdate.add(Calendar.DATE, 1);
            } else if (datetime.length() >= 6) {
                DateFormat fm = new SimpleDateFormat("yyyy-MM");
                try {
                    mindate = fm.parse(datetime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                maxdate = Calendar.getInstance();
                maxdate.setTime(mindate);
                maxdate.add(Calendar.MONTH, 1);
            } else {
                DateFormat fm = new SimpleDateFormat("yyyy");
                try {
                    mindate = fm.parse(datetime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                maxdate = Calendar.getInstance();
                maxdate.setTime(mindate);
                maxdate.add(Calendar.YEAR, 1);
            }
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(fmt.format(maxdate.getTime()).toString());
        return fmt.format(maxdate.getTime()).toString();
    }

    public static Double numFormat(Double num) {
        String data = new java.text.DecimalFormat("0.00").format(num);
        return Double.valueOf(data);
    }

    public static String moneyFormat(BigDecimal money) {
        if (money != null)
            return new java.text.DecimalFormat("#,###,###,###,##0.00").format(money.doubleValue());
        return "0.00";
    }

    public static String moneyFormat1(BigDecimal money) {
        if (money != null)
            return new java.text.DecimalFormat("#,###,###,###,##0.0000").format(money.doubleValue());
        return "0.00";
    }


    public static boolean compareDate(String date, String ccdate) {
        boolean bool = false;
        Date cdate = null;
        Date ydate = null;
        try {
            cdate = DateUtil.parseString2Date(ccdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ydate = DateUtil.parseString2Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cdate.before(ydate)) {// 若当前时间小，则返回true
            return true;
        }
        return bool;
    }

    // public static void main(String args[]) throws Exception {
    // String d = "2015-11-3 19:30:00";
    // Date cdate = DateUtils.parseString2Date(DateUtils
    // .getCurrentDateToString2());
    // Date ydate = DateUtils.parseString2Date(d);
    // if (cdate.before(ydate)) {
    // System.out.println("当前时间小");
    // }
    // if (cdate.after(ydate)) {
    // System.out.println("当前时间大");
    // }
    // System.out.println("当前时间:"+DateUtils.getCurrentDateToString2());
    // System.out.println(DateUtils.getDateToString(DateUtils
    // .getLastDayOfWeek(DateUtils.getCurrentDate(), 5)));
    // System.out.println(DateUtils.getDateToString(DateUtils
    // .getLastDayOfWeek(DateUtils.getCurrentDate(), 6)));
    // System.out.println(DateUtils.getDateToString(DateUtils
    // .getDateAfter(DateUtils.getLastDayOfWeek(DateUtils
    // .getCurrentDate(), 6), 6, 1)));
    // System.out.println(DateUtils.getDateToString(DateUtils
    // .getDateAfter(DateUtils.getLastDayOfWeek(DateUtils
    // .getCurrentDate(), 6), 7, 1)));
    // }

    public static BigDecimal getTwoDecimal(BigDecimal formateNum) {
        return formateNum.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @author qinchangying
     * @date 2011-11-10
     * @param currentDate
     *            YYYY-MM-dd或YYYY-MM格式的日期字符串。
     * @desc 得到当前月最后一天的方法。
     * @return YYYY-MM-dd格式的字符串。
     */
    public static String getCurrentMonthLastDayToString(String currentDate) {
        currentDate = currentDate.substring(0, 7) + "-01";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(fmt.parse(currentDate));
        } catch (ParseException e) {

        }
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return fmt.format(calendar.getTime());
    }

    /**
     * @author zhanglianjie 获得下周星期一的日期
     * @return
     */
    public static String getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        // DateFormat df = DateFormat.getDateInstance();
        // String preMonday = df.format(monday);
        // 时间格式 yyyy-M-d--->yyyy-MM-dd变更
        SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = test.format(monday);
        return preMonday;
    }

    /**
     * @author zhanglianjie 获得当前日期与本周日相差的天数
     * @return
     */
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else if (dayOfWeek == 0) {
            return 1 - 7;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    // 获取本月第一天
    public static String getCurrentMonthFirstDay() {
        String currentDate = DateUtil.getCurrentDateToString();
        currentDate = currentDate.substring(0, 7) + "-01";
        return currentDate;
    }

    // 获取本周第一天
    public static String getCurrentWorkFirstDay() {
        int day = DateUtil.getMondayPlus();
        Date date = DateUtil.parseString2Date2(DateUtil.getCurrentDateToString() + " 00:00:01");
        String currentDate = DateUtil.getDateAfterToString2(date, day, 1).substring(0, 10);
        return currentDate;
    }

    // 取返回日期的年
    public static String getYear(String date) {
        String[] item = getYMD(date);
        return item[0];
    }

    // 返回日期里的月
    public static String getMonth(String date) {
        String[] item = getYMD(date);
        return item[1];
    }

    // 返回日期里的天
    public static String getDay(String date) {
        String[] item = getYMD(date);
        return item[2];
    }

    // 返回日期里的年月日
    public static String[] getYMD(String date) {
        String[] item = new String[3];
        item[0] = date.substring(0, 4);
        item[1] = date.substring(5, 7);
        item[2] = date.substring(8, 10);

        return item;
    }

    // 返回一天最后时点
    public static String getDate235959(String date) {
        String dateStr = "";
//        if (StringUtils.isNotBlank(date)) {
            dateStr = date.trim() + " 23:59:59";
//        }

        return dateStr;
    }

    // 返回一天最早时点
    public static String getDate000000(String date) {
        String dateStr = "";
//        if (StringUtils.isNotBlank(date)) {
            dateStr = date.trim() + " 00:00:00";
//        }
        return dateStr;
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回格式:xx天xx小时xx秒
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static String getUsedDateTime(String minDate, String maxDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long to = 0L;
        long from = 0L;
        long millisecondOfSecond = 1000;
        long millisecondOfMinute = 1000 * 60;
        long millisecondOfHour = 1000 * 60 * 60;
        long millisecondOfDay = 1000 * 60 * 60 * 24;
        try {
            to = df.parse(maxDate).getTime();
            from = df.parse(minDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long differenceMilli = to - from;
        StringBuffer sb = new StringBuffer("");
        if (differenceMilli / millisecondOfDay > 0) {
            sb.append((differenceMilli / millisecondOfDay) + "天 ");
        }
        differenceMilli -= (differenceMilli / millisecondOfDay) * millisecondOfDay;
        if (differenceMilli / millisecondOfHour > 0) {
            sb.append((differenceMilli / millisecondOfHour) + "小时 ");
        }

        differenceMilli -= (differenceMilli / millisecondOfHour) * millisecondOfHour;
        if (differenceMilli / millisecondOfMinute > 0) {
            sb.append((differenceMilli / millisecondOfMinute) + "分钟 ");
        }

        differenceMilli -= (differenceMilli / millisecondOfMinute) * millisecondOfMinute;
        if (differenceMilli / millisecondOfSecond > 0) {
            sb.append((differenceMilli / millisecondOfSecond) + "秒 ");
        }
        return sb.toString();
    }

    /**
     * 返回时间格式 xx天xx小时
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static String getUsedFormatTime(String minDate, String maxDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long to = 0L;
        long from = 0L;
        long millisecondOfMinute = 1000 * 60;
        long millisecondOfHour = 1000 * 60 * 60;
        long millisecondOfDay = 1000 * 60 * 60 * 24;
        try {
            to = df.parse(maxDate).getTime();
            from = df.parse(minDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long differenceMilli = to - from;
        StringBuffer sb = new StringBuffer("");
        if (differenceMilli / millisecondOfDay > 0) {
            sb.append((differenceMilli / millisecondOfDay) + "天 ");
        }
        differenceMilli -= (differenceMilli / millisecondOfDay) * millisecondOfDay;
        if (differenceMilli / millisecondOfHour > 0) {
            sb.append((differenceMilli / millisecondOfHour) + "小时 ");
        }

        differenceMilli -= (differenceMilli / millisecondOfHour) * millisecondOfHour;
        if (differenceMilli / millisecondOfMinute > 0) {
            sb.append((differenceMilli / millisecondOfMinute) + "分钟 ");
        }

        return sb.toString();
    }

    /**
     * 比较时间大小 (yyyy-MM-dd HH:mm:ss 格式)
     *
     * @param startDate
     * @param endDate
     * @return 0-相等 1-大于 2-小于
     */
    public static int compareDate2(String startDate, String endDate) {
        try {
            Long startLong = parseString2Date2(startDate).getTime();
            Long endLong = parseString2Date2(endDate).getTime();
            Long val = startLong - endLong;
            if (val == 0) {
                return 0;
            } else if (val > 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }

    /**
     * 返回当前时间多少小时前的时间 (yyyy-MM-dd HH:mm:ss格式)
     *
     * @param hours
     * @return
     */
    public static String getDayBeforeHour(int hours) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR_OF_DAY, -hours);
        return sdf.format(nowTime.getTime());
    }

    public static String getCurrentDateToString(String formatStr) {
        SimpleDateFormat fmt = new SimpleDateFormat(formatStr);
        return fmt.format(new Date());
    }

    /**
     * 当前时间 通过加减分钟转换为时间字符串
     *
     * @param formatStr
     *            type 1 为加 2 为减
     * @return
     */
    public static String getDateToMintesString(String minutes, Integer type) {
        long time = 60 * 1000 * (Integer.parseInt(minutes));
        Date beforeDate = new Date((System.currentTimeMillis()) - time);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(beforeDate);
    }

    /**
     * 得到日期是第几周 以每个星期一为第一周
     * @param dateStr ("YYYY-MM-DD")
     * @return
     */
    public static String getWeekOfDate(String dateStr) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(date);
            return calendar.get(Calendar.WEEK_OF_YEAR)+"";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("日期转换出错！");
        }
    }

}
