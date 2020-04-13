package com.xdjd.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xdjd.distribution.R;

import java.math.BigDecimal;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/13
 *     desc   : 动画工具类
 *     version: 1.0
 * </pre>
 */

public class AnimUtils {

    /**
     * 弹性动画
     */
    public static void springAnimation(final View v){
        //弹性动画
        ObjectAnimator oo = ObjectAnimator.ofFloat(
                v, "ljp", 0.5f, 1.5f, 1)
                .setDuration(300);
        oo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(
                    ValueAnimator animation) {
                float f = (Float) animation
                        .getAnimatedValue();
                v.setScaleX(f);
                v.setScaleY(f);
            }
        });
        oo.setInterpolator(new BounceInterpolator());
        oo.start();// 执行动画
    }

    /**
     * 平移动画
     * @param et
     * @param ll
     * @param rl
     * @param iv
     */
    public static void setTranslateAnimation(final EditText et, LinearLayout ll, RelativeLayout rl, ImageView iv) {
        Animation inFromLeft = null;
        if (ll.getVisibility() == View.GONE && !et.getText().toString().equals("0") && !et.getText().toString().equals("")) {
            ll.setVisibility(View.VISIBLE);

            inFromLeft = new TranslateAnimation(rl.getWidth() - iv.getWidth(), 0, 0, 0);
            inFromLeft.setDuration(400);
            inFromLeft.setFillAfter(true);
            ll.startAnimation(inFromLeft);

//            ll.setVisibility(View.VISIBLE);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(ll, "translationX", rl.getWidth() - iv.getWidth(),0);
//            animator.setDuration(400);
//            animator.start();

        } else if (ll.getVisibility() == View.VISIBLE && (et.getText().toString().equals("0") || et.getText().toString().equals(""))) {
            inFromLeft = new TranslateAnimation(0, rl.getWidth() - iv.getWidth(), 0, 0);
            inFromLeft.setDuration(400);
            inFromLeft.setInterpolator(new AccelerateInterpolator());
            ll.startAnimation(inFromLeft);
            ll.setVisibility(View.GONE);

//            ObjectAnimator animator = ObjectAnimator.ofFloat(ll, "translationX", 0, rl.getWidth() - iv.getWidth());
//            animator.setDuration(400);
//            animator.start();
//            ll.setVisibility(View.GONE);
        }
    }

    /** PopupWindow显示的动画 */
    public static void translateAnimIn(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_in);
        view.startAnimation(animation);
    }

    /** PopupWindow消失的动画 */
    public static void translateAnimOut(Context context,View view, final PopupWindow pw) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_out);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pw.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    public static void hide(final View view) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setInterpolator(INTERPOLATOR)
                .setDuration(300)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        view.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                    }
                })
                .start();
    }

    public static void show(final View view, ViewPropertyAnimatorListener listener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setInterpolator(INTERPOLATOR)
                .setDuration(300)
                .setListener(listener)
                .start();
    }

}
