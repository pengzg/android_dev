package com.xdjd.distribution.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.PlaceAnOrderDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.PlaceAnOrderBean;
import com.xdjd.distribution.bean.PlaceAnOrderDetailBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.PublicFinal;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   : 订货明细详情
 *     version: 1.0
 * </pre>
 */

public class PlaceAnOrderDetailActivity extends BaseActivity {

    @BindView(R.id.lv_order_detail)
    ListView mLvOrderDetail;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_cancel_order)
    Button mTvCancelOrder;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
    @BindView(R.id.tv_single_again)
    Button mTvSingleAgain;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.tv_order_code)
    TextView mTvOrderCode;
    @BindView(R.id.tv_order_state)
    TextView mTvOrderState;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_no_delivery_amount)
    TextView mTvNoDeliveryAmount;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_discounts_amount)
    TextView mTvDiscountsAmount;

    private String orderType;

    private PlaceAnOrderDetailAdapter adapter;

    private String oa_id;//订单id
    private PlaceAnOrderBean bean = null;

    private PlaceAnOrderDetailBean orderDetailBean;
    private List<PlaceAnOrderDetailBean> list;

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
        return R.layout.activity_place_an_order_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        oa_id = getIntent().getStringExtra("oa_id");
        bean = (PlaceAnOrderBean) getIntent().getSerializableExtra("bean");

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("订货详情");
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
        mTvCustomerName.setText(bean.getOa_customername());
        mTvOrderCode.setText(bean.getOa_applycode());

        adapter = new PlaceAnOrderDetailAdapter();
        mLvOrderDetail.setAdapter(adapter);

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

    @OnClick({R.id.tv_single_again, R.id.tv_cancel_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_single_again://再来一单
                if (clientBean == null) {
                    DialogUtil.showCustomDialog(this, "提示", "无法离店后再来一单,请先进店!", "确定", null, null);
                    return;
                }
                copyOrder();
                break;
            case R.id.tv_cancel_order://取消订单
                DialogUtil.showCustomDialog(this, "提示", "确定取消这个订单?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        cancelOrderDh();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    //新方法:撤消订单
    private void cancelOrderDh() {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.cancelOrderDh, L_RequestParams.cancelOrderDh(bean.getOa_id(), orderDetailBean.getOa_version()), true);
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

//                boolean isSuccess = Prints.printPlaceAnOrderList(socket, clientBean, "订货", bean.getOa_applycode(), userBean.getBud_name(),
//                        userBean.getMobile(), list, orderDetailBean);
                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTitle("订货");
                paramBean.setOrderCode(bean.getOa_applycode());
                paramBean.setUserBean(userBean);
                paramBean.setClientBean(clientBean);
                paramBean.setT(list);
                paramBean.setT1(orderDetailBean);

                boolean isSuccess = PrintsCopy.printPlaceAnOrderList(socket,paramBean);
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
        AsyncHttpUtil<PlaceAnOrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, PlaceAnOrderDetailBean.class, new IUpdateUI<PlaceAnOrderDetailBean>() {
            @Override
            public void updata(PlaceAnOrderDetailBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    orderDetailBean = bean;

                    if (orderDetailBean == null) {
                        mTvAmount.setText("¥:0.00");
                        mTvNoDeliveryAmount.setText("¥:0.00");
                        mTvDiscountsAmount.setText("¥:0.00");
                        return;
                    } else {
                        mTvAmount.setText("¥:" + orderDetailBean.getTotal_amount());
                        mTvNoDeliveryAmount.setText("¥:" + orderDetailBean.getNo_delivery_amount());
                        mTvDiscountsAmount.setText("¥:"+orderDetailBean.getDiscountAmount());
                    }

                    mTvOrderState.setText(bean.getOa_state_nameref());

                    if (clientBean == null) {
                        clientBean = new ClientBean();
                        clientBean.setCc_name(bean.getOa_customername());
                        clientBean.setCc_contacts_mobile(bean.getOa_mobile());
                        clientBean.setCc_address(bean.getOa_address());
                    }

                    if (bean.getFlag() != null && "1".equals(bean.getFlag())) {//可以取消订单
                        mLlBottom.setVisibility(View.VISIBLE);
                    } else {
                        mLlBottom.setVisibility(View.GONE);
                    }

                    list = bean.getListData();
                    adapter.setData(list);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        }

        );
        httpUtil.post(M_Url.querySalesOrderApplyDetail, L_RequestParams.querySalesOrderApplyDetail(oa_id), true);
    }

    /**
     * 复制订单
     */
    private void copyOrder() {
        /*AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    beanCopy = bean;
                    //2 终端申请;3 车销
                    if ("2".equals(orderDetailBean.getOm_source())) {
                        Intent intent = new Intent(PlaceAnOrderDetailActivity.this, SelectStoreActivity.class);
                        intent.putExtra("beanCopy", beanCopy);
                        startActivity(intent);
                    } else if ("3".equals(orderDetailBean.getOm_source())) {//车销
                        Intent intent = new Intent(PlaceAnOrderDetailActivity.this, SalesOutboundActivity.class);
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
        httpUtil.post(M_Url.copyOrder, L_RequestParams.copyOrder(userBean.getUserId(), om_id), true);*/
    }


}
