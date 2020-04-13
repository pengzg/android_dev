package com.xdjd.utils.http.url;


import com.xdjd.distribution.base.BaseConfig;

/**
 * Created by lijipei on 2016/11/24.
 */

public class M_Url {

    /**
     * 版本更新接口
     */
//    public static final String getNewVersion = BaseConfig.URL + "/b2b/app/baseB2BControl/getNewVersion.action";

    /** 验证域名地址 */
    public static final String validateUrl =  "/terminal/base/terminalSystemControl/validateUrl.action";

    /**
     * 登录
     */
    public static final String login = BaseConfig.URL + "/terminal/base/terminalSystemControl/login.action";
    /** 微信第三方登录 */
    public static final String thirdPartyLogin = BaseConfig.URL + "/terminal/base/terminalSystemControl/thirdPartyLogin.action";
    /** 绑定微信 */
    public static final String bindWx = BaseConfig.URL + "/terminal/base/terminalSystemControl/bindWx_login.action";

    /** 版本更新 */
    public static final String getNewVersion = BaseConfig.URL + "/terminal/base/terminalSystemControl/getNewVersion.action";

    /**
     * 获取用户信息
     */
    public static final String getUserInfo =  BaseConfig.URL + "/terminal/base/terminalSystemControl/getUserInfo.action";
    /** 根据线路id获取线路上的客户 */
    public static final String getLineCategory = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getLineCategory.action";
    /** 线路客户页面 搜索 刷新 */
    public static final String getLineCsSearch = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getLineCsSearch.action";

    /** 客户详细信息 */
    public static final String getCustomerInfo = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerInfo.action";
    /** 获取用户的线路配置信息 */
    public static final String getUserLineOrSettingInfo = BaseConfig.URL + "/terminal/base/terminalSystemControl/getUserLineOrSettingInfo.action";

    /** 扫码得到客户信息 */
    public static final String getCustomerInfoByCode = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerInfoByCode.action";

    /** 签到 */
    public static final String sign = BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/sign.action";
    /** 离店 */
    public static final String signOut = BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/signOut.action";
    /** 更新定位 */
    public static final String updateLocation = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/updateLocation.action";

    /** 上传头像 */
    public static final String updateImg = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/updateImg.action";

    /** 业务员一天的签到签退 */
    public static final String addUserSign = BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/addUserSign.action";
    /** 图片上传接口 */
    public static final String uploadImage = BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/uploadImage.action";


    /*得到附近店铺列表*/
    public static final String getNearbyShop = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getNearCustomerList.action";
    /*采集员得到店铺信息列表*/
    public static final String getCollectShop = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCaijiCustList.action";
    /*采集员添加店铺和定位店铺*/
    public static final String collectShopAndLocateShop = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/addCaijiCustInfo.action";
    /*根据手机号返回客户信息*/
    public static final String getGetCustomerInfo = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerInfoByMobile.action";
    /** 根据locationid得到客户信息 */
    public static final String getCustomerInfoByLocationid = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerInfoByLocationid.action";
    /** 添加客户类别和渠道 */
    public static final String getAddInfo = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getAddInfo.action";
    /** 添加客户接口 */
    public static final String addCustomer = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/addCustomer.action";
    /**业务员根据客户得到微信开通商城二维码*/
    public static final String showQrcodeImg = BaseConfig.URL + "/terminal/base/terminalCommonControl/showQrcodeImg.action";
//    public static final String showQrcodeImg = BaseConfig.URL + "/terminal/base/terminalCommonControl/showQrcodeImg.action";

    /** 开通商城 */
    public static final String openAccount = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/openAccount.action";
    /** 发送短信 */
    public static final String sendSms = BaseConfig.URL + "/terminal/base/terminalSystemControl/sendSms.action";

    /** 查询用户余额，安全欠款 */
    public static final String queryCusBalance = BaseConfig.URL + "/terminal/gl/terminalGlControl/queryCusBalance.action";

