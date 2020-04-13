package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.CustomerSelectListingAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.event.BindingFacilityEvent;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/13
 *     desc   : 客户选择列表--进行绑定设备客户选择
 *     version: 1.0
 * </pre>
 */

public class CustomerSelectListingActivity extends BaseActivity{
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private List<AddressListBean> list = new ArrayList<>();
    private CustomerSelectListingAdapter adapterCustomer;

    private int page = 1;
    private int mFlag = 0;

    private ClientBean clientBean = new ClientBean();

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_select_listing;
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
        mTitleBar.setTitle("客户列表");
        mTitleBar.leftBack(this);

        adapterCustomer = new CustomerSelectListingAdapter();
        adapterCustomer.setData(list);
        mLvNoScroll.setAdapter(adapterCustomer);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page =1;
                mFlag = 1;
                list.clear();
                adapterCustomer.notifyDataSetChanged();
                getCustomerList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getCustomerList();
            }
        });

        mLvNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressListBean bean = list.get(i);

                clientBean.setCc_id(bean.getCc_id());
                clientBean.setCc_image(bean.getCc_image());
                clientBean.setCc_name(bean.getCc_name());
                clientBean.setCc_contacts_name(bean.getCc_contacts_name());
                clientBean.setCc_contacts_mobile(bean.getCc_contacts_mobile());
                clientBean.setCc_address(bean.getCc_address());

                getCustomerInfo(bean.getCc_id());
            }
        });

        getCustomerList();
    }

    /**
     * 获取客户通讯录
     */
    private void getCustomerList() {
        AsyncHttpUtil<AddressListBean> httpUtil = new AsyncHttpUtil<>(this, AddressListBean.class, new IUpdateUI<AddressListBean>() {
            @Override
            public void updata(AddressListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        list.addAll(jsonBean.getDataList());
                        adapterCustomer.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCustomerList, L_RequestParams.getCustomerList(
                UserInfoUtils.getId(this), "", String.valueOf(page), String.valueOf(10), mEtSearch.getText().toString()
                , "", "", "", "2"), true);
    }

    @OnClick({R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapterCustomer.notifyDataSetChanged();
                getCustomerList();
                break;
        }
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo(final String cusId) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    clientBean.setCc_contacts_name(jsonBean.getCc_contacts_name());//联系人
                    clientBean.setCc_contacts_mobile(jsonBean.getCc_contacts_mobile());//联系电话
                    clientBean.setCc_categoryid(jsonBean.getCc_categoryid());//类别
                    clientBean.setCc_categoryid_nameref(jsonBean.getCc_categoryid_nameref());//类别名称
                    clientBean.setCc_channelid(jsonBean.getCc_channelid());//渠道
                    clientBean.setCc_channelid_nameref(jsonBean.getCc_channelid_nameref());

                    clientBean.setCc_depotid(jsonBean.getCc_depotid());
                    clientBean.setCc_depotid_nameref(jsonBean.getCc_depotid_nameref());
                    clientBean.setCc_image(jsonBean.getCc_image());

                    clientBean.setCc_isaccount(jsonBean.getCc_isaccount());
                    clientBean.setCc_openid(jsonBean.getCc_openid());

                    if ("Y".equals(clientBean.getCc_isaccount())) {//已经开通商城
                        DialogUtil.showCustomDialog(CustomerSelectListingActivity.this, "提示", "去查看绑定设备详情? 或修改店铺绑定商城的手机号?",
                                "查看绑定详情", "修改手机号码", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                EventBus.getDefault().post(new BindingFacilityEvent(clientBean));
                                finishActivity();
                            }

                            @Override
                            public void no() {
                                Intent intent = new Intent(CustomerSelectListingActivity.this, OpenMallActivity.class);
                                intent.putExtra("isBinding", true);//是否是绑定设备
                                intent.putExtra("clientBean", clientBean);
                                intent.putExtra("isEdit",true);
                                startActivity(intent);
                                finishActivity();
                            }
                        });
                    } else {
                        DialogUtil.showCustomDialog(CustomerSelectListingActivity.this, "提示", "请先开通商城,再进行设备绑定!", "确定", "取消",
                                new DialogUtil.MyCustomDialogListener2() {
                                    @Override
                                    public void ok() {
                                        Intent intent = new Intent(CustomerSelectListingActivity.this, OpenMallActivity.class);
                                        intent.putExtra("isBinding", true);//是否是绑定设备
                                        intent.putExtra("clientBean", clientBean);
                                        startActivity(intent);
                                        finishActivity();
                                    }

                                    @Override
                                    public void no() {
                                    }
                                });
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
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(this) + "",
                cusId), true);
    }
}
