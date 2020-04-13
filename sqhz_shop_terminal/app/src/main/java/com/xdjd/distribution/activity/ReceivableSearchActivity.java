package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.adapter.RecyclerReceivableListAdapter;
import com.xdjd.steward.listener.AnimListenerBuilder;
import com.xdjd.steward.popup.DateOnePopup;
import com.xdjd.utils.AnimUtils;
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

public class ReceivableSearchActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
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
    @BindView(R.id.tv_ws_amount)
    TextView mTvWsAmount;
    @BindView(R.id.tv_yh_amount)
    TextView mTvYhAmount;
    @BindView(R.id.tv_sj_amount)
    TextView mTvSjAmount;
    @BindView(R.id.tv_total_amount)
    TextView mTvTotalAmount;
    @BindView(R.id.ll_footer)
    LinearLayout mLlFooter;
    @BindView(R.id.rv_recycler)
    RecyclerView mRvRecycler;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.tv_page_count)
    TextView mTvPageCount;
    @BindView(R.id.tv_customer_total_num)
    TextView mTvCustomerTotalNum;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    //是否是开始或结束时间
    private boolean isStart;

    private RecyclerReceivableListAdapter adapter;
    private List<ReceivableListBean> list = new ArrayList<>();

    private String dateStartStr;
    private String dateEndStr;

    /**
     * 客户id
     */
    private String clientId;

    private Date date = new Date();

    private UserBean userBean;

    private TimePickerUtil mTimePickerUtil;

    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;
    private int dateType = 1;

    private int page = 1;

    private int PAGE_PER = 1;//总共多少页
    private int PAGE_SIZE = 20;//每页的数量
    private int COUNT = 0;//总的条数,默认是0

    @Override
    protected int getContentView() {
        return R.layout.activity_receivable_search;
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
        mTitleBar.setTitle("应收款查询");

        ClientBean clientBean = UserInfoUtils.getClientInfo(this);
        if (clientBean == null || "".equals(clientBean.getCc_id())) {
            clientId = "";
        } else {
            clientId = clientBean.getCc_id();
        }

        mTimePickerUtil = new TimePickerUtil();
        userBean = UserInfoUtils.getUser(this);

        //默认本月时间
        mTvDateTypeSelect.setText("本月");
        dateStartStr = DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
        dateEndStr = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);

        mTvDate.setText(dateStartStr + "-" + dateEndStr);
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
                page = 1;
                adapter.notifyDataSetChanged();
                loadData();
            }
        }, this);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(UIUtils.getColor(R.color.color_blue));

        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerReceivableListAdapter(list);
        adapter.setOnLoadMoreListener(this, mRvRecycler);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRvRecycler.setAdapter(adapter);

        final AnimListenerBuilder builder = new AnimListenerBuilder();

        mRvRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int page;

                    if (((LinearLayoutManager) layoutManager).getChildCount() == 0) {
                        page = 0;
                        PAGE_PER = 0;
                    } else {
                        if (lastVisiblePosition % PAGE_SIZE == 0) {
                            page = lastVisiblePosition / PAGE_SIZE;
                        } else {
                            page = lastVisiblePosition / PAGE_SIZE + 1;
                        }
                    }

                    mTvPageCount.setText(page + "/" + PAGE_PER);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                builder.setNewState(newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && builder.isAnimFinish()) {
                    //如果是IDLE状态，并且显示动画执行完毕，再执行隐藏动画，避免出现动画闪烁
                    //如果快速简短滑动，可能导致出现IDLE状态，但是动画未执行完成。因此无法执行隐藏动画。所以保存当前newState，在onAnimationEnd中增加判断。
                    AnimUtils.hide(mTvPageCount);
                } else if (mTvPageCount.getVisibility() != View.VISIBLE) {
                    AnimUtils.show(mTvPageCount, builder.build());
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ReceivableSearchActivity.this, ReceivableDetailActivity.class);
                intent.putExtra("clientId", list.get(position).getGr_customerid());
                intent.putExtra("dateStartStr", dateStartStr);
                intent.putExtra("dateEndStr", dateEndStr);
                intent.putExtra("dateTypeStr", mTvDateTypeSelect.getText().toString());
                startActivity(intent);
            }
        });

        loadData();
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

    private void loadData() {
        AsyncHttpUtil<ReceivableListBean> httpUtil = new AsyncHttpUtil<>(this, ReceivableListBean.class, new IUpdateUI<ReceivableListBean>() {
            @Override
            public void updata(ReceivableListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mTvWsAmount.setText(jsonBean.getTotalWsAmount());
                    mTvYhAmount.setText(jsonBean.getTotalDiscountAmount());
                    mTvSjAmount.setText(jsonBean.getTotalTradeAmount());
                    mTvTotalAmount.setText(jsonBean.getTotalAmount());

                    COUNT = jsonBean.getTotal();
                    if (COUNT % PAGE_SIZE == 0) {
                        PAGE_PER = COUNT / PAGE_SIZE;
                    } else {
                        PAGE_PER = COUNT / PAGE_SIZE + 1;
                    }

                    mTvCustomerTotalNum.setText(""+jsonBean.getTotal());
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        adapter.addData(jsonBean.getListData());
                        adapter.loadMoreComplete();
                    } else {
                        page--;
                        if (page == 0) {
                            page = 1;
                            adapter.getData().clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            adapter.loadMoreEnd(false);
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
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(true);
                adapter.setEnableLoadMore(true);
            }
        });
        httpUtil.post(M_Url.getReceivableSumList, L_RequestParams.getReceivableSumList(String.valueOf(PAGE_SIZE), String.valueOf(page),
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-")), true);
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
        page = 1;
        adapter.notifyDataSetChanged();
        loadData();
    }

    @Override
    public void onRefresh() {
        // 下拉刷新
        mSwipeLayout.setEnabled(false);
        adapter.setEnableLoadMore(false);
        adapter.getData().clear();
        list.clear();
        page = 1;
        adapter.notifyDataSetChanged();
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeLayout.setEnabled(false);
        if (adapter.getData().size() < PAGE_SIZE) {
            //第一页数据就小于pageSize的时候
            adapter.loadMoreEnd(false);
            mSwipeLayout.setEnabled(true);
        } else {
            page++;
            loadData();
        }
    }
}
