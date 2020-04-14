package com.bikejoy.testdemo.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.view.EaseTitleBar;
import com.bikejoy.testdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/22.
 * 柜子查询选择popup
 */

public class StoreSearchPopup extends PopupWindow {
    private EaseTitleBar mTitleBar;
    private Context context;
    private OnStoreSearchListener listener;
    private View view;

    private LinearLayout popLayout,ll_search;
    private ListView lvList;
    private SmartRefreshLayout mRefreshLayout;
    private EditText mEtSearch;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    private List<StoreListBean> list;
    private StoreSearchListAdapter adapter;

    private int page = 1;
    private int mFlag = 0;

    private PopupWindow pw;

    public void showPopup(View view, PopupWindow pw) {
        this.pw = pw;
        page = 1;
        mFlag = 0;
        list.clear();
        adapter.notifyDataSetChanged();
        queryStoreList(PublicFinal.FIRST, true);
    }

    public StoreSearchPopup(final Context context, final OnStoreSearchListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_storer_list, null);
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

        mTitleBar = view.findViewById(R.id.title_bar);
        popLayout = (LinearLayout) view.findViewById(R.id.pop_layout);
        lvList = (ListView) view.findViewById(R.id.lv_list);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mEtSearch = view.findViewById(R.id.et_search);
        ll_search = view.findViewById(R.id.ll_search);

        mVaryViewHelper = new VaryViewHelper(popLayout);


        mTitleBar.setTitle("请选择柜子");

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout  refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryStoreList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout  refreshLayout) {
                page++;
                mFlag = 2;
                queryStoreList(PublicFinal.TWO, true);
            }
        });

//        view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                dismiss();
//                return true;
//            }
//        });

        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryStoreList(PublicFinal.FIRST, false);
            }
        });

        adapter = new StoreSearchListAdapter(new StoreSearchListAdapter.StorehouseListListener() {
            @Override
            public void onItemStore(int i) {
                if (listener!=null){
                    listener.onItem(list.get(i));
                    dismiss();
                }
            }
        });
        lvList.setAdapter(adapter);
    }

    public interface OnStoreSearchListener {
        void onItem(StoreListBean bean);
    }

    /**
     * 获取柜子列表
     */
    private void queryStoreList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<StoreListBean> httpUtil = new AsyncHttpUtil<>((Activity) context, StoreListBean.class, new IUpdateUI<StoreListBean>() {
            @Override
            public void updata(StoreListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    if (jsonBean.getData() != null && jsonBean.getData().size() > 0) {
                        list.addAll(jsonBean.getData());
                        adapter.setData(list);
                        pw.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                        page = 1;
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            UIUtils.Toast("暂无数据");
                            mVaryViewHelper.showEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    page = 1;
                                    mFlag = 1;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    queryStoreList(PublicFinal.FIRST, false);
                                }
                            });
                        } else {
                            page--;
                            UIUtils.Toast("数据全部加载完毕");
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
        httpUtil.post(M_Url.queryStoreList, L_RequestParams.queryStoreList("", "", page + "",
                mEtSearch.getText().toString(), 0, 0), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryStoreList(PublicFinal.FIRST, false);
        }
    }

}
