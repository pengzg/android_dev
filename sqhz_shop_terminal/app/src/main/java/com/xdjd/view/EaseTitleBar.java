package com.xdjd.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.utils.UIUtils;


/**
 * title bar
 */
public class EaseTitleBar extends RelativeLayout {

    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    protected RelativeLayout rightLayout;
    protected ImageView rightImage, rightleftImage;
    protected TextView titleView, rightText,rightText02, tv_lefttext_titlebar;
    protected RelativeLayout titleLayout, right_left_layout, right_textLayout;

    public EaseTitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseTitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ease_widget_title_bar, this);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        leftImage = (ImageView) findViewById(R.id.left_image);
        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        rightImage = (ImageView) findViewById(R.id.right_image);
        titleView = (TextView) findViewById(R.id.title);
        titleLayout = (RelativeLayout) findViewById(R.id.root);
        right_left_layout = (RelativeLayout) findViewById(R.id.right_left_layout);
        right_textLayout = (RelativeLayout) findViewById(R.id.right_Text_layout);
        rightleftImage = (ImageView) findViewById(R.id.right_left_image);
        rightText = (TextView) findViewById(R.id.right_Text);
        rightText02 = (TextView) findViewById(R.id.right_text02);
        tv_lefttext_titlebar = (TextView) findViewById(R.id.tv_lefttext_titlebar);

        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseTitleBar);
            String title = ta.getString(R.styleable.EaseTitleBar_titleBarTitle);
            titleView.setText(title);

            Drawable leftDrawable = ta.getDrawable(R.styleable.EaseTitleBar_titleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(R.styleable.EaseTitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }

            Drawable background = ta.getDrawable(R.styleable.EaseTitleBar_titleBarBackground);
            if (null != background) {
                titleLayout.setBackgroundDrawable(background);
            }

            ta.recycle();
        }
    }

    public void setLeftImageResource(int resId) {
        leftLayout.setVisibility(View.VISIBLE);
        leftImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        rightLayout.setVisibility(View.VISIBLE);
        rightImage.setImageResource(resId);
    }

    public void showRightImage() {
        rightImage.setVisibility(View.VISIBLE);
    }

    public void setLeftLayoutClickListener(OnClickListener listener) {
        leftLayout.setVisibility(View.VISIBLE);
        leftLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener) {

        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility) {
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility) {
        rightLayout.setVisibility(visibility);
    }

    public void setRightTextViewVisibility(int visibility) {
        right_textLayout.setVisibility(visibility);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setTitleToLeft(String title) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
        //因为对SDK要求17以上 所以放弃此方法
//        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);     //这里需要清除掉之前设置的位置 否则不生效
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.leftMargin = UIUtils.dp2px(12);
        titleView.setLayoutParams(layoutParams);

        titleView.setText(title);
    }

    public void setTitleToLeft(String title,int leftMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
        //因为对SDK要求17以上 所以放弃此方法
        //        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);     //这里需要清除掉之前设置的位置 否则不生效
        //        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.leftMargin = UIUtils.dp2px(leftMargin);
        titleView.setLayoutParams(layoutParams);

        titleView.setText(title);
    }

    public void setTitleColor(int color) {
        titleView.setTextColor(color);
    }

    public void hideTitle() {
        titleView.setVisibility(View.GONE);
    }

    public void showTitle() {
        titleView.setVisibility(View.VISIBLE);
    }


    public void setBackgroundColor(int color) {
        titleLayout.setBackgroundColor(color);
    }

    public RelativeLayout getLeftLayout() {
        return leftLayout;
    }

    public RelativeLayout getTitleLayout() {
        return titleLayout;
    }

    public RelativeLayout getRightLayout() {
        return rightLayout;
    }

    public void setRightTextBackgroud(int resId) {
        rightText.setBackgroundResource(resId);
    }

    public void setRightText(String righttext) {
        right_textLayout.setVisibility(View.VISIBLE);
        rightText.setText(righttext);
    }

    public void setRightTextDipSize(int unit, float size) {
        rightText.setTextSize(unit,size);
    }

    public void setRightTextColor(int color) {

        rightText.setTextColor(getResources().getColor(color));
    }

    public void setRightTextClickListener(OnClickListener listener) {
        right_textLayout.setVisibility(View.VISIBLE);
        right_textLayout.setOnClickListener(listener);
    }


    public void setRightText02(String righttext) {
        rightText02.setText(righttext);
        rightText02.setVisibility(View.VISIBLE);
    }

    public void setRightText02Gone() {
        rightText02.setVisibility(View.GONE);
    }

    public void setRightTextColor02(int color) {
        rightText02.setTextColor(getResources().getColor(color));
    }

    public void setRightTextClickListener02(OnClickListener listener) {
        rightText02.setVisibility(View.VISIBLE);
        rightText02.setOnClickListener(listener);
    }


    public void setRightLeftImageResource(int resId) {
        right_left_layout.setVisibility(View.VISIBLE);
        rightleftImage.setImageResource(resId);
        rightleftImage.setVisibility(View.VISIBLE);

    }

    public void setRightLeftImageLayout(OnClickListener listener) {
        right_left_layout.setVisibility(View.VISIBLE);
        right_left_layout.setOnClickListener(listener);
    }

    public void setRightViewGone() {
        right_left_layout.setVisibility(View.GONE);
        rightLayout.setVisibility(View.GONE);
    }

    public void leftBack(final Activity activity) {
        leftLayout.setVisibility(View.VISIBLE);
        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    public String getRightText() {
        return rightText.getText().toString();
    }

    public void leftText(String text) {
        leftLayout.setVisibility(View.VISIBLE);
        tv_lefttext_titlebar.setVisibility(View.VISIBLE);
        tv_lefttext_titlebar.setText(text);
    }

}
