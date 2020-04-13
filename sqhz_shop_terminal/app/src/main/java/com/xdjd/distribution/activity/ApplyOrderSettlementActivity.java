package com.xdjd.distribution.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.BankItemBean;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.BaseOrderBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderApplyDetailBean;
import com.xdjd.distribution.bean.OrderDetailStrBean;
import com.xdjd.distribution.bean.PrintParamBean;
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
import com.xdjd.view.LastInputEditText;
import com.xdjd.view.toast.ToastUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import cn.baymax.android.keyboard.BaseKeyboard;
//import cn.baymax.android.keyboard.KeyboardManager;
//import cn.baymax.android.keyboard.NumberKeyboard;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/26
 *     desc   : 订货结算付款界面
 *     version: 1.0
 * </pre>
 */

public class ApplyOrderSettlementActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.iv_print_num_minus)
    ImageView mIvPrintNumMinus;
    @BindView(R.id.et_print_num)
    EditText mEtPrintNum;
    @BindView(R.id.iv_print_num_plus)
    ImageView mIvPrintNumPlus;
    @BindView(R.id.print_num_nameref)
    TextView mPrintNumNameref;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_delivery_money)
    TextView mTvDeliveryMoney;
    @BindView(R.id.et_credit_card)
    EditText mEtCreditCard;
    @BindView(R.id.et_cash)
    EditText mEtCash;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.rl_sk_del)
    RelativeLayout mRlSkDel;
    @BindView(R.id.rl_xj_del)
    RelativeLayout mRlXjDel;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.tv_receipt_describe)
    TextView mTvReceiptDescribe;
    @BindView(R.id.et_preferential)
    LastInputEditText mEtPreferential;
    @BindView(R.id.rl_yh_del)
    RelativeLayout mRlYhDel;

    /**
     * 订货商品
     */
    private List<GoodsBean> listGoods;
    /**
     * 选中客户的id
     */
    private String customerId;
    /**
     * 客户店铺名称
     */
    private String customer_name;
    /**
     * 传过来的订单总价格
     */
    private String amount;

    /**
     * 订单号
     */
    private String om_ordercode;

    private int om_ordertype;//订单类型
    private int listIndex;//订单集合下标-->只有配送任务挑转过来时才有

    private String orderDetailStr;//订单商品详情字段

    private BigDecimal totalPrice;//应收总金额


    //优惠输入之前的价格
    private String preBeforeAmount;
    //刷卡输入之前的价格
    private String cardBeforeAmount;
    //现金输入之前的价格
    private String cashBeforeAmount;

    BigDecimal yhAmount;//优惠
    BigDecimal skAmount;//刷卡
    BigDecimal xjAmount;//现金
    /**
     * 订货总金额
     */
    private BigDecimal totalAmount;

    /**
     * 蓝牙对象列表
     */
    private List<BluetoothDevice> printerDevices;

    /**
     * 刷卡方式
     */
    private List<BankItemBean> listBank;

    /**
     * 刷卡方式id,如果没有的时候传递""
     */
    private String bankTypeId = "";
    private UserBean userBean;
    private ClientBean clientBean;
    private EditText editText;
    /**
     * 业务模式	Y	1 代销 2 订货
     */
    private String businesstype;

