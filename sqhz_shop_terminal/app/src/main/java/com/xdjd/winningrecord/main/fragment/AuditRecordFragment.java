package com.xdjd.winningrecord.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.CashPrizesRecordAtivity;
import com.xdjd.distribution.activity.ShopWinningRecordActivity;
import com.xdjd.distribution.activity.ShopWinningRecordDetailAtivity;
import com.xdjd.distribution.adapter.ShopWinningRecordAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.steward.activity.GoodsUpShopWinningActivity;
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
import butterknife.Unbinder;

/**
 * Created by lijipei on 2017/11/2.
 * 核销界面
 */

public class AuditRecordFragment extends BaseFragment  implements CalendarSelectPopup.ItemDateOnListener{

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
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_shop)
    TextView mTvShop;
    @BindView(R.id.tv_goods)
    TextView mTvGoods;

    private ShopWinningRecordAdapter adapterShop;

    private int page = 1;
    private int mFlag = 0;

    List<ShopWinningDetailBean> listWinning;//客户兑奖列表

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天

    private int dateType = 1;

    private TimePickerUtil mTimePickerUtil;

    private int mWinningType = 1;//店铺核销查询类型,1:店铺,2:商品

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_audit_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

        mTimePickerUtil = new TimePickerUtil();
        mEtSearch.setHint("按店铺名称查询");

        //默认本月
        dateStartStr = DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
        dateEndStr = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);
        mTvDate.setText(dateStartStr + "-" + dateEndStr);
        mTvDateTypeSelect.setText("本月");

        mTimePickerUtil.initCustomTimePicker(getActivity(), new TimePickerUtil.OnTimePickerListener() {
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
                if (mWinningType == 1){
                    Intent intent = new Intent(getActivity(), CashPrizesRecordAtivity.class);
                    intent.putExtra("title", "核销详情");
                    intent.putExtra("prizeid", listWinning.get(position).getMw_prizeid());
                    intent.putExtra("shopid", listWinning.get(position).getShopid());
                    intent.putExtra("dateStartStr", dateStartStr);
                    intent.putExtra("dateEndStr", dateEndStr);
                    intent.putExtra("dateTypeStr", mTvDateTypeSelect.getText().toString());
                    startActivity(intent);
                }else if (mWinningType == 2){
                    Intent intent = new Intent(getActivity(),GoodsUpShopWinningActivity.class);
                    intent.putExtra("dateStartStr", dateStartStr);
                    intent.putExtra("dateEndStr", dateEndStr);
                    intent.putExtra("dateTypeStr", mTvDateTypeSelect.getText().toString());
                    startActivity(intent);
                }
            }
        });
        getCustomerWinningList();
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar, R.id.ll_search,R.id.tv_shop, R.id.tv_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_search://条件查询
                mFlag = 1;
                page = 1;

                listWinning.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();
                break;
            case R.id.tv_shop:
                mWinningType = 1;
                mTvShop.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvGoods.setTextColor(UIUtils.getColor(R.color.text_gray));
                adapterShop.setType(mWinningType);
                mFlag = 1;
                page = 1;

                listWinning.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();
                break;
            case R.id.tv_goods:
                mWinningType = 2;
                mTvShop.setTextColor(UIUtils.getColor(R.color.text_gray));
                mTvGoods.setTextColor(UIUtils.getColor(R.color.text_blue));
                adapterShop.setType(mWinningType);
                mFlag = 1;
                page = 1;

                listWinning.clear();
                adapterShop.notifyDataSetChanged();
                getCustomerWinningList();
                break;
        }
    }

    /**
     * 获取店铺核销汇总
     */
    private void getCustomerWinningList() {
        AsyncHttpUtil<ShopWinningDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ShopWinningDetailBean.class, new IUpdateUI<ShopWinningDetailBean>() {
            @Override
            public void updata(ShopWinningDetailBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if(mWinningType == 1){
                        mTvTotalNum.setText("店铺数量:" + jsonStr.getNum());
                    }else{
                        mTvTotalNum.setText("商品数量:" + jsonStr.getNum());
                    }
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
                String.valueOf(page), mEtSearch.getText().toString(), String.valueOf(mWinningType)), true);
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
