package com.xdjd.storebox.base;

import android.os.Environment;

import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

/**
 *
 * 公共配置类
 * Created by lijipei on 2016/11/15.
 */

public class BaseConfig {

    /**
     * 是否是正式环境
     * true:正式
     * false:测试
     */
    public static final boolean isOfficial = false;

    //http://testapp.sqkx.net :正式环境
    //http://testdev.sqkx.net
    public static final String URL = UserInfoUtils.getDomainName(UIUtils.getContext());/*"http://192.168.10.227:8080";*//*isOfficial ? "http://testapp.sqkx.net"
             : "http://testdev.sqkx.net";*/

    // true-->显示所有日志
    // false-->关闭所有日志
    public static final boolean DEBUGLEVEL = true;

    /**
     * 友盟推送alias_type参数
     */
    public static final String Alias_Type = "USER_ID";

    /** 安装包下载地址 */
    public static final String installPath =  Environment
            .getExternalStorageDirectory() + "/Android/data/"+ UIUtils.getContext().getPackageName()+
            "/files/xiaodianhezi/xiaodianhezi.apk";// 下载安装包安装路径;

}
