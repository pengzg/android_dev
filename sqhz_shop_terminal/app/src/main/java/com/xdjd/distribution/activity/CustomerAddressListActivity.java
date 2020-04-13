package com.xdjd.distribution.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.CustomerAddressListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.CustomerAddressListEvent;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
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
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 客户通讯录
 */
public class CustomerAddressListActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_customer_address_list)
    ListView mLvCustomerAddressList;
    @BindView(R.id.tv_center)
    TextView mTvCenter;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_customer_total_num)
    TextView mTvCustomerTotalNum;

    private List<AddressListBean> list = new ArrayList<>();
    private CustomerAddressListAdapter adapter;
    private final String MY_BAIDU_PACKAGE = "com.baidu.BaiduMap";
    private UserBean userBean;
    private boolean isLocation = false;//是否跳转定位界面,false不去,true去;

    private int page = 1;
    private int mFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_address_list;
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("客户通讯录");
        userBean = UserInfoUtils.getUser(this);
        mEtSearch.setHint("按店铺名称、客户姓名和电话进行查询");
        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                getCustomerList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getCustomerList();
            }
        });

        adapter = new CustomerAddressListAdapter();
        mLvCustomerAddressList.setAdapter(adapter);
        adapter.setData(list);

        mLvCustomerAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String hint = "";
                if ("N".equals(list.get(position).getCc_islocation())) {
                    hint = "选择您的操作";
                    isLocation = true;
                } else {
                    hint = list.get(position).getCc_name() + "\n" + list.get(position).getCc_contacts_mobile();
                    hint.replace("\\n", "\n");
                    isLocation = false;
                }

                DialogUtil.showCustomDialog(CustomerAddressListActivity.this, "提示",
                        hint, "联系客户",
                        isLocation ? "去定位" : "地图导航", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                if ("".equals(list.get(position).getCc_contacts_mobile()) || list.get(position).getCc_contacts_mobile() == null) {
                                    showToast("请先完善客户电话信息");
                                    return;
                                }
                                //联系客户
                                Intent intent = new Intent(Intent.ACTION_DIAL,
                                        Uri.parse("tel:" + list.get(position).getCc_contacts_mobile()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void no() {
                                if (isLocation) {//跳转定位界面
                                    Intent intent = new Intent(CustomerAddressListActivity.this, PositionLocationActivity.class);
                                    intent.putExtra("isHome", 2);
                                    intent.putExtra("customer", list.get(position));
                                    intent.putExtra("index", position);
                                    startActivity(intent);
                                } else {
                                    //地图导航
                                    boolean baidu = isAvilible(CustomerAddressListActivity.this, MY_BAIDU_PACKAGE);
                                    if (baidu) {
                                        if ("".equals(list.get(position).getCc_latitude()) || list.get(position).getCc_latitude() == null) {
                                            return;
                                        }
                                        Intent intent;
                                        try {
                                            intent = new Intent();
                                            intent.setData(Uri.parse("baidumap://map/geocoder?location=" + list.get(position).getCc_latitude() +
                                                    "," + list.get(position).getCc_longitude() + "&title=" + list.get(position).getCc_name()));
                                            startActivity(intent); //启动调用
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        showToast("请安装百度地图软件");
                                    }
                                }
                            }
                        });
            }
        });

        getCustomerList();
    }

    private boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取客户通讯录
     */
    private void getCustomerList() {
        AsyncHttpUtil<AddressListBean> httpUtil = new AsyncHttpUtil<>(CustomerAddressListActivity.this, AddressListBean.class, new IUpdateUI<AddressListBean>() {
            @Override
            public void updata(AddressListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        list.addAll(jsonBean.getDataList());
                        // 填充数据 , 排序
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                    mTvCustomerTotalNum.setText(jsonBean.getTotal()+"位客户联系人");
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCustomerList, L_RequestParams.getCustomerList(
                userBean.getUserId(), UserInfoUtils.getLineId(this) + "", String.valueOf(page),
                "20", mEtSearch.getText().toString(), "", "", "", "1"), true);
    }

    @OnClick({R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetInvalidated();
                getCustomerList();
                break;
        }
    }

    public void onEventMainThread(CustomerAddressListEvent event) {
        int index = event.getIndex();
        if (index == -1) {
            list.clear();
            adapter.notifyDataSetInvalidated();
            getCustomerList();
        } else {
            list.set(index, event.getBean());
            adapter.setData(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
