package com.xdjd.utils.http.url;

import com.xdjd.storebox.base.BaseConfig;

/**
 * Created by lijipei on 2016/11/24.
 */

public class M_Url {

    /** 验证域名地址 */
    public static final String validateUrl =  "/terminal/base/terminalSystemControl/validateUrl.action";

    /** 添加异常信息接口 */
    public static final String addException = BaseConfig.URL + "/shop/base/baseSystemControl/addException.action";

    /**
     * 版本更新接口
     */
    public static final String getNewVersion = BaseConfig.URL + "/shop/base/baseSystemControl/getNewVersion.action";

    /**
     * 首页四个按钮
     */
    public static final String getHomeIndex =  BaseConfig.URL + "/shop/home/homeController/getHomeIndex_login.action";

    /** 采购一、二级分类数据 */
    public static final String GoodsCategory = BaseConfig.URL + "/shop/goods/goods2CControl/getGoodsCategory_login.action";
    /** 商品列表 */
    public static final String GoodsList = BaseConfig.URL + "/shop/goods/goods2CControl/getGoodsList_login.action";
    /** 搜索商品列表 */
    public static final String searchGoodsList = BaseConfig.URL + "/shop/goods/goods2CControl/getGoodsListByKey_login.action";
    /*查询某个商品在购物车中数量*/
    public  static  final String queryGoodsNums = BaseConfig.URL + "/shop/cart/cart2CControl/queryCartByGoods_login.action";
    /*修改购物车*/
    public  static  final String editCartGoodNum = BaseConfig.URL + "/shop/cart/cart2CControl/editCart_login.action";
/*8**************************************/


    /** 商品筛选分类数据 */
    public static final String SearchList = BaseConfig.URL + "/shop/goods/goods2CControl/getSearchList_login.action";
    /** 加入购物车接口 */
    public static final String addCart = BaseConfig.URL + "/b2b/app/b2BAppCartControl/addCart_login.action";
    /** 获取购物车总数量和总价格接口 */
    public static final String cartInfo = BaseConfig.URL + "/shop/cart/cart2CControl/getCartAmount_login.action";
    /** 购物车列表 */
    public static final String cartList = BaseConfig.URL + "/shop/cart/cart2CControl/queryCartList_login.action";
    /** 清空购物车 */
    public static final String clearCart =  BaseConfig.URL + "/shop/cart/cart2CControl/clearCart_login.action";
    /** 删除购物车接口 */
    public static final String deleteCart = BaseConfig.URL + "/shop/cart/cart2CControl/removeCart_login.action";
    /** 购物车选商品 */
    public static final String getSingleAmount =  BaseConfig.URL + "/b2b/app/b2BAppCartControl/getSingleAmount_login.action";
    /** 生成结算单界面 */
    public static final String bulidSettlementOrder = BaseConfig.URL + "/shop/cart/cart2CControl/bulidSettlementOrder_login.action";
    /** 收藏商品接口 */
//    public static final String addFavorite = BaseConfig.URL + "/b2b/app/userMobileControl/addFavorite_login.action";
    /** 我的收藏商品列表 */
    public static final String getFavoriteGoods = BaseConfig.URL + "/shop/goods/goods2CControl/getFavoriteGoodsList_login.action";
    /** 我常买列表 */
    public static final String getUserBuyGoods = BaseConfig.URL + "/shop/goods/goods2CControl/getBuyGoodsList_login.action";
    /** 结算单界面商品详情列表 */
    public static final String getBulidCartList = BaseConfig.URL + "/shop/cart/cart2CControl/getBulidCartList_login.action";
    /** 加价购商品列表 */
    public static final String getPromotionGoods = BaseConfig.URL + "/b2b/app/b2BAppGoodsControl/getPromotionGoods_login.action";
    /** 购物车批量移入收藏 */
    public static final String addFavoriteBatch = BaseConfig.URL + "/shop/base/baseSystemControl/addFavorite_login.action";
    /** 扫码加入购物车 */
    public static final String addCartByScan = BaseConfig.URL + "/b2b/app/b2BAppCartControl/addCartByScan_login.action";

    /** 首页推荐列表 */
    public static final String getHomeList = BaseConfig.URL + "/shop/home/homeController/queryHomePageList_login.action";
    /** 首页广告轮播图 */
    public static final String getFocus = BaseConfig.URL + "/shop/home/homeController/queryFocusList_login.action";
    /** 首页活动 */
    public static final String getHomeActivity = BaseConfig.URL + "/shop/activity/shopActivityController/getActivityList_login.action";
    /** 获取活动商品列表 */
    public static final String getActivityGoods = BaseConfig.URL + "/shop/activity/shopActivityController/getActivityGoodsListDetail_login.action";
    /** 首页推荐商品列表  中部分区 */
    public static final String getHomegoodsList = BaseConfig.URL + "/shop/home/homeController/getHomePageGoodsList_login.action";
    /*首页最下面推荐商品*/
    public static final String getHomeBottomRecommendGoodList = BaseConfig.URL + "/shop/home/homeController/getRecommendGoodsList_login.action";
    /** 临期商品接口 */
    public static final String getExpireGoodsList = BaseConfig.URL + "/b2b/app/b2BAppGoodsControl/getExpireGoodsList_login.action";

