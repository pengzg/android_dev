package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.StringFormatUtil;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsBuyListingAdapter extends BaseAdapter {

    private ItemOnListener listener;

    private int index = -1;

    public List<GoodsBean> list;

    public void removeItem(int i) {
        list.remove(i);
        notifyDataSetChanged();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public void setData(List<GoodsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public GoodsBuyListingAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public GoodsBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_goods_buy_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        GoodsBean bean = list.get(i);

        if (bean.getOa_applycode() !=null && bean.getOa_applycode().length() > 0){//还货商品
            holder.mNameTv.setText(bean.getGg_title()
                    + bean.getGg_model()+"["+bean.getOa_applycode()+"]");
        }else{
            String name = StringFormatUtil.fillColor(viewGroup.getContext(), (i + 1) + "." + list.get(i).getGg_title()
                    + list.get(i).getGg_model(), (i + 1) + ".", R.color.text_blue);
            //1 正 2 临 3残 4过
            if (1==bean.getGoodsStatus() || 0==bean.getGoodsStatus()){
                holder.mNameTv.setText(name);
            }else{
                String goodsStatusStr;
                switch (bean.getGoodsStatus()) {//商品状态 1 正 2 临 3残 4过
                    case Comon.GOODS_STATUS:
                        goodsStatusStr = "[正]";
                        break;
                    case Comon.GOODS_STATUS_L:
                        goodsStatusStr = "[临]";
                        break;
                    case Comon.GOODS_STATUS_C:
                        goodsStatusStr = "[残]";
                        break;
                    case Comon.GOODS_STATUS_G:
                        goodsStatusStr = "[过]";
                        break;
                    default:
                        goodsStatusStr = "";
                        break;
                }
                holder.mNameTv.setText(goodsStatusStr + name);
            }
        }

        if ("1".equals(list.get(i).getGgp_unit_num())){
            holder.mTvBuyNum.setText(list.get(i).getMinNum() + list.get(i).getGg_unit_min_nameref());
            holder.mTvUnitPrice.setText(list.get(i).getMinPrice() + "/" + list.get(i).getGg_unit_min_nameref());
        }else{
            holder.mTvBuyNum.setText(list.get(i).getMaxNum() + list.get(i).getGg_unit_max_nameref() +
                    list.get(i).getMinNum() + list.get(i).getGg_unit_min_nameref());
            holder.mTvUnitPrice.setText(list.get(i).getMaxPrice() + "/" + list.get(i).getGg_unit_max_nameref() +" "
                    +list.get(i).getMinPrice() + "/" + list.get(i).getGg_unit_min_nameref());
        }

        if (index == i) {
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvBuyNum.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvUnitPrice.setTextColor(UIUtils.getColor(R.color.white));
            holder.mLlMain.setBackgroundColor(UIUtils.getColor(R.color.color_blue_tinge));
        } else {
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvBuyNum.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvUnitPrice.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mLlMain.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.tv_buy_num)
        TextView mTvBuyNum;
        @BindView(R.id.tv_unit_price)
        TextView mTvUnitPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
