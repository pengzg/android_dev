package com.xdjd.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xdjd.distribution.R;
import com.xdjd.utils.UIUtils;

/**
 * Created by lijipei on 2017/1/8.
 */

public class DrawPadView extends View {

    private Path mPath;
    private Paint mPaint;
    private float mX;
    private float mY;

    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;

    public Bitmap getBitmap() {
        return cachebBitmap;
    }

    public Path getPath() {
        return mPath;
    }

    public DrawPadView(Context context) {
        super(context);
        init();
    }

    public DrawPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();

        cachebBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                mPath.moveTo(mX,mY);
                break;
            case MotionEvent.ACTION_MOVE:
                float x1 = event.getX();
                float y1 = event.getY();

                float cx = (x1 + mX) / 2;
                float cy = (y1 + mY) / 2;

                mPath.quadTo(mX,mY,cx,cy);
                //mPath.lineTo(x1,y1);
                mX = x1;
                mY = y1;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
        int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }

        if (curW < w)
            curW = w;
        if (curH < h)
            curH = h;

        Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (cachebBitmap != null) {
            newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
        }
        cachebBitmap = newBitmap;
        cacheCanvas = newCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(mPath,mPaint);
    }
}
