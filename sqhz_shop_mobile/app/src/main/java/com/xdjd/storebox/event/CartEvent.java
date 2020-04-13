package com.xdjd.storebox.event;

import com.xdjd.utils.PublicFinal;

/**
 * Created by lijipei on 2016/12/5.
 */

public class CartEvent {

    private String cartNum; //购物车中的数量
    private String totalAmount;//总价格

    /**
     *
     * @param cartNum 总数量
     * @param totalAmount 总价格
     */
    public CartEvent(String cartNum, String totalAmount) {
        this.cartNum = cartNum;
        this.totalAmount = totalAmount;
    }

    public String getCartNum() {
        return cartNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

}
