package com.xdjd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NewLinearLayout extends LinearLayout {

    int startX;
    int startY;

    public NewLinearLayout(Context context) {
        super(context);
    }

    public NewLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发, 请求父控件及祖宗控件是否拦截事件 -- 上下滑动, 需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 不要拦截,
                // 这样是为了保证ACTION_MOVE调用
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动
                    if (endX > startX) {// 右划
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {// 左划
                        // 需要拦截
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {// 上下滑动
//                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
