package com.xdjd.distribution.popup;

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

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.GoRolloutGoodsAdapter;
import com.xdjd.distribution.adapter.LineAdapter;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   : 商品购物车popup
 *     version: 1.0
 * </pre>
 */

public class GoodsCartPopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView list;
    private GoRolloutGoodsAdapter adapter;

    private ItemOnListener listener;

    public GoodsCartPopup(Context context, int height) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_go_rollout_cart, null);

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
        adapter = new GoRolloutGoodsAdapter(null);
        list.setAdapter(adapter);
    }

    public void showGoodsCartPopup(View v){
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        this.showAtLocation(v, Gravity.TOP,
                0,-UIUtils.dp2px(90));
    }

}
