package com.xdjd.distribution.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.PhStatisticBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.RolloutGoodsEvent;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.risenumber.RiseNumberTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/7
 *     desc   : 商品铺货activity主信息展示界面
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_qsfx_select)
    TextView mTvQsfxSelect;
    @BindView(R.id.tv_qsfx_date)
    TextView mTvQsfxDate;
    @BindView(R.id.ll_qsfx)
    LinearLayout mLlQsfx;
    @BindView(R.id.chart_line_qsfx)
    LineChart mChartLineQsfx;
    @BindView(R.id.ll_qsfx_line)
    LinearLayout mLlQsfxLine;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.ll_go_rollout_goods)
    LinearLayout mLlGoRolloutGoods;
    @BindView(R.id.ll_sell)
    LinearLayout mLlSell;
    @BindView(R.id.ll_withdraw_goods)
    LinearLayout mLlWithdrawGoods;
    @BindView(R.id.ll_rollout_goods_list)
    LinearLayout mLlRolloutGoodsList;
    @BindView(R.id.tv_phsj_select)
    TextView mTvPhsjSelect;
    @BindView(R.id.tv_phsj_date)
    TextView mTvPhsjDate;
    @BindView(R.id.tv_ph_total_amount)
    RiseNumberTextView tvPhTotalAmount;
    @BindView(R.id.tv_sale_amount)
    RiseNumberTextView tvSaleAmount;
    @BindView(R.id.tv_wxs_amount)
    RiseNumberTextView tvWxsAmount;
    @BindView(R.id.ll_rollout_goods)
    LinearLayout mLlRolloutGoods;
    @BindView(R.id.ll_sales)
    LinearLayout mLlSales;
    @BindView(R.id.ll_recall)
    LinearLayout mLlRecall;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_yth_amount)
    RiseNumberTextView mTvYthAmount;
    @BindView(R.id.ll_rollout_goods_declare)
    LinearLayout mLlRolloutGoodsDeclare;
    @BindView(R.id.ll_rollout_goods_task)
    LinearLayout mLlRolloutGoodsTask;
    @BindView(R.id.ll_rollout_declare_order)
    LinearLayout mLlRolloutDeclareOrder;

    private TimePickerUtil mTimePickerUtil;
    private int dateQsfx = 1;
    //店铺统计起始日期
    private String startDateQsfx, endDateQsfx;
    //数据统计起始日期
    private String startDateSjtj, endDateSjtj;

    private Typeface mTf;
    private int width;
    private VaryViewHelper qsfxHelper = null;

    private List<PhStatisticBean> listAdd;
    private List<PhStatisticBean> listTotal;
    //选择日期区间类型 1-铺货数据统计 2-铺货店铺统计
    private int datePrickType = 1;

    private ClientBean mClientBean;
    private UserBean mUserBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("铺货主页");

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        mClientBean = UserInfoUtils.getClientInfo(this);
        mUserBean = UserInfoUtils.getUser(this);

        String todayStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        startDateSjtj = todayStr;
        endDateSjtj = todayStr;
        mTvPhsjDate.setText(startDateSjtj + "-" + endDateSjtj);

        startDateQsfx = DateUtils.getFirstDayOfWeek(new Date(), DateUtils.dateFormater4);
        endDateQsfx = DateUtils.getLastDayOfWeek(new Date(), DateUtils.dateFormater4);
        mTvQsfxDate.setText(startDateQsfx + "-" + endDateQsfx);

        qsfxHelper = new VaryViewHelper(mLlQsfxLine);
        qsfxHelper.showEmptyView(UIUtils.getString(R.string.no_data));
        mTimePickerUtil = new TimePickerUtil();
        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                int numMonth = DateUtils.getMonth(DateUtils.getDate(startDate),
                        DateUtils.getDate(endDate));
                if (numMonth > 3) {
                    showToast("筛选时间不能超过三个月");
                    return;
                }

                if (datePrickType == 2 && DateUtils.getDateSpace(startDate, endDate) < 7) {
                    showToast("筛选时间不能小于7天");
                    LogUtils.e("getDutyDays1", DateUtils.getDateSpace(
                            startDate, endDate) + "");
                    return;
                }

                startDate = startDate.replace("-", ".");
                endDate = endDate.replace("-", ".");
                switch (datePrickType) {
                    case 1:
                        mTvPhsjSelect.setText("自定义");
                        startDateSjtj = startDate;
                        endDateSjtj = endDate;
                        mTvPhsjDate.setText(startDate + "-" + endDate);
                        loadPhDataStatistic(true);
                        break;
                    case 2:
                        mTvQsfxSelect.setText("自定义");
                        dateQsfx = 4;
                        startDateQsfx = startDate;
                        endDateQsfx = endDate;
                        mTvQsfxDate.setText(startDate + "-" + endDate);
                        loadQsfx(true);
                        break;
                }
                mTimePickerUtil.calendarPopup.dismiss();
            }
        }, this);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadQsfx(true);
                loadPhDataStatistic(true);
            }
        });

        loadQsfx(true);
        loadPhDataStatistic(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mClientBean = UserInfoUtils.getClientInfo(this);
    }

    @OnClick({R.id.ll_go_rollout_goods, R.id.ll_sell, R.id.ll_withdraw_goods, R.id.ll_rollout_goods_list, R.id.tv_qsfx_select,
            R.id.tv_qsfx_date, R.id.tv_phsj_select, R.id.tv_phsj_date, R.id.ll_rollout_goods, R.id.ll_sales, R.id.ll_recall,
            R.id.ll_rollout_goods_declare, R.id.ll_rollout_goods_task,R.id.ll_rollout_declare_order})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_rollout_goods_declare://铺货申报
                if (mClientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    startActivity(SelectPHStoreActivity.class);
                }
                break;
            case R.id.ll_rollout_goods_task://铺货单配送任务
                if ("".equals(mUserBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                startActivity(PHDistributionTaskActivity.class);
                break;
            case R.id.ll_go_rollout_goods://铺货
                if ("".equals(mUserBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                if (mClientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    //                    startActivity(GoRolloutGoodsActivity.class);
                    startActivity(NewGoRolloutGoodsActivity.class);
                }
                break;
            case R.id.ll_sell://铺货销售
                if ("".equals(mUserBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                if (mClientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    intent = new Intent(this, RolloutGoodsSellActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_withdraw_goods://撤货 withdraw
                if ("".equals(mUserBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                if (mClientBean == null) {
                    hint("请先刷客户档案或选择客户...");
                } else {
                    intent = new Intent(this, RolloutGoodsWithdrawActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_rollout_goods_list:
                startActivity(RolloutGoodsListingActivity.class);
                break;
            case R.id.tv_qsfx_select:
                datePrickType = 2;
                mTimePickerUtil.hideYear();
                mTimePickerUtil.showTimePicker(mLlMain, mTvQsfxDate.getText().toString().split("-")[0],
                        mTvQsfxDate.getText().toString().split("-")[1], false);
                break;
            case R.id.tv_qsfx_date:
                datePrickType = 2;
                mTimePickerUtil.hideYear();
                mTimePickerUtil.showTimePicker(mLlMain, mTvQsfxDate.getText().toString().split("-")[0],
                        mTvQsfxDate.getText().toString().split("-")[1], false);
                break;
            case R.id.tv_phsj_select:
                datePrickType = 1;
                mTimePickerUtil.hideYear();
                mTimePickerUtil.showTimePicker(mLlMain, mTvPhsjDate.getText().toString().split("-")[0],
                        mTvPhsjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.tv_phsj_date:
                datePrickType = 1;
                mTimePickerUtil.hideYear();
                mTimePickerUtil.showTimePicker(mLlMain, mTvPhsjDate.getText().toString().split("-")[0],
                        mTvPhsjDate.getText().toString().split("-")[1], true);
                break;
            case R.id.ll_rollout_goods:
                //1:铺货单详情 2：铺货销售单详情 3：撤货单详情 4:铺货申报单
                intent = new Intent(this, RolloutGoodsOrderActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.ll_sales:
                intent = new Intent(this, RolloutGoodsOrderActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.ll_recall:
                intent = new Intent(this, RolloutGoodsOrderActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.ll_rollout_declare_order:
                intent = new Intent(this, RolloutGoodsOrderActivity.class);
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
        }
    }

    /**
     * 没有选择客户提示
     */
    private void hint(String message) {
        DialogUtil.showCustomDialog(this, "注意", message, "确定", null, new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                startActivity(SelectClientActivity.class);
            }

            @Override
            public void no() {

            }
        });
    }

    /**
     * 没有车仓库是提示弹框
     */
    private void noCarHint() {
        DialogUtil.showCustomDialog(this, "提示", "您还没有相关联的车仓库,请联系后台文员进行车仓库关联!", "确定", null, null);
    }

    /**
     * 铺货数据统计
     */
    private void loadPhDataStatistic(boolean isShowDialog) {
        AsyncHttpUtil<PhStatisticBean> httpUtil = new AsyncHttpUtil<>(this, PhStatisticBean.class, new IUpdateUI<PhStatisticBean>() {
            @Override
            public void updata(PhStatisticBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    tvPhTotalAmount.setText(bean.getPhZje() + "");//铺货总金额
                    // 设置数据
                    tvPhTotalAmount.withNumber(bean.getPhZje());
                    // 设置动画播放时间
                    tvPhTotalAmount.setDuration(1000);
                    // 开始播放动画
                    tvPhTotalAmount.start();
                    tvSaleAmount.setText(bean.getYxsje() + "");//已销售金额
                    tvSaleAmount.withNumber(bean.getYxsje());
                    tvSaleAmount.setDuration(1000);
                    tvSaleAmount.start();
                    tvWxsAmount.setText(bean.getWxsje() + "");//未销售金额
                    tvWxsAmount.withNumber(bean.getWxsje());
                    tvWxsAmount.setDuration(1000);
                    tvWxsAmount.start();
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
        String startDate = startDateSjtj.replace(".", "-");
        String endDate = endDateSjtj.replace(".", "-");
        httpUtil.post(M_Url.phDataStatistic, L_RequestParams.phDataStatistic(startDate, endDate, "0"), isShowDialog);
    }

    /**
     * 趋势分析
     */
    private void loadQsfx(boolean isShowDialog) {
        AsyncHttpUtil<PhStatisticBean> httpUtil = new AsyncHttpUtil<>(this, PhStatisticBean.class, new IUpdateUI<PhStatisticBean>() {
            @Override
            public void updata(PhStatisticBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getAddList() != null && jsonStr.getAddList().size() > 0) {
                        qsfxHelper.showDataView();
                        initLineView();
                        listAdd = jsonStr.getAddList();
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
        httpUtil.post(M_Url.phShopStatistic, L_RequestParams.phShopStatistic(UserInfoUtils.getId(this), startDate, endDate), isShowDialog);
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

        //累计店铺数量
        ArrayList<Entry> e3 = new ArrayList<Entry>();
        LineDataSet d3 = null;
        for (int i = 0; i < listTotal.size(); i++) {
            e3.add(new Entry(listTotal.get(i).getTotalNum(), i));
        }
        d3 = new LineDataSet(e3, "累计店铺");
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

        //新增的店铺数量
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < listAdd.size(); i++) {
            e1.add(new Entry(listAdd.get(i).getAddNum(), i));
        }
        LineDataSet d1 = null;
        d1 = new LineDataSet(e1, "新增店铺");
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
        //        sets.add(d2);
        sets.add(d1);

        LineData cd = new LineData(getMonths(listAdd), sets);
        mChartLineQsfx.setData(cd);
    }

    private ArrayList<String> m = new ArrayList<String>();

    private ArrayList<String> getMonths(List<PhStatisticBean> dataList) {
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
            for (PhStatisticBean bean : dataList) {
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
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        switch (datePrickType) {
            case 1:
                mTvPhsjSelect.setText(dateName);
                startDateSjtj = startDate;
                endDateSjtj = endDate;
                mTvPhsjDate.setText(startDateSjtj + "-" + endDateSjtj);
                loadPhDataStatistic(true);
                break;
            case 2:
                mTvQsfxSelect.setText(dateName);
                startDateQsfx = startDate;
                endDateQsfx = endDate;
                dateQsfx = position;
                mTvQsfxDate.setText(startDateQsfx + "-" + endDateQsfx);
                loadQsfx(true);
                break;
        }
    }

    public void onEventMainThread(RolloutGoodsEvent event) {
        loadQsfx(false);
        loadPhDataStatistic(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
