package com.bikejoy.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bikejoy.view.roundedimage.RoundedDrawable;
import com.bikejoy.testdemo.R;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/4/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BoxBackgroundView extends View {

    private Paint paint;
    private ColorStateList borderColor =
            ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    private float cornerRadius = 0f;//圆角角度
    private float innerPadding = 0f;//外线与内线边距

    public BoxBackgroundView(Context context) {
        super(context);
    }

    public BoxBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BoxBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.BoxBackgroundView);
        cornerRadius = a.getDimensionPixelSize(R.styleable.BoxBackgroundView_box_corner_radius, -1);
        innerPadding = a.getDimensionPixelSize(R.styleable.BoxBackgroundView_box_inner_padding, -1);
        borderColor = a.getColorStateList(R.styleable.BoxBackgroundView_box_color);

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
