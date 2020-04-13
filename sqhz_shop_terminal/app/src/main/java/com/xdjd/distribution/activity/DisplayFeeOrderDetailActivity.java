package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.DisplayFeeSettlementAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.DisplayInDetailBean;
import com.xdjd.distribution.bean.DisplayListBean;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.UserBean;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/22.
 */

public class DisplayFeeOrderDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    ListView mListView;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.tv_order_code)
    TextView mTvOrderCode;
    @BindView(R.id.tv_order_amount)
    TextView mTvOrderAmount;
    @BindView(R.id.ll_order_amount)
    LinearLayout mLlOrderAmount;
    @BindView(R.id.tv_order_title)
    TextView mTvOrderTitle;
    @BindView(R.id.tv_order_amount_title)
    TextView mTvOrderAmountTitle;

    private String orderId;
    private int page = 1;
    private int mFlag = 0;

    private DisplayFeeSettlementAdapter adapter;
    private List<DisplayInDetailBean> list = new ArrayList<>();

    private ClientBean mClientBean;
    private UserBean mUserBean;

    private DisplayListBean bean;
    private List<BluetoothDevice> printerDevices;

    private DisplayInDetailBean detailBean;

    BigDecimal skAmount;//刷卡
    BigDecimal xjAmount;//现金
    BigDecimal yeAmount;//余额

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
        return R.layout.activity_display_fee_order_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setRightImageResource(R.mipmap.dyjsz);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
                    BluetoothUtil.turnOnBluetooth();
                connectBluetooth();
            }
        });

        orderId = getIntent().getStringExtra("orderId");
        bean = (DisplayListBean) getIntent().getSerializableExtra("bean");

        mTitleBar.setTitle("返陈列详情");

        mUserBean = UserInfoUtils.getUser(this);
        mClientBean = UserInfoUtils.getClientInfo(this);

        adapter = new DisplayFeeSettlementAdapter(2);
        mListView.setAdapter(adapter);

        getDisplayInDetail();
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

    private void getDisplayInDetail() {
        AsyncHttpUtil<DisplayInDetailBean> httpUtil = new AsyncHttpUtil<>(this, DisplayInDetailBean.class,
                new IUpdateUI<DisplayInDetailBean>() {
                    @Override
                    public void updata(DisplayInDetailBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            detailBean = jsonStr;

                            mTvCustomerName.setText(detailBean.getCustomerName());
                            mTvOrderCode.setText(detailBean.getEim_code());
                            mTvOrderAmount.setText(detailBean.getAmount());

                            if (detailBean.getDataList() != null && detailBean.getDataList().size() > 0) {
                                    list = detailBean.getDataList();
                                    adapter.setListDetail(list);
                            } else {
                                showToast(UIUtils.getString(R.string.on_pull_remind));
                            }
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
        httpUtil.post(M_Url.getDisplayInDetail, L_RequestParams.getDisplayInDetail(bean.getEim_customerid(),orderId), true);
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
                clientBean.setCc_name(bean.getEim_customerid_nameref());
                clientBean.setCc_contacts_name(bean.getContact_name());
                clientBean.setCc_contacts_mobile(bean.getContact_mobile());
                clientBean.setCc_address(bean.getCustomer_address());

                PrintParamBean paramBean = new PrintParamBean();
//                paramBean.setClientBean(clientBean);
                paramBean.setUserBean(mUserBean);
                paramBean.setOrderCode(bean.getEim_code());
                paramBean.setTime(time);
                paramBean.setTotalAmount(bean.getEim_totalamount());
                paramBean.setPrintCode(false);

                boolean isSuccess = false;

                paramBean.setTitle("返陈列");
                paramBean.setT(list);
                paramBean.setType(16);
                isSuccess = Prints.printOrder(socket, 16, clientBean, "返陈列", time, detailBean.getEim_code(),
                        mUserBean.getBud_name(),
                        mUserBean.getMobile(), detailBean.getDataList(), detailBean.getAmount(), /*mCheckPrintGoodscode.isChecked() ? true :*/ false, "");

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

    /*private void initAmount() {
        if (TextUtils.isEmpty(detailBean.getSkAmount())) {
            skAmount = new BigDecimal("0");
        } else {
            skAmount = new BigDecimal(detailBean.getSkAmount());
        }

        if (TextUtils.isEmpty(detailBean.getXjAmount())) {
            xjAmount = new BigDecimal("0");
        } else {
            xjAmount = new BigDecimal(detailBean.getXjAmount());
        }

        if (TextUtils.isEmpty(detailBean.getYeAmount())) {
            yeAmount = new BigDecimal("0");
        } else {
            yeAmount = new BigDecimal(detailBean.getYeAmount());
        }

    }*/

}
