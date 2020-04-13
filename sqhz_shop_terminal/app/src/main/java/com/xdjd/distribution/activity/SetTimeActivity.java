package com.xdjd.distribution.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.view.EaseTitleBar;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/6/2.
 */

public class SetTimeActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.start_time_rl)
    RelativeLayout mStartTimeRl;
    @BindView(R.id.end_time_rl)
    RelativeLayout mEndTimeRl;
    @BindView(R.id.query_btn)
    Button mQueryBtn;
    @BindView(R.id.tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.tv_end_date)
    TextView mTvEndDate;

    private String startData;//开始时间
    private String endData;//结束时间

    //是否是开始或结束时间
    private boolean isStart;

    private DatePickerDialog startDateDialog;
    private DatePickerDialog endDateDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_set_time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("时间设置");

        startData = getIntent().getStringExtra("startData");
        endData = getIntent().getStringExtra("endData");

        mTvStartDate.setText(startData);
        mTvEndDate.setText(endData);

        Calendar cStrar = Calendar.getInstance();
        cStrar.setTime(StringUtils.toDateFormater2(startData));
        startDateDialog = new DatePickerDialog(this, dateListener, cStrar.get(Calendar.YEAR),
                cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
//        startDateDialog.updateDate(cStrar.get(Calendar.YEAR),
//                cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));

        Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(StringUtils.toDateFormater2(endData));
        endDateDialog = new DatePickerDialog(this, dateListener, cEnd.get(Calendar.YEAR),
                cEnd.get(Calendar.MONTH), cEnd.get(Calendar.DAY_OF_MONTH));
//        endDateDialog.updateDate(cEnd.get(Calendar.YEAR),
//                cEnd.get(Calendar.MONTH), cEnd.get(Calendar.DAY_OF_MONTH));
    }

    @OnClick({R.id.start_time_rl, R.id.end_time_rl, R.id.query_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_rl:
                isStart = true;
                startDateDialog.show();
                break;
            case R.id.end_time_rl:
                isStart = false;
                endDateDialog.show();
                break;
            case R.id.query_btn:
                Intent intent = new Intent();
                intent.putExtra("startData", mTvStartDate.getText());
                intent.putExtra("endData", mTvEndDate.getText());
                setResult(100, intent);
                finish();
                break;
        }
    }

    /**
     * 日选择回调接口
     */
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
            String date = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            date = DateUtils.getCurTimeStr(date);
            if (isStart) {
                int flag = DateUtils.calDateDifferent(mTvEndDate.getText().toString(), date);
                if (flag == 1) {
                    showToast("不能高于结束时间");
                    return;
                }

                mTvStartDate.setText(date);
                Calendar cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvStartDate.getText().toString()));
                startDateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
            } else {
                if (DateUtils.isMoreThanToday(date)) {
                    showToast("结束时间不能超过今天");
                    return;
                }

                if (DateUtils.calDateDifferent(mTvStartDate.getText().toString(), date) == -1) {
                    showToast("不能低于开始时间");
                    return;
                }

                mTvEndDate.setText(date);
                Calendar cEnd = Calendar.getInstance();
                cEnd.setTime(StringUtils.toDateFormater2(mTvEndDate.getText().toString()));
                endDateDialog.updateDate(cEnd.get(Calendar.YEAR),
                        cEnd.get(Calendar.MONTH), cEnd.get(Calendar.DAY_OF_MONTH));
            }
        }
    };
}
