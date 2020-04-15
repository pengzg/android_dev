package com.bikejoy.utils.http.operation;

import com.loopj.android.http.RequestParams;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.UIUtils;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/11/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class M_RequestParams {

    private final static String ROWS = "20";
    /**
     * 公共参数方法
     *
     * @param params
     * @param reqCode
     */
    public static void setCommonParams(RequestParams params, String reqCode) {



        params.put("reqCode", reqCode);
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");

    }



}
