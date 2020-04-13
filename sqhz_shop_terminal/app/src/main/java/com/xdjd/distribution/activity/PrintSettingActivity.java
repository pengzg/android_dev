package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.MyBluetoothDeviceAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.popup.PrinterTypePopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.utils.bluetooth.Prints;
import com.xdjd.view.EaseTitleBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PrintSettingActivity extends BaseActivity implements PrinterTypePopup.ItemOnListener, AdapterView.OnItemClickListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_printer_name)
    TextView mTvPrinterName;
    @BindView(R.id.lv_paired_devices)
    ListView mLvPairedDevices;
    @BindView(R.id.ll_printer)
    LinearLayout mLlPrinter;
    @BindView(R.id.iv_print)
    ImageView mIvPrint;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.tv_printer_address_name)
    TextView mTvPrinterAddressName;
    @BindView(R.id.spinnerImageView)
    ImageView mSpinnerImageView;
    @BindView(R.id.ll_bg)
    LinearLayout mLlBg;

    private PrinterTypePopup popupPrinter;
    BluetoothDevice bluetoothDevice;//选中的打印机设备

    // 蓝牙适配器
    private BluetoothAdapter bluetoothAdapter;
    // listview的数据适配器
    private MyBluetoothDeviceAdapter myBluetoothDeviceAdapter;
    // 扫描到了蓝牙设备
    ArrayList<BluetoothDevice> list;
    // 扫描蓝牙的广播接受者
    private MyBluetoothReceiver myBluetoothReceiver;

    AnimationDrawable spinner;//蓝牙扫描加载动画

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
                DialogUtil.showCustomDialog(PrintSettingActivity.this,
                        "提示", "打印格式是否正常?如果不正常,请更改打印机的类型,重新打印!", "正常", "不正常", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                UserInfoUtils.setDeviceAddress(PrintSettingActivity.this, bluetoothDevice.getAddress());
                                mTvPrinterAddressName.setText("当前打印机:" + bluetoothDevice.getAddress());
                                finishActivity();
                            }

                            @Override
                            public void no() {
                            }
                        });
            } else if (msg.what == PublicFinal.ERROR) {
                if (!bluetoothAdapter.isEnabled()) {
                    DialogUtil.showCustomDialog(PrintSettingActivity.this, "提示",
                            UIUtils.getString(R.string.bluetooth_close_printing_failed), "确定", null, null);
                } else {
                    DialogUtil.showCustomDialog(PrintSettingActivity.this, "提示",
                            UIUtils.getString(R.string.bluetooth_printing_failed), "确定", null, null);
                }
            } else if (msg.what == PublicFinal.CONNECT_SUCDESS) {
                //                showToast("连接成功!");
                connectDevice(bluetoothDevice, TASK_TYPE_PRINT);
            } else if (msg.what == PublicFinal.CONNECT_FAILURE) {
                //连接打印机失败
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_print_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("打印配置");

        mLvPairedDevices = (ListView) findViewById(R.id.lv_paired_devices);

        mLlPrinter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlPrinter.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPrintTypePopup();
            }
        });

        if (UserInfoUtils.getPtinterType(this) == BaseConfig.printer80) {
            UserInfoUtils.setPtinterType(this, BaseConfig.printer80);
            mTvPrinterName.setText("打印机（80mm）");
            mIvPrint.setImageResource(R.mipmap.printer_big);
        } else if (UserInfoUtils.getPtinterType(this) == BaseConfig.printer58) {
            UserInfoUtils.setPtinterType(this, BaseConfig.printer58);
            mTvPrinterName.setText("打印机（58mm）");
            mIvPrint.setImageResource(R.mipmap.printer_small);
        }

        if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
            BluetoothUtil.turnOnBluetooth();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        myBluetoothReceiver = new MyBluetoothReceiver();
        registerReceiver(myBluetoothReceiver, filter);

        mLvPairedDevices.setOnItemClickListener(this);
        mTvPrinterAddressName.setText("当前打印机:" + UserInfoUtils.getDeviceAddress(this));

        myBluetoothDeviceAdapter = new MyBluetoothDeviceAdapter(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        myBluetoothReceiver.clearAbortBroadcast();
        unregisterReceiver(myBluetoothReceiver);
    }

    public void connectDevice(BluetoothDevice device, int taskType) {
        if (device != null) {
            super.connectDevice(device, taskType);
        } else {
            Toast.makeText(this, "还未选择打印设备", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                boolean isSuccess = Prints.printReceipt(socket);
                if (isSuccess) {
                    mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                }
                break;
            case TASK_TYPE_CONNECT:
                if (socket == null || !socket.isConnected()) {
                    mHandler.sendEmptyMessage(PublicFinal.CONNECT_FAILURE);
                } else {
                    mHandler.sendEmptyMessage(PublicFinal.CONNECT_SUCDESS);
                }
                break;
        }
    }

    /**
     * 初始化打印机类型popup
     */
    private void initPrintTypePopup() {
        popupPrinter = new PrinterTypePopup(this, this, mLlPrinter.getWidth());
    }

    private void showPrintTypePopup() {
        popupPrinter.showAsDropDown(mLlPrinter, 0, 0);
    }

    @Override
    public void onItemPrinterType(int i) {
        if (i == BaseConfig.printer80) {
            UserInfoUtils.setPtinterType(this, BaseConfig.printer80);
            mTvPrinterName.setText("打印机（80mm）");
            mIvPrint.setImageResource(R.mipmap.printer_big);
        } else if (i == BaseConfig.printer58) {
            UserInfoUtils.setPtinterType(this, BaseConfig.printer58);
            mTvPrinterName.setText("打印机（58mm）");
            mIvPrint.setImageResource(R.mipmap.printer_small);
        }
    }

    @OnClick({R.id.ll_printer, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_printer:
                showPrintTypePopup();
                break;
            //                connectDevice(TASK_TYPE_PRINT);
            case R.id.btn_search://蓝牙搜索
                // 扫描蓝牙
                if (bluetoothAdapter.isEnabled()) {
                    if (list != null) {
                        list.clear();
                        myBluetoothDeviceAdapter.notifyDataSetInvalidated();
                    }

                    if (!bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.startDiscovery();

                        mBtnSearch.setVisibility(View.GONE);
                        mLlBg.setVisibility(View.VISIBLE);
                        startImageAmiaition();
                    } else {
                        showToast("正在扫描");
                    }
                } else {
                    showToast("蓝牙还没打开");
                }
                break;
        }
    }

    /**
     * 当搜索是调用图片背景动画
     */
    public void startImageAmiaition() {
        // 获取ImageView上的动画背景
        spinner = (AnimationDrawable) mSpinnerImageView
                .getBackground();
        // 开始动画
        spinner.start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        bluetoothDevice = list.get(i);
        connectDevice(bluetoothDevice, TASK_TYPE_CONNECT);
    }

    class MyBluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                showToast("开始扫描");
            } else if (action
                    .equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                showToast("扫描结束");

                mBtnSearch.setVisibility(View.VISIBLE);
                mLlBg.setVisibility(View.GONE);
                spinner.stop();
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (list == null) {
                    list = new ArrayList<BluetoothDevice>();
                }
                if (!list.contains(device)) {
                    list.add(device);
                }
                // 判断listview是否设置Adapter
                if (mLvPairedDevices.getAdapter() == null) {
                    mLvPairedDevices.setAdapter(myBluetoothDeviceAdapter);
                } else {
                    myBluetoothDeviceAdapter.setData(list);
                    //                    myBluetoothDeviceAdapter.notifyDataSetChanged();
                }
            }
        }

    }
}
