package com.xdjd.steward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.CashPrizesRecordAtivity;
import com.xdjd.distribution.adapter.ShopWinningRecordAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.bean.ShopWinningDetailBean;
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
 * Created by lijipei on 2017/11/1.
 * 查询商品下的所有店铺核销详情
 */

public class GoodsUpShopWinningActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private ShopWinningRecordAdapter adapterShop;

    private int page = 1;
    private int mFlag = 0;

    List<ShopWinningDetailBean> listWinning;//客户兑奖列表

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    private int dateType = 1;

    private TimePickerUtil mTimePickerUtil;

    @Override
    protected int getContentView() {
        return R.layout.activity_goods_up_shop_winning;
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
        mTitleBar.setTitle("商品核销汇总");

        mTimePickerUtil = new TimePickerUtil();

        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");

        if (dateStartStr == null || dateStartStr.length() == 0){
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        }else{
            mTvDateTypeSelect.setText(dateTypeStr);
        }
        mTvDate.setText(dateStartStr + "-" + dateEndStr);
        mTvDateTypeSelect.setText("本月");

        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                mTimePickerUtil.calendarPopup.dismiss();

                dateStartStr = startDate.replace("-", ".");
                dateEndStr = endDate.replace("-", ".");

                mTvDate.setText(dateStartStr + "-" + dateEndStr);
                mTvDateTypeSelect.setText("自定义");
                dateType = 4;

                listWinning.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();
            }
        }, this);

        listWinning = new ArrayList<>();

        adapterShop = new ShopWinningRecordAdapter();
        mLvNoScroll.setAdapter(adapterShop);
        adapterShop.setData(listWinning);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;

                listWinning.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;

                getCustomerWinningList();
            }
        });

        mLvNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoodsUpShopWinningActivity.this, CashPrizesRecordAtivity.class);
                intent.putExtra("title", "核销详情");
                intent.putExtra("prizeid", listWinning.get(position).getMw_prizeid());
                intent.putExtra("shopid", listWinning.get(position).getShopid());
                intent.putExtra("dateStartStr", dateStartStr);
                intent.putExtra("dateEndStr", dateEndStr);
                intent.putExtra("dateTypeStr", mTvDateTypeSelect.getText().toString());
                startActivity(intent);
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

    /**
     * 获取店铺核销汇总
     */
    private void getCustomerWinningList() {
        AsyncHttpUtil<ShopWinningDetailBean> httpUtil = new AsyncHttpUtil<>(this, ShopWinningDetailBean.class, new IUpdateUI<ShopWinningDetailBean>() {
            @Override
            public void updata(ShopWinningDetailBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    //                    mTvTotalNum.setText("总数量:" + jsonStr.getTotal_nums());
                    if (jsonStr.getHxList() != null && jsonStr.getHxList().size() > 0) {
                        listWinning.addAll(jsonStr.getHxList());
                        adapterShop.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了!");
                        }
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
        httpUtil.post(M_Url.queryHxSum, G_RequestParams.queryHxSum(dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"),
                String.valueOf(page), "", "1"), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        listWinning.clear();
        adapterShop.notifyDataSetChanged();
        getCustomerWinningList();
    }

}
