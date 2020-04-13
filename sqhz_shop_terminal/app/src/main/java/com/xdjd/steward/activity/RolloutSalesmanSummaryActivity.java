package com.xdjd.steward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.SearchGoodsActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.PhSalesmanBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.adapter.RolloutSalesmanSummaryAdapter;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutSalesmanSummaryActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private RolloutSalesmanSummaryAdapter adapter;
    private List<PhSalesmanBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private TimePickerUtil mTimePickerUtil;
    private int dateType = 1;
    private String dateTypeStr;//日期区间描述,如:今天
    private String dateStartStr;
    private String dateEndStr;

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_salesman_summary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("业务员铺货汇总");
    }

    @Override
    protected void initData() {
        super.initData();
        initRefresh(mLvPull);
        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);

        adapter = new RolloutSalesmanSummaryAdapter();
        mLvPull.setAdapter(adapter);
        adapter.setData(list);
        adapter.setListener(new ItemOnListener() {
            @Override
            public void onItem(int position) {
                Intent intent = new Intent(RolloutSalesmanSummaryActivity.this, RolloutShopSummaryActivity.class);
                intent.putExtra("salesId", list.get(position).getSalesid());
                intent.putExtra("dateStartStr",dateStartStr);
                intent.putExtra("dateEndStr",dateEndStr);
                intent.putExtra("dateTypeStr",mTvDateTypeSelect.getText().toString());
                startActivity(intent);
            }
        });

        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        mTimePickerUtil = new TimePickerUtil();
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");
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
        }, this);
        if (dateTypeStr != null && !"".equals(dateTypeStr)) {
            mTvDateTypeSelect.setText(dateTypeStr);
        }
        if (dateStartStr == null || "".equals(dateStartStr)) {
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        }
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        loadData();
    }

    private void loadData() {
        AsyncHttpUtil httpUtil = new AsyncHttpUtil<>(this, PhSalesmanBean.class, new IUpdateUI<PhSalesmanBean>() {
            @Override
            public void updata(PhSalesmanBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                    adapter.notifyDataSetChanged();
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
                mLvPull.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.ywyPhHz, L_RequestParams.ywyPhHz(String.valueOf(page),
                mTvSearch.getText().toString(),
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-")), true);
    }

    @OnClick({R.id.ll_clear, R.id.ll_search, R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_search:
                intent = new Intent(this, SearchGoodsActivity.class);
                intent.putExtra("searchStr", mTvSearch.getText().toString());
                intent.putExtra("hint", "店铺名称");
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_clear:
                mTvSearch.setText("");
                mLlClear.setVisibility(View.GONE);
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                mTvSearch.setText(data.getStringExtra("searchStr"));
                if (mTvSearch.getText().length() > 0) {
                    mLlClear.setVisibility(View.VISIBLE);
                } else {
                    mLlClear.setVisibility(View.GONE);
                }
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
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

        list.clear();
        adapter.notifyDataSetChanged();
        page = 1;
        mFlag = 1;
        loadData();
    }
}
