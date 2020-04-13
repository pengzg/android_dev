package com.xdjd.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by lijipei on 2017/2/10.
 */

public class EditUtil {

    /*文本框获取焦点和文本内容监听*/
    public static void editListener(final EditText editText, final LinearLayout linearLayout) {
        if (editText.getText().length() > 0) {
            linearLayout.setVisibility(View.INVISIBLE);
            editText.setSelection(editText.getText().length());
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    Log.e("change", "c");
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && editText.getText().length() > 0) {//获得焦点
                    linearLayout.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    Log.e("Focus", "F");
                } else {//失去焦点
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

}
