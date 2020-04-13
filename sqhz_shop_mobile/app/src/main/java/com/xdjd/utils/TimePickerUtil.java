package com.xdjd.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.popup.CalendarSelectPopup;

import java.util.Calendar;

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

    //自定义日期选择样式
    public TimePickerView pvTime;//pvCustomTime;

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

    public CalendarSelectPopup calendarPopup;

    public void initCustomTimePicker(Context context,OnTimePickerListener listener,CalendarSelectPopup.ItemDateOnListener listenerDate){
        calendarPopup = new CalendarSelectPopup(context,listener,listenerDate);
    }

    /**
     * 显示日历选择格式
     * @param view
     * @param startDate
     * @param endDate
     * @param isShowDay 是否显示天的选择区间,true:显示,false:不显示
     */
    public void showTimePicker(View view,String startDate, String endDate,boolean isShowDay) {
        if (calendarPopup != null) {
            startDate = startDate.replace(".","-");
            endDate = endDate.replace(".","-");
            calendarPopup.setDate(startDate, endDate,isShowDay);
            calendarPopup.showAtLocation(view,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void showTimePicker(View view,String startDate, String endDate) {
        if (calendarPopup != null) {
            calendarPopup.hideYear();

            startDate = startDate.replace(".","-");
            endDate = endDate.replace(".","-");
            calendarPopup.setDate(startDate, endDate,true);
            calendarPopup.showAtLocation(view,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

}
