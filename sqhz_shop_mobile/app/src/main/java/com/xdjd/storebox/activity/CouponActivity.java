package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 优惠券activity
 * Created by lijipei on 2016/12/6.
 */

public class CouponActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooupon);
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("优惠券");
    }
}
