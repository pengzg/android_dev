package com.bikejoy.testdemo.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bikejoy.utils.UIUtils;
import com.bikejoy.testdemo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 是否区分生产日期popup
 * Created by lijipei on 2017/5/14.
 */

public class DomainPopup extends PopupWindow {

    private View view;
    private ListView lvDomain;

    private ItemOnListener listener;

    public List<DomainBean> list;

    private MyAdapter adapter;

    public void setData(List<DomainBean> list) {
        this.list = list;
        adapter.notifyDataSetChanged();
    }

    public DomainPopup(Context context, final ItemOnListener listener, EditText et, final OnDismissPopListener listener1) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_select_domain, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(et.getWidth());
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //点击外部消失
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(UIUtils.getColor(R.color.transparent));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        lvDomain = (ListView) view.findViewById(R.id.lv_domain);

        adapter = new MyAdapter();
        lvDomain.setAdapter(adapter);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                listener1.onDismiss();
            }
        });//添加一个消失的监听
    }

    public interface OnDismissPopListener{
        void onDismiss();
    }

    public interface ItemOnListener {
        void onItem(int i);

        void onItemDel(int i);
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records_domain_select, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.mTvName.setText(list.get(position).getUrl());

            holder.mTvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItem(position);
                }
            });

            holder.mRlDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemDel(position);
                }
            });

            return view;
        }

        class ViewHolder {
            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.rl_del)
            RelativeLayout mRlDel;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
