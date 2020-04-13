package com.xdjd.storebox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.fragment.OrderAllFragment;
import com.xdjd.storebox.fragment.OrderPresellFragment;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.view.EaseTitleBar;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单activity
 * Created by lijipei on 2016/12/1.
 */

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    /*@BindView(R.id.tv_0)
    TextView mTv0;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.tv_3)
    TextView mTv3;
    @BindView(R.id.line)
    View mLine;*/
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    private List<String> mDataList = new ArrayList<>();
    private TextView[] tvs;
    public String mIndex;

    private int index = 0;

    private MyFragmentAdapter adapter;
    //public  static int Flag;//订单状态


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("我的订单");
        mTitleBar.setRightImageResource(R.drawable.to_homepage);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
                intent.putExtra("currentTab", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
       /* tvs = new TextView[]{mTv0*//*, mTv1, mTv2, mTv3*//*};
        tvs[mIndex].setSelected(true);*/
        mIndex = getIntent().getStringExtra("index");
        OrderAllFragment fragment1 = new OrderAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderStatus", mIndex);
        fragment1.setArguments(bundle);

        OrderPresellFragment fragment2 = new OrderPresellFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("orderStatus", mIndex);
        fragment2.setArguments(bundle2);


        mViewPager.setAdapter(adapter = new MyFragmentAdapter(
                getSupportFragmentManager(), new Fragment[]{
                fragment1,fragment2//全部订单
                /*new DelayPaymentFragment(),//待付款
                new WaitReceiveGoodsFragment(),//待收货
                new FinishOrderFragment()*/}));//已完成

        /*mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int position;

            @Override
            public void onPageSelected(int position) {
                moveAnimation(position);
                this.position = position;
                for (int i = 0; i < tvs.length; i++) {
                    if (i == position) {
                        tvs[i].setSelected(true);
                    } else {
                        tvs[i].setSelected(false);
                    }
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tvs[mIndex].setSelected(true);
        mViewPager.setCurrentItem(mIndex);*/
        //mViewPager.setOffscreenPageLimit(0);

        initMagicIndicator();
    }

    private void initMagicIndicator() {
        mDataList.add("普通订单");
        mDataList.add("预售订单");

        mMagicIndicator.setBackgroundResource(R.drawable.round_indicator_bg);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#e94220"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(clipPagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = UIUtil.dip2px(MyOrderActivity.this,35);
                float borderWidth = UIUtil.dip2px(context, 1);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(lineHeight / 2);
                indicator.setYOffset(borderWidth);
                indicator.setColors(Color.parseColor("#E82D00"));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    public void getOrderStatus() {
        //return mIndex;
    }

    @OnClick({/*R.id.tv_0, R.id.tv_1, R.id.tv_2, R.id.tv_3*/})
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.tv_0:
                index = 0;
                break;
            case R.id.tv_1:
                index = 1;
                break;
            case R.id.tv_2:
                index = 2;
                break;
            case R.id.tv_3:
                index = 3;
                break;*/
        }
        mViewPager.setCurrentItem(index);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public MyFragmentAdapter(FragmentManager fm, Fragment[] frag) {
            super(fm);
            this.fragments = frag;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return fragments[position];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragments.length;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

    }

    /*private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 4
                                * index).setDuration(300).start();
    }*/

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 4;
        }
    }*/
}
