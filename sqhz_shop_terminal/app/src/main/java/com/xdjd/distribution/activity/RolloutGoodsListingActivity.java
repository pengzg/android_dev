package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RolloutGoodsListingAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.PHShopListBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsListingActivity extends BaseActivity implements RolloutGoodsListingAdapter.itemListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_noscroll)
    NoScrollListView mLvNoscroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;

    private RolloutGoodsListingAdapter adapter;
    private List<PHShopListBean> listPHShop = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_listing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("铺货店铺汇总列表");

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullScroll.onRefreshComplete();
                mFlag = 1;
                page = 1;

                listPHShop.clear();
                adapter.notifyDataSetChanged();
                phShopList(PublicFinal.TWO,true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullScroll.onRefreshComplete();
                page++;
                mFlag = 2;
                phShopList(PublicFinal.TWO,true);
            }
        });

        mVaryViewHelper = new VaryViewHelper(mPullScroll);

        adapter = new RolloutGoodsListingAdapter(this);
        mLvNoscroll.setAdapter(adapter);
        adapter.setData(listPHShop);
        phShopList(PublicFinal.FIRST,false);
       /* mLvNoscroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转铺货详情列表接口
                startActivity(RolloutGoodsDetailActivity.class);
            }
        });*/
    }


    @Override
    public void item(int i) {
        Intent intent = new Intent(this, RolloutGoodsDetailActivity.class);
        intent.putExtra("customerId", listPHShop.get(i).getShopId());
        startActivity(intent);
    }

    private void phShopList(int isFirst,boolean isDialog) {
        if (isFirst == PublicFinal.FIRST){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<PHShopListBean> httpUtil = new AsyncHttpUtil<>(this, PHShopListBean.class, new IUpdateUI<PHShopListBean>() {
            @Override
            public void updata(PHShopListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mVaryViewHelper.showDataView();
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listPHShop.addAll(jsonBean.getListData());
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }else if (mFlag == 0){
                            mVaryViewHelper.showEmptyView();
                        }
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getRepMsg(),new OnErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(),new OnErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.phShopList, L_RequestParams.
                phShopList(String.valueOf(page), mTvSearch.getText().toString()), true);
    }

    public class OnErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mFlag = 1;
            page = 1;
            listPHShop.clear();
            adapter.notifyDataSetChanged();
            phShopList(PublicFinal.FIRST,false);
        }
    }

    @OnClick({R.id.ll_clear, R.id.ll_search})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_clear:
                mTvSearch.setText("");
                mLlClear.setVisibility(View.GONE);
                mFlag = 1;
                page = 1;
                listPHShop.clear();
                adapter.notifyDataSetChanged();
                phShopList(PublicFinal.FIRST,false);
                break;
            case R.id.ll_search:
                intent = new Intent(this, SearchGoodsActivity.class);
                intent.putExtra("searchStr", mTvSearch.getText().toString());
                intent.putExtra("hint","请输入店铺名称");
                startActivityForResult(intent, 100);
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
                mFlag = 1;
                page = 1;
                listPHShop.clear();
                adapter.notifyDataSetChanged();
                phShopList(PublicFinal.FIRST,false);
                break;
        }
    }
}
