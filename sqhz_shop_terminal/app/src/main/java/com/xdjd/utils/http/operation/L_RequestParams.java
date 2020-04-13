package com.xdjd.utils.http.operation;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MD5Util;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

import java.util.List;

/**
 * 网络请求参数类
 * Created by lijipei on 2016/11/24.
 */
public class L_RequestParams {

    /**
     * 公共参数方法
     *
     * @param params
     * @param reqCode
     */
    public static void setCommonParams(RequestParams params, String reqCode) {
        params.put("reqCode", reqCode);
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("orgid",UserInfoUtils.getOrgid(UIUtils.getContext()));
        params.put("sign", UserInfoUtils.getSign(UIUtils.getContext()));
        params.put("userDocId", UserInfoUtils.getBudId(UIUtils.getContext()));
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
     * @param curVersion 当前版本号
     * @return
     */
    public static RequestParams getNewVersion(String userId, String curVersion) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("userId", userId);
        params.put("curVersion", curVersion);
        params.put("type", "1");
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
    public static RequestParams getLogin(String loginName, String password,
                                         String longitude,String latitude,String netaddress) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("loginName", loginName);
        params.put("password", MD5Util.toMD5_32(password));
        params.put("userId", "");
        params.put("longitude ",longitude );
        params.put("latitude",latitude);
        params.put("netaddress",netaddress);
        LogUtils.e("getLogin", params.toString());
        return params;
    }

    /**
     * 第三方登录标识
     *
     * @param unionId
     * @return
     */
    public static RequestParams thirdPartyLogin(String unionId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1001");
        params.put("unionId", unionId);//第三方登录标识unionid
        LogUtils.e("thirdPartyLogin", params.toString());
        return params;
    }

    /**
     * 绑定微信
     *
     * @param bind_type Y	1绑定 2解绑
     * @param unionId   bind_type 为1 时必传
     * @return
     */
    public static RequestParams bindWx(String bind_type, String unionId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("bind_type", bind_type);
        if ("1".equals(bind_type))
            params.put("unionId", unionId);//第三方登录标识unionid
        LogUtils.e("bindWx", params.toString());
        return params;
    }

    /**
     * 根据用户id,获取用户信息
     *
     * @param userId
     * @return
     */
    public static RequestParams getUserInfo(String userId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("userId", userId);
        return params;
    }

    public static RequestParams getUserLineOrSettingInfo(){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        LogUtils.e("getUserLineOrSettingInfo",params.toString());
        return params;
    }

    /**
     * 用户头像上传
     *
     * @param userId
     * @param customerId
     * @param imgStr
     * @return
     */
    public static RequestParams updateImg(String userId, String customerId, String imgStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("imgStr", imgStr);
        LogUtils.e("updateImg",params.toString());
        return params;
    }

    /**
     * 业务员一天的签到签退
     *
     * @param userId
     * @param lineId
     * @param cud_id   签到id	N	签到时不用传 签退时传
     * @param signType 类型	Y	1签到 2签退
     * @return
     */
    public static RequestParams addUserSign(String userId, String lineId, String cud_id, String signType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        params.put("lineId", lineId);
        if (!cud_id.equals("")) {
            params.put("cud_id", cud_id);
        }
        params.put("signType", signType);
        LogUtils.e("addUserSign", params.toString());
        return params;
    }

    /**
     * 照片上传接口
     *
     * @param imgStr
     * @param cameraType
     * @return
     */
    public static RequestParams uploadImage(String imgStr, String cameraType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("imgStr", imgStr);
        params.put("type", cameraType);//1签到  2盘点 3门店照片
        LogUtils.e("addUserSign", params.toString());
        return params;
    }

    /**
     * 获取任务数量
     *
     * @param isDelivery 是否配送	Y	1 是  2未配送
     * @return
     */
    public static RequestParams getTaskNum(String isDelivery, String customerId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("isDelivery", isDelivery);
        params.put("customerId", customerId);
        return params;
    }

    /**
     * 获取客户余额字段
     * @return
     */
    public static RequestParams getCustomerBalance(String customerId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("customerId", customerId);
        return params;
    }

