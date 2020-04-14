package com.bikejoy.utils.net;

import android.content.Context;

import com.bikejoy.utils.net.callback.IError;
import com.bikejoy.utils.net.callback.IFailure;
import com.bikejoy.utils.net.callback.IProgress;
import com.bikejoy.utils.net.callback.IRequest;
import com.bikejoy.utils.net.callback.ISuccess;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/8/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RestClientBuilder<T> {

    private WeakHashMap<String, Object> mParams = new WeakHashMap<>();//请求参数
    private String mUrl = null;
    private IRequest mIRequest = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private RequestBody mBody = null;
    private boolean isLoading = false;
    private String mDialog = null;//加载时显示文字
    private File mFile = null;
    private Context mContext = null;
    private Class<T> mClass;
    private IProgress mProgress;

    public RestClientBuilder() {
    }

    public RestClientBuilder(Class<T> mClass) {
        this.mClass = mClass;
    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(String key, String value){
        mParams.put(key,value);
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String file) {
        this.mFile = new File(file);
        return this;
    }

    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final RestClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context, String mDialog) {
        this.isLoading = true;
        this.mDialog = mDialog;
        this.mContext = context;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.isLoading = true;
        this.mContext = context;
        return this;
    }

//    public final RestClientBuilder mClass(Class<T> mClass){
//        this.mClass = mClass;
//        return this;
//    }

    public final RestClientBuilder progress(IProgress mProgress){
        this.mProgress = mProgress;
        return this;
    }


    public final RestClient build(){
        return new RestClient<>(mParams,mUrl, mIRequest, mDownloadDir,
                mExtension, mName, mISuccess, mIFailure, mIError,
                mBody, mClass,isLoading,mDialog,mFile, mContext,mProgress);
    }

}
