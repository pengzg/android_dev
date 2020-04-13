package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.VoucherReprintAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.OutboundDetailBean;
import com.xdjd.distribution.bean.PrintGlCashBean;
import com.xdjd.distribution.bean.PrintListBean;
import com.xdjd.distribution.bean.PrintOrderBean;
import com.xdjd.distribution.bean.PrintParamBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.DialogUtil;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/5/29.
 */

public class VoucherReprintActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_voucher_reprint)
    ListView mLvVoucherReprint;
    @BindView(R.id.front_ll)
    LinearLayout mFrontLl;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.backwards_ll)
    LinearLayout mBackwardsLl;
    @BindView(R.id.tv_0)
    TextView mTv0;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.check_print_goodscode)
    CheckBox mCheckPrintGoodscode;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;

    private DatePickerDialog dateDialog;

    /**
     * 查询日期
     */
    private String dateStr;

    private int dateNum = 0;

    GregorianCalendar calToDay;
    GregorianCalendar calDate;

    private Date date = new Date();

    private VoucherReprintAdapter adapter;

    List<PrintListBean> list;

    private OutboundDetailBean outboundBean;
    private List<OutboundDetailBean> outboundList;

    /**
     * 单据补打列表下标
     */
    private int listIndex = -1;

    /**
     * 蓝牙对象列表
     */
    private List<BluetoothDevice> printerDevices;

    /**
     * 订单类型打印参数
     */
    private PrintOrderBean beanOrder;

    private PrintGlCashBean beanGlCash;

    private UserBean userBean;

    private int index = 0;
    private TextView[] tvs;

    /**
     * 收款单号
     */
    private String billCode = "";
    /**
     * 收款单类型
     */
    private String orderStatus = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 5) {
                showToast("打印失败");
            } else if (msg.what == PublicFinal.ERROR) {
                showToast("连接打印机失败!");
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_voucher_reprint;
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
        mTitleBar.setTitle("单据补打");

        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 3;

        tvs = new TextView[]{mTv0, mTv1, mTv2};
        tvs[index].setSelected(true);
        selectTab();

        userBean = UserInfoUtils.getUser(this);

        dateStr = DateUtils.getDataTime(DateUtils.dateFormater2);
        mTvDate.setText(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
        dateDialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        adapter = new VoucherReprintAdapter();
        mLvVoucherReprint.setAdapter(adapter);

        calToDay = new GregorianCalendar();
        calDate = new GregorianCalendar();
        calToDay.setTime(date);

        //1订单  2.收付款 3出入库
        printList();

        mLvVoucherReprint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listIndex = i;

                if (!BluetoothUtil.isBluetoothOn()) {//如果没有打开蓝牙就去打开蓝牙
                    boolean isOpen = BluetoothUtil.turnOnBluetooth();
                    if (!isOpen) {
                        return;
                    }
                }

                if ("1".equals(list.get(i).getType())) {
                    if ("9".equals(list.get(i).getStats()) || "10".equals(list.get(i).getStats())) {
                        DialogUtil.showCustomDialog(VoucherReprintActivity.this, "提示", "已取消或已作废订单无法打印!", "确定", null, null);
                        return;
                    }
                    //订单
                    printOrder(list.get(i).getId());
                } else if ("2".equals(list.get(i).getType())) {
                    billCode = list.get(i).getCode();
                    orderStatus = list.get(i).getStats_nameref();
                    //收付款
                    printGlCash(list.get(i).getId());
                } else if ("3".equals(list.get(i).getType())) {
                    //出入库
                    getOrderDetail(list.get(i).getId());
                }
            }
        });
    }

    @OnClick({R.id.tv_0, R.id.tv_1, R.id.tv_2, R.id.front_ll, R.id.backwards_ll, R.id.tv_date})
    public void onClick(View view) {
        Calendar cStrar;
        switch (view.getId()) {
            case R.id.tv_0:
                index = 0;
                selectTab();
                printList();
                mLlBottom.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_1:
                index = 1;
                selectTab();
                printList();
                mLlBottom.setVisibility(View.GONE);
                break;
            case R.id.tv_2:
                index = 2;
                selectTab();
                printList();
                mLlBottom.setVisibility(View.VISIBLE);
                break;
            case R.id.front_ll:
                dateNum--;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
                printList();
                break;
            case R.id.backwards_ll:
                dateNum++;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
                printList();
                break;
            case R.id.tv_date:
                dateDialog.show();
                break;
        }
    }

    private void selectTab() {
        moveAnimation(index);
        alterWidth(tvs[index]);
        for (int i = 0; i < tvs.length; i++) {
            if (i == index) {
                tvs[i].setSelected(true);
                tvs[i].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                tvs[i].setSelected(false);
                tvs[i].setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    /**
     * 日选择回调接口
     */
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
            String str = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            Date date1 = StringUtils.toDateFormater2(str);
            calDate.setTime(date1);

            dateNum = (int) ((calDate.getTimeInMillis() - calToDay.getTimeInMillis()) / (1000 * 3600 * 24));//从间隔毫秒变成间隔天数
            dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
            mTvDate.setText(dateStr);

            Calendar cStrar = Calendar.getInstance();
            cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
            dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                    cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));

            printList();
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

    /**
     * 请求单据补打列表
     */
    private void printList() {
        list = null;
        adapter.notifyDataSetChanged();
        AsyncHttpUtil<PrintListBean> httpUtil = new AsyncHttpUtil<>(this, PrintListBean.class, new IUpdateUI<PrintListBean>() {
            @Override
            public void updata(PrintListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    list = jsonBean.getDataList();
                    adapter.setData(list);
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
        httpUtil.post(M_Url.printList, L_RequestParams.printList(userBean.getUserId(), mTvDate.getText().toString(), String.valueOf((index + 1))), true);
    }

    /**
     * 打印订单类型的单据
     *
     * @param om_id
     */
    private void printOrder(String om_id) {
        AsyncHttpUtil<PrintOrderBean> httpUtil = new AsyncHttpUtil<>(this, PrintOrderBean.class, new IUpdateUI<PrintOrderBean>() {
            @Override
            public void updata(PrintOrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    beanOrder = jsonBean;

                    if (beanOrder != null) {
                        DialogUtil.showCustomDialog(VoucherReprintActivity.this, "提示", "是否打印单据?", "打印", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                connectBluetooth();
                            }

                            @Override
                            public void no() {

                            }
                        });
                    } else {
                        showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.printOrder, L_RequestParams.printOrder(userBean.getUserId(), om_id), true);
    }

    private void printGlCash(String gc_id) {
        AsyncHttpUtil<PrintGlCashBean> httpUtil = new AsyncHttpUtil<>(this, PrintGlCashBean.class, new IUpdateUI<PrintGlCashBean>() {
            @Override
            public void updata(PrintGlCashBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    beanGlCash = jsonBean;
                    if (beanGlCash != null) {
                        DialogUtil.showCustomDialog(VoucherReprintActivity.this, "提示", "是否打印单据?", "打印", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                connectBluetooth();
                            }

                            @Override
                            public void no() {

                            }
                        });
                    } else {
                        showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.printGlCash, L_RequestParams.printGlCash(userBean.getUserId(), gc_id), true);
    }

    /**
     * 出入库单据补打
     */
    private void getOrderDetail(String orderId) {
        AsyncHttpUtil<OutboundDetailBean> httpUtil = new AsyncHttpUtil<>(this, OutboundDetailBean.class, new IUpdateUI<OutboundDetailBean>() {
            @Override
            public void updata(OutboundDetailBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    outboundBean = bean;
                    outboundList = bean.getDataList();
                    if (outboundList != null) {
                        DialogUtil.showCustomDialog(VoucherReprintActivity.this, "提示", "是否打印单据?", "打印", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                connectBluetooth();
                            }

                            @Override
                            public void no() {

                            }
                        });
                    } else {
                        showToast(bean.getRepMsg());
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getStockOutDetail, L_RequestParams.getStockOutDetail(userBean.getUserId(), orderId), true);
    }

    /**
     * 车销连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            showToast("连接打印机失败");
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
                            showToast("连接打印机失败");
                        }
                        return;
                    }
                }
            } else {
                showToast("连接打印机失败");
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
                boolean isSuccess = false;
                PrintParamBean paramBean = new PrintParamBean();

                ClientBean clientBean = new ClientBean();
                clientBean.setCc_name(list.get(listIndex).getCustomerid_nameref());
                clientBean.setCc_contacts_name(list.get(listIndex).getContact_name());
                clientBean.setCc_contacts_mobile(list.get(listIndex).getContact_mobile());
                clientBean.setCc_address(list.get(listIndex).getContact_address());

                String time = StringUtils.getDate();

                if ("1".equals(list.get(listIndex).getType())) {
                    StringBuilder orderTitle = new StringBuilder();
                    if ("2".equals(list.get(listIndex).getSource())) {
                        orderTitle.append("订单申报-");
                    } else if ("3".equals(list.get(listIndex).getSource())) {
                        orderTitle.append("销售-");
                    }

                    switch (list.get(listIndex).getOrderType()) {
                        case BaseConfig.OrderType1:
                            if ("2".equals(list.get(listIndex).getSource())) {
                                orderTitle.append("普通订单");
                            } else {
                                orderTitle.append("销售单");
                            }
                            break;
                        case BaseConfig.OrderType2:
                            orderTitle.append("处理单");
                            break;
                        case BaseConfig.OrderType4:
                            orderTitle.append("换货单");
                            break;
                        case BaseConfig.OrderType3:
                            orderTitle.append("退货单");
                            break;
                        case BaseConfig.OrderType5:
                            orderTitle.append("还货单");
                            break;
                        case BaseConfig.OrderType6:
                            orderTitle.append("铺货单");
                            break;
                    }
                    //                    Bitmap bitmap = ZXingUtil.creatBarcode(this, beanOrder.getOm_code(), 240, 100, false);
                    paramBean.setTitle(orderTitle.toString());
                    paramBean.setClientBean(clientBean);
                    paramBean.setUserBean(userBean);
                    paramBean.setOrderCode(beanOrder.getOm_code());
                    paramBean.setTime(time);
                    paramBean.setT(beanOrder);
                    paramBean.setTotalAmount(beanOrder.getOm_delivery_amount());
                    paramBean.setPrintCode(mCheckPrintGoodscode.isChecked() ? true : false);
                    paramBean.setSkAmount(beanOrder.getSkAmount());
                    paramBean.setYhAmount(beanOrder.getYhAmount());
                    paramBean.setXjAmount(beanOrder.getXjAmount());
                    paramBean.setYeAmount(beanOrder.getYsAmount());
                    paramBean.setYsAmount(beanOrder.getYskAmount());
                    paramBean.setSfAmount(beanOrder.getSfkAmount());

                    if (beanOrder.getSign_url_nameref()!=null && beanOrder.getSign_url_nameref().length()>0){
                        Bitmap myBitmap=null;
                        try{
                            myBitmap = Glide.with(this)
                                    .load(beanOrder.getSign_url_nameref())
                                    .asBitmap() //必须
                                    .into(1000, 500)
                                    .get();
                        }catch(Exception e){
                        }
                        if (myBitmap!=null){
                            paramBean.setSignImg(myBitmap);
                        }
                    }

                    //2 订单申报  3.车销
                    if ("2".equals(list.get(listIndex).getSource())) {
                        paramBean.setType(2);
                    } else if ("3".equals(list.get(listIndex).getSource())) {
                        paramBean.setType(3);
                    }
//                    isSuccess = Prints.printOrderBu(socket, paramBean);
                    isSuccess = PrintsCopy.printOrderBu(socket, paramBean);

                    if (beanOrder.getSign_url_nameref()!=null && beanOrder.getSign_url_nameref().length()>0){
                        try {
                            Thread.sleep(8000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if ("2".equals(list.get(listIndex).getType())) {
                    //收付款
                    isSuccess = Prints.printReceipt(socket, 3, orderStatus, billCode, clientBean, userBean.getBud_name(),
                            userBean.getMobile(), time, beanGlCash,
                            null, null, null, "");
                } else if ("3".equals(list.get(listIndex).getType())) {//出入库打印
                    if ("208".equals(outboundBean.getEim_type())){//陈列
                        clientBean.setCc_name(outboundBean.getCustomerName());
                        clientBean.setCc_address(outboundBean.getCustomerid_address());
                        clientBean.setCc_contacts_name(outboundBean.getCustomerid_contacts_name());
                        clientBean.setCc_contacts_mobile(outboundBean.getCustomerid_contacts_mobile());
                    }else{
                        clientBean = null;
                    }
                    paramBean.setTitle(list.get(listIndex).getSource_nameref());
                    paramBean.setType(4);
                    paramBean.setClientBean(clientBean);
                    paramBean.setTime(time);
                    paramBean.setOrderCode(list.get(listIndex).getCode());
                    paramBean.setUserBean(userBean);
                    paramBean.setT(outboundList);
                    paramBean.setTotalAmount(list.get(listIndex).getAmount());
                    paramBean.setPrintCode(false);
                    paramBean.setSfAmount(null);

//                    isSuccess = Prints.printOrder(socket, 4, clientBean, list.get(listIndex).getSource_nameref(),
//                            time, list.get(listIndex).getCode(), userBean.getBud_name(),
//                            userBean.getMobile(), outboundList, list.get(listIndex).getAmount(), false,"");
                    isSuccess = PrintsCopy.printOrder(socket,paramBean);
                }

                if (!isSuccess) {
                    mHandler.sendEmptyMessage(5);
                    return;
                }
                break;
            case TASK_TYPE_CONNECT:
                LogUtils.e("bluetooth", "PrintSettingActivity成功了");
                break;
        }
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 3
                                * index).setDuration(300).start();
    }

}
