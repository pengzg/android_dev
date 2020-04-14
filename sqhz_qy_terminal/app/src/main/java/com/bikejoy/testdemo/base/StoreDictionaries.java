package com.bikejoy.testdemo.base;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/2/25
 *     desc   : 仓库字典工具类
 *     version: 1.0
 * </pre>
 */

public class StoreDictionaries {

    //1 格子柜  2重力柜 3.A01户外机械柜  4.B01机械柜
    public static final String boxType01 = "1";
    public static final String boxType02 = "2";
    public static final String boxType03 = "3";
    public static final String boxType04 = "4";

    public static String storeTypeName(String boxType){
        String name="";
        switch (boxType){
            case boxType01:
                name = "格子柜";
                break;
            case boxType02:
                name = "重力柜";
                break;
            case boxType03:
                name = "机械柜A01";
                break;
            case boxType04:
                name = "机械柜B01";
                break;
        }
        return name;
    }

}
