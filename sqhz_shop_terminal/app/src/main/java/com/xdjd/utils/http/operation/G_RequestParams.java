package com.xdjd.utils.http.operation;

import com.loopj.android.http.RequestParams;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/11
 *     desc   : 管理员接口请求参数
 *     version: 1.0
 * </pre>
 */

public class G_RequestParams {

    /**
     * 公共参数方法
     * @param params
     * @param reqCode
     */
    public static void setCommonParams(RequestParams params, String reqCode){
        params.put("reqCode",reqCode);
        params.put("device", "1");//设备编码,默认传1
        params.put("deviceType", "android");
        params.put("userId", UserInfoUtils.getId(UIUtils.getContext()));
        params.put("orgid",UserInfoUtils.getOrgid(UIUtils.getContext()));
        params.put("sign", UserInfoUtils.getSign(UIUtils.getContext()));
        params.put("userDocId",UserInfoUtils.getBudId(UIUtils.getContext()));
    }

    /**
     * 首页接口
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getOperatingData(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        return params;
    }

    /**
     * 业务员销售/退货排行
     * @param startDate
     * @param endDate
     * @param salesType 2销售 3退货
     * @return
     */
    public static RequestParams getSalesList(String startDate,String endDate,String rows,String salesType){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page","1");
        params.put("rows",rows);
        params.put("salesType",salesType);
        LogUtils.e("getSalesList",params.toString());
        return params;
    }

    /**
     * 商品销售/退货排行
     * @param page
     * @param startDate
     * @param endDate
     * @param salesType 2销售 3退货
     * @return
     */
    public static RequestParams getGoodsSaleList(String page,String startDate,String endDate,String salesType,String rows){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page",page);
        params.put("rows",rows);
        params.put("salesType",salesType);
        LogUtils.e("getGoodsSaleList",params.toString());
        return params;
    }

    /**
     * 查询库存商品数量
     * @param userId
     * @param storeid
     * @param searchKey
     * @param page
     * @return
     */
    public static RequestParams getGoodsStock(String userId,String storeid,String searchKey,String page,String rows){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",userId);
        params.put("storeid",storeid);
        params.put("searchKey",searchKey);
        params.put("page",page);
        params.put("rows",rows);
        return params;
    }

    /**
     * 拜访明细
     * @param userId
     * @param salesid
     * @param page
     * @param startDate
     * @param endDate
     * @param salesmanType 业务员类型		1全部 2.业务员 3配送员
     * @param lineId 线路id
     * @return
     */
    public static RequestParams getReportTaskList(String userId,String salesid,String page,
                                                  String startDate,String endDate,String salesmanType,String rows,String type,String customerId,String lineId){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",userId);
        params.put("salesid",salesid);
        params.put("page",page);
        params.put("rows",rows);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("type",type);//type	请求类型		1 列表  2.业务员位置
        params.put("customerId",customerId);
        params.put("salesmanType",salesmanType);
        params.put("lineId",lineId);//lineId	线路
        LogUtils.e("getReportTaskList",params.toString());
        return params;
    }

