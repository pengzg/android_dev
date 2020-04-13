package com.xdjd.view.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by freestyle_hong on 2017/9/18.
 */

public class BalanceFilterPopupWindow extends PopupWindow {
    private Context context;
    private View view;
    public BalanceFilterPopupWindow(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // view = LayoutInflater.from(context).inflate()
    }
}
