package com.bikejoy.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EditTextUtil {
    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.equals(" ")) {
                    return "";
                }else{
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    /**
     * 每次获取到焦点时,全部选中方式
     * @param et
     */
    public static void setFocusChange(final EditText et) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    et.setSelectAllOnFocus(true);//全部选中方式
                } else {
                }
            }
        });
    }

    /**
     * 将光标移到末尾
     * @param et
     */
    public static void setEditSelection(EditText et){
        et.setSelection(et.getText().length());
    }

}
