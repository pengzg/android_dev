package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.ReceivableDetailAdapter;
import com.xdjd.distribution.adapter.ReceivableListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
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

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ReceivableDetailActivity extends BaseActivity implements ItemOnListener,CalendarSelectPopup.ItemDateOnListener {
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
    @BindView(R.id.lv_list)
    NoScrollListView mLvList;
    @BindView(R.id.lv_pull)
    PullToRefreshScrollView mLvPull;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    //是否是开始或结束时间
    private boolean isStart;

    private ReceivableDetailAdapter adapter;
    private List<ReceivableListBean> list = new ArrayList<>();

    private String dateStartStr;
    private String dateEndStr;

    private String dateTypeStr;

    /**
     * 客户id
     */
    private String clientId = "";

    private Date date = new Date();

    private UserBean userBean;

    private TimePickerUtil mTimePickerUtil;

    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;
    private int dateType = 1;

    private int page = 1;
    private int mFlag = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_receivable_detail;
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
        mTitleBar.setTitle("应收款明细");

        clientId = getIntent().getStringExtra("clientId");
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");

        mTvDateTypeSelect.setText(dateTypeStr);

        if (dateStartStr == null || dateStartStr.length() == 0){
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            mTvDateTypeSelect.setText("今天");
        }

        mTimePickerUtil = new TimePickerUtil();
        userBean = UserInfoUtils.getUser(this);

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        adapter = new ReceivableDetailAdapter(this);
        mLvList.setAdapter(adapter);
        adapter.setData(list);

        initRefresh(mLvPull);
        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);
        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                mFlag = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {

                mTimePickerUtil.calendarPopup.dismiss();

                dateStartStr = startDate.replace("-", ".");
                dateEndStr = endDate.replace("-", ".");

                mTvDate.setText(dateStartStr + "-" + dateEndStr);
                mTvDateTypeSelect.setText("自定义");
                dateType = 4;

                list.clear();
                page =1;
                mFlag = 1;
                adapter.notifyDataSetChanged();
                loadData();
            }
        },this);

        loadData();
    }

    @OnClick({ R.id.ll_date_calendar, R.id.tv_date_type_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_date_calendar://日期区间选择
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr, dateEndStr,true);
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain,dateStartStr, dateEndStr,true);
                break;
        }
    }

    private void loadData() {
        AsyncHttpUtil<ReceivableListBean> httpUtil = new AsyncHttpUtil<>(this, ReceivableListBean.class, new IUpdateUI<ReceivableListBean>() {
            @Override
            public void updata(ReceivableListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                    } else {
                        if (mFlag == 2){
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }else{
                            showToast("暂无数据!");
                        }
                    }
                    adapter.notifyDataSetChanged();
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
                mLvPull.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getGlReceivableList, L_RequestParams.getGlReceivableList("10", String.valueOf(page), clientId,
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-")), true);
    }

    @Override
    public void onItem(int position) {
        /*Intent intent = new Intent(this, ReceiptPaymentDetailActivity.class);
        intent.putExtra("customerId", list.get(position).getGc_customerid());
        intent.putExtra("customerName", list.get(position).getGc_customerid_nameref());
        intent.putExtra("dateStartStr", dateStartStr);
        intent.putExtra("dateEndStr", dateEndStr);
        intent.putExtra("amount", list.get(position).getGcrr_total_amount());
        intent.putExtra("bean", list.get(position));
        startActivity(intent);*/
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
        page =1;
        mFlag = 1;
        adapter.notifyDataSetChanged();
        loadData();
    }
}
