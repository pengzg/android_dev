package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RolloutGoodsOrderListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.RolloutGoodsOrderBean;
import com.xdjd.distribution.bean.StatusBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.SelectOrderStatusPopup;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/22.
 */

public class RolloutGoodsOrderActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener, SelectOrderStatusPopup.ItemOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    PullToRefreshListView mPullList;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.ll_select_category)
    LinearLayout mLlSelectCategory;
    @BindView(R.id.tv_category)
    TextView mTvCategory;

    private int type;// 1:铺货单详情 2：铺货销售单详情 3：撤货单详情 4: 申报订单

    private int page = 1;
    private int mFlag = 0;

    RolloutGoodsOrderListAdapter adapter;
    private List<RolloutGoodsOrderBean> list = new ArrayList<>();

    private ClientBean mClientBean;
    private UserBean mUserBean;

    private TimePickerUtil mTimePickerUtil;
    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天
    private int dateType = 1;

    private SelectOrderStatusPopup popupOrderStatus;
    private List<StatusBean> listStatus = new ArrayList<>();
    //订单状态
    private int status = 0;//0:全部 1：已完成 2：未完成 3：已取消

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);

        type = getIntent().getIntExtra("type", 1);

        switch (type) {
            case 1:
                mTitleBar.setTitle("铺货单");
                mLlSelectCategory.setVisibility(View.VISIBLE);
                //                listStatus.add(new StatusBean(0, "全部"));
                //                listStatus.add(new StatusBean(1, "已完成"));
                //                listStatus.add(new StatusBean(2, "未完成"));

                //                订单状态1、已申报 4已配货 10  取消 12 完成
                listStatus.add(new StatusBean(0, "全部"));
                listStatus.add(new StatusBean(12, "已完成"));
                listStatus.add(new StatusBean(4, "已配货"));
                listStatus.add(new StatusBean(10, "已取消"));
                //                listStatus.add(new StatusBean(1, "已申报"));
                break;
            case 2:
                mTitleBar.setTitle("销售单");
                mLlSelectCategory.setVisibility(View.VISIBLE);
                listStatus.add(new StatusBean(0, "全部"));
                listStatus.add(new StatusBean(1, "已完成"));
                listStatus.add(new StatusBean(3, "已取消"));
                break;
            case 3:
                mTitleBar.setTitle("撤货单");
                mLlSelectCategory.setVisibility(View.GONE);
                break;
            case 4:
                mTitleBar.setTitle("铺货申报单");
                mLlSelectCategory.setVisibility(View.VISIBLE);
                listStatus.add(new StatusBean(0, "全部"));
                listStatus.add(new StatusBean(12, "已完成"));
                listStatus.add(new StatusBean(4, "已配货"));
                listStatus.add(new StatusBean(10, "已取消"));
                listStatus.add(new StatusBean(1, "已申报"));
                break;
        }

        mUserBean = UserInfoUtils.getUser(this);
        mClientBean = UserInfoUtils.getClientInfo(this);

        adapter = new RolloutGoodsOrderListAdapter(list, type);
        initRefresh(mPullList);
        mPullList.setMode(PullToRefreshBase.Mode.BOTH);
        mPullList.setAdapter(adapter);
        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryPhOrderDetailList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryPhOrderDetailList();
            }
        });

        mPullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i = i - 1;//刷新listview会添加头部,下标所以减1
                Intent intent = new Intent(RolloutGoodsOrderActivity.this, RolloutGoodsOrderDetailActivity.class);
                intent.putExtra("orderId", list.get(i).getOrder_id());
                intent.putExtra("type", type);
                intent.putExtra("bean", list.get(i));
                startActivity(intent);
            }
        });

        dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
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

                list.clear();
                adapter.notifyDataSetChanged();
                queryPhOrderDetailList();
            }
        }, this);

        mLlSelectCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlSelectCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopup();
            }
        });

        queryPhOrderDetailList();
    }

    private void queryPhOrderDetailList() {
        AsyncHttpUtil<RolloutGoodsOrderBean> httpUtil = new AsyncHttpUtil<>(this, RolloutGoodsOrderBean.class,
                new IUpdateUI<RolloutGoodsOrderBean>() {
                    @Override
                    public void updata(RolloutGoodsOrderBean jsonStr) {
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
                        mPullList.onRefreshComplete();
                    }
                });
        httpUtil.post(M_Url.queryPhOrderList, L_RequestParams.queryPhOrderList("", UserInfoUtils.getId(this),
                String.valueOf(page), String.valueOf(type), dateStartStr.replace(".", "-"),
                dateEndStr.replace(".", "-"), mTvSearch.getText().toString(),
                String.valueOf(status)), true);
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar, R.id.ll_clear, R.id.ll_search, R.id.ll_select_category})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
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
                queryPhOrderDetailList();
                break;
            case R.id.ll_select_category:
                showPopup();
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
        queryPhOrderDetailList();
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
                queryPhOrderDetailList();
                break;
        }
    }

    private void initPopup() {
        popupOrderStatus = new SelectOrderStatusPopup(this, mLlSelectCategory, this);
        popupOrderStatus.setData(listStatus);
    }

    private void showPopup() {
        popupOrderStatus.setData(listStatus);
        popupOrderStatus.showAsDropDown(mLlSelectCategory, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onItem(int i) {
        popupOrderStatus.dismiss();
        status = listStatus.get(i).getType();
        mTvCategory.setText(listStatus.get(i).getTypeName());
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryPhOrderDetailList();
    }
}
