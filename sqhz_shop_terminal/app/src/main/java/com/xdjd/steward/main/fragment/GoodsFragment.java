package com.xdjd.steward.main.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
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
import com.xdjd.steward.activity.GoodsRankingActivity;
import com.xdjd.steward.activity.StockNumActivity;
import com.xdjd.steward.bean.GoodsReportBean;
import com.xdjd.steward.bean.GoodsStockBean;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/6/19.
 */

public class GoodsFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener{

    @BindView(R.id.ll_goods_sales_ranking)
    LinearLayout mLlGoodsSalesRanking;
    @BindView(R.id.ll_goods_refund_ranking)
    LinearLayout mLlGoodsRefundRanking;
    @BindView(R.id.ll_stock_num)
    LinearLayout mLlStockNum;
    @BindView(R.id.ll_stock_balance)
    LinearLayout mLlStockBalance;
    @BindView(R.id.tv_spxs_select)
    TextView mTvSpxsSelect;
    @BindView(R.id.tv_spxs_date)
    TextView mTvSpxsDate;
    @BindView(R.id.ll_spxs)
    LinearLayout mLlSpxs;
    @BindView(R.id.btn_spxs_left)
    TextView mBtnSpxsLeft;
    @BindView(R.id.btn_spxs_right)
    TextView mBtnSpxsRight;
    @BindView(R.id.chart_spxs)
    BarChart mChartSpxs;
    @BindView(R.id.tv_spth_select)
    TextView mTvSpthSelect;
    @BindView(R.id.tv_spth_date)
    TextView mTvSpthDate;
    @BindView(R.id.ll_spth)
    LinearLayout mLlSpth;
    @BindView(R.id.btn_spth_left)
    TextView mBtnSpthLeft;
    @BindView(R.id.btn_spth_right)
    TextView mBtnSpthRight;
    @BindView(R.id.chart_spth)
    BarChart mChartSpth;
    @BindView(R.id.tv_kcsl_select)
    TextView mTvKcslSelect;
    @BindView(R.id.tv_kcsl_date)
    TextView mTvKcslDate;
    @BindView(R.id.ll_kcsl)
    LinearLayout mLlKcsl;
    @BindView(R.id.btn_kcsl_left)
    TextView mBtnKcslLeft;
    @BindView(R.id.btn_kcsl_right)
    TextView mBtnKcslRight;
    @BindView(R.id.chart_kcsl)
    BarChart mChartKcsl;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private Intent intent;
    private Typeface mTf;

    private TextView[] spxsTv;
    private TextView[] spthTv;
    private TextView[] kcslTv;

    //排行查询--1 前五 2.前十
    private int spxsRows = 5;
    private int spthRows = 5;
    private int kcslRows = 5;

    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;

    /**
     * 日期第一种popup弹框条件类别---1.商品销售;2.商品退货;3.库存数量;
     */
    private int datePopupOneType = 1;

    //1.今日;2.本周;3.本月;4.自定义;5上月
    private int dateSpxs = 1;
    private int dateSpth = 1;
    private int dateKcsl = 1;

    //商品销售
    private String startDateSpxs;
    private String endDateSpxs;
    //商品退货
    private String startDateSpth;
    private String endDateSpth;
    //库存数量
    private String startDateKcsl;
    private String endDateKcsl;

    /**
     * 选择日期区间type--1.商品销售;2.商品退货;3.库存数量;
     */
    private int datePrickType = 1;

    //商品销售动态加载布局控件
    private VaryViewHelper spxsHelper = null;
    //商品退货动态加载布局控件
    private VaryViewHelper spthHelper = null;
    //库存数量动态加载布局控件
    private VaryViewHelper kcslHelper = null;

    private TimePickerUtil mTimePickerUtil;

    StewardActivity main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steward_goods, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        main = (StewardActivity) getActivity();
        mTimePickerUtil = new TimePickerUtil();

        spxsTv = new TextView[]{mBtnSpxsLeft, mBtnSpxsRight};
        spthTv = new TextView[]{mBtnSpthLeft, mBtnSpthRight};
        kcslTv = new TextView[]{mBtnKcslLeft, mBtnKcslRight};

        typeBtnSytle(0, spxsTv);
        typeBtnSytle(0, spthTv);
        typeBtnSytle(0, kcslTv);

        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateSpxs = todayStr;
        endDateSpxs = todayStr;
        mTvSpxsDate.setText(startDateSpxs + "-" + endDateSpxs);

        startDateSpth = todayStr;
        endDateSpth = todayStr;
        mTvSpthDate.setText(startDateSpth + "-" + endDateSpth);

        startDateKcsl = todayStr;
        endDateKcsl = todayStr;
        mTvKcslDate.setText(startDateKcsl + "-" + endDateKcsl);

        spxsHelper = new VaryViewHelper(mChartSpxs);
        spthHelper = new VaryViewHelper(mChartSpth);
        kcslHelper = new VaryViewHelper(mChartKcsl);

        spxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
        spthHelper.showEmptyView(UIUtils.getString(R.string.no_data));
        kcslHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        initChartBar(mChartSpxs);
        initChartBar(mChartSpth);
        initChartBar(mChartKcsl);

        mChartSpxs.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            }

            @Override
            public void onNothingSelected() {
                intent = new Intent(getActivity(), GoodsRankingActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("dateStartStr",startDateSpxs);
                intent.putExtra("dateEndStr",endDateSpxs);
                intent.putExtra("dateTypeStr",mTvSpxsSelect.getText().toString());
                startActivity(intent);
            }
        });
        mChartSpth.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
                intent = new Intent(getActivity(), GoodsRankingActivity.class);
                intent.putExtra("type", "3");
                intent.putExtra("dateStartStr",startDateSpth);
                intent.putExtra("dateEndStr",endDateSpth);
                intent.putExtra("dateTypeStr",mTvSpthSelect.getText().toString());
                startActivity(intent);
            }
        });
        mChartKcsl.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
                startActivity(StockNumActivity.class);
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
                    case 1://商品销售
                        mTvSpxsSelect.setText("自定义");
                        dateSpxs = 4;
                        startDateSpxs = startDate;
                        endDateSpxs = endDate;
                        mTvSpxsDate.setText(startDate + "-" + endDate);
                        loadSpxs();
                        break;
                    case 2://商品退货
                        mTvSpthSelect.setText("自定义");
                        dateSpth = 4;
                        startDateSpth = startDate;
                        endDateSpth = endDate;
                        mTvSpthDate.setText(startDate + "-" + endDate);
                        loadSpth();
                        break;
                    case 3://库存数量
                        mTvKcslSelect.setText("自定义");
                        dateKcsl = 4;
                        startDateKcsl = startDate;
                        endDateKcsl = endDate;
                        mTvKcslDate.setText(startDate + "-" + endDate);
                        loadKcsl();
                        break;
                }

                mTimePickerUtil.calendarPopup.dismiss();
            }
        },this);

        loadSpxs();
        loadSpth();
        loadKcsl();

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadSpxs();
                loadSpth();
                loadKcsl();
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

    /**
     * 设置销售/退货数据
     *
     * @param list
     * @param mChart
     */
    private void setDataSP(List<GoodsReportBean> list, BarChart mChart) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGoodsName().length() > 3) {
                xVals.add(list.get(i).getGoodsName().substring(0, 3).toString() + "..");
            } else {
                xVals.add(list.get(i).getGoodsName());
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

    private void setDataKcsl(List<GoodsStockBean> list, BarChart mChart) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGg_title().length() > 3) {
                xVals.add(list.get(i).getGg_title().substring(0, 3).toString() + "..");
            } else {
                xVals.add(list.get(i).getGg_title());
            }
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < list.size(); i++) {
            yVals1.add(new BarEntry(list.get(i).getGgp_stock(), i));
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


    /*@OnClick({, R.id.ll_stock_balance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_stock_balance://库存余额
                startActivity(StockBalanceActivity.class);
                break;
        }
    }*/

    @OnClick({R.id.tv_spxs_select, R.id.ll_spxs, R.id.btn_spxs_left, R.id.btn_spxs_right, R.id.tv_spth_select,
            R.id.ll_spth, R.id.btn_spth_left, R.id.btn_spth_right, R.id.tv_kcsl_select, R.id.ll_kcsl, R.id.btn_kcsl_left, R.id.btn_kcsl_right,
            R.id.ll_goods_sales_ranking, R.id.ll_goods_refund_ranking, R.id.ll_stock_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_spxs_select:
                datePopupOneType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(),mTvSpxsDate.getText().toString().split("-")[0],
                        mTvSpxsDate.getText().toString().split("-")[1],true);
                break;
            case R.id.ll_spxs:
                datePrickType = 1;
                mTimePickerUtil.showTimePicker(main.getMainId(),mTvSpxsDate.getText().toString().split("-")[0],
                        mTvSpxsDate.getText().toString().split("-")[1],true);
                break;
            case R.id.btn_spxs_left:
                spxsRows = 5;
                typeBtnSytle(0, spxsTv);
                loadSpxs();
                break;
            case R.id.btn_spxs_right:
                spxsRows = 10;
                typeBtnSytle(1, spxsTv);
                loadSpxs();
                break;
            case R.id.tv_spth_select:
                datePopupOneType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(),mTvSpthDate.getText().toString().split("-")[0],
                        mTvSpthDate.getText().toString().split("-")[1],true);
                break;
            case R.id.ll_spth:
                datePrickType = 2;
                mTimePickerUtil.showTimePicker(main.getMainId(),mTvSpthDate.getText().toString().split("-")[0],
                        mTvSpthDate.getText().toString().split("-")[1],true);
                break;
            case R.id.btn_spth_left:
                spthRows = 5;
                typeBtnSytle(0, spthTv);
                loadSpth();
                break;
            case R.id.btn_spth_right:
                spthRows = 10;
                typeBtnSytle(1, spthTv);
                loadSpth();
                break;
            case R.id.tv_kcsl_select:
                datePopupOneType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(),mTvKcslDate.getText().toString().split("-")[0],
                        mTvKcslDate.getText().toString().split("-")[1],true);
                break;
            case R.id.ll_kcsl:
                datePrickType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(),mTvKcslDate.getText().toString().split("-")[0],
                        mTvKcslDate.getText().toString().split("-")[1],true);
                break;
            case R.id.btn_kcsl_left:
                kcslRows = 5;
                typeBtnSytle(0, kcslTv);
                loadKcsl();
                break;
            case R.id.btn_kcsl_right:
                kcslRows = 10;
                typeBtnSytle(1, kcslTv);
                loadKcsl();
                break;
            case R.id.ll_goods_sales_ranking://销售排行
                intent = new Intent(getActivity(), GoodsRankingActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("dateStartStr",startDateSpxs);
                intent.putExtra("dateEndStr",endDateSpxs);
                intent.putExtra("dateTypeStr",mTvSpxsSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_goods_refund_ranking://退货排行
                intent = new Intent(getActivity(), GoodsRankingActivity.class);
                intent.putExtra("type", "3");
                intent.putExtra("dateStartStr",startDateSpth);
                intent.putExtra("dateEndStr",endDateSpth);
                intent.putExtra("dateTypeStr",mTvSpthSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_stock_num://库存数量
                startActivity(StockNumActivity.class);
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
                textViewIds[index].setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_right_btn));
                break;
        }
        textViewIds[index].setTextColor(UIUtils.getColor(R.color.white));
        textViewIds[index].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void loadSpxs() {
        AsyncHttpUtil<GoodsReportBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsReportBean.class, new IUpdateUI<GoodsReportBean>() {
            @Override
            public void updata(GoodsReportBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        spxsHelper.showDataView();
                        setDataSP(jsonBean.getDataList(), mChartSpxs);
                    } else {
                        spxsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    spxsHelper.showEmptyView(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                spxsHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });

        String startDate = startDateSpxs.replace(".", "-");
        String endDate = endDateSpxs.replace(".", "-");

        httpUtil.post(M_Url.getGoodsSaleList, G_RequestParams.getGoodsSaleList(String.valueOf(1),
                startDate, endDate, "2", String.valueOf(spxsRows)), true);
    }

    private void loadSpth() {
        AsyncHttpUtil<GoodsReportBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsReportBean.class, new IUpdateUI<GoodsReportBean>() {
            @Override
            public void updata(GoodsReportBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        spthHelper.showDataView();
                        setDataSP(jsonBean.getDataList(), mChartSpth);
                    } else {
                        spthHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    spthHelper.showEmptyView(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                spthHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });

        String startDate = startDateSpth.replace(".", "-");
        String endDate = endDateSpth.replace(".", "-");
        httpUtil.post(M_Url.getGoodsSaleList, G_RequestParams.getGoodsSaleList(String.valueOf(1),
                startDate, endDate, "3", String.valueOf(spthRows)), true);
    }

    private void loadKcsl() {
        AsyncHttpUtil<GoodsStockBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsStockBean.class, new IUpdateUI<GoodsStockBean>() {
            @Override
            public void updata(GoodsStockBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        kcslHelper.showDataView();
                        setDataKcsl(jsonBean.getDataList(), mChartKcsl);
                    } else {
                        kcslHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    kcslHelper.showEmptyView(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                kcslHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getGoodsStock, G_RequestParams.getGoodsStock(UserInfoUtils.getId(getActivity()),
                "", "", String.valueOf(1), String.valueOf(kcslRows)), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
        //1.今日;2.本周;3.本月;5.上月
        switch (datePopupOneType) {
            case 1:
                dateSpxs = position;
                mTvSpxsSelect.setText(dateName);
                startDateSpxs = startDate;
                endDateSpxs = endDate;
                mTvSpxsDate.setText(startDateSpxs + "-" + endDateSpxs);
                loadSpxs();
                break;
            case 2://商品退货
                dateSpth = position;
                mTvSpthSelect.setText(dateName);
                startDateSpth = startDate;
                endDateSpth = endDate;
                mTvSpthDate.setText(startDateSpth + "-" + endDateSpth);
                loadSpth();
                break;
            case 3://员工退货
                dateKcsl = position;
                mTvKcslSelect.setText(dateName);

                startDateKcsl = startDate;
                endDateKcsl = endDate;
                mTvKcslDate.setText(startDateKcsl + "-" + endDateKcsl);
                loadKcsl();
                break;
        }
    }
}
