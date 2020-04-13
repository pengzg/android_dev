package com.xdjd.distribution.fragment;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.BankTypeAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.BankItemBean;
import com.xdjd.distribution.bean.BrandBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ReceiptBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.BrandPopup;
import com.xdjd.distribution.popup.ReceiptTypePopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.EditTextUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.utils.bluetooth.PrintUtil;
import com.xdjd.utils.bluetooth.Prints;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/12
 *     desc   : 收款fragment
 *     version: 1.0
 * </pre>
 */

public class ReceiptFragment extends BaseFragment implements BrandPopup.ItemOnListener, ReceiptTypePopup.ItemOnListener {

    @BindView(R.id.tv_payment_type)
    TextView mTvPaymentType;
    @BindView(R.id.et_cash)
    EditText mEtCash;
    @BindView(R.id.et_credit_card)
    EditText mEtCreditCard;
    @BindView(R.id.et_preferential)
    EditText mEtPreferential;
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
    @BindView(R.id.ll_ysk)
    LinearLayout mLlYsk;
    @BindView(R.id.iv_qk)
    ImageView mIvQk;
    @BindView(R.id.ll_qk)
    LinearLayout mLlQk;
    @BindView(R.id.tv_receipt_describe)
    TextView mTvReceiptDescribe;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.iv_dy)
    ImageView mIvDy;
    @BindView(R.id.ll_dy)
    LinearLayout mLlDy;

    private ReceiptTypePopup receiptTypePopup;

    /**
     * 收款类型 1 预收款 2 收欠款
     */
    private int receiptType = 1;

    private ClientBean mClientBean;

    public List<ReceiptBean> list;

    /**
     * 是否打印订单
     */
    private boolean isPrint = true;

    private List<BluetoothDevice> printerDevices;

    BigDecimal bigCash = BigDecimal.ZERO;//现金
    BigDecimal bigCard = BigDecimal.ZERO;//刷卡
    BigDecimal bigPreferential = BigDecimal.ZERO;//优惠

    BigDecimal xjAmount = BigDecimal.ZERO;//现金
    BigDecimal skAmoount = BigDecimal.ZERO;//刷卡
    BigDecimal yhAmount = BigDecimal.ZERO;//优惠

    private UserBean userBean;

    /**
     * 刷卡方式
     */
    private List<BankItemBean> listBank;

    /**
     * 刷卡方式id,如果没有的时候传递""
     */
    private String bankTypeId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        list = new ArrayList<>();

        selectReceiptType(receiptType);

        mClientBean = UserInfoUtils.getClientInfo(getActivity());

        queryCusBrandBalance(false, -1);

        EditTextUtil.setFocusChange(mEtCash);
        EditTextUtil.setFocusChange(mEtCreditCard);
        EditTextUtil.setFocusChange(mEtPreferential);

        if (userBean.getSkType().equals("N")){
            mEtCreditCard.setEnabled(false);
        }else{
            mEtCreditCard.setEnabled(true);
        }
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


    @OnClick({R.id.rl_payment_type, R.id.ll_ysk, R.id.ll_qk, R.id.tv_submit, R.id.ll_dy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_payment_type:
                showReceiptTypePopup();
                break;
            case R.id.ll_ysk://预收款
                receiptType = BaseConfig.ReceiptAdvancePayments;
                selectReceiptType(receiptType);
                break;
            case R.id.ll_qk://欠款
                receiptType = BaseConfig.ReceiptDebt;
                selectReceiptType(receiptType);
                break;
            case R.id.tv_submit://提交
                addReceipt();
                break;
            case R.id.ll_dy://是否打印
                if (isPrint) {
                    isPrint = false;
                    mIvDy.setImageResource(R.drawable.check_false);
                } else {
                    isPrint = true;
                    mIvDy.setImageResource(R.drawable.check_true);
                }
                break;
        }
    }

    private void selectReceiptType(int i) {
        if (i == BaseConfig.ReceiptAdvancePayments) {
            mIvYsk.setImageResource(R.drawable.check_true);
            mIvQk.setImageResource(R.drawable.check_false);
            mLlDebt.setVisibility(View.INVISIBLE);
        } else if (i == BaseConfig.ReceiptDebt) {
            mIvYsk.setImageResource(R.drawable.check_false);
            mIvQk.setImageResource(R.drawable.check_true);
            mLlDebt.setVisibility(View.VISIBLE);
        }
    }

    public void leftFinish() {
        if (list != null && list.size() > 0) {
            DialogUtil.showCustomDialog(getActivity(), "询问", "有数据未提交,确认退出吗?", "确认", "取消", new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                    finishActivity();
                }

                @Override
                public void no() {
                }
            });
        } else {
            finishActivity();
        }
    }

    /**
     * 添加收款信息
     */
    public void addReceipt() {
        ReceiptBean bean = null;

        BigDecimal bigDebt;//欠款bigdecimal
        BigDecimal bigCash;//现金
        BigDecimal bigCard;//刷卡
        BigDecimal bigPreferential;//优惠

        if (TextUtils.isEmpty(mEtCash.getText()) && TextUtils.isEmpty(mEtCreditCard.getText())) {
            DialogUtil.showCustomDialog(getActivity(), "注意", "输入的金额不能为空。", "确定", null, null);
            return;
        }

        if (receiptType == BaseConfig.ReceiptAdvancePayments) {//预收款

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

            if (TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")) {
                bigPreferential = new BigDecimal("0.00");
            } else {
                bigPreferential = new BigDecimal(mEtPreferential.getText().toString());
            }
        } else {
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

            if (TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")) {
                bigPreferential = new BigDecimal("0.00");
            } else {
                bigPreferential = new BigDecimal(mEtPreferential.getText().toString());
            }

            if (TextUtils.isEmpty(mTvDebt.getText())) {
                bigDebt = new BigDecimal("0.00");
            } else {
                bigDebt = new BigDecimal(mTvDebt.getText().toString());
            }

            if (bigDebt.compareTo(BigDecimal.ZERO) == 0){
                //-1表示小于,0是等于,1是大于
                if (bigCash.add(bigCard).add(bigPreferential).compareTo(bigDebt) == 1) {
                    DialogUtil.showCustomDialog(getContext(), "注意", "输入的金额大于了客户所欠款。", "确定", null, null);
                    return;
                }
            }
        }

        if (bigCard.compareTo(BigDecimal.ZERO) == 0 && bigCash.compareTo(BigDecimal.ZERO) == 0 &&
                bigPreferential.compareTo(BigDecimal.ZERO) == 0) {
            DialogUtil.showCustomDialog(getContext(), "注意", "输入的所有金额不能为0。", "确定", null, null);
            return;
        }

        if (bigCash.compareTo(BigDecimal.ZERO) == -1){
            showToast("现金金额不能小于0!");
            return;
        }

        if (bigCard.compareTo(BigDecimal.ZERO) == -1){
            showToast("刷卡金额不能小于0!");
            return;
        }

        list.clear();

        bean = new ReceiptBean();
        bean.setCashAmount(bigCash.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        bean.setCardAmount(bigCard.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        bean.setDisCountAmount(bigPreferential.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        bean.setType(receiptType);
        list.add(bean);

        //        calculateTotal();
        if (bigCard.compareTo(BigDecimal.ZERO) == 1) {
            if (listBank == null || listBank.size()==0){
                queryBankItemList();
            }else{
                DialogUtil.showDialogList(getActivity(), "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
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

    /**
     * 计算总的付款价格
     */
    private void calculateTotal() {
        for (ReceiptBean bean : list) {
            BigDecimal bdCash = new BigDecimal(bean.getCashAmount());
            bigCash = bigCash.add(bdCash);

            BigDecimal bdCard = new BigDecimal(bean.getCardAmount());
            bigCard = bigCard.add(bdCard);

            BigDecimal bdPreferential = new BigDecimal(bean.getDisCountAmount());
            bigPreferential = bigPreferential.add(bdPreferential);
        }

        StringBuilder str = new StringBuilder();
        if (bigCard.doubleValue() > 0) {
            str.append("刷卡:¥" + bigCard.setScale(2, BigDecimal.ROUND_HALF_UP) + ",");
        }
        if (bigCash.doubleValue() > 0) {
            str.append("收款:¥" + bigCash.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (bigPreferential.doubleValue() > 0) {
            str.append(",优惠:¥" + bigPreferential.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        mTvReceiptDescribe.setText(str);
    }

    /**
     * 没有欠款弹框提示
     */
    private void noDerbtDialog() {
        DialogUtil.showCustomDialog(getContext(), "注意", "客户没有欠款，不能进行收款。", "确定", null, null);
    }

    /*private void queryBaseBrand(final Boolean isShowPopup) {
        AsyncHttpUtil<BrandBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BrandBean.class, new IUpdateUI<BrandBean>() {
            @Override
            public void updata(BrandBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listBrand = jsonBean.getListData();
                        if (isShowPopup) {
                            brandPopup.setData(listBrand);
                            brandPopup.showAsDropDown(mRlBrand, 0, 0);
                        } else {
//                            queryCusBrandBalance(listBrand.get(0),false,-1);
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }
        });
        httpUtil.post(M_Url.queryBaseBrand, L_RequestParams.queryBaseBrand(UserInfoUtils.getId(getActivity())), true);
    }*/

    /**
     * 查询客户欠款
     */
    private void queryCusBrandBalance(final boolean isSelect, final int i) {
        AsyncHttpUtil<BrandBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BrandBean.class, new IUpdateUI<BrandBean>() {
            @Override
            public void updata(BrandBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvDebt.setText(jsonBean.getDebtBalance());
                    if (isSelect) {
                        mEtCash.setText(list.get(i).getCashAmount());
                        mEtCreditCard.setText(list.get(i).getCardAmount());
                        mEtPreferential.setText(list.get(i).getDisCountAmount());
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
        httpUtil.post(M_Url.queryCusBrandBalance, L_RequestParams.queryCusBrandBalance(
                userBean.getUserId(), mClientBean.getCc_id(), ""), true);
    }

    /**
     * 收款信息提交
     */
    private void makeCollections() {
        String glCollectionsStr = JsonUtils.toJSONString(list);

        AsyncHttpUtil<BrandBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BrandBean.class, new IUpdateUI<BrandBean>() {
            @Override
            public void updata(BrandBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    showToast(jsonBean.getRepMsg());
                    connectBluetooth();
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
//        httpUtil.post(M_Url.makeCollections, L_RequestParams.makeCollections(
//                userBean.getUserId(), mClientBean.getCc_id(), mEtRemarks.getText().toString(), glCollectionsStr, bankTypeId), true);
    }

    /**
     * 获取刷卡支付方式
     */
    private void queryBankItemList() {
        AsyncHttpUtil<BankItemBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BankItemBean.class, new IUpdateUI<BankItemBean>() {
            @Override
            public void updata(BankItemBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    listBank = jsonStr.getList();

                    DialogUtil.showDialogList(getActivity(), "选择刷卡方式", listBank, new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void item(int i) {
                            bankTypeId = listBank.get(i).getBi_id();
                            makeCollections();
                        }
                    });

                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("选择刷卡方式").setAdapter(new BankTypeAdapter(listBank), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    bankTypeId = listBank.get(i).getBi_id();
                                    makeCollections();
                                }
                            });*//*setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bankTypeId = listBank.get(which).getBi_id();
                                    makeCollections();
                                }
                            });*/
                    //                    builder.create().show();
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
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (!isPrint) {
            intentActivity();
        } else if (printerDevices == null || printerDevices.size() == 0) {
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
                    showToast(UIUtils.getString(R.string.ly_null_error));
                    intentActivity();
                    return;
                }

                if (TextUtils.isEmpty(mEtCash.getText()) || mEtCash.getText().toString().equals("-")){
                    xjAmount = BigDecimal.ZERO;
                }else{
                    xjAmount = new BigDecimal(mEtCash.getText().toString());
                }

                if (TextUtils.isEmpty(mEtCreditCard.getText()) || mEtCreditCard.getText().toString().equals("-")){
                    skAmoount = BigDecimal.ZERO;
                }else{
                    skAmoount = new BigDecimal(mEtCreditCard.getText().toString());
                }

                if (TextUtils.isEmpty(mEtPreferential.getText()) || mEtPreferential.getText().toString().equals("-")){
                    yhAmount = BigDecimal.ZERO;
                }else{
                    yhAmount = new BigDecimal(mEtPreferential.getText().toString());
                }

                String time = StringUtils.getDate();

                boolean isSuccess = Prints.printReceipt(socket, 1,"","", mClientBean,userBean.getBud_name(),
                        userBean.getMobile(), time, list, skAmoount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                        yhAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                        xjAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),"");
                if (!isSuccess) {
                    showToast(UIUtils.getString(R.string.ly_null_error));
                    intentActivity();
                    return;
                }

                intentActivity();
                break;
            case TASK_TYPE_CONNECT:
                break;
        }
    }

    private void intentActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        UserInfoUtils.setCustomerBalance(getActivity(), null);
        UserInfoUtils.setSafetyArrearsNum(getActivity(),null);
        UserInfoUtils.setAfterAmount(getActivity(),null);
    }

    @Override
    public void onItemReceiptType(int i) {
        receiptType = i;
        if (receiptType == BaseConfig.ReceiptAdvancePayments) {
            mTvPaymentType.setText("预收款");
            mLlDebt.setVisibility(View.INVISIBLE);
        } else if (receiptType == BaseConfig.ReceiptDebt) {
            mTvPaymentType.setText("欠款");
            mLlDebt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItem(int i) {
        //        queryCusBrandBalance(listBrand.get(i),false,-1);
    }

    private void showReceiptTypePopup() {
        receiptTypePopup.showAsDropDown(mRlPaymentType, 0, 0);
    }

}
