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
import com.xdjd.distribution.adapter.OrderDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.OrderDetailBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.OrderCancelEvent;
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

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   : 订单详情
 *     version: 1.0
 * </pre>
 */

public class OrderDetailActivity extends BaseActivity {

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
    @BindView(R.id.tv_cancel_order)
    Button mTvCancelOrder;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
    @BindView(R.id.tv_delivery_amount)
    TextView mTvDeliveryAmount;
    @BindView(R.id.tv_sett_amount)
    TextView mTvSettAmount;
    @BindView(R.id.tv_single_again)
    Button mTvSingleAgain;
    @BindView(R.id.tv_order_return)
    Button mTvOrderReturn;

    private String orderType;

    private OrderDetailAdapter adapter;

    private String om_id;//订单id

    private OrderBean bean = new OrderBean();
    private OrderDetailBean orderDetailBean;
    private List<OrderDetailBean> list;

    private UserBean userBean;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    /**
     * 订单复制参数
     */
    private GoodsBean beanCopy;

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

    @Override
    protected int getContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        orderType = getIntent().getStringExtra("orderType");
        om_id = getIntent().getStringExtra("om_id");
        bean = (OrderBean) getIntent().getSerializableExtra("bean");

        mTitleBar.leftBack(this);
        mTitleBar.setTitle(orderType);
        //        mRightLeftText.setText("复制");

        //初始化数据
        clientBean = UserInfoUtils.getClientInfo(this);

        adapter = new OrderDetailAdapter();
        mLvOrderDetail.setAdapter(adapter);

        //        if (("2".equals(bean.getOm_source()) || "3".equals(bean.getOm_source())) &&
        //                UserInfoUtils.getId(this).equals(bean.getOm_salesid()) &&
        //                bean.getOm_stats() < 6) {
        //            mTvCancelOrder.setVisibility(View.VISIBLE);
        //        }

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

