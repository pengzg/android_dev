package com.xdjd.distribution.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseConfig;

/**
 * 收款类型popup
 * Created by lijipei on 2017/5/14.
 */

public class ReceiptTypePopup extends PopupWindow {

    private View view;
    private TextView tv_ysk,tv_qk;

    private ItemOnListener listener;

    public ReceiptTypePopup(Context context, final ItemOnListener listener, int width) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_receipt_type, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
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
        //        ColorDrawable dw = new ColorDrawable(R.color.transparent);
        //设置SelectPicPopupWindow弹出窗体的背景
        //        this.setBackgroundDrawable(dw);

        tv_ysk = (TextView) view.findViewById(R.id.tv_ysk);
        tv_qk = (TextView) view.findViewById(R.id.tv_qk);

        tv_ysk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemReceiptType(BaseConfig.ReceiptAdvancePayments);
                dismiss();
            }
        });

        tv_qk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemReceiptType(BaseConfig.ReceiptDebt);
                dismiss();
            }
        });

    }

    public interface ItemOnListener {
        void onItemReceiptType(int i);
    }


}
