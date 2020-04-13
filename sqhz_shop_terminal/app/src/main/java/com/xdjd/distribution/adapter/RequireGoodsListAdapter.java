package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.GoodsBean;
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

public class RequireGoodsListAdapter extends BaseAdapter {

    private RequireGoodsListListener listener;

    private int index = -1;
    public List<GoodsBean> list;
//    String nameStr = "[150件] 200ML*6版(24瓶) 塑料QQ星儿童乳品草莓味";

    public int getIndex() {
        return index;
    }

    public void setData(List<GoodsBean> list) {
        this.list = list;
        index = -1;
        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public RequireGoodsListAdapter(RequireGoodsListListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null ? 0:list.size();
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
                    inflate(R.layout.item_require_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String name = list.get(i).getGg_title()+" "+list.get(i).getGg_model();
        holder.mNameTv.setText(name);

        if (index == i){
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_blue));
            holder.mLine.setVisibility(View.VISIBLE);
        }else{
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mLine.setVisibility(View.INVISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemGoods(i);
            }
        });
        return view;
    }

    public interface RequireGoodsListListener{
        void onItemGoods(int i);
    }

    class ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.line)
        View mLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
