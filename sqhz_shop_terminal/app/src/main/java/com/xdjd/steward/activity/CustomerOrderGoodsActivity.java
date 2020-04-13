package com.xdjd.steward.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.adapter.CustomerOrderGoodsAdapter;
import com.xdjd.steward.bean.CustOrderAmountBean;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

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

public class CustomerOrderGoodsActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_customer_order_goods)
    NoScrollListView mLvCustomerOrderGoods;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private int page = 1;
    private int mFlag = 0;

    private List<CustOrderAmountBean> list = new ArrayList<>();
    private CustomerOrderGoodsAdapter adapter;

    private TimePickerUtil mTimePickerUtil;
    private int dateType = 1;

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_order_goods;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("客户订货排行列表");

        mTimePickerUtil = new TimePickerUtil();

        //将外面选择的日期带到这个界面中
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");
        if (dateTypeStr!=null && !"".equals(dateTypeStr)){
            mTvDateTypeSelect.setText(dateTypeStr);
        }
        if (dateStartStr == null || "".equals(dateStartStr)){
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        }

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

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

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadKhtj();
            }
        },this);


        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadKhtj();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadKhtj();
            }
        });

        adapter = new CustomerOrderGoodsAdapter();
        mLvCustomerOrderGoods.setAdapter(adapter);
        adapter.setData(list);

        loadKhtj();
    }

    /**
     * 客户订货统计
     */
    private void loadKhtj() {
        AsyncHttpUtil<CustOrderAmountBean> httpUtil = new AsyncHttpUtil<>(this, CustOrderAmountBean.class, new IUpdateUI<CustOrderAmountBean>() {
            @Override
            public void updata(CustOrderAmountBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
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
        String startDate = dateStartStr.replace(".", "-");
        String endDate = dateEndStr.replace(".", "-");
        httpUtil.post(M_Url.getCustOrderAmountList, G_RequestParams.getCustOrderAmountList(startDate, endDate,
                String.valueOf(page), String.valueOf(10)), true);
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr, dateEndStr,true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr, dateEndStr,true);
                break;
        }
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        loadKhtj();
    }
}
