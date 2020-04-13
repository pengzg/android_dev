package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.RefundApplyActivity;
import com.xdjd.distribution.adapter.RefundAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.AddRefundBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.RefundRequireBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
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
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.quickindex.CharacterParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RefundSumbitFragment extends BaseFragment implements RefundAdapter.RefundListener {

    @BindView(R.id.tv_today_return_goods)
    TextView mTvTodayReturnGoods;
    @BindView(R.id.tv_return_goods_apply)
    TextView mTvReturnGoodsApply;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.lv_order_declare_goods)
    ListView mLvOrderDeclareGoods;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.check_all_goods)
    CheckBox mCheckAllGoods;
    @BindView(R.id.cb_print)
    CheckBox mCbPrint;

    private View view;

    public int index = 0;//0今日退货 1:明日退货

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    /**
     * 仓库id
     */
    public String storehouseId = "";

    /**
     * 今日退货
     */
    public List<GoodsBean> listTodayRefund = new ArrayList<>();

    /**
     * 退货申请
     */
    public List<GoodsBean> listRefund = new ArrayList<>();

    private RefundEditFragment todayFragment;

    private RefundApplyActivity context;

    private RefundAdapter adapter;

    private CharacterParser characterParser;

    /**
     * 订单编号
     */
    private String orderCode;

    private List<BluetoothDevice> printerDevices;

    //是否提交成功
    private boolean isSubmitTodayRefund = true;
    private boolean isSubmitRefundApply = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
                if (isSubmitRefundApply) {
                    showToast("订单提交成功");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finishActivity();
                } else {
                }
            } else if (msg.what == PublicFinal.ERROR){
                showToast(UIUtils.getString(R.string.ly_null_error));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_refund_sumbit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData(RefundEditFragment todayFragment) {
        this.todayFragment = todayFragment;

        index = BaseConfig.OrderType1;

        listTodayRefund = context.listTodayRefund;
        listRefund = context.listRefund;

        selectTab();
        updateAdapter();

        updateTabNum();
        mTvTotalPrice.setText(allAmountMoney());
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;

        clientBean = UserInfoUtils.getClientInfo(getActivity());
        storehouseId = getActivity().getIntent().getStringExtra("storehouseId");

        context = (RefundApplyActivity) getActivity();

        adapter = new RefundAdapter(this);
        mLvOrderDeclareGoods.setAdapter(adapter);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        listTodayRefund = context.listTodayRefund;
        listRefund = context.listRefund;

        selectTab();
        updateTabNum();

        mCheckAllGoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getAllGoodsList();
                } else {
                    context.listRefund.clear();
                    updateAdapter();
                    updateTabNum();
                    mTvTotalPrice.setText(allAmountMoney());
                }
            }
        });
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
        //推荐使用 BluetoothUtil.getPairedPrinterDevices()
        printerDevices = BluetoothUtil.getPairedDevices();
        LogUtils.e("收付款蓝牙长度", printerDevices.size() + "--");
    }

    /**
     * 根据订单状态刷新选中商品
     */
    private void updateAdapter() {
        switch (index) {
            case 0:
                adapter.setData(listTodayRefund);
                break;
            case 1:
                adapter.setData(listRefund);
                break;
        }
    }

    /**
     * 跟新tab显示选中商品数量
     */
    private void updateTabNum() {
        if (context.listTodayRefund.size() > 0) {
            mTvTodayReturnGoods.setText("今日退货(" + context.listTodayRefund.size() + ")");
        } else {
            mTvTodayReturnGoods.setText("今日退货");
        }

        if (context.listRefund.size() > 0) {
            mTvReturnGoodsApply.setText("退货申请(" + context.listRefund.size() + ")");
        } else {
            mTvReturnGoodsApply.setText("退货申请");
        }
    }

    @OnClick({R.id.tv_today_return_goods, R.id.tv_return_goods_apply, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_today_return_goods:
                index = 0;
                selectTab();
                break;
            case R.id.tv_return_goods_apply:
                index = 1;
                selectTab();
                break;
            case R.id.tv_submit:
                DialogUtil.showCustomDialog(getActivity(), "询问", "确认要提交数据吗?", "提交", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        if (listRefund.size() > 0) {
                            isSubmitRefundApply = false;
                            createOrder(listRefund, 1);
                        } else {
                            isSubmitRefundApply = true;
                        }
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    /**
     * 生成订单接口
     */
    private void createOrder(List<GoodsBean> list, final int index) {

        if (list == null || list.size() == 0) {
            return;
        }
        List<AddRefundBean> listAddRefund = new ArrayList<>();

        BigDecimal sum = BigDecimal.ZERO;
        for (GoodsBean bean : list) {
                AddRefundBean strBean = new AddRefundBean();
                strBean.setEii_price_id(bean.getGgp_id());
                strBean.setEii_goodsId(bean.getGgp_goodsid());
                strBean.setEii_goods_quantity_max(bean.getMaxNum());
                strBean.setEii_goods_price_max(bean.getMaxPrice());
                strBean.setEii_goods_quantity_min(bean.getMinNum());
                strBean.setEii_goods_price_min(bean.getMinPrice());
                strBean.setEii_goods_name(bean.getGg_title());
                strBean.setEii_goods_amount(bean.getTotalPrice());
                strBean.setEii_selltype_id(bean.getSaleType());

                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);

                listAddRefund.add(strBean);
        }

        String addRefundStr = JsonUtils.toJSONString(listAddRefund);
        Log.e("tag", addRefundStr);

        AsyncHttpUtil<RefundRequireBean> httpUtil = new AsyncHttpUtil<>(getActivity(), RefundRequireBean.class, new IUpdateUI<RefundRequireBean>() {
            @Override
            public void updata(RefundRequireBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
//                    if (index == 0) {
//                        isSubmitTodayRefund = true;
//                    } else if (index == 1) {
                        isSubmitRefundApply = true;
//                    }
//                    mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                    orderCode = jsonBean.getEim_code();
                    showToast(jsonBean.getRepMsg());
                    if (!mCbPrint.isChecked()) {
                        intentActivity();
                    } else {
                        connectBluetooth();
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
        httpUtil.post(M_Url.addRefund, L_RequestParams.
                addRefund(userBean.getUserId(), storehouseId,
                        sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), addRefundStr), true);
    }

    /**
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            showToast(UIUtils.getString(R.string.ly_null_error));
            intentActivity();
        } else {
            String deviceAddress = UserInfoUtils.getDeviceAddress(getActivity());

            for (int i = 0; i < printerDevices.size(); i++) {
                if (deviceAddress.equals(printerDevices.get(i).getAddress())) {
                    if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
                        BluetoothUtil.turnOnBluetooth();

                    if (printerDevices.get(i) != null) {
                        super.connectDevice(printerDevices.get(i), TASK_TYPE_PRINT);
                    } else {
                        showToast(UIUtils.getString(R.string.ly_null_error));
                        intentActivity();
                    }
                    return;
                }
            }

            showToast(UIUtils.getString(R.string.ly_null_error));
            intentActivity();
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
                paramBean.setTitle("退货申请");
                paramBean.setTime(time);
                paramBean.setOrderCode(orderCode);
                paramBean.setType(6);
                paramBean.setUserBean(userBean);
                paramBean.setClientBean(null);
                paramBean.setT(listRefund);
                paramBean.setTotalAmount(mTvTotalPrice.getText().toString());
                paramBean.setPrintCode(false);

                boolean isSuccess = Prints.printOrder(socket,6,null,"退货申请",time,orderCode,userBean.getBud_name(),
                        userBean.getMobile(),listRefund,mTvTotalPrice.getText().toString(),false,"");
                if (!isSuccess) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
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
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finishActivity();
    }

    /**
     * 停止提交提示
     */
    private void stopSubmit() {
        DialogUtil.showCustomDialog(getActivity(), "注意", "请输入要申报的产品信息!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {

            }

            @Override
            public void no() {

            }
        });
    }

    /**
     * 加载车库存所有商品
     */
    private void getAllGoodsList() {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    context.listRefund.clear();
                    context.listRefund = jsonBean.getListData();
                    if (context.listRefund != null && context.listRefund.size() > 0) {
                        for (int i = 0; i < context.listRefund.size(); i++) {
                            LogUtils.e("tag", i + "");
                            calculateGoods(context.listRefund.get(i));
                        }
                        listRefund = context.listRefund;
                        updateAdapter();
                        mTvTotalPrice.setText(allAmountMoney());
                        updateTabNum();
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
        httpUtil.post(M_Url.getAllGoodsList, L_RequestParams.
                getAllGoodsList(""), true);
    }

    /**
     * 根据库存计算商品大单位数量和小单位数量
     *
     * @param bean
     */
    private void calculateGoods(GoodsBean bean) {
        BigDecimal bigStore;
        if (bean.getGgs_stock() != null && !"".equals(bean.getGgs_stock())) {
            bigStore = new BigDecimal(bean.getGgs_stock());
        } else {
            bigStore = new BigDecimal(BigInteger.ZERO);
        }

        if ("1".equals(bean.getGgp_unit_num())){
            bean.setMaxNum("0");
            bean.setMinNum(bigStore.toString());
        }else{
            BigDecimal bigUnitNum = new BigDecimal(bean.getGgp_unit_num());

            BigDecimal[] results = bigStore.divideAndRemainder(bigUnitNum);
            bean.setMaxNum(String.valueOf(results[0].intValue()));
            bean.setMinNum(String.valueOf(results[1].intValue()));
        }

        bean.setMaxPrice(bean.getMax_price());
        bean.setMinPrice(bean.getMin_price());

        calculatePrice(bean);
    }

    /**
     * 根据计算单件商品总价格
     */
    private void calculatePrice(GoodsBean beanGoods) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;


        if (TextUtils.isEmpty(beanGoods.getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(beanGoods.getMaxPrice());
        }

        if (TextUtils.isEmpty(beanGoods.getMaxNum())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(beanGoods.getMaxNum());
        }

        if (TextUtils.isEmpty(beanGoods.getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(beanGoods.getMinPrice());
        }
        if (TextUtils.isEmpty(beanGoods.getMinNum())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(beanGoods.getMinNum());
        }

        if ("1".equals(beanGoods.getGgp_unit_num())){//大小单位换算比1==1,只显示小单位
            String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            beanGoods.setTotalPrice(sumPrice);
        }else{
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            String sumPrice = bdSumPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            beanGoods.setTotalPrice(sumPrice);
        }

        /*//1 都有 2 小单位 3 大单位
        if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
            if (TextUtils.isEmpty(beanGoods.getMaxPrice())) {
                bdMaxPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMaxPrice = new BigDecimal(beanGoods.getMaxPrice());
            }

            if (TextUtils.isEmpty(beanGoods.getMaxNum())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(beanGoods.getMaxNum());
            }

            if (TextUtils.isEmpty(beanGoods.getMinPrice())) {
                bdMinPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMinPrice = new BigDecimal(beanGoods.getMinPrice());
            }
            if (TextUtils.isEmpty(beanGoods.getMinNum())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(beanGoods.getMinNum());
            }

            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
        } else if ("2".equals(beanGoods.getUnit_type())) {
            if (TextUtils.isEmpty(beanGoods.getMinPrice())) {
                bdMinPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMinPrice = new BigDecimal(beanGoods.getMinPrice());
            }

            if (TextUtils.isEmpty(beanGoods.getMinNum())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(beanGoods.getMinNum());
            }

            bdSumPrice = bdMinPrice.multiply(bdMinNum);
        } else if ("3".equals(beanGoods.getUnit_type())) {
            if (TextUtils.isEmpty(beanGoods.getMaxPrice())) {
                bdMaxPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMaxPrice = new BigDecimal(beanGoods.getMaxPrice());
            }
            if (TextUtils.isEmpty(beanGoods.getMaxNum())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(beanGoods.getMaxNum());
            }

            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (bdSumPrice != null && bdSumPrice.doubleValue() > 0) {
            beanGoods.setTotalPrice(bdSumPrice.toString());
        } else {
            beanGoods.setTotalPrice("0");
        }*/
    }

    private void selectTab() {
        restoreTab();
        switch (index) {
            case 0:
                mTvTodayReturnGoods.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvTodayReturnGoods.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(0);
                mTvSelectedNum.setText("已选(" + context.listTodayRefund.size() + "件)");
                //                amountMoney(context.listTodayRefund);
                break;
            case 1:
                mTvReturnGoodsApply.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvReturnGoodsApply.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(1);
                mTvSelectedNum.setText("已选(" + context.listRefund.size() + "件)");
                //                amountMoney(context.listRefund);
                break;
        }
        updateAdapter();
    }


    /**
     * 计算所有订单状态的价格
     */
    private String allAmountMoney() {
        listTodayRefund = context.listTodayRefund;
        listRefund = context.listRefund;

        BigDecimal sum = BigDecimal.ZERO;
        if (listTodayRefund.size() > 0) {
            for (GoodsBean bean : listTodayRefund) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        if (listRefund.size() > 0) {
            for (GoodsBean bean : listRefund) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        return sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvTodayReturnGoods.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvTodayReturnGoods.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvReturnGoodsApply.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvReturnGoodsApply.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    }

    @Override
    public void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.getItem(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.getItem(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        mEtMaxNum.setText("0");
        mEtMinNum.setText("0");
        tvSumPrice.setText("¥0.00元");
        adapter.getItem(i).setMaxNum("0");
        adapter.getItem(i).setMinNum("0");
        adapter.getItem(i).setTotalPrice("0.00");
        mTvTotalPrice.setText(allAmountMoney());
    }

    @Override
    public void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        adapter.getItem(i).setMaxNum(num);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    @Override
    public void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        adapter.getItem(i).setMinNum(num);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(int i, EditText et, TextView tvSumPrice) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        et.setText((bd.intValue() + 1) + "");
    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private void minusCalculation(int i, EditText et, TextView tvSumPrice) {
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
    }

    /**
     * 计算adapter中item商品价格
     */
    private void calculatePrice(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格

        if (TextUtils.isEmpty(adapter.getItem(i).getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapter.getItem(i).getMaxPrice());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapter.getItem(i).getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapter.getItem(i).getMinPrice());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        if ("1".equals(adapter.getItem(i).getGgp_unit_num())){//大小单位换算比1==1,只显示小单位
            String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).setTotalPrice(sumPrice);
        }else{
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            String sumPrice = bdSumPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).setTotalPrice(sumPrice);
        }

        mTvTotalPrice.setText(allAmountMoney());
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 2
                                * index).setDuration(200).start();
    }

    public void searchGoods(String s) {
        filterData(s);
    }

    /**
     * 根据条件进行模糊查询
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GoodsBean> filterDateList = new ArrayList<GoodsBean>();

        if (TextUtils.isEmpty(filterStr)) {
            updateAdapter();
        } else {
            filterDateList.clear();
            List<GoodsBean> list = null;
            switch (index) {
                case BaseConfig.OrderType1:
                    list = listTodayRefund;
                    break;
                case BaseConfig.OrderType2:
                    list = listRefund;
                    break;
            }
            for (GoodsBean sortModel : list) {
                String name = sortModel.getGg_title();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }

            adapter.setData(filterDateList);
            if (filterDateList.size() == 0) {
                //			tvNofriends.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 二维码扫描商品结果
     *
     * @param bean
     */
    public void addGoods(GoodsBean bean) {
        List<GoodsBean> filterDateList = new ArrayList<GoodsBean>();

        List<GoodsBean> list = null;
        switch (index) {
            case BaseConfig.OrderType1:
                list = listTodayRefund;
                break;
            case BaseConfig.OrderType2:
                list = listRefund;
                break;
        }
        for (GoodsBean sortModel : list) {
            if (bean.getGgp_id().equals(sortModel.getGgp_id())) {
                filterDateList.add(sortModel);
            }
        }

        if (filterDateList.size() == 0) {
            adapter.setData(list);
        } else {
            adapter.setData(filterDateList);
        }
    }
}
