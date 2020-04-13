package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.fragment.AllIntegralFragment;
import com.xdjd.storebox.fragment.HaveObtainedIntegralFragment;
import com.xdjd.storebox.fragment.HaveUsedIntegralFragment;
import com.xdjd.utils.FragmentTabAdapter;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class MyIntegralActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.tv_02)
    TextView tv02;
    @BindView(R.id.tv_03)
    TextView tv03;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.integral_account)
    TextView integralAccount;

    private FragmentManager fragmentManager;
    private List<Fragment> mfragmentList;/*fragment 集合*/
    private FragmentTabAdapter fragmentTabAdapter;/*fragment 适配器*/
    private int currentTab = 0;//当前页索引
    // 主菜单按钮
    private View[] lls;
    private String totalI;//积分账户

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("积分明细");
        //titleBar.setRightText("积分规则");
        titleBar.setRightTextColor(R.color.text_black_212121);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(IntegralRuleActivity.class);
            }
        });
        totalI = getIntent().getStringExtra("totalI");
        Log.e("total:",totalI);
        integralAccount.setText(totalI);
        initView();
    }

    private void initView() {
        lls = new View[3];
        //添加3个fragment
        mfragmentList = new ArrayList<>();
        mfragmentList.add(new AllIntegralFragment());
        mfragmentList.add(new HaveUsedIntegralFragment());
        mfragmentList.add(new HaveObtainedIntegralFragment());
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();//默认显示第一页
        ft.add(R.id.fl_container, mfragmentList.get(currentTab));
        ft.addToBackStack(mfragmentList.get(currentTab).getClass().getName());
        ft.commit();
    }

    public void setSelectFragment(int i) {
        if (i == 0) {
            tv01.setBackgroundResource(R.drawable.shape_integral_btn_bg);
            tv01.setTextColor(UIUtils.getColor(R.color.white));

            tv02.setBackgroundResource(R.color.white);
            tv02.setTextColor(UIUtils.getColor(R.color.black));

            tv03.setBackgroundResource(R.color.white);
            tv03.setTextColor(UIUtils.getColor(R.color.black));
        } else if (i == 1) {
            tv02.setBackgroundResource(R.drawable.shape_integral_btn_bg);
            tv02.setTextColor(UIUtils.getColor(R.color.white));

            tv01.setBackgroundResource(R.color.white);
            tv01.setTextColor(UIUtils.getColor(R.color.black));

            tv03.setBackgroundResource(R.color.white);
            tv03.setTextColor(UIUtils.getColor(R.color.black));
        } else {
            tv03.setBackgroundResource(R.drawable.shape_integral_btn_bg);
            tv03.setTextColor(UIUtils.getColor(R.color.white));

            tv02.setBackgroundResource(R.color.white);
            tv02.setTextColor(UIUtils.getColor(R.color.black));

            tv01.setBackgroundResource(R.color.white);
            tv01.setTextColor(UIUtils.getColor(R.color.black));
        }

        Fragment fragment = mfragmentList.get(i);
        FragmentTransaction ft = obtainFragmentTransaction(i);//动画
        //                getCurrentFragment().onPause(); // 暂停当前tab
        getCurrentFragment().onStop(); // 暂停当前tab
        YesOrNoLoadingOnstart.INDEX_ID = i;
        YesOrNoLoadingOnstart.INDEX = true;
        if (fragment.isAdded()) {
            if (YesOrNoLoadingOnstart.INDEX == true) {
                fragment.onStart(); // 启动目标tab的onStart()
                //                       fragment.onResume(); // 启动目标tab的onResume()
            }

        } else {
            ft.addToBackStack(fragment.getClass().getName());
            ft.add(R.id.fl_container, fragment);
        }
        showTab(i); // 显示目标tab
        ft.commit();
    }

    public Fragment getCurrentFragment() {
        return mfragmentList.get(currentTab);
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

    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < mfragmentList.size(); i++) {
            Fragment fragment = mfragmentList.get(i);
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

    @OnClick({R.id.tv_01, R.id.tv_02, R.id.tv_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_01:
                setSelectFragment(0);
                break;
            case R.id.tv_02:
                setSelectFragment(1);
                break;
            case R.id.tv_03:
                setSelectFragment(2);
                break;
            /*case R.id.integral_store:
                startActivity(IntegralStoreActivity.class);//积分商城*//*
                break;*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyIntegralActivity.this, IntegralMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
