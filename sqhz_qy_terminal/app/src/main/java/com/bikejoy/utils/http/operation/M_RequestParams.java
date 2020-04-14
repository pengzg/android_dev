package com.bikejoy.utils.http.operation;

import com.loopj.android.http.RequestParams;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/11/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class M_RequestParams {

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
     * 升级会员为金牌合伙人
     * @param memberId
     * @param ba_agent_rate 代理分配比例%
     * @param ba_recommend1_rate 直接推广人分配比例
     * @param ba_recommend2_rate 间接推广人分配比例
     * @return
     */
    public static RequestParams upateMemberToAgent(String memberId,String ba_agent_rate,
                                                   String ba_recommend1_rate,String ba_recommend2_rate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("memberId", memberId);
        params.put("ba_agent_rate", ba_agent_rate);
        params.put("ba_recommend1_rate", ba_recommend1_rate);
        params.put("ba_recommend2_rate", ba_recommend2_rate);
        LogUtils.e("getTotalAmount", params.toString());
        return params;
    }

    /**
     * 查询合伙人列表
     * @param page
     * @param searchKey
     * @param userType
     * @return
     */
    public static RequestParams queryBaseAgentList(String page,
                                                   String searchKey,String userType) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("userType", userType);
        LogUtils.e("getTotalAmount", params.toString());
        return params;
    }

    /**
     * 查询合伙人列表
     * @param page
     * @param searchKey
     * @param userType
     * @return
     */
    public static RequestParams queryAgentAmountData(String page,
                                                     String searchKey,String userType,String agenttype) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("userType", userType);
        params.put("agenttype",agenttype);
        LogUtils.e("getTotalAmount", params.toString());
        return params;
    }

    /**
     * 修改分配比例
     * @param ba_id 主键
     * @param ba_recommend1_rate 直接推广人分配比例
     * @param ba_recommend2_rate 间接推广人分配比例
     * @return
     */
    public static RequestParams updateAgentProfitRatio(String ba_id,
                                                       String ba_recommend1_rate,String ba_recommend2_rate) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("ba_id", ba_id);
        params.put("ba_agent_rate", "");
        params.put("ba_recommend1_rate", ba_recommend1_rate);
        params.put("ba_recommend2_rate", ba_recommend2_rate);
        //ba_agent_rate	代理分配比例%
        LogUtils.e("updateAgentProfitRatio", params.toString());
        return params;
    }

    /**
     * 提现列表
     * @param page
     * @param searchKey
     * @return
     */
    public static RequestParams dataGridCashApply(String page,
                                                     String searchKey,String state) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("gca_audit_state",state);
        LogUtils.e("dataGridCashApply", params.toString());
        return params;
    }

    /**
     * 提现列表详情
     * @param gca_id
     * @return
     */
    public static RequestParams getCashApplyInfo(String gca_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("gca_id", gca_id);
        LogUtils.e("getCashApplyInfo", params.toString());
        return params;
    }

    /**
     * 获取发票列表
     * @param page
     * @param searchKey
     * @param state
     * @return
     */
    public static RequestParams dataGridInvoice(String page,
                                                  String searchKey,String state) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("page", page);
        params.put("rows", ROWS);
        params.put("searchKey", searchKey);
        params.put("oil_invoice_state",state);
        LogUtils.e("dataGridInvoice", params.toString());
        return params;
    }

    /**
     * 发票详情
     * @param oil_id
     * @return
     */
    public static RequestParams getInvoiceLogInfo(String oil_id) {
        RequestParams params = new RequestParams();
        setCommonParams(params, "1103");
        params.put("oil_id", oil_id);
        LogUtils.e("getInvoiceLogInfo", params.toString());
        return params;
    }

}
