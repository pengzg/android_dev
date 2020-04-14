package com.bikejoy.testdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/18
 *     desc   : 会员订单列表
 *     version: 1.0
 * </pre>
 */

@SuppressLint("ValidFragment")
public class MemberOrderListFragment extends BaseFragment {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;

    private final int indexType;

    private OrderListAdapter adapter;
    private List<OrderInfo> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private MemberBean customer;//传递过来的客户信息
    Unbinder unbinder;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    public MemberOrderListFragment(int indexType, MemberBean customer) {
        this.indexType = indexType;
        this.customer = customer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

        mVaryViewHelper = new VaryViewHelper(mLvPull);
        mErrorListener = new MyErrorListener();

        initRefresh(mLvPull);
        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);
        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderList(PublicFinal.FIRST,false,"");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryOrderList(PublicFinal.TWO,true,"");
            }
        });

        adapter = new OrderListAdapter();
        adapter.setData(list);
        adapter.setType("1");
        adapter.setListener(new OrderListAdapter.OnOrderListListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderInfo", list.get(i));
                startActivity(intent);
            }
        });
        mLvPull.setAdapter(adapter);

        queryOrderList(PublicFinal.FIRST,false,"");
    }

    /**
     * 获取订单列表
     */
    private void queryOrderList(int isFirst,boolean isDialog,String searchStr) {
        if (isFirst == PublicFinal.FIRST){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<OrderInfo> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderInfo.class, new IUpdateUI<OrderInfo>() {
            @Override
            public void updata(OrderInfo jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    OrderInfo bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    queryOrderList(PublicFinal.FIRST, false,"");
                                }
                            });
                        } else {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getDesc(),mErrorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(),mErrorListener);
            }

            @Override
            public void finish() {
                if (mLvPull!=null){
                    mLvPull.onRefreshComplete();
                }
            }
        });
        httpUtil.post(M_Url.queryOrderList, L_RequestParams.queryOrderList("",customer.getMb_id(),"2","",
                "", page + "", "","","","",searchStr,"",""), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            queryOrderList(PublicFinal.FIRST,false,"");
        }
    }

    public void searchOrder(String searchStr){
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryOrderList(PublicFinal.FIRST,false,searchStr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
