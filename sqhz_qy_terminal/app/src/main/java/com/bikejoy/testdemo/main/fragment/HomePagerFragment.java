package com.bikejoy.testdemo.main.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.bikejoy.utils.DateUtils;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.TimePickerUtil;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.view.formatter.MyYAxisValueDecimalFormatter;
import com.bikejoy.view.formatter.MyYAxisValueIntegerFormatter;
import com.bikejoy.view.risenumber.RiseNumberTextView;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.popup.CalendarSelectPopup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 首页
 *     version: 1.0
 * </pre>
 */

public class HomePagerFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.tv_ysjb_select)
    TextView mTvYsjbSelect;
    @BindView(R.id.tv_ysjb_date)
    TextView mTvYsjbDate;
    @BindView(R.id.ll_ysjb)
    LinearLayout mLlYsjb;
    @BindView(R.id.tv_total_amount)
    RiseNumberTextView mTvTotalAmount;
    @BindView(R.id.tv_order_num)
    RiseNumberTextView mTvOrderNum;
    @BindView(R.id.ll_order_search)
    LinearLayout mLlOrderSearch;
    @BindView(R.id.tv_num)
    RiseNumberTextView mTvNum;
    @BindView(R.id.ll_receipt)
    LinearLayout mLlReceipt;
    @BindView(R.id.tv_ysqs_select)
    TextView mTvYsqsSelect;
    @BindView(R.id.tv_ysqs_date)
    TextView mTvYsqsDate;
    @BindView(R.id.ll_ysqs)
    LinearLayout mLlYsqs;
    @BindView(R.id.btn_order_amount)
    TextView mBtnOrderAmount;
    @BindView(R.id.btn_order_num)
    TextView mBtnOrderNum;
    @BindView(R.id.chart_line)
    LineChart mChartLine;
    @BindView(R.id.line_chart_ll)
    LinearLayout mLineChartLl;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    Unbinder unbinder;
    @BindView(R.id.tv_yhqs_select)
    TextView mTvYhqsSelect;
    @BindView(R.id.tv_yhqs_date)
    TextView mTvYhqsDate;
    @BindView(R.id.ll_yhqs)
    LinearLayout mLlYhqs;
    @BindView(R.id.chart_yh_line)
    LineChart mChartYhLine;
    @BindView(R.id.ll_yh_line_chart)
    LinearLayout mLlYhLineChart;
    @BindView(R.id.ll_apply_member)
    LinearLayout mLlApplyMember;
    @BindView(R.id.ll_order_group)
    LinearLayout mLlOrderGroup;
    @BindView(R.id.ll_ask)
    LinearLayout mLlAsk;
    //营收简报起止时间
    private String startDateYs;
    private String endDateYs;
    //营收趋势起止时间
    private String startDateYsqs;
    private String endDateYsqs;

    //用户增长趋势起止时间
    private String startDateYhqs;
    private String endDateYhqs;

    //1.今日;2.本周;3.本月;5.上月---营收趋势
    private int dateYsqs = 2;
    //1.今日;2.本周;3.本月;5.上月---营收趋势
    private int dateYhqs = 2;
    /**
     * 营收趋势订单类型--1 订单金额 2.收款 3订单数量
     */
    private int ysqsType = 1;
    //营收趋势日、月
    private int chartType = 1;
    //用户增长趋势日、月
    private int yhChartType = 1;

    /**
     * 选择日期区间type--1.营收简报;2.营收趋势 3.用户增长趋势
     */
    private int datePrickType = 1;

    private Typeface mTf;
    private int width;
    //营收趋势动态加载布局控件
    private VaryViewHelper ysqsHelper = null;
    private VaryViewHelper yhqsHelper = null;

    private Date date = new Date();
    private TimePickerUtil mTimePickerUtil;

    private UserBean userBean;
    private AdministratorMainActivity main;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepager, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mTimePickerUtil = new TimePickerUtil();

        userBean = UserInfoUtils.getUser(getActivity());
        main = (AdministratorMainActivity) getActivity();

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

        startDateYhqs = DateUtils.getFirstDayOfWeek(date, DateUtils.dateFormater4);
        endDateYhqs = DateUtils.getLastDayOfWeek(date, DateUtils.dateFormater4);
        mTvYhqsDate.setText(startDateYhqs + "-" + endDateYhqs);

        ysqsHelper = new VaryViewHelper(mLineChartLl);
        ysqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        yhqsHelper = new VaryViewHelper(mLlYhLineChart);
        yhqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));

        loadYsjb();
        loadYsqs();
        loadYhqs();

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadYsjb();
                loadYsqs();
                loadYhqs();
            }
        });
    }

    @OnClick({R.id.tv_ysjb_select, R.id.tv_ysqs_select, R.id.btn_order_amount, R.id.btn_order_num, R.id.ll_ysjb, R.id.ll_ysqs, R.id.ll_store,
            R.id.tv_yhqs_select, R.id.ll_yhqs, R.id.ll_order_search, R.id.ll_apply_member, R.id.ll_receipt, R.id.ll_order_group,R.id.ll_ask})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
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
            case R.id.tv_yhqs_select:
                datePrickType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYhqsDate.getText().toString().split("-")[0],
                        mTvYhqsDate.getText().toString().split("-")[1], false);
                break;
            case R.id.ll_yhqs:
                datePrickType = 3;
                mTimePickerUtil.showTimePicker(main.getMainId(), mTvYhqsDate.getText().toString().split("-")[0],
                        mTvYhqsDate.getText().toString().split("-")[1], false);
                break;
            case R.id.btn_order_amount:
                dateBtnSytle(1);//订单金额
                //1 订单金额 2.收款 3订单数量
                ysqsType = 1;
                loadYsqs();
                break;
            /*case R.id.btn_receipt_amount:
                dateBtnSytle(2);//收款金额
                ysqsType = 2;
                loadYsqs();
                break;*/
            case R.id.btn_order_num:
                dateBtnSytle(3);//订单量
                ysqsType = 3;
                loadYsqs();
                break;
            case R.id.ll_receipt:
                intent = new Intent(getActivity(), StoreOperateListActivity.class);
                intent.putExtra("dateStartStr", startDateYs);
                intent.putExtra("dateEndStr", endDateYs);
                intent.putExtra("dateTypeStr", mTvYsjbSelect.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_store://柜子销售明细
                startActivity(StoreOperateListActivity.class);
                break;
            case R.id.ll_order_search:
                intent = new Intent(getActivity(), OrderActivity.class);
                //                intent.putExtra("dateType",);
                intent.putExtra("dateTypeSelectStr", mTvYsjbSelect.getText().toString());
                intent.putExtra("dateStartStr", startDateYs);
                intent.putExtra("dateEndStr", endDateYs);
                startActivity(intent);
                break;
            case R.id.ll_apply_member://会员申请
                startActivity(ApplyMemberListActivity.class);
                break;
            case R.id.ll_order_group://团购订单
                startActivity(OrderGroupActivity.class);
                break;
            case R.id.ll_ask:
                DialogUtil.showExplainCustomDialog(getActivity(),"营收简报统计说明",UIUtils.getString(R.string.jb_explain));
                break;
        }
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
                    case 3:
                        mTvYhqsSelect.setText("自定义");
                        startDateYhqs = startDate;
                        endDateYhqs = endDate;
                        mTvYhqsDate.setText(startDate + "-" + endDate);
                        yhChartType = 1;
                        loadYhqs();
                        break;
                }
            }
        }, this);

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
            case 3:
                mTvYhqsSelect.setText(dateName);
                startDateYhqs = startDate;
                endDateYhqs = endDate;

                dateYhqs = position;

                switch (position) {
                    case 11:
                    case 12:
                    case 13:
                        yhChartType = 2;
                        loadYhqs();
                        break;
                    default:
                        yhChartType = 1;
                        loadYhqs();
                        break;
                }

                mTvYhqsDate.setText(startDate + "-" + endDate);
                break;
        }
    }


    /**
     * 简报请求接口
     */
    private void loadYsjb() {
        AsyncHttpUtil<BriefingBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BriefingBean.class, new IUpdateUI<BriefingBean>() {
            @Override
            public void updata(BriefingBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    BriefingBean bean = jsonBean.getData();
                    mTvTotalAmount.setText(bean.getTotalAmount() + "");
                    // 设置数据
                    mTvTotalAmount.withNumber(bean.getTotalAmount());
                    // 设置动画播放时间
                    mTvTotalAmount.setDuration(1000);
                    // 开始播放动画
                    mTvTotalAmount.start();
                    //                    mTvOrderNum.setText("/" + bean.getOrderNum() + "笔");

                    mTvOrderNum.setText(bean.getOrderNum() + "");
                    // 设置数据
                    mTvOrderNum.withNumber(bean.getOrderNum());
                    // 设置动画播放时间
                    mTvOrderNum.setDuration(1000);
                    // 开始播放动画
                    mTvOrderNum.start();

                    mTvNum.setText(bean.getSellNum() + "");
                    // 设置数据
                    mTvNum.withNumber(bean.getSellNum());
                    // 设置动画播放时间
                    mTvNum.setDuration(1000);
                    // 开始播放动画
                    mTvNum.start();
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                if (mPullScroll != null) {
                    mPullScroll.onRefreshComplete();
                }
            }
        });
        String startDate = startDateYs.replace(".", "-");
        String endDate = endDateYs.replace(".", "-");
        httpUtil.post(M_Url.getBriefing, L_RequestParams.getBriefing(startDate, endDate), true);
    }

    /**
     * 营收趋势
     */
    private void loadYsqs() {
        AsyncHttpUtil<TrendsChartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TrendsChartBean.class, new IUpdateUI<TrendsChartBean>() {
            @Override
            public void updata(TrendsChartBean jsonStr) {
                if ("200".equals(jsonStr.getCode())) {
                    TrendsChartBean bean = jsonStr.getData();
                    if (bean.getDataList() != null && bean.getDataList().size() > 0) {
                        ysqsHelper.showDataView();
                        initLineView();
                        setYsqsData(bean.getDataList());
                    } else {
                        ysqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    showToast(jsonStr.getDesc());
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
        httpUtil.post(M_Url.getTrendsChart, L_RequestParams.getTrendsChart(startDate, endDate,
                String.valueOf(ysqsType), String.valueOf(chartType)), true);
    }

    /**
     * 用户增长趋势
     */
    private void loadYhqs() {
        AsyncHttpUtil<TrendsChartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TrendsChartBean.class, new IUpdateUI<TrendsChartBean>() {
            @Override
            public void updata(TrendsChartBean jsonStr) {
                if ("200".equals(jsonStr.getCode())) {
                    TrendsChartBean bean = jsonStr.getData();
                    if (bean.getDataList() != null && bean.getDataList().size() > 0) {
                        yhqsHelper.showDataView();
                        initLineYhqsView();
                        setYhqsData(bean.getDataList());
                    } else {
                        yhqsHelper.showEmptyView(UIUtils.getString(R.string.no_data));
                    }
                } else {
                    showToast(jsonStr.getDesc());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mChartYhLine.clear();
                yhqsHelper.showEmptyView(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        String startDate = startDateYhqs.replace(".", "-");
        String endDate = endDateYhqs.replace(".", "-");
        httpUtil.post(M_Url.getTrendsNewMember, L_RequestParams.getTrendsNewMember(startDate, endDate,
                String.valueOf(yhChartType)), true);
    }

    /**
     * 设置 1:订单金额 2:收款金额 3:订单量
     *
     * @param type
     */
    private void dateBtnSytle(int type) {
        mBtnOrderAmount.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        mBtnOrderNum.setBackgroundColor(UIUtils.getColor(R.color.transparent));

        mBtnOrderAmount.setTextColor(UIUtils.getColor(R.color.text_gray));
        mBtnOrderNum.setTextColor(UIUtils.getColor(R.color.text_gray));

        mBtnOrderAmount.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mBtnOrderNum.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        switch (type) {
            case 1:
                mBtnOrderAmount.setBackgroundDrawable(UIUtils.getDrawable(R.drawable.bg_left_btn));
                mBtnOrderAmount.setTextColor(UIUtils.getColor(R.color.white));
                mBtnOrderAmount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
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
        Description description = new Description();
        description.setText("");
        mChartLine.setDescription(description);
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
            layoutParams.width = width - UIUtils.dp2px(15);
            mChartLine.setLayoutParams(layoutParams);
        }

        XAxis xAxis = mChartLine.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonths(dataList,dateYsqs)));

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < dataList.size(); i++) {
            if (ysqsType == 3) {//订单数量
                e1.add(new Entry(i,dataList.get(i).getOrderNum()));
            } else {
                e1.add(new Entry(i,dataList.get(i).getOrderAmount()));
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
        d1.setDrawFilled(true);
        d1.setValueTextSize(9f);
        d1.setFillAlpha(75);
        d1.setFillColor(Color.rgb(201, 220, 255));//设置填充颜色
        d1.setColor(UIUtils.getColor(R.color.color_699dff));
        d1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        if (ysqsType == 3) {
            d1.setValueFormatter(new MyYAxisValueIntegerFormatter());
        } else {
            d1.setValueFormatter(new MyYAxisValueDecimalFormatter());
        }

        LineData cd = new LineData(d1);
        mChartLine.setData(cd);
    }

    /**
     * 初始换折线图
     */
    private void initLineYhqsView() {
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        //绘制图表右下角文字描述信息
        Description description = new Description();
        description.setText("");
        mChartYhLine.setDescription(description);
        mChartYhLine.setDrawGridBackground(false);
        mChartYhLine.setDragEnabled(false);// 是否可以拖拽
        mChartYhLine.setScaleEnabled(false);// 是否可以缩放
        //        mChartLine.setBackgroundColor(getResources().getColor(R.color.white));
        //        mChartLine.setGridBackgroundColor(getResources().getColor(R.color.white)); //设置折线图的背景颜色

        //绘制图表的X轴
        XAxis xAxis = mChartYhLine.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(UIUtils.getColor(R.color.text_gray));

        //绘制图表的Y轴
        YAxis leftAxis = mChartYhLine.getAxisLeft();
        leftAxis.setTypeface(mTf);
        //false:代表值是平均分配的;
        leftAxis.setLabelCount(7, false);
        leftAxis.setEnabled(false); // 隐藏Y坐标轴
        //        leftAxis.setGridColor(
        //                getResources().getColor(R.color.transparent));

        mChartYhLine.getAxisRight().setEnabled(false);
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

        mChartYhLine.animateY(400); // 立即执行的动画,x轴

        Legend l = mChartYhLine.getLegend();
        l.setEnabled(false);//去掉表外面显示的提示
    }

    /**
     * 设置营收趋势数据
     *
     * @param dataList
     */
    private void setYhqsData(List<TrendsChartBean> dataList) {
        if (dataList.size() > 7) {
            int width = this.width / 7;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartYhLine.getLayoutParams();
            layoutParams.width = width * dataList.size();
            mChartYhLine.setLayoutParams(layoutParams);
        } else {
            int width = this.width;
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) mChartYhLine.getLayoutParams();
            layoutParams.width = width - UIUtils.dp2px(20);
            mChartYhLine.setLayoutParams(layoutParams);
        }

        XAxis xAxis = mChartYhLine.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonths(dataList,ysqsType)));

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < dataList.size(); i++) {
            e1.add(new Entry(i,dataList.get(i).getNewAddNum()));
        }
        LineDataSet d1 = new LineDataSet(e1, "用户增长数量");
        //指定数据集合绘制时候的属性
        //        d1.setLineWidth(1.5f);
        d1.setCircleSize(4f);
        d1.setHighLightColor(Color.BLACK);
        d1.setDrawValues(true);
        d1.setDrawCircles(true);//比现实小圆点
        d1.setDrawCircleHole(true);
        d1.setDrawFilled(true);//设置允许填充
        d1.setDrawFilled(true);
        d1.setCubicIntensity(1);
        d1.setValueTextSize(9f);
        d1.setFillAlpha(75);
        d1.setFillColor(Color.rgb(201, 220, 255));//设置填充颜色
        d1.setColor(UIUtils.getColor(R.color.color_699dff));
        d1.setValueFormatter(new MyYAxisValueIntegerFormatter());
        d1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        LineData cd = new LineData(d1);
        mChartYhLine.setData(cd);
    }

    private ArrayList<String> m = new ArrayList<String>();

    private ArrayList<String> getMonths(List<TrendsChartBean> dataList, int dateType) {
        m.clear();
        if (dateType == 2 && dataList.size() == 7) {
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
                switch (dateType) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
