package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsCategoryTwoAdapter extends BaseAdapter {

    private GoodsCategoryTwoListener listener;

    private int index = -1;
    List<GoodsCategoryBean> list;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public void setData(List<GoodsCategoryBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public GoodsCategoryTwoAdapter(GoodsCategoryTwoListener listener) {
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
                    inflate(R.layout.item_goods_category_two, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GoodsCategoryBean bean = list.get(i);
        if (bean.getGc_name() == null || "".equals(bean.getGc_name())) {//还货分类名称
            holder.mLlGiveback.setVisibility(View.VISIBLE);
            holder.mTvCode.setText(bean.getOa_applycode());

            if (bean.getOa_remarks() == null || bean.getOa_remarks().length() == 0) {
                holder.mTvRemark.setVisibility(View.GONE);
            } else {
                holder.mTvRemark.setVisibility(View.VISIBLE);
                holder.mTvRemark.setText("备注:" + bean.getOa_remarks());
            }

        } else {
            holder.mLlGiveback.setVisibility(View.GONE);
            holder.mNameTv.setText(bean.getGc_name());
        }

        if (index == i) {
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_blue));
            holder.mTvCode.setTextColor(UIUtils.getColor(R.color.text_blue));
            holder.mLine.setVisibility(View.VISIBLE);
            holder.mRightLine.setVisibility(View.INVISIBLE);
        } else {
            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvCode.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mLine.setVisibility(View.INVISIBLE);
            holder.mRightLine.setVisibility(View.VISIBLE);
        }

        if (index == i) {
            holder.mLlChild.setBackgroundColor(UIUtils.getColor(R.color.white));
        } else {
            holder.mLlChild.setBackgroundColor(UIUtils.getColor(R.color.color_f5f5f5));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                index = i;
                //                notifyDataSetChanged();
                listener.onItemTwo(i);
            }
        });
        return view;
    }

    public interface GoodsCategoryTwoListener {
        void onItemTwo(int i);
    }

    class ViewHolder {
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_remark)
        TextView mTvRemark;
        @BindView(R.id.ll_giveback)
        LinearLayout mLlGiveback;
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.right_line)
        View mRightLine;
        @BindView(R.id.ll_child)
        LinearLayout mLlChild;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
