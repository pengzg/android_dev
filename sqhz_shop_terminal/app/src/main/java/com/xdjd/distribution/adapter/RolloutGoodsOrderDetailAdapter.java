package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PHOrderDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsOrderDetailAdapter extends BaseAdapter {

    List<PHOrderDetailBean> list;
    private int type;

    public void setData(List<PHOrderDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public RolloutGoodsOrderDetailAdapter(List<PHOrderDetailBean> list, int type) {
        this.list = list;
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rollout_goods_order_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PHOrderDetailBean bean = list.get(i);

        holder.mTvIndex.setText((i+1)+"");
        holder.mTvGoodName.setText(bean.getGoodsName());//商品名称
        if ("1".equals(bean.getUnitNum())) {
            holder.mTvMaxpriceDesc.setText("");
            holder.mTvMinpriceDesc.setText(bean.getMinPrice() + "元/" + bean.getMinName());//小单位价格描述
        } else {
            holder.mTvMaxpriceDesc.setText(bean.getMaxPrice() + "元/" + bean.getMaxName());//大单位价格描述
            holder.mTvMinpriceDesc.setText(bean.getMinPrice() + "元/" + bean.getMinName());//小单位价格描述
        }

        switch (type) {
            case 1:
                holder.mLlBottom.setVisibility(View.VISIBLE);
                holder.mTvTotalnumDesc.setText("总数量：" + bean.getPhTotalNum_desc());//铺货总数量描述
                holder.mTvRecallnumDesc.setText("已撤货:" + list.get(i).getRefundNum_desc());//撤货数量描述
                if ("".equals(bean.getPhSaleNum_desc()) || bean.getPhSaleNum_desc() == null) {
                    holder.mTvSalenumDesc.setText("已销售：" + "0");//已销售数量描述
                } else {
                    holder.mTvSalenumDesc.setText("已销售：" + bean.getPhSaleNum_desc());//已销售数量描述
                }
                if ("".equals(bean.getResidueNum_desc()) || bean.getResidueNum_desc() == null) {
                    holder.mTvSynumDesc.setText("未销售：" + "0");//剩余数量描述
                } else {
                    holder.mTvSynumDesc.setText("未销售：" + bean.getResidueNum_desc());//剩余数量描述
                }
                break;
            case 2://销售
                holder.mLlBottom.setVisibility(View.GONE);
                holder.mTvTotalnumDesc.setText(bean.getPhSaleNum_desc());
                break;
            case 3://撤货
                holder.mLlBottom.setVisibility(View.GONE);
                holder.mTvTotalnumDesc.setText(bean.getRefundNum_desc());
                break;
            case 4://申报单
                holder.mLlBottom.setVisibility(View.GONE);
                holder.mTvTotalnumDesc.setText(bean.getPhTotalNum_desc());
                break;
        }

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_good_name)
        TextView mTvGoodName;
        @BindView(R.id.tv_maxprice_desc)
        TextView mTvMaxpriceDesc;
        @BindView(R.id.tv_minprice_desc)
        TextView mTvMinpriceDesc;
        @BindView(R.id.tv_totalnum_desc)
        TextView mTvTotalnumDesc;
        @BindView(R.id.tv_recallnum_desc)
        TextView mTvRecallnumDesc;
        @BindView(R.id.tv_salenum_desc)
        TextView mTvSalenumDesc;
        @BindView(R.id.tv_synum_desc)
        TextView mTvSynumDesc;
        @BindView(R.id.ll_bottom)
        LinearLayout mLlBottom;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
