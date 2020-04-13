package com.xdjd.distribution.base;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.math.BigDecimal;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/25
 *     desc   : 大小单位转换计算类
 *     version: 1.0
 * </pre>
 */

public class UnitCalculateUtils {

    /**
     * 计算大小单位数量--(只有大小单位都有时才使用这个方法计算)
     * @param unitNum 换算数量
     * @param stockNum 库存数量
     * @param maxUnitName 大单位
     * @param minUnitName 小单位
     * @return
     */
    public static String unitStr(String unitNum, String stockNum, String maxUnitName, String minUnitName) {
        String strUnit;

        BigDecimal bgUnitNum;//换算单位数量
        BigDecimal stock;

        if (unitNum == null || "".equals(unitNum)){
            bgUnitNum = BigDecimal.ONE;
        }else{
            bgUnitNum = new BigDecimal(unitNum);
        }

        if (bgUnitNum.compareTo(BigDecimal.ONE) == 0){//换算比是1,只显示小单位
            strUnit = stockNum + minUnitName;
            return strUnit;
        }

        if (stockNum == null || "".equals(stockNum)){
            stock = BigDecimal.ZERO;
        }else{
            stock = new BigDecimal(stockNum);
        }

        BigDecimal[] result = stock.divideAndRemainder(bgUnitNum);
        strUnit = result[0] + maxUnitName + result[1] + minUnitName;
        return strUnit;
    }

    public static void setCalculatePlusMinus(final EditText et, LinearLayout ll, RelativeLayout rl, ImageView iv) {
        Animation inFromLeft = null;
        if (!et.getText().toString().equals("0") && !et.getText().toString().equals("")) {
            ll.setVisibility(View.VISIBLE);
        } else if (et.getText().toString().equals("0") || et.getText().toString().equals("")) {
            if (ll.getVisibility() == View.VISIBLE){
                inFromLeft = new TranslateAnimation(0, rl.getWidth() - iv.getWidth(), 0, 0);
                inFromLeft.setDuration(10);
                inFromLeft.setInterpolator(new AccelerateInterpolator());
                ll.startAnimation(inFromLeft);
            }
            ll.setVisibility(View.GONE);
        }
    }

    public static void setTranslateAnimation(final EditText et, LinearLayout ll, RelativeLayout rl, ImageView iv) {
        Animation inFromLeft = null;
        if (ll.getVisibility() == View.GONE && !et.getText().toString().equals("0") && !et.getText().toString().equals("")) {
            ll.setVisibility(View.VISIBLE);
            inFromLeft = new TranslateAnimation(rl.getWidth() - iv.getWidth(), 0, 0, 0);
            inFromLeft.setDuration(10);
            inFromLeft.setFillAfter(true);
            ll.startAnimation(inFromLeft);
        } else if (ll.getVisibility() == View.VISIBLE && (et.getText().toString().equals("0") || et.getText().toString().equals(""))) {
            inFromLeft = new TranslateAnimation(0, rl.getWidth() - iv.getWidth(), 0, 0);
            inFromLeft.setDuration(10);
            inFromLeft.setInterpolator(new AccelerateInterpolator());
            ll.startAnimation(inFromLeft);
            ll.setVisibility(View.GONE);
        }

        /*if (ll.getVisibility() == View.GONE && !et.getText().toString().equals("0") && !et.getText().toString().equals("")) {
            ll.setVisibility(View.VISIBLE);
        } else if (ll.getVisibility() == View.VISIBLE && (et.getText().toString().equals("0") || et.getText().toString().equals(""))) {
            ll.setVisibility(View.GONE);
        }*/
    }


    /**
     * 小单位等于大单位换算数量时转成大单位
     * @param unitNum 换算数量
     * @param maxNum 大单位数量
     * @param minNum 小单位数量
     * @param maxUnitName 大单位
     * @param minUnitName 小单位
     * @return
     */
    public static String goodsNumDesc(String unitNum, String maxNum,String minNum, String maxUnitName, String minUnitName) {
        String strUnit;

        BigDecimal bgUnitNum;//换算单位数量
        BigDecimal bgMaxNum,bgMinNum;
        BigDecimal totalNum;

        if (unitNum == null || "".equals(unitNum)){
            bgUnitNum = BigDecimal.ONE;
        }else{
            bgUnitNum = new BigDecimal(unitNum);
        }

        if (bgUnitNum.compareTo(BigDecimal.ONE) == 0){//换算比是1,只显示小单位
            strUnit = minNum + minUnitName;
            return strUnit;
        }

        if (maxNum == null || "".equals(maxNum)){
            bgMaxNum = BigDecimal.ZERO;
        }else{
            bgMaxNum = new BigDecimal(maxNum);
        }

        if (minNum == null || "".equals(minNum)){
            bgMinNum = BigDecimal.ZERO;
        }else{
            bgMinNum = new BigDecimal(minNum);
        }

        totalNum = bgMaxNum.multiply(bgUnitNum).add(bgMinNum);
        BigDecimal[] result = totalNum.divideAndRemainder(bgUnitNum);
        if (result[1].compareTo(BigDecimal.ZERO) == 0){//小单位是0
            strUnit = result[0] + maxUnitName;
        }else if (result[0].compareTo(BigDecimal.ZERO) == 0){//大单位是0
            strUnit = result[1] + minUnitName;
        }else{
            strUnit = result[0] + maxUnitName + result[1] + minUnitName;
        }
        return strUnit;
    }

    /*public String getRefundNum_desc() {
        String refundNum_desc = "";
        if(getRefundNum()!=null&&getRefundNum()>0){
            if(getUnitNum()!=null&&getUnitNum()!=1){
                Integer max = getRefundNum()/getUnitNum();
                Integer min = getRefundNum()%getUnitNum();
                if(max>0){
                    refundNum_desc = refundNum_desc+max+getMaxName();
                }
                if(min>0){
                    refundNum_desc = refundNum_desc+min+getMinName();
                }
            }else{
                refundNum_desc = getRefundNum()+getMinName();
            }
        }else{
            refundNum_desc = 0+getMinName();
        }

        return refundNum_desc;
    }*/


}
