package com.bikejoy.testdemo.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.event.UpdateStorehouseBoxEvent;
import com.bikejoy.testdemo.event.UpdateStorehouseInfoEvent;
import com.bikejoy.testdemo.popup.SelectStoreStatusPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/4/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class StorehouseFragment extends BaseFragment implements SelectStoreStatusPopup.ItemOnListener {
    Unbinder unbinder;

    @BindView(R.id.my_coupon_convert_tv)
    TextView mMyCouponConvertTv;
    @BindView(R.id.rl_scan)
    RelativeLayout mRlScan;
    @BindView(R.id.lv_list)
    ListView mLvList;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_store_state)
    TextView mTvStoreState;
    @BindView(R.id.ll_select_category)
    LinearLayout mLlSelectCategory;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.tv_empty_num)
    TextView mTvEmptyNum;
    @BindView(R.id.tv_full_num)
    TextView mTvFullNum;
    @BindView(R.id.tv_alert_state)
    TextView mTvAlertState;
    @BindView(R.id.ll_select_alert)
    LinearLayout mLlSelectAlert;
    @BindView(R.id.btn_addstore)
    Button mBtnAddstore;

    private StorehouseAdapter adapter;
    private List<StoreListBean> list = new ArrayList();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper = null;
    private MyErrorListener mErrorListener;

    private SelectStoreStatusPopup popupStoreStatus;
    private List<StatusBean> listStatus = new ArrayList<>();

    private SelectStoreStatusPopup popupStoreAlertStatus;
    //预警类型集合
    private List<StatusBean> listAlertStatus = new ArrayList<>();

    private int storeState = 0;//柜子在线状态
    private int storeAlertState = 0;//柜子预警状态

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storehouse, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mEtSearch.setHint("请输入编号、名称或地址查询");

        //0全部 1已铺货 2.未铺货
        listStatus.add(new StatusBean(0, "全部"));
        listStatus.add(new StatusBean(1, "离线"));
        listStatus.add(new StatusBean(2, "在线"));
        //
        listAlertStatus.add(new StatusBean(0, "全部"));
        listAlertStatus.add(new StatusBean(1, "低于预警"));
        listAlertStatus.add(new StatusBean(2, "高于预警"));

        adapter = new StorehouseAdapter();
        adapter.setData(list);
        mLvList.setAdapter(adapter);

        mVaryViewHelper = new VaryViewHelper(mRefreshLayout);
        mErrorListener = new MyErrorListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryStoreList(PublicFinal.TWO, false);
                getTotalInventory();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                queryStoreList(PublicFinal.TWO, true);
            }
        });

        adapter.setListener(new StorehouseAdapter.OnStoreListListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent();
                intent.putExtra("storeBean", list.get(i));
                //1 格子柜  2重力柜 3户外机械柜
                if ("3".equals(list.get(i).getBs_box_type()) || "4".equals(list.get(i).getBs_box_type())) {
                    intent.setClass(getActivity(), StorehouseThreeDetailActivity.class);
                } else {
                    intent.setClass(getActivity(), StorehouseDetailActivity.class);
                }
                startActivity(intent);
            }
        });

        mLlSelectCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlSelectCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initTypePopup();
            }
        });
        mLlSelectAlert.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlSelectAlert.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initAlertPopup();
            }
        });

        queryStoreList(PublicFinal.FIRST, false);
        getTotalInventory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_scan, R.id.ll_search, R.id.ll_select_category, R.id.ll_select_alert,R.id.btn_addstore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_scan:
                startActivity(StorehouseDetailActivity.class);
                break;
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryStoreList(PublicFinal.FIRST, false);
                break;
            case R.id.ll_select_category:
                showTypePopup();
                break;
            case R.id.ll_select_alert:
                showAlertPopup();
                break;
            case R.id.btn_addstore://添加柜子
                startActivity(AddStoreActivity.class);
                break;
        }
    }

    /**
     * 获取柜子列表
     */
    private void queryStoreList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<StoreListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), StoreListBean.class, new IUpdateUI<StoreListBean>() {
            @Override
            public void updata(StoreListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    if (jsonBean.getData() != null && jsonBean.getData().size() > 0) {
                        list.addAll(jsonBean.getData());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView("请联系管理员分配柜子", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    queryStoreList(PublicFinal.FIRST, false);
                                }
                            });
                        } else {
                            page--;
                            showToast("数据全部加载完毕");
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
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
            }
        });
        httpUtil.post(M_Url.queryStoreList, L_RequestParams.queryStoreList("", "", page + "",
                mEtSearch.getText().toString(), storeState, storeAlertState), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryStoreList(PublicFinal.FIRST, false);
            getTotalInventory();
        }
    }

    private void getTotalInventory() {
        AsyncHttpUtil<TotalInventoryBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TotalInventoryBean.class, new IUpdateUI<TotalInventoryBean>() {
            @Override
            public void updata(TotalInventoryBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    TotalInventoryBean bean = jsonBean.getData();
                    if (bean != null) {
                        mTvTotalNum.setText("总容量:" + bean.getTotal_num());
                        mTvEmptyNum.setText("需补货:" + bean.getEmpty_num_total());
                        mTvFullNum.setText("剩余数量:" + bean.getFull_num_total());
                    }
                } else {
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getTotalInventory, L_RequestParams.getTotalInventory(), false);
    }

    public void onEventMainThread(UpdateStorehouseBoxEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryStoreList(PublicFinal.FIRST, false);
        getTotalInventory();
    }

    public void onEventMainThread(UpdateStorehouseInfoEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryStoreList(PublicFinal.FIRST, false);
        getTotalInventory();
    }

    private void initTypePopup() {
        popupStoreStatus = new SelectStoreStatusPopup(getActivity(), mLlSelectCategory, this);
        popupStoreStatus.setData(listStatus);
    }

    private void showTypePopup() {
        popupStoreStatus.setData(listStatus);
        popupStoreStatus.showAsDropDown(mLlSelectCategory, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onItem(int i) {
        popupStoreStatus.dismiss();
        storeState = listStatus.get(i).getType();
        mTvStoreState.setText(listStatus.get(i).getTypeName());

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryStoreList(PublicFinal.FIRST, false);
        getTotalInventory();
    }

    private void initAlertPopup() {
        popupStoreAlertStatus = new SelectStoreStatusPopup(getActivity(), mLlSelectCategory, new SelectStoreStatusPopup.ItemOnListener() {
            @Override
            public void onItem(int i) {
                popupStoreAlertStatus.dismiss();
                storeAlertState = listStatus.get(i).getType();
                mTvAlertState.setText(listAlertStatus.get(i).getTypeName());

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryStoreList(PublicFinal.FIRST, false);
                getTotalInventory();
            }
        });
        popupStoreAlertStatus.setData(listAlertStatus);
    }

    private void showAlertPopup() {
        popupStoreAlertStatus.setData(listAlertStatus);
        popupStoreAlertStatus.showAsDropDown(mLlSelectAlert, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
