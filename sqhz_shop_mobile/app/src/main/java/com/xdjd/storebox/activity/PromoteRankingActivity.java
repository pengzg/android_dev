package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.PromoteRankAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.PromoteRankBean;
import com.xdjd.storebox.bean.PromoteStatisticBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
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
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PromoteRankingActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.one_week)
    TextView oneWeek;
    @BindView(R.id.down1)
    ImageView down1;
    @BindView(R.id.linear_week)
    LinearLayout linearWeek;
    @BindView(R.id.one_month)
    TextView oneMonth;
    @BindView(R.id.down2)
    ImageView down2;
    @BindView(R.id.linear_month)
    LinearLayout linearMonth;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.down3)
    ImageView down3;
    @BindView(R.id.linear_total)
    LinearLayout linearTotal;
    @BindView(R.id.user_num)
    TextView userNum;
    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.total_account)
    TextView totalAccount;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.statistic_listView)
    PullToRefreshListView statisticListView;
    private PromoteRankAdapter adapter;
    private String type;//统计时间段选择

    private VaryViewHelper mVaryViewHelper = null;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<PromoteRankBean> list = new ArrayList<PromoteRankBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_ranking);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("推广排名");
        down1.setVisibility(View.GONE);
        down2.setVisibility(View.GONE);
        oneWeek.setTextColor(UIUtils.getColor(R.color.color_666666));
        oneMonth.setTextColor(UIUtils.getColor(R.color.color_666666));
        PromoteStatistic(UserInfoUtils.getId(PromoteRankingActivity.this), "3");//默认显示累计
        total.setTextColor(UIUtils.getColor(R.color.colorPrimary));
        linearTotal.setSelected(true);
        down3.setVisibility(View.VISIBLE);
        mVaryViewHelper = new VaryViewHelper(statisticListView);
        adapter = new PromoteRankAdapter(pageNo);
        statisticListView.setAdapter(adapter);
        initView();
    }

    /*上下拉刷新数据*/
    private void initView() {
        statisticListView.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(statisticListView);
        statisticListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO);
            }
        });
        adapter = new PromoteRankAdapter(pageNo);
        statisticListView.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    private void loadData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<PromoteRankBean> httpUtil = new AsyncHttpUtil<>(this, PromoteRankBean.class, new IUpdateUI<PromoteRankBean>() {
            @Override
            public void updata(PromoteRankBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getDataList() && bean.getDataList().size() > 0) {
                        if (mFlag == 0 || mFlag == 1) {

                        }
                        list.addAll(bean.getDataList());
                        adapter.setData(list);
                    } else {
                        if (mFlag == 2) {
                            showToast("没有更多数据了！");
                            pageNo--;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    mVaryViewHelper.showDataView();
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                //mVaryViewHelper.showErrorView(new ActionActivity.OnErrorListener());
            }

            @Override
            public void finish() {
                statisticListView.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.PromoteRankList, L_RequestParams.PromoteRank(UserInfoUtils.getId(PromoteRankingActivity.this), pageNo), false);
    }

    @OnClick({R.id.linear_week, R.id.linear_month, R.id.linear_total})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_week:
                type = "1";
                down1.setVisibility(View.VISIBLE);
                down2.setVisibility(View.GONE);
                down3.setVisibility(View.GONE);
                linearMonth.setSelected(false);
                linearWeek.setSelected(true);
                linearTotal.setSelected(false);
                oneWeek.setTextColor(UIUtils.getColor(R.color.colorPrimary));
                oneMonth.setTextColor(UIUtils.getColor(R.color.color_666666));
                total.setTextColor(UIUtils.getColor(R.color.color_666666));
                PromoteStatistic(UserInfoUtils.getId(PromoteRankingActivity.this), type);
                break;
            case R.id.linear_month:
                type = "2";
                down2.setVisibility(View.VISIBLE);
                down1.setVisibility(View.GONE);
                down3.setVisibility(View.GONE);
                linearMonth.setSelected(true);
                linearWeek.setSelected(false);
                linearTotal.setSelected(false);
                oneWeek.setTextColor(UIUtils.getColor(R.color.color_666666));
                oneMonth.setTextColor(UIUtils.getColor(R.color.colorPrimary));
                total.setTextColor(UIUtils.getColor(R.color.color_666666));
                PromoteStatistic(UserInfoUtils.getId(PromoteRankingActivity.this), type);
                break;
            case R.id.linear_total:
                type = "3";
                down1.setVisibility(View.GONE);
                down2.setVisibility(View.GONE);
                down3.setVisibility(View.VISIBLE);
                linearMonth.setSelected(false);
                linearWeek.setSelected(false);
                linearTotal.setSelected(true);
                oneWeek.setTextColor(UIUtils.getColor(R.color.color_666666));
                oneMonth.setTextColor(UIUtils.getColor(R.color.color_666666));
                total.setTextColor(UIUtils.getColor(R.color.colorPrimary));
                PromoteStatistic(UserInfoUtils.getId(PromoteRankingActivity.this), type);
                break;
        }
    }


    /*推广统计*/
    private void PromoteStatistic(String uid, String type) {
        AsyncHttpUtil<PromoteStatisticBean> httpUtil = new AsyncHttpUtil<>(this, PromoteStatisticBean.class, new IUpdateUI<PromoteStatisticBean>() {
            @Override
            public void updata(PromoteStatisticBean bean) {
                if (bean.getRepCode().equals("00")) {
                    userNum.setText(bean.getSpreadNum());
                    orderNum.setText(bean.getOrderCount());
                    totalAccount.setText(bean.getTotalAmount());
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

            }
        });
        httpUtil.post(M_Url.PromoteStatistic, L_RequestParams.PromoteStatistic(uid, "1", type), true);
    }
}
