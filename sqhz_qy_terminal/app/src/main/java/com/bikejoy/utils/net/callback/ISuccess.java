package com.bikejoy.utils.net.callback;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/8/3
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface ISuccess<T> {
    void onSuccess(T obj);
}
