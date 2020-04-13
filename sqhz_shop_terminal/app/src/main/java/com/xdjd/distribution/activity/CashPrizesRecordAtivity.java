package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
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
import com.xdjd.distribution.adapter.CashPrizesRecordAdapter;
import com.xdjd.distribution.adapter.WinningRecordAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.WinningListBean;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
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
 * 核销兑换奖品记录
 * Created by lijipei on 2017/11/1.
 */

public class CashPrizesRecordAtivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    PullToRefreshListView mPullScroll;
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
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private int page = 1;
    private int mFlag = 0;

    private CashPrizesRecordAdapter adapter;
    private List<WinningListBean> listWinning = new ArrayList();

    private TextView[] tvs;
    public int mIndex = 0;

    private String dateStartStr;
    private String dateEndStr;

    private String dateTypeStr;
    private int dateType = 1;

    private TimePickerUtil mTimePickerUtil;

    private int mwState = 1;//1未核销 2已核销

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    private String prizeid;//奖品id
    private String shopid;//店铺id

    @Override
    protected int getContentView() {
        return R.layout.activity_cash_prizes_record;
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
        mTitleBar.setRightImageResource(R.mipmap.search);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupSeaarh();
            }
        });

        prizeid = getIntent().getStringExtra("prizeid");
        shopid = getIntent().getStringExtra("shopid");

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;

                listWinning.clear();
                adapter.notifyDataSetChanged();
                loadData("");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                loadData("");
            }
        });

        tvs = new TextView[]{mTvNoCheck, mTvCheck};
        tvs[mIndex].setSelected(true);
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;

        adapter = new CashPrizesRecordAdapter(listWinning);
        mPullScroll.setAdapter(adapter);

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

        mTimePickerUtil = new TimePickerUtil();
        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {

                mTimePickerUtil.calendarPopup.dismiss();

                dateStartStr = startDate.replace("-", ".");
                dateEndStr = endDate.replace("-", ".");

                mTvDate.setText(dateStartStr + "-" + dateEndStr);
                mTvDateTypeSelect.setText("自定义");
                dateType = 4;

                page = 1;
                mFlag = 1;
                listWinning.clear();
                adapter.notifyDataSetChanged();
                loadData("");
            }
        }, this);
        loadData("");

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });
    }

    @OnClick({R.id.tv_no_check, R.id.tv_check, R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_no_check:
                mIndex = 0;
                mwState = 1;
                adapter.setMwState(mwState);
                selectTab();
                //                mTvTotalNum.setText("未核销商品数量:30箱");
                break;
            case R.id.tv_check:
                mIndex = 1;
                mwState = 2;
                adapter.setMwState(mwState);
                selectTab();
                //                mTvTotalNum.setText("已核销商品数量:30箱");
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
    private void loadData(String searchStr) {
        AsyncHttpUtil<WinningListBean> httpUtil = new AsyncHttpUtil<>(this, WinningListBean.class, new IUpdateUI<WinningListBean>() {
            @Override
            public void updata(WinningListBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getHxList() != null && jsonStr.getHxList().size() > 0) {
                        listWinning.addAll(jsonStr.getHxList());
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了!");
                        }
                    }

                    mTvTotalNum.setText(jsonStr.getNum());

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
        httpUtil.post(M_Url.queryHxList, L_RequestParams.queryHxList(dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"),
                "", String.valueOf(page), "10", String.valueOf(mwState),prizeid,mwState==1?"":shopid, searchStr), true);
    }

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this, mLlMain, new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                listWinning.clear();
                adapter.notifyDataSetChanged();
                loadData(searchStr);
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }

    private void selectTab() {
        for (int i = 0; i < tvs.length; i++) {
            if (i == mIndex) {
                tvs[i].setSelected(true);
            } else {
                tvs[i].setSelected(false);
            }
        }
        moveAnimation(mIndex);

        mFlag = 1;
        page = 1;
        listWinning.clear();
        adapter.notifyDataSetChanged();
        loadData("");
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
        listWinning.clear();
        adapter.notifyDataSetChanged();
        loadData("");
    }
}
