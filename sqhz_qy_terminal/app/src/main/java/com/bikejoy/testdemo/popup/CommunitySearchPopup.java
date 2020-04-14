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
 * 查询选择写字楼popup
 */

public class CommunitySearchPopup extends PopupWindow {
    private EaseTitleBar mTitleBar;
    private Context context;
    private OnCommunitySearchListener listener;
    private View view;

    private LinearLayout popLayout,ll_search;
    private ListView lvList;
    private SmartRefreshLayout mRefreshLayout;
    private EditText mEtSearch;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    private List<CommunityBean> list;
    private CommunitySearchListAdapter adapter;

    private int page = 1;
    private int mFlag = 0;

    private PopupWindow pw;

    public void showPopup(View view, PopupWindow pw) {
        this.pw = pw;
        page = 1;
        mFlag = 0;
        list.clear();
        adapter.notifyDataSetChanged();
        getCommunityList(PublicFinal.FIRST, true);
    }

    public CommunitySearchPopup(final Context context, final OnCommunitySearchListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_community_list, null);
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


        mTitleBar.setTitle("请选择配送员");

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout  refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getCommunityList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout  refreshLayout) {
                page++;
                mFlag = 2;
                getCommunityList(PublicFinal.TWO, true);
            }
        });
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
                getCommunityList(PublicFinal.FIRST, false);
            }
        });

        adapter = new CommunitySearchListAdapter(new CommunitySearchListAdapter.CommunityListListener() {
            @Override
            public void onItemStore(int i) {
                if (listener!=null){
                    listener.onItem(list.get(i), pw);
                }
            }
        });
        lvList.setAdapter(adapter);
    }

    public interface OnCommunitySearchListener {
        void onItem(CommunityBean bean, PopupWindow popup);
    }

    private void getCommunityList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<CommunityBean> httpUtil = new AsyncHttpUtil<>((Activity)context, CommunityBean.class,
                new IUpdateUI<CommunityBean>() {
            @Override
            public void updata(CommunityBean jsonBean) {
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
                            mVaryViewHelper.showEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pullUpUpdate();
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

        httpUtil.post(M_Url.getCommunityList, L_RequestParams.getCommunityList(
                "",page + "", mEtSearch.getText().toString()), isDialog);
    }

    /**
     * 下拉刷新列表数据
     */
    private void pullUpUpdate() {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        getCommunityList(PublicFinal.FIRST, false);
    }


    /**
     * 获取业务员列表
     */
   /* private void getMemberBaseWorkList(int isFirst, boolean isDialog){
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil httpStore = new AsyncHttpUtil<>((Activity) context, MemberWorkListBean.class, new IUpdateUI<MemberWorkListBean>() {
            @Override
            public void updata(MemberWorkListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {

                    if (jsonBean.getData() != null && jsonBean.getData().getRows()!=null && jsonBean.getData().getRows().size() > 0) {
                        list.addAll(jsonBean.getData().getRows());
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
                                    getMemberBaseWorkList(PublicFinal.FIRST, false);
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
        httpStore.post(M_Url.getMemberBaseWorkList, L_RequestParams.getMemberBaseWorkList(page+""), true);
    }*/

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getCommunityList(PublicFinal.FIRST, false);
        }
    }

}