//    private KeyboardManager keyboardManagerNumber;
//    private NumberKeyboard numberKeyboard;


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
        return R.layout.activity_apply_order_settlement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("结账打印");
        //        mTitleBar.setRightText("预览打印");
        //        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //            }
        //        });

        userBean = UserInfoUtils.getUser(this);

        customerId = getIntent().getStringExtra("customerId");
        customer_name = getIntent().getStringExtra("customer_name");
        amount = getIntent().getStringExtra("amount");
        om_ordertype = getIntent().getIntExtra("om_ordertype", 1);
        listIndex = getIntent().getIntExtra("listIndex", 0);

        businesstype = getIntent().getStringExtra("businesstype");

        mTitleBar.setTitle("结账打印-" + customer_name);

        mTvDeliveryMoney.setText("¥:" + amount);
        mEtCash.setText(amount);
        totalPrice = BigDecimal.ZERO;

        totalAmount = new BigDecimal(amount);

        initListData();

        if (om_ordertype == BaseConfig.OrderType3) {//退货状态不能进行刷卡
            mEtCreditCard.setEnabled(false);
        } else {
            mEtCreditCard.setEnabled(true);
        }

        skAmount = new BigDecimal(BigInteger.ZERO);
        xjAmount = new BigDecimal(BigInteger.ZERO);

        mEtPreferential.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (mEtPreferential.getText().length() > 0) {
                        mRlYhDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlYhDel.setVisibility(View.INVISIBLE);
                    }
                    preBeforeAmount = mEtPreferential.getText().toString();
                    mEtPreferential.addTextChangedListener(preferentialText);
                } else {
                    mRlYhDel.setVisibility(View.INVISIBLE);
                    mEtPreferential.removeTextChangedListener(preferentialText);
                }
            }
        });

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

        mLlLeft.setVisibility(View.VISIBLE);
        mEtPrintNum.setText("2");

