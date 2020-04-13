package com.xdjd.distribution.event;

import com.xdjd.distribution.bean.ClientBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/13
 *     desc   : 客户绑定传递客户信息event
 *     version: 1.0
 * </pre>
 */

public class BindingFacilityEvent {
    private ClientBean mClientBean;

    public BindingFacilityEvent(ClientBean clientBean) {
        mClientBean = clientBean;
    }

    public ClientBean getClientBean() {
        return mClientBean;
    }
}
