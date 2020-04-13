package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OrderSettlementAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.DistributionGoodsBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.OrderDeclareStrBean;
import com.xdjd.distribution.bean.OrderDetailStrBean;
import com.xdjd.distribution.bean.OrderListBean;
import com.xdjd.distribution.bean.OrderSettlementBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.ResultBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.utils.bluetooth.Prints;
import com.xdjd.utils.bluetooth.PrintsCopy;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.toast.ToastUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderSettlementActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_order_form)
    TextView mTvOrderForm;
    @BindView(R.id.tv_process_sheet_form)
    TextView mTvProcessSheetForm;
    @BindView(R.id.tv_exchange_form)
    TextView mTvExchangeForm;
    @BindView(R.id.tv_refund_form)
    TextView mTvRefundForm;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.activity_order_settlement)
    LinearLayout mActivityOrderSettlement;
    @BindView(R.id.tv_amount_price)
    TextView mTvAmountPrice;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.iv_print_num_minus)
    ImageView mIvPrintNumMinus;
    @BindView(R.id.et_print_num)
    EditText mEtPrintNum;
    @BindView(R.id.iv_print_num_plus)
    ImageView mIvPrintNumPlus;
    @BindView(R.id.print_num_nameref)
    TextView mPrintNumNameref;
    @BindView(R.id.tv_customer_balance)
    TextView mTvCustomerBalance;
    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.ll_print_main)
    LinearLayout mLlPrintMain;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
    @BindView(R.id.tv_give_back_form)
    TextView mTvGiveBackForm;

    private OrderSettlementAdapter adapter;

    /**
     * 订单选中的商品列表
     */
    public List<OrderSettlementBean> listGoodsOrderSettlement = new ArrayList<>();

    /**
     * 处理单选中的商品列表
     */
    public List<OrderSettlementBean> listProcessOrderSettlement = new ArrayList<>();

    /**
     * 换货单选中的商品列表
     */
    public List<OrderSettlementBean> listExchangeOrderSettlement = new ArrayList<>();

    /**
     * 退货单选中的商品列表
     */
    public List<OrderSettlementBean> listRefundOrderSettlement = new ArrayList<>();

    /**
     * 还货单选中的商品
     */
    public List<OrderSettlementBean> listGiveBackSettlement = new ArrayList<>();

    private String goodsOrderTotalPrice = "0.00";
    private String processOrderTotalPrice = "0.00";
    private String exchangeOrderTotalPrice = "0.00";
    private String refundOrderTotalPrice = "0.00";
    private String givebackOrderTotalPrice = "0.00";

    /**
     * 需要结算的商品列表
     */
    private List<OrderListBean> listOrderList;

    /**
     * 生成的订单商品
     */
    private List<OrderSettlementBean> listSettlement;

    /**
     * 所有订单结算信息
     */
    private OrderSettlementBean beanSettlement;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    /**
     * 仓库id
     */
    public String storehouseId = "";

    /**
     * 发货时间
     */
    public String deliveryTime;

    /**
     * 备注
     */
    public String note;

    public int indexOrder = 1;

    /**
     * 结算的订单类型名称
     */
    private String orderTitle;

    /**
     * 订单编号
     */
    private String orderCode;

    private List<BluetoothDevice> printerDevices;

    /**
     * 记录传过来的订单类型,提交订单是传这个参数
     */
    private int tabIndex;

    /**
     * 订单类型 1.申报;2.车销;3.配送任务商品列表
     */
    private int orderType = 1;

    /**
     * 配送任务商品接口
     */
    private OrderBean mOrderBean;
    private CustomerTaskBean taskCustomer;
    private String customerId;
    private String customer_name;
    private int listIndex;
    private String amount;//配送任务总价格

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
                showToast("打印成功!");
            } else if (msg.what == PublicFinal.ERROR) {
                showToast(UIUtils.getString(R.string.ly_null_error));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_settlement;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.setTitle("订单结算");
        mTitleBar.leftBack(this);

        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 4;

        userBean = UserInfoUtils.getUser(this);
        listOrderList = (List<OrderListBean>) getIntent().getSerializableExtra("list");

        storehouseId = getIntent().getStringExtra("storehouseId");
        deliveryTime = getIntent().getStringExtra("deliveryTime");
        note = getIntent().getStringExtra("note");
        indexOrder = getIntent().getIntExtra("indexOrder", 1);
        orderType = getIntent().getIntExtra("orderType", 1);

        tabIndex = indexOrder;

        adapter = new OrderSettlementAdapter();
        mLvGoods.setAdapter(adapter);

        if (orderType == 1) {
            mLlPrintMain.setVisibility(View.VISIBLE);
        } else {
            mLlPrintMain.setVisibility(View.GONE);
        }

        if (listOrderList!=null && listOrderList.size()>0){
            for (OrderListBean bean:listOrderList){
                if (bean.getListData()!=null && bean.getListData().size() > 0){
                    indexOrder = bean.getOrderType();
                    break;
                }
            }
            if (indexOrder == 0){//如果不是正常的tab显示
                indexOrder = BaseConfig.OrderType1;
            }
        }

        selectTab();
        if (orderType != 3) {
            clientBean = UserInfoUtils.getClientInfo(this);
            mTvName.setText(clientBean.getCc_name());
        }

        if (orderType == 3) {//配送任务
            listIndex = getIntent().getIntExtra("listIndex", 0);
            taskCustomer = (CustomerTaskBean) getIntent().getSerializableExtra("taskCustomer");
            customerId = taskCustomer.getCc_id();
            customer_name = taskCustomer.getCc_name();
            mOrderBean = (OrderBean) getIntent().getSerializableExtra("listOrderGoods");
            amount = getIntent().getStringExtra("amount");

            mTvName.setText(customer_name);

            clientBean = new ClientBean();
            clientBean.setCc_id(customerId);
            clientBean.setCc_name(customer_name);
            clientBean.setCc_address(taskCustomer.getCc_address());
            clientBean.setCc_contacts_name(mOrderBean.getContact_name());
            clientBean.setCc_contacts_mobile(mOrderBean.getContact_mobile());

            if (mOrderBean == null) {
                finishActivity();
                return;
            }

            deliverySettlementOrder();
        } else {//申报、车销生成结算信息接口
            customerId = clientBean.getCc_id();
            //            bulidSettlementOrder(listGoods, orderType + "");

            bulidSettlementListOrder(String.valueOf(orderType));
        }
        getCustomerBalance();//获取客户余额
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillAdapter();
    }

    /**
     * 从所有已配对设备中找出打印设备并显示
     */
    private void fillAdapter() {
        //推荐使用 BluetoothUtil.getPairedPrinterDevices()
        printerDevices = BluetoothUtil.getPairedDevices();
    }

    /**
     * 获取客户余额
     */
    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ClientBean clientBean = UserInfoUtils.getClientInfo(OrderSettlementActivity.this);
                    if (clientBean != null && clientBean.getCc_id().equals(customerId)) {
                        UserInfoUtils.setCustomerBalance(OrderSettlementActivity.this, jsonBean.getBalance());
                        UserInfoUtils.setAfterAmount(OrderSettlementActivity.this, jsonBean.getGcb_after_amount());
                        UserInfoUtils.setSafetyArrearsNum(OrderSettlementActivity.this, jsonBean.getCc_safety_arrears_num());
                    }

                    mTvCustomerBalance.setText(jsonBean.getBalance());
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getCustomerBalance, L_RequestParams.
                getCustomerBalance(customerId), false);
    }

    /**
     * 配送任务生成订单结算接口
     */
    private void deliverySettlementOrder() {

        List<OrderDetailStrBean> listOrderDetailStrBeen = new ArrayList<>();

        List<OrderDetailStrBean> list = new ArrayList<>();

        OrderDetailStrBean bean1 = new OrderDetailStrBean();
        bean1.setOm_id(mOrderBean.getOm_id());
        bean1.setOm_version(mOrderBean.getOm_version());

        for (DistributionGoodsBean bean : mOrderBean.getOrderDetailVoList()) {

            if ("2".equals(bean.getOd_goodstype()))//请求接口时,去掉赠品
                continue;

            OrderDetailStrBean strBean = new OrderDetailStrBean();
            strBean.setOd_id(bean.getOd_id());
            strBean.setOd_goods_price_id(bean.getOd_goods_price_id());
            strBean.setGoods_num_max(bean.getOd_goods_num_max());
            strBean.setGoods_num_min(bean.getOd_goods_num_min());
            strBean.setOd_price_max(bean.getOd_price_max());
            strBean.setOd_price_min(bean.getOd_price_min());
            strBean.setOd_pricetype("");
            strBean.setOd_note("");
            strBean.setOd_price_strategyid(bean.getOd_price_strategyid());

            listOrderDetailStrBeen.add(strBean);
        }
        bean1.setOrderDetailStr(listOrderDetailStrBeen);
        list.add(bean1);
        String orderListStr = JsonUtils.toJSONString(list);

        AsyncHttpUtil<OrderSettlementBean> httpUtil = new AsyncHttpUtil<>(this, OrderSettlementBean.class, new IUpdateUI<OrderSettlementBean>() {
            @Override
            public void updata(OrderSettlementBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSettlement = jsonBean.getListData();

                    switch (indexOrder) {//设置集合数据
                        case BaseConfig.OrderType1:
                            listGoodsOrderSettlement = listSettlement;
                            goodsOrderTotalPrice = "0".equals(amount) ? "0.00" : amount;
                            break;
                        case BaseConfig.OrderType2:
                            listProcessOrderSettlement = listSettlement;
                            processOrderTotalPrice = "0".equals(amount) ? "0.00" : amount;
                            break;
                        case BaseConfig.OrderType3:
                            listRefundOrderSettlement = listSettlement;
                            refundOrderTotalPrice = "0".equals(amount) ? "0.00" : amount;
                            break;
                        case BaseConfig.OrderType4:
                            listExchangeOrderSettlement = listSettlement;
                            exchangeOrderTotalPrice = "0".equals(amount) ? "0.00" : amount;
                            break;
                        case BaseConfig.OrderType5:
                            listGiveBackSettlement = listSettlement;
                            givebackOrderTotalPrice = "0".equals(amount) ? "0.00" : amount;
                            break;
                    }

                    adapter.setData(listSettlement);
                    updateOrderList();
                    selectTab();
                } else {
                    showToast(jsonBean.getRepMsg());
                    finishActivity();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                finishActivity();
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.deliverySettlementOrder, L_RequestParams.deliverySettlementOrder(clientBean.getCc_id(),
                String.valueOf(indexOrder), orderListStr), true);
    }

    /**
     * 多订单生成结算单信息接口
     */
    private void bulidSettlementListOrder(String type) {
        //生成参集合
        List<OrderDetailStrBean> orderList = new ArrayList<>();

        for (OrderListBean bean : listOrderList) {
            if (bean.getListData() == null || bean.getListData().size() == 0) {
                continue;
            }

            OrderDetailStrBean strBean = new OrderDetailStrBean();
            strBean.setOm_ordertype(String.valueOf(bean.getOrderType()));
            List<OrderDetailStrBean> orderDetailStr = new ArrayList<>();
            strBean.setOrderDetailStr(orderDetailStr);
            for (GoodsBean beanGoods : bean.getListData()) {

                OrderDetailStrBean strBean1 = new OrderDetailStrBean();
                strBean1.setOd_goods_price_id(beanGoods.getGgp_id());
                strBean1.setOd_goods_num_max(beanGoods.getMaxNum());
                strBean1.setOd_goods_num_min(beanGoods.getMinNum());

                if ("1".equals(beanGoods.getGgp_unit_num())) {//如果但单位换算比是1,则将小单位价格赋给大单位
                    strBean1.setOd_price_max(beanGoods.getMinPrice());
                } else {
                    strBean1.setOd_price_max(beanGoods.getMaxPrice());
                }
                strBean1.setOd_price_min(beanGoods.getMinPrice());
                strBean1.setOd_pricetype(beanGoods.getSaleType());

                strBean1.setOd_note(beanGoods.getRemarks());
                strBean1.setOd_price_strategyid(beanGoods.getGps_id());
                if (bean.getOrderType() == BaseConfig.OrderType3 || bean.getOrderType() == BaseConfig.OrderType4){
                    strBean1.setGoodsStatus(String.valueOf(beanGoods.getGoodsStatus()));
                }

                if (bean.getOrderType() == BaseConfig.OrderType5){
                    strBean1.setSource_id(beanGoods.getOad_id());
                }

                strBean.getOrderDetailStr().add(strBean1);
            }
            orderList.add(strBean);

            bean.setListSettlement(strBean.getOrderDetailStr());//添加拼接好的结算参数
        }
        String orderDetailStr = JsonUtils.toJSONString(orderList);

        AsyncHttpUtil<OrderSettlementBean> httpUtil = new AsyncHttpUtil<>(this, OrderSettlementBean.class, new IUpdateUI<OrderSettlementBean>() {
            @Override
            public void updata(OrderSettlementBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    beanSettlement = jsonBean;
                    mTvAmountPrice.setText("¥:" + beanSettlement.getTotal_amount() + "元");//合计总金额
                    for (OrderSettlementBean bean : beanSettlement.getListBuildData()) {

                        for (int i = 0; i < listOrderList.size(); i++) {//给这单集合数据赋总价格
                            if (listOrderList.get(i).getOrderType() != bean.getOm_ordertype())
                                continue;
                            switch (listOrderList.get(i).getOrderType()) {
                                case BaseConfig.OrderType1:
                                    listOrderList.get(i).setOrder_type_amount(bean.getOrder_type_amount());
                                    break;
                                case BaseConfig.OrderType2:
                                    listOrderList.get(i).setOrder_type_amount(bean.getOrder_type_amount());
                                    break;
                                case BaseConfig.OrderType3:
                                    listOrderList.get(i).setOrder_type_amount(bean.getOrder_type_amount());
                                    break;
                                case BaseConfig.OrderType4:
                                    listOrderList.get(i).setOrder_type_amount(bean.getOrder_type_amount());
                                    break;
                                case BaseConfig.OrderType5:
                                    listOrderList.get(i).setOrder_type_amount(bean.getOrder_type_amount());
                                    break;
                            }
                        }

                        switch (bean.getOm_ordertype()) {//设置集合数据
                            case BaseConfig.OrderType1:
                                listGoodsOrderSettlement = bean.getListData();
                                goodsOrderTotalPrice = "0".equals(bean.getOrder_type_amount()) ? "0.00" : bean.getOrder_type_amount();
                                break;
                            case BaseConfig.OrderType2:
                                listProcessOrderSettlement = bean.getListData();
                                processOrderTotalPrice = "0".equals(bean.getOrder_type_amount()) ? "0.00" : bean.getOrder_type_amount();
                                break;
                            case BaseConfig.OrderType3:
                                listRefundOrderSettlement = bean.getListData();
                                refundOrderTotalPrice = "0".equals(bean.getOrder_type_amount()) ? "0.00" : bean.getOrder_type_amount();
                                break;
                            case BaseConfig.OrderType4:
                                listExchangeOrderSettlement = bean.getListData();
                                exchangeOrderTotalPrice = "0".equals(bean.getOrder_type_amount()) ? "0.00" : bean.getOrder_type_amount();
                                break;
                            case BaseConfig.OrderType5:
                                listGiveBackSettlement = bean.getListData();
                                givebackOrderTotalPrice = "0".equals(bean.getOrder_type_amount()) ? "0.00" : bean.getOrder_type_amount();
                                break;
                        }
                    }
                    updateOrderList();
                    selectTab();
                } else {
                    showToast(jsonBean.getRepMsg());
                    finishActivity();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                finishActivity();
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.bulidSettlementListOrder, L_RequestParams.
                bulidSettlementListOrder(this, clientBean.getCc_id(), orderDetailStr, storehouseId, type), true);
    }

    /**
     * 更新OrderTitle名称
     */
    private void updateOrderList() {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                adapter.setData(listGoodsOrderSettlement);
                mTvTotalPrice.setText(goodsOrderTotalPrice);
                orderTitle = "订单";
                break;
            case BaseConfig.OrderType2:
                adapter.setData(listProcessOrderSettlement);
                mTvTotalPrice.setText(processOrderTotalPrice);
                orderTitle = "处理单";
                break;
            case BaseConfig.OrderType4:
                adapter.setData(listExchangeOrderSettlement);
                mTvTotalPrice.setText(exchangeOrderTotalPrice);
                orderTitle = "换货单";
                break;
            case BaseConfig.OrderType3:
                adapter.setData(listRefundOrderSettlement);
                mTvTotalPrice.setText(refundOrderTotalPrice);
                orderTitle = "退货单";
                break;
            case BaseConfig.OrderType5:
                adapter.setData(listGiveBackSettlement);
                mTvTotalPrice.setText(givebackOrderTotalPrice);
                orderTitle = "还货单";
                break;
        }
        updateTabNum();
    }


    /**
     * 生成订单接口
     */
    private void createOrder() {
        //生成参集合
        List<OrderDeclareStrBean> orderList = new ArrayList<>();

        for (OrderListBean bean : listOrderList) {
            if (bean.getListData() == null || bean.getListData().size() == 0) {
                continue;
            }

            OrderDeclareStrBean strBean = new OrderDeclareStrBean();
            strBean.setOm_ordertype(String.valueOf(bean.getOrderType()));
            strBean.setOm_deliverydate(deliveryTime);//发货日期
            strBean.setOm_storeid(storehouseId);//仓库id
            strBean.setOm_ordertype(String.valueOf(bean.getOrderType()));
            strBean.setOm_remarks(note);

            List<OrderDeclareStrBean> orderDetailStr = new ArrayList<>();
            strBean.setOrderDetailStr(orderDetailStr);
            for (GoodsBean beanGoods : bean.getListData()) {
                OrderDeclareStrBean strBean1 = new OrderDeclareStrBean();
                strBean1.setOd_goods_price_id(beanGoods.getGgp_id());
                strBean1.setOd_pricetype(beanGoods.getSaleType());
                strBean1.setOd_goods_num_max(beanGoods.getMaxNum());
                strBean1.setOd_goods_num_min(beanGoods.getMinNum());
                if ("1".equals(beanGoods.getGgp_unit_num())) {//如果但单位换算比是1,则将小单位价格赋给大单位
                    strBean1.setOd_price_max(beanGoods.getMinPrice());
                } else {
                    strBean1.setOd_price_max(beanGoods.getMaxPrice());
                }
                strBean1.setOd_price_min(beanGoods.getMinPrice());
                strBean1.setOd_note(beanGoods.getRemarks());
                strBean1.setOd_price_strategyid(beanGoods.getGps_id());
                if (bean.getOrderType() == BaseConfig.OrderType5) {
                    strBean1.setSource_id(beanGoods.getOad_id());
                }
                if (bean.getOrderType() == BaseConfig.OrderType3 || bean.getOrderType() == BaseConfig.OrderType4){
                    strBean1.setGoodsStatus(String.valueOf(beanGoods.getGoodsStatus()));
                }

                strBean.getOrderDetailStr().add(strBean1);
            }

            orderList.add(strBean);
        }
        String orderDetailStr = JsonUtils.toJSONString(orderList);


        AsyncHttpUtil<ResultBean> httpUtil = new AsyncHttpUtil<>(this, ResultBean.class, new IUpdateUI<ResultBean>() {
            @Override
            public void updata(ResultBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    //                    showToast(jsonBean.getRepMsg());
                    ToastUtils.showToastInCenterSuccess(OrderSettlementActivity.this, jsonBean.getRepMsg());
                    orderCode = jsonBean.getOm_ordercode();

                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        if (!BluetoothUtil.isBluetoothOn()) {//如果没有打开蓝牙就去打开蓝牙
                            showToast("蓝牙未开启!");
                            eventLaunch();
                        } else {
                            connectBluetooth();
                        }
                    } else {
                        eventLaunch();
//                        EventBus.getDefault().post(new OrderDeclareEvent(indexOrder));
//                        EventBus.getDefault().post(new TaskNumEvent());
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
//                EventBus.getDefault().post(new OrderDeclareEvent(indexOrder));
//                EventBus.getDefault().post(new TaskNumEvent());
            }

            @Override
            public void finish() {
            }
        });
        //订单类型 1 普通 2 处理 3 退货 4 换货 5 还货
        httpUtil.post(M_Url.createOrUpdateListOrder, L_RequestParams.
                createOrUpdateListOrder(this, clientBean.getCc_id(), orderDetailStr,UserInfoUtils.getLineId(this)), true);
    }

    /**
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            showToast(UIUtils.getString(R.string.ly_null_error));
            finishActivity();
            eventLaunch();
        } else {
            String deviceAddress = UserInfoUtils.getDeviceAddress(this);

            for (int i = 0; i < printerDevices.size(); i++) {
                if (deviceAddress.equals(printerDevices.get(i).getAddress())) {
                    if (printerDevices.get(i) != null) {
                        super.connectDevice(printerDevices.get(i), TASK_TYPE_PRINT);
                    } else {
                        eventLaunch();
                        showToast(UIUtils.getString(R.string.ly_null_error));
                    }
                    return;
                }
            }
            eventLaunch();
            showToast(UIUtils.getString(R.string.ly_null_error));
        }
    }

    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                if (socket == null) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
                }

                String time = StringUtils.getDate();
                if (tabIndex == BaseConfig.OrderType3) {
                    BigDecimal big = new BigDecimal("-1");
                    List<OrderSettlementBean> list = new ArrayList<>();
                    for (OrderSettlementBean bean : adapter.list) {
                        BigDecimal totalPrice;
                        if (bean.getOd_real_total() != null && !bean.getOd_real_total().equals("")) {
                            totalPrice = new BigDecimal(bean.getOd_real_total());
                            if (totalPrice.compareTo(BigDecimal.ZERO) == 1) {
                                bean.setOd_real_total(totalPrice.multiply(big).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            }
                        }
                    }
                }

                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTitle("订单申报-" + orderTitle);
                paramBean.setOrderCode(orderCode);
                paramBean.setType(1);
                paramBean.setUserBean(userBean);
                paramBean.setClientBean(clientBean);
                paramBean.setTime(time);
                paramBean.setT(beanSettlement);
                paramBean.setTotalAmount(beanSettlement.getTotal_amount());
                paramBean.setPrintCode(mCheckPrintGoodscode.isChecked() ? true : false);
                paramBean.setSkAmount(null);
                paramBean.setYhAmount(null);
                paramBean.setXjAmount(null);
                paramBean.setYeAmount(null);
                paramBean.setYsAmount(null);
                paramBean.setSfAmount(null);

                BigDecimal printNum = new BigDecimal(mEtPrintNum.getText().toString());
                for (int i = 0; i < printNum.intValue(); i++) {

                    if (i > 0 && i < printNum.intValue()) {
                        Prints.printsDivider(socket);
                    }

//                    boolean isSuccess = Prints.printOrderList(socket, 1, clientBean, "订单申报-" + orderTitle, time, orderCode, userBean.getBud_name(),
//                            userBean.getMobile(), beanSettlement,  beanSettlement.getTotal_amount(), mCheckPrintGoodscode.isChecked() ? true : false);
                    boolean isSuccess = PrintsCopy.printOrderList(socket, paramBean);
                    if (!isSuccess) {
                        mHandler.sendEmptyMessage(PublicFinal.ERROR);
                        return;
                    } else {
                        mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                    }
                }
                break;
            case TASK_TYPE_CONNECT:
                break;
        }
    }

    @Override
    protected void onConnectionFinish() {
        super.onConnectionFinish();
        eventLaunch();
    }

    private void eventLaunch() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finishActivity();
//        EventBus.getDefault().post(new OrderDeclareEvent(indexOrder));
//        EventBus.getDefault().post(new TaskNumEvent());
    }

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form,
            R.id.iv_print_num_minus, R.id.iv_print_num_plus, R.id.tv_submit,R.id.tv_give_back_form})
    public void onClick(View view) {
        switch (view.getId()) {
            //1 普通 2 处理 3 退货 4 换货 5 还货
            case R.id.tv_order_form://订单
                indexOrder = 1;
                selectTab();
                break;
            case R.id.tv_process_sheet_form://处理单
                indexOrder = 2;
                selectTab();
                break;
            case R.id.tv_exchange_form://换货单
                indexOrder = 4;
                selectTab();
                break;
            case R.id.tv_refund_form://退货单
                indexOrder = 3;
                selectTab();
                break;
            case R.id.tv_give_back_form://还货单
                indexOrder = 5;
                selectTab();
                break;
            case R.id.iv_print_num_minus://打印数量减
                minusCalculation(mEtPrintNum);
                break;
            case R.id.iv_print_num_plus://打印数量加
                plusCalculation(mEtPrintNum);
                break;
            case R.id.tv_submit:
                if (orderType == 1) {//申报订单提交

                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                            (UserInfoUtils.getDeviceAddress(this) == null || "".equals(UserInfoUtils.getDeviceAddress(this)))) {
                        DialogUtil.showCustomDialog(this, "提示", "还未设置默认打印机,是否去设置打印机?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                startActivity(PrintSettingActivity.class);
                            }

                            @Override
                            public void no() {
                                mEtPrintNum.setText("0");
                            }
                        });
                    } else {
                        //                        if (listGoodsOrderSettlement.size() > 0) {
                        createOrder();
                        //                        }

                    }

                } else if (orderType == 2) {//车销
                   /* BigDecimal sum = BigDecimal.ZERO;
                    for (GoodsBean bean : listGoods) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }*/
                    Intent intent = new Intent(this, SalesSettlementActivity.class);
                    intent.putExtra("list", (Serializable) listOrderList);
                    intent.putExtra("beanSettlement", (Serializable) beanSettlement);
                    intent.putExtra("businessType", Comon.BUSINESS_CX);
                    intent.putExtra("om_ordertype", indexOrder);
                    intent.putExtra("customerId", clientBean.getCc_id());//客户id
                    intent.putExtra("customer_name", clientBean.getCc_name());//客户名称
                    intent.putExtra("amount", beanSettlement.getTotal_amount());//总的订单金额
                    startActivity(intent);
                    finishActivity();
                } else if (orderType == 3) {
                    Intent intent = new Intent(this, SalesSettlementActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("listOrderGoods", (Serializable) mOrderBean);
                    intent.putExtra("businessType", Comon.BUSINESS_RW);
                    intent.putExtra("om_ordertype", indexOrder);
                    intent.putExtra("listIndex", listIndex);//集合下标
                    intent.putExtra("customerId", customerId);//客户id
                    intent.putExtra("customer_name", customer_name);//客户名称
                    intent.putExtra("taskCustomer",taskCustomer);
                    intent.putExtra("listSettlement", (Serializable) listSettlement);
                    startActivity(intent);
                    finishActivity();
                }
                break;
        }
    }

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        if ((bd.intValue() + 1) > 4) {
            showToast("最多打印四份");
            return;
        }
        et.setText((bd.intValue() + 1) + "");
        AnimUtils.setTranslateAnimation(mEtPrintNum, mLlLeft, mRlMain, mIvPrintNumPlus);
    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private void minusCalculation(EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            if (num == 0) {
                et.setText("0");
            } else if (num - 1 == 0) {
                et.setText("0");
            } else {
                et.setText(num - 1 + "");
            }
        }
        AnimUtils.setTranslateAnimation(mEtPrintNum, mLlLeft, mRlMain, mIvPrintNumPlus);
    }

    /**
     * 跟新tab显示选中商品数量
     */
    private void updateTabNum() {
        if (listGoodsOrderSettlement.size() > 0) {
            mTvOrderForm.setText("订单(" + listGoodsOrderSettlement.size() + ")");
        } else {
            mTvOrderForm.setText("订单");
        }

        if (listProcessOrderSettlement.size() > 0) {
            mTvProcessSheetForm.setText("处理单(" + listProcessOrderSettlement.size() + ")");
        } else {
            mTvProcessSheetForm.setText("处理单");
        }

        if (listRefundOrderSettlement.size() > 0) {
            mTvRefundForm.setText("退货单(" + listRefundOrderSettlement.size() + ")");
        } else {
            mTvRefundForm.setText("退货单");
        }

        if (listExchangeOrderSettlement.size() > 0) {
            mTvExchangeForm.setText("换货单(" + listExchangeOrderSettlement.size() + ")");
        } else {
            mTvExchangeForm.setText("换货单");
        }

        if (listGiveBackSettlement.size() > 0) {
            mTvGiveBackForm.setText("还货单(" + listGiveBackSettlement.size() + ")");
        } else {
            mTvGiveBackForm.setText("还货单");
        }
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                alterWidth(mTvOrderForm);
                moveAnimation(0);
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                alterWidth(mTvProcessSheetForm);
                moveAnimation(1);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                alterWidth(mTvRefundForm);
                moveAnimation(3);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                alterWidth(mTvExchangeForm);
                moveAnimation(2);
                break;
            case BaseConfig.OrderType5:
                mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                alterWidth(mTvGiveBackForm);
                moveAnimation(1);
                break;
        }
        updateAdapter();
    }

    /**
     * 根据订单状态刷新选中商品
     */
    private void updateAdapter() {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                adapter.setData(listGoodsOrderSettlement);
                mTvTotalPrice.setText(goodsOrderTotalPrice);
                break;
            case BaseConfig.OrderType2:
                adapter.setData(listProcessOrderSettlement);
                mTvTotalPrice.setText(processOrderTotalPrice);
                break;
            case BaseConfig.OrderType3:
                adapter.setData(listRefundOrderSettlement);
                mTvTotalPrice.setText(refundOrderTotalPrice);
                break;
            case BaseConfig.OrderType4:
                adapter.setData(listExchangeOrderSettlement);
                mTvTotalPrice.setText(exchangeOrderTotalPrice);
                break;
            case BaseConfig.OrderType5:
                adapter.setData(listGiveBackSettlement);
                mTvTotalPrice.setText(givebackOrderTotalPrice);
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

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 4
                                * index).setDuration(200).start();
    }

}
