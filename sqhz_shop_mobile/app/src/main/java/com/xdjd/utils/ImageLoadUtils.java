package com.xdjd.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * 图片加载封装工具类
 * Created by lijipei on 2017/3/27.
 */

public class ImageLoadUtils {

    /**
     * 图片加载
     *
     * @param context 上下文
     * @param imgUrl  图片路径
     * @param view    imageView控件
     */
    public static void loadImage(Context context, String imgUrl, ImageView view) {
        Glide.with(context).load(imgUrl).into(view);
    }

    /**
     * 图片加载-设置默认错误图片
     *
     * @param context
     * @param imgUrl
     * @param defaultErrorId
     * @param view
     */
    public static void loadImage(Context context, String imgUrl, int defaultErrorId, ImageView view) {
        Glide.with(context).load(imgUrl).error(defaultErrorId).into(view);
    }

    /**
     * 图片加载-设置默认错误图片
     *
     * @param context
     * @param imgUrl
     * @param defaultErrorDrawable
     * @param view
     */
    public static void loadImage(Context context, String imgUrl, Drawable defaultErrorDrawable, ImageView view) {
        Glide.with(context).load(imgUrl).error(defaultErrorDrawable).into(view);
    }

    /**
     * 将图片链接转换成bitmap加载进view
     * @param context
     * @param imgUrl
     * @param view
     */
    public static void loadImageBitmap(Context context, String imgUrl, final ImageView view) {
        Glide.with(context).load(imgUrl).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(bitmap);
                        view.setBackground(drawable);
                    }
                });
    }

}
