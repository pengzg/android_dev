package com.xdjd.winningrecord.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.MyYAxisValueTwoFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.risenumber.RiseNumberTextView;
import com.xdjd.winningrecord.adapter.ActionStatisticsAdapter;
import com.xdjd.winningrecord.bean.ActivityPeriodStatsBean;
import com.xdjd.winningrecord.bean.ActivityTodayStatsBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/20
 *     desc   : 活动详情数据
 *     version: 1.0
 * </pre>
 */

public class ActionDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_statistics)
    NoScrollListView mLvStatistics;
    @BindView(R.id.line_chart_ll)
    LinearLayout mLineChartLl;
    @BindView(R.id.tv_cj_num)
    RiseNumberTextView mTvCjNum;
    @BindView(R.id.tv_zj_num)
    RiseNumberTextView mTvZjNum;
    @BindView(R.id.tv_dj_num)
    RiseNumberTextView mTvDjNum;
    @BindView(R.id.chart_line_qsfx)
    LineChart mChartLineQsfx;
    @BindView(R.id.tv_gjzb_date)
    TextView mTvGjzbDate;
    @BindView(R.id.tv_qsfx_date)
    TextView mTvQsfxDate;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.h_scroll)
    HorizontalScrollView mHScroll;

    private ActionStatisticsAdapter adapter;

    private String activityId;
    private String startDate, endDate;

    private VaryViewHelper qsfxHelper = null;

    private Typeface mTf;
    private int width;

    List<ActivityPeriodStatsBean> listCj = new ArrayList<>();
    List<ActivityPeriodStatsBean> listZj = new ArrayList<>();
    List<ActivityPeriodStatsBean> listDj = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_action_detail;
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
        mTitleBar.setTitle("活动参与记录");

        activityId = getIntent().getStringExtra("activityId");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        mTvGjzbDate.setText(startDate + "~" + endDate);
        mTvQsfxDate.setText(startDate + "~" + endDate);

        qsfxHelper = new VaryViewHelper(mLineChartLl);
        qsfxHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        adapter = new ActionStatisticsAdapter();
        mLvStatistics.setAdapter(adapter);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadGjzb();
                loadQsfx();
            }
        });

        loadGjzb();
        loadQsfx();
    }


    /**
     * 关键指标接口
     */
    private void loadGjzb() {
        AsyncHttpUtil<ActivityTodayStatsBean> httpUtil = new AsyncHttpUtil<>(this, ActivityTodayStatsBean.class, new IUpdateUI<ActivityTodayStatsBean>() {
            @Override
            public void updata(ActivityTodayStatsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvCjNum.setText(jsonBean.getCjNum() + "");
                    mTvCjNum.withNumber(jsonBean.getCjNum());
                    mTvCjNum.setDuration(1000);
                    mTvCjNum.start();

                    mTvZjNum.setText(jsonBean.getZjNum() + "");
                    mTvZjNum.withNumber(jsonBean.getZjNum());
                    mTvZjNum.setDuration(1000);
                    mTvZjNum.start();

                    mTvDjNum.setText(jsonBean.getDjNum() + "");
                    mTvDjNum.withNumber(jsonBean.getDjNum());
                    mTvDjNum.setDuration(1000);
                    mTvDjNum.start();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });

        httpUtil.post(M_Url.queryActivityTodayStats, G_RequestParams.queryActivityTodayStats(startDate, endDate, activityId), true);
    }

    /**
     * 趋势分析
     */
    private void loadQsfx() {
        AsyncHttpUtil<ActivityPeriodStatsBean> httpUtil = new AsyncHttpUtil<>(this, ActivityPeriodStatsBean.class, new IUpdateUI<ActivityPeriodStatsBean>() {
            @Override
            public void updata(ActivityPeriodStatsBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getCjList() != null && jsonStr.getCjList().size() > 0) {
                        qsfxHelper.showDataView();
                        listCj.clear();
                        listDj.clear();
                        listZj.clear();

                        initLineView();
                        listCj.addAll(jsonStr.getCjList());
                        listZj.addAll(jsonStr.getZjList());
                        listDj.addAll(jsonStr.getDjList());
                        Collections.reverse(listCj);
                        Collections.reverse(listZj);
                        Collections.reverse(listDj);
                        setYsqsData(jsonStr.getCjList(), jsonStr.getDjList(), jsonStr.getZjList());
                        adapter.setData(listCj, listZj, listDj);
                    } else {
                        qsfxHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mChartLineQsfx.clear();
                qsfxHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryActivityPeriodStats, G_RequestParams.queryActivityPeriodStats(startDate, endDate, activityId), true);
    }

    /**
     * 初始换折线图
     */
    private void initLineView() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        //绘制图表右下角文字描述信息
        mChartLineQsfx.setDescription("");
        mChartLineQsfx.setDrawGridBackground(false);
        mChartLineQsfx.setDragEnabled(false);// 是否可以拖拽
        mChartLineQsfx.setScaleEnabled(false);// 是否可以缩放
        //        mChartLine.setBackgroundColor(getResources().getColor(R.color.white));
        //        mChartLine.setGridBackgroundColor(getResources().getColor(R.color.white)); //设置折线图的背景颜色

        //绘制图表的X轴
        XAxis xAxis = mChartLineQsfx.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(UIUtils.getColor(R.color.text_gray));
        xAxis.setSpaceBetweenLabels(1); //实现X轴数据显示的间隔

        //绘制图表的Y轴
        YAxis leftAxis = mChartLineQsfx.getAxisLeft();
        leftAxis.setTypeface(mTf);
        //false:代表值是平均分配的;
        leftAxis.setLabelCount(7, false);
        leftAxis.setEnabled(false); // 隐藏Y坐标轴
        //        leftAxis.setGridColor(
        //                getResources().getColor(R.color.transparent));
        mChartLineQsfx.getAxisRight().setEnabled(false);
        //可以放大X轴比例,从而实现X轴的左右滑动----此方法不能分本解决问题
        //        Matrix m=new Matrix();
        //        m.postScale(1.2f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        //        mChartLine.getViewPortHandler().refresh(m, mChartLine, false);//将图表动画显示之前进行缩放

        mChartLineQsfx.animateY(1000); // 立即执行的动画,x轴

        Legend l = mChartLineQsfx.getLegend();
        l.setEnabled(false);//去掉表外面显示的提示
    }

    /**
     * 设置趋势分析数据
     */
    private void setYsqsData(List<ActivityPeriodStatsBean> listCj, List<ActivityPeriodStatsBean> listDj, List<ActivityPeriodStatsBean> listZj) {
        final int width;
        if (listCj.size() > 7) {
            width = this.width / 7;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartLineQsfx.getLayoutParams();
            layoutParams.width = width * listCj.size();
            mChartLineQsfx.setLayoutParams(layoutParams);
        } else {
            width = this.width;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartLineQsfx.getLayoutParams();
            layoutParams.width = width - UIUtils.dp2px(20);
            mChartLineQsfx.setLayoutParams(layoutParams);
        }

        int index = 0;
        for (int i = 0; i < listCj.size(); i++) {
            if (listCj.get(i).getStatsNum() > 0 || listDj.get(i).getStatsNum() > 0 ||
                    listZj.get(i).getStatsNum() > 0) {
                index = i;
                break;
            }
        }
        final int finalIndex = index>2?index-1:index;
        mHScroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHScroll.scrollTo((finalIndex *width),0);
                mHScroll.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //活跃用户数量
        ArrayList<Entry> e3 = new ArrayList<Entry>();
        LineDataSet d3 = null;
        for (int i = 0; i < listDj.size(); i++) {
            e3.add(new Entry(listDj.get(i).getStatsNum(), i));
        }
        d3 = new LineDataSet(e3, "兑奖次数");
        d3.setColor(UIUtils.getColor(R.color.text_status));
        d3.setDrawValues(true);
        d3.setDrawCircles(true);//比现实小圆点
        d3.setDrawCircleHole(true);
        d3.setDrawFilled(true);//设置允许填充
        d3.setDrawCubic(true);
        d3.setValueTextSize(8f);
        d3.setFillAlpha(25);
        d3.setLineWidth(1.5f);
        d3.setCircleSize(3f);
        //            d2.setFillColor(Color.RED);
        d3.setHighLightColor(Color.rgb(244, 117, 117));
        d3.setValueFormatter(new MyYAxisValueTwoFormatter());

        //活跃用户数量
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        LineDataSet d2 = null;
        for (int i = 0; i < listZj.size(); i++) {
            e2.add(new Entry(listZj.get(i).getStatsNum(), i));
        }
        d2 = new LineDataSet(e2, "中奖次数");
        d2.setColor(UIUtils.getColor(R.color.color_699dff));
        d2.setDrawValues(true);
        d2.setDrawCircles(true);//比现实小圆点
        d2.setDrawCircleHole(true);
        d2.setDrawFilled(true);//设置允许填充
        d2.setDrawCubic(true);
        d2.setValueTextSize(8f);
        d2.setFillAlpha(25);
        d2.setLineWidth(1.5f);
        d2.setCircleSize(3f);
        //            d2.setFillColor(Color.RED);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setValueFormatter(new MyYAxisValueTwoFormatter());


        //新增的数量
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < listCj.size(); i++) {
            e1.add(new Entry(listCj.get(i).getStatsNum(), i));
        }
        LineDataSet d1 = null;
        d1 = new LineDataSet(e1, "抽奖次数");
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
        d1.setValueFormatter(new MyYAxisValueTwoFormatter());

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d3);
        sets.add(d2);
        sets.add(d1);

        LineData cd = new LineData(getMonths(listCj), sets);
        mChartLineQsfx.setData(cd);
    }

    private ArrayList<String> m = new ArrayList<String>();

    private ArrayList<String> getMonths(List<ActivityPeriodStatsBean> dataList) {
        m.clear();
        for (ActivityPeriodStatsBean bean : dataList) {
            m.add(bean.getSc_month() + "." + bean.getSc_day());
        }
        return m;
    }
}
