package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.GoodsListAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ConfirmOrderBean;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.xdjd.utils.DividerItemDecoration;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品列表
 * Created by lijipei on 2016/12/10.
 */

public class GoodsListActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    NestedScrollView mPullScroll;
    @BindView(R.id.goods_rv)
    RecyclerView mGoodsRv;

    private GoodsListAdapter adapter;
    private List<OrderGoodsDetailBean> list = new ArrayList<>();

    private VaryViewHelper mViewHelper = null;

    private String cartIds;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("商品列表");

        mViewHelper = new VaryViewHelper(mPullScroll);

        cartIds = getIntent().getStringExtra("cartIds");
        type = getIntent().getStringExtra("type");

        //禁止recyclerview滑动,这样在scrollview中会显示全部
        mGoodsRv.setNestedScrollingEnabled(false);
        //设置布局管理器
        mGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mGoodsRv.addItemDecoration(new DividerItemDecoration(this,1));

        adapter = new GoodsListAdapter(this);
        mGoodsRv.setAdapter(adapter);

        loadData(PublicFinal.FIRST, false);
    }

    private void loadData(int isFirst, boolean isDialog) {
        if (PublicFinal.FIRST == isFirst) {
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ConfirmOrderBean> httpUtil = new AsyncHttpUtil<>(this, ConfirmOrderBean.class, new IUpdateUI<ConfirmOrderBean>() {
            @Override
            public void updata(ConfirmOrderBean bean) {

                if (bean.getRepCode().equals("00")) {

                    list.addAll(bean.getDataList());
                    adapter.setData(list);
                    mViewHelper.showDataView();
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mViewHelper.showErrorView(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadData(PublicFinal.FIRST, false);
                    }
                });
            }

            @Override
            public void finish() {
                //mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getBulidCartList, L_RequestParams.getBulidCartList(
                UserInfoUtils.getId(this), cartIds, type,UserInfoUtils.getStoreHouseId(this)), isDialog);
    }
}
