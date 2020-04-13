package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.MainStockBean;
import com.xdjd.utils.LogUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MainStorageAdapter extends BaseAdapter {

    public List<MainStockBean> list;

    public void setData(List<MainStockBean> list) {
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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_main_storage, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText(String.valueOf(i+1));
        holder.mTvGoodsName.setText(list.get(i).getGg_title() + " " + (list.get(i).getGg_model() == null?"":list.get(i).getGg_model()));

        if ("".equals(list.get(i).getGgp_unit_num()) || list.get(i).getGgp_unit_num() == null){
            holder.mTvStock.setText(list.get(i).getGgs_stock()+list.get(i).getGg_unit_min_nameref());
        }else{
            if (!"".equals(list.get(i).getGgs_stock()) && list.get(i).getGgs_stock() != null){
                BigDecimal bdStock = new BigDecimal(list.get(i).getGgs_stock());
                BigDecimal bdUnitNum = new BigDecimal(list.get(i).getGgp_unit_num());

                if (bdUnitNum.intValue() == 1){
                    holder.mTvStock.setText(bdStock+list.get(i).getGg_unit_min_nameref());
                }else{
                    BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);
                    holder.mTvStock.setText(results[0]+list.get(i).getGg_unit_max_nameref()+results[1]+list.get(i).getGg_unit_min_nameref());
                }
            }else{
                holder.mTvStock.setText("无库存");
            }
        }
        holder.mTvStorehouseName.setText(list.get(i).getGgs_storehouseid_nameref());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_storehouse_name)
        TextView mTvStorehouseName;
        @BindView(R.id.tv_stock)
        TextView mTvStock;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
