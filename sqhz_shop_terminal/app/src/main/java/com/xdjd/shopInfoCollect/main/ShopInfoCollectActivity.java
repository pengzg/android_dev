package com.xdjd.shopInfoCollect.main;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.PositionLocationActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.shopInfoCollect.activity.CollectClientActivity;
import com.xdjd.shopInfoCollect.activity.MeInfoActivity;
import com.xdjd.shopInfoCollect.activity.SetUpActivity;
import com.xdjd.shopInfoCollect.main.fragment.MeAddShopFragment;
import com.xdjd.shopInfoCollect.main.fragment.NearByShopFragment;
import com.xdjd.shopInfoCollect.main.fragment.NotLocateFragment;
import com.xdjd.shopInfoCollect.popup.MeInfoPopup;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.permissions.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/9/28.
 */

public class ShopInfoCollectActivity extends BaseActivity implements MeInfoPopup.personInfoListener {

    @BindView(R.id.fl_comment)
    FrameLayout flComment;

    @BindView(R.id.collect_shop_info)
    LinearLayout collectShopInfo;
    @BindView(R.id.ll_me_info)
    LinearLayout llMeInfo;
    @BindView(R.id.tv_btn1)
    TextView tvBtn1;
    @BindView(R.id.rl_btn1)
    RelativeLayout rlBtn1;
    @BindView(R.id.tv_btn2)
    TextView tvBtn2;
    @BindView(R.id.ll_btn2)
    LinearLayout llBtn2;
    @BindView(R.id.tv_btn3)
    TextView tvBtn3;
    @BindView(R.id.ll_btn3)
    LinearLayout llBtn3;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.collec_man_info)
    TextView collecManInfo;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.person_info)
    LinearLayout personInfo;
    @BindView(R.id.set_up)
    LinearLayout setUp;
    @BindView(R.id.drawL)
    DrawerLayout drawL;

    private int currentTab = 0;
    private FragmentManager fm;
    private List<Fragment> fragments;
    private NearByShopFragment nearByShopFragment;
    private NotLocateFragment notLocateFragment;
    private MeAddShopFragment meAddShopFragment;
    private TextView[] tvs;
    private MeInfoPopup meInfoPopup;
    private UserBean userBean;
    //纬度
    public String latitude = "";
    //经度
    public String longtitude = "";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    protected void onConnected(BluetoothSocket socket, int taskType) {
        super.onConnected(socket, taskType);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_info_collect;
    }

    @Override
    protected void initData() {
        super.initData();
        PermissionUtils.requstPermission(this, 10, null);
        userBean = UserInfoUtils.getUser(this);
        collecManInfo.setText(userBean.getCompanyName() + "@" + userBean.getBud_name());
        fragments = new ArrayList<>();
        nearByShopFragment = new NearByShopFragment();
        notLocateFragment = new NotLocateFragment();
        meAddShopFragment = new MeAddShopFragment();
        fragments.add(nearByShopFragment);
        fragments.add(notLocateFragment);
        fragments.add(meAddShopFragment);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_comment, fragments.get(0));
        ft.commit();

        tvs = new TextView[3];
        tvs[0] = tvBtn1;
        tvs[1] = tvBtn2;
        tvs[2] = tvBtn3;
        initMeInfoPopup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /* @OnClick({R.id.ll_btn1, R.id.ll_btn2, R.id.ll_btn3})
     public void onClick(View view) {
         switch (view.getId()) {
             case R.id.ll_btn1:
                 addFragment(0);
                 //changeColor(0);
                 break;
             case R.id.ll_btn2:
                 addFragment(1);
                // changeColor(1);
                 break;
             case R.id.ll_btn3:
                 addFragment(2);
                 //changeColor(2);
                 break;
         }
     }
 */
    /*添加fragment*/
    private void addFragment(int index) {
        showTab(index);
        if (!fragments.get(index).isAdded()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_comment, fragments.get(index));
            ft.commit();
        } else {
            showTab(index);
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

    /*改变图标和字体的颜色*/
    private void changeColor(int index) {
        for (int i = 0; i < tvs.length; i++) {
            tvs[i].setBackgroundColor(UIUtils.getColor(R.color.white));
            tvs[i].setTextColor(UIUtils.getColor(R.color.text_gray));
        }
        tvs[index].setBackgroundResource(R.drawable.bg_inventory_manager_title_true);
        tvs[index].setTextColor(UIUtils.getColor(R.color.white));
    }

    private long exitTime;

    // 点击两次退出（2秒内）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                showToast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(getPackageName());
                AppManager.getInstance().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        //animator.addListener(animatorListener);
        animator.setDuration(400).start();
    }

    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim_in);
        view.startAnimation(animation);
    }

    /**
     * PopupWindow消失的动画
     */
    private void translateAnimOut(View view, final PopupWindow pw) {
        //setTab(PublicFinal.ALLDEFAULT);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim_out);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /*初始化个人信息popup*/
    private void initMeInfoPopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        meInfoPopup = new MeInfoPopup(this, dm.widthPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {

            }
        }, this);
    }

    /*显示个人信息popup*/
    private void showPwMeInfoPopup() {
        meInfoPopup.showAtLocation(collectShopInfo, Gravity.LEFT, 0, 0);
    }

    @Override
    public void itemPerson() {

    }

    @Override
    public void itemSetup() {

    }

    @OnClick({R.id.ll_me_info, R.id.rl_btn1, R.id.ll_btn2, R.id.ll_btn3, R.id.ll_add_client, R.id.person_info, R.id.set_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_me_info:
                //showPwMeInfoPopup();
                mobile.setText(userBean.getUserName());
                drawL.openDrawer(Gravity.LEFT);
                break;
            case R.id.rl_btn1:
                addFragment(0);
                changeColor(0);
                moveAnimation(tvBtn1);
                break;
            case R.id.ll_btn2:
                addFragment(1);
                changeColor(1);
                moveAnimation(tvBtn2);
                break;
            case R.id.ll_btn3:
                addFragment(2);
                changeColor(2);
                moveAnimation(tvBtn3);
                break;
            case R.id.ll_add_client://采集客户信息
                PermissionUtils.requstActivityLocation(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(ShopInfoCollectActivity.this, CollectClientActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }

                    @Override
                    public void onDilogCancal() {
                    }
                });

                break;
            case R.id.person_info:
                startActivity(MeInfoActivity.class);
                break;
            case R.id.set_up:
                startActivity(SetUpActivity.class);
                break;
        }
    }
}
