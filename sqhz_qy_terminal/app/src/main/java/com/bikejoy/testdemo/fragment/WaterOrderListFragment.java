package com.bikejoy.testdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.event.CreateOrderSuccessEvent;
import com.bikejoy.testdemo.popup.SelectStorePopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

@SuppressLint("ValidFragment")
public class WaterOrderListFragment extends BaseFragment {

    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;

    private VaryViewHelper helper;

    private WaterOrderListAdapter adapter;
    private List<WaterOrderBean> list = new ArrayList<>();

    Unbinder unbinder;
    private final int indexType;//订单类型

    private int page = 1;
    private int mFlag = 0;

    private MemberBean customer;//传递过来的客户信息

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;
    //仓库列表
    private List<StoreListBean> listStore;
    //仓库code
    private String storeCode;
    private String odId;

    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;
    private MemberInformationActivity activity;

    public WaterOrderListFragment(int indexType, MemberBean customer) {
        this.indexType = indexType;
        this.customer = customer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_water_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        activity = (MemberInformationActivity) getActivity();

        EventBus.getDefault().register(this);

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
                queryWaterVoteOrderList(PublicFinal.FIRST, false,"");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryWaterVoteOrderList(PublicFinal.TWO, true,"");
            }
        });

        adapter = new WaterOrderListAdapter();
        adapter.setData(list);
        mLvPull.setAdapter(adapter);

        adapter.setListener(new WaterOrderListAdapter.OnWaterOrderListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent(getActivity(), WaterVoteRecordsActivity.class);
                intent.putExtra("waterId", list.get(i).getOd_id());
                intent.putExtra("mbId",customer.getMb_id());//会员id
                startActivity(intent);
            }

            @Override
            public void onPlaceTheOrder(int i) {
                odId = list.get(i).getOd_id();
                if (listStore != null && listStore.size() > 0) {
                    if (listStore.size() == 1) {
                        storeCode = listStore.get(0).getBs_code();
                        Intent intent = new Intent(getActivity(), TicketUseActivity.class);
                        intent.putExtra("odId", odId);
                        intent.putExtra("storeCode", storeCode);
                        startActivity(intent);
                    } else {
                        showPwStore();
                    }
                } else {
                    queryMapStoreList(true);
                }
            }
        });

        initStorePopup();

        queryWaterVoteOrderList(PublicFinal.FIRST, false,"");
//        queryMapStoreList(false);
    }

    /**
     * 获取订单列表
     */
    private void queryWaterVoteOrderList(int isFirst, boolean isDialog,String searchStr) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<WaterOrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), WaterOrderBean.class, new IUpdateUI<WaterOrderBean>() {
            @Override
            public void updata(WaterOrderBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    WaterOrderBean bean = jsonBean.getData();
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
                                    queryWaterVoteOrderList(PublicFinal.FIRST, false,"");
                                }
                            });
                        } else {
                            page--;
                            showToast("没有更多数据了");
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
                if (mLvPull!=null){
                    mLvPull.onRefreshComplete();
                }
            }
        });
        httpUtil.post(M_Url.queryWaterVoteOrderList, L_RequestParams.
                queryWaterVoteOrderList(customer.getMb_id(), "", page + "",searchStr), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryWaterVoteOrderList(PublicFinal.FIRST, false,"");
        }
    }


    /**
     * 获取仓库列表
     */
    private void queryMapStoreList(final boolean isShowPop) {
        AsyncHttpUtil<StoreListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), StoreListBean.class, new IUpdateUI<StoreListBean>() {
            @Override
            public void updata(StoreListBean bean) {
                if ("200".equals(bean.getCode())) {
                    if (null != bean.getData() && bean.getData().size() > 0) {
                        listStore = bean.getData();
                        if (isShowPop){
                            if (listStore.size() == 1) {
                                storeCode = listStore.get(0).getBs_code();
                                Intent intent = new Intent(getActivity(), TicketUseActivity.class);
                                intent.putExtra("odId", odId);
                                intent.putExtra("storeCode", storeCode);
                                startActivity(intent);
                            } else {
                                showPwStore();
                            }
                        }
                    }else{
                        if (isShowPop){
                            DialogUtil.showCustomDialog(getActivity(),"提示",
                                    "没有仓库信息","确定",null,null);
                        }
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.queryMapStoreList, L_RequestParams.queryMapStoreList("1"), false);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(getActivity(), dm.heightPixels, new SelectStorePopup.OnStoreSelectListener() {
            @Override
            public void onItem(final int position) {
                storeCode = listStore.get(position).getBs_code();
                Intent intent = new Intent(getActivity(), TicketUseActivity.class);
                intent.putExtra("odId", odId);
                intent.putExtra("storeCode", storeCode);
                intent.putExtra("mbId",customer.getMb_id());
                startActivity(intent);
                popupStore.dismiss();
            }
        });
    }

    /**
     * 显示仓库popup
     */
    private void showPwStore() {
        popupStore.setData(listStore);
        popupStore.setId(storeCode);
        // 显示窗口
        popupStore.showAtLocation(activity.getRlMain(),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void onEventMainThread(CreateOrderSuccessEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryWaterVoteOrderList(PublicFinal.TWO, true,"");
    }

    public void searchOrder(String searchStr){
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
            queryWaterVoteOrderList(PublicFinal.FIRST,false,searchStr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
