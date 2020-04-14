package com.bikejoy.view.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bikejoy.utils.UIUtils;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/4/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private int paddingLeftRight;
    private boolean includeEdge;

    /**
     *
     * @param dividerHeight  分割线高度
     * @param paddingLeftRight 左右两侧padding
     * @param includeEdge  是否在顶部添加间隔
     */
    public LinearItemDecoration(int dividerHeight, int paddingLeftRight, boolean includeEdge) {
        this.dividerHeight = dividerHeight;
        this.paddingLeftRight = paddingLeftRight;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (includeEdge && position==0){
            outRect.top = UIUtils.dp2px(dividerHeight);
        }
        outRect.left = UIUtils.dp2px(paddingLeftRight);
        outRect.right = UIUtils.dp2px(paddingLeftRight);
        outRect.bottom = UIUtils.dp2px(dividerHeight);

    }

}
