package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.fragment.RolloutGoodsGoodsFragment;
import com.xdjd.distribution.fragment.RolloutGoodsOrderFragment;
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
 *     time   : 2017/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsDetailActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_goods)
    TextView mTvGoods;
    @BindView(R.id.tv_order)
    TextView mTvOrder;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.viewpager)
    NoScrollViewPager mViewpager;

    private MyFragmentAdapter mFragmentAdapter;

    private RolloutGoodsGoodsFragment fragmentGoods;
    private RolloutGoodsOrderFragment fragmentOrder;

    private List<Fragment> listFragments;
    private String customerId;

    public EaseTitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("铺货汇总详情");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentOrder.unfold(mTitleBar);
            }
        });

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 2;

        fragmentGoods = new RolloutGoodsGoodsFragment();
        fragmentOrder = new RolloutGoodsOrderFragment();

        listFragments = new ArrayList<>();
        listFragments.add(fragmentGoods);
        listFragments.add(fragmentOrder);

        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mFragmentAdapter);
        mViewpager.setCurrentItem(0);
        alterWidth(mTvGoods);
        customerId = getIntent().getStringExtra("customerId");
    }

    @OnClick({R.id.tv_goods, R.id.tv_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goods:
                mTitleBar.setRightText("");
                selectTab(1);
                mViewpager.setCurrentItem(0);
                break;
            case R.id.tv_order:
                mTitleBar.setRightText("展开全部订单");
                selectTab(2);
                mViewpager.setCurrentItem(1);
                break;
        }
    }


    /**
     * 切换按钮状态1.商品;2.订单
     */
    private void selectTab(int index) {
        restoreTab();
        switch (index) {
            case 1:
                mTvGoods.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvGoods.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvGoods.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(0);
                alterWidth(mTvGoods);
                break;
            case 2:
                mTvOrder.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvOrder.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvOrder.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(1);
                alterWidth(mTvOrder);
                break;
        }

        //        getSaleTypeList(false);
    }


    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvOrder.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvOrder.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvOrder.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvGoods.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvGoods.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvGoods.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 2
                                * index).setDuration(300).start();
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments == null ? 0 : listFragments.size();
        }
    }
}
