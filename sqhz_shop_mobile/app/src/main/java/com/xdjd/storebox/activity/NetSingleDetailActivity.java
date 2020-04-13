package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.NetSingleGoodsListAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 网单详情activity
 * Created by lijipei on 2016/12/13.
 */

public class NetSingleDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.goods_list_listview)
    NoScrollListView mGoodsListListview;

    NetSingleGoodsListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_single_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitleBar.setTitle("网单详情");
        mTitleBar.leftBack(this);

        adapter = new NetSingleGoodsListAdapter();
        mGoodsListListview.setAdapter(adapter);
    }
}
