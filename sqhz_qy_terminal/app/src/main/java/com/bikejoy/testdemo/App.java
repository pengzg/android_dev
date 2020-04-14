package com.bikejoy.testdemo;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mob.MobApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.bikejoy.utils.CrashHandler;
import com.bikejoy.utils.DynamicTimeFormat;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.testdemo.base.Common;
import com.bikejoy.testdemo.location.LocationService;
import com.bikejoy.testdemo.main.MainActivity;
import com.bikejoy.testdemo.service.AppService;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lijipei on 2016/11/14.
 */

public class App extends MobApplication {

    public static Context context = null;

    public static Handler handler = null;

    public static Thread mainThread = null;

    public static int mainThreadId = 0;

    public LocationService locationService;
    public Vibrator mVibrator;

    UserBean mUserBean;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//启用矢量图兼容
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_ededed, R.color.color_999999);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setEnableLoadMoreWhenContentNotFull(true);//内容不满一页时候启用加载更多
                ClassicsFooter footer = new ClassicsFooter(context);
                footer.setBackgroundResource(android.R.color.white);
                footer.setSpinnerStyle(SpinnerStyle.Scale);//设置为拉伸模式
                return footer;//指定为经典Footer，默认是 BallPulseFooter
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();

        Intent intent = new Intent(context, AppService.class);
        startService(intent);

        //        MobSDK.init(this);

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        initUmeng();//初始化友盟推送

        //初始化错误监听
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(this);
        LogUtils.e("myapplication", "是否有异常信息:" + UserInfoUtils.readError(this));
        //上传错误文档
        if ("1".equals(UserInfoUtils.readIsErrorFile(this))) {
            goReportError();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void initUmeng() {
        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
        // 参数一：当前上下文context；
        // 参数二：应用申请的Appkey（需替换）；
        // 参数三：渠道名称；
        // 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
        // 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, "5d9fe33a570df381d300079a", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "47cdc212f2b7107e17247d1afad2c7f3");
        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.e("PushAgent", "注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("PushAgent", "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        //参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。
        mPushAgent.setDisplayNotificationNumber(0);

        /*UmengMessageHandler messageHandler = new UmengMessageHandler() {
            *//**
         * 自定义消息的回调方法
         * *//*
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

            *//**
         * 自定义通知栏样式的回调方法
         * *//*
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
        mPushAgent.setMessageHandler(messageHandler);*/

        /**
         * 消息点击时获取自定义参数
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        /*UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                LogUtils.e("Umeng-dealWithCustomAction", "dealWithCustomAction");
                *//*Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    LogUtils.e("key:" + key, "value:" + value);
                    map.put(key, value);
                }*//*
               *//* UserBean userBean = UserInfoUtils.getUser(context);
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
                }*//*
            }

        };*/

        //消息到达时获取自定义参数
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                for (Map.Entry entry : msg.extra.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                }
                return super.getNotification(context, msg);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        //消息点击时获取自定义参数
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
                LogUtils.e("推送消息", "launchApp:" + msg.toString());
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    LogUtils.e("推送消息key:" + key, "value:" + value);
                    map.put(key, value);
                }

                //跳转类型 1 h5 2 活动 3 商品 4 消息详情 5订单 6配送任务 10配送申请列表
                // 13 主管开发领用   14开发员开发领用  15办公领用
                //                private String mp_jump_type;
                //消息跳转模块  1 app 2 h5页面
                //                private String mp_jump_model;
                Intent intent;
                if ("1".equals(map.get("model"))) {
                    //1 h5 2 活动 3 商品 4 消息详情 5订单6配送任务 7会员列表
                    switch (map.get("jumpType")) {
                        case "5":
                            intent = new Intent(context, OrderDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("orderId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                        case "6":
                            intent = new Intent(context, DeliveryOrderDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("odmId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                        case "7":
                            //                            if ("1".equals(UserInfoUtils.getUser(context).getUsertype())) {//管理员端
                            //                                intent = new Intent(context, AdministratorMainActivity.class);
                            //                            } else {//业务员
                            intent = new Intent(context, MainActivity.class);
                            //                            }
                            intent.putExtra("message", "message");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case "8":
                            intent = new Intent(context, StorehouseThreeDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("storeId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                        case "9":
                            intent = new Intent(context, StorehouseThreeDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("storeId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                        case "10":
                            intent = new Intent(context, GetWaterRecordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("recordId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                        case "11":
                            intent = new Intent(context, TicketSendApplyListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case "12"://套餐审核申请
                            intent = new Intent(context, PackageApplyListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        // 13 主管开发领用   14开发员开发领用  15办公领用
                        case "13":
                            intent = new Intent(context, PackageRelationListActivity.class);
                            intent.putExtra("sprType", "2");//开发主管
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case "14"://14开发员开发领用
                            intent = new Intent(context, PackageApplyListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case "15":
                            intent = new Intent(context, PackageApplyListActivity.class);
                            mUserBean = UserInfoUtils.getUser(context);
                            if (mUserBean==null){
                                return;
                            }
                            if (Common.ROLE3002.equals(mUserBean.getRoleCode())) { //开发主管时才传递参数
                                intent.putExtra("queryType", "2");
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case "16"://提现
                            intent = new Intent(context, CashApplyDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("gcaId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                        case "17"://发票
                            intent = new Intent(context, InvoiceDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("oilId", map.get("jumpValue"));
                            startActivity(intent);
                            break;
                    }
                    //消息id字段,直接标记为已读
                    readMessage(map.get("msg_id"));
                }
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                LogUtils.e("推送消息", "openUrl");
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                LogUtils.e("推送消息", "openActivity");
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                //                LogUtils.e("推送消息","dealWithCustomAction:"+msg.toString());
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    LogUtils.e("推送消息key:" + key, "value:" + value);
                    map.put(key, value);
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

    private void readMessage(String messageids) {
        AsyncHttpUtil.post(M_Url.readMessage, L_RequestParams.readMessage(messageids),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable throwable, String responseString) {
                    }

                    @Override
                    public void onSuccess(int statusCode, String responseString) {
                    }
                });
    }

    /**
     * 反馈问题
     */
    private void goReportError() {
        //        File f = new File(UserInfoUtils.readError(this));
        //        if (!f.exists()) {
        //            return;
        //        }

        /*AsyncHttpUtil.post(M_Url.addException,
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
                });*/
    }

}