    /** 提交订单 */
    public static final String createOrder = BaseConfig.URL + "/shop/order/order2CController/createOrder_login.action";

    /** 商品详情页 */
    public static final String getGoodsDetail = BaseConfig.URL + "/shop/goods/goods2CControl/getGoodsDetail_login.action";
    /** 登录接口*/
    public static final String login = BaseConfig.URL + "/shop/base/baseSystemControl/login.action";
    /**注册接口*/
    public static final String Register = BaseConfig.URL + "/b2b/app/userMobileControl/registe.action";
    /**获取中心仓地址列表接口*/
    public static final String getCenterShopList = BaseConfig.URL + "/b2b/app/userMobileControl/getCenterShopList.action";
    /** 请求快速注册接口 */
    public static final String quickReg = BaseConfig.URL + "/b2b/app/userMobileControl/quickReg.action";
    /** 快速注册验证手机号和推广码 */
    public static final String checkMobileAndSpread = BaseConfig.URL + "/b2b/app/userMobileControl/checkMobileAndSpread.action";
    /*获取验证码*/
    public static final String Getmsmcode = BaseConfig.URL + "/shop/base/baseSystemControl/sendVerCode.action";
    /*找回密码时判断用户手机号是否存在*/
    public static final String CheckPhoneNum = BaseConfig.URL + "/shop/base/baseSystemControl/isRegiste.action";
    /*注册用户时手机号校验*/
    public static final String CheckRegisterMobile = BaseConfig.URL + "/b2b/app/userMobileControl/checkMobile.action";
    /*重置密码*/
    public static final String ForgotPwd = BaseConfig.URL + "/shop/base/baseSystemControl/resetPassword.action";
    /*选择地区接口*/
    public static final String ShopArea = BaseConfig.URL + "/b2b/app/baseB2BControl/getAreaShop.action";
    /*修改用户密码接口*/
    public static final String ModifyPwd = BaseConfig.URL + "/shop/base/baseSystemControl/updatePassword_login.action";
    /*获取用户个人信息接口*/
    public static final String GetuserInfo = BaseConfig.URL + "/shop/base/baseSystemControl/getUserInfo_login.action";
    /*修改用户信息*/
    public static final String ModifyuserInfo = BaseConfig.URL + "/b2b/app/userMobileControl/updateUserInfo_login.action";
    /*用户地址列表*/
    public static final String UserAddrssList = BaseConfig.URL + "/b2b/app/userMobileControl/getAddressList_login.action";
    /*修改用户手机号*/
    public static final String ModifyMobile = BaseConfig.URL + "/b2b/app/userMobileControl/resetMobile_login.action";
    /*新增和修改地址*/
    public static final String AddAndModifyAddress = BaseConfig.URL + "/b2b/app/userMobileControl/addAddress_login.action";
    /*删除用户地址*/
    public static final String DeleteUserReceiveAddress = BaseConfig.URL  + "/b2b/app/userMobileControl/deleteAddress_login.action";
    /*设置默认地址*/
    public static final String SetDefaultAddress = BaseConfig.URL + "/b2b/app/userMobileControl/setDefault_login.action";
    /*设置微信登录密码*/
    public static final String SetWinxinLoginPassword = BaseConfig.URL + "/b2b/app/userMobileControl/setWxMobilePass.action";
    /*获取订单列表*/
    public static final String GetOrderList = BaseConfig.URL + "/shop/order/order2CController/getOrderList_login.action";
    /*获取订单详情*/
    public static final String GetOrderDetail = BaseConfig.URL + "/shop/order/order2CController/getOrderDetail_login.action";
    /*获取订单状态*/
    public static final String GetOrderStatus = BaseConfig.URL + "/b2b/app/b2BAppOrderControl/getOrderLog_login.action";
    /*获取取消订单原因*/
    public static final String queryCancelReasons = BaseConfig.URL + "/shop/order/order2CController/getCancelList_login.action";
    /*取消订单*/
    public static final String CancelOrder = BaseConfig.URL + "/shop/order/order2CController/cancelOrder_login.action";
    /*再次购买*/
    public static final String BuyAgain = BaseConfig.URL + "/shop/order/order2CController/buyAgain_login.action";
    /*微信第三方登录接口*/
    public static final String WeixinBinding = BaseConfig.URL + "/shop/base/baseSystemControl/txLogin.action";
    /*信息反馈*/
    public static final String Feedback = BaseConfig.URL + "/shop/base/baseSystemControl/addFeedBack_login.action";
    /*绑定微信*/
    public static final String ConnectWX = BaseConfig.URL + "/shop/base/baseSystemControl/bindWx_login.action";
    /*解绑微信*/
    public static final String clearWxData =  BaseConfig.URL + "/shop/base/baseSystemControl/clearWxData_login.action";
    /*上传个人图像*/
    public static final String PersonImage = BaseConfig.URL + "/b2b/app/userMobileControl/uploadImage_login.action";
    /*开屏广告图*/
    public static final String Advertise = BaseConfig.URL + "/b2b/app/indexControl/getAdIndex.action";
    /*获取推广用户列表*/
    public static final String PromoteUserList = BaseConfig.URL + "/b2b/app/userStatisticsController/getSpreadList_login.action";
    /*推广统计信息*/
    public static final String PromoteStatistic = BaseConfig.URL + "/b2b/app/userStatisticsController/getSpreadInfo_login.action";
    /*推广订单列表*/
    public static final String PromoteOrderList = BaseConfig.URL + "/b2b/app/b2BAppOrderControl/getSpreadOrderList_login.action";
    /*推广排名列表*/
    public static final String PromoteRankList = BaseConfig.URL + "/b2b/app/userStatisticsController/getSpreadStatistics_login.action";
    /*待处理订单列表*/
    public static final String PendingOrderList = BaseConfig.URL + "/b2b/app/userTaskControl/getUserTaskOrderList_login.action";
    /*我的反馈列表*/
    public static final String MeFeedbackList = BaseConfig.URL + "/shop/base/baseSystemControl/getFeedbackList_login.action";
    /** 分享参数请求 */
    public static final String shareController = BaseConfig.URL + "/app/mobileBaseShareController/commonShare.action?";

