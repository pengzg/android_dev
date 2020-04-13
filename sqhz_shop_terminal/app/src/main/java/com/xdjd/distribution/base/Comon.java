package com.xdjd.distribution.base;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Comon {

    /** 扫描商品请求码 */
    public static final int QR_GOODS_REQUEST_CODE = 100;
    /** 扫描商品结果码 */
    public static final int QR_GOODS_RESULT_CODE = 101;

    /** 扫描客户档案卡请求码 */
    public static final int QR_CUSTOMER_CODE = 102;
    /** 扫描客户档案卡进行定位的请求码 */
    public static final int UPDATE_LOADING = 103;
    /*添加客户门店照片请求码*/
    public static  final int ADD_CUSTOMER_CODE = 104;
    /*添加客户门店照片返回码*/
    public static final int ADD_CUSTOMER_RESULT_CODE = 105;
    /*找回密码请求码*/
    public static final int FORGOT_PWD_REQUEST_CODE = 106;
    /** 现金日报简单打印模式 */
    public static final String CashType1 = "1";
    /** 现金日报精单打印模式 */
    public static final String CashType2 = "2";
    /** 现金日报详细打印模式 */
    public static final String CashType3 = "3";

    //---- 客户档案卡中:1.订单;2.订货;3.促销活动;4.应收款;5.收付款;6.拜访记录 ----
    /** 订单 */
    public static final int CD_ORDER = 1;
    /** 订货 */
    public static final int CD_DH = 2;
    /** 促销活动 */
    public static final int CD_ACTION = 3;
    /** 应收款 */
    public static final int CD_YS = 4;
    /** 收付款 */
    public static final int CD_SFK = 5;
    /** 拜访记录 */
    public static final int CD_VISIT = 6;
    /** 铺货查询 */
    public static final int CD_PH = 7;

    /** 百度地图的包名 */
    public static final String MY_BAIDU_PACKAGE = "com.baidu.BaiduMap";

    //商品状态 1 正 2 临 3残 4过
    /**
     * 正常品
     */
    public static final int GOODS_STATUS = 1;
    /** 临期 */
    public static final int GOODS_STATUS_L = 2;
    /** 残次品 */
    public static final int GOODS_STATUS_C = 3;
    /** 过期 */
    public static final int GOODS_STATUS_G = 4;


    //-------------交易收款类型:1.车销;2.配送任务;3.铺货销售;-------------
    /** 车销 */
    public static final int BUSINESS_CX = 1;
    /** 配送任务 */
    public static final int BUSINESS_RW = 2;
    /** 铺货销售 */
    public static final int BUSINESS_PHXS = 3;

}
