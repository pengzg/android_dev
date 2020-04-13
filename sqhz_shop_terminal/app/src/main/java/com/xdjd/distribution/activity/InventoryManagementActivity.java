package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.fragment.CarStorageFragment;
import com.xdjd.distribution.fragment.MainStorageFragment;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class InventoryManagementActivity extends BaseActivity {
    @BindView(R.id.tv_car_storage)
    TextView mTvCarStorage;
    @BindView(R.id.tv_main_storage)
    TextView mTvMainStorage;
    @BindView(R.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private UserBean userBean;

    private FragmentManager fm;
    private int currentTab = 0; // 当前Tab页面索引

    private List<Fragment> fragments;
    private CarStorageFragment mCarStorageFragment;
    private MainStorageFragment mMainStorageFragment;

    private boolean isCarAdd = false;
    private boolean isMainAdd = false;

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    @Override
    protected int getContentView() {
        return R.layout.activity_inventory_management;
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
        mTitle.setText("库存管理");
        mRlRightSearch.setVisibility(View.VISIBLE);
        mRlRightQr.setVisibility(View.VISIBLE);

        userBean = UserInfoUtils.getUser(this);

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });

        mCarStorageFragment = new CarStorageFragment();
        mMainStorageFragment = new MainStorageFragment();
        fragments = new ArrayList<>();
        fragments.add(mCarStorageFragment);
        fragments.add(mMainStorageFragment);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (!"".equals(userBean.getSu_storeid()) && (userBean.getSu_usertype().equals(BaseConfig.userTypeCardSell)
                || userBean.getSu_usertype().equals(BaseConfig.userTypeDistribution))) {
            ft.add(R.id.fl_comment, fragments.get(0));
            currentTab = 0;
        } else {
            ft.add(R.id.fl_comment, fragments.get(1));
            currentTab = 1;

            mTvCarStorage.setVisibility(View.GONE);
            mTvMainStorage.setTextColor(UIUtils.getColor(R.color.white));
        }

        ft.commit();

    }

    @OnClick({R.id.left_layout, R.id.tv_car_storage, R.id.tv_main_storage, R.id.rl_right_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.tv_car_storage://车库库存
                mTvCarStorage.setTextColor(UIUtils.getColor(R.color.white));
                //                mTvCarStorage.setBackgroundResource(R.drawable.bg_inventory_manager_title_true);

                mTvMainStorage.setTextColor(UIUtils.getColor(R.color.text_gray));
                //                mTvMainStorage.setBackgroundResource(R.color.transparent);

                if (!fragments.get(0).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(0));
                    ft.commit();
                } else {
                    showTab(0);
                }
                moveAnimation(mTvCarStorage);
                break;
            case R.id.tv_main_storage://主库库存
                mTvMainStorage.setTextColor(UIUtils.getColor(R.color.white));
                //                mTvMainStorage.setBackgroundResource(R.drawable.bg_inventory_manager_title_true);

                mTvCarStorage.setTextColor(UIUtils.getColor(R.color.text_gray));
                //                mTvCarStorage.setBackgroundResource(R.color.transparent);

                if (!fragments.get(1).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(1));
                    ft.commit();
                } else {
                    showTab(1);
                }

                moveAnimation(mTvMainStorage);
                break;
            case R.id.rl_right_search://搜索
                showPopupSeaarh();
                break;
            case R.id.rl_right_qr://二维码扫描
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Comon.QR_GOODS_REQUEST_CODE) {
            switch (resultCode) {
                case Comon.QR_GOODS_RESULT_CODE:
                    String result = data.getStringExtra("result");
                    if (result == null || result.equals("")) {
                    } else {
                        if (currentTab == 0) {//车库存
                            mCarStorageFragment.searchGoods(result);
                        } else {//主库存
                            mMainStorageFragment.searchGoods(result);
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this,mLlMain, new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                if (currentTab == 0) {//编辑
                    mCarStorageFragment.searchGoods(searchStr);
                } else {//提交
                    mMainStorageFragment.searchGoods(searchStr);
                }
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }


    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = fm.beginTransaction();

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.addListener(animatorListener);
        animator.setDuration(400).start();
    }

    private MyAnimatorListener animatorListener = new MyAnimatorListener();

    public class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (currentTab == 0) {
                if (!isCarAdd) {
                } else {
                    mCarStorageFragment.searchGoods("");
                }
                isCarAdd = true;
            } else {
                if (!isMainAdd) {
                } else {
                    mMainStorageFragment.searchGoods("");
                }
                isMainAdd = true;
            }
            animator.removeListener(animatorListener);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }
}
