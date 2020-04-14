package com.bikejoy.testdemo.main;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bikejoy.utils.AppManager;
import com.bikejoy.utils.AppUtil;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.FileUtils;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.SystemUtil;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.YesOrNoLoadingOnstart;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.NetUtils;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.version.DownloadUtil;
import com.bikejoy.view.EaseTitleBar;
import com.bikejoy.view.NoScrollViewPager;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseActivity;
import com.bikejoy.testdemo.base.BaseConfig;
import com.bikejoy.testdemo.main.fragment.HomePagerFragment;
import com.bikejoy.testdemo.main.fragment.MeFragment;
import com.bikejoy.testdemo.main.fragment.MemberFragment;
import com.bikejoy.testdemo.main.fragment.OrderMainFragment;
import com.bikejoy.testdemo.popup.SelectShopPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 管理员main界面
 */
public class AdministratorMainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, SelectShopPopup.ItemOnListener {

    @BindView(R.id.main_tab_home)
    RadioButton mMainTabHome;
    @BindView(R.id.main_tab_order)
    RadioButton mMainTabOrder;
    @BindView(R.id.main_tab_customer)
    RadioButton mMainTabCustomer;
    @BindView(R.id.main_tab_me)
    RadioButton mMainTabMe;
    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.main_viewpage)
    NoScrollViewPager mMainViewpage;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;

    /**
     * Fragment的集合
     */
    public List<Fragment> mFragments;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_order)
    TextView mTvOrder;
    @BindView(R.id.tv_distribution_order)
    TextView mTvDistributionOrder;
    @BindView(R.id.ll_order_tab)
    LinearLayout mLlOrderTab;
    @BindView(R.id.tv_qs_order)
    TextView mTvQsOrder;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.rl_title2)
    RelativeLayout mRlTitle2;
    @BindView(R.id.rl_can2)
    RelativeLayout mRlCan2;
    @BindView(R.id.tv_recharge_order)
    TextView mTvRechargeOrder;

    private PurchaseViewpageAdapter viewpageAdapter;
    private OrderMainFragment orderMainFragment;

    private UserBean mUserBean;
    private List<ShopListBean> shopList;
    private ShopListBean shopBean;

    private SelectShopPopup popupShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.administrator_activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

        mUserBean = UserInfoUtils.getUser(this);

        mRlCan2.setVisibility(View.GONE);
        shopList = UserInfoUtils.getShopList(this);
        if (shopList != null && shopList.size() > 0) {
            for (ShopListBean bean : shopList) {
                if (mUserBean.getShopId().equals(bean.getMs_id())) {
                    shopBean = bean;
                }
            }

            if (shopBean == null) {
                shopBean = shopList.get(0);
            }
        }

        mFragments = new ArrayList<>();
        orderMainFragment = new OrderMainFragment();
        mFragments.add(new HomePagerFragment());
        mFragments.add(orderMainFragment);
        mFragments.add(new MemberFragment());
        mFragments.add(new MeFragment());

        viewpageAdapter = new PurchaseViewpageAdapter(this.getSupportFragmentManager(), mFragments);
        mMainViewpage.setAdapter(viewpageAdapter);
        mMainViewpage.setCurrentItem(0);
        mMainViewpage.setOnPageChangeListener(this);
        //        mRadioGroup1.check(mRadioGroup1.getChildAt(0).getId());
        mRadioGroup1.setOnCheckedChangeListener(this);
        YesOrNoLoadingOnstart.INDEX_ID = 0;
        YesOrNoLoadingOnstart.INDEX = true;

        ((RadioButton) mRadioGroup1.getChildAt(0)).setChecked(true);
        //                FragmentManager manager = getSupportFragmentManager();
        //                mTabAdapter = new FragmentTabAdapter(manager, mFragments, R.id.main_fragment_contain, mRadioGroup1);
        //当某个Fragment 需要先登录才能进去的时候，调用此事件（index=fragment的下标）
        //        mTabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
        //            @Override
        //            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId,
        //                                                 int index) {
        //                if (index == 4 && TextUtils.isEmpty(UserInfoUtils.getId(getApplicationContext()))
        //                        || index == 3 && TextUtils.isEmpty(UserInfoUtils.getId(getApplicationContext()))) {
        //                    startActivityForResult(new Intent(getApplicationContext(),
        //                            LoginActivity.class), 2);
        //                }
        //            }
        //        });

        mTvTitle2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTvTitle2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initShopPopup();
            }
        });

        getNewVersion();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String message = intent.getStringExtra("message");
        if (message != null && message.length() > 0) {
            ((RadioButton) mRadioGroup1.getChildAt(2)).setChecked(true);
        }
    }


    @OnClick({R.id.left_layout, R.id.tv_order, R.id.tv_distribution_order, R.id.tv_qs_order, R.id.tv_recharge_order, R.id.tv_title2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finishActivity();
                break;
            case R.id.tv_distribution_order:
                mTvDistributionOrder.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvQsOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvRechargeOrder.setTextColor(UIUtils.getColor(R.color.white));

                if (!orderMainFragment.fragments.get(0).isAdded()) {
                    FragmentTransaction ft = orderMainFragment.fm.beginTransaction();
                    ft.add(R.id.fl_comment, orderMainFragment.fragments.get(0));
                    ft.commit();
                } else {
                    orderMainFragment.showTab(0);
                }
                moveAnimation(mTvDistributionOrder);
                break;
            case R.id.tv_order:
                mTvOrder.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvDistributionOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvQsOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvRechargeOrder.setTextColor(UIUtils.getColor(R.color.white));

                if (!orderMainFragment.fragments.get(2).isAdded()) {
                    FragmentTransaction ft = orderMainFragment.fm.beginTransaction();
                    ft.add(R.id.fl_comment, orderMainFragment.fragments.get(2));
                    ft.commit();
                } else {
                    orderMainFragment.showTab(2);
                }
                moveAnimation(mTvOrder);
                break;
            case R.id.tv_qs_order:
                mTvQsOrder.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvDistributionOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvRechargeOrder.setTextColor(UIUtils.getColor(R.color.white));

                if (!orderMainFragment.fragments.get(1).isAdded()) {
                    FragmentTransaction ft = orderMainFragment.fm.beginTransaction();
                    ft.add(R.id.fl_comment, orderMainFragment.fragments.get(1));
                    ft.commit();
                } else {
                    orderMainFragment.showTab(1);
                }
                moveAnimation(mTvQsOrder);
                break;
            case R.id.tv_recharge_order://充值订单
                mTvQsOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvDistributionOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvRechargeOrder.setTextColor(UIUtils.getColor(R.color.text_blue));

                if (!orderMainFragment.fragments.get(3).isAdded()) {
                    FragmentTransaction ft = orderMainFragment.fm.beginTransaction();
                    ft.add(R.id.fl_comment, orderMainFragment.fragments.get(3));
                    ft.commit();
                } else {
                    orderMainFragment.showTab(3);
                }
                moveAnimation(mTvRechargeOrder);
                break;
            case R.id.tv_title2:
                showShopPopup();
                break;
        }
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.setDuration(400).start();
    }

    private void getNewVersion() {
        AsyncHttpUtil<NewVersionBean> httpUtil = new AsyncHttpUtil<>(this, NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean jsonBean) {
                //LogUtils.e("版本信息"+SystemUtil.getVersion(MainActivity.this),"--"+bean.getFlag());
                if ("200".equals(jsonBean.getCode()) && jsonBean.getData() != null) {
                    NewVersionBean bean = jsonBean.getData();
                    //判断版本
                    if (SystemUtil.getVersionCode(AdministratorMainActivity.this) < bean.getBv_version1()) {
                        if ("1".equals(bean.getBv_upgrade())) {//强制更新
                            updateDialog(bean);
                        } else if ("2".equals(bean.getBv_upgrade())) {
                            //本地已下载的安装包版本号
                            int SD_Version_code = AppUtil.apkInfo(BaseConfig.installPath, AdministratorMainActivity.this);
                            //系统返回的版本号
                            final int SYSTEM_VersionCode = bean.getBv_version1();
                            if (UserInfoUtils.getVersion_code(AdministratorMainActivity.this) == SYSTEM_VersionCode) {
                                //最新的版本号,当点击取消后,不会再弹框了
                            } else {
                                if (SD_Version_code == AppUtil.Overdue || SD_Version_code < SYSTEM_VersionCode) {
                                    updateDialog(bean);
                                } else if (SD_Version_code == SYSTEM_VersionCode) {
                                    DialogUtil.showVersionDialog(AdministratorMainActivity.this, "0", bean.getBv_version_name(), bean.getBv_desc(),
                                            "安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                                @Override
                                                public void ok() {
                                                    File file = new File(BaseConfig.installPath);
                                                    Intent i = new Intent();
                                                    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    i.setAction(Intent.ACTION_VIEW);
                                                    i.setDataAndType(FileUtils.getUriForFile(AdministratorMainActivity.this, file),
                                                            "application/vnd.android.package-archive");
                                                    startActivity(i);
                                                }

                                                @Override
                                                public void no() {
                                                    UserInfoUtils.setVersion_code(AdministratorMainActivity.this, SYSTEM_VersionCode);
                                                }
                                            });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        int versionCode = SystemUtil.getVersionCode(AdministratorMainActivity.this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(this) && NetUtils.isWifi(this) && !"1".equals(bean.getBv_upgrade())) {
            DownloadUtil.updataVersion(
                    UIUtils.getString(R.string.app_name),
                    AdministratorMainActivity.this,
                    bean.getBv_down_url(), "sss",
                    "sss.apk", false, bean.getBv_version_name(), bean.getBv_desc());
        } else {
            if ("1".equals(bean.getBv_upgrade())) {
                DialogUtil.showVersionDialog(this, bean.getBv_upgrade(), bean.getBv_version_name(), bean.getBv_desc(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                if (!NetUtils.isConnected(AdministratorMainActivity.this)) {
                                    Toast.makeText(AdministratorMainActivity.this, ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                showProgressDialog(AdministratorMainActivity.this, "下载中...");
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        AdministratorMainActivity.this,
                                        bean.getBv_down_url(), "sss",
                                        "sss.apk", true, bean.getBv_version_name(), bean.getBv_desc());
                            }

                            @Override
                            public void no() {

                            }
                        });
            } else {
                DialogUtil.showVersionDialog(this, bean.getBv_upgrade(), bean.getBv_version_name(), bean.getBv_desc(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        AdministratorMainActivity.this,
                                        bean.getBv_down_url(), "sss",
                                        "sss.apk", false, bean.getBv_version_name(), bean.getBv_desc());
                            }

                            @Override
                            public void no() {

                            }
                        });
            }
        }
    }

    private long exitTime;

    // 点击两次退出（2秒内）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                showToast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(getPackageName());
                AppManager.getInstance().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public RadioGroup getRadioGroup1() {
        return mRadioGroup1;
    }

    public LinearLayout getMainId() {
        return mLlMain;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < mRadioGroup1.getChildCount(); i++) {
            if (mRadioGroup1.getChildAt(i).getId() == checkedId) {
                mMainViewpage.setCurrentItem(i);
                switch (i) {
                    case 0:
                        //                        mTitleBar.setTitle("首页");
                        //                        mTitleBar.setVisibility(View.VISIBLE);
                        //                        mLlOrderTab.setVisibility(View.GONE);
                        if (shopBean != null) {
                            mTvTitle2.setText(shopBean.getMs_name());
                        }
                        mTitleBar.setVisibility(View.GONE);
                        mLlOrderTab.setVisibility(View.GONE);
                        mRlTitle2.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        showTitle();
                        mTitleBar.setTitle("订单");
                        mTitleBar.setVisibility(View.GONE);
                        mLlOrderTab.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        showTitle();
                        mTitleBar.setTitle("会员");
                        mTitleBar.setVisibility(View.VISIBLE);
                        mLlOrderTab.setVisibility(View.GONE);
                        break;
                    case 3:
                        showTitle();
                        mTitleBar.setTitle("我的");
                        mTitleBar.setVisibility(View.VISIBLE);
                        mLlOrderTab.setVisibility(View.GONE);
                        break;
                }
                break;
            }
        }
    }

    private void showTitle() {
        mTitleBar.setVisibility(View.VISIBLE);
        mRlTitle2.setVisibility(View.GONE);
    }

    private void initShopPopup() {
        popupShop = new SelectShopPopup(this, mTvTitle2, this);
        popupShop.setData(shopList);
    }

    private void showShopPopup() {
        popupShop.setData(shopList);
        popupShop.showAsDropDown(mTvTitle2, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onItem(final int i) {
        if (shopBean != null) {
            if (shopBean.getMs_id().equals(shopList.get(i).getMs_id())) {
                if (popupShop != null)
                    popupShop.dismiss();
                return;
            }
        }

        DialogUtil.showCustomDialog(this, "提示", "是否切换店铺?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                switchShop(i);
            }

            @Override
            public void no() {
            }
        });
    }

    private void switchShop(final int i) {
        AsyncHttpUtil<UserBean> httpUtil = new AsyncHttpUtil<>(this, UserBean.class, new IUpdateUI<UserBean>() {
            @Override
            public void updata(UserBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    UserBean bean = jsonBean.getData();
                    //店铺信息
                    mUserBean.setShopId(shopList.get(i).getMs_id());
                    mUserBean.setShopName(bean.getShopName());
                    mUserBean.setShopCusTel(bean.getShopCusTel());
                    mUserBean.setShopAddress(bean.getShopAddress());
                    //工作人员id
                    mUserBean.setWorkid(bean.getWorkid());
                    //车仓库id
                    mUserBean.setCarid(bean.getCarid());
                    mUserBean.setCarid_nameref(bean.getCarid_nameref());
                    mUserBean.setUsertype(bean.getUsertype());

                    UserInfoUtils.setUser(AdministratorMainActivity.this, mUserBean);

                    UserInfoUtils.setId(AdministratorMainActivity.this, bean.getWorkid());
                    UserInfoUtils.setOrgid(AdministratorMainActivity.this, bean.getOrgid());

                    mUserBean = UserInfoUtils.getUser(AdministratorMainActivity.this);
                    AppManager.getInstance().finishAllActivity();
                    finishActivity();
                    startActivity(SplashActivity.class);
                } else {
                    showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.switchShop, L_RequestParams.switchShop(shopList.get(i).getMs_id()), true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.e("onPageSelected", "onPageSelected:" + position);
        YesOrNoLoadingOnstart.INDEX_ID = position;
        YesOrNoLoadingOnstart.INDEX = true;
        Fragment fragment = mFragments.get(position);
        if (YesOrNoLoadingOnstart.INDEX == true) {
            fragment.onStart(); // 启动目标tab的onStart()
            //                       fragment.onResume(); // 启动目标tab的onResume()
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
