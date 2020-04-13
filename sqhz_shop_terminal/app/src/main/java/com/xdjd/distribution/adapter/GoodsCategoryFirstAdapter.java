package com.xdjd.distribution.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/11
 *     desc   : 公共模板
 *     version: 1.0
 * </pre>
 */

public class GoodsCategoryFirstAdapter extends BaseAdapter {

    List<GoodsCategoryBean> list;
    public int index = 0;
    private OnGoodsCategoryFirstListener listener;

    public GoodsCategoryFirstAdapter(OnGoodsCategoryFirstListener listener) {
        this.listener = listener;
    }

    public void setList(List<GoodsCategoryBean> list) {
        this.list = list;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods_category_one, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mNameTv.setText(list.get(i).getGc_name());

        if (index == i) {
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_blue));
            holder.mNameTv.setBackgroundColor(UIUtils.getColor(R.color.white));
            holder.mNameTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            holder.mLine.setVisibility(View.VISIBLE);
        } else {
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mNameTv.setBackgroundColor(UIUtils.getColor(R.color.color_f5f5f5));
            holder.mNameTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            holder.mLine.setVisibility(View.INVISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });

        return view;
    }

    public interface OnGoodsCategoryFirstListener{
        void onItem(int i);
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
