package com.bikejoy.testdemo.base;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/4/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BitmapUtils {
    public static final BitmapUtils INSTANCE;

    public final byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)os);
        byte[] var10000 = os.toByteArray();
        return var10000;
    }

    public final Bitmap mirror(Bitmap rawBitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1.0F, 1.0F);
        Bitmap var10000 = Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.getWidth(), rawBitmap.getHeight(), matrix, true);
        return var10000;
    }

    public final Bitmap rotate( Bitmap rawBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap var10000 = Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.getWidth(), rawBitmap.getHeight(), matrix, true);
        return var10000;
    }

    public final Bitmap decodeBitmap( Bitmap bitmap, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)bos);
        BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size(), options);
        options.inSampleSize = this.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap var10000 = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size(), options);
        return var10000;
    }

    public final Bitmap decodeBitmapFromFile( String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = this.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap var10000 = BitmapFactory.decodeFile(path, options);
        return var10000;
    }

    public final Bitmap decodeBitmapFromResource( Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = this.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap var10000 = BitmapFactory.decodeResource(res, resId, options);
        return var10000;
    }

    private final int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;
        int inSampleSize = 1;
        if(rawWidth > reqWidth || rawHeight > reqHeight) {
            int halfWidth = rawWidth / 2;

            for(int halfHeight = rawHeight / 2; halfWidth / inSampleSize > reqWidth && halfHeight / inSampleSize > reqHeight; inSampleSize *= 2) {
                ;
            }
        }

        return inSampleSize;
    }

    static {
        BitmapUtils var0 = new BitmapUtils();
        INSTANCE = var0;
    }
}
