package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.ClientDetailActivity;
import com.xdjd.distribution.activity.OrderDetailActivity;
import com.xdjd.distribution.activity.PhotoActivity;
import com.xdjd.distribution.activity.RolloutGoodsOrderActivity;
import com.xdjd.distribution.activity.RolloutGoodsOrderDetailActivity;
import com.xdjd.distribution.activity.SalesOutboundActivity;
import com.xdjd.distribution.activity.SelectStoreActivity;
import com.xdjd.distribution.adapter.ApplyOrderAdapter;
import com.xdjd.distribution.adapter.OrderSearchAdapter;
import com.xdjd.distribution.adapter.ReceiptPaymentDetailAdapter;
import com.xdjd.distribution.adapter.ReceivableListAdapter;
import com.xdjd.distribution.adapter.RolloutGoodsOrderListAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.ApplyOrderBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.ReceiptPaymentDetailBean;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.distribution.bean.RolloutGoodsOrderBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.steward.adapter.SalesmanVisitingAdapter;
import com.xdjd.steward.bean.ReportTaskBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.NoScrollListView;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

@SuppressLint("ValidFragment")
public class ClientOrderListFragment extends BaseFragment implements SalesmanVisitingAdapter.OnImgListener,
        ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener, ApplyOrderAdapter.OnApplyOrderListener {

    @BindView(R.id.lv_no_list)
    NoScrollListView mLvNoList;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.elv_order_goods)
    AnimatedExpandableListView mElvOrderGoods;


    Unbinder unbinder;
    private View view;
    private int indexType;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private int page = 1;
    private int mFlag = 0;

    /**
     * 订单adapter
     */
    private OrderSearchAdapter adapterOrder;
    List<OrderBean> listOrder = new ArrayList<>();

    /**
     * 订货数据
     */
    private ApplyOrderAdapter adapterOrderGoods;
    public List<ApplyOrderBean> listOrderGoods = new ArrayList<>();

    /**
     * 拜访数据
     */
    private SalesmanVisitingAdapter adapterVisit;
    public List<ReportTaskBean> listVisit = new ArrayList<>();

    /**
     * 应收款
     */
    private ReceivableListAdapter adapterYs;
    private List<ReceivableListBean> listYs = new ArrayList<>();

    /**
     * 收付款
     */
    private ReceiptPaymentDetailAdapter adapterSfk;
    private List<ReceiptPaymentDetailBean> listSfk = new ArrayList<>();

    /**
     * 铺货
     */
    RolloutGoodsOrderListAdapter adapterPh;
    private List<RolloutGoodsOrderBean> listPh = new ArrayList<>();

    private VaryViewHelper helper;

    public ClientOrderListFragment(int indexType) {
        this.indexType = indexType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_client_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        //从客户列表跳转过来的
        clientBean = (ClientBean) getActivity().getIntent().getSerializableExtra("customer");

        if (clientBean == null) {//如果没有客户信息,则是从首页跳转过来的
            clientBean = UserInfoUtils.getClientInfo(getActivity());
        }

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                switch (indexType) {
                    case Comon.CD_ORDER:
                        listOrder.clear();
                        adapterOrder.notifyDataSetChanged();
                        queryOrderGrid();
                        break;
                    case Comon.CD_ACTION:
                        mPullScroll.onRefreshComplete();
                        break;
                    case Comon.CD_YS:
                        listYs.clear();
                        adapterYs.notifyDataSetChanged();
                        queryYs();
                        break;
                    case Comon.CD_SFK:
                        listSfk.clear();
                        adapterSfk.notifyDataSetChanged();
                        querySfk();
                        break;
                    case Comon.CD_VISIT:
                        listVisit.clear();
                        adapterVisit.notifyDataSetChanged();
                        queryVisit();
                        break;
                    case Comon.CD_DH:
                        listOrderGoods.clear();
                        adapterOrderGoods.notifyDataSetChanged();
                        queryApplyOrder();
                        break;
                    case Comon.CD_PH://铺货
                        listPh.clear();
                        adapterPh.notifyDataSetChanged();
                        queryPhOrderDetailList();
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                switch (indexType) {
                    case Comon.CD_ORDER:
                        queryOrderGrid();
                        break;
                    case Comon.CD_ACTION:
                        mPullScroll.onRefreshComplete();
                        break;
                    case Comon.CD_YS:
                        queryYs();
                        break;
                    case Comon.CD_SFK:
                        querySfk();
                        break;
                    case Comon.CD_VISIT:
                        queryVisit();
                        break;
                    case Comon.CD_DH:
                        queryApplyOrder();
                        break;
                    case Comon.CD_PH:
                        queryPhOrderDetailList();
                        break;
                }
            }
        });

        adapterOrder = new OrderSearchAdapter(new ItemOnListener() {
            @Override
            public void onItem(int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderType", listOrder.get(position).getOm_ordertype_nameref());
                intent.putExtra("om_id", listOrder.get(position).getOm_id());
                intent.putExtra("bean", listOrder.get(position));
                startActivity(intent);
            }
        });

        mElvOrderGoods.setOnGroupExpandListener(this);
        mElvOrderGoods.setOnChildClickListener(this);
        mElvOrderGoods.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //设置扩展动画
                if (mElvOrderGoods.isGroupExpanded(groupPosition)) {
                    mElvOrderGoods.collapseGroupWithAnimation(groupPosition);
                } else {
                    mElvOrderGoods.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        mLvNoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (indexType == Comon.CD_PH){
                    Intent intent = new Intent(getActivity(), RolloutGoodsOrderDetailActivity.class);
                    intent.putExtra("orderId", listPh.get(i).getOrder_id());
                    intent.putExtra("type", "1");
                    intent.putExtra("bean", listPh.get(i));
                    startActivity(intent);
                }
            }
        });


        adapterOrder.setData(listOrder);

        adapterVisit = new SalesmanVisitingAdapter(this);
        adapterVisit.setData(listVisit);

        adapterYs = new ReceivableListAdapter(null);
        adapterYs.setData(listYs);

        adapterSfk = new ReceiptPaymentDetailAdapter(null);
        adapterSfk.setData(listSfk);

        adapterOrderGoods = new ApplyOrderAdapter(this);
        mElvOrderGoods.setAdapter(adapterOrderGoods);

        adapterPh = new RolloutGoodsOrderListAdapter(listPh,1);

        if (indexType == Comon.CD_DH) {//订货
            helper = new VaryViewHelper(mElvOrderGoods);

            mPullScroll.setVisibility(View.GONE);
            mElvOrderGoods.setVisibility(View.VISIBLE);
        } else {
            helper = new VaryViewHelper(mPullScroll);

            mPullScroll.setVisibility(View.VISIBLE);
            mElvOrderGoods.setVisibility(View.GONE);
        }

        helper.showLoadingView();//加载
        switch (indexType) {
            case Comon.CD_ORDER:
                mLvNoList.setAdapter(adapterOrder);
                queryOrderGrid();
                break;
            case Comon.CD_ACTION:
                mLvNoList.setAdapter(null);
                helper.showEmptyView();
                break;
            case Comon.CD_YS:
                mLvNoList.setAdapter(adapterYs);
                queryYs();
                break;
            case Comon.CD_SFK:
                mLvNoList.setAdapter(adapterSfk);
                querySfk();
                break;
            case Comon.CD_VISIT:
                mLvNoList.setAdapter(adapterVisit);
                queryVisit();
                break;
            case Comon.CD_DH:
                mElvOrderGoods.setAdapter(adapterOrderGoods);
                queryApplyOrder();
                break;
            case Comon.CD_PH:
                mLvNoList.setAdapter(adapterPh);
                queryPhOrderDetailList();
                break;
        }
    }

    /**
     * 查询订单列表
     */
    private void queryOrderGrid() {
        AsyncHttpUtil<OrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderBean.class, new IUpdateUI<OrderBean>() {
            @Override
            public void updata(OrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    helper.showDataView();
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listOrder.addAll(jsonBean.getListData());
                        adapterOrder.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了!");
                        }else{
                            helper.showEmptyView();
                        }
                    }
                } else {
                    helper.showErrorView(jsonBean.getRepMsg(),new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(s.getDetail(),new MyErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.dataOrderGrid, L_RequestParams.queryOrderGrid(clientBean.getCc_id(), "", "",
                "1", "", "", String.valueOf(page), ""), true);
    }

    /**
     * 加载拜访明细
     */
    private void queryVisit() {
        AsyncHttpUtil<ReportTaskBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ReportTaskBean.class, new IUpdateUI<ReportTaskBean>() {
            @Override
            public void updata(ReportTaskBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    helper.showDataView();
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        listVisit.addAll(jsonStr.getDataList());
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            page--;
                        } else {
                            helper.showEmptyView();
                        }
                    }
                    adapterVisit.notifyDataSetChanged();
                } else {
                    helper.showErrorView(jsonStr.getRepMsg(),new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(s.getDetail(),new MyErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getReportTaskList, G_RequestParams.getReportTaskList(UserInfoUtils.getId(getActivity()),
                "", String.valueOf(page), "", "",
                "1", "10", "1", clientBean.getCc_id(), ""), true);
    }

    private void queryYs() {
        AsyncHttpUtil<ReceivableListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ReceivableListBean.class, new IUpdateUI<ReceivableListBean>() {
            @Override
            public void updata(ReceivableListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    helper.showDataView();
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listYs.addAll(jsonBean.getListData());
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        } else {
                            helper.showEmptyView();
                        }
                    }
                    adapterYs.notifyDataSetChanged();
                } else {
                    helper.showErrorView(jsonBean.getRepMsg(),new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(s.getDetail(),new MyErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getGlReceivableList, L_RequestParams.getGlReceivableList("10", String.valueOf(page), clientBean.getCc_id(),
                "", ""), true);
    }

    private void querySfk() {
        AsyncHttpUtil<ReceiptPaymentDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ReceiptPaymentDetailBean.class, new IUpdateUI<ReceiptPaymentDetailBean>() {
            @Override
            public void updata(ReceiptPaymentDetailBean jsonBean) {
                helper.showDataView();
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listSfk.addAll(jsonBean.getListData());
                        adapterSfk.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }else{
                            helper.showEmptyView();
                        }
                    }
                } else {
                    helper.showErrorView(jsonBean.getRepMsg(),new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(s.getDetail(),new MyErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryCashDetailList, L_RequestParams.queryCashDetailList(UserInfoUtils.getId(getActivity()), clientBean.getCc_id(),
                "", "", String.valueOf(page), "10"), true);
    }


    /**
     * 代销,订货列表查询
     */
    private void queryApplyOrder() {
        AsyncHttpUtil<ApplyOrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ApplyOrderBean.class, new IUpdateUI<ApplyOrderBean>() {
            @Override
            public void updata(ApplyOrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listOrderGoods = jsonBean.getListData();
                        helper.showDataView();

                        //计算商品数量
                        for (int i = 0; i < listOrderGoods.size(); i++) {
                            for (int k = 0; k < listOrderGoods.get(i).getListData().size(); k++) {
                                ApplyOrderBean bean = listOrderGoods.get(i).getListData().get(k);

                                BigDecimal surplusNum;//商品未发货总数量
                                BigDecimal unitNum;//单位换算数量

                                if (bean.getOrder_surplus_num() == null || "".equals(bean.getOrder_surplus_num())) {
                                    surplusNum = BigDecimal.ZERO;
                                } else {
                                    surplusNum = new BigDecimal(bean.getOrder_surplus_num());
                                }

                                if (bean.getOad_unit_num() == null || "".equals(bean.getOad_unit_num())) {
                                    unitNum = BigDecimal.ZERO;
                                } else {
                                    unitNum = new BigDecimal(bean.getOad_unit_num());
                                }

                                //如果大小单位换算比==1隐藏大单位
                                if (unitNum.compareTo(BigDecimal.ONE) == 0) {
                                    listOrderGoods.get(i).getListData().get(k).
                                            setGoods_num_desc(surplusNum.toString() + "/" + bean.getOad_goods_unitname_min());
                                } else {
                                    BigDecimal[] results = surplusNum.divideAndRemainder(unitNum);
                                    listOrderGoods.get(i).getListData().get(k).
                                            setGoods_num_desc(results[0].toString() + "/" + bean.getOad_goods_unitname_max()
                                                    + results[1].toString() + "/" + bean.getOad_goods_unitname_min());
                                }
                            }
                        }

                        adapterOrderGoods.setData(listOrderGoods);
                        for (int i = 0; i < listOrderGoods.size(); i++) {
                            mElvOrderGoods.expandGroup(i);
                        }
                    } else {
                        helper.showEmptyView();
                    }
                } else {
                    helper.showErrorView(jsonBean.getRepMsg(),new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(s.getDetail(),new MyErrorListener());
            }

            @Override
            public void finish() {
                //                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryApplyOrder, L_RequestParams.queryApplyOrder(getActivity(), "2", clientBean.getCc_id()), true);
    }

    private void queryPhOrderDetailList() {
        AsyncHttpUtil<RolloutGoodsOrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), RolloutGoodsOrderBean.class,
                new IUpdateUI<RolloutGoodsOrderBean>() {
                    @Override
                    public void updata(RolloutGoodsOrderBean jsonStr) {
                        helper.showDataView();
                        if ("00".equals(jsonStr.getRepCode())) {
                            if (jsonStr.getListData() != null && jsonStr.getListData().size() > 0) {
                                listPh.addAll(jsonStr.getListData());
                            } else {
                                if (mFlag == 2) {
                                    page--;
                                    showToast(UIUtils.getString(R.string.on_pull_remind));
                                }else{
                                    helper.showEmptyView();
                                }
                            }
                            adapterPh.notifyDataSetChanged();
                        } else {
                            helper.showErrorView(jsonStr.getRepMsg(),new MyErrorListener());
                        }
                    }

                    @Override
                    public void sendFail(ExceptionType s) {
                        helper.showErrorView(s.getDetail(),new MyErrorListener());
                    }

                    @Override
                    public void finish() {
                        mPullScroll.onRefreshComplete();
                    }
                });
        httpUtil.post(M_Url.queryPhOrderList, L_RequestParams.queryPhOrderList(clientBean.getCc_id(), "",
                String.valueOf(page), "1", "",
                "", "", "0"), true);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            helper.showLoadingView();
            page = 1;
            mFlag = 1;
            switch (indexType) {
                case Comon.CD_ORDER:
                    listOrder.clear();
                    adapterOrder.notifyDataSetChanged();
                    queryOrderGrid();
                    break;
                case Comon.CD_ACTION:
                    mPullScroll.onRefreshComplete();
                    break;
                case Comon.CD_YS:
                    listYs.clear();
                    adapterYs.notifyDataSetChanged();
                    queryYs();
                    break;
                case Comon.CD_SFK:
                    listSfk.clear();
                    adapterSfk.notifyDataSetChanged();
                    querySfk();
                    break;
                case Comon.CD_VISIT:
                    listVisit.clear();
                    adapterVisit.notifyDataSetChanged();
                    queryVisit();
                    break;
                case Comon.CD_DH:
                    listOrderGoods.clear();
                    adapterOrderGoods.notifyDataSetChanged();
                    queryApplyOrder();
                    break;
                case Comon.CD_PH://铺货
                    listPh.clear();
                    adapterPh.notifyDataSetChanged();
                    queryPhOrderDetailList();
                    break;
            }
        }
    }

    @Override
    public void onImgItem(int i, int k) {
        //图片点击
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("ID", k);
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < listVisit.get(i).getImageList().size(); j++) {
            jsonArray.put(listVisit.get(i).getImageList().get(j));
        }
        intent.putExtra("drr", jsonArray.toString());
        intent.putExtra("isNetwork", 1);
        LogUtils.e("drr", jsonArray.toString());
        startActivityForResult(intent, 101);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
        final ApplyOrderBean bean = listOrderGoods.get(groupPosition).getListData().get(childPosition);
        final BigDecimal ggsStock;
        if (bean.getGgs_stock() == null || bean.getGgs_stock().length() == 0) {
            ggsStock = BigDecimal.ZERO;
        } else {
            ggsStock = new BigDecimal(bean.getGgs_stock());
        }
        DialogUtil.showCustomDialog(getActivity(), "提示", "选择执行还货方式", "销售出库", "订单申报", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                if (ggsStock.compareTo(BigDecimal.ZERO) == 0 || ggsStock.compareTo(BigDecimal.ZERO) == -1) {
                    showToast(bean.getOad_goods_name() + "库存不足");
                    return;
                }
                Intent intent = new Intent(getActivity(), SalesOutboundActivity.class);
                intent.putExtra("orderGoodsId", listOrderGoods.get(groupPosition).getOa_id());
                intent.putExtra("orderGoodsGoodsId", bean.getOad_goods_id());
                startActivity(intent);
            }

            @Override
            public void no() {
                Intent intent = new Intent(getActivity(), SelectStoreActivity.class);
                intent.putExtra("orderGoodsId", listOrderGoods.get(groupPosition).getOa_id());
                intent.putExtra("orderGoodsGoodsId", bean.getOad_goods_id());
                startActivity(intent);
            }
        });
        return false;
    }

    @Override
    public void onGroupExpand(int groupPosition) {
    }

    @Override
    public void onArrowItem(int i, ImageView iv) {
        if (mElvOrderGoods.isGroupExpanded(i)) {
            mElvOrderGoods.collapseGroup(i);
            ObjectAnimator visToInvis = ObjectAnimator.ofFloat(iv, "rotationX", 180f, 0f);
            visToInvis.setDuration(200);
            visToInvis.start();
        } else {
            mElvOrderGoods.expandGroup(i);
            ObjectAnimator invisToVis = ObjectAnimator.ofFloat(iv, "rotationX",
                    0f, 180f);
            invisToVis.setDuration(300);
            invisToVis.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
