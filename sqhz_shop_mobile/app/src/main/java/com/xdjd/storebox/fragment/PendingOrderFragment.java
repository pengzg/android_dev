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
import com.xdjd.storebox.activity.PendingOrderDetailActivity;
import com.xdjd.storebox.adapter.PendingOrderAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.PendingOrderBean;
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
 * Created by freestyle_hong on 2017/1/10.
 */

public class PendingOrderFragment extends BaseFragment implements PendingOrderAdapter.pendingOrderListener{

    @BindView(R.id.listview)
    PullToRefreshListView listview;
    private PendingOrderAdapter adapter;
    private VaryViewHelper mVaryViewHelper = null;
    private List<PendingOrderBean> orderList = new ArrayList<PendingOrderBean>();
    private int pageNo = 1;
    private int mFlag = 0;
    private String searchKey = "";
    public  void setData(String searchKey )
    {
        this.searchKey = searchKey;
        Log.e("关键字",this.searchKey);
        mFlag = 1;
        pageNo = 1;
        orderList.clear();
        adapter.notifyDataSetChanged();
        loadData(PublicFinal.FIRST);
    }

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
        adapter = new PendingOrderAdapter(this,pageNo);
        listview.setAdapter(adapter);
        initView() ;
    }



    @Override
    public void item(String  i) {
        Intent intent = new Intent(getActivity(), PendingOrderDetailActivity.class);
        intent.putExtra("orderId",i) ;
        startActivity(intent);
    }

    /*上下拉刷新数据*/
    private void initView(){
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
        adapter = new PendingOrderAdapter(this,pageNo) ;
        listview.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    /*全部订单请求接口*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<PendingOrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), PendingOrderBean.class, new IUpdateUI<PendingOrderBean>() {
            @Override
            public void updata(PendingOrderBean bean) {
                if (bean.getRepCode().equals("00")){
                    if (null!=bean.getListData() && bean.getListData().size()>0){
                        orderList.addAll(bean.getListData());
                        adapter.setData(orderList);
                        mVaryViewHelper.showDataView();
                    }else{
                        if (mFlag == 2) {
                            showToast("没有更多数据了！");
                            pageNo--;
                        } else if (mFlag == 0 || mFlag == 1) {
                            if ("".equals(searchKey) || searchKey == null){
                                mVaryViewHelper.showEmptyView("还没有待处理订单，再等等吧!");
                            }else {
                                mVaryViewHelper.showEmptyView("没有搜索结果!");
                                showToast("没有搜索结果!");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();

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

        httpUtil.post(M_Url.PendingOrderList , L_RequestParams.GetPendingOrderList(
                UserInfoUtils.getId(getActivity()),"1",String.valueOf(pageNo),searchKey ),false);
    }

}
