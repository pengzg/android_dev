package com.xdjd.utils.update;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.FileUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用系统DownloadManager版本更新工具类
 *
 * @author lijipei
 */
public class DownloadUtil {

    private static DownloadManager manager; // 系统下载类
    static DownloadCompeleteReceiver receiver; // 广播
    static long downloadId = 0;

    private static Context context;

    //	private static String installPath = Environment
    //			.getExternalStorageDirectory() + "/Android/data/";// 下载安装包安装路径

    private static boolean isShow;//是否显示版本更新下载进度,true:显示,false:不显示
    private static String versionStr;
    private static String messageStr;

    /**
     * 版本更新方法
     *
     * @param appName     :应用名称
     * @param con         :上下文对象
     * @param downloadUrl :版本下载路径
     * @param dirType     :本地下载的文件夹名称
     * @param subPath     :本地下载后的安装包名称,必须以.apk结尾
     * @param message     :版本更新内容提示信息
     */
    public static void updataVersion(String appName, Context con,
                                     String downloadUrl, String dirType, String subPath, boolean isshow, String version, final String message) {
        context = con;
        isShow = isshow;
        versionStr = version;
        messageStr = message;
        manager = (DownloadManager) context
                .getSystemService(context.DOWNLOAD_SERVICE);
        receiver = new DownloadCompeleteReceiver();

        File file = new File(BaseConfig.installPath);
        if (file.exists()) {
            file.delete();
        }

        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle(appName);
        request.setDestinationInExternalFilesDir(context, dirType, subPath);
        request.setDescription("版本更新...");
        if (isShow) {
            // 显示进度
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        } else {
            // 设置不显示进度
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        downloadId = manager.enqueue(request);

        IntentFilter filter = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 调用系统下载的广播
     *
     * @author lijipei
     */
    public static class DownloadCompeleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            LogUtils.d("getAction", "---->" + intent.getAction());
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                if (downloadId == intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {

                    if (isShow) {
                        File file = new File(BaseConfig.installPath);
                        Intent i = new Intent();
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        i.setAction(Intent.ACTION_VIEW);
                        i.setDataAndType(FileUtils.getUriForFile(context, file),
                                "application/vnd.android.package-archive");
                        context.startActivity(i);
                    } else {
                        DialogUtil.showVersionDialog(context, "0", versionStr, messageStr,
                                "立即安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                    @Override
                                    public void ok() {
                                        File file = new File(BaseConfig.installPath);
                                        Intent i = new Intent();
                                        i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.setAction(Intent.ACTION_VIEW);
                                        i.setDataAndType(FileUtils.getUriForFile(context, file),
                                                "application/vnd.android.package-archive");
                                        context.startActivity(i);
                                    }

                                    @Override
                                    public void no() {

                                    }
                                });
                    }


                    //Toast.makeText(context, "下载完毕!", Toast.LENGTH_SHORT).show();
                }
            }
            context.unregisterReceiver(receiver);
        }
    }


    /*public static void updateViews(final long downlaodId) {
        final Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downlaodId);
                Cursor cursor = ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).query(q);
                cursor.moveToFirst();
                int bytes_downloaded = cursor
                        .getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                cursor.close();
                final int dl_progress = (bytes_downloaded * 100 / bytes_total);
                if (dl_progress == 100) {
                    myTimer.cancel();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
//                            mDownloadFileBtn.setText("下载完成");
//                            mProgressBar.setProgress(dl_progress);
                            UIUtils.Toast("下载完成");
                            LogUtils.e("下载进度","-:"+dl_progress);
                        }
                    });
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            mProgressBar.setProgress(dl_progress);
//                            mDownloadFileBtn.setText(dl_progress + "%");
                            LogUtils.e("下载进度","-:"+dl_progress+"%");
                        }
                    });

                }

            }

        }, 0, 10);

    }*/


}
