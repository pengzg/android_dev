package com.xdjd.distribution.base;

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
    //http://test.sqkx.net
    public static final String URL = /*"http://sjfx.sqkx.net";*/UserInfoUtils.getDomainName(UIUtils.getContext());/*isOfficial ? "http://test2.sqkx.net"
             : "http://test2.sqkx.net";*/

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
            "/files/hezifenxiao/hezifenxiao.apk";// 下载安装包安装路径;

    /** sd卡默认地址 */
    public static final String sdUrl = "/sdcard/"+UIUtils.getContext().getPackageName()+"/";
    /** 图片地址 */
    public static final String sdUrlImg = sdUrl+"img/";

    //订单类型 1 普通 2 处理 3 退货 4 换货 5还货
    /** 普通订单 */
    public static final int OrderType1 = 1;
    /** 处理单 */
    public static final int OrderType2 = 2;
    /** 退货单 */
    public static final int OrderType3 = 3;
    /** 换货单 */
    public static final int OrderType4 = 4;
    /** 还货 */
    public static final int OrderType5 = 5;
    /** 销售 */
    public static final int OrderType6 = 6;

    //	用户类型 	1  车销
    //    2   跑单
    //    4   配送员
    //    5   管理员
    //管理员的
    public static final String userTypeAdministrator = "5";
    /**采集员*/
    public static final String collectMan = "6";
    /** 车销 */
    public static final String userTypeCardSell = "1";
    /** 跑单 */
    public static final String userTypeRunAsingle = "2";
    /** 配送 */
    public static final String userTypeDistribution = "4";

    //1 预收款 2 收欠款
    /** 预收款 */
    public static final int ReceiptAdvancePayments = 1;
    /** 欠款 */
    public static final int ReceiptDebt = 2;

    /**
     * 80打印机
     */
    public static final int printer80 = 80;
    public static final int printer58 = 58;

}
