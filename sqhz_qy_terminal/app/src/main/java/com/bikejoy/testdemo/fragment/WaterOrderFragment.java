package com.bikejoy.testdemo.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.DateUtils;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.TimePickerUtil;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.event.OrderListUpdateEvent;
import com.bikejoy.testdemo.popup.CalendarSelectPopup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 会员配送订单
 *     version: 1.0
 * </pre>
 */

@SuppressLint("ValidFragment")
public class WaterOrderFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    Unbinder unbinder;
    @BindView(R.id.lv_pull)
    ListView mLvPull;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
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
    @BindView(R.id.ll_calendar)
    LinearLayout mLlCalendar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.tv_total_amount_title)
    TextView mTvTotalAmountTitle;
    @BindView(R.id.tv_total_amount)
    TextView mTvTotalAmount;
    @BindView(R.id.ll_list)
    LinearLayout mLlList;

    private List<String> listTab = new ArrayList<>();

    private OrderListAdapter adapter;
    private List<OrderInfo> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private String payWay = "1";//支付方式 1 线上 2 货到付款 3 账期  4赠送

    private VaryViewHelper mVaryViewHelper = null;
    private MyErrorListener mErrorListener;

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;

    private Date date = new Date();
    private TimePickerUtil mTimePickerUtil;
    private int dateType = 1;
    private String dateTypeSelectStr = "";

    private MemberBean customer;//传递过来的客户信息

    @SuppressLint("ValidFragment")
    public WaterOrderFragment(MemberBean customer) {
        this.customer = customer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 2;

        mVaryViewHelper = new VaryViewHelper(mLlList);
        mErrorListener = new MyErrorListener();

        dateStartStr = getActivity().getIntent().getStringExtra("dateStartStr");
        dateEndStr = getActivity().getIntent().getStringExtra("dateEndStr");
        dateTypeStr = getActivity().getIntent().getStringExtra("dateTypeStr");
        dateTypeSelectStr = getActivity().getIntent().getStringExtra("dateTypeSelectStr");

        if (dateTypeSelectStr != null && !"".equals(dateTypeSelectStr)) {
            mTvDateTypeSelect.setText(dateTypeSelectStr);
        }

        if (dateStartStr == null || "".equals(dateStartStr)) {
            mTvDateTypeSelect.setText("今日");
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        }
        mTvDate.setText(dateStartStr + "-" + dateEndStr);
        mTimePickerUtil = new TimePickerUtil();
        //传入会员信息时隐藏日期选择,查询全部
        if (customer != null) {
            mLlCalendar.setVisibility(View.GONE);
        } else {
            mLlCalendar.setVisibility(View.VISIBLE);
        }

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                queryOrderList(PublicFinal.TWO, true);
            }
        });

        adapter = new OrderListAdapter();
        adapter.setData(list);
        adapter.setListener(new OrderListAdapter.OnOrderListListener() {
            @Override
            public void onItem(int i) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderInfo", list.get(i));
                startActivity(intent);
            }
        });
        mLvPull.setAdapter(adapter);

        mTv1.post(new Runnable() {
            @Override
            public void run() {
                alterWidth(mTv1);
            }
        });

        initCustomTimePicker();
        queryOrderList(PublicFinal.FIRST, false);
    }

    /**
     * 获取配送订单列表
     */
    private void queryOrderList(int isFirst, boolean isDialog) {
        String memberId = "";
        if (customer != null) {
            memberId = customer.getMb_id();
        }
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<OrderInfo> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderInfo.class, new IUpdateUI<OrderInfo>() {
            @Override
            public void updata(OrderInfo jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    OrderInfo bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView();
                        } else {
                            page--;
                            showToast("数据全部加载完毕");
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }

                    if (bean.getFooter() != null && bean.getFooter().size() > 0 && bean.getFooter().get(0).getOrderVO() != null) {
                        mTvTotalAmount.setText(bean.getFooter().get(0).getOrderVO().getOm_collect_amount());
                    } else {
                        mTvTotalAmount.setText("0.00");
                    }
                    mTvTotalNum.setText("总条数:" + bean.getTotal());
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getDesc(), mErrorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(), mErrorListener);
            }

            @Override
            public void finish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.setNoMoreData(false);
                }
            }
        });
        String startDate = dateStartStr.replace(".", "-");
        String endDate = dateEndStr.replace(".", "-");
        if (customer != null) {
            startDate = "";
            endDate = "";
        }
        httpUtil.post(M_Url.queryOrderList, L_RequestParams.queryOrderList("", memberId, "1", "",
                "", page + "", "", payWay, startDate,
                endDate, "", "1", ""), isDialog);
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                payWay = "1";
                moveAnimation(0);
                alterWidth(mTv1);
                break;
            case R.id.tv2:
                payWay = "4";
                moveAnimation(1);
                alterWidth(mTv2);
                break;
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(getView(), dateStartStr,
                        dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(getView(), dateStartStr,
                        dateEndStr, true);
                break;
        }
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
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
        queryOrderList(PublicFinal.FIRST, false);
    }

    private void initCustomTimePicker() {
        mTimePickerUtil.initCustomTimePicker(getActivity(), new TimePickerUtil.OnTimePickerListener() {
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
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderList(PublicFinal.FIRST, false);
            }
        }, this);

    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryOrderList(PublicFinal.FIRST, false);
        }
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 2
                                * index).setDuration(300).start();
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryOrderList(PublicFinal.FIRST, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onEventMainThread(OrderListUpdateEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryOrderList(PublicFinal.FIRST, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
