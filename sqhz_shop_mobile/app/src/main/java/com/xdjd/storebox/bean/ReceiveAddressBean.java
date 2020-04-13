package com.xdjd.storebox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class ReceiveAddressBean extends BaseBean implements Serializable {

    List<ReceiveAddressBean> addressList;

    public List<ReceiveAddressBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<ReceiveAddressBean> addressList) {
        this.addressList = addressList;
    }

        private String  usa_id ;
        private String usa_address;
        private String usa_receiver_name;
        private String usa_mobile;
        private int usa_is_default;

    public int getUsa_is_default() {
        return usa_is_default;
    }

    public void setUsa_is_default(int usa_is_default) {
        this.usa_is_default = usa_is_default;
    }

    public String getUsa_id() {
        return usa_id;
    }

    public void setUsa_id(String usa_id) {
        this.usa_id = usa_id;
    }

    public String getUsa_address() {
            return usa_address;
        }

        public void setUsa_address(String usa_address) {
            this.usa_address = usa_address;
        }

        public String getUsa_receiver_name() {
            return usa_receiver_name;
        }

        public void setUsa_receiver_name(String usa_receiver_name) {
            this.usa_receiver_name = usa_receiver_name;
        }

        public String getUsa_mobile() {
            return usa_mobile;
        }

        public void setUsa_mobile(String usa_mobile) {
            this.usa_mobile = usa_mobile;
        }



}
