package com.bikejoy.utils.http.operation;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.MD5Util;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.testdemo.bean.request.OrderDeliveryReq;

/**
 * 网络请求参数类
 * Created by lijipei on 2016/11/24.
 */
public class L_RequestParams {

    private final static String ROWS = "20";
    private static UserBean userBean;

    /**
     * 公共参数方法
     *
     * @param params
     * @param reqCode
     */
    public static void setCommonParams(RequestParams params, String reqCode) {

        if (userBean==null){
            userBean = UserInfoUtils.getUser(UIUtils.getContext());
        }

        params.put("reqCode", reqCode);
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");
        params.put("userId", UserInfoUtils.getUser(UIUtils.getContext()).getUserid());//会员人员id
        params.put("work_id", UserInfoUtils.getId(UIUtils.getContext()));//工作人员id
        params.put("orgid", UserInfoUtils.getOrgid(UIUtils.getContext()));//公司id
        params.put("sign", UserInfoUtils.getSign(UIUtils.getContext()));
        params.put("token", UserInfoUtils.getUser(UIUtils.getContext()).getToken());
        params.put("mb_id", UserInfoUtils.getUser(UIUtils.getContext()).getUserid());//会员id
        params.put("shopid", UserInfoUtils.getUser(UIUtils.getContext()).getShopId());//店铺id
        params.put("mbw_storeid", UserInfoUtils.getUser(UIUtils.getContext()).getCarid());//车仓库id
        params.put("userType",UserInfoUtils.getUser(UIUtils.getContext()).getUsertype());
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

    /**
     * 添加异常信息
     *
     * @param userId
     * @param bae_msg
     * @return
     */
    public static RequestParams addException(String userId, String bae_msg) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", userId);
        params.put("bae_device_name", android.os.Build.MODEL);//	设备名
        params.put("bae_device_type", "1");//类型	Y	1安卓  2苹果
        params.put("bae_device_code", "");//设备编码
        params.put("bae_sys_version", android.os.Build.VERSION.RELEASE);//系统版本号
        params.put("bae_page", "");//异常页面
        params.put("bae_msg", bae_msg);//异常内容
        //        LogUtils.e("addException",params.toString());
        return params;
    }

    /**
     * 版本更新接口
     *
     * @return
     */
    public static RequestParams getNewVersion(String curVersion) {
        RequestParams params = new RequestParams();
        userBean = UserInfoUtils.getUser(UIUtils.getContext());
        if (userBean!=null && userBean.getUserid()!=null){
            setCommonParams(params, "1200");
        }
        params.put("curVersion", curVersion);
        params.put("apptype", "android");
        params.put("source", "8"); //8.--2B订水业务员端
        LogUtils.e("getNewVersion", params.toString());
        return params;
    }

    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @return
     */
    public static RequestParams getLogin(String loginName, String password) {
        RequestParams params = new RequestParams();
        //        setCommonParams(params, "1103");
        params.put("logincode", loginName);
        params.put("pwd", MD5Util.md5(password));
        LogUtils.e("getLogin", params.toString());
        return params;
    }

    /**
     * 第三方微信登录
     *
     * @param unionid
     * @return
     */
    public static RequestParams thirdLogin(String unionid) {
        RequestParams params = new RequestParams();
        //                setCommonParams(params, "1103");
        params.put("unionid", unionid);
        LogUtils.e("thirdLogin", params.toString());
        return params;
    }

    public static RequestParams dataGridStorehouseOrder(String store_code,String page,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("store_code",store_code);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("order_by_time","desc");//降序
        LogUtils.e("dataGridStorehouseOrder", params.toString());
        return params;
    }

