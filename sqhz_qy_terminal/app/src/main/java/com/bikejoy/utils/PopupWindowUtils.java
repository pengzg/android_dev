package com.bikejoy.utils;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/6/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PopupWindowUtils {
    /**
     * 终极解决方案(7.0, 7.1, 8.0)
     * @param pw     popupWindow
     * @param anchor v
     * @param xoff   x轴偏移
     * @param yoff   y轴偏移
     */
    public static void showAsDropDown(final PopupWindow pw, final View anchor, final int xoff, final int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pw.setHeight(height);
            pw.showAsDropDown(anchor, xoff, yoff);
        } else {
            pw.showAsDropDown(anchor, xoff, yoff);
        }
    }
}
