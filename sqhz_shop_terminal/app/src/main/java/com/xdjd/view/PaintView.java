package com.xdjd.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PaintView extends View {
    private Paint paint;
    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;
    private Path path;

    public boolean isSign() {
        return isSign;
    }

    //是否签名
    private boolean isSign = false;

    public Bitmap getCachebBitmap() {
        return cachebBitmap;
    }

    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        path = new Path();
        cachebBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(Color.WHITE);
    }

    public void clear() {
        if (cacheCanvas != null) {
            isSign = false;
            paint.setColor(Color.WHITE);
            cacheCanvas.drawPaint(paint);
            paint.setColor(Color.BLACK);
            cacheCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(BRUSH_COLOR);
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
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

    private float cur_x, cur_y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isSign = true;
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }
        invalidate();
        return true;
    }
}