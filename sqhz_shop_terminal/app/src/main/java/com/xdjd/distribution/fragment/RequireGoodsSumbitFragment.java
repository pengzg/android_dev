package com.xdjd.distribution.fragment;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.RequireGoodsApplyActivity;
import com.xdjd.distribution.adapter.RefundAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.AddRefundBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.RefundRequireBean;
import com.xdjd.distribution.bean.UserBalanceBean;
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

public class RequireGoodsSumbitFragment extends BaseFragment implements RefundAdapter.RefundListener {

    @BindView(R.id.lv_order_declare_goods)
    ListView mLvOrderDeclareGoods;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.cb_print)
    CheckBox mCbPrint;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.tv_user_balance)
    TextView mTvUserBalance;
    @BindView(R.id.tv_saft_balance)
    TextView mTvSaftBalance;

    private View view;

    public int index = 0;//0今日退货 1:明日退货

    /**
     * 仓库id
     */
    public String storehouseId = "";


    /**
     * 要货申请
     */
    public List<GoodsBean> listRequire = new ArrayList<>();

    private RequireGoodsEditFragment editFragment;

    private RequireGoodsApplyActivity context;

    private RefundAdapter adapter;

    private CharacterParser characterParser;

    private List<BluetoothDevice> printerDevices;

    private UserBean userBean;

    /**
     * 订单编号
     */
    private String orderCode;


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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_require_goods_sumbit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData(RequireGoodsEditFragment editFragment) {
        this.editFragment = editFragment;

        index = BaseConfig.OrderType1;


        listRequire = context.listRequire;

        mTvSelectedNum.setText("已选(" + context.listRequire.size() + "件)");
        adapter.setData(listRequire);

        allAmountMoney();
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;

        storehouseId = getActivity().getIntent().getStringExtra("storehouseId");

        context = (RequireGoodsApplyActivity) getActivity();

        adapter = new RefundAdapter(this);
        mLvOrderDeclareGoods.setAdapter(adapter);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        listRequire = context.listRequire;

        mTvSelectedNum.setText("已选(" + context.listRequire.size() + "件)");

        mTvUsername.setText(userBean.getBud_name());
        queryCusBalance();
    }

    @OnClick({R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                DialogUtil.showCustomDialog(getActivity(), "询问", "确认要提交数据吗?", "提交", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        removeZeroPriceGoods();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    /**
     * 获取业务员余额、安全欠款
     */
    private void queryCusBalance() {
        AsyncHttpUtil<UserBalanceBean> httpUtil = new AsyncHttpUtil<>(getActivity(), UserBalanceBean.class, new IUpdateUI<UserBalanceBean>() {
            @Override
            public void updata(UserBalanceBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    mTvUserBalance.setText(jsonStr.getBalance());
                    if (jsonStr.getSaftBalance()!=null && jsonStr.getSaftBalance().length()>0){
                        mTvSaftBalance.setVisibility(View.VISIBLE);
                        mTvSaftBalance.setText("安全欠款: ¥"+jsonStr.getSaftBalance());
                    }else{
                        mTvSaftBalance.setVisibility(View.GONE);
                    }
                } else {
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.queryCusBalance, L_RequestParams.queryCusBalance(), true);
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
     * 删除价格为0的商品
     */
    private void removeZeroPriceGoods() {
        BigDecimal totalNum = BigDecimal.ZERO;
        if (listRequire.size() > 0) {
            for (int i = 0; i < listRequire.size(); i++) {

                BigDecimal maxNum = BigDecimal.ZERO;
                BigDecimal minNum = BigDecimal.ZERO;
                if ("1".equals(listRequire.get(i).getGgp_unit_num())) {
                    if (listRequire.get(i).getMinNum() != null && !"".equals(listRequire.get(i).getMinNum())) {
                        minNum = new BigDecimal(listRequire.get(i).getMinNum());
                    }
                } else {
                    if (listRequire.get(i).getMinNum() != null && !"".equals(listRequire.get(i).getMinNum())) {
                        minNum = new BigDecimal(listRequire.get(i).getMinNum());
                    }

                    if (listRequire.get(i).getMaxNum() != null && !"".equals(listRequire.get(i).getMaxNum())) {
                        maxNum = new BigDecimal(listRequire.get(i).getMaxNum());
                    }
                }
                totalNum = totalNum.add(maxNum).add(minNum);
            }
        }
        if (listRequire == null || listRequire.size() == 0 || totalNum.compareTo(BigDecimal.ZERO) == 0) {
            DialogUtil.showCustomDialog(getActivity(), "注意", "请录入要申报的产品信息!", "确定", null, null);
            return;
        }
        createOrder(listRequire, 0);
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
            BigDecimal bg = new BigDecimal(bean.getTotalPrice());
            if (bg.compareTo(BigDecimal.ZERO) == 1) {
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
        }

        String addRequireGoodsStr = JsonUtils.toJSONString(listAddRefund);
        Log.e("addRequireGoodsStr", addRequireGoodsStr);

        AsyncHttpUtil<RefundRequireBean> httpUtil = new AsyncHttpUtil<>(getActivity(), RefundRequireBean.class, new IUpdateUI<RefundRequireBean>() {
            @Override
            public void updata(RefundRequireBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
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
        httpUtil.post(M_Url.addRequireGoods, L_RequestParams.
                addRequireGoods(userBean.getUserId(), storehouseId,
                        sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), addRequireGoodsStr), true);
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
                paramBean.setTitle("要货申请");
                paramBean.setTime(time);
                paramBean.setOrderCode(orderCode);
                paramBean.setType(5);
                paramBean.setUserBean(userBean);
                paramBean.setClientBean(null);
                paramBean.setT(listRequire);
                paramBean.setTotalAmount(mTvTotalPrice.getText().toString());
                paramBean.setPrintCode(false);

                boolean isSuccess = Prints.printOrder(socket, 5, null, "要货申请", time, orderCode, userBean.getBud_name(),
                        userBean.getMobile(), listRequire, mTvTotalPrice.getText().toString(), false,"");
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
     * 计算所有订单状态的价格
     */
    private void allAmountMoney() {
        listRequire = context.listRequire;

        BigDecimal sum = BigDecimal.ZERO;
        if (listRequire.size() > 0) {
            for (GoodsBean bean : listRequire) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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
        allAmountMoney();
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

        if ("1".equals(adapter.getItem(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).setTotalPrice(sumPrice);
        }

        allAmountMoney();
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
            adapter.setData(listRequire);
        } else {
            filterDateList.clear();
            List<GoodsBean> list = null;
            list = listRequire;
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
        list = listRequire;
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
