package com.bikejoy.testdemo.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseDataTypeCodeConstant;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.fragment.QsAndRechargeOrderFragment;
import com.bikejoy.testdemo.fragment.WaterOrderFragment;
import com.bikejoy.testdemo.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 订单
 *     version: 1.0
 * </pre>
 */

public class OrderMainFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.fl_comment)
    FrameLayout mFlComment;

    public FragmentManager fm;
    public int currentTab = 0; // 当前Tab页面索引

    public List<Fragment> fragments ;

    //订单类型 1 水票订单  2 送水订单 3 购物订单
    private WaterOrderFragment waterOrderFragment;
    private OrderFragment orderFragment;
    private OrderFragment qsOrderFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

        qsOrderFragment = new OrderFragment();
        waterOrderFragment = new WaterOrderFragment(null);
        orderFragment = new OrderFragment();

        Bundle bundleWater = new Bundle();
        bundleWater.putString("orderType", BaseDataTypeCodeConstant.order_type01);
        waterOrderFragment.setArguments(bundleWater);

        Bundle bundleOrder = new Bundle();
        bundleOrder.putString("orderType", BaseDataTypeCodeConstant.order_type02);//配送订单
        orderFragment.setArguments(bundleOrder);

        fragments = new ArrayList<>();
        fragments.add(orderFragment);
        fragments.add(new QsAndRechargeOrderFragment("3"));
        fragments.add(waterOrderFragment);
        fragments.add(new QsAndRechargeOrderFragment("4"));

        fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_comment, fragments.get(0));
        ft.commit();
    }

    /*@OnClick({R.id.tv_order, R.id.tv_giving_order})
    public void onViewClicked(View view) {
        convert (view.getId()) {
            case R.id.tv_order:
                mTvOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvGivingOrder.setTextColor(UIUtils.getColor(R.color.text_gray));

                if (!fragments.get(0).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(0));
                    ft.commit();
                } else {
                    showTab(0);
                }
                moveAnimation(mTvOrder);
                break;
            case R.id.tv_giving_order:
                mTvGivingOrder.setTextColor(UIUtils.getColor(R.color.white));
                mTvOrder.setTextColor(UIUtils.getColor(R.color.text_gray));

                if (!fragments.get(1).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(1));
                    ft.commit();
                } else {
                    showTab(1);
                }
                moveAnimation(mTvGivingOrder);
                break;
        }
    }*/
    /**
     * 切换tab
     *
     * @param idx
     */
    public void showTab(int idx) {
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
//        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
//        animator.setDuration(400).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
