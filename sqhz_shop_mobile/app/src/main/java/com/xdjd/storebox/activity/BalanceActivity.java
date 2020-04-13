package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.BalanceAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/18.
 */

public class BalanceActivity extends BaseActivity implements BalanceAdapter.incomeAndExpenseListener{
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.balance_account)
    TextView balanceAccount;
    @BindView(R.id.listView)
    NoScrollListView listView;
    @BindView(R.id.refreshScrollView)
    PullToRefreshScrollView refreshScrollView;

    private VaryViewHelper viewHelper;
    private BalanceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_balance);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleBar.leftBack(this);
        titleBar.setTitle("我的余额");
        titleBar.setRightText("筛选");
        titleBar.setRightTextColor(R.color.text_555);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityBalanceDetailFilter.class);
            }
        });
        viewHelper = new VaryViewHelper(refreshScrollView);
        initRefresh(refreshScrollView);
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
        adapter = new BalanceAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void item() {
        startActivity(IncomeAndExpensesActivity.class);
    }
}
