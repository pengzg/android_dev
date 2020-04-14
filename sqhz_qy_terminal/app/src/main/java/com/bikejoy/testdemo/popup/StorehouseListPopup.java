package com.bikejoy.testdemo.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bikejoy.testdemo.R;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/22.
 * 库存管理柜子选择popup
 */

public class StorehouseListPopup extends PopupWindow implements StorehouseListAdapter.StorehouseListListener{
    private Context context;
    private OnStorehouseListener listener;
    private View view;

    private LinearLayout popLayout;
    private ListView lvList;

    private List<StoreListBean> list;

    private StorehouseListAdapter adapter;

    public void setData(List<StoreListBean> list){
        this.list = list;
        adapter.setData(list);
    }
    public StorehouseListPopup(Context context, OnStorehouseListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_storehouse_list,null);
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

        this.setTouchable(true);
        this.setOutsideTouchable(true);

        popLayout = (LinearLayout)view.findViewById(R.id.pop_layout);
        lvList = (ListView)view.findViewById(R.id.lv_list);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        adapter = new StorehouseListAdapter(this);
        lvList.setAdapter(adapter);
    }

    @Override
    public void onItemStore(int i) {
        listener.onItem(i);
    }

    public void setIndex(int index) {
        adapter.setIndex(index);
    }

    public interface OnStorehouseListener{
        void onItem(int i);
    }

}
