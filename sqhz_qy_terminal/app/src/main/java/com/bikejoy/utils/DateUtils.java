package com.bikejoy.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lijipei on 2017/3/15.
 */

public class DateUtils {

    /**
     * 日期格式:yyyy-MM-dd
     */
    public final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 日期格式:yyyy-MM
     */
    public final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM");
        }
    };

    /**
     * 日期格式:yyyy.MM.dd
     */
    public final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy.MM.dd");
        }
    };

    /**
     * 获取当前时间yyyy-MM--dd
     */
    public static String getDate2() {
        long time = System.currentTimeMillis();
        Date d = new Date(time);
        return dateFormater2.get().format(d);
    }


    /**
     * 返回当前系统时间
     */
    public static String getDataTime(ThreadLocal<SimpleDateFormat> dateFormater) {
        long time = System.currentTimeMillis();
        Date d = new Date(time);
        return dateFormater.get().format(new Date());
    }

    /**
     * 将字符串日期进行格式化
     *
     * @param date
     * @return
     */
    public static String getStringFormat(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        try {
            calendar1.setTime(df.parse(date));
            return dateFormater2.get().format(calendar1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormater2.get().format(calendar1.getTime());
    }

    /**
     * 获取当月第一天日期
     *
     * @param dateFormater
     * @param value  0为当前月时间,-1位上月
     * @return
     */
    public static String currentMonthFirst(ThreadLocal<SimpleDateFormat> dateFormater, int value) {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, value);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = dateFormater.get().format(c.getTime());
        return first;
    }

    /**
     * 获取当月最后一天日期
     *
     * @param dateFormater
     * @param value  0为当前月时间
     * @return
     */
    public static String currentMonthEnd(ThreadLocal<SimpleDateFormat> dateFormater, int value) {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, value);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String first = dateFormater.get().format(c.getTime());
        return first;
    }

    /**
     * 获取明天时间
     *
     * @param date
     * @return
     */
    public static String getTomorrowDay(Date date, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 获取昨天时间
     *
     * @param date
     * @return
     */
    public static String getYesterdayDay(Date date, ThreadLocal<SimpleDateFormat> dateFormater, int dayIndex) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayIndex);//-1);
        date = calendar.getTime();
        String str = dateFormater.get().format(date);
        return str;
    }

    /**
     * 根据距离天数得到--新日期
     *
     * @param date
     * @param format
     * @param dayNum 距离今天有几天
     * @return
     */
    public static String getDistanceDay(Date date, String format, int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 判断日期是否大于今天
     *
     * @param myString
     * @return
     */
    public static boolean isMoreThanToday(String myString) {
        Date nowdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
        try {
            Date d = sdf.parse(myString);
            flag = d.after(nowdate);
            return flag;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 格式化日期字符串
     *
     * @param myString
     * @return
     */
    public static String getCurTimeStr(String myString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-M-dd").parse(myString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //        Date curDate = null;
        //        try {
        //            curDate = sdf.parse(myString);
        //        } catch (ParseException e) {
        //            e.printStackTrace();
        //        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /***
     * 计算两个时间差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calDateDifferent(String startDate, String endDate) {
        int flag = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = sdf.parse(startDate);
            d2 = sdf.parse(endDate);
            // 毫秒ms
            //            diff = d2.getTime() - d1.getTime();
            LogUtils.e("tag", d2.getTime() + "--" + d1.getTime());
            if (d2.getTime() > d1.getTime()) {
                return 1;
            } else if (d2.getTime() == d1.getTime()) {
                return 0;
            } else {
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int getDateSpace(String date1, String date2) {
        int result = 0;
        Calendar calst = Calendar.getInstance();;
        Calendar caled = Calendar.getInstance();

        calst.setTime(getDate(date1));
        caled.setTime(getDate(date2));

        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;

        return days+1;
    }

    /**
     * 计算指定年月距离当前月有多少个月份
     *
     * @param date
     * @return
     */
    public static int getMonthNum(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        try {
            calendar1.setTime(df.parse(date));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(df.parse(DateUtils.getDataTime(dateFormater2)));

            LogUtils.e("tag", calendar1.get(Calendar.YEAR) + "--" + calendar1.get(Calendar.MONTH));
            LogUtils.e("tag2", calendar2.get(Calendar.YEAR) + "--" + calendar2.get(Calendar.MONTH));
            LogUtils.e("tag3", (calendar2.get(Calendar.MONTH) + 1) - (calendar1.get(Calendar.MONTH) + 1) + "");
            int m = (calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR)) * 12 +
                    (calendar1.get(Calendar.MONTH) + 1) - (calendar2.get(Calendar.MONTH) + 1);
            Log.e("tag", "距离:" + m);
            return m;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将字符串日期格式转换成Calendar类型
     *
     * @param date
     * @return
     */
    public static Calendar getCalendar(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        try {
            calendar1.setTime(df.parse(date));
            return calendar1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar1;
    }

    /**
     * 将字符串日期转换成date类型
     *
     * @param date
     * @return
     */
    public static Date getDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        try {
            calendar1.setTime(df.parse(date));
            return calendar1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar1.getTime();
    }


    public static int getMonths(String startDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");

        Calendar startCalendar = new GregorianCalendar();
        try {
            startCalendar.setTime(df.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //当前时间
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(new Date());
        LogUtils.e("end", endCalendar.get(Calendar.YEAR) + "-" + endCalendar.get(Calendar.MONTH));
        LogUtils.e("start", startCalendar.get(Calendar.YEAR) + "-" + startCalendar.get(Calendar.MONTH));

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }


    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfWeek(Date date, ThreadLocal<SimpleDateFormat> format) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        System.out.println("要计算日期为:" + format.get().format(cal.getTime())); //输出要计算日期
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = format.get().format(cal.getTime());
        System.out.println("所在周星期一的日期：" + imptimeBegin);
//        cal.add(Calendar.DATE, 6);
//        String imptimeEnd = sdf.format(cal.getTime());
//        System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin;
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfWeek(Date date, ThreadLocal<SimpleDateFormat> format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        System.out.println("要计算日期为:" + format.get().format(cal.getTime())); //输出要计算日期
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = format.get().format(cal.getTime());
        System.out.println("所在周星期一的日期：" + imptimeBegin);

        cal.add(Calendar.DATE, 6);
        String imptimeEnd = format.get().format(cal.getTime());
        System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeEnd;
    }

    /**
     * 取得上周第一天
     * @param weekIndex -1:上周
     * @return
     */
    public static String getLastWeekFirstDay(String format, int weekIndex) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, weekIndex);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        DateFormat df = new SimpleDateFormat(format);
        System.out.println("要计算日期为:" + df.format(cal.getTime())); //输出要计算日期
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = df.format(cal.getTime());
        System.out.println("所在周星期一的日期：" + imptimeBegin);
        //        cal.add(Calendar.DATE, 6);
        //        String imptimeEnd = sdf.format(cal.getTime());
        //        System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin;
    }

    /**
     * 获取上周最后一天
     * @param weekIndex
     * @return
     */
    public static String getLastWeekLastDay(String format, int weekIndex) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, weekIndex);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        DateFormat df = new SimpleDateFormat(format);
        System.out.println("要计算日期为:" + df.format(cal.getTime())); //输出要计算日期
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = df.format(cal.getTime());
        System.out.println("所在周星期一的日期：" + imptimeBegin);

        cal.add(Calendar.DATE, 6);
        String imptimeEnd = df.format(cal.getTime());
        System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeEnd;
    }


    /**
     * 获取当年的第一天
     * @param yearIndex 0:今年;-1:去年
     * @return
     */
    public static String getCurrYearFirst(ThreadLocal<SimpleDateFormat> dateFormater, int yearIndex) {
        Calendar currCal = Calendar.getInstance();
        currCal.add(Calendar.YEAR,yearIndex);
        int currentYear = currCal.get(Calendar.YEAR);
        return dateFormater.get().format(getYearFirst(currentYear));
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static String getCurrYearLast(ThreadLocal<SimpleDateFormat> dateFormater, int yearIndex) {
        Calendar currCal = Calendar.getInstance();
        currCal.add(Calendar.YEAR,yearIndex);
        int currentYear = currCal.get(Calendar.YEAR);
        return dateFormater.get().format(getYearLast(currentYear));
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    /**
     * 获取两个日期相差几个月
     *
     * @param start
     * @param end
     * @return
     * @author 石冬冬-Heil Hilter(dd.shi02@zuche.com)
     * @date 2016-11-30 下午7:57:32
     */
    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }


    // 解析时间戳
    private static SimpleDateFormat sdf = null;

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {
        String str = "";
        long time = System.currentTimeMillis();
        Date d1 = new Date(time);
        Date d2 = new Date(Long.parseLong(timeStr));
        if (d1.getYear() - d2.getYear() > 0) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            str = sdf.format(d2);
        } else if (d1.getMonth() - d2.getMonth() == 0) {
            sdf = new SimpleDateFormat("HH:mm");
            if (d1.getDate() - d2.getDate() == 0) {
                //                str = "今天" + sdf.format(d2);

                if (d1.getHours() - d2.getHours() == 0) {
                    if (d1.getMinutes() - d2.getMinutes() >= 5) {
                        str = d1.getMinutes() - d2.getMinutes() + "分钟前";
                    } else {
                        str = "刚刚";
                    }
                } else if (d1.getHours() - d2.getHours() == 1 && d1.getMinutes() - d2.getMinutes() < 0) {
                    if (d1.getMinutes() + 60 - d2.getMinutes() >= 5) {
                        str = d1.getMinutes() + 60 - d2.getMinutes() + "分钟前";
                    } else {
                        str = "刚刚";
                    }
                } else {
                    str = d1.getHours() - d2.getHours() + "小时前";
                }

            } else if (d1.getDate() - d2.getDate() == 1) {
                str = "昨天" + sdf.format(d2);
            } else if (d1.getDate() - d2.getDate() == 2) {
                str = "前天" + sdf.format(d2);
            } else {
                sdf = new SimpleDateFormat("MM-dd HH:mm");
                str = sdf.format(d2);
            }
        } else if (d1.getMonth() - d2.getMonth() == 1) {
            if (d2.getMonth() == 1) {
                sdf = new SimpleDateFormat("HH:mm");
                if (d2.getYear() % 4 == 0) {
                    if (d1.getDate() - d2.getDate() == -28) {
                        str = "昨天" + sdf.format(d2);
                    } else if (d1.getDate() - d2.getDate() == -27) {
                        str = "前天" + sdf.format(d2);
                    } else {
                        sdf = new SimpleDateFormat("MM-dd HH:mm");
                        str = sdf.format(d2);
                    }
                } else {
                    if (d1.getDate() - d2.getDate() == -27) {
                        str = "昨天" + sdf.format(d2);
                    } else if (d1.getDate() - d2.getDate() == -26) {
                        str = "前天" + sdf.format(d2);
                    } else {
                        sdf = new SimpleDateFormat("MM-dd HH:mm");
                        str = sdf.format(d2);
                    }
                }
            } else if (d2.getMonth() == 0 || d2.getMonth() == 2
                    || d2.getMonth() == 4 || d2.getMonth() == 6
                    || d2.getMonth() == 7 || d2.getMonth() == 9
                    || d2.getMonth() == 11) {
                sdf = new SimpleDateFormat("HH:mm");
                if (d1.getDate() - d2.getDate() == -30) {
                    str = "昨天" + sdf.format(d2);
                } else if (d1.getDate() - d2.getDate() == -29) {
                    str = "前天" + sdf.format(d2);
                } else {
                    sdf = new SimpleDateFormat("MM-dd HH:mm");
                    str = sdf.format(d2);
                }
            } else {
                sdf = new SimpleDateFormat("HH:mm");
                if (d1.getDate() - d2.getDate() == -29) {
                    str = "昨天" + sdf.format(d2);
                } else if (d1.getDate() - d2.getDate() == -28) {
                    str = "前天" + sdf.format(d2);
                } else {
                    sdf = new SimpleDateFormat("MM-dd HH:mm");
                    str = sdf.format(d2);
                }
            }
        } else {
            sdf = new SimpleDateFormat("MM-dd HH:mm");
            str = sdf.format(d2);
        }
        return str;
    }

    /**
     * 时间转时间戳
     *
     * @param user_time
     * @return
     */
    public static String getTime(String user_time) {
        String re_time = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            re_time = String.valueOf(l / 1000);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }


}
