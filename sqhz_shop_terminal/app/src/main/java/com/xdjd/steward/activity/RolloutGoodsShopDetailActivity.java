package com.xdjd.steward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.SearchGoodsActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.PHGoodsDetailListBean;
import com.xdjd.distribution.bean.PhShopHzBean;
import com.xdjd.steward.adapter.RolloutGoodsShopAdapter;
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
 *     time   : 2018/1/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsShopDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;

    private String goodsId;//商品id
    private String dateStartStr;
    private String dateEndStr;

    private RolloutGoodsShopAdapter adapter;
    private List<PhShopHzBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_shop_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("商品铺货店铺");
    }

    @Override
    protected void initData() {
        super.initData();
        goodsId = getIntent().getStringExtra("goodsId");
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");

        initRefresh(mLvPull);
        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);

        adapter = new RolloutGoodsShopAdapter();
        mLvPull.setAdapter(adapter);
        adapter.setData(list);

        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        AsyncHttpUtil httpUtil = new AsyncHttpUtil<>(this, PhShopHzBean.class, new IUpdateUI<PhShopHzBean>() {
            @Override
            public void updata(PhShopHzBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                    adapter.notifyDataSetChanged();
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
        httpUtil.post(M_Url.phShopHzList, L_RequestParams.phShopHzList(goodsId,String.valueOf(page),
                mTvSearch.getText().toString(), "",
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-")), true);
    }

    @OnClick({R.id.ll_search, R.id.ll_clear})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_search:
                intent = new Intent(this, SearchGoodsActivity.class);
                intent.putExtra("searchStr", mTvSearch.getText().toString());
                intent.putExtra("hint", "店铺名称");
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_clear:
                mTvSearch.setText("");
                mLlClear.setVisibility(View.GONE);
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                mTvSearch.setText(data.getStringExtra("searchStr"));
                if (mTvSearch.getText().length() > 0) {
                    mLlClear.setVisibility(View.VISIBLE);
                } else {
                    mLlClear.setVisibility(View.GONE);
                }
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
                break;
        }
    }

}
