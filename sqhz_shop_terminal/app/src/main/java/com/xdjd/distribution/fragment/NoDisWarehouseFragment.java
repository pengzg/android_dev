package com.xdjd.distribution.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.DistributionActivity;
import com.xdjd.distribution.activity.DistributionWarehouseActivity;
import com.xdjd.distribution.adapter.DistributionCategoryAdapter;
import com.xdjd.distribution.adapter.DistributionTaskAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.TaskBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.event.OrderRejectedEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/17
 *     desc   : 未配送fragment
 *     version: 1.0
 * </pre>
 */

public class NoDisWarehouseFragment extends BaseFragment implements DistributionCategoryAdapter.DistributionCategoryListener,
        DistributionTaskAdapter.DistributionTaskListener {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.lv_category)
    ListView mLvCategory;
    @BindView(R.id.lv_task)
    ListView mLvTask;

    private View view;

    private UserBean userBean;

    private DistributionCategoryAdapter adapterCategory;
    private DistributionTaskAdapter adapterTask;

    private List<TaskBean> list;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private DistributionWarehouseActivity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_no_distribution_warehouse, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        adapterCategory = new DistributionCategoryAdapter(this);
        mLvCategory.setAdapter(adapterCategory);

        adapterTask = new DistributionTaskAdapter(this,2);
        mLvTask.setAdapter(adapterTask);

        clientBean = UserInfoUtils.getClientInfo(getActivity());

        context = (DistributionWarehouseActivity) getActivity();
        if (!context.longtitude.equals("")){
            getTaskList();
        }
    }

    @OnClick({R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                getTaskList();
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
        httpUtil.post(M_Url.getTaskList, L_RequestParams.getTaskList(userBean.getUserId(),
                "2", "", mEtSearch.getText().toString(), context.longtitude, context.latitude,clientBean.getCc_id()), true);
    }

    @Override
    public void onItemCategory(int i) {
        adapterTask.setData(list.get(i).getCustomerList());
    }

    @Override
    public void onItemTask(int i) {
        CustomerTaskBean bean = list.get(adapterCategory.getIndex()).getCustomerList().get(i);
        Intent intent = new Intent();
        intent.setClass(getActivity(), DistributionActivity.class);
//        intent.putExtra("title", bean.getCc_name());
//        intent.putExtra("customerId",bean.getCc_id());
//        intent.putExtra("customer_name",bean.getCc_name());
//        intent.putExtra("salesmanid",bean.getCc_salesmanid());
        intent.putExtra("bean",bean);
        startActivity(intent);
    }

    @Override
    public void onItemLong(int i) {
        showToast("长按");
    }

    public void loadData() {
        getTaskList();
    }

    public void onEventMainThread(OrderRejectedEvent event){
        getTaskList();
    }
}
