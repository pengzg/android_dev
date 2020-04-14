package com.bikejoy.testdemo.enums;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/5/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public enum UserTypeEnum {
    //1 管理员 2业务员 3会员 4运维人员
    USER_ADMIN("2006","管理员"),USER_SALESMAN("2001","业务员"),USER_MEMBER("3","会员"),USER_OPS("2005","运维人员");

    private String code;
    private String name;

    UserTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
