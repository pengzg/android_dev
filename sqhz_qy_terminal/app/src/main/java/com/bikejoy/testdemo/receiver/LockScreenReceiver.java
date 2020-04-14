package com.bikejoy.testdemo.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bikejoy.testdemo.service.AppService;

import java.util.ArrayList;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/2/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LockScreenReceiver extends BroadcastReceiver {

    private String TAG = "LockScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action==Intent.ACTION_SCREEN_OFF){
            Log.e(TAG,"屏幕关闭了");
        }else if (action==Intent.ACTION_SCREEN_ON){
            Log.e(TAG,"屏幕开启了111");

            //通过监听屏幕SCREEN_ON和SCREEN_OFF这两个action。奇怪的是，这两个action只能通过代码的形式注册，才能被监听到，
            // 使用AndroidManifest.xml 完全监听不到。查了一下，发现这是PowerManager那边在发这个广播的时候，做了限制，
            // 限制只能有register到代码中的receiver才能接收， 这里告诉了我们监听开锁屏事件是不能静态注册广播监听的。
            //那么我们怎么才能保证我的服务一直是启动状态呢？其实还有另一个Action是可以反映出用户正在使用手机的行为，每个用户在使用手机的时候，
            // 首先按电源键将屏幕点亮，紧接着就是解锁。解锁动作通过 android.intent.action.USER_PRESENT 发送出来，
            // 我们就能识别出该用户进入了home界面，也就能启动我们相应的服务了。该Action在AndroidManifest.xml中可以静态注册且监听得到的。

        }else if (action==Intent.ACTION_USER_PRESENT){
            Log.e(TAG,"屏幕开启了222");
            //Intent.ACTION_USER_PRESENT会在手机解锁屏幕时调用发出，这个广播可以在静态注册的情况下被接收到，重启服务的操作可以写在这里面
//            initMyService(context)
        }
    }

    //初始化系统消息推送服务
    private void initMyService(Context context) {
        if (!isServiceRunning(context, "com.xdjd.water.service.AppService")) {
            Intent intent =new Intent(context, AppService.class);
            context.startService(intent);
            Log.d(TAG, "MyService没有运行");
        } else {
            Log.d(TAG, "MyService正在运行");
        }
    }

    /**
     * 查看服务是否开启
     */
    public Boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
}
