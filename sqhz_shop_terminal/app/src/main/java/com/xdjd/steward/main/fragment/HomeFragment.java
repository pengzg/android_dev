package com.xdjd.steward.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.CustomPercentFormatter;
import com.github.mikephil.charting.formatter.MyYAxisValueOneFormatter;
import com.github.mikephil.charting.formatter.MyYAxisValueTwoFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.OrderSearchActivity;
import com.xdjd.distribution.activity.ReceiptPaymentQueryActivity;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.PhStatisticBean;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.activity.GoodsRankingActivity;
import com.xdjd.steward.activity.RolloutGoodsSummaryActivity;
import com.xdjd.steward.activity.RolloutSalesmanSummaryActivity;
import com.xdjd.steward.activity.RolloutShopSummaryActivity;
import com.xdjd.steward.bean.BriefingBean;
import com.xdjd.steward.bean.CustOrderBean;
import com.xdjd.steward.bean.GoodsSalesCategoryBean;
import com.xdjd.steward.bean.RefundReportBean;
import com.xdjd.steward.bean.TrendsChartBean;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.risenumber.RiseNumberTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/6/19.
 */

public class HomeFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.tv_today)
    TextView mTvToday;
    @BindView(R.id.tv_yesterday)
    TextView mTvYesterday;
    @BindView(R.id.tv_month)
    TextView mTvMonth;
    @BindView(R.id.tv_sales)
    TextView mTvSales;
    @BindView(R.id.tv_th_amount)
    TextView mTvThAmount;
    @BindView(R.id.tv_ys_amount)
    TextView mTvYsAmount;
    @BindView(R.id.ll_income_hand_in)
    LinearLayout mLlIncomeHandIn;
    @BindView(R.id.tv_previous_amount)
    TextView mTvPreviousAmount;
    @BindView(R.id.btn_order_amount)
    TextView mBtnOrderAmount;
    @BindView(R.id.btn_receipt_amount)
    TextView mBtnReceiptAmount;
    @BindView(R.id.btn_order_num)
    TextView mBtnOrderNum;
    @BindView(R.id.btn_customer_area)
    TextView mBtnCustomerArea;
    @BindView(R.id.btn_customer_level)
    TextView mBtnCustomerLevel;
    @BindView(R.id.btn_goods_area)
    TextView mBtnGoodsArea;
    @BindView(R.id.btn_goods_level)
    TextView mBtnGoodsLevel;
    @BindView(R.id.tv_ysjb_select)
    TextView mTvYsjbSelect;
    @BindView(R.id.tv_ysjb_date)
    TextView mTvYsjbDate;
    @BindView(R.id.ll_ysjb)
    LinearLayout mLlYsjb;
    @BindView(R.id.tv_ysqs_select)
    TextView mTvYsqsSelect;
    @BindView(R.id.tv_ysqs_date)
    TextView mTvYsqsDate;
    @BindView(R.id.ll_ysqs)
    LinearLayout mLlYsqs;
    @BindView(R.id.tv_khddtj_select)
    TextView mTvKhddtjSelect;
    @BindView(R.id.tv_khddtj_date)
    TextView mTvKhddtjDate;
    @BindView(R.id.ll_khddtj)
    LinearLayout mLlKhddtj;
    @BindView(R.id.tv_spxstj_select)
    TextView mTvSpxstjSelect;
    @BindView(R.id.tv_spxstj_date)
    TextView mTvSpxstjDate;
    @BindView(R.id.ll_spxstj)
    LinearLayout mLlSpxstj;
    @BindView(R.id.tv_thtj_select)
    TextView mTvThtjSelect;
    @BindView(R.id.tv_thtj_date)
    TextView mTvThtjDate;
    @BindView(R.id.ll_thtj)
    LinearLayout mLlThtj;
    @BindView(R.id.chart_line)
    LineChart mChartLine;
    @BindView(R.id.line_chart_ll)
    LinearLayout mLineChartLl;
    @BindView(R.id.pie_chart_customer)
    PieChart mPieChartCustomer;
    @BindView(R.id.tv_total_amount)
    RiseNumberTextView mTvTotalAmount;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_receive_amount)
    RiseNumberTextView mTvReceiveAmount;
    @BindView(R.id.pie_chart_goods)
    PieChart mPieChartGoods;
    @BindView(R.id.tv_th_num)
    TextView mTvThNum;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.ll_order_search)
    LinearLayout mLlOrderSearch;
    @BindView(R.id.check_unfinished)
    AppCompatCheckBox mCheckUnfinished;
    @BindView(R.id.tv_unfinished)
    TextView mTvUnfinished;
    @BindView(R.id.check_success)
    AppCompatCheckBox mCheckSuccess;
    @BindView(R.id.tv_success)
    TextView mTvSuccess;
    @BindView(R.id.ll_check)
    LinearLayout mLlCheck;
    @BindView(R.id.ll_unfinished)
    LinearLayout mLlUnfinished;
    @BindView(R.id.ll_success)
    LinearLayout mLlSuccess;
    @BindView(R.id.ll_order_amount)
    LinearLayout mLlOrderAmount;
    @BindView(R.id.ll_receipt)
    LinearLayout mLlReceipt;
    @BindView(R.id.ll_th_amount)
    LinearLayout mLlThAmount;
    @BindView(R.id.ll_th_num)
    LinearLayout mLlThNum;
    @BindView(R.id.ll_rollout_goods_summary)
    LinearLayout mLlRolloutGoodsSummary;
    @BindView(R.id.ll_shop_summary)
    LinearLayout mLlShopSummary;
    @BindView(R.id.ll_salesman_summary)
    LinearLayout mLlSalesmanSummary;
    @BindView(R.id.tv_phsj_select)
    TextView mTvPhsjSelect;
    @BindView(R.id.tv_phsj_date)
    TextView mTvPhsjDate;
    @BindView(R.id.ll_gjzb)
    LinearLayout mLlGjzb;
    @BindView(R.id.tv_ph_total_amount)
    RiseNumberTextView mTvPhTotalAmount;
    @BindView(R.id.tv_sale_amount)
    RiseNumberTextView mTvSaleAmount;
    @BindView(R.id.tv_wxs_amount)
    RiseNumberTextView mTvWxsAmount;
    @BindView(R.id.tv_yth_amount)
    RiseNumberTextView mTvYthAmount;

    private Date date = new Date();

    //1.今日;2.本周;3.本月;4.自定义;5上月---营收简报、客户订单统计、商品销售统计、退货统计
    private int dateYsjb = 1;
    private int dateKhsstj = 1;
    private int dateSpxstj = 1;
    private int dateThtj = 1;
    //1.本周;2.本月;3.本年;4.自定义;5上月;6.上周---营收趋势
    private int dateYsqs = 1;

    /**
     * 营收趋势订单类型--1 订单金额 2.收款 3订单数量
     */
    private int ysqsType = 1;
    //营收趋势日、月
    private int chartType = 1;
    /**
     * 客户订单type:-- 1区域  2类别
     */
    private int khddType = 1;
    /**
     * 商品销售type:-- 1区域  2类别
     */
    private int spxsType = 1;
    /**
     * 日期第一种popup弹框条件类别---1.营收简报;2.客户订单统计;3.商品销售统计;4.退货统计
     */
    //    private int datePopupOneType = 1;

    //营收简报起止时间
    private String startDateYs;
    private String endDateYs;
    //营收趋势起止时间
    private String startDateYsqs;
    private String endDateYsqs;
    //客户订单统计起止时间
    private String startDateKhdd;
    private String endDateKhdd;
    //商品销售统计起止时间
    private String startDateSp;
    private String endDateSp;
    //退货统计起止时间
    private String startDateTh;
    private String endDateTh;
    //退货统计起止时间
    private String startDatePh;
    private String endDatePh;

    /**
     * 选择日期区间type--1.营收简报;2.营收趋势;3.客户订单统计;4.商品销售统计;5.退货统计;6.铺货数据统计
     */
    private int datePrickType = 1;

    private Typeface mTf;
    private int width;
    //营收趋势动态加载布局控件
    private VaryViewHelper ysqsHelper = null;
    //客户订单动态加载布局控件
    private VaryViewHelper khddHelper = null;
    //商品销售动态加载布局控件
    private VaryViewHelper spxsHelper = null;

    private TimePickerUtil mTimePickerUtil;

    private StewardActivity main;
    //营收趋势未完成数据列表
    private List<TrendsChartBean> unFinishList;
    //营收趋势已完成数据列表
    private List<TrendsChartBean> successList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steward_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        main = (StewardActivity) getActivity();

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        mTimePickerUtil = new TimePickerUtil();

        dateBtnSytle(1);//订单金额
        customerBtnSytle(1);
        goodsBtnSytle(1);

        initCustomTimePicker();
        initLineView();
        initPieView(mPieChartCustomer);
        initPieView(mPieChartGoods);

        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateYs = todayStr;
        endDateYs = todayStr;
        mTvYsjbDate.setText(startDateYs + "-" + endDateYs);

        startDateKhdd = todayStr;
        endDateKhdd = todayStr;
        mTvKhddtjDate.setText(startDateKhdd + "-" + endDateKhdd);

        startDateSp = todayStr;
        endDateSp = todayStr;
        mTvSpxstjDate.setText(startDateSp + "-" + endDateSp);

        startDateTh = todayStr;
        endDateTh = todayStr;
        mTvThtjDate.setText(startDateTh + "-" + endDateTh);

        startDatePh = todayStr;
        endDatePh = todayStr;
        mTvPhsjDate.setText(startDatePh + "-" + endDatePh);

        startDateYsqs = DateUtils.getFirstDayOfWeek(date, DateUtils.dateFormater4);
        endDateYsqs = DateUtils.getLastDayOfWeek(date, DateUtils.dateFormater4);
        mTvYsqsDate.setText(startDateYsqs + "-" + endDateYsqs);

        ysqsHelper = new VaryViewHelper(mLineChartLl);
        ysqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        khddHelper = new VaryViewHelper(mPieChartCustomer);
        khddHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        spxsHelper = new VaryViewHelper(mPieChartGoods);
        spxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        //营收趋势
        mLlCheck.setVisibility(View.VISIBLE);

        loadYsjb();
        loadYsqs();
        loadKhdd();
        loadSpxs();
        loadTh();
        loadPhDataStatistic();

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadYsjb();
                loadYsqs();
                loadKhdd();
                loadSpxs();
                loadTh();
                loadPhDataStatistic();
            }
        });

        mCheckSuccess.setClickable(false);
        mCheckUnfinished.setClickable(false);

        mCheckSuccess.setOnCheckedChangeListener(mySuccess);
        mCheckUnfinished.setOnCheckedChangeListener(myUnfinished);
    }

    private CompoundButton.OnCheckedChangeListener mySuccess = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                mCheckUnfinished.setClickable(true);
                mCheckUnfinished.setEnabled(true);
            } else {
                mCheckUnfinished.setClickable(false);
                mCheckUnfinished.setEnabled(false);
            }
            setYsqsData(successList, unFinishList);
        }
    };

    private CompoundButton.OnCheckedChangeListener myUnfinished = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                mCheckSuccess.setClickable(true);
                mCheckSuccess.setEnabled(true);
            } else {
                mCheckSuccess.setClickable(false);
                mCheckSuccess.setEnabled(false);
            }
            setYsqsData(successList, unFinishList);
        }
    };

    private void initCustomTimePicker() {
        mTimePickerUtil.initCustomTimePicker(getActivity(), new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                int numMonth = DateUtils.getMonth(DateUtils.getDate(startDate),
                        DateUtils.getDate(endDate));
                if (datePrickType != 1 && numMonth > 3) {
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

                switch (datePrickType) {
                    case 1://营收简报
                        mTvYsjbSelect.setText("自定义");
                        dateYsjb = 4;
                        startDateYs = startDate;
                        endDateYs = endDate;
                        mTvYsjbDate.setText(startDate + "-" + endDate);
                        loadYsjb();
                        break;
                    case 2://营收趋势
                        mTvYsqsSelect.setText("自定义");
                        dateYsqs = 4;
                        startDateYsqs = startDate;
                        endDateYsqs = endDate;
                        mTvYsqsDate.setText(startDate + "-" + endDate);
                        chartType = 1;
                        loadYsqs();
                        break;
                    case 3://客户统计
                        mTvKhddtjSelect.setText("自定义");
                        dateKhsstj = 4;
                        startDateKhdd = startDate;
                        endDateKhdd = endDate;
                        mTvKhddtjDate.setText(startDate + "-" + endDate);

                        loadKhdd();
                        break;
                    case 4://商品统计
                        mTvSpxstjSelect.setText("自定义");
                        dateSpxstj = 4;
                        startDateSp = startDate;
                        endDateSp = endDate;
                        mTvSpxstjDate.setText(startDate + "-" + endDate);

                        loadSpxs();
                        break;
                    case 5:
                        mTvThtjSelect.setText("自定义");
                        dateThtj = 4;
                        startDateTh = startDate;
                        endDateTh = endDate;
                        mTvThtjDate.setText(startDate + "-" + endDate);
                        loadTh();
                        break;
                    case 6:
                        mTvPhsjSelect.setText("自定义");
                        startDatePh = startDate;
                        endDatePh = endDate;
                        mTvPhsjDate.setText(startDate + "-" + endDate);
                        loadPhDataStatistic();
                        break;
                }
                mTimePickerUtil.calendarPopup.dismiss();
            }
        }, this);
    }

    @OnClick({R.id.btn_order_amount, R.id.btn_receipt_amount, R.id.btn_order_num,
            R.id.btn_customer_area, R.id.btn_customer_level, R.id.btn_goods_area, R.id.btn_goods_level,
            R.id.tv_ysjb_select, R.id.ll_ysjb, R.id.tv_ysqs_select, R.id.ll_ysqs, R.id.tv_khddtj_select,
            R.id.ll_khddtj, R.id.tv_spxstj_select, R.id.ll_spxstj, R.id.tv_thtj_select, R.id.ll_thtj,
            R.id.ll_order_search, R.id.ll_unfinished, R.id.ll_success, R.id.ll_order_amount, R.id.ll_receipt,
            R.id.ll_th_amount, R.id.ll_th_num, R.id.ll_rollout_goods_summary, R.id.ll_shop_summary, R.id.ll_salesman_summary,
            R.id.tv_phsj_select, R.id.ll_gjzb})
    public void onClick(View view) {
        Calendar calendar;
        switch (view.getId()) {
            case R.id.btn_order_amount:
                dateBtnSytle(1);//订单金额
                //1 订单金额 2.收款 3订单数量
                ysqsType = 1;
                mLlCheck.setVisibility(View.VISIBLE);
                mTvUnfinished.setText("未收款订单");
                mTvSuccess.setText("已收款订单");
                mCheckSuccess.setOnCheckedChangeListener(null);
                mCheckUnfinished.setOnCheckedChangeListener(null);
                mCheckSuccess.setClickable(true);
                mCheckSuccess.setEnabled(true);
                mCheckSuccess.setChecked(true);
                mCheckUnfinished.setClickable(true);
                mCheckUnfinished.setEnabled(true);
                mCheckUnfinished.setChecked(true);
                mCheckSuccess.setOnCheckedChangeListener(mySuccess);
                mCheckUnfinished.setOnCheckedChangeListener(myUnfinished);
                loadYsqs();
                break;
            case R.id.btn_receipt_amount:
                dateBtnSytle(2);//收款金额
                ysqsType = 2;
                mLlCheck.setVisibility(View.INVISIBLE);
                loadYsqs();
                break;
            case R.id.btn_order_num:
                dateBtnSytle(3);//订单量
                ysqsType = 3;
                mLlCheck.setVisibility(View.VISIBLE);
                mTvUnfinished.setText("未完成订单");
                mTvSuccess.setText("已完成订单");
                mCheckSuccess.setOnCheckedChangeListener(null);
                mCheckUnfinished.setOnCheckedChangeListener(null);
                mCheckSuccess.setClickable(true);
                mCheckSuccess.setEnabled(true);
                mCheckSuccess.setChecked(true);
                mCheckUnfinished.setClickable(true);
                mCheckUnfinished.setEnabled(true);
                mCheckUnfinished.setChecked(true);
                mCheckSuccess.setOnCheckedChangeListener(mySuccess);
                mCheckUnfinished.setOnCheckedChangeListener(myUnfinished);
                loadYsqs();
                break;
            case R.id.btn_customer_area://客户订单统计区域
                customerBtnSytle(1);
                khddType = 1;
                loadKhdd();
                break;
            case R.id.btn_customer_level://客户订单类别
                customerBtnSytle(2);
                khddType = 2;
                loadKhdd();
                break;
            case R.id.btn_goods_area://商品销售统计区域
                goodsBtnSytle(1);
                spxsType = 1;
                loadSpxs();
                break;
            case R.id.btn_goods_level://商品销售类别
                goodsBtnSytle(2);
                spxsType = 2;
                loadSpxs();
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
            case R.id.tv_khddtj_select://客户订单统计
                datePrickType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvKhddtjDate.getText().toString().split("-")[0],
                        mTvKhddtjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_khddtj:
                datePrickType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvKhddtjDate.getText().toString().split("-")[0],
                        mTvKhddtjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_spxstj_select://商品销售统计
                datePrickType = 4;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvSpxstjDate.getText().toString().split("-")[0],
                        mTvSpxstjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_spxstj:
                datePrickType = 4;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvSpxstjDate.getText().toString().split("-")[0],
                        mTvSpxstjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_thtj_select://退货统计
                datePrickType = 5;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvThtjDate.getText().toString().split("-")[0],
                        mTvThtjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_thtj:
                datePrickType = 5;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvThtjDate.getText().toString().split("-")[0],
                        mTvThtjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_phsj_select://铺货数据统计
                datePrickType = 6;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvPhsjDate.getText().toString().split("-")[0],
                        mTvPhsjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_gjzb:
                datePrickType = 6;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvPhsjDate.getText().toString().split("-")[0],
                        mTvPhsjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_order_search://订单查询
                Intent intent = new Intent(getActivity(), OrderSearchActivity.class);
                intent.putExtra("dateStartStr", startDateYsqs);
                intent.putExtra("dateEndStr", endDateYsqs);
                intent.putExtra("dateTypeStr", mTvYsqsSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_unfinished:
                if (!mCheckSuccess.isChecked()) {
                    return;
                }
                if (mCheckUnfinished.isChecked()) {
                    mCheckUnfinished.setChecked(false);
                    mCheckSuccess.setClickable(false);
                    mCheckSuccess.setEnabled(false);
                } else {
                    mCheckUnfinished.setChecked(true);
                    mCheckSuccess.setClickable(true);
                    mCheckSuccess.setEnabled(true);
                }
                setYsqsData(successList, unFinishList);
                break;
            case R.id.ll_success:
                if (!mCheckUnfinished.isChecked()) {
                    return;
                }
                if (mCheckSuccess.isChecked()) {
                    mCheckSuccess.setChecked(false);
                    mCheckUnfinished.setClickable(false);
                    mCheckUnfinished.setEnabled(false);
                } else {
                    mCheckSuccess.setChecked(true);
                    mCheckUnfinished.setClickable(true);
                    mCheckUnfinished.setEnabled(true);
                }
                setYsqsData(successList, unFinishList);
                break;
            case R.id.ll_order_amount:
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
            case R.id.ll_th_amount://退货金额
                intent = new Intent(getActivity(), GoodsRankingActivity.class);
                intent.putExtra("type", "3");
                intent.putExtra("dateStartStr", startDateTh);
                intent.putExtra("dateEndStr", endDateTh);
                intent.putExtra("dateTypeStr", mTvThtjSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_th_num://退货数量
                intent = new Intent(getActivity(), GoodsRankingActivity.class);
                intent.putExtra("type", "3");
                intent.putExtra("dateStartStr", startDateTh);
                intent.putExtra("dateEndStr", endDateTh);
                intent.putExtra("dateTypeStr", mTvThtjSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_rollout_goods_summary://铺货商品汇总
                startActivity(RolloutGoodsSummaryActivity.class);
                break;
            case R.id.ll_shop_summary://铺货店铺汇总
                startActivity(RolloutShopSummaryActivity.class);
                break;
            case R.id.ll_salesman_summary://业务员铺货汇总
                startActivity(RolloutSalesmanSummaryActivity.class);
                break;
        }
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
     * 设置客户订单统计 1:区域 2:级别
     *
     * @param type
     */
    private void customerBtnSytle(int type) {
        mBtnCustomerArea.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        mBtnCustomerLevel.setBackgroundColor(UIUtils.getColor(R.color.transparent));

        mBtnCustomerArea.setTextColor(UIUtils.getColor(R.color.text_gray));
        mBtnCustomerLevel.setTextColor(UIUtils.getColor(R.color.text_gray));

        mBtnCustomerArea.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mBtnCustomerLevel.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        switch (type) {
            case 1:
                mBtnCustomerArea.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_left_btn));
                mBtnCustomerArea.setTextColor(UIUtils.getColor(R.color.white));
                mBtnCustomerArea.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                mBtnCustomerLevel.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_right_btn));
                mBtnCustomerLevel.setTextColor(UIUtils.getColor(R.color.white));
                mBtnCustomerLevel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }
    }

    /**
     * 设置商品销售 1:区域 2:级别
     *
     * @param type
     */
    private void goodsBtnSytle(int type) {
        mBtnGoodsArea.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        mBtnGoodsLevel.setBackgroundColor(UIUtils.getColor(R.color.transparent));

        mBtnGoodsArea.setTextColor(UIUtils.getColor(R.color.text_gray));
        mBtnGoodsLevel.setTextColor(UIUtils.getColor(R.color.text_gray));

        mBtnGoodsArea.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mBtnGoodsLevel.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        switch (type) {
            case 1:
                mBtnGoodsArea.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_left_btn));
                mBtnGoodsArea.setTextColor(UIUtils.getColor(R.color.white));
                mBtnGoodsArea.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                mBtnGoodsLevel.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_right_btn));
                mBtnGoodsLevel.setTextColor(UIUtils.getColor(R.color.white));
                mBtnGoodsLevel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }
    }

    //    private void initDateOnePopup() {
    //        dateOnePopup = new DateOnePopup(getActivity(), new DateOnePopup.ItemDateOnListener() {
    //            @Override
    //            public void onItemDate(int position, String dateName, String startDate, String endDate) {
    //                LogUtils.e("dateOnePopup", position + "-" + dateName);
    //                //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
    //                switch (datePopupOneType) {
    //                    case 1:
    //                        dateYsjb = position;
    //                        mTvYsjbSelect.setText(dateName);
    //                        startDateYs = startDate;
    //                        endDateYs = endDate;
    //                        mTvYsjbDate.setText(startDateYs + "-" + endDateYs);
    //                        loadYsjb();
    //                        break;
    //                    case 2://客户订单统计
    //                        dateKhsstj = position;
    //                        mTvKhddtjSelect.setText(dateName);
    //                        startDateKhdd = startDate;
    //                        endDateKhdd = endDate;
    //                        mTvKhddtjDate.setText(startDateKhdd + "-" + endDateKhdd);
    //                        loadKhdd();
    //                        break;
    //                    case 3://商品销售
    //                        dateSpxstj = position;
    //                        mTvSpxstjSelect.setText(dateName);
    //                        startDateSp = startDate;
    //                        endDateSp = endDate;
    //                        mTvSpxstjDate.setText(startDateSp + "-" + endDateSp);
    //                        loadSpxs();
    //                        break;
    //                    case 4://退货统计
    //                        dateThtj = position;
    //                        mTvThtjSelect.setText(dateName);
    //                        startDateTh = startDate;
    //                        endDateTh = endDate;
    //                        mTvThtjDate.setText(startDateTh + "-" + endDateTh);
    //                        loadTh();
    //                        break;
    //                }
    //            }
    //        });
    //    }
    //    /**
    //     * 显示日、周、月、上月选择
    //     */
    //    private void showPwDateOne() {
    //        StewardActivity main = (StewardActivity) getActivity();
    //
    //        switch (datePopupOneType) {
    //            case 1:
    //                dateOnePopup.setDateType(dateYsjb);
    //                break;
    //            case 2:
    //                dateOnePopup.setDateType(dateKhsstj);
    //                break;
    //            case 3:
    //                dateOnePopup.setDateType(dateSpxstj);
    //                break;
    //            case 4:
    //                dateOnePopup.setDateType(dateThtj);
    //                break;
    //        }
    //        // 显示窗口
    //        dateOnePopup.showAtLocation(main.getMainId(),
    //                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    //    }
    //    private void initDateTwoPopup() {
    //        dateTwoPopup = new DateTwoPopup(getActivity(), new DateTwoPopup.ItemDateOnListener() {
    //            @Override
    //            public void onItemDate(int position, String dateName, String startDate, String endDate) {
    //                LogUtils.e("dateTwoPopup", position + "-" + dateName);
    //                //1.本周;2.本月;3.本年;5.上月;6.上周
    //                dateYsqs = position;
    //                mTvYsqsSelect.setText(dateName);
    //                startDateYsqs = startDate;
    //                endDateYsqs = endDate;
    //                switch (dateYsqs) {
    //                    case 1:
    //                        chartType = 1;
    //                        loadYsqs();
    //                        break;
    //                    case 2:
    //                        chartType = 1;
    //                        loadYsqs();
    //                        break;
    //                    case 3:
    //                        chartType = 2;
    //                        loadYsqs();
    //                        break;
    //                    case 5:
    //                        chartType = 1;
    //                        loadYsqs();
    //                        break;
    //                    case 6:
    //                        chartType = 1;
    //                        loadYsqs();
    //                        break;
    //                }
    //                mTvYsqsDate.setText(startDateYsqs + "-" + endDateYsqs);
    //            }
    //        });
    //    }
    //    /**
    //     * 显示周、月、年选择
    //     */
    //    private void showPwDateTwo() {
    //        StewardActivity main = (StewardActivity) getActivity();
    //
    //        dateTwoPopup.setDateType(dateYsqs);
    //        // 显示窗口
    //        dateTwoPopup.showAtLocation(main.getMainId(),
    //                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    //    }

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

        mChartLine.animateY(1000); // 立即执行的动画,x轴

        Legend l = mChartLine.getLegend();
        l.setEnabled(true);//去掉表外面显示的提示
    }

    /**
     * 设置营收趋势数据
     *
     * @param dataList
     * @param unFinishListData 未完成数量
     */
    private void setYsqsData(List<TrendsChartBean> dataList, List<TrendsChartBean> unFinishListData) {
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

        //----------未完成的订单数量或金额--1 订单金额 2.收款 3订单数量--------------
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        LineDataSet set2 = null;
        if (ysqsType != 2 && unFinishListData != null && unFinishListData.size() > 0 && mCheckUnfinished.isChecked()) {
            for (int i = 0; i < unFinishListData.size(); i++) {
                if (ysqsType == 3) {//订单数量
                    yVals.add(new Entry(unFinishListData.get(i).getOrderNum(), i));
                } else {
                    yVals.add(new Entry(unFinishListData.get(i).getOrderAmount(), i));
                }
            }
            if (ysqsType == 1) {
                set2 = new LineDataSet(yVals, "未收款订单金额");
            } else if (ysqsType == 3) {
                set2 = new LineDataSet(yVals, "未完成订单数量");
            }

            set2.setColor(UIUtils.getColor(R.color.color_699dff));
            set2.setDrawValues(true);
            set2.setDrawCircles(true);//比现实小圆点
            set2.setDrawCircleHole(true);
            set2.setDrawFilled(true);//设置允许填充
            set2.setDrawCubic(true);
            set2.setValueTextSize(8f);
            set2.setFillAlpha(25);
            set2.setLineWidth(1.5f);
            set2.setCircleSize(3f);
            //            set2.setFillColor(Color.RED);
            set2.setHighLightColor(Color.rgb(244, 117, 117));

            if (ysqsType == 3) {
                set2.setValueFormatter(new MyYAxisValueTwoFormatter());
            } else {
                set2.setValueFormatter(new MyYAxisValueOneFormatter());
            }
        }

        //--------------已完成的数量或金额----------------
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < dataList.size(); i++) {
            if (ysqsType == 3) {//订单数量
                e1.add(new Entry(dataList.get(i).getOrderNum(), i));
            } else {
                e1.add(new Entry(dataList.get(i).getAmount(), i));
            }
        }

        LineDataSet d1 = null;
        switch (ysqsType) {
            case 1:
                d1 = new LineDataSet(e1, "已收款订单金额");
                break;
            case 2:
                d1 = new LineDataSet(e1, "收款金额");
                break;
            case 3:
                d1 = new LineDataSet(e1, "已完成订单数量");
                break;
            default:
                d1 = new LineDataSet(e1, "订单完成量");
                break;
        }
        //指定数据集合绘制时候的属性
        d1.setLineWidth(1.5f);
        d1.setCircleSize(3f);
        d1.setHighLightColor(Color.BLACK);
        d1.setDrawValues(true);
        d1.setDrawCircles(true);//比现实小圆点
        d1.setDrawCircleHole(true);
        d1.setDrawFilled(true);//设置允许填充
        d1.setDrawCubic(true);
        d1.setValueTextSize(8f);
        d1.setFillAlpha(75);
        d1.setCircleColor(UIUtils.getColor(R.color.color_orange));
        d1.setColor(UIUtils.getColor(R.color.color1));
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        if (ysqsType == 3) {
            d1.setValueFormatter(new MyYAxisValueTwoFormatter());
        } else {
            d1.setValueFormatter(new MyYAxisValueOneFormatter());
        }

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        if (ysqsType != 2 && unFinishListData != null && unFinishListData.size() > 0 && mCheckUnfinished.isChecked()) {
            sets.add(set2);
        }
        if (mCheckSuccess.isChecked()) {
            sets.add(d1);
        }

        LineData cd = new LineData(getMonths(dataList), sets);
        mChartLine.setData(cd);
        mChartLine.invalidate();
        /*ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int i = 0; i < 3; i++) {
            ArrayList<Entry> yVals = new ArrayList<>();
            for (int j = 0; j < yy[i].length; j++) {
                yVals.add(new Entry(Float.parseFloat(yy[i][j]), j));
            }
            LineDataSet set1 = new LineDataSet(yVals, "DataSet " + (i + 1));
            set1.setDrawCubic(true);  //设置曲线为圆滑的线
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);  //设置包括的范围区域填充颜色
            set1.setDrawCircles(true);  //设置有圆点
            set1.setLineWidth(2f);    //设置线的宽度
            set1.setCircleSize(5f);   //设置小圆的大小
            int color = mColors[i % mColors.length];
            set1.setHighLightColor(color);
            set1.setColor(color);    //设置曲线的颜色
            set1.setFillColor(color);
            dataSets.add(set1);
        }*/
    }

    private ArrayList<String> m = new ArrayList<String>();

    private ArrayList<String> getMonths(List<TrendsChartBean> dataList) {
        m.clear();
        if (dateYsqs == 1 && dataList.size() == 7) {
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
     * 初始化饼状view
     *
     * @param mChart
     */
    private void initPieView(PieChart mChart) {
        //是否按百分比显示
        mChart.setUsePercentValues(false);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        //        mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
        //        mChart.setCenterText(generateCenterSpannableText());
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setBackgroundColor(Color.WHITE);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        //        mChart.setHighlightPerTapEnabled(true);
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);
        // add a selection listener
        //        mChart.setOnChartValueSelectedListener(this);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        Legend l = mChart.getLegend();
        //        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //        l.setXEntrySpace(7f);
        //        l.setYEntrySpace(0f);
        //        l.setYOffset(0f);
        l.setEnabled(false);
    }

    /**
     * 设置饼状图数据
     *
     * @param dataList
     * @param mChart
     * @param pieType  1.客户订单统计;2.商品销售统计
     * @param <T>
     */
    private <T> void setPieData(List<T> dataList, PieChart mChart, int pieType) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < dataList.size(); i++) {
            float yValue;
            if (pieType == 1) {//客户订单统计
                yValue = ((CustOrderBean) dataList.get(i)).getAmount();
            } else {
                yValue = ((GoodsSalesCategoryBean) dataList.get(i)).getAmount();
            }

            yVals1.add(new Entry(yValue, i));
            LogUtils.e("饼状图的值", yValue + "--");
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++) {
            String xValue;
            //1区域  2类别
            if (pieType == 1) {
                CustOrderBean bean = (CustOrderBean) dataList.get(i);
                xValue = khddType == 1 ? bean.getAreaid_nameref() : bean.getCategoryid_nameref();
            } else {
                GoodsSalesCategoryBean bean = (GoodsSalesCategoryBean) dataList.get(i);
                xValue = spxsType == 1 ? bean.getAreaid_nameref() : bean.getCategoryid_nameref();
            }
            xVals.add(xValue);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //        for (int c : ColorTemplate.VORDIPLOM_COLORS)
        //            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        //        colors.add(UIUtils.getColor(R.color.color1));
        //        colors.add(UIUtils.getColor(R.color.color2));
        //        colors.add(UIUtils.getColor(R.color.color3));

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new CustomPercentFormatter());
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    /**
     * 简报请求接口
     */
    private void loadYsjb() {
        mTvTotalAmount.setText("0.00");
        mTvOrderNum.setText("/0" + "笔");
        mTvReceiveAmount.setText("0.00");
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
                        unFinishList = jsonStr.getUnFinishListData();
                        successList = jsonStr.getDataList();
                        setYsqsData(successList, unFinishList);
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
                mPullScroll.onRefreshComplete();
            }
        });
        String startDate = startDateYsqs.replace(".", "-");
        String endDate = endDateYsqs.replace(".", "-");
        httpUtil.post(M_Url.getTrendsChart, G_RequestParams.getTrendsChart(startDate, endDate,
                String.valueOf(ysqsType), String.valueOf(chartType)), true);
    }

    /**
     * 客户订单统计接口
     */
    private void loadKhdd() {
        AsyncHttpUtil<CustOrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CustOrderBean.class, new IUpdateUI<CustOrderBean>() {
            @Override
            public void updata(CustOrderBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        khddHelper.showDataView();
                        initPieView(mPieChartCustomer);
                        setPieData(jsonStr.getDataList(), mPieChartCustomer, 1);
                    } else {
                        khddHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mPieChartCustomer.clear();
                khddHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDateKhdd.replace(".", "-");
        String endDate = endDateKhdd.replace(".", "-");
        httpUtil.post(M_Url.getCustOrder, G_RequestParams.getCustOrder(startDate, endDate,
                String.valueOf(khddType)), true);
    }

    private void loadSpxs() {
        AsyncHttpUtil<GoodsSalesCategoryBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsSalesCategoryBean.class,
                new IUpdateUI<GoodsSalesCategoryBean>() {
                    @Override
                    public void updata(GoodsSalesCategoryBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                                spxsHelper.showDataView();
                                initPieView(mPieChartGoods);
                                setPieData(jsonStr.getDataList(), mPieChartGoods, 2);
                            } else {
                                spxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                            }
                        } else {
                            showToast(jsonStr.getRepMsg());
                        }
                    }

                    @Override
                    public void sendFail(ExceptionType s) {
                        mPieChartGoods.clear();
                        spxsHelper.showEmptyView(s.getDetail());
                    }

                    @Override
                    public void finish() {
                    }
                });
        String startDate = startDateSp.replace(".", "-");
        String endDate = endDateSp.replace(".", "-");
        httpUtil.post(M_Url.getGoodsSalesCategory, G_RequestParams.getGoodsSalesCategory(startDate, endDate,
                String.valueOf(spxsType)), true);
    }

    private void loadTh() {
        mTvThAmount.setText("0.00");
        mTvThNum.setText("0");
        AsyncHttpUtil<RefundReportBean> httpUtil = new AsyncHttpUtil<>(getActivity(), RefundReportBean.class, new IUpdateUI<RefundReportBean>() {
            @Override
            public void updata(RefundReportBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvThAmount.setText(jsonBean.getAmount());
                    mTvThNum.setText(jsonBean.getOrderNum());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDateTh.replace(".", "-");
        String endDate = endDateTh.replace(".", "-");
        httpUtil.post(M_Url.getRefundReport, G_RequestParams.getRefundReport(startDate, endDate), true);
    }

    /**
     * 铺货数据统计
     */
    private void loadPhDataStatistic() {
        AsyncHttpUtil<PhStatisticBean> httpUtil = new AsyncHttpUtil<>(getActivity(), PhStatisticBean.class, new IUpdateUI<PhStatisticBean>() {
            @Override
            public void updata(PhStatisticBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    mTvPhTotalAmount.setText(bean.getPhZje() + "");//铺货总金额
                    // 设置数据
                    mTvPhTotalAmount.withNumber(bean.getPhZje());
                    // 设置动画播放时间
                    mTvPhTotalAmount.setDuration(1000);
                    // 开始播放动画
                    mTvPhTotalAmount.start();
                    mTvSaleAmount.setText(bean.getYxsje() + "");//已销售金额
                    mTvSaleAmount.withNumber(bean.getYxsje());
                    mTvSaleAmount.setDuration(1000);
                    mTvSaleAmount.start();
                    mTvWxsAmount.setText(bean.getWxsje() + "");//未销售金额
                    mTvWxsAmount.withNumber(bean.getWxsje());
                    mTvWxsAmount.setDuration(1000);
                    mTvWxsAmount.start();
                    mTvYthAmount.setText(bean.getThsje() + "");//未销售金额
                    mTvYthAmount.withNumber(bean.getThsje());
                    mTvYthAmount.setDuration(1000);
                    mTvYthAmount.start();
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        String startDate = startDatePh.replace(".", "-");
        String endDate = endDatePh.replace(".", "-");
        httpUtil.post(M_Url.phDataStatistic, L_RequestParams.phDataStatistic(startDate, endDate, "1"), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
        //1.营收简报;2.营收趋势;3.客户订单统计;4.商品销售统计;5.退货统计
        switch (datePrickType) {
            case 1:
                dateYsjb = position;
                mTvYsjbSelect.setText(dateName);
                startDateYs = startDate;
                endDateYs = endDate;
                mTvYsjbDate.setText(startDateYs + "-" + endDateYs);
                loadYsjb();
                break;
            case 2:
                //1.本周;2.本月;3.本年;5.上月;6.上周
                dateYsqs = position;
                mTvYsqsSelect.setText(dateName);

                startDateYsqs = startDate;
                endDateYsqs = endDate;

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
            case 3://客户订单统计
                dateKhsstj = position;
                mTvKhddtjSelect.setText(dateName);
                startDateKhdd = startDate;
                endDateKhdd = endDate;
                mTvKhddtjDate.setText(startDateKhdd + "-" + endDateKhdd);
                loadKhdd();
                break;
            case 4://商品销售
                dateSpxstj = position;
                mTvSpxstjSelect.setText(dateName);
                startDateSp = startDate;
                endDateSp = endDate;
                mTvSpxstjDate.setText(startDateSp + "-" + endDateSp);
                loadSpxs();
                break;
            case 5://退货统计
                dateThtj = position;
                mTvThtjSelect.setText(dateName);
                startDateTh = startDate;
                endDateTh = endDate;
                mTvThtjDate.setText(startDateTh + "-" + endDateTh);
                loadTh();
                break;
            case 6://铺货统计
                mTvPhsjSelect.setText(dateName);
                startDatePh = startDate;
                endDatePh = endDate;
                mTvPhsjDate.setText(startDatePh + "-" + endDatePh);
                loadPhDataStatistic();
                break;
        }
    }

}