    /** 获取任务数量 */
    public static final String getTaskNum = BaseConfig.URL + "/terminal/order/terminalOrderControl/getTaskNum.action";

    /** 获取客户余额 */
    public static final String getCustomerBalance = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerBalance.action";
    /** 获取客户信息 */
    public static final String getCustomerList = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerList.action";

    /** 根据定位 或者类别 查询客户 */
    public static final String getCustomerToCategory = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerToSelectByCategory.action";

    /** 请求仓库接口 */
    public static final String queryStorehouseList = BaseConfig.URL + "/terminal/base/terminalBaseStorehouseController/queryStorehouseList.action";
    /** 获取商品分类列表接口 */
    public static final String queryGsCategory = BaseConfig.URL + "/terminal/base/terminalGsCategoryController/queryGsCategory.action";
    /** 获取商品列表 */
    public static final String getGoodsList = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getGoodsList.action";

    /** 获取还货分类接口 */
    public static final String queryApplyOrderGoods = BaseConfig.URL + "/terminal/apply/terminalApplyControl/queryApplyOrderGoods.action";
    /** 获取还货商品列表 */
    public static final String queryApplyOrderGoodsDetail = BaseConfig.URL + "/terminal/apply/terminalApplyControl/queryApplyOrderGoodsDetail.action";

    /** 获取商品销售类型接口 */
    public static final String getSaleTypeList = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getSaleTypeList.action";
    /** 获取获取商品状态 */
    public static final String getGoodsStatusList = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getGoodsStatusList.action";

    /** 陈列商品出库 */
    public static final String displayGoodsOutStore = BaseConfig.URL + "/terminal/erp/terminalErpControl/displayGoodsOutStore.action";
    /** 陈列出库查询列表 */
    public static final String getDisplayInList = BaseConfig.URL + "/terminal/erp/terminalErpControl/getDisplayInList.action";
    /** 陈列详情 */
    public static final String getDisplayInDetail = BaseConfig.URL + "/terminal/erp/terminalErpControl/getDisplayInDetail.action";

    /** 铺货商品列表 */
    public static final String phGsCategory = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/queryGsCategory.action";
    /** 铺货下单 */
    public static final String distributionOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/distributionOrder.action";
    /** 铺货店铺列表 */
    public static final String phShopList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phShopList.action";
    /**铺货商品详情列表*/
    public static final String phGoodsDetailList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phGoodsDetailList.action";
    /** 铺货单、铺货销售单、撤货单列表 */
    public static final String queryPhOrderList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/queryPhOrderList.action";
    /** 铺货单、铺货销售单、撤货单详情列表 */
    public static final String queryPhOrderDetailList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/queryPhOrderDetailList.action";
    /** 创建铺货销售单 */
    public static final String createPhSaleOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/createPhSaleOrder.action";
    /** 铺货列表按订单查询 */
    public static final String phOrderDetailList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phOrderDetailList.action";
    /*铺货店铺统计*/
    public static final String  phShopStatistic = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phShopStatistic.action";
   /** 铺货数据统计 */
   public static final String  phDataStatistic = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phDataStatistic.action";
    /** 根据铺货单查询销售单/撤货单 */
   public static final String saleOrderByPhOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/saleOrderByPhOrder.action";
   /** 根据销售单/撤货单查询铺货单 */
   public static final String  phOrderBySaleOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phOrderBySaleOrder.action";
    /** app生成退货单接口 */
    public static final String insertRefundPhGoods = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/insertRefundPhGoods.action";
    /** 铺货订单获取活动接口 */
    public static final String buildSettlementPhOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/buildSettlementPhOrder.action";
    /** 铺货申报下单接口 */
    public static final String insertApplyPhOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/insertApplyPhOrder.action";
    /** 铺货申报任务列表 */
    public static final String getPhTaskList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/getPhTaskList.action";
    /** 铺货任务详情列表 */
    public static final String getTaskPhDetail = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/getTaskPhDetail.action";
    /** 铺货配送接口 */
    public static final String deliverPhOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/deliverPhOrder.action";
    /** 铺货申报取消 */
    public static final String cancelApplyOrder = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/cancelApplyOrder.action";


