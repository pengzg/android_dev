package com.xdjd.storebox.adapter.listener;

/**
 * Created by lijipei on 2016/12/1.
 */

public interface CartOneListener {
    /**
     * 结算回调
     * @param i
     */
    void cartEnter(int i);

    /**
     * 全选回调
     * @param i
     */
    void cartAll(int i);

    /**
     * 去凑单回调
     * @param i
     */
    void cartSpell(int i);

    /**
     * 电话回调
     * @param i
     */
    void cartPhone(int i);

    /**
     * 购物车移入收藏方法
     * @param i
     */
    void cartCollect(int i);

    /**
     * 商品删除回调
     * @param i
     * @param j
     */
    void cartChildDelete(int i, int j,int k);

    /**
     * 加购物车数量方法
     * @param i
     * @param j
     */
    void cartPlus(int i, int j,int k);

    /**
     * 减购物车商品数量方法
     * @param i
     * @param j
     */
    void cartMinus(int i, int j,int k);

    /**
     * 编辑购物车商品数量方法
     * @param i
     * @param j
     */
    void cartEdit(int i, int j,int k);

    /**
     * 商品单选回调
     * @param i
     * @param j
     */
    void cartLeft(int i, int j,int k);

    /**
     * item点击事件
     * @param i
     * @param j
     */
    void cartChildItem(int i, int j,int k);

}
