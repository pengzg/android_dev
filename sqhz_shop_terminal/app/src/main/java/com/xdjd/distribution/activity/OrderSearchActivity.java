package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OrderSearchAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.OrderStatsBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.OrderCancelEvent;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.OrderStatePopup;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class OrderSearchActivity extends BaseActivity implements ItemOnListener, CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.activity_order_search)
    LinearLayout mActivityOrderSearch;
    @BindView(R.id.lv_order)
    PullToRefreshListView mLvOrder;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.tv_order_state)
    TextView mTvOrderState;
    @BindView(R.id.rl_order_state)
    RelativeLayout mRlOrderState;
    //    @BindView(R.id.pull_scroll)
    //    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.ll_x)
    LinearLayout mLlX;
    @BindView(R.id.ll_shop)
    LinearLayout mLlShop;

    private UserBean userBean;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    //客户id
    private String customerId = "";

    private OrderSearchAdapter adapter;
    List<OrderBean> list = new ArrayList<>();

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    private Date date = new Date();

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    /**
     * 订单状态id
     */
    private String orderStateId = "";

    private List<OrderStatsBean> listOrderState;
    /**
     * 订单状态弹框
     */
    private OrderStatePopup popupOrderState;

    private int page = 1;
    private int mFlag = 0;

    private TimePickerUtil mTimePickerUtil;

    private int dateType = 1;
    //是否从首页过来,1是首页
    private int isFromMain;

    @Override
    protected int getContentView() {
        return R.layout.activity_order_search;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("订单查询");
        mRlRightSearch.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);

        mTimePickerUtil = new TimePickerUtil();

        userBean = UserInfoUtils.getUser(this);

        //初始化数据
        clientBean = UserInfoUtils.getClientInfo(this);
        if (clientBean != null) {
            customerId = clientBean.getCc_id();
            mLlShop.setVisibility(View.VISIBLE);
            mTvShopName.setText(clientBean.getCc_name());
        }

        isFromMain = getIntent().getIntExtra("isFromMain", 0);
        if (isFromMain == 1) {//如果从首页应收简报过来,隐藏订单状态选择栏
            mRlOrderState.setVisibility(View.GONE);
        } else {
            mRlOrderState.setVisibility(View.VISIBLE);
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

        adapter = new OrderSearchAdapter(this);
        mLvOrder.setAdapter(adapter);
        adapter.setData(list);

        initRefresh(mLvOrder);
        mLvOrder.setMode(PullToRefreshBase.Mode.BOTH);

        queryOrderGrid("");

        mActivityOrderSearch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mActivityOrderSearch.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });

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

        mLvOrder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderGrid("");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                page++;
                queryOrderGrid("");
            }
        });

        initPopupOrderState();//初始化订单类型popup
    }

    @OnClick({R.id.left_layout, R.id.rl_right_search, R.id.ll_date_calendar, R.id.tv_date_type_select, R.id.rl_order_state, R.id.ll_x})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.rl_right_search://搜索
                showPopupSeaarh();
                break;
            case R.id.ll_date_calendar://日期区间选择
                mTimePickerUtil.showTimePicker(mActivityOrderSearch, dateStartStr, dateEndStr, true);
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mActivityOrderSearch, dateStartStr, dateEndStr, true);
                break;
            case R.id.rl_order_state://选择订单类型
                if (listOrderState == null || listOrderState.size() == 0) {
                    getOrderStatsList();
                } else {
                    showPopupOrderState();
                }
                break;
            case R.id.ll_x://删除签到店铺
                clientBean = null;
                customerId = "";
                mLlShop.setVisibility(View.GONE);

                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderGrid("");
                break;
        }
    }

    /**
     * 初始化订单类型popup
     */
    private void initPopupOrderState() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupOrderState = new OrderStatePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                orderStateId = listOrderState.get(position).getBd_code();
                mTvOrderState.setText(listOrderState.get(position).getBd_name());
                popupOrderState.dismiss();

                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderGrid("");
            }
        });
    }

    private void showPopupOrderState() {
        popupOrderState.setData(listOrderState);
        popupOrderState.setId(orderStateId);
        // 显示窗口
        popupOrderState.showAtLocation(mActivityOrderSearch,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    /**
     * 订单类型查询
     */
    private void getOrderStatsList() {
        AsyncHttpUtil<OrderStatsBean> httpUtil = new AsyncHttpUtil<>(this, OrderStatsBean.class, new IUpdateUI<OrderStatsBean>() {
            @Override
            public void updata(OrderStatsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listOrderState = jsonBean.getDataList();
                    if (listOrderState != null && listOrderState.size() > 0) {
                        showPopupOrderState();
                    } else {
                        showToast("没有订单类型!");
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
                //                mScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getOrderStatsList,
                L_RequestParams.getOrderStatsList(), true);
    }

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this, mActivityOrderSearch, new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderGrid(searchStr);
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mActivityOrderSearch, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }

    /**
     * 查询订单列表
     */
    private void queryOrderGrid(String searchStr) {
        AsyncHttpUtil<OrderBean> httpUtil = new AsyncHttpUtil<>(this, OrderBean.class, new IUpdateUI<OrderBean>() {
            @Override
            public void updata(OrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了!");
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
                mLvOrder.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.dataOrderGrid, L_RequestParams.queryOrderGrid(customerId, dateStartStr.replace(".", "-"),
                dateEndStr.replace(".", "-"), "1", searchStr, orderStateId, String.valueOf(page), String.valueOf(isFromMain)
        ), true);
    }

    @Override
    public void onItem(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderType", list.get(position).getOm_ordertype_nameref());
        intent.putExtra("om_id", list.get(position).getOm_id());
        intent.putExtra("bean", list.get(position));
        startActivity(intent);
    }

    /**
     * 计算选中商品列表的价格
     */
    private void amountMoney(List<OrderBean> list) {
        if (list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (OrderBean bean : list) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
            mTvAmount.setText(sum.setScale(2).doubleValue() + "");
        } else {
            mTvAmount.setText("0.00");
        }
    }

    public void onEventMainThread(OrderCancelEvent event) {
        mFlag = 1;
        page = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryOrderGrid("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

}
