package com.xdjd.steward.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.steward.adapter.PurchaseViewpageAdapter;
import com.xdjd.steward.fragment.CustomerInformationFragment;
import com.xdjd.steward.fragment.CustomerMapFragment;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/25
 *     desc   : 客户资料列表
 *     version: 1.0
 * </pre>
 */

public class CustomerInformationActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.no_scroll_viewpage)
    NoScrollViewPager mNoScrollViewpage;
    @BindView(R.id.tv_map_location)
    TextView mTvMapLocation;
    @BindView(R.id.tv_customer_list)
    TextView mTvCustomerList;

    private List<Fragment> fragments;
    private PurchaseViewpageAdapter viewpageAdapter;

    private CustomerMapFragment customerMapFragment;
    private CustomerInformationFragment customerInformationFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("客户信息");

        fragments = new ArrayList<>();
        customerMapFragment = new CustomerMapFragment();
        customerInformationFragment = new CustomerInformationFragment();

        fragments.add(customerMapFragment);
        fragments.add(customerInformationFragment);

        viewpageAdapter = new PurchaseViewpageAdapter(getSupportFragmentManager(), fragments);
        mNoScrollViewpage.setAdapter(viewpageAdapter);
        mNoScrollViewpage.setOnPageChangeListener(this);
        mNoScrollViewpage.setCurrentItem(0);

        mTvCustomerList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTvCustomerList.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (getIntent().getBooleanExtra("isCustomerListing",false)){
                    mTvCustomerList.setTextColor(UIUtils.getColor(R.color.white));
                    mTvMapLocation.setTextColor(UIUtils.getColor(R.color.text_gray));

                    mNoScrollViewpage.setCurrentItem(1);
                    moveAnimation(mTvCustomerList);
                }
            }
        });
    }

    @OnClick({R.id.tv_map_location,R.id.tv_customer_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_map_location:
                mTvMapLocation.setTextColor(UIUtils.getColor(R.color.white));
                mTvCustomerList.setTextColor(UIUtils.getColor(R.color.text_gray));

                mNoScrollViewpage.setCurrentItem(0);
                moveAnimation(mTvMapLocation);
                break;
            case R.id.tv_customer_list:
                mTvCustomerList.setTextColor(UIUtils.getColor(R.color.white));
                mTvMapLocation.setTextColor(UIUtils.getColor(R.color.text_gray));

                mNoScrollViewpage.setCurrentItem(1);
                moveAnimation(mTvCustomerList);
                break;
        }
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
//        animator.addListener(animatorListener);
        animator.setDuration(400).start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*private MyAnimatorListener animatorListener = new MyAnimatorListener();

    public class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (currentTab == 0) {
                if (!isAddNoFragment) {
                } else {
                    if (!latitude.equals("")) {
                        mNoDisFragment.getTaskList();
                    }
                }
                isAddNoFragment = true;
            } else {
                if (!isAddBeenFragment) {
                } else {
                    mBeenDisFragment.getTaskList();
                }
                isAddBeenFragment = true;
            }
            animator.removeListener(animatorListener);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }*/

}