    /**
     * 请求订单列表
     *
     * @param om_logistics_deliveryid 配送员id
     * @param memberid                会员id
     * @param om_order_type           订单类型 1 水票订单  2 送水订单 3 购物订单 4 充值订单
     * @param om_pay_type             支付类型1 微信 2 支付宝 3混合 4 现金 5 水票购买
     * @param om_state                //订单状态: ''.全部 2.待付款 6.待收货 8.已完成 1.已取消
     * @param page
     * @return
     */
    public static RequestParams queryOrderList(String om_logistics_deliveryid, String memberid,
                                               String om_order_type, String om_pay_type, String om_state, String page, String od_source_id, String om_pay_way,
                                               String startDate, String endDate, String searchKey,String om_pay_state,String deliverable) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        if (!TextUtils.isEmpty(om_logistics_deliveryid)) {
            params.put("om_logistics_deliveryid", om_logistics_deliveryid);
        }
        params.put("memberid", memberid);
        params.put("om_order_type_arr", om_order_type);
        params.put("om_pay_type", om_pay_type);
        params.put("om_state", om_state);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("od_source_id", od_source_id);
        params.put("om_pay_way", om_pay_way);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("searchKey", searchKey);
//        params.put("om_pay_state",om_pay_state);
        params.put("deliverable",deliverable);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }

    public static RequestParams queryOrderListQueryTotal(String om_logistics_deliveryid, String memberid,
                                               String om_order_type, String om_pay_type, String om_state, String page, String od_source_id, String om_pay_way,
                                               String startDate, String endDate, String searchKey,String om_pay_state,String deliverable,String rows) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        if (!TextUtils.isEmpty(om_logistics_deliveryid)) {
            params.put("om_logistics_deliveryid", om_logistics_deliveryid);
        }
        params.put("memberid", memberid);
        params.put("om_order_type_arr", om_order_type);
        params.put("om_pay_type", om_pay_type);
        params.put("om_state", om_state);
        params.put("page", page);
        params.put("rows", rows);
        params.put("od_source_id", od_source_id);
        params.put("om_pay_way", om_pay_way);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("searchKey", searchKey);
        //        params.put("om_pay_state",om_pay_state);
        params.put("deliverable",deliverable);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }

    /**
     * 获取会员水票列表
     *
     * @param memberid    会员id
     * @param orderStatus 2:只查询可使用水票
     * @param page
     * @return
     */
    public static RequestParams queryWaterVoteOrderList(String memberid, String orderStatus, String page, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("memberid", memberid);
        params.put("orderStatus", orderStatus);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        LogUtils.e("queryWaterVoteOrderList", params.toString());
        return params;
    }


    public static RequestParams queryWaterAvailableTotalNum(String memberid, String orderStatus, String product_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1105");
        params.put("memberid", memberid);
        params.put("orderStatus", orderStatus);
        params.put("product_id", product_id);
        LogUtils.e("queryWaterAvailableTotalNum", params.toString());
        return params;
    }


    /**
     * 查询水票使用明细
     *
     * @param memberid
     * @param om_order_type_arr 订单类型数据 类型之间用,分割
     * @param om_pay_type
     * @param om_state
     * @param page
     * @param rows
     * @param od_source_id
     * @return
     */
    public static RequestParams queryWaterVoteStatementList(String memberid,
                                                            String om_order_type_arr, String om_pay_type, String om_state,
                                                            String page, String rows, String od_source_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("memberid", memberid);
        params.put("om_order_type_arr", om_order_type_arr);
        params.put("om_pay_type", om_pay_type);
        params.put("om_state", om_state);
        //        params.put("page", page);
        //        params.put("rows",rows);
        params.put("od_source_id", od_source_id);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    public static RequestParams queryOrderDetailList(String orderId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("om_id", orderId);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }

    /**
     * 订单确认送达接口
     *
     * @param orderId
     * @return
     */
    public static RequestParams confirmOrder(String orderId, String version) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("orderId", orderId);
        params.put("version", version);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }

    /**
     * 配送任务确认送达接口
     *
     * @param odmId
     * @return
     */
    public static RequestParams confirmDeliveryOrder(String odmId, String version, String memberid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("odmId", odmId);
        params.put("version", version);
        params.put("memberid", memberid);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }

    /**
     * 获取会员列表
     *
     * @return
     */
    public static RequestParams customerList(String page, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("order", "desc");
        params.put("sort", "mb_followtime");
        LogUtils.e("customerList", params.toString());
        return params;
    }

    public static RequestParams queryMemberNum() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        LogUtils.e("queryMemberNum", params.toString());
        return params;
    }

    /**
     * 客户池请求信息
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams inPoolCustomerList(String page, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("order", "desc");
        params.put("sort", "mb_followtime");
        LogUtils.e("customerList", params.toString());
        return params;
    }

    /**
     * 修改密码
     *
     * @param memberid
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public static RequestParams updatePwd(String memberid, String oldPwd, String newPwd) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("memberid", memberid);
        params.put("oldPwd", MD5Util.md5(oldPwd));
        params.put("newPwd", MD5Util.md5(newPwd));
        LogUtils.e("customerList", params.toString());
        return params;
    }


    /**
     * 请求配送任务列表
     *
     * @param logistics_deliveryid 配送员id
     * @param odmState             发货状态(3:已发货;4:已签收;0:全部)
     * @param page
     * @return
     */
    public static RequestParams queryDeliveryList(String logistics_deliveryid,
                                                  String odmState, String page, String rows,String userType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        if (!TextUtils.isEmpty(logistics_deliveryid) && !"1".equals(userType)) {
            params.put("logistics_deliveryid", logistics_deliveryid);
        }
        params.put("odmState", odmState);
        params.put("page", page);
        if (TextUtils.isEmpty(rows)){
            params.put("rows", ROWS);
        }else{
            params.put("rows", rows);
        }

        LogUtils.e("queryDeliveryList", params.toString());
        return params;
    }

    public static RequestParams selectDeliveryList(String ordercode) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("ordercode", ordercode);
        LogUtils.e("selectDeliveryList", params.toString());
        return params;
    }

    /**
     * 查询订单开箱记录列表
     *
     * @param source_id
     * @return
     */
    public static RequestParams boxOperationLog(String source_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("source_id", source_id);
        LogUtils.e("selectDeliveryList", params.toString());
        return params;
    }

    /**
     * 查询配送任务详情
     *
     * @param odmId
     * @return
     */
    public static RequestParams queryDeliveryDetail(String odmId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("odm_id", odmId);
        LogUtils.e("queryDeliveryDetail", params.toString());
        return params;
    }

    /**
     * 获取会员申请活动或商品详情
     *
     * @param pa_id
     * @return
     */
    public static RequestParams getApplyDetail(String pa_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("pa_id", pa_id);
        LogUtils.e("queryDeliveryDetail", params.toString());
        return params;
    }

    /**
     * 审核用户申请
     *
     * @param pam_id
     * @param state  0:不通过 1:审核通过
     * @return
     */
    public static RequestParams checkApply(String pam_id, String state, String num, String pam_def1) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("pam_id", pam_id);
        params.put("state", state);
        params.put("num", num);
        if (pam_def1 != null && pam_def1.length() > 0) {
            params.put("pam_def1", pam_def1);
        }
        LogUtils.e("checkApply", params.toString());
        return params;
    }

    /**
     * 营收简报
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getBriefing(String startDate, String endDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        //1 微信 2 支付宝 3混合 4 现金 5 水票购买 6余额 7 奖励金 8 优惠券 9其它
        LogUtils.e("getBriefing", params.toString());
        return params;
    }

    /**
     * 营收趋势
     *
     * @param startDate
     * @param endDate
     * @param type      1 订单金额 2.收款 3订单数量
     * @param chartType 分组类型	Y	1日  2月
     * @return
     */
    public static RequestParams getTrendsChart(String startDate, String endDate, String type, String chartType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("type", type);
        params.put("chartType", chartType);
        LogUtils.e("getTrendsChart", params.toString());
        return params;
    }

    /**
     * 用户增长趋势
     *
     * @param startDate
     * @param endDate
     * @param chartType
     * @return
     */
    public static RequestParams getTrendsNewMember(String startDate, String endDate, String chartType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("chartType", chartType);
        LogUtils.e("getTrendsNewMember", params.toString());
        return params;
    }

    /**
     * 获取柜子列表
     *
     * @param lng          经度
     * @param lat          纬度
     * @param page         页数
     * @param searchKey
     * @param onlinestatus 在线状态   1在线  2不在线
     * @return
     */
    public static RequestParams queryStoreList(String lng, String lat, String page, String searchKey, int onlinestatus,int alertstate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        if (!"1".equals(UserInfoUtils.getUser(UIUtils.getContext()).getUsertype())) {
            params.put("work_id", UserInfoUtils.getId(UIUtils.getContext()));
        }
        if (onlinestatus != 0) {
            params.put("onlinestatus", onlinestatus + "");
        }
        if (alertstate!=0){
            params.put("alertstate",alertstate+"");
        }
        LogUtils.e("queryStoreList", params.toString());
        return params;
    }

    /**
     * 柜子箱子列表
     * //@param token 用户信息编码
     *
     * @param storeId     仓库id
     * @param isException 是否是异常 1是  2不是
     * @return
     */
    public static RequestParams queryBoxList(String storeId, String qr_code, String isException) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("storeId", storeId);
        params.put("qr_code", qr_code);
        params.put("isException", isException);
        if (!"1".equals(UserInfoUtils.getUser(UIUtils.getContext()).getUsertype())) {
            params.put("work_id", UserInfoUtils.getId(UIUtils.getContext()));
        }
        LogUtils.e("queryBoxList", params.toString());
        return params;
    }

    /**
     * 补货
     *
     * @param storeId   仓库id
     * @param boxIdStr  箱子id	Y	英文逗号分隔
     * @param state     操作	Y	 1清空 2补货 3锁定
     * @param productid 商品id
     * @param skuid
     * @param num       补货数量
     * @param fix_state 维修状态   1正常  2 维修中
     * @param box_state 状态 	   1空 2满  3取货中 4补货中
     * @return
     */
    public static RequestParams updateBoxState(String storeId, String boxIdStr, String state,
                                               String productid, String skuid, String num, String fix_state, String box_state,String imgListStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("storeId", storeId);
        params.put("boxIdStr", boxIdStr);
        params.put("state", state);
        if (fix_state != null && fix_state.length() > 0) {
            params.put("fix_state", fix_state);
        }

        params.put("productid", productid);
        params.put("skuid", skuid);
        params.put("num", num);
        params.put("box_state", box_state);
        params.put("imgListStr",imgListStr);
        LogUtils.e("updateBoxState", params.toString());
        return params;
    }

    /**
     * 机械柜补货参数
     *
     * @param storeId   仓库id
     * @param boxIdStr  箱子id	Y	英文逗号分隔
     * @param state     操作	Y	 1清空 2补货 3锁定
     * @param productid 商品id
     * @param skuid
     * @param num       补货数量
     * @return
     */
    public static RequestParams updateBoxStateTypeThree(String storeId, String boxIdStr, String state, String productid
            , String skuid, String fix_state,String imgListStr,String num) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("storeId", storeId);
        params.put("boxIdStr", boxIdStr);
        params.put("state", state);
        params.put("productid", productid);
        params.put("skuid", skuid);
        if (fix_state != null && fix_state.length() > 0) {
            params.put("fix_state", fix_state);
        }
        params.put("imgListStr",imgListStr);
        params.put("num",num);
        LogUtils.e("updateBoxStateTypeThree", params.toString());
        return params;
    }

    /**
     * 上传图片
     *
     * @param type   1仓库 2调拨、补货 3拜访图片
     * @param imgStr base64加密
     * @return
     */
    public static RequestParams uploadImg(String type, String imgStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("type", type);
        params.put("imgStr", imgStr);
        LogUtils.e("uploadImg", params.toString());
        return params;
    }

    /**
     * 更新仓库
     *
     * @param storeId   仓库id
     * @param storeName 仓库名称
     * @param imgPath   图片地址
     * @param lng       经度
     * @param lat       纬度
     * @param address   地址
     * @return
     */
    public static RequestParams updateStore(String storeId, String storeName, String imgPath, String lng, String lat, String address) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("storeId", storeId);
        params.put("storeName", storeName);
        params.put("imgPath", imgPath);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("address", address);
        LogUtils.e("updateStore", params.toString());
        return params;
    }

    /**
     * 获取仓库地图列表
     *
     * @param storeType 1:主仓库 2.车仓库 4.柜子仓库
     * @return
     */
    public static RequestParams queryMapStoreList(String storeType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("storeType", storeType);
        LogUtils.e("queryMapStoreList", params.toString());
        return params;
    }

    /**
     * @param od_id
     * @return
     */
    public static RequestParams settlementUseTicket(String od_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("od_id", od_id);
        LogUtils.e("settlementUseTicket", params.toString());
        return params;
    }

    /**
     * 生成订单
     *
     * @param orderId 订单id
     * @param num     数量
     * @param bs_code 仓库code
     * @return
     */
    public static RequestParams createOrderUseTicket(String orderId, String num, String bs_code, String user_work_id, String remark) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("orderDetailId", orderId);
        params.put("num", num);
        params.put("buy_type", "1");// 1 水票买水 2直接购
        params.put("bs_code", bs_code);
        params.put("order_type", "3");
        params.put("user_work_id", user_work_id);
        params.put("om_remarks", remark);
        //        orderDetailId
        //                order_type
        //        user_work_id
        //                bs_code
        //        num
        //                mb_id
        //        om_remarks
        LogUtils.e("settlementUseTicket", params.toString());
        return params;
    }

    public static RequestParams getDefaultAddress(String memberid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("memberid", memberid);
        return params;
    }


    /**
     * 柜子销售明细
     *
     * @param startDate
     * @param endDate
     * @param bol_type  操作类型  1客户买货  2业务员补货 3业务员维修
     * @param bol_state 1成功  2不成功
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams getStoreSalesSubsidiary(String startDate, String endDate, String bol_type, String bol_state, String page, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("bol_type", bol_type);
        params.put("bol_state", bol_state);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        if (!"1".equals(UserInfoUtils.getUser(UIUtils.getContext()).getUsertype())) {
            params.put("work_id", UserInfoUtils.getId(UIUtils.getContext()));
        }
        LogUtils.e("getStoreSalesSubsidiary", params.toString());
        return params;
    }

    /**
     * 柜子销售详情列表
     *
     * @param startDate
     * @param endDate
     * @param bs_code
     * @param chartType
     * @param bol_state 1 成功 2失败
     * @return
     */
    public static RequestParams getStoreSalesSubsidiaryDetail(String startDate, String endDate, String bs_code, String storeId, String chartType,String bol_state) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("bs_code", bs_code);
        params.put("chartType", chartType);
        params.put("bol_state",bol_state);
        params.put("storeId",storeId);
        LogUtils.e("getStoreSalesSubsidiaryDetail", params.toString());
        return params;
    }

    /**
     * 会员申请列表
     *
     * @param pam_apply_state 申请状态 1 审核中；2审核通过 3 审核不通过
     * @return
     */
    public static RequestParams dataGridApplyMember(String pam_apply_state, String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("pam_apply_state", pam_apply_state);
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("dataGridApplyMember", params.toString());
        return params;
    }

    /**
     * @param ogr_role                  团中角色类型 1 团长 2 团员
     * @param ogr_group_state           开团状态（1 开团中；2开团成功；3开团失败）
     *                                  //@param ogr_source_type 1.商品 2.活动
     * @param page
     * @param group_remove_false_member 是否去掉虚假团 有值:去掉 ''或不传:不去掉
     * @return
     */
    public static RequestParams dataOrderGroupGrid(String ogr_role, String ogr_group_state/*,String ogr_source_type*/,
                                                   String page, String group_remove_false_member, String startDate, String endDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("ogr_role", ogr_role);
        params.put("ogr_group_state", ogr_group_state);
        //        params.put("ogr_source_type",ogr_source_type);
        params.put("group_remove_false_member", group_remove_false_member);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        LogUtils.e("dataGridApplyMember", params.toString());
        return params;
    }

    /**
     * 自动成团
     *
     * @param orderId
     * @return
     */
    public static RequestParams updateSuccessAuto(String orderId, String ogrId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("orderId", orderId);
        params.put("ogrId", ogrId);
        LogUtils.e("updateSuccessAuto", params.toString());
        return params;
    }

    /**
     * @param ogr_role                  团中角色类型 1 团长 2 团员
     * @param ogr_group_state           开团状态（1 开团中；2开团成功；3开团失败）
     *                                  //@param ogr_source_type 1.商品 2.活动
     * @param page
     * @param group_remove_false_member 是否去掉虚假团 有值:去掉 ''或不传:不去掉
     * @return
     */
    public static RequestParams dataGridOrderGroup(String ogr_role, String ogr_group_state/*,String ogr_source_type*/,
                                                   String page, String group_remove_false_member, String startDate, String endDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("ogr_role", ogr_role);
        params.put("ogr_group_state", ogr_group_state);
        //        params.put("ogr_source_type",ogr_source_type);
        params.put("group_remove_false_member", group_remove_false_member);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        LogUtils.e("dataGridApplyMember", params.toString());
        return params;
    }

    /**
     * 查询团购订单详情
     *
     * @param orderId
     * @return
     */
    public static RequestParams orderGroupDetail(String orderId, String ogrId, String groupId, String sourceId, String sourceType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("orderId", orderId);
        params.put("ogrId", ogrId);
        params.put("groupId", groupId);
        params.put("sourceId", sourceId);
        params.put("sourceType", sourceType);
        LogUtils.e("queryOrderList", params.toString());
        return params;
    }


    /**
     * 查询团购订单详情
     *
     * @param pm_isgroup 是否参团Y/N
     * @param pm_state   状态1  上架 2 下架
     * @return
     */
    public static RequestParams productMainDataGrid(String pm_isgroup, String pm_state, String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("pm_isgroup", pm_isgroup);
        params.put("pm_state", pm_state);
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("productMainDataGrid", params.toString());
        return params;
    }

    /**
     * 自动开团接口
     *
     * @param sourceType 团购订单来源类型 1 商品 2 活动
     * @param sourceId   商品或活动id
     * @return
     */
    public static RequestParams insertGroupAuto(String sourceType, String sourceId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("sourceType", sourceType);
        params.put("sourceId", sourceId);
        LogUtils.e("insertGroupAuto", params.toString());
        return params;
    }

    /**
     * 活动列表
     *
     * @param ppm_state   活动状态 1取消发布 2 暂存状态 3 进行中 4 已结束
     * @param ppm_type    活动类型 :1 商品促销 2、组合促销3、订单促销 4、套餐活动
     * @param ppm_isgroup 是否参团Y/N
     * @param page
     * @return
     */
    public static RequestParams productPromotionMainDataGrid(String ppm_state, String ppm_type, String ppm_isgroup, String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("ppm_state", ppm_state);
        params.put("ppm_type", ppm_type);
        params.put("ppm_isgroup", ppm_isgroup);
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("productPromotionMainDataGrid", params.toString());
        return params;
    }

    /**
     * 获取柜子总的库存明细
     *
     * @return
     */
    public static RequestParams getTotalInventory() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        LogUtils.e("getTotalInventory", params.toString());
        return params;
    }

    /**
     * 查询所有仓库全部商品
     *
     * @param page
     * @return
     */
    public static RequestParams dataGridSku(String page, String searchKey,String pm_typeid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("pm_typeid",pm_typeid);
        LogUtils.e("dataGridSku", params.toString());
        return params;
    }

    /**
     * 获取仓库商品列表
     *
     * @param storeId
     * @param page
     * @param gtzero 车仓库传0 其他1
     * @return
     */
    public static RequestParams dataGridStore(String storeId, String page, String searchKey,String gtzero) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("storeid", storeId);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("gtzero",gtzero);
        LogUtils.e("dataGridSku", params.toString());
        return params;
    }

    /**
     * 调拨、采购提交接口
     *
     * @param eim_storeid       如果 是调拨则存出库的仓库
     * @param eim_other_storeid
     * @param type              101进货入库  201.调拨  301 出库
     * @param erpInoroutItemStr 数据详情
     * @param eim_supplierid    供应商id
     * @param eim_totalamount   总价格
     * @param eim_totalnum      总数量
     * @return
     */
    public static RequestParams insertErpInfo(String eim_storeid, String eim_other_storeid, String type, String erpInoroutItemStr,
                                              String eim_supplierid,String imgListStr,String eim_totalamount,String eim_totalnum
            ,String eim_stock_type) {
        return insertErpInfo(eim_storeid, eim_other_storeid, type, erpInoroutItemStr,
                eim_supplierid, imgListStr, eim_totalamount, eim_totalnum,"","","",eim_stock_type);
    }

    /**
     *
     * @param eim_storeid
     * @param eim_other_storeid
     * @param type
     * @param erpInoroutItemStr
     * @param eim_supplierid
     * @param imgListStr
     * @param eim_totalamount
     * @param eim_totalnum
     * @param eim_box_id
     * @param eim_box_code
     * @param eim_note
     * @param eim_stock_type 30103:报损出库
     * @return
     */
    public static RequestParams insertErpInfo(String eim_storeid, String eim_other_storeid, String type, String erpInoroutItemStr,
                                              String eim_supplierid,String imgListStr,String eim_totalamount,String eim_totalnum,
                                              String eim_box_id,String eim_box_code,String eim_note
            ,String eim_stock_type) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("eim_storeid", eim_storeid);
        if ("201".equals(type)) {
            params.put("eim_other_storeid", eim_other_storeid);
        }
        params.put("type", type);
        params.put("erpInoroutItemStr", erpInoroutItemStr);
        if (eim_supplierid != null && eim_supplierid.length()>0)
            params.put("eim_supplierid", eim_supplierid);
        params.put("imgListStr",imgListStr);

        params.put("eim_totalamount",eim_totalamount);
        params.put("eim_totalnum",eim_totalnum);
        params.put("eim_box_id",eim_box_id);
        params.put("eim_box_code",eim_box_code);
        params.put("eim_note",eim_note);
        params.put("eim_stock_type",eim_stock_type);
        LogUtils.e("insertErpInfo", params.toString());
        return params;
    }

    /**
     *
     * @param page
     * @param type  101进货入库  201.调拨
     * @param startDate
     * @param endDate
     * @param eim_other_storeid
     * @param eim_stock_type   30102:送货记录  30103:报损
     * @param salesId 30102、30103:时传递配送员workId
     * @return
     */
    public static RequestParams getErpList(String page, String type,String startDate,
                                           String endDate,String eim_other_storeid,
                                           String eim_stock_type,String salesId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("type", type);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("eim_other_storeid",eim_other_storeid);
        params.put("eim_stock_type",eim_stock_type);
        params.put("salesId",salesId);
        LogUtils.e("getErpList", params.toString());
        return params;
    }

    /**
     * 查询erp详情
     *
     * @param mainId
     * @return
     */
    public static RequestParams getErpDetail(String mainId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("mainId", mainId);
        LogUtils.e("getErpDetail", params.toString());
        return params;
    }

    /**
     * 获取供应商列表
     *
     * @return
     */
    public static RequestParams getCustomerList() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("rows", ROWS);
        //        params.put("lat",lat);
        //        params.put("lng",lng);
        LogUtils.e("getCustomerList", params.toString());
        return params;
    }

    public static RequestParams doRelation(String storeid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("workid", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("storeid", storeid);
        LogUtils.e("doRelation", params.toString());
        return params;
    }


    public static RequestParams addStore(SrorehouseBean bean) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("workid", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("bs_stats", "1");//1启用 0禁用
        params.put("bs_type", "4");//仓库类型 1 主仓库 2 车 3备建库  4柜子
        params.put("bs_stock_status", "Y");
        params.put("bs_is_negative", "0");
        params.put("bs_is_virtual", "0");
        params.put("bs_boxnum", bean.getBs_boxnum());
        params.put("bs_box_type", bean.getBs_box_type());//1 格子柜  2重力柜 3户外机械柜
        params.put("bs_box_state", bean.getBs_box_state());//柜子状态  1正常  2维修  3异常
        params.put("bs_activestate", bean.getBs_activestate());//激活状态 1:已激活 2:未激活
        params.put("bs_alertnum", bean.getBs_alertnum());
        params.put("bs_spec", bean.getBs_spec());
        params.put("bs_name", bean.getBs_name());
        params.put("bs_code", bean.getBs_code());

        params.put("bs_lng",bean.getBs_lng());
        params.put("bs_lat",bean.getBs_lat());
        params.put("bs_address",bean.getBs_address());
        LogUtils.e("addStore", params.toString());
        return params;
    }

    /**
     * 获取关联商品
     * @param psr_storeid
     * @param page
     * @return
     */
    public static RequestParams productStoreRelationDataGrid(String psr_storeid,String page){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("psr_storeid",psr_storeid);
        params.put("psr_state","1");
        params.put("rows", ROWS);
        params.put("page",page);
        LogUtils.e("productStoreRelationDataGrid", params.toString());
        return params;
    }

    /**
     * 添加柜子关联商品
     * @param storeid
     * @param relationListStr
     * @return
     */
    public static RequestParams productStoreRelationUpdate(String storeid,String relationListStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("storeid",storeid);
        params.put("relationListStr",relationListStr);
        LogUtils.e("productStoreRelationUpdate", params.toString());
        return params;
    }

    /**
     *
     * @param storeid
     * @param page
     * @param startDate
     * @param endDate
     * @param bml_state   1正常 2离线 3故障
     * @return
     */
    public static RequestParams baseMonitoringLog(String storeid,String page,String startDate,String endDate,String bml_state){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("storeid",storeid);
        params.put("rows", ROWS);
        params.put("page",page);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if ("0".equals(bml_state)){
            params.put("bml_state","");
        }else{
            params.put("bml_state",bml_state);
        }
        //水柜状态 1正常 2离线 3故障
//        private String bml_state;
        LogUtils.e("baseMonitoringLog", params.toString());
        return params;
    }

    public static RequestParams baseAlarmLog(String storeid,String page,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("storeid",storeid);
        params.put("rows", ROWS);
        params.put("page",page);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        LogUtils.e("baseAlarmLog", params.toString());
        return params;
    }

    /**
     * 切换店铺信息
     * @return
     */
    public static RequestParams switchShop(String shopid) {
        RequestParams params = new RequestParams();
//        setCommonParams(params, "1200");
//        params.put("reqCode", reqCode);
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));//工作人员id
        params.put("work_id", UserInfoUtils.getId(UIUtils.getContext()));//工作人员id
        params.put("orgid", UserInfoUtils.getOrgid(UIUtils.getContext()));//公司id
        params.put("sign", UserInfoUtils.getSign(UIUtils.getContext()));
        params.put("token", UserInfoUtils.getUser(UIUtils.getContext()).getToken());
        params.put("mb_id", UserInfoUtils.getUser(UIUtils.getContext()).getUserid());//会员id
        params.put("shopid", shopid);
        LogUtils.e("switchShop", params.toString());
        return params;
    }

    /**
     * 获取优惠券列表
     * @param memberId
     * @param state 使用状态 1：未领用   2：已领用未使用  3：已使用  4：已过期
     * @return
     */
    public static RequestParams queryCouponList(String memberId,String state,String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("memberId", memberId);
        params.put("state", state);
        params.put("page",page);
        params.put("rows",ROWS);
        LogUtils.e("queryCouponList", params.toString());
        return params;
    }

    public static RequestParams queryCouponList2(String couponId,String startDate,String endDate,String page,String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("couponId",couponId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("page",page);
        params.put("rows",ROWS);
        params.put("searchKey",searchKey);
//        map.put("cd_coupon_id", request.getParameter("couponId"));
//        map.put("startDate", request.getParameter("startDate"));
//        map.put("endDate", request.getParameter("endDate"));
//        map.put("searchKey", request.getParameter("searchKey"));
        LogUtils.e("queryCouponList2", params.toString());
        return params;
    }


    public static RequestParams scanPay(String orderCode,String code,String odmId, String version, String memberid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("code", code);
        params.put("orderCode", orderCode);
        params.put("odmId", odmId);
        params.put("version", version);
        params.put("memberid", memberid);
        LogUtils.e("scanPay", params.toString());
        return params;
    }


    /**
     * 获取后台优惠券列表
     * @param page
     * @param cb_coupon_type 1取水分享 2用户注册 3普通
     * @param state   状态	N	1 未发布 2已发布 3已取消
     * @return
     */
    public static RequestParams getCouponList(String page,String cb_coupon_type,String state) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("cb_state", state);
        params.put("cb_coupon_type", cb_coupon_type);
        params.put("page", page);
        params.put("rows", ROWS);
//        cb_state	状态	N	1 未发布 2已发布 3已取消
//        cb_coupon_type	类型	N	1取水分享 2用户注册 3普通
        LogUtils.e("getCouponList", params.toString());
        return params;
    }

    /**
     * 赠送优惠券接口
     * @param couponid
     * @param memberId
     * @param cb_shop_id
     * @param couponnum
     * @return
     */
    public static RequestParams sendCouponToMember(String couponid,String memberId,String cb_shop_id,String couponnum) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("couponid", couponid);
        params.put("memberId", memberId);
        params.put("cb_shop_id", cb_shop_id);
        params.put("couponnum",couponnum);
        LogUtils.e("getCouponList", params.toString());
        return params;
    }

    /**
     * 查询消息列表
     * @param mp_read_flag 是否已读	Y	1 已读 0 未读
     * @param page
     * @return
     */
    public static RequestParams queryMessageList(String mp_read_flag,String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("mp_read_flag", mp_read_flag);
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("queryMessageList", params.toString());
        return params;
    }

    /**
     * 标记已读消息
     * @param messageids
     * @return
     */
    public static RequestParams readMessage(String messageids) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("messageids", messageids);
        LogUtils.e("readMessage", params.toString());
        return params;
    }

    public static RequestParams readMessageAll(String workid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("workid", workid);
        LogUtils.e("readMessageAll", params.toString());
        return params;
    }

    /**
     * 获取分享优惠券到小程序字段信息
     * @param couponid
     * @return
     */
    public static RequestParams queryCouponShare(String couponid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("couponid", couponid);
        LogUtils.e("queryCouponShare", params.toString());
        return params;
    }

    /**
     * 拜访信息列表
     * @param mn_memberid
     * @param page
     * @param salesId 开发员id
     * @param searchKey 关键词
     * @return
     */
    public static RequestParams queryNoteList(String mn_memberid,String page,String salesId,String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("mn_memberid", mn_memberid);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("sort","mn_addtime");
        params.put("order","desc");
        params.put("salesId",salesId);
        params.put("searchKey",searchKey);
        LogUtils.e("queryNoteList", params.toString());
        return params;
    }

    /**
     * 添加拜访记录
     * @param mn_memberid 拜访的会员id
     * @param mn_msg  拜访内容
     * @param imgListStr 图片 用,号分割
     * @param mn_tagid_str 标签id	$$44$$5$$3$$  前后都用$$进行分割
     * @return
     */
    public static RequestParams addNote(String mn_memberid,String mn_msg,String imgListStr,String mn_tagid_str) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("mn_memberid", mn_memberid);
        params.put("mn_msg", mn_msg);
        params.put("imgListStr",imgListStr);
        params.put("mn_tagid_str",mn_tagid_str);
        LogUtils.e("addNote", params.toString());
        return params;
    }

    /**
     * 取水记录
     * @param startDate
     * @param endDate
     * @param page
     * @param shopid
     * @param storeid
     * @param ticketid
     * @param state
     * @param recordId 取水记录id
     * @return
     */
    public static RequestParams queryWaterRecordList(String startDate,String endDate,String page,String shopid,
                                                     String storeid,String ticketid,String state,String recordId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("page", page);
        params.put("shopid",shopid);
        params.put("storeid",storeid);
        params.put("ticketid",ticketid);
        params.put("boxid","");
        params.put("rows", ROWS);
        params.put("state",state);
        params.put("recordId",recordId);
//        owr_shopid	店铺
//        owr_storeid	水柜ID
//        owr_boxid	格子id
//        owr_ticketid	电子券主键
        LogUtils.e("queryWaterRecordList", params.toString());
        return params;
    }

    /**
     * 查询客户下的电子券
     * @param startDate
     * @param endDate
     * @param page
     * @param tm_use_state 2使用中 3使用完
     * @param searchKey
     * @return
     */
    public static RequestParams queryTicketListByAll(String startDate,String endDate,String page,
                                                     String tm_use_state,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("tm_use_state", tm_use_state);
        params.put("searchKey", searchKey);
        LogUtils.e("queryTicketListByAll", params.toString());
        return params;
    }

    /**
     * 查询会员下的电子券
     * @param memberid
     * @param page
     * @return
     */
    public static RequestParams queryTicketListByMember(String memberid, String page){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("memberid", memberid);
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("queryTicketListByMember", params.toString());
        return params;
    }

    /**
     * 查询电子券核销记录
     * @param ticketid 电子券主键
     * @param page
     * @return
     */
    public static RequestParams queryTicketVerificationList(String ticketid, String page){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("ticketid", ticketid);
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("queryTicketListByMember", params.toString());
        return params;
    }

    /**
     * 得到业务员列表
     * @param roleStr 查询业务员类型
     * @param mbw_isdelivery Y:配送员 N:不是  mbw_isdelivery和roleStr只传一个值
     * @return
     */
    public static RequestParams getMemberBaseWorkList(String page,String roleStr,String mbw_isdelivery,String leader_workid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("roleStr",roleStr);
        params.put("mbw_isdelivery",mbw_isdelivery);
        params.put("leader_workid",leader_workid);
        LogUtils.e("getMemberBaseWorkList", params.toString());
        return params;
    }

    /**
     * 得到待出库列表
     * @param om_id 订单id
     * @return
     */
    public static RequestParams queryDckGoodsListNoStore(String om_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("om_id", om_id);
        //params.put("store_id", store_id);
        LogUtils.e("queryDckGoodsListNoStore", params.toString());
        return params;
    }

    /**
     * 发货接口
     * @param req
     * @return
     */
    public static RequestParams insertOrderDeliveryNoStore(OrderDeliveryReq req){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        //params.put("deliveryStoreId", req.getDeliveryStoreId());//仓库id
        params.put("deliveryTotalNum", req.getDeliveryTotalNum());//发货数量
        params.put("deliverymanId", req.getDeliverymanId());//业务员id
        params.put("deliveryAmount", req.getDeliveryAmount());//发货总金额
        params.put("productAmount", req.getProductAmount());//商品总金额
        params.put("orderMainId", req.getOrderMainId());//订单id
        params.put("orderDetailListStr", req.getOrderDetailListStr());
	/*（待出库列表返回的字段  json串化 加一个outNum 发货数量）
        od_delivery_num
                od_id
        od_mainid
                od_product_code
        od_product_id
                od_product_name
        od_product_num
                od_product_price
        od_product_skuid
                od_sale_price
        pss_stocknum
        outNum 出库数量*/
        params.put("orderCode", req.getOrderCode());
        params.put("memberId", req.getMemberId());
        params.put("consigneename", req.getConsigneename());
        params.put("consigneemobile", req.getConsigneemobile());
        params.put("consigneeaddress", req.getConsigneeaddress());
        params.put("orderRemarks", req.getOrderRemarks());
        LogUtils.e("insertOrderDelivery", params.toString());
        return params;
    }

    /**
     * 更换发货单业务员
     * @param odm_id 配送记录id
     * @param salesId 业务员id
     * @return
     */
    public static RequestParams updateDeliver(String odm_id,String salesId){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("odm_id", odm_id);
        params.put("salesId", salesId);
        LogUtils.e("updateDeliver", params.toString());
        return params;
    }

    /**
     * 赠送列表
     * @return
     */
    public static RequestParams getMemberGiftList(String memberId,String page,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey",searchKey );
        params.put("mg_memberid",memberId);
        LogUtils.e("updateDeliver", params.toString());
        return params;
    }

    /**
     * 业务员向客户赠送电子水票
     * @param memberid 客户id
     * @param tm_id 水票id
     * @param userid 操作者
     * @param num
     * @return
     */
    public static RequestParams sendWaterTicket(String memberid,String tm_id,String userid,String num) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("memberid", memberid);
        params.put("tm_id", tm_id);
        params.put("userid", userid);
        params.put("num",num);
        LogUtils.e("sendWaterTicket", params.toString());
        return params;
    }

    /**
     * 水票核销
     * @param storehouseId 车仓库id
     * @param ticketMainId 会员水票
     * @param ticketNum    核销数量
     * @return
     */
    public static RequestParams verificationTicket(String storehouseId,String ticketMainId,String ticketNum){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("storehouseId", storehouseId);
        params.put("ticketMainId", ticketMainId);
        params.put("ticketNum",ticketNum );
        LogUtils.e("verificationTicket", params.toString());
        return params;
    }

    /**
     * 审核水票列表
     * @param storehouseId  车仓库
     * @param applyId   会员申请id
     * @param memberId  会员id
     * @param ticketNum 核销数量
     * @return
     */
    public static RequestParams verificationTicket2(String storehouseId,String applyId,String memberId,String ticketNum){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("storehouseId", storehouseId);
        params.put("applyId", applyId);
        params.put("memberId", memberId);
        params.put("ticketNum",ticketNum );
        LogUtils.e("verificationTicket2", params.toString());
        return params;
    }

    /**
     * 采集企业用户信息
     * @param mb_id  客户id	Y
     * @param mb_name 企业名	N
     * @param mb_mobile  联系电话	Y
     * @param mb_corpaddress 企业地址	Y
     * @param mb_peoplenum  企业人数	Y
     * @param mb_bagnum   每次配送数量	Y
     * @param mb_weekday  周几配送	Y
     * @param mb_frequency  配送频次	Y	1 每一周 2每两周
     * @param mb_lat  纬度
     * @param mb_lng  经度
     * @param mb_communityid 写字楼id
     * @param mb_floornum  写字楼层数
     * @return
     */
    public static RequestParams updateMemberBase(String mb_id,String mb_name,String mb_mobile,String mb_corpaddress,
                                                 String mb_peoplenum,
                                                 String mb_bagnum,String mb_weekday,String mb_frequency,
                                                 String mb_lat,String mb_lng,
                                                 String mb_communityid,String mb_floornum,String mb_leadname,String mb_phone,
                                                 String accountPeriod){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("mb_id", mb_id);
        params.put("mb_name", mb_name);
        params.put("mb_mobile",mb_mobile );
        params.put("mb_corpaddress", mb_corpaddress);
        params.put("mb_peoplenum", mb_peoplenum);
        params.put("mb_bagnum", mb_bagnum);
        params.put("mb_weekday", mb_weekday);
        params.put("mb_frequency", mb_frequency);
        params.put("mb_lat",mb_lat);
        params.put("mb_lng",mb_lng);
        params.put("mb_communityid",mb_communityid);
        params.put("mb_floornum",mb_floornum);

        params.put("mb_leadname",mb_leadname);
        params.put("mb_phone",mb_phone);

        params.put(" mb_is_account_period",accountPeriod);

        LogUtils.e("verificationTicket", params.toString());
        return params;
    }


    /**
     * 添加写字楼参数
     * @param bcm_name
     * @param bcm_address
     * @param bcm_longitude 经度
     * @param bcm_latitude 纬度
     * @param bcm_weekday 星期几配送
     * @return
     */
    public static RequestParams updateCommunity(String bcm_name,String bcm_address,String bcm_longitude,String bcm_latitude,
                                                String bcm_weekday){
        RequestParams params = new RequestParams();

        UserBean userBean = UserInfoUtils.getUser(UIUtils.getContext());

        setCommonParams(params, "1200");
        params.put("bcm_name", bcm_name);
        params.put("bcm_address", bcm_address);
        params.put("bcm_longitude",bcm_longitude );
        params.put("bcm_latitude", bcm_latitude);
        params.put("bcm_workid", UserInfoUtils.getId(UIUtils.getContext())); //工作人员id
        params.put("bcm_workuserid", UserInfoUtils.getId(UIUtils.getContext()));//会员id
        params.put("bcm_add_user_id", UserInfoUtils.getId(UIUtils.getContext()));//会员id
        params.put("bcm_weekday", bcm_weekday);
        params.put("bcm_shopid", userBean.getShopId()); //经销商id
        LogUtils.e("updateCommunity", params.toString());
        return params;
    }

    /**
     * 修改写字楼
     * @param bcm_name
     * @param bcm_address
     * @param bcm_longitude
     * @param bcm_latitude
     * @param bcm_weekday
     * @param bcm_id  写字楼id
     * @return
     */
    public static RequestParams updateCommunity(String bcm_name,String bcm_address,String bcm_longitude,String bcm_latitude,
                                                String bcm_weekday,String bcm_id) {
        RequestParams params = new RequestParams();

        UserBean userBean = UserInfoUtils.getUser(UIUtils.getContext());

        setCommonParams(params, "1200");
        params.put("bcm_name", bcm_name);
        params.put("bcm_address", bcm_address);
        params.put("bcm_longitude", bcm_longitude);
        params.put("bcm_latitude", bcm_latitude);
        params.put("bcm_deliverid", UserInfoUtils.getId(UIUtils.getContext())); //配送员的workid
        params.put("bcm_deliveruserid", userBean.getUserid());//配送员的mb_id
        params.put("bcm_weekday", bcm_weekday);
        params.put("bcm_id", bcm_id); //楼宇id
        LogUtils.e("updateCommunity", params.toString());
        return params;
    }

    /**
     * 写字楼列表
     * @param queryType 查询类型	1 业务员  2配送员
     * @param page 页数
     * @param searchKey 写字楼名
     * @return
     */
    public static RequestParams getCommunityList(String queryType,String page,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("queryType", queryType);
        params.put("searchKey", searchKey);
        params.put("week_day","");//星期几
        params.put("lng", "");//经度
        params.put("lat", "");//纬度
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("getCommunityList", params.toString());
        return params;
    }

    /**
     * 获取客户申请配送列表
     * @param applyState  状态 1 未处理 2配送中3已完成 4已取消
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams queryTicketSendApplyList(String applyState,String page,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("applyState", applyState);
        params.put("searchKey", searchKey);
        params.put("week_day","");//星期几
        params.put("lng", "");//经度
        params.put("lat", "");//纬度
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("queryTicketSendApplyList", params.toString());
        return params;
    }

    public static RequestParams queryTicketSendApplyListTotal(String applyState,String page,String searchKey,String rows){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("applyState", applyState);
        params.put("searchKey", searchKey);
        params.put("week_day","");//星期几
        params.put("lng", "");//经度
        params.put("lat", "");//纬度
        params.put("page", page);
        params.put("rows", rows);
        LogUtils.e("queryTicketSendApplyList", params.toString());
        return params;
    }


    /**
     * 申请单分配配送员
     * @param tsa_id
     * @param tsa_delivery_workid 配送员workid
     * @param tsa_delivery_userid 配送员mb_id
     * @param tsa_apply_state
     * @return
     */
    public static RequestParams updateTicketSendApply(String tsa_id,String tsa_delivery_workid,
                                                      String tsa_delivery_userid,String tsa_apply_state){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("tsa_id", tsa_id);
        params.put("tsa_delivery_workid", tsa_delivery_workid);
        params.put("tsa_delivery_userid",tsa_delivery_userid);
        params.put("tsa_apply_state", tsa_apply_state);//经度
        LogUtils.e("updateTicketSendApply", params.toString());
        return params;
    }

    /**
     * 查询角色列表
     * @param bean
     * @return
     */
    public static RequestParams queryRoleList(UserBean bean){
        RequestParams params = new RequestParams();
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");
        params.put("userId",bean.getUserid());//会员人员id
        params.put("work_id",bean.getWorkid());//工作人员id
        params.put("orgid", bean.getOrgid());//公司id
        params.put("sign", UserInfoUtils.getSign(UIUtils.getContext()));
        params.put("token", bean.getToken());
        params.put("mb_id", bean.getUserid());//会员id
        params.put("shopid", bean.getShopId());//店铺id
        params.put("mbw_storeid", bean.getCarid());//车仓库id
        params.put("userType",bean.getUsertype());
        LogUtils.e("queryRoleList", params.toString());
        return params;
    }

    /**
     * 待发货清单列表
     * @param page
     * @param deliveryId 配送员work_id
     * @param deliveryState 发货状态1 未发货 2部分发货 3 已发货 4 已签收5 取货中 6 已完成
     * @return
     */
    public static RequestParams queryDeliveryProductAndNum(String page,String deliveryId,String deliveryState){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("deliveryId",deliveryId);
        params.put("deliveryState",deliveryState);
        LogUtils.e("queryDeliveryProductAndNum", params.toString());
        return params;
    }

    /**
     * 获取工作人员信息接口
     * @param userBean
     * @return
     */
    public static RequestParams getMemberWorkInfo(UserBean userBean){
        RequestParams params = new RequestParams();
        //setCommonParams(params, "1200");
        params.put("workId", userBean.getWorkid());
        params.put("token",userBean.getToken());
        LogUtils.e("getMemberWorkInfo", params.toString());
        return params;
    }

    /**
     * 获取会员详情
     * @param memberId
     * @return
     */
    public static RequestParams getMemberInfo(String memberId){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("memberId", memberId);
        LogUtils.e("getMemberInfo", params.toString());
        return params;
    }

    /**
     * 得到申请详情
     * @param tsa_id
     * @return
     */
    public static RequestParams getTicketSendApplyInfo(String tsa_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("tsa_id", tsa_id);
        LogUtils.e("getTicketSendApplyInfo", params.toString());
        return params;
    }

    /**
     * 获取字典列表(默认查询客户标签)
     * @return
     */
    public static RequestParams getBaseDataDetailList(){
        return getBaseDataDetailList("2187");
    }

    /**
     * 获取字典列表(默认查询客户标签)
     * @return
     */
    public static RequestParams getBaseDataDetailList(String codekey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("codekey", codekey);
        LogUtils.e("getBaseDataDetailList", params.toString());
        return params;
    }

    /**
     * 体验套餐列表
     * @param page
     * @return
     */
    public static RequestParams getPackageList(String page){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("getPackageList", params.toString());
        return params;
    }

    /**
     * 获取体验套餐详情
     * @param ppm_id
     * @return
     */
    public static RequestParams getPackageDetail(String ppm_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("ppm_id", ppm_id);
        LogUtils.e("getPackageDetail", params.toString());
        return params;
    }

    /**
     * 申请套餐活动
     * @param spa_promotionid  套餐id
     * @param spa_num
     * @param spa_type 申请类型		1、套餐 2领导领用 3员工领用 4办公领用
     * @param spa_goodid 商品id
     * @return
     */
    public static RequestParams addPackageApply(String spa_promotionid,String spa_num,String spa_type,
                                                String spa_goodid,String spa_good_skuid,String reason){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("spa_promotionid", spa_promotionid);
        params.put("spa_def1", spa_promotionid);
        params.put("spa_num", spa_num);
        params.put("spa_type",spa_type);
        params.put("spa_goodid",spa_goodid);
        params.put("spa_good_skuid",spa_good_skuid);
        params.put("spa_reason",reason);
        LogUtils.e("addPackageApply", params.toString());
        return params;
    }

    /**
     * 申请的套餐列表
     * @param checkState 1未审核 2通过  3不通过
     * @param page
     * @param queryType queryType  1申请  2 审核
     * @return
     */
    public static RequestParams getPackageApplyList(String checkState,String page,String queryType){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("checkState", checkState);
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("promotionId", "");
        params.put("salesId", "");
        params.put("queryType",queryType);
        LogUtils.e("getPackageApplyList", params.toString());
        return params;
    }

    public static RequestParams getPackageApplyListTotal(String checkState,String page,String rows){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("checkState", checkState);
        params.put("page", page);
        params.put("rows", rows);
        params.put("promotionId", "");
        params.put("salesId", "");
        params.put("queryType","2");
        LogUtils.e("getPackageApplyList", params.toString());
        return params;
    }


    /**
     * 套餐审核
     * @param spa_id
     * @param checkState 审核状态	2通过  3不通过
     * @param spa_out_storeid 出库仓库
     * @param spa_context 审核内容
     * @return
     */
    public static RequestParams checkApplyPackage(String spa_id,String checkState,String spa_out_storeid,String spa_context){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("spa_id", spa_id);
        params.put("checkState", checkState);
        params.put("spa_storeid", spa_out_storeid);//之前的就字段
        params.put("spa_out_storeid", spa_out_storeid);
        params.put("spa_context", spa_context);
        LogUtils.e("checkApplyPackage", params.toString());
        return params;
    }

    /**
     * 得到工作人员自己的套餐列表
     * @param page
     * @param leaderId 开发员主管id
     * @param spr_type 申请类型 1套餐、2主管、3开发人员
     * @return
     */
    public static RequestParams getPackageRelationList(String page,String leaderId,String spr_type){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("promotionId", "");//套餐id
        params.put("salesId", "");//人员id
        params.put("leaderId",leaderId);
        params.put("spr_type",spr_type);
        LogUtils.e("getPackageRelationList", params.toString());
        return params;
    }

    /**
     * 分享数据
     * @param relationid 业务员自己与套餐的关联id
     * @return
     */
    public static RequestParams getSharePromotionVO(String relationid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("relationid", relationid);
        LogUtils.e("getSharePromotionVO", params.toString());
        return params;
    }

    /**
     * 注册分享
     * @return
     */
    public static RequestParams getShareRegisterVO(){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        LogUtils.e("getShareRegisterVO", params.toString());
        return params;
    }

    /**
     * 审核
     * @param mainId    单据id
     * @param check_state  2审核通过  3审核不通过
     * @param check_comment  审核说明
     * @return
     */
    public static RequestParams updateCheckErp(String mainId,String check_state,String check_comment) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("mainId", mainId);
        params.put("check_state", check_state);
        params.put("check_comment", check_comment);
        LogUtils.e("updateCheckErp", params.toString());
        return params;
    }

    /**
     * 得到工作人员自己使用套餐的详情列表
     * @param promotionId 套餐id
     * @param salesId     人员id
     * @param relationId  关联id
     * @return
     */
    public static RequestParams getSalesPackageDetailList(String page,String promotionId,String salesId,String relationId){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("promotionId", promotionId);//套餐id
        params.put("salesId", "");//人员id
        LogUtils.e("getSalesPackageDetailList", params.toString());
        return params;
    }

