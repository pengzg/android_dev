package com.bikejoy.utils.net.download;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.bikejoy.utils.FileUtils;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.net.RestCreate;
import com.bikejoy.utils.net.callback.IError;
import com.bikejoy.utils.net.callback.IFailure;
import com.bikejoy.utils.net.callback.IProgress;
import com.bikejoy.utils.net.callback.IRequest;
import com.bikejoy.utils.net.callback.ISuccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class DownloadHandler {

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS;
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;//下载的目录
    private final String EXTENSION;//后缀
    private final String NAME;//文件名
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final Context mContext;
    private final IProgress mProgress;

    public DownloadHandler(String url,
                           WeakHashMap<String, Object> params,
                           IRequest request,
                           String downDir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error,
                           Context mContext,
                           IProgress mProgress) {
        this.URL = url;
        this.PARAMS = params;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = downDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.mContext = mContext;
        this.mProgress = mProgress;
    }

    private static final String PATH_CHALLENGE_VIDEO = Environment
            .getExternalStorageDirectory() + "/Android/data/"+ UIUtils.getContext().getPackageName()+
            "/files/testdemo"; //Environment.getExternalStorageDirectory() + "/DownloadFile";
    private File mFile;
    private Thread mThread;
    private String mDownloadPath; //下载到本地的路径

    public final void handleDownload() {
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        Log.e("DownloadHandler",PATH_CHALLENGE_VIDEO);
        //通过Url得到保存到本地的文件名
        String name = URL;
        if (FileUtils.createOrExistsDir(PATH_CHALLENGE_VIDEO)) {
            int i = name.lastIndexOf('/');//一定是找最后一个'/'出现的位置
            if (i != -1) {
                name = name.substring(i);
                Log.e("DownloadHandler", name);
                mDownloadPath = PATH_CHALLENGE_VIDEO +
                        name;
            }
        }

        if (TextUtils.isEmpty(mDownloadPath)) {
            Log.e("DownloadHandler", "downloadVideo: 存储路径为空了:"+mDownloadPath);
            return;
        }
        //建立一个文件
        mFile = new File(mDownloadPath);

        RestCreate.getRestService()
                .download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            /*final ResponseBody responseBody = response.body();
                            Log.e("updateVersion","contentLength:"+responseBody.contentLength());
                            final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS,mContext);
                            //使用asynctask多线程方式下载
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    DOWNLOAD_DIR, EXTENSION, responseBody, NAME);

                            //这里一定要注意判断，否则文件下载不全
                            if (task.isCancelled()) {
                                if (REQUEST != null) {
                                    REQUEST.onRequestEnd();
                                }
                            }*/

                            //下载文件放在子线程
                            mThread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    //保存到本地
                                    writeFile2Disk(response, mFile);
                                }
                            };
                            mThread.start();
                        } else {
                            if (ERROR != null) {
                                ERROR.onError(response.code(), response.message());
                            }
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        if (FAILURE != null) {
                            FAILURE.onFailure();
                        }
                    }
                });
    }


    private void writeFile2Disk(Response<ResponseBody> response, File file) {
        long currentLength = 0;
        OutputStream os = null;

        if (response.body() == null) {
            ERROR.onError(response.code(),"资源错误！");
            return;
        }
        InputStream is = response.body().byteStream();
        long totalLength = response.body().contentLength();

        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                Log.e("DownloadHandler", "当前进度: " + currentLength);
                int progress = (int) (100 * currentLength / totalLength);
                mProgress.progress(progress);
                if ((int) (100 * currentLength / totalLength) == 100) {
//                    downloadListener.onFinish(mVideoPath);
                    SUCCESS.onSuccess(file);
                }
            }
        } catch (FileNotFoundException e) {
            ERROR.onError(response.code(),"未找到文件！");
            e.printStackTrace();
        } catch (IOException e) {
            ERROR.onError(response.code(),"IO错误！");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
