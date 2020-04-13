package com.xdjd.view;

import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/11/29.
 */

public class Select_showPassword {
    //Select_showPassword select_showPassword = new Select_showPassword();
    public  void showPassword(EditText et) {
        // 设置EditText文本为可见的
        et.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        CharSequence charSequence = et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
    public  void hidePassword(EditText et) {
        // 设置EditText文本为可见的
        et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        CharSequence charSequence = et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }


}
