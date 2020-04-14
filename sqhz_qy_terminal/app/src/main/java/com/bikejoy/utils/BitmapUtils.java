package com.bikejoy.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/1/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BitmapUtils {


    public static byte[] bmpToByteArray(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
