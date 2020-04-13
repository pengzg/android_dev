package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class IntegralRuleActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_rule);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("积分规则");
    }
}
