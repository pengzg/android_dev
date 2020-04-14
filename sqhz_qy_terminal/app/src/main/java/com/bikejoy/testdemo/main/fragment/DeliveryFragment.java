package com.bikejoy.testdemo.main.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.view.itemdecoration.LinearItemDecoration;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.event.OrderListUpdateEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 配送任务
 *     version: 1.0
 * </pre>
 */

public class DeliveryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.tv4)
    TextView mTv4;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.lv_pull)
    ListView mLvPull;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private List<String> listTab = new ArrayList<>();

    private DeliveryListAdapter adapter;
    private List<DeliveryMainAndDetailBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper = null;
    private MyErrorListener mErrorListener;
    //发货状态(3:已发货;4:已签收;"":全部)
    private String odmState = "";//

    private UserBean mUserBean;

    private int checkedIndex = 0;//订单列表选中的index

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delivery_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 3;
        mUserBean = UserInfoUtils.getUser(getActivity());
        //        listTab.add("待配送");
        //        listTab.add("配送中");
        //        listTab.add("已完成");
        //        listTab.add("已关闭");
        //        mSwipeLayout.setOnRefreshListener(this);
        //        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //        adapter = new OrderListAdapter(list);
        //        adapter.setOnLoadMoreListener(this, mRvList);
        //        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //        mRvList.setAdapter(adapter);
        //        //添加Android自带的分割线
        //        mRvList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //                startActivity(OrderDetailActivity.class);
        //            }
        //        });
        mVaryViewHelper = new VaryViewHelper(mRefreshLayout);
        mErrorListener = new MyErrorListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryDeliveryList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                queryDeliveryList(PublicFinal.TWO, true);
            }
        });

        adapter = new DeliveryListAdapter(getActivity());
        adapter.setDataList(list);
        adapter.setListener(new DeliveryListAdapter.OnDeliveryListListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent(getActivity(), DeliveryOrderDetailActivity.class);
                intent.putExtra("odmId", list.get(i).getMainVO().getOdm_id());
                intent.putExtra("odmState", odmState);
                startActivity(intent);
            }

            @Override
            public void onConfirmDelivery(final int i) {
                DialogUtil.showCustomDialog(getActivity(), "提示", "是否已送达?",
                        "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                confirmDeliveryOrder(i);
                            }

                            @Override
                            public void no() {
                            }
                        });
            }

            @Override
            public void onScanPay(final int i) {
                DialogUtil.showCustomDialog(getActivity(), "提示", "是否扫码收款?",
                        "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                checkedIndex = i;
                                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                                intent.putExtra("type",CaptureActivity.TYPE01);
                                startActivityForResult(intent,10);
                            }

                            @Override
                            public void no() {
                            }
                        });
            }

            @Override
            public void onVerificationTicket(int i, DeliveryMainAndDetailBean orderBean) {

            }

            @Override
            public void onMapNavigation(int i, DeliveryMainAndDetailBean orderBean) {

            }

            @Override
            public void onPhone(int i, DeliveryMainAndDetailBean orderBean) {

            }
        });
        mRvList.addItemDecoration(new LinearItemDecoration(10,
                10, true));
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvList.setAdapter(adapter);

        queryDeliveryList(PublicFinal.FIRST, false);
    }

    /**
     * 获取配送订单列表
     */
    private void queryDeliveryList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<DeliveryMainAndDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(), DeliveryMainAndDetailBean.class, new IUpdateUI<DeliveryMainAndDetailBean>() {
            @Override
            public void updata(DeliveryMainAndDetailBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    DeliveryMainAndDetailBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setDataList(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView();
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
                if (mRefreshLayout!=null){
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.setNoMoreData(false);
                }
            }
        });
        httpUtil.post(M_Url.queryDeliveryList, L_RequestParams.queryDeliveryList(UserInfoUtils.getId(getActivity()),
                odmState + "", page + "", "10",mUserBean.getUsertype()), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryDeliveryList(PublicFinal.FIRST, false);
        }
    }

    /**
     * 确认送达
     *
     * @param i
     */
    private void confirmDeliveryOrder(int i) {
        AsyncHttpUtil<OrderInfo> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderInfo.class, new IUpdateUI<OrderInfo>() {
            @Override
            public void updata(OrderInfo jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    showToast(jsonBean.getDesc());

                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryDeliveryList(PublicFinal.FIRST, false);
                } else {
                    showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.confirmDeliveryOrder2, L_RequestParams.confirmDeliveryOrder(list.get(i).getMainVO().getOdm_id(),
                list.get(i).getMainVO().getOdm_version(), mUserBean.getUserid()), true);
    }

    private void scanPay(String code) {
        AsyncHttpUtil httpStore = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("200".equals(jsonStr.getCode())){
                    showToast("收款成功");
                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryDeliveryList(PublicFinal.FIRST, false);
                }else{
                    showToast(jsonStr.getDesc());
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
        DeliveryMainBean bean = list.get(checkedIndex).getMainVO();
        httpStore.post(M_Url.scanPay, L_RequestParams.scanPay(bean.getOdm_ordercode(),code,bean.getOdm_id(), bean.getOdm_version(), mUserBean.getUserid()), true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                String code = data.getStringExtra("result");
                scanPay(code);
                break;
        }
    }

    @Override
    public void onRefresh() {
        // 下拉刷新
        mSwipeLayout.setEnabled(false);
        //        adapter.setEnableLoadMore(false);
        //        queryOrderList();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeLayout.setEnabled(false);
        //        adapter.loadMoreEnd(false);
        mSwipeLayout.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                odmState = "";
                moveAnimation(0);
                alterWidth(mTv1);
                break;
            case R.id.tv2:
                odmState = "3";
                moveAnimation(1);
                alterWidth(mTv2);
                break;
            case R.id.tv3:
                odmState = "4";
                moveAnimation(2);
                alterWidth(mTv3);
                break;
            case R.id.tv4:
                odmState = "1";
                moveAnimation(3);
                alterWidth(mTv4);
                break;
        }
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 3
                                * index).setDuration(300).start();

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryDeliveryList(PublicFinal.FIRST, false);
    }

    public void onEventMainThread(OrderListUpdateEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryDeliveryList(PublicFinal.FIRST, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
