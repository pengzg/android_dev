package com.xdjd.storebox.adapter.listener;

/**
 * 购物车商品点击事件
 * Created by lijipei on 2016/12/1.
 */

public interface CartGoodsListener {
    void deleteItem(int k);
    void goodsPlusCart(int k);
    void goodsMinusCart(int k);
    void goodsEditCart(int k);
    void goodsLeft(int k);
    void goodsItem(int k);
}
