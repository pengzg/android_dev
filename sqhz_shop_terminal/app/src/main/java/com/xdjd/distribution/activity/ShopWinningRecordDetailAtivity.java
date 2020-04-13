package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.WinningRecordAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/11/1.
 */

public class ShopWinningRecordDetailAtivity extends BaseActivity  implements CalendarSelectPopup.ItemDateOnListener{
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.tv_no_check)
    TextView mTvNoCheck;
    @BindView(R.id.tv_check)
    TextView mTvCheck;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;

    private int page = 1;
    private int mFlag = 0;

    private WinningRecordAdapter adapter;

    private TextView[] tvs;
    public int mIndex = 0;

    private String dateStartStr;
    private String dateEndStr;

    private String dateTypeStr;
    private int dateType = 1;

    private TimePickerUtil mTimePickerUtil;

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_winning_record_detail;
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
        mTitleBar.setTitle(getIntent().getStringExtra("title"));

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;

                //                listCustomerWinning.clear();
                //                adapterShop.notifyDataSetChanged();
                //                getCustomerWinningList();
                mPullScroll.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;

                //                getCustomerWinningList();
                mPullScroll.onRefreshComplete();
            }
        });

        tvs = new TextView[]{mTvNoCheck, mTvCheck};
        tvs[mIndex].setSelected(true);
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;

        adapter = new WinningRecordAdapter(null);
        mLvNoScroll.setAdapter(adapter);

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
//                dateType = 4;

                page = 1;
                mFlag = 1;
//                list.clear();
                adapter.notifyDataSetChanged();
            }
        },this);
    }

    @OnClick({R.id.tv_no_check, R.id.tv_check,R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_no_check:
                mIndex = 0;
                selectTab();
                mTvTotalNum.setText("未核销商品数量:30箱");
                break;
            case R.id.tv_check:
                mIndex = 1;
                selectTab();
                mTvTotalNum.setText("已核销商品数量:30箱");
                break;
            case R.id.tv_date_type_select:
                break;
            case R.id.ll_date_calendar:
                break;
        }
    }

    private void selectTab() {
        mLvNoScroll.setAdapter(adapter);

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

        page = 1;
        mFlag = 1;
//        list.clear();
        adapter.notifyDataSetChanged();
//        loadData();
    }
}
