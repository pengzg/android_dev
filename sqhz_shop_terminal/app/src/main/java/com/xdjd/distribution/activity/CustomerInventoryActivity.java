package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.event.InventoryPhotoEvent;
import com.xdjd.distribution.fragment.InventoryEditFragment;
import com.xdjd.distribution.fragment.InventorySubmitFragment;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.NoScrollViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerInventoryActivity extends BaseActivity {

    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.viewpager_common)
    NoScrollViewPager mViewpagerCommon;
    @BindView(R.id.right_image)
    ImageView mRightImage;

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    private List<Fragment> fragments;
    private InventoryEditFragment fragmentEdit;
    private InventorySubmitFragment fragmentSubmit;

    private MyFragmentAdapter myAdapter;
    private int index = 0;

    /**
     * 客户盘点信息
     */
    public List<GoodsBean> listInventry = new ArrayList<>();

    // 上传后返回的图片路径
    public ArrayList<String> pathList = new ArrayList<String>();
    // 图片sd地址
    public List<String> drr = new ArrayList<String>();

    private ClientBean mClientBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_inventory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mTitle.setText("客户盘点");
        mRlRightQr.setVisibility(View.VISIBLE);
        mRlRightSearch.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(R.mipmap.white_camera);

        mClientBean = UserInfoUtils.getClientInfo(this);

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });

        fragmentEdit = new InventoryEditFragment();
        fragmentSubmit = new InventorySubmitFragment();

        fragments = new ArrayList<>();
        fragments.add(fragmentEdit);
        fragments.add(fragmentSubmit);

        myAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewpagerCommon.setAdapter(myAdapter);

        mViewpagerCommon.setCurrentItem(index);
    }

    @OnClick({R.id.left_layout, R.id.rl_right_search, R.id.rl_right_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                if (mViewpagerCommon.getCurrentItem() == 0) {
                    if (listInventry.size() == 0) {
                        finish();
                    } else {
                        DialogUtil.showCustomDialog(this, "提示", "确定放弃编辑退出?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                finish();
                            }

                            @Override
                            public void no() {
                            }
                        });
                    }
                } else {
                    index = 0;
                    mViewpagerCommon.setCurrentItem(0);
                }
                break;
            case R.id.rl_right_search://搜索
                showPopupSeaarh();
                break;
            case R.id.rl_right_qr://给商品拍照
                Intent intent = new Intent(this,TakingPicturesActivity.class);
                intent.putExtra("type", PublicFinal.INVENTORY);
                intent.putExtra("drr", (Serializable) drr);
                intent.putExtra("pathList",pathList);
                intent.putExtra("customer",mClientBean);
                startActivity(intent);
                break;
        }
    }

    public void toApplyFragment() {
        if (!fragmentEdit.addGoods()) {
            return;
        }
        index = 1;
        mViewpagerCommon.setCurrentItem(index);
        fragmentSubmit.refreshData(fragmentEdit);
    }

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this, mLlMain, new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                if (index == 0) {//编辑
                    fragmentEdit.searchGoods(searchStr);
                } else {//提交
                    fragmentSubmit.searchGoods(searchStr);
                }
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(InventoryPhotoEvent event){
        drr = event.getDrr();
        pathList = event.getPathList();
        if (pathList != null && pathList.size() > 0){
            LogUtils.e("pathList",pathList.size()+"--");
        }
    }

    public LinearLayout getLlMain() {
        return mLlMain;
    }
}
