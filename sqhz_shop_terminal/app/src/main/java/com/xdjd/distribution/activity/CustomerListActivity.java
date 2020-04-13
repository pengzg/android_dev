package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.CustomerListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.CustomerBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.UserInfoUtils;
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

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerListActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.client_lv)
    ListView mClientLv;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private CustomerListAdapter adapter;
    private List<CustomerBean> list = new ArrayList<>();

    private UserBean userBean;
    private int page = 1;
    private int mFlag = 0;

    private String startDate,endDate;

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("客户信息");
        /*if (userBean !=null && !BaseConfig.userTypeAdministrator.equals(userBean.getSu_usertype())){//如果是管理员就隐藏
            mTitleBar.setRightImageResource(R.mipmap.add_client);
            mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CustomerListActivity.this, AddClientActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }*/

        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getCustomerList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                page++;
                getCustomerList();
            }
        });

        adapter = new CustomerListAdapter();
        mClientLv.setAdapter(adapter);

        getCustomerList();
    }

    private void getCustomerList() {
        String searchKey;
        if (TextUtils.isEmpty(mEtSearch.getText())) {
            searchKey = "";
        } else {
            searchKey = mEtSearch.getText().toString();
        }
        AsyncHttpUtil<CustomerBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBean.class, new IUpdateUI<CustomerBean>() {
            @Override
            public void updata(CustomerBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList()!=null && jsonBean.getDataList().size()>0){
                        list.addAll(jsonBean.getDataList());
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了");
                            page--;
                        }
                    }
                    adapter.setData(list);
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCustomerList, L_RequestParams.
                getCustomerList(userBean.getUserId(), "", String.valueOf(page), "8", searchKey,startDate,endDate,"","1"), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case 1:
                    getCustomerList();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