    /** 管理端-商品铺货店铺/铺货店铺汇总 */
    public static final String phShopHzList = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/phShopHzList.action";
    /** 管理端-业务员铺货汇总 */
    public static final String ywyPhHz = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/ywyPhHz.action";


    /** 二维码扫描商品码 */
    public static final String getGoodsDetail = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getGoodsDetail.action";

    /** 提交盘点接口 */
    public static final String addInventory = BaseConfig.URL + "/terminal/inventory/terminalInventoryControl/addInventory.action";
    /** 盘点列表 */
    public static final String getInventoryList = BaseConfig.URL + "/terminal/inventory/terminalInventoryControl/getInventoryList.action";
    /** 盘点详情 */
    public static final String getInventoryDetail = BaseConfig.URL + "/terminal/inventory/terminalInventoryControl/getInventoryDetail.action";

    /** 支付方式列表 */
    public static final String getPayTypeList = BaseConfig.URL + "/terminal/order/terminalOrderControl/getPayTypeList.action";
    /** 获取商品品牌列表 */
//    public static final String getBrandAmountByGoods = BaseConfig.URL + "/terminal/order/terminalOrderControl/getBrandAmountByGoods.action";
    /** 车销出库生成单接口 */
    public static final String createCarOut = BaseConfig.URL + "/terminal/order/terminalOrderControl/createCarOut.action";
    /** 多订单车销订单生成接口 */
    public static final String createListCarOut = BaseConfig.URL + "/terminal/order/terminalOrderControl/createListCarOut.action";

    /** 配送任务 */
    public static final String getTaskList = BaseConfig.URL + "/terminal/order/terminalOrderControl/getTaskList.action";

    /** 生成订单 */
    public static final String createOrUpdateOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/createOrUpdateOrder.action";
    /** 多订单申报生成接口 */
    public static final String createOrUpdateListOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/createOrUpdateListOrder.action";

    /** 订单列表 */
    public static final String dataOrderGrid = BaseConfig.URL + "/terminal/order/terminalOrderControl/dataGrid.action";
    /** 查询订单详情 */
    public static final String getOrderDetail = BaseConfig.URL + "/terminal/order/terminalOrderControl/getDetail.action";
    /** 订单复制 */
    public static final String copyOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/copyOrder.action";

    /** 订货列表查询 */
    public static final String querySalesOrderApply = BaseConfig.URL + "/terminal/apply/terminalApplyControl/querySalesOrderApply.action";
    /** 订货详情查询 */
    public static final String querySalesOrderApplyDetail = BaseConfig.URL + "/terminal/apply/terminalApplyControl/querySalesOrderApplyDetail.action";

    /** 订单拒收 */
    public static final String updateRejectionOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/updateRejectionOrder.action";

    /** 生成结算信息接口(车销、申报) */
    public static final String bulidSettlementOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/bulidSettlementOrder.action";
    /** 配送任务生成结算单接口 */
    public static final String deliverySettlementOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/deliverySettlementOrder.action";

    /** 多订单 订单结算接口 */
    public static final String bulidSettlementListOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/bulidSettlementListOrder.action";

    /** 退货申请 */
    public static final String addRefund = BaseConfig.URL + "/terminal/erp/terminalErpControl/addRefund.action";
    /** 要货申请 */
    public static final String addRequireGoods = BaseConfig.URL + "/terminal/erp/terminalErpControl/addRequireGoods.action";
    /** 获取车库存全部商品 */
    public static final String getAllGoodsList = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getAllGoodsList.action";
    /** 退货申请接口(残次、临期、过期) */
    public static final String addRefundByGoodsStatus = BaseConfig.URL + "/terminal/erp/terminalErpControl/addRefundByGoodsStatus.action";
    /** 任务详情列表(配送任务和配送出库--未配送) */
    public static final String getTaskDetail = BaseConfig.URL + "/terminal/order/terminalOrderControl/getTaskDetail.action";
    /** 任务发货收款接口 */
    public static final String deliveryPaymentTaskOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/deliveryPaymentTaskOrder.action";

