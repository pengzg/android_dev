package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.NearbyShopAdapter;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.EaseTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/22.
 */

public class NearbyShopPopup extends BasePopop {
    private Context context;
    private ItemOnListener listener;
    private View view;
    private NearbyShopAdapter adapter;

    private LinearLayout popLayout;
    private ListView listView;

    private List<NearbyShopBean> shopList;
    public void setData(List<NearbyShopBean> list){
        this.shopList = list;
        adapter.notifyDataSetChanged();
    }
    public NearbyShopPopup(Context context,int height,ItemOnListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_nearby_shop,null);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        popLayout = (LinearLayout)view.findViewById(R.id.pop_layout);
        listView = (ListView)view.findViewById(R.id.listView);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) popLayout.getLayoutParams();
        lp.height = height / 2+ UIUtils.dp2px(100);
        popLayout.setLayoutParams(lp);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        adapter = new NearbyShopAdapter(listener);
        listView.setAdapter(adapter);
    }

    public class NearbyShopAdapter extends BaseAdapter {
        private ItemOnListener listener;

        public NearbyShopAdapter(ItemOnListener listener) {
            this.listener = listener;
        }


        @Override
        public int getCount() {
            return null == shopList ? 0:shopList.size();
        }

        @Override
        public Object getItem(int position) {
            return shopList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nearby_shop, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            if(!"".equals(shopList.get(i).getCcl_img())&& null != shopList.get(i).getCcl_img()){
                Glide.with(viewGroup.getContext()).load(shopList.get(i).getCcl_img()).into(viewHolder.csImage);
            }
            viewHolder.tvName.setText(shopList.get(i).getCcl_name());
            viewHolder.tvContactsName.setText(shopList.get(i).getCcl_contacts_name());
            viewHolder.tvMobile.setText(shopList.get(i).getCcl_contacts_mobile());
            viewHolder.tvAddress.setText(shopList.get(i).getCcl_address());
            if(Integer.valueOf(shopList.get(i).getDistance()) > 1000){
                viewHolder.distance.setText(Integer.valueOf(shopList.get(i).getDistance())/1000 + "公里");
            }else{
                viewHolder.distance.setText(shopList.get(i).getDistance()+"米");
            }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItem(i);
                }
            });
            return view;
        }

         class ViewHolder {
            @BindView(R.id.cs_image)
            CircleImageView csImage;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_contacts_name)
            TextView tvContactsName;
            @BindView(R.id.tv_mobile)
            TextView tvMobile;
            @BindView(R.id.tv_address)
            TextView tvAddress;
            @BindView(R.id.iv_location)
            ImageView ivLocation;
            @BindView(R.id.distance)
            TextView distance;


            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
