package com.xdjd.steward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.DisplayFeeOrderDetailActivity;
import com.xdjd.distribution.activity.SearchGoodsActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.DisplayListBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.SelectOrderStatusPopup;
import com.xdjd.distribution.popup.SelectSalesmanPopup;
import com.xdjd.steward.adapter.DisplayFeeStatisticalAdapter;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
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
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DisplayFeeStatisticalActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener,
        SelectSalesmanPopup.SalesmanSearchListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    PullToRefreshListView mPullScroll;
    @BindView(R.id.tv_category)
    TextView mTvCategory;
    @BindView(R.id.ll_select_category)
    LinearLayout mLlSelectCategory;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_total_amount)
    TextView mTvTotalAmount;
    @BindView(R.id.tv_salesman)
    TextView mTvSalesman;
    @BindView(R.id.ll_select_salesman)
    LinearLayout mLlSelectSalesman;

    private DisplayFeeStatisticalAdapter adapter;
    private List<DisplayListBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private TimePickerUtil mTimePickerUtil;
    private String dateStartStr;
    private String dateEndStr;
    private String dateTypeStr;//日期区间描述,如:今天
    private int dateType = 1;

    /**
     * 业务员列表
     */
    private List<SalesdocListBean> listSalesman;
    //业务员选择popup
    private SelectSalesmanPopup popupSalesman;
    //业务员
    public String salesid = "";
    //业务员名称
    private String salesName = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_display_fee_statistical;
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
        mTitleBar.setTitle("返陈列信息");

        adapter = new DisplayFeeStatisticalAdapter();

        mPullScroll.setAdapter(adapter);
        adapter.setList(list);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setAdapter(adapter);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getDisplayInList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                getDisplayInList();
            }
        });

        mPullScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i = i - 1;//刷新listview会添加头部,下标所以减1
                Intent intent = new Intent(DisplayFeeStatisticalActivity.this, DisplayFeeStatisticalDetailActivity.class);
                intent.putExtra("orderId", list.get(i).getEim_id());
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
                getDisplayInList();
            }
        }, this);

        initSalesmanPopup();
        getDisplayInList();
    }

    private void getDisplayInList() {
        AsyncHttpUtil<DisplayListBean> httpUtil = new AsyncHttpUtil<>(this, DisplayListBean.class,
                new IUpdateUI<DisplayListBean>() {
                    @Override
                    public void updata(DisplayListBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            mTvNum.setText("总数量:" + jsonStr.getNum());
                            mTvTotalAmount.setText("返陈列总金额:" + jsonStr.getTotalAmount());
                            if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                                list.addAll(jsonStr.getDataList());
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
                        mPullScroll.onRefreshComplete();
                    }
                });
        httpUtil.post(M_Url.getDisplayInList, L_RequestParams.getDisplayInList("2",salesid,"",
                dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"),
                String.valueOf(page), "10", mTvSearch.getText().toString()), true);
    }

    /**
     * 获取业务员列表接口
     */
    private void querySalesmanList(final boolean isDialog, int salesmanType, String searchStr) {
        AsyncHttpUtil<SalesdocListBean> httpUtil = new AsyncHttpUtil<>(this, SalesdocListBean.class, new IUpdateUI<SalesdocListBean>() {
            @Override
            public void updata(SalesdocListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSalesman = jsonBean.getDataList();
                    if (listSalesman != null && listSalesman.size() > 0) {
                        SalesdocListBean bean = new SalesdocListBean();
                        bean.setSu_id("");
                        bean.setSu_name("全部");
                        listSalesman.add(0,bean);
                        showPwSalesman();
                    } else {
                        showToast("没有业务员数据");
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
            }
        });
        httpUtil.post(M_Url.getSalesdocList,
                L_RequestParams.getSalesdocList(searchStr, String.valueOf(salesmanType)), isDialog);
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar, R.id.ll_clear, R.id.ll_search,R.id.ll_select_salesman})
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
                getDisplayInList();
                break;
            case R.id.ll_select_salesman:
                if (listSalesman == null || listSalesman.size() == 0) {
                    querySalesmanList(true, 1, "");
                } else {
                    showPwSalesman();
                }
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
        getDisplayInList();
    }

    /**
     * 初始化业务员popup
     */
    private void initSalesmanPopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupSalesman = new SelectSalesmanPopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                salesid = listSalesman.get(position).getSu_id();
                salesName = listSalesman.get(position).getSu_name();
                mTvSalesman.setText(salesName);
                popupSalesman.dismiss();

                if (salesid == null) {
                    salesid = "";
                }

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getDisplayInList();
            }
        }, this);
    }

    /**
     * 显示员工popup
     */
    private void showPwSalesman() {
        if (!popupSalesman.isShowing()) {
            popupSalesman.setData(listSalesman);
            popupSalesman.setId(salesid);
            // 显示窗口
            popupSalesman.showAtLocation(mLlMain,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        } else {
            popupSalesman.setData(listSalesman);
            popupSalesman.setId(salesid);
        }
    }

    @Override
    public void onSearch(int salesmanType, String searchStr) {
        //搜索员工
        querySalesmanList(true, salesmanType, searchStr);
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
                getDisplayInList();
                break;
        }
    }

}
