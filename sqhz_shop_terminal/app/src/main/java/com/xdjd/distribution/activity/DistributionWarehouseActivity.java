package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.fragment.BeenDisWarehouseFragment;
import com.xdjd.distribution.fragment.NoDisWarehouseFragment;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/25
 *     desc   : 配送出库
 *     version: 1.0
 * </pre>
 */

public class DistributionWarehouseActivity extends BaseActivity implements LocationListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_no)
    TextView mTvNo;
    @BindView(R.id.tv_been)
    TextView mTvBeen;
    @BindView(R.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R.id.activity_distribution_task)
    LinearLayout mActivityDistributionTask;

    public LocationService locationService;
    @BindView(R.id.bg_view)
    View mBgView;
    private MyLocationUtil locationUtil;

    /**
     * 纬度
     */
    public String latitude = "";
    /**
     * 经度
     */
    public String longtitude = "";

    private FragmentManager fm;
    private int currentTab; // 当前Tab页面索引

    private List<Fragment> fragments;

    private NoDisWarehouseFragment mNoDisFragment;
    private BeenDisWarehouseFragment mBeenDisFragment;

    private boolean isAddNoFragment = false;
    private boolean isAddBeenFragment = false;

    /**
     * 是否是第一次加载
     */
    public boolean isFirst = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disProgressDialog(DistributionWarehouseActivity.this);
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                if (isFirst) {
                    isFirst = false;
                    mNoDisFragment.loadData();
                }
            } else {
                showToast("位置获取失败");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_distribution_task;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("配送出库列表");

        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);

        mNoDisFragment = new NoDisWarehouseFragment();
        mBeenDisFragment = new BeenDisWarehouseFragment();

        fragments = new ArrayList<>();
        fragments.add(mNoDisFragment);
        fragments.add(mBeenDisFragment);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_comment, fragments.get(0));
        ft.commit();

        showProgressDialog(this);
    }

    @OnClick({R.id.tv_no, R.id.tv_been})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_no:
                mTvNo.setTextColor(UIUtils.getColor(R.color.white));
                mTvBeen.setTextColor(UIUtils.getColor(R.color.text_gray));

                if (!fragments.get(0).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(0));
                    ft.commit();
                } else {
                    showTab(0);
                }
                locationService.start();
                moveAnimation(mTvNo);
                break;
            case R.id.tv_been:
                mTvBeen.setTextColor(UIUtils.getColor(R.color.white));
                mTvNo.setTextColor(UIUtils.getColor(R.color.text_gray));

                if (!fragments.get(1).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(1));
                    ft.commit();
                } else {
                    showTab(1);
                }
                moveAnimation(mTvBeen);
                locationService.start();
                break;
        }
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

    @Override
    public void onPause() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    @Override
    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        LogUtils.e("location", "selectClient:" + location.getAddrStr() + location.getLatitude());
        mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        locationService.stop();
    }

    @Override
    public void locationError(BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        locationService.stop();
        mHandler.sendEmptyMessage(PublicFinal.ERROR);
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
    }
}
