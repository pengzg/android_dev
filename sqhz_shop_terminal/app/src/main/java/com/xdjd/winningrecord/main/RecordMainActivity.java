package com.xdjd.winningrecord.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.LoginActivity;
import com.xdjd.distribution.activity.MessageActivity;
import com.xdjd.distribution.activity.NearCustomerActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.NewVersionBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.steward.activity.SettingActivity;
import com.xdjd.steward.adapter.PurchaseViewpageAdapter;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.utils.AppUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.FileUtils;
import com.xdjd.utils.FragmentTabAdapter;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
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
import com.xdjd.winningrecord.main.fragment.ActionFragment;
import com.xdjd.winningrecord.main.fragment.AuditRecordFragment;
import com.xdjd.winningrecord.main.fragment.MerchantsFragment;
import com.xdjd.winningrecord.main.fragment.UserFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordMainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.main_viewpage)
    NoScrollViewPager mMainViewpage;
    @BindView(R.id.ll_action)
    LinearLayout mLlAction;

    private UserBean userBean;

    /**
     * Fragment的集合
     */
    public List<Fragment> mFragments;
    /**
     * Fragment的适配器
     */
    public FragmentTabAdapter mTabAdapter;

    private PurchaseViewpageAdapter viewpageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //        savedInstanceState = null;
        //        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        //svn://182.92.180.219/src/sqkx/source/truck/sqkx_box/app/android2/safemanager
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_record_main;
    }

    @Override
    protected void initData() {
        super.initData();
        PermissionUtils.requstPermission(this, 10, null);
        userBean = UserInfoUtils.getUser(this);
        mTitleBar.setTitleToLeft(userBean.getCompanyName() + "@" + userBean.getBud_name());
        mTitleBar.setRightImageResource(R.drawable.setting);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.class);
            }
        });

        initView();
    }

    @OnClick({R.id.ll_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_action:
                Intent intent = new Intent(RecordMainActivity.this,StewardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    private void initView() {
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new UserFragment());
        mFragments.add(new ActionFragment());
        mFragments.add(new MerchantsFragment());
        mFragments.add(new AuditRecordFragment());

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
    }

    public LinearLayout getMainId() {
        return mActivityMain;
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
                        int SD_Version_code = AppUtil.apkInfo(BaseConfig.installPath, RecordMainActivity.this);
                        //系统返回的版本号
                        final int SYSTEM_VersionCode = Integer.parseInt(bean.getNewVersion());
                        if (UserInfoUtils.getVersion_code(RecordMainActivity.this) == SYSTEM_VersionCode) {
                            //最新的版本号,当点击取消后,不会再弹框了
                        } else {
                            if (SD_Version_code == AppUtil.Overdue || SD_Version_code < SYSTEM_VersionCode) {
                                updateDialog(bean);
                            } else if (SD_Version_code == SYSTEM_VersionCode) {
                                DialogUtil.showVersionDialog(RecordMainActivity.this, "0", bean.getNewVersionName(), bean.getUpdateContent(),
                                        "安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                            @Override
                                            public void ok() {
                                                File file = new File(BaseConfig.installPath);
                                                Intent i = new Intent();
                                                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.setAction(Intent.ACTION_VIEW);
                                                i.setDataAndType(FileUtils.getUriForFile(RecordMainActivity.this, file),
                                                        "application/vnd.android.package-archive");
                                                startActivity(i);
                                            }

                                            @Override
                                            public void no() {
                                                UserInfoUtils.setVersion_code(RecordMainActivity.this, SYSTEM_VersionCode);
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
        int versionCode = SystemUtil.getVersionCode(RecordMainActivity.this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(userBean.getUserId(), String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(this) && NetUtils.isWifi(this) && !"1".equals(bean.getFlag())) {
            DownloadUtil.updataVersion(
                    UIUtils.getString(R.string.app_name),
                    RecordMainActivity.this,
                    bean.getDownloadUrl(), "hezifenxiao",
                    "hezifenxiao.apk", false, bean.getNewVersionName(), bean.getUpdateContent());
        } else {
            if ("1".equals(bean.getFlag())) {
                DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                if (!NetUtils.isConnected(RecordMainActivity.this)) {
                                    Toast.makeText(RecordMainActivity.this, ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                showProgressDialog(RecordMainActivity.this, "下载中...");
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        RecordMainActivity.this,
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
                                        RecordMainActivity.this,
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

    public RadioGroup getRadioGroup1() {
        return mRadioGroup1;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < mRadioGroup1.getChildCount(); i++) {
            if (mRadioGroup1.getChildAt(i).getId() == checkedId) {
                mMainViewpage.setCurrentItem(i);

               /* switch (i) {
                    case 0:
                        mTitleBar.setRightImageResource(R.mipmap.message);
                        mTitleBar.setRightLayoutClickListener(homeListener);
                        break;
                    case 2:
                        mTitleBar.setRightImageResource(R.mipmap.add_client);
                        mTitleBar.setRightLayoutClickListener(cusListener);
                        break;
                    case 1:
                    case 3:
                        mTitleBar.setRightLayoutVisibility(View.GONE);
                        break;
                }*/
            }
        }
    }

    private MyHomeClickListener homeListener = new MyHomeClickListener();
    private MyCusClickListener cusListener = new MyCusClickListener();

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

    public class MyHomeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(MessageActivity.class);
        }
    }

    public class MyCusClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //            Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
            Intent intent = new Intent(RecordMainActivity.this, NearCustomerActivity.class);
            startActivityForResult(intent, 1);
        }
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

}
