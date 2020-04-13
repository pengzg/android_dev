package com.xdjd.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnSelectStopListener;
import com.xdjd.distribution.R;
import com.xdjd.distribution.popup.CalendarOnePopup;
import com.xdjd.distribution.popup.CalendarSelectPopup;

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

    //自定义日期选择样式
    public TimePickerView pvTime;//pvCustomTime;
    //一天日期选择样式
    private CalendarOnePopup mCalendarOnePopup;

    /*public void showTimePicker(String startDate, String endDate) {
        startDate = startDate.replace(".","-");
        endDate = endDate.replace(".","-");
        LogUtils.e("showDate",startDate+"=="+endDate);
        if (pvCustomTime != null) {
            selectPickerTab(1);
            pvCustomTime.setDate(DateUtils.getCalendar(startDate));
            tvStartDate.setText(startDate);
            tvEndDate.setText(endDate);
            pvCustomTime.show();
        }
    }

    public void initCustomTimePicker(Context context, final OnTimePickerListener listener) {
        *//**
         * @description
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         *//*
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        final Calendar startDate = Calendar.getInstance();
        startDate.set(2010, 1, 01);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2036, 12, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
            }
        })
                .setSlideStopListener(new OnSelectStopListener() {
                    @Override
                    public void onItemTimeStop() {
                        Date date = pvCustomTime.returnSlideStopDate();
                        if (startLine.getVisibility() == View.VISIBLE) {
                            tvStartDate.setText(StringUtils.getDateString2(date));
                        } else {
                            tvEndDate.setText(StringUtils.getDateString2(date));
                        }
                    }
                })*//*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               *//*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*//*
               *//*.gravity(Gravity.RIGHT)// default is center*//*
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        rlStartDate = (RelativeLayout) v.findViewById(R.id.rl_start_date);
                        rlEndDate = (RelativeLayout) v.findViewById(R.id.rl_end_date);
                        tvStartTitle = (TextView) v.findViewById(R.id.tv_start_title);
                        tvEndTitle = (TextView) v.findViewById(R.id.tv_end_title);
                        tvStartDate = (TextView) v.findViewById(R.id.tv_start_date);
                        tvEndDate = (TextView) v.findViewById(R.id.tv_end_date);
                        startLine = v.findViewById(R.id.start_line);
                        endLine = v.findViewById(R.id.end_line);

                        tvStartTitle.setTextColor(UIUtils.getColor(R.color.text_gray));
                        tvStartDate.setTextColor(UIUtils.getColor(R.color.text_black_212121));

                        tvEndTitle.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                        tvEndDate.setTextColor(UIUtils.getColor(R.color.text_gray));

                        rlStartDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectPickerTab(1);
                            }
                        });

                        rlEndDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectPickerTab(2);
                            }
                        });

                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (DateUtils.calDateDifferent(tvStartDate.getText().toString().replace(".","-"),
                                        tvEndDate.getText().toString().replace(".","-")) == -1) {
                                    UIUtils.Toast("开始时间需小于结束时间");
                                    return;
                                }

                                listener.onDateStr(tvStartDate.getText().toString(), tvEndDate.getText().toString());
                                //                                pvCustomTime.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(UIUtils.getColor(R.color.line_gray))
                .build();
    }*/

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
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
//        Calendar selectedDate = Calendar.getInstance();
//        Calendar startDate = Calendar.getInstance();
//        startDate.set(2000, 1, 23);
//        Calendar endDate = Calendar.getInstance();
//        endDate.set(2035, 11, 28);
//        //时间选择器
//        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {//选中事件回调
//                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
//
//                /*btn_Time.setText(getTime(date));*/
//                listener.onDateStr(getTime(date));
//            }
//        }).setSlideStopListener(null)
//                //年月日时分秒 的显示与否，不设置则默认全部显示
//                .setType(new boolean[]{true, true, true, false, false, false})
//                .setLabel("年", "月", "日", "", "", "")
//                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
//                .setContentSize(21)
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
//                .setDecorView(null)
//                .build();
        mCalendarOnePopup = new CalendarOnePopup(context,listener);
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public void showTimePicker(View view,String dateStr) {
//        if (pvTime != null) {
//            pvTime.setDate(DateUtils.getCalendar(dateStr));
//            pvTime.show();
//        }
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
