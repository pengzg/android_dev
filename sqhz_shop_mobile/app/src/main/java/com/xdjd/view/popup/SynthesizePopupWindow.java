package com.xdjd.view.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 综合选择popup
 * Created by lijipei on 2016/12/9.
 */

public class SynthesizePopupWindow extends PopupWindow {

    private View view;

    public int index;

    //综合分类的id
    private TextView synthesize_tv1,synthesize_tv2,synthesize_tv3;

    public SynthesizePopupWindow(Context context,int type, final synthesizePopupLisenter listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_synthesize_layout, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        this.setTouchable(true);
        this.setOutsideTouchable(true);

        AutoUtils.auto(view);
        this.setContentView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        synthesize_tv1 = (TextView) view.findViewById(R.id.synthesize_tv1);
        synthesize_tv2 = (TextView) view.findViewById(R.id.synthesize_tv2);
        synthesize_tv3 = (TextView) view.findViewById(R.id.synthesize_tv3);

        index=type;

        switch (type){
            case PublicFinal.sx1:
                setSynthesize(PublicFinal.sx1);
                break;
            case PublicFinal.sx5:
                setSynthesize(PublicFinal.sx5);
                break;
            case PublicFinal.sx6:
                setSynthesize(PublicFinal.sx6);
                break;
        }

        synthesize_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.synthesizeType(PublicFinal.sx1,"综合");
                index = PublicFinal.sx1;
                setSynthesize(PublicFinal.sx1);
                dismiss();
            }
        });

        synthesize_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.synthesizeType(PublicFinal.sx5,"新品");
                index = PublicFinal.sx5;
                setSynthesize(PublicFinal.sx5);
                dismiss();
            }
        });

        synthesize_tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.synthesizeType(PublicFinal.sx6,"人气");
                index = PublicFinal.sx6;
                setSynthesize(PublicFinal.sx6);
                dismiss();
            }
        });
    }

    private void setSynthesize(int type){
        switch (type){
            case PublicFinal.sx1:
                synthesize_tv1.setTextColor(UIUtils.getColor(R.color.text_df1122));
                synthesize_tv2.setTextColor(UIUtils.getColor(R.color.text_212121));
                synthesize_tv3.setTextColor(UIUtils.getColor(R.color.text_212121));
                break;
            case PublicFinal.sx5:
                synthesize_tv1.setTextColor(UIUtils.getColor(R.color.text_212121));
                synthesize_tv2.setTextColor(UIUtils.getColor(R.color.text_df1122));
                synthesize_tv3.setTextColor(UIUtils.getColor(R.color.text_212121));
                break;
            case PublicFinal.sx6:
                synthesize_tv1.setTextColor(UIUtils.getColor(R.color.text_212121));
                synthesize_tv2.setTextColor(UIUtils.getColor(R.color.text_212121));
                synthesize_tv3.setTextColor(UIUtils.getColor(R.color.text_df1122));
                break;
        }
    }

    /**
     * 综合选择回调监听
     */
    public interface synthesizePopupLisenter{
        void synthesizeType(int position,String title);
    }
}
