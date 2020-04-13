package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserBean extends BaseBean {

    private String userId = "0";//	用户信息编号
    private String userName = "";//	登录用户名
    private String orgid = "0";// 公司id
    private String su_storeid_name = "";//	车号

    private String bud_customerid = "";//	对应客户的id
    private String su_storeid = "";//	业务员关联的仓库

    private String unionId;//	微信登录标识unionid

    private String sign;//签名字段

    private String mobile = "";//	电话
    private String bud_name = "";//	员工 名
    private String inPhoto = "";//	进店是否拍照 1是 2否
    private String outPhoto = "";//	离店是否拍照	1是 2否
    private String isScan;//	是否扫卡 1扫卡 2手动
    private List<LineBean> lineList;//	线路名

    private String isChangPrice = "2";//	是否变价	1 是2不是
    private String isChangeThPrice = "2";//	是否变价(退货）	1是2不是
    private String isQueryStock;//	是否查看库存	1是 2不是

    private String su_usertype = "1";//	用户类型--默认是1;-- 1 车销; 2 跑单,4 配送员,5 管理员

    private String ticketmsg1;//	头信息
    private String ticketmsg2;//	尾信息

    private String isChangeYH;//	是否修改优惠	 1是 2否
    private String skType = "N";//	刷卡方式	 N不使用

    private String isAllowSign = "2";//1 只能500米内  2  不限制
    private String bud_id;//员工id
    private int signDistance = 1200;//后台传过来的定位距离,默认1200距离

    private String companyName = "";//	公司名
    private String isReLocation = "1";//	是否允许重复定位	1是 2否
    private String companyLocation = "";//	公司经纬度 逗号分隔 	 114.561968,37.068902

    private String refundDays;//	退货日期
    private String refundMode;//	退货模式	 1商品  2订单 3 商品+订单

    private String isShopAccount;//	是否开通商城 	N不开启  Y开启
    private String isSendSms;//	是否发送短信	N不 开启Y开启
    private String isSign = "N"; //是否需要签名	N不开启  Y开启

    private String city;//	城市

    private String hzdh_download_url;//盒子助手下载链接


    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getHzdh_download_url() {
        return hzdh_download_url;
    }

    public void setHzdh_download_url(String hzdh_download_url) {
        this.hzdh_download_url = hzdh_download_url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsShopAccount() {
        return isShopAccount;
    }

    public void setIsShopAccount(String isShopAccount) {
        this.isShopAccount = isShopAccount;
    }

    public String getIsSendSms() {
        return isSendSms;
    }

    public void setIsSendSms(String isSendSms) {
        this.isSendSms = isSendSms;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getRefundMode() {
        return refundMode;
    }

    public void setRefundMode(String refundMode) {
        this.refundMode = refundMode;
    }

    public String getRefundDays() {
        return refundDays;
    }

    public void setRefundDays(String refundDays) {
        this.refundDays = refundDays;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getIsReLocation() {
        return isReLocation;
    }

    public void setIsReLocation(String isReLocation) {
        this.isReLocation = isReLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getSignDistance() {
        return signDistance;
    }

    public void setSignDistance(int signDistance) {
        this.signDistance = signDistance;
    }

    public String getBud_id() {
        return bud_id;
    }

    public void setBud_id(String bud_id) {
        this.bud_id = bud_id;
    }

    public String getIsAllowSign() {
        return isAllowSign;
    }

    public void setIsAllowSign(String isAllowSign) {
        this.isAllowSign = isAllowSign;
    }

    public String getIsChangeYH() {
        return isChangeYH;
    }

    public void setIsChangeYH(String isChangeYH) {
        this.isChangeYH = isChangeYH;
    }

    public String getSkType() {
        return skType;
    }

    public void setSkType(String skType) {
        this.skType = skType;
    }

    public String getTicketmsg2() {
        return ticketmsg2;
    }

    public void setTicketmsg2(String ticketmsg2) {
        this.ticketmsg2 = ticketmsg2;
    }

    public String getTicketmsg1() {
        return ticketmsg1;
    }

    public void setTicketmsg1(String ticketmsg1) {
        this.ticketmsg1 = ticketmsg1;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSu_usertype() {
        return su_usertype;
    }

    public void setSu_usertype(String su_usertype) {
        this.su_usertype = su_usertype;
    }

    public String getIsChangPrice() {
        return isChangPrice;
    }

    public void setIsChangPrice(String isChangPrice) {
        this.isChangPrice = isChangPrice;
    }

    public String getIsChangeThPrice() {
        return isChangeThPrice;
    }

    public void setIsChangeThPrice(String isChangeThPrice) {
        this.isChangeThPrice = isChangeThPrice;
    }

    public String getIsQueryStock() {
        return isQueryStock;
    }

    public void setIsQueryStock(String isQueryStock) {
        this.isQueryStock = isQueryStock;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSu_storeid() {
        return su_storeid;
    }

    public void setSu_storeid(String su_storeid) {
        this.su_storeid = su_storeid;
    }

    public String getBud_customerid() {
        return bud_customerid;
    }

    public void setBud_customerid(String bud_customerid) {
        this.bud_customerid = bud_customerid;
    }

    public String getOutPhoto() {
        return outPhoto;
    }

    public void setOutPhoto(String outPhoto) {
        this.outPhoto = outPhoto;
    }

    public String getInPhoto() {
        return inPhoto;
    }

    public void setInPhoto(String inPhoto) {
        this.inPhoto = inPhoto;
    }

    public String getIsScan() {
        return isScan;
    }

    public void setIsScan(String isScan) {
        this.isScan = isScan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getSu_storeid_name() {
        return su_storeid_name;
    }

    public void setSu_storeid_name(String su_storeid_name) {
        this.su_storeid_name = su_storeid_name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBud_name() {
        return bud_name;
    }

    public void setBud_name(String bud_name) {
        this.bud_name = bud_name;
    }

    public List<LineBean> getLineList() {
        return lineList;
    }

    public void setLineList(List<LineBean> lineList) {
        this.lineList = lineList;
    }
}
