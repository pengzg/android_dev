package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.AuditPrizeAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ShopAuditBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.ShopAuditEvent;
import com.xdjd.distribution.popup.CalendarSelectPopup;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2017/11/1.
 * 店铺结算汇总列表
 */

public class ShopAuditActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener,ItemOnListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.pull_scroll)
    PullToRefreshListView mPullScroll;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private AuditPrizeAdapter adapterShop;

    private int page = 1;
    private int mFlag = 0;

    List<ShopAuditBean> listShopGoodsAudit;//客户兑奖列表

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    private int dateType = 1;

    private TimePickerUtil mTimePickerUtil;

    private ClientBean mClientBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_audit;
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
        mTitleBar.setTitle("店铺结算");

        EventBus.getDefault().register(this);

        mClientBean = UserInfoUtils.getClientInfo(this);

        mTimePickerUtil = new TimePickerUtil();

        mTvDateTypeSelect.setText("本月");
        //将外面选择的日期带到这个界面中
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


                listShopGoodsAudit.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();

            }
        }, this);

        listShopGoodsAudit = new ArrayList<>();

        adapterShop = new AuditPrizeAdapter(this);
        mPullScroll.setAdapter(adapterShop);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;

                listShopGoodsAudit.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
               /* page++;
                mFlag = 2;

                getCustomerWinningList();
                mPullScroll.onRefreshComplete();*/

            }
        });

        getCustomerWinningList();
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
        }
    }

    public void onEventMainThread(ShopAuditEvent event) {
        mFlag = 1;
        page = 1;

        listShopGoodsAudit.clear();
        adapterShop.notifyDataSetChanged();
        getCustomerWinningList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取已兑奖客户列表
     */
    private void getCustomerWinningList() {
        AsyncHttpUtil<ShopAuditBean> httpUtil = new AsyncHttpUtil<>(this, ShopAuditBean.class, new IUpdateUI<ShopAuditBean>() {
            @Override
            public void updata(ShopAuditBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    //                    mTvTotalNum.setText("已兑奖总数:" + jsonStr.getTotal_nums());
                    if (jsonStr.getWinningList() != null && jsonStr.getWinningList().size() > 0) {
                        listShopGoodsAudit.addAll(jsonStr.getWinningList());
                        adapterShop.setData(listShopGoodsAudit);
                    } else {
                        showToast("暂无数据!");
                    }

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
        httpUtil.post(M_Url.queryJsSum, L_RequestParams.queryJsSum(dateStartStr.replace(".", "-"),
                dateEndStr.replace(".", "-"), mClientBean.getCc_id()), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        listShopGoodsAudit.clear();
        adapterShop.notifyDataSetChanged();
        getCustomerWinningList();
    }

    @Override
    public void onItem(int position) {
        Intent intent = new Intent(ShopAuditActivity.this, ShopAuditDetailActivity.class);
        intent.putExtra("dateStartStr", dateStartStr);
        intent.putExtra("dateEndStr", dateEndStr);
        intent.putExtra("shopId", listShopGoodsAudit.get(position).getShopid());
        intent.putExtra("prizeId",listShopGoodsAudit.get(position).getMw_prizeid());
        intent.putExtra("goodsName",listShopGoodsAudit.get(position).getProductname());
        startActivity(intent);
    }
}
