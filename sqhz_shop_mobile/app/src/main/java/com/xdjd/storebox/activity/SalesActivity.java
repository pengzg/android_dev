package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.SalesAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.HomeActivityBean;
import com.xdjd.storebox.bean.HomeBean;
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
 * 促销活动
 * Created by lijipei on 2016/12/8.
 */

public class SalesActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.sales_listview)
    NoScrollListView mSalesListview;
    @BindView(R.id.sales_pull_scroll)
    ScrollView mSalesPullScroll;

    private SalesAdapter adapter;

    private List<HomeActivityBean> datalist = new ArrayList<>();

    private VaryViewHelper mViewHelper = null;

    private Intent intent;

    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_promotion);
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
        title = getIntent().getStringExtra("title");
        if (title!=null){
            mTitleBar.setTitle(title);
        }else{
            mTitleBar.setTitle("促销活动");
        }
        initView();
    }

    private void initView() {
        mViewHelper = new VaryViewHelper(mSalesPullScroll);

        /*initRefresh(mSalesPullScroll);
        mSalesPullScroll.setMode(PullToRefreshBase.Mode.BOTH);

        mSalesPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });*/

        mSalesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(datalist.get(i).getType().equals("5")){
                    intent = new Intent(SalesActivity.this,ActionActivityGroup.class);
                    intent.putExtra("activityId",datalist.get(i).getActivityId());
                    startActivity(intent);
                }else{
                    intent = new Intent(SalesActivity.this,ActionActivity.class);
                    intent.putExtra("activityId",datalist.get(i).getActivityId());
                    startActivity(intent);
                }

            }
        });

        adapter = new SalesAdapter();
        mSalesListview.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    /**
     * 获取中部轮播、下部轮播和底部活动列表
     */
    private void loadData(int isFirst) {
        if (PublicFinal.FIRST == isFirst){
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<HomeActivityBean> httpUtil = new AsyncHttpUtil<>(this, HomeActivityBean.class, new IUpdateUI<HomeActivityBean>() {
            @Override
            public void updata(HomeActivityBean bean) {
                if (bean.getRepCode().equals("00")) {
                    datalist.addAll(bean.getListData());
                    adapter.setData(datalist);
                    mViewHelper.showDataView();
                }else{
                    mViewHelper.showErrorView(new OnErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mViewHelper.showErrorView(new OnErrorListener());
            }
            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getHomeActivity, L_RequestParams.getHomeActivity(UserInfoUtils.
                getId(this), ""), false);
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST);
        }
    }
}
