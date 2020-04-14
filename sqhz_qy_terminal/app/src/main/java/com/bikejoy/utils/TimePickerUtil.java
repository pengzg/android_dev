package com.bikejoy.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.popup.CalendarOnePopup;
import com.bikejoy.testdemo.popup.CalendarSelectPopup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/1
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class TimePickerUtil {

    //日期选择控件控件id
    public TextView tvCancel;
    public RelativeLayout rlStartDate, rlEndDate;
    public TextView tvStartTitle, tvEndTitle, tvStartDate, tvEndDate;
    public View startLine, endLine;

    //一天日期选择样式
    private CalendarOnePopup mCalendarOnePopup;

    //日期选择滚轮样式起止时间按钮状态选择
    private void selectPickerTab(int type) {
        switch (type) {
            case 1:
                tvStartTitle.setTextColor(UIUtils.getColor(R.color.text_gray));
                tvStartDate.setTextColor(UIUtils.getColor(R.color.text_black_212121));

                tvEndTitle.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                tvEndDate.setTextColor(UIUtils.getColor(R.color.text_gray));

                startLine.setVisibility(View.VISIBLE);
                endLine.setVisibility(View.GONE);

                Calendar calendar = DateUtils.getCalendar(tvStartDate.getText().toString());
//                pvCustomTime.setDate(calendar);
                break;
            case 2:
                tvStartTitle.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                tvStartDate.setTextColor(UIUtils.getColor(R.color.text_gray));

                tvEndTitle.setTextColor(UIUtils.getColor(R.color.text_gray));
                tvEndDate.setTextColor(UIUtils.getColor(R.color.text_black_212121));

                startLine.setVisibility(View.GONE);
                endLine.setVisibility(View.VISIBLE);

                Calendar calendar2 = DateUtils.getCalendar(tvEndDate.getText().toString());
//                pvCustomTime.setDate(calendar2);
                break;
        }
    }

    public interface OnTimePickerListener {
        void onDateStr(String startDate, String endDate);
    }



    public interface OnTimePickerOneListener {
        void onDateStr(String dateStr);
    }

    /**
     * 默认日历样式
     */
    public void initTimePicker(Context context, final OnTimePickerOneListener listener) {
        mCalendarOnePopup = new CalendarOnePopup(context,listener);
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public void showTimePicker(View view, String dateStr) {
        if (mCalendarOnePopup!=null){
            dateStr = dateStr.replace(".","-");
            mCalendarOnePopup.setDate(dateStr);
            mCalendarOnePopup.showAtLocation(view,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void hideYear() {
        if (calendarPopup != null) {
            calendarPopup.hideYear();
        }
    }

    public void showYear(){
        if (calendarPopup != null) {
            calendarPopup.showYear();
        }
    }

    public CalendarSelectPopup calendarPopup;

    public void initCustomTimePicker(Context context, OnTimePickerListener listener, CalendarSelectPopup.ItemDateOnListener listenerDate){
        calendarPopup = new CalendarSelectPopup(context,listener,listenerDate);
    }

    /**
     * 显示日历选择格式  默认显示有至今
     * @param view
     * @param startDate
     * @param endDate
     * @param isShowDay 是否显示天的选择区间,true:显示,false:不显示
     */
    public void showTimePicker(View view, String startDate, String endDate, boolean isShowDay) {
        if (calendarPopup != null) {
            startDate = startDate.replace(".","-");
            endDate = endDate.replace(".","-");
            calendarPopup.setDate(startDate, endDate,isShowDay,true);
            calendarPopup.showAtLocation(view,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void showTimePicker(View view, String startDate, String endDate, boolean isShowDay,boolean isToday) {
        if (calendarPopup != null) {
            startDate = startDate.replace(".","-");
            endDate = endDate.replace(".","-");
            calendarPopup.setDate(startDate, endDate,isShowDay,isToday);
            calendarPopup.showAtLocation(view,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void showTimePicker(View view, String startDate, String endDate) {
        if (calendarPopup != null) {
            calendarPopup.hideYear();

            startDate = startDate.replace(".","-");
            endDate = endDate.replace(".","-");
            calendarPopup.setDate(startDate, endDate,true,false);
            calendarPopup.showAtLocation(view,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

}
