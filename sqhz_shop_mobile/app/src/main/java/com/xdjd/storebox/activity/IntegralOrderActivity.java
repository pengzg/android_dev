package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.IntegralOrderAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.integralOrderBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/2/24.
 */

public class IntegralOrderActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.order_list)
    NoScrollListView mOrderList;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    private IntegralOrderAdapter adapter;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<integralOrderBean> list = new ArrayList<>();
    private VaryViewHelper mVaryViewHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_order);
        ButterKnife.bind(this);
        mVaryViewHelper = new VaryViewHelper(mOrderList);
        initData();
    }

    protected void initData() {
        mTitleBar.setTitle("我的订单");
        mTitleBar.leftBack(this);

        mTitleBar.setRightImageResource(R.drawable.kefu_img);
        mTitleBar.showRightImage();
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("客服");
            }
        });

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNo++;
                mFlag = 2;
                loadData(PublicFinal.TWO);
            }
        });

        adapter = new IntegralOrderAdapter();
        mOrderList.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }
    /*加载积分订单*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<integralOrderBean> httpUtil = new AsyncHttpUtil<>(this, integralOrderBean.class, new IUpdateUI<integralOrderBean>() {
            @Override
            public void updata(integralOrderBean bean) {
                if(bean.getRepCode().equals("00")){
                    if(null != bean.getListData() && bean.getListData().size() > 0){
                        list.addAll(bean.getListData());
                        adapter.setData(list);
                        adapter.notifyDataSetChanged();
                        mVaryViewHelper.showDataView();
                    }else{
                        if(mFlag == 2){
                            showToast("没有更多数据了！");
                            pageNo--;
                        }else{
                            mVaryViewHelper.showEmptyView("还没有积分订单，快去积分商城逛逛吧！");
                        }
                    }

                }else{
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.getIntegralOrder, L_RequestParams.GetIntegralOrder(UserInfoUtils.getId(this),String.valueOf(pageNo),"10"),false);
    }

}
