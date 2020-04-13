package com.xdjd.view;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xdjd.storebox.R;

/**
 * 验证码自定义控件
 */
public class CustomTitleView extends View {
	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };

	/*, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z'*/

	public String mTitleText; // 文本
	private int mTitleTextColor; // 文本的颜色
	private int mTitleTextSize; // 文本的大小

	/**
	 * 绘制时控制文本绘制的范围
	 */
	private Rect mBound;
	private Paint mPaint;
	private String str;

	/**
	 * 获得自定义的样式属性
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/**
		 * 获得我们所自定义的样式属性
		 */
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomTitleView, defStyle, 0);

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CustomTitleView_titleText:
				
				mTitleText = randomText();//a.getString(attr);

				break;
			case R.styleable.CustomTitleView_titleTextColor:
				mTitleTextColor = a.getColor(attr, Color.BLACK);

				break;
			case R.styleable.CustomTitleView_titleTextSize:
				// 默认设置为12dp，TypeValue也可以把sp转化为px
				mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
								getResources().getDisplayMetrics()));

				break;
			default:
				break;
			}
		}

		a.recycle();

		/**
		 * 获得绘制文本的宽和高
		 */
		mPaint = new Paint();
		mPaint.setTextSize(mTitleTextSize);
		mBound = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTitleText = randomText();
				postInvalidate();
			}
		});

	}

	public CustomTitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomTitleView(Context context) {
		this(context, null);
	}

	private String randomText() {
		Random random = new Random();
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			mPaint.setTextSize(mTitleTextSize);
			mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
			float textWidth = mBound.width();
			int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
			width = desired;
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			height = widthSize;
		} else {
			mPaint.setTextSize(mTitleTextSize);
			mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
			float textHeight = mBound.height();
			int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
			height = desired;
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Random random = new Random();
		mPaint.setColor(Color.GRAY);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
		mPaint.setColor(mTitleTextColor);
		canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2,
				getHeight() / 2 + mBound.height() / 2, mPaint);
		/**
		 * 画一些混淆的点
		 */
//		for (int i = 0; i < 20; i++) {
//			int x = random.nextInt(getMeasuredWidth());
//			int y = random.nextInt(getMeasuredHeight());
//			mPaint.setStrokeWidth(3);
//			canvas.drawPoint(x, y, mPaint);
//		}
		/**
		 * 画一些混淆的线
		 */
//		for (int i = 0; i < 4; i++) {
//			int startX = random.nextInt(getMeasuredWidth());
//			int startY = random.nextInt(getMeasuredHeight());
//			int stopX = random.nextInt(getMeasuredWidth());
//			int stopY = random.nextInt(getMeasuredHeight());
//			mPaint.setStrokeWidth(2);
//			canvas.drawLine(startX, startY, stopX, stopY, mPaint);
//		}
	}
}
