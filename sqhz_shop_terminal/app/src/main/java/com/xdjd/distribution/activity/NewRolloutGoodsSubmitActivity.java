package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.NewRolloutMakesureOrderAdapter;
import com.xdjd.distribution.adapter.OrderSettlementAdapter;
import com.xdjd.distribution.adapter.PHOrderDistributionAdapter;
import com.xdjd.distribution.adapter.RolloutGoodsConfirmShowAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.BaseRolloutBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.OrderDetailStrBean;
import com.xdjd.distribution.bean.OrderSettlementBean;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.distribution.bean.PHTaskBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.RolloutGoodsStrBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.DistributionEvent;
import com.xdjd.distribution.event.RolloutGoodsEvent;
import com.xdjd.distribution.event.RolloutGoodsFinishEvent;
import com.xdjd.distribution.event.TaskNumEvent;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.utils.bluetooth.Prints;
import com.xdjd.utils.bluetooth.PrintsCopy;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.toast.ToastUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NewRolloutGoodsSubmitActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.elv_goods)
    AnimatedExpandableListView mElvGoods;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_customer_balance)
    TextView mTvCustomerBalance;
    @BindView(R.id.tv_amount_price)
    TextView mTvAmountPrice;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.ll_bottom_remarks)
    LinearLayout mLlBottomRemarks;
    @BindView(R.id.iv_print_num_minus)
    ImageView mIvPrintNumMinus;
    @BindView(R.id.et_print_num)
    EditText mEtPrintNum;
    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.iv_print_num_plus)
    ImageView mIvPrintNumPlus;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.print_num_nameref)
    TextView mPrintNumNameref;
    @BindView(R.id.ll_bottom_print)
    LinearLayout mLlBottomPrint;

    //    private RolloutMakesureOrderAdapter adapter;
    OrderSettlementAdapter adapter;
    private List<OrderSettlementBean> listSettlement;
    //    private NewRolloutMakesureOrderAdapter adapter;
    private RolloutGoodsConfirmShowAdapter adapterShow;
    /**
     * 配送任务adapter
     */
    private PHOrderDistributionAdapter adapterDis;
    private int orderType = 1;//订单类型:1.铺货;2.铺货销售;3.撤货;4.铺货申报;5.铺货配送任务
    //去铺货商品列表
    private List<GoodsBean> listRolloutGoods;
    //铺货销售商品列表
    private List<PHOrderDetailBean> listSaleGoods;
    //撤货商品列表
    private List<PHOrderDetailBean> listWithdrawGoods;
    private UserBean mUserBean;
    private ClientBean mClientBean;
    //铺货提交商品数据集合
    private List<RolloutGoodsStrBean> orderApplyDetailStr = new ArrayList<>();
    //铺货销售json商品
    private String phSaleJsonStr;
    //铺货退货json商品
    private String phWithdrawJsonStr;
    /**
     * 未完成的所有铺货单数据
     */
    private PHOrderDetailBean beanRollout;
    private String imgStr;//客户签名图片
    private Bitmap bitmapSign = null;//签名图片

    private String om_ordercode = "";//订单号
    public String storehouseId = "";//仓库id
    public String deliveryTime;//发货时间

    //配送任务传递的参数
    private PHTaskBean mOrderBean;
    private CustomerTaskBean taskCustomer;
    private int listIndex;
    private String amount;//配送任务总价格

    private List<BluetoothDevice> printerDevices;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
                showToast("打印成功!");
            } else if (msg.what == PublicFinal.ERROR) {
                if (orderType == 1) {//铺货
                    showToast("连接打印机失败,请到铺货单详情中进行补打!");
                } else {//撤货
                    showToast("连接打印机失败,请到撤货单详情中进行补打!");
                }
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_submit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mTitleBar.leftBack(this);
        orderType = getIntent().getIntExtra("orderType", 1);

        switch (orderType) {
            case 1:
                mTitleBar.setTitle("确认铺货");
                break;
            case 2:
                mTitleBar.setTitle("确认销售");
                break;
            case 3:
                mTitleBar.setTitle("确认撤货");
                break;
            case 4:
                mTitleBar.setTitle("确认订单");
                break;
            case 5://铺货配送任务
                mTitleBar.setTitle("确认订单");
                break;
        }

        mUserBean = UserInfoUtils.getUser(this);
        if (orderType!=5){
            mClientBean = UserInfoUtils.getClientInfo(this);
        }

        switch (orderType) {
            case 1:
            case 4:
                listRolloutGoods = (List<GoodsBean>) getIntent().getSerializableExtra("listRolloutGoods");
                //申报时传递的参数
                storehouseId = getIntent().getStringExtra("storehouseId");
                deliveryTime = getIntent().getStringExtra("deliveryTime");

                mElvGoods.setVisibility(View.GONE);
                adapter = new OrderSettlementAdapter();
                mLvGoods.setAdapter(adapter);
                //                adapter.setData(listRolloutGoods);

                for (GoodsBean beanGoods : listRolloutGoods) {
                    RolloutGoodsStrBean bean = new RolloutGoodsStrBean();
                    bean.setOad_goods_id(beanGoods.getGgp_goodsid());
                    bean.setOad_goods_price_id(beanGoods.getGgp_id());
                    bean.setOad_goods_num_max(beanGoods.getMaxNum());
                    bean.setOad_goods_num_min(beanGoods.getMinNum());
                    bean.setOad_price_max(beanGoods.getMaxPrice());
                    bean.setOad_price_min(beanGoods.getMinPrice());
                    bean.setOad_price_strategyid(beanGoods.getGps_id());
                    bean.setOad_pricetype(beanGoods.getSaleType());//销售类型

                    orderApplyDetailStr.add(bean);
                }

                BigDecimal sum = BigDecimal.ZERO;
                if (listRolloutGoods.size() > 0) {
                    for (GoodsBean bean : listRolloutGoods) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }
                    mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    mTvTotalPrice.setText("0.00");
                }
                mTvAmountPrice.setText("¥:" + sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "元");//合计总金额
                buildSettlementPhOrder();
                break;
            case 2:
                mLlBottomRemarks.setVisibility(View.GONE);
                mLlBottomPrint.setVisibility(View.GONE);
                listSaleGoods = (List<PHOrderDetailBean>) getIntent().getSerializableExtra("listSaleGoods");

                List<OrderDetailStrBean> listStr = new ArrayList<>();
                for (PHOrderDetailBean bean : listSaleGoods) {
                    for (PHOrderDetailBean bean1 : bean.getListGoodsData()) {
                        OrderDetailStrBean strBean = new OrderDetailStrBean();
                        strBean.setOd_goods_price_id(bean1.getOad_goods_price_id());
                        strBean.setOd_goods_num_max(bean1.getMaxNum());
                        strBean.setOd_goods_num_min(bean1.getMinNum());
                        strBean.setOd_price_max(bean1.getNewMaxPrice());
                        strBean.setOd_price_min(bean1.getNewMinPrice());
                        strBean.setOd_pricetype("");//销售类型
                        strBean.setOd_note("");
                        strBean.setSource_id(bean1.getOad_id());
                        listStr.add(strBean);
                    }
                }
                phSaleJsonStr = JsonUtils.toJSONString(listStr);

                BigDecimal sum1 = BigDecimal.ZERO;
                if (listSaleGoods.size() > 0) {
                    for (PHOrderDetailBean bean : listSaleGoods) {
                        for (PHOrderDetailBean bean1 : bean.getListGoodsData()) {
                            BigDecimal bdPrice = new BigDecimal(bean1.getTotalPrice());
                            sum1 = sum1.add(bdPrice);
                        }
                    }
                    mTvTotalPrice.setText(sum1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    mTvTotalPrice.setText("0.00");
                }
                mTvAmountPrice.setText("¥:" + sum1.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "元");//合计总金额

                mLvGoods.setVisibility(View.GONE);
                adapterShow = new RolloutGoodsConfirmShowAdapter();
                adapterShow.setData(listSaleGoods);
                mElvGoods.setAdapter(adapterShow);
                int groupCount = listSaleGoods.size();
                for (int i = 0; i < groupCount; i++) {
                    mElvGoods.expandGroup(i);
                }
                break;
            case 3://撤货
                listWithdrawGoods = (List<PHOrderDetailBean>) getIntent().getSerializableExtra("listWithdrawGoods");

                List<OrderDetailStrBean> listWithdrawStr = new ArrayList<>();
                for (PHOrderDetailBean bean : listWithdrawGoods) {
                    for (PHOrderDetailBean bean1 : bean.getListGoodsData()) {
                        OrderDetailStrBean strBean = new OrderDetailStrBean();
                        strBean.setOd_goods_price_id(bean1.getOad_goods_price_id());
                        strBean.setOd_goods_num_max(bean1.getMaxNum());
                        strBean.setOd_goods_num_min(bean1.getMinNum());
                        strBean.setOd_price_max(bean1.getNewMaxPrice());
                        strBean.setOd_price_min(bean1.getNewMinPrice());
                        strBean.setOd_pricetype("");
                        strBean.setOd_note("");
                        strBean.setSource_id(bean1.getOad_id());
                        listWithdrawStr.add(strBean);
                    }
                }
                phWithdrawJsonStr = JsonUtils.toJSONString(listWithdrawStr);

                BigDecimal sum2 = BigDecimal.ZERO;
                if (listWithdrawGoods.size() > 0) {
                    for (PHOrderDetailBean bean : listWithdrawGoods) {
                        for (PHOrderDetailBean bean1 : bean.getListGoodsData()) {
                            BigDecimal bdPrice = new BigDecimal(bean1.getTotalPrice());
                            sum2 = sum2.add(bdPrice);
                        }
                    }
                    mTvTotalPrice.setText(sum2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    mTvTotalPrice.setText("0.00");
                }
                mTvAmountPrice.setText("¥:" + sum2.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "元");//合计总金额

                mLvGoods.setVisibility(View.GONE);
                adapterShow = new RolloutGoodsConfirmShowAdapter();
                adapterShow.setData(listWithdrawGoods);
                mElvGoods.setAdapter(adapterShow);
                int groupCount1 = listWithdrawGoods.size();
                for (int i = 0; i < groupCount1; i++) {
                    mElvGoods.expandGroup(i);
                }
                break;
            case 5://配送任务
                listIndex = getIntent().getIntExtra("listIndex", 0);
                mOrderBean = (PHTaskBean) getIntent().getSerializableExtra("listOrderGoods");
                taskCustomer = (CustomerTaskBean) getIntent().getSerializableExtra("taskCustomer");
                amount = getIntent().getStringExtra("amount");

                if (mClientBean==null){
                    mClientBean = new ClientBean();
                    mClientBean.setCc_id(taskCustomer.getCc_id());
                    mClientBean.setCc_name(taskCustomer.getCc_name());
                    mClientBean.setCc_contacts_name(taskCustomer.getCc_contacts_name());
                    mClientBean.setCc_contacts_mobile(taskCustomer.getCc_contacts_mobile());
                    mClientBean.setCc_address(taskCustomer.getCc_address());

                    mTvName.setText(mClientBean.getCc_name());
                }

                mElvGoods.setVisibility(View.GONE);
                adapterDis = new PHOrderDistributionAdapter();
                mLvGoods.setAdapter(adapterDis);
                adapterDis.setData(mOrderBean.getDetailList());

                mTvTotalPrice.setText(amount);
                mTvAmountPrice.setText("¥:" + amount + "元");//合计总金额
                break;
        }
        mTvName.setText(mClientBean.getCc_name());

        getCustomerBalance();
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
     * 获取铺货活动数据
     */
    private void buildSettlementPhOrder() {
        String orderListStr = JsonUtils.toJSONString(orderApplyDetailStr);
        AsyncHttpUtil<OrderSettlementBean> httpUtil = new AsyncHttpUtil<>(this, OrderSettlementBean.class, new IUpdateUI<OrderSettlementBean>() {
            @Override
            public void updata(OrderSettlementBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSettlement = jsonBean.getListData();
                    adapter.setData(listSettlement);
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
        httpUtil.post(M_Url.buildSettlementPhOrder, L_RequestParams.
                buildSettlementPhOrder(mClientBean.getCc_id(), orderListStr), false);
    }

    /**
     * 铺货订单提交
     */
    private void distributionOrder() {
        String orderListStr = JsonUtils.toJSONString(orderApplyDetailStr);
        LogUtils.e("orderListStr", orderListStr);
        AsyncHttpUtil<BaseRolloutBean> httpUtil = new AsyncHttpUtil<>(this, BaseRolloutBean.class, new IUpdateUI<BaseRolloutBean>() {
            @Override
            public void updata(BaseRolloutBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    om_ordercode = jsonBean.getOrder_code();
                    ToastUtils.showToastInCenterSuccess(NewRolloutGoodsSubmitActivity.this, jsonBean.getRepMsg());
                    EventBus.getDefault().post(new RolloutGoodsEvent());

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
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.distributionOrder, L_RequestParams.
                distributionOrder(mClientBean.getCc_id(), mEtRemarks.getText().toString(), imgStr, orderListStr,
                        UserInfoUtils.getLineId(this)), true);
    }

    /**
     * 撤货订单提交
     */
    private void insertRefundPhGoods() {
        AsyncHttpUtil<BaseRolloutBean> httpUtil = new AsyncHttpUtil<>(this, BaseRolloutBean.class, new IUpdateUI<BaseRolloutBean>() {
            @Override
            public void updata(BaseRolloutBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    om_ordercode = jsonBean.getOm_ordercode();
                    ToastUtils.showToastInCenterSuccess(NewRolloutGoodsSubmitActivity.this, jsonBean.getRepMsg());
                    EventBus.getDefault().post(new RolloutGoodsEvent());//刷新铺货首页统计数据

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
                            //                                connectBluetooth();
                            loadRolloutData();
                        }
                    } else {
                        eventLaunch();
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.insertRefundPhGoods, L_RequestParams.
                insertRefundPhGoods(mClientBean.getCc_id(), phWithdrawJsonStr, mEtRemarks.getText().toString()), true);
    }

    /**
     * 铺货申报订单提交
     */
    private void insertApplyPhOrder() {
        String orderListStr = JsonUtils.toJSONString(orderApplyDetailStr);
        LogUtils.e("orderListStr", orderListStr);
        AsyncHttpUtil<BaseRolloutBean> httpUtil = new AsyncHttpUtil<>(this, BaseRolloutBean.class, new IUpdateUI<BaseRolloutBean>() {
            @Override
            public void updata(BaseRolloutBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    om_ordercode = jsonBean.getOrder_code();
                    ToastUtils.showToastInCenterSuccess(NewRolloutGoodsSubmitActivity.this, jsonBean.getRepMsg());

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
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.insertApplyPhOrder, L_RequestParams.
                insertApplyPhOrder(mClientBean.getCc_id(),
                        mEtRemarks.getText().toString(), deliveryTime, orderListStr, storehouseId,UserInfoUtils.getLineId(this)), true);
    }

    /**
     * 铺货配送接口
     */
    private void deliverPhOrder() {
        String orderListStr = JsonUtils.toJSONString(orderApplyDetailStr);
        LogUtils.e("orderListStr", orderListStr);
        AsyncHttpUtil<BaseRolloutBean> httpUtil = new AsyncHttpUtil<>(this, BaseRolloutBean.class, new IUpdateUI<BaseRolloutBean>() {
            @Override
            public void updata(BaseRolloutBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    om_ordercode = jsonBean.getOrder_code();
                    ToastUtils.showToastInCenterSuccess(NewRolloutGoodsSubmitActivity.this, jsonBean.getRepMsg());
                    EventBus.getDefault().post(new DistributionEvent(0, listIndex));
                    //                    EventBus.getDefault().post(new RolloutGoodsEvent());
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
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.deliverPhOrder, L_RequestParams.
                deliverPhOrder(mClientBean.getCc_id(),
                        mEtRemarks.getText().toString(), mOrderBean.getOa_id(),mOrderBean.getOa_version(),imgStr), true);
    }

    /**
     * 加载历史铺货单数据
     */
    private void loadRolloutData() {
        AsyncHttpUtil<PHOrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, PHOrderDetailBean.class, new IUpdateUI<PHOrderDetailBean>() {
            @Override
            public void updata(PHOrderDetailBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    beanRollout = bean;
                    connectBluetooth();
                } else {
                    showToast("连接打印机失败,请到撤货单详情中进行补打!");
                    eventLaunch();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast("连接打印机失败,请到撤货单详情中进行补打!");
                eventLaunch();
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.phOrderDetailList, L_RequestParams.phOrderDetailList(mClientBean.getCc_id(), "N"), true);
    }

    @OnClick({R.id.iv_print_num_minus, R.id.iv_print_num_plus, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_print_num_minus://打印数量减
                minusCalculation(mEtPrintNum);
                break;
            case R.id.iv_print_num_plus://打印数量加
                plusCalculation(mEtPrintNum);
                break;
            case R.id.tv_submit:
                if (orderType == 2) {
                    submitOrder();
                } else {
                    DialogUtil.showCustomDialog(this, "提示", "确认提交吗?", "提交", "取消", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            submitOrder();
                        }

                        @Override
                        public void no() {

                        }
                    });
                }
                break;
        }
    }

    /**
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            if (orderType == 1) {//铺货
                showToast("连接打印机失败,请到铺货单详情中进行补打!");
            } else {//撤货
                showToast("连接打印机失败,请到撤货单详情中进行补打!");
            }
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
                        if (orderType == 1) {//铺货
                            showToast("连接打印机失败,请到铺货单详情中进行补打!");
                        } else {//撤货
                            showToast("连接打印机失败,请到撤货单详情中进行补打!");
                        }
                    }
                    return;
                }
            }
            eventLaunch();
            if (orderType == 1) {//铺货
                showToast("连接打印机失败,请到铺货单详情中进行补打!");
            } else {//撤货
                showToast("连接打印机失败,请到撤货单详情中进行补打!");
            }
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

                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setSignImg(null);//默认签名图片为空
                paramBean.setTime(time);
                paramBean.setOrderCode(om_ordercode);
                paramBean.setUserBean(mUserBean);
                paramBean.setClientBean(mClientBean);
                paramBean.setTotalAmount(mTvTotalPrice.getText().toString());
                paramBean.setPrintCode(false);
                paramBean.setRemarks(mEtRemarks.getText().toString());

                BigDecimal printNum = new BigDecimal(mEtPrintNum.getText().toString());
                switch (orderType) {
                    case 1:
                        paramBean.setTitle("铺货单");
                        paramBean.setType(9);
                        //                        paramBean.setT(listRolloutGoods);
                        paramBean.setT(listSettlement);
                        if (bitmapSign != null) {
                            paramBean.setSignImg(bitmapSign);
                        }

                        for (int i = 0; i < printNum.intValue(); i++) {
                            if (i > 0 && i < printNum.intValue()) {
                                Prints.printsDivider(socket);
                            }
                            //                            boolean isSuccess = Prints.printOrder(socket, 9, mClientBean, "铺货单", time, om_ordercode, mUserBean.getBud_name(),
                            //                                    mUserBean.getMobile(), listRolloutGoods, mTvTotalPrice.getText().toString(), false, mEtRemarks.getText().toString());
                            boolean isSuccess = PrintsCopy.printOrder(socket, paramBean);
                            if (bitmapSign != null) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!isSuccess) {
                                mHandler.sendEmptyMessage(PublicFinal.ERROR);
                                return;
                            } else {
                                mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                            }
                        }
                        break;
                    case 3:
                        paramBean.setTitle("铺货撤货单");
                        paramBean.setType(11);
                        paramBean.setT(listWithdrawGoods);
                        for (int i = 0; i < printNum.intValue(); i++) {
                            if (i > 0 && i < printNum.intValue()) {
                                Prints.printsDivider(socket);
                            }
                            boolean isSuccess = Prints.printOrder(socket, 11, mClientBean, "铺货撤货单", time, om_ordercode, mUserBean.getBud_name(),
                                    mUserBean.getMobile(), listWithdrawGoods, mTvTotalPrice.getText().toString(),
                                    null, null, null, null, null, null,
                                    false, beanRollout, mEtRemarks.getText().toString());
                            if (!isSuccess) {
                                mHandler.sendEmptyMessage(PublicFinal.ERROR);
                                return;
                            } else {
                                mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                            }
                        }
                        break;
                    case 4:
                        paramBean.setTitle("铺货申报");
                        paramBean.setType(16);
                        paramBean.setT(listSettlement);

                        for (int i = 0; i < printNum.intValue(); i++) {
                            if (i > 0 && i < printNum.intValue()) {
                                Prints.printsDivider(socket);
                            }
                            boolean isSuccess = PrintsCopy.printOrder(socket, paramBean);
                            if (!isSuccess) {
                                mHandler.sendEmptyMessage(PublicFinal.ERROR);
                                return;
                            } else {
                                mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                            }
                        }
                        break;
                    case 5://铺货配送任务
                        paramBean.setTitle("铺货单");
                        paramBean.setOrderCode(mOrderBean.getOa_applycode());
                        paramBean.setType(17);
                        paramBean.setT(mOrderBean.getDetailList());
                        if (bitmapSign != null) {
                            paramBean.setSignImg(bitmapSign);
                        }

                        for (int i = 0; i < printNum.intValue(); i++) {
                            if (i > 0 && i < printNum.intValue()) {
                                Prints.printsDivider(socket);
                            }
                            boolean isSuccess = PrintsCopy.printOrder(socket, paramBean);
                            if (bitmapSign != null) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!isSuccess) {
                                mHandler.sendEmptyMessage(PublicFinal.ERROR);
                                return;
                            } else {
                                mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                            }
                        }
                        break;
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
        Intent intent;
        switch (orderType) {
            case 1:
                intent = new Intent(NewRolloutGoodsSubmitActivity.this, RolloutGoodsSuccessActivity.class);
                intent.putExtra("type", 1);//铺货
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(NewRolloutGoodsSubmitActivity.this, RolloutGoodsSuccessActivity.class);
                intent.putExtra("type", 3);//撤货
                startActivity(intent);
                break;
            case 4:
                break;
            case 5:
                intent = new Intent(NewRolloutGoodsSubmitActivity.this, RolloutGoodsSuccessActivity.class);
                intent.putExtra("type", 5);//配送任务
                startActivity(intent);
                break;
        }
        EventBus.getDefault().post(new RolloutGoodsFinishEvent());//关闭相关页面
        finishActivity();
    }

    private void submitOrder() {
        //判断是否设置打印机
        BigDecimal printNum;
        if ("".equals(mEtPrintNum.getText().toString())) {
            printNum = BigDecimal.ZERO;
        } else {
            printNum = new BigDecimal(mEtPrintNum.getText().toString());
        }

        switch (orderType) {
            case 1:
                if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                        (UserInfoUtils.getDeviceAddress(this) == null || "".equals(UserInfoUtils.getDeviceAddress(this)))) {
                    //设置打印选项,且,没有设置默认打印机
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
                    if ("Y".equalsIgnoreCase(mUserBean.getIsSign())) {
                        if (imgStr != null && !"".equals(imgStr)) {
                            distributionOrder();
                        } else {
                            DialogUtil.showInputSign(this, getResources(), new DialogUtil.OnInputSignListener() {
                                @Override
                                public void ok(String imgSign, Bitmap bitmap) {
                                    imgStr = imgSign;
                                    bitmapSign = bitmap;
                                    distributionOrder();
                                }

                                @Override
                                public void no() {
                                }
                            });
                        }
                    } else {
                        distributionOrder();
                    }
                }
                break;
            case 2:
                Intent intent = new Intent(this, SalesSettlementActivity.class);
                intent.putExtra("customerId", mClientBean.getCc_id());//客户id
                intent.putExtra("customer_name", mClientBean.getCc_name());//客户名称
                intent.putExtra("amount", mTvTotalPrice.getText().toString());//总的订单金额
                intent.putExtra("businessType", Comon.BUSINESS_PHXS);
                intent.putExtra("phSaleJsonStr", phSaleJsonStr);
                intent.putExtra("listSaleGoods", (Serializable) listSaleGoods);
                startActivity(intent);
                break;
            case 3:
                if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                        (UserInfoUtils.getDeviceAddress(this) == null || "".equals(UserInfoUtils.getDeviceAddress(this)))) {
                    //设置打印选项,且,没有设置默认打印机
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
                    insertRefundPhGoods();
                }
                break;
            case 4:
                if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                        (UserInfoUtils.getDeviceAddress(this) == null || "".equals(UserInfoUtils.getDeviceAddress(this)))) {
                    //设置打印选项,且,没有设置默认打印机
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
                    insertApplyPhOrder();
                }
                break;
            case 5://铺货配送接口
                if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                        (UserInfoUtils.getDeviceAddress(this) == null || "".equals(UserInfoUtils.getDeviceAddress(this)))) {
                    //设置打印选项,且,没有设置默认打印机
                    DialogUtil.showCustomDialog(this, "提示", "还未设置默认打印机,是否去设置打印机?", "确定",
                            "取消", new DialogUtil.MyCustomDialogListener2() {
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
                    if ("Y".equalsIgnoreCase(mUserBean.getIsSign())) {
                        if (imgStr != null && !"".equals(imgStr)) {
                            deliverPhOrder();
                        } else {
                            DialogUtil.showInputSign(this, getResources(), new DialogUtil.OnInputSignListener() {
                                @Override
                                public void ok(String imgSign, Bitmap bitmap) {
                                    imgStr = imgSign;
                                    bitmapSign = bitmap;
                                    deliverPhOrder();
                                }

                                @Override
                                public void no() {
                                }
                            });
                        }
                    } else {
                        deliverPhOrder();
                    }
                }
                break;
        }
    }

    /**
     * 获取客户余额
     */
    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ClientBean clientBean = UserInfoUtils.getClientInfo(NewRolloutGoodsSubmitActivity.this);
                    UserInfoUtils.setCustomerBalance(NewRolloutGoodsSubmitActivity.this, jsonBean.getBalance());
                    UserInfoUtils.setAfterAmount(NewRolloutGoodsSubmitActivity.this, jsonBean.getGcb_after_amount());
                    UserInfoUtils.setSafetyArrearsNum(NewRolloutGoodsSubmitActivity.this, jsonBean.getCc_safety_arrears_num());

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
                getCustomerBalance(mClientBean.getCc_id()), false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RolloutGoodsFinishEvent event) {
        finishActivity();
    }

}
