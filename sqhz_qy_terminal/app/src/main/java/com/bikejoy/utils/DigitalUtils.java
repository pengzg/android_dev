package com.bikejoy.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/7/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DigitalUtils {

    /**
     * 将数字字符串转换成BigDecimal类型,并判空
     * @param num
     * @return
     */
    public static BigDecimal strToBigDecimal(String num){
        BigDecimal bg;
        if (TextUtils.isEmpty(num)) {
            bg = BigDecimal.ZERO;
        } else {
            bg = new BigDecimal(num);
        }
        return bg;
    }
}
