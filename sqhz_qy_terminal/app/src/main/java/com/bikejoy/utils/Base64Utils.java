package com.bikejoy.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/4/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Base64Utils {

    //把bitmap转换成String
    public static String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();
        Log.e("d-d", "压缩后的大小=" + b.length);
        Log.e("d-d", "压缩后的大小=" + b.length/1024);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
