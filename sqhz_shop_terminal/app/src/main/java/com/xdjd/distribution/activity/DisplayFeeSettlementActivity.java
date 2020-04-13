package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.DisplayFeeSettlementAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.bean.DisplayFeeStrBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.DisplayFinishEvent;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.JsonUtils;
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
import com.xdjd.view.toast.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2018/1/15.
 */

public class DisplayFeeSettlementActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
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
    @BindView(R.id.ll_print_main)
    LinearLayout mLlPrintMain;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_customer_balance)
    TextView mTvCustomerBalance;
    @BindView(R.id.tv_amount_price)
    TextView mTvAmountPrice;

    //选择的客户信息
    private ClientBean clientBean;
    private UserBean mUserBean;

    private List<GoodsBean> listGoods;
    private DisplayFeeSettlementAdapter adapter;

    private String om_ordercode = "";//订单号

    //陈列参数提交集合
    private List<DisplayFeeStrBean> orderApplyDetailStr = new ArrayList<>();

    private List<BluetoothDevice> printerDevices;

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
    protected int getContentView() {
        return R.layout.activity_display_fee_settlement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("陈列出库提交");

        clientBean = UserInfoUtils.getClientInfo(this);
        mUserBean = UserInfoUtils.getUser(this);
        listGoods = (List<GoodsBean>) getIntent().getSerializableExtra("listGoods");

        adapter = new DisplayFeeSettlementAdapter(1);
        mLvGoods.setAdapter(adapter);
        adapter.setData(listGoods);

        orderApplyDetailStr.clear();
        for (GoodsBean beanGoods : listGoods) {
            DisplayFeeStrBean bean = new DisplayFeeStrBean();
            bean.setEii_price_id(beanGoods.getGgp_id());
            bean.setEii_goodsId(beanGoods.getGgp_goodsid());
            bean.setEii_goods_name(beanGoods.getGg_title());
            bean.setEii_goods_price_max(beanGoods.getMaxPrice());
            bean.setEii_goods_price_min(beanGoods.getMinPrice());
            bean.setEii_goods_quantity_max(beanGoods.getMaxNum());
            bean.setEii_goods_quantity_min(beanGoods.getMinNum());
            orderApplyDetailStr.add(bean);
        }

        BigDecimal sum2 = BigDecimal.ZERO;
        if (listGoods.size() > 0) {
            for (GoodsBean bean : listGoods) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum2 = sum2.add(bdPrice);
            }
            mTvTotalPrice.setText(sum2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            mTvAmountPrice.setText(sum2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvTotalPrice.setText("0.00");
            mTvAmountPrice.setText("0.00");
        }
        mTvName.setText(clientBean.getCc_name());
        getCustomerBalance();
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
                DialogUtil.showCustomDialog(this, "提示", "确认提交吗?", "提交", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        //判断是否设置打印机
                        BigDecimal printNum;
                        if ("".equals(mEtPrintNum.getText().toString())) {
                            printNum = BigDecimal.ZERO;
                        } else {
                            printNum = new BigDecimal(mEtPrintNum.getText().toString());
                        }

                        if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                                (UserInfoUtils.getDeviceAddress(DisplayFeeSettlementActivity.this) == null ||
                                        "".equals(UserInfoUtils.getDeviceAddress(DisplayFeeSettlementActivity.this)))) {
                            //设置打印选项,且,没有设置默认打印机
                            DialogUtil.showCustomDialog(DisplayFeeSettlementActivity.this, "提示", "还未设置默认打印机,是否去设置打印机?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
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
                            displayGoodsOutStore();
                        }
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
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

    private void displayGoodsOutStore() {
        String orderListStr = JsonUtils.toJSONString(orderApplyDetailStr);
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    om_ordercode = jsonBean.getEim_code();
                    ToastUtils.showToastInCenterSuccess(DisplayFeeSettlementActivity.this, jsonBean.getRepMsg());

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

            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.displayGoodsOutStore, L_RequestParams.
                displayGoodsOutStore(clientBean.getCc_id(), orderListStr), true);
    }

    /**
     * 获取客户余额
     */
    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ClientBean clientBean = UserInfoUtils.getClientInfo(DisplayFeeSettlementActivity.this);
                    UserInfoUtils.setCustomerBalance(DisplayFeeSettlementActivity.this, jsonBean.getBalance());
                    UserInfoUtils.setAfterAmount(DisplayFeeSettlementActivity.this, jsonBean.getGcb_after_amount());
                    UserInfoUtils.setSafetyArrearsNum(DisplayFeeSettlementActivity.this, jsonBean.getCc_safety_arrears_num());

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
                getCustomerBalance(clientBean.getCc_id()), false);
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

                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTime(time);
                paramBean.setOrderCode(om_ordercode);
                paramBean.setUserBean(mUserBean);
                paramBean.setClientBean(clientBean);
                paramBean.setTotalAmount(mTvTotalPrice.getText().toString());
                paramBean.setPrintCode(false);
//                paramBean.setRemarks(mEtRemarks.getText().toString());

                BigDecimal printNum = new BigDecimal(mEtPrintNum.getText().toString());
                paramBean.setTitle("返陈列");
                paramBean.setType(15);
                paramBean.setT(listGoods);
                for (int i = 0; i < printNum.intValue(); i++) {
                    if (i > 0 && i < printNum.intValue()) {
                        Prints.printsDivider(socket);
                    }
                    boolean isSuccess = Prints.printOrder(socket, 15, clientBean, "返陈列", time, om_ordercode, mUserBean.getBud_name(),
                            mUserBean.getMobile(), listGoods, mTvTotalPrice.getText().toString(), false, "");
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
        Intent intent;
        intent = new Intent(DisplayFeeSettlementActivity.this, RolloutGoodsSuccessActivity.class);
        intent.putExtra("type", 4);//返陈列
        startActivity(intent);
        EventBus.getDefault().post(new DisplayFinishEvent());
        finishActivity();
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
}
