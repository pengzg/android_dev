package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ConfirmOrderAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.fragment.OrderDetailFragment;
import com.xdjd.storebox.fragment.OrderStatusFragment;
import com.xdjd.utils.FragmentTabAdapter;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * 订单详情页
 * Created by lijipei on 2016/12/1.
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener,ConfirmOrderAdapter.GpidListener {
    @BindView(R.id.left_back)
    LinearLayout leftBack;
    @BindView(R.id.detail_title)
    TextView detailTitle;
    @BindView(R.id.status_title)
    TextView statusTitle;

    private FragmentManager fragmentManager;
    private List<Fragment> mfragmentList;/*fragment 集合*/
    private FragmentTabAdapter fragmentTabAdapter;/*fragment 适配器*/
    private int currentTab = 0;//当前页索引
    // 主菜单按钮
    private View[] lls;
    private String OrderId;
    private String btnFlag;
    private ConfirmOrderAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        OrderId = getIntent().getStringExtra("orderId");
        btnFlag = getIntent().getStringExtra("btnFlag");
        initView();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//返回键回到订单界面，避免返回出现空白问题
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(OrderDetailActivity.this, MyOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }

    private void initView() {
        lls = new View[2];
        //添加两个fragment
        mfragmentList = new ArrayList<>();
        mfragmentList.add(new OrderDetailFragment());
        mfragmentList.add(new OrderStatusFragment());
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();//默认显示第一页
        ft.add(R.id.detail_container, mfragmentList.get(currentTab));
        ft.addToBackStack(mfragmentList.get(currentTab).getClass().getName());
        ft.commit();
    }

    public  String  getFlag(){
        return OrderId;
    }

    public String getBtnFlag(){
        return btnFlag;
    }

    @OnClick({R.id.left_back, R.id.detail_title, R.id.status_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_back:
//                Intent intent = new Intent(OrderDetailActivity.this, MyOrderActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
                break;
            case R.id.detail_title:
                setSelectFragment(0);//订单详情
                break;
            case R.id.status_title:
                setSelectFragment(1);//订单状态
                break;
        }
    }

    public void setSelectFragment(int i) {
        if (i == 0) {
            detailTitle.setBackgroundResource(R.drawable.title_bg);
            detailTitle.setTextColor(UIUtils.getColor(R.color.white));

            statusTitle.setBackgroundResource(R.color.white);
            statusTitle.setTextColor(UIUtils.getColor(R.color.black));
        } else {
            detailTitle.setBackgroundResource(R.color.white);
            detailTitle.setTextColor(UIUtils.getColor(R.color.black));

            statusTitle.setBackgroundResource(R.drawable.title_bg);
            statusTitle.setTextColor(UIUtils.getColor(R.color.white));
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
            ft.add(R.id.detail_container, fragment);
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

    @Override
    public void getGpid(String gpid,String ggpid) {
        Log.e("gpid",gpid);
    }
}
