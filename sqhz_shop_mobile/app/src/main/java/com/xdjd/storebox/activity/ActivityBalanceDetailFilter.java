package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.utils.StringUtils;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/9/18.
 */

public class ActivityBalanceDetailFilter extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.ll_start)
    LinearLayout llStart;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.ll_end)
    LinearLayout llEnd;
    @BindView(R.id.filter_tv)
    TextView filterTv;

    private OptionsPickerView pickerView;
    private TimePickerView timePickerView;
    private ArrayList<String> listStr = new ArrayList<String>();
    private String[] typeStr = {"全部","支出","收入"};
    private Boolean timeFlag;
    private String start_time;
    private String end_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail_filter);
        ButterKnife.bind(this);
        initView();
    }



    private void initView() {
        titleBar.leftBack(this);
        titleBar.setTitle("筛选收支明细");
        pickerView = new OptionsPickerView(this);
        pickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                type.setText(listStr.get(options1));
            }
        });
        for(int i = 0; i < typeStr.length; i++){
            listStr.add(typeStr[i]);
        }
        initViewTime();
    }

    private void initViewTime(){
        timePickerView = new TimePickerView(this,TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar =  Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR)-3,calendar.get(Calendar.YEAR));
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if(date.after(new Date())){
                    showToast("日期不能再当前日期之后");
                }else{
                    if(timeFlag == true){
                        start_time = StringUtils.getTime(date);
                        startTime.setText(start_time);
                    }else{
                        end_time = StringUtils.getTime(date);
                        if(StringUtils.calDateDifferent(end_time,start_time) < 0){
                            showToast("结束日期不能小于起始日期！");
                        }else{
                            endTime.setText(end_time);
                        }

                    }

                }
            }
        });
    }

    @OnClick({R.id.ll_type, R.id.ll_start, R.id.ll_end, R.id.filter_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_type:
                pickerView.setPicker(listStr);
                pickerView.setCyclic(false);//是否循环
                pickerView.show();
                break;
            case R.id.ll_start:
                timeFlag = true;
                timePickerView.show();
                break;
            case R.id.ll_end:
                timeFlag = false;
                timePickerView.show();
                break;
            case R.id.filter_tv:
                finish();
                break;
        }
    }
}
