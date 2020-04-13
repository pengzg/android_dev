package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.UIUtils;

import java.math.BigDecimal;
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

public class GoodsListAdapter extends BaseAdapter {

    private GoodsListListener listener;
    private boolean isGiveback = false;//是否是还货
    private String applyOrderCode;//还货订单单号,用来区分商品的订单区别

    private int index = -1;
    public List<GoodsBean> list;
    //    String nameStr = "[150件] 200ML*6版(24瓶) 塑料QQ星儿童乳品草莓味";

    public int getIndex() {
        return index;
    }

    /**
     * 普通商品刷新列表方法
     * @param list
     */
    public void setData(List<GoodsBean> list) {
        this.list = list;
        index = -1;
        isGiveback = false;
        notifyDataSetChanged();
    }

    /**
     * 还货商品刷新列表方法
     * @param list
     * @param isGiveback
     */
    public void setData(List<GoodsBean> list,boolean isGiveback,String applyOrderCode){
        this.list = list;
        index = -1;
        this.isGiveback = isGiveback;
        this.applyOrderCode = applyOrderCode;
        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public GoodsListAdapter(GoodsListListener listener) {
        this.listener = listener;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GoodsBean bean = list.get(i);

        if (isGiveback) {//还货商品
            if (applyOrderCode==null||"".equals(applyOrderCode)){
                holder.mTvName.setText(bean.getGg_title() + " " + bean.getGg_model());
            }else{
                holder.mTvName.setText(bean.getGg_title() + " " + bean.getGg_model()+"["+applyOrderCode+"]");
            }

            BigDecimal unitNum = new BigDecimal(bean.getGgp_unit_num());

            if ("1".equals(bean.getGgp_unit_num())) {
                holder.mTvSurplusNum.setText("剩余" + bean.getOrder_surplus_num() + bean.getGg_unit_min_nameref());
            } else {
                BigDecimal surplusNum = new BigDecimal(bean.getOrder_surplus_num());
                BigDecimal[] results = surplusNum.divideAndRemainder(unitNum);

                holder.mTvSurplusNum.setText("剩余" + results[0] + bean.getGg_unit_max_nameref() + results[1] + bean.getGg_unit_min_nameref());
            }
            bean.setOrder_surplus_num_str(holder.mTvSurplusNum.getText().toString());

            holder.mTvSurplusNum.setVisibility(View.VISIBLE);
        } else {//普通商品
            holder.mTvName.setText(bean.getGg_title() + " " + bean.getGg_model());
            holder.mTvSurplusNum.setVisibility(View.GONE);
        }

        if (index == i) {
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvSurplusNum.setTextColor(UIUtils.getColor(R.color.white));
            holder.mLlView.setBackgroundColor(UIUtils.getColor(R.color.color_blue_tinge));
        } else {
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvSurplusNum.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mLlView.setBackgroundColor(UIUtils.getColor(R.color.transparent));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemGoods(i);
            }
        });
        return view;
    }

    public interface GoodsListListener {
        void onItemGoods(int i);
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_surplus_num)
        TextView mTvSurplusNum;
        @BindView(R.id.ll_view)
        LinearLayout mLlView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
