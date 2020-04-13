package com.xdjd.utils.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import com.xdjd.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.taobao.accs.ACCSManager.mContext;

public class BluetoothUtil {

    /**
     * 蓝牙是否打开
     */
    public static boolean isBluetoothOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null)
            // 蓝牙已打开
            if (mBluetoothAdapter.isEnabled())
                return true;

        return false;
    }

    /**
     * 获取所有已配对的设备
     */
    public static List<BluetoothDevice> getPairedDevices() {
        List deviceList = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
            }
        }
        return deviceList;
    }

    /**
     * 获取所有已配对的打印类设备
     */
    public static List<BluetoothDevice> getPairedPrinterDevices() {
        return getSpecificDevice(BluetoothClass.Device.Major.IMAGING);
    }

    /**
     * 从已配对设配中，删选出某一特定类型的设备展示
     *
     * @param deviceClass
     * @return
     */
    public static List<BluetoothDevice> getSpecificDevice(int deviceClass) {
        List<BluetoothDevice> devices = BluetoothUtil.getPairedDevices();
        List<BluetoothDevice> printerDevices = new ArrayList<>();

        for (BluetoothDevice device : devices) {
            BluetoothClass klass = device.getBluetoothClass();
            // 关于蓝牙设备分类参考 http://stackoverflow.com/q/23273355/4242112
            if (klass.getMajorDeviceClass() == deviceClass)
                printerDevices.add(device);
        }

        return printerDevices;
    }

    /**
     * 弹出系统对话框，请求打开蓝牙
     */
    public static void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 666);
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    public static boolean turnOnBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }

        return false;
    }

    public BluetoothSocket connectDevice(BluetoothDevice device) {
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();

        } catch (IOException e) {
            LogUtils.e("connectDevice Exception",e.toString());
            try {
                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                socket.connect();
            } catch (Exception closeException) {

                return null;
            }
            return null;
        }
        return socket;
    }

    public void run(final BluetoothSocket mSocket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSocket != null)
                        mSocket.connect();

                } catch (IOException e) {
                    try {
                        if (mSocket != null) {
                            mSocket.close();
                        }
                    } catch (Exception e2) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    /*BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    if (btAdapter.isEnabled()) {
        SharedPreferences prefs_btdev = getSharedPreferences("btdev", 0);
        String btdevaddr=prefs_btdev.getString("btdevaddr","?");

        if (btdevaddr != "?")
        {
            BluetoothDevice device = btAdapter.getRemoteDevice(btdevaddr);

            UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service
            //UUID SERIAL_UUID = device.getUuids()[0].getUuid(); //if you don't know the UUID of the bluetooth device service, you can get it like this from android cache

            BluetoothSocket socket = null;

            try {
                socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
            } catch (Exception e) {Log.e("","Error creating socket");}

            try {
                socket.connect();
                Log.e("","Connected");
            } catch (IOException e) {
                Log.e("",e.getMessage());
                try {
                    Log.e("","trying fallback...");

                    socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                    socket.connect();

                    Log.e("","Connected");
                }
                catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }
        }
        else
        {
            Log.e("","BT device not selected");
        }
    }
*/

}