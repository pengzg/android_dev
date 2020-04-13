package com.xdjd.storebox.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

/**
 * Created by Sai on 15/8/4.
 * 商品详情图片网络加载
 */
public class GoodsDetailImageHolderView implements Holder<String> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String str) {
        Glide.with(context).load(str)
                .crossFade()
                .into(imageView);
    }
}
