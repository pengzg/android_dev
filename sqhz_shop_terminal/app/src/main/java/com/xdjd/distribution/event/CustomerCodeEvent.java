package com.xdjd.distribution.event;

import com.xdjd.distribution.bean.ClientBean;

/**
 * Created by lijipei on 2017/8/27.
 */

public class CustomerCodeEvent {

    private ClientBean mClientBean;

    public CustomerCodeEvent(ClientBean clientBean) {
        mClientBean = clientBean;
    }

    public ClientBean getClientBean() {
        return mClientBean;
    }
}
