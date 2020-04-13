package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.fragment.RefundEditFragment;
import com.xdjd.distribution.fragment.RefundSumbitFragment;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/22
 *     desc   : 退货申请
 *     version: 1.0
 * </pre>
 */

public class  RefundApplyActivity extends BaseActivity {
    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.viewpager_common)
    NoScrollViewPager mViewpagerCommon;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private FragmentManager fm;
    private int currentTab; // 当前Tab页面索引

    private List<Fragment> fragments;

    private MyFragmentAdapter myAdapter;

    /**
     * 仓库id
     */
    public String storehouseId = "";

    private RefundEditFragment todayFragment;
    private RefundSumbitFragment returnGoodsFragment;

    private int index = 0;

    /**
     * 今日退货
     */
    public List<GoodsBean> listTodayRefund = new ArrayList<>();

    /**
     * 退货申请
     */
    public List<GoodsBean> listRefund = new ArrayList<>();

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply;
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
        userBean = UserInfoUtils.getUser(this);

        mTitle.setText("退货申请");
        mRlRightSearch.setVisibility(View.VISIBLE);
        mRlRightQr.setVisibility(View.VISIBLE);

        storehouseId = getIntent().getStringExtra("storehouseId");
        clientBean = UserInfoUtils.getClientInfo(this);

        todayFragment = new RefundEditFragment();
        returnGoodsFragment = new RefundSumbitFragment();

        fragments = new ArrayList<>();
        fragments.add(todayFragment);
        fragments.add(returnGoodsFragment);

        myAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewpagerCommon.setAdapter(myAdapter);

        mViewpagerCommon.setCurrentItem(index);

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });
    }

    @OnClick({R.id.left_layout, R.id.rl_right_search, R.id.rl_right_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                if (mViewpagerCommon.getCurrentItem() == 0) {
                    if (listTodayRefund.size() == 0 && listRefund.size() == 0) {
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
                    todayFragment.refreshData();
                }
                break;
            case R.id.rl_right_search:
                showPopupSeaarh();
                break;
            case R.id.rl_right_qr:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                break;
        }
    }

    public void toApplyFragment() {
        if (!todayFragment.addGoods()) {
            return;
        }
        index = 1;
        mViewpagerCommon.setCurrentItem(index);
        returnGoodsFragment.refreshData(todayFragment);
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

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this, mLlMain,new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                if (index == 0) {//编辑
                    todayFragment.searchGoods(searchStr);
                } else {//提交
                    returnGoodsFragment.searchGoods(searchStr);
                }
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }

    @Override
    public void onBackPressed() {
        if (mViewpagerCommon.getCurrentItem() == 0) {
            if (listTodayRefund.size() == 0 && listRefund.size() == 0) {
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
            todayFragment.refreshData();
        }
    }

    /**
     * 二维码查询
     *
     * @param search_code
     */
    private void getGoodsDetail(String search_code) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (index == 0) {
                        if (bean.getGgp_id() != null && !"".equals(bean.getGgp_id())) {
                            todayFragment.addGoods(bean);
                        } else {
                            showToast("没有对应的商品");
                        }
                    } else {
                        if (bean.getGgp_id() != null && !"".equals(bean.getGgp_id())) {
                            returnGoodsFragment.addGoods(bean);
                        } else {
                            showToast("没有对应的商品");
                        }
                    }
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
        httpUtil.post(M_Url.getGoodsDetail, L_RequestParams.getGoodsDetail(
                userBean.getUserId(), clientBean.getCc_id(), search_code, storehouseId), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Comon.QR_GOODS_REQUEST_CODE) {
            switch (resultCode) {
                case Comon.QR_GOODS_RESULT_CODE:
                    String result = data.getStringExtra("result");
                    if (result == null || result.equals("")) {
                    } else {
                        getGoodsDetail(result);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
