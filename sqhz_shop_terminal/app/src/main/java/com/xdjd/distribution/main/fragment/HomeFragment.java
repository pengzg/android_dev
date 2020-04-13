package com.xdjd.distribution.main.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.MyYAxisValueOneFormatter;
import com.github.mikephil.charting.formatter.MyYAxisValueTwoFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.CaptureActivity;
import com.xdjd.distribution.activity.CashReportDayActivity;
import com.xdjd.distribution.activity.ClientDetailActivity;
import com.xdjd.distribution.activity.CommissionSalesActivity;
import com.xdjd.distribution.activity.CustomerAddressListActivity;
import com.xdjd.distribution.activity.DistributionTaskActivity;
import com.xdjd.distribution.activity.DistributionWarehouseActivity;
import com.xdjd.distribution.activity.OrderSearchActivity;
import com.xdjd.distribution.activity.PicturesActivity;
import com.xdjd.distribution.activity.PositionLocationActivity;
import com.xdjd.distribution.activity.ReceiptPaymentQueryActivity;
import com.xdjd.distribution.activity.RolloutGoodsActivity;
import com.xdjd.distribution.activity.SalesOutboundActivity;
import com.xdjd.distribution.activity.SelectClientActivity;
import com.xdjd.distribution.activity.SelectStoreActivity;
import com.xdjd.distribution.activity.TakingPicturesActivity;
import com.xdjd.distribution.activity.WxBindActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.bean.TaskNum;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.bean.UserSignBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.distribution.event.OpenMallEvent;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.distribution.event.TaskNumEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.LinePopup;
import com.xdjd.steward.bean.BriefingBean;
import com.xdjd.steward.bean.TrendsChartBean;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.risenumber.RiseNumberTextView;
import com.xdjd.view.roundedimage.RoundedImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2016/8/22.
 */
public class HomeFragment extends BaseFragment implements LocationListener, CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.add_client)
    LinearLayout mAddClient;
    @BindView(R.id.message_ll)
    LinearLayout mMessageLl;
    @BindView(R.id.line_tv)
    TextView mLineTv;
    @BindView(R.id.line_ll)
    LinearLayout mLineLl;
    @BindView(R.id.tv_leave)
    TextView mTvLeave;
    @BindView(R.id.client_message_ll)
    LinearLayout mClientMessageLl;
    @BindView(R.id.sweep_card_ll)
    LinearLayout mSweepCardLl;
    @BindView(R.id.ll_commission_order)
    LinearLayout mLlCommissionOrder;
    @BindView(R.id.ll_position_location)
    LinearLayout mLlPositionLocation;
    @BindView(R.id.ll_order_to_declare)
    LinearLayout mLlOrderToDeclare;
    @BindView(R.id.ll_sales_of_outbound)
    LinearLayout mLlSalesOfOutbound;
    @BindView(R.id.ll_receipt_payment)
    LinearLayout mLlReceiptPayment;
    @BindView(R.id.ll_cash_report_every_day)
    LinearLayout mLlCashReportEveryDay;
    @BindView(R.id.ll_sign_back_out)
    LinearLayout mLlSignBackOut;
    @BindView(R.id.client_name)
    TextView mClientName;
    @BindView(R.id.client_contacts_name)
    TextView mClientContactsName;
    @BindView(R.id.client_balance)
    TextView mClientBalance;
    @BindView(R.id.client_tel)
    TextView mClientTel;
    @BindView(R.id.client_img)
    RoundedImageView mClientImg;
    @BindView(R.id.ll_client_communication_directory)
    LinearLayout mLlClientCommunicationDirectory;
    @BindView(R.id.ll_distribution_task)
    LinearLayout mLlDistributionTask;
    @BindView(R.id.ll_distribution_warehouse)
    LinearLayout mLlDistributionWarehouse;
    @BindView(R.id.ll_placeholder)
    LinearLayout mLlPlaceholder;
    @BindView(R.id.tv_ysjb_select)
    TextView mTvYsjbSelect;
    @BindView(R.id.tv_ysjb_date)
    TextView mTvYsjbDate;
    @BindView(R.id.ll_ysjb)
    LinearLayout mLlYsjb;
    @BindView(R.id.tv_total_amount)
    RiseNumberTextView mTvTotalAmount;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_receive_amount)
    RiseNumberTextView mTvReceiveAmount;
    @BindView(R.id.tv_ysqs_select)
    TextView mTvYsqsSelect;
    @BindView(R.id.tv_ysqs_date)
    TextView mTvYsqsDate;
    @BindView(R.id.ll_ysqs)
    LinearLayout mLlYsqs;
    @BindView(R.id.btn_order_amount)
    TextView mBtnOrderAmount;
    @BindView(R.id.btn_receipt_amount)
    TextView mBtnReceiptAmount;
    @BindView(R.id.btn_order_num)
    TextView mBtnOrderNum;
    @BindView(R.id.chart_line)
    LineChart mChartLine;
    @BindView(R.id.line_chart_ll)
    LinearLayout mLineChartLl;
    @BindView(R.id.tv_distribution_task_num)
    TextView mTvDistributionTaskNum;
    @BindView(R.id.tv_distribution_warehouse_num)
    TextView mTvDistributionWarehouseNum;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.ll_order_search)
    LinearLayout mLlOrderSearch;
    @BindView(R.id.ll_receipt)
    LinearLayout mLlReceipt;
    @BindView(R.id.tv_wx_bind)
    TextView mTvWxBind;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.ll_rollout_goods)
    LinearLayout mLlRolloutGoods;

