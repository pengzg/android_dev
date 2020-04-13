package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2016/12/21.
 */

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.welcome_vp)
    ViewPager welcomeVp;
    @BindView(R.id.dot_layout)
    LinearLayout dotLayout;
    private List<View> mList;//图片集合
    private Adapter adapter;
    private  int currentPage = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setData();
        adapter = new Adapter();
        welcomeVp.setAdapter(adapter);
        initListener();
        dotLayout.getChildAt(0).setEnabled(true);
        dotLayout.getChildAt(1).setEnabled(false);
        dotLayout.getChildAt(2).setEnabled(false);
    }

    /*初始化小圆点*/
    private void initDots(){
        for(int i = 0; i < mList.size(); i++){
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25,25);
            if(i != 0){
                params.leftMargin = 10;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.select_dot);
            dotLayout.addView(view);
        }
    }

    private void setData() {
        mList = new ArrayList<View>();
        View view1 = getLayoutInflater().inflate(R.layout.welcome1, null);
        View view2 = getLayoutInflater().inflate(R.layout.welcome2, null);
        View view3 = getLayoutInflater().inflate(R.layout.welcome3, null);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        initDots();
        Button welcomeBtn = (Button) view3.findViewById(R.id.welcome_btn);

        welcomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoUtils.setWelcome_btn(WelcomeActivity.this, "1");
                startActivity(LoginActivity.class);
                finish_Wecome();
                AppManager.getInstance().finishAllActivity();// 进入主界面，将之前的界面杀死
                //ActivityCollector.finishAll();
            }
        });
    }

    private void finish_Wecome() {
        finish();
    }

    class Adapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }

    /*初始化监听器，页面改变时，显示小圆点*/
    private void initListener(){
        welcomeVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                updateDots();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
    /*更新小圆点显示*/
    private void updateDots(){
         currentPage = welcomeVp.getCurrentItem() % mList.size();
        for(int i = 0; i < mList.size(); i++){
            dotLayout.getChildAt(i).setEnabled(i == currentPage);
        }
    }
}
