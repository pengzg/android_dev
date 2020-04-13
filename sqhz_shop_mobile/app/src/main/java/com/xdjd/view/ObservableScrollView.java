package com.xdjd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 可监听scrollview滑动
 * Created by lijipei on 2016/7/18.
 */
public class ObservableScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent) {
        if(scrollViewListener!=null){
            scrollViewListener.onScrollChanged(scrollY);
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * Taken from the AOSP ScrollView source
     */
    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getHeight()
                    - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    /**
     * scrollview的滑动事件监听回调接口
     * Created by lijipei on 2016/7/18.
     */
    public interface ScrollViewListener {
        void onScrollChanged(int scrollY);
    }
}
