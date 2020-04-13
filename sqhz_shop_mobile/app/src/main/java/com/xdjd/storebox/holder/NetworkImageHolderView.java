package com.xdjd.storebox.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.xdjd.storebox.bean.HomeBean;

/**
 * 首页轮播图
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<HomeBean> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
//        View view = View.inflate(context, R.layout.item_artist_common, null);//头部内容
//        ButterKnife.bind(view);
//        mPictureImg = (ImageView) view.findViewById(R.id.picture_img);
//        mHomeArtistItemTvTitle = (TextView) view.findViewById(R.id.home_artist_item_tv_title);
//        mHomeArtistItemTvDescription = (TextView) view.findViewById(R.id.home_artist_item_tv_description);
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, HomeBean data) {
        Glide.with(context).load(data.getCover())
                .crossFade()
                .into(imageView);
    }
}
