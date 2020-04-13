package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ClientBean extends BaseBean implements Serializable{

    private List<ClientBean> dataList;//	客户列表

    private String cc_name = "";//	客户名
    private String cc_id = "";//	客户id
    private String cc_code;//	编码
    private String cc_barcode;//卡号
    private String cc_islocation;//	是否定位	 Y已经定位 N没有定位
    private String cc_address = "";//	地址
    private String cc_latitude;//	纬度
    private String cc_longitude;//	经度

    private String distance;//	距离 	返回单位是米 所以前台转
    private String cc_image;//	店铺图
    private String cc_contacts_mobile = "";//	电话
    private String cc_contacts_name = "";//	联系人名字

    private String taskId;//	签到id

    private String cc_categoryid;//	类别id
    private String cc_categoryid_nameref;//	类别名
    private String cc_channelid;//	渠道id
    private String cc_channelid_nameref;//	渠道名

    private String lineId;//	线路id
    private String lineName = "";//	线路名

    private String allocatingFlag;//	是否划分在条件线路下

    private String cc_depotid = "";//	客户仓库id
    private String cc_depotid_nameref = "";//	客户仓库名

    private String cc_goods_gradeid_nameref;//	价格方案名
    private String cc_goods_gradeid;//	价格方案id

    private String cc_isaccount;//	是否开通商城 账号	Y开通 N不开通

    private String issign;//N是没有签到，其它是签到了

    private String cc_openid;//	微信openid  	有则不显示绑定微信

//    private String cc_checkstats	审核状态	 1.提交待审核  2.审核通过  3.审核不通过

    //微信绑定中的参数
    private String  imgUrl;//	图片地址返回

    @Override
    public String toString() {
        return "ClientBean{" +
                "dataList=" + dataList +
                ", cc_name='" + cc_name + '\'' +
                ", cc_id='" + cc_id + '\'' +
                ", cc_code='" + cc_code + '\'' +
                ", cc_islocation='" + cc_islocation + '\'' +
                ", cc_address='" + cc_address + '\'' +
                ", cc_latitude='" + cc_latitude + '\'' +
                ", cc_longitude='" + cc_longitude + '\'' +
                ", distance='" + distance + '\'' +
                ", cc_image='" + cc_image + '\'' +
                ", cc_contacts_mobile='" + cc_contacts_mobile + '\'' +
                ", cc_contacts_name='" + cc_contacts_name + '\'' +
                ", taskId='" + taskId + '\'' +
                ", cc_categoryid='" + cc_categoryid + '\'' +
                ", cc_categoryid_nameref='" + cc_categoryid_nameref + '\'' +
                ", cc_channelid='" + cc_channelid + '\'' +
                ", cc_channelid_nameref='" + cc_channelid_nameref + '\'' +
                ", lineId='" + lineId + '\'' +
                ", lineName='" + lineName + '\'' +
                ", allocatingFlag='" + allocatingFlag + '\'' +
                ", cc_depotid='" + cc_depotid + '\'' +
                ", cc_depotid_nameref='" + cc_depotid_nameref + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }


    public String getCc_goods_gradeid_nameref() {
        return cc_goods_gradeid_nameref;
    }

    public void setCc_goods_gradeid_nameref(String cc_goods_gradeid_nameref) {
        this.cc_goods_gradeid_nameref = cc_goods_gradeid_nameref;
    }

    public String getCc_goods_gradeid() {
        return cc_goods_gradeid;
    }

    public void setCc_goods_gradeid(String cc_goods_gradeid) {
        this.cc_goods_gradeid = cc_goods_gradeid;
    }

    public String getCc_openid() {
        return cc_openid;
    }

    public void setCc_openid(String cc_openid) {
        this.cc_openid = cc_openid;
    }

    public String getCc_barcode() {
        return cc_barcode;
    }

    public void setCc_barcode(String cc_barcode) {
        this.cc_barcode = cc_barcode;
    }

    public String getIssign() {
        return issign;
    }

    public void setIssign(String issign) {
        this.issign = issign;
    }

    public String getCc_isaccount() {
        return cc_isaccount;
    }

    public void setCc_isaccount(String cc_isaccount) {
        this.cc_isaccount = cc_isaccount;
    }

    public String getCc_depotid() {
        return cc_depotid;
    }

    public void setCc_depotid(String cc_depotid) {
        this.cc_depotid = cc_depotid;
    }

    public String getCc_depotid_nameref() {
        return cc_depotid_nameref;
    }

    public void setCc_depotid_nameref(String cc_depotid_nameref) {
        this.cc_depotid_nameref = cc_depotid_nameref;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAllocatingFlag() {
        return allocatingFlag;
    }

    public void setAllocatingFlag(String allocatingFlag) {
        this.allocatingFlag = allocatingFlag;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getCc_categoryid() {
        return cc_categoryid;
    }

    public void setCc_categoryid(String cc_categoryid) {
        this.cc_categoryid = cc_categoryid;
    }

    public String getCc_categoryid_nameref() {
        return cc_categoryid_nameref;
    }

    public void setCc_categoryid_nameref(String cc_categoryid_nameref) {
        this.cc_categoryid_nameref = cc_categoryid_nameref;
    }

    public String getCc_channelid() {
        return cc_channelid;
    }

    public void setCc_channelid(String cc_channelid) {
        this.cc_channelid = cc_channelid;
    }

    public String getCc_channelid_nameref() {
        return cc_channelid_nameref;
    }

    public void setCc_channelid_nameref(String cc_channelid_nameref) {
        this.cc_channelid_nameref = cc_channelid_nameref;
    }

    public List<ClientBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ClientBean> dataList) {
        this.dataList = dataList;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCc_image() {
        return cc_image;
    }

    public void setCc_image(String cc_image) {
        this.cc_image = cc_image;
    }

    public String getCc_contacts_mobile() {
        return cc_contacts_mobile;
    }

    public void setCc_contacts_mobile(String cc_contacts_mobile) {
        this.cc_contacts_mobile = cc_contacts_mobile;
    }

    public String getCc_contacts_name() {
        return cc_contacts_name;
    }

    public void setCc_contacts_name(String cc_contacts_name) {
        this.cc_contacts_name = cc_contacts_name;
    }

    public String getCc_latitude() {
        return cc_latitude;
    }

    public void setCc_latitude(String cc_latitude) {
        this.cc_latitude = cc_latitude;
    }

    public String getCc_longitude() {
        return cc_longitude;
    }

    public void setCc_longitude(String cc_longitude) {
        this.cc_longitude = cc_longitude;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getCc_islocation() {
        return cc_islocation;
    }

    public void setCc_islocation(String cc_islocation) {
        this.cc_islocation = cc_islocation;
    }

    public String getCc_address() {
        return cc_address;
    }

    public void setCc_address(String cc_address) {
        this.cc_address = cc_address;
    }
}
