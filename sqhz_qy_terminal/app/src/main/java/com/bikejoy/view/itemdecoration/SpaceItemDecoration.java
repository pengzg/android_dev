package com.bikejoy.view.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.bikejoy.testdemo.R;


/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/7/9
 *     desc   : recyclerview间隔分割线
 *     version: 1.0
 * </pre>
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


    private int dividerHeight;//分割线高度
    private Paint dividerPaint;

    private Context context;

    private String TAG = "SpaceItemDecoration";

    public SpaceItemDecoration(Context context, int dpValue) {
        this.context = context;
        dividerHeight = dp2px(context,dpValue);
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.transparent));
    }

    public void setDrawable(int color) {
        dividerPaint.setColor(context.getResources().getColor(color));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) > 0) {
            //从第二个条目开始，距离上方Item的距离
            outRect.top = dividerHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //绘制背景色
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getTop();
            float bottom = view.getTop() + dividerHeight;
            Log.e(TAG,"view:left:"+view.getLeft()+"--top:"+view.getTop()+"--right:"+view.getRight()+"--bottom:"+view.getBottom());
            Log.e(TAG,"divider:left:"+left+"--top:"+top+"--right:"+right+"--bottom:"+bottom);
            c.drawRect(left, top, right, bottom, dividerPaint);
        }

    }

    /**
     * dp to px转换
     * @param context
     * @param dpValue
     * @return
     */
    private int dp2px(Context context,int dpValue){
        int pxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return pxValue;
    }

}
