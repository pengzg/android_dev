package com.xdjd.storebox.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.PersonInfoActivity;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.base.BaseConfig;
import com.xdjd.storebox.bean.NewVersionBean;
import com.xdjd.storebox.mainfragment.AllFragment;
import com.xdjd.storebox.mainfragment.CartFragment;
import com.xdjd.storebox.mainfragment.HomePageFragment;
import com.xdjd.storebox.mainfragment.MeFragment;
import com.xdjd.storebox.mainfragment.PurchaseFragment;
import com.xdjd.utils.AppUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.FileUtils;
import com.xdjd.utils.FragmentTabAdapter;
import com.xdjd.utils.PublicFinal;
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
import com.xdjd.utils.runtimepermissions.PermissionUtils;
import com.xdjd.utils.update.DownloadUtil;
import com.xdjd.view.popup.SettingMessagePopopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;
    @BindView(R.id.main_button1)
    LinearLayout mMainButton1;
    @BindView(R.id.main_button2)
    LinearLayout mMainButton2;
    /*@BindView(R.id.main_btn_invisible)
    RelativeLayout mMainBtnInvisible;*/
    @BindView(R.id.main_button4)
    LinearLayout mMainButton4;
    @BindView(R.id.main_button5)
    LinearLayout mMainButton5;
    @BindView(R.id.main_bottom_ll)
    LinearLayout mMainBottomLl;
    @BindView(R.id.ylx_main_white_line)
    View mYlxMainWhiteLine;
    //    @BindView(R.id.main_relative3)
    //    RelativeLayout mMainRelative3;
    @BindView(R.id.ll)
    RelativeLayout mLl;
    @BindView(R.id.main_img3)
    ImageView mMainImg3;

    @BindView(R.id.main_tv1)
    TextView mainTv1;
    @BindView(R.id.main_tv2)
    TextView mainTv2;
    @BindView(R.id.main_tv3)
    TextView mainTv3;
    @BindView(R.id.main_tv4)
    TextView mainTv4;
    @BindView(R.id.main_tv5)
    TextView mainTv5;


    private boolean isSpeedy;//是否是快速注册进来的

    /**
     * Fragment的适配器
     */
    private FragmentTabAdapter mTabAdapter;
    /**
     * Fragment的集合
     */
    private List<Fragment> mFragments;

    // 主菜单按钮
    private View[] lls;
    private TextView[] tvLls;

    private FragmentManager fragmentManager; // Fragment所属的Activity

    private int currentTab; // 当前Tab页面索引

    //private onWindowFocusListener listener;

    //--------友盟推送代码--------
    PushAgent mPushAgent;

    private SettingMessagePopopWindow settingPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSystemBarColor(R.color.colorPrimaryDark);
        PermissionUtils.requestPermissions(this);
        initView();
        getNewVersion();
    }

    private void initView() {
        mPushAgent = PushAgent.getInstance(this);

        //添加友盟推送的Alias（支持一个alias对应多个devicetoken）：
        mPushAgent.addAlias(UserInfoUtils.getId(this), BaseConfig.Alias_Type, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
            }
        });
        //添加Alias（设置用户id和device_token的一一映射关系，确保同一个alias只对应一台设备）：
        //mPushAgent.setExclusiveAlias("zhangsan@sina.com", ALIAS_TYPE.SINA_WEIBO);
        //移除Alias
        //mPushAgent.deleteAlias("zhangsan@sina.com", ALIAS_TYPE.SINA_WEIBO);

        lls = new View[5];
        tvLls = new TextView[5];
        lls[0] = mMainButton1;
        lls[1] = mMainButton2;
        lls[2] = mMainImg3;
        lls[3] = mMainButton4;
        lls[4] = mMainButton5;

        tvLls[0] = mainTv1;
        tvLls[1] = mainTv2;
        tvLls[2] = mainTv3;
        tvLls[3] = mainTv4;
        tvLls[4] = mainTv5;

        for (int i = 0; i < lls.length; i++) {
            lls[i].setOnClickListener(this);
        }

        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomePageFragment());
        mFragments.add(new PurchaseFragment());
        mFragments.add(new AllFragment());
        //mFragments.add(new NetSingleFragment());
        mFragments.add(new CartFragment());
        mFragments.add(new MeFragment());

        fragmentManager = getSupportFragmentManager();

        lls[currentTab].setSelected(true);
        tvLls[0].setTextColor(UIUtils.getColor(R.color.color_EC193A));

        FragmentTransaction ft1 = fragmentManager.beginTransaction();
        ft1.add(R.id.fl_container, mFragments.get(1));
        ft1.addToBackStack(mFragments.get(1).getClass().getName());
        ft1.commit();
        getCurrentFragment().onStop();
        ft1.hide(mFragments.get(1));

        // 默认显示第一页
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fl_container, mFragments.get(currentTab));
        ft.addToBackStack(mFragments.get(currentTab).getClass().getName());
        ft.commit();

        isSpeedy = getIntent().getBooleanExtra("isSpeedy", false);
        if (isSpeedy) {
            DialogUtil.showSureDialog(MainActivity.this, "个人信息还没有完善，是否去完善信息？",
                    "去完善", new DialogUtil.MyCustomDialogListener3() {
                        @Override
                        public void sure() {
                            startActivity(PersonInfoActivity.class);
                        }
                    });
//            settingPopup = new SettingMessagePopopWindow(MainActivity.this, new SettingMessagePopopWindow.SettingPopupLisenter() {
//                @Override
//                public void settingPopupListener() {
//                    startActivity(PersonInfoActivity.class);
//                }
//            });
//            settingPopup.showAsDropDown(mLl, 0, 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PublicFinal.isSuccess) {
            //GetMeInfo();
        }
    }

    /**
     * 获取参数
     */
    /*private void GetMeInfo() {

        AsyncHttpUtil<PersonBean> httpUtil = new AsyncHttpUtil<>(this, PersonBean.class, new IUpdateUI<PersonBean>() {
            @Override
            public void updata(PersonBean bean) {
                if (bean.getRepCode().equals("00")) {
                    LogUtils.e("tag11", bean.getTagList().toString());
                    UserInfoUtils.setSpreadName(MainActivity.this, bean.getSpread_name());
                    UserInfoUtils.setSpreadMobile(MainActivity.this, bean.getSpreadMobile());
                    UserInfoUtils.setCenterShopName(MainActivity.this, bean.getCenterShopName());
                    UserInfoUtils.setCenterShopId(MainActivity.this, bean.getShopId());

                    if (!"".equals(bean.getUserShopId()) || bean.getUserShopId() != null) {
                        UserInfoUtils.setUserShopId(MainActivity.this, bean.getUserShopId());
                    }

                    PublicFinal.isSuccess = true;

                    String[] tabList;
                    if (UserInfoUtils.getTagList(MainActivity.this) == null && bean.getTagList().size()>0) {

                        tabList = bean.getTagList().toArray(new String[bean.getTagList().size()]);
                        UserInfoUtils.setTagList(MainActivity.this, bean.getTagList());

                        mPushAgent.getTagManager().add(new TagManager.TCallBack() {
                            @Override
                            public void onMessage(boolean b, ITagManager.Result result) {

                            }
                        }, tabList);
                    } else if (bean.getTagList().size()>0){

                        tabList = bean.getTagList().toArray(new String[bean.getTagList().size()]);

                        if (!UserInfoUtils.getTagList(MainActivity.this).toString().equals(
                                bean.getTagList().toString())) {

                            mPushAgent.getTagManager().update(new TagManager.TCallBack() {
                                @Override
                                public void onMessage(final boolean isSuccess, final ITagManager.Result result) {

                                }
                            }, tabList);
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
        httpUtil.post(M_Url.GetuserInfo, L_RequestParams.getUserInfo(
                UserInfoUtils.getId(this),UserInfoUtils.getId(this)), false);
    }*/
    private void setSelectFragment(int i) {
        for (int j = 0; j < lls.length; j++) {
            lls[j].setSelected(false);
        }
        for(int k = 0; k < tvLls.length; k++){
            tvLls[k].setTextColor(UIUtils.getColor(R.color.main_btn_false));
        }
        tvLls[i].setTextColor(UIUtils.getColor(R.color.color_EC193A));
        lls[i].setSelected(true);
        Fragment fragment = mFragments.get(i);
        FragmentTransaction ft = obtainFragmentTransaction(i);
        //                getCurrentFragment().onPause(); // 暂停当前tab
        getCurrentFragment().onStop(); // 暂停当前tab
        YesOrNoLoadingOnstart.INDEX_ID = i;
        YesOrNoLoadingOnstart.INDEX = true;
        if (fragment.isAdded()) {
            if (YesOrNoLoadingOnstart.INDEX == true) {
                fragment.onStart(); // 启动目标tab的onStart()
                //                       fragment.onResume(); // 启动目标tab的onResume()
            }
        } else {
            ft.addToBackStack(fragment.getClass().getName());
            ft.add(R.id.fl_container, fragment);
        }
        showTab(i); // 显示目标tab
        ft.commit();
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(currentTab);
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            // ft.setCustomAnimations(R.anim.main_anim_in, R.anim.main_anim_in);
        } else {
            // ft.setCustomAnimations(R.anim.main_anim_out, R.anim.main_anim_in);
        }
        return ft;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_button1:
                setSelectFragment(0);
                break;
            case R.id.main_button2:
                setSelectFragment(1);
                break;
            case R.id.main_img3:
                setSelectFragment(2);
                break;
            case R.id.main_button4:
                setSelectFragment(3);
                break;
            case R.id.main_button5:
                setSelectFragment(4);
                //Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                //startActivity(intent);
                break;
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
                //AppManager.getInstance().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 跳转到采购界面
     *
     * @param type 0:统配进货; 1:直配进货
     */
    public void toPurchase(int type) {
        setSelectFragment(1);
        PurchaseFragment purchaseFragment = (PurchaseFragment) mFragments.get(1);
        purchaseFragment.setTab(type);
    }

    /**
     * 获取采购实例
     *
     * @return
     */
    public PurchaseFragment getPurchase() {
        PurchaseFragment fragment = (PurchaseFragment) mFragments.get(1);
        return fragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra("currentTab", 0);
        setSelectFragment(index);
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
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(UserInfoUtils.getId(this),
                String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(this) && NetUtils.isWifi(this) && !"1".equals(bean.getFlag())) {
            DownloadUtil.updataVersion(
                    "小店盒子",
                    MainActivity.this,
                    bean.getDownloadUrl(), "xiaodianhezi",
                    "xiaodianhezi.apk", false, bean.getNewVersionName(), bean.getUpdateContent());
        } else {
            if ("1".equals(bean.getFlag())) {
                DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                showProgressDialog("下载中...");
                                DownloadUtil.updataVersion(
                                        "小店盒子",
                                        MainActivity.this,
                                        bean.getDownloadUrl(), "xiaodianhezi",
                                        "xiaodianhezi.apk", true, bean.getNewVersionName(), bean.getUpdateContent());
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
                                        "小店盒子",
                                        MainActivity.this,
                                        bean.getDownloadUrl(), "xiaodianhezi",
                                        "xiaodianhezi.apk", false, bean.getNewVersionName(), bean.getUpdateContent());
                            }

                            @Override
                            public void no() {
                            }
                        });
            }
        }

    }
}
