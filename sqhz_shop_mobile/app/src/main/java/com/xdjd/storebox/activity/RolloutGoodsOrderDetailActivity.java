package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.RolloutGoodsOrderDetailAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.PHOrderDetailBean;
import com.xdjd.storebox.bean.RolloutGoodsOrderBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.EaseTitleBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/22.
 */

public class RolloutGoodsOrderDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    PullToRefreshListView mPullList;
    @BindView(R.id.tv_order_code)
    TextView mTvOrderCode;
    @BindView(R.id.tv_order_amount)
    TextView mTvOrderAmount;
    @BindView(R.id.ll_order_amount)
    LinearLayout mLlOrderAmount;

    private String orderId;

    private int page = 1;
    private int mFlag = 0;

    RolloutGoodsOrderDetailAdapter adapter;
    private List<PHOrderDetailBean> list = new ArrayList<>();

    private RolloutGoodsOrderBean bean;

    private PHOrderDetailBean detailBean;

    BigDecimal skAmount;//刷卡
    BigDecimal xjAmount;//现金
    BigDecimal yeAmount;//余额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_rollout_goods_order_detail);
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);

        orderId = getIntent().getStringExtra("orderId");
        bean = (RolloutGoodsOrderBean) getIntent().getSerializableExtra("bean");

        mTitleBar.setTitle("铺货单详情");

        mTvOrderCode.setText(bean.getOrderCode());
        mTvOrderAmount.setText(bean.getTotalAmount());

        adapter = new RolloutGoodsOrderDetailAdapter(list);
        initRefresh(mPullList);
        mPullList.setMode(PullToRefreshBase.Mode.DISABLED);
        mPullList.setAdapter(adapter);
        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryPhOrderDetailList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryPhOrderDetailList();
            }
        });

        mPullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i = i - 1;//刷新listview会添加头部,下标所以减1
            }
        });

        queryPhOrderDetailList();
    }

    private void queryPhOrderDetailList() {
        AsyncHttpUtil<PHOrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, PHOrderDetailBean.class,
                new IUpdateUI<PHOrderDetailBean>() {
                    @Override
                    public void updata(PHOrderDetailBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            if (jsonStr.getList() != null && jsonStr.getList().size() > 0) {
                                detailBean = jsonStr.getList().get(0);
                            }
                            if (jsonStr.getList() != null && jsonStr.getList().size() > 0) {
                                list = jsonStr.getList().get(0).getListData();
                                adapter.setData(list);
                            } else {
                                showToast("没有更多数据了");
                            }
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
                        mPullList.onRefreshComplete();
                    }
                });
        httpUtil.post(M_Url.queryPhOrderDetailList, L_RequestParams.
                queryPhOrderDetailList(UserInfoUtils.getId(this), orderId), true);
    }

}
