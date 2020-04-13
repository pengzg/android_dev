package com.xdjd.distribution.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.AnimatedExpandableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsCategotyAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    List<GoodsCategoryBean> list;
    public int index = 0;
    public int index2 = 0;

    public void setData(List<GoodsCategoryBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getRealChildrenCount(int i) {
        return list.get(i).getSecondCategoryList() == null ? 0 : list.get(i).getSecondCategoryList().size();
    }


    @Override
    public GoodsCategoryBean getGroup(int i) {
        return list.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getSecondCategoryList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    //获得子列表项的Id
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_goods_category_one, viewGroup, false);
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
        return view;
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

    @Override
    public View getRealChildView(final int i, final int i1, boolean b, View view, ViewGroup parent) {
        ViewHolderChild holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_goods_category_two, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }
        GoodsCategoryBean bean = list.get(i).getSecondCategoryList().get(i1);
        if (bean.getGc_name() == null || "".equals(bean.getGc_name())) {//还货分类名称
            holder.mLlGiveback.setVisibility(View.VISIBLE);
            holder.mTvCode.setText(bean.getOa_applycode());

            if (bean.getOa_remarks() == null || bean.getOa_remarks().length() == 0){
                holder.mTvRemark.setVisibility(View.GONE);
            }else{
                holder.mTvRemark.setVisibility(View.VISIBLE);
                holder.mTvRemark.setText("备注:" + bean.getOa_remarks());
            }

        } else {
            holder.mLlGiveback.setVisibility(View.GONE);
            holder.mNameTv.setText(bean.getGc_name());
        }

        if (index2 == i1) {
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

        return view;
    }


    //获得子列表项，比较重要、难理解的 一个方法
    //    @Override
    //    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
    //
    //    }

    class ViewHolderChild {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.tv_remark)
        TextView mTvRemark;
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.ll_child)
        LinearLayout mLlChild;
        @BindView(R.id.right_line)
        View mRightLine;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.ll_giveback)
        LinearLayout mLlGiveback;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
