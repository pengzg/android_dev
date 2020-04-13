package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.GoodsCategoryOneAdapter;
import com.xdjd.distribution.adapter.GoodsCategoryTwoAdapter;
import com.xdjd.distribution.base.GoodsCategoryBean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/22.
 */

public class GoodsCategoryPopup extends BasePopop implements GoodsCategoryOneAdapter.GoodsCategoryOneListener,
        GoodsCategoryTwoAdapter.GoodsCategoryTwoListener{
    private Context context;
    private OnGoodsCategoryListener listener;
    private View view;

    private LinearLayout popLayout;
    private ListView lvLeft,lvRight;

    private List<GoodsCategoryBean> list;

    private GoodsCategoryOneAdapter adapterFirst;
    private GoodsCategoryTwoAdapter adapterSecond;

    public void setData(List<GoodsCategoryBean> list){
        this.list = list;
        adapterFirst.setData(list);
    }
    public GoodsCategoryPopup(Context context,OnGoodsCategoryListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_goods_category_select_layout,null);
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
        lvLeft = (ListView)view.findViewById(R.id.lv_left);
        lvRight = (ListView)view.findViewById(R.id.lv_right);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        adapterFirst = new GoodsCategoryOneAdapter(this);
        adapterSecond = new GoodsCategoryTwoAdapter(this);
        lvLeft.setAdapter(adapterFirst);
        lvRight.setAdapter(adapterSecond);
    }

    @Override
    public void onItemOne(int i) {
        if (list.get(i).getSecondCategoryList()!=null && list.get(i).getSecondCategoryList().size()>0){
            adapterSecond.setData(list.get(i).getSecondCategoryList());
        }else{
            listener.onItemCategory(i,-1);
        }
    }

    @Override
    public void onItemTwo(int i) {//第二级分类
        listener.onItemCategory(adapterFirst.getIndex(),i);
    }

    public void setIndex(int firstIndex, int secondIndex) {
        adapterFirst.setIndex(firstIndex);
        adapterSecond.setIndex(secondIndex);
        adapterSecond.setData(list.get(firstIndex).getSecondCategoryList());
    }

    public interface OnGoodsCategoryListener{
        void onItemCategory(int first,int second);
    }

}
