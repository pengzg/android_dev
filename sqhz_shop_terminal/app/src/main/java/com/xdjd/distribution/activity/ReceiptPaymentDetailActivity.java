package com.xdjd.distribution.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.ReceiptPaymentDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ReceiptPaymentBean;
import com.xdjd.distribution.bean.ReceiptPaymentDetailBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.utils.bluetooth.Prints;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.toast.ToastUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiptPaymentDetailActivity extends BaseActivity implements ReceiptPaymentDetailAdapter.ReceiptPaymentDetailListener {

    @BindView(R.id.lv_order_detail)
    NoScrollListView mLvOrderDetail;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private ReceiptPaymentDetailAdapter adapter;
    private List<ReceiptPaymentDetailBean> list;

    private String customerId;//客户id

    private UserBean userBean;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private ReceiptPaymentBean bean = new ReceiptPaymentBean();

    /**
     * 订单复制参数
     */
    //    private OutboundDetailBean beanCopy;

    private String dateStartStr;
    private String dateEndStr;
    //总金额
    private String totalAmount = "0.00";

    BigDecimal bigCash = BigDecimal.ZERO;//现金
    BigDecimal bigCard = BigDecimal.ZERO;//刷卡
    BigDecimal bigPreferential = BigDecimal.ZERO;//优惠

    /**
     * 收付款客户名称
     */
    private String customerName;

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
        return R.layout.activity_receipt_payment_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        customerId = getIntent().getStringExtra("customerId");
        customerName = getIntent().getStringExtra("customerName");
        bean = (ReceiptPaymentBean) getIntent().getSerializableExtra("bean");
        totalAmount = getIntent().getStringExtra("amount");

        mTvAmount.setText(totalAmount);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("收付款明细");
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
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");

        adapter = new ReceiptPaymentDetailAdapter(this);
        mLvOrderDetail.setAdapter(adapter);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (list!=null){
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
                getOrderDetail();
            }
        });

        getOrderDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fillAdapter();
    }

    /**
     * 从所有已配对设备中找出打印设备并显示
     */
    private void fillAdapter() {
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

                ClientBean clientBean = new ClientBean();
                clientBean.setCc_name(bean.getGc_customerid_nameref());
                clientBean.setCc_contacts_name(bean.getContact_name());
                clientBean.setCc_contacts_mobile(bean.getContact_mobile());
                clientBean.setCc_address(bean.getAddress() );

                boolean isSuccess = Prints.printReceipt(socket, 2, "","", clientBean, userBean.getBud_name(),
                        userBean.getMobile(), time, list, bigCard.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                        bigPreferential.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                        bigCash.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), "");
                if (!isSuccess) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
                }
                break;
            case TASK_TYPE_CONNECT:
                break;
        }
    }

    private void getOrderDetail() {
        AsyncHttpUtil<ReceiptPaymentDetailBean> httpUtil = new AsyncHttpUtil<>(this, ReceiptPaymentDetailBean.class, new IUpdateUI<ReceiptPaymentDetailBean>() {
            @Override
            public void updata(ReceiptPaymentDetailBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    list = jsonBean.getListData();
                    adapter.setData(list);
                    if (list == null) {
                        mTvOrderNum.setText("0条");
                        //                        mTvAmount.setText("0.00");
                    } else {
                        mTvOrderNum.setText(list.size() + "条");
                        //                        amountMoney(list);
                    }

                    for (ReceiptPaymentDetailBean bean : list) {
                        BigDecimal bdCash = new BigDecimal(bean.getCashAmount() == null ? "0.00" : bean.getCashAmount());
                        bigCash = bigCash.add(bdCash);

                        BigDecimal bdCard = new BigDecimal(bean.getCardAmount() == null ? "0.00" : bean.getCardAmount());
                        bigCard = bigCard.add(bdCard);

                        BigDecimal bdPreferential = new BigDecimal(bean.getDiscountAmount() == null ? "0.00" : bean.getDiscountAmount());
                        bigPreferential = bigPreferential.add(bdPreferential);
                    }

                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryCashDetailList, L_RequestParams.queryCashDetailList(userBean.getUserId(), customerId,
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"), "1", "9999"), true);
    }

    @Override
    public void onCancelPayment(final int i) {
        DialogUtil.showCustomDialog(this, "提示", "是否取消这个收款单?", "确定", "取消",
                new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        cancelGather(i);
                    }

                    @Override
                    public void no() {
                    }
                });
    }

    private void cancelGather(final int i) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    list.get(i).setGc_stats("4");
                    list.get(i).setGc_stats_nameref("已取消");
                    adapter.notifyDataSetChanged();
                    ToastUtils.showToastInCenterSuccess(ReceiptPaymentDetailActivity.this, jsonStr.getRepMsg());
                } else {
                    DialogUtil.showCustomDialog(ReceiptPaymentDetailActivity.this,"提示",jsonStr.getRepMsg(),
                            "确定",null,null);
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
        httpUtil.post(M_Url.cancelGather, L_RequestParams.cancelGather(list.get(i).getGc_id()), true);
    }

    /**
     * 计算选中商品列表的价格
     */
    //    private void amountMoney(List<ReceiptPaymentDetailBean> list) {
    //        if (list.size() > 0) {
    //            BigDecimal sum = BigDecimal.ZERO;
    //            for (ReceiptPaymentDetailBean bean : list) {
    //                BigDecimal bdPrice = new BigDecimal(bean.get);
    //                sum = sum.add(bdPrice);
    //            }
    //            mTvAmount.setText(sum.setScale(2).doubleValue()+"");
    //        } else {
    //            mTvAmount.setText("0.00");
    //        }
    //    }

    /**
     * 复制订单
     */
    //    private void copyOrder() {
    //        AsyncHttpUtil<OutboundDetailBean> httpUtil = new AsyncHttpUtil<>(this, OutboundDetailBean.class, new IUpdateUI<OutboundDetailBean>() {
    //            @Override
    //            public void updata(OutboundDetailBean bean) {
    //                if ("00".equals(bean.getRepCode())) {
    //                    beanCopy = bean;
    //                    Intent intent = new Intent(OrderDetailActivity.this, SelectStoreActivity.class);
    //                    intent.putExtra("beanCopy", beanCopy);
    //                    startActivity(intent);
    //                } else {
    //                    showToast(bean.getRepMsg());
    //                }
    //            }
    //
    //            @Override
    //            public void sendFail(ExceptionType s) {
    //                showToast(s.getDetail());
    //            }
    //
    //            @Override
    //            public void finish() {
    //
    //            }
    //        });
    //        httpUtil.post(M_Url.copyOrder, L_RequestParams.copyOrder(UserInfoUtils.getId(this), om_id), true);
    //    }
}
