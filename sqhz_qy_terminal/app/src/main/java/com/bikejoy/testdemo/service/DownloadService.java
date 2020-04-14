package com.bikejoy.testdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bikejoy.utils.net.RestClient;
import com.bikejoy.utils.net.callback.IError;
import com.bikejoy.utils.net.callback.IProgress;
import com.bikejoy.utils.net.callback.ISuccess;

import java.io.File;

/**
 * Created by lijipei on 2019/8/5.
 */

public class DownloadService extends Service {

    private DownloadBinder binder = new DownloadBinder();
    DownloadCallback callback;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 和activity通讯的binder
     */
    public class DownloadBinder extends Binder {
        public DownloadService getService(){
            return DownloadService.this;
        }
    }


    public void threadDownload(String downloadUrl,DownloadCallback callback0) {
        this.callback = callback0;
        RestClient.builder(File.class)
                .url(downloadUrl)
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.e("onError", msg);
                        if (callback!=null){
                            callback.failure(msg);
                        }
                    }
                })
                .success(new ISuccess<File>() {
                    @Override
                    public void onSuccess(final File file) {
                        if (callback!=null){
                            callback.success(file);
                        }
                    }
                })
                .progress(new IProgress() {
                    @Override
                    public void progress(int progress) {
                        //mProgress2.setProgress(progress);
                        if (callback!=null){
                            callback.progress(progress);
                        }
                    }
                })
                .build()
                .download();
    }

    public interface DownloadCallback{
        void progress(int progress);
        void failure(String msg);
        void success(File file);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
