package com.xdjd.storebox.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.PrizeAdapter;
import com.xdjd.storebox.adapter.WinningRecordAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.CustomerWinningBean;
import com.xdjd.storebox.bean.GiftWinningBean;
import com.xdjd.storebox.popup.CalendarSelectPopup;
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

/**
 * Created by lijipei on 2017/10/28.
 * 中奖记录核销记录
 */

public class WinningRecordActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener{
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_prize)
    TextView mTvPrize;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private TextView[] tvs;
    public int mIndex = 0;

    private WinningRecordAdapter adapter;
    private PrizeAdapter adapterPrize;

    private int page = 1;
    private int mFlag = 0;

    List<CustomerWinningBean> listCustomerWinning;//客户兑奖列表
    List<GiftWinningBean> listGiftWinning;//奖品列表

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    private int dateType = 1;

    private TimePickerUtil mTimePickerUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_record);
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("核销记录");

        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;
        mTimePickerUtil = new TimePickerUtil();

        //将外面选择的日期带到这个界面中
        dateStartStr = DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
        dateEndStr = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

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

                if (mIndex == 0) {
                    listCustomerWinning.clear();
                    adapter.notifyDataSetChanged();
                    getCustomerWinningList();
                } else {
                    listGiftWinning.clear();
                    adapterPrize.notifyDataSetChanged();
                    getGiftWinningList();
                }
            }
        },this);

        initData();
        getCustomerWinningList();
    }

    protected void initData() {
        tvs = new TextView[]{mTvComplete, mTvPrize};
        tvs[mIndex].setSelected(true);

        listCustomerWinning = new ArrayList<>();
        listGiftWinning = new ArrayList<>();

        adapter = new WinningRecordAdapter(listCustomerWinning);
        mLvNoScroll.setAdapter(adapter);

        adapterPrize = new PrizeAdapter(listGiftWinning);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                if (mIndex == 0) {
                    listCustomerWinning.clear();
                    adapter.notifyDataSetChanged();
                    getCustomerWinningList();
                } else {
                    listGiftWinning.clear();
                    adapterPrize.notifyDataSetChanged();
                    getGiftWinningList();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                if (mIndex == 0) {
                    getCustomerWinningList();
                } else {
                    getGiftWinningList();
                }
            }
        });

        mLvNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mIndex == 1) {
                    Intent intent = new Intent(WinningRecordActivity.this, PrizeRecordActivity.class);
                    intent.putExtra("dateStartStr", dateStartStr);
                    intent.putExtra("dateEndStr", dateEndStr);
                    intent.putExtra("prizeid", listGiftWinning.get(position).getMw_prizeid());
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.tv_complete, R.id.tv_prize, R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_complete:
                mIndex = 0;
                selectTab();
                mLlBottom.setVisibility(View.VISIBLE);
                mTvTotalNum.setText("已兑奖总数:0");

                mFlag = 1;
                page = 1;
                listCustomerWinning.clear();
                adapter.notifyDataSetChanged();
                getCustomerWinningList();
                break;
            case R.id.tv_prize://奖品
                mIndex = 1;
                selectTab();
                mLlBottom.setVisibility(View.GONE);

                mFlag = 1;
                page = 1;
                listGiftWinning.clear();
                adapterPrize.notifyDataSetChanged();
                getGiftWinningList();
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
        }
    }

    /**
     * 获取已兑奖客户列表
     */
    private void getCustomerWinningList() {
        AsyncHttpUtil<CustomerWinningBean> httpUtil = new AsyncHttpUtil<>(this, CustomerWinningBean.class, new IUpdateUI<CustomerWinningBean>() {
            @Override
            public void updata(CustomerWinningBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    mTvTotalNum.setText("已兑奖总数:" + jsonStr.getTotal_nums());
                    listCustomerWinning.addAll(jsonStr.getListData());
                } else {
                    if (mFlag == 2) {
                        page--;
                        showToast("没有更多数据了!");
                    }
                }
                adapter.notifyDataSetChanged();
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
        httpUtil.post(M_Url.getWinningList, L_RequestParams.getWinningList(UserInfoUtils.getId(this),
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"), String.valueOf(page), "", ""), true);
    }

    /**
     * 获取奖品列表
     */
    private void getGiftWinningList() {
        AsyncHttpUtil<GiftWinningBean> httpUtil = new AsyncHttpUtil<>(this, GiftWinningBean.class, new IUpdateUI<GiftWinningBean>() {
            @Override
            public void updata(GiftWinningBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    listGiftWinning.addAll(jsonStr.getListData());
                } else {
                    if (mFlag == 2) {
                        page--;
                        showToast("没有更多数据了!");
                    }
                }
                adapterPrize.notifyDataSetChanged();
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
        httpUtil.post(M_Url.getGiftWinningList, L_RequestParams.getGiftWinningList(UserInfoUtils.getId(this),
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"), String.valueOf(page)), true);
    }

    private void selectTab() {
        if (mIndex == 1) {//奖品
            mLvNoScroll.setAdapter(adapterPrize);
        } else {
            mLvNoScroll.setAdapter(adapter);
        }

        for (int i = 0; i < tvs.length; i++) {
            if (i == mIndex) {
                tvs[i].setSelected(true);
            } else {
                tvs[i].setSelected(false);
            }
        }
        moveAnimation(mIndex);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 2
                                * index).setDuration(300).start();
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        if (mIndex == 0) {
            listCustomerWinning.clear();
            adapter.notifyDataSetChanged();
            getCustomerWinningList();
        } else {
            listGiftWinning.clear();
            adapterPrize.notifyDataSetChanged();
            getGiftWinningList();
        }
    }
}