    @OnClick({R.id.tv_cancel_order, R.id.tv_single_again,R.id.tv_order_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_order://取消订单
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
            case R.id.tv_single_again://再来一单
                if (clientBean == null) {
                    DialogUtil.showCustomDialog(this, "提示", "无法离店后再来一单,请先进店!", "确定", null, null);
                    return;
                }
                copyOrder();
                break;
            case R.id.tv_order_return://订单退货
                Intent intent = new Intent(this,OrderReturnActivity.class);
                startActivity(intent);
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
                    EventBus.getDefault().post(new OrderCancelEvent());
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
        httpUtil.post(M_Url.updateCancelOrder, L_RequestParams.updateCancelOrder(om_id, orderDetailBean.getOm_version()), true);
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

                ClientBean clientBean = new ClientBean();
                clientBean.setCc_name(bean.getOm_customerid_nameref());
                clientBean.setCc_contacts_name(bean.getContact_name());
                clientBean.setCc_contacts_mobile(bean.getContact_mobile());
                clientBean.setCc_address(bean.getOm_address());

                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTitle("订单申报-" + orderType);
                paramBean.setOrderCode(orderDetailBean.getOm_ordercode());
                paramBean.setType(7);
                paramBean.setUserBean(userBean);
                paramBean.setClientBean(clientBean);
                paramBean.setTime(time);
                paramBean.setT(list);
                paramBean.setTotalAmount(orderDetailBean.getOm_order_amount());
                paramBean.setPrintCode(mCheckPrintGoodscode.isChecked() ? true : false);
                paramBean.setOrderStatus(orderDetailBean.getOm_stats()+"");
                if (orderDetailBean.getOm_stats()==6){//订单是已发货收款状态
                    paramBean.setSkAmount(orderDetailBean.getSkAmount());
                    paramBean.setYhAmount(orderDetailBean.getYhAmount());
                    paramBean.setXjAmount(orderDetailBean.getXjAmount());
                    paramBean.setYeAmount(orderDetailBean.getYuskAmount());
                    paramBean.setYsAmount(orderDetailBean.getYskAmount());
                    paramBean.setSfAmount(orderDetailBean.getSfkAmount());
                }

                if (orderDetailBean.getSign_url_nameref()!=null && orderDetailBean.getSign_url_nameref().length()>0){
                    Bitmap myBitmap=null;
                    try{
                        myBitmap = Glide.with(this)
                                .load(orderDetailBean.getSign_url_nameref())
                                .asBitmap() //必须
                                .into(1000, 500)
                                .get();
                    }catch(Exception e){
                    }
                    if (myBitmap!=null){
                        paramBean.setSignImg(myBitmap);
                    }
                }

//                boolean isSuccess = Prints.printOrder(socket, 7, clientBean, "订单申报-" + orderType, time, orderDetailBean.getOm_ordercode(),
//                        userBean.getBud_name(),
//                        userBean.getMobile(), list, orderDetailBean.getOm_delivery_amount(), mCheckPrintGoodscode.isChecked() ? true : false,"");
                boolean isSuccess = PrintsCopy.printOrder(socket,paramBean);

                if (orderDetailBean.getSign_url_nameref()!=null && orderDetailBean.getSign_url_nameref().length()>0){
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
        AsyncHttpUtil<OrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, OrderDetailBean.class, new IUpdateUI<OrderDetailBean>() {
            @Override
            public void updata(OrderDetailBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    orderDetailBean = bean;

                    mTvCustomerName.setText(orderDetailBean.getOm_customerid_nameref());
                    mTvOrderCode.setText(orderDetailBean.getOm_ordercode());
                    mTvOrderState.setText(orderDetailBean.getOm_stats_nameref());

                    if (9 != bean.getOm_stats() && 10 != bean.getOm_stats() && !BaseConfig.userTypeAdministrator.equals(userBean.getSu_usertype())) {
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
                    }

                    list = bean.getListData();
                    adapter.setData(list);
                    if (list == null) {
                        mTvOrderNum.setText("0件");
                        mTvAmount.setText("¥:0.00");
                        mTvDeliveryAmount.setText("¥:0.00");
                        mTvSettAmount.setText("¥:0.00");
                    } else {
                        BigDecimal deliveryAmount = BigDecimal.ZERO;
                        BigDecimal settAmount = BigDecimal.ZERO;
                        if (bean.getOm_delivery_amount() != null && bean.getOm_delivery_amount().length() > 0) {
                            deliveryAmount = new BigDecimal(bean.getOm_delivery_amount());
                        }
                        if (bean.getOm_sett_amount() != null && bean.getOm_sett_amount().length() > 0) {
                            settAmount = new BigDecimal(bean.getOm_sett_amount());
                        }

                        if (deliveryAmount.compareTo(settAmount) == 1) {
                            mTvSettAmount.setTextColor(UIUtils.getColor(R.color.text_dark_red));
                        }

                        mTvDeliveryAmount.setText("¥:" + bean.getOm_delivery_amount());
                        mTvSettAmount.setText("¥:" + bean.getOm_sett_amount());

                        mTvOrderNum.setText(list.size() + "件");
                        mTvAmount.setText("¥:" + bean.getOm_order_amount());

                        if (BaseConfig.userTypeAdministrator.equals(userBean.getSu_usertype())) {
                            mLlBottom.setVisibility(View.GONE);
                        } else {
                            if ("Y".equals(bean.getIs_cancel()) && 5 != bean.getOm_ordertype()) {//还货不显示取消订单按钮
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
        httpUtil.post(M_Url.getOrderDetail, L_RequestParams.getOrderDetail(userBean.getUserId(), om_id), true);
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
                    if ("2".equals(orderDetailBean.getOm_source())) {
                        Intent intent = new Intent(OrderDetailActivity.this, SelectStoreActivity.class);
                        intent.putExtra("beanCopy", beanCopy);
                        startActivity(intent);
                    } else if ("3".equals(orderDetailBean.getOm_source())) {//车销
                        Intent intent = new Intent(OrderDetailActivity.this, SalesOutboundActivity.class);
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
        httpUtil.post(M_Url.copyOrder, L_RequestParams.copyOrder(userBean.getUserId(), om_id), true);
    }


}
