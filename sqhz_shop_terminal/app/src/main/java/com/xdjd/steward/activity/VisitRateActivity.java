package com.xdjd.steward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.adapter.VisitRateAdapter;
import com.xdjd.steward.bean.VisitRateBean;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
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
 *     time   : 2017/8/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VisitRateActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener{
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;


    private int page = 1;
    private int mFlag = 0;

    private DisplayMetrics metric;

    private String dateStartStr;
    private String dateEndStr;

    private String dateTypeStr;

    private TimePickerUtil mTimePickerUtil;

    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;
    private int dateType = 1;

    private List<VisitRateBean> list = new ArrayList<>();
    private VisitRateAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_visit_rate;
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
        mTitleBar.setTitle("访店达成率");

        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");

        if (dateTypeStr != null && !"".equals(dateTypeStr)) {
            mTvDateTypeSelect.setText(dateTypeStr);
        }
        if (dateStartStr == null || "".equals(dateStartStr)) {
            //默认本月时间
            mTvDateTypeSelect.setText("本月");
            dateStartStr = DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
            dateEndStr = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);
        }

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

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }
        },this);

        adapter = new VisitRateAdapter();
        mLvNoScroll.setAdapter(adapter);
        adapter.setData(list);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        loadData();

        mLvNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(VisitRateActivity.this,StaffVisitListActivity.class);
                intent.putExtra("salesid",list.get(i).getSalesid());
                intent.putExtra("salesName",list.get(i).getSalesid_nameref());
                intent.putExtra("dateType",dateType);
                intent.putExtra("dateTypeSelectStr",mTvDateTypeSelect.getText().toString());
                intent.putExtra("dateStartStr",dateStartStr);
                intent.putExtra("dateEndStr",dateEndStr);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr,
                        dateEndStr,true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr,
                        dateEndStr,true);
                break;
        }
    }

    private void loadData() {
        AsyncHttpUtil<VisitRateBean> httpUtil = new AsyncHttpUtil<>(this, VisitRateBean.class, new IUpdateUI<VisitRateBean>() {
            @Override
            public void updata(VisitRateBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getListData() != null && jsonStr.getListData().size() > 0) {
                        list.addAll(jsonStr.getListData());
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(jsonStr.getRepMsg());
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

        httpUtil.post(M_Url.getVisitRate, G_RequestParams.getVisitRate(dateStartStr.replace(".","-"),
                dateEndStr.replace(".","-"), String.valueOf(page)), true);
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

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        loadData();
    }
}
