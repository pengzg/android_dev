package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.CashReportAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.CashReportBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.DateUtils;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CashReportDayActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.front_ll)
    LinearLayout mFrontLl;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.backwards_ll)
    LinearLayout mBackwardsLl;
    @BindView(R.id.lv_cash_report)
    ListView mLvCashReport;
    @BindView(R.id.paidAmount)
    TextView mPaidAmount;
    @BindView(R.id.respondReceivableAmount)
    TextView mRespondReceivableAmount;
    @BindView(R.id.cardAmount)
    TextView mCardAmount;
    @BindView(R.id.cardFee)
    TextView mCardFee;
    @BindView(R.id.saleAmount)
    TextView mSaleAmount;
    @BindView(R.id.useAmount)
    TextView mUseAmount;
    @BindView(R.id.discountAmount)
    TextView mDiscountAmount;
    @BindView(R.id.receivableAmount)
    TextView mReceivableAmount;
    @BindView(R.id.payAmount)
    TextView mPayAmount;
    @BindView(R.id.tv_print)
    TextView mTvPrint;
    @BindView(R.id.ll_footer)
    LinearLayout mLlFooter;

    private DatePickerDialog dateDialog;

    /**
     * 查询日期
     */
    private String dateStr;

    private int dateNum = 0;

    GregorianCalendar calToDay;
    GregorianCalendar calDate;

    private Date date = new Date();

    private CashReportAdapter adapter;

    private CashReportBean bean;

    private List<BluetoothDevice> printerDevices;

    private UserBean userBean;

    int touchSlop = 10;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS){
            }else if (msg.what == PublicFinal.ERROR){
                showToast(UIUtils.getString(R.string.ly_null_error));
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_cash_report_day;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("现金日报");
        mTitleBar.setRightImageResource(R.mipmap.setting_cash);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CashReportSettingActivity.class);
            }
        });

        touchSlop = (int) (ViewConfiguration.get(CashReportDayActivity.this).getScaledTouchSlop() * 0.9);

        userBean = UserInfoUtils.getUser(this);

        dateStr = DateUtils.getDataTime(DateUtils.dateFormater2);
        mTvDate.setText(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
        dateDialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        calToDay = new GregorianCalendar();
        calDate = new GregorianCalendar();
        calToDay.setTime(date);

        adapter = new CashReportAdapter();
        mLvCashReport.setAdapter(adapter);

//        mLvCashReport.setOnTouchListener(onTouchListener);
//        mLvCashReport.setOnScrollListener(onScrollListener);

        queryCashReport();
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
        printerDevices = BluetoothUtil.getPairedDevices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.front_ll, R.id.tv_date, R.id.backwards_ll, R.id.tv_print})
    public void onClick(View view) {
        Calendar cStrar;
        switch (view.getId()) {
            case R.id.front_ll:
                dateNum--;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
                queryCashReport();
                break;
            case R.id.backwards_ll:
                dateNum++;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
                queryCashReport();
                break;
            case R.id.tv_date:
                dateDialog.show();
                break;
            case R.id.tv_print:
                if (bean != null && bean.getListData() != null && bean.getListData().size() > 0) {
                    connectBluetooth();
                }
                break;
        }
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

                boolean isSuccess = Prints.printCash(socket, bean, userBean.getBud_name(), userBean.getMobile());
                if (!isSuccess) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
                }
                break;
            case TASK_TYPE_CONNECT:
                break;
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
            queryCashReport();
        }
    };

    private void queryCashReport() {
        clearZero();
        adapter.setData(null);

        AsyncHttpUtil<CashReportBean> httpUtil = new AsyncHttpUtil<>(this, CashReportBean.class, new IUpdateUI<CashReportBean>() {
            @Override
            public void updata(CashReportBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    bean = jsonBean;

                    mPaidAmount.setText(bean.getPaidAmount());
                    mRespondReceivableAmount.setText(bean.getRespondReceivableAmount());
                    mCardAmount.setText(bean.getCardAmount());
                    mCardFee.setText(bean.getCardFee());
                    mSaleAmount.setText(bean.getSaleAmount());
//                    mGoodsAmount.setText(bean.getGoodsAmount());
                    mUseAmount.setText(bean.getUseAmount());
                    mDiscountAmount.setText(bean.getDiscountAmount());
                    mReceivableAmount.setText(bean.getReceivableAmount());
                    mPayAmount.setText(bean.getPayAmount());
//                    mRefundGiroAmount.setText(bean.getRefundGiroAmount());

                    adapter.setData(bean.getListData());
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
        httpUtil.post(M_Url.queryCashReport, L_RequestParams.queryCashReport(
                userBean.getUserId(), dateStr), true);
    }

    /**
     * 界面数据清零
     */
    private void clearZero() {
        mPaidAmount.setText("0.00");
        mRespondReceivableAmount.setText("0.00");
        mCardAmount.setText("0.00");
        mCardFee.setText("0.00");
        mSaleAmount.setText("0.00");
        mUseAmount.setText("0.00");
        mDiscountAmount.setText("0.00");
        mReceivableAmount.setText("0.00");
        mPayAmount.setText("0.00");
    }

    AnimatorSet backAnimatorSet;

    private void animateBack() {
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {

        } else {
            backAnimatorSet = new AnimatorSet();
            //            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), 0f);
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(mLlFooter, "translationY", mLlFooter.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            //            animators.add(headerAnimator);
            animators.add(footerAnimator);
            backAnimatorSet.setDuration(500);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }

    AnimatorSet hideAnimatorSet;

    private void animateHide() {
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {

        } else {
            hideAnimatorSet = new AnimatorSet();
            //            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), -toolbar.getHeight());
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(mLlFooter, "translationY", mLlFooter.getTranslationY(), mLlFooter.getHeight());
            ArrayList<Animator> animators = new ArrayList<>();
            //            animators.add(headerAnimator);
            animators.add(footerAnimator);
            hideAnimatorSet.setDuration(300);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {


        float lastY = 0f;
        float currentY = 0f;
        int lastDirection = 0;
        int currentDirection = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    currentY = event.getY();
                    currentDirection = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mLvCashReport.getFirstVisiblePosition() > 0) {
                        float tmpCurrentY = event.getY();
                        if (Math.abs(tmpCurrentY - lastY) > touchSlop) {
                            currentY = tmpCurrentY;
                            currentDirection = (int) (currentY - lastY);
                            if (lastDirection != currentDirection) {
                                if (currentDirection < 0) {
                                    animateHide();
                                } else {
                                    animateBack();
                                }
                            }
                            lastY = currentY;
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    currentDirection = 0;
                    break;
            }
            return false;
        }
    };


    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        int lastPosition = 0;
        int state = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            state = scrollState;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                animateBack();
            }
            if (firstVisibleItem > 0) {
                if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
                    animateHide();
                }
            }
            lastPosition = firstVisibleItem;
        }
    };

}
