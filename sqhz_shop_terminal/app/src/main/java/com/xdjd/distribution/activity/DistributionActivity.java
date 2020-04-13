package com.xdjd.distribution.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.fragment.DistributionSubmitFragment;
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
 *     time   : 2017/5/26
 *     desc   : 配送任务商品列表操作界面
 *     version: 1.0
 * </pre>
 */

public class DistributionActivity extends BaseActivity {

    @BindView(R.id.viewpager_common)
    NoScrollViewPager mViewpagerCommon;
    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.right_image)
    ImageView mRightImage;

    private List<Fragment> fragments;
    //    private DistributionEditFragment fragmentEdit;
    private DistributionSubmitFragment fragmentSubmit;

    private MyFragmentAdapter myAdapter;

    private int index = 0;

    /**
     * 订单选中的商品列表
     */
    public List<OrderBean> listGoodsOrder = new ArrayList<>();

    /**
     * 处理单选中的商品列表
     */
    public List<OrderBean> listProcessOrder = new ArrayList<>();

    /**
     * 换货单选中的商品列表
     */
    public List<OrderBean> listExchangeOrder = new ArrayList<>();

    /**
     * 退货单选中的商品列表
     */
    public List<OrderBean> listRefundOrder = new ArrayList<>();

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    private String title = "";
    private CustomerTaskBean bean;

    @Override
    protected int getContentView() {
        return R.layout.activity_distribution;
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
        bean = (CustomerTaskBean) getIntent().getSerializableExtra("bean");
        if (bean!=null){
            title = bean.getCc_name();
        }

        userBean = UserInfoUtils.getUser(this);

        mTitle.setText(title);
        mRlRightQr.setVisibility(View.VISIBLE);
        //        mRlRightSearch.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(R.mipmap.route_white);

        clientBean = UserInfoUtils.getClientInfo(this);

        //        fragmentEdit = new DistributionEditFragment();
        fragmentSubmit = new DistributionSubmitFragment();

        fragments = new ArrayList<>();
        //        fragments.add(fragmentEdit);
        fragments.add(fragmentSubmit);

        myAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewpagerCommon.setAdapter(myAdapter);

        mViewpagerCommon.setCurrentItem(index);
    }

    @OnClick({R.id.left_layout, R.id.rl_right_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.rl_right_qr://百度地图导航
                if (bean == null){
                    showToast("客户没有维护位置信息,请先维护客户位置信息!");
                    return;
                }

                //地图导航
                boolean baidu = isAvilible(DistributionActivity.this, Comon.MY_BAIDU_PACKAGE);
                if (baidu) {
                    DialogUtil.showCustomDialog(DistributionActivity.this, "提示", "是否开启百度地图导航?", "是", "否", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            Intent intent;
                            intent = new Intent();
                            intent.setData(Uri.parse("baidumap://map/navi?location=" + bean.getCc_latitude() +
                                    "," + bean.getCc_longitude() ));
                            startActivity(intent);
                        }

                        @Override
                        public void no() {
                        }
                    });
                }else{
                    showToast("请安装百度地图软件");
                }
                break;
        }
    }

    private boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void toSubmitFragment() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //        if (mViewpagerCommon.getCurrentItem() == 0) {
        //            if (listGoodsOrder.size() == 0 && listProcessOrder.size() == 0 &&
        //                    listExchangeOrder.size() == 0 && listRefundOrder.size() == 0) {
        //                finish();
        //            } else {
        //                DialogUtil.showCustomDialog(this, "提示", "确定放弃编辑退出?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
        //                    @Override
        //                    public void ok() {
        //                        finish();
        //                    }
        //
        //                    @Override
        //                    public void no() {
        //                    }
        //                });
        //            }
        //        } else {
        //            index = 0;
        //            mViewpagerCommon.setCurrentItem(0);
        //        }
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
                        //                        fragmentEdit.addGoods(bean);
                    } else {
                        //                        fragmentSubmit.addGoods(bean);
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
                userBean.getUserId(), clientBean.getCc_id(), search_code, ""), true);
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
}
