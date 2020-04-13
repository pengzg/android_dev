package com.xdjd.distribution.base;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.LoginActivity;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.SystemBarTintManager;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.view.CustomProgress;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

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

    private GpsStatusReceiver gpsStatusReceiver = new GpsStatusReceiver();


    String tag = getClass().getSimpleName();
    private BluetoothSocket mSocket;
    private BluetoothStateReceiver mBluetoothStateReceiver;
    private AsyncTask mConnectTask;
    public ProgressDialog mProgressDialog;

    BluetoothAdapter mBluetoothAdapter;

    /**
     * 连接
     */
    protected final int TASK_TYPE_CONNECT = 1;
    /**
     * 打印
     */
    protected final int TASK_TYPE_PRINT = 2;

    /**
     * 蓝牙连接成功后回调，该方法在子线程执行，可执行耗时操作
     */
    protected void onConnected(BluetoothSocket socket, int taskType) {
    }

    /**
     * 蓝牙打印机链接失败
     */
    protected void onConnectionFails(){
    }

    /**
     * 蓝牙连接完结
     */
    protected void onConnectionFinish(){

    }

    /**
     * 蓝牙状态发生变化时回调
     */
    protected void onBluetoothStateChanged(Intent intent) {
    }

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
        PushAgent.getInstance(this).onAppStart();
        //        setSystemBarColor(R.color.title_bg);
        initData();
        initReceiver();
    }

    protected abstract int getContentView();

    protected void initData() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        //        initGPS();
        currentGPSState = getGPSState(this);
        if (!currentGPSState) {
            if (!currentGPSState) {
                Toast.makeText(BaseActivity.this, "客户定位需要打开GPS功能!",
                        Toast.LENGTH_SHORT).show();
                // 转到手机设置界面，用户设置GPS
                Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent2, 12808); // 设置完成后返回到原来的界面
            }
        }

        try {
            ready(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        cancelConnectTask();
        closeSocket();
        super.onStop();
        unregisterReceiver(gpsStatusReceiver);
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
     * 获取ＧＰＳ当前状态
     *
     * @param context
     * @return
     */
    private boolean getGPSState(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return on;
    }

    /**
     * 注册监听广播
     *
     * @param context
     * @throws Exception
     */
    public void ready(Context context) throws Exception {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        context.registerReceiver(gpsStatusReceiver, filter);
    }

    boolean currentGPSState = false;

    /**
     * 监听GPS 状态变化广播
     */
    private class GpsStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                currentGPSState = getGPSState(context);
                LogUtils.e("currentGPSState", currentGPSState + "");
                if (!currentGPSState) {
                    Toast.makeText(BaseActivity.this, "客户定位需要打开GPS功能!",
                            Toast.LENGTH_SHORT).show();
                    // 转到手机设置界面，用户设置GPS
                    Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent2, 12808); // 设置完成后返回到原来的界面
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12808 && !getGPSState(this)) {
            Toast.makeText(BaseActivity.this, "客户定位需要打开GPS功能!",
                    Toast.LENGTH_SHORT).show();
            // 转到手机设置界面，用户设置GPS
            Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent2, 12808); // 设置完成后返回到原来的界面
        }
        LogUtils.e("onActivityResult", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
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
        if (mBluetoothStateReceiver != null) {
            unregisterReceiver(mBluetoothStateReceiver);
        }

        isShowDismissDialog();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
        AppManager.getInstance().finishActivity(this.getClass());
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


    protected void closeSocket() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                mSocket = null;
                e.printStackTrace();
            }
        }
    }

    protected void cancelConnectTask() {
        if (mConnectTask != null) {
            mConnectTask.cancel(true);
            mConnectTask = null;
        }
    }

    private void initReceiver() {
        mBluetoothStateReceiver = new BluetoothStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothStateReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 检查蓝牙状态，如果已打开，则查找已绑定设备
     *
     * @return
     */
    public boolean checkBluetoothState() {
        if (BluetoothUtil.isBluetoothOn()) {
            LogUtils.e("base蓝牙connectBluetooth",true+"--");
            return true;
        } else {
            LogUtils.e("base蓝牙connectBluetooth",false+"--");
            BluetoothUtil.openBluetooth(this);
            return false;
        }
    }

    public void connectDevice(BluetoothDevice device, int taskType) {
        LogUtils.e("connectDevice",(device != null)+"--");
        if (checkBluetoothState() && device != null) {
            mConnectTask = new ConnectBluetoothTask(taskType).execute(device);
        }
    }


    class ConnectBluetoothTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {

        int mTaskType;

        public ConnectBluetoothTask(int taskType) {
            this.mTaskType = taskType;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            showProgressDialog("请稍候...");
            super.onPreExecute();
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... params) {

            if (mSocket != null && mSocket.getRemoteDevice().getAddress().equals(params[0].getAddress())){
                //如果已创建连接,且地址都相等,则不用再去多次创建连接
//                LogUtils.e("doInBackground--1","如果已创建连接,且地址都相等,则不用再去多次创建连接");
            }else if (mSocket != null && !mSocket.getRemoteDevice().getAddress().equals(params[0].getAddress())){
//                LogUtils.e("doInBackground--2","不同打印机进行切换");
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
//                        LogUtils.e("mSocket.close()",e.toString());
                    }
                }
                LogUtils.e("getAddress",params[0].getAddress());
                BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(params[0].getAddress());
                mSocket = connectDevice(mBluetoothDevice,true);
            }else{
//                LogUtils.e("doInBackground--3","重新连接");
                if (mSocket != null) {
                    try {
//                        LogUtils.e("mSocket","走了");
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
//                        LogUtils.e("mSocket.close()",e.toString());
                    }
                }
                BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(params[0].getAddress());
                mSocket = connectDevice(mBluetoothDevice,true);
            }

            if (mBluetoothAdapter.getRemoteDevice(params[0].getAddress()).getBondState() == BluetoothDevice.BOND_NONE){
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //如果失败后,使用旧版本的连接蓝牙方式进行尝试连接
                mSocket = connectDevice(mBluetoothAdapter.getRemoteDevice(params[0].getAddress()), false);
            }
            onConnected(mSocket, mTaskType);
            return mSocket;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            mProgressDialog.dismiss();
            if (socket == null || !socket.isConnected()) {
                showToast("连接打印机失败");
                onConnectionFails();
                //如果失败了,证明这个连接失效了,关闭这个连接,重新创建新的连接
                closeSocket();
                onConnectionFinish();
                return;
            } else {
//                toast("连接成功！");
                onConnectionFinish();
            }
//            onConnected(mSocket, mTaskType);
            super.onPostExecute(socket);
        }

       /* @Override
        protected void onCancelled() {
            onConnectionFinish();
            super.onCancelled();
        }*/
    }

    /**
     * 连接蓝牙设备方法
     * @param device
     * @param isNewVersion true:4.0设备使用createInsecureRfcommSocketToServiceRecord方法
     *                     false:2.0设备使用createRfcommSocketToServiceRecord方法
     * @return
     */
    public BluetoothSocket connectDevice(BluetoothDevice device,boolean isNewVersion) {
        BluetoothSocket socket = null;
        try {
            if (isNewVersion){//新android系统
                socket = device.createInsecureRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            }else{//安卓2.3系统及以下用的
                socket = device.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            }

            socket.connect();
        } catch (Exception e) {
            LogUtils.e("connectDevice Exception",e.toString());
            try {
//                socket =(BluetoothSocket) device.getClass()
//                        .getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
//                socket.connect();
                socket.close();
            } catch (Exception closeException) {

                return null;
            }
            return null;
        }
        return socket;
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void disProgressDialog(){
        isShowDismissDialog();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 监听蓝牙状态变化的系统广播
     */
    class BluetoothStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    //                    toast("蓝牙已开启");
                    break;

                case BluetoothAdapter.STATE_TURNING_OFF:
                    //                    toast("蓝牙已关闭");
                    break;
            }
            onBluetoothStateChanged(intent);
        }
    }


}