    /**
     * 业务员拜访次数排名列表
     * @param startDate
     * @param endDate
     * @param rows
     * @return
     */
    public static RequestParams getSalesVisitNum(String startDate,String endDate,String rows,String page){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("rows",rows);
        params.put("page",page);
        return params;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param rows
     * @param page
     * @return
     */
    public static RequestParams ywySaleRank(String startDate,String endDate,String rows,String page){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("rows",rows);
        params.put("page",page);
        LogUtils.e("ywySaleRank",params.toString());
        return params;
    }

    /**
     * 上交款列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getHandinList(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"1001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        return params;
    }

    /**
     * 人员管理销售信息
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getVisitData(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("getVisitData",params.toString());
        return params;
    }

    /**
     * 营收简报
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getBriefing(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("getBriefing",params.toString());
        return params;
    }

    /**
     * 营收趋势
     * @param startDate
     * @param endDate
     * @param type 1 订单金额 2.收款 3订单数量
     * @param chartType 分组类型	Y	1日  2月
     * @return
     */
    public static RequestParams getTrendsChart(String startDate,String endDate,String type,String chartType){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("type",type);
        params.put("chartType",chartType);
        LogUtils.e("getTrendsChart",params.toString());
        return params;
    }

    /**
     * 客户订单统计
     * @param startDate
     * @param endDate
     * @param type 1区域  2类别
     * @return
     */
    public static RequestParams getCustOrder(String startDate,String endDate,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("type",type);
        params.put("page","1");
        params.put("rows","5");
        LogUtils.e("getCustOrder",params.toString());
        return params;
    }

    /**
     * 商品销售分类统计
     * @param startDate
     * @param endDate
     * @param type 1区域 2分类
     * @return
     */
    public static RequestParams getGoodsSalesCategory(String startDate,String endDate,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("type",type);
        params.put("page","1");
        params.put("rows","5");
        LogUtils.e("getGoodsSalesCategory",params.toString());
        return params;
    }

    /**
     * 退货统计
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getRefundReport(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("getRefundReport",params.toString());
        return params;
    }

    /**
     * 访店达成率
     * @param startDate
     * @param endDate
     * @param page
     * @return
     */
    public static RequestParams getVisitRate(String startDate,String endDate,String page){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page",page);
        params.put("rows","10");
        LogUtils.e("getVisitRate",params.toString());
        return params;
    }

    /**
     * 获取业务员位置
     * @return
     */
    public static RequestParams getSalesLocation(){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        LogUtils.e("getSalesLocation",params.toString());
        return params;
    }

    /**
     * 客户数量接口
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams getCustomerNum(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("getCustomerNum",params.toString());
        return params;
    }

    /**
     * 客户订货统计
     * @param startDate
     * @param endDate
     * @param page
     * @param rows
     * @return
     */
    public static RequestParams getCustOrderAmountList(String startDate,String endDate,String page,String rows){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page",page);
        params.put("rows",rows);
        LogUtils.e("getCustOrderAmountList",params.toString());
        return params;
    }

    /**
     * 得到访店达成率按天统计
     * @param startDate
     * @param endDate
     * @param page
     * @param salesid 业务员id
     * @return
     */
    public static RequestParams getVisitRateDay(String startDate,String endDate,String page,String salesid){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("userId",UserInfoUtils.getId(UIUtils.getContext()));
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("page",page);
        params.put("rows","999");
        params.put("salesid",salesid);
        LogUtils.e("getCustOrderAmountList",params.toString());
        return params;
    }

    /**
     * 获取核销详情
     * @param startdate
     * @param enddate
     * @param page
     * @return
     */
    public static RequestParams queryHxSum(String startdate,String enddate,String page,String searchKey,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("startdate",startdate);
        params.put("enddate",enddate);
        params.put("page",page);
        params.put("searchKey",searchKey);
        params.put("type",type);//type	类型	N	type:1未被绑定的设备 2：已被绑定的设备
        LogUtils.e("queryHxSum",params.toString());
        return params;
    }

    /**
     * 获取活动列表
     * @param page
     * @param searchKey
     * @param type  活动状态 1取消发布 2 暂存状态 3 进行中 4 已结束
     * @return
     */
    public static RequestParams getActivityList(String page,String searchKey,String type){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("page",page);
        params.put("searchKey",searchKey);
        params.put("type",type);//type  活动状态 1取消发布 2 暂存状态 3 进行中 4 已结束
        LogUtils.e("queryHxSum",params.toString());
        return params;
    }

    /**
     * 店铺结算每个商品的详情列表
     * @param type 2:是限制距离范围
     * @param cc_latitude 经度	N
     * @param cc_longitude 维度	N
     * @return
     */
    public static RequestParams getAllShopList(String type,String cc_longitude,String cc_latitude){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("type",type);
        params.put("cc_latitude",cc_latitude);
        params.put("cc_longitude",cc_longitude);
        LogUtils.e("getAllShopList",params.toString());
        return params;
    }

    /**
     * 用户统计--今日指标
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams queryUserTodayStats(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("startDate",startDate);
        params.put("endDate",endDate);
//        firstMOnthDay	本月第一天
        LogUtils.e("queryUserTodayStats",params.toString());
        return params;
    }

    /**
     * 用户统计--趋势分析
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams queryUserMonthlyStats(String startDate,String endDate){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        LogUtils.e("queryUserTodayStats",params.toString());
        return params;
    }

    /**
     * 活动统计--今日指标
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams queryActivityTodayStats(String startDate,String endDate,String activityId){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("activityId",activityId);
        LogUtils.e("queryActivityTodayStats",params.toString());
        return params;
    }

    /**
     * 活动统计--趋势分析
     * @param startDate
     * @param endDate
     * @return
     */
    public static RequestParams queryActivityPeriodStats(String startDate,String endDate,String activityId){
        RequestParams params = new RequestParams();
        setCommonParams(params,"3001");
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("activityId",activityId);
        LogUtils.e("queryUserTodayStats",params.toString());
        return params;
    }
}
