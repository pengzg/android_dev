package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OrderGoodsSearchAdapter;
import com.xdjd.distribution.adapter.PlaceAnOrderDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.PlaceAnOrderBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderGoodsSearchActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener, ItemOnListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    //    @BindView(R.id.lv_order)
    //    NoScrollListView mLvOrder;
    @BindView(R.id.pull_scroll)
    PullToRefreshListView mPullScroll;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private OrderGoodsSearchAdapter adapter;
    private List<PlaceAnOrderBean> list = new ArrayList<>();

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    //客户id
    private String customerId = "";

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    private Date date = new Date();

    private int page = 1;
    private int mFlag = 0;

    private TimePickerUtil mTimePickerUtil;
    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;
    private int dateType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_goods_search;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("订货查询");

        mTimePickerUtil = new TimePickerUtil();

        //初始化数据
        clientBean = UserInfoUtils.getClientInfo(this);
        if (clientBean != null) {
            customerId = clientBean.getCc_id();
        }

        //将外面选择的日期带到这个界面中
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");
        if (dateTypeStr != null && !"".equals(dateTypeStr)) {
            mTvDateTypeSelect.setText(dateTypeStr);
        }
        if (dateStartStr == null || "".equals(dateStartStr)) {
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        }

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        adapter = new OrderGoodsSearchAdapter(this);
        mPullScroll.setAdapter(adapter);
        adapter.setData(list);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.DISABLED);

        queryOrderGrid("");

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
                adapter.notifyDataSetChanged();
                queryOrderGrid("");
            }
        }, this);

        /*mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderGrid("");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                page++;
                queryOrderGrid("");
            }
        });*/
    }

    @OnClick({R.id.ll_date_calendar, R.id.tv_date_type_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_date_calendar://日期区间选择
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
        }
    }

    /**
     * 查询订单列表
     */
    private void queryOrderGrid(String searchStr) {
        AsyncHttpUtil<PlaceAnOrderBean> httpUtil = new AsyncHttpUtil<>(this, PlaceAnOrderBean.class, new IUpdateUI<PlaceAnOrderBean>() {
            @Override
            public void updata(PlaceAnOrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    //                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                    list = jsonBean.getListData();
                    adapter.setData(list);
                    //                    } else {
                    //                        if (mFlag == 2) {
                    //                            page--;
                    //                            showToast("没有更多数据了!");
                    //                        }
                    //                    }
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
                //                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.querySalesOrderApply, L_RequestParams.querySalesOrderApply("2", dateStartStr.replace(".", "-"),
                dateEndStr.replace(".", "-")), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        list.clear();
        adapter.notifyDataSetChanged();
        queryOrderGrid("");
    }

    @Override
    public void onItem(int position) {
        Intent intent = new Intent(this, PlaceAnOrderDetailActivity.class);
        intent.putExtra("oa_id", list.get(position).getOa_id());
        intent.putExtra("bean", list.get(position));
        startActivity(intent);
    }
}
