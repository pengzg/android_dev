package com.xdjd.distribution.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.BrandBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 品牌popup
 * Created by lijipei on 2017/5/14.
 */

public class BrandPopup extends PopupWindow {

    private View view;
    private ListView lv_brand;

    private ItemOnListener listener;

    public List<BrandBean> list;

    private MyAdapter adapter;

    public void setData(List<BrandBean> list) {
        this.list = list;
    }

    public BrandPopup(Context context, final ItemOnListener listener, int width) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_brand, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //点击外部消失
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        //        ColorDrawable dw = new ColorDrawable(R.color.transparent);
        //设置SelectPicPopupWindow弹出窗体的背景
        //        this.setBackgroundDrawable(dw);

        lv_brand = (ListView) view.findViewById(R.id.lv_brand);
        adapter = new MyAdapter();
        lv_brand.setAdapter(adapter);
    }

    public interface ItemOnListener {
        void onItem(int i);
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.mTvBrandName.setText(list.get(position).getBd_title());

            holder.mTvBrandName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItem(position);
                    dismiss();
                }
            });

            return view;
        }

        class ViewHolder {
            @BindView(R.id.tv_brand_name)
            TextView mTvBrandName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
