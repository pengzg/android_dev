package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.xdjd.distribution.bean.OrderStatsBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 仓库选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   : 选择业务员popup
 *     version: 1.0
 * </pre>
 */

public class OrderStatePopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView list;
    private OrderStateAdapter adapter;

    private ItemOnListener listener;

    private List<OrderStatsBean> listName = new ArrayList<>();
    private String salesmanId = null;

    public void setData(List<OrderStatsBean> listName) {
        this.listName = listName;
    }

    public void setId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public OrderStatePopup(Context context, int height, ItemOnListener listener) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_order_state, null);

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

        ll = (LinearLayout) view.findViewById(R.id.pop_layout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        lp.height = height / 2;
        ll.setLayoutParams(lp);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
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

        list = (ListView) view.findViewById(R.id.lv_salesman);
        adapter = new OrderStateAdapter();
        list.setAdapter(adapter);
    }

    public class OrderStateAdapter extends BaseAdapter {

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
                        inflate(R.layout.item_state_select, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvName.setText(listName.get(i).getBd_name());

            if (salesmanId != null && salesmanId.equals(listName.get(i).getBd_code())) {
                holder.mTvImg.setVisibility(View.VISIBLE);
                holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_blue));
            } else {
                holder.mTvImg.setVisibility(View.GONE);
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
            @BindView(R.id.tv_img)
            ImageView mTvImg;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
