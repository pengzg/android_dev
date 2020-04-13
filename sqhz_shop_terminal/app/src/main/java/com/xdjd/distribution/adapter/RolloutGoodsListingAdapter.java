package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PHShopListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsListingAdapter extends BaseAdapter {

    List<PHShopListBean> listPHShop;
    private itemListener listener;

    public RolloutGoodsListingAdapter(itemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return listPHShop == null ? 0 : listPHShop.size();
    }

    @Override
    public Object getItem(int i) {
        return listPHShop.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rollout_goods_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(listPHShop.get(i).getShopName());//店铺名称
        holder.tvTotal.setText("已铺商品种类数："+listPHShop.get(i).getGoodsKindNum()+"种");//商品种类数
        holder.tvFirstTime.setText("首次铺货时间："+listPHShop.get(i).getFirstPhDate());//首次铺货时间
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.item(i);
            }
        });
        return view;
    }

    public void setData(List<PHShopListBean> listPHShop) {
        this.listPHShop = listPHShop;
        notifyDataSetChanged();
    }


    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.tv_first_time)
        TextView tvFirstTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface itemListener{
        public void item(int i);
    }
}
