package com.xdjd.storebox.activity;

import com.xdjd.storebox.bean.ReceiveAddressBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */

public class AddressInfoActivity  {

    private String receiveName;
    private String receivePhone;
    private String receiveAddress;
    private List<ReceiveAddressBean> list;
    public List<ReceiveAddressBean> getList() {
        return list;
    }

    public void setList(List<ReceiveAddressBean> list) {
        this.list = list;
    }

    /*public AddressInfoActivity(String receiveName, String receivePhone, String receiveAddress) {
        this.receiveName = receiveName;
        this.receivePhone = receivePhone;
        this.receiveAddress = receiveAddress;
    }*/

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
}
