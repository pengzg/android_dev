package com.bikejoy.testdemo.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bikejoy.testdemo.R;

import java.util.List;

/**
 * 线路选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsListPopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView list;
    private GoodsSelectListAdapter adapter;

    private GoodsSelectListAdapter.ItemOnListener listener;

    private List<RelationGoodsBean> listName;
    private String name;
    private String goodsId;

    public void setData(List<RelationGoodsBean> listName){
        this.listName = listName;
        adapter.setData(listName);
    }

    public void setItem(String name){
        this.name = name;
    }

    public void setId(String goodsId){
        this.goodsId = goodsId;
        adapter.setId(goodsId);
        adapter.notifyDataSetChanged();
    }

    public GoodsListPopup(Context context, int height, GoodsSelectListAdapter.ItemOnListener listener) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_goods_list, null);

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
        lp.height = height/3*2;
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

        list = (ListView) view.findViewById(R.id.lv_goods);
        adapter = new GoodsSelectListAdapter(listener);
        adapter.setId(goodsId);
        list.setAdapter(adapter);
    }

    public void showPopup(LinearLayout mLlMain){
        showAtLocation(mLlMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

}
