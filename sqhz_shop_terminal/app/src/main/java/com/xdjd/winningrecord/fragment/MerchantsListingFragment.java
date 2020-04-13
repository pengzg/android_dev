package com.xdjd.winningrecord.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.BindingFacilityShopAdapter;
import com.xdjd.distribution.adapter.MerchantsListingAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ShopListingBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/9
 *     desc   : 客户信息列表fragment
 *     version: 1.0
 * </pre>
 */

public class MerchantsListingFragment extends BaseFragment {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.lv_noscroll)
    NoScrollListView mLvNoscroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;

    private int page = 1;
    private int mFlag = 0;

    private MerchantsListingAdapter adapter;
    private List<ShopListingBean> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchants_listing, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mEtSearch.setHint("按店铺名称搜索");

        adapter = new MerchantsListingAdapter();
        mLvNoscroll.setAdapter(adapter);
        adapter.setData(list);

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;

                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        loadData();
    }

    @OnClick({R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;

                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
                break;
        }
    }

    /**
     * 获取店铺的关联详情
     */
    private void loadData() {
        AsyncHttpUtil<ShopListingBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ShopListingBean.class, new IUpdateUI<ShopListingBean>() {
            @Override
            public void updata(ShopListingBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvTotalNum.setText(jsonBean.getTotalNum());
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.getShopList, L_RequestParams.getShopList(String.valueOf(page), mEtSearch.getText().toString()), true);
    }

}
