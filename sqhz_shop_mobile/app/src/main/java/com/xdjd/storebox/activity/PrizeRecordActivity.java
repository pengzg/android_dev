package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.WinningRecordAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.CustomerWinningBean;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 * 奖品兑换记录
 */

public class PrizeRecordActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private WinningRecordAdapter adapter;
    List<CustomerWinningBean> listWinning = new ArrayList<>();//客户兑奖列表

    private String dateStartStr;
    private String dateEndStr;
    private String prizeid;

    private int page = 1;
    private int mFlag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_record);
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("奖品兑换记录");

        initData();
    }

    protected void initData() {
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        prizeid = getIntent().getStringExtra("prizeid");

        adapter = new WinningRecordAdapter(listWinning);
        mLvNoScroll.setAdapter(adapter);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                listWinning.clear();
                adapter.notifyDataSetChanged();
                getCustomerWinningList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getCustomerWinningList();
            }
        });

        getCustomerWinningList();
    }

    /**
     * 获取已兑奖客户列表
     */
    private void getCustomerWinningList() {
        AsyncHttpUtil<CustomerWinningBean> httpUtil = new AsyncHttpUtil<>(this, CustomerWinningBean.class, new IUpdateUI<CustomerWinningBean>() {
            @Override
            public void updata(CustomerWinningBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    listWinning.addAll(jsonStr.getListData());
                } else {
                    if (mFlag == 2) {
                        page--;
                        showToast("没有更多数据了!");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getWinningList, L_RequestParams.getWinningList(UserInfoUtils.getId(this),
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"), String.valueOf(page), prizeid, ""), true);
    }
}
