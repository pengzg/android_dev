package com.xdjd.distribution.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.VisitingListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.TaskListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.bean.VisitingAlarmBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisitingListActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_visiting)
    NoScrollListView mLvVisiting;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.front_ll)
    LinearLayout mFrontLl;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.backwards_ll)
    LinearLayout mBackwardsLl;

    /**
     * 查询日期
     */
    private String dateStr;

    private int dateNum = 0;

    GregorianCalendar calToDay;
    GregorianCalendar calDate;

    private Date date = new Date();

    private DatePickerDialog dateDialog;

    private UserBean userBean;

    private int mFlag = 0;
    private int page = 1;

    List<TaskListBean> list = new ArrayList<>();

    private VisitingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_visiting_list;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("拜访明细");

        userBean = UserInfoUtils.getUser(this);

        dateStr = DateUtils.getDataTime(DateUtils.dateFormater2);
        mTvDate.setText(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
        dateDialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        calToDay = new GregorianCalendar();
        calDate = new GregorianCalendar();
        calToDay.setTime(date);

        adapter = new VisitingListAdapter();
        mLvVisiting.setAdapter(adapter);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);

        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetInvalidated();

                getCsTaskList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;

                getCsTaskList();
            }

        });

        getCsTaskList();
    }

    @OnClick({R.id.front_ll, R.id.backwards_ll,R.id.tv_date})
    public void onClick(View view) {
        Calendar cStrar;
        switch (view.getId()) {
            case R.id.front_ll:
                dateNum--;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));

                list.clear();
                adapter.notifyDataSetInvalidated();
                page = 1;
                mFlag = 1;
                getCsTaskList();
                break;
            case R.id.backwards_ll:
                dateNum++;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));

                list.clear();
                adapter.notifyDataSetInvalidated();
                page = 1;
                mFlag = 1;
                getCsTaskList();
                break;
            case R.id.tv_date:
                dateDialog.show();
                break;
        }
    }

    /**
     * 日选择回调接口
     */
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
            String str = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            Date date1 = StringUtils.toDateFormater2(str);
            calDate.setTime(date1);

            dateNum = (int) ((calDate.getTimeInMillis() - calToDay.getTimeInMillis()) / (1000 * 3600 * 24));//从间隔毫秒变成间隔天数
            dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
            mTvDate.setText(dateStr);

            Calendar cStrar = Calendar.getInstance();
            cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
            dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                    cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));

            list.clear();
            adapter.notifyDataSetInvalidated();
            page = 1;
            mFlag = 1;
            getCsTaskList();
        }
    };

    private void getCsTaskList(){
        AsyncHttpUtil<TaskListBean> httpUtil = new AsyncHttpUtil<>(this,TaskListBean.class , new IUpdateUI<TaskListBean>() {
            @Override
            public void updata(TaskListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())){
                    if (jsonBean.getDataList()!=null && jsonBean.getDataList().size()>0){
                        list.addAll(jsonBean.getDataList());
                        adapter.setData(list);
                    }else{
                        if (mFlag == 2){
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                }else{
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.getCsTaskList, L_RequestParams.getCsTaskList(userBean.getUserId(),String.valueOf(page),dateStr,dateStr),true);
    }
}
