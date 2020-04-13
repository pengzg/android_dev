package com.xdjd.storebox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.OrderDetailActivity;
import com.xdjd.storebox.adapter.OrderListAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.OrderBean;
import com.xdjd.storebox.bean.OrderListBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预售订单
 * Created by freestyle_hong on 2016/12/1.
 */

public class OrderPresellFragment extends BaseFragment implements OrderListAdapter.AllOrderListener{
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    private OrderListAdapter adapter;
    private VaryViewHelper mVaryViewHelper = null;
    private List<OrderListBean> orderList = new ArrayList<OrderListBean>();
    private int pageNo = 1;
    private int mFlag = 0;
    private String orderStatus = "";
    //private int isFirst ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_all, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mVaryViewHelper = new VaryViewHelper(listview);
        initView() ;
    }

   /*上下拉刷新数据*/
    private void initView(){
        orderStatus = getArguments().getString("orderStatus","0");
        Log.e("or",orderStatus);
        if(orderStatus.equals("0")){
            orderStatus = " ";
            Log.e("or1",orderStatus);
        }
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(listview);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                orderList.clear();
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
        adapter = new OrderListAdapter(this,pageNo) ;
        listview.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    @Override
    public void item(int i) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("orderId",orderList.get(i).getOrderId()) ;
        intent.putExtra("btnFlag","2");
        startActivity(intent);
    }


    /*全部订单请求接口*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<OrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderBean.class, new IUpdateUI<OrderBean>() {
            @Override
            public void updata(OrderBean bean) {
                if (bean.getRepCode().equals("00")){
                    if (null!=bean.getList() && bean.getList().size()>0){
                        if (mFlag == 0 || mFlag == 1){
                        }
                        orderList.addAll(bean.getList());
                        adapter.setData(orderList);
                        adapter.notifyDataSetChanged();
                        mVaryViewHelper.showDataView();
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了！");
                            pageNo--;
                        }else{
                            mVaryViewHelper.showEmptyView("还没有订单，快去下单吧！");
                        }
                    }
                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                //mVaryViewHelper.showErrorView(new ActionActivity.OnErrorListener());
            }
            @Override
            public void finish() {
                listview.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.GetOrderList, L_RequestParams.GetOrderList(
                UserInfoUtils.getId(getActivity()) ,String.valueOf(pageNo),orderStatus,"Y"),false);
    }

}

