package com.xdjd.steward.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.SetTimeActivity;
import com.xdjd.distribution.adapter.OrderSearchAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.steward.bean.HandinListBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/7/10.
 */

public class IncomeHandInActivity extends BaseActivity implements OnChartValueSelectedListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.chart)
    HorizontalBarChart mChart;
    @BindView(R.id.front_ll)
    LinearLayout mFrontLl;
    @BindView(R.id.tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.tv_end_date)
    TextView mTvEndDate;
    @BindView(R.id.ll_date_section)
    LinearLayout mLlDateSection;
    @BindView(R.id.backwards_ll)
    LinearLayout mBackwardsLl;
    @BindView(R.id.tv_table_title)
    TextView mTvTableTitle;
    @BindView(R.id.scroll)
    PullToRefreshScrollView mScroll;

    private Typeface tf;

    private DisplayMetrics metric;

    private DatePickerDialog startDateDialog;
    private DatePickerDialog endDateDialog;

    private OrderSearchAdapter adapter;
    private List<OrderBean> list;

    private int dateStartNum = 0;
    private int dateEndNum = 0;

    private String dateStartStr;
    private String dateEndStr;

    private final int DATA_REQUEST = 100;

    private Date date = new Date();

    //是否是开始或结束时间
    private boolean isStart;

    private VaryViewHelper mVaryViewHelper = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_income_hand_in;
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
        mTitleBar.setTitle("收入上交款");
        //获取屏幕总宽度
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        dateStartStr = StringUtils.getDate2();
        dateEndStr = StringUtils.getDate2();

        mTvStartDate.setText(dateStartStr);
        mTvEndDate.setText(dateEndStr);

        Calendar calendar = Calendar.getInstance();
        startDateDialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        endDateDialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mScroll.setMode(PullToRefreshBase.Mode.DISABLED);
        mVaryViewHelper = new VaryViewHelper(mScroll);

        initChart();
        loadData();
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

        //        setData(12, 50);
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

        Log.e("bounds", bounds.toString());
        Log.e("position", position.toString());
    }

    @Override
    public void onNothingSelected() {
    }

    private void loadData() {
        mVaryViewHelper.showLoadingView();
        AsyncHttpUtil<HandinListBean> httpUtil = new AsyncHttpUtil<>(this, HandinListBean.class, new IUpdateUI<HandinListBean>() {
            @Override
            public void updata(HandinListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() == null || jsonBean.getDataList().size() == 0) {
                        mVaryViewHelper.showEmptyView("暂无收入上交款数据!");
                    } else {
                        setData(jsonBean.getDataList());
                        mVaryViewHelper.showDataView();
                    }
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

            }
        });
        httpUtil.post(M_Url.getHandinList, G_RequestParams.getHandinList(dateStartStr, dateEndStr), true);
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData();
        }
    }

    private void setData(List<HandinListBean> list) {

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
            xVals.add(list.get(i).getGc_salesid_nameref());
            yVals1.add(new BarEntry(list.get(i).getAmount(), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        //        data.setGroupSpace(2);
        //                data.setDrawValues();
        data.setValueTypeface(tf);

        mChart.setData(data);

        mChart.animateY(2500);
    }

    @OnClick({R.id.front_ll, R.id.backwards_ll, R.id.ll_date_section})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.front_ll:
                dateStartNum--;
                dateStartStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateStartNum);
                mTvStartDate.setText(dateStartStr);

                dateEndNum--;
                dateEndStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateEndNum);
                mTvEndDate.setText(dateEndStr);

                mChart.clear();
                loadData();
                break;
            case R.id.backwards_ll:
                dateStartNum++;
                dateStartStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateStartNum);
                mTvStartDate.setText(dateStartStr);

                dateEndNum++;
                dateEndStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateEndNum);
                mTvEndDate.setText(dateEndStr);

                mChart.clear();
                loadData();
                break;
            case R.id.ll_date_section://日期区间选择
                Intent intent = new Intent(this, SetTimeActivity.class);
                intent.putExtra("startData", mTvStartDate.getText());
                intent.putExtra("endData", mTvEndDate.getText());
                startActivityForResult(intent, DATA_REQUEST);
                break;
        }
    }

    /**
     * 日选择回调接口
     */
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
            String date = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            date = DateUtils.getCurTimeStr(date);
            if (isStart) {
                int flag = DateUtils.calDateDifferent(mTvEndDate.getText().toString(), date);
                if (flag == 1) {
                    showToast("不能高于结束时间");
                    return;
                }

                mTvStartDate.setText(date);
                Calendar cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvStartDate.getText().toString()));
                startDateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
            } else {
                if (DateUtils.isMoreThanToday(date)) {
                    showToast("结束时间不能超过今天");
                    return;
                }

                if (DateUtils.calDateDifferent(mTvStartDate.getText().toString(), date) == -1) {
                    showToast("不能低于开始时间");
                    return;
                }

                mTvEndDate.setText(date);
                Calendar cEnd = Calendar.getInstance();
                cEnd.setTime(StringUtils.toDateFormater2(mTvEndDate.getText().toString()));
                endDateDialog.updateDate(cEnd.get(Calendar.YEAR),
                        cEnd.get(Calendar.MONTH), cEnd.get(Calendar.DAY_OF_MONTH));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                dateStartStr = data.getStringExtra("startData");
                dateEndStr = data.getStringExtra("endData");

                mTvStartDate.setText(dateStartStr);
                mTvEndDate.setText(dateEndStr);

                dateStartNum = 0;
                dateEndNum = 0;

                mChart.clear();
                loadData();
                break;
        }
    }
}
