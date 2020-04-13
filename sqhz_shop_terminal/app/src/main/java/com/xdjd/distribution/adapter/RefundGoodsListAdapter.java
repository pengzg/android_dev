package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.UIUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/25
 *     desc   : 退货商品列表adapter
 *     version: 1.0
 * </pre>
 */

public class RefundGoodsListAdapter extends BaseAdapter {

    public List<GoodsBean> list;

    public void setData(List<GoodsBean> list) {
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_refund_goods_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        GoodsBean bean = list.get(i);

        holder.mTvGoodsName.setText(bean.getGg_title());
        holder.mTvGoodsModel.setText(bean.getGg_model());
        if ("1".equals(bean.getGgp_unit_type())){//小单位
            holder.mTvGoodsUnit.setText(bean.getGg_unit_min_nameref());

            holder.mTvStock.setText(bean.getGgs_stock());
            holder.mTvStockLq.setText(bean.getGgs_stock_lq());
            holder.mTvStockCc.setText(bean.getGgs_stock_cc());
            holder.mTvStockGq.setText(bean.getGgs_stock_gq());
        }else{//大单位
            holder.mTvGoodsUnit.setText(bean.getGg_unit_max_nameref());
            holder.mTvStock.setText(UnitCalculateUtils.unitStr(bean.getGgp_unit_num(),
                    bean.getGgs_stock(),bean.getGg_unit_max_nameref(),bean.getGg_unit_min_nameref()));
            holder.mTvStockLq.setText(UnitCalculateUtils.unitStr(bean.getGgp_unit_num(),
                    bean.getGgs_stock_lq(),bean.getGg_unit_max_nameref(),bean.getGg_unit_min_nameref()));
            holder.mTvStockCc.setText(UnitCalculateUtils.unitStr(bean.getGgp_unit_num(),
                    bean.getGgs_stock_cc(),bean.getGg_unit_max_nameref(),bean.getGg_unit_min_nameref()));
            holder.mTvStockGq.setText(UnitCalculateUtils.unitStr(bean.getGgp_unit_num(),
                    bean.getGgs_stock_gq(),bean.getGg_unit_max_nameref(),bean.getGg_unit_min_nameref()));
        }

        if (0 == bean.getIsSelect()){
            holder.mIvIcon.setImageResource(R.drawable.check_true);
        }else{
            holder.mIvIcon.setImageResource(R.drawable.check_false);
        }

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_goods_model)
        TextView mTvGoodsModel;
        @BindView(R.id.tv_goods_unit)
        TextView mTvGoodsUnit;
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_stock)
        TextView mTvStock;
        @BindView(R.id.tv_stock_lq)
        TextView mTvStockLq;
        @BindView(R.id.tv_stock_cc)
        TextView mTvStockCc;
        @BindView(R.id.tv_stock_gq)
        TextView mTvStockGq;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
