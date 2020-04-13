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
import com.github.mikephil.charting.formatter.MyYAxisValueTwoFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.activity.CartLocationActivity;
import com.xdjd.steward.activity.DisplayFeeStatisticalActivity;
import com.xdjd.steward.activity.SalesmanPhRakingActivity;
import com.xdjd.steward.activity.SalesmanRankingActivity;
import com.xdjd.steward.activity.SalesmanRefundActivity;
import com.xdjd.steward.activity.SalesmanVisitingActivity;
import com.xdjd.steward.activity.VisitMoreActivity;
import com.xdjd.steward.activity.VisitRateActivity;
import com.xdjd.steward.adapter.DisplayFeeStatisticalAdapter;
import com.xdjd.steward.bean.SalesVisitNumBean;
import com.xdjd.steward.bean.SalesmanBean;
import com.xdjd.steward.bean.VisitDataBean;
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
 * Created by lijipei on 2017/6/19.
 */

public class SalesmanFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.ll_salesman_ranking)
    LinearLayout mLlSalesmanRanking;
    @BindView(R.id.ll_visit_detail)
    LinearLayout mLlVisitDetail;
    @BindView(R.id.ll_refund_ranking)
    LinearLayout mLlRefundRanking;
    @BindView(R.id.tv_today_visit)
    TextView mTvTodayVisit;
    @BindView(R.id.tv_today_deal)
    TextView mTvTodayDeal;
    @BindView(R.id.tv_month_visit)
    TextView mTvMonthVisit;
    @BindView(R.id.tv_month_deal)
    TextView mTvMonthDeal;
    @BindView(R.id.tv_bfcj_select)
    TextView mTvBfcjSelect;
    @BindView(R.id.tv_bfcj_date)
    TextView mTvBfcjDate;
    @BindView(R.id.tv_visit)
    RiseNumberTextView mTvVisit;
    @BindView(R.id.tv_deal)
    TextView mTvDeal;
    @BindView(R.id.ll_bfcj)
    LinearLayout mLlBfcj;
    @BindView(R.id.tv_ygxs_select)
    TextView mTvYgxsSelect;
    @BindView(R.id.tv_ygxs_date)
    TextView mTvYgxsDate;
    @BindView(R.id.ll_ygxs)
    LinearLayout mLlYgxs;
    @BindView(R.id.tv_ygth_select)
    TextView mTvYgthSelect;
    @BindView(R.id.ll_ygth)
    LinearLayout mLlYgth;
    @BindView(R.id.tv_bfmx_select)
    TextView mTvBfmxSelect;
    @BindView(R.id.ll_bfmx)
    LinearLayout mLlBfmx;
    @BindView(R.id.tv_ygth_date)
    TextView mTvYgthDate;
    @BindView(R.id.tv_bfmx_date)
    TextView mTvBfmxDate;
    @BindView(R.id.btn_ygxs_left)
    TextView mBtnYgxsLeft;
    @BindView(R.id.btn_ygxs_right)
    TextView mBtnYgxsRight;
    @BindView(R.id.btn_ygth_left)
    TextView mBtnYgthLeft;
    @BindView(R.id.btn_ygth_center)
    TextView mBtnYgthCenter;
    @BindView(R.id.btn_ygth_right)
    TextView mBtnYgthRight;
    @BindView(R.id.btn_bfmx_left)
    TextView mBtnBfmxLeft;
    @BindView(R.id.btn_bfmx_right)
    TextView mBtnBfmxRight;
    @BindView(R.id.chart_ygxs)
    BarChart mChartYgxs;
    @BindView(R.id.chart_ygth)
    BarChart mChartYgth;
    @BindView(R.id.chart_bfmx)
    BarChart mChartBfmx;
    @BindView(R.id.tv_deal_amount)
    RiseNumberTextView mTvDealAmount;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.line_chart_ll)
    LinearLayout mLineChartLl;
    @BindView(R.id.ll_visit_rate)
    LinearLayout mLlVisitRate;
    @BindView(R.id.ll_map_trajectory)
    LinearLayout mLlMapTrajectory;
    @BindView(R.id.ll_visit_more)
    LinearLayout mLlVisitMore;
    @BindView(R.id.ll_bfmd_num)
    LinearLayout mLlBfmdNum;
    @BindView(R.id.ll_cjmd_num)
    LinearLayout mLlCjmdNum;
    @BindView(R.id.tv_phxs_select)
    TextView mTvPhxsSelect;
    @BindView(R.id.tv_phxs_date)
    TextView mTvPhxsDate;
    @BindView(R.id.ll_phxs)
    LinearLayout mLlPhxs;
    @BindView(R.id.btn_phxs_left)
    TextView mBtnPhxsLeft;
    @BindView(R.id.btn_phxs_right)
    TextView mBtnPhxsRight;
    @BindView(R.id.chart_phxss)
    BarChart mChartPhxss;
    @BindView(R.id.ll_chart_phcs)
    LinearLayout mLlChartPhcs;
    @BindView(R.id.ll_salesman_phxs)
    LinearLayout mLlSalesmanPhxs;

    private Typeface mTf;

    private TextView[] ygxsTv;
    private TextView[] ygthTv;
    private TextView[] bfmxTv;
    private TextView[] phxsTv;

    //排行查询--1 前五 2.前十 3前15
    private int ygxsRows = 5;
    private int ygthRows = 5;
    private int bfmxRows = 5;
    private int phxsRows = 5;

    /**
     * 日期第一种popup弹框条件类别---1.拜访成交;2.员工销售;3.员工退货;4.拜访明细;
     */
    private int datePopupOneType = 1;

    //1.今日;2.本周;3.本月;4.自定义;5上月
    private int dateBfcj = 1;
    private int dateYgxs = 1;
    private int dateYgth = 1;
    private int dateBfmx = 1;
    private int datePhxs = 1;

    //拜访成交
    private String startDateBfcj;
    private String endDateBfcj;
    //员工销售排行
    private String startDateYgxs;
    private String endDateYgxs;
    //员工退货
    private String startDateYgth;
    private String endDateYgth;
    //拜访明细排行
    private String startDateBfmx;
    private String endDateBfmx;
    //铺货业绩排行
    private String startDatePhxs;
    private String endDatePhxs;

    /**
     * 选择日期区间type--1.拜访成交;2.员工销售;3.员工退货;4.拜访明细;5.铺货业绩排行
     */
    private int datePrickType = 1;

    //员工销售动态加载布局控件
    private VaryViewHelper ygxsHelper = null;
    //员工退货动态加载布局控件
    private VaryViewHelper ygthHelper = null;
    //拜访明细动态加载布局控件
    private VaryViewHelper bfmxHelper = null;
    //铺货业绩动态加载布局控件
    private VaryViewHelper phxsHelper = null;

    private TimePickerUtil mTimePickerUtil;

    private StewardActivity main;
    //员工销售排行点击下标记录
    private int ygxsXIndex = 0;
    //拜访明细柱状图点击下标记录
    private int bfmxXIndex = 0;

    /**
     * 拜访明细接口
     */
    private List<SalesVisitNumBean> dataListBfmx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steward_salesman, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        main = (StewardActivity) getActivity();
        mTimePickerUtil = new TimePickerUtil();

        ygxsTv = new TextView[]{mBtnYgxsLeft, mBtnYgxsRight};
        ygthTv = new TextView[]{mBtnYgthLeft, mBtnYgthCenter, mBtnYgthRight};
        bfmxTv = new TextView[]{mBtnBfmxLeft, mBtnBfmxRight};
        phxsTv = new TextView[]{mBtnPhxsLeft, mBtnPhxsRight};

        typeBtnSytle(0, ygxsTv);
        typeBtnSytle(0, ygthTv);
        typeBtnSytle(0, bfmxTv);
        typeBtnSytle(0, phxsTv);

        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateBfcj = todayStr;
        endDateBfcj = todayStr;
        mTvBfcjDate.setText(startDateBfcj + "-" + endDateBfcj);

        startDateYgxs = todayStr;
        endDateYgxs = todayStr;
        mTvYgxsDate.setText(startDateYgxs + "-" + endDateYgxs);

        startDateYgth = todayStr;
        endDateYgth = todayStr;
        mTvYgthDate.setText(startDateYgth + "-" + endDateYgth);

        startDateBfmx = todayStr;
        endDateBfmx = todayStr;
        mTvBfmxDate.setText(startDateBfmx + "-" + endDateBfmx);

        startDatePhxs = todayStr;
        endDatePhxs = todayStr;
        mTvPhxsDate.setText(startDatePhxs + "-" + endDatePhxs);

        ygxsHelper = new VaryViewHelper(mChartYgxs);
        ygthHelper = new VaryViewHelper(mChartYgth);
        bfmxHelper = new VaryViewHelper(mChartBfmx);
        phxsHelper = new VaryViewHelper(mChartPhxss);

        ygxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
        ygthHelper.showEmptyView(UIUtils.getString(R.string.no_data));
        bfmxHelper.showEmptyView(UIUtils.getString(R.string.no_data));
        phxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        initChartBar(mChartYgxs);
        initChartBar(mChartYgth);
        initChartBar(mChartBfmx);
        initChartBar(mChartPhxss);

        mChartYgxs.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                ygxsXIndex = h.getXIndex();
            }

            @Override
            public void onNothingSelected() {
                Intent intent = new Intent(getActivity(), SalesmanRankingActivity.class);
                intent.putExtra("dateStartStr", startDateYgxs);
                intent.putExtra("dateEndStr", endDateYgxs);
                intent.putExtra("dateTypeStr", mTvYgxsSelect.getText().toString());
                startActivity(intent);
            }
        });
        mChartBfmx.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                LogUtils.e("onValueSelected", "onValueSelected:" + h.getXIndex());
                bfmxXIndex = h.getXIndex();
            }

            @Override
            public void onNothingSelected() {
                if (dataListBfmx != null) {
                    Intent intent = new Intent(getActivity(), SalesmanVisitingActivity.class);
                    intent.putExtra("dateStartStr", startDateBfmx);
                    intent.putExtra("dateEndStr", endDateBfmx);
                    intent.putExtra("dateTypeStr", mTvBfmxSelect.getText().toString());
                    intent.putExtra("salesid", dataListBfmx.get(bfmxXIndex).getClct_userid());
                    startActivity(intent);
                }
            }
        });


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
                        mTvBfcjSelect.setText("自定义");
                        dateBfcj = 4;
                        startDateBfcj = startDate;
                        endDateBfcj = endDate;
                        mTvBfcjDate.setText(startDate + "-" + endDate);
                        loadBfcj();
                        break;
                    case 2://员工销售
                        mTvYgxsSelect.setText("自定义");
                        dateYgxs = 4;
                        startDateYgxs = startDate;
                        endDateYgxs = endDate;
                        mTvYgxsDate.setText(startDate + "-" + endDate);
                        loadYgxs();
                        break;
                    case 3://员工退货
                        mTvYgthSelect.setText("自定义");
                        dateYgth = 4;
                        startDateYgth = startDate;
                        endDateYgth = endDate;
                        mTvYgthDate.setText(startDate + "-" + endDate);
                        loadYgth();
                        break;
                    case 4://拜访明细
                        mTvBfmxSelect.setText("自定义");
                        dateBfmx = 4;
                        startDateBfmx = startDate;
                        endDateBfmx = endDate;
                        mTvBfmxDate.setText(startDate + "-" + endDate);
                        loadBfmx();
                        break;
                    case 5://铺货销售业绩
                        mTvPhxsSelect.setText("自定义");
                        datePhxs = 4;
                        startDatePhxs = startDate;
                        endDatePhxs = endDate;
                        mTvPhxsDate.setText(startDate + "-" + endDate);
                        loadPhxs();
                        break;
                }

                mTimePickerUtil.calendarPopup.dismiss();
            }
        }, this);

        loadBfcj();
        loadYgxs();
        loadYgth();
        loadBfmx();
        loadPhxs();

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadBfcj();
                loadYgxs();
                loadYgth();
                loadBfmx();
                loadPhxs();
            }
        });
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

    private void setDataYG(List<SalesmanBean> list, BarChart mChart) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getOm_salesdocid_nameref().length() > 3) {
                xVals.add(list.get(i).getOm_salesdocid_nameref().substring(0, 3).toString() + "..");
            } else {
                xVals.add(list.get(i).getOm_salesdocid_nameref());
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

    private void setDataBfmx(List<SalesVisitNumBean> list, BarChart mChart) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClct_userid_nameref().length() > 3) {
                xVals.add(list.get(i).getClct_userid_nameref().substring(0, 3).toString() + "..");
            } else {
                xVals.add(list.get(i).getClct_userid_nameref());
            }
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < list.size(); i++) {
            yVals1.add(new BarEntry(list.get(i).getVisitNum(), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTf);
        data.setValueFormatter(new MyYAxisValueTwoFormatter());

        mChart.setData(data);
        mChart.invalidate();
    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     * @param color
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {
        /*initLineChart();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);

        barDataSet.setColor(color);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        mBarChart.setData(data);*/
    }


    @OnClick({R.id.tv_bfcj_select, R.id.ll_bfcj, R.id.tv_ygxs_select, R.id.ll_ygxs, R.id.tv_ygth_select, R.id.ll_ygth, R.id.tv_bfmx_select, R.id.ll_bfmx,
            R.id.btn_ygxs_left, R.id.btn_ygxs_right, R.id.btn_ygth_left,
            R.id.btn_ygth_center, R.id.btn_ygth_right, R.id.btn_bfmx_left, R.id.btn_bfmx_right,
            R.id.ll_salesman_ranking, R.id.ll_refund_ranking, R.id.ll_visit_detail, R.id.ll_visit_rate, R.id.ll_map_trajectory, R.id.ll_visit_more,
            R.id.ll_bfmd_num, R.id.ll_cjmd_num, R.id.tv_phxs_select, R.id.ll_phxs, R.id.btn_phxs_left, R.id.btn_phxs_right, R.id.ll_chart_phcs,
            R.id.ll_salesman_phxs})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_bfcj_select:
                datePopupOneType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvBfcjDate.getText().toString().split("-")[0],
                        mTvBfcjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_bfcj:
                datePrickType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvBfcjDate.getText().toString().split("-")[0],
                        mTvBfcjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_ygxs_select:
                datePopupOneType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYgxsDate.getText().toString().split("-")[0],
                        mTvYgxsDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_ygxs:
                datePrickType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYgxsDate.getText().toString().split("-")[0],
                        mTvYgxsDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_ygth_select:
                datePopupOneType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYgthDate.getText().toString().split("-")[0],
                        mTvYgthDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_ygth:
                datePrickType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYgthDate.getText().toString().split("-")[0],
                        mTvYgthDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_bfmx_select:
                datePopupOneType = 4;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvBfmxDate.getText().toString().split("-")[0],
                        mTvBfmxDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_bfmx:
                datePrickType = 4;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvBfmxDate.getText().toString().split("-")[0],
                        mTvBfmxDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_phxs_select://业务员销售排行
                datePopupOneType = 5;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvPhxsDate.getText().toString().split("-")[0],
                        mTvPhxsDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_phxs:
                datePrickType = 5;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvPhxsDate.getText().toString().split("-")[0],
                        mTvPhxsDate.getText().toString().split("-")[1], true);
                break;
            case R.id.btn_ygxs_left:
                ygxsRows = 5;
                typeBtnSytle(0, ygxsTv);
                loadYgxs();
                break;
            case R.id.btn_ygxs_right:
                ygxsRows = 10;
                typeBtnSytleTwo(1, ygxsTv);
                loadYgxs();
                break;
            case R.id.btn_ygth_left:
                ygthRows = 5;
                typeBtnSytle(0, ygthTv);
                loadYgth();
                break;
            case R.id.btn_ygth_center:
                ygthRows = 10;
                typeBtnSytle(1, ygthTv);
                loadYgth();
                break;
            case R.id.btn_ygth_right:
                ygthRows = 15;
                typeBtnSytle(2, ygthTv);
                loadYgth();
                break;
            case R.id.btn_bfmx_left:
                bfmxRows = 5;
                typeBtnSytle(0, bfmxTv);
                loadBfmx();
                break;
            case R.id.btn_bfmx_right:
                bfmxRows = 10;
                typeBtnSytleTwo(1, bfmxTv);
                loadBfmx();
                break;
            case R.id.btn_phxs_left:
                phxsRows = 5;
                typeBtnSytle(0, phxsTv);
                loadPhxs();
                break;
            case R.id.btn_phxs_right:
                phxsRows = 10;
                typeBtnSytle(1, phxsTv);
                loadPhxs();
                break;
            case R.id.ll_salesman_ranking://销售排行
                intent = new Intent(getActivity(), SalesmanRankingActivity.class);
                intent.putExtra("dateStartStr", startDateYgxs);
                intent.putExtra("dateEndStr", endDateYgxs);
                intent.putExtra("dateTypeStr", mTvYgxsSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_refund_ranking:
                startActivity(SalesmanRefundActivity.class);
                break;
            case R.id.ll_visit_detail://拜访明细
                intent = new Intent(getActivity(), SalesmanVisitingActivity.class);
                intent.putExtra("dateStartStr", startDateBfmx);
                intent.putExtra("dateEndStr", endDateBfmx);
                intent.putExtra("dateTypeStr", mTvBfmxSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_visit_rate://访店达成率
                startActivity(VisitRateActivity.class);
                break;
            case R.id.ll_map_trajectory://车辆位置
                startActivity(CartLocationActivity.class);
                break;
            case R.id.ll_visit_more://查看更多
                startActivity(VisitMoreActivity.class);
                break;
            case R.id.ll_bfmd_num://拜访门店数
                intent = new Intent(getActivity(), SalesmanVisitingActivity.class);
                intent.putExtra("dateStartStr", startDateBfcj);
                intent.putExtra("dateEndStr", endDateBfcj);
                intent.putExtra("dateTypeStr", mTvBfcjSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_cjmd_num://成交门店数
                intent = new Intent(getActivity(), VisitRateActivity.class);
                intent.putExtra("dateStartStr", startDateBfcj);
                intent.putExtra("dateEndStr", endDateBfcj);
                intent.putExtra("dateTypeStr", mTvBfcjSelect.getText().toString());
                startActivity(intent);
                break;

            case R.id.ll_chart_phcs:
                break;
            case R.id.ll_salesman_phxs:
                intent = new Intent(getActivity(), SalesmanPhRakingActivity.class);
                intent.putExtra("dateStartStr", startDatePhxs);
                intent.putExtra("dateEndStr", endDatePhxs);
                intent.putExtra("dateTypeStr", mTvPhxsSelect.getText().toString());
                startActivity(intent);
                break;
        }
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
                textViewIds[index].setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_centre_btn));
                break;
            case 2:
                textViewIds[index].setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_right_btn));
                break;
        }
        textViewIds[index].setTextColor(UIUtils.getColor(R.color.white));
        textViewIds[index].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    /**
     * 设置 1:左侧按钮 2:右侧按钮
     *
     * @param index
     */
    private void typeBtnSytleTwo(int index, TextView[] textViewIds) {
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

    private void loadBfcj() {
        mTvVisit.setText("0");
        mTvDealAmount.setText("0.00");
        mTvDeal.setText("/0" + "家");
        AsyncHttpUtil<VisitDataBean> httpUtil = new AsyncHttpUtil<>(getActivity(), VisitDataBean.class, new IUpdateUI<VisitDataBean>() {
            @Override
            public void updata(VisitDataBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvVisit.setText(jsonBean.getDayVisitNum() + "");
                    // 设置数据
                    mTvVisit.withNumber(jsonBean.getDayVisitNum());
                    // 设置动画播放时间
                    mTvVisit.setDuration(500);
                    // 开始播放动画
                    mTvVisit.start();

                    mTvDealAmount.setText(jsonBean.getAmount() + "");
                    // 设置数据
                    mTvDealAmount.withNumber(jsonBean.getAmount());
                    // 设置动画播放时间
                    mTvDealAmount.setDuration(1000);
                    // 开始播放动画
                    mTvDealAmount.start();

                    mTvDeal.setText("/" + jsonBean.getDayDealNum() + "家");
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
        String startDate = startDateBfcj.replace(".", "-");
        String endDate = endDateBfcj.replace(".", "-");
        httpUtil.post(M_Url.getVisitData, G_RequestParams.getVisitData(startDate, endDate), true);
    }

    /**
     * 员工销售排行
     */
    private void loadYgxs() {
        AsyncHttpUtil<SalesmanBean> httpUtil = new AsyncHttpUtil<>(getActivity(), SalesmanBean.class, new IUpdateUI<SalesmanBean>() {
            @Override
            public void updata(SalesmanBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {

                    if (jsonBean.getDataList() == null || jsonBean.getDataList().size() == 0) {
                        ygxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    } else {
                        ygxsHelper.showDataView();
                        setDataYG(jsonBean.getDataList(), mChartYgxs);
                    }
                } else {
                    ygxsHelper.showEmptyView(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                ygxsHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {

            }
        });
        String startDate = startDateYgxs.replace(".", "-");
        String endDate = endDateYgxs.replace(".", "-");
        httpUtil.post(M_Url.getSalesList, G_RequestParams.getSalesList(startDate, endDate, String.valueOf(ygxsRows), "2"), true);
    }

    private void loadYgth() {
        AsyncHttpUtil<SalesmanBean> httpUtil = new AsyncHttpUtil<>(getActivity(), SalesmanBean.class, new IUpdateUI<SalesmanBean>() {
            @Override
            public void updata(SalesmanBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() == null || jsonBean.getDataList().size() == 0) {
                        ygthHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    } else {
                        ygthHelper.showDataView();
                        setDataYG(jsonBean.getDataList(), mChartYgth);
                    }
                } else {
                    ygthHelper.showEmptyView(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                ygthHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDateYgth.replace(".", "-");
        String endDate = endDateYgth.replace(".", "-");
        httpUtil.post(M_Url.getSalesList, G_RequestParams.getSalesList(startDate, endDate, String.valueOf(ygthRows), "3"), true);
    }

    /**
     * 加载拜访明细
     */
    private void loadBfmx() {
        AsyncHttpUtil<SalesVisitNumBean> httpUtil = new AsyncHttpUtil<>(getActivity(), SalesVisitNumBean.class, new IUpdateUI<SalesVisitNumBean>() {
            @Override
            public void updata(SalesVisitNumBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        bfmxHelper.showDataView();
                        dataListBfmx = jsonStr.getDataList();
                        setDataBfmx(jsonStr.getDataList(), mChartBfmx);
                    } else {
                        bfmxHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    bfmxHelper.showEmptyView(jsonStr.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                bfmxHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDateBfmx.replace(".", "-");
        String endDate = endDateBfmx.replace(".", "-");
        httpUtil.post(M_Url.getSalesVisitNum, G_RequestParams.getSalesVisitNum(startDate, endDate, String.valueOf(bfmxRows), "1"), true);
    }

    /**
     * 铺货销售
     */
    private void loadPhxs() {
        AsyncHttpUtil<SalesmanBean> httpUtil = new AsyncHttpUtil<>(getActivity(), SalesmanBean.class, new IUpdateUI<SalesmanBean>() {
            @Override
            public void updata(SalesmanBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        phxsHelper.showDataView();
                        setDataYG(jsonStr.getDataList(), mChartPhxss);
                    } else {
                        phxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    phxsHelper.showEmptyView(jsonStr.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                phxsHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDatePhxs.replace(".", "-");
        String endDate = endDatePhxs.replace(".", "-");
        httpUtil.post(M_Url.ywySaleRank, G_RequestParams.ywySaleRank(startDate, endDate, String.valueOf(phxsRows), "1"), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        switch (datePopupOneType) {
            case 1:
                dateBfcj = position;
                mTvBfcjSelect.setText(dateName);
                startDateBfcj = startDate;
                endDateBfcj = endDate;
                mTvBfcjDate.setText(startDateBfcj + "-" + endDateBfcj);
                loadBfcj();
                break;
            case 2://员工销售
                dateYgxs = position;
                mTvYgxsSelect.setText(dateName);

                startDateYgxs = startDate;
                endDateYgxs = endDate;

                mTvYgxsDate.setText(startDateYgxs + "-" + endDateYgxs);
                loadYgxs();
                break;
            case 3://员工退货
                dateYgth = position;
                mTvYgthSelect.setText(dateName);

                startDateYgth = startDate;
                endDateYgth = endDate;
                mTvYgthDate.setText(startDateYgth + "-" + endDateYgth);
                loadYgth();
                break;
            case 4://拜访明细
                dateBfmx = position;
                mTvBfmxSelect.setText(dateName);
                startDateBfmx = startDate;
                endDateBfmx = endDate;
                mTvBfmxDate.setText(startDateBfmx + "-" + endDateBfmx);
                loadBfmx();
                break;
            case 5://铺货销售
                datePhxs = position;
                mTvPhxsSelect.setText(dateName);
                startDatePhxs = startDate;
                endDatePhxs = endDate;
                mTvPhxsDate.setText(startDatePhxs + "-" + endDatePhxs);
                loadPhxs();
                break;
        }
    }

}
