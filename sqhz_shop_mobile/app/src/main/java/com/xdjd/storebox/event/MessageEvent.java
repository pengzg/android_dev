package com.xdjd.storebox.event;

/**
 * Created by lijipei on 2017/2/23.
 */

public class MessageEvent {

    private int position;

    public MessageEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
