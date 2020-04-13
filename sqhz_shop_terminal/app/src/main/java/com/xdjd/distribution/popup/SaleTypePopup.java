package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/5/14.
 */

public class SaleTypePopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView lv;
    private SaleTypeAdapter adapter;

    private ItemOnListener listener;

    private List<SaleTypeBean> listName = new ArrayList<>();

    private String saleTypeId;

    public void setId(String saleTypeId) {
        this.saleTypeId = saleTypeId;
    }

    public void setData(List<SaleTypeBean> listName) {
        this.listName = listName;
    }


    public SaleTypePopup(Context context,int width, ItemOnListener listener) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_sale_type, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width/3);
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
        ColorDrawable dw = new ColorDrawable(R.color.transparent);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        lv = (ListView) view.findViewById(R.id.lv_sale_type);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lv.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lv.setLayoutParams(lp);


        adapter = new SaleTypeAdapter();
        lv.setAdapter(adapter);
    }

    public class SaleTypeAdapter extends BaseAdapter {

        public int getCount() {
            return listName == null ? 0 : listName.size();
        }

        @Override
        public Object getItem(int i) {
            return listName.get(i);
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
                        inflate(R.layout.item_sale_type, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvSaleType.setText(listName.get(i).getSp_name());

//            if (saleTypeId.equals(listName.get(i).getSp_id())) {
//                holder.mTvSaleType.setTextColor(UIUtils.getColor(R.color.white));
//                holder.mTvSaleType.setBackgroundColor(UIUtils.getColor(R.color.text_blue));
//            } else {
//                holder.mTvSaleType.setTextColor(UIUtils.getColor(R.color.text_black_212121));
//                holder.mTvSaleType.setBackgroundColor(UIUtils.getColor(R.color.white));
//            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItem(i);
                }
            });
            return view;
        }


        class ViewHolder {
            @BindView(R.id.tv_sale_type)
            TextView mTvSaleType;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
