package com.xdjd.distribution.event;

import com.xdjd.distribution.bean.ClientBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/19
 *     desc   : 开通商城或编辑客户信息 刷新event
 *     version: 1.0
 * </pre>
 */

public class OpenMallEvent {

    private ClientBean clientBean;

    public OpenMallEvent(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    public ClientBean getClientBean() {
        return clientBean;
    }
}
