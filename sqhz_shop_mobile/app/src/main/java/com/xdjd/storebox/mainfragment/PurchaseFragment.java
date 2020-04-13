package com.xdjd.storebox.mainfragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.CartActivity;
import com.xdjd.storebox.activity.SearchActivity;
import com.xdjd.storebox.adapter.PurchaseViewpageAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.event.SwitchUpdateEvent;
import com.xdjd.storebox.fragment.DirectPurchaseFragment;
import com.xdjd.storebox.fragment.UnifyPurchaseFragment;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 采购fragment
 * Created by lijipei on 2016/11/16.
 */

public class PurchaseFragment extends BaseFragment {

    //    @BindView(R.id.purchase_title_bg)
    //    ImageView mPurchaseTitleBg;
    //    @BindView(R.id.purchase_unify_rl)
    //    RelativeLayout mPurchaseUnifyRl;
    //    @BindView(R.id.purchase_direct_rl)
    //    RelativeLayout mPurchaseDirectRl;
    @BindView(R.id.purchase_common)
    FrameLayout mPurchaseCommon;
    //    @BindView(R.id.purchase_titlebar)
    //    RelativeLayout mPurchaseTitlebar;
    @BindView(R.id.refresh_iv)
    ImageView mRefreshIv;
    @BindView(R.id.refresh_ll)
    LinearLayout mRefreshLl;
    @BindView(R.id.seek_ll)
    LinearLayout mSeekLl;
    @BindView(R.id.unify_title)
    TextView mUnifyTitle;
    @BindView(R.id.direct_title)
    TextView mDirectTitle;
    @BindView(R.id.purchase_cart_tv)
    TextView mPurchaseCartTv;
    @BindView(R.id.purchase_cart_rl)
    RelativeLayout mPurchaseCartRl;
    @BindView(R.id.bg_iv)
    ImageView mBgIv;
    @BindView(R.id.purchase_viewpage)
    NoScrollViewPager mPurchaseViewpage;
    /**
     * Fragment的集合
     */
    private List<Fragment> mFragments;

    FragmentManager fragmentManager;

    private int currentTab; // 当前Tab页面索引

    private PurchaseViewpageAdapter viewpageAdapter;
    private int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mFragments = new ArrayList<>();
        mFragments.add(new UnifyPurchaseFragment());
        mFragments.add(new DirectPurchaseFragment());
        fragmentManager = getChildFragmentManager();

        viewpageAdapter = new PurchaseViewpageAdapter(getActivity().getSupportFragmentManager(),mFragments);
        mPurchaseViewpage.setAdapter(viewpageAdapter);
        mPurchaseViewpage.setCurrentItem(0);

        // 默认显示第一页
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.add(R.id.purchase_common, mFragments.get(currentTab));
//        ft.addToBackStack(mFragments.get(currentTab).getClass().getName());
//        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.unify_title, R.id.direct_title, R.id.seek_ll, R.id.refresh_ll, R.id.purchase_cart_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.unify_title://统一进货
                index = 0;
                mPurchaseViewpage.setCurrentItem(0);
                mUnifyTitle.setTextColor(UIUtils.getColor(R.color.white));
                mDirectTitle.setTextColor(UIUtils.getColor(R.color.black));
                ObjectAnimator
                        .ofFloat(
                                mBgIv,
                                "translationX",
                                mBgIv.getTranslationX(), mUnifyTitle.getX()).setDuration(100).start();
                refresh(0);
                break;
            case R.id.direct_title://预售进货
                index = 1;
                mPurchaseViewpage.setCurrentItem(1);
                mUnifyTitle.setTextColor(UIUtils.getColor(R.color.black));
                mDirectTitle.setTextColor(UIUtils.getColor(R.color.white));
                if (mDirectTitle.getX()==0){
                    mDirectTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mDirectTitle.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            ObjectAnimator
                                    .ofFloat(mBgIv, "translationX",
                                             mBgIv.getTranslationX(), mDirectTitle.getX()).setDuration(100).start();
                        }
                    });
                }else{
                    ObjectAnimator
                            .ofFloat(
                                    mBgIv,
                                    "translationX",
                                    mBgIv.getTranslationX(), mDirectTitle.getX()).setDuration(100).start();
                    refresh(1);
                }
                break;
            case R.id.seek_ll://搜索
                startActivity(SearchActivity.class);
                break;
            case R.id.refresh_ll://刷新
                Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.rotate_anim_refresh);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                if (operatingAnim != null) {
                    mRefreshIv.startAnimation(operatingAnim);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshIv.clearAnimation();
                    }
                }, 1000);
                refresh(index);
                break;
            case R.id.purchase_cart_rl://跳转购物车
                startActivity(CartActivity.class);
                break;
        }
    }
private void refresh(int index){
    if (index == 0){
        UnifyPurchaseFragment fragment = (UnifyPurchaseFragment) mFragments.get(index);
        fragment.updateGoodsList();
    }else if (index == 1){
        DirectPurchaseFragment fragment = (DirectPurchaseFragment) mFragments.get(index);
        fragment.updateGoodsList();
    }
}

    private void setSelectFragment(int i) {
        if (i == 0) {
            onClick(mUnifyTitle);
        } else {
            onClick(mDirectTitle);
        }
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(currentTab);
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            //            ft.setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left);
        } else {
            //            ft.setCustomAnimations(R.anim.pull_in_left, R.anim.push_out_right);
        }
        return ft;
    }

    public void setTab(int type) {
        setSelectFragment(type);
    }


    public void setCartTotalAmount(String totalPrice) {
        mPurchaseCartTv.setText(totalPrice);
    }

    /**
     * event切换公司刷新方法
     * @param event
     */
    public void onEventMainThread(SwitchUpdateEvent event) {
        refresh(index);
    }

    /**
     * 接收购物车数量和价格
     *
     * @param event
     */
    public void onEventMainThread(CartEvent event) {
        if ("0".equals(event.getTotalAmount()) || event.getTotalAmount() == null) {
            mPurchaseCartTv.setText("0.00");
        } else {
            mPurchaseCartTv.setText(event.getTotalAmount());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
