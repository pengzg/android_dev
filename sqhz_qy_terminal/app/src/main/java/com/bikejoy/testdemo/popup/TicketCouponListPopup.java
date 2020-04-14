package com.bikejoy.testdemo.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/22.
 * 赠送水票列表(电子券)
 */

public class TicketCouponListPopup extends PopupWindow{
    private Context context;
    private OnTicketListener listener;
    private View view;

    private LinearLayout popLayout,llClose;
    private ListView lvList;
    private TextView tvGrant;
    private SmartRefreshLayout mRefreshLayout;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    private List<TicketListBean> list;
    private TicketCheckListAdapter adapter;

    private int page = 1;
    private int mFlag = 0;

    private PopupWindow pw;

    private UserBean userBean;

    public void showPopup(View view,PopupWindow pw,UserBean userBean){
        this.pw = pw;
        this.userBean = userBean;
        page = 1;
        mFlag = 0;
        list.clear();
        //adapter.setCouponId("");
        adapter.notifyDataSetChanged();
        queryTicketListByMember(PublicFinal.FIRST,true);
    }

    public TicketCouponListPopup(final Context context, final OnTicketListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_ticket_coupon,null);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        this.setTouchable(true);
        this.setOutsideTouchable(true);

        list = new ArrayList<>();

        popLayout = (LinearLayout)view.findViewById(R.id.pop_layout);
        lvList = (ListView)view.findViewById(R.id.lv_list);
        tvGrant = view.findViewById(R.id.tv_grant);
        llClose = view.findViewById(R.id.ll_close);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);

        mVaryViewHelper = new VaryViewHelper(popLayout);

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.setTicketId("");
                adapter.setCheckId("");
                adapter.notifyDataSetChanged();
                queryTicketListByMember(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                queryTicketListByMember(PublicFinal.TWO, true);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        tvGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(adapter.ticketId)){
                    UIUtils.Toast("请选择水票");
                    return;
                }

                TicketListBean ticketBean = null;
                for (TicketListBean bean:list){
                    if (adapter.ticketId.equals(bean.getTm_id())){
                        ticketBean = bean;
                        break;
                    }
                }

                if (ticketBean==null){
                    UIUtils.Toast("请选择水票");
                    return;
                }

                DialogUtil.showEditNumDialog(context, "提示", "请输入赠送水票数量", "1",ticketBean.getTm_available_num(),"已超出限制数量",
                        "赠送", "取消", new DialogUtil.MyCustomDialogListener() {
                    @Override
                    public void ok(Dialog dialog, String str) {
                        for (TicketListBean bean:list){
                            if (adapter.ticketId.equals(bean.getTm_id())){
                                listener.onItemTicket(bean,str);
                                break;
                            }
                        }
                    }
                    @Override
                    public void no() {
                    }
                });
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        adapter = new TicketCheckListAdapter();
        lvList.setAdapter(adapter);
    }

    public interface OnTicketListener{
        void onItemTicket(TicketListBean ticketBean, String num);
    }

    /**
     * 获取赠送优惠券列表
     */
    private void queryTicketListByMember(int isFirst,boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<TicketListBean> httpUtil = new AsyncHttpUtil<>((Activity) context, TicketListBean.class, new IUpdateUI<TicketListBean>() {
            @Override
            public void updata(TicketListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    TicketListBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        List<TicketListBean> list1 = bean.getRows();
                        for (TicketListBean bean1 : list1) {
                            BigDecimal total;
                            BigDecimal available;
                            if (!TextUtils.isEmpty(bean1.getTm_total_num())) {
                                total = new BigDecimal(bean1.getTm_total_num());
                            } else {
                                total = BigDecimal.ZERO;
                            }
                            if (!TextUtils.isEmpty(bean1.getTm_available_num())) {
                                available = new BigDecimal(bean1.getTm_available_num());
                            } else {
                                available = BigDecimal.ZERO;
                            }
                            bean1.setUse_num(total.subtract(available).toString());
                        }
                        list.addAll(list1);
                        adapter.setData(list);
                        pw.showAtLocation(view, Gravity.BOTTOM,0,0);
                        page = 1;
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            UIUtils.Toast("暂无数据");
                            mVaryViewHelper.showEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    page = 1;
                                    mFlag = 1;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    queryTicketListByMember(PublicFinal.FIRST, false);
                                }
                            });
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
                if (mRefreshLayout!=null){
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.setNoMoreData(false);
                }
            }
        });
        httpUtil.post(M_Url.queryTicketListByMember, L_RequestParams.queryTicketListByMember(userBean.getUserid(),page+""), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryTicketListByMember(PublicFinal.FIRST, false);
        }
    }

}
