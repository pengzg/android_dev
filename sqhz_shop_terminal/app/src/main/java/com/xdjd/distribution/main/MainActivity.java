package com.xdjd.distribution.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.LoginActivity;
import com.xdjd.distribution.activity.MessageActivity;
import com.xdjd.distribution.activity.NearCustomerActivity;
import com.xdjd.distribution.activity.ReceiptPaymentActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.NewVersionBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.fragment.CustomerFragment;
import com.xdjd.distribution.main.fragment.HomeFragment;
import com.xdjd.distribution.main.fragment.OrdersFragment;
import com.xdjd.distribution.main.fragment.SettingFragment;
import com.xdjd.steward.adapter.PurchaseViewpageAdapter;
import com.xdjd.utils.AppManager;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.main_viewpage)
    NoScrollViewPager mMainViewpage;
    @BindView(R.id.rl_payment)
    RelativeLayout mRlPayment;
    @BindView(R.id.iv_payment_close)
    ImageView mIvPaymentClose;
    @BindView(R.id.rl_ysk)
    RelativeLayout mRlYsk;
    @BindView(R.id.rl_qk)
    RelativeLayout mRlQk;

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
    private AnimationSet animationShowSet;

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
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        PermissionUtils.requstPermission(this, 10, null);
        userBean = UserInfoUtils.getUser(this);
        mTitleBar.setTitleToLeft(userBean.getCompanyName() + "@" + userBean.getBud_name());

        initView();
        initShowAnimation();
        getNewVersion();
    }

    private void initView() {
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new OrdersFragment());
        mFragments.add(new CustomerFragment());
        mFragments.add(new SettingFragment());

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

    public RelativeLayout getMainId() {
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
                        int SD_Version_code = AppUtil.apkInfo(BaseConfig.installPath, MainActivity.this);
                        //系统返回的版本号
                        final int SYSTEM_VersionCode = Integer.parseInt(bean.getNewVersion());
                        if (UserInfoUtils.getVersion_code(MainActivity.this) == SYSTEM_VersionCode) {
                            //最新的版本号,当点击取消后,不会再弹框了
                        } else {
                            if (SD_Version_code == AppUtil.Overdue || SD_Version_code < SYSTEM_VersionCode) {
                                updateDialog(bean);
                            } else if (SD_Version_code == SYSTEM_VersionCode) {
                                DialogUtil.showVersionDialog(MainActivity.this, "0", bean.getNewVersionName(), bean.getUpdateContent(),
                                        "安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                            @Override
                                            public void ok() {
                                                File file = new File(BaseConfig.installPath);
                                                Intent i = new Intent();
                                                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.setAction(Intent.ACTION_VIEW);
                                                i.setDataAndType(FileUtils.getUriForFile(MainActivity.this, file),
                                                        "application/vnd.android.package-archive");
                                                startActivity(i);
                                            }

                                            @Override
                                            public void no() {
                                                UserInfoUtils.setVersion_code(MainActivity.this, SYSTEM_VersionCode);
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
        int versionCode = SystemUtil.getVersionCode(MainActivity.this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(userBean.getUserId(), String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(this) && NetUtils.isWifi(this) && !"1".equals(bean.getFlag())) {
            DownloadUtil.updataVersion(
                    UIUtils.getString(R.string.app_name),
                    MainActivity.this,
                    bean.getDownloadUrl(), "hezifenxiao",
                    "hezifenxiao.apk", false, bean.getNewVersionName(), bean.getUpdateContent());
        } else {
            if ("1".equals(bean.getFlag())) {
                DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                if (!NetUtils.isConnected(MainActivity.this)) {
                                    Toast.makeText(MainActivity.this, ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                showProgressDialog(MainActivity.this, "下载中...");
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        MainActivity.this,
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
                                        MainActivity.this,
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < mRadioGroup1.getChildCount(); i++) {
            if (mRadioGroup1.getChildAt(i).getId() == checkedId) {
                mMainViewpage.setCurrentItem(i);

                switch (i) {
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
                }
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

    @OnClick({R.id.iv_payment_close,R.id.rl_payment,R.id.rl_qk,R.id.rl_ysk})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_payment_close:
                startOpenAnimation(false);
                break;
            case R.id.rl_payment:
                startOpenAnimation(false);
                break;
            case R.id.rl_qk://欠款
                intent = new Intent(this,ReceiptPaymentActivity.class);
                intent.putExtra("receiptType",2);
                startActivity(intent);
                startOpenAnimation(false);
                break;
            case R.id.rl_ysk://预收款
                intent = new Intent(this,ReceiptPaymentActivity.class);
                intent.putExtra("receiptType",1);
                startActivity(intent);
                startOpenAnimation(false);
                break;
        }
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
            Intent intent = new Intent(MainActivity.this, NearCustomerActivity.class);
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

    /**
     * 初始化动画
     */
    private void initShowAnimation() {
        animationShowSet = new AnimationSet(this, null);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1f);
        alphaAnimation.setDuration(200);
        scaleAnimation.setDuration(200);
        animationShowSet.addAnimation(scaleAnimation);
        animationShowSet.addAnimation(alphaAnimation);
    }

    public void payment() {
        if (mRlPayment.getVisibility() == View.INVISIBLE) {
            mRlPayment.setVisibility(View.VISIBLE);
            startOpenAnimation(true);
        } else {
            startOpenAnimation(false);
        }
    }

    private void startOpenAnimation(final boolean b) {
        if (b) {
            if (mRlYsk.getVisibility() == View.INVISIBLE) {
                mRlYsk.setVisibility(View.VISIBLE);
                startTranstateAnimation(mRlYsk, true, 2);
            }
        } else {
            mRlPayment.setEnabled(false);
            startTranstateAnimation(mRlYsk, false, 2);
            startTranstateAnimation(mRlQk, false, 2);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
            alphaAnimation.setDuration(600);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRlPayment.setEnabled(true);
                    mRlPayment.setVisibility(View.INVISIBLE);

                    mRlYsk.setVisibility(View.INVISIBLE);
                    mRlQk.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mRlPayment.startAnimation(alphaAnimation);
        }
    }


    private void startTranstateAnimation(final View v, boolean open, int position) {
        if (open) {
            LogUtils.e("startTranstateAnimation", "Open.getY()=" + mRlYsk.getY() + ",v.getY()=" + v.getY());
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "y",
                    mRlYsk.getY(),
                    v.getY() - UIUtils.dp2px(20),
                    v.getY() + UIUtils.dp2px(10),
                    v.getY()).setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (v.getId() == mRlYsk.getId()) {
                        if (mRlQk.getVisibility() == View.INVISIBLE) {
                            mRlQk.setVisibility(View.VISIBLE);
                            startTranstateAnimation(mRlQk, true, 3);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mIvPaymentClose.setEnabled(true);
        } else {
            mIvPaymentClose.setEnabled(false);
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_PARENT, 0.4f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(500);
            animationSet.addAnimation(translateAnimation);
            animationSet.setFillAfter(false);
            v.startAnimation(animationSet);
        }
    }

}
