package com.xdjd.storebox.adapter.listener;

/**
 * 购物车商品点击事件
 * Created by lijipei on 2016/12/1.
 */

public interface CartChildListener {
    void deleteItem(int j,int k);
    void childPlusCart(int j,int k);
    void childMinusCart(int j,int k);
    void childEditCart(int j,int k);
    void childLeft(int j,int k);
    void childItem(int j,int k);
    void childActon(int j);
}
