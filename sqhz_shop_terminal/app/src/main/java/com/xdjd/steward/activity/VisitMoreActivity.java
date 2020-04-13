package com.xdjd.steward.activity;

import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.MyYAxisValueOneFormatter;
import com.github.mikephil.charting.formatter.MyYAxisValueTwoFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.bean.SalesVisitNumBean;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/8/22.
 */

public class VisitMoreActivity extends BaseActivity implements OnChartValueSelectedListener,CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.tv_table_title)
    TextView mTvTableTitle;
    @BindView(R.id.chart)
    HorizontalBarChart mChart;
    @BindView(R.id.scroll)
    PullToRefreshScrollView mScroll;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_hint)
    TextView mTvHint;

    private Typeface tf;

    private DisplayMetrics metric;

    private String dateStartStr;
    private String dateEndStr;

    private Date date = new Date();

    //是否是开始或结束时间
    private boolean isStart;

    private VaryViewHelper mVaryViewHelper = null;

    private int page = 1;
    private int mFlag = 0;
    private List<SalesVisitNumBean> list = new ArrayList<>();

    private TimePickerUtil mTimePickerUtil;

    private int dateType = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_visit_more;
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
        mTitleBar.setTitle("拜访排行");
        mTitleBar.setRightText("详情");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SalesmanVisitingActivity.class);
            }
        });

        mScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mScroll);
        mScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                mChart.clear();
                loadData(PublicFinal.TWO, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData(PublicFinal.TWO, true);
            }
        });

        dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        mTimePickerUtil = new TimePickerUtil();

        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                if (DateUtils.isMoreThanToday(endDate)) {
                    showToast("结束时间不能超过今天");
                    return;
                }

                mTimePickerUtil.calendarPopup.dismiss();

                dateStartStr = startDate.replace("-", ".");
                dateEndStr = endDate.replace("-", ".");

                mTvDate.setText(dateStartStr + "-" + dateEndStr);
                mTvDateTypeSelect.setText("自定义");
                dateType = 4;

                list.clear();
                mChart.clear();
                page = 1;
                mFlag = 1;
                loadData(PublicFinal.FIRST, false);
            }
        },this);

        mVaryViewHelper = new VaryViewHelper(mScroll);

        initChart();
        loadData(PublicFinal.FIRST, false);
    }

    private void initChart() {
        mChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);
        //        mChart.setDragEnabled(false);// 是否可以拖拽
        mChart.setScaleEnabled(false);// 是否可以缩放
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setBackgroundColor(getResources().getColor(R.color.transparent));

        //自定义代码
        mChart.getAxisLeft().setStartAtZero(true);
        mChart.getAxisRight().setStartAtZero(true);
        mChart.setAutoScaleMinMaxEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be drawn
        // mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        // mChart.setDrawXLabels(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);
        xl.setTextSize(12f);

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setGridLineWidth(0.3f);
        yl.setTextSize(9f);
        //        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);//去掉表外面显示的提示
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(dataSetIndex)
                .getAxisDependency());
    }

    @Override
    public void onNothingSelected() {
    }

    /**
     * 加载拜访明细
     */
    private void loadData(int isFirst, boolean isDialog) {
        if (PublicFinal.FIRST == isFirst) {
            mVaryViewHelper.showLoadingView();
        }

        AsyncHttpUtil<SalesVisitNumBean> httpUtil = new AsyncHttpUtil<>(this, SalesVisitNumBean.class, new IUpdateUI<SalesVisitNumBean>() {
            @Override
            public void updata(SalesVisitNumBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mVaryViewHelper.showDataView();
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        list.addAll(jsonBean.getDataList());
                        mTvHint.setVisibility(View.VISIBLE);
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了!");
                        } else {
                            mTvHint.setVisibility(View.GONE);
                            mVaryViewHelper.showEmptyView("暂无排行数据!");
                        }
                    }
                    setData();
                } else {
                    mVaryViewHelper.showErrorView(new OnErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(new OnErrorListener());
            }

            @Override
            public void finish() {
                mScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getSalesVisitNum, G_RequestParams.
                getSalesVisitNum(dateStartStr.replace(".","-"), dateEndStr.replace(".","-"), String.valueOf(10), String.valueOf(page)), isDialog);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        list.clear();
        mChart.clear();
        page = 1;
        mFlag = 1;
        loadData(PublicFinal.FIRST, false);
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST, false);
        }
    }

    private void setData() {
        if (list.size() == 0) {
            return;
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        //动态计算linearlayout布局的宽度
        if (list.size() > 0) {
            int height = UIUtils.dp2px(20) * list.size();
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChart.getLayoutParams();
            layoutParams.height = height + UIUtils.dp2px(40);
            mChart.setLayoutParams(layoutParams);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClct_userid_nameref().length() > 5) {
                xVals.add(list.get(i).getClct_userid_nameref().substring(0, 5).toString() + "..");
            } else {
                xVals.add(list.get(i).getClct_userid_nameref());
            }
            yVals1.add(new BarEntry(list.get(i).getVisitNum(), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        //        data.setGroupSpace(2);
        //                data.setDrawValues();
        data.setValueTypeface(tf);
        data.setValueFormatter(new MyYAxisValueTwoFormatter());

        mChart.setData(data);
        mChart.animateY(2500);
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select://选择日期范围
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr, dateEndStr,true);
                break;
            case R.id.ll_date_calendar://日历选择时间
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr, dateEndStr,true);
                break;
        }
    }


}
