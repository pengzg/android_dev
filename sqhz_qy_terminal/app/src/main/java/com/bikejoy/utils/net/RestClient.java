package com.bikejoy.utils.net;

import android.content.Context;

import com.bikejoy.utils.ProgressUtils;
import com.bikejoy.utils.net.callback.IError;
import com.bikejoy.utils.net.callback.IFailure;
import com.bikejoy.utils.net.callback.IProgress;
import com.bikejoy.utils.net.callback.IRequest;
import com.bikejoy.utils.net.callback.ISuccess;
import com.bikejoy.utils.net.callback.RequestCallbacks;
import com.bikejoy.utils.net.download.DownloadHandler;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/8/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public final class RestClient<T> {
    private final WeakHashMap<String, Object> params;//请求参数
    private final String url;
    private final IRequest request;
    private final String downloadDir;
    private final String extension;
    private final String name;
    private final ISuccess success; //成功回调
    private final IFailure failure; //失败回调
    private final IError error; //错误回调
    private final RequestBody body; //返回信息主体
    private final boolean isLoading;//是否请求时显示加载框
    private final String dialog;//加载时显示文字
    private final File file;
    private final Context context;
    private final Class<T> mClass;//解析json时用到的泛型
    private final IProgress mProgress;

    ProgressUtils pu;

    public RestClient(WeakHashMap<String, Object> params, String url, IRequest request, String downloadDir,
                      String extension, String name, ISuccess success, IFailure failure, IError error,
                      RequestBody body, Class<T> mClass, boolean isLoading, String dialog, File file, Context context, IProgress mProgress) {
        this.params = params;
        this.url = url;
        this.request = request;
        this.downloadDir = downloadDir;
        this.extension = extension;
        this.name = name;
        this.success = success;
        this.failure = failure;
        this.error = error;
        this.body = body;
        this.isLoading = isLoading;
        this.file = file;
        this.dialog = dialog;
        this.context = context;
        this.mClass = mClass;
        this.mProgress = mProgress;
        pu = new ProgressUtils();//初始化加载样式
    }

    /**
     * 不带泛型参数,构建时默认返回字符串
     * @return
     */
    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    /**
     * 构建时传入泛型,解析时返回解析后的对象参数
     * @param t
     * @return
     */
    public static <T> RestClientBuilder builder(Class<T> t) {
        return new RestClientBuilder<>(t);
    }

    /**
     * 请求方式
     * @param httpMethod
     */
    private void request(HttpMethod httpMethod){
        final RestService service = RestCreate.getRestService();
        Call<String> call = null;
        if (request != null){
            request.onRequestStart();
        }

        if (isLoading){
            //显示加载框
            if (dialog == null){
                pu.showDialog(context);
            }else{
                pu.showDialog(context,dialog);
            }
        }

        switch (httpMethod){
            case GET:
                call = service.get(url,params);
                break;
            case POST:
                call = service.post(url,params);
                break;
            case POST_RAW:
                call = service.postRaw(url, body);
                break;
            case PUT:
                call = service.put(url, params);
                break;
            case PUT_RAW:
                call = service.putRaw(url, body);
                break;
            case DELETE:
                call = service.delete(url, params);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                call = service.upload(url, body);
                break;
            default:
                break;
        }

        if (call!=null){
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback(){
        RequestCallbacks callbacks = new RequestCallbacks(context,request,success,failure,error,mClass,isLoading,pu);
        return callbacks;
    }


    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (body == null) {
            request(HttpMethod.POST);
        } else {
            if (!params.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (body == null) {
            request(HttpMethod.PUT);
        } else {
            if (!params.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    public final void download() {
        new DownloadHandler(url, params,request, downloadDir, extension, name,
                success, failure, error,context,mProgress)
                .handleDownload();
    }

}