    /** 获取主仓库库存列表 */
    public static final String getMainStockGoods = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getMainStockGoods.action";
    /** 获取车仓库库存列表 */
    public static final String getStockGoods = BaseConfig.URL + "/terminal/goods/terminalGoodsControl/getStockGoods.action";

    /** 出库查询列表 */
    public static final String getStockOutList = BaseConfig.URL + "/terminal/erp/terminalErpControl/getStockOutList.action";
    /** 出库详情查询 */
    public static final String getStockOutDetail = BaseConfig.URL + "/terminal/erp/terminalErpControl/getStockOutDetail.action";

    /** 获取品牌列表接口 */
    public static final String queryBaseBrand = BaseConfig.URL + "/terminal/base/terminalBaseBrandsControl/queryBaseBrand.action";
    /** 查询客户品牌欠款 */
    public static final String queryCusBrandBalance = BaseConfig.URL + "/terminal/gl/terminalGlControl/queryCusBrandBalance.action";
    /** 客户收款提交接口 */
    public static final String makeCollections = BaseConfig.URL + "/terminal/gl/terminalGlControl/makeCollections.action";
    /** 收取客户欠款接口(按订单收取) */
    public static final String makeReceiveYsk = BaseConfig.URL + "/terminal/gl/terminalGlControl/makeReceiveYsk.action";

    /** 现金日报 */
    public static final String queryCashReport = BaseConfig.URL + "/terminal/gl/terminalGlControl/queryCashReport.action";

    /** 收付款查询 */
    public static final String queryCashList = BaseConfig.URL + "/terminal/gl/terminalGlControl/queryCashList.action";
    /** 收付款明细 */
    public static final String queryCashDetailList = BaseConfig.URL + "/terminal/gl/terminalGlControl/queryCashDetailList.action";
    /** 取消收款 */
    public static final String cancelGather = BaseConfig.URL + "/terminal/gl/terminalGlControl/cancelGather.action";

    /** 代销,订货列表查询 */
    public static final String queryApplyOrder = BaseConfig.URL + "/terminal/apply/terminalApplyControl/queryApplyOrder.action";

    /** 查询刷卡时的项目表 */
    public static final String queryBankItemList = BaseConfig.URL + "/terminal/base/terminalCommonControl/queryBankItemList.action";

    /** 单据补打列表 */
    public static final String printList = BaseConfig.URL + "/terminal/order/terminalOrderPrintControl/printList.action";
    /** 订单单据信息详情接口 */
    public static final String printOrder = BaseConfig.URL + "/terminal/order/terminalOrderPrintControl/printOrder.action";
    /** 收付款打印 */
    public static final String printGlCash = BaseConfig.URL + "/terminal/order/terminalOrderPrintControl/printGlCash.action";

    /** 业务员查看拜访提醒列表 */
    public static final String getCsAlarmList =  BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCsAlarmList.action";
    /** 业务员拜访列表 */
    public static final String getCsTaskList =  BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/getCsTaskList.action";

    /** 查询业务员消息列表 */
    public static final String queryUserMessage = BaseConfig.URL + "/terminal/message/terminalMessageControl/queryUserMessage.action";
    /** 读取消息 */
    public static final String readMessage = BaseConfig.URL + "/terminal/message/terminalMessageControl/readMessage.action";

    /** 修改密码 */
    public static final String updatePwd = BaseConfig.URL + "/terminal/base/terminalSystemControl/updatePwd.action";
    /*重置密码*/
    public static final String resetPwd = BaseConfig.URL + "/terminal/base/terminalSystemControl/findPwd.action";

