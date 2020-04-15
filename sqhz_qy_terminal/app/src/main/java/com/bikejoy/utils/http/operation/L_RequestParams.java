package com.bikejoy.utils.http.operation;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.MD5Util;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.testdemo.bean.request.OrderDeliveryReq;

/**
 * 网络请求参数类
 * Created by lijipei on 2016/11/24.
 */
public class L_RequestParams {

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

    /**
     * 验证域名
     *
     * @return
     */
    public static RequestParams validateUrl() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", "");
        return params;
    }



}