    /** 消息类别列表 */
    public static final String querySummeryMessage = BaseConfig.URL + "/b2b/app/b2BAppMessageController/querySummeryMessage_login.action";
    /** 消息列表 */
    public static final String queryMessageList = BaseConfig.URL + "/b2b/app/b2BAppMessageController/queryMessageList_login.action";

    /*获取积分明细*/
    public static final String getIntegralDetail = BaseConfig.URL + "/b2b/app/b2BMemberControl/getMemberAccountDetailList_login.action";
    /*获取积分商品列表*/
    public static final String getIntegralGoodsList = BaseConfig.URL + "/b2b/app/b2BMemberControl/getIntegrateGoodsList_login.action";
    /*获取积分商品详情*/
    public static final String getIntegralGoodsDetail = BaseConfig.URL + "/b2b/app/b2BMemberControl/getIntegrateGoodsDetail_login.action";
    /*获取会员积分订单*/
    public static final String getIntegralOrder = BaseConfig.URL + "/b2b/app/b2BIntegrateOrderControl/getMemberIntegrateOrderList_login.action";
    /*获取积分商品积分档次*/
    public static final String getIntegralGrage = BaseConfig.URL + "/b2b/app/b2BMemberControl/getIntegerPriceList_login.action";

    /** 签到H5链接 */
    public static final String signIn = BaseConfig.URL +
            "/html/b2b/app/signboard/index.html?";//user=237&device=1&deviceType=1

    /** 生成积分商品结算单 */
    public static final String createIntegralOrderDetail = BaseConfig.URL +
            "/b2b/app/b2BMemberControl/bulidInSettlementOrder_login.action";
    /** 生成积分订单 */
    public static final String createIntegralOrder = BaseConfig.URL +
            "/b2b/app/b2BIntegrateOrderControl/createOrder_login.action";
    /** 获取会员积分订单 */
    public static final String getMemberIntegrateOrderList = BaseConfig.URL +
            "/b2b/app/b2BIntegrateOrderControl/getMemberIntegrateOrderList_login.action";

    /** 铺货订单列表 */
    public static final String queryPhOrderList = BaseConfig.URL +"/shop/order/phOrderController/queryPhOrderList.action";
    /** 铺货订单详情列表 */
    public static final String queryPhOrderDetailList = BaseConfig.URL +"/shop/order/phOrderController/queryPhOrderDetailList.action";
    /** 根据铺货单查询销售单/撤货单 */
    public static final String saleOrderByPhOrder = BaseConfig.URL +"/shop/order/phOrderController/saleOrderByPhOrder.action";


    /** 扫码获取奖品码 */
    public static final String getGiftPrizeDetailByCode = BaseConfig.URL + "/shop/dbh/dbhActivityController/getGiftPrizeDetailByCode_login.action";
    /** 奖品核销提交 */
    public static final String checkGiftWinning = BaseConfig.URL + "/shop/dbh/dbhActivityController/checkGiftWinning_login.action";
    /**核销奖品列表 */
    public static final String getGiftWinningList = BaseConfig.URL + "/shop/dbh/dbhActivityController/getGiftWinningList_login.action";
    /** 已兑奖列表 */
    public static final String getWinningList = BaseConfig.URL + "/shop/dbh/dbhActivityController/getWinningList_login.action";

    /** 获取核销密码 */
    public static final String queryHxPwd = BaseConfig.URL + "/shop/dbh/dbhActivityController/queryHxPwd_login.action";
    /** 修改核销密码 */
    public static final String setUpHxPwd = BaseConfig.URL + "/shop/dbh/dbhActivityController/setUpHxPwd_login.action";

}
