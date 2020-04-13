package com.xdjd.utils;

/**
 * Created by freestyle_hong on 2017/8/7.
 */

public class GoodsNumDesc {
    public String getGoodsNumDesc(String numMax,String unitMax,String numMin,String unitMin,String desc){
        if(numMax != null && numMin != null){
            if(numMax.equals("0")&&(!numMin.equals("0"))){
                desc = numMin+unitMin;
            }else if((!numMax.equals("0"))&&(numMin.equals("0"))){
                desc = numMax+unitMax;
            }else if(numMax.equals("0")&&(numMin.equals("0"))){
                desc = "0";
            }else{
                desc = numMax+unitMax+numMin+unitMin;
            }
        }else{
            desc = "";
        }
        return desc;
    }
}
