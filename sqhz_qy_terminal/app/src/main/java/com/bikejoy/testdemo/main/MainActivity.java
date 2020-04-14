package com.bikejoy.testdemo.main;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
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
import com.bikejoy.utils.permissions.PermissionUtils;
import com.bikejoy.view.NoScrollViewPager;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseActivity;
import com.bikejoy.testdemo.base.BaseConfig;
import com.bikejoy.testdemo.base.Common;
import com.bikejoy.testdemo.event.UpdateMessageNumEvent;
import com.bikejoy.testdemo.main.fragment.CRMFragment;
import com.bikejoy.testdemo.main.fragment.MeFragment;
import com.bikejoy.testdemo.main.fragment.MessageFragment;
import com.bikejoy.testdemo.main.fragment.SalesmanHomePagerFragment;
import com.bikejoy.testdemo.popup.SelectShopPopup;
import com.bikejoy.testdemo.service.DownloadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, SelectShopPopup.ItemOnListener, DownloadService.DownloadCallback {

    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.main_viewpage)
    NoScrollViewPager mMainViewpage;

    /**
     * Fragment的集合
     */
    public List<Fragment> mFragments;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.right_left_image)
    ImageView mRightLeftImage;
    @BindView(R.id.right_image)
    ImageView mRightImage;
    @BindView(R.id.tv_lefttext_titlebar)
    TextView mTvLefttextTitlebar;
    @BindView(R.id.rl_titlte)
    RelativeLayout mRlTitlte;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.rl_message)
    RelativeLayout mRlMessage;
    @BindView(R.id.tv_message_num)
    TextView mTvMessageNum;
    @BindView(R.id.main_tab_stock)
    RadioButton mMainTabStock;
    @BindView(R.id.main_tab_home)
    RadioButton mMainTabHome;

    private PurchaseViewpageAdapter viewpageAdapter;

    private UserBean mUserBean;
    private List<ShopListBean> shopList;
    private ShopListBean shopBean;

    private MyStorehouseClickListener listenerStorehouse;
    private MyMapClickListener listenerMap;
    private MeClickListener listenerMe;

    private SelectShopPopup popupShop;

    private MyConnection conn;
    private DownloadService downloadService;//版本下载service
    private ProgressDialog progressDialog;

    /**
     * 版本更新参数
     */
    NewVersionBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        //        mRlMessage.setVisibility(View.VISIBLE);
        mRlRightQr.setVisibility(View.VISIBLE);
        mRlRightSearch.setVisibility(View.VISIBLE);

        //获取权限
        PermissionUtils.requstPermission(this, 10, null);
        //repertory
        mUserBean = UserInfoUtils.getUser(this);
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

        mTvTitle2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTvTitle2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initShopPopup();
            }
        });

        listenerStorehouse = new MyStorehouseClickListener();
        listenerMap = new MyMapClickListener();
        listenerMe = new MeClickListener();

        mFragments = new ArrayList<>();

        switch (mUserBean.getRoleCode()) {
            case Common.ROLE3001://开发员
                mRadioGroup1.removeView(mMainTabHome);

                mMainTabHome.setVisibility(View.GONE);
                mFragments.add(new CRMFragment());
                mFragments.add(new MessageFragment());
                mFragments.add(new MeFragment());
                break;
            case Common.ROLE3002://开发主管
                mMainTabHome.setVisibility(View.VISIBLE);
                mFragments.add(new SalesmanHomePagerFragment());
                mFragments.add(new CRMFragment());
                mFragments.add(new MessageFragment());
                mFragments.add(new MeFragment());
                break;
            case Common.ROLE3006://库管
                mRadioGroup1.removeView(mMainTabHome);

                mMainTabHome.setVisibility(View.GONE);
                mFragments.add(new CRMFragment());
                mFragments.add(new MessageFragment());
                mFragments.add(new MeFragment());
                break;
            case Common.ROLE3008:
            case Common.ROLE3009:
            case Common.ROLE3011:
                mMainTabHome.setVisibility(View.VISIBLE);
                mFragments.add(new SalesmanHomePagerFragment());
                mFragments.add(new CRMFragment());
                mFragments.add(new MessageFragment());
                mFragments.add(new MeFragment());
                break;
            default:
                mRadioGroup1.removeView(mMainTabHome);
                mMainTabHome.setVisibility(View.GONE);

                mFragments.add(new CRMFragment());
                mFragments.add(new MessageFragment());
                mFragments.add(new MeFragment());
                break;
        }


        viewpageAdapter = new PurchaseViewpageAdapter(this.getSupportFragmentManager(), mFragments);
        mMainViewpage.setAdapter(viewpageAdapter);
        mMainViewpage.setCurrentItem(0);
        mMainViewpage.setOnPageChangeListener(this);
        mRadioGroup1.setOnCheckedChangeListener(this);
        YesOrNoLoadingOnstart.INDEX_ID = 0;
        YesOrNoLoadingOnstart.INDEX = true;


        ((RadioButton) mRadioGroup1.getChildAt(0)).setChecked(true);
        mMainTabStock.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //设置消息数量显示位置
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTvMessageNum.getLayoutParams();
                lp.leftMargin = mMainTabStock.getLeft() + (mMainTabStock.getWidth() / 3 * 2);
                mTvMessageNum.setLayoutParams(lp);
            }
        });

        conn = new MyConnection();
        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void progress(int progress) {
        LogUtils.e("progress", "progress:" + progress);
        if (progressDialog != null) {
            progressDialog.setProgress(progress);
        }
    }

    @Override
    public void failure(String msg) {
        LogUtils.e("failure", "failure:" + msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disProgressDialog(MainActivity.this);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void success(final File file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                DialogUtil.showVersionDialog(MainActivity.this, "0",
                        bean.getBv_version_name(), bean.getBv_desc(),
                        null, null, new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                installApk(file);
                            }

                            @Override
                            public void no() {

                            }
                        });
            }
        });
    }

    private void installApk(File file) {
        Uri downloadFileUri = FileUtils.getUriForFile(this, file);
        if (downloadFileUri != null) {
            Intent i = new Intent();
            i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            startActivity(i);
        } else {
            DialogUtil.showCustomDialog(this, "提示", "下载失败,请在->我的->点击<版本号>重试", "确定", "取消", null);
        }
        disProgressDialog(this);
    }

    /**
     * 下载service连接
     */
    class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DownloadService.DownloadBinder downloadBinder = (DownloadService.DownloadBinder) iBinder;
            downloadService = downloadBinder.getService();
            getNewVersion1();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            showToast("连接失败");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String message = intent.getStringExtra("message");
        if (message != null && message.length() > 0) {
            ((RadioButton) mRadioGroup1.getChildAt(4)).setChecked(true);
        }
    }

    private void getNewVersion1() {
        AsyncHttpUtil<NewVersionBean> httpUtil = new AsyncHttpUtil<>(this, NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean jsonBean) {
                if ("200".equals(jsonBean.getCode()) && jsonBean.getData() != null) {
                    bean = jsonBean.getData();
                    //判断版本
                    if (SystemUtil.getVersionCode(MainActivity.this) < bean.getBv_version1()) {
                        if ("1".equals(bean.getBv_upgrade())) {//强制更新
                            updateDialog(bean);
                        } else if ("2".equals(bean.getBv_upgrade())) {
                            //本地已下载的安装包版本号
                            int SD_Version_code = AppUtil.apkInfo(BaseConfig.installPath, MainActivity.this);
                            //系统返回的版本号
                            final int SYSTEM_VersionCode = bean.getBv_version1();
                            if (UserInfoUtils.getVersion_code(MainActivity.this) == SYSTEM_VersionCode) {
                                //最新的版本号,当点击取消后,不会再弹框了
                            } else {
                                if (SD_Version_code == AppUtil.Overdue || SD_Version_code < SYSTEM_VersionCode) {
                                    updateDialog(bean);
                                } else if (SD_Version_code == SYSTEM_VersionCode) {
                                    updateDialog(bean);
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
        int versionCode = SystemUtil.getVersionCode(MainActivity.this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(this) && NetUtils.isWifi(this) && !"1".equals(bean.getBv_upgrade())) {
            //wifi情况下自动更新
            downloadService.threadDownload(bean.getBv_down_url(), this);
        } else {
            if ("1".equals(bean.getBv_upgrade())) { //强制更新
                DialogUtil.showVersionDialog(this, bean.getBv_upgrade(), bean.getBv_version_name(), bean.getBv_desc(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                if (!NetUtils.isConnected(MainActivity.this)) {
                                    Toast.makeText(MainActivity.this, ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                showProgressBar();
                                downloadService.threadDownload(bean.getBv_down_url(), MainActivity.this);
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
                                showProgressBar();
                                downloadService.threadDownload(bean.getBv_down_url(), MainActivity.this);
                            }

                            @Override
                            public void no() {

                            }
                        });
            }
        }
    }

    private void showProgressBar() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIcon(R.mipmap.logo);
        progressDialog.setTitle("版本更新");
        progressDialog.setMessage("正在下载中");
        progressDialog.setMax(100);
        //ProgressDialog.STYLE_SPINNER  默认进度条是转圈
        //ProgressDialog.STYLE_HORIZONTAL  横向进度条
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();
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
                switch (checkedId) {
                    case R.id.main_tab_home:
                        showTitle();
                        mTitle.setText("首页");

                        if (shopBean != null) {
                            mTvTitle2.setText(shopBean.getMs_name());
                        }
                        mRlRightQr.setVisibility(View.GONE);
                        mRightImage.setImageResource(R.mipmap.scan_white);
                        mRlRightSearch.setVisibility(View.GONE);
                        mRlRightQr.setOnClickListener(listenerStorehouse);
                        break;
                    case R.id.main_tab_crm:
                        showTitle();
                        mTitle.setText("办公");

                        mRlRightQr.setVisibility(View.GONE);
                        mRightImage.setImageResource(R.mipmap.scan_white);
                        mRlRightSearch.setVisibility(View.GONE);

                        mRlRightSearch.setOnClickListener(listenerMap);
                        mRlRightQr.setOnClickListener(listenerStorehouse);
                        break;
                    case R.id.main_tab_stock:
                        showTitle();
                        mTitle.setText("消息");
                        mRlRightQr.setVisibility(View.GONE);
                        mRightImage.setImageResource(R.mipmap.scan_white);
                        mRlRightSearch.setVisibility(View.GONE);

                        mRlRightQr.setOnClickListener(listenerStorehouse);
                        break;
                    case R.id.main_tab_me:
                        showTitle();
                        mTitle.setText("我的");
                        mRlRightQr.setVisibility(View.GONE);
                        mRightImage.setImageResource(R.mipmap.scan_white);
                        mRlRightSearch.setVisibility(View.GONE);

                        mRlRightQr.setOnClickListener(listenerStorehouse);
                        break;
                }
                break;
            }
        }
    }

    private void showTitle() {
        mTitle.setVisibility(View.VISIBLE);
        mTvTitle2.setVisibility(View.GONE);
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

    @OnClick({R.id.tv_title2, R.id.rl_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title2:
                showShopPopup();
                break;
            case R.id.rl_message:
                startActivity(MessageActivity.class);
                break;
        }
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
                    //重新绑定推送别名
                    bindAlias(mUserBean);

                    UserInfoUtils.setUser(MainActivity.this, mUserBean);

                    UserInfoUtils.setId(MainActivity.this, bean.getWorkid());
                    UserInfoUtils.setOrgid(MainActivity.this, bean.getOrgid());

                    mUserBean = UserInfoUtils.getUser(MainActivity.this);
                    LogUtils.e("用户id", bean.getWorkid() + "--用户id" + mUserBean.getWorkid());
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

    private void bindAlias(final UserBean bean) {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //别名增加，将某一类型的别名ID绑定至某设备，老的绑定设备信息还在，别名ID和device_token是一对多的映射关系
        //        mPushAgent.addAlias("别名ID", "自定义类型", new UTrack.ICallBack() {
        //            @Override
        //            public void onMessage(boolean isSuccess, String message) {
        //            }
        //        });
        //先删除旧的别名,再添加新的别名
        mPushAgent.deleteAlias(bean.getWorkid(), BaseConfig.Alias_Type, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                LogUtils.e("先删除别名", "isSuccess:" + isSuccess + "--message:" + message);
            }
        });

        //别名绑定，将某一类型的别名ID绑定至某设备，老的绑定设备信息被覆盖，别名ID和deviceToken是一对一的映射关系
        mPushAgent.setAlias(bean.getWorkid(), BaseConfig.Alias_Type, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                LogUtils.e("别名", bean.getWorkid());
                LogUtils.e("绑定别名", "isSuccess:" + isSuccess + "--message:" + message);
            }
        });
    }

    public class MyMapClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            PermissionUtils.requstActivityLocation(MainActivity.this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(MainActivity.this, MapStoreActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onDilogCancal() {
                    showToast("获取位置权限失败!");
                }
            });
        }
    }

    public class MyStorehouseClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mUserBean.getCarid() == null || mUserBean.getCarid().length() == 0) {
                DialogUtil.showCustomDialog(MainActivity.this, "提示",
                        "没有车仓库无法补货,请联系后台管理员", "确定", null, null);
                return;
            }
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivity(intent);
           /* PermissionUtils.requstActivityCamera(MainActivity.this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onDilogCancal() {
                    showToast("获取拍照权限失败!");
                }
            });*/
        }
    }

    public class MeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MeActivity.class);
            startActivity(intent);
        }
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

