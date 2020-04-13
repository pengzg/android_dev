package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OutboundQueryAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.StockOutBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.OutboundCancelEvent;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.SearchPopup;
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
import com.xdjd.view.NoScrollListView;

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
 *     time   : 2017/5/22
 *     desc   : 出库查询
 *     version: 1.0
 * </pre>
 */

public class OutboundQueryActivity extends BaseActivity implements ItemOnListener, CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.tv_sale)
    TextView mTvSale;
    @BindView(R.id.tv_process_sheet_form)
    TextView mTvProcessSheetForm;
    @BindView(R.id.tv_refund_form)
    TextView mTvRefundForm;
    @BindView(R.id.tv_exchange_form)
    TextView mTvExchangeForm;
    @BindView(R.id.tv_enquiry)
    TextView mTvEnquiry;
    @BindView(R.id.tv_refund_apply)
    TextView mTvRefundApply;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.tv_give_back_form)
    TextView mTvGiveBackForm;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    public int indexOrder = 0;

    private String dateStartStr;
    private String dateEndStr;

    private Date date = new Date();

    private OutboundQueryAdapter adapter;

    private UserBean userBean;

    //单据类型 101 进货 102期初入库 103其它入库 104 进货退货 105 换货入库
    // 106 移库入库  201 销售出库 202 还货出库 203处理出库 204 其它出库 301调拨
    // 302要货审核 303退货审核 206换货 305 撤销配货 306 拆装出库 501 报废出库 601 退货入库
    private String indexType = "201";//默认销售

    private List<StockOutBean> list = new ArrayList<>();

    /**
     * 客户id
     */
    private String clientId;

    private int page = 1;
    private int mFlag = 0;

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    private TimePickerUtil mTimePickerUtil;

    /**
     * 今日、本周、本月、上月--格式popup
     */
    private DateOnePopup dateOnePopup;
    private int dateType = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_outbound_query;
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mTimePickerUtil = new TimePickerUtil();

        mTitle.setText("出库查询");
        mRlRightSearch.setVisibility(View.VISIBLE);

        userBean = UserInfoUtils.getUser(this);

        ClientBean clientBean = UserInfoUtils.getClientInfo(this);
        if (clientBean == null || "".equals(clientBean.getCc_id())) {
            clientId = "";
        } else {
            clientId = clientBean.getCc_id();
        }

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 6;

        dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        adapter = new OutboundQueryAdapter(this);
        mLvPull.setAdapter(adapter);

        initRefresh(mLvPull);
        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);
        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                mFlag = 1;
                loadData("");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                loadData("");
            }
        });

        loadData("");

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData("");
            }
        }, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_layout, R.id.rl_right_search, R.id.tv_sale, R.id.tv_process_sheet_form,
            R.id.tv_refund_form, R.id.tv_exchange_form, R.id.tv_enquiry, R.id.tv_refund_apply,
            R.id.tv_date_type_select, R.id.ll_date_calendar,R.id.tv_give_back_form})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout://退出
                finish();
                break;
            case R.id.rl_right_search://搜索
                showPopupSeaarh();
                break;
            case R.id.tv_sale:
                indexOrder = 0;
                indexType = "201";//销售
                selectTab();
                break;
            case R.id.tv_process_sheet_form:
                indexOrder = 2;
                indexType = "203";//处理
                selectTab();
                break;
            case R.id.tv_give_back_form://还货
                indexOrder = 2;
                indexType = "202";//还货
                selectTab();
                break;
            case R.id.tv_refund_form:
                indexOrder = 3;
                indexType = "601";//车销里的退货
                selectTab();
                break;
            case R.id.tv_exchange_form:
                indexOrder = 4;
                indexType = "206";//换货
                selectTab();
                break;
            case R.id.tv_enquiry:
                indexOrder = 5;
                indexType = "302";//要货申请
                selectTab();
                break;
            case R.id.tv_refund_apply:
                indexOrder = 6;
                indexType = "303";//退货申请
                selectTab();
                break;
            case R.id.ll_date_calendar://日期区间选择
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
        }
    }

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this, mLlMain, new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                loadData(searchStr);
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }

    private void loadData(String searchStr) {
        AsyncHttpUtil<StockOutBean> httpUtil = new AsyncHttpUtil<>(this, StockOutBean.class, new IUpdateUI<StockOutBean>() {
            @Override
            public void updata(StockOutBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        list.addAll(jsonBean.getDataList());
                        adapter.setData(list);
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                        } else {
                            page--;
                            showToast("没有更多数据了");
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
                mLvPull.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getStockOutList, L_RequestParams.getStockOutList(userBean.getUserId(),
                clientId, indexType, String.valueOf(page), dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"), searchStr), true);
    }

    @Override
    public void onItem(int position) {
        Intent intent = new Intent(this, OutboundDetailActivity.class);
        switch (indexOrder) {
            case 0:
                intent.putExtra("orderType", "销售单");
                break;
            case 2:
                intent.putExtra("orderType", "还货单");
                break;
            case 3:
                intent.putExtra("orderType", "退货单");
                break;
            case 4:
                intent.putExtra("orderType", "换货单");
                break;
            case 5:
                intent.putExtra("orderType", "要货申请单");
                break;
            case 6:
                intent.putExtra("orderType", "退货申请单");
                break;
        }
        intent.putExtra("om_id", list.get(position).getEim_id());
        intent.putExtra("orderAmount", list.get(position).getEim_totalamount());
        intent.putExtra("bean", list.get(position));
        intent.putExtra("indexType", indexType);
        startActivity(intent);
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (indexOrder) {
            case 0:
                mTvSale.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvSale.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(0);
                //                mTvSelectedNum.setText("已选(" + context.listGoodsOrder.size() + "件)");
                //                amountMoney(context.listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(1);
                //                mTvSelectedNum.setText("已选(" + context.listProcessOrder.size() + "件)");
                //                amountMoney(context.listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(2);
                //                mTvSelectedNum.setText("已选(" + context.listRefundOrder.size() + "件)");
                //                amountMoney(context.listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(3);
                //                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                //                amountMoney(context.listExchangeOrder);
                break;
            case 5:
                mTvEnquiry.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvEnquiry.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(4);
                //                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                //                amountMoney(context.listExchangeOrder);
                break;
            case 6:
                mTvRefundApply.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvRefundApply.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(5);
                //                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                //                amountMoney(context.listExchangeOrder);
                break;
        }

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.setIndexType(indexType);
        adapter.notifyDataSetChanged();
        loadData("");
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvSale.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvSale.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvEnquiry.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvEnquiry.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvRefundApply.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundApply.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 6
                                * index).setDuration(200).start();
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
        loadData("");
    }

    public void onEventMainThread(OutboundCancelEvent event) {
        page = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        mFlag = 1;
        loadData("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
