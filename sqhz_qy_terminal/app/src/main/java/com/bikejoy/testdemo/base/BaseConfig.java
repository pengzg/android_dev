package com.bikejoy.testdemo.base;

import android.os.Environment;

import com.bikejoy.utils.UIUtils;

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
    public static final boolean isOfficial = true;
    //ds
    //http://yxtest.sqkx.net
    public static final String URL = "";

    // true-->显示所有日志
    // false-->关闭所有日志
    public static final boolean DEBUGLEVEL = true;

    /**
     * 友盟推送alias_type参数
     */
    public static final String Alias_Type = "USER_ID";
    public static final String miniProgramId = "gh_cc6cf58ca67a";

    /** 安装包下载地址 */
    public static final String installPath =  Environment
            .getExternalStorageDirectory() + "/Android/data/"+ UIUtils.getContext().getPackageName()+
            "/files/sss/sss.apk";// 下载安装包安装路径;

    /** sd卡默认地址 */
    public static final String sdUrl = "/sdcard/"+UIUtils.getContext().getPackageName()+"/";
    /** 图片地址 */
    public static final String sdUrlImg = sdUrl+"img/";

}
