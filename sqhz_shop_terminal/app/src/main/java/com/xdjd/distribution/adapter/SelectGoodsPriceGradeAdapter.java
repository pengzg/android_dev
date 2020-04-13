package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SelectGoodsPriceGradeAdapter extends BaseAdapter {

    private ItemOnListener listener;
    private String gradeId = "";//选中的等级id
    public List<AddInfoBean> list;

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public void setData(List<AddInfoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public SelectGoodsPriceGradeAdapter(ItemOnListener listener) {
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
                    inflate(R.layout.item_select_goodsprice_grade, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvName.setText(list.get(i).getGpg_title());
        holder.mTvGradeDiscount.setText("折扣:"+list.get(i).getGpg_discount());

        if (gradeId != null && gradeId.equals(list.get(i).getGpg_id())) {
            holder.mIvIcon.setVisibility(View.VISIBLE);
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_blue));
        } else {
            holder.mIvIcon.setVisibility(View.GONE);
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
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
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_grade_discount)
        TextView mTvGradeDiscount;
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
