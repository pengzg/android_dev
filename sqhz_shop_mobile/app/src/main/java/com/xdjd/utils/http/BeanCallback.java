package com.xdjd.utils.http;

import com.xdjd.utils.JsonUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/4
 *     desc   : 泛型回调数据解析
 *     version: 1.0
 * </pre>
 */

public abstract class BeanCallback<T> extends Callback<String> {

    private Class<T> clazz;//传入的泛型
    private T mT;//返回的泛型数据

    public BeanCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {

        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {
        if (clazz == String.class) {
            mT = (T) response;
        } else {
            mT = JsonUtils.parseObject(response, clazz);
        }
    }

    public abstract void updata(T jsonStr);

    public abstract void sendFail(ExceptionType s);

    /**
     * 自定义结束回调
     */
    public abstract void onFinish();
}
