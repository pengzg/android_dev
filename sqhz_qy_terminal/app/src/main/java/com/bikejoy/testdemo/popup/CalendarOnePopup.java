package com.bikejoy.testdemo.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bikejoy.testdemo.R;
import com.bikejoy.utils.DateUtils;
import com.bikejoy.utils.StringUtils;
import com.bikejoy.utils.TimePickerUtil;
import com.bikejoy.view.calendarview.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * 线路选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CalendarOnePopup extends PopupWindow {

    private View v;
    private LinearLayout ll;
    private CalendarPickerView calendarPickerView;

    private Date date;

    public TextView tvCancel;

    ItemDateOnListener listenerDate;

    public interface ItemDateOnListener{
        void onItemDate(int position, String dateName, String startDate, String endDate);
    }

    public CalendarOnePopup(Context context, final TimePickerUtil.OnTimePickerOneListener listener) {
        super(context);

        this.listenerDate = listenerDate;
        date = new Date();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.popup_calendar_one, null);

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

        ll = (LinearLayout) v.findViewById(R.id.pop_layout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        lp.height = height/2;
        ll.setLayoutParams(lp);

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
        tvCancel = (TextView) v.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                listener.onDateStr(StringUtils.getDateString2(date));
                dismiss();
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

    }


    public void setDate(String startDate) {
        Calendar calendar = DateUtils.getCalendar(startDate);
        calendarPickerView.selectDate(calendar.getTime());
    }

}
