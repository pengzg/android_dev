package com.xdjd.storebox.event;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class WinningCodeEvent {
    private String winningCode;

    public WinningCodeEvent(String winningCode) {
        this.winningCode = winningCode;
    }

    public String getWinningCode() {
        return winningCode;
    }
}
