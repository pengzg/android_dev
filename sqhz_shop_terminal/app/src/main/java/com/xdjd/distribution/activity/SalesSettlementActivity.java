package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OrderSettlementAdapter;
import com.xdjd.distribution.adapter.SettlementGoodsAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.BankItemBean;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.DistributionGoodsBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.OrderDetailStrBean;
import com.xdjd.distribution.bean.OrderListBean;
import com.xdjd.distribution.bean.OrderSettlementBean;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.distribution.bean.PayTypeBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.ResultBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.DistributionEvent;
import com.xdjd.distribution.event.RolloutGoodsEvent;
import com.xdjd.distribution.event.RolloutGoodsFinishEvent;
import com.xdjd.distribution.event.TaskNumEvent;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.PayTypePopup;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.LogUtils;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/26
 *     desc   : 结算付款界面
 *     version: 1.0
 * </pre>
 */

public class SalesSettlementActivity extends BaseActivity implements PayTypePopup.ItemOnListener {
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
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_pay_type)
    TextView mTvPayType;
    @BindView(R.id.ll_pay_type)
    LinearLayout mLlPayType;
    @BindView(R.id.tv_customer_balance)
    TextView mTvCustomerBalance;
    @BindView(R.id.tv_delivery_money)
    TextView mTvDeliveryMoney;
    @BindView(R.id.et_credit_card)
    EditText mEtCreditCard;
    @BindView(R.id.et_preferential)
    EditText mEtPreferential;
    @BindView(R.id.et_cash)
    EditText mEtCash;
    @BindView(R.id.et_ye_amount)
    EditText mEtYeAmount;
    @BindView(R.id.et_ys_amount)
    EditText mEtYsAmount;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.rl_yh_del)
    RelativeLayout mRlYhDel;
    @BindView(R.id.rl_sk_del)
    RelativeLayout mRlSkDel;
    @BindView(R.id.rl_xj_del)
    RelativeLayout mRlXjDel;
    @BindView(R.id.rl_ye_del)
    RelativeLayout mRlYeDel;
    @BindView(R.id.rl_qk_del)
    RelativeLayout mRlQkDel;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
    @BindView(R.id.tv_hint)
    TextView mTvHint;

    private List<PayTypeBean> payTypeList;
    public PayTypePopup popupPayType;

    private PayTypeBean payTypeBean;

    /**
     * 车销商品
     */
    private List<OrderListBean> listOrderList;
    /**
     * 配送任务商品接口
     */
    private OrderBean mOrderBean;
    //配送任务传递的客户信息
    private CustomerTaskBean taskCustomer;
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
     * 是否是配送任务跳转过来的,true是
     */
    //    private boolean isDistribution;
    /**
     * 收款类型
     */
    private int businessType;
    /**
     * 未完成的所有铺货单数据
     */
    private PHOrderDetailBean beanRollout;
    //    private List<BrandAmountBean> listBrand;
    /**
     * 订单号
     */
    private String om_ordercode;
    private int om_ordertype;//订单类型
    private int listIndex;//订单集合下标-->只有配送任务挑转过来时才有
    private String orderDetailStr;//订单商品详情字段
    private BigDecimal totalPrice;//应收总金额
    /**
     * 刷卡价格再输入之前的价格字段
     */
    private String cardBeforePrice;
    /**
     * 优惠价格再输入之前的价格字段
     */
    private String preBeforePrice;
    /**
     * 收款现金金额
     */
    private String cashBeforePrice;
    /**
     * 余额价格
     */
    private String yeBeforePrice;
    /**
     * 应收价格
     */
    private String ysBeforePrice;

    BigDecimal skAmount;//刷卡
    BigDecimal yhAmount;//优惠
    BigDecimal xjAmount;//现金
    BigDecimal yeAmount;//余额
    BigDecimal ysAmount;//应收

    /**
     * 总的发货金额
     */
    private BigDecimal fhTotalAmount;
    /**
     * 客户余额
     */
    private BigDecimal cusYeAmount = BigDecimal.ZERO;
    private int etType;//编辑框类型: 1-刷卡,2-优惠,3-现金 ,4-余额,5-应收
    /**
     * 展示车销过来商品的adapter
     */
    private OrderSettlementAdapter adapterCar;
    private SettlementGoodsAdapter adapterDis;
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
    // 客户余额和预警欠款bean
    private CustomerBalanceBean customerBalanceBean;

    private EditText editText;
    private List<OrderSettlementBean> listSettlement;
    private OrderSettlementBean beanSettlement;
    /**
     * 订单商品
     */
    private List<GoodsBean> listGoodsOrder;
    /**
     * 退货商品
     */
    private List<GoodsBean> listRefundOrder;
    /**
     * 订单商品总金额
     */
    private String goodsOrderAmount = "0";
    /**
     * 退回商品总金额
     */
    private String refundOrderAmount = "0";

    /**
     * 业务模式	Y	1 代销 2 订货
     */
    private String businesstype;

    /**
     * 1普通
     * 2 （传仓库）退货申请 车销
     * 3要货申请
     * 4.客户盘点
     * 5.代销
     * 6订货
     */
    private String orderFrom;
    /**
     * 铺货销售商品json商品数据
     */
    private String phSaleJsonStr;
    /**
     * 铺货销售跳转过来的数据
     */
    private List<PHOrderDetailBean> listSaleGoods;
    private String imgStr;//客户签名图片
    private Bitmap bitmapSign=null;//签名图片

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
            } else if (msg.what == PublicFinal.ERROR) {
                if (businessType == Comon.BUSINESS_PHXS){
                    showToast("连接打印机失败,请到销售单详情中进行补打!");
                }else{
                    showToast(UIUtils.getString(R.string.ly_null_error));
                }
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_sales_settlement;
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
        businessType = getIntent().getIntExtra("businessType", Comon.BUSINESS_CX);
        amount = getIntent().getStringExtra("amount");
        om_ordertype = getIntent().getIntExtra("om_ordertype", 1);
        listIndex = getIntent().getIntExtra("listIndex", 0);
        listSettlement = (List<OrderSettlementBean>) getIntent().getSerializableExtra("listSettlement");
        beanSettlement = (OrderSettlementBean) getIntent().getSerializableExtra("beanSettlement");
        businesstype = getIntent().getStringExtra("businesstype");

        mTitleBar.setTitle("结账打印-" + customer_name);
        mLlPayType.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mLlPayType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopup();
            }
        });

        mTvDeliveryMoney.setText(amount);
        mEtCash.setText(amount);
        totalPrice = BigDecimal.ZERO;
        fhTotalAmount = new BigDecimal(amount);
        mTvCustomerBalance.setText(UserInfoUtils.getCustomerBalance(this));

        initListData();
        loadPayType(false);
        getCustomerBalance();
        //        getBrandAmountByGoods();
        if (om_ordertype == BaseConfig.OrderType3) {//退货状态不能进行刷卡
            mEtCreditCard.setEnabled(false);
        } else {
            mEtCreditCard.setEnabled(true);
        }

        skAmount = new BigDecimal(BigInteger.ZERO);
        yhAmount = new BigDecimal(BigInteger.ZERO);
        xjAmount = new BigDecimal(BigInteger.ZERO);
        yeAmount = new BigDecimal(BigInteger.ZERO);
        ysAmount = new BigDecimal(BigInteger.ZERO);

        setFocusChange(mEtCreditCard);
        setFocusChange(mEtPreferential);
        setFocusChange(mEtCash);
        setFocusChange(mEtYeAmount);
        setFocusChange(mEtYsAmount);

        mLlLeft.setVisibility(View.VISIBLE);
        mEtPrintNum.setText("2");
    }

    /**
     * 初始化订单列表数据
     */
    private void initListData() {
        List<OrderDetailStrBean> listOrderDetailStrBeen = new ArrayList<>();

        if (businessType == Comon.BUSINESS_RW) {//配送任务
            mOrderBean = (OrderBean) getIntent().getSerializableExtra("listOrderGoods");
            taskCustomer = (CustomerTaskBean) getIntent().getSerializableExtra("taskCustomer");

            clientBean = new ClientBean();

            clientBean.setCc_id(taskCustomer.getCc_id());
            clientBean.setCc_name(taskCustomer.getCc_name());
            clientBean.setCc_address(taskCustomer.getCc_address());
            clientBean.setCc_contacts_name(mOrderBean.getContact_name());
            clientBean.setCc_contacts_mobile(mOrderBean.getContact_mobile());

            if (mOrderBean == null) {
                finishActivity();
                return;
            }
            for (DistributionGoodsBean bean : mOrderBean.getOrderDetailVoList()) {
                if ("2".equals(bean.getOd_goodstype()))//请求接口时,去掉赠品
                    continue;

                OrderDetailStrBean strBean = new OrderDetailStrBean();
                strBean.setOd_goods_price_id(bean.getOd_goods_price_id());
                strBean.setOd_goods_num_max(bean.getOd_goods_num_max());
                strBean.setOd_goods_num_min(bean.getOd_goods_num_min());
                strBean.setOd_price_max(bean.getOd_price_max());
                strBean.setOd_price_min(bean.getOd_price_min());
                strBean.setOd_pricetype("");
                strBean.setOd_note("");
                strBean.setOd_price_strategyid(bean.getOd_price_strategyid());

                listOrderDetailStrBeen.add(strBean);
            }

            orderDetailStr = JsonUtils.toJSONString(listOrderDetailStrBeen);
            LogUtils.e("配送任务", orderDetailStr);
        } else if (businessType == Comon.BUSINESS_CX) {//车销
            clientBean = UserInfoUtils.getClientInfo(this);
            listOrderList = (List<OrderListBean>) getIntent().getSerializableExtra("list");
            if (listOrderList == null || listOrderList.size() == 0) {
                finishActivity();
                return;
            }


            for (OrderListBean bean : listOrderList) {
                switch (bean.getOrderType()) {
                    case BaseConfig.OrderType1:
                        listGoodsOrder = bean.getListData();
                        goodsOrderAmount = bean.getOrder_type_amount();
                        break;
                    case BaseConfig.OrderType3:
                        listRefundOrder = bean.getListData();
                        refundOrderAmount = bean.getOrder_type_amount();
                        break;
                }
            }
        } else if (businessType == Comon.BUSINESS_PHXS) {
            clientBean = UserInfoUtils.getClientInfo(this);
            phSaleJsonStr = getIntent().getStringExtra("phSaleJsonStr");
            listSaleGoods = (List<PHOrderDetailBean>) getIntent().getSerializableExtra("listSaleGoods");
        }
    }

    private void setFocusChange(final EditText et) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (et == mEtCreditCard) {
                        if (mEtCreditCard.getText().length() > 0) {
                            mRlSkDel.setVisibility(View.VISIBLE);
                        } else {
                            mRlSkDel.setVisibility(View.INVISIBLE);
                        }
                        etType = 1;
                        cardBeforePrice = mEtCreditCard.getText().toString();
                    } else if (et == mEtPreferential) {
                        if (mEtPreferential.getText().length() > 0) {
                            mRlYhDel.setVisibility(View.VISIBLE);
                        } else {
                            mRlYhDel.setVisibility(View.INVISIBLE);
                        }
                        etType = 2;
                        preBeforePrice = mEtPreferential.getText().toString();
                    } else if (et == mEtCash) {
                        if (mEtCash.getText().length() > 0) {
                            mRlXjDel.setVisibility(View.VISIBLE);
                        } else {
                            mRlXjDel.setVisibility(View.INVISIBLE);
                        }
                        etType = 3;
                        cashBeforePrice = mEtCash.getText().toString();
                    } else if (et == mEtYeAmount) {
                        if (mEtYeAmount.getText().length() > 0) {
                            mRlYeDel.setVisibility(View.VISIBLE);
                        } else {
                            mRlYeDel.setVisibility(View.INVISIBLE);
                        }
                        //余额
                        etType = 4;
                        yeBeforePrice = mEtYeAmount.getText().toString();
                    } else if (et == mEtYsAmount) {
                        if (mEtYsAmount.getText().length() > 0) {
                            mRlQkDel.setVisibility(View.VISIBLE);
                        } else {
                            mRlQkDel.setVisibility(View.INVISIBLE);
                        }
                        //应收
                        etType = 5;
                        ysBeforePrice = mEtYsAmount.getText().toString();
                    }
                    editText = et;
                    et.addTextChangedListener(mTextWatcher);
                    //                    et.setSelection(et.getText().length());
                    et.setSelectAllOnFocus(true);
                } else {
                    if (et == mEtCreditCard) {
                        mRlSkDel.setVisibility(View.INVISIBLE);
                    } else if (et == mEtPreferential) {
                        mRlYhDel.setVisibility(View.INVISIBLE);
                    } else if (et == mEtCash) {
                        mRlXjDel.setVisibility(View.INVISIBLE);
                    } else if (et == mEtYeAmount) {
                        mRlYeDel.setVisibility(View.INVISIBLE);
                    } else if (et == mEtYsAmount) {
                        mRlQkDel.setVisibility(View.INVISIBLE);
                    }
                    et.removeTextChangedListener(mTextWatcher);
                }
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            initAmount();
            //价格总和
            BigDecimal sumAmount = null;

            switch (etType) {
                case 1://刷卡
                    sumAmount = skAmount.add(yhAmount);
                    if (TextUtils.isEmpty(editable)) {
                        cardBeforePrice = editable.toString();
                        //计算现金价格
                        String cashStr = null;
                        if (payTypeBean.getBd_code().equals("3")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else if (payTypeBean.getBd_code().equals("4")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        }
                        mEtCash.setText(cashStr);
                        clearYeAndYsPrice();
                    } else if (sumAmount.compareTo(fhTotalAmount) == 1 && fhTotalAmount.compareTo(BigDecimal.ZERO) != -1) {
                        DialogUtil.showCustomDialog(SalesSettlementActivity.this, "警告", "输入的刷卡金额超过了发货金额。", "确定", null, null);
                        resetFocusable(mEtCreditCard, cardBeforePrice);
                    } else {
                        if (!"-".equals(editable.toString()) && !".".equals(editable.toString())) {
                            cardBeforePrice = editable.toString();
                        }

                        //计算现金价格
                        String cashStr = null;
                        if (payTypeBean.getBd_code().equals("3")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else if (payTypeBean.getBd_code().equals("4")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        }
                        mEtCash.setText(cashStr);
                        clearYeAndYsPrice();
                    }
                    if (editable.toString().length() > 0) {
                        mRlSkDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlSkDel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2://优惠
                    sumAmount = yhAmount.add(skAmount);
                    if (TextUtils.isEmpty(mEtPreferential.getText())) {
                        cardBeforePrice = editable.toString();
                        //计算现金价格
                        String cashStr = null;
                        if (payTypeBean.getBd_code().equals("3")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else if (payTypeBean.getBd_code().equals("4")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        }
                        mEtCash.setText(cashStr);
                        clearYeAndYsPrice();
                    } else if (sumAmount.compareTo(fhTotalAmount) == 1 && fhTotalAmount.compareTo(BigDecimal.ZERO) != -1) {
                        DialogUtil.showCustomDialog(SalesSettlementActivity.this, "警告", "输入的优惠金额超过了现金金额。", "确定", null, null);
                        resetFocusable(mEtPreferential, preBeforePrice);
                    } else {
                        if (!"-".equals(editable.toString()) && !".".equals(editable.toString()))
                            preBeforePrice = editable.toString();
                        //计算现金价格
                        String cashStr = null;
                        if (payTypeBean.getBd_code().equals("3")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else if (payTypeBean.getBd_code().equals("4")) {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        } else {
                            cashStr = fhTotalAmount.subtract(skAmount).subtract(yhAmount).
                                    setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        }
                        mEtCash.setText(cashStr);
                        clearYeAndYsPrice();
                    }
                    if (editable.toString().length() > 0) {
                        mRlYhDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlYhDel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 3://现金
                    sumAmount = xjAmount.add(skAmount).add(yhAmount);
                    if (TextUtils.isEmpty(editable)) {
                        cashBeforePrice = editable.toString();
                        if ("3".equals(payTypeBean.getBd_code())) {
                            calculateYE();
                        } else if ("4".equals(payTypeBean.getBd_code())) {
                            calculateYS();
                        } else {
                            calculateXjAndHh();
                        }
                    } else if (sumAmount.compareTo(fhTotalAmount) == 1 && fhTotalAmount.compareTo(BigDecimal.ZERO) != -1) {
                        DialogUtil.showCustomDialog(SalesSettlementActivity.this, "警告", "输入金额超过了发货金额。", "确定", null, null);
                        resetFocusable(mEtCash, cashBeforePrice);
                    } else {
                        if (!"-".equals(editable.toString()) && !".".equals(editable.toString()))
                            cashBeforePrice = editable.toString();
                        if (payTypeBean.getBd_code().equals("3")) {
                            calculateYE();
                        } else if (payTypeBean.getBd_code().equals("4")) {
                            calculateYS();
                        } else {
                            //混合和现金时价格计算
                            calculateXjAndHh();
                        }
                    }
                    if (editable.toString().length() > 0) {
                        mRlXjDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlXjDel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 4://余额
                    sumAmount = yhAmount.add(skAmount).add(xjAmount);
                    BigDecimal residueAmount = fhTotalAmount.subtract(sumAmount);//总发货金额减去刷卡、现金、优惠剩余的钱

                    if (TextUtils.isEmpty(editable)) {
                        yeBeforePrice = editable.toString();
                        mEtYsAmount.setText(residueAmount.subtract(yeAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else if (sumAmount.add(yeAmount).compareTo(fhTotalAmount) == 1 && fhTotalAmount.compareTo(BigDecimal.ZERO) != -1) {
                        DialogUtil.showCustomDialog(SalesSettlementActivity.this, "警告", "输入的余额超过了发货金额。", "确定", null, null);
                        resetFocusable(mEtYeAmount, yeBeforePrice);
                    } else if (yeAmount.compareTo(cusYeAmount) == 1) {
                        DialogUtil.showCustomDialog(SalesSettlementActivity.this, "警告", "输入的余额超过了可用余额。", "确定", null, null);
                        resetFocusable(mEtYeAmount, yeBeforePrice);
                    } else {
                        if (!"-".equals(editable.toString()) && !".".equals(editable.toString()))
                            yeBeforePrice = editable.toString();
                        mEtYsAmount.setText(residueAmount.subtract(yeAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                    if (editable.toString().length() > 0) {
                        mRlYeDel.setVisibility(View.VISIBLE);
                    } else {
                        mRlYeDel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 5:
                    break;
            }


            if (TextUtils.isEmpty(mEtYsAmount.getText()) || "-".equals(mEtYsAmount.getText().toString())
                    || ".".equals(mEtYsAmount.getText().toString())) {
                ysAmount = new BigDecimal("0");
            } else {
                ysAmount = new BigDecimal(mEtYsAmount.getText().toString());
            }

            if (ysAmount.compareTo(BigDecimal.ZERO) == -1) {//如果有欠款,且必须是负数,才显示转余额提示
                mTvHint.setVisibility(View.VISIBLE);
            } else {
                mTvHint.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 初始化结算参数
     */
    private void initAmount() {
        if (TextUtils.isEmpty(mEtCreditCard.getText()) || "-".equals(mEtCreditCard.getText().toString())
                || ".".equals(mEtCreditCard.getText().toString())) {
            skAmount = new BigDecimal("0");
        } else {
            skAmount = new BigDecimal(mEtCreditCard.getText().toString());
        }

        if (TextUtils.isEmpty(mEtPreferential.getText()) || "-".equals(mEtPreferential.getText().toString())
                || ".".equals(mEtPreferential.getText().toString())) {
            yhAmount = new BigDecimal("0");
        } else {
            yhAmount = new BigDecimal(mEtPreferential.getText().toString());
        }

        if (TextUtils.isEmpty(mEtCash.getText()) || "-".equals(mEtCash.getText().toString())
                || ".".equals(mEtCash.getText().toString())) {
            xjAmount = new BigDecimal("0");
        } else {
            xjAmount = new BigDecimal(mEtCash.getText().toString());
        }

        if (TextUtils.isEmpty(mEtYeAmount.getText()) || "-".equals(mEtYeAmount.getText().toString())
                || ".".equals(mEtYeAmount.getText().toString())) {
            yeAmount = new BigDecimal("0");
        } else {
            yeAmount = new BigDecimal(mEtYeAmount.getText().toString());
        }

        if (TextUtils.isEmpty(mEtYsAmount.getText()) || "-".equals(mEtYsAmount.getText().toString())
                || ".".equals(mEtYsAmount.getText().toString())) {
            ysAmount = new BigDecimal("0");
        } else {
            ysAmount = new BigDecimal(mEtYsAmount.getText().toString());
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

    /**
     * 计算现金变化是现金和混合状态变化
     */
    private void calculateXjAndHh() {
        mEtYeAmount.setText("");
        mEtYsAmount.setText("");
        calculateYE();
    }

    /**
     * 应付款时现金金额变化计算
     */
    private void calculateYS() {
        BigDecimal sumAmount = xjAmount.add(skAmount).add(yhAmount);
        BigDecimal bigYS = fhTotalAmount.subtract(sumAmount);//所需支付的货款金额

        mEtYsAmount.setText(bigYS.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    /**
     * 计算预收款时,从集合后面开始依次减余额
     */
    private void calculateYE() {
        BigDecimal sumAmount = xjAmount.add(skAmount).add(yhAmount);
        BigDecimal residueAmount = fhTotalAmount.subtract(sumAmount);//所需支付的剩余货款金额

        if (fhTotalAmount.compareTo(BigDecimal.ZERO) == -1) {
            //当发货金额为负数的时候
            mEtYsAmount.setText(residueAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            //客户余额>0时
            if (cusYeAmount.compareTo(BigDecimal.ZERO) == 1) {
                if (cusYeAmount.compareTo(residueAmount) == 1 || cusYeAmount.compareTo(residueAmount) == 0) {
                    //余额>=剩余金额
                    mEtYeAmount.setText(residueAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    mEtYsAmount.setText("");
                } else {
                    //余额<剩余金额,不足的移入应收款中
                    mEtYeAmount.setText(cusYeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    mEtYsAmount.setText(residueAmount.subtract(cusYeAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else {
                //客户余额<=0时,全部移入应收款
                String ysStr = residueAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtYsAmount.setText(ysStr);
            }
        }
    }

    /**
     * 清楚余额和应收的价格
     */
    private void clearYeAndYsPrice() {
        mEtYeAmount.setText("");
        mEtYsAmount.setText("");
    }

    @OnClick({R.id.rl_yh_del, R.id.rl_sk_del, R.id.rl_xj_del, R.id.rl_ye_del, R.id.rl_qk_del,
            R.id.iv_print_num_minus, R.id.iv_print_num_plus, R.id.ll_pay_type, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_yh_del:
                mEtPreferential.setText("");
                break;
            case R.id.rl_sk_del:
                mEtCreditCard.setText("");
                break;
            case R.id.rl_xj_del:
                mEtCash.setText("");
                break;
            case R.id.rl_ye_del:
                mEtYeAmount.setText("");
                break;
            case R.id.rl_qk_del:
                mEtYsAmount.setText("");
                break;
            case R.id.iv_print_num_minus://打印数量减
                minusCalculation(mEtPrintNum);
                break;
            case R.id.iv_print_num_plus://打印数量加
                plusCalculation(mEtPrintNum);
                break;
            case R.id.ll_pay_type:
                showPopup();
                break;
            case R.id.tv_submit:
                DialogUtil.showCustomDialog(this, "提示", "确认提交吗?", "提交", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        toSubmitOrder();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    private void toSubmitOrder() {
        BigDecimal customerYSAmount;//客户欠款
        BigDecimal customerYJAmount;//客户预警欠款金额

        if (TextUtils.isEmpty(mEtCreditCard.getText()) || "-".equals(mEtCreditCard.getText().toString())
                || ".".equals(mEtCreditCard.getText().toString())) {
            skAmount = new BigDecimal("0");
        } else {
            skAmount = new BigDecimal(mEtCreditCard.getText().toString());
        }

        if (TextUtils.isEmpty(mEtPreferential.getText()) || "-".equals(mEtPreferential.getText().toString())
                || ".".equals(mEtPreferential.getText().toString())) {
            yhAmount = new BigDecimal("0");
        } else {
            yhAmount = new BigDecimal(mEtPreferential.getText().toString());
        }

        if (TextUtils.isEmpty(mEtCash.getText()) || "-".equals(mEtCash.getText().toString())
                || ".".equals(mEtCash.getText().toString())) {
            xjAmount = new BigDecimal("0");
        } else {
            xjAmount = new BigDecimal(mEtCash.getText().toString());
        }

        if (TextUtils.isEmpty(mEtYeAmount.getText()) || "-".equals(mEtYeAmount.getText().toString())
                || ".".equals(mEtYeAmount.getText().toString())) {
            yeAmount = new BigDecimal("0");
        } else {
            yeAmount = new BigDecimal(mEtYeAmount.getText().toString());
        }

        if (TextUtils.isEmpty(mEtYsAmount.getText()) || "-".equals(mEtYsAmount.getText().toString())
                || ".".equals(mEtYsAmount.getText().toString())) {
            ysAmount = new BigDecimal("0");
        } else {
            ysAmount = new BigDecimal(mEtYsAmount.getText().toString());
        }

        if (customerBalanceBean == null) {
            customerBalanceBean = new CustomerBalanceBean();
        }

        //客户应收金额
        if (customerBalanceBean.getGcb_after_amount() == null || customerBalanceBean.getGcb_after_amount().equals("")) {
            customerYSAmount = BigDecimal.ZERO;
        } else {
            customerYSAmount = new BigDecimal(customerBalanceBean.getGcb_after_amount());
        }

        //客户安全欠款金额
        if (customerBalanceBean.getCc_safety_arrears_num() == null || customerBalanceBean.getCc_safety_arrears_num().equals("")) {
            customerYJAmount = BigDecimal.ZERO;
        } else {
            customerYJAmount = new BigDecimal(customerBalanceBean.getCc_safety_arrears_num());
        }
        //if--客户应收款+输入的客户应收款(绝对值)>预警应收金额
        LogUtils.e("应收金额", customerYSAmount.toString() + "--ysAmount" + ysAmount.toString() + "--customerYJAmount.abs()" + customerYJAmount.abs().toString());

        if (customerYSAmount.add(ysAmount).abs().compareTo(customerYJAmount.abs()) == 1
                && customerYJAmount.compareTo(BigDecimal.ZERO) == 1 && ysAmount.compareTo(BigDecimal.ZERO) == 1) {
            DialogUtil.showCustomDialog(this, "提示", "您的应收款已超出预警应收金额,请选择其他支付方式!", "确定", null, null);
            return;
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
            if (skAmount.compareTo(BigDecimal.ZERO) == 1) {
                if (listBank == null || listBank.size() == 0) {
                    queryBankItemList();
                } else {
                    DialogUtil.showDialogList(SalesSettlementActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void item(int i) {
                            bankTypeId = listBank.get(i).getBi_id();

                            if (businessType == Comon.BUSINESS_RW) {
                                deliveryPaymentTaskOrder(bankTypeId);
                            } else if (businessType == Comon.BUSINESS_CX) {
                                dialogSignCartOut(bankTypeId);
                            } else if (businessType == Comon.BUSINESS_PHXS) {//铺货销售
                                dialogCreatePhSaleOrder(bankTypeId);
                            }
                        }
                    });
                }
            } else {

                if (businessType == Comon.BUSINESS_RW) {
                    deliveryPaymentTaskOrder("");
                } else if (businessType == Comon.BUSINESS_CX) {
                    dialogSignCartOut("");
                } else if (businessType == Comon.BUSINESS_PHXS) {//铺货销售
                    dialogCreatePhSaleOrder("");
                }
            }
        }
    }

    /**
     * 车销弹框
     */
        private void dialogSignCartOut(final String skType){
        if ("Y".equalsIgnoreCase(userBean.getIsSign())) {
            if (imgStr != null && !"".equals(imgStr)) {
                createCarOut(skType);
            } else {
                DialogUtil.showInputSign(this, getResources(), new DialogUtil.OnInputSignListener() {
                    @Override
                    public void ok(String imgSign, Bitmap bitmap) {
                        imgStr = imgSign;
                        bitmapSign = bitmap;
                        createCarOut(skType);
                    }

                    @Override
                    public void no() {
                    }
                });
            }
        } else {
            createCarOut(skType);
        }
    }

    private void dialogCreatePhSaleOrder(final String skType) {
        if ("Y".equalsIgnoreCase(userBean.getIsSign())) {
            if (imgStr != null && !"".equals(imgStr)) {
                createPhSaleOrder(skType);
            } else {
                DialogUtil.showInputSign(this, getResources(), new DialogUtil.OnInputSignListener() {
                    @Override
                    public void ok(String imgSign, Bitmap bitmap) {
                        imgStr = imgSign;
                        bitmapSign = bitmap;
                        createPhSaleOrder(skType);
                    }

                    @Override
                    public void no() {
                    }
                });
            }
        } else {
            createPhSaleOrder(skType);
        }
    }

    /**
     * 生成铺货销售单
     *
     * @param skType
     */
    private void createPhSaleOrder(String skType) {
        AsyncHttpUtil<ResultBean> httpUtil = new AsyncHttpUtil<>(this, ResultBean.class, new IUpdateUI<ResultBean>() {
            @Override
            public void updata(ResultBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(SalesSettlementActivity.this, "订单提交成功!");
                    om_ordercode = jsonBean.getOm_ordercode();

                    EventBus.getDefault().post(new RolloutGoodsEvent());
                    EventBus.getDefault().post(new RolloutGoodsFinishEvent());

                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        loadRolloutData();//铺货销售完,打印未完结铺货单据
                    } else {
                        //                        Intent intent = new Intent(SalesSettlementActivity.this, RolloutGoodsActivity.class);
                        //                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        //                        startActivity(intent);
                        Intent intent = new Intent(SalesSettlementActivity.this, RolloutGoodsSuccessActivity.class);
                        intent.putExtra("type", 2);//销售
                        startActivity(intent);
                        finishActivity();
                    }
                } else {
                    DialogUtil.showCustomDialog(SalesSettlementActivity.this, "提示", jsonBean.getRepMsg(), "确定", null, null);
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
        httpUtil.post(M_Url.createPhSaleOrder, L_RequestParams.createPhSaleOrder(clientBean.getCc_id(), "6",
                phSaleJsonStr, "", payTypeBean.getBd_code(), skType,
                TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-") ? "0.00" : mEtCreditCard.getText().toString(),
                TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-") ? "0.00" : mEtPreferential.getText().toString(),
                TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-") ? "0.00" : mEtCash.getText().toString(),
                TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-") ? "0.00" : mEtYeAmount.getText().toString(),
                mEtYsAmount.getText().toString(), imgStr), true);
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

                    DialogUtil.showDialogList(SalesSettlementActivity.this, "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void item(int i) {
                            bankTypeId = listBank.get(i).getBi_id();

                            if (businessType == Comon.BUSINESS_RW) {
                                deliveryPaymentTaskOrder(bankTypeId);
                            } else if (businessType == Comon.BUSINESS_CX) {
                                dialogSignCartOut(bankTypeId);
                            } else if (businessType == Comon.BUSINESS_PHXS) {
                                dialogCreatePhSaleOrder(bankTypeId);
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

    /**
     * 获取客户余额
     */
    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ClientBean customer = UserInfoUtils.getClientInfo(SalesSettlementActivity.this);
                    //只有签到的客户名称和当前结算的客户名称相等的时--进行余额进行存储
                    if (customer != null && customer.getCc_id().equals(clientBean.getCc_id())) {
                        UserInfoUtils.setCustomerBalance(SalesSettlementActivity.this, jsonBean.getBalance());
                        UserInfoUtils.setAfterAmount(SalesSettlementActivity.this, jsonBean.getGcb_after_amount());
                        UserInfoUtils.setSafetyArrearsNum(SalesSettlementActivity.this, jsonBean.getCc_safety_arrears_num());
                    }
                    customerBalanceBean = jsonBean;

                    if (jsonBean.getBalance() != null && !"".equals(jsonBean.getBalance())) {
                        mTvCustomerBalance.setText(jsonBean.getBalance());
                    } else {
                        mTvCustomerBalance.setText("0.00");
                    }
                    cusYeAmount = new BigDecimal(mTvCustomerBalance.getText().toString());
                    if (cusYeAmount.compareTo(BigDecimal.ZERO) == 1) {
                        mEtYeAmount.setEnabled(true);
                    } else {
                        mEtYeAmount.setEnabled(false);
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                    finishActivity();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                finishActivity();
                showToast("客户余额获取失败");
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getCustomerBalance, L_RequestParams.
                getCustomerBalance(customerId), true);
    }

    @Override
    public void onItem(int i) {
        if (payTypeList.get(i).getBd_code().equals(payTypeBean.getBd_code())) {
            return;
        }

        payTypeBean = payTypeList.get(i);
        mTvPayType.setText(payTypeBean.getBd_name());

        if (yhAmount.compareTo(BigDecimal.ZERO) == -1) {
            preBeforePrice = "";
            resetFocusable(mEtPreferential, preBeforePrice);
        }

        if (TextUtils.isEmpty(mEtCreditCard.getText()) || "-".equals(mEtCreditCard.getText().toString())
                || ".".equals(mEtCreditCard.getText().toString())) {
            skAmount = new BigDecimal("0");
        } else {
            skAmount = new BigDecimal(mEtCreditCard.getText().toString());
        }

        if (TextUtils.isEmpty(mEtPreferential.getText()) || "-".equals(mEtPreferential.getText().toString())
                || ".".equals(mEtPreferential.getText().toString())) {
            yhAmount = new BigDecimal("0");
        } else {
            yhAmount = new BigDecimal(mEtPreferential.getText().toString());
        }

        if (TextUtils.isEmpty(mEtCash.getText()) || "-".equals(mEtCash.getText().toString())
                || ".".equals(mEtCash.getText().toString())) {
            xjAmount = new BigDecimal("0");
        } else {
            xjAmount = new BigDecimal(mEtCash.getText().toString());
        }

        if (TextUtils.isEmpty(mEtYeAmount.getText()) || "-".equals(mEtYeAmount.getText().toString())
                || ".".equals(mEtYeAmount.getText().toString())) {
            yeAmount = new BigDecimal("0");
        } else {
            yeAmount = new BigDecimal(mEtYeAmount.getText().toString());
        }

        if (TextUtils.isEmpty(mEtYsAmount.getText()) || "-".equals(mEtYsAmount.getText().toString())
                || ".".equals(mEtYsAmount.getText().toString())) {
            ysAmount = new BigDecimal("0");
        } else {
            ysAmount = new BigDecimal(mEtYsAmount.getText().toString());
        }

        //[{"bd_code":"1","bd_name":"混合收款"},{"bd_code":"2","bd_name":"优先现款"},
        // {"bd_code":"3","bd_name":"优先预收款"},{"bd_code":"4","bd_name":"优先应收款"}]
        if (payTypeBean.getBd_code().equals("1") || payTypeBean.getBd_code().equals("2")) {
            cardBeforePrice = "";
            cashBeforePrice = "";
            yeBeforePrice = "";
            ysBeforePrice = "";

            resetFocusable(mEtCreditCard, cardBeforePrice);
            resetFocusable(mEtCash, cashBeforePrice);
            resetFocusable(mEtYeAmount, yeBeforePrice);
            resetFocusable(mEtYsAmount, ysBeforePrice);

            mEtCash.setText(fhTotalAmount.subtract(yhAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

            mEtYeAmount.setEnabled(false);
        } else if (payTypeBean.getBd_code().equals("3")) {
            cardBeforePrice = "";
            cashBeforePrice = "";
            yeBeforePrice = "";
            ysBeforePrice = "";

            resetFocusable(mEtCreditCard, cardBeforePrice);
            resetFocusable(mEtCash, cashBeforePrice);
            resetFocusable(mEtYeAmount, yeBeforePrice);
            resetFocusable(mEtYsAmount, ysBeforePrice);

            payTypeYE();
            if (cusYeAmount.compareTo(BigDecimal.ZERO) == 1) {
                mEtYeAmount.setEnabled(true);
            } else {
                mEtYeAmount.setEnabled(false);
            }
        } else if (payTypeBean.getBd_code().equals("4")) {
            cardBeforePrice = "";
            cashBeforePrice = "";
            yeBeforePrice = "";
            ysBeforePrice = "";

            resetFocusable(mEtCreditCard, cardBeforePrice);
            resetFocusable(mEtCash, cashBeforePrice);
            resetFocusable(mEtYeAmount, yeBeforePrice);
            resetFocusable(mEtYsAmount, ysBeforePrice);

            payTypeYS();
            mEtYeAmount.setEnabled(false);
        }
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
     * 优先预收款
     */
    private void payTypeYE() {
        mEtCreditCard.setText("");
        mEtCash.setText("");
        mEtYsAmount.setText("");
        mEtYeAmount.setText("");

        BigDecimal residueAmount = fhTotalAmount.subtract(yhAmount);
        if (fhTotalAmount.compareTo(BigDecimal.ZERO) == -1) {
            //当发货金额为负数的时候
            String ysStr = residueAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            mEtYsAmount.setText(ysStr);
        } else {
            //客户余额>0时
            if (cusYeAmount.compareTo(BigDecimal.ZERO) == 1) {
                if (cusYeAmount.compareTo(residueAmount) == 1 || cusYeAmount.compareTo(residueAmount) == 0) {
                    //余额>=发货金额
                    mEtYeAmount.setText(residueAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    //余额<发货金额,不足的移入应收款中
                    mEtYeAmount.setText(cusYeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    mEtYsAmount.setText(residueAmount.subtract(cusYeAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else {
                //客户余额<=0时,全部移入应收款
                String ysStr = residueAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtYsAmount.setText(ysStr);
            }
        }
    }

    /**
     * 优先应收款
     */
    private void payTypeYS() {
        mEtCreditCard.setText("");
        mEtCash.setText("");
        mEtYeAmount.setText("");
        mEtYsAmount.setText("");

        String ysStr = fhTotalAmount.subtract(yhAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        mEtYsAmount.setText(ysStr);
    }

    private void initPopup() {
        popupPayType = new PayTypePopup(this, this, mLlPayType.getWidth());
        popupPayType.setData(payTypeList);
    }

    private void showPopup() {
        if (payTypeList != null && payTypeList.size() > 0) {
            popupPayType.setData(payTypeList);
            popupPayType.showAsDropDown(mLlPayType, 0, 0);
        } else {
            loadPayType(true);
        }
    }

    private void loadPayType(final boolean isDialog) {
        AsyncHttpUtil<PayTypeBean> httpUtil = new AsyncHttpUtil<>(this, PayTypeBean.class, new IUpdateUI<PayTypeBean>() {
            @Override
            public void updata(PayTypeBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        payTypeList = jsonBean.getDataList();
                        //                        popupPayType.setData(payTypeList);
                        if (isDialog) {
                            popupPayType.showAsDropDown(mLlPayType, 0, 0);
                        } else {
                            payTypeBean = payTypeList.get(0);
                            mTvPayType.setText(payTypeBean.getBd_name());
                        }
                    }
                } else {
                    if (isDialog)
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
        httpUtil.post(M_Url.getPayTypeList, L_RequestParams.getPayTypeList(userBean.getUserId()), isDialog);
    }

    /**
     * 车销出库生成单接口
     */
    private void createCarOut(String skType) {
        if (listOrderList == null || listOrderList.size() == 0) {
            finishActivity();
            return;
        }

        List<OrderDetailStrBean> cartOutlist = getCxStrBean(skType);
        String cartOutlistStr = JsonUtils.toJSONString(cartOutlist);

        AsyncHttpUtil<ResultBean> httpUtil = new AsyncHttpUtil<>(this, ResultBean.class, new IUpdateUI<ResultBean>() {
            @Override
            public void updata(ResultBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(SalesSettlementActivity.this, "订单提交成功!");
                    om_ordercode = jsonBean.getOm_ordercode();

                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        connectBluetooth();
                    } else {
                        Intent intent = new Intent(SalesSettlementActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finishActivity();
                        //                        EventBus.getDefault().post(new SalesOutboundEvent(om_ordertype));
                    }
                } else {
                    DialogUtil.showCustomDialog(SalesSettlementActivity.this, "提示", jsonBean.getRepMsg(), "确定", null, null);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                //                finishActivity();
                //                EventBus.getDefault().post(new SalesOutboundEvent(om_ordertype));
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.createListCarOut, L_RequestParams.createListCarOut(userBean.getUserId(), customerId, cartOutlistStr,imgStr), true);
        //                createCarOut(userBean.getUserId(), customerId, String.valueOf(om_ordertype), orderDetailStr,
        //                        "", payTypeBean.getBd_code(), skType,
        //                        TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-") ? "0.00" : mEtCreditCard.getText().toString(),
        //                        TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-") ? "0.00" : mEtPreferential.getText().toString(),
        //                        TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-") ? "0.00" : mEtCash.getText().toString(),
        //                        TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-") ? "0.00" : mEtYeAmount.getText().toString(),
        //                        mEtYsAmount.getText().toString()), true);
    }

    /**
     * 车销连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            if (businessType == Comon.BUSINESS_PHXS){
                showToast("连接打印机失败,请到销售单详情中进行补打!");
            }else{
                showToast(UIUtils.getString(R.string.ly_null_error));
            }
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
                            if (businessType == Comon.BUSINESS_PHXS){
                                showToast("连接打印机失败,请到销售单详情中进行补打!");
                            }else{
                                showToast(UIUtils.getString(R.string.ly_null_error));
                            }
                        }
                        return;
                    }
                }
                finishActivity();
                eventLaunch();
            } else {
                finishActivity();
                eventLaunch();
                if (businessType == Comon.BUSINESS_PHXS){
                    showToast("连接打印机失败,请到销售单详情中进行补打!");
                }else{
                    showToast(UIUtils.getString(R.string.ly_null_error));
                }
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

                String orderTitle = "";//单据标题
                switch (businessType) {
                    case Comon.BUSINESS_CX:
                        orderTitle = "销售单";
                    case Comon.BUSINESS_RW:
                        switch (om_ordertype) {
                            case BaseConfig.OrderType1:
                                orderTitle = "销售-销售单";
                                break;
                            case BaseConfig.OrderType2:
                                orderTitle = "销售-处理单";
                                break;
                            case BaseConfig.OrderType4:
                                orderTitle = "销售-换货单";
                                break;
                            case BaseConfig.OrderType3:
                                orderTitle = "销售-退货单";
                                break;
                            case BaseConfig.OrderType5:
                                orderTitle = "销售-还货单";
                                break;
                        }
                        break;
                    case Comon.BUSINESS_PHXS:
                        orderTitle = "铺货销售单";
                        break;
                }

                initAmount();
                //                String sfAmount = xjAmount.add(skAmount).add(yeAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                String sfAmount = xjAmount.add(skAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();


                PrintParamBean paramBean = new PrintParamBean();
                paramBean.setTitle(orderTitle);
                paramBean.setUserBean(userBean);
                paramBean.setClientBean(clientBean);
                paramBean.setTime(time);
                paramBean.setTotalAmount(fhTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setPrintCode(mCheckPrintGoodscode.isChecked() ? true : false);
                paramBean.setSkAmount(skAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setYhAmount(yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setXjAmount(xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setYeAmount(yeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setYsAmount(ysAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                paramBean.setSfAmount(sfAmount);

                if (businessType == Comon.BUSINESS_RW) {
                    //配送任务
                    paramBean.setType(3);
                    paramBean.setOrderCode(mOrderBean.getOm_ordercode());
                    paramBean.setT(listSettlement);

                    for (int i = 0; i < printNum.intValue(); i++) {

                        if (i > 0 && i < printNum.intValue()) {
                            Prints.printsDivider(socket);
                        }

//                        boolean isSuccess = Prints.printOrder(socket, 3, clientBean, orderTitle, time, mOrderBean.getOm_ordercode(), userBean.getBud_name(),
//                                userBean.getMobile(), listSettlement, fhTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
//                                , skAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                                xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                                ysAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), sfAmount,
//                                mCheckPrintGoodscode.isChecked() ? true : false, null, "");
                        boolean isSuccess = PrintsCopy.printOrder(socket,paramBean);
                        if (!isSuccess) {
                            mHandler.sendEmptyMessage(PublicFinal.ERROR);
                            return;
                        }
                    }
                } else if (businessType == Comon.BUSINESS_CX) {
                    //车销出库
                    paramBean.setType(2);
                    paramBean.setOrderCode(om_ordercode);
                    paramBean.setT(beanSettlement);
                    if (bitmapSign!=null){
                        paramBean.setSignImg(bitmapSign);
                    }

                    for (int i = 0; i < printNum.intValue(); i++) {
                        if (i > 0 && i < printNum.intValue()) {
                            Prints.printsDivider(socket);
                        }
//                        boolean isSuccess = Prints.printOrderList(socket, 2, clientBean, orderTitle, time, om_ordercode, userBean.getBud_name(),
//                                userBean.getMobile(), beanSettlement, fhTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
//                                , skAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                                xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                                ysAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), sfAmount, mCheckPrintGoodscode.isChecked() ? true : false);
                        boolean isSuccess = PrintsCopy.printOrderList(socket,paramBean);
                        if (bitmapSign!=null){
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!isSuccess) {
                            mHandler.sendEmptyMessage(PublicFinal.ERROR);
                            return;
                        }
                    }
                } else if (businessType == Comon.BUSINESS_PHXS) {//铺货销售
                    paramBean.setType(10);
                    paramBean.setOrderCode(om_ordercode);
                    paramBean.setT(listSaleGoods);
                    paramBean.setT1(beanRollout);
                    if (bitmapSign!=null){
                        paramBean.setSignImg(bitmapSign);
                    }

                    for (int i = 0; i < printNum.intValue(); i++) {
                        if (i > 0 && i < printNum.intValue()) {
                            Prints.printsDivider(socket);
                        }
//                        boolean isSuccess = Prints.printOrder(socket, 10, clientBean, orderTitle, time, om_ordercode, userBean.getBud_name(),
//                                userBean.getMobile(), listSaleGoods, fhTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
//                                , skAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                                xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), yeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
//                                ysAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), sfAmount,
//                                mCheckPrintGoodscode.isChecked() ? true : false, beanRollout, "");
                        boolean isSuccess = PrintsCopy.printOrder(socket,paramBean);
                        if (bitmapSign!=null){
                            try {
                                if (beanRollout!=null && beanRollout.getListData()!=null &&
                                        beanRollout.getListData().size()>0){
                                    Thread.sleep(10000);
                                }else{
                                    Thread.sleep(5000);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!isSuccess) {
                            mHandler.sendEmptyMessage(PublicFinal.ERROR);
                            return;
                        }
                    }
                }
                break;
            case TASK_TYPE_CONNECT:
                LogUtils.e("bluetooth", "PrintSettingActivity成功了");
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
        finishActivity();
        if (businessType == Comon.BUSINESS_RW) {
            EventBus.getDefault().post(new DistributionEvent(om_ordertype, listIndex));
            EventBus.getDefault().post(new TaskNumEvent());
        } else if (businessType == Comon.BUSINESS_CX) {
            //            EventBus.getDefault().post(new SalesOutboundEvent(om_ordertype));
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (businessType == Comon.BUSINESS_PHXS) {
            Intent intent = new Intent(SalesSettlementActivity.this, RolloutGoodsSuccessActivity.class);
            intent.putExtra("type", 2);//销售
            startActivity(intent);
        }
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
                    //                    showToast(bean.getRepMsg());
                    showToast("连接打印机失败,请到销售单详情中进行补打!");
                    finishActivity();
                    eventLaunch();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                //                showToast(s.getDetail());
                showToast("连接打印机失败,请到销售单详情中进行补打!");
                finishActivity();
                eventLaunch();
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.phOrderDetailList, L_RequestParams.phOrderDetailList(customerId, "N"), true);
    }

    /**
     * 配送任务生成单接口
     */
    private void deliveryPaymentTaskOrder(String skType) {

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

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(SalesSettlementActivity.this, "配送订单提交成功!");
                    BigDecimal printNum;
                    if ("".equals(mEtPrintNum.getText().toString())) {
                        printNum = BigDecimal.ZERO;
                    } else {
                        printNum = new BigDecimal(mEtPrintNum.getText().toString());
                    }

                    if (printNum.compareTo(BigDecimal.ZERO) == 1) {
                        connectBluetooth();
                    } else {
                        finishActivity();
                        EventBus.getDefault().post(new DistributionEvent(om_ordertype, listIndex));
                        EventBus.getDefault().post(new TaskNumEvent());
                    }
                } else {
                    DialogUtil.showCustomDialog(SalesSettlementActivity.this, "提示", jsonBean.getRepMsg(), "确定", null, null);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                finishActivity();
                EventBus.getDefault().post(new DistributionEvent(om_ordertype, listIndex));
                EventBus.getDefault().post(new TaskNumEvent());
                showToast(s.getDetail());
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.deliveryPaymentTaskOrder, L_RequestParams.
                deliveryPaymentTaskOrder(userBean.getUserId(), customerId, String.valueOf(om_ordertype), orderListStr,
                        "", payTypeBean.getBd_code(), skType,
                        TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-") ? "0.00" : mEtCreditCard.getText().toString(),
                        TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-") ? "0.00" : mEtPreferential.getText().toString(),
                        TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-") ? "0.00" : mEtCash.getText().toString(),
                        TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-") ? "0.00" : mEtYeAmount.getText().toString(),
                        mEtYsAmount.getText().toString()), true);
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

    private List<OrderDetailStrBean> getCxStrBean(String skType) {
        List<OrderDetailStrBean> cartOutlist = new ArrayList<>();
        //        om_ordertype	订单类型
        //        orderDetailStr	订单详情字段
        //        payType	支付方式
        //        skType	刷卡方式
        //        skAmount	刷卡金额
        //        yhAmount	优惠金额
        //        xjAmount	预收金额
        //ysAmount  应收金额
        if (fhTotalAmount.compareTo(BigDecimal.ZERO) == 1) {//总金额>0时

            if (((listRefundOrder == null || listRefundOrder.size() == 0) && (listGoodsOrder != null && listGoodsOrder.size() > 0))
                    || ((listRefundOrder != null || listRefundOrder.size() > 0) && (listGoodsOrder == null && listGoodsOrder.size() == 0))) {
                //当只有订单或退货商品时
                for (OrderListBean beanOrder : listOrderList) {
                    OrderDetailStrBean strBean = new OrderDetailStrBean();

                    if (beanOrder.getOrderType() == BaseConfig.OrderType1) {//订单金额拼装
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount(TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                ? "0.00" : mEtCreditCard.getText().toString());
                        strBean.setYhAmount(TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")
                                ? "0.00" : mEtPreferential.getText().toString());
                        strBean.setXjAmount(TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")
                                ? "0.00" : mEtCash.getText().toString());
                        strBean.setYeAmount(TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-")
                                ? "0.00" : mEtYeAmount.getText().toString());
                        strBean.setYsAmount(mEtYsAmount.getText().toString());
                    } else if (beanOrder.getOrderType() == BaseConfig.OrderType3) {//退货金额拼装
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount(TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                ? "0.00" : mEtCreditCard.getText().toString());
                        strBean.setYhAmount(TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")
                                ? "0.00" : mEtPreferential.getText().toString());
                        strBean.setXjAmount(TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")
                                ? "0.00" : mEtCash.getText().toString());
                        strBean.setYeAmount(TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-")
                                ? "0.00" : mEtYeAmount.getText().toString());
                        strBean.setYsAmount(mEtYsAmount.getText().toString());
                    } else {
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType("");
                        strBean.setPayType("");
                        strBean.setSkAmount("0.00");
                        strBean.setYhAmount("0.00");
                        strBean.setXjAmount("0.00");
                        strBean.setYeAmount("0.00");
                        strBean.setYsAmount("0.00");
                    }

                    cartOutlist.add(strBean);
                }
            } else if ((listGoodsOrder != null && listGoodsOrder.size() > 0) && (listRefundOrder != null && listRefundOrder.size() > 0)) {
                //总金额>0--订货和退货都有数据时
                for (OrderListBean beanOrder : listOrderList) {
                    OrderDetailStrBean strBean = new OrderDetailStrBean();
                    if (beanOrder.getOrderType() == BaseConfig.OrderType1) {//订单金额拼装

                        BigDecimal xjAmount;//订单现金收款金额
                        BigDecimal thAmount;//退货总金额
                        if (TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                || mEtCreditCard.getText().toString().equals(".")) {
                            xjAmount = BigDecimal.ZERO;
                        } else {
                            xjAmount = new BigDecimal(mEtCash.getText().toString());
                        }
                        if (refundOrderAmount == null || refundOrderAmount.length() == 0) {
                            thAmount = BigDecimal.ZERO;
                        } else {
                            thAmount = new BigDecimal(refundOrderAmount);
                        }

                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount(TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                ? "0.00" : mEtCreditCard.getText().toString());
                        strBean.setYhAmount(TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")
                                ? "0.00" : mEtPreferential.getText().toString());
                        strBean.setXjAmount(xjAmount.add(thAmount.abs()).toString());
                        strBean.setYeAmount(TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-")
                                ? "0.00" : mEtYeAmount.getText().toString());
                        strBean.setYsAmount(mEtYsAmount.getText().toString());
                    } else if (beanOrder.getOrderType() == BaseConfig.OrderType3) {//退货金额拼装
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount("0.00");
                        strBean.setYhAmount("0.00");
                        strBean.setXjAmount(beanOrder.getOrder_type_amount());
                        strBean.setYeAmount("0.00");
                        strBean.setYsAmount("0.00");
                    } else {
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType("");
                        strBean.setPayType("");
                        strBean.setSkAmount("0.00");
                        strBean.setYhAmount("0.00");
                        strBean.setXjAmount("0.00");
                        strBean.setYeAmount("0.00");
                        strBean.setYsAmount("0.00");
                    }
                    cartOutlist.add(strBean);
                }
            } else {
                for (OrderListBean beanOrder : listOrderList) {
                    OrderDetailStrBean strBean = new OrderDetailStrBean();
                    strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                    strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                    strBean.setSkType("");
                    strBean.setPayType("");
                    strBean.setSkAmount("0.00");
                    strBean.setYhAmount("0.00");
                    strBean.setXjAmount("0.00");
                    strBean.setYeAmount("0.00");
                    strBean.setYsAmount("0.00");

                    cartOutlist.add(strBean);
                }
            }

        } else if (fhTotalAmount.compareTo(BigDecimal.ZERO) == 0) {//总金额=0时
            for (OrderListBean beanOrder : listOrderList) {
                OrderDetailStrBean strBean = new OrderDetailStrBean();

                strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                strBean.setSkType(skType);
                strBean.setPayType(payTypeBean.getBd_code());
                strBean.setSkAmount("0.00");
                strBean.setYhAmount("0.00");
                strBean.setXjAmount(beanOrder.getOrder_type_amount());
                strBean.setYsAmount("0.00");

                cartOutlist.add(strBean);
            }
        } else {//总金额小于0时
            if (((listRefundOrder == null || listRefundOrder.size() == 0) && (listGoodsOrder != null && listGoodsOrder.size() > 0))
                    || ((listRefundOrder != null && listRefundOrder.size() > 0) && (listGoodsOrder == null || listGoodsOrder.size() == 0))) {
                //当只有订单或退货商品时
                for (OrderListBean beanOrder : listOrderList) {
                    OrderDetailStrBean strBean = new OrderDetailStrBean();

                    if (beanOrder.getOrderType() == BaseConfig.OrderType1) {//订单金额拼装
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount(TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                ? "0.00" : mEtCreditCard.getText().toString());
                        strBean.setYhAmount(TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")
                                ? "0.00" : mEtPreferential.getText().toString());
                        strBean.setXjAmount(TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")
                                ? "0.00" : mEtCash.getText().toString());
                        strBean.setYeAmount(TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-")
                                ? "0.00" : mEtYeAmount.getText().toString());
                        strBean.setYsAmount(mEtYsAmount.getText().toString());
                    } else if (beanOrder.getOrderType() == BaseConfig.OrderType3) {//退货金额拼装
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount(TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                ? "0.00" : mEtCreditCard.getText().toString());
                        strBean.setYhAmount(TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")
                                ? "0.00" : mEtPreferential.getText().toString());
                        strBean.setXjAmount(TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")
                                ? "0.00" : mEtCash.getText().toString());
                        strBean.setYeAmount(TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-")
                                ? "0.00" : mEtYeAmount.getText().toString());
                        strBean.setYsAmount(mEtYsAmount.getText().toString());
                    } else {
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType("");
                        strBean.setPayType("");
                        strBean.setSkAmount("0.00");
                        strBean.setYhAmount("0.00");
                        strBean.setXjAmount("0.00");
                        strBean.setYeAmount("0.00");
                        strBean.setYsAmount("0.00");
                    }

                    cartOutlist.add(strBean);
                }
            } else if ((listGoodsOrder != null && listGoodsOrder.size() > 0) && (listRefundOrder != null && listRefundOrder.size() > 0)) {
                //总金额<0--订货和退货都有数据时
                for (OrderListBean beanOrder : listOrderList) {
                    OrderDetailStrBean strBean = new OrderDetailStrBean();
                    if (beanOrder.getOrderType() == BaseConfig.OrderType1) {//订单金额拼装
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType("");
                        strBean.setPayType("");
                        strBean.setSkAmount("0.00");
                        strBean.setYhAmount("0.00");
                        strBean.setXjAmount(goodsOrderAmount);
                        strBean.setYeAmount("0.00");
                        strBean.setYsAmount("0.00");
                    } else if (beanOrder.getOrderType() == BaseConfig.OrderType3) {//退货金额拼装
                        BigDecimal xjAmount;//退货现金收款金额
                        BigDecimal ddAmount;//订单总金额
                        if (TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                || mEtCreditCard.getText().toString().equals(".")) {
                            xjAmount = BigDecimal.ZERO;
                        } else {
                            xjAmount = new BigDecimal(mEtCash.getText().toString());
                        }
                        if (goodsOrderAmount == null || goodsOrderAmount.length() == 0) {
                            ddAmount = BigDecimal.ZERO;
                        } else {
                            ddAmount = new BigDecimal(goodsOrderAmount);
                        }

                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType(skType);
                        strBean.setPayType(payTypeBean.getBd_code());
                        strBean.setSkAmount(TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")
                                ? "0.00" : mEtCreditCard.getText().toString());
                        strBean.setYhAmount(TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")
                                ? "0.00" : mEtPreferential.getText().toString());
                        strBean.setXjAmount(xjAmount.add(ddAmount.multiply(new BigDecimal(-1))).toString());
                        strBean.setYeAmount(TextUtils.isEmpty(mEtYeAmount.getText()) || mEtYeAmount.getText().toString().equals("-")
                                ? "0.00" : mEtYeAmount.getText().toString());
                        strBean.setYsAmount(mEtYsAmount.getText().toString());
                    } else {
                        strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                        strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                        strBean.setSkType("");
                        strBean.setPayType("");
                        strBean.setSkAmount("0.00");
                        strBean.setYhAmount("0.00");
                        strBean.setXjAmount("0.00");
                        strBean.setYeAmount("0.00");
                        strBean.setYsAmount("0.00");
                    }
                    cartOutlist.add(strBean);
                }
            } else {
                for (OrderListBean beanOrder : listOrderList) {
                    OrderDetailStrBean strBean = new OrderDetailStrBean();
                    strBean.setOm_ordertype(String.valueOf(beanOrder.getOrderType()));//订单类型
                    strBean.setOrderDetailStr(beanOrder.getListSettlement());//订单结算详情列表
                    strBean.setSkType("");
                    strBean.setPayType("0.00");
                    strBean.setSkAmount("0.00");
                    strBean.setYhAmount("0.00");
                    strBean.setXjAmount("0.00");
                    strBean.setYeAmount("0.00");
                    strBean.setYsAmount("0.00");

                    cartOutlist.add(strBean);
                }
            }
        }

        return cartOutlist;
    }

}
