package com.bikejoy.testdemo.enums;

/**
 * Created by lijipei on 2019/10/9.
 */

public enum WeekdayEnum {

    WEEK1("1","周一"),WEEK2("2","周二"),
    WEEK3("3","周三"),WEEK4("4","周四"),
    WEEK5("5","周五"),WEEK6("6","周六"),
    WEEK7("7","周日");

    private String code;
    private String name;

    WeekdayEnum(String code, String name) {
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
