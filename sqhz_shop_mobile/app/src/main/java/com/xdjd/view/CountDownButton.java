package com.xdjd.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 点击后等待60秒倒计时button
 * @author slg
 *
 */
public class CountDownButton extends CountDownTimer {
    public static final int TIME_COUNT_FUTURE = 60000;  
    public static final int TIME_COUNT_INTERVAL = 1000;  
      
    private Context mContext;
    private Button mButton;
    private String mOriginalText;
    private Drawable mOriginalBackground;
    private Drawable mTickBackground;
    private int mOriginalTextColor;  
      
    public CountDownButton() {  
        super(TIME_COUNT_FUTURE, TIME_COUNT_INTERVAL);  
    }  
      
    public CountDownButton(long millisInFuture, long countDownInterval) {  
        super(millisInFuture, countDownInterval);  
    }  
      
    public void init(Context context, Button button) {
        this.mContext = context;  
        this.mButton = button;  
        this.mOriginalText = mButton.getText().toString();  
//        this.mOriginalBackground = mButton.getBackground();  
//        this.mTickBackground = this.mOriginalBackground;  
        this.mOriginalTextColor = mButton.getCurrentTextColor();  
    }  
      
    public void setTickDrawable(Drawable tickDrawable) {
        this.mTickBackground = tickDrawable;  
    }  
  
    @Override
    public void onFinish() {  
        if (mContext != null && mButton != null) {  
            mButton.setText(mOriginalText);  
            mButton.setTextColor(mOriginalTextColor);  
//            mButton.setBackgroundDrawable(mOriginalBackground);  
            mButton.setClickable(true);  
            mButton.setSelected(false);
        }  
    }  
  
    @Override
    public void onTick(long millisUntilFinished) {  
        if (mContext != null && mButton != null) {  
            mButton.setClickable(false); 
            mButton.setSelected(true);
//            mButton.setBackgroundDrawable(mTickBackground);  
            mButton.setTextColor(mContext.getResources().getColor(android.R.color.white));  
            mButton.setText(millisUntilFinished / 1000+"秒后重新获取" );  
        }  
    }  
}  