package com.bikejoy.testdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2019/10/31.
 * 待审核APP
 */

public class MemberApplyFragment extends BaseFragment {


    @BindView(R.id.rv_customer)
    RecyclerView mRvCustomer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private MemberApplyAdapter adapter;
    private List<MemberBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_apply, container, false);
        EventBus.getDefault().register(this);
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

                getList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                getList(PublicFinal.TWO, true);
            }
        });

        adapter = new MemberApplyAdapter(getActivity());
        adapter.setData(list);
        adapter.setListener(new MemberApplyAdapter.OnCustomerListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent(getActivity(), MemberInformationActivity.class);
                intent.putExtra("customer", list.get(i));
                startActivity(intent);
            }

            @Override
            public void onYes(int i, MemberBean bean) {

            }

            @Override
            public void onNo(int i, MemberBean bean) {

            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRvCustomer.setLayoutManager(llm);

        mRvCustomer.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRvCustomer.setAdapter(adapter);
    }

    /**
     * 获取客户列表
     */
    private void getList(int isFirst, boolean isDialog) {
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
        httpUtil.post(M_Url.getList, L_RequestParams.getList(page + "", ""), isDialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getList(PublicFinal.FIRST, false);
        }
    }
}
