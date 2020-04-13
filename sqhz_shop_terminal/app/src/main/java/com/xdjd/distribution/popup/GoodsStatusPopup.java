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

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.GoodsStatusBean;
import com.xdjd.distribution.callback.ItemOnListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/5/14.
 * 商品类型id
 */

public class GoodsStatusPopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView lv;
    private GoodsStatusAdapter adapter;

    private OnGoodsStatusListener listener;

    private List<GoodsStatusBean> listStatus = new ArrayList<>();

    private String statusId;

    public void setId(String statusId) {
        this.statusId = statusId;
    }

    public void setData(List<GoodsStatusBean> listStatus) {
        this.listStatus = listStatus;
    }


    public GoodsStatusPopup(Context context, int width, OnGoodsStatusListener listener) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_goods_status, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width / 3);
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

        lv = (ListView) view.findViewById(R.id.lv_goods_status);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lv.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lv.setLayoutParams(lp);


        adapter = new GoodsStatusAdapter();
        lv.setAdapter(adapter);
    }

    public class GoodsStatusAdapter extends BaseAdapter {

        public int getCount() {
            return listStatus == null ? 0 : listStatus.size();
        }

        @Override
        public Object getItem(int i) {
            return listStatus.get(i);
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
                        inflate(R.layout.item_goods_status, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            switch (listStatus.get(i).getCode()){
                case Comon.GOODS_STATUS:
                    holder.mIvGoodsStatus.setImageResource(R.mipmap.goods_status);
                    break;
                case Comon.GOODS_STATUS_C:
                    holder.mIvGoodsStatus.setImageResource(R.mipmap.goods_status_cc);
                    break;
                case Comon.GOODS_STATUS_G:
                    holder.mIvGoodsStatus.setImageResource(R.mipmap.goods_status_gq);
                    break;
                case Comon.GOODS_STATUS_L:
                    holder.mIvGoodsStatus.setImageResource(R.mipmap.goods_status_lq);
                    break;
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGoodsStatus(i);
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.iv_goods_status)
            ImageView mIvGoodsStatus;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

    public interface OnGoodsStatusListener{
        void onGoodsStatus(int i);
    }
}
