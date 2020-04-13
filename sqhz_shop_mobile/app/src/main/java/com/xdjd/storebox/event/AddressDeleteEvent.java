package com.xdjd.storebox.event;

/**
 * 地址删除event
 * Created by lijipei on 2017/3/9.
 */

public class AddressDeleteEvent {
    private String id;

    public AddressDeleteEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
