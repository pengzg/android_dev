package com.xdjd.storebox.event;

/**
 * 商品加减数量event
 * Created by lijipei on 2017/3/10.
 */

public class GoodsNumEvent {

    private String cartNum;//商品在购物车中的数量
    private String goodsId;//商品的id

    public GoodsNumEvent(String goodsId, String cartNum) {
        this.goodsId = goodsId;
        this.cartNum = cartNum;
    }

    public String getCartNum() {
        return cartNum;
    }

    public String getGoodsId() {
        return goodsId;
    }

}