//    private void setBottomNavigationItem(int space, int imgLen) {
//        float contentLen = 36;
//        Class barClass = mBottomNavBar.getClass();
//        Field[] fields = barClass.getDeclaredFields();
//        for (int i = 0; i < fields.length; i++) {
//            Field field = fields[i];
//            field.setAccessible(true);
//            if (field.getName().equals("mTabContainer")) {
//                try { //反射得到 mTabContainer
//                    LinearLayout mTabContainer = (LinearLayout) field.get(mBottomNavBar);
//                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
//                        //获取到容器内的各个 Tab
//                        View view = mTabContainer.getChildAt(j);
//                        //获取到Tab内的各个显示控件
//                        // 获取到Tab内的文字控件
//                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
//                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
//                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
//                        //获取到Tab内的图像控件
//                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
//                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
//                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) UIUtils.dp2px(imgLen), (int) UIUtils.dp2px(imgLen));
//                        params.gravity = Gravity.CENTER;
//                        iconView.setLayoutParams(params);
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public void onEventMainThread(UpdateMessageNumEvent event) {
        if (!TextUtils.isEmpty(event.getNum()) && !"0".equals(event.getNum())) {
            mTvMessageNum.setVisibility(View.VISIBLE);
            mTvMessageNum.setText(event.getNum());
        } else {
            mTvMessageNum.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
