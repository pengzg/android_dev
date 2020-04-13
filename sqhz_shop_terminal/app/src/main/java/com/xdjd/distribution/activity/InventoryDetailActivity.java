package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.InventoryDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.InventoryDetailBean;
import com.xdjd.utils.UIUtils;
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
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/11
 *     desc   : 盘点详情页
 *     version: 1.0
 * </pre>
 */

public class InventoryDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_inventory_detail)
    NoScrollListView mLvInventoryDetail;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private String cimId;

    private int page = 1;
    private int mFlag = 0;

    private List<InventoryDetailBean> list = new ArrayList<>();
    private InventoryDetailAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_inventory_detail;
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
        mTitleBar.setTitle("客户盘点详情");

        cimId = getIntent().getStringExtra("cimId");

        adapter = new InventoryDetailAdapter();
        mLvInventoryDetail.setAdapter(adapter);
        adapter.setDate(list);

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadDate();
            }
        });

        loadDate();
    }

    private void loadDate() {
        AsyncHttpUtil<InventoryDetailBean> httpUtil = new AsyncHttpUtil<>(this, InventoryDetailBean.class, new IUpdateUI<InventoryDetailBean>() {
            @Override
            public void updata(InventoryDetailBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        list.addAll(jsonStr.getDataList());
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(jsonStr.getRepMsg());
                }
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
        httpUtil.post(M_Url.getInventoryDetail, L_RequestParams.getInventoryDetail(cimId, String.valueOf(page)), true);
    }
}
