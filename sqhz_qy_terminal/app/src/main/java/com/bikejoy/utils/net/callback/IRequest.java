package com.bikejoy.utils.net.callback;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/8/3
 *     desc   : 请求开始结束接口
 *     version: 1.0
 * </pre>
 */

public interface IRequest {

    void onRequestStart();
    void onRequestEnd();
}
