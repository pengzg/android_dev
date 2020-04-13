package com.xdjd.steward.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.UIUtils;

import java.util.Date;

import butterknife.BindView;

/**
 * 线路选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DateOnePopup extends PopupWindow implements View.OnClickListener{

    TextView mTvDay;
    TextView mTvWeek;
    TextView mTvMonth;
    TextView mTvLastMonth;
    TextView mTvCancel;
    TextView mTvYesterday;
    TextView mTvLastweek;
    private View view;
    private LinearLayout ll;

    private int dateType = 1;

    private ItemDateOnListener listener;

    private String startDate,endDate;

    public void setDateType(int dateType){
        //1.今日;2.本周;3.本月;4.自定义;5.上月;6.昨天;7.上周
        this.dateType = dateType;
        mTvDay.setTextColor(UIUtils.getColor(R.color.black));
        mTvWeek.setTextColor(UIUtils.getColor(R.color.black));
        mTvMonth.setTextColor(UIUtils.getColor(R.color.black));
        mTvLastMonth.setTextColor(UIUtils.getColor(R.color.black));
        mTvYesterday.setTextColor(UIUtils.getColor(R.color.black));
        mTvLastweek.setTextColor(UIUtils.getColor(R.color.black));

        switch (dateType){
            case 1:
                mTvDay.setTextColor(UIUtils.getColor(R.color.color_699dff));
                break;
            case 2:
                mTvWeek.setTextColor(UIUtils.getColor(R.color.color_699dff));
                break;
            case 3:
                mTvMonth.setTextColor(UIUtils.getColor(R.color.color_699dff));
                break;
            case 5:
                mTvLastMonth.setTextColor(UIUtils.getColor(R.color.color_699dff));
                break;
            case 6:
                mTvYesterday.setTextColor(UIUtils.getColor(R.color.color_699dff));
                break;
            case 7:
                mTvLastweek.setTextColor(UIUtils.getColor(R.color.color_699dff));
                break;
        }
    }

    public DateOnePopup(Context context, ItemDateOnListener listener) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_date_one, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
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

        mTvDay = (TextView) view.findViewById(R.id.tv_day);
        mTvWeek = (TextView) view.findViewById(R.id.tv_week);
        mTvMonth = (TextView) view.findViewById(R.id.tv_month);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        mTvLastMonth = (TextView) view.findViewById(R.id.tv_last_month);
        mTvYesterday = (TextView) view.findViewById(R.id.tv_yesterday);
        mTvLastweek = (TextView) view.findViewById(R.id.tv_lastweek);

        mTvDay.setOnClickListener(this);
        mTvWeek.setOnClickListener(this);
        mTvMonth.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvLastMonth.setOnClickListener(this);
        mTvYesterday.setOnClickListener(this);
        mTvLastweek.setOnClickListener(this);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
            case R.id.tv_day://当天
                startDate = DateUtils.getDataTime(DateUtils.dateFormater4);
                endDate = DateUtils.getDataTime(DateUtils.dateFormater4);
                listener.onItemDate(1,mTvDay.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_week://本周
                startDate = DateUtils.getFirstDayOfWeek(new Date(),DateUtils.dateFormater4);
                endDate = DateUtils.getLastDayOfWeek(new Date(),DateUtils.dateFormater4);
                listener.onItemDate(2,mTvWeek.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_month://当月
                startDate = DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
                endDate = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);
                listener.onItemDate(3,mTvMonth.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_last_month://上月
                startDate = DateUtils.currentMonthFirst(DateUtils.dateFormater4, -1);
                endDate = DateUtils.currentMonthEnd(DateUtils.dateFormater4, -1);
                listener.onItemDate(5,mTvLastMonth.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_yesterday://昨天
                startDate = DateUtils.getYesterdayDay(new Date(),DateUtils.dateFormater4,-1);
                endDate = DateUtils.getYesterdayDay(new Date(),DateUtils.dateFormater4,-1);
                listener.onItemDate(6,mTvYesterday.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_lastweek://上周
                startDate = DateUtils.getLastWeekFirstDay("yyyy.MM.dd",-1);
                endDate = DateUtils.getLastWeekLastDay("yyyy.MM.dd",-1);
                listener.onItemDate(7,mTvLastweek.getText().toString(),startDate,endDate);
                dismiss();
                break;
            case R.id.tv_cancel://取消
                dismiss();
                break;
        }
    }

    public interface ItemDateOnListener{
        void onItemDate(int position,String dateName,String startDate,String endDate);
    }
}
