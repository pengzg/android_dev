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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.SystemBarTintManager;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.view.CustomProgress;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lijipei on 2016/8/22.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 键盘管理对象
     */
    protected InputMethodManager manager;

    /**
     * 状态栏通知的管理类
     */
    protected NotificationManager notificationManager;

    String tag = getClass().getSimpleName();
    private BluetoothSocket mSocket;
    private BluetoothStateReceiver mBluetoothStateReceiver;
    BluetoothAdapter mBluetoothAdapter;
    private AsyncTask mConnectTask;
    public ProgressDialog mProgressDialog;

    protected final int TASK_TYPE_CONNECT = 1;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        OnActCreate(savedInstanceState);
        notificationManager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        initReceiver();
    }

    /**
     * 子类实现的onActivityCreated方法
     * @param savedInstanceState
     */
    public abstract void OnActCreate(Bundle savedInstanceState);

    /**
     * 修改通知栏颜色方法
     * @param color
     */
    public void setSystemBarColor(int color){
        //透明状态栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    /**
     * 设置状态
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
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
        intent.setClass(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * toast
     * @param info
     */
    public void showToast(String info) {
        Toast toast= Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public int screenWidth;
    public int screenHeigh;
    // 屏幕密度dpi
    public int densityDpi;

    /**
     * 获取屏幕信息
     */
    public void getScreenDetails() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
        densityDpi = dm.densityDpi;
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

    /** 销毁界面--由于网络回调有时候销毁不掉界面而写的方法 */
    protected void finishActivity(){
        getActivity().finish();
    }

    public void isShowDismissDialog(){
        CustomProgress.isShowDismissDialog();
    }

    @Override
    public void onStop() {
        cancelConnectTask();
        closeSocket();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mBluetoothStateReceiver);
        isShowDismissDialog();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
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
        getActivity().registerReceiver(mBluetoothStateReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 检查蓝牙状态，如果已打开，则查找已绑定设备
     *
     * @return
     */
    public boolean checkBluetoothState() {
        if (BluetoothUtil.isBluetoothOn()) {
            return true;
        } else {
            BluetoothUtil.openBluetooth(getActivity());
            return false;
        }
    }

    public void connectDevice(BluetoothDevice device, int taskType) {
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
            }else if (mSocket != null && !mSocket.getRemoteDevice().getAddress().equals(params[0].getAddress())){
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtils.e("mSocket.close()",e.toString());
                    }
                }
                BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(params[0].getAddress());
                mSocket = connectDevice(mBluetoothDevice);
            }else{
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtils.e("mSocket.close()",e.toString());
                    }
                }
                BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(params[0].getAddress());
                mSocket = connectDevice(mBluetoothDevice);
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
                toast("连接打印机失败");
                onConnectionFails();

                //如果失败了,证明这个连接失效了,关闭这个连接,重新创建新的连接
                closeSocket();
                onConnectionFinish();
            } else {
                //                toast("连接成功！");
                onConnectionFinish();
            }
            super.onPostExecute(socket);
        }
    }

    public BluetoothSocket connectDevice(BluetoothDevice device) {
        BluetoothSocket socket = null;
        try {               //createRfcommSocketToServiceRecord
            socket = device.createInsecureRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
        } catch (Exception e) {
            LogUtils.e("connectDevice Exception",e.toString());
            try {
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
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                    toast("蓝牙已开启");
                    break;

                case BluetoothAdapter.STATE_TURNING_OFF:
                    toast("蓝牙已关闭");
                    break;
            }
            onBluetoothStateChanged(intent);
        }
    }


}
