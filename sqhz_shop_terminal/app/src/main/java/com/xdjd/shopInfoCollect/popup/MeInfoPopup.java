package com.xdjd.shopInfoCollect.popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.shopInfoCollect.activity.MeInfoActivity;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

/**
 * Created by freestyle_hong on 2017/9/28.
 */

public class MeInfoPopup extends PopupWindow{
    private Context context;
    private View view;
    private LinearLayout popLayout;
    private LinearLayout llPersonInfo;
    private LinearLayout llSetup;
    private TextView tv_mobile;
    private personInfoListener personInfoListener;
    private UserBean userBean;

    public MeInfoPopup(Context context, int width, final ItemOnListener listener, final personInfoListener personInfoListener) {
        this.context = context;
        this.personInfoListener = personInfoListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_me_info,null);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimLeft);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        popLayout = (LinearLayout)view.findViewById(R.id.pop_layout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) popLayout.getLayoutParams();
        lp.width = width / 2 + UIUtils.dp2px(60);
        popLayout.setLayoutParams(lp);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int width = view.findViewById(R.id.pop_layout).getRight();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x > width) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        llPersonInfo = (LinearLayout)view.findViewById(R.id.person_info);
        llSetup = (LinearLayout)view.findViewById(R.id.set_up);
        tv_mobile = (TextView)view.findViewById(R.id.mobile);
        userBean = UserInfoUtils.getUser(view.getContext());
        tv_mobile.setText(userBean.getUserName());
        llPersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                personInfoListener.itemPerson();
            }
        });
        llSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                personInfoListener.itemSetup();
            }
        });

    }
     public interface personInfoListener{
         public void itemPerson();
         public void itemSetup();
     }
}