//        keyboardManagerNumber = new KeyboardManager(this);
//        initNumberKeyboard();
//        keyboardManagerNumber.bindToEditor(mEtPreferential, numberKeyboard);
//        keyboardManagerNumber.bindToEditor(mEtCash, numberKeyboard);
//        keyboardManagerNumber.bindToEditor(mEtCreditCard, numberKeyboard);

    }

    /*private void initNumberKeyboard() {
        numberKeyboard = new NumberKeyboard(this,NumberKeyboard.DEFAULT_NUMBER_XML_LAYOUT);
        numberKeyboard.setActionDoneClickListener(new NumberKeyboard.ActionDoneClickListener() {
            @Override
            public void onActionDone(CharSequence charSequence) {
                *//*if(TextUtils.isEmpty(charSequence) || charSequence.toString().equals("0") || charSequence.toString().equals("0.0")) {
                    Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                }else {
                    //                    onNumberkeyActionDone();
                }*//*
            }
        });

        numberKeyboard.setKeyStyle(new BaseKeyboard.KeyStyle() {
            @Override
            public Drawable getKeyBackound(Keyboard.Key key) {
                if(key.iconPreview != null) {
                    return key.iconPreview;
                } else {
                    return ContextCompat.getDrawable(ApplyOrderSettlementActivity.this,R.drawable.key_number_bg);
                }
            }

            @Override
            public Float getKeyTextSize(Keyboard.Key key) {
                if(key.codes[0] == getResources().getInteger(R.integer.action_done)) {
                    return convertSpToPixels(ApplyOrderSettlementActivity.this, 20f);
                }
                return convertSpToPixels(ApplyOrderSettlementActivity.this, 24f);
            }

            @Override
            public Integer getKeyTextColor(Keyboard.Key key) {
                if(key.codes[0] == getResources().getInteger(R.integer.action_done)) {
                    return Color.WHITE;
                }
                return null;
            }

            @Override
            public CharSequence getKeyLabel(Keyboard.Key key) {
                return null;
            }
        });
    }*/

    public float convertSpToPixels(Context context, float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * 初始化订单列表数据
     */
    private void initListData() {
        List<OrderDetailStrBean> listOrderDetailStrBeen = new ArrayList<>();
        //订货
        clientBean = UserInfoUtils.getClientInfo(this);
        listGoods = (List<GoodsBean>) getIntent().getSerializableExtra("list");
        if (listGoods == null || listGoods.size() == 0) {
            finishActivity();
            return;
        }

        for (GoodsBean bean : listGoods) {
            OrderDetailStrBean strBean = new OrderDetailStrBean();
            strBean.setOd_goods_price_id(bean.getGgp_id());
            strBean.setOd_goods_num_max(bean.getMaxNum());
            strBean.setOd_goods_num_min(bean.getMinNum());

            if ("1".equals(bean.getGgp_unit_num())) {//如果但单位换算比是1,则将小单位价格赋给大单位
                strBean.setOd_price_max(bean.getMinPrice());
            } else {
                strBean.setOd_price_max(bean.getMaxPrice());
            }
            strBean.setOd_price_min(bean.getMinPrice());
            strBean.setOd_pricetype(bean.getSaleType());
            strBean.setOd_note(bean.getRemarks());
            strBean.setOd_price_strategyid(bean.getGps_id());
            if (bean.getOd_id() != null && bean.getOd_id().length() > 0) {
                strBean.setSource_id(bean.getOd_id());
            }

            listOrderDetailStrBeen.add(strBean);
        }

        orderDetailStr = JsonUtils.toJSONString(listOrderDetailStrBeen);
    }

    /**
     * 初始化结算参数
     */
    private void initAmount() {
        if (TextUtils.isEmpty(mEtPreferential.getText()) || "-".equals(mEtPreferential.getText().toString())
                || ".".equals(mEtPreferential.getText().toString())) {
            yhAmount = new BigDecimal("0");
        } else {
            yhAmount = new BigDecimal(mEtPreferential.getText().toString());
        }

        if (TextUtils.isEmpty(mEtCreditCard.getText()) || "-".equals(mEtCreditCard.getText().toString())
                || ".".equals(mEtCreditCard.getText().toString())) {
            skAmount = new BigDecimal("0");
        } else {
            skAmount = new BigDecimal(mEtCreditCard.getText().toString());
        }

        if (TextUtils.isEmpty(mEtCash.getText()) || "-".equals(mEtCash.getText().toString())
                || ".".equals(mEtCash.getText().toString())) {
            xjAmount = new BigDecimal("0");
        } else {
            xjAmount = new BigDecimal(mEtCash.getText().toString());
        }
    }

    //优惠
    private TextWatcher preferentialText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtPreferential.setText(s);
                    mEtPreferential.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtPreferential.setText(s);
                mEtPreferential.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEtPreferential.setText(s.subSequence(0, 1));
                    mEtPreferential.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            initAmount();
            //价格总和
            BigDecimal sumAmount = null;

            sumAmount = yhAmount.add(skAmount);
            if (TextUtils.isEmpty(mEtPreferential.getText())) {
                preBeforeAmount = editable.toString();
                //计算现金价格
                String cashStr = null;
                cashStr = totalAmount.subtract(skAmount).subtract(yhAmount).
                        setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtCash.setText(cashStr);
            } else if (sumAmount.compareTo(totalAmount) == 1 && totalAmount.compareTo(BigDecimal.ZERO) != -1) {
                DialogUtil.showCustomDialog(ApplyOrderSettlementActivity.this, "警告", "输入的优惠金额超过了现金金额。", "确定", null, null);
                resetFocusable(mEtPreferential, preBeforeAmount);
            } else {
                if (!"-".equals(editable.toString()) && !".".equals(editable.toString()))
                    preBeforeAmount = editable.toString();
                //计算现金价格
                String cashStr = null;
                cashStr = totalAmount.subtract(skAmount).subtract(yhAmount).
                        setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtCash.setText(cashStr);
            }
            if (editable.toString().length() > 0) {
                mRlYhDel.setVisibility(View.VISIBLE);
            } else {
                mRlYhDel.setVisibility(View.INVISIBLE);
            }
        }
    };

    //现金
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
            initAmount();
            if (editable.toString().length() > 0) {
                mRlXjDel.setVisibility(View.VISIBLE);
            } else {
                mRlXjDel.setVisibility(View.INVISIBLE);
            }

            if (TextUtils.isEmpty(mEtCash.getText())) {
                cashBeforeAmount = editable.toString();
                mEtCreditCard.setText(totalAmount.subtract(xjAmount).subtract(yhAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else if (xjAmount.compareTo(totalAmount) == 1) {//输入金额大于订货总金额
                DialogUtil.showCustomDialog(ApplyOrderSettlementActivity.this, "警告", "输入的金额超过了订货金额。", "确定", null, null);
                resetFocusable(mEtCash, cashBeforeAmount);
            } else {
                if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                    cashBeforeAmount = editable.toString();
                }
                mEtCreditCard.setText(totalAmount.subtract(xjAmount).subtract(yhAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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
            initAmount();
            if (editable.toString().length() > 0) {
                mRlSkDel.setVisibility(View.VISIBLE);
            } else {
                mRlSkDel.setVisibility(View.INVISIBLE);
            }

            if (TextUtils.isEmpty(mEtCreditCard.getText())) {
                cardBeforeAmount = editable.toString();
                mEtCash.setText(totalAmount.subtract(skAmount).subtract(yhAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else if (skAmount.add(yhAmount).compareTo(totalAmount) == 1) {//输入金额大于订货总金额
                DialogUtil.showCustomDialog(ApplyOrderSettlementActivity.this, "警告", "输入的金额超过了订货金额。", "确定", null, null);
                resetFocusable(mEtCreditCard, cardBeforeAmount);
            } else {
                if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                    cardBeforeAmount = editable.toString();
                }
                mEtCash.setText(totalAmount.subtract(skAmount).subtract(yhAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }
    };

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


    @OnClick({R.id.rl_sk_del, R.id.rl_xj_del, R.id.rl_yh_del, R.id.iv_print_num_minus, R.id.iv_print_num_plus, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sk_del:
                mEtCreditCard.setText("");
                break;
            case R.id.rl_xj_del:
                mEtCash.setText("");
                break;
            case R.id.rl_yh_del://优惠删除
                mEtPreferential.setText("");
                break;
            case R.id.iv_print_num_minus://打印数量减
                minusCalculation(mEtPrintNum);
                break;
            case R.id.iv_print_num_plus://打印数量加
                plusCalculation(mEtPrintNum);
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(mEtCreditCard.getText()) || "-".equals(mEtCreditCard.getText().toString())
                        || ".".equals(mEtCreditCard.getText().toString())) {
                    skAmount = new BigDecimal("0");
                } else {
                    skAmount = new BigDecimal(mEtCreditCard.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCash.getText()) || "-".equals(mEtCash.getText().toString())
                        || ".".equals(mEtCash.getText().toString())) {
                    xjAmount = new BigDecimal("0");
                } else {
                    xjAmount = new BigDecimal(mEtCash.getText().toString());
                }

                //判断是否设置打印机
                BigDecimal printNum;
                if ("".equals(mEtPrintNum.getText().toString())) {
                    printNum = BigDecimal.ZERO;
                } else {
                    printNum = new BigDecimal(mEtPrintNum.getText().toString());
                }

                if (printNum.compareTo(BigDecimal.ZERO) == 1 &&
                        (UserInfoUtils.getDeviceAddress(this) == null || "".equals(UserInfoUtils.getDeviceAddress(this)))) {
                    //设置打印选项,且,没有设置默认打印机
                    DialogUtil.showCustomDialog(this, "提示", "还未设置默认打印机,是否去设置打印机?", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            startActivity(PrintSettingActivity.class);
                        }

                        @Override
                        public void no() {
                        }
                    });
                } else {
                    if (skAmount.compareTo(BigDecimal.ZERO) == 1) {
                        if (listBank == null || listBank.size() == 0) {
                            queryBankItemList();
                        } else {
                            DialogUtil.showDialogList(ApplyOrderSettlementActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                                @Override
                                public void item(int i) {
                                    bankTypeId = listBank.get(i).getBi_id();
                                    applyOrder(bankTypeId);

                                }
                            });
                        }
                    } else {
                        applyOrder("");
                    }
                }
                break;
        }
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

                    DialogUtil.showDialogList(ApplyOrderSettlementActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void item(int i) {
                            bankTypeId = listBank.get(i).getBi_id();
                            applyOrder(bankTypeId);
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
     * 车销连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            showToast(UIUtils.getString(R.string.ly_null_error));
            finishActivity();
            eventLaunch();
        } else {
            String deviceAddress = UserInfoUtils.getDeviceAddress(this);
            if (!deviceAddress.equals("")) {
                for (int i = 0; i < printerDevices.size(); i++) {
                    if (deviceAddress.equals(printerDevices.get(i).getAddress())) {
                        if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
                            BluetoothUtil.turnOnBluetooth();
                        if (printerDevices.get(i) != null) {
                            super.connectDevice(printerDevices.get(i), TASK_TYPE_PRINT);
                        } else {
                            finishActivity();
                            eventLaunch();
                            showToast(UIUtils.getString(R.string.ly_null_error));
                        }
                        return;
                    }
                }
                finishActivity();
                eventLaunch();
            } else {
                finishActivity();
                eventLaunch();
                showToast(UIUtils.getString(R.string.ly_null_error));
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
                BigDecimal printNum = new BigDecimal(mEtPrintNum.getText().toString());
                //初始换付款参数
                initAmount();
                //实付金额
                String sfAmount = xjAmount.add(skAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();

                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTitle("订货");
                paramBean.setClientBean(clientBean);
                paramBean.setUserBean(userBean);
                paramBean.setTime(time);
                paramBean.setOrderCode(om_ordercode);
                paramBean.setT(listGoods);
                paramBean.setType(8);
                paramBean.setTotalAmount(amount);
                paramBean.setPrintCode(false);
                paramBean.setSkAmount(skAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setYhAmount(yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setXjAmount(xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setYeAmount("0.00");
                paramBean.setYsAmount("0.00");
                paramBean.setSfAmount(sfAmount);

                for (int i = 0; i < printNum.intValue(); i++) {

                    if (i > 0 && i < printNum.intValue()) {
                        Prints.printsDivider(socket);
                    }

//                    String orderTitle = "订货";//单据标题
//                    boolean isSuccess = Prints.printOrder(socket, 8, clientBean, orderTitle, time, om_ordercode, userBean.getBud_name(),
//                            userBean.getMobile(), listGoods, amount
//                            , skAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                            xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), "0.00",
//                            "0.00", sfAmount, false,null,"");
                    boolean isSuccess = PrintsCopy.printOrder(socket,paramBean);
                    if (!isSuccess) {
                        mHandler.sendEmptyMessage(PublicFinal.ERROR);
                        return;
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

    /**
     * 车销和配送任务的event数据发送
     */
    private void eventLaunch() {
        Intent intent = new Intent(ApplyOrderSettlementActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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
     * 订货、代销订单提交
     */
    private void applyOrder(String skType) {

        List<OrderApplyDetailBean> orderApplyList = new ArrayList();
        for (GoodsBean bean : listGoods) {
            OrderApplyDetailBean applyBean = new OrderApplyDetailBean();
            applyBean.setOad_goods_id(bean.getGgp_goodsid());
            applyBean.setOad_goods_price_id(bean.getGgp_id());
            applyBean.setOad_price_strategyid(bean.getGps_id());
            applyBean.setOad_pricetype(bean.getSaleType());
            applyBean.setOad_goods_num_max(bean.getMaxNum());
            applyBean.setOad_goods_num_min(bean.getMinNum());
            applyBean.setOad_price_max(bean.getMaxPrice());
            applyBean.setOad_price_min(bean.getMinPrice());

            orderApplyList.add(applyBean);
        }

        String orderApplyDetailStr = JsonUtils.toJSONString(orderApplyList);

        AsyncHttpUtil<BaseOrderBean> httpUtil = new AsyncHttpUtil<>(this, BaseOrderBean.class, new IUpdateUI<BaseOrderBean>() {
            @Override
            public void updata(BaseOrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    om_ordercode = jsonBean.getOrder_code();
                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        connectBluetooth();
                    } else {
                       eventLaunch();
                    }

                    ToastUtils.showToastInCenterSuccess(ApplyOrderSettlementActivity.this, jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.applyOrder, L_RequestParams.applyOrder(businesstype, customerId, "", mEtRemarks.getText().toString(), skType,
                TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-") ? "0.00" : mEtCreditCard.getText().toString(),
                TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-") ? "0.00" : mEtPreferential.getText().toString(),
                TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-") ? "0.00" : mEtCash.getText().toString(),
                "0.00", orderApplyDetailStr), true);
    }

}
