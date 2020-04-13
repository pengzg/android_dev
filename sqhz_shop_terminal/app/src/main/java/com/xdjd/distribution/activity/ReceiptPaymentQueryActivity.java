package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.ReceiptPaymentQueryAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ReceiptPaymentBean;
import com.xdjd.distribution.bean.UserBean;
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
import com.xdjd.view.NoScrollListView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/22
 *     desc   : 收付款查询
 *     version: 1.0
 * </pre>
 */

public class ReceiptPaymentQueryActivity extends BaseActivity implements ItemOnListener, CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.pull_scroll)
    PullToRefreshListView mPullScroll;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    //是否是开始或结束时间
    private boolean isStart;

    private ReceiptPaymentQueryAdapter adapter;

    private List<ReceiptPaymentBean> list;

    private String dateStartStr;
    private String dateEndStr;

    private Date date = new Date();

    private UserBean userBean;

    private TimePickerUtil mTimePickerUtil;

    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;
    private int dateType = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_receipt_payment_query;
    }

    @Override
    protected void initData() {
        super.initData();
        mTimePickerUtil = new TimePickerUtil();
        userBean = UserInfoUtils.getUser(this);

        //        mTitleBar.leftBack(this);
        mTitle.setText("收付款查询");

        dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        adapter = new ReceiptPaymentQueryAdapter(this);
        mPullScroll.setAdapter(adapter);

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

                if (list != null)
                    list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }
        }, this);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (list!=null){
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
                loadData();
            }
        });

        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_layout, R.id.ll_date_calendar, R.id.tv_date_type_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout://退出
                finish();
                break;
            case R.id.ll_date_calendar://日期区间选择
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
        }
    }

    private void loadData() {
        AsyncHttpUtil<ReceiptPaymentBean> httpUtil = new AsyncHttpUtil<>(this, ReceiptPaymentBean.class, new IUpdateUI<ReceiptPaymentBean>() {
            @Override
            public void updata(ReceiptPaymentBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list = jsonBean.getListData();
                        adapter.setData(list);
                    } else {

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
        httpUtil.post(M_Url.queryCashList, L_RequestParams.queryCashList(userBean.getUserId(),
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-")), true);
    }

    @Override
    public void onItem(int position) {
        Intent intent = new Intent(this, ReceiptPaymentDetailActivity.class);
        intent.putExtra("customerId", list.get(position).getGc_customerid());
        intent.putExtra("customerName", list.get(position).getGc_customerid_nameref());
        intent.putExtra("dateStartStr", dateStartStr);
        intent.putExtra("dateEndStr", dateEndStr);
        intent.putExtra("amount", list.get(position).getGcrr_total_amount());
        intent.putExtra("bean", list.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        if (list != null)
            list.clear();
        adapter.notifyDataSetChanged();
        loadData();
    }
}
