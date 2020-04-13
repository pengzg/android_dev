package com.xdjd.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * 购物车商品数量计算工具类
 * Created by lijipei on 2016/11/29.
 */

public class CartUtil {
    /**
     * 数量转换异常
     */
    public static final int First = -1;
    /**
     * 超出商品数量
     */
    public static final int Two = -2;
    /**
     * 购物车数量和商品库存相等,提示库存不足少买点吧
     */
    public static final int Three = -3;

    /**
     * 点击加号--加购物车计算方法
     *
     * @param cartNumStr
     * @param minNumStr     最小起订量
     * @param addNumStr     增量
     * @param goodsStockStr
     * @return
     */
    public static int countPlusNum(String cartNumStr, String minNumStr, String addNumStr, String goodsStockStr) {
        BigDecimal cartNum = BigDecimal.ZERO;//商品在购物车中实际数量
        BigDecimal minNum = BigDecimal.ONE;//起订量
        BigDecimal addNum = BigDecimal.ONE;//购物车增量
        BigDecimal stockNum = BigDecimal.ZERO;//库存数量

        LogUtils.e("加购物车参数",cartNumStr+"--" + minNumStr
                +"--addNum:"+addNumStr + "--stockNum:" + goodsStockStr);

        if (!TextUtils.isEmpty(cartNumStr) && !"".equals(cartNumStr)){
            cartNum = new BigDecimal(cartNumStr);
        }
        if (!TextUtils.isEmpty(minNumStr) && !"".equals(minNumStr) && !"0".equals(minNumStr)){//最小起订量不能为0,默认为1
            minNum = new BigDecimal(minNumStr);
        }
        if (!TextUtils.isEmpty(addNumStr) && !"".equals(addNumStr) && !"0".equals(addNumStr)){//最小增量不能为0,默认为1
            addNum = new BigDecimal(addNumStr);
        }
        if (!TextUtils.isEmpty(goodsStockStr) && !"".equals(goodsStockStr)){
            stockNum = new BigDecimal(goodsStockStr);
        }

        if (cartNum.compareTo(stockNum) == 0){
            return Three;
        }

        if (cartNum.compareTo(BigDecimal.ZERO) == 0) {
            return minNum.intValue();//如果数量从0开始加,则返回起订数量
        }
        if (cartNum.compareTo(minNum) == -1 && (cartNum.add(addNum)).compareTo(minNum) == -1) {
            return minNum.intValue();
        } else if (cartNum.compareTo(minNum) == 1 && (cartNum.add(addNum)).compareTo(stockNum) == 1) {
            //超出库存数量
            return Two;
        } else {
            int sum = cartNum.add(addNum).intValue();
            LogUtils.e("sum", String.valueOf(sum));
            return sum;
        }
    }

    /**
     * 点击减号--减购物车计算方法
     *
     * @param cartNumStr
     * @param minNumStr
     * @param addNumStr     增量
     * @param goodsStockStr
     * @return
     */
    public static int countMinusNum(String cartNumStr, String minNumStr, String addNumStr, String goodsStockStr) {
        BigDecimal cartNum = BigDecimal.ZERO;//商品在购物车中实际数量
        BigDecimal minNum = BigDecimal.ONE;//起订量
        BigDecimal addNum = BigDecimal.ONE;//购物车增量
        BigDecimal stockNum = BigDecimal.ZERO;//库存数量

        if (!TextUtils.isEmpty(cartNumStr) && !"".equals(cartNumStr)){
            cartNum = new BigDecimal(cartNumStr);
        }
        if (!TextUtils.isEmpty(minNumStr) && !"".equals(minNumStr) && !"0".equals(minNumStr)){//最小起订量不能为0,默认为1
            minNum = new BigDecimal(minNumStr);
        }
        if (!TextUtils.isEmpty(addNumStr) && !"".equals(addNumStr) && !"0".equals(addNumStr)){//最小增量不能为0,默认为1
            addNum = new BigDecimal(addNumStr);
        }
        if (!TextUtils.isEmpty(goodsStockStr) && !"".equals(goodsStockStr)){
            stockNum = new BigDecimal(goodsStockStr);
        }

        if (cartNum.compareTo(BigDecimal.ZERO) == 0) {
            return 0;//返回起订数量
        } else if (cartNum.compareTo(minNum) == -1 || (cartNum.subtract(addNum)).compareTo(minNum) == -1) {
            return 0;
        } else if (cartNum.compareTo(minNum) == 1 && (cartNum.subtract(addNum)).compareTo(stockNum) == 1) {
            //如长时间未操作,库存不足后,直接恢复库存数量
            return stockNum.intValue();
        } else {
            int poor = cartNum.subtract(addNum).intValue();
            return poor;
        }
    }

}
