package com.bikejoy.testdemo.base;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   : 公共参数类
 *     version: 1.0
 * </pre>
 */

public class Common {

    /**
     * 扫描商品请求码
     */
    public static final int QR_GOODS_REQUEST_CODE = 100;
    /**
     * 扫描商品结果码
     */
    public static final int QR_GOODS_RESULT_CODE = 101;

    /**
     * 扫描客户档案卡请求码
     */
    public static final int QR_CUSTOMER_CODE = 102;
    /**
     * 扫描客户档案卡进行定位的请求码
     */
    public static final int UPDATE_LOADING = 103;

    /**
     * 添加写字楼请求码
     */
    public static final int LOCATION_REQUEST_CODE = 101;


    /**
     * 进货入库
     */
    public static final String stockType1 = "10101";// 进货入库
    /**
     * 主到车调拨
     */
    public static final String stockType2 = "20101"; //主到车调拨
    /**
     * 车到主调拨
     */
    public static final String stockType3 = "20102";// 车到主调拨
    /**
     * 撤货
     */
    public static final String stockType4 = "20104";//  撤货



    // 企业端用户角色
    // 客户开发3001
    // 客户开发主管3002
    // 配送员3003
    // 调度员3004
    // 配送站3005
    // 库管人员3006
    // 采购员3007
    // 财务人员权限3008
    // 总经理3009
    // 设备运维权限3010
    // 管理员权限3011

    /** 客户开发3001 */
    public static final String ROLE3001 = "3001";
    /** 客户开发主管3002 */
    public static final String ROLE3002 = "3002";
    /** 配送员3003 */
    public static final String ROLE3003 = "3003";
    /** 调度员3004 */
    public static final String ROLE3004 = "3004";
    /** 配送站3005 */
    public static final String ROLE3005 = "3005";
    /** 库管人员3006 */
    public static final String ROLE3006 = "3006";
    /** 采购员3007 */
    public static final String ROLE3007 = "3007";
    /** 财务人员权限3008 */
    public static final String ROLE3008 = "3008";
    /** 总经理3009 */
    public static final String ROLE3009 = "3009";
    /** 设备运维权限3010 */
    public static final String ROLE3010 = "3010";
    /** 管理员权限3011 */
    public static final String ROLE3011 = "3011";

    // 1 未处理 2配送中 3已完成 4已取消
    /** 配送申请类型 -未处理*/
    public static final String TicketSendApplyType1 = "1";
    /** 配送申请类型 -配送中*/
    public static final String TicketSendApplyType2 = "2";
    /** 配送申请类型 -已完成 */
    public static final String TicketSendApplyType3 = "3";
    /** 配送申请类型 -已取消*/
    public static final String TicketSendApplyType4 = "4";


    // 订单类型 1 水票订单 2 送水订单 3 购物订单
    /** 水票订单*/
    public static final String orderType1 = "1";
    /** 送水订单*/
    public static final String orderType2 = "2";
    /** 购物订单 */
    public static final String orderType3 = "3";

    //单据操作类型 30102:送货记录  30103:报损
    /**
     * 30102:送货记录
     */
    public static final String stockType30102 = "30102";
    /**
     * 30103:报损
     */
    public static final String stockType30103 = "30103";



    //1	普通	 2合伙人  3金牌合伙人  4社区
    /** 合作类型--普通 */
    public static final String agenttype1 = "1";
    /** 合作类型--合伙人 */
    public static final String agenttype2 = "2";
    /** 合作类型--金牌合伙人 */
    public static final String agenttype3 = "3";
    /** 合作类型--社区 */
    public static final String agenttype4 = "4";