    /**
     * 根据线路id得到用户分类列表
     *
     * @param userId 用户id
     * @param lineId 线路id
     * @param orgid  公司id
     * @param lng    经度
     * @param lat    纬度
     * @return
     */
    public static RequestParams getLineCategory(String userId, String lineId, String orgid, String lng, String lat) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        params.put("lineId", lineId);
        params.put("orgid", orgid);
        params.put("lng", lng);
        params.put("lat", lat);
        LogUtils.e("getLineCategory", params.toString());
        return params;
    }

    /**
     * 根据定位 或者类别 查询客户
     *
     * @param lineId
     * @param orgid
     * @param lng          经度
     * @param lat          纬度
     * @param categorytype 类型	Y	1.附近 2未定位3类别
     * @param categoryid   类别 id	N	类别 id
     * @return
     */
    public static RequestParams getCustomerToCategory(String lineId, String orgid, String lng, String lat,
                                                      String categorytype, String categoryid, String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("lineId", lineId);
        params.put("orgid", orgid);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("categorytype", categorytype);
        params.put("categoryid", categoryid);
        params.put("page", page);
        params.put("rows", "15");

        LogUtils.e("getCustomerToCategory", params.toString());
        return params;
    }

    /**
     * 根据手机号得到客户的信息
     * @param userId
     * @param mobile
     * @return
     */
    public static  RequestParams getCustomerAllInfor(String userId,String mobile){
        RequestParams params = new RequestParams();
        setCommonParams(params,"2001");
        params.put("userId",userId);
        params.put("mobile",mobile);
        Log.e("phone:",params.toString());
        return params;
    }

    /**
     * 获取客户详细信息接口
     *
     * @param lineId
     * @param customerId
     * @return
     */
    public static RequestParams getCustomerInfo(String lineId, String customerId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("lineId", lineId);
        params.put("customerId", customerId);
        LogUtils.e("getCustomerInfo", params.toString());
        return params;
    }

    /**
     * 线路客户页面 搜索 刷新
     *
     * @param userId 用户id
     * @param lineId 线路id
     * @param orgid  公司id
     * @param lng    经度
     * @param lat    纬度
     * @return
     * @params isLocation Y是  N不是（线路id一块传）
     */
    public static RequestParams getLineCsSearch(String userId, String lineId, String orgid, String lng, String lat, String isLocation, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        params.put("lineId", lineId);
        params.put("orgid", orgid);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("isLocation", isLocation);
        params.put("searchKey", searchKey);
        LogUtils.e("getLineCategory", params.toString());
        return params;
    }

    /**
     * 获取商品一二级分类
     *
     * @param uid
     * @param source 1 统配进货2预定进货
     * @return
     */
    public static RequestParams getGoodsCategory(String uid, String source) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1200");
        params.put("user", uid);
        params.put("source", source);
        return params;
    }

    /**
     * 进店签到
     *
     * @param userId
     * @param lineId
     * @param orgid
     * @param cc_id
     * @param lng
     * @param lat
     * @param clct_type          签到方式 1扫卡 2手动
     * @param clct_location_type 定位方式 1基站  2GPS
     * @param imgListStr         图片路径 多个用逗号分开
     * @return
     */
    public static RequestParams sign(String userId, String lineId, String orgid, String cc_id,
                                     String lng, String lat, String clct_type, String clct_location_type,
                                     String clct_address, String imgListStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        params.put("lineId", lineId);
        params.put("orgid", orgid);
        params.put("cc_id", cc_id);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("clct_type", clct_type);
        params.put("clct_location_type", clct_location_type);
        params.put("clct_address", clct_address);
        if (imgListStr != null && !"".equals(imgListStr)) {
            params.put("imgListStr", imgListStr);//图片路径	多个用逗号分开
        }
        LogUtils.e("sign", params.toString());
        return params;
    }

    /**
     * 离店签退
     *
     * @param userId
     * @param lineId
     * @param orgid
     * @param cc_id
     * @param taskId
     * @param imgListStr 图片路径 多个用逗号分开
     * @return
     */
    public static RequestParams signOut(String userId, String lineId, String orgid, String cc_id,
                                        String lng, String lat, String clct_type, String clct_location_type, String taskId, String imgListStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        params.put("lineId", lineId);
        params.put("orgid", orgid);
        params.put("cc_id", cc_id);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("clct_type", clct_type);
        params.put("clct_location_type", clct_location_type);
        if (taskId==null||"".equals(taskId)){
            taskId="-1";//如果客户签到id是空,默认传-1
        }
        params.put("taskId", taskId);
        params.put("imgListStr", imgListStr);//图片路径	多个用逗号分开
        LogUtils.e("signOut", params.toString());
        return params;
    }

    /**
     * 获取客户通讯录列表
     *
     * @param userId
     * @return
     */
    public static RequestParams getCustomerList(String userId, String lineId, String page, String rows, String searchKey
    ,String startDate,String endDate,String categoryId,String isAll) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        if (!"".equals(lineId)) {
            params.put("lineId", lineId);//lineId	线路id	N	如果是通讯 录则需要入
        }
        params.put("page", page);
        params.put("rows", rows);
        params.put("searchKey", searchKey);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("categoryId",categoryId);
        params.put("isAll",isAll);
        LogUtils.e("getCustomerList", params.toString());
        return params;
    }

    /**
     * 得到附近店铺
     * @param lng
     * @param lat
     * @param searchKey
     * @param page
     * @param rows
     * @param type  类型		1是附近客户
     * @return
     */

    public static RequestParams getNearbyShop(String lng,String lat,String searchKey,String page,String rows,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"2001");
        params.put("lng",lng);
        params.put("lat",lat);
        params.put("searchKey",searchKey);
        params.put("page",page);
        params.put("rows",rows);
        params.put("type",type);
        Log.e("param:",params.toString());
        return  params;
    }

    /**
     * 采集员得到店铺信息
     * @param userId
     * @param lng
     * @param lat
     * @param searchKey
     * @param page
     * @param rows
     * @param type 1：附近店铺  2：未定位店铺 3：已采集
     * @param checkstatus
     * @return
     */
    public static RequestParams getCollectShop(String userId,String lng,String lat,String searchKey,String page,
                                               String rows,String type,String checkstatus){
        RequestParams params = new RequestParams();
        setCommonParams(params,"2001");
        params.put("userId",userId);
        if(!"".equals(lng)&& null != lng){
            params.put("lng",lng);
        }
        if(!"".equals(lat) && null != lat){
            params.put("lat",lat);
        }
        params.put("searchKey",searchKey);
        params.put("page",page);
        params.put("rows",rows);
        params.put("type",type);
        if(!"".equals(checkstatus) && null != checkstatus){
            params.put("checkstatus",checkstatus);
        }
        return params;
    }

    /**
     * 管理员附近客户列表
     * @param lng
     * @param lat
     * @param searchKey
     * @param page
     * @param rows
     * @param type 1附近
     * @return
     */
    public static RequestParams getGlyNearCustomerList(String lng,String lat,String searchKey,String page,
                                                       String rows,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"2001");
        params.put("lng",lng);
        params.put("lat",lat);
        params.put("searchKey",searchKey);
        params.put("page",page);
        params.put("rows",rows);
        params.put("type",type);
        LogUtils.e("getGlyNearCustomerList",params.toString());
        return params;
    }

    /**
     * 采集员添加店铺和定位店铺信息
     * @param userId
     * @param lng
     * @param lat
     * @param custId
     * @param contacts_name
     * @param contacts_mobile
     * @param address
     * @param counter_num
     * @return
     */
    public static RequestParams CollectAndLocateShop(String userId,String lng,String lat,String custId,String name,String contacts_name,
                                              String contacts_mobile,String address,String counter_num,String image){
        RequestParams params = new RequestParams();
        setCommonParams(params,"2001");
        params.put("userId",userId);
        params.put("lng",lng);
        params.put("lat",lat);
        if(!"".equals(custId)&& null != custId){
            params.put("custId",custId);
        }
        params.put("name",name);
        params.put("contacts_name",contacts_name);
        params.put("contacts_mobile",contacts_mobile);
        params.put("address",address);
        params.put("counter_num",counter_num);
        params.put("image",image);
        Log.e("params",params.toString());
        return params;
    }

    public static RequestParams getCustomerInfoByLocationid(String locationid){
        RequestParams params = new RequestParams();
        setCommonParams(params,"2001");
        params.put("locationid",locationid);
        LogUtils.e("getCustomerInfoByLocationid",params.toString());
        return params;
    }

    /**
     * 添加客户类别和渠道
     * @return
     */
    public static RequestParams getAddInfo() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        return params;
    }

    /**
     * 添加客户
     *
     * @param userId
     * @param orgid
     * @param cc_code            编码	Y
     * @param cc_name
     * @param cc_categoryid      类别	Y
     * @param cc_channelid       渠道	Y
     * @param cc_contacts_name   联系人
     * @param cc_contacts_mobile 联系电话
     * @param cc_barcode         卡号
     * @param cc_remarks         备注
     * @param cc_depotid         仓库id
     * @param cc_goods_gradeid   价格id
     * @return
     */
    public static RequestParams addCustomer(String userId, String orgid, String cc_code, String cc_id,String cc_name, String cc_categoryid
            , String cc_channelid, String cc_contacts_name, String cc_contacts_mobile, String cc_address, String cc_barcode, String cc_remarks, String lineIds,
                                            String cc_longitude, String cc_latitude,String cc_depotid,String cc_image,String locationid,String cc_goods_gradeid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", userId);
        params.put("orgid", orgid);
        params.put("cc_code", cc_code);
        params.put("cc_name", cc_name);
        params.put("cc_categoryid", cc_categoryid);
        params.put("cc_channelid", cc_channelid);
        params.put("cc_contacts_name", cc_contacts_name);
        params.put("cc_contacts_mobile", cc_contacts_mobile);
        params.put("cc_address", cc_address);
        //        params.put("cc_is_offer",cc_is_offer?"1":"2"); 是否开通商城 	Y	1开  2不开
        params.put("cc_barcode", cc_barcode);
        params.put("cc_remarks", cc_remarks);
        params.put("lineIds", lineIds);
        params.put("cc_longitude", cc_longitude);
        params.put("cc_latitude", cc_latitude);
        params.put("cc_image", cc_image);
        params.put("cc_depotid",cc_depotid);//cc_depotid	仓库	Y	仓库
        if(!"".equals(cc_id)){
            params.put("cc_id",cc_id);
        }
        if(!"".equals(locationid)){
            params.put("cc_locationid",locationid);
        }
        params.put("cc_goods_gradeid",cc_goods_gradeid);
        LogUtils.e("addCustomer",params.toString());
        return params;
    }

    /**
     * 扫码得到客户信息
     * @param cc_code 客户编码
     * @return
     */
    public static RequestParams getCustomerInfoByCode(String cc_code){
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("cc_code", cc_code);
        LogUtils.e("showQrcodeImg", params.toString());
        return params;
    }

    /**
     * 绑定微信
     *
     * @param customerId
     * @return
     */
    public static RequestParams showQrcodeImg(String customerId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("customerId", customerId);
        LogUtils.e("showQrcodeImg", params.toString());
        return params;
    }

    /**
     * 添加盘点商品信息
     *
     * @param customerId
     * @param cim_note         备注
     * @param cim_totalamount  总金额
     * @param cim_totalnum     总数量
     * @param inventoryItemStr 数据详情
     * @param imgListStr       图片列表 逗号分隔
     * @return
     */
    public static RequestParams addInventory(String customerId, String cim_note, String cim_totalamount, String cim_totalnum,
                                             String inventoryItemStr, String imgListStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("customerId", customerId);
        params.put("cim_note", cim_note);
        params.put("cim_totalamount", cim_totalamount);
        params.put("cim_totalnum", cim_totalnum);
        params.put("inventoryItemStr", inventoryItemStr);
        params.put("imgListStr", imgListStr);

        LogUtils.e("addInventory", params.toString());
        return params;
    }

    /**
     * 盘点客户列表
     *
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getInventoryList(String page, String startDate, String endDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("page", page);
        params.put("rows", "10");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return params;
    }

    /**
     * 盘点客户详情
     *
     * @param cim_id
     * @param page
     * @return
     */
    public static RequestParams getInventoryDetail(String cim_id, String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("page", page);
        params.put("rows", "10");
        params.put("cim_id", cim_id);
        LogUtils.e("getInventoryDetail", params.toString());
        return params;
    }

    /**
     * 更新位置信息
     *
     * @param cc_name 店铺名称
     * @param lineId
     * @param cc_id
     * @param cc_longitude
     * @param cc_latitude
     * @param cc_net_address     定位得到的地址
     * @param cc_contacts_name   客户姓名
     * @param cc_contacts_mobile 客户电话
     * @param cc_categoryid      类别
     * @param cc_channelid       渠道
     * @param cc_address         编辑的地址
     * @param cc_image
     * @param cc_goods_gradeid   价格方案
     * @return
     */
    public static RequestParams updateLocation(String cc_name, String lineId,
                                               String cc_id, String cc_longitude, String cc_latitude,
                                               String cc_net_address, String cc_contacts_name, String cc_contacts_mobile,
                                               String cc_categoryid, String cc_channelid,String cc_depotid,
                                               String cc_address,String cc_image,String cc_goods_gradeid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("cc_name", cc_name);
        if (!lineId.equals("")) {
            params.put("lineId", lineId);
        }
        params.put("cc_id", cc_id);
        params.put("cc_longitude", cc_longitude);
        params.put("cc_latitude", cc_latitude);
        if (cc_net_address != null && !"".equals(cc_net_address)) {
            params.put("cc_net_address", cc_net_address);
        }
        params.put("cc_contacts_name", cc_contacts_name);
        params.put("cc_contacts_mobile", cc_contacts_mobile);

        params.put("cc_categoryid", cc_categoryid);
        params.put("cc_channelid", cc_channelid);
        params.put("cc_depotid", cc_depotid);//cc_depotid	仓库

        if (cc_goods_gradeid != null && !"".equals(cc_goods_gradeid)) {
            params.put("cc_goods_gradeid", cc_goods_gradeid);//	价格方案
        }
        params.put("cc_address",cc_address);
        params.put("cc_image",cc_image);
        LogUtils.e("updateLocation", params.toString());
        return params;
    }

    /**
     * 获取仓库列表接口
     *
     * @param customerId
     * @param sourceType 来源	N	1 要货 2.退货 3订单申报 4管理员端--1，2    主仓库且不包含自己的仓库
     * @return
     */
    public static RequestParams queryStorehouseList(String customerId, String isManage,
                                                    String sourceType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("customerId", customerId);
        params.put("rows", "1000");
        params.put("page", "1");
        if (!isManage.equals("")) {
            params.put("isManage", isManage);//isManage	是否是管理员	N   1是
        }
        params.put("sourceType",sourceType);
        return params;
    }

    /**
     * 获取业务员列表接口
     *
     * @param searchKey
     * @param salesmanType 类型	1全部  2业务员 3配送员
     * @return
     */
    public static RequestParams getSalesdocList(String searchKey, String salesmanType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("searchKey", searchKey);
        params.put("salesmanType", salesmanType);
        return params;
    }

    /**
     * 获取订单申报商品一二级分类接口
     *
     * @param userId
     * @param customerId 客户id
     * @param orderType  订单类型 1 普通 2 处理 3 退货 4 换货 5 还货
     * @param from       1普通 2 （传仓库）退货申请、车销出库 3 要货申请 4.客户盘点 5.代销 6订货
     * @param storeid    仓库id
     * @return
     */
    public static RequestParams queryGsCategory(String userId, String customerId, String orderType, String from, String storeid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("orderType", orderType);
        params.put("from", from);
        params.put("storeid", storeid);
        LogUtils.e("queryGsCategory",params.toString());
        return params;
    }

    /**
     * 获取还货商品一二级分类接口
     *
     * @param context
     * @param customerId 客户id
     * @param oa_businesstype  Y	1 代销 2 订货
     * @return
     */
    public static RequestParams queryApplyOrderGoods(Context context, String customerId,String oa_businesstype) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("customerId", customerId);
        params.put("oa_businesstype", oa_businesstype);
        return params;
    }

    /**
     * 二维码扫描商品
     *
     * @param userId
     * @param customerId
     * @param search_code
     * @param ggs_storehouseid
     * @return
     */
    public static RequestParams getGoodsDetail(String userId, String customerId, String search_code, String ggs_storehouseid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3004");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("search_code", search_code);
        params.put("ggs_storehouseid", ggs_storehouseid);
        LogUtils.e("getGoodsDetail", params.toString());
        return params;
    }


    /**
     * 获取商品列表
     *
     * @param userId
     * @param customerId            客户id
     * @param orderType             订单类型 1 普通 2 处理 3 退货 4 换货 5 还货
     * @param searchKey
     * @param gg_category_code_like
     * @param ggs_storehouseid      仓库id
     * @param from                  1普通 2 （传仓库）退货申请 3 要货申请 2车销出库 4客户盘点 5.代销 6订货 7.返陈列
     * @param type                  类型	N	4重点  5推荐 6我常买 7促销
     * @return
     */
    public static RequestParams getGoodsList(String userId, String customerId, String orderType, String searchKey,
                                             String gg_category_code_like, String ggs_storehouseid, String from,
                                             String page, String orderDate,String type) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3002");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("orderType", orderType);
        if (!searchKey.equals(""))
            params.put("searchKey", searchKey);
        params.put("gg_category_code_like", gg_category_code_like);
        params.put("ggs_storehouseid", ggs_storehouseid);
        params.put("from", from);
        //page	第几页	Y
        params.put("page", page);
        params.put("rows", "20");
        if (!"".equals(orderDate)) {
            params.put("orderDate", orderDate);//orderDate	日期	N
        }
        params.put("type",type);
        LogUtils.e("getGoodsList", params.toString());
        return params;
    }

    /**
     * 获取还货商品列表
     *
     * @param userId
     * @param customerId            客户id
     * @param orderType             订单类型 1 普通 2 处理 3 退货 4 换货 5 还货
     * @param searchKey
     * @param oa_id                 主键id
     * @return
     */
    public static RequestParams queryApplyOrderGoodsDetail(String userId, String customerId, String orderType, String searchKey,
                                             String oa_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3002");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("orderType", orderType);
        if (!searchKey.equals(""))
            params.put("searchKey", searchKey);
        params.put("oa_id",oa_id);
        LogUtils.e("queryApplyOrderGoodsDetail", params.toString());
        return params;
    }

    /**
     * 获取商品销售类型
     *
     * @param userId
     * @param om_ordertype 订单类型	Y	订单类型 订单类型 1 普通 2 处理 3 退货 4 换货 5 还货 6退货申请 7要货申请
     * @param customerId
     * @param type type 1 全部 2 铺货
     * @return
     */
    public static RequestParams getSaleTypeList(String userId, String om_ordertype, String customerId,String type) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3003");
        params.put("userId", userId);
        params.put("om_ordertype", om_ordertype);
        params.put("customerId", customerId);
        params.put("type",type);
        LogUtils.e("getSaleTypeList",params.toString());
        return params;
    }

    /**
     * 获取商品销售类型
     *
     * @param userId
     * @param om_ordertype 订单类型	Y	订单类型 订单类型 1 普通 2 处理 3 退货 4 换货 5 还货 6退货申请 7要货申请
     * @param customerId
     * @return
     */
    public static RequestParams getSaleTypeList(String userId, String om_ordertype, String customerId){
        return getSaleTypeList(userId,om_ordertype,customerId,"1");
    }

    /**
     * 获取商品退货类型数据
     * @return
     */
    public static RequestParams getGoodsStatusList() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3003");
        return params;
    }

    /**
     * 开通商城
     * @param context
     * @param customerId 客户id
     * @param mobile 电话
     * @param sms_code 短信验证码
     * @return
     */
    public static RequestParams openAccount(Context context,String customerId,String mobile,String sms_code){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1001");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("mobile", mobile);
        params.put("customerId", customerId);
        params.put("sms_code",sms_code);
        LogUtils.e("openAccount",params.toString());
        return params;
    }

    /**
     * 找回密码
     * @param context
     * @param customerId
     * @param mobile
     * @param type 短信类型	Y	1注册  2 找回 密码  3.开通商城
     * @return
     */
    public static RequestParams sendSms(Context context,String customerId,String mobile,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1001");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("mobile", mobile);
        params.put("customerId", customerId);
        params.put("type",type);
        return params;
    }

    /**
     * 生成订单接口
     *
     * @param userId          用户信息编号
     * @param customerId      客户id
     * @param om_deliverydate 订单发货日期	Y
     * @param om_storeid      发货仓库
     * @param om_ordertype    订单类型 1 普通 2 处理 3 退货 4 换货 5 还货
     * @param om_remarks      订单备注
     * @param orderDetailStr  订单详情字段
     * @return
     */
    public static RequestParams createOrUpdateOrder(String userId, String customerId, String om_deliverydate, String om_storeid,
                                                    String om_ordertype, String om_remarks, String orderDetailStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4000");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("om_deliverydate", om_deliverydate);
        params.put("om_storeid", om_storeid);
        params.put("om_ordertype", om_ordertype);
        //        params.put("om_ischages",om_ischages);
        params.put("om_remarks", om_remarks);
        params.put("orderDetailStr", orderDetailStr);
        LogUtils.e("createOrUpdateOrder", params.toString());
        return params;
    }

    public static RequestParams createOrUpdateListOrder(Context context,String customerId,String orderList,String lineid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4000");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("customerId", customerId);
        params.put("orderList", orderList);
        params.put("lineid",lineid);
        LogUtils.e("createOrUpdateListOrder",params.toString());
                /*orderList说明
        序号	字段	说明
        om_deliverydate	订单发货日期	Y
        om_storeid	发货仓库	Y
        om_ordertype	订单类型	Y
        om_ischages	是否变价	N   Y变价 N未变货
        om_remarks	订单备注	Y
        orderDetailStr	订单详情字段	Y
                orderDetailStr说明
        序号	字段	说明
        1	od_goods_price_id	商品价格主键
        2	od_pricetype	商品类型
        3	od_goods_num_min	小单位购买数量
        4	od_goods_num_max	大单位购买数量
        5	od_price_min	小单位价格
        6	od_price_max	大单位价格
        7	od_note	商品备注
        od_price_strategyid	价格方案id
        source_id	L来源id
        goodsStatus	商品状态*/
        return params;
    }

    /**
     * 查询订单列表
     *
     * @param customerId
     * @param start_date
     * @param end_date
     * @param from       来源	N	1普通2 任务列表
     * @return
     */
    public static RequestParams queryOrderGrid(String customerId, String start_date,
                                               String end_date, String from, String searchKey, String om_stats, String page,String isFromMain) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("customerId", customerId);
        params.put("from", from);
        params.put("start_date", start_date);
        params.put("end_date", end_date);
        params.put("searchKey", searchKey);//searchKey	关键字	N
        if (!"".equals(om_stats)) {
            params.put("om_stats", om_stats);
        }
        params.put("rows", "20");
        params.put("page", page);
        params.put("isFromMain",isFromMain);
        Log.e("queryOrderGrid", params.toString());
        return params;
    }

    /**
     * 订单详情
     *
     * @param userId
     * @param om_id  订单id
     * @return
     */
    public static RequestParams getOrderDetail(String userId, String om_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("userId", userId);
        params.put("om_id", om_id);
        Log.e("getOrderDetail", params.toString());
        return params;
    }

    /**
     * 复制订单
     *
     * @param userId
     * @param om_id  订单id
     * @return
     */
    public static RequestParams copyOrder(String userId, String om_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4003");
        params.put("userId", userId);
        params.put("om_id", om_id);
        Log.e("copyOrder", params.toString());
        return params;
    }

    /**
     * 订货列表查询
     * @param oa_businesstype 1 代销 2 订货
     * @return
     */
    public static RequestParams querySalesOrderApply(String oa_businesstype, String start_time,String end_time) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4003");
        params.put("oa_businesstype", oa_businesstype);
        params.put("start_time",start_time);
        params.put("end_time",end_time);
        Log.e("querySalesOrderApply", params.toString());
        return params;
    }

    /**
     * 订货明细详情
     * @param oa_id 订货id
     * @return
     */
    public static RequestParams querySalesOrderApplyDetail(String oa_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4003");
        params.put("oa_id", oa_id);
        Log.e("querySalesOrderApply", params.toString());
        return params;
    }

    /**
     * 退货申请订单申请
     *
     * @param userId
     * @param eim_other_storeid 仓库
     * @param eim_totalamount   总金额
     * @param erpInoroutItemStr 数据详情
     * @return
     */
    public static RequestParams addRefund(String userId, String eim_other_storeid, String eim_totalamount,
                                          String erpInoroutItemStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4003");
        params.put("userId", userId);
        params.put("eim_other_storeid", eim_other_storeid);
        params.put("eim_totalamount", eim_totalamount);
        params.put("erpInoroutItemStr", erpInoroutItemStr);
        Log.e("addRefund", params.toString());
        return params;
    }

    /**
     * 临期 残次 过期 退货正常
     *
     * @param userId
     * @param eim_other_storeid 仓库
     * @param eim_totalamount   总金额
     * @param erpInoroutItemStr 数据详情
     * @return
     */
    public static RequestParams addRefundByGoodsStatus(String userId, String eim_other_storeid, String eim_totalamount,
                                          String erpInoroutItemStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4003");
        params.put("userId", userId);
        params.put("eim_other_storeid", eim_other_storeid);
        params.put("eim_totalamount", eim_totalamount);
        params.put("erpInoroutItemStr", erpInoroutItemStr);
        Log.e("addRefund", params.toString());
        return params;
    }

    /**
     * 要货申请
     *
     * @param userId
     * @param eim_other_storeid 仓库
     * @param eim_totalamount   总金额
     * @param erpInoroutItemStr 数据详情
     * @return
     */
    public static RequestParams addRequireGoods(String userId, String eim_other_storeid, String eim_totalamount,
                                                String erpInoroutItemStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4003");
        params.put("userId", userId);
        params.put("eim_other_storeid", eim_other_storeid);
        params.put("eim_totalamount", eim_totalamount);
        params.put("erpInoroutItemStr", erpInoroutItemStr);
        Log.e("addRefund", params.toString());
        return params;
    }

    /**
     * 获取商品全部接口
     *
     * @param searchKey
     * @return
     */
    public static RequestParams getAllGoodsList(String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("searchKey", searchKey);
        return params;
    }

    /**
     * 配送任务
     *
     * @param userId
     * @param isDelivery         是否配送	Y	1 是  2未配送
     * @param deliverydate_start 开始时间
     * @param searchKey          关键字	N
     * @param customerId         客户id
     * @return
     */
    public static RequestParams getTaskList(String userId, String isDelivery, String deliverydate_start, String searchKey,
                                            String lng, String lat, String customerId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("userId", userId);
        params.put("isDelivery", isDelivery);
        params.put("deliverydate_start", deliverydate_start);
        params.put("searchKey", searchKey);
        params.put("lng", lng);//经度	Y
        params.put("lat", lat);//纬度	Y
        if (!customerId.equals("")) {
            params.put("customerId", customerId);
        }

        LogUtils.e("getTaskList", params.toString());
        return params;
    }

    /**
     * 获取主仓库库存
     *
     * @param userId
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams getMainStockGoods(String userId, String page, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("userId", userId);
        params.put("page", page);
        params.put("searchKey", searchKey);
        LogUtils.e("getMainStockGoods", params.toString());
        return params;
    }

    /**
     * 获取车仓库库存
     *
     * @param userId
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams getStockGoods(String userId, String storehouseid, String page, String searchKey, String type) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("userId", userId);
        params.put("storehouseid", storehouseid);
        params.put("page", page);
        params.put("searchKey", searchKey);
        params.put("type", type);//type	是否有0库存		 1 有  2无
        LogUtils.e("getStockGoods", params.toString());
        return params;
    }

    /**
     * 获取支付方式列表
     *
     * @return
     */
    public static RequestParams getPayTypeList(String userId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("userId", userId);
        return params;
    }

    /**
     * 获取商品品牌列表
     * @param orderDetailStr 商品字段
     * @return
     */
    //    public static RequestParams getBrandAmountByGoods(String userId,String customerId,String om_ordertype,String orderDetailStr){
    //        RequestParams params = new RequestParams();
    //        setCommonParams(params,"4008");
    //        params.put("userId",userId);
    //        params.put("customerId",customerId);
    //        params.put("om_ordertype",om_ordertype);
    //        params.put("orderDetailStr",orderDetailStr);
    //        LogUtils.e("getBrandAmountByGoods",params.toString());
    //        return params;
    //    }

    /**
     * 车销出库结算接口
     *
     * @param userId
     * @param customerId
     * @param om_ordertype
     * @param orderDetailStr
     * @param orderBrandStr
     * @param payType
     * @param skType
     * @param skAmount
     * @param yhAmount
     * @param xjAmount
     * @return
     */
    public static RequestParams createCarOut(String userId, String customerId, String om_ordertype, String orderDetailStr,
                                             String orderBrandStr, String payType, String skType, String skAmount, String yhAmount, String xjAmount,
                                             String yeAmount, String ysAmount) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("om_ordertype", om_ordertype);
        params.put("orderDetailStr", orderDetailStr);
        params.put("orderBrandStr", orderBrandStr);
        params.put("payType", payType);//支付方式	Y	1 混合收款 2优先现款 3优先预收款 4优先应收款
        params.put("skType", skType);//刷卡方式	Y	1不使用刷卡2非系统集成刷卡3银联商务4支付宝支付5微信支付6第三方
        params.put("skAmount", skAmount);//	刷卡金额	Y
        params.put("yhAmount", yhAmount);//	优惠金额	Y
        params.put("xjAmount", xjAmount);//	现金	Y
        params.put("yeAmount", yeAmount);//余额
        params.put("ysAmount", ysAmount);//应收
        LogUtils.e("createCarOut", params.toString());
        return params;
    }

    /**
     * 车销多订单生成接口
     * @param userId
     * @param customerId
     * @param cartOutlist 现金收支list
     * @param imgStr 签名图片
     * @return
     */
    public static RequestParams createListCarOut(String userId, String customerId,String cartOutlist,String imgStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("cartOutlist",cartOutlist);
        if (imgStr!=null && imgStr.length()>0){
            params.put("imgStr",imgStr);
        }
        //cartOutlist的列表参数
//        om_ordertype	订单类型	Y
//        orderDetailStr	订单详情字段	Y
//        orderBrandStr	商品品牌字段	Y
//        payType	支付方式	Y	1 混合收款 2优先现款 3优先预收款 4优先应收款
//        skType	刷卡方式	Y	1不使用刷卡2非系统集成刷卡3银联商务4支付宝支付5微信支付6第三方
//        skAmount	刷卡金额	Y
//        yhAmount	优惠金额	Y
//        xjAmount	预收金额	Y
        LogUtils.e("createListCarOut", params.toString());
        return params;
    }

    /**
     * 生成铺货销售
     * @param om_ordertype 	订单类型	Y	1 普通 2 处理 3 退货 4 换货 5 还货 6铺货销售
     * @param orderDetailStr
     * @param orderBrandStr
     * @param payType
     * @param skType
     * @param skAmount
     * @param yhAmount
     * @param xjAmount
     * @param yeAmount
     * @param ysAmount
     * @return
     */
    public static RequestParams createPhSaleOrder(String customerId,String om_ordertype,String orderDetailStr,String orderBrandStr,String payType,
                                                  String skType,String skAmount,String yhAmount,String xjAmount,
                                                  String yeAmount, String ysAmount,String imgStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("customerId",customerId);
        params.put("om_ordertype", om_ordertype);
        params.put("orderDetailStr", orderDetailStr);
        params.put("orderBrandStr",orderBrandStr);
        params.put("payType",payType);
        params.put("skType",skType);
        params.put("skAmount",skAmount);
        params.put("yhAmount",yhAmount);
        params.put("xjAmount",xjAmount);
        params.put("yeAmount", yeAmount);//余额
        params.put("ysAmount", ysAmount);//应收
        if (imgStr!=null && imgStr.length()>0){
            params.put("imgStr",imgStr);
        }
//        yeAmount	所用金额
//        ysAmount	应收金额
        LogUtils.e("createPhSaleOrder",params.toString());
        return params;
    }

    /**
     * 铺货退货
     * @param customerId
     * @param orderDetailStr
     * @return
     */
    public static RequestParams insertRefundPhGoods(String customerId,String orderDetailStr,String oa_remarks){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("customerId",customerId);
        params.put("orderDetailStr",orderDetailStr);
        params.put("oa_remarks",oa_remarks);
        LogUtils.e("insertRefundPhGoods",params.toString());
        return params;
    }

    /**
     * 铺货申报任务列表
     *
     * @param userId
     * @param isDelivery         是否配送	Y	1 是  2未配送
     * @param deliverydate_start 开始时间
     * @param searchKey          关键字	N
     * @param customerId         客户id
     * @return
     */
    public static RequestParams getPhTaskList(String userId, String isDelivery, String deliverydate_start, String searchKey,
                                            String lng, String lat, String customerId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("userId", userId);
        params.put("isDelivery", isDelivery);
        params.put("deliverydate_start", deliverydate_start);
        params.put("searchKey", searchKey);
        params.put("lng", lng);//经度	Y
        params.put("lat", lat);//纬度	Y
        if (!customerId.equals("")) {
            params.put("customerId", customerId);
        }
        LogUtils.e("getTaskList", params.toString());
        return params;
    }

    /**
     * 铺货任务详情列表
     *
     * @return
     */
    public static RequestParams getTaskPhDetail(String customerId, String salesid, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("customerId", customerId);
        params.put("salesid", salesid);
        params.put("searchKey", searchKey);
        LogUtils.e("getStockOutDetail", params.toString());
        return params;
    }

    /**
     * 任务详情列表
     *
     * @return
     */
    public static RequestParams getTaskDetail(String userId, String customerId, String salesid, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("salesid", salesid);
        params.put("searchKey", searchKey);
        LogUtils.e("getStockOutDetail", params.toString());
        return params;
    }

    /**
     * 配送任务结算接口
     *
     * @param userId
     * @param customerId
     * @param om_ordertype
     * @param orderListStr  订单	Y
     * @param orderBrandStr 商品品牌字段	Y
     * @param payType       支付方式	Y	1 混合收款 2优先现款 3优先预收款 4优先应收款
     * @param skType        刷卡方式	Y	1不使用刷卡2非系统集成刷卡3银联商务4支付宝支付5微信支付6第三方
     * @param skAmount      刷卡金额	Y
     * @param yhAmount      优惠金额	Y
     * @param xjAmount      预收金额	Y
     * @return
     */
    public static RequestParams deliveryPaymentTaskOrder(String userId, String customerId, String om_ordertype, String orderListStr,
                                                         String orderBrandStr, String payType, String skType, String skAmount, String yhAmount, String xjAmount
            , String yeAmount, String ysAmount) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("om_ordertype", om_ordertype);
        params.put("orderListStr", orderListStr);
        params.put("orderBrandStr", orderBrandStr);
        params.put("payType", payType);
        params.put("skType", skType);
        params.put("skAmount", skAmount);//	刷卡金额	Y
        params.put("yhAmount", yhAmount);//	优惠金额	Y
        params.put("xjAmount", xjAmount);//	现金	Y
        params.put("yeAmount", yeAmount);//余额
        params.put("ysAmount", ysAmount);//应收
        LogUtils.e("deliveryPaymentTaskOrder", params.toString());
        return params;
    }

    /**
     * 出库查询列表接口
     *
     * @param userId
     * @return
     */
    public static RequestParams getStockOutList(String userId, String customerId, String eim_type, String page,
                                                String startDate, String endDate, String searchKey) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("eim_type", eim_type);
        params.put("page", page);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isRed", "N");
        params.put("searchKey", searchKey);
        //        eim_type	单据类型	Y	单据类型 101 进货 102期初入库 103其它入库 104 进货退货 105 换货入库 106 移库入库
        //        201 销售出库 202 还货出库 203处理出库 204 其它出库 301调拨 302要货审核 303退货审核 304换货
        //        305 撤销配货 306 拆装出库 501 报废出库 601 退货入库
        LogUtils.e("getStockOutList", params.toString());
        return params;
    }

    /**
     * 出库查询详情
     *
     * @return
     */
    public static RequestParams getStockOutDetail(String userId, String eim_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "3001");
        params.put("userId", userId);
        params.put("eim_id", eim_id);//单据id	Y
        return params;
    }

    /**
     * 查询品牌列表
     *
     * @param userId
     * @return
     */
    public static RequestParams queryBaseBrand(String userId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        return params;
    }

    /**
     * 根据查询客户的品牌欠款
     *
     * @param userId
     * @param customerId 客户id
     * @param brandId    品牌id
     * @return
     */
    public static RequestParams queryCusBrandBalance(String userId, String customerId, String brandId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("brandId", brandId);
        return params;
    }

    /**
     * 客户收款提交接口
     *
     * @param cardBaseItemid 第三方支付方式参数
     * @param cashAmount 现金
     * @param cardAmount 刷卡
     * @param disCountAmount 优惠
     * @return
     */
    public static RequestParams makeCollections(String userId, String customerId, String remarks, String cardBaseItemid,
                                                String cashAmount,String cardAmount,String disCountAmount) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("customerId", customerId);
        params.put("remarks", remarks);
        params.put("cardBaseItemid", cardBaseItemid);
        params.put("cashAmount",cashAmount);
        params.put("cardAmount",cardAmount);
        params.put("disCountAmount",disCountAmount);
        LogUtils.e("makeCollections", params.toString());
        return params;
    }

    /**
     * 收取客户欠款
     * @param context
     * @param customerId
     * @param xjAmount 现金
     * @param skAmount 刷卡
     * @param disCountAmount 优惠
     * @param note 备注
     * @param skBaseItemId 刷卡时的id
     * @param glReceivableListStr 收欠款
     * @return
     */
    public static RequestParams makeReceiveYsk(Context context, String customerId,String xjAmount,String skAmount,
                                               String disCountAmount,String note,String skBaseItemId,String glReceivableListStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
            params.put("userId", UserInfoUtils.getId(context));
        params.put("customerId", customerId);
        params.put("xjAmount", "".equals(xjAmount)?"0.00":xjAmount);
        params.put("skAmount", "".equals(skAmount)?"0.00":skAmount);
        params.put("disCountAmount",disCountAmount);
        params.put("note", note);
        params.put("skBaseItemId",skBaseItemId);
        params.put("glReceivableListStr",glReceivableListStr);
        LogUtils.e("makeReceiveYsk", params.toString());
        return params;
    }

    /**
     * 现金日报接口
     *
     * @param userId
     * @param queryDate
     * @return
     */
    public static RequestParams queryCashReport(String userId, String queryDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("salesid", userId);
        params.put("queryDate", queryDate);
        LogUtils.e("queryCashReport", params.toString());
        return params;
    }

    /**
     * 收付款明细查询
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams queryCashList(String userId, String startDate, String endDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("salesid", userId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        LogUtils.e("queryCashReport", params.toString());
        return params;
    }

    /**
     * 收付款明细
     * @param userId
     * @param customerId
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams queryCashDetailList(String userId, String customerId, String startDate,
                                                    String endDate,String page,String rows) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("salesid", userId);
        params.put("customerId", customerId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("page",page);
        params.put("rows",rows);
        LogUtils.e("queryCashDetailList", params.toString());
        return params;
    }

    /**
     * 取消收款单
     * @param gc_id
     * @return
     */
    public static RequestParams cancelGather(String gc_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("gc_id", gc_id);
        LogUtils.e("cancelGather", params.toString());
        return params;
    }

    /**
     * 代销,订货列表查询
     * @param context
     * @param oa_businesstype 1 代销 2 订货
     * @param oa_customerid
     * @return
     */
    public static RequestParams queryApplyOrder(Context context,String oa_businesstype, String oa_customerid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("oa_businesstype", oa_businesstype);
        params.put("oa_customerid", oa_customerid);
        LogUtils.e("queryApplyOrder", params.toString());
        return params;
    }

    /**
     * 查询刷卡时的项目表
     */
    public static RequestParams queryBankItemList(String userId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        return params;
    }

    /**
     * 单据补打列表
     *
     * @param userId
     * @param date_str
     * @param type     1订单  2.收付款 3出入库
     * @return
     */
    public static RequestParams printList(String userId, String date_str, String type) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("date_str", date_str);
        params.put("type", type);
        return params;
    }

    /**
     * 订单单据打印
     *
     * @param userId
     * @param om_id  订单id
     * @return
     */
    public static RequestParams printOrder(String userId, String om_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("om_id", om_id);
        return params;
    }

    /**
     * 收付款打印
     *
     * @param userId
     * @param gc_id
     * @return
     */
    public static RequestParams printGlCash(String userId, String gc_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("gc_id", gc_id);
        return params;
    }

    /**
     * 业务员拜访提醒接口
     *
     * @param userId
     * @param page
     * @param searchKey
     * @param alarmType 类型	Y	1超期 2 提醒
     * @param salesid 业务员id
     * @param  phType	铺货状态 	Y	0全部 1已铺货 2铺货
     * @return
     */
    public static RequestParams getCsAlarmList(String userId, String page, String searchKey, String alarmType,String salesid,String phType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("page", page);
        params.put("searchKey", searchKey);
        params.put("rows", "10");
        params.put("alarmType", alarmType);
        params.put("salesid",salesid);
        params.put("phType",phType);
        LogUtils.e("getCsAlarmList", params.toString());
        return params;
    }

    /**
     * 业务员查看自己的拜访记录
     *
     * @param userId
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getCsTaskList(String userId, String page, String startDate, String endDate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "2002");
        params.put("userId", userId);
        params.put("page", page);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("rows", "10");
        return params;
    }

    /**
     * 修改密码
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    public static RequestParams updatePwd(String oldPwd, String newPwd) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("oldPwd", MD5Util.toMD5_32(oldPwd));
        params.put("newPwd", MD5Util.toMD5_32(newPwd));
        return params;
    }

    public static RequestParams resetPwd(String mobile,String sms_code,String newPwd){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1002");
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        params.put("newPwd",MD5Util.toMD5_32(newPwd));
        Log.e("reset:",params.toString());
        return params;
    }

    /**
     * 消息列表接口
     *
     * @param salesdocId 员工id
     * @param page       页数
     * @return
     */
    public static RequestParams queryUserMessage(String salesdocId, String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("salesdocId", salesdocId);
        params.put("page", page);
        return params;
    }

    /**
     * 读取消息状态接口
     *
     * @param readType   1 单条 2 全部
     * @param salesdocId 员工id
     * @param msgId      readType 为1 必传
     * @return
     */
    public static RequestParams readMessage(String readType, String salesdocId, String msgId) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("readType", readType);
        params.put("salesdocId", salesdocId);
        if ("1".equals(readType)) {
            params.put("msgId", msgId);
        }
        return params;
    }

    /**
     * 生成商品结算信息接口
     *
     * @param customerId
     * @param om_storeid
     * @param om_ordertype
     * @param type           1订单申报 2 车销
     * @param orderDetailStr
     * @return
     */
    public static RequestParams bulidSettlementOrder(String customerId, String om_storeid, String om_ordertype, String type, String orderDetailStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("customerId", customerId);
        params.put("om_storeid", om_storeid);
        params.put("om_ordertype", om_ordertype);
        params.put("type", type);
        params.put("orderDetailStr", orderDetailStr);
        return params;
    }

    /**
     * 生成商品结算信息接口
     *
     * @param customerId
     * @param orderList
     * @param type           1订单申报 2 车销
     * @return
     */
    public static RequestParams bulidSettlementListOrder(Context context,String customerId, String orderList,String om_storeid, String type) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1002");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("customerId", customerId);
        params.put("orderList", orderList);
        params.put("om_storeid", om_storeid);
        params.put("type", type);
        LogUtils.e("bulidSettlementListOrder",params.toString());
        return params;
    }

    /**
     * 配送任务结算接口
     *
     * @param customerId
     * @param om_ordertype
     * @param orderListStr 订单	Y
     * @return
     */
    public static RequestParams deliverySettlementOrder(String customerId, String om_ordertype, String orderListStr) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("customerId", customerId);
        params.put("om_ordertype", om_ordertype);
        params.put("orderListStr", orderListStr);
        LogUtils.e("deliverySettlementOrder", params.toString());
        return params;
    }

    /**
     * 订单状态列表
     *
     * @return
     */
    public static RequestParams getOrderStatsList() {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4007");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        LogUtils.e("getOrderStatsList", params.toString());
        return params;
    }

    /**
     * 订单拒收
     * @param context
     * @param om_id 订单编号
     * @param om_version 订单版本号
     * @return
     */
    public static RequestParams updateRejectionOrder(Context context,String om_id, String om_version){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("userId", UserInfoUtils.getId(context));
        params.put("om_id", om_id);
        params.put("om_version", om_version);
        LogUtils.e("updateRejectionOrder", params.toString());
        return params;
    }

    /**
     * 订单拒收
     * @param oa_id 订单id
     * @param oa_customerid 客户id
     * @return
     */
    public static RequestParams cancelApplyOrder(String oa_id, String oa_customerid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("oa_id", oa_id);
        params.put("oa_customerid", oa_customerid);
        LogUtils.e("updateRejectionOrder", params.toString());
        return params;
    }

    /**
     * 撤消订单
     *
     * @param om_id      订单编号
     * @param om_version 订单版本号
     * @return
     */
    public static RequestParams updateCancelOrder(String om_id, String om_version) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("om_id", om_id);
        params.put("om_version", om_version);
        LogUtils.e("cancelOrder", params.toString());
        return params;
    }

    /**
     * 订货撤消订单
     *
     * @param oa_id      订单编号
     * @param oa_version 订单版本号
     * @return
     */
    public static RequestParams cancelOrderDh(String oa_id, String oa_version) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("oa_id", oa_id);
        params.put("oa_version", oa_version);
        return params;
    }

    /**
     * 撤消出库订单
     *
     * @param eim_id      出库id
     * @param eim_version 订单版本号
     * @return
     */
    public static RequestParams updateCancelErp(String eim_id, String eim_version,String orgid) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("eim_id", eim_id);
        params.put("eim_version", eim_version);
        params.put("orgid",orgid); //版本号
        LogUtils.e("cancelOrder", params.toString());
        return params;
    }

    /**
     * 获取客户反馈列表
     *
     * @param page
     * @return
     */
    public static RequestParams getFeedbackList(String page) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("rows", "10");
        params.put("page", page);
        LogUtils.e("getFeedbackList", params.toString());
        return params;
    }

    /**
     * 回复客户反馈接口
     * @param cf_id 反馈id
     * @param note 回复内容
     * @return
     */
    public static RequestParams updateFeedbackNote(String cf_id,String note){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("cf_id", cf_id);
        params.put("note", note);
        LogUtils.e("getFeedbackList", params.toString());
        return params;
    }

    /**
     * 客户欠款列表
     * @param rows
     * @param page
     * @param customerId
     * @return
     */
    public static RequestParams getGlReceivableList(String rows,String page,String customerId,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("rows", rows);
        params.put("page", page);
        params.put("customerId",customerId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        LogUtils.e("getGlReceivableList", params.toString());
        return params;
    }

    /**
     * 得到客户应收款汇总
     * @param rows
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getReceivableSumList(String rows,String page, String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("rows", rows);
        params.put("page", page);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        LogUtils.e("getReceivableSumList", params.toString());
        return params;
    }

    /**
     * 获取业务员用户的余额和安全欠款
     * @return
     */
    public static RequestParams queryCusBalance(){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4009");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        return params;
    }

    /**
     * 订货、代销
     * @param oa_businesstype 业务模式	Y	1 代销 2 订货
     * @param oa_customerid 客户id
     * @param oa_storeid 发货仓库
     * @param oa_remarks 备注
     * @param skType 刷卡方式
     * @param skAmount 刷卡金额
     * @param yhAmount 优惠金额
     * @param xjAmount 现金金额
     * @param yeAmount 余额
     * @param orderApplyDetailStr 申请明细集合
     * @return
     */
    public static RequestParams applyOrder(String oa_businesstype,String oa_customerid,String oa_storeid,
                                           String oa_remarks,String skType,String skAmount,String yhAmount,String xjAmount,
                                           String yeAmount,String orderApplyDetailStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "4001");
        params.put("oa_businesstype", oa_businesstype);
        params.put("oa_customerid", oa_customerid);
        params.put("oa_storeid", oa_storeid);
        params.put("oa_remarks",oa_remarks);
        params.put("skType", skType);
        params.put("skAmount",skAmount);
        params.put("yhAmount",yhAmount);
        params.put("xjAmount",xjAmount);
        params.put("yeAmount",yeAmount);
        params.put("orderApplyDetailStr",orderApplyDetailStr);
        LogUtils.e("applyOrder",params.toString());
        return params;
    }

    /**
     * 扫码核销获取奖品和用户信息
     *
     * @param mw_prizecode 中奖编码
     * @return
     */
    public static RequestParams getGiftPrizeDetailByCode(String mw_prizecode) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("mw_prizecode", mw_prizecode);
        LogUtils.e("getGiftPrizeDetailByCode", params.toString());
        return params;
    }

    /**
     * 扫码核销获取奖品和用户信息
     * @param mw_id 中奖id
     * @return
     */
    public static RequestParams checkGiftWinning(String mw_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("mw_id", mw_id);
        LogUtils.e("checkGiftWinning", params.toString());
        return params;
    }

    public static RequestParams getGiftWinningList(String start_time,String  end_time,String page){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("start_time", start_time);
        params.put("end_time", end_time);
        params.put("page", page);
        LogUtils.e("getGiftWinningList", params.toString());
        return params;
    }

    /**
     * 获取会员 核销员列表
     * @param page
     * @return
     */
    public static RequestParams getMemberList(String page,String searchKey,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("page", page);
        params.put("searchKey",searchKey);
        params.put("type",type); //2全部 1其他
        LogUtils.e("getMemberList", params.toString());
        return params;
    }

    /**
     * 获取设备列表
     * @param page
     * @param cc_id
     * @return
     */
    public static RequestParams getEquipmentList(String searchKey,String page,String cc_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("searchKey", searchKey);
        params.put("page", page);
        params.put("cc_id", cc_id);
        LogUtils.e("getGiftWinningList", params.toString());
        return params;
    }

    /**
     * 店铺绑定设备和核销员添加接口
     * @param addIdStr 新增的设备ids
     * @param delIdsStr 删除的设备ids
     * @param mb_id 会员id（核销员）
     * @return
     */
    public static RequestParams relatedEqpAndMangerUser(String addIdStr, String delIdsStr, String mb_id, ClientBean clientBean){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("addIdStr", addIdStr);
        params.put("delIdsStr", delIdsStr);
        params.put("mb_id", mb_id);//核销员默认不传

        params.put("cc_id", clientBean.getCc_id());
        params.put("cc_name", clientBean.getCc_name());
        params.put("cc_address", clientBean.getCc_address());
        params.put("cc_contacts_mobile", clientBean.getCc_contacts_mobile());
        params.put("cc_latitude", clientBean.getCc_latitude());
        params.put("cc_longitude", clientBean.getCc_longitude());
        params.put("cc_image",clientBean.getCc_image());
        LogUtils.e("relatedEqpAndMangerUser", params.toString());
        return params;
    }

    /**
     * 获取客户线路列表
     * @param customerId
     * @return
     */
    public static RequestParams getCustomerLines(String customerId){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("customerId", customerId);
        LogUtils.e("getCustomerLines", params.toString());
        return params;
    }

    /**
     *  更改客户线路
     * @param customerId
     * @param lineIds 线路id 英文逗号拼接
     * @return
     */
    public static RequestParams updateCustomerLines(String customerId,String lineIds){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("customerId", customerId);
        params.put("lineIds", lineIds);
        LogUtils.e("getEquipmentRelatedDetail", params.toString());
        return params;
    }

    /**
     * 获取店铺关联设备详情
     * @param page
     * @param cc_id
     * @return
     */
    public static RequestParams getEquipmentRelatedDetail(String page,String cc_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("page", page);
        params.put("cc_id", cc_id);
        LogUtils.e("getEquipmentRelatedDetail", params.toString());
        return params;
    }

    /**
     * 店铺结算汇总
     * @param startdate
     * @param enddate
     * @param customerId
     * @return
     */
    public static RequestParams queryJsSum(String startdate,String enddate,String customerId){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("startdate", startdate);
        params.put("enddate",enddate);
        params.put("customerId", customerId);
        LogUtils.e("queryJsSum", params.toString());
        return params;
    }

    /**
     * 业务核销列表
     * @param startdate
     * @param enddate
     * @param customerId 小店id
     * @param page
     * @param rows
     * @param mw_state 1未核销 2已核销
     * @param mw_prizeid
     * @param shopid
     * @return
     */
    public static RequestParams queryHxList(String startdate,String enddate,String customerId,String page,String rows,
                                            String mw_state,String mw_prizeid,String shopid,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("startdate", startdate);
        params.put("enddate",enddate);
        params.put("customerId", customerId);
        params.put("page", page);
        params.put("rows",rows);
        params.put("mw_state", mw_state);
        params.put("mw_prizeid",mw_prizeid);
        params.put("shopid",shopid);
        params.put("searchKey",searchKey);
        LogUtils.e("queryJsSum", params.toString());
        return params;
    }

    /**
     * 业务员批量结算
     * @param mw_id_str 数据id 逗号分隔
     * @return
     */
    public static RequestParams updateCheckState(String mw_id_str){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("mw_id_str", mw_id_str);
        LogUtils.e("updateCheckState", params.toString());
        return params;
    }

    /**
     * 店铺结算每个商品的详情列表
     * @param startdate
     * @param enddate
     * @param customerId 店铺id
     * @param page
     * @param rows
     * @param mw_check_state 1未结算 2已结算
     * @param mw_productname 商品名
     * @param searchKey
     * @return
     */
    public static RequestParams queryJsDetailList(String startdate,String enddate,String customerId,String page,String rows,
                                                  String mw_check_state,String mw_productname,String mw_prizeid,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("startdate", startdate);
        params.put("enddate",enddate);
        params.put("customerId", customerId);
        params.put("page", page);
        params.put("rows",rows);
        params.put("mw_check_state", mw_check_state);
        params.put("mw_productname",mw_productname);
        params.put("mw_prizeid",mw_prizeid);//mw_prizeid	奖品id
        params.put("searchKey",searchKey);
        LogUtils.e("queryJsSum", params.toString());
        return params;
    }

    /**
     * 获取已关联设备的店铺列表
     * @param page
     * @return
     */
    public static RequestParams getShopList(String page,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("page", page);
        params.put("searchKey",searchKey);
        LogUtils.e("getShopList", params.toString());
        return params;
    }

    /**
     * 修改店铺的核销密码
     * @param cc_id
     * @param hxPassword
     * @return
     */
    public static RequestParams setUpHxPwd(String cc_id,String hxPassword){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("cc_id", cc_id);
        params.put("hxPassword",hxPassword);
        LogUtils.e("setUpHxPwd", params.toString());
        return params;
    }

    /**
     * 根据客户手机号获取客户列表
     * @param mobile
     * @param customerId
     * @return
     */
    public static RequestParams getCustomerListByMobile(String mobile,String customerId){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("mobile", mobile);
        params.put("customerId",customerId);
        LogUtils.e("getCustomerListByMobile", params.toString());
        return params;
    }

    /**
     * 陈列商品出库
     * @param customerId
     * @param erpInoroutItemStr
     * @return
     */
    public static RequestParams displayGoodsOutStore(String customerId,String erpInoroutItemStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("customerId", customerId);
        params.put("erpInoroutItemStr",erpInoroutItemStr);
        LogUtils.e("displayGoodsOutStore", params.toString());
        return params;
    }

    /**
     * 陈列出库查询列表
     * @param type 1.业务员;2.管理员
     * @param salesid 业务员id(type=1时,传空)
     * @param customerId
     * @param startDate
     * @param endDate
     * @param page
     * @param rows
     * @param searchKey
     * @return
     */
    public static RequestParams getDisplayInList(String type,String salesid,String customerId,String startDate,String endDate,
                                                 String page,String rows,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("type",type);
        params.put("salesid",salesid);
        params.put("customerId", customerId);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page", page);
        params.put("rows",rows);
        params.put("searchKey",searchKey);
        LogUtils.e("getDisplayInList", params.toString());
        return params;
    }

    /**
     * 陈列详情
     * @param customerId
     * @param eim_id 单据id
     * @return
     */
    public static RequestParams getDisplayInDetail(String customerId,String eim_id){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("customerId", customerId);
        params.put("eim_id",eim_id);
        LogUtils.e("getDisplayInDetail", params.toString());
        return params;
    }

    /**
     * 铺货商品分类列表
     * @param customerId 客户id
     * @param type  1铺货 2 销售 3 退货
     * @param storeid 仓库id
     * @return
     */
    public static RequestParams phGsCategory(String customerId,String type,String storeid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("customerId", customerId);
        params.put("type",type);
        params.put("storeid", storeid);
        LogUtils.e("phGsCategory", params.toString());
        return params;
    }

    /**
     * 铺货店铺列表
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams phShopList(String page,String searchKey){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("page", page);
        params.put("searchKey",searchKey);
        LogUtils.e("phShop", params.toString());
        return params;
    }

    /**
     * 铺货下单
     * @param oa_customerid
     * @param oa_remarks
     * @param imgStr 签名图片
     * @param orderApplyDetailStr
     * @param lineid 线路id
     * @return
     */
    public static RequestParams distributionOrder(String oa_customerid,String oa_remarks,String imgStr,String orderApplyDetailStr,String lineid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("oa_customerid", oa_customerid);
        params.put("oa_remarks",oa_remarks);
        if (imgStr!=null && imgStr.length()>0){
            params.put("imgStr",imgStr);
        }
        params.put("orderApplyDetailStr",orderApplyDetailStr);
        params.put("lineid",lineid);
        LogUtils.e("phGsCategory", params.toString());
        return params;
    }

    /**
     * 铺货申报下单
     * @param oa_customerid 客户id
     * @param oa_remarks 备注
     * @param oa_deliverydate	配送时间
     * @param orderApplyDetailStr 申请明细集合
     * @return
     */
    public static RequestParams insertApplyPhOrder(String oa_customerid,String oa_remarks,String oa_deliverydate,
                                                   String orderApplyDetailStr,String storeid,String lineid){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("oa_customerid", oa_customerid);
        params.put("oa_remarks",oa_remarks);
        params.put("oa_deliverydate",oa_deliverydate);
        params.put("orderApplyDetailStr",orderApplyDetailStr);
        params.put("oa_storeid",storeid);
        params.put("lineid",lineid);
        LogUtils.e("phGsCategory", params.toString());
        return params;
    }

    /**
     * 铺货任务配送
     * @param oa_customerid 客户id
     * @param oa_remarks 备注
     * @param oa_id 订单id
     * @return
     */
    public static RequestParams deliverPhOrder(String oa_customerid,String oa_remarks,
                                                   String oa_id,String oa_version,String imgStr){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("oa_customerid", oa_customerid);
        params.put("oa_remarks",oa_remarks);
        params.put("oa_id",oa_id);
        if (imgStr!=null && imgStr.length()>0){
            params.put("imgStr",imgStr);
        }
        params.put("oa_version",oa_version);
        LogUtils.e("phGsCategory", params.toString());
        return params;
    }

    /**
     * 铺货商品详情列表
     * @param page
     * @param customerId
     * @param type type:0业务员端 type:1 管理端
     * @return
     */
    public static RequestParams phGoodsDetailList(String page,String customerId,String type,String searchKey,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("page", page);
        params.put("customerId",customerId);
        params.put("type",type);
        params.put("searchKey",searchKey);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("phGoodsDetail:", params.toString());
        return params;
    }


    /**
     * 铺货列表按订单查询
     * @param customerId 客户id
     * @param type N:不显示已完结订单
     * @return
     */
    public static RequestParams phOrderDetailList(String customerId,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("customerId", customerId);
        params.put("type",type);
        LogUtils.e("phGsCategory", params.toString());
        return params;
    }

    /**
     * 根据铺货单查询销售单/撤货单
     * @param orderId 铺货单id
     * @param type  查询类型	Y	type:1根据撤货单；type:2 根据销售单
     * @return
     */
    public static RequestParams saleOrderByPhOrder(String orderId,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params, "1704");
        params.put("orderId", orderId);
        params.put("type",type);
        LogUtils.e("saleOrderByPhOrder", params.toString());
        return params;
    }

    /**
     * 铺货店铺统计
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams phShopStatistic(String userId,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("userId",userId);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("phShopStas:",params.toString());
        return params;
    }

    /**
     * 铺货数据统计
     * @param startDate
     * @param endDate
     * @param type type:0业务员端 type:1 管理端
     * @return
     */
    public static RequestParams phDataStatistic(String startDate,String endDate,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("type",type);
        LogUtils.e("phDataStas:",params.toString());
        return params;
    }

    /**
     * 铺货单、铺货销售单、撤货单列表
     * @param type 1:铺货单 2：铺货销售单 3：撤货单
     * @param status 0:全部 1：已完成 2：未完成 3：已取消
     * @return
     */
    public static RequestParams queryPhOrderList(String customerId,String salesid,String page,String type,String startDate,String endDate,String searchKey,
                                                 String status){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("customerId",customerId);
        params.put("salesid",salesid);
        params.put("page",page);
        params.put("type",type);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("searchKey",searchKey);
        params.put("status",status);
//        startDate	查询起始日期
//        endDate	查询结束日期
        LogUtils.e("queryPhOrderDetailList:",params.toString());
        return params;
    }

    /**
     * 铺货单、铺货销售单、撤货单详情列表
     * @param orderId
     * @param type 1:铺货单详情 2：铺货销售单详情 3：撤货单详情
     * @return
     */
    public static RequestParams queryPhOrderDetailList(String orderId,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("orderId",orderId);
        params.put("type",type);
        LogUtils.e("queryPhOrderDetailList:",params.toString());
        return params;
    }

    /**
     * 管理端-商品铺货店铺/铺货店铺汇总
     * @param goodsId    管理端-商品铺货店铺/铺货 店铺汇总
     * @param page
     * @param searchKey
     * @param salesid   业务员id
     * @return
     */
    public static RequestParams phShopHzList(String goodsId,String page,String searchKey,String salesid,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("goodsId",goodsId);
        params.put("page",page);
        params.put("searchKey",searchKey);
        params.put("salesid",salesid);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("phShopHzList:",params.toString());
        return params;
    }

    /**
     * 管理端-业务员铺货汇总
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams ywyPhHz(String page,String searchKey,String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("page",page);
        params.put("searchKey",searchKey);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
//        params.put("salesid",salesid);
        LogUtils.e("ywyPhHz:",params.toString());
        return params;
    }

    /**
     * 获取铺货活动数据接口
     * @param oa_customerid
     * @param orderApplyDetailStr 申请明细集合
     * @return
     */
    public static RequestParams buildSettlementPhOrder(String oa_customerid,String orderApplyDetailStr){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1704");
        params.put("oa_customerid",oa_customerid);
        params.put("orderApplyDetailStr",orderApplyDetailStr);
        LogUtils.e("buildSettlementPhOrder:",params.toString());
        return params;
    }

}
