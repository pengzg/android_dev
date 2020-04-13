package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.calendarview.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线路选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CalendarSelectPopup extends PopupWindow implements View.OnClickListener{

    private View v;
    private LinearLayout ll;
    private CalendarPickerView calendarPickerView;

    private Date date;

    public TextView tvCancel,tvSubmit;
    public RelativeLayout rlStartDate, rlEndDate;
    public TextView tvStartTitle, tvEndTitle, tvStartDate, tvEndDate;
    public View startLine, endLine;

    private LinearLayout llDay,llYear;//天数的linearLayout

    private String startDate,endDate;

    TextView mTvDay;
    TextView mTvWeek;
    TextView mTvMonth;
    TextView mTvLastMonth;
    TextView mTvCancel;
    TextView mTvYesterday;
    TextView mTvLastweek;
    TextView mTvYear,mTvLastYear;
    TextView mTvBeforeYesterday,mTvSevenDay,mTvBeforeLastMonth,mTvLastBeforeYear;

    ItemDateOnListener listenerDate;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周,
            //8.前天;9.上上周;10.上上月;11.今年;12.去年;13.前年
            case R.id.tv_day://当天
                startDate = DateUtils.getDataTime(DateUtils.dateFormater4);
                endDate = DateUtils.getDataTime(DateUtils.dateFormater4);
                listenerDate.onItemDate(1,mTvDay.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_week://本周
                startDate = DateUtils.getFirstDayOfWeek(new Date(),DateUtils.dateFormater4);
                endDate = DateUtils.getLastDayOfWeek(new Date(),DateUtils.dateFormater4);
                listenerDate.onItemDate(2,mTvWeek.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_month://当月
                startDate = DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
                endDate = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);
                listenerDate.onItemDate(3,mTvMonth.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_last_month://上月
                startDate = DateUtils.currentMonthFirst(DateUtils.dateFormater4, -1);
                endDate = DateUtils.currentMonthEnd(DateUtils.dateFormater4, -1);
                listenerDate.onItemDate(5,mTvLastMonth.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_yesterday://昨天
                startDate = DateUtils.getYesterdayDay(new Date(),DateUtils.dateFormater4,-1);
                endDate = DateUtils.getYesterdayDay(new Date(),DateUtils.dateFormater4,-1);
                listenerDate.onItemDate(6,mTvYesterday.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_lastweek://上周
                startDate = DateUtils.getLastWeekFirstDay("yyyy.MM.dd",-1);
                endDate = DateUtils.getLastWeekLastDay("yyyy.MM.dd",-1);
                listenerDate.onItemDate(7,mTvLastweek.getText().toString(),startDate,endDate);
                dismiss();
                break;

            //-----------------------------------------
            case R.id.tv_before_yesterday://前天
                startDate = DateUtils.getYesterdayDay(new Date(),DateUtils.dateFormater4,-2);
                endDate = DateUtils.getYesterdayDay(new Date(),DateUtils.dateFormater4,-2);
                listenerDate.onItemDate(8,mTvBeforeYesterday.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_seven_day://前七天--9
                startDate = DateUtils.getDistanceDay(new Date(),"yyyy.MM.dd",-6);
                endDate = DateUtils.getDistanceDay(new Date(),"yyyy.MM.dd",0);
                listenerDate.onItemDate(9,mTvSevenDay.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_before_last_month://上上月--10
                startDate = DateUtils.currentMonthFirst(DateUtils.dateFormater4, -2);
                endDate = DateUtils.currentMonthEnd(DateUtils.dateFormater4, -2);
                listenerDate.onItemDate(10,mTvBeforeLastMonth.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_year://今年
                startDate = DateUtils.getCurrYearFirst(DateUtils.dateFormater4,0);
                endDate = DateUtils.getCurrYearLast(DateUtils.dateFormater4,0);
                listenerDate.onItemDate(11,mTvYear.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_last_year://去年12
                startDate = DateUtils.getCurrYearFirst(DateUtils.dateFormater4,-1);
                endDate = DateUtils.getCurrYearLast(DateUtils.dateFormater4,-1);
                listenerDate.onItemDate(12,mTvLastYear.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_last_before_year://前年13
                startDate = DateUtils.getCurrYearFirst(DateUtils.dateFormater4,-2);
                endDate = DateUtils.getCurrYearLast(DateUtils.dateFormater4,-2);
                listenerDate.onItemDate(13,mTvLastBeforeYear.getText().toString(),startDate,endDate);
                dismiss();
                break;
        }
    }

    /**
     * 隐藏年的选择条件
     */
    public void hideYear() {
        llYear.setVisibility(View.GONE);
    }

    public void showYear(){
        llYear.setVisibility(View.VISIBLE);
    }


    public interface OnCalendarPickerListener {
        void onDateStr(String startDate, String endDate);
    }

    public interface ItemDateOnListener{
        void onItemDate(int position,String dateName,String startDate,String endDate);
    }

    public CalendarSelectPopup(Context context, final TimePickerUtil.OnTimePickerListener listener, ItemDateOnListener listenerDate) {
        super(context);

        this.listenerDate = listenerDate;
        date = new Date();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.popup_calendar_select, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(v);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        v.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = v.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);

        calendarPickerView = (CalendarPickerView) v.findViewById(R.id.calendar_view);
        //初始化日历
        calendarPickerView.init(lastYear.getTime(), nextYear.getTime()).withSelectedDate(new Date());

        llDay = (LinearLayout) v.findViewById(R.id.ll_day);
        llYear = (LinearLayout) v.findViewById(R.id.ll_year);

        tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
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

        mTvDay = (TextView) v.findViewById(R.id.tv_day);
        mTvWeek = (TextView) v.findViewById(R.id.tv_week);
        mTvMonth = (TextView) v.findViewById(R.id.tv_month);
        mTvCancel = (TextView) v.findViewById(R.id.tv_cancel);
        mTvLastMonth = (TextView) v.findViewById(R.id.tv_last_month);
        mTvYesterday = (TextView) v.findViewById(R.id.tv_yesterday);
        mTvLastweek = (TextView) v.findViewById(R.id.tv_lastweek);

        mTvBeforeYesterday = (TextView) v.findViewById(R.id.tv_before_yesterday);
        mTvSevenDay = (TextView) v.findViewById(R.id.tv_seven_day);
        mTvBeforeLastMonth = (TextView) v.findViewById(R.id.tv_before_last_month);

        mTvYear = (TextView) v.findViewById(R.id.tv_year);
        mTvLastYear = (TextView) v.findViewById(R.id.tv_last_year);
        mTvLastBeforeYear = (TextView) v.findViewById(R.id.tv_last_before_year);

        mTvDay.setOnClickListener(this);
        mTvWeek.setOnClickListener(this);
        mTvMonth.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvLastMonth.setOnClickListener(this);
        mTvYesterday.setOnClickListener(this);
        mTvLastweek.setOnClickListener(this);
        mTvBeforeYesterday.setOnClickListener(this);
        mTvSevenDay.setOnClickListener(this);
        mTvBeforeLastMonth.setOnClickListener(this);
        mTvYear.setOnClickListener(this);
        mTvLastYear.setOnClickListener(this);
        mTvLastBeforeYear.setOnClickListener(this);

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
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (startLine.getVisibility() == View.VISIBLE){
                    tvStartDate.setText(StringUtils.getDateString2(date));
                } else {
                    tvEndDate.setText(StringUtils.getDateString2(date));
                }
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

    }


    public void setDate(String startDate, String endDate,boolean isShowDay) {
        Calendar calendar = DateUtils.getCalendar(startDate);
        calendarPickerView.selectDate(calendar.getTime());

        if (isShowDay){
            llDay.setVisibility(View.VISIBLE);
        }else{
            llDay.setVisibility(View.GONE);
        }

        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);

        selectPickerTab(1);
    }

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
                calendarPickerView.selectDate(calendar.getTime());
                break;
            case 2:
                tvStartTitle.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                tvStartDate.setTextColor(UIUtils.getColor(R.color.text_gray));

                tvEndTitle.setTextColor(UIUtils.getColor(R.color.text_gray));
                tvEndDate.setTextColor(UIUtils.getColor(R.color.text_black_212121));

                startLine.setVisibility(View.GONE);
                endLine.setVisibility(View.VISIBLE);

                Calendar calendar2 = DateUtils.getCalendar(tvEndDate.getText().toString());
                calendarPickerView.selectDate(calendar2.getTime());
                break;
        }
    }
}
