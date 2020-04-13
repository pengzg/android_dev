package com.xdjd.winningrecord.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.popup.CalendarSelectPopup;
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
import com.xdjd.winningrecord.activity.UserListingActivity;
import com.xdjd.winningrecord.bean.UserMonthlyStatsBean;
import com.xdjd.winningrecord.bean.UserTodayStatsBean;
import com.xdjd.winningrecord.main.RecordMainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lijipei on 2017/11/2.
 * 活动界面
 */

public class UserFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    Unbinder unbinder;
    @BindView(R.id.ll_customer_listing)
    LinearLayout mLlCustomerListing;
    @BindView(R.id.tv_gjzb_select)
    TextView mTvGjzbSelect;
    @BindView(R.id.tv_gjzb_date)
    TextView mTvGjzbDate;
    @BindView(R.id.tv_qsfx_select)
    TextView mTvQsfxSelect;
    @BindView(R.id.tv_qsfx_date)
    TextView mTvQsfxDate;
    @BindView(R.id.chart_line_qsfx)
    LineChart mChartLineQsfx;
    @BindView(R.id.tv_new_num)
    RiseNumberTextView mTvNewNum;
    @BindView(R.id.tv_active_num)
    RiseNumberTextView mTvActiveNum;
    @BindView(R.id.tv_total_num)
    RiseNumberTextView mTvTotalNum;
    @BindView(R.id.ll_qsfx_line)
    LinearLayout mLlQsfxLine;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private View view;

    private TimePickerUtil mTimePickerUtil;
    /**
     * 选择日期区间type--1.关键指标;2.趋势分析;
     */
    private int datePrickType = 1;

    private int dateQsfx = 1;

    private String startDateGjzb, endDateGjzb;
    private String startDateQsfx, endDateQsfx;

    private RecordMainActivity main;

    private Typeface mTf;
    private int width;

    private VaryViewHelper qsfxHelper = null;

    private List<UserMonthlyStatsBean> listAdd;
    private List<UserMonthlyStatsBean> listActive;
    private List<UserMonthlyStatsBean> listTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        main = (RecordMainActivity) getActivity();

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        qsfxHelper = new VaryViewHelper(mLlQsfxLine);
        qsfxHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        mTimePickerUtil = new TimePickerUtil();
        initCustomTimePicker();

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

    @OnClick({R.id.tv_gjzb_select, R.id.tv_qsfx_select, R.id.ll_customer_listing
            , R.id.tv_gjzb_date, R.id.tv_qsfx_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_gjzb_select:
                datePrickType = 1;
                mTimePickerUtil.showYear();
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvGjzbDate.getText().toString().split("-")[0],
                        mTvGjzbDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_qsfx_select:
                datePrickType = 2;
                mTimePickerUtil.hideYear();
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvQsfxDate.getText().toString().split("-")[0],
                        mTvQsfxDate.getText().toString().split("-")[1], false);
                break;
            case R.id.ll_customer_listing:
                startActivity(UserListingActivity.class);
                break;
            case R.id.tv_gjzb_date:
                datePrickType = 1;
                mTimePickerUtil.showYear();
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvGjzbDate.getText().toString().split("-")[0],
                        mTvGjzbDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_qsfx_date:
                datePrickType = 2;
                mTimePickerUtil.hideYear();
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvQsfxDate.getText().toString().split("-")[0],
                        mTvQsfxDate.getText().toString().split("-")[1], false);
                break;
        }
    }

    private void initCustomTimePicker() {
        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateGjzb = todayStr;
        endDateGjzb = todayStr;
        mTvGjzbDate.setText(startDateGjzb + "-" + endDateGjzb);

        startDateQsfx = DateUtils.getFirstDayOfWeek(new Date(), DateUtils.dateFormater4);
        endDateQsfx = DateUtils.getLastDayOfWeek(new Date(), DateUtils.dateFormater4);
        mTvQsfxDate.setText(startDateQsfx + "-" + endDateQsfx);

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
                    case 1://关键指标
                        mTvGjzbSelect.setText("自定义");
                        startDateGjzb = startDate;
                        endDateGjzb = endDate;
                        mTvGjzbDate.setText(startDate + "-" + endDate);
                        loadGjzb();
                        break;
                    case 2://趋势分析
                        mTvQsfxSelect.setText("自定义");
                        dateQsfx = 4;
                        startDateQsfx = startDate;
                        endDateQsfx = endDate;
                        mTvQsfxDate.setText(startDate + "-" + endDate);
                        loadQsfx();
                        break;
                }
                mTimePickerUtil.calendarPopup.dismiss();
            }
        }, this);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        switch (datePrickType) {
            case 1:
                mTvGjzbSelect.setText(dateName);
                startDateGjzb = startDate;
                endDateGjzb = endDate;
                mTvGjzbDate.setText(startDateGjzb + "-" + endDateGjzb);

                loadGjzb();
                break;
            case 2:
                mTvQsfxSelect.setText(dateName);
                startDateQsfx = startDate;
                endDateQsfx = endDate;

                dateQsfx = position;

                mTvQsfxDate.setText(startDateQsfx + "-" + endDateQsfx);
                loadQsfx();
                break;
        }
    }

    /**
     * 关键指标接口
     */
    private void loadGjzb() {
        AsyncHttpUtil<UserTodayStatsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), UserTodayStatsBean.class, new IUpdateUI<UserTodayStatsBean>() {
            @Override
            public void updata(UserTodayStatsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvNewNum.setText(jsonBean.getAddNum() + "");
                    mTvNewNum.withNumber(jsonBean.getAddNum());
                    mTvNewNum.setDuration(1000);
                    mTvNewNum.start();

                    mTvActiveNum.setText(jsonBean.getDailyActiveNum() + "");
                    mTvActiveNum.withNumber(jsonBean.getDailyActiveNum());
                    mTvActiveNum.setDuration(1000);
                    mTvActiveNum.start();

                    mTvTotalNum.setText(jsonBean.getTotalNum() + "");
                    mTvTotalNum.withNumber(jsonBean.getTotalNum());
                    mTvTotalNum.setDuration(1000);
                    mTvTotalNum.start();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });

        String startDate = startDateGjzb.replace(".", "-");
        String endDate = endDateGjzb.replace(".", "-");
        httpUtil.post(M_Url.queryUserTodayStats, G_RequestParams.queryUserTodayStats(startDate, endDate), true);
    }

    /**
     * 趋势分析
     */
    private void loadQsfx() {
        AsyncHttpUtil<UserMonthlyStatsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), UserMonthlyStatsBean.class, new IUpdateUI<UserMonthlyStatsBean>() {
            @Override
            public void updata(UserMonthlyStatsBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getAddList() != null && jsonStr.getAddList().size() > 0) {
                        qsfxHelper.showDataView();
                        initLineView();
                        listAdd = jsonStr.getAddList();
                        listActive = jsonStr.getActiveList();
                        listTotal = jsonStr.getTotalList();
                        setYsqsData();
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
        String startDate = startDateQsfx.replace(".", "-");
        String endDate = endDateQsfx.replace(".", "-");
        httpUtil.post(M_Url.queryUserMonthlyStats, G_RequestParams.queryUserMonthlyStats(startDate, endDate), true);
    }

    /**
     * 初始换折线图
     */
    private void initLineView() {
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
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
        l.setEnabled(true);//去掉表外面显示的提示
    }

    /**
     * 设置趋势分析数据
     */
    private void setYsqsData() {
        if (listAdd.size() > 7) {
            int width = this.width / 7;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartLineQsfx.getLayoutParams();
            layoutParams.width = width * listAdd.size();
            mChartLineQsfx.setLayoutParams(layoutParams);
        } else {
            int width = this.width;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartLineQsfx.getLayoutParams();
            layoutParams.width = width - UIUtils.dp2px(20);
            mChartLineQsfx.setLayoutParams(layoutParams);
        }

        //活跃用户数量
        ArrayList<Entry> e3 = new ArrayList<Entry>();
        LineDataSet d3 = null;
        for (int i = 0; i < listTotal.size(); i++) {
            e3.add(new Entry(listTotal.get(i).getNewNum(), i));
        }
        d3 = new LineDataSet(e3, "累计用户");
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
        for (int i = 0; i < listActive.size(); i++) {
            e2.add(new Entry(listActive.get(i).getNewNum(), i));
        }
        d2 = new LineDataSet(e2, "活跃用户");
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
        for (int i = 0; i < listAdd.size(); i++) {
            e1.add(new Entry(listAdd.get(i).getNewNum(), i));
        }
        LineDataSet d1 = null;
        d1 = new LineDataSet(e1, "新增用户");
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

        LineData cd = new LineData(getMonths(listAdd), sets);
        mChartLineQsfx.setData(cd);
    }

    private ArrayList<String> m = new ArrayList<String>();

    private ArrayList<String> getMonths(List<UserMonthlyStatsBean> dataList) {
        m.clear();
        if (dateQsfx == 1 && dataList.size() == 7) {
            m.add("周一");
            m.add("周二");
            m.add("周三");
            m.add("周四");
            m.add("周五");
            m.add("周六");
            m.add("周日");
        } else {
            for (UserMonthlyStatsBean bean : dataList) {
                //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周,
                //8.前天;9.上上周;10.上上月;11.今年;12.去年;13.前年
                switch (dateQsfx) {
                    case 3://本月
                    case 5:
                    case 10:
                        m.add(bean.getSc_month() + "." + bean.getSc_day());
                        break;
                    case 11:
                    case 12:
                    case 13:
                        m.add(bean.getSc_month() + "月");
                        break;
                    default:
                        m.add(bean.getSc_month() + "." + bean.getSc_day());
                        break;
                }
            }
        }
        return m;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
