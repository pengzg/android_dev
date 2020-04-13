package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CashReportSettingActivity extends BaseActivity {
    @BindView(R.id.iv_jd)
    ImageView mIvJd;
    @BindView(R.id.ll_jd)
    LinearLayout mLlJd;
    @BindView(R.id.iv_jj)
    ImageView mIvJj;
    @BindView(R.id.ll_jj)
    LinearLayout mLlJj;
    @BindView(R.id.iv_xx)
    ImageView mIvXx;
    @BindView(R.id.ll_xx)
    LinearLayout mLlXx;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_cash_report_setting;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("设置现金日报打印模式");

        if (UserInfoUtils.getCashReportType(this).equals(Comon.CashType1)){//简单
            mIvJd.setImageResource(R.drawable.check_true);
        }else if (UserInfoUtils.getCashReportType(this).equals(Comon.CashType2)){//精简
            mIvJj.setImageResource(R.drawable.check_true);
        }else if (UserInfoUtils.getCashReportType(this).equals(Comon.CashType3)){//详细
            mIvXx.setImageResource(R.drawable.check_true);
        }
    }

    @OnClick({R.id.ll_jd, R.id.ll_jj, R.id.ll_xx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_jd:
                clearIv();
                mIvJd.setImageResource(R.drawable.check_true);
                UserInfoUtils.setCashReportType(this, Comon.CashType1);
                break;
            case R.id.ll_jj:
                clearIv();
                mIvJj.setImageResource(R.drawable.check_true);
                UserInfoUtils.setCashReportType(this,Comon.CashType2);
                break;
            case R.id.ll_xx:
                clearIv();
                mIvXx.setImageResource(R.drawable.check_true);
                UserInfoUtils.setCashReportType(this,Comon.CashType3);
                break;
        }
    }

    /**
     * 清楚恢复所有图片的样式
     */
    private void clearIv() {
        mIvJd.setImageResource(R.drawable.check_false);
        mIvJj.setImageResource(R.drawable.check_false);
        mIvXx.setImageResource(R.drawable.check_false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