    //表格点击参数
    /**
     * 订单
     */
    public static final String typeOrder = "1";
    /**
     * 配送任务
     */
    public static final String typeDistrTask = "2";
    /**
     * 团购订单
     */
    public static final String typeGroupOrder = "3";
    /**
     * 优惠券
     */
    public static final String typeCounpon = "4";
    /**
     * 拜访列表
     */
    public static final String typeNote = "5";
    /**
     * 库存管理
     */
    public static final String typeStockControl = "6";
    /**
     * 柜子
     */
    public static final String typeStock = "7";
    /**
     * 会员列表
     */
    public static final String typeMember = "8";
    /**
     * 柜子销量
     */
    public static final String typeStockSales = "9";
    /**
     * 柜子日志
     */
    public static final String typeStockLog = "10";
    /**
     * 柜子警告日志
     */
    public static final String typeAlertLog = "11";
    /**
     * 商家电子券
     */
    public static final String typeTicketList = "12";
    /**
     * 取水记录
     */
    public static final String typeWaterRecord = "13";
    /**
     * 水票赠送列表
     */
    public static final String typeMemberGift = "14";
    /**
     * 车仓库
     */
    public static final String typeCardStock = "15";
    /**
     * 入库申请
     */
    public static final String typePurchaseApply = "16";
    /**
     * 入库查询
     */
    public static final String typePurchaseSearch = "17";
    /**
     * 调拨申请
     */
    public static final String typeAllotApply = "18";
    /**
     * 调拨查询
     */
    public static final String typeAllotSearch = "19";
    /**
     * 添加写字楼
     */
    public static final String typeAddBuilding = "20";
    /**
     * 写字楼列表
     */
    public static final String typeBuildingList = "21";
    /**
     * 申请配送单
     */
    public static final String typeTicketSendApplyList = "22";
    /**
     * 待发货订单
     */
    public static final String typeSendTheGoods = "23";
    /**
     * 待出库清单(配货清单)
     */
    public static final String typeDeliveryProducAndNum = "24";
    /**
     * 调拨查询
     */
    public static final String typeDeliveryWaterSearch = "25";
    /**
     * 客户池
     */
    public static final String typeCustomerPool = "26";
    /**
     * 体验套餐列表
     */
    public static final String typePackageList = "27";
    /**
     * 申请的套餐
     */
    public static final String typeApplyPackageList = "28";
    /**
     * 得到工作人员自己的套餐列表
     */
    public static final String typePackageRelationList = "29";
    /**
     * 注册分享小程序
     */
    public static final String typeShareRegister = "30";
    /**
     * 客户申请记录
     */
    public static final String typeApplyMemberList = "31";
    /**
     * 待审核列表
     */
    public static final String typeToAudit = "32";
    /**
     * 商品报损
     */
    public static final String typeBreakageApply = "33";
    /**
     * 报损查询
     */
    public static final String typeBreakageSearch = "34";
    /**
     * (开发主管)我的领用申请
     */
    public static final String typeApplyLeaderPackageList = "35";
    /**
     * 领用的库存
     */
    public static final String typePackageRelationStore = "36";
    /**
     * 全部客户
     */
    public static final String typeAllMember = "37";
    /**
     * 全部金牌合伙人列表
     */
    public static final String typeBaseAgent = "38";
    /**
     * 库存查询
     */
    public static final String typeStockInquiry = "39";
    /**
     * 办公领用
     */
    public static final String typeOfficeOfRecipients = "40";
    /**
     * 全部合伙人列表资金汇总
     */
    public static final String typeQueryAgentAmountData = "41";

    /**
     * 提现列表
     */
    public static final String typeCashApply = "42";
    /**
     * 发票列表
     */
    public static final String typeInvoice = "43";

    /**
     * 全部合伙人列表资金汇总
     */
    public static final String typeQueryProfitRecommendListData = "45";


    /**
     * 匹配星期字符串
     *
     * @param positionStr
     * @return
     */
    public static String weekdayStr(String positionStr) {
        String str = "";
        switch (positionStr) {
            case "1":
                str = "周一";
                break;
            case "2":
                str = "周二";
                break;
            case "3":
                str = "周三";
                break;
            case "4":
                str = "周四";
                break;
            case "5":
                str = "周五";
                break;
            case "6":
                str = "周六";
                break;
            case "7":
                str = "周日";
                break;
            default:
                str = "周一";
                break;
        }
        return str;
    }


}
