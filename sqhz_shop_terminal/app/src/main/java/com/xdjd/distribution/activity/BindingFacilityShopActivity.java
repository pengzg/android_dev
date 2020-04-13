package com.xdjd.distribution.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.BindingFacilityShopAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.ShopListingBean;
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
import com.xdjd.view.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/14
 *     desc   : 绑定设备的店铺列表
 *     version: 1.0
 * </pre>
 */

public class BindingFacilityShopActivity extends BaseActivity implements BindingFacilityShopAdapter.BindingFacilityShopListener{

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_noscroll)
    NoScrollListView mLvNoscroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;

    private int page = 1;
    private int mFlag = 0;

    private BindingFacilityShopAdapter adapter;
    private List<ShopListingBean> list = new ArrayList<>();

    private boolean isOrderJump;

    @Override
    protected int getContentView() {
        return R.layout.activity_binding_facility_shoplisting;
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
        mTitleBar.setTitle("已绑店铺列表");

        mEtSearch.setHint("按店铺名称搜索");

        isOrderJump = getIntent().getBooleanExtra("isOrderJump",false);

        adapter = new BindingFacilityShopAdapter(this);
        mLvNoscroll.setAdapter(adapter);
        adapter.setData(list);

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;

                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        mLvNoscroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getCustomerInfo(list.get(i));
            }
        });

        loadData();
    }

    /**
     * 获取店铺的关联详情
     */
    private void loadData() {
        AsyncHttpUtil<ShopListingBean> httpUtil = new AsyncHttpUtil<>(this, ShopListingBean.class, new IUpdateUI<ShopListingBean>() {
            @Override
            public void updata(ShopListingBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                        adapter.notifyDataSetChanged();
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
        httpUtil.post(M_Url.getShopList, L_RequestParams.getShopList(String.valueOf(page),mEtSearch.getText().toString()), true);
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo(final ShopListingBean bean) {
        final AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ClientBean clientBean = new ClientBean();

                    clientBean.setCc_id(bean.getMs_id());
                    clientBean.setCc_address(bean.getMs_address());
                    clientBean.setCc_name(bean.getMs_name());

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

                    if (isOrderJump){
                        Intent intent = new Intent(BindingFacilityShopActivity.this,BindingFacilityActivity.class);
                        intent.putExtra("clientBean",clientBean);
                        startActivity(intent);
                    }else{
                        EventBus.getDefault().post(new BindingFacilityEvent(clientBean));
                    }
                    finishActivity();
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
                bean.getMs_id()), true);
    }

    @OnClick({R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;

                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
                break;
        }
    }

    @Override
    public void onItemEditCode(final int i) {
        DialogUtil.showEditPwd(this, list.get(i).getMmu_pwd(), "确定", "取消", new DialogUtil.MyCustomDialogListener() {
            @Override
            public void ok(Dialog dialog,String str) {
                setUpHxPwd(dialog,i,list.get(i).getMs_id(),str);
            }

            @Override
            public void no() {
            }
        });
    }

    private void setUpHxPwd(final Dialog dialog, final int i, String ccId, final String pwd){
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())){
                    list.get(i).setMmu_pwd(pwd);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    ToastUtils.showToastInCenterSuccess(BindingFacilityShopActivity.this,jsonStr.getRepMsg());
                }else{
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.setUpHxPwd,L_RequestParams.setUpHxPwd(ccId,pwd),true);
    }
}
