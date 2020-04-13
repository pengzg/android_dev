package com.xdjd.distribution.event;

import com.xdjd.distribution.bean.MemberBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MemberEvent {
    private MemberBean bean;

    public MemberEvent(MemberBean bean) {
        this.bean = bean;
    }

    public MemberBean getBean() {
        return bean;
    }

}
