package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.utils.UIUtils;

/**
 * 收款类型popup
 * Created by lijipei on 2017/5/14.
 */

public class PrinterTypePopup extends PopupWindow {

    private View view;
    private TextView tv_printer80, tv_printer58;

    private ItemOnListener listener;

    public PrinterTypePopup(Context context, final ItemOnListener listener, int width) {
        super(context);
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_printer_type, null);

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
        ColorDrawable dw = new ColorDrawable(UIUtils.getColor(R.color.transparent));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        tv_printer80 = (TextView) view.findViewById(R.id.tv_printer80);
        tv_printer58 = (TextView) view.findViewById(R.id.tv_printer58);

        tv_printer80.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemPrinterType(BaseConfig.printer80);
                dismiss();
            }
        });

        tv_printer58.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemPrinterType(BaseConfig.printer58);
                dismiss();
            }
        });

    }

    public interface ItemOnListener {
        void onItemPrinterType(int i);
    }


}
