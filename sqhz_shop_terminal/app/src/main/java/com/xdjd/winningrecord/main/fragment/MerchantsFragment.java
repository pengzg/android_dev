package com.xdjd.winningrecord.main.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.steward.adapter.PurchaseViewpageAdapter;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.NoScrollViewPager;
import com.xdjd.winningrecord.fragment.MerchantsListingFragment;
import com.xdjd.winningrecord.fragment.MerchantsMapFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lijipei on 2017/11/2.
 * 商家界面
 */

public class MerchantsFragment extends BaseFragment  implements ViewPager.OnPageChangeListener{

    Unbinder unbinder;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_left)
    TextView mTvLeft;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.no_scroll_viewpage)
    NoScrollViewPager mNoScrollViewpage;
    private View view;

    private List<Fragment> fragments;
    private PurchaseViewpageAdapter viewpageAdapter;

    private MerchantsMapFragment mapFragment;
    private MerchantsListingFragment listingFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_merchants, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

        fragments = new ArrayList<>();
        mapFragment = new MerchantsMapFragment();
        listingFragment = new MerchantsListingFragment();

        fragments.add(mapFragment);
        fragments.add(listingFragment);

        viewpageAdapter = new PurchaseViewpageAdapter(getActivity().getSupportFragmentManager(), fragments);
        mNoScrollViewpage.setAdapter(viewpageAdapter);
        mNoScrollViewpage.setCurrentItem(0);
        mNoScrollViewpage.setOnPageChangeListener(this);
    }

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                mTvLeft.setTextColor(UIUtils.getColor(R.color.white));
                mTvRight.setTextColor(UIUtils.getColor(R.color.text_gray));

                mNoScrollViewpage.setCurrentItem(0);
                moveAnimation(mTvLeft);
                break;
            case R.id.tv_right:
                mTvRight.setTextColor(UIUtils.getColor(R.color.white));
                mTvLeft.setTextColor(UIUtils.getColor(R.color.text_gray));

                mNoScrollViewpage.setCurrentItem(1);
                moveAnimation(mTvRight);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
