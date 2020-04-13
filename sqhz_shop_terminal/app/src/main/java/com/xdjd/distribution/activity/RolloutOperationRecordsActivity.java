package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RolloutOperationRecordsAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.SaleOrderByPhOrderBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/10
 *     desc   : 铺货操作记录界面
 *     version: 1.0
 * </pre>
 */

public class RolloutOperationRecordsActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    PullToRefreshExpandableListView mPullScroll;

    RolloutOperationRecordsAdapter adapter;
    private List<SaleOrderByPhOrderBean> list;
    private VaryViewHelper mVaryViewHelper;
    private String orderId;
    private String type;//type:1撤货单；type:2 销售单

    private boolean isUnfold = false;//是否全部展开

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_operation_records;
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
        orderId = getIntent().getStringExtra("orderId");
        type = getIntent().getStringExtra("type");
        mTitleBar.leftBack(this);
        if ("2".equals(type)) {
            mTitleBar.setTitle("铺货销售记录");
        } else {
            mTitleBar.setTitle("撤货记录");
        }
        mTitleBar.setRightText("收起全部订单");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnfold) {
                    isUnfold = false;
                    mTitleBar.setRightText(UIUtils.getString(R.string.pack_up_all_order));
                } else {
                    isUnfold = true;
                    mTitleBar.setRightText(UIUtils.getString(R.string.unfold_all_order));
                }
                if (list == null){
                    return;
                }
                //遍历所有group,将所有项设置成默认展开
                int groupCount = list.size();
                for (int i = 0; i < groupCount; i++) {
                    if (isUnfold) {
                        mPullScroll.getRefreshableView().collapseGroup(i);
                    } else {
                        mPullScroll.getRefreshableView().expandGroup(i);
                    }
                }
            }
        });

        mVaryViewHelper = new VaryViewHelper(mPullScroll);

        adapter = new RolloutOperationRecordsAdapter();
        adapter.setType(type);
        mPullScroll.getRefreshableView().setAdapter(adapter);
        mPullScroll.getRefreshableView().setGroupIndicator(null);//将一级菜单头部的图标去掉
        mPullScroll.getRefreshableView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                /*isUnfold = true;
                int groupCount = list.size();
                for (int k= 0; k < groupCount; k++) {
                    if (!mPullScroll.getRefreshableView().isGroupExpanded(k)) {
                        isUnfold = false;
                        break;
                    }
                }
                if (isUnfold) {
                    mTitleBar.setRightText(UIUtils.getString(R.string.pack_up_all_order));
                } else {
                    mTitleBar.setRightText(UIUtils.getString(R.string.unfold_all_order));
                }*/
                return false;
            }
        });

        phOrderBySaleOrder();
    }

    private void phOrderBySaleOrder() {
        mVaryViewHelper.showLoadingView();
        AsyncHttpUtil<SaleOrderByPhOrderBean> httpUtil = new AsyncHttpUtil<>(this, SaleOrderByPhOrderBean.class, new IUpdateUI<SaleOrderByPhOrderBean>() {
            @Override
            public void updata(SaleOrderByPhOrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        mVaryViewHelper.showDataView();
                        list = jsonBean.getListData();
                        adapter.setData(list);

                        //遍历所有group,将所有项设置成默认展开
                        int groupCount = list.size();
                        for (int i = 0; i < groupCount; i++) {
                            mPullScroll.getRefreshableView().expandGroup(i);
                        }
                    } else {
                        mVaryViewHelper.showEmptyView();
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getRepMsg(), new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(), new MyErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.saleOrderByPhOrder, L_RequestParams.
                saleOrderByPhOrder(orderId, type), false);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phOrderBySaleOrder();
        }
    }
}
