package com.xdjd.steward.main.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.MyYAxisValueFormatter;
import com.github.mikephil.charting.formatter.MyYAxisValueOneFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.CustomerListActivity;
import com.xdjd.distribution.activity.ReceivableSearchActivity;
import com.xdjd.distribution.activity.RedeemActivity;
import com.xdjd.distribution.activity.ShopWinningRecordActivity;
import com.xdjd.distribution.activity.VisitingAlarmActivity;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.activity.CustomerInformationActivity;
import com.xdjd.steward.activity.CustomerOrderGoodsActivity;
import com.xdjd.steward.activity.DisplayFeeStatisticalActivity;
import com.xdjd.steward.bean.CustOrderAmountBean;
import com.xdjd.steward.bean.CustomerNumBean;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.risenumber.RiseNumberTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.tv_customer_total_num)
    RiseNumberTextView mTvCustomerTotalNum;
    @BindView(R.id.tv_new_add_num)
    RiseNumberTextView mTvNewAddNum;
    @BindView(R.id.ll_customer_list)
    LinearLayout mLlCustomerList;
    @BindView(R.id.tv_khsl_select)
    TextView mTvKhslSelect;
    @BindView(R.id.tv_khsl_date)
    TextView mTvKhslDate;
    @BindView(R.id.ll_khsl)
    LinearLayout mLlKhsl;
    @BindView(R.id.tv_khtj_select)
    TextView mTvKhtjSelect;
    @BindView(R.id.tv_khtj_date)
    TextView mTvKhtjDate;
    @BindView(R.id.ll_khtj)
    LinearLayout mLlKhtj;
    @BindView(R.id.btn_khtj_five)
    TextView mBtnKhtjFive;
    @BindView(R.id.btn_khtj_ten)
    TextView mBtnKhtjTen;
    @BindView(R.id.chart_khtj)
    BarChart mChartKhtj;
    @BindView(R.id.line_chart_ll)
    LinearLayout mLineChartLl;
    @BindView(R.id.ll_customer_order_goods)
    LinearLayout mLlCustomerOrderGoods;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.ll_visiting_alarm)
    LinearLayout mLlVisitingAlarm;
    @BindView(R.id.ll_customer_receivable)
    LinearLayout mLlCustomerReceivable;
    @BindView(R.id.ll_new_customer)
    LinearLayout mLlNewCustomer;
    @BindView(R.id.ll_customer_total)
    LinearLayout mLlCustomerTotal;
    @BindView(R.id.ll_winning)
    LinearLayout mLlWinning;
    @BindView(R.id.ll_display_in)
    LinearLayout mLlDisplayIn;

    /**
     * 日期第一种popup弹框条件类别---1.客户数量统计;2.客户订货统计;
     */
    private int datePopupOneType = 1;

    /**
     * 选择日期区间type--1.客户数量;2.客户订货数量
     */
    private int datePrickType = 1;

    //1.今日;2.本周;3.本月;4.自定义;5上月
    private int dateKhsl = 1;
    private int dateKhtj = 1;

    //客户数量
    private String startDateKhsl;
    private String endDateKhsl;
    //客户订货统计
    private String startDateKhtj;
    private String endDateKhtj;

    //排行查询--1 前五 2.前十
    private int khtjRows = 5;

    //客户订货统计
    private VaryViewHelper khtjHelper = null;

    private TimePickerUtil mTimePickerUtil;

    private Typeface mTf;
    //客户订货统计排名按钮
    private TextView[] khtjTv;

    StewardActivity main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_statistical, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        main = (StewardActivity) getActivity();
        mTimePickerUtil = new TimePickerUtil();

        khtjTv = new TextView[]{mBtnKhtjFive, mBtnKhtjTen};

        typeBtnSytle(0, khtjTv);

        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateKhsl = todayStr;
        endDateKhsl = todayStr;
        mTvKhslDate.setText(startDateKhsl + "-" + endDateKhsl);

        startDateKhtj = todayStr;
        endDateKhtj = todayStr;
        mTvKhtjDate.setText(startDateKhtj + "-" + endDateKhtj);

        khtjHelper = new VaryViewHelper(mChartKhtj);
        khtjHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        initChartBar(mChartKhtj);

        mTimePickerUtil.initCustomTimePicker(getActivity(), new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                int numMonth = DateUtils.getMonth(DateUtils.getDate(startDate),
                        DateUtils.getDate(endDate));
                if (numMonth >= 3) {
                    showToast("筛选时间不能超过三个月");
                    return;
                }

                startDate = startDate.replace("-", ".");
                endDate = endDate.replace("-", ".");

                switch (datePrickType) {
                    case 1:
                        mTvKhslSelect.setText("自定义");
                        dateKhsl = 4;
                        startDateKhsl = startDate;
                        endDateKhsl = endDate;
                        mTvKhslDate.setText(startDate + "-" + endDate);
                        loadKhsl();
                        break;
                    case 2://员工销售
                        mTvKhtjSelect.setText("自定义");
                        dateKhtj = 4;
                        startDateKhtj = startDate;
                        endDateKhtj = endDate;
                        mTvKhtjDate.setText(startDate + "-" + endDate);
                        loadKhtj();
                        break;
                }

                mTimePickerUtil.calendarPopup.dismiss();
            }
        }, this);

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadKhsl();
                loadKhtj();
            }
        });

        mChartKhtj.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
                Intent intent = new Intent(getActivity(), CustomerOrderGoodsActivity.class);
                intent.putExtra("dateStartStr", startDateKhtj);
                intent.putExtra("dateEndStr", endDateKhtj);
                intent.putExtra("dateTypeStr", mTvKhtjSelect.getText().toString());
                startActivity(intent);
            }
        });

        loadKhsl();
        loadKhtj();
    }

    @OnClick({R.id.tv_khsl_select, R.id.ll_khsl, R.id.tv_khtj_select, R.id.ll_khtj, R.id.btn_khtj_five, R.id.btn_khtj_ten,
            R.id.ll_customer_order_goods, R.id.ll_customer_list, R.id.ll_visiting_alarm, R.id.ll_customer_receivable, R.id.ll_new_customer,
            R.id.ll_customer_total,R.id.ll_winning,R.id.ll_display_in})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_khsl_select:
                datePopupOneType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvKhslDate.getText().toString().split("-")[0],
                        mTvKhslDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_khsl:
                datePrickType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvKhslDate.getText().toString().split("-")[0],
                        mTvKhslDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_khtj_select:
                datePopupOneType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvKhtjDate.getText().toString().split("-")[0],
                        mTvKhtjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_khtj:
                datePrickType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvKhtjDate.getText().toString().split("-")[0],
                        mTvKhtjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.btn_khtj_five:
                khtjRows = 5;
                typeBtnSytle(0, khtjTv);
                loadKhtj();
                break;
            case R.id.btn_khtj_ten:
                khtjRows = 10;
                typeBtnSytle(1, khtjTv);
                loadKhtj();
                break;
            case R.id.ll_customer_order_goods://客户订货排行
                intent = new Intent(getActivity(), CustomerOrderGoodsActivity.class);
                intent.putExtra("dateStartStr", startDateKhtj);
                intent.putExtra("dateEndStr", endDateKhtj);
                intent.putExtra("dateTypeStr", mTvKhtjSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_customer_list://客户信息列表
                startActivity(CustomerInformationActivity.class);
                break;
            case R.id.ll_visiting_alarm:
                intent = new Intent(getActivity(), VisitingAlarmActivity.class);
                intent.putExtra("isManage", true);
                startActivity(intent);
                break;
            case R.id.ll_customer_receivable://客户应收款列表
                startActivity(ReceivableSearchActivity.class);
                break;
            case R.id.ll_new_customer://新增客户
                String startDate = startDateKhsl.replace(".", "-");
                String endDate = endDateKhsl.replace(".", "-");
                Intent intent1 = new Intent(getActivity(), CustomerListActivity.class);
                intent1.putExtra("startDate", startDate);
                intent1.putExtra("endDate", endDate);
                startActivity(intent1);
                break;
            case R.id.ll_customer_total://客户总数量
                intent = new Intent(getActivity(), CustomerInformationActivity.class);
                intent.putExtra("isCustomerListing", true);
                startActivity(intent);
                break;
            case R.id.ll_winning://店铺核销汇总
                startActivity(ShopWinningRecordActivity.class);
                break;
            case R.id.ll_display_in://返陈列查询
                startActivity(DisplayFeeStatisticalActivity.class);
                break;
        }
    }

    private void initChartBar(BarChart mChart) {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        // mChart.setDrawXLabels(false);
        //        mChart.setDragEnabled(false);// 是否可以拖拽
        //        mChart.setScaleEnabled(false);// 是否可以缩放
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);

        YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //        leftAxis.setTypeface(mTf);
        //        leftAxis.setLabelCount(8, false);
        //        leftAxis.setValueFormatter(custom);
        //        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //        leftAxis.setSpaceTop(15f);
        leftAxis.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        //        rightAxis.setDrawGridLines(false);
        //        rightAxis.setTypeface(mTf);
        //        rightAxis.setLabelCount(8, false);
        //        rightAxis.setValueFormatter(custom);
        //        rightAxis.setSpaceTop(15f);
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        //        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        //        l.setForm(Legend.LegendForm.SQUARE);
        //        l.setFormSize(9f);
        //        l.setTextSize(11f);
        //        l.setXEntrySpace(4f);
        l.setEnabled(false);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        //        setData(12, 50, mChart);
    }

    /**
     * 设置 1:左侧按钮 2:中间按钮 3:右侧按钮
     *
     * @param index
     */
    private void typeBtnSytle(int index, TextView[] textViewIds) {
        for (TextView tv : textViewIds) {
            tv.setBackgroundColor(UIUtils.getColor(R.color.transparent));
            tv.setTextColor(UIUtils.getColor(R.color.text_gray));
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        switch (index) {
            case 0:
                textViewIds[index].setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_left_btn));
                break;
            case 1:
                textViewIds[index].setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_right_btn));
                break;
        }
        textViewIds[index].setTextColor(UIUtils.getColor(R.color.white));
        textViewIds[index].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    /**
     * 客户数量
     */
    private void loadKhsl() {
        mTvCustomerTotalNum.setText("0");
        mTvNewAddNum.setText("0");
        AsyncHttpUtil<CustomerNumBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CustomerNumBean.class, new IUpdateUI<CustomerNumBean>() {
            @Override
            public void updata(CustomerNumBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvCustomerTotalNum.setText(jsonBean.getTotalCustomerNum() + "");
                    // 设置数据
                    mTvCustomerTotalNum.withNumber(jsonBean.getTotalCustomerNum());
                    // 设置动画播放时间
                    mTvCustomerTotalNum.setDuration(500);
                    // 开始播放动画
                    mTvCustomerTotalNum.start();

                    mTvNewAddNum.setText(jsonBean.getNewCustomerNum() + "");
                    // 设置数据
                    mTvNewAddNum.withNumber(jsonBean.getNewCustomerNum());
                    // 设置动画播放时间
                    mTvNewAddNum.setDuration(1000);
                    // 开始播放动画
                    mTvNewAddNum.start();
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
                mPullScroll.onRefreshComplete();
            }
        });
        String startDate = startDateKhsl.replace(".", "-");
        String endDate = endDateKhsl.replace(".", "-");
        httpUtil.post(M_Url.getCustomerNum, G_RequestParams.getCustomerNum(startDate, endDate), true);
    }

    /**
     * 客户订货统计
     */
    private void loadKhtj() {
        AsyncHttpUtil<CustOrderAmountBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CustOrderAmountBean.class, new IUpdateUI<CustOrderAmountBean>() {
            @Override
            public void updata(CustOrderAmountBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {

                    if (jsonBean.getListData() == null || jsonBean.getListData().size() == 0) {
                        khtjHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    } else {
                        khtjHelper.showDataView();
                        setDataChart(jsonBean.getListData(), mChartKhtj);
                    }
                } else {
                    khtjHelper.showEmptyView(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                khtjHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {

            }
        });
        String startDate = startDateKhtj.replace(".", "-");
        String endDate = endDateKhtj.replace(".", "-");
        httpUtil.post(M_Url.getCustOrderAmountList, G_RequestParams.getCustOrderAmountList(startDate, endDate, "1", String.valueOf(khtjRows)), true);
    }

    private void setDataChart(List<CustOrderAmountBean> list, BarChart mChart) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCustomerId_nameref().length() > (khtjRows == 5 ? 5 : 3)) {//(khtjRows==5?5:3)如果是前途排行则显示5个字,其余显示三个字
                xVals.add(list.get(i).getCustomerId_nameref().substring(0, (khtjRows == 5 ? 5 : 3)).toString() + "..");
            } else {
                xVals.add(list.get(i).getCustomerId_nameref());
            }
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < list.size(); i++) {
            yVals1.add(new BarEntry(list.get(i).getAmount(), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTf);
        data.setValueFormatter(new MyYAxisValueOneFormatter());

        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        switch (datePopupOneType) {
            case 1:
                dateKhsl = position;
                mTvKhslSelect.setText(dateName);
                startDateKhsl = startDate;
                endDateKhsl = endDate;
                mTvKhslDate.setText(startDateKhsl + "-" + endDateKhsl);
                loadKhsl();
                break;
            case 2://员工销售
                dateKhtj = position;
                mTvKhtjSelect.setText(dateName);

                startDateKhtj = startDate;
                endDateKhtj = endDate;

                mTvKhtjDate.setText(startDateKhtj + "-" + endDateKhtj);
                loadKhtj();
                break;
        }
    }

}
