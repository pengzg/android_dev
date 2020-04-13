package com.xdjd.distribution;

import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.widget.RemoteViews;

import com.baidu.mapapi.SDKInitializer;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xdjd.distribution.activity.LaunchActivity;
import com.xdjd.distribution.activity.MessageActivity;
import com.xdjd.distribution.bean.UrlBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.dao.UrlDao;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.utils.CrashHandler;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lijipei on 2016/11/14.
 */

public class App extends Application {

    public static Context context = null;

    public static Handler handler = null;

    public static Thread mainThread = null;

    public static int mainThreadId = 0;

    public LocationService locationService;
    public Vibrator mVibrator;

//    private UrlDao mUrlDao;
//    public List<UrlBean> list;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

        //初始化错误监听
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(this);
        LogUtils.e("myapplication", "是否有异常信息:" + UserInfoUtils.readError(this));
        //上传错误文档
        if ("1".equals(UserInfoUtils.readIsErrorFile(this))) {
            goReportError();
        }

        initUmeng();

        /*mUrlDao = new UrlDao(this);
        list = mUrlDao.queryList();
        if (list == null || list.size() == 0){
            if (UserInfoUtils.getDomainName(context).equals("0")){

            }else{
                UserInfoUtils.setDomainName(context,);
            }
        }
        mUrlDao.destroy();*/


    }

    private void initUmeng() {
        PushAgent mPushAgent = PushAgent.getInstance(this);

        //参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。
        mPushAgent.setDisplayNotificationNumber(0);


        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.e("deviceToken1", "deviceToken:" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e("deviceToken2", s + "--s1:" + s1);
            }
        });

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        //Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                        LogUtils.e("Umeng-dealWithCustomMessage", msg.custom);
                    }
                });
            }

            /**
             * 自定义通知栏样式的回调方法
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                LogUtils.e("Umeng-getNotification", msg.extra.toString());
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                LogUtils.e("Umeng-dealWithCustomAction", "dealWithCustomAction");
                /*Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    LogUtils.e("key:" + key, "value:" + value);
                    map.put(key, value);
                }*/
                UserBean userBean = UserInfoUtils.getUser(context);
                //如果消息错误推过来,则禁止跳转
                if (userBean==null || "5".equals(userBean.getSu_usertype())){
                    return;
                }

                if (userBean!=null && !UserInfoUtils.getLoginState(context).equals("0")){
                    Intent intent = new Intent(context,MessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("isMessage",true);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(context,LaunchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

        };

        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //此处是完全自定义处理设置
        //mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);


    }

    /**
     * 反馈问题
     */
    private void goReportError() {
        //        File f = new File(UserInfoUtils.readError(this));
        //        if (!f.exists()) {
        //            return;
        //        }

        AsyncHttpUtil.post(M_Url.addException,
                UserInfoUtils.readError(this), L_RequestParams.addException(UserInfoUtils.getId(context), UserInfoUtils.readError(context)),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable throwable, String responseString) {
                    }

                    @Override
                    public void onSuccess(int statusCode, String responseString) {
                        try {
                            JSONObject obj = new JSONObject(responseString);
                            if ("00".equals(obj.getString("repCode"))) {
                                UserInfoUtils.saveIsErrorFile(UIUtils.getContext(), "0");
                                UserInfoUtils.saveError(context, "");
                            }
                            LogUtils.e("==========上传文件", responseString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
