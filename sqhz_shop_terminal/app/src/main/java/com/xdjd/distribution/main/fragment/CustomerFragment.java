package com.xdjd.distribution.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.AddClientActivity;
import com.xdjd.distribution.activity.ClientDetailActivity;
import com.xdjd.distribution.adapter.CustomerListAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.AddCustomerUpdateEvent;
import com.xdjd.distribution.event.OpenMallEvent;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2016/8/22.
 */
public class CustomerFragment extends BaseFragment {

//    @BindView(R.id.client_lv)
//    NoScrollListView mClientLv;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.pull_scroll)
    PullToRefreshListView mPullScroll;

    private CustomerListAdapter adapter;

    private List<CustomerBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private UserBean userBean;

    private VaryViewHelper mVaryViewHelper = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        userBean = UserInfoUtils.getUser(getActivity());

        mVaryViewHelper = new VaryViewHelper(mPullScroll);

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getCustomerList(PublicFinal.TWO,true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                page++;
                getCustomerList(PublicFinal.TWO,true);
            }
        });

        adapter = new CustomerListAdapter();
        mPullScroll.setAdapter(adapter);

        mPullScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i=i-1;
                CustomerBean bean = list.get(i);

                ClientBean clientBean = new ClientBean();
                clientBean.setCc_id(bean.getCc_id());
                clientBean.setCc_image(bean.getCc_image());
                clientBean.setCc_name(bean.getCc_name());
                clientBean.setCc_contacts_name(bean.getCc_contacts_name());
                clientBean.setCc_contacts_mobile(bean.getCc_contacts_mobile());
                clientBean.setCc_address(bean.getCc_address());

                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                intent.putExtra("customer", clientBean);
                startActivity(intent);
            }
        });

        getCustomerList(PublicFinal.FIRST, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //判断是否要运行此方法里的内容
        if (!(YesOrNoLoadingOnstart.INDEX = true && YesOrNoLoadingOnstart.INDEX_ID == 2)) {
            return;
        }

        if (!TextUtils.isEmpty(mEtSearch.getText())) {
            //如果输入框不是空
//            mEtSearch.setText("");
//            getCustomerList();
        }
    }

    private void getCustomerList(int isFirst,boolean isDialog) {

        if (isFirst == PublicFinal.FIRST){
            mVaryViewHelper.showLoadingView();
        }

        String searchKey;
        if (TextUtils.isEmpty(mEtSearch.getText())) {
            searchKey = "";
        } else {
            searchKey = mEtSearch.getText().toString();
        }
        AsyncHttpUtil<CustomerBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CustomerBean.class, new IUpdateUI<CustomerBean>() {
            @Override
            public void updata(CustomerBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mVaryViewHelper.showDataView();
                    if (jsonBean.getDataList()!=null && jsonBean.getDataList().size()>0){
                        list.addAll(jsonBean.getDataList());
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了");
                            page--;
                        } else {
                            mVaryViewHelper.showEmptyView();
                        }
                    }
                    adapter.setData(list);

                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getRepMsg(),new OnErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(),new OnErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCustomerList, L_RequestParams.
                getCustomerList(userBean.getUserId(), UserInfoUtils.getLineId(getActivity()) + "", String.valueOf(page),
                        "8", searchKey,"","","","1"), isDialog);
    }

    public class OnErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            page = 1;
            mFlag = 1;
            list.clear();
            adapter.notifyDataSetChanged();
            getCustomerList(PublicFinal.FIRST,false);
        }
    }

    @OnClick({R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getCustomerList(PublicFinal.FIRST,false);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AddCustomerUpdateEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        getCustomerList(PublicFinal.FIRST,false);
    }

    public void onEventMainThread(OpenMallEvent event) {
        ClientBean clientBean = event.getClientBean();
        for (CustomerBean bean:list){
            if (bean.getCc_id().equals(clientBean.getCc_id())){
                bean.setCc_address(clientBean.getCc_address());
                bean.setCc_name(clientBean.getCc_name());
                bean.setCc_contacts_name(clientBean.getCc_contacts_name());
                bean.setCc_contacts_mobile(clientBean.getCc_contacts_mobile());
                adapter.notifyDataSetChanged();
            }
        }
    }

}
