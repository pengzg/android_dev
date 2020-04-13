package com.xdjd.distribution.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.DistributionActivity;
import com.xdjd.distribution.activity.DistributionTaskActivity;
import com.xdjd.distribution.adapter.DistributionCategoryAdapter;
import com.xdjd.distribution.adapter.DistributionTaskAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.TaskBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/17
 *     desc   : 已配送fragment
 *     version: 1.0
 * </pre>
 */

public class BeenDistributionFragment extends BaseFragment implements DistributionCategoryAdapter.DistributionCategoryListener,
        DistributionTaskAdapter.DistributionTaskListener {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.lv_category)
    ListView mLvCategory;
    @BindView(R.id.lv_task)
    ListView mLvTask;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.backwards_ll)
    LinearLayout mBackwardsLl;
    @BindView(R.id.front_ll)
    LinearLayout mFrontLl;

    private View view;

    private UserBean userBean;

    private DistributionCategoryAdapter adapterCategory;
    private DistributionTaskAdapter adapterTask;

    private List<TaskBean> list;

    private DatePickerDialog dateDialog;

    /**
     * 查询日期
     */
    private String dateStr;
    Calendar cStrar;

    private int dateNum = 0;

    GregorianCalendar calToDay;
    GregorianCalendar calDate;

    private Date date = new Date();

//    private LocationService locationService;
//    private MyLocationUtil locationUtil;

    private DistributionTaskActivity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_been_distribution, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        adapterCategory = new DistributionCategoryAdapter(this);
        mLvCategory.setAdapter(adapterCategory);

        adapterTask = new DistributionTaskAdapter(this,1);
        mLvTask.setAdapter(adapterTask);

        dateStr = DateUtils.getDataTime(DateUtils.dateFormater2);
        mTvDate.setText(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
        dateDialog = new DatePickerDialog(getActivity(), dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        calToDay = new GregorianCalendar();
        calDate = new GregorianCalendar();
        calToDay.setTime(date);

        context = (DistributionTaskActivity) getActivity();
        if (!context.longtitude.equals("")){
            getTaskList();
        }
    }

    @OnClick({R.id.ll_search, R.id.front_ll, R.id.backwards_ll, R.id.tv_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                getTaskList();
                break;
            case R.id.front_ll:
                dateNum--;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
                if (list!=null)
                list.clear();
                adapterCategory.notifyDataSetChanged();
                adapterTask.setData(null);
                context.locationService.start();
                getTaskList();
                break;
            case R.id.backwards_ll:
                dateNum++;
                dateStr = DateUtils.getDistanceDay(date, "yyyy-MM-dd", dateNum);
                mTvDate.setText(dateStr);

                cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvDate.getText().toString()));
                dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
                if (list!=null)
                list.clear();
                adapterCategory.notifyDataSetChanged();
                adapterTask.setData(null);
                context.locationService.start();
                getTaskList();
                break;
            case R.id.tv_date:
                dateDialog.show();
                break;
        }
    }

    public void getTaskList() {
        AsyncHttpUtil<TaskBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TaskBean.class, new IUpdateUI<TaskBean>() {
            @Override
            public void updata(TaskBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    list = bean.getDataList();
                    if (list != null) {
                        adapterCategory.setData(list);
                        if (list!=null && list.size()>0){
                            onItemCategory(0);
                        }else{
                            adapterCategory.setData(null);
                            adapterTask.setData(null);
                        }
                    }
                } else {
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.getTaskList, L_RequestParams.getTaskList(userBean.getUserId(), "1",
                mTvDate.getText().toString(), mEtSearch.getText().toString(), context.longtitude, context.latitude,""), true);
    }

    @Override
    public void onItemCategory(int i) {
        if (list.get(i).getCustomerList() != null && list.get(i).getCustomerList().size()>0){
            adapterTask.setData(list.get(i).getCustomerList());
        }else{
            adapterTask.setData(null);
        }
    }

    @Override
    public void onItemTask(int i) {
//        CustomerTaskBean bean = list.get(adapterCategory.getIndex()).getCustomerList().get(i);
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), DistributionActivity.class);
//        intent.putExtra("title", bean.getCc_name());
//        startActivity(intent);
    }

    @Override
    public void onItemLong(int i) {

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
            if (list!=null)
            list.clear();
            adapterCategory.notifyDataSetChanged();
            adapterTask.setData(null);
            context.locationService.start();
            getTaskList();
        }
    };

   /* @Override
    public void onPause() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((App) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }*/

}
