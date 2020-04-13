package com.xdjd.shopInfoCollect.main;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.LoginActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.shopInfoCollect.activity.CollectClientActivity;
import com.xdjd.shopInfoCollect.activity.SetUpActivity;
import com.xdjd.shopInfoCollect.main.fragment.MeAddShopFragment;
import com.xdjd.shopInfoCollect.main.fragment.NotLocateFragment;
import com.xdjd.shopInfoCollect.main.fragment.ShopMapFragment;
import com.xdjd.steward.adapter.PurchaseViewpageAdapter;
import com.xdjd.steward.bean.CustomerNumBean;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/9
 *     desc   : 店铺采集main-activity
 *     version: 1.0
 * </pre>
 */

public class ShopCollectMainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.drawer_main)
    DrawerLayout mDrawerMain;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_btn1)
    TextView mTvBtn1;
    @BindView(R.id.rl_btn1)
    RelativeLayout mRlBtn1;
    @BindView(R.id.tv_btn2)
    TextView mTvBtn2;
    @BindView(R.id.ll_btn2)
    LinearLayout mLlBtn2;
    @BindView(R.id.tv_btn3)
    TextView mTvBtn3;
    @BindView(R.id.ll_btn3)
    LinearLayout mLlBtn3;
    @BindView(R.id.no_scroll_viewpage)
    NoScrollViewPager mNoScrollViewpage;
    @BindView(R.id.mobile)
    TextView mMobile;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.cv_add_client)
    CardView mCvAddClient;
    @BindView(R.id.ll_me_info)
    LinearLayout mLlMeInfo;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.person_info)
    LinearLayout mPersonInfo;
    @BindView(R.id.set_up)
    LinearLayout mSetUp;
    @BindView(R.id.today_num)
    TextView mTodayNum;
    @BindView(R.id.total_num)
    TextView mTotalNum;
    @BindView(R.id.ll_left_layout)
    LinearLayout mLlLeftLayout;

    private UserBean userBean;
    private TextView[] tvs;

    private List<Fragment> fragments;
    private PurchaseViewpageAdapter viewpageAdapter;

    private ShopMapFragment shopMapFragment;
    private NotLocateFragment notLocateFragment;
    private MeAddShopFragment meAddShopFragment;

    private String startDate;
    private String endDate;

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_collect_main;
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
        PermissionUtils.requstPermission(this, 10, null);
        userBean = UserInfoUtils.getUser(this);

        mTvTitle.setText(userBean.getCompanyName() + "@" + userBean.getBud_name());

        fragments = new ArrayList<>();
        shopMapFragment = new ShopMapFragment();
        notLocateFragment = new NotLocateFragment();
        meAddShopFragment = new MeAddShopFragment();

        fragments.add(shopMapFragment);
        fragments.add(notLocateFragment);
        fragments.add(meAddShopFragment);

        viewpageAdapter = new PurchaseViewpageAdapter(getSupportFragmentManager(), fragments);
        mNoScrollViewpage.setAdapter(viewpageAdapter);
        mNoScrollViewpage.setCurrentItem(0);
        mNoScrollViewpage.setOnPageChangeListener(this);

        tvs = new TextView[3];
        tvs[0] = mTvBtn1;
        tvs[1] = mTvBtn2;
        tvs[2] = mTvBtn3;

        startDate = DateUtils.getDataTime(DateUtils.dateFormater4);
        endDate = DateUtils.getDataTime(DateUtils.dateFormater4);
    }

    @OnClick({R.id.rl_btn1, R.id.ll_btn2, R.id.ll_btn3, R.id.cv_add_client, R.id.ll_me_info, R.id.set_up,R.id.ll_left_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_btn1:
                mNoScrollViewpage.setCurrentItem(0);
                changeColor(0);
                moveAnimation(mTvBtn1);
                break;
            case R.id.ll_btn2:
                mNoScrollViewpage.setCurrentItem(1);
                changeColor(1);
                moveAnimation(mTvBtn2);
                break;
            case R.id.ll_btn3:
                mNoScrollViewpage.setCurrentItem(2);
                changeColor(2);
                moveAnimation(mTvBtn3);
                break;
            case R.id.cv_add_client:
                PermissionUtils.requstActivityLocation(ShopCollectMainActivity.this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(ShopCollectMainActivity.this, CollectClientActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }

                    @Override
                    public void onDilogCancal() {
                    }
                });
                break;
            case R.id.ll_me_info:
                loadCustomerNum();

                mTvUserName.setText(userBean.getUserName());
                if (userBean.getMobile() == null || userBean.getMobile().length() == 0) {
                    mMobile.setVisibility(View.GONE);
                } else {
                    mMobile.setVisibility(View.VISIBLE);
                    mMobile.setText(userBean.getMobile());
                }

                mDrawerMain.openDrawer(Gravity.LEFT);
                break;
            case R.id.set_up://设置
                startActivity(SetUpActivity.class);
                break;
            case R.id.ll_left_layout:
                mDrawerMain.closeDrawers();
                break;
        }
    }

    /**
     * 获取采集客户数量
     */
    private void loadCustomerNum() {
        AsyncHttpUtil<CustomerNumBean> httpUtil = new AsyncHttpUtil<>(ShopCollectMainActivity.this, CustomerNumBean.class, new IUpdateUI<CustomerNumBean>() {
            @Override
            public void updata(CustomerNumBean bean) {
                if (bean.getRepCode().equals("00")) {
                    mTodayNum.setText("今日采集:" + String.valueOf(bean.getNewCustomerNum()));
                    mTotalNum.setText("累计采集:" + String.valueOf(bean.getTotalCustomerNum()));
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getCustomerNum, G_RequestParams.getCustomerNum(startDate.replace(".", "-"), endDate.replace(".", "-")), false);
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

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        //animator.addListener(animatorListener);
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
    protected void onResume() {
        super.onResume();
        //如果日期不等于今天日期也需要从新登录
        if (!UserInfoUtils.getLoginDate(this).equals(StringUtils.getDate2())) {
            UserInfoUtils.setLoginState(this, "0");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
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

}
