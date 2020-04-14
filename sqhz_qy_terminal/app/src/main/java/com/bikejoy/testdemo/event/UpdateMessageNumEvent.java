package com.bikejoy.testdemo.event;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/2/22
 *     desc   : 更新推送消息数量
 *     version: 1.0
 * </pre>
 */

public class UpdateMessageNumEvent {
    private String num;

    public UpdateMessageNumEvent(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }
}
