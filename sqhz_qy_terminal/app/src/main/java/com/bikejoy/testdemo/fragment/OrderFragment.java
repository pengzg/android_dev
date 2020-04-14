package com.bikejoy.testdemo.fragment;

import android.animation.ObjectAnimator;
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
import com.bikejoy.utils.UserInfoUtils;
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
 *     desc   : 订单
 *     version: 1.0
 * </pre>
 */

public class OrderFragment extends BaseFragment implements CalendarSelectPopup.ItemDateOnListener {

    Unbinder unbinder;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.tv4)
    TextView mTv4;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.lv_pull)
    ListView mLvPull;
    @BindView(R.id.tv5)
    TextView mTv5;
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

    private VaryViewHelper mVaryViewHelper = null;
    private MyErrorListener mErrorListener;
    /**
     * 订单状态: 0.全部 2.待付款 6.待收货 8.已完成 1.已取消
     */
    private String omState = "";

    private String orderType = "";

    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;
    private String dateTypeSelectStr = "";

    private Date date = new Date();
    private TimePickerUtil mTimePickerUtil;
    private int dateType = 1;

    private UserBean mUserBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        orderType = (String) getArguments().get("orderType");

        mUserBean = UserInfoUtils.getUser(getActivity());

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 5;

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

        mVaryViewHelper = new VaryViewHelper(mLlList);
        mErrorListener = new MyErrorListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryOrderList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
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

        initCustomTimePicker();
        queryOrderList(PublicFinal.FIRST, false);
    }

    /**
     * 获取配送订单列表
     */
    private void queryOrderList(int isFirst, boolean isDialog) {
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
                            //                            showToast("没有更多数据了");
                            showToast("数据全部加载完毕");
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }

                    if (bean.getFooter()!=null && bean.getFooter().size()>0 && bean.getFooter().get(0).getOrderVO()!=null){
                        mTvTotalAmount.setText(bean.getFooter().get(0).getOrderVO().getOm_collect_amount());
                    }else{
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
        String payState;
        //订单类型 1 水票订单  2 送水订单 3 购物订单
        if ("2".equals(orderType) && "1".equals(mUserBean.getUsertype())) {//送水订单 且是管理员时
            payState = "";
        } else {
            payState = "1";
        }
        httpUtil.post(M_Url.queryOrderList, L_RequestParams.queryOrderList("", "", orderType, "",
                omState, page + "", "", "", dateStartStr.replace(".", "-"),
                dateEndStr.replace(".", "-"), "", payState, ""), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryOrderList(PublicFinal.FIRST, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                omState = "";
                moveAnimation(0);
                alterWidth(mTv1);
                break;
            case R.id.tv2:
                omState = "2";
                moveAnimation(1);
                alterWidth(mTv2);
                break;
            case R.id.tv3:
                omState = "6";
                moveAnimation(2);
                alterWidth(mTv3);
                break;
            case R.id.tv4:
                omState = "8";
                moveAnimation(3);
                alterWidth(mTv4);
                break;
            case R.id.tv5:
                omState = "1";
                moveAnimation(4);
                alterWidth(mTv5);
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
                        getResources().getDisplayMetrics().widthPixels / 5
                                * index).setDuration(300).start();

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryOrderList(PublicFinal.FIRST, false);
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
