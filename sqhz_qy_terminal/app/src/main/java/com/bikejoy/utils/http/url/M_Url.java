package com.bikejoy.utils.http.url;


import com.bikejoy.testdemo.base.BaseConfig;

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
    public static final String login = BaseConfig.URL + "/mobile/qy/loginControl/login.action";
    /** 微信第三方登录 */
    public static final String thirdLogin = BaseConfig.URL + "/mobile/qy/loginControl/thirdLogin.action";
    /** 绑定微信 */
    public static final String bindWx = BaseConfig.URL + "/terminal/base/terminalSystemControl/bindWx_login.action";
    /** 用户角色列表 */
    public static final String queryRoleList = BaseConfig.URL + "/mobile/qy/memberControl/queryRoleList.action";


    /** 版本更新 */
    public static final String getNewVersion = BaseConfig.URL + "/mobile/qy/loginControl/getNewVersion.action";


    /** 获取订单列表信息 */
    public static final String queryOrderList =  BaseConfig.URL + "/mobile/qy/orderControl/queryOrderList.action";
    /** 获取订单详情信息 */
    public static final String queryOrderDetailList = BaseConfig.URL + "/mobile/qy/orderControl/queryOrderDetailList.action";
    /** 自由购订单开箱记录 */
    public static final String boxOperationLog = BaseConfig.URL + "/mobile/qy/orderControl/boxOperationLog.action";

    /** 根据柜子查询销售订单 */
    public static final String dataGridStorehouseOrder = BaseConfig.URL +"/mobile/qy/orderControl/dataGridStorehouseOrder.action";

    /** 水票下单明细列表 */
    public static final String queryWaterVoteStatementList =  BaseConfig.URL + "/mobile/qy/orderControl/queryWaterVoteStatementList.action";

    /** 首页营收简报 */
    public static final String getBriefing = BaseConfig.URL + "/mobile/qy/reportControl/getBriefing.action";
    /** 营收趋势 */
    public static final String getTrendsChart = BaseConfig.URL + "/mobile/qy/reportControl/getTrendsChart.action";
    /** 用户增长趋势 */
    public static final String getTrendsNewMember = BaseConfig.URL + "/mobile/qy/reportControl/getTrendsNewMember.action";

    /** 获取配送订单任务列表 */
    public static final String queryDeliveryList =  BaseConfig.URL + "/mobile/qy/orderControl/queryDeliveryList.action";
    /** 获取配送任务订单详情信息 */
    public static final String queryDeliveryDetail = BaseConfig.URL + "/mobile/qy/orderControl/queryDeliveryDetail.action";
    /** 获取某订单下的全部配送记录列表 */
    public static final String selectDeliveryList = BaseConfig.URL + "/mobile/qy/orderControl/selectDeliveryList.action";
    /** 配送任务确认送达 */
    public static final String confirmDeliveryOrder = BaseConfig.URL + "/mobile/qy/orderControl/confirmDeliveryOrder.action";
    /** 配送任务确认送达2 */
    public static final String confirmDeliveryOrder2 = BaseConfig.URL + "/mobile/qy/orderControl/confirmDeliveryOrder2.action";
    /** 获取客户默认地址 */
    public static final String getDefaultAddress = BaseConfig.URL + "/mobile/qy/memberControl/getDefaultAddress.action";

    /** 线路客户列表 */
    public static final String customerList = BaseConfig.URL + "/mobile/qy/memberControl/customerList.action";
    /** 获取新增会员统计 */
    public static final String queryMemberNum = BaseConfig.URL + "/mobile/qy/memberControl/queryMemberNum.action";
    /** 获取客户水票订单 */
    public static final String queryWaterVoteOrderList = BaseConfig.URL + "/mobile/qy/orderControl/queryWaterOrderList.action";
    /** 获取相同商品水票的可用总数量 */
    public static final String queryWaterAvailableTotalNum = BaseConfig.URL + "/mobile/qy/orderControl/queryWaterAvailableTotalNum.action";

    /** 订单确认送达 */
    public static final String confirmOrder = BaseConfig.URL + "/mobile/qy/orderControl/confirmOrder.action";
    /** 修改密码 */
    public static final String updatePwd = BaseConfig.URL + "/mobile/qy/memberControl/updatePwd.action";
    /** 会员申请列表 */
    public static final String dataGridApplyMember = BaseConfig.URL + "/mobile/qy/memberApplyControl/dataGridApplyMember.action";
    /** 获取会员申请活动详情 */
    public static final String getApplyDetail = BaseConfig.URL + "/mobile/qy/memberApplyControl/getDetail.action";
    /** 审核会员申请 */
    public static final String checkApply = BaseConfig.URL + "/mobile/qy/orderControl/checkApply.action";

    /** 获取柜子列表 */
    public static final String queryStoreList = BaseConfig.URL + "/mobile/qy/storeControl/queryStoreList.action";
    /** 获取柜子箱子列表 */
    public static final String queryBoxList = BaseConfig.URL + "/mobile/qy/storeControl/queryBoxList.action";
    /** 补货 */
    public static final String updateBoxState = BaseConfig.URL + "/mobile/qy/storeControl/updateBoxState.action";

    /** 机械柜补货 */
    public static final String updateBoxStateTypeThree = BaseConfig.URL + "/mobile/qy/storeControl/updateBoxStateTypeThree.action";

    /** 上传图片 */
    public static final String uploadImg = BaseConfig.URL + "/mobile/qy/storeControl/uploadImg.action";
    /** 更新仓库 */
    public static final String updateStore = BaseConfig.URL + "/mobile/qy/storeControl/updateStore.action";
    /** 获取柜子地图信息列表 */
    public static final String queryMapStoreList = BaseConfig.URL + "/mobile/qy/storeControl/queryMapStoreList.action";

    /** 生成水票结算信息 */
    public static final String settlementUseTicket = BaseConfig.URL + "/mobile/qy/orderControl/settlementUseTicket.action";
    /** 创建订单 */
    public static final String createOrderUseTicket = BaseConfig.URL + "/mobile/qy/orderControl/createOrderUseTicket.action";

    /** 柜子销售明细列表 */
    public static final String getStoreSalesSubsidiary = BaseConfig.URL + "/mobile/qy/reportControl/getStoreSalesSubsidiary.action";
    /** 柜子操作明细详情列表 */
    public static final String getStoreSalesSubsidiaryDetail = BaseConfig.URL + "/mobile/qy/reportControl/getStoreSalesSubsidiaryDetail.action";


    /** 获取团购信息列表信息 */
    public static final String dataOrderGroupGrid =  BaseConfig.URL + "/mobile/qy/orderControl/dataOrderGroupGrid.action";
    /** 自动成团接口 */
    public static final String updateSuccessAuto = BaseConfig.URL + "/mobile/qy/orderControl/updateSuccessAuto.action";
    /** 获取团购订单详情 */
    public static final String orderGroupDetail =  BaseConfig.URL + "/mobile/qy/orderControl/orderGroupDetail.action";
    /** 获取团购信息列表信息 */
    public static final String dataGridOrderGroup =  BaseConfig.URL + "/mobile/qy/orderControl/dataGridOrderGroup.action";

    /** 获取商品列表信息 */
    public static final String productMainDataGrid =  BaseConfig.URL + "/mobile/qy/productController/productMainDataGrid.action";
    /** 获取促销活动列表信息 */
    public static final String productPromotionMainDataGrid = BaseConfig.URL +
            "/mobile/qy/productController/productPromotionMainDataGrid.action";
    /** 自动开团 */
    public static final String insertGroupAuto =  BaseConfig.URL + "/mobile/qy/productController/insertGroupAuto.action";

    /** 获取柜子总的库存明细 */
    public static final String getTotalInventory = BaseConfig.URL + "/mobile/qy/reportControl/getTotalInventory.action";

    /** 获取所有仓库下面的商品列表 */
    public static final String dataGridSku = BaseConfig.URL + "/mobile/qy/erpControl/dataGridSku.action";
    /** 获取某个仓库下面的商品列表 */
    public static final String dataGridStore = BaseConfig.URL + "/mobile/qy/erpControl/dataGridStore.action";
    /** 提交调拨或采购信息 */
    public static final String insertErpInfo = BaseConfig.URL + "/mobile/qy/erpControl/insertErpInfo.action";
    /** 获取erp列表 */
    public static final String getErpList = BaseConfig.URL + "/mobile/qy/erpControl/getErpList.action";
    /** 获取erp详情 */
    public static final String getErpDetail = BaseConfig.URL + "/mobile/qy/erpControl/getErpDetail.action";
    /** 审核 */
    public static final String updateCheckErp =  BaseConfig.URL + "/mobile/qy/erpControl/updateCheckErp.action";
    /** 获取供应商列表 */
    public static final String getCustomerList = BaseConfig.URL + "/mobile/qy/csControl/getCustomerList.action";

    /** 扫码上传信息 */
    public static final String doRelation = BaseConfig.URL + "/mobile/qy/loginControl/doRelation.action";

    /** 添加柜子信息 */
    public static final String addStore = BaseConfig.URL + "/mobile/qy/storeControl/addStore.action";
    /** 查询柜子关联商品列表 */
    public static final String productStoreRelationDataGrid = BaseConfig.URL + "/mobile/qy/productController/productStoreRelationDataGrid.action";
    /** 添加柜子商品关联 */
    public static final String productStoreRelationUpdate = BaseConfig.URL + "/mobile/qy/productController/productStoreRelationUpdate.action";

    /** 查询柜子日志 */
    public static final String queryMonitoringLogList = BaseConfig.URL + "/mobile/qy/storeControl/queryMonitoringLogList.action";
    /** 查询柜子警告日志 */
    public static final String baseAlarmLog = BaseConfig.URL + "/mobile/qy/storeControl/baseAlarmLog.action";

    /** 切换店铺信息 */
    public static final String switchShop = BaseConfig.URL + "/mobile/qy/loginControl/switchShop.action";

    /** 获取可用优惠券 */
    public static final String queryCouponList = BaseConfig.URL + "/mobile/qy/workCouponControl/queryCouponList.action";

    /** 业务员扫码收款 */
    public static final String scanPay = BaseConfig.URL + "/mobile/storeApp/storeAppPayControl/scanPay.action";

    /** 赠送的优惠券列表 */
    public static final String getCouponList = BaseConfig.URL + "/mobile/qy/workCouponControl/getCouponList.action";
    /** 赠送优惠券 */
    public static final String sendCouponToMember = BaseConfig.URL + "/mobile/qy/workCouponControl/sendCouponToMember.action";

    /** 消息列表 */
    public static final String queryMessageList = BaseConfig.URL + "/mobile/qy/workMessageControl/queryMessageList.action";
    /** 标记消息已读 */
    public static final String readMessage = BaseConfig.URL + "/mobile/qy/workMessageControl/readMessage.action";
    /** 小程序分享字段 */
    public static final String queryCouponShare = BaseConfig.URL + "/mobile/qy/workCouponControl/queryCouponShare.action";

    /** 拜访新增接口 */
    public static final String addNote = BaseConfig.URL + "/mobile/qy/memberControl/addNote.action";
    /** 拜访记录查询 */
    public static final String queryNoteList = BaseConfig.URL + "/mobile/qy/memberControl/queryNoteList.action";

    /** 查询会员下电子券 */
    public static final String queryTicketListByMember = BaseConfig.URL + "/mobile/qy/commonQueryControl/queryTicketListByMember.action";
    /** 查询商家下电子券 */
    public static final String queryTicketListByAll = BaseConfig.URL + "/mobile/qy/commonQueryControl/queryTicketListByAll.action";
    /** 查询电子券核销记录 */
    public static final String queryTicketVerificationList = BaseConfig.URL + "/mobile/qy/commonQueryControl/queryTicketVerificationList.action";
    /** 查询会员取水记录 */
    public static final String queryWaterRecordList = BaseConfig.URL + "/mobile/qy/commonQueryControl/queryWaterRecordList.action";

    /** 得到业务员列表 */
    public static final String getMemberBaseWorkList =  BaseConfig.URL + "/mobile/qy/memberControl/getMemberBaseWorkList.action";
    /** 更换发货单业务员 */
    public static final String updateDeliver = BaseConfig.URL + "/mobile/qy/orderControl/updateDeliver.action";
    /** 获取待出库列表 */
    public static final String queryDckGoodsListNoStore = BaseConfig.URL + "/mobile/qy/orderControl/queryDckGoodsListNoStore.action";
    /** 发货 */
    public static final String insertOrderDelivery = BaseConfig.URL + "/mobile/qy/orderControl/insertOrderDelivery.action";
    /** 发货-不用传递仓库id */
    public static final String insertOrderDeliveryNoStore = BaseConfig.URL + "/mobile/qy/orderControl/insertOrderDeliveryNoStore.action";

    /** 用户转增列表 */
    public static final String getMemberGiftList = BaseConfig.URL + "/mobile/qy/memberControl/getMemberGiftList.action";
    /** 消息标记全部已读 */
    public static final String readMessageAll = BaseConfig.URL + "/mobile/qy/workMessageControl/readMessageAll.action";

    /** 业务员向客户赠送电子水票 */
    public static final String sendWaterTicket = BaseConfig.URL + "/mobile/qy/orderControl/sendWaterTicket.action";

    /** 核销水票功能 */
    public static final String verificationTicket = BaseConfig.URL + "/mobile/qy/orderControl/verificationTicket.action";
    /** 配送任务列表中--核销水票功能 */
    public static final String verificationTicket2 = BaseConfig.URL + "/mobile/qy/orderControl/verificationTicket2.action";

    /** 采集企业用户订水信息 */
    public static final String updateMemberBase = BaseConfig.URL + "/mobile/qy/memberControl/updateMemberBase.action";

    /** 业务员添加、修改写字楼 */
    public static final String updateCommunity = BaseConfig.URL + "/mobile/qy/memberControl/updateCommunity.action";
    /** 得到写字楼列表 */
    public static final String getCommunityList = BaseConfig.URL + "/mobile/qy/memberControl/getCommunityList.action";

    /** 获取客户配送申请列表 */
    public static final String queryTicketSendApplyList = BaseConfig.URL + "/mobile/qy/orderControl/queryTicketSendApplyList.action";
    /** 给送水票单指派配送员 */
    public static final String updateTicketSendApply = BaseConfig.URL + "/mobile/qy/orderControl/updateTicketSendApply.action";

    /** 得到需要配送列表 */
    public static final String queryDeliveryProductAndNum = BaseConfig.URL + "/mobile/qy/orderControl/queryDeliveryProductAndNum.action";
    /** 获取会员信息 */
    public static final String getMemberInfo = BaseConfig.URL + "/mobile/qy/memberControl/getMemberInfo.action";

    /** 得到申请详情 */
    public static final String getTicketSendApplyInfo = BaseConfig.URL + "/mobile/qy/orderControl/getTicketSendApplyInfo.action";
    /** 获取字典接口 */
    public static final String getBaseDataDetailList = BaseConfig.URL + "/mobile/qy/memberControl/getBaseDataDetailList.action";

    /** 客户池信息 */
    public static final String inPoolCustomerList = BaseConfig.URL + "/mobile/qy/memberControl/inPoolCustomerList.action";


    /** 获取0元套餐列表 */
    public static final String getPackageList = BaseConfig.URL + "/mobile/qy/salesControl/getPackageList.action";
    /** 获取0元套餐详情 */
    public static final String getPackageDetail = BaseConfig.URL + "/mobile/qy/salesControl/getPackageDetail.action";
    /** 添加套餐申请 */
    public static final String addPackageApply = BaseConfig.URL + "/mobile/qy/salesControl/addPackageApply.action";
    /** 得到申请套餐列表 */
    public static final String getPackageApplyList = BaseConfig.URL + "/mobile/qy/salesControl/getPackageApplyList.action";
    /** 得到申请套餐列表(不区分角色) */
    public static final String getMyPackageApplyList = BaseConfig.URL + "/mobile/qy/salesControl/getMyPackageApplyList.action";
    /** 套餐申请审核 */
    public static final String checkApplyPackage = BaseConfig.URL + "/mobile/qy/salesControl/checkApply.action";

    /** 得到工作人员自己的套餐列表 */
    public static final String getPackageRelationList = BaseConfig.URL + "/mobile/qy/salesControl/getPackageRelationList.action";
    /** 得到工作人员分享自己的套餐 */
    public static final String getSharePromotionVO = BaseConfig.URL + "/mobile/qy/salesControl/getSharePromotionVO.action";
    /** 得到工作人员分享自己使用的套餐详情 */
    public static final String getSalesPackageDetailList = BaseConfig.URL + "/mobile/qy/salesControl/getSalesPackageDetailList.action";
    /** 注册分享 */
    public static final String getShareRegisterVO = BaseConfig.URL + "/mobile/qy/salesControl/getShareRegisterVO.action";


    /** 开发申请 */
    public static final String addApplyMember = BaseConfig.URL + "/mobile/qy/developmentApplyControl/addApply.action";
    /** 所有客户列表 */
    public static final String allCustomerList = BaseConfig.URL + "/mobile/qy/memberControl/allCustomerList.action";
    /** 待审核列表 */
    public static final String getList = BaseConfig.URL + "/mobile/qy/developmentApplyControl/getList.action";
    /** 我的申请列表 */
    public static final String myApplyMemberList = BaseConfig.URL + "/mobile/qy/developmentApplyControl/myList.action";

    /** 主管审核 */
    public static final String leadCheckApply = BaseConfig.URL + "/mobile/qy/developmentApplyControl/checkApply.action";

    /** 客户转移 */
    public static final String transfer = BaseConfig.URL + "/mobile/qy/memberControl/transfer.action";
    /** 我的客户申请记录 */
    public static final String myList = BaseConfig.URL + "/mobile/qy/developmentApplyControl/myList.action";
    /** 消费笔数 */
    public static final String getMemberAmount = BaseConfig.URL + "/mobile/qy/memberControl/getMemberAmount.action";
    /** 待审核数量 */
    public static final String getToCheckNum = BaseConfig.URL + "/mobile/qy/developmentApplyControl/getToCheckNum.action";

    /** 汇总客户消费金额 */
    public static final String getTotalAmount = BaseConfig.URL + "/mobile/qy/memberControl/getTotalAmount.action";

    /** 获取员工个人信息接口 */
    public static final String getMemberWorkInfo = BaseConfig.URL + "/mobile/qy/memberControl/getMemberWorkInfo.action";

    /** 升级会员为金牌合伙人 */
    public static final String upateMemberToAgent = BaseConfig.URL + "/mobile/qy/agentCenterControl/upateMemberToAgent.action";
    /** 金牌合伙人团队列表信息 */
    public static final String queryBaseAgentList = BaseConfig.URL + "/mobile/qy/agentCenterControl/queryBaseAgentList.action";
    /** 金牌合伙人团队详情信息 */
    public static final String findBaseAgentVO = BaseConfig.URL + "/mobile/qy/agentCenterControl/findBaseAgentVO.action";
    /** 修改分配比例 */
    public static final String updateAgentProfitRatio = BaseConfig.URL + "/mobile/qy/agentCenterControl/updateAgentProfitRatio.action";

    /** 得到分销员资金汇总 */
    public static final String queryAgentAmountData = BaseConfig.URL + "/mobile/qy/agentCenterControl/queryAgentAmountData.action";

    /** 客户信息列表 */
    public static final String queryMemberAgentList = BaseConfig.URL + "/mobile/qy/agentCenterControl/queryMemberAgentList.action";

    /** 更改配送时间 */
    public static final String updateDeliverTime = BaseConfig.URL + "/mobile/qy/orderControl/updateDeliverTime.action";

    /** 得到提现列表 */
    public static final String dataGridCashApply = BaseConfig.URL + "/mobile/qy/orderControl/dataGridCashApply.action";
    /** 得到提现详情 */
    public static final String getCashApplyInfo = BaseConfig.URL + "/mobile/qy/orderControl/getCashApplyInfo.action";

    /** 得到发票列表 */
    public static final String dataGridInvoice = BaseConfig.URL + "/mobile/qy/orderControl/dataGridInvoice.action";
    /** 得到发票详情 */
    public static final String getInvoiceLogInfo = BaseConfig.URL + "/mobile/qy/orderControl/getInvoiceLogInfo.action";

    /** 得到分润列表 */
    public static final String queryGlProfitList = BaseConfig.URL + "/mobile/qy/agentCenterControl/queryGlProfitList.action";
    /** 得到战绩 */
    public static final String queryProfitRecommendList = BaseConfig.URL + "/mobile/qy/agentCenterControl/queryProfitRecommendList.action";

}
