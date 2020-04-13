package com.xdjd.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.xdjd.distribution.App;

/**
 * UI工具类
 * Created by lijipei on 2016/3/19.
 */
public class UIUtils {

    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    public static View getXmlView(int layoutId){
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 1dp- - -1px;(160)
     * @param dp
     * @return
     */
    public static int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);
    }

    public static int px2dp(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px/density+0.5);
    }

    public static String[] getStringArr(int arrId){
        return getContext().getResources().getStringArray(arrId);
    }

    public static String getString(int strId){
        return getContext().getResources().getString(strId);
    }

    public static Context getContext(){
        return App.context;
    }

    public static Handler getHandler(){
        return App.handler;
    }

    /**
     * 保证runnable对象的run方法是运行在主线程当中
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isInMainThread()) {
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    private static boolean isInMainThread() {
        //当前线程的id
        int tid = android.os.Process.myTid();
        if (tid == App.mainThreadId) {
            return true;
        }
        return false;
    }


    public static void Toast(String text) {
        Toast toast= Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }
    // 鑾峰彇鍥剧墖
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
