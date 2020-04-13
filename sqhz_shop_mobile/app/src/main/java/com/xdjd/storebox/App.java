package com.xdjd.storebox;

import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.RemoteViews;

import com.baidu.mapapi.SDKInitializer;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xdjd.storebox.activity.ActionActivity;
import com.xdjd.storebox.activity.CommonWebActivity;
import com.xdjd.storebox.activity.GoodsCategoryActivity;
import com.xdjd.storebox.activity.GoodsDetailActivity;
import com.xdjd.storebox.activity.MessageActivity;
import com.xdjd.storebox.activity.OrderDetailActivity;
import com.xdjd.storebox.service.LocationService;
import com.xdjd.utils.CrashHandler;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by lijipei on 2016/11/14.
 */

public class App extends Application{

    public static Context context = null;

    public static Handler handler = null;

    public static Thread mainThread = null;

    public static int mainThreadId = 0;

    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();


        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

        //        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(25000L, TimeUnit.MILLISECONDS)//设置连接超时时间
                .readTimeout(25000L, TimeUnit.MILLISECONDS)//设置读取超时时间
                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar1)//设置cookie
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);


        //初始化错误监听
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(this);
        //上传错误文档
        if ("1".equals(UserInfoUtils.readIsErrorFile(context))) {
            goReportError();
        }

        /***
         * 百度地图初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

        //--------友盟推送代码--------
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //默认不合并通知
        mPushAgent.setDisplayNotificationNumber(0);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.e("deviceToken",deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        //23:00-7:00消息免打扰模式
        mPushAgent.setNoDisturbMode(23, 0, 7, 0);

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
                LogUtils.e("Umeng-getNotification",msg.extra.toString());
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
                LogUtils.e("Umeng-dealWithCustomAction","dealWithCustomAction");
                Map<String,String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    LogUtils.e("key:"+key,"value:"+value);
                    map.put(key,value);
                }
                //Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();

                Intent intent;
                //1 跳转类型(1 h5 2 app) jumpType
                //2 模块(1 商品详情 2 活动页 3 首页标签分类推荐 4 订单详情) model
                //3 跳转值 jumpValue
                //4 h5 标题 title
                if (map.get("jumpType").equals("1")){
                    intent = new Intent(context,CommonWebActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("title",map.get("title"));
                    intent.putExtra("url",map.get("jumpValue"));
                    startActivity(intent);
                }else if (map.get("jumpType").equals("2")){
                    if ("1".equals(map.get("model"))){//商品详情
                        intent = new Intent(context,GoodsDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("gpId",map.get("jumpValue"));
                        startActivity(intent);
                    }else if ("2".equals(map.get("model"))){//活动商品界面
                        intent = new Intent(context,ActionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("activityId",map.get("jumpValue"));
                        startActivity(intent);
                    }else if ("3".equals(map.get("model"))){//分类界面
                        intent = new Intent(context,GoodsCategoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("whp_id",map.get("jumpValue"));
                        startActivity(intent);
                    }else if ("4".equals(map.get("model"))){//订单详情
                        intent = new Intent(context,OrderDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("orderId",map.get("jumpValue"));
                        startActivity(intent);
                    }else if ("5".equals(map.get("model"))){//跳转消息分类列表
                        intent = new Intent(context,MessageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else{
                    LogUtils.e("tag",map.size()+"--");
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
        /*File f = new File(UserInfoUtils.readErrorFile(context));
        if (!f.exists()) {
            return;
        }
        RequestParams params = new RequestParams();
        try {
            params.put("imgFile", f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

       /* AsyncHttpUtil.postFile(M_Url.addException,
                UserInfoUtils.readErrorFile(context), params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable throwable, String responseString) {
                    }
                    @Override
                    public void onSuccess(int statusCode, String responseString) {
                        try {
                            JSONObject obj = new JSONObject(responseString);
                            if ("0".equals(obj.getString("errno"))) {
                                UserInfoUtils.saveIsErrorFile(context,"0");
                            }
                            LogUtils.e("==========上传文件", responseString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/

        AsyncHttpUtil.post(M_Url.addException,
                UserInfoUtils.readError(this),
                L_RequestParams.addException(UserInfoUtils.getId(context), UserInfoUtils.readError(context)),
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
