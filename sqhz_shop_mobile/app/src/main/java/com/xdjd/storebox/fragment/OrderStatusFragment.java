package com.xdjd.storebox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.OrderDetailActivity;
import com.xdjd.storebox.adapter.OrderStatusAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.OrderStatusBean;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/10.
 */

public class OrderStatusFragment extends BaseFragment {
    @BindView(R.id.orderStatus_listview)
    NoScrollListView orderStatusListview;
    private OrderStatusAdapter adapter;
    private String OrderId;
    private VaryViewHelper mVaryViewHelper = null;
    private List<OrderStatusBean> statusBeenList = new ArrayList<>();

    /*public OrderStatusFragment(String orderId) {
        OrderId = orderId;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mVaryViewHelper = new VaryViewHelper(orderStatusListview);
        adapter = new OrderStatusAdapter();
        orderStatusListview.setAdapter(adapter);
        OrderDetailActivity activity = (OrderDetailActivity)getActivity();
        OrderId = activity.getFlag();
        GetOrderStatus(UserInfoUtils.getId(getActivity()), OrderId);
    }



    /*订单状态接口*/
    private void GetOrderStatus(String uid, String orderId) {
        AsyncHttpUtil<OrderStatusBean> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderStatusBean.class, new IUpdateUI<OrderStatusBean>() {
            @Override
            public void updata(OrderStatusBean bean) {
                if (bean.getRepCode().equals("00")) {
                    mVaryViewHelper.showDataView();
                    statusBeenList.clear();
                    statusBeenList.addAll(bean.getListData());
                    if (statusBeenList == null || statusBeenList.size() == 0) {
                        Log.e("tag", "空地址");
                        mVaryViewHelper.showEmptyView();
                    }
                    adapter.setData(statusBeenList);
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.GetOrderStatus, L_RequestParams.GetOrderStatus(uid, orderId), true);
    }

}
