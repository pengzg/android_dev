package com.xdjd.steward.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.SelectSalesmanPopup;
import com.xdjd.steward.adapter.StaffVisitListAdapter;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.steward.bean.VisitRateBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
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
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/4
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class StaffVisitListActivity extends BaseActivity implements CalendarSelectPopup.ItemDateOnListener,
        SelectSalesmanPopup.SalesmanSearchListener{

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
    @BindView(R.id.tv_visit_num)
    TextView mTvVisitNum;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_ratio)
    TextView mTvRatio;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private List<VisitRateBean> list = new ArrayList<>();
    private StaffVisitListAdapter adapter;

    private int page = 1;
    private int mFlag = 0;

    private String dateStartStr;
    private String dateEndStr;

    private TimePickerUtil mTimePickerUtil;

    private int dateType = 1;
    private String dateTypeSelectStr = "";

    /**
     * 业务员列表
     */
    private List<SalesdocListBean> listSalesman;
    /**
     * 业务员选择popup
     */
    private SelectSalesmanPopup popupSalesman;

    private String salesid = "";//员工id
    private String salesName = "";//员工名称

    private int salesmanType = 1;// 业务员类型		1全部 2.业务员 3配送员

    @Override
    protected int getContentView() {
        return R.layout.activity_staff_visit_list;
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
        salesid = getIntent().getStringExtra("salesid");
        salesName = getIntent().getStringExtra("salesName");

        mTitleBar.setTitle(salesName);
        mTitleBar.leftBack(this);
        mTitleBar.setRightImageResource(R.mipmap.search);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSalesman == null || listSalesman.size() == 0) {
                    querySalesmanList(true, 1, "");
                } else {
                    showPwSalesman();
                }
            }
        });

        adapter = new StaffVisitListAdapter();
        mLvNoScroll.setAdapter(adapter);
        adapter.setData(list);

        mLvNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).getVisitNum()!=null && !"0".equals(list.get(i).getVisitNum())){
                    Intent intent = new Intent(StaffVisitListActivity.this,SalesmanVisitingActivity.class);
                    intent.putExtra("salesid",list.get(i).getSalesid());
                    intent.putExtra("dateTypeStr","自定义");
                    intent.putExtra("dateStartStr",list.get(i).getVisitDate().replace("-","."));
                    intent.putExtra("dateEndStr",list.get(i).getVisitDate().replace("-","."));
                    startActivity(intent);
                }
            }
        });

        dateType = getIntent().getIntExtra("dateType",3);
        dateTypeSelectStr = getIntent().getStringExtra("dateTypeSelectStr");
        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");

        if (dateType == 11 || dateType == 12 || dateType == 13){
            dateStartStr =  DateUtils.currentMonthFirst(DateUtils.dateFormater4, 0);
            dateEndStr = DateUtils.currentMonthEnd(DateUtils.dateFormater4, 0);

            dateType = 3;
            mTvDateTypeSelect.setText("本月");
        }else{
            mTvDateTypeSelect.setText(dateTypeSelectStr);
        }

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        mTimePickerUtil = new TimePickerUtil();
        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                int numMonth = DateUtils.getMonth(DateUtils.getDate(startDate),
                        DateUtils.getDate(endDate));
                LogUtils.e("numMonth","月数:"+numMonth);
                if (numMonth > 3) {
                    showToast("筛选时间不能超过三个月");
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
                mTvVisitNum.setText("0");
                mTvOrderNum.setText("0");
                mTvRatio.setText("0%");
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }
        }, this);

        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                mTvVisitNum.setText("0");
                mTvOrderNum.setText("0");
                mTvRatio.setText("0%");
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }
        });

        loadData();
        initSalesmanPopup();
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr);
                break;
        }
    }

    private void loadData() {
        AsyncHttpUtil<VisitRateBean> httpUtil = new AsyncHttpUtil<>(this, VisitRateBean.class, new IUpdateUI<VisitRateBean>() {
            @Override
            public void updata(VisitRateBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getListData() != null && jsonStr.getListData().size() > 0) {
                        list.addAll(jsonStr.getListData());
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                    adapter.notifyDataSetChanged();

                    mTvVisitNum.setText(jsonStr.getVisitNum());
                    mTvOrderNum.setText(jsonStr.getOrderCustNum());
                    mTvRatio.setText(jsonStr.getRatio() + "%");
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

        httpUtil.post(M_Url.getVisitRateDay, G_RequestParams.getVisitRateDay(dateStartStr.replace(".", "-"),
                dateEndStr.replace(".", "-"), String.valueOf(page), salesid), true);
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        page = 1;
        mFlag = 1;
        mTvVisitNum.setText("0");
        mTvOrderNum.setText("0");
        mTvRatio.setText("0%");
        list.clear();
        adapter.notifyDataSetChanged();
        loadData();
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
                mTitleBar.setTitle(salesName);
                popupSalesman.dismiss();

                if (salesid == null) {
                    salesid = "";
                }

                list.clear();
                adapter.notifyDataSetChanged();
                mTvVisitNum.setText("0");
                mTvOrderNum.setText("0");
                mTvRatio.setText("0%");
                page = 1;
                mFlag = 1;
                loadData();
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
}
