package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xdjd.distribution.R;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.UIUtils;

/**
 * 收款类型popup
 * Created by lijipei on 2017/5/14.
 */

public class SearchPopup extends PopupWindow {

    private View view;
    private RelativeLayout rlMain;
    private LinearLayout llMain, llSearch, llX;
    private EditText etSearch;

    private ItemOnListener listener;

    private Context mContext;

    public SearchPopup(Context context,View v, final ItemOnListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_search, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(v.getHeight());
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //点击外部消失
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(UIUtils.getColor(R.color.tran_black3));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
        llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        llSearch = (LinearLayout) view.findViewById(R.id.ll_search);
        llX = (LinearLayout) view.findViewById(R.id.ll_x);

        etSearch = (EditText) view.findViewById(R.id.et_search);

        //        llX.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                listener.onItemPrinterType(BaseConfig.printer80);
        //                dismiss();
        //            }
        //        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSearch(etSearch.getText().toString());
                disPopup();
            }
        });

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disPopup();
            }
        });

    }

    public void setSearchStr(String searchStr){
        etSearch.setText(searchStr);
    }

    public interface ItemOnListener {
        void onItemSearch(String str);
    }

    public void showPopup(){
        AnimUtils.translateAnimIn(mContext,view);
    }

    public void disPopup(){
        AnimUtils.translateAnimOut(mContext,view,this);
    }

}
