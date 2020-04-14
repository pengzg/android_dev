package com.bikejoy.utils.permissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.UIUtils;


/**
 * 请求权限的工具类
 * Created by 晋 on 2016/11/30.
 */

public class PermissionUtils {
    /**
     * 定位
     */
    public static final int LOCATIONMASSAGE = 0;//定位
    /**
     * 电话
     */
    public static final int CALLPHONEMESSAGE = 1;//打电话
    /**
     * 相机
     */
    public static final int CAMERAMESSAGE = 2;//相机
    /**
     * 存储
     */
    public static final int STORAGEMESSAGE = 3;//
    public static String setAappName = "社区盒子";
    private static String[] messages = {"定位", "拨打电话", "相机", "读写SD卡"};//提示信息
    private static Context appContext = UIUtils.getContext();//不建议使用


    /**
     * 批量请求权限
     *
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstPermission(Activity mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(mactivity, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            perRequestCode);
                } else {
                    //有权限
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                }
            } catch (RuntimeException e) {
                LogUtils.e("tag", "RuntimeException:" + e.getMessage());
                return;
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 在ACTIVITY中请求相机权限的方法
     *
     * @param mactivity      所在activity
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstActivityCamera(Activity mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //需要加入异常处理,如果多个权限请求,其中一个点击取消,下面再申请时可能会报异常
            try {
                if (ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    //判断用户是否点击不再提示的勾选按钮
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.CAMERA)
                            && ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(mactivity, new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                perRequestCode);
                    } else {
                        //如果勾选了,提示去应用管理中进行手动打开处理
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.CAMERA)
                                || !ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || !ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            myDialog(mactivity, "拍照和读写SD卡", "相机", callBack);
                        }
                    }
                } else {
                    //有权限
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                }
            } catch (RuntimeException e) {
                LogUtils.e("tag", "RuntimeException:" + e.getMessage());
                return;
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 在Activity中请求拍照的方法
     *
     * @param mactivity      所在activity
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstActivityLocaltion(Activity mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mactivity, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            perRequestCode);
                } else {
                    //有权限
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                }
            } catch (RuntimeException e) {
                LogUtils.e("tag", "RuntimeException:" + e.getMessage());
                return;
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 在Fragment中请求拍照的方法
     *
     * @param mactivity      所在activity
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstFragmentLocaltion(Fragment mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mactivity.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mactivity.getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mactivity.requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        perRequestCode);
            } else {
                //有权限
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 在Activity中请求存储权限的方法  可以用在本地图库选择等
     *
     * @param mactivity      所在activity
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstAcivityStorage(Activity mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mactivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mactivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(mactivity, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            perRequestCode);
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || !ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        myDialog(mactivity, STORAGEMESSAGE, callBack);
                    }
                }

            } else {
                //有权限
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 在ACTIVITY中请求电话权限的方法
     *
     * @param mactivity      所在activity
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstActivityCallPhone(Activity mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mactivity,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (mactivity.shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    // 是否显示不在提醒
                    myDialog(mactivity, CALLPHONEMESSAGE, callBack);
                } else {
                    ActivityCompat.requestPermissions(mactivity, new String[]{
                                    Manifest.permission.CALL_PHONE},
                            perRequestCode);
                }
            } else {
                //有权限
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 在ACTIVITY中请求定位的方法
     *
     * @param mactivity      所在activity
     * @param perRequestCode 请求码
     * @param callBack       回调
     */
    public static void requstActivityLocation(Activity mactivity, int perRequestCode, OnRequestCarmerCall callBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                /*if (ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(mactivity,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        ActivityCompat.requestPermissions(mactivity, new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                                perRequestCode);
                    } else {
                        // 是否显示不在提醒
                        myDialog(mactivity, LOCATIONMASSAGE, callBack);
                    }
                } else {
                    //有权限
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                }*/

                if (ContextCompat.checkSelfPermission(mactivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    //判断用户是否点击不再提示的勾选按钮
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                        ActivityCompat.requestPermissions(mactivity, new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                                perRequestCode);
                    } else {
                        //如果勾选了,提示去应用管理中进行手动打开处理
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            myDialog(mactivity, "定位", "定位", callBack);
                        }
                    }

                } else {
                    //有权限
                    if (callBack != null) {
                        callBack.onSuccess();
                    }
                }

            } catch (RuntimeException e) {
                LogUtils.e("tag", "RuntimeException:" + e.getMessage());
                return;
            }
        } else {
            //			有权限
            if (callBack != null) {
                callBack.onSuccess();
            }
        }
    }

    /**
     * 定位权限申请结果
     *
     * @param context
     * @param message      提示信息
     * @param grantResults
     */
    public static void perMissionLocationResult(Context context, int message,
                                                int[] grantResults, OnRequestCarmerCall callBack) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (callBack != null)
                callBack.onSuccess();
        } else {
            myDialog(context, message, callBack);
        }
    }

    /**
     * 定位权限申请结果
     *
     * @param context
     * @param message      提示信息
     * @param grantResults
     */
    public static void perMissionLocationResult(Activity context, int message,
                                                int[] grantResults, OnRequestCarmerCall callBack) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            myDialog(context, message, callBack);
        } else {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (callBack != null)
                    callBack.onSuccess();
            } else {
                myDialog(context, message, callBack);
            }
        }
    }

    /**
     * 相机权限申请结果
     *
     * @param context
     * @param message      提示信息
     * @param grantResults
     */
    public static void perMissionCameraResult(Context context, int message,
                                              int[] grantResults, OnRequestCarmerCall callBack) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
            if (callBack != null)
                callBack.onSuccess();
        } else {
            myDialog(context, message, callBack);
        }
    }

    /**
     * 存储权限申请结果
     *
     * @param context
     * @param message      提示信息
     * @param grantResults
     */
    public static void perMissionStorageResult(Context context, int message,
                                               int[] grantResults, OnRequestCarmerCall callBack) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
            if (callBack != null)
                callBack.onSuccess();
        } else {
            myDialog(context, message, callBack);
        }
    }

    /**
     * 电话权限申请结果
     *
     * @param context
     * @param message      提示信息
     * @param grantResults
     */
    public static void perMissionCallPhoneResult(Context context, int message,
                                                 int[] grantResults, OnRequestCarmerCall callBack) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (callBack != null)
                callBack.onSuccess();
        } else {
            myDialog(context, message, callBack);
        }
    }

    private static void myDialog(final Context context, int message, final OnRequestCarmerCall callBack) {
        String showMessage = "相应的权限，以正常使用定位等功能";
        switch (message) {
            case LOCATIONMASSAGE:
                showMessage = messages[message];
                break;
            case CALLPHONEMESSAGE:
                showMessage = messages[message];
                break;
            case CAMERAMESSAGE:
                showMessage = messages[message];
                break;
            case STORAGEMESSAGE:
                showMessage = messages[message];
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("在设置-应用-" + setAappName + "-权限，中设置" + showMessage + "等权限，以正常使用" + showMessage + "等功能");

        builder.setTitle("权限申请");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                context.startActivity(intent);
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (callBack != null) {
                    callBack.onDilogCancal();
                }
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private static void myDialog(final Context context, String message, String message2, final OnRequestCarmerCall callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("在设置-应用-" + setAappName + "-权限，中设置" + message + "等权限，以正常使用" + message2 + "等功能");

        builder.setTitle("权限申请");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                context.startActivity(intent);
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (callBack != null) {
                    callBack.onDilogCancal();
                }
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 回调接口
     */
    public interface OnRequestCarmerCall {
        void onSuccess();

        //        void onRefused();
        void onDilogCancal();
    }

    @TargetApi(23)
    public static void requestPermissions(Activity context) {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(context, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                //				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    @TargetApi(23)
    //    public static void requestPermissionsofType(Activity context, int type) {
    //        convert (type) {
    //            case LOCATIONMASSAGE://位置
    //
    //            break;
    //            case CALLPHONEMESSAGE://打电话
    //            break;
    //            case CAMERAMESSAGE://相机
    //            break;
    //            case STORAGEMESSAGE://存储
    //            break;
    //            default:
    //                return;
    //        }
    //    }
    //    @TargetApi(23)
    //    public static void requestPermissionsofType(Fragment fragment, int type) {
    //        convert (type) {
    //            case LOCATIONMASSAGE://位置
    //
    //                break;
    //            case CALLPHONEMESSAGE://打电话
    //
    //                break;
    //            case CAMERAMESSAGE://相机
    //
    //                break;
    //            case STORAGEMESSAGE://存储
    //
    //                break;
    //            default:
    //                return;
    //        }
    //    }
}
