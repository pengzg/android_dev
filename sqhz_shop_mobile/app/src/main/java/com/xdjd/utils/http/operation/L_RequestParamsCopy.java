package com.xdjd.utils.http.operation;

import android.content.Context;
import android.util.Log;

import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MD5Util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 网络请求参数类
 * Created by lijipei on 2016/11/24.
 */
public class L_RequestParamsCopy {

    /**
     * 公共参数方法
     *
     * @param params
     * @param reqCode
     */
    public static void setCommonParams(Map params, String reqCode) {
        params.put("reqCode", reqCode);
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");
        params.put("sign", "");
    }

    /**
     * 验证域名
     *
     * @return
     */
    public static Map validateUrl() {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1002");
        params.put("userId", "");
        return params;
    }

    /**
     * 版本更新接口
     *
     * @param uid
     * @param curVersion 当前版本号
     * @return
     */
    public static Map getNewVersion(String uid, String curVersion) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1200");
        params.put("user", uid);
        params.put("curVersion", curVersion);
        LogUtils.e("getNewVersion", params.toString());
        return params;
    }

    /**
     * 添加异常信息
     *
     * @param userId
     * @param bae_msg
     * @return
     */
    public static Map addException(String userId, String bae_msg) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1010");
        params.put("userId", userId);
        params.put("bae_device_name", android.os.Build.MODEL);//	设备名
        params.put("bae_device_type", "3");//类型	3.小店 安卓 4小店ios
        params.put("bae_device_code", "");//设备编码
        params.put("bae_sys_version", android.os.Build.VERSION.RELEASE);//系统版本号
        params.put("bae_page", "");//异常页面
        params.put("bae_msg", bae_msg);//异常内容
        LogUtils.e("addException", params.toString());
        return params;
    }

    /**
     * 首页四个图标接口
     *
     * @param uid
     * @return
     */
    public static Map getHomeIndex(String uid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1501");
        params.put("userId", uid);
        Log.e("4个图标：", params.toString());
        return params;
    }


    /**
     * 获取商品一二级分类
     *
     * @param uid
     * @param source       1 统配进货2预定进货
     * @param storehouseid 发货仓库id
     * @return
     */
    public static Map getGoodsCategory(String uid, String source, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2001");
        params.put("userId", uid);
        params.put("goodsType", source);
        params.put("storehouseid", storehouseid);
        return params;
    }

    /**
     * 采购商品列表
     *
     * @param uid
     * @param categoryType 类型	Y	1:标签2分类 3: 全部
     * @param
     * @param pageNo       页数
     * @param source       来源	Y	1 统配进货2预定进货 3:新品推荐 4.普通
     * @param orderType    1综合1;2销量;3价格升序;4价格降序;5综合2;6综合3
     * @param storehouseid 仓库id
     * @return
     */
    public static Map getGoodsList(String uid, String categoryType, String gcCode, String pageNo, String source, String orderType,
                                             String brandIds, String priceId, String scategoryId, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1201");
        params.put("userId", uid);
        params.put("categoryType", categoryType);
        params.put("categoryCode", gcCode);
        params.put("page", pageNo);
        params.put("goodsType", source);
        params.put("orderType", orderType);
        params.put("storehouseid", storehouseid);

        if (null != brandIds && !brandIds.equals("")) {
            params.put("brandIds", brandIds);//品牌	多个用英文逗号分隔   4,5,6
        }
        if (null != priceId && !priceId.equals("")) {
            params.put("priceId", priceId);//价格
        }
        if (null != scategoryId && !scategoryId.equals("")) {
            params.put("scategoryId", scategoryId);//三级分类 	多个用英文逗号分隔   4,5,6
        }
        LogUtils.e("getGoodsList", params.toString());
        return params;
    }

    /**
     * 查询商品在购物车的数量
     *
     * @param userId
     * @param ggp_id
     * @return
     */
    public static Map queryGoodsCarNum(String userId, String ggp_id, String storehouseid, String pa_id) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "3002");
        params.put("userId", userId);
        params.put("ggp_id", ggp_id);
        params.put("storehouseid", storehouseid);
        if (!"".equals(pa_id)) {
            params.put("pa_id", pa_id);
        }
        LogUtils.e("goodsNUm", params.toString());
        return params;
    }

    /**
     * 筛选参数
     *
     * @param uid
     * @param categoryId
     * @return
     */
    public static Map getSearchList(String uid, String categoryId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1202");
        params.put("userId", uid);
        params.put("categoryType", "2");
        params.put("categoryId", categoryId);
        LogUtils.e("getSearchList", params.toString());
        return params;
    }

    /**
     * * 登录
     *
     * @param context
     * @param userPhone
     * @param userPassword
     * @return
     */
    public static Map getLogin(Context context, String userPhone, String userPassword) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1101");
        params.put("loginName", userPhone);
        params.put("password", MD5Util.toMD5_32(userPassword));
        LogUtils.e("getLogin", params.toString());
        return params;
    }

    /**
     * 新用户注册
     *
     * @param context
     * @param register_params
     * @return
     */
    public static Map getRegister(Context context, Register_Params register_params) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1101");
        params.put("userTel", register_params.getUserTel());
        params.put("userName", register_params.getUserName());
        params.put("marketName", register_params.getShopName());
        params.put("cityId", register_params.getShopArea());
        params.put("marketAddress", register_params.getShopAddress());
        params.put("spareTel", register_params.getSecondPhone());
        params.put("extension", register_params.getExtension());
        params.put("passWord", MD5Util.toMD5_32(register_params.getPassword()));
        params.put("smsCode", register_params.getSmscode());
        params.put("unionid", register_params.getUnionid());
        params.put("userId", register_params.getUserId());
        Log.e("注册参数", params.toString());
        return params;
    }

    /**
     * * 获取短信验证码
     *
     * @param context
     * @param userTel
     * @param smsType
     * @param registerType
     * @return
     */
    public static Map getMsmcode(Context context, String userTel, String smsType, String registerType) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1005");
        params.put("phone", userTel);
        params.put("smsType", smsType);
        params.put("registeType", registerType);
        Log.e("获取短信参数", params.toString());
        return params;
    }

    /**
     * 注册选择地区
     *
     * @return
     */
    public static Map getRegisterArea() {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1102");
        return params;
    }

    /**
     * 获取中心仓地址列表
     *
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    public static Map getCenterShopList(String lat, String lng) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1134");
        params.put("lat", lat);
        params.put("lng", lng);
        return params;
    }

    /**
     * 找回密码手机号判断用户是否存在
     *
     * @param phoneNum
     * @return
     */
    public static Map CheckForgotPhoneNum(String phoneNum) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1118");
        params.put("phone", phoneNum);
        Log.e("tag", params.toString());
        return params;
    }

    /**
     * 注册手机号校验
     *
     * @param mobile
     * @return
     */
    public static Map CheckRegisterMobile(String mobile) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1124");
        params.put("mobile", mobile);
        LogUtils.e("CheckRegisterMobile", params.toString());
        return params;
    }

    /**
     * 设置微信登录密码
     *
     * @param phoneNum
     * @param smsCode
     * @param password
     * @return
     */
    public static Map SetWeixinLoginPwd(String phoneNum, String smsCode, String password) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1117");
        params.put("mobile", phoneNum);
        params.put("passWord", MD5Util.toMD5_32(password));
        params.put("smsCode", smsCode);
        Log.e("weixin", params.toString());
        return params;
    }

    /**
     * 重置密码
     *
     * @param context
     * @param userId
     * @param newPwd
     * @param verificationCode
     * @param phone
     * @return
     */
    public static Map getForgotPwd(Context context, String userId, String newPwd, String verificationCode, String phone) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1003");
        params.put("userId", userId);
        params.put("newPwd", MD5Util.toMD5_32(newPwd));
        params.put("verificationCode", verificationCode);
        params.put("phone", phone);
        Log.e("重置密码参数：", params.toString());
        return params;
    }

    /**
     * 修改用户密码
     *
     * @param uid
     * @param newPwd
     * @param oldPwd
     * @return
     */
    public static Map modifyPassword(String uid, String newPwd, String oldPwd) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1004");
        params.put("userId", uid);
        params.put("newPwd", newPwd/*MD5Util.toMD5_32(newPwd)*/);
        params.put("oldPwd", oldPwd/*MD5Util.toMD5_32(oldPwd)*/);
        Log.e("tag", params.toString());
        return params;
    }

    /**
     * 获取用户个人信息
     *
     * @param
     * @return
     */
    public static Map getUserInfo(String uid, String userId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1102");
        params.put("user", uid);
        params.put("userId", userId);
        Log.e("tag", params.toString());
        return params;
    }

    /**
     * 修改用户信息
     *
     * @param uid
     * @param nickName
     * @param birthDay
     * @param shopName
     * @return
     */
    public static Map ModifyUserInfo(String uid, String nickName, String birthDay, String shopName) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1108");
        params.put("user", uid);
        params.put("nickName", nickName);
        params.put("birthDay", birthDay);
        params.put("shopName", shopName);
        Log.e("tag", params.toString());
        return params;
    }

    /**
     * 修改手机号
     *
     * @param uid
     * @param mobile
     * @param smscode
     * @return
     */
    public static Map ModifyMobile(String uid, String mobile, String smscode) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1113");
        params.put("mobile", mobile);
        params.put("smsCode", smscode);
        params.put("user", uid);
        Log.e("tag", params.toString());
        return params;
    }

    /**
     * 用户地址列表
     *
     * @param uid
     * @return
     */
    public static Map GetUserAddressList(String uid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1109");
        params.put("user", uid);
        Log.e("uid", params.toString());
        return params;
    }

    /**
     * 用户地址添加和修改
     *
     * @param uid
     * @param receiveName
     * @param mobile
     * @param address
     * @param usaId
     * @param isDefault
     * @return
     */
    public static Map UserAddressAddModify(String uid, String receiveName, String mobile,
                                                     String address, String usaId, String isDefault) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1110");
        params.put("user", uid);
        params.put("receiverName", receiveName);
        params.put("mobile", mobile);
        params.put("address", address);
        params.put("usa_id", usaId);
        params.put("is_default", isDefault);
        return params;
    }

    /**
     * 删除用户地址
     *
     * @param uid
     * @param usa_id
     * @return
     */
    public static Map DeleteUserAddress(String uid, String usa_id) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1112");
        params.put("user", uid);
        params.put("usaId", usa_id);
        return params;
    }

    /**
     * 设置默认地址
     *
     * @param uid
     * @param usa_id
     * @return
     */
    public static Map SetDefaultAddress(String uid, String usa_id) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1111");
        params.put("user", uid);
        params.put("usaId", usa_id);
        return params;
    }

    /**
     * 获取订单列表
     *
     * @param uid
     * @param
     * @param pageNo
     * @return
     */
    public static Map GetOrderList(String uid, String pageNo, String orderStatus) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4001");
        params.put("userId", uid);
        params.put("pageNo", pageNo);
        if (!(orderStatus == null || orderStatus.equals(" "))) {
            params.put("orderStatus", orderStatus);
        }
        LogUtils.e("GetOrderList", params.toString());
        return params;
    }

    /**
     * 获取订单详情
     *
     * @param uid
     * @param orderId
     * @return
     */
    public static Map GetOrderDetail(String uid, String orderId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4002");
        params.put("userId", uid);
        params.put("orderId", orderId);
        Log.e("详情", params.toString());
        return params;
    }

    /**
     * 获取订单状态
     *
     * @param uid
     * @param orderId
     * @return
     */
    public static Map GetOrderStatus(String uid, String orderId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4004");
        params.put("user", uid);
        params.put("orderId", orderId);
        return params;
    }

    /**
     * 获取取消订单原因
     *
     * @param userId
     * @return
     */
    public static Map queryCancelReasons(String userId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4001");
        params.put("userId", userId);
        return params;
    }

    /**
     * 取消订单
     *
     * @param uid
     * @param orderId
     * @param reasonId
     * @param reason
     * @param version
     * @return
     */
    public static Map CancelOrder(String uid, String orderId, String reasonId, String reason, String version) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4003");
        params.put("userId", uid);
        params.put("orderId", orderId);
        params.put("cancleId", reasonId);
        params.put("cancelStr", reason);
        params.put("om_version", version);
        Log.e("cancel", params.toString());
        return params;
    }

    /**
     * 再次购买
     *
     * @param uid
     * @param orderId
     * @return
     */
    public static Map BuyAgain(String uid, String orderId, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4005");
        params.put("userId", uid);
        params.put("orderId", orderId);
        params.put("storehouseid", storehouseid);
        Log.e("bugAgain:", params.toString());
        return params;
    }

    /**
     * 第三方微信登录
     *
     * @param unionid
     * @return
     */
    public static Map WeixinLogin(String unionid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1120");
        params.put("unionid", unionid);
        return params;
    }

    /**
     * 信息反馈
     *
     * @param uid
     * @param mobile
     * @param content
     * @return
     */
    public static Map CommitFeedback(String uid, String mobile, String content) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1008");
        params.put("userId", uid);
        params.put("cf_mobile", mobile);
        params.put("cf_content", content);
        return params;
    }

    /**
     * 反馈回复列表
     *
     * @param uid
     * @param pageNo
     * @return
     */
    public static Map MeFeedbackList(String uid, String pageNo, String rows) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1009");
        params.put("userId", uid);
        params.put("page", pageNo);
        params.put("rows", rows);
        Log.e("参数：", params.toString());
        return params;
    }


    /**
     * 登录后绑定微信
     *
     * @param userId
     * @param unionid
     * @return
     */
    public static Map ConnectWX(String userId, String unionid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1123");
        params.put("userId", userId);
        params.put("unionid", unionid);
        Log.e("绑定微信", params.toString());
        return params;
    }


    /**
     * 广告播放页
     *
     * @param ai_platform
     * @param shop
     * @return
     */
    public static Map Advertise(String ai_platform, String shop) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1506");
        params.put("ai_platform", ai_platform);
        params.put("shop", shop);
        Log.e("所属平台", params.toString());
        return params;
    }


    /**
     * 获取推广用户列表
     *
     * @param uid
     * @param searchKey
     * @param pageNo
     * @return
     */
    public static Map PromoteUser(String uid, String searchKey, int pageNo) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1126");
        params.put("user", uid);
        if (!"".equals(searchKey) || searchKey != null) {
            params.put("searchKey", searchKey);
        }
        params.put("pageNo", String.valueOf(pageNo));
        Log.e("用户列表参数", params.toString());
        return params;
    }


    /**
     * 推广统计
     *
     * @param uid
     * @param isAll
     * @param type
     * @return
     */
    public static Map PromoteStatistic(String uid, String isAll, String type) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1127");
        params.put("user", uid);
        params.put("all", isAll);
        params.put("type", type);
        return params;
    }


    /**
     * 推广订单
     *
     * @param uid
     * @param rows
     * @param pageNo
     * @return
     */
    public static Map PromoteOrder(String uid, String rows, int pageNo) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4007");
        params.put("user", uid);
        params.put("rows", rows);
        params.put("pageNo", String.valueOf(pageNo));
        return params;
    }


    /**
     * 推广排名
     *
     * @param uid
     * @param pageNo
     * @return
     */
    public static Map PromoteRank(String uid, int pageNo) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1128");
        params.put("user", uid);
        params.put("pageNo", String.valueOf(pageNo));
        return params;
    }


    /**
     * 获取待处理订单和搜索
     *
     * @param uid
     * @param orderType
     * @param pageNo
     * @param searchKey 关键词
     * @return
     */
    public static Map GetPendingOrderList(String uid, String orderType, String pageNo, String searchKey) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1300");
        params.put("user", uid);
        params.put("orderType", orderType);
        params.put("pageNo", pageNo);
        params.put("searchKey", searchKey);
        Log.e("参数", params.toString());
        return params;
    }

    /**
     * 获取积分明细列表
     *
     * @param userId
     * @param pageNo
     * @param rows
     * @param type
     * @return
     */

    public static Map GetIntegralDetailList(String userId, String pageNo, String rows, String type) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1700");
        params.put("userId", userId);
        params.put("pageNo", pageNo);
        params.put("rows", rows);
        params.put("type", type);
        Log.e("积分明细参数：", params.toString());
        return params;
    }

    /**
     * 获取积分商品列表
     *
     * @param userId
     * @param pageNo
     * @param rows
     * @return
     */
    public static Map GetIntegralGoodsList(String userId, String pageNo, String rows, String searchType
    ) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1701");
        params.put("userId", userId);
        params.put("pageNo", pageNo);
        params.put("rows", rows);
        if (!"".equals(searchType))
            params.put("searchType", searchType);
        Log.e("积分商品参数：", params.toString());
        return params;
    }

    /**
     * 获取积分商品详情
     *
     * @param userId
     * @param wig_id
     * @return
     */
    public static Map GetIntegralGoodsDetail(String userId, String wig_id) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1702");
        params.put("userId", userId);
        params.put("wig_id", wig_id);
        Log.e("积分商品详情参数：", params.toString());
        return params;
    }

    /**
     * 获取会员积分订单
     *
     * @param userId
     * @param pageNo
     * @param rows
     * @return
     */

    public static Map GetIntegralOrder(String userId, String pageNo, String rows) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1705");
        params.put("userId", userId);
        params.put("pageNo", pageNo);
        params.put("rows", rows);
        Log.e("canshu:", params.toString());
        return params;
    }

    /**
     * 获取积分档次
     *
     * @param userId
     * @return
     */
    public static Map GetIngetralGrade(String userId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1706");
        params.put("userId", userId);
        return params;
    }


    /**
     * 修改购物车数量
     *
     * @param uid
     * @param goodsId    商品id
     * @param gpId
     * @param goodsNum   商品数量
     * @param supplierid 店家id
     * @param activityId 活动id
     * @return
     */
    public static Map alterCart(String uid, String goodsId, String gpId, String goodsNum, String supplierid, String activityId, String center_shop) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2004");
        params.put("user", uid);
        params.put("goodsId", goodsId);
        params.put("gpId", gpId);
        params.put("goodsNum", goodsNum);
        params.put("shop", supplierid);
        params.put("center_shop", center_shop);
        if (activityId != null || !activityId.equals("")) {
            params.put("activityId", activityId);
        }
        LogUtils.e("alterCart-->", params.toString());
        return params;
    }

    /**
     * 修改购物车
     *
     * @param userId
     * @param ggp_id
     * @param c_goods_num
     * @param storehouseid
     * @return
     */
    public static Map editCart(String userId, String ggp_id, String c_goods_num, String storehouseid, String pa_id) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "3003");
        params.put("userId", userId);
        params.put("ggp_id", ggp_id);
        params.put("c_goods_num", c_goods_num);
        params.put("storehouseid", storehouseid);
        if (!pa_id.equals("")) {
            params.put("pa_id", pa_id);
        }
        Log.e("pa", params.toString());
        return params;
    }

    /**
     * 购物车移入收藏夹接口
     *
     * @param userId
     * @param favoriteType 收藏类型 1.商品
     * @param favoriteids  商品对应的 gpId 多个用英文逗号分
     * @param isFavorite   1收藏 2取消收藏
     * @return
     */
    public static Map addFavoriteBatch(String userId, String favoriteType, String favoriteids, String isFavorite/*,String cartIds,String center_shop*/) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2004");
        params.put("userId", userId);
        params.put("favoriteType", favoriteType);
        params.put("favoriteids", favoriteids);
        params.put("isFavorite", isFavorite);
        //        params.put("cartIds",cartIds);
        //        params.put("center_shop",center_shop);
        LogUtils.e("addFavoriteBatch", params.toString());
        return params;
    }

    /**
     * 商品详情
     *
     * @param uid
     * @param gp_id
     * @param storehouseid //仓库id
     * @return
     */
    public static Map getGoodsDetail(String uid, String pricePlanId, String gp_id, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2003");
        params.put("userId", uid);
        params.put("pricePlanId", pricePlanId);
        params.put("priceId", gp_id);
        params.put("storehouseid", storehouseid);
        Log.e("商品详情参数", params.toString());
        return params;
    }

    /**
     * 请求购物车列表
     *
     * @param uid
     * @return
     */
    public static Map getCartList(String uid, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2003");
        params.put("userId", uid);
        params.put("storehouseid", storehouseid);
        LogUtils.e("购物车列表", params.toString());
        return params;
    }

    /**
     * 清空购物车
     *
     * @param uid
     * @return
     */
    public static Map getCleartCart(String uid, String center_shop, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2006");
        params.put("userId", uid);
        params.put("center_shop", center_shop);
        params.put("storehouseid", storehouseid);
        return params;
    }

    /**
     * 删除购物车
     *
     * @param uid
     * @param cartIds
     * @return
     */
    public static Map getDeleteCart(String uid, String cartIds, String center_shop, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2009");
        params.put("user", uid);
        params.put("cartIds", cartIds);
        params.put("center_shop", center_shop);
        params.put("storehouseid", storehouseid);
        LogUtils.e("getDeleteCart", params.toString());
        return params;
    }

    /**
     * 购物车选商品
     *
     * @param uid
     * @param cartIds 购物车id	用英文逗号分隔
     * @return
     */
    public static Map getSingleAmount(String uid, String cartIds, String center_shop) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2007");
        params.put("user", uid);
        params.put("cartIds", cartIds);
        params.put("center_shop", center_shop);
        return params;
    }

    /**
     * 获取购物车总数量总金额
     *
     * @param uid
     * @return
     */
    public static Map getCartInfo(String uid, String center_shop, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "3005");
        params.put("userId", uid);
        params.put("center_shop", center_shop);
        params.put("storehouseid", storehouseid);
        return params;
    }

    /**
     * 扫码加入购物车
     *
     * @param userId
     * @param shop          中心仓id
     * @param goodsInteCode
     * @return
     */
    public static Map addCartByScan(String userId, String shop, String goodsInteCode) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2011");
        params.put("user", userId);
        params.put("userId", userId);
        params.put("shop", shop);
        params.put("goodsInteCode", goodsInteCode);
        Log.e("addCartByScan", params.toString());
        return params;
    }


    /**
     * 获得结算单数据参数
     *
     * @param userId
     * @param cartIds     购物车id用,号进行分割
     * @param payMethod   支付方式
     * @param couponId    优惠券
     * @param giftgoodsid 加价购商品id	Y	没有传入0
     * @return
     */
    public static Map bulidSettlementOrder(String userId, String cartIds,
                                                     String payMethod, String couponId, String addressId, String giftgoodsid, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2008");
        params.put("userId", userId);
        params.put("cartIds", cartIds);
        params.put("storehouseid", storehouseid);
        if (null != payMethod && !payMethod.equals("")) {
            params.put("payMethod", payMethod);
        }
        /*if (giftgoodsid==null||giftgoodsid.equals("")){
            params.put("giftgoodsid","0");
        }else{
            params.put("giftgoodsid",giftgoodsid);
        }

        if (!addressId.equals("")&&!addressId.equals("0")){
            params.put("addressId",addressId);
        }
        if (!couponId.equals("")&&null!=couponId){
            params.put("couponId",couponId);
        }*/

        LogUtils.e("bulidSettlementOrder", params.toString());
        return params;
    }


    /**
     * 提交订单参数
     *
     * @param userId
     * @param cartIds
     * @param payMethod
     * @param addressId
     * @param note
     * @param couponId
     * @return
     */
    public static Map createOrder(String userId, String cartIds, String payMethod,
                                            String addressId, String note, String couponId,
                                            String giftgoodsid, String center_shop, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1400");
        params.put("userId", userId);
        params.put("cartIds", cartIds);
        params.put("payMethod", payMethod);
        params.put("addressId", addressId);
        params.put("center_shop", center_shop);
        params.put("storehouseid", storehouseid);
        if (null != note && !note.equals("")) {
            params.put("note", note);
        }
        if (null != couponId && !couponId.equals("")) {
            params.put("couponId", couponId);
        }
        if (giftgoodsid == null || giftgoodsid.equals("")) {
            params.put("giftgoodsid", "0");
        } else {
            params.put("giftgoodsid", giftgoodsid);
        }

        LogUtils.e("createOrder", params.toString());
        return params;
    }

    /**
     * 收藏参数
     *
     * @param user
     * @param favoriteType :1商品
     * @param favoriteid   商品对应的 gpId
     * @param isFavorite   1收藏 2取消收藏
     * @return
     */
    public static Map addFavorite(String user, String favoriteType, String favoriteid, String isFavorite) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1115");
        params.put("user", user);
        params.put("favoriteType", favoriteType);
        params.put("favoriteid", favoriteid);
        params.put("isFavorite", isFavorite);
        return params;
    }

    /**
     * 首页推荐列表
     *
     * @param user
     * @return
     */
    public static Map getHomeList(String user) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1501");
        params.put("userId", user);
        Log.e("推荐列表：", params.toString());
        return params;
    }

    /**
     * 首页底部新品推荐列表
     *
     * @param userId
     * @param page
     * @return
     */
    public static Map getRecommendGoods(String userId, String page, String stroehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1505");
        params.put("userId", userId);
        params.put("page", page);
        params.put("stroehouseid", stroehouseid);
        Log.e("recommend:", params.toString());
        return params;
    }

    /**
     * 首页轮播图(顶部广告)
     *
     * @param user
     * @return
     */
    public static Map getFocus(String user) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4001");
        params.put("userId", user);
        Log.e("顶部轮播图：", params.toString());
        return params;
    }

    /**
     * 首页活动
     *
     * @param user
     * @param localtion 位置 1 首页顶部；2 首页中部 3首页底部 4 列表
     * @return
     */
    public static Map getHomeActivity(String user, String localtion) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1504");
        params.put("userId", user);
        if (!localtion.equals("")) {
            params.put("localtion", localtion);
        }
        return params;
    }

    /**
     * 首页商品推荐列表
     *
     * @param user
     * @param whp_id
     * @return
     */
    public static Map getHomegoodsList(String user, String whp_id, String pageNo, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1503");
        params.put("userId", user);
        params.put("whp_id", whp_id);
        params.put("page", pageNo);
        params.put("storehouseid", storehouseid);
        return params;
    }

    /**
     * 获取活动商品列表
     *
     * @param user
     * @param activityId
     * @return
     */
    public static Map getActivityGoods(String user, String activityId, String pageNo, String stroehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1505");
        params.put("userId", user);
        params.put("pa_id", activityId);
        params.put("page", pageNo);
        params.put("storehouseid", stroehouseid);
        LogUtils.e("getActivityGoods", params.toString());
        return params;
    }

    /**
     * 临期商品参数
     *
     * @param user
     * @param pageNo
     * @return
     */
    public static Map getExpireGoodsList(String user, String pageNo) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1206");
        params.put("user", user);
        params.put("pageNo", pageNo);
        LogUtils.e("getExpireGoodsList", params.toString());
        return params;
    }

    /**
     * 结算单界面查看购买的商品列表界面
     *
     * @param userId
     * @param cartIds
     * @param type    //@param pageNo
     * @return
     */
    public static Map getBulidCartList(String userId, String cartIds, String type, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2010");
        params.put("userId", userId);
        params.put("cartIds", cartIds);
        params.put("type", type);
        params.put("storehouseid", storehouseid);
        //params.put("pageNo",pageNo);
        LogUtils.e("getBulidCartList", params.toString());
        return params;
    }

    /**
     * 收藏商品列表
     *
     * @param user
     * @param pageNo
     * @return
     */
    public static Map getFavoriteGoods(String user, String pageNo, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1506");
        params.put("userId", user);
        params.put("page", pageNo);
        params.put("storehouseid", storehouseid);
        return params;
    }

    /**
     * 我常买接口
     *
     * @param user
     * @param pageNo
     * @return
     */
    public static Map getUserBuyGoods(String user, String pageNo, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2006");
        params.put("userId", user);
        params.put("page", pageNo);
        params.put("storehouseid", storehouseid);
        LogUtils.e("getUserBuyGoods", params.toString());
        return params;
    }

    /**
     * 搜索商品列表
     *
     * @param user
     * @param pageNo
     * @param searchKey
     * @return
     */
    public static Map searchGoodsList(String user, String pageNo, String searchKey, String storehouseid) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "2004");
        params.put("userId", user);
        params.put("page", pageNo);
        params.put("searchKey", searchKey);
        params.put("storehouseid", storehouseid);
        Log.e("search", params.toString());
        return params;
    }

    /**
     * 加价购商品列表
     *
     * @param user
     * @return
     */
    public static Map getPromotionGoods(String user) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "4005");
        params.put("user", user);
        //params.put("shop",shop);
        return params;
    }

    /**
     * 分享参数请求
     *
     * @param user
     * @return
     */
    public static Map getShareParam(String user, String bs_code) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "0122");
        params.put("user", user);
        params.put("bs_code", bs_code);
        return params;
    }

    /**
     * 快速注册请求参数
     *
     * @param mobile   手机号
     * @param smsCode  验证码
     * @param password 密码
     * @param spread   邀请码
     * @return
     */
    public static Map getQuickReg(String mobile, String smsCode, String password, String spread) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1131");
        params.put("mobile", mobile);
        params.put("smsCode", smsCode);
        params.put("password", MD5Util.toMD5_32(password));
        params.put("spread", spread);
        return params;
    }

    /**
     * 快速注册验证手机号和邀请码
     *
     * @param mobile
     * @param spread
     * @return
     */
    public static Map checkMobileAndSpread(String mobile, String spread) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1132");
        params.put("mobile", mobile);
        params.put("spread", spread);
        return params;
    }

    /**
     * 消息类别
     *
     * @param user
     * @param shop 中心仓id
     * @return
     */
    public static Map querySummeryMessage(String user, String shop) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1600");
        params.put("user", user);
        params.put("shop", shop);
        return params;
    }

    /**
     * 消息列表 1 系统消息 2 活动促销 3 物流消息
     *
     * @param user
     * @param message_type
     * @param shop         中心仓id
     * @return
     */
    public static Map queryMessageList(String user, String page, String message_type, String shop) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1601");
        params.put("user", user);
        params.put("page", page);
        params.put("message_type", message_type);
        params.put("shop", shop);
        return params;
    }

    /**
     * 生成积分商品结算单详情
     *
     * @param userId
     * @param wig_id
     * @param goodsNum
     * @param addressId
     * @return
     */
    public static Map createIntegralOrderDetail(String userId, String wig_id, String goodsNum, String addressId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1703");
        params.put("userId", userId);

        params.put("wig_id", wig_id);
        params.put("goodsNum", goodsNum);
        if ("".equals(addressId) || addressId == null) {
            params.put("addressId", "0");
        } else {
            params.put("addressId", addressId);
        }
        LogUtils.e("createIntegralOrderDetail", params.toString());
        return params;
    }

    /**
     * 生成积分订单
     *
     * @param userId
     * @param wig_id
     * @param goodsNum
     * @param addressId
     * @return
     */
    public static Map createIntegralOrder(String userId, String wig_id, String goodsNum, String addressId) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        params.put("wig_id", wig_id);
        params.put("goodsNum", goodsNum);
        if ("".equals(addressId) || addressId == null) {
            params.put("addressId", "0");
        } else {
            params.put("addressId", addressId);
        }
        LogUtils.e("createIntegralOrder", params.toString());
        return params;
    }

    /**
     * 扫码核销获取奖品和用户信息
     *
     * @param mw_prizecode 中奖编码
     * @return
     */
    public static Map getGiftPrizeDetailByCode(String userId, String mw_prizecode) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        params.put("mw_prizecode", mw_prizecode);
        LogUtils.e("getGiftPrizeDetailByCode", params.toString());
        return params;
    }

    /**
     * 扫码核销获取奖品和用户信息
     * @param mw_id 中奖id
     * @return
     */
    public static Map checkGiftWinning(String userId, String mw_id) {
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        params.put("mw_id", mw_id);
        LogUtils.e("checkGiftWinning", params.toString());
        return params;
    }

    /**
     * 获取客户兑奖列表
     * @param userId
     * @param start_time
     * @param end_time
     * @param page
     * @return
     */
    public static Map getWinningList(String userId,String start_time,String end_time,String page
    ,String mw_prizeid,String mw_memberid){
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        params.put("end_time", end_time);
        params.put("start_time", start_time);
        params.put("page", page);
        params.put("mw_prizeid",mw_prizeid);
        params.put("mw_memberid",mw_memberid);
        LogUtils.e("getCustomerWinningList", params.toString());
        return params;
    }

    /**
     * 获取已领取的奖品列表
     * @param userId
     * @param start_time
     * @param end_time
     * @param page
     * @return
     */
    public static Map getGiftWinningList(String userId,String start_time,String end_time,String page){
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        params.put("end_time", end_time);
        params.put("start_time", start_time);
        params.put("page", page);
        LogUtils.e("getGiftWinningList", params.toString());
        return params;
    }

    /**
     * 获取核销密码
     * @param userId
     * @return
     */
    public static Map queryHxPwd(String userId){
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        LogUtils.e("queryHxPwd", params.toString());
        return params;
    }

    /**
     * 修改核销密码
     * @param userId
     * @param hxPassword
     * @return
     */
    public static Map setUpHxPwd(String userId,String hxPassword){
        Map params = new LinkedHashMap();
        setCommonParams(params, "1704");
        params.put("userId", userId);
        params.put("hxPassword", hxPassword);
        LogUtils.e("setUpHxPwd", params.toString());
        return params;
    }

}
