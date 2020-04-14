package com.bikejoy.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/23
 *     desc   : 图片加载工具类
 *     version: 1.0
 * </pre>
 */

public class ImageLoader {

    /**
     * 图片加载方法
     * @param context
     * @param url
     * @param iv
     */
    public static void imageLoader(Context context, String url, ImageView iv){
        Glide.with(context).load(url).asBitmap().into(iv);
    }

    /**
     * 图片加载方法
     * @param context
     * @param url
     * @param iv
     * @param placeholder 默认图片
     * @param error 加载错误图片
     */
    public static void imageLoader(Context context, String url, ImageView iv,int placeholder,int error){
        Glide.with(context).load(url).asBitmap().placeholder(placeholder).error(error).into(iv);
    }

    /**
     * 图片加载方法
     * @param context
     * @param url
     * @param iv
     * @param placeholder 默认图片
     * @param error 加载错误图片
     */
    public static void imageLoader(Context context, String url, ImageView iv, Drawable placeholder, Drawable error){
        Glide.with(context).load(url).asBitmap().placeholder(placeholder).error(error).into(iv);
    }
}
