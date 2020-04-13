package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.ReceivableCollectionAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.BankItemBean;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.BrandBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.BrandPopup;
import com.xdjd.distribution.popup.ReceiptTypePopup;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.EditTextUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.LogUtils;
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
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.LastInputEditText;
import com.xdjd.view.toast.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/28
 *     desc   : 收付款
 *     version: 1.0
 * </pre>
 */

public class ReceiptPaymentActivity extends BaseActivity implements BrandPopup.ItemOnListener, ReceiptTypePopup.ItemOnListener,
        ReceivableCollectionAdapter.OnReceivableListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_payment_type)
    TextView mTvPaymentType;
    @BindView(R.id.et_cash)
    EditText mEtCash;
    @BindView(R.id.et_credit_card)
    EditText mEtCreditCard;
    @BindView(R.id.rl_payment_type)
    RelativeLayout mRlPaymentType;
    @BindView(R.id.tv_debt)
    TextView mTvDebt;
    @BindView(R.id.ll_debt)
    LinearLayout mLlDebt;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.iv_ysk)
    ImageView mIvYsk;
    @BindView(R.id.iv_qk)
    ImageView mIvQk;
    @BindView(R.id.tv_receipt_describe)
    TextView mTvReceiptDescribe;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
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
    @BindView(R.id.rl_xj_del)
    RelativeLayout mRlXjDel;
    @BindView(R.id.rl_sk_del)
    RelativeLayout mRlSkDel;
    @BindView(R.id.lv_receivable)
    ListView mLvReceivable;
    @BindView(R.id.tv_ws_order_amount)
    TextView mTvWsOrderAmount;
    @BindView(R.id.ll_receivable)
    LinearLayout mLlReceivable;
    @BindView(R.id.ll_receivable_main)
    LinearLayout mLlReceivableMain;
    @BindView(R.id.iv_all)
    ImageView mIvAll;
    @BindView(R.id.ll_select)
    LinearLayout mLlSelect;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_ysk)
    TextView mTvYsk;
    @BindView(R.id.tv_qk)
    TextView mTvQk;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.ll_ysk)
    LinearLayout mLlYsk;
    @BindView(R.id.ll_qk)
    LinearLayout mLlQk;
    @BindView(R.id.et_discounts)
    LastInputEditText mEtDiscounts;
    @BindView(R.id.rl_yh_del)
    RelativeLayout mRlYhDel;
    @BindView(R.id.ll_discounts)
    LinearLayout mLlDiscounts;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;

    private ReceiptTypePopup receiptTypePopup;

    /**
     * 收款类型 1 预收款 2 收欠款
     */
    private int receiptType = 1;

    private ClientBean mClientBean;

    /**
     * 是否打印订单
     */
    private boolean isPrint = true;

    private List<BluetoothDevice> printerDevices;

    BigDecimal xjAmount = BigDecimal.ZERO;//现金
    BigDecimal skAmoount = BigDecimal.ZERO;//刷卡
    BigDecimal yhAmount = BigDecimal.ZERO;//优惠

    private UserBean userBean;

    /**
     * 订单编号
     */
    private String billCode = "";

    /**
     * 刷卡方式
     */
    private List<BankItemBean> listBank;

    /**
     * 刷卡方式id,如果没有的时候传递""
     */
    private String bankTypeId = "";

    //    private FragmentManager fm;
    //    private int currentTab = 0; // 当前Tab页面索引
    //    private List<Fragment> fragments;
    //    private ReceiptFragment mReceiptFragment;
    //    private PaymentFragment mPaymentFragment;

    private ReceivableCollectionAdapter adapter;
    private List<ReceivableListBean> listReceivable = new ArrayList<>();
    //刷卡输入之前的价格
    private String cardBeforeAmount;
    //现金输入之前的价格
    private String cashBeforeAmount;
    //优惠输入之前的价格
    private String discountsBeforeAmount;
    //是否全部选中
    private boolean isAll = false;
    /**
     * 是否加载收款列表成功
     */
    private boolean isLoddingSuccess = false;
    /**
     * 应收款列表加载容器
     */
    private VaryViewHelper helper = null;

    BigDecimal bigCash;//现金
    BigDecimal bigCard;//刷卡

    private BigDecimal safetyArrearsAmount;//安全欠款金额
    private BigDecimal afterAmount;//欠款金额
    private BigDecimal balanceAmount;//客户余额

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
            } else if (msg.what == PublicFinal.ERROR) {
                showToast(UIUtils.getString(R.string.ly_null_error));
            } else if (msg.what == 10){
                showToast(UIUtils.getString(R.string.ly_null_error));
                intentActivity();
                disProgressDialog();
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_receipt_payment;
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

        userBean = UserInfoUtils.getUser(this);
        receiptType = getIntent().getIntExtra("receiptType", 1);

        if (receiptType == 1){
            mTitleBar.setTitle("收预收款");
        }else{
            mTitleBar.setTitle("收欠款");
        }

        selectReceiptType(receiptType);
        mTvDebt.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View view, View view1) {
                mTvDebt.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
                if (receiptType == BaseConfig.ReceiptAdvancePayments) {
                    moveAnimation(mTvYsk);
                } else if (receiptType == BaseConfig.ReceiptDebt) {
                    moveAnimation(mTvQk);

                    loadDataReceivable();
                    mLlReceivable.setVisibility(View.VISIBLE);
                    mLlReceivableMain.setVisibility(View.VISIBLE);
                    mLlDiscounts.setVisibility(View.VISIBLE);
                }
            }
        });

        mClientBean = UserInfoUtils.getClientInfo(this);
        mTvCustomerName.setText("客户:" + mClientBean.getCc_name());

        getCustomerBalance();

        EditTextUtil.setFocusChange(mEtCash);
        EditTextUtil.setFocusChange(mEtCreditCard);

        mEtCash.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mEtCash.getText().length() > 0) {
                        mRlXjDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlXjDel.setVisibility(View.INVISIBLE);
                    }
                    mEtCash.addTextChangedListener(cashText);
                } else {
                    mRlXjDel.setVisibility(View.INVISIBLE);
                    mEtCash.removeTextChangedListener(cashText);
                }
            }
        });

        mEtCreditCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mEtCreditCard.getText().length() > 0) {
                        mRlSkDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlSkDel.setVisibility(View.INVISIBLE);
                    }
                    cardBeforeAmount = mEtCreditCard.getText().toString();
                    mEtCreditCard.addTextChangedListener(cardText);
                } else {
                    mRlSkDel.setVisibility(View.INVISIBLE);
                    mEtCreditCard.removeTextChangedListener(cardText);
                }
            }
        });

        mEtDiscounts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (mEtDiscounts.getText().length() > 0) {
                        mRlYhDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlYhDel.setVisibility(View.INVISIBLE);
                    }
                    discountsBeforeAmount = mEtDiscounts.getText().toString();
                    mEtDiscounts.addTextChangedListener(discountsText);
                } else {
                    mRlSkDel.setVisibility(View.INVISIBLE);
                    mEtDiscounts.removeTextChangedListener(discountsText);
                }
            }
        });

        mEtCreditCard.setEnabled(true);

        mLlLeft.setVisibility(View.VISIBLE);
        mEtPrintNum.setText("2");

        adapter = new ReceivableCollectionAdapter(this);
        mLvReceivable.setAdapter(adapter);

        helper = new VaryViewHelper(mLvReceivable);
    }

    private TextWatcher cashText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtCash.setText(s);
                    mEtCash.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtCash.setText(s);
                mEtCash.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEtCash.setText(s.subSequence(0, 1));
                    mEtCash.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                mRlXjDel.setVisibility(View.VISIBLE);
            } else {
                mRlXjDel.setVisibility(View.INVISIBLE);
            }

            if (receiptType == BaseConfig.ReceiptDebt) {//如果是欠款
                BigDecimal totalWsEtAmount;//选中的订单,未收款编辑框中的总价格
                BigDecimal cashAmount;//现金
                BigDecimal discountsAmouont;//优惠金额

                if (TextUtils.isEmpty(mTvWsOrderAmount.getText())) {
                    totalWsEtAmount = BigDecimal.ZERO;
                } else {
                    totalWsEtAmount = new BigDecimal(mTvWsOrderAmount.getText().toString());
                }

                if (TextUtils.isEmpty(editable) || "-".equals(editable.toString())
                        || ".".equals(editable.toString())) {
                    cashAmount = BigDecimal.ZERO;
                } else {
                    cashAmount = new BigDecimal(editable.toString());
                }

                if (TextUtils.isEmpty(mEtDiscounts.getText()) || "-".equals(mEtDiscounts.getText().toString())
                        || ".".equals(mEtDiscounts.getText().toString())) {
                    discountsAmouont = BigDecimal.ZERO;
                } else {
                    discountsAmouont = new BigDecimal(mEtDiscounts.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCash.getText())) {
                    cashBeforeAmount = editable.toString();
                    mEtCreditCard.setText(totalWsEtAmount.subtract(cashAmount).
                            subtract(discountsAmouont).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else if (cashAmount.subtract(discountsAmouont).compareTo(totalWsEtAmount) == 1) {//刷卡+订单 是否> 选中订单的编辑款中未收款总金额
                    DialogUtil.showCustomDialog(ReceiptPaymentActivity.this, "警告", "输入的金额超过了勾选订单输入的未收总金额。", "确定", null, null);
                    resetFocusable(mEtCash, cashBeforeAmount);
                } else {
                    if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                        cashBeforeAmount = editable.toString();
                    }
                    mEtCreditCard.setText(totalWsEtAmount.subtract(cashAmount).subtract(discountsAmouont).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }
    };

    private TextWatcher cardText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtCreditCard.setText(s);
                    mEtCreditCard.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtCreditCard.setText(s);
                mEtCreditCard.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEtCreditCard.setText(s.subSequence(0, 1));
                    mEtCreditCard.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                mRlSkDel.setVisibility(View.VISIBLE);
            } else {
                mRlSkDel.setVisibility(View.INVISIBLE);
            }

            if (receiptType == BaseConfig.ReceiptDebt) {//如果是欠款
                BigDecimal totalWsEtAmount;//选中的订单,未收款编辑框中的总价格
                BigDecimal cardAmount;//刷卡
                BigDecimal discountsAmouont;//优惠金额

                if (TextUtils.isEmpty(mEtDiscounts.getText()) || "-".equals(mEtDiscounts.getText().toString())
                        || ".".equals(mEtDiscounts.getText().toString())) {
                    discountsAmouont = BigDecimal.ZERO;
                } else {
                    discountsAmouont = new BigDecimal(mEtDiscounts.getText().toString());
                }

                if (TextUtils.isEmpty(mTvWsOrderAmount.getText())) {
                    totalWsEtAmount = BigDecimal.ZERO;
                } else {
                    totalWsEtAmount = new BigDecimal(mTvWsOrderAmount.getText().toString());
                }

                if (TextUtils.isEmpty(editable) || "-".equals(editable.toString())
                        || ".".equals(editable.toString())) {
                    cardAmount = BigDecimal.ZERO;
                } else {
                    cardAmount = new BigDecimal(editable.toString());
                }

                if (TextUtils.isEmpty(mEtCreditCard.getText())) {
                    cardBeforeAmount = editable.toString();
                    mEtCash.setText(totalWsEtAmount.subtract(cardAmount).subtract(discountsAmouont).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else if (cardAmount.add(discountsAmouont).compareTo(totalWsEtAmount) == 1) {//刷卡+订单 是否> 选中订单的编辑款中未收款总金额
                    DialogUtil.showCustomDialog(ReceiptPaymentActivity.this, "警告", "输入的金额超过了勾选订单输入的未收总金额。", "确定", null, null);
                    resetFocusable(mEtCreditCard, cardBeforeAmount);
                } else {
                    if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                        cardBeforeAmount = editable.toString();
                    }
                    mEtCash.setText(totalWsEtAmount.subtract(cardAmount).subtract(discountsAmouont).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }
    };

    /**
     * 优惠的监听
     */
    private TextWatcher discountsText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtDiscounts.setText(s);
                    mEtDiscounts.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtDiscounts.setText(s);
                mEtDiscounts.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEtDiscounts.setText(s.subSequence(0, 1));
                    mEtDiscounts.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                mRlYhDel.setVisibility(View.VISIBLE);
            } else {
                mRlYhDel.setVisibility(View.INVISIBLE);
            }

            if (receiptType == BaseConfig.ReceiptDebt) {//如果是欠款
                BigDecimal totalWsEtAmount;//选中的订单,未收款编辑框中的总价格
                BigDecimal cardAmount;//刷卡
                BigDecimal discountsAmouont;//优惠金额

                if (TextUtils.isEmpty(editable) || "-".equals(editable.toString())
                        || ".".equals(editable.toString())) {
                    discountsAmouont = BigDecimal.ZERO;
                } else {
                    discountsAmouont = new BigDecimal(editable.toString());
                }

                if (TextUtils.isEmpty(mTvWsOrderAmount.getText())) {
                    totalWsEtAmount = BigDecimal.ZERO;
                } else {
                    totalWsEtAmount = new BigDecimal(mTvWsOrderAmount.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCreditCard.getText()) || "-".equals(mEtCreditCard.getText().toString())
                        || ".".equals(mEtCreditCard.getText().toString())) {
                    cardAmount = BigDecimal.ZERO;
                } else {
                    cardAmount = new BigDecimal(mEtCreditCard.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCreditCard.getText())) {
                    discountsBeforeAmount = editable.toString();
                    mEtCash.setText(totalWsEtAmount.subtract(cardAmount).subtract(discountsAmouont).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else if (discountsAmouont.add(cardAmount).compareTo(totalWsEtAmount) == 1 && discountsAmouont.compareTo(BigDecimal.ZERO) != -1) {
                    DialogUtil.showCustomDialog(ReceiptPaymentActivity.this, "警告", "输入的金额超过了勾选订单输入的未收总金额。", "确定", null, null);
                    resetFocusable(mEtDiscounts, discountsBeforeAmount);
                } else {
                    if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                        discountsBeforeAmount = editable.toString();
                    }
                    mEtCash.setText(totalWsEtAmount.subtract(cardAmount).subtract(discountsAmouont).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        fillAdapter();
    }

    /**
     * 从所有已配对设备中找出打印设备并显示
     */
    private void fillAdapter() {
        //推荐使用 BluetoothUtil.getPairedPrinterDevices()
        printerDevices = BluetoothUtil.getPairedDevices();
        LogUtils.e("收付款蓝牙长度", printerDevices.size() + "--");
    }


    @OnClick({R.id.rl_payment_type, R.id.tv_submit, R.id.iv_print_num_minus,
            R.id.iv_print_num_plus, R.id.rl_sk_del, R.id.rl_xj_del, R.id.ll_select, R.id.tv_ysk, R.id.tv_qk, R.id.rl_yh_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_payment_type:
                showReceiptTypePopup();
                break;
           /* case R.id.ll_ysk://预收款
                receiptType = BaseConfig.ReceiptAdvancePayments;
                selectReceiptType(receiptType);
                break;
            case R.id.ll_qk://欠款
                receiptType = BaseConfig.ReceiptDebt;
                selectReceiptType(receiptType);
                break;*/
            case R.id.tv_ysk://预收款
                receiptType = BaseConfig.ReceiptAdvancePayments;
                mTvYsk.setTextColor(UIUtils.getColor(R.color.white));
                mTvQk.setTextColor(UIUtils.getColor(R.color.text_gray));
                moveAnimation(mTvYsk);

                adapter.setData(null);
                mEtCash.setText("");
                mEtCreditCard.setText("");
                mTvWsOrderAmount.setText("");

                //                mLlDebt.setVisibility(View.GONE);
                mLlReceivable.setVisibility(View.GONE);
                mLlReceivableMain.setVisibility(View.GONE);
                mLlDiscounts.setVisibility(View.GONE);
                break;
            case R.id.tv_qk://欠款
                receiptType = BaseConfig.ReceiptDebt;
                mTvQk.setTextColor(UIUtils.getColor(R.color.white));
                mTvYsk.setTextColor(UIUtils.getColor(R.color.text_gray));
                moveAnimation(mTvQk);

                //                mLlDebt.setVisibility(View.VISIBLE);
                loadDataReceivable();
                mLlReceivable.setVisibility(View.VISIBLE);
                mLlReceivableMain.setVisibility(View.VISIBLE);
                mLlDiscounts.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_submit://提交
                if (receiptType == BaseConfig.ReceiptAdvancePayments) {//预收
                    addReceipt();
                } else {

                    if (!isLoddingSuccess) {
                        DialogUtil.showCustomDialog(this, "提示", "请先加载出欠款列表再提交!", "确定", null, null);
                        return;
                    }

                    BigDecimal bigDebt;//欠款bigdecimal
                    BigDecimal bigCash;//现金
                    BigDecimal bigCard;//刷卡
                    bigDebt = new BigDecimal(mTvDebt.getText().toString());
                    //欠款
                    if (!TextUtils.isEmpty(mTvDebt.getText()) && bigDebt.doubleValue() == 0) {
                        noDerbtDialog();
                        return;
                    }

                    if (TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")) {
                        bigCash = new BigDecimal("0.00");
                    } else {
                        bigCash = new BigDecimal(mEtCash.getText().toString());
                    }

                    if (TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")) {
                        bigCard = new BigDecimal("0.00");
                    } else {
                        bigCard = new BigDecimal(mEtCreditCard.getText().toString());
                    }

                    if (TextUtils.isEmpty(mTvDebt.getText())) {
                        bigDebt = new BigDecimal("0.00");
                    } else {
                        bigDebt = new BigDecimal(mTvDebt.getText().toString());
                    }

                    if (bigDebt.compareTo(BigDecimal.ZERO) == 0) {
                        //-1表示小于,0是等于,1是大于
                        if (bigCash.add(bigCard).compareTo(bigDebt) == 1) {
                            DialogUtil.showCustomDialog(this, "注意", "输入的金额大于了客户所欠款。", "确定", null, null);
                            return;
                        }
                    }

                    if (bigCard.add(bigCash).compareTo(BigDecimal.ZERO) == 0) {
                        DialogUtil.showCustomDialog(this, "注意", "收欠款金额不能为0!", "确定", null, null);
                        return;
                    }

                    if (bigCard.compareTo(BigDecimal.ZERO) == 1) {
                        if (listBank == null || listBank.size() == 0) {
                            queryBankItemList();
                        } else {
                            DialogUtil.showDialogList(this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                                @Override
                                public void item(int i) {
                                    bankTypeId = listBank.get(i).getBi_id();
                                    makeReceiveYsk();
                                }
                            });
                        }
                    } else {
                        makeReceiveYsk();
                    }
                }
                break;
            case R.id.iv_print_num_minus://打印数量减
                minusCalculation(mEtPrintNum);
                break;
            case R.id.iv_print_num_plus://打印数量加
                plusCalculation(mEtPrintNum);
                break;
            case R.id.rl_sk_del:
                mEtCreditCard.setText("");
                break;
            case R.id.rl_xj_del:
                mEtCash.setText("");
                break;
            case R.id.rl_yh_del:
                mEtDiscounts.setText("");
                break;
            case R.id.ll_select://全选
                if (isAll) {
                    isAll = false;
                    mIvAll.setImageResource(R.drawable.check_false);
                    for (ReceivableListBean bean : listReceivable) {
                        bean.setIsSelect(1);
                    }
                } else {
                    isAll = true;
                    mIvAll.setImageResource(R.drawable.check_true);
                    for (ReceivableListBean bean : listReceivable) {
                        bean.setIsSelect(0);
                    }
                }
                adapter.setData(listReceivable);
                calculateWsTotalAmount();
                break;
        }
    }

    /**
     * 加载应收款列表
     */
    private void loadDataReceivable() {
        helper.showLoadingView();
        AsyncHttpUtil<ReceivableListBean> httpUtil = new AsyncHttpUtil<>(this, ReceivableListBean.class, new IUpdateUI<ReceivableListBean>() {
            @Override
            public void updata(ReceivableListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listReceivable = jsonBean.getListData();
                    if (listReceivable != null && listReceivable.size() > 0) {
                        for (ReceivableListBean bean : listReceivable) {
                            bean.setEt_ws_amount(bean.getWs_amount());
                        }
                        adapter.setData(listReceivable);
                        isSelectAll();
                        calculateWsTotalAmount();
                        helper.showDataView();
                        isLoddingSuccess = true;
                    } else {
                        isLoddingSuccess = true;
                        helper.showEmptyView("没有欠款信息!");
                    }
                } else {
                    helper.showErrorView(jsonBean.getRepMsg(), errorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(errorListener);
                isLoddingSuccess = false;
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getGlReceivableList, L_RequestParams.getGlReceivableList("9999", "1", mClientBean.getCc_id(),
                "", ""), true);
    }

    private OnErrorListener errorListener = new OnErrorListener();

    public class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadDataReceivable();
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

    private void selectReceiptType(int i) {
        if (i == BaseConfig.ReceiptAdvancePayments) {
            mIvYsk.setImageResource(R.drawable.check_true);
            mIvQk.setImageResource(R.drawable.check_false);
            //            mLlDebt.setVisibility(View.GONE);

            mTvYsk.setTextColor(UIUtils.getColor(R.color.white));
            mTvQk.setTextColor(UIUtils.getColor(R.color.text_gray));
        } else if (i == BaseConfig.ReceiptDebt) {
            mIvYsk.setImageResource(R.drawable.check_false);
            mIvQk.setImageResource(R.drawable.check_true);
            //            mLlDebt.setVisibility(View.VISIBLE);

            mTvQk.setTextColor(UIUtils.getColor(R.color.white));
            mTvYsk.setTextColor(UIUtils.getColor(R.color.text_gray));
        }
    }

    /**
     * 添加收款信息
     */
    public void addReceipt() {

        if (TextUtils.isEmpty(mEtCash.getText()) && TextUtils.isEmpty(mEtCreditCard.getText())) {
            DialogUtil.showCustomDialog(this, "注意", "输入的金额不能为空。", "确定", null, null);
            return;
        }

        if (TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")) {
            bigCash = new BigDecimal("0.00");
        } else {
            bigCash = new BigDecimal(mEtCash.getText().toString());
        }

        if (TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")) {
            bigCard = new BigDecimal("0.00");
        } else {
            bigCard = new BigDecimal(mEtCreditCard.getText().toString());
        }

        if (bigCard.compareTo(BigDecimal.ZERO) == 0 && bigCash.compareTo(BigDecimal.ZERO) == 0) {
            DialogUtil.showCustomDialog(this, "注意", "输入的所有金额不能为0。", "确定", null, null);
            return;
        }

        if (bigCash.compareTo(BigDecimal.ZERO) == -1) {
            showToast("现金金额不能小于0!");
            return;
        }

        if (bigCard.compareTo(BigDecimal.ZERO) == -1) {
            showToast("刷卡金额不能小于0!");
            return;
        }


        /*if (afterAmount.compareTo(BigDecimal.ZERO) == 1 &&
                (safetyArrearsAmount.compareTo(BigDecimal.ZERO) == 1 && bigCard.add(bigCash).compareTo(safetyArrearsAmount) == 1)) {
            //如果有欠款,且有安全欠款限制
            DialogUtil.showCustomDialog(this, "提示",
                    mClientBean.getCc_name() + "--已超出安全欠款金额" +
                            safetyArrearsAmount.subtract(bigCard.add(bigCash)).setScale(2, BigDecimal.ROUND_HALF_UP) + ",请先结清欠款金额",
                    "确定", null, new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            onClick(mTvQk);
                        }

                        @Override
                        public void no() {

                        }
                    });
        } else if (afterAmount.compareTo(BigDecimal.ZERO) == 1) {
            DialogUtil.showCustomDialog(this, "提示", mClientBean.getCc_name() + "--还有欠款没结清,是否先结清欠款?",
                    "去收欠款", "继续收预收", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            onClick(mTvQk);
                        }

                        @Override
                        public void no() {
                            if (bigCard.compareTo(BigDecimal.ZERO) == 1) {
                                if (listBank == null || listBank.size() == 0) {
                                    queryBankItemList();
                                } else {
                                    DialogUtil.showDialogList(ReceiptPaymentActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                                        @Override
                                        public void item(int i) {
                                            bankTypeId = listBank.get(i).getBi_id();
                                            makeCollections();
                                        }
                                    });
                                }
                            } else {
                                makeCollections();
                            }
                        }
                    });
        } else {*/
            if (bigCard.compareTo(BigDecimal.ZERO) == 1) {
                if (listBank == null || listBank.size() == 0) {
                    queryBankItemList();
                } else {
                    DialogUtil.showDialogList(ReceiptPaymentActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void item(int i) {
                            bankTypeId = listBank.get(i).getBi_id();
                            makeCollections();
                        }
                    });
                }
            } else {
                makeCollections();
            }
//        }
    }

    /**
     * 没有欠款弹框提示
     */
    private void noDerbtDialog() {
        DialogUtil.showCustomDialog(this, "注意", "客户没有欠款，不能进行收款。", "确定", null, null);
    }

    /**
     * 查询客户欠款
     */
    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvDebt.setText(jsonBean.getGcb_after_amount());
                    mTvBalance.setText(jsonBean.getBalance());
                    //余额
                    if (TextUtils.isEmpty(jsonBean.getBalance())) {
                        balanceAmount = BigDecimal.ZERO;
                    } else {
                        balanceAmount = new BigDecimal(jsonBean.getBalance());
                    }
                    //安全欠款
                    if (TextUtils.isEmpty(jsonBean.getBalance())) {
                        safetyArrearsAmount = BigDecimal.ZERO;
                    } else {
                        safetyArrearsAmount = new BigDecimal(jsonBean.getCc_safety_arrears_num());
                    }
                    //欠款
                    if (TextUtils.isEmpty(jsonBean.getBalance())) {
                        afterAmount = BigDecimal.ZERO;
                    } else {
                        afterAmount = new BigDecimal(jsonBean.getGcb_after_amount());
                    }

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
        httpUtil.post(M_Url.getCustomerBalance, L_RequestParams.
                getCustomerBalance(mClientBean.getCc_id()), true);
    }

    /**
     * 收款信息提交
     */
    private void makeCollections() {
        AsyncHttpUtil<BrandBean> httpUtil = new AsyncHttpUtil<>(this, BrandBean.class, new IUpdateUI<BrandBean>() {
            @Override
            public void updata(BrandBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(ReceiptPaymentActivity.this, jsonBean.getRepMsg());
                    billCode = jsonBean.getBillCode();

                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        connectBluetooth();
                    } else {
                        intentActivity();
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
        httpUtil.post(M_Url.makeCollections, L_RequestParams.makeCollections(
                userBean.getUserId(), mClientBean.getCc_id(), mEtRemarks.getText().toString(),
                bankTypeId, bigCash.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), bigCard.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), ""), true);
    }

    /**
     * 收取欠款提交
     */
    private void makeReceiveYsk() {
        BigDecimal discountsAmouont;//优惠金额字段
        if (TextUtils.isEmpty(mEtDiscounts.getText()) || "-".equals(mEtDiscounts.getText().toString())
                || ".".equals(mEtDiscounts.getText().toString())) {
            discountsAmouont = BigDecimal.ZERO;
        } else {
            discountsAmouont = new BigDecimal(mEtDiscounts.getText().toString());
        }

        List<ReceivableListBean> glReceivableListStr = new ArrayList();
        for (ReceivableListBean bean : listReceivable) {
            if (bean.getIsSelect() == 0) {//只有选中的才加进去
                ReceivableListBean copyBean = new ReceivableListBean();
                copyBean.setGr_id(bean.getGr_id());
                copyBean.setGr_version(bean.getGr_version());
                copyBean.setGr_this_amount(bean.getEt_ws_amount());
                glReceivableListStr.add(copyBean);
            } else {
                continue;
            }
        }
        String glYskStr = JsonUtils.toJSONString(glReceivableListStr);

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(ReceiptPaymentActivity.this, jsonBean.getRepMsg());
                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        connectBluetooth();
                    } else {
                        intentActivity();
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
        httpUtil.post(M_Url.makeReceiveYsk, L_RequestParams.makeReceiveYsk(this,
                mClientBean.getCc_id(), mEtCash.getText().toString(), mEtCreditCard.getText().toString(),
                discountsAmouont.setScale(2,BigDecimal.ROUND_HALF_UP).toEngineeringString(),
                mEtRemarks.getText().toString(), bankTypeId, glYskStr), true);
    }

    /**
     * 获取刷卡支付方式
     */
    private void queryBankItemList() {
        AsyncHttpUtil<BankItemBean> httpUtil = new AsyncHttpUtil<>(this, BankItemBean.class, new IUpdateUI<BankItemBean>() {
            @Override
            public void updata(BankItemBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    listBank = jsonStr.getList();

                    DialogUtil.showDialogList(ReceiptPaymentActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void item(int i) {
                            bankTypeId = listBank.get(i).getBi_id();
                            if (receiptType == BaseConfig.ReceiptAdvancePayments) {
                                makeCollections();
                            } else {
                                makeReceiveYsk();
                            }
                        }
                    });
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
        httpUtil.post(M_Url.queryBankItemList, L_RequestParams.queryBankItemList(userBean.getUserId()), true);
    }

    public class ThreadShow implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(4000);
                mHandler.sendEmptyMessage(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
       if (printerDevices == null || printerDevices.size() == 0) {
           showProgressDialog("请稍候...");
           new Thread(new ThreadShow()).start();
        } else {
            String deviceAddress = UserInfoUtils.getDeviceAddress(this);

            for (int i = 0; i < printerDevices.size(); i++) {
                if (deviceAddress.equals(printerDevices.get(i).getAddress())) {
                    if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
                        BluetoothUtil.turnOnBluetooth();

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

                if (TextUtils.isEmpty(mEtDiscounts.getText()) || mEtDiscounts.getText().toString().equals("-")) {
                    yhAmount = BigDecimal.ZERO;
                } else {
                    yhAmount = new BigDecimal(mEtDiscounts.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")) {
                    xjAmount = BigDecimal.ZERO;
                } else {
                    xjAmount = new BigDecimal(mEtCash.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")) {
                    skAmoount = BigDecimal.ZERO;
                } else {
                    skAmoount = new BigDecimal(mEtCreditCard.getText().toString());
                }

                String time = StringUtils.getDate();

                BigDecimal printNum = new BigDecimal(mEtPrintNum.getText().toString());
                for (int i = 0; i < printNum.intValue(); i++) {

                    if (i > 0 && i < printNum.intValue()) {
                        Prints.printsDivider(socket);
                    }

                    boolean isSuccess;
                    if (receiptType == BaseConfig.ReceiptAdvancePayments) {
                        isSuccess = Prints.printReceipt(socket, 1, "",billCode, mClientBean, userBean.getBud_name(),
                                userBean.getMobile(), time, null, skAmoount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                                yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                                xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), skAmoount.add(xjAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {//欠款
                        isSuccess = Prints.printReceiptYsk(socket, mClientBean, userBean.getBud_name(),
                                userBean.getMobile(), time, listReceivable,
                                yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                                skAmoount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                                xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                                skAmoount.add(xjAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                    if (!isSuccess) {
                        mHandler.sendEmptyMessage(PublicFinal.ERROR);
                        return;
                    }
                }

                try {
                    Thread.sleep(1000*4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case TASK_TYPE_CONNECT:
                break;
        }
    }

    @Override
    protected void onConnectionFinish() {
        super.onConnectionFinish();
        intentActivity();
    }

    private void intentActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        UserInfoUtils.setCustomerBalance(this, null);
        UserInfoUtils.setSafetyArrearsNum(this, null);
        UserInfoUtils.setAfterAmount(this, null);
    }

    @Override
    public void onItemReceiptType(int i) {
        receiptType = i;
        if (receiptType == BaseConfig.ReceiptAdvancePayments) {
            mTvPaymentType.setText("预收款");
            //            mLlDebt.setVisibility(View.INVISIBLE);
        } else if (receiptType == BaseConfig.ReceiptDebt) {
            mTvPaymentType.setText("欠款");
            //            mLlDebt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItem(int i) {
        //        queryCusBrandBalance(listBrand.get(i),false,-1);
    }

    private void showReceiptTypePopup() {
        receiptTypePopup.showAsDropDown(mRlPaymentType, 0, 0);
    }

    @Override
    public void onImgSelect(int i, ImageView mIvImg) {
        if (listReceivable.get(i).getIsSelect() == 0) {
            listReceivable.get(i).setIsSelect(1);
            mIvImg.setImageResource(R.drawable.check_false);
        } else {
            listReceivable.get(i).setIsSelect(0);
            mIvImg.setImageResource(R.drawable.check_true);
        }
        isSelectAll();
        calculateWsTotalAmount();
    }

    @Override
    public void onWsEtPrice(int index, String s, Editable editable, EditText mEtWsAmount, String wsBeforeAmount) {
        BigDecimal wsAmount = new BigDecimal(listReceivable.get(index).getWs_amount());
        BigDecimal etWsAmount;
        if (TextUtils.isEmpty(mEtWsAmount.getText()) || "-".equals(mEtWsAmount.getText().toString())
                || ".".equals(mEtWsAmount.getText().toString())) {
            etWsAmount = new BigDecimal("0");
        } else {
            etWsAmount = new BigDecimal(editable.toString());
        }

        if (TextUtils.isEmpty(mEtWsAmount.getText())) {
            adapter.wsBeforeAmount = editable.toString();
            //            mEtWsAmount.setText(editable.toString());
            listReceivable.get(index).setEt_ws_amount(editable.toString());
        } else if (etWsAmount.compareTo(wsAmount) == 1) {
            LogUtils.e("警告", "etWsAmount:" + etWsAmount.toString() + "--wsAmount:" + wsAmount.toString());
            DialogUtil.showCustomDialog(this, "警告", "输入的金额超过了这笔订单未收款金额。", "确定", null, null);
            resetFocusable(mEtWsAmount, wsBeforeAmount);
            listReceivable.get(index).setEt_ws_amount(wsBeforeAmount);
        } else {
            if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                adapter.wsBeforeAmount = editable.toString();
            }
            listReceivable.get(index).setEt_ws_amount(editable.toString());
        }
        calculateWsTotalAmount();
    }

    private void resetFocusable(EditText et, String str) {
        et.setFocusable(false);
        if (str != null) {
            et.setText(str);
            et.setSelection(et.getText().length());
        }

        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.findFocus();
    }

    /**
     * 计算选中的订单总欠款金额
     */
    private void calculateWsTotalAmount() {
        //计算选中的编辑框中的金额
        BigDecimal totalWsEtAmount = BigDecimal.ZERO;
        for (ReceivableListBean bean : listReceivable) {
            if (bean.getIsSelect() == 0) {
                BigDecimal wsEtAmount;
                if (bean.getEt_ws_amount() == null || "".equals(bean.getEt_ws_amount())) {
                    wsEtAmount = BigDecimal.ZERO;
                } else {
                    wsEtAmount = new BigDecimal(bean.getEt_ws_amount());
                }
                totalWsEtAmount = totalWsEtAmount.add(wsEtAmount);
            }
        }

        mTvWsOrderAmount.setText(totalWsEtAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        //计算现金金额
        mEtCash.setText(totalWsEtAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        mEtCreditCard.setText("0.00");
    }

    private void isSelectAll() {
        if (listReceivable == null || listReceivable.size() == 0) {
            isAll = false;
            mIvAll.setImageResource(R.drawable.check_false);
            return;
        }

        isAll = true;
        for (ReceivableListBean bean : listReceivable) {
            if (bean.getIsSelect() == 1) {
                isAll = false;
                mIvAll.setImageResource(R.drawable.check_false);
                return;
            }
        }
        mIvAll.setImageResource(R.drawable.check_true);
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.setDuration(400).start();
    }
}
