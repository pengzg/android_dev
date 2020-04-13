package com.xdjd.storebox.adapter;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.HomeGoodsBean;
import com.xdjd.utils.ImageLoadUtils;
import com.xdjd.utils.StringUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品推荐adapter
 * Created by lijipei on 2016/11/17.
 */

public class RecommendAdapter extends BaseAdapter {


    List<HomeGoodsBean> list;

    public void setData(List<HomeGoodsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommend, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //shape_c3c3c3_r0
        if (list.get(i).getShp_background_show() == null || "".equals(list.get(i).getShp_background_show())) {
            holder.mRecommendMainLl.setBackgroundResource(R.drawable.shape_c3c3c3_r0);
        } else {
            final LinearLayout ll = holder.mRecommendMainLl;
            Glide.with(viewGroup.getContext()).load(list.get(i).getShp_background_show())
                    .asBitmap()
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(bitmap);
                            ll.setBackground(drawable);
                        }
                    });
        }

        holder.mGoodsType.setText(list.get(i).getShp_title());

        holder.mGoodsName.setText(list.get(i).getGg_tittle());
        holder.mGoodsPrice.setText("¥  "+list.get(i).getGoods_price()+"/"+list.get(i).getUnitid_nameref());

        //ImageLoadUtils.loadImage(viewGroup.getContext(),list.get(i).getGb_cover(),holder.mGoodsImg);

       Glide.with(viewGroup.getContext()).load(list.get(i).
               getShp_url_show()).into(holder.mGoodsImg);

        /**
         * 设置字体的大小
         */
        /*Spannable span = StringUtils.getSpannableStr(list.get(i).getShp_title() + " 专区",
                16, 0, list.get(i).getShp_title().length());
        holder.mGoodsType.setText(span);*/

        return view;
    }


    class ViewHolder {
        @BindView(R.id.goods_type)
        TextView mGoodsType;
        @BindView(R.id.goods_name)
        TextView mGoodsName;
        @BindView(R.id.goods_price)
        TextView mGoodsPrice;
        @BindView(R.id.goods_img)
        ImageView mGoodsImg;
        @BindView(R.id.recommend_main_ll)
        LinearLayout mRecommendMainLl;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
