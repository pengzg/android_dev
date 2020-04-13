package com.xdjd.storebox.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.xdjd.storebox.bean.HomeActivityBean;
import com.xdjd.storebox.bean.HomeBean;

/**
 * 首页活动轮播图
 * Created by Sai on 15/8/4.
 */
public class ActionImageHolderView implements Holder<HomeActivityBean> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, HomeActivityBean data) {
        Glide.with(context).load(data.getActivityCover())
                .crossFade()
                .into(imageView);
    }
}