    /** 添加异常接口 */
    public static final String addException = BaseConfig.URL + "/terminal/base/terminalSystemControl/addException.action";
    /** 订单状态列表 */
    public static final String getOrderStatsList = BaseConfig.URL + "/terminal/order/terminalOrderControl/getOrderStatsList.action";
    /** 取消订单--废弃*/
//    public static final String cancelOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/cancelOrder.action";
    /** 撤消订单 */
    public static final String updateCancelOrder = BaseConfig.URL + "/terminal/order/terminalOrderControl/updateCancelOrder.action";
    /** 订货取消订单 */
    public static final String cancelOrderDh = BaseConfig.URL + "/terminal/apply/terminalApplyControl/updateCancelOrder.action";
    /** 撤消出库订单 */
    public static final String updateCancelErp = BaseConfig.URL + "/terminal/erp/terminalErpControl/updateCancelErp.action";
    /** 客户反馈列表接口 */
    public static final String getFeedbackList = BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/getFeedbackList.action";
    /** 反馈回复接口 */
    public static final String updateFeedbackNote = BaseConfig.URL + "/terminal/cs/terminalUserTaskControl/updateFeedbackNote.action";
    /** 扫码获取奖品码 */
    public static final String getGiftPrizeDetailByCode = BaseConfig.URL + "/shop/dbh/dbhActivityController/getGiftPrizeDetailByCode_login.action";
    /** 奖品核销提交 */
    public static final String checkGiftWinning = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/checkGiftWinning_login.action";
    /** 查询核销奖品列表 */
    public static final String getGiftWinningList = BaseConfig.URL + "/terminal/dbh/terminalDbhControl /getGiftWinningList_login.action";

    /** 获取客户的线路列表 */
    public static final String getCustomerLines = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerLines.action";
    /** 更改客户线路 */
    public static final String updateCustomerLines = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/updateCustomerLines.action";

    /** 获取会员列表 */
    public static final String getMemberList = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/getMemberList_login.action";
    /** 获取设备列表 */
    public static final String getEquipmentList = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/getEquipmentList_login.action";
    /** 提交绑定设备和核销员接口 */
    public static final String relatedEqpAndMangerUser = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/relatedEqpAndMangerUser_login.action";
    /** 获取店铺关联详情 */
    public static final String getEquipmentRelatedDetail = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/getEquipmentRelatedDetail_login.action";
    /** 店铺结算汇总 */
    public static final String queryJsSum = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryJsSum_login.action";
    /** 业务核销列表 */
    public static final String queryHxList = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryHxList.action";
    /** 店铺结算每个商品的详情列表 */
    public static final String queryJsDetailList = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryJsDetailList.action";
    /** 业务员批量结算 */
    public static final String updateCheckState = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/updateCheckState.action";

    /** 获取已关联设备的店铺列表 */
    public static final String getShopList = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/getShopList_login.action";
    /** 修改店铺核销密码接口 */
    public static final String setUpHxPwd = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/setUpHxPwd_login.action";
    /** 根据客户手机号得到客户列表 */
    public static final String getCustomerListByMobile = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getCustomerListByMobile.action";

    //==============管理员界面接口=================
    /** 首页参数接口 */
    public static final String getOperatingData =  BaseConfig.URL + "/terminal/report/terminalReportControl/getOperatingData.action";
    /** 上交款列表 */
    public static final String getHandinList = BaseConfig.URL + "/terminal/report/terminalReportControl/getHandinList.action";
    /** 业务员销售/退货排行 */
    public static final String getSalesList = BaseConfig.URL + "/terminal/report/terminalReportControl/getSalesList.action";
    /** 商品销售/退货排行 */
    public static final String getGoodsSaleList = BaseConfig.URL + "/terminal/report/terminalReportControl/getGoodsSaleList.action";
    /** 商品库存数量 */
    public static final String getGoodsStock = BaseConfig.URL + "/terminal/report/terminalReportControl/getGoodsStock.action";
    /** 获取业务员列表 */
    public static final String getSalesdocList = BaseConfig.URL + "/terminal/report/terminalReportControl/getSalesdocList.action";
    /** 获取拜访明细 */
    public static final String getReportTaskList = BaseConfig.URL + "/terminal/report/terminalReportControl/getReportTaskList.action";
    /** 业务员拜访次数排名列表 */
    public static final String getSalesVisitNum = BaseConfig.URL + "/terminal/report/terminalReportControl/getSalesVisitNum.action";

