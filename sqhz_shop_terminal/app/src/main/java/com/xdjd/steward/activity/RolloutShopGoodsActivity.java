package com.xdjd.steward.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RolloutGoodsGoodsAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.PHGoodsDetailListBean;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/5
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutShopGoodsActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;

    private String shopName;//店铺名称
    private String dateStartStr;
    private String dateEndStr;

    private RolloutGoodsGoodsAdapter adapterGoods;
    private List<PHGoodsDetailListBean> list = new ArrayList<>();
    private int page = 1;
    private int mFlag = 0;
    private String customerId;

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_shop_goods;
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
        shopName = getIntent().getStringExtra("shopName");
        customerId = getIntent().getStringExtra("shopId");
        mTitleBar.leftBack(this);
        mTitleBar.setTitle(shopName);

        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");

        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mLvPull);
        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;

                list.clear();
                adapterGoods.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        adapterGoods = new RolloutGoodsGoodsAdapter();
        mLvPull.setAdapter(adapterGoods);
        loadData();
    }

    private void loadData() {
        AsyncHttpUtil httpUtil = new AsyncHttpUtil<>(this, PHGoodsDetailListBean.class, new IUpdateUI<PHGoodsDetailListBean>() {
            @Override
            public void updata(PHGoodsDetailListBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        adapterGoods.setData(list);
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mLvPull.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.phGoodsDetailList, L_RequestParams.phGoodsDetailList(String.valueOf(page), customerId, "0", ""
        ,dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-")), true);
    }

    @OnClick({R.id.ll_clear, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_clear:
                break;
            case R.id.ll_search:
                break;
        }
    }
}
