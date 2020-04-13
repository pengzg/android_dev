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
import com.xdjd.distribution.adapter.LineAdapter;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线路选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LinePopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView list;
    private LineAdapter adapter;

    private ItemOnListener listener;

    private List<LineBean> listName;
    private String name;
    private String lineId;

    public void setData(List<LineBean> listName){
        this.listName = listName;
        adapter.setData(listName);
    }

    public void setItem(String name){
        this.name = name;
    }

    public void setId(String lineId){
        this.lineId = lineId;
        adapter.setId(lineId);
        adapter.notifyDataSetChanged();
    }

    public LinePopup(Context context,int height, ItemOnListener listener) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_lines, null);

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
        lp.height = height/2;
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



        list = (ListView) view.findViewById(R.id.lines_lv);
        adapter = new LineAdapter(listener);
        adapter.setId(lineId);
        list.setAdapter(adapter);
    }

}
