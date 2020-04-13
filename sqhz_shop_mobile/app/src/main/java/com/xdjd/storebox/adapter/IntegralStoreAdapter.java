package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.IntegralGoodsBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class IntegralStoreAdapter extends BaseAdapter {
    private detailListener listener;
    private List<IntegralGoodsBean>list;
    public IntegralStoreAdapter(detailListener listener) {
        this.listener = listener;
    }

    public void setData(List<IntegralGoodsBean>list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null==list? 0:list.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingegral_store_list, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.item(list.get(i).getWig_id(),list.get(i).getWig_stock());
            }
        });
        Glide.with(viewGroup.getContext()).load(list.get(i).getPath()).into(viewHolder.goodsImage);
        viewHolder.goodsName.setText(list.get(i).getGoods_title());//商品名称
        viewHolder.integralNum.setText(list.get(i).getWig_integrate_price());//兑换积分数
        viewHolder.price.setText(list.get(i).getGp_wholesale_price());//市场价格
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.goods_image)
        ImageView goodsImage;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.integral_num)
        TextView integralNum;
        @BindView(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface detailListener{
        void item(int wig_id,int stock);
    }
}
