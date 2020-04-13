package com.xdjd.steward.main;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.LoginActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.NewVersionBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.steward.activity.SettingActivity;
import com.xdjd.steward.adapter.PurchaseViewpageAdapter;
import com.xdjd.steward.main.fragment.CustomerFragment;
import com.xdjd.steward.main.fragment.GoodsFragment;
import com.xdjd.steward.main.fragment.HomeFragment;
import com.xdjd.steward.main.fragment.SalesmanFragment;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.AppUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.FileUtils;
import com.xdjd.utils.FragmentTabAdapter;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.NetUtils;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.utils.update.DownloadUtil;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollViewPager;
import com.xdjd.winningrecord.main.RecordMainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StewardActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    //    @BindView(R.id.main_fragment_contain)
    //    FrameLayout mMainFragmentContain;
    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;
    @BindView(R.id.main_viewpage)
    NoScrollViewPager mMainViewpage;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.ll_action)
    LinearLayout mLlAction;

    /**
     * Fragment的集合
     */
    private List<Fragment> mFragments;
    /**
     * Fragment的适配器
     */
    public FragmentTabAdapter mTabAdapter;

    private UserBean userBean;

    /**
     * 线路dao
     */
    private LineDao lineDao;

    private PurchaseViewpageAdapter viewpageAdapter;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //        savedInstanceState = null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_steward;
    }

    @Override
    protected void initData() {
        super.initData();
        PermissionUtils.requestPermissions(this);
        initView();
        getNewVersion();
    }

    private void initView() {
        lineDao = new LineDao(this);
        userBean = UserInfoUtils.getUser(this);
        mTitleBar.setTitleToLeft(userBean.getCompanyName() + "@" + userBean.getBud_name());
        mTitleBar.setRightImageResource(R.drawable.setting);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.class);
            }
        });

        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new SalesmanFragment());
        mFragments.add(new CustomerFragment());
        mFragments.add(new GoodsFragment());

        viewpageAdapter = new PurchaseViewpageAdapter(this.getSupportFragmentManager(), mFragments);
        mMainViewpage.setAdapter(viewpageAdapter);
        mMainViewpage.setCurrentItem(0);
        //        mRadioGroup1.check(mRadioGroup1.getChildAt(0).getId());
        mRadioGroup1.setOnCheckedChangeListener(this);
        ((RadioButton) mRadioGroup1.getChildAt(0)).setChecked(true);

        //        FragmentManager manager = getSupportFragmentManager();
        //        mTabAdapter = new FragmentTabAdapter(manager, mFragments, R.id.main_fragment_contain, mRadioGroup1);
        //当某个Fragment 需要先登录才能进去的时候，调用此事件（index=fragment的下标）
       /* mTabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId,
                                                 int index) {
                //                if (index == 4 && TextUtils.isEmpty(UserInfoUtils.getId(getApplicationContext()))
                //                        || index == 3 && TextUtils.isEmpty(UserInfoUtils.getId(getApplicationContext()))) {
                //                    startActivityForResult(new Intent(getApplicationContext(),
                //                            LoginActivity.class), 2);
                //                }
            }
        });*/
        getUserLineOrSettingInfo();
    }

    public LinearLayout getMainId() {
        return mActivityMain;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果日期不等于今天日期也需要从新登录
        if (!UserInfoUtils.getLoginDate(this).equals(StringUtils.getDate2())) {
            UserInfoUtils.setLoginState(this, "0");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 获取用户配置信息
     */
    private void getUserLineOrSettingInfo() {
        AsyncHttpUtil<UserBean> httpUtil = new AsyncHttpUtil<>(this, UserBean.class, new IUpdateUI<UserBean>() {
            @Override
            public void updata(UserBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    userBean.setInPhoto(bean.getInPhoto());
                    userBean.setOutPhoto(bean.getOutPhoto());
                    userBean.setIsScan(bean.getIsScan());
                    userBean.setIsChangPrice(bean.getIsChangPrice());
                    userBean.setIsChangeThPrice(bean.getIsChangeThPrice());
                    userBean.setIsQueryStock(bean.getIsQueryStock());
                    userBean.setIsChangeYH(bean.getIsChangeYH());
                    userBean.setSkType(bean.getSkType());
                    userBean.setIsAllowSign(bean.getIsAllowSign());
                    userBean.setSignDistance(bean.getSignDistance());
                    userBean.setIsReLocation(bean.getIsReLocation());
                    userBean.setRefundDays(bean.getRefundDays());
                    userBean.setRefundMode(bean.getRefundMode());
                    userBean.setIsShopAccount(bean.getIsShopAccount());
                    userBean.setIsSendSms(bean.getIsSendSms());
                    userBean.setIsSign(bean.getIsSign());//是否需要手动签名

                    //设置默认签到距离
                    UserInfoUtils.setSignDistance(StewardActivity.this, bean.getSignDistance());
                    //设置线路信息
                    lineDao.batchInsert(bean.getLineList());//添加线路信息
                    UserInfoUtils.setUser(StewardActivity.this, userBean);
                } else {
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getUserLineOrSettingInfo, L_RequestParams.getUserLineOrSettingInfo(), true);
    }

    private void getNewVersion() {
        AsyncHttpUtil<NewVersionBean> httpUtil = new AsyncHttpUtil<>(this, NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean bean) {
                //LogUtils.e("版本信息"+SystemUtil.getVersion(MainActivity.this),"--"+bean.getFlag());
                if (bean.getRepCode().equals("00")) {

                    if ("1".equals(bean.getFlag())) {//强制更新
                        updateDialog(bean);
                    } else if ("2".equals(bean.getFlag())) {
                        //本地已下载的安装包版本号
                        int SD_Version_code = AppUtil.apkInfo(BaseConfig.installPath, StewardActivity.this);
                        //系统返回的版本号
                        final int SYSTEM_VersionCode = Integer.parseInt(bean.getNewVersion());
                        if (UserInfoUtils.getVersion_code(StewardActivity.this) == SYSTEM_VersionCode) {
                            //最新的版本号,当点击取消后,不会再弹框了
                        } else {
                            if (SD_Version_code == AppUtil.Overdue || SD_Version_code < SYSTEM_VersionCode) {
                                updateDialog(bean);
                            } else if (SD_Version_code == SYSTEM_VersionCode) {
                                DialogUtil.showVersionDialog(StewardActivity.this, "0", bean.getNewVersionName(), bean.getUpdateContent(),
                                        "安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                            @Override
                                            public void ok() {
                                                File file = new File(BaseConfig.installPath);
                                                Intent i = new Intent();
                                                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.setAction(Intent.ACTION_VIEW);
                                                i.setDataAndType(FileUtils.getUriForFile(StewardActivity.this, file),
                                                        "application/vnd.android.package-archive");
                                                startActivity(i);
                                            }

                                            @Override
                                            public void no() {
                                                UserInfoUtils.setVersion_code(StewardActivity.this, SYSTEM_VersionCode);
                                            }
                                        });
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
        int versionCode = SystemUtil.getVersionCode(StewardActivity.this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(userBean.getUserId(), String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(this) && NetUtils.isWifi(this) && !"1".equals(bean.getFlag())) {
            DownloadUtil.updataVersion(
                    UIUtils.getString(R.string.app_name),
                    StewardActivity.this,
                    bean.getDownloadUrl(), "hezifenxiao",
                    "hezifenxiao.apk", false, bean.getNewVersionName(), bean.getUpdateContent());
        } else {
            if ("1".equals(bean.getFlag())) {
                DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                showProgressDialog(StewardActivity.this, "下载中...");
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        StewardActivity.this,
                                        bean.getDownloadUrl(), "hezifenxiao",
                                        "hezifenxiao.apk", true, bean.getNewVersionName(), bean.getUpdateContent());
                            }

                            @Override
                            public void no() {

                            }
                        });
            } else {
                DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        StewardActivity.this,
                                        bean.getDownloadUrl(), "hezifenxiao",
                                        "hezifenxiao.apk", false, bean.getNewVersionName(), bean.getUpdateContent());
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
                finishActivity();
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(getPackageName());
                AppManager.getInstance().finishAllActivity();
                System.exit(0);

                UserInfoUtils.setLoginState(this, "0");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public RadioGroup getRadioGroup1() {
        return mRadioGroup1;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < mRadioGroup1.getChildCount(); i++) {
            if (mRadioGroup1.getChildAt(i).getId() == checkedId) {
                mMainViewpage.setCurrentItem(i);
            }
        }
    }

    @OnClick({R.id.ll_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_action://跳转活动页面
                startActivity(RecordMainActivity.class);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lineDao.destroy();
    }
}