/*规格*/
    /**
     * 线路popup
     */
    private LinePopup linePopup;

    private List<LineBean> listName = new ArrayList<>();

    private boolean isLineSuccess = false;//配置和线路信息是否加载成功

    /**
     * 线路dao
     */
    private LineDao lineDao;
    /**
     * 选择的客户信息
     */
    public ClientBean clientBean;

    /**
     * 是否签退退出
     */
    boolean isSignBackOut = false;

    /**
     * 纬度
     */
    private String latitude = "";
    /**
     * 经度
     */
    private String longtitude = "";
    /**
     * 定位后的地址
     */
    private String address = "";

    private LocationService locationService;

    private MyLocationUtil locationUtil;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                main.disProgressDialog(getActivity());
                signOut();
            }
        }
    };

    private MainActivity main;
    private UserBean userBean;

    //营收简报起止时间
    private String startDateYs;
    private String endDateYs;
    //营收趋势起止时间
    private String startDateYsqs;
    private String endDateYsqs;

    //1.本周;2.本月;3.本年;4.自定义;5上月;6.上周---营收趋势
    private int dateYsqs = 1;

    /**
     * 营收趋势订单类型--1 订单金额 2.收款 3订单数量
     */
    private int ysqsType = 1;
    //营收趋势日、月
    private int chartType = 1;

    /**
     * 选择日期区间type--1.营收简报;2.营收趋势
     */
    private int datePrickType = 1;

    private Typeface mTf;
    private int width;
    //营收趋势动态加载布局控件
    private VaryViewHelper ysqsHelper = null;

    private Date date = new Date();
    private TimePickerUtil mTimePickerUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mTimePickerUtil = new TimePickerUtil();

        userBean = UserInfoUtils.getUser(getActivity());

        //设置选中的线路名称
        mLineTv.setText(UserInfoUtils.getLineName(getActivity()));

        EventBus.getDefault().register(this);
        main = (MainActivity) getActivity();

        lineDao = new LineDao(getActivity());

        //初始化定位工具类
        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);//设置定位回调监听

        clientBean = UserInfoUtils.getClientInfo(getActivity());
        if (clientBean == null) {
            mClientMessageLl.setVisibility(View.GONE);
            mSweepCardLl.setVisibility(View.VISIBLE);
        } else {
            mClientMessageLl.setVisibility(View.VISIBLE);
            mSweepCardLl.setVisibility(View.GONE);

            mClientName.setText(clientBean.getCc_name());
            mClientContactsName.setText(clientBean.getCc_contacts_name());
            mClientTel.setText(clientBean.getCc_contacts_mobile());

            if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
                Glide.with(getActivity()).load(clientBean.getCc_image())
                        .error(R.mipmap.customer_img)
                        .into(mClientImg);
            } else {
                mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
            }
        }

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        dateBtnSytle(1);//订单金额

        initCustomTimePicker();

        initLineView();//初始化折线图

        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateYs = todayStr;
        endDateYs = todayStr;
        mTvYsjbDate.setText(startDateYs + "-" + endDateYs);

        startDateYsqs = DateUtils.getFirstDayOfWeek(date, DateUtils.dateFormater4);
        endDateYsqs = DateUtils.getLastDayOfWeek(date, DateUtils.dateFormater4);
        mTvYsqsDate.setText(startDateYsqs + "-" + endDateYsqs);

        ysqsHelper = new VaryViewHelper(mLineChartLl);
        ysqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        initPwLines();

        loadYsjb();
        loadYsqs();

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (clientBean != null)
                    getCustomerBalance();
                getTaskNum();
                loadYsjb();
                loadYsqs();
            }
        });

        if(UserInfoUtils.getClientInfo(getActivity())!=null &&
                UserInfoUtils.getUpdateShop(getActivity()).equals("1")){
            //如果没有签退店铺,第二天重新登录客户信息进行刷新
            UserInfoUtils.setUpdateShop(getActivity(),"0");
            getCustomerInfo();
        }

        getUserLineOrSettingInfo();
    }


    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo() {
        LogUtils.e("updateClinet","刷新客户信息");
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    clientBean = jsonBean;
                    UserInfoUtils.setClientInfo(getActivity(),clientBean);
                    mClientName.setText(clientBean.getCc_name());
                    mClientContactsName.setText(clientBean.getCc_contacts_name());
                    mClientTel.setText(clientBean.getCc_contacts_mobile());

                    if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
                        Glide.with(getActivity()).load(clientBean.getCc_image())
                                .error(R.mipmap.customer_img)
                                .into(mClientImg);
                    } else {
                        mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
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
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(getActivity()) + "",
                clientBean.getCc_id()), false);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月
        switch (datePrickType) {
            case 1:
                mTvYsjbSelect.setText(dateName);
                startDateYs = startDate;
                endDateYs = endDate;
                mTvYsjbDate.setText(startDateYs + "-" + endDateYs);
                loadYsjb();
                break;
            case 2:
                mTvYsqsSelect.setText(dateName);
                startDateYsqs = startDate;
                endDateYsqs = endDate;

                dateYsqs = position;

                switch (position) {
                    case 11:
                    case 12:
                    case 13:
                        chartType = 2;
                        loadYsqs();
                        break;
                    default:
                        chartType = 1;
                        loadYsqs();
                        break;
                }
                mTvYsqsDate.setText(startDateYsqs + "-" + endDateYsqs);
                break;
        }
    }

    @Override
    public void onPause() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (YesOrNoLoadingOnstart.INDEX_ID == 0 && YesOrNoLoadingOnstart.INDEX == true) {
            LogUtils.e("HomeFragment-->onStart", "onStart");
            // -----------location config ------------
            locationService = ((App) getActivity().getApplication()).locationService;
            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
            locationService.registerListener(locationUtil);
            //注册监听
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
            mClientBalance.setText("");
            if (clientBean != null) {
                if (UserInfoUtils.getCustomerBalance(getContext()) != null) {
                    mClientBalance.setText(UserInfoUtils.getCustomerBalance(getContext()));
                } else {
                    getCustomerBalance();
                }
            }

            getTaskNum();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        lineDao.destroy();
    }

    /**
     * 获取任务数量
     */
    private void getTaskNum() {
        AsyncHttpUtil<TaskNum> httpUtil = new AsyncHttpUtil<>(getActivity(), TaskNum.class, new IUpdateUI<TaskNum>() {
            @Override
            public void updata(TaskNum jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getTaskNum() == null || "0".equals(jsonBean.getTaskNum())) {
                        mTvDistributionTaskNum.setVisibility(View.GONE);
                    } else {
                        mTvDistributionTaskNum.setVisibility(View.VISIBLE);
                        mTvDistributionTaskNum.setText(jsonBean.getTaskNum());
                    }

                    if (jsonBean.getTaskNumCus() == null || "0".equals(jsonBean.getTaskNumCus())) {
                        mTvDistributionWarehouseNum.setVisibility(View.GONE);
                    } else {
                        mTvDistributionWarehouseNum.setVisibility(View.VISIBLE);
                        mTvDistributionWarehouseNum.setText(jsonBean.getTaskNumCus());
                    }
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getTaskNum, L_RequestParams.
                getTaskNum("2", clientBean == null ? "" : clientBean.getCc_id()), false);
    }

    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    UserInfoUtils.setCustomerBalance(getActivity(), jsonBean.getBalance());
                    UserInfoUtils.setAfterAmount(getActivity(), jsonBean.getGcb_after_amount());
                    UserInfoUtils.setSafetyArrearsNum(getActivity(), jsonBean.getCc_safety_arrears_num());
                    mClientBalance.setText(jsonBean.getBalance());
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

    @OnClick({R.id.client_img, R.id.tv_wx_bind, R.id.ll_position_location, R.id.ll_client_communication_directory, R.id.tv_leave, R.id.add_client, R.id.message_ll, R.id.sweep_card_ll, R.id.line_ll,
            R.id.ll_order_to_declare, R.id.ll_sales_of_outbound, R.id.ll_receipt_payment, R.id.ll_cash_report_every_day, R.id.ll_commission_order,
            R.id.ll_sign_back_out, R.id.ll_distribution_task, R.id.ll_distribution_warehouse, R.id.tv_ysjb_select, R.id.ll_ysjb, R.id.tv_ysqs_select, R.id.ll_ysqs,
            R.id.btn_order_amount, R.id.btn_receipt_amount, R.id.btn_order_num, R.id.ll_order_search, R.id.ll_receipt,R.id.ll_rollout_goods})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_wx_bind://绑定微信
                if (clientBean != null) {
                    intent = new Intent(getActivity(), WxBindActivity.class);
                    intent.putExtra("customerId", clientBean.getCc_id());
                    startActivity(intent);
                }
                break;
            case R.id.client_img://头像拍照
                PermissionUtils.requstActivityCamera(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(getActivity(), PicturesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type", 0);
                        startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                    }

                    @Override
                    public void onDilogCancal() {
                        showToast("获取相机权限失败!");
                    }
                });
                break;
            case R.id.ll_position_location://定位
                if (clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请先选择客户,再进行定位。", "确定", null, null);
                } else {
                    //请求位置权限
                    PermissionUtils.requstActivityLocation(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getActivity(), PositionLocationActivity.class);
                            intent.putExtra("isHome", 1);
                            intent.putExtra("customer", clientBean);
                            startActivity(intent);
                        }

                        @Override
                        public void onDilogCancal() {
                        }
                    });
                }
                break;
            case R.id.ll_client_communication_directory://客户通讯录
                startActivity(CustomerAddressListActivity.class);
                break;
            case R.id.tv_leave://离店
                isSignBackOut = false;
                if (clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请刷客户档案卡...!", "确定", null, null);
                } else {
                    DialogUtil.showCustomDialog(getActivity(), "确认", "确认要离店吗?", "离店", "取消",
                            new DialogUtil.MyCustomDialogListener2() {
                                @Override
                                public void ok() {
                                    if (userBean.getOutPhoto().equals("1")) {//拍照退出
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), TakingPicturesActivity.class);
                                        intent.putExtra("type", PublicFinal.SIGNOUT);
                                        intent.putExtra("customer", clientBean);
                                        startActivity(intent);
                                    } else {
                                        if (locationService.client.isStarted()) {
                                        } else {
                                            locationService.registerListener(locationUtil);
                                            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                                        }
                                        latitude = "";
                                        longtitude = "";
                                        address = "";
                                        locationService.start();
                                        main.showProgressDialog(getActivity());
                                    }
                                }

                                @Override
                                public void no() {
                                }
                            });
                }
                break;
            case R.id.add_client://添加客户
                ((RadioButton) main.getRadioGroup1().getChildAt(2)).setChecked(true);
                break;
            case R.id.message_ll://跳转客户信息卡
                startActivity(ClientDetailActivity.class);
                break;
            case R.id.sweep_card_ll://扫描客户档案
                if (UserInfoUtils.getLineId(getActivity()).equals("")) {
                    DialogUtil.showCustomDialog(getActivity(), "提示", "请先选择线路!", "确定", null, null);
                    return;
                }
                //请求位置权限
                PermissionUtils.requstActivityLocation(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        if ("1".equals(userBean.getIsScan())) {//是否扫卡 1扫卡 2手动
                            //跳转扫描界面
                            Intent intent = new Intent(getActivity(), CaptureActivity.class);
                            intent.putExtra("titleStr", "扫描档案卡");
                            startActivityForResult(intent, Comon.QR_CUSTOMER_CODE);
                        } else {
                            startActivity(SelectClientActivity.class);
                        }
                    }

                    @Override
                    public void onDilogCancal() {
                    }
                });
                break;
            case R.id.line_ll://线路
                if (!isLineSuccess) {
                    showToast("正在获取线路信息");
                    getUserLineOrSettingInfo();
                } else {
                    if (clientBean != null) {
                        hint("请离店后选择线路!");
                    } else {
                        showPwLines();
                    }
                }
                break;
            case R.id.ll_distribution_task://配送任务
                startActivity(DistributionTaskActivity.class);
                break;
            case R.id.ll_distribution_warehouse://配送出库
                if (clientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    startActivity(DistributionWarehouseActivity.class);

                    //订货
                    //                    intent = new Intent(getActivity(), CommissionSalesActivity.class);
                    //                    intent.putExtra("businesstype", "2");
                    //                    startActivity(intent);
                }
                break;
          /*  case R.id.ll_order_goods://订货
                if (clientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    startActivity(OrderGoodsActivity.class);
                }
                break;*/
            case R.id.ll_order_to_declare://订单申报

                if (clientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    startActivity(SelectStoreActivity.class);
                }
                break;
            case R.id.ll_sales_of_outbound://销售出库
                if ("".equals(userBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }

                if (clientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    startActivity(SalesOutboundActivity.class);
                }
                break;
            case R.id.ll_receipt_payment://收付款
                if (clientBean == null) {
                    hint("请先选择客户");
                } else {
                    main.payment();
                }
                break;
            case R.id.ll_cash_report_every_day://现金日报
                startActivity(CashReportDayActivity.class);
                break;
            case R.id.ll_commission_order://订货
               /* if (clientBean == null) {//代销销售
                    hint("请先刷客户档案或选择客户...");
                    return;
                }
                intent = new Intent(getActivity(),CommissionSalesActivity.class);
                intent.putExtra("businesstype", "1");
                startActivity(intent);*/
                if (clientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    //订货
                    intent = new Intent(getActivity(), CommissionSalesActivity.class);
                    intent.putExtra("businesstype", "2");
                    startActivity(intent);
                }
                //                startActivity(InventoryManagementActivity.class);//库存管理
                break;
            case R.id.ll_rollout_goods://铺货
                startActivity(RolloutGoodsActivity.class);
                break;
            case R.id.ll_sign_back_out://签退退出
                if (clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "提示", "确认要退出吗?", "退出", "取消", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            addUserSign();
                        }

                        @Override
                        public void no() {
                        }
                    });
                } else {
                    isSignBackOut = true;
                    DialogUtil.showCustomDialog(getActivity(), "提示", "您尚未离店,确认离店并退出系统吗?", "离店退出", "取消",
                            new DialogUtil.MyCustomDialogListener2() {
                                @Override
                                public void ok() {
                                    if (userBean.getOutPhoto().equals("1")) {//拍照退出
                                        PermissionUtils.requstActivityCamera(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
                                            @Override
                                            public void onSuccess() {
                                                Intent intent = new Intent();
                                                intent.setClass(getActivity(), TakingPicturesActivity.class);
                                                intent.putExtra("type", PublicFinal.SIGNOUT);
                                                intent.putExtra("customer", clientBean);
                                                intent.putExtra("isSignBackOut", isSignBackOut);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onDilogCancal() {
                                                showToast("获取拍照权限失败!");
                                            }
                                        });
                                    } else {
                                        if (locationService.client.isStarted()) {
                                        } else {
                                            locationService.registerListener(locationUtil);
                                            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                                        }

                                        latitude = "";
                                        longtitude = "";
                                        address = "";
                                        locationService.start();
                                    }
                                }
                                @Override
                                public void no() {
                                }
                            });
                }
                break;

            case R.id.tv_ysjb_select://营收简报
                datePrickType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYsjbDate.getText().toString().split("-")[0],
                        mTvYsjbDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_ysjb:
                datePrickType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYsjbDate.getText().toString().split("-")[0],
                        mTvYsjbDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_ysqs_select://营收趋势
                datePrickType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYsqsDate.getText().toString().split("-")[0],
                        mTvYsqsDate.getText().toString().split("-")[1], false);
                break;
            case R.id.ll_ysqs:
                datePrickType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYsqsDate.getText().toString().split("-")[0],
                        mTvYsqsDate.getText().toString().split("-")[1], false);
                break;
            case R.id.btn_order_amount:
                dateBtnSytle(1);//订单金额
                //1 订单金额 2.收款 3订单数量
                ysqsType = 1;
                loadYsqs();
                break;
            case R.id.btn_receipt_amount:
                dateBtnSytle(2);//收款金额
                ysqsType = 2;
                loadYsqs();
                break;
            case R.id.btn_order_num:
                dateBtnSytle(3);//订单量
                ysqsType = 3;
                loadYsqs();
                break;
            case R.id.ll_order_search:
                intent = new Intent(getActivity(), OrderSearchActivity.class);
                intent.putExtra("dateStartStr", startDateYs);
                intent.putExtra("dateEndStr", endDateYs);
                intent.putExtra("dateTypeStr", mTvYsjbSelect.getText().toString());
                intent.putExtra("isFromMain", 1);
                startActivity(intent);
                break;
            case R.id.ll_receipt:
                startActivity(ReceiptPaymentQueryActivity.class);
                break;
        }
    }

    /**
     * 没有选择客户提示
     */
    private void hint(String message) {
        DialogUtil.showCustomDialog(getActivity(), "注意", message, "确定", null, null);
    }

    /**
     * 没有车仓库是提示弹框
     */
    private void noCarHint() {
        DialogUtil.showCustomDialog(getActivity(), "提示", "您还没有相关联的车仓库,请联系后台文员进行车仓库关联!", "确定", null, null);
    }

    /**
     * 没有关联的业务员id
     */
    private void noCustomerid() {
        DialogUtil.showCustomDialog(getActivity(), "提示", "没有相应的操作权限,请联系后台文员", "确定", null, null);
    }


    /**
     * 初始化线路popup
     */
    private void initPwLines() {
        lineDao = new LineDao(getActivity());
        listName = lineDao.query();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        linePopup = new LinePopup(getActivity(), dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                UserInfoUtils.setLineId(getActivity(), listName.get(position).getBl_id());
                UserInfoUtils.setLineName(getActivity(), listName.get(position).getBl_name());
                mLineTv.setText(listName.get(position).getBl_name());
                linePopup.dismiss();
            }
        });
        linePopup.setData(listName);
    }

    /**
     * 显示线路popup
     */
    private void showPwLines() {
        MainActivity main = (MainActivity) getActivity();

        linePopup.setItem(mLineTv.getText().toString());
        linePopup.setId(UserInfoUtils.getLineId(getActivity()));
        // 显示窗口
        linePopup.showAtLocation(main.getMainId(),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    /**
     * 获取用户配置信息
     */
    private void getUserLineOrSettingInfo() {
        AsyncHttpUtil<UserBean> httpUtil = new AsyncHttpUtil<>(getActivity(), UserBean.class, new IUpdateUI<UserBean>() {
            @Override
            public void updata(UserBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    userBean.setInPhoto(bean.getInPhoto());
                    userBean.setOutPhoto(bean.getOutPhoto());
                    userBean.setIsScan(bean.getIsScan());
                    userBean.setIsChangPrice(bean.getIsChangPrice());
                    userBean.setIsChangeThPrice(bean.getIsChangeThPrice());
                    userBean.setIsQueryStock(bean.getIsQueryStock());
                    userBean.setIsChangeYH(bean.getIsChangeYH());
                    userBean.setSkType(bean.getSkType());
                    userBean.setIsAllowSign(bean.getIsAllowSign());
                    userBean.setSignDistance(bean.getSignDistance());
                    userBean.setIsReLocation(bean.getIsReLocation());
                    userBean.setRefundDays(bean.getRefundDays());
                    userBean.setRefundMode(bean.getRefundMode());
                    userBean.setIsShopAccount(bean.getIsShopAccount());
                    userBean.setIsSendSms(bean.getIsSendSms());
                    userBean.setIsSign(bean.getIsSign());//是否需要手动签名

                    //设置默认签到距离
                    UserInfoUtils.setSignDistance(getActivity(), bean.getSignDistance());
                    //设置线路信息
                    lineDao.batchInsert(bean.getLineList());//添加线路信息
                    listName = bean.getLineList();
                    linePopup.setData(listName);

                    //如果线路不为空
                    if (bean.getLineList().size() > 0 && "".equals(UserInfoUtils.getLineId(getActivity()))) {
                        UserInfoUtils.setLineId(getActivity(), bean.getLineList().get(0).getBl_id());
                        UserInfoUtils.setLineName(getActivity(), bean.getLineList().get(0).getBl_name());
                        mLineTv.setText(UserInfoUtils.getLineName(getActivity()));
                    } else if (!UserInfoUtils.getLineId(getActivity()).equals("")) {
                        mLineTv.setText(UserInfoUtils.getLineName(getActivity()));
                    }
                    UserInfoUtils.setUser(getActivity(), userBean);

                    isLineSuccess = true;
                } else {
                    showToast(bean.getRepMsg());
                    isLineSuccess = false;
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                isLineSuccess = false;
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getUserLineOrSettingInfo, L_RequestParams.getUserLineOrSettingInfo(), true);
    }


    /**
     * 离店签退
     */
    private void signOut() {
        if ("".equals(latitude) || latitude == null) {
            showToast("定位获取失败,请检查GPS或网络是否打开");
            return;
        }

        if (clientBean == null || clientBean.getCc_id() == null) {
            //定位有时候会重复定位
            return;
        }

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    showToast(jsonStr.getRepMsg());
                    mClientMessageLl.setVisibility(View.GONE);
                    mSweepCardLl.setVisibility(View.VISIBLE);

                    UserInfoUtils.setClientInfo(getActivity(), null);
                    UserInfoUtils.setTaskId(getActivity(),null);
                    clientBean = null;
                    UserInfoUtils.setCustomerBalance(getActivity(), null);
                    UserInfoUtils.setSafetyArrearsNum(getActivity(), null);
                    UserInfoUtils.setAfterAmount(getActivity(), null);

                    mTvDistributionWarehouseNum.setVisibility(View.GONE);

                    if (isSignBackOut) {
                        addUserSign();
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                //                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.signOut, L_RequestParams.signOut(userBean.getUserId(),
                UserInfoUtils.getLineId(getActivity()) + "", userBean.getOrgid(), clientBean.getCc_id(),//有bug--clientBean.getCc_id()是空,签退后
                longtitude, latitude, userBean.getIsScan(), "2", UserInfoUtils.getTaskId(getActivity()), ""), true);
    }

    /**
     * 业务员签退
     */
    private void addUserSign() {
        AsyncHttpUtil<UserSignBean> httpUtil = new AsyncHttpUtil<>(getActivity(), UserSignBean.class, new IUpdateUI<UserSignBean>() {

            @Override
            public void updata(UserSignBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (userBean.getBud_id() != null && !"".equals(userBean.getBud_id())) {
                        PushAgent mPushAgent = PushAgent.getInstance(getActivity());
                        //退出时解除别名的绑定
                        mPushAgent.removeAlias(userBean.getBud_id(), BaseConfig.Alias_Type,
                                new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String s) {
                                    }
                                });
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
                blackApp();
            }
        });
        httpUtil.post(M_Url.addUserSign, L_RequestParams.addUserSign(userBean.getUserId(), UserInfoUtils.getLineId(getActivity()),
                UserInfoUtils.getCudId(getActivity()), "2"), true);
    }

    private void blackApp() {
        UserInfoUtils.setCudId(getActivity(), "");//清空签到id
        UserInfoUtils.setLoginState(getActivity(), "0");//设置登录状态

        UserInfoUtils.setLineId(getActivity(), "");//清空线路id
        UserInfoUtils.setLineName(getActivity(), "");

        //退出时将余额设置为空
        UserInfoUtils.setCustomerBalance(getActivity(), null);
        getActivity().finish();
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(getActivity().getPackageName());
        AppManager.getInstance().finishAllActivity();
        System.exit(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Comon.QR_GOODS_REQUEST_CODE == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {
            //头像拍照回传结果
            clientBean = UserInfoUtils.getClientInfo(getActivity());
            if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
                Glide.with(getActivity()).load(clientBean.getCc_image()).into(mClientImg);
            } else {
                mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
            }
        } else if (Comon.QR_CUSTOMER_CODE == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {//扫描客户卡得到的信息
            getCustomerInfoByCode(data.getStringExtra("result"));
        } else if (Comon.UPDATE_LOADING == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {
            LogUtils.e("UPDATE_LOADING", "UPDATE_LOADING");
            //客户扫卡重新定位后进行签到
            ClientBean bean = (ClientBean) data.getSerializableExtra("result");
            selectCustomer(bean);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 签到客户的信息刷新
     */
    private void customerSignUpdate() {
        mClientMessageLl.setVisibility(View.VISIBLE);
        mSweepCardLl.setVisibility(View.GONE);

        if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
            Glide.with(getActivity())
                    .load(clientBean.getCc_image())
                    .error(R.mipmap.customer_img)
                    .into(mClientImg);
        } else {
            mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
        }

        mClientName.setText(clientBean.getCc_name());
        mClientContactsName.setText(clientBean.getCc_contacts_name());
        mClientTel.setText(clientBean.getCc_contacts_mobile());
        mClientBalance.setText("0.00");

        getCustomerBalance();

        mLineTv.setText(UserInfoUtils.getLineName(getActivity()));
        getTaskNum();
    }

    public void onEventMainThread(SignClientEvent event) {
        clientBean = UserInfoUtils.getClientInfo(getActivity());
        if (clientBean == null) {
            mClientMessageLl.setVisibility(View.GONE);
            mSweepCardLl.setVisibility(View.VISIBLE);
        } else {
            customerSignUpdate();
        }
    }

    public void onEventMainThread(TaskNumEvent event) {
        if (clientBean != null) {
            getTaskNum();
        }
    }

    public void onEventMainThread(OpenMallEvent event) {
        ClientBean eventClient = event.getClientBean();
        //编辑客户信息是当前签到客户时刷新数据
        if (eventClient.getCc_id().equals(clientBean.getCc_id())){
            clientBean = UserInfoUtils.getClientInfo(getActivity());

            clientBean.setCc_name(eventClient.getCc_name());
            clientBean.setCc_latitude(eventClient.getCc_latitude());
            clientBean.setCc_longitude(eventClient.getCc_longitude());
            clientBean.setCc_address(eventClient.getCc_address());
            clientBean.setCc_contacts_name(eventClient.getCc_contacts_name());//客户姓名
            clientBean.setCc_contacts_mobile(eventClient.getCc_contacts_mobile());//客户电话
            clientBean.setCc_categoryid(eventClient.getCc_categoryid());
            clientBean.setCc_categoryid_nameref(eventClient.getCc_categoryid_nameref());
            clientBean.setCc_channelid(eventClient.getCc_channelid());
            clientBean.setCc_channelid_nameref(eventClient.getCc_channelid_nameref());
            clientBean.setCc_depotid(eventClient.getCc_depotid());
            clientBean.setCc_depotid_nameref(eventClient.getCc_depotid_nameref());

            clientBean.setCc_goods_gradeid(eventClient.getCc_goods_gradeid());
            clientBean.setCc_goods_gradeid_nameref(eventClient.getCc_goods_gradeid_nameref());
            UserInfoUtils.setClientInfo(getActivity(),clientBean);

            mClientName.setText(clientBean.getCc_name());
            mClientContactsName.setText(clientBean.getCc_contacts_name());
            mClientTel.setText(clientBean.getCc_contacts_mobile());

            if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
                Glide.with(this).load(clientBean.getCc_image())
                        .error(R.mipmap.customer_img)
                        .into(mClientImg);
            } else {
                mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
            }
        }
    }

    /**
     * 根据客户编码获取客户信息接口
     *
     * @param cc_code
     */
    private void getCustomerInfoByCode(String cc_code) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    selectCustomer(jsonBean);
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
        httpUtil.post(M_Url.getCustomerInfoByCode, L_RequestParams.getCustomerInfoByCode(cc_code), true);
    }

    /**
     * 选择客户签到方法
     */
    private void selectCustomer(final ClientBean clientBean) {
        LogUtils.e("clientBean", clientBean.toString());
        if (userBean.getIsAllowSign().equals("1") &&
                clientBean.getCc_islocation().equals("Y")) {//限制500米以内签到且必须是已经定位

            BigDecimal distance;
            if (TextUtils.isEmpty(clientBean.getDistance()) || clientBean.getDistance() == null) {
                distance = BigDecimal.ZERO;
            } else {
                distance = new BigDecimal(clientBean.getDistance());
            }

            boolean isReLocation = false;
            if ("1".equals(userBean.getIsReLocation())) {//是否允许重新定位
                isReLocation = true;
            }

            if (distance.intValue() > UserInfoUtils.getSignDistance(getActivity())) {
                DialogUtil.showCustomDialog(getActivity(), "提示", "您已超出店铺距离范围,不能进行销售!",
                        isReLocation ? "重新定位" : null, isReLocation ? "取消" : "确定", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(getActivity(), PositionLocationActivity.class);
                                intent.putExtra("isHome", 1);
                                intent.putExtra("customer", clientBean);
                                intent.putExtra("isScan", true);
                                startActivityForResult(intent, Comon.UPDATE_LOADING);
                            }

                            @Override
                            public void no() {
                            }
                        });
                return;
            }
        }

        if (clientBean.getCc_islocation().equals("N")) {
            DialogUtil.showCustomDialog(getActivity(), "提示", "这个客户还没有定位,请先去定位!", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                    Intent intent = new Intent(getActivity(), PositionLocationActivity.class);
                    intent.putExtra("isHome", 1);
                    intent.putExtra("customer", clientBean);
                    intent.putExtra("isScan", true);
                    startActivityForResult(intent, Comon.UPDATE_LOADING);
                }

                @Override
                public void no() {
                }
            });
            return;
        }
        if (userBean.getInPhoto().equals("1")) {//拍照退出
            PermissionUtils.requstActivityCamera(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
                @Override
                public void onSuccess() {//CaptureActivity
                    showToast("请进行进店拍照");
                    //进店签到
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), TakingPicturesActivity.class);
                    intent.putExtra("type", PublicFinal.SIGN);
                    intent.putExtra("customer", clientBean);
                    /*intent.putExtra("latitude", clientBean.getCc_latitude());
                    intent.putExtra("longtitude", clientBean.getCc_longitude());
                    if (address == null || address.length() == 0) {
                        intent.putExtra("address", clientBean.getCc_address());
                    } else {
                        intent.putExtra("address", clientBean.getCc_address());
                    }*/
                    startActivity(intent);
                }

                @Override
                public void onDilogCancal() {
                    showToast("获取相机权限失败!");
                }
            });
        } else {
            sign(clientBean);
        }
    }

    /**
     * 签到--客户刷档案卡进行签到
     */
    private void sign(final ClientBean bean) {

        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    showToast(jsonBean.getRepMsg());
                    UserInfoUtils.setTaskId(getActivity(),jsonBean.getTaskId());//客户签到id
                    UserInfoUtils.setClientInfo(getActivity(), bean);

                    //将客户余额、欠款重置
                    UserInfoUtils.setCustomerBalance(getActivity(), null);
                    UserInfoUtils.setSafetyArrearsNum(getActivity(), null);
                    UserInfoUtils.setAfterAmount(getActivity(), null);

                    //"cc_name":"小李","lineId":"85","lineName":"阴飞虎3线"
                    if (!TextUtils.isEmpty(bean.getLineId()) && !bean.getLineId().equals(UserInfoUtils.getLineId(getActivity()))) {
                        UserInfoUtils.setLineId(getActivity(), bean.getLineId());
                        UserInfoUtils.setLineName(getActivity(), bean.getLineName());
                    }

                    clientBean = bean;
                    customerSignUpdate();
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
        httpUtil.post(M_Url.sign, L_RequestParams.sign(userBean.getUserId(), UserInfoUtils.getLineId(getActivity()) + "",
                userBean.getOrgid(), bean.getCc_id(), bean.getCc_longitude(), bean.getCc_latitude(),
                userBean.getIsScan(), "2", bean.getCc_address(), null), true);
    }

    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();
        //        signOut();
        LogUtils.e("location", "home" + location.getAddrStr() + location.getLatitude());
        mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        locationService.stop();

        LogUtils.e("home", "定位成功");
    }

    @Override
    public void locationError(BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        locationService.stop(); //停止定位服务
        LogUtils.e("home", "定位失败");
    }


    private void initCustomTimePicker() {

        mTimePickerUtil.initCustomTimePicker(getActivity(), new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                int numMonth = DateUtils.getMonth(DateUtils.getDate(startDate),
                        DateUtils.getDate(endDate));
                if (numMonth > 3) {
                    showToast("筛选时间不能超过三个月");
                    return;
                }

                if (datePrickType == 2 && DateUtils.getDateSpace(
                        startDate, endDate) < 7) {
                    showToast("筛选时间不能小于7天");
                    LogUtils.e("getDutyDays1", DateUtils.getDateSpace(
                            startDate, endDate) + "");
                    return;
                }

                startDate = startDate.replace("-", ".");
                endDate = endDate.replace("-", ".");

                mTimePickerUtil.calendarPopup.dismiss();
                switch (datePrickType) {
                    case 1:
                        mTvYsjbSelect.setText("自定义");
                        startDateYs = startDate;
                        endDateYs = endDate;
                        mTvYsjbDate.setText(startDate + "-" + endDate);
                        loadYsjb();
                        break;
                    case 2:
                        mTvYsqsSelect.setText("自定义");
                        startDateYsqs = startDate;
                        endDateYsqs = endDate;
                        mTvYsqsDate.setText(startDate + "-" + endDate);
                        chartType = 1;
                        loadYsqs();
                        break;
                }
            }
        }, this);

    }


    /**
     * 设置 1:订单金额 2:收款金额 3:订单量
     *
     * @param type
     */
    private void dateBtnSytle(int type) {
        mBtnOrderAmount.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        mBtnReceiptAmount.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        mBtnOrderNum.setBackgroundColor(UIUtils.getColor(R.color.transparent));

        mBtnOrderAmount.setTextColor(UIUtils.getColor(R.color.text_gray));
        mBtnReceiptAmount.setTextColor(UIUtils.getColor(R.color.text_gray));
        mBtnOrderNum.setTextColor(UIUtils.getColor(R.color.text_gray));

        mBtnOrderAmount.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mBtnReceiptAmount.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mBtnOrderNum.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        switch (type) {
            case 1:
                mBtnOrderAmount.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_left_btn));
                mBtnOrderAmount.setTextColor(UIUtils.getColor(R.color.white));
                mBtnOrderAmount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                mBtnReceiptAmount.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_centre_btn));
                mBtnReceiptAmount.setTextColor(UIUtils.getColor(R.color.white));
                mBtnReceiptAmount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 3:
                mBtnOrderNum.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_right_btn));
                mBtnOrderNum.setTextColor(UIUtils.getColor(R.color.white));
                mBtnOrderNum.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }
    }

    /**
     * 初始换折线图
     */
    private void initLineView() {
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        //绘制图表右下角文字描述信息
        mChartLine.setDescription("");
        mChartLine.setDrawGridBackground(false);
        mChartLine.setDragEnabled(false);// 是否可以拖拽
        mChartLine.setScaleEnabled(false);// 是否可以缩放
        //        mChartLine.setBackgroundColor(getResources().getColor(R.color.white));
        //        mChartLine.setGridBackgroundColor(getResources().getColor(R.color.white)); //设置折线图的背景颜色

        //绘制图表的X轴
        XAxis xAxis = mChartLine.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(UIUtils.getColor(R.color.text_gray));
        xAxis.setSpaceBetweenLabels(1); //实现X轴数据显示的间隔

        //绘制图表的Y轴
        YAxis leftAxis = mChartLine.getAxisLeft();
        leftAxis.setTypeface(mTf);
        //false:代表值是平均分配的;
        leftAxis.setLabelCount(7, false);
        leftAxis.setEnabled(false); // 隐藏Y坐标轴
        //        leftAxis.setGridColor(
        //                getResources().getColor(R.color.transparent));

        mChartLine.getAxisRight().setEnabled(false);
        //        YAxis rightAxis = chart.getAxisRight();
        //        rightAxis.setTypeface(mTf);
        //        rightAxis.setLabelCount(5, false);
        //        rightAxis.setDrawGridLines(false);

        //        mChartLine.setData(generateDataLine());
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        //mChartLine.animateX(750);

        //可以放大X轴比例,从而实现X轴的左右滑动----此方法不能分本解决问题
        //        Matrix m=new Matrix();
        //        m.postScale(1.2f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        //        mChartLine.getViewPortHandler().refresh(m, mChartLine, false);//将图表动画显示之前进行缩放

        mChartLine.animateY(400); // 立即执行的动画,x轴

        Legend l = mChartLine.getLegend();
        l.setEnabled(false);//去掉表外面显示的提示
    }

    /**
     * 设置营收趋势数据
     *
     * @param dataList
     */
    private void setYsqsData(List<TrendsChartBean> dataList) {
        if (dataList.size() > 7) {
            int width = this.width / 7;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartLine.getLayoutParams();
            layoutParams.width = width * dataList.size();
            mChartLine.setLayoutParams(layoutParams);
        } else {
            int width = this.width;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartLine.getLayoutParams();
            layoutParams.width = width - UIUtils.dp2px(20);
            mChartLine.setLayoutParams(layoutParams);
        }

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < dataList.size(); i++) {
            if (ysqsType == 3) {//订单数量
                e1.add(new Entry(dataList.get(i).getOrderNum(), i));
            } else {
                e1.add(new Entry(dataList.get(i).getAmount(), i));
            }
        }
        LineDataSet d1 = new LineDataSet(e1, "订单数量");
        //指定数据集合绘制时候的属性
        //        d1.setLineWidth(1.5f);
        d1.setCircleSize(4f);
        d1.setHighLightColor(Color.BLACK);
        d1.setDrawValues(true);
        d1.setDrawCircles(true);//比现实小圆点
        d1.setDrawCircleHole(true);
        d1.setDrawFilled(true);//设置允许填充
        d1.setDrawCubic(true);
        d1.setDrawFilled(true);
        d1.setValueTextSize(9f);
        d1.setFillAlpha(75);
        d1.setFillColor(Color.rgb(201, 220, 255));//设置填充颜色
        d1.setColor(UIUtils.getColor(R.color.color_699dff));
        if (ysqsType == 3) {
            d1.setValueFormatter(new MyYAxisValueTwoFormatter());
        } else {
            d1.setValueFormatter(new MyYAxisValueOneFormatter());
        }


        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(getMonths(dataList), sets);
        mChartLine.setData(cd);
    }

    private ArrayList<String> m = new ArrayList<String>();

    private ArrayList<String> getMonths(List<TrendsChartBean> dataList) {
        m.clear();
        if (dateYsqs == 2 && dataList.size() == 7) {
            m.add("周一");
            m.add("周二");
            m.add("周三");
            m.add("周四");
            m.add("周五");
            m.add("周六");
            m.add("周日");
        } else {
            for (TrendsChartBean bean : dataList) {
                //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周,
                //8.前天;9.上上周;10.上上月;11.今年;12.去年;13.前年
                switch (dateYsqs) {
                    case 3://本月
                    case 5:
                    case 10:
                        m.add(bean.getMonthStr() + "." + bean.getDayStr());
                        break;
                    case 11:
                    case 12:
                    case 13:
                        m.add(bean.getMonthStr() + "月");
                        break;
                    default:
                        m.add(bean.getMonthStr() + "." + bean.getDayStr());
                        break;
                }
            }
        }
        return m;
    }


    /**
     * 简报请求接口
     */
    private void loadYsjb() {
        AsyncHttpUtil<BriefingBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BriefingBean.class, new IUpdateUI<BriefingBean>() {
            @Override
            public void updata(BriefingBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvTotalAmount.setText(jsonBean.getTotalAmount() + "");
                    // 设置数据
                    mTvTotalAmount.withNumber(jsonBean.getTotalAmount());
                    // 设置动画播放时间
                    mTvTotalAmount.setDuration(1000);
                    // 开始播放动画
                    mTvTotalAmount.start();
                    mTvOrderNum.setText("/" + jsonBean.getOrderNum() + "笔");

                    mTvReceiveAmount.setText(jsonBean.getReceiveAmount() + "");
                    // 设置数据
                    mTvReceiveAmount.withNumber(jsonBean.getReceiveAmount());
                    // 设置动画播放时间
                    mTvReceiveAmount.setDuration(1000);
                    // 开始播放动画
                    mTvReceiveAmount.start();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        String startDate = startDateYs.replace(".", "-");
        String endDate = endDateYs.replace(".", "-");
        httpUtil.post(M_Url.getBriefing, G_RequestParams.getBriefing(startDate, endDate), true);
    }

    /**
     * 营收趋势
     */
    private void loadYsqs() {
        AsyncHttpUtil<TrendsChartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TrendsChartBean.class, new IUpdateUI<TrendsChartBean>() {
            @Override
            public void updata(TrendsChartBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        ysqsHelper.showDataView();
                        initLineView();
                        setYsqsData(jsonStr.getDataList());
                    } else {
                        ysqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mChartLine.clear();
                ysqsHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDateYsqs.replace(".", "-");
        String endDate = endDateYsqs.replace(".", "-");
        httpUtil.post(M_Url.getTrendsChart, G_RequestParams.getTrendsChart(startDate, endDate,
                String.valueOf(ysqsType), String.valueOf(chartType)), true);
    }

}