    /** 管理端-业务员销售排行 */
    public static final String ywySaleRank = BaseConfig.URL + "/terminal/apply/terminalAgencysalesControl/ywySaleRank.action";

    /** 人员管理信息 */
    public static final String getVisitData = BaseConfig.URL + "/terminal/report/terminalReportControl/getVisitData.action";

    /** 营收简报 */
    public static final String getBriefing = BaseConfig.URL + "/terminal/report/terminalReportControl/getBriefing.action";
    /** 营收趋势 */
    public static final String getTrendsChart = BaseConfig.URL + "/terminal/report/terminalReportControl/getTrendsChart.action";
    /** 客户订单统计 */
    public static final String getCustOrder = BaseConfig.URL + "/terminal/report/terminalReportControl/getCustOrder.action";
    /** 商品销售分类统计 */
    public static final String getGoodsSalesCategory = BaseConfig.URL + "/terminal/report/terminalReportControl/getGoodsSalesCategory.action";
    /** 退货统计 */
    public static final String getRefundReport = BaseConfig.URL + "/terminal/report/terminalReportControl/getRefundReport.action";
    /** 业务员访店达成率 */
    public static final String getVisitRate = BaseConfig.URL + "/terminal/report/terminalReportControl/getVisitRate.action";
    /** 获取业务员位置 */
    public static final String getSalesLocation = BaseConfig.URL + "/terminal/report/terminalReportControl/getSalesLocation.action";

    /** 得到访店达成率按天统计 */
    public static final String getVisitRateDay = BaseConfig.URL + "/terminal/report/terminalReportControl/getVisitRateDay.action";

    /** 获取客户数量接口 */
    public static final String getCustomerNum = BaseConfig.URL + "/terminal/report/terminalReportControl/getCustomerNum.action";
    /** 得到客户订单总金额排序 */
    public static final String getCustOrderAmountList = BaseConfig.URL + "/terminal/report/terminalReportControl/getCustOrderAmountList.action";

    /** 得到客户应收款汇总 */
    public static final String getReceivableSumList = BaseConfig.URL + "/terminal/gl/terminalGlControl/getReceivableSumList.action";
    /** 得到客户应收款列表 */
    public static final String getGlReceivableList = BaseConfig.URL + "/terminal/gl/terminalGlControl/getGlReceivableList.action";

    /** 订货、代销结算接口 */
    public static final String applyOrder = BaseConfig.URL + "/terminal/apply/terminalApplyControl/applyOrder.action";

    /** 客户位置 */
    public static final String getGlyNearCustomerList = BaseConfig.URL + "/terminal/cs/terminalCustomerControl/getGlyNearCustomerList.action";
    /** 获取核销统计 */
    public static final String queryHxSum = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryHxSum_login.action";
    /** 获取活动列表 */
    public static final String getActivityList = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/getActivityList_login.action";
    /** 查询全部已关联了设备和周围的店铺列表 */
    public static final String getAllShopList = BaseConfig.URL + "/terminal/dbh/terminalDbhControl/getAllShopList_login.action";

    /** 用户统计-今日指标  */
    public static final String queryUserTodayStats = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryUserTodayStats_login.action";
    /** 用户统计-趋势分析 */
    public static final String queryUserMonthlyStats = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryUserMonthlyStats_login.action";

    /** 活动统计-今日指标  */
    public static final String queryActivityTodayStats = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryActivityTodayStats_login.action";
    /** 活动统计-趋势分析 */
    public static final String queryActivityPeriodStats = BaseConfig.URL + "/terminal/dbh/terminalDbhReportControl/queryActivityPeriodStats_login.action";

}
