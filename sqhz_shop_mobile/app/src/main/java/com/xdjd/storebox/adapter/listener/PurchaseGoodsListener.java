package com.xdjd.storebox.adapter.listener;

import android.widget.RelativeLayout;

import com.xdjd.storebox.bean.ActionBean;
import com.xdjd.storebox.bean.GoodsBean;

/**
 * 采购商品借口回调接口
 * Created by lijipei on 2016/11/29.
 */

public interface PurchaseGoodsListener {
    /**
     * 加购物车
     *
     * @param position
     */
    void plusCart(int position, RelativeLayout rl);

    /**
     * 减购物车
     *
     * @param position
     */
   void minusCart(int position, RelativeLayout rl);

    /**
     * 编辑添加购物车数量
     *
     * @param i
     */
    void editGoodsCartNum(int i, RelativeLayout rl,String ggpId);
    //采购商品列表跳pop
    void editGoodsCartNumNew(int i, RelativeLayout rl, GoodsBean bean);
    //活动商品跳pop
    void editGoodCartNumActicon(int i, RelativeLayout rl, ActionBean actionBean);
    /**
     * 商品点击
     *
     * @param position
     */
    void itemGoods(int position);


}
