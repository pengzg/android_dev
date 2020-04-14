package com.bikejoy.testdemo.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.umeng.message.PushAgent;
import com.bikejoy.utils.AppManager;
import com.bikejoy.utils.SystemBarTintManager;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.view.CustomProgress;

import butterknife.ButterKnife;


/**
 * Created by lijipei on 2016/11/15.
 */

public abstract class BaseActivity extends FragmentActivity {

    /**
     * 键盘管理对象
     */
    protected InputMethodManager manager;
    protected NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //去掉actionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AppManager.getInstance().addActivity(this);
        //        setSystemBarColor(R.color.title_bg);
        PushAgent.getInstance(this).onAppStart();

        initData();
    }

    protected abstract int getContentView();

    protected void initData() {
    }

    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "客户定位需要打开GPS功能",
                    Toast.LENGTH_SHORT).show();
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 12808); // 设置完成后返回到原来的界面
        } else {
        }
    }

    /**
     * 修改通知栏颜色方法
     *
     * @param color
     */
    public void setSystemBarColor(int color) {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    /**
     * 设置状态
     */
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 跳转一个界面不传递数据
     *
     * @param clazz
     */
    public void startActivity(Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }


    public void showToast(String info) {
        Toast toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示Log
     */
    public void log(String toast) {
        Log.e("TAG", toast);
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        isShowDismissDialog();

        super.onDestroy();
        AppManager.getInstance().finishActivity(this.getClass());
        AsyncHttpUtil.cancelRequests(this,true);
    }

    /**
     * 下拉刷新和上拉加载的提示信息
     */
    public void initRefresh(PullToRefreshBase my_scroll) {
        ILoadingLayout startLabels = my_scroll.getLoadingLayoutProxy(true,
                false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = my_scroll.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    public void showProgressDialog(Context context) {
        CustomProgress.show(context, "加载中...", true, null);
    }

    /**
     * 自己填写信息的ProgressDialog
     */
    public void showProgressDialog(Context context, String message) {
        if (message == null || message.equals("")) {
            showProgressDialog(context);
        } else {
            CustomProgress.show(this, message, true, null);
        }
    }

    /**
     * 停止progressDialog
     */
    public void disProgressDialog(Context context) {
        CustomProgress.hideProgress(context);
    }

    public void isShowDismissDialog(){
        CustomProgress.isShowDismissDialog();
    }

    /**
     * 销毁界面--由于网络回调有时候销毁不掉界面而写的方法
     */
    protected void finishActivity() {
        finish();
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
