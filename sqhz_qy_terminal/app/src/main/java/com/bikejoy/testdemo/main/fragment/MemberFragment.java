package com.bikejoy.testdemo.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 会员
 *     version: 1.0
 * </pre>
 */

public class MemberFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.rv_customer)
    RecyclerView mRvCustomer;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.tv_day_num)
    TextView mTvDayNum;
    @BindView(R.id.tv_week_num)
    TextView mTvWeekNum;
    @BindView(R.id.tv_month_num)
    TextView mTvMonthNum;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private MemberListAdapter adapter;
    private List<MemberBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

        mVaryViewHelper = new VaryViewHelper(mLlMain);
        mErrorListener = new MyErrorListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                customerList(PublicFinal.TWO, false);
                queryMemberNum();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                customerList(PublicFinal.TWO, true);
                queryMemberNum();
            }
        });

        adapter = new MemberListAdapter(getActivity());
        adapter.setData(list);
        adapter.setListener(new MemberListAdapter.OnCustomerListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent(getActivity(), MemberInformationActivity.class);
                intent.putExtra("customer", list.get(i));
                startActivity(intent);
            }

            @Override
            public void onAddNote(int i, MemberBean bean) {

            }

            @Override
            public void onEditMember(int i, MemberBean bean) {
                Intent intent = new Intent(getActivity(), UpdateMemberActivity.class);
                intent.putExtra("customer", bean);
                startActivity(intent);
            }

            @Override
            public void onAllot(int i, MemberBean bean) {

            }

            @Override
            public void onReset(int i, MemberBean bean) {

            }

            @Override
            public void onDelay(int i, MemberBean bean) {

            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRvCustomer.setLayoutManager(llm);
        mRvCustomer.setAdapter(adapter);

        customerList(PublicFinal.FIRST, false);
        queryMemberNum();
    }

    private ScrollView scrollView;

    /**
     * 获取客户列表
     */
    private void customerList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<MemberBean> httpUtil = new AsyncHttpUtil<>(getActivity(), MemberBean.class, new IUpdateUI<MemberBean>() {
            @Override
            public void updata(MemberBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    MemberBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();

                        SpannableString span = new SpannableString("总数量:" + bean.getTotal());
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ED3A36"));
                        span.setSpan(colorSpan, 4, span.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        mTvTotalNum.setText(span);
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView();
                        } else {
                            page--;
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getDesc(), mErrorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(), mErrorListener);
            }

            @Override
            public void finish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.setNoMoreData(false);
                }
            }
        });
        httpUtil.post(M_Url.customerList, L_RequestParams.customerList(page + "", mEtSearch.getText().toString()), isDialog);
    }

    @OnClick({R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                customerList(PublicFinal.FIRST, false);
                queryMemberNum();
                break;
        }
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            customerList(PublicFinal.FIRST, false);
            queryMemberNum();
        }
    }

    private void queryMemberNum() {
        AsyncHttpUtil<ReportMemberCountBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ReportMemberCountBean.class, new IUpdateUI<ReportMemberCountBean>() {
            @Override
            public void updata(ReportMemberCountBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    ReportMemberCountBean bean = jsonBean.getData();
                    //                    mTvTotalNum.setText("总数量:" + bean.getTotal_num());
                    SpannableString span = new SpannableString("当天:" + bean.getDay_num());
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ED3A36"));
                    span.setSpan(colorSpan, 3, span.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    mTvDayNum.setText(span);

                    SpannableString span1 = new SpannableString("本周:" + bean.getWeek_num());
                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#ED3A36"));
                    span1.setSpan(colorSpan1, 3, span1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    mTvWeekNum.setText(span1);

                    SpannableString span2 = new SpannableString("本月:" + bean.getMonth_num());
                    ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#ED3A36"));
                    span2.setSpan(colorSpan2, 3, span2.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    mTvMonthNum.setText(span2);

                    //                    mTvDayNum.setText("当天:" + bean.getDay_num());
                    //                    mTvWeekNum.setText("本周:" + bean.getWeek_num());
                    //                    mTvMonthNum.setText("本月:" + bean.getMonth_num());
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getDesc(), mErrorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.queryMemberNum, L_RequestParams.queryMemberNum(), false);
    }

    //    @Override
    //    public void onRefresh() {
    //        // 下拉刷新
    //        mSwipeLayout.setEnabled(false);
    //        adapter.setEnableLoadMore(false);
    //    }
    //
    //    @Override
    //    public void onLoadMoreRequested() {
    //        mSwipeLayout.setEnabled(false);
    //        adapter.loadMoreEnd(false);
    //        mSwipeLayout.setEnabled(true);
    //    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
