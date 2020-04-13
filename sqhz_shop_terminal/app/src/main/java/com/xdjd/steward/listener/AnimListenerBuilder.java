package com.xdjd.steward.listener;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.AbsListView;

import com.xdjd.utils.AnimUtils;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/24
 *     desc   : 动画监听
 *     version: 1.0
 * </pre>
 */

public class AnimListenerBuilder {

    private int newState;
    private boolean isAnimFinish = false;

    private ViewPropertyAnimatorListener listener;

    public AnimListenerBuilder() {
        listener = new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                isAnimFinish = false;
            }

            @Override
            public void onAnimationEnd(View view) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //IDLE状态下隐藏，其余状态保持View显示
                    AnimUtils.hide(view);
                }
                isAnimFinish = true;
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        };
    }

    public ViewPropertyAnimatorListener build() {
        return listener;
    }

    public void setNewState(int newState) {
        this.newState = newState;
    }

    public boolean isAnimFinish() {
        return isAnimFinish;
    }

}
