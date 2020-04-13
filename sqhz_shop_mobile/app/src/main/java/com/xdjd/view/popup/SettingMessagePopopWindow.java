package com.xdjd.view.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.xdjd.storebox.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 设置个人信息提示popup
 * Created by lijipei on 2017/2/10.
 */

public class SettingMessagePopopWindow extends PopupWindow {

    private View view;
    private Button btn;

    public SettingMessagePopopWindow(Context context, final SettingPopupLisenter listener){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_setting_message_layout, null);
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

        btn = (Button) view.findViewById(R.id.message_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.settingPopupListener();
            }
        });

    }

    /**
     * 综合选择回调监听
     */
    public interface SettingPopupLisenter{
        void settingPopupListener();
    }
}
