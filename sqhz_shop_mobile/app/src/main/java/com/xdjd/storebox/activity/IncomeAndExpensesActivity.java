package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.BalanceAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/19.
 */

public class IncomeAndExpensesActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expenses);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleBar.leftBack(this);
        titleBar.setTitle("收支详情");
    }


}