//    /**
//     * 开发申请
//     * @param mda_memberid 客户主键
//     * @param mda_apply_type 1延期申请；2开发申请
//     * @return
//     */
//    public static RequestParams addApply(String mda_memberid,String mda_apply_type){
//        RequestParams params = new RequestParams();
//        setCommonParams(params, "1200");
//        params.put("mda_memberid", mda_memberid);//客户主键	Y
//        params.put("mda_apply_type", mda_apply_type);//1延期申请；2开发申请
//        LogUtils.e("addApply", params.toString());
//        return params;
//    }

    /**
     * 开发客户列表
     * @param page
     * @param searchKey
     * @param mb_kh_state 客户状态		1潜在客户 2 正式客户
     * @param mb_kf_memberid2 当前开发人员
     * @param userType 角色类型
     * @return
     */
    public static RequestParams allCustomerList(String page, String searchKey,String mb_kh_state,
                                                String mb_kf_memberid2,String userType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("order", "desc");
        params.put("sort", "mb_followtime");
        params.put("mb_kh_state",mb_kh_state);
        params.put("mb_kf_memberid2",mb_kf_memberid2);
        params.put("userType",userType);
        LogUtils.e("allCustomerList", params.toString());
        return params;
    }

    /**
     * 客户信息列表
     * @param page
     * @param searchKey
     * @param userType 角色类型
     * @param mb_agentid 团队id
     * @param mb_agenttype
     * @param agentTypeStr    1 普通   2 合伙人  3金牌   英文逗号分隔
     * @return
     */
    public static RequestParams queryMemberAgentList(String page, String searchKey, String userType,String mb_agentid,String mb_partner_first,
                                                     String mb_agenttype,String agentTypeStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("order", "desc");
        params.put("sort", "mb_followtime");
        params.put("userType",userType);
        params.put("mb_agentid",mb_agentid);
        params.put("mb_partner_first",mb_partner_first);
        params.put("mb_agenttype",mb_agenttype);
        params.put("agentTypeStr",agentTypeStr);
        LogUtils.e("queryMemberAgentList", params.toString());
        return params;
    }


    public static RequestParams getList(String page, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("order", "desc");
        params.put("sort", "mb_followtime");
        LogUtils.e("getList", params.toString());
        return params;
    }

    /**
     * 开发申请
     * @param mda_memberid   客户主键
     * @param mda_apply_type 申请类型	Y	1延期申请；2开发申请
     * @param mda_apply_reason 申请原因
     * @return
     */
    public static RequestParams addApplyMember(String mda_memberid,String mda_apply_type,String mda_apply_reason) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("mda_memberid", mda_memberid);
        params.put("mda_apply_type", mda_apply_type);
        params.put("mda_apply_reason",mda_apply_reason);
        LogUtils.e("addApplyMember", params.toString());
        return params;
    }

    /**
     * 审核用户申请
     *
     * @param mda_id 申请记录主键	Y
     * @param mda_apply_state 审核状态	Y	4审核通过 5 审核不通过
     * @param mda_context  审批意见
     * @return
     */
    public static RequestParams leadCheckApply(String mda_id, String mda_apply_state,String mda_context) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("mda_id", mda_id);
        params.put("mda_apply_state", mda_apply_state);
        params.put("mda_context", mda_context);
        LogUtils.e("leadCheckApply", params.toString());
        return params;
    }

    /**
     * 转移客户
     * @param transferType 转移类型	Y	//1转给开发员 2 放到客户池
     * @param mb_id  客户id	N
     * @param kf_workid 转移到开发人员id	Y
     * @return
     */
    public static RequestParams transfer(String transferType, String mb_id, String kf_workid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("transferType", transferType);
        params.put("mb_id", mb_id);
        params.put("kf_workid", kf_workid);
        LogUtils.e("transfer", params.toString());
        return params;
    }

    /**
     * 我的客户申请记录
     * @param page
     * @return
     */
    public static RequestParams myList(String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        LogUtils.e("myList", params.toString());
        return params;
    }

    /**
     * 消费笔数
     * @param memberId
     * @return
     */
    public static RequestParams getMemberAmount(String memberId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("memberId", memberId);
        LogUtils.e("getMemberAmount", params.toString());
        return params;
    }

    /**
     * 待审核数量
     * @return
     */
    public static RequestParams getToCheckNum() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        LogUtils.e("getToCheckNum", params.toString());
        return params;
    }

    /**
     * 客户汇总消费金额
     * @return
     */
    public static RequestParams getTotalAmount() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        LogUtils.e("getTotalAmount", params.toString());
        return params;
    }

    /**
     * 更改配送单配送时间
     * @param odm_id 配送记录id
     * @param odm_suggest_time 配送时间
     * @return
     */
    public static RequestParams updateDeliverTime(String odm_id,String odm_suggest_time) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("odm_id", odm_id);
        params.put("odm_suggest_time", odm_suggest_time);
        LogUtils.e("updateDeliverTime", params.toString());
        return params;
    }

    /**
     * 查询分润列表
     * @param page
     * @param searchKey
     * @param spreadid 合伙人id
     * @return
     */
    public static RequestParams queryGlProfitList(String page, String searchKey,String spreadid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("gp_state", "2");//结算状态 	默认2
        params.put("spreadid",spreadid);
        LogUtils.e("queryBaseAgentList", params.toString());
        return params;
    }

}
