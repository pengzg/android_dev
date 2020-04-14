package com.bikejoy.testdemo.enums;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/5/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public enum RoleTypeEnum {

    // 企业端用户角色
    // 客户开发3001
    // 客户开发主管3002
    // 配送员3003
    // 调度员3004
    // 配送站3005
    // 库管人员3006
    // 采购员3007
    // 财务人员权限3008
    // 总经理3009
    // 设备运维权限3010
    // 管理员权限3011

    ROLE3001("3001","客户开发"),ROLE3002("3002","客户开发主管"),ROLE3003("3003","配送员"),ROLE3004("3004","调度员"),
    ROLE3005("3005","配送站"),ROLE3006("3006","库管人员"),ROLE3007("3007","采购员"),ROLE3008("3008","财务人员"),
    ROLE3009("3009","总经理"),ROLE3010("3010","设备运维"),ROLE3011("3011","管理员"),
    ROLE2001("2001","业务员"),ROLE2002("2002","库管人员"),ROLE2003("2003","财务人员权限"),ROLE2004("2004","总经理"),
    ROLE2005("2005","设备运维"),ROLE2006("2006","管理员权限");

    private String code;
    private String name;

    RoleTypeEnum(String code, String name) {
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
