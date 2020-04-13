package com.xdjd.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.distribution.base.BaseConfig;

/**
 * Created by lijipei on 2017/1/5.
 */

public class AppUtil {

    /**
     * apk安装包已安装
     */
    //public static final int Has_been_installed = 1;

    /**
     * apk安装包未安装
     */
    //public static final int Not_installed = 2;

    /**
     * 过期或没有安装包
     */
    public static final int Overdue = -1;

    /**
     * 获取apk包的信息：版本号，名称，图标等
     *
     * @param absPath  apk包的绝对路径
     * @param context 
     */


    public static int apkInfo(String absPath, Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
             /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名 
            String packageName = appInfo.packageName; // 得到包名 
            String version = pkgInfo.versionName; // 得到版本信息 
            int versionCode = pkgInfo.versionCode;
             /* icon1和icon2其实是一样的 */

            //            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息 
            //            Drawable icon2 = appInfo.loadIcon(pm);

            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
            Log.i("apkInfo", String.format("PkgInfo: %s", pkgInfoStr));

            return versionCode;

            //            if (SystemUtil.getVersionCode(context) == versionCode){
            //                return Has_been_installed;
            //            }else if (SystemUtil.getVersionCode(context) < versionCode){
            //                return Not_installed;
            //            }
        }
        return Overdue;
    }


    public void exitApp(Context context) {

        PushAgent mPushAgent = PushAgent.getInstance(context);
        //退出时解除别名的绑定
        mPushAgent.removeAlias(UserInfoUtils.getId(context), BaseConfig.Alias_Type,
                new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String s) {
                    }
                });

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(context.getPackageName());
        AppManager.getInstance().finishAllActivity();

        //终止当前正在运行的Java虚拟机，导致程序终止
        System.exit(0);
    }

}
