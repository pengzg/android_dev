package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.AdBean;
import com.xdjd.storebox.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2016/12/27.
 */

public class AdverActivity extends BaseActivity {
    @BindView(R.id.show_ad)
    ImageView showAd;
    @BindView(R.id.ad_time)
    TextView adTime;
    @BindView(R.id.ad_btn)
    TextView adBtn;
    @BindView(R.id.ad_linear)
    LinearLayout adLinear;
    AlphaAnimation animation = new AlphaAnimation(1, 0.1f);
    private AdBean ab;
    private int x =3;//倒计时数秒
    private Timer timer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        ButterKnife.bind(this);
        ab = (AdBean)getIntent().getSerializableExtra("adBean");
        Glide.with(AdverActivity.this).load(ab.getAi_cover()).into(showAd);//加载图片
        adTime.setText(x + "");
        timer = new Timer();
        task =new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(task,1000,1000);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adTime.setText(x + "");
                    x--;
                    if (x < 0) {
                        timer.cancel();
                        finish();
                        startActivity(MainActivity.class);

//                        animation.setDuration(500);//设置动画持续时间
//                        animation.setFillAfter(true); //执行完成后保存状态

//                        showAd.setAnimation(animation);
//                        animation.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {}
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {}
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                finish();
//                                startActivity(MainActivity.class);
//                            }
//                        });
                    }
                    break;
            }
        }
    };

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            x--;
            adTime.setText(String.valueOf(x));
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @OnClick({R.id.show_ad, R.id.ad_linear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_ad://图片
                animation.cancel();
                timer.cancel();
                finish();
                //ab.setAi_type(2);
                switch(ab.getAi_type()){
                    case 1://h5界面
                        Intent intent = new Intent(AdverActivity.this,CommonWebActivity.class);
                        intent.putExtra("title",ab.getAi_title());
                        intent.putExtra("url",ab.getAi_type_value());
                        intent.putExtra("type",ab.getAi_type());
                        startActivity(intent);break;
                    case 2://活动
                        Intent intent1 = new Intent(AdverActivity.this, ActionActivity.class);
                        Log.e("活动2",ab.getAi_type_value());
                        intent1.putExtra("activityId", ab.getAi_type_value());
                        intent1.putExtra("type",ab.getAi_type());
                        startActivity(intent1);
                        break;
                    case 3: //分类
                        Intent intent2 = new Intent(AdverActivity.this,NewRecommendActivity.class);
                        intent2.putExtra("title",ab.getAi_title());
                        intent2.putExtra("tagId",ab.getAi_type_value());
                        intent2.putExtra("type",ab.getAi_type());
                        startActivity(intent2);
                        break;
                    case 4:// 标签
                        Intent intent3 = new Intent(AdverActivity.this,NewRecommendActivity.class);
                        intent3.putExtra("tagId",ab.getAi_type_value());
                        intent3.putExtra("title",ab.getAi_title());
                        intent3.putExtra("type",ab.getAi_type());
                        startActivity(intent3);
                        break;
                    case 5://商品
                        Intent intent4 = new Intent(AdverActivity.this, GoodsDetailActivity.class);
                        intent4.putExtra("gpId", ab.getAi_type_value());
                        intent4.putExtra("type",ab.getAi_type());
                        startActivity(intent4);
                        break;
                    default :
                        startActivity(MainActivity.class);
                        break;
                }
                break;
            case R.id.ad_linear://跳过
                animation.cancel();
                timer.cancel();
                finish();
                startActivity(MainActivity.class);
                break;
        }
    }
}
