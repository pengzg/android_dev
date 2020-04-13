package com.xdjd.storebox.adapter.listener;

import java.util.HashMap;

/**
 * Created by lijipei on 2016/12/1.
 */

public interface CartListener {

    void cartAction(int i);


    /**
     * 加购物车数量方法
     * @param i
     */
    void cartPlus(int i);

    /**
     * 减购物车商品数量方法
     * @param i
     */
    void cartMinus(int i);

    /**
     * 编辑购物车商品数量方法
     * @param i
     */
    void cartEdit(int i);

    /**
     * 商品单选回调
     * @param i
     */
    void cartLeft(int i);


    /**
     * 商品删除回调
     * @param i
     * @param j
     */
    void cartChildDelete(int i,int j);

    /**
     * 加购物车数量方法
     * @param i
     * @param j
     */
    void cartPlus(int i,int j);

    /**
     * 减购物车商品数量方法
     * @param i
     * @param j
     */
    void cartMinus(int i,int j);

    /**
     * 编辑购物车商品数量方法
     * @param i
     * @param j
     */
    void cartEdit(int i,int j);

    /**
     * 商品单选回调
     * @param i
     * @param j
     */
    void cartLeft(int i,int j);

    /**
     * item点击事件
     * @param i
     * @param j
     */
    void cartChildItem(int i,int j);

}
