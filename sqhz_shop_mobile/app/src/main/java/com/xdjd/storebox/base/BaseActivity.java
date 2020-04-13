package com.xdjd.storebox.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.SystemBarTintManager;
import com.xdjd.view.CustomProgress;
import com.zhy.autolayout.AutoLayoutActivity;


/**
 * Created by lijipei on 2016/11/15.
 */

public class BaseActivity extends AutoLayoutActivity{

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AppManager.getInstance().addActivity(this);
        //LogUtils.e("栈中的activity的数量--1",AppManager.getInstance().getSize()+"");
        // ActivityCollector.addActivity(this);
        PushAgent.getInstance(this).onAppStart();
    }

    //友盟统计代码
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

   /* protected void initData() {
    }*/


    /**
     * 修改通知栏颜色方法
     * @param color
     */
    public void setSystemBarColor(int color){
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
        Toast toast= Toast.makeText(this, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /** 显示Log */
    public void log(String toast) {
        Log.i("TAG", toast);
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
        super.onDestroy();
        AppManager.getInstance().finishActivity(this.getClass());
//        ActivityCollector.removeActivity(this);
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
    public void showProgressDialog() {
        CustomProgress.show(this, "加载中...", true, null);
    }

    /**
     * 自己填写信息的ProgressDialog
     */
    public void showProgressDialog(String message) {
        if (message == null || message.equals("")) {
            showProgressDialog();
        } else {
            CustomProgress.show(this, message, true, null);
        }
    }

    /**
     * 停止progressDialog
     */
    public void disProgressDialog() {
        CustomProgress.hideProgress();
    }

    /** 销毁界面--由于网络回调有时候销毁不掉界面而写的方法 */
    protected void finishActivity(){
        finish();
    }
}
