package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OutboundDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OutboundDetailBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.StockOutBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.OutboundCancelEvent;
import com.xdjd.utils.DialogUtil;
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

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class OutboundDetailActivity extends BaseActivity {

    @BindView(R.id.lv_order_detail)
    ListView mLvOrderDetail;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.tv_order_code)
    TextView mTvOrderCode;
    @BindView(R.id.tv_order_state)
    TextView mTvOrderState;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.tv_cancel_order)
    Button mTvCancelOrder;
    @BindView(R.id.tv_single_again)
    Button mTvSingleAgain;
    @BindView(R.id.tv_eim_code)
    TextView mTvEimCode;

    private String orderType;
    /**
     * 订单类型编号
     */
    private String indexType;

    private StockOutBean bean;

    private OutboundDetailAdapter adapter;

    private String om_id;//订单id

    private UserBean userBean;

    /**
     * 订单复制参数
     */
    private GoodsBean beanCopy;

    /**
     * 订单总金额
     */
    private String orderAmount;

    /**
     * 打印单据信息bean
     */
    private OutboundDetailBean outboundBean;

    private List<OutboundDetailBean> list;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private List<BluetoothDevice> printerDevices;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
            } else if (msg.what == PublicFinal.ERROR) {
                showToast(UIUtils.getString(R.string.ly_null_error));
            }
        }
    };


    /**
     * 订单复制参数
     */
    //    private OutboundDetailBean beanCopy;
    @Override
    protected int getContentView() {
        return R.layout.activity_outbound_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        orderType = getIntent().getStringExtra("orderType");
        indexType = getIntent().getStringExtra("indexType");
        om_id = getIntent().getStringExtra("om_id");
        orderAmount = getIntent().getStringExtra("orderAmount");
        bean = (StockOutBean) getIntent().getSerializableExtra("bean");

        mTitleBar.leftBack(this);
        mTitleBar.setTitle(orderType);
        mTitleBar.setRightImageResource(R.mipmap.dyjsz);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
                    BluetoothUtil.turnOnBluetooth();
                if (list != null && list.size() > 0) {
                    connectBluetooth();
                }
            }
        });

        //初始化数据
        clientBean = UserInfoUtils.getClientInfo(this);

        adapter = new OutboundDetailAdapter();
        mLvOrderDetail.setAdapter(adapter);

        mTvCustomerName.setText(bean.getEim_customerid_nameref());
        mTvEimCode.setText(bean.getEim_code());
        mTvOrderCode.setText(bean.getEim_source_code());
        mTvOrderState.setText(bean.getEim_stats_nameref());

        getOrderDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            showToast(UIUtils.getString(R.string.ly_null_error));
        } else {

            String deviceAddress = UserInfoUtils.getDeviceAddress(this);

            for (int i = 0; i < printerDevices.size(); i++) {
                if (deviceAddress.equals(printerDevices.get(i).getAddress())) {
                    if (printerDevices.get(i) != null) {
                        super.connectDevice(printerDevices.get(i), TASK_TYPE_PRINT);
                    } else {
                        showToast(UIUtils.getString(R.string.ly_null_error));
                    }
                    return;
                }
            }
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

                ClientBean clientBean;
                boolean isSuccess;
                String orderCode;
                String skAmount;
                if ("1".equals(outboundBean.getEim_source())) {
                    orderCode = outboundBean.getEim_source_code();
                    skAmount = outboundBean.getSkAmount();
                } else {
                    orderCode = bean.getEim_code();
                    skAmount = null;
                }

                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTitle(orderType);
                paramBean.setType(4);
                paramBean.setUserBean(userBean);
                paramBean.setTime(time);
                paramBean.setT(list);
                paramBean.setTotalAmount(orderAmount);

                if (outboundBean.getSign_url_nameref()!=null && outboundBean.getSign_url_nameref().length()>0){
                    Bitmap myBitmap=null;
                    try{
                        myBitmap = Glide.with(this)
                                .load(outboundBean.getSign_url_nameref())
                                .asBitmap() //必须
                                .into(1000, 500)
                                .get();
                    }catch(Exception e){
                    }
                    if (myBitmap!=null){
                        paramBean.setSignImg(myBitmap);
                    }
                }

                if (indexType.equals("302") || indexType.equals("303")) {
                    paramBean.setClientBean(null);
                    paramBean.setOrderCode(bean.getEim_code());
                    paramBean.setPrintCode(false);

//                    isSuccess = Prints.printOrder(socket, 4, null, orderType, time, bean.getEim_code(), userBean.getBud_name(),
//                            userBean.getMobile(), list, orderAmount, false,"");
                } else {
                    clientBean = new ClientBean();
                    clientBean.setCc_name(bean.getEim_customerid_nameref());
                    clientBean.setCc_contacts_name(bean.getContact_name());
                    clientBean.setCc_contacts_mobile(bean.getContact_mobile());
                    clientBean.setCc_address(bean.getCustomer_address());

                    paramBean.setClientBean(clientBean);
                    paramBean.setOrderCode(orderCode);
                    paramBean.setSkAmount(skAmount);
                    paramBean.setYhAmount(outboundBean.getYhAmount());
                    paramBean.setXjAmount(outboundBean.getXjAmount());
                    paramBean.setYeAmount(outboundBean.getYsAmount());
                    paramBean.setYsAmount(outboundBean.getYskAmount());
                    paramBean.setSfAmount(outboundBean.getSfkAmount());
                    paramBean.setPrintCode(mCheckPrintGoodscode.isChecked() ? true : false);

//                    isSuccess = Prints.printOrder(socket, 4, clientBean, orderType, time, orderCode, userBean.getBud_name(),
//                            userBean.getMobile(), list, orderAmount, skAmount, outboundBean.getYhAmount(), outboundBean.getXjAmount()
//                            ,outboundBean.getYsAmount() , outboundBean.getYskAmount(), outboundBean.getSfkAmount(),
//                            mCheckPrintGoodscode.isChecked() ? true : false,null,"");
                }

                isSuccess = PrintsCopy.printOrder(socket,paramBean);
                if (outboundBean.getSign_url_nameref()!=null && outboundBean.getSign_url_nameref().length()>0){
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!isSuccess) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
                }
                finishActivity();
                break;
            case TASK_TYPE_CONNECT:
                break;
        }
    }

    private void getOrderDetail() {
        AsyncHttpUtil<OutboundDetailBean> httpUtil = new AsyncHttpUtil<>(this, OutboundDetailBean.class, new IUpdateUI<OutboundDetailBean>() {
            @Override
            public void updata(OutboundDetailBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    outboundBean = bean;
                    list = bean.getDataList();
                    adapter.setData(list);
                    if (list == null) {
                        mTvOrderNum.setText("0件");
                        mTvAmount.setText("¥:0.00");
                    } else {
                        mTvOrderNum.setText(list.size() + "件");
                        amountMoney(list);
                    }

                    if (BaseConfig.userTypeAdministrator.equals(userBean.getSu_usertype())) {
                        mLlBottom.setVisibility(View.GONE);
                    } else {
                        if ("Y".equals(bean.getIs_cancel())) {
                            mTvCancelOrder.setVisibility(View.VISIBLE);
                        } else {
                            mTvCancelOrder.setVisibility(View.GONE);
                        }
                    }

                    if ("2".equals(bean.getOm_source()) || "3".equals(bean.getOm_source())) {
                        if (1 == bean.getOm_ordertype() || 2 == bean.getOm_ordertype()) {
                            mTvSingleAgain.setVisibility(View.VISIBLE);
                        } else {
                            mTvSingleAgain.setVisibility(View.GONE);
                        }
                    } else {
                        mTvSingleAgain.setVisibility(View.GONE);
                    }
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
        httpUtil.post(M_Url.getStockOutDetail, L_RequestParams.getStockOutDetail(userBean.getUserId(), om_id), true);
    }

    /**
     * 计算选中商品列表的价格
     */
    private void amountMoney(List<OutboundDetailBean> list) {
        if (list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (OutboundDetailBean bean : list) {
                BigDecimal bdPrice = new BigDecimal(bean.getEii_goods_amount());
                sum = sum.add(bdPrice);
            }
            mTvAmount.setText("¥:" + sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "");
        } else {
            mTvAmount.setText("0.00");
        }
    }

    @OnClick({R.id.tv_cancel_order, R.id.tv_single_again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_order:
                DialogUtil.showCustomDialog(this, "提示", "是否撤消订单?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        cancelOrder();
                    }

                    @Override
                    public void no() {
                    }
                });
                break;
            case R.id.tv_single_again:
                if (clientBean == null) {
                    DialogUtil.showCustomDialog(this, "提示", "无法离店后再来一单,请先进店!", "确定", null, null);
                    return;
                }
                copyOrder();
                break;
        }
    }

    //新方法:撤消订单
    private void cancelOrder() {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    showToast(jsonStr.getRepMsg());
                    EventBus.getDefault().post(new OutboundCancelEvent());
                    finishActivity();
                } else {
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.updateCancelErp, L_RequestParams.updateCancelErp(outboundBean.getEim_id(),
                outboundBean.getEim_version(), userBean.getOrgid()), true);
    }

    /**
     * 复制订单
     */
    private void copyOrder() {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    beanCopy = bean;
                    //2 终端申请;3 车销
                    if ("2".equals(outboundBean.getOm_source())) {
                        Intent intent = new Intent(OutboundDetailActivity.this, SelectStoreActivity.class);
                        intent.putExtra("beanCopy", beanCopy);
                        startActivity(intent);
                    } else if ("3".equals(outboundBean.getOm_source())) {//车销
                        Intent intent = new Intent(OutboundDetailActivity.this, SalesOutboundActivity.class);
                        intent.putExtra("beanCopy", beanCopy);
                        startActivity(intent);
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
        httpUtil.post(M_Url.copyOrder, L_RequestParams.copyOrder(userBean.getUserId(), outboundBean.getOm_id()), true);
    }
}
