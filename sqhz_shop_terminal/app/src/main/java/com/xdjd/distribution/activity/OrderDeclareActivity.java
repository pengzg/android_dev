package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
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
import com.xdjd.distribution.bean.OrderDetailBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.fragment.GoodsEditFragment;
import com.xdjd.distribution.fragment.OrderGoodsEditFragment;
import com.xdjd.distribution.fragment.OrderGoodsSubmitFragment;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollViewPager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class OrderDeclareActivity extends BaseActivity {

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
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private List<Fragment> fragments;
    private OrderGoodsEditFragment fragmentEdit;
    private OrderGoodsSubmitFragment fragmentSubmit;
    //    private GoodsEditFragment fragmentEdit;

    private MyFragmentAdapter myAdapter;
    private int index = 0;

    /**
     * 仓库id
     */
    public String storehouseId = "";

    /**
     * 发货时间
     */
    public String deliveryTime;

    /**
     * 备注
     */
    public String note;
    /**
     * 订单复制参数
     */
    private GoodsBean beanCopy;

    /**
     * 订单选中的商品列表
     */
    public List<GoodsBean> listGoodsOrder = new ArrayList<>();

    /**
     * 处理单选中的商品列表
     */
    public List<GoodsBean> listProcessOrder = new ArrayList<>();

    /**
     * 换货单选中的商品列表
     */
    public List<GoodsBean> listExchangeOrder = new ArrayList<>();

    /**
     * 退货单选中的商品列表
     */
    public List<GoodsBean> listRefundOrder = new ArrayList<>();
    /**
     * 还货单
     */
    public List<GoodsBean> listGiveBack = new ArrayList<>();

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
        return R.layout.activity_order_to_declare;
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
        mTitle.setText("订单申报");
        mRlRightQr.setVisibility(View.VISIBLE);
        mRlRightSearch.setVisibility(View.VISIBLE);

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });

        userBean = UserInfoUtils.getUser(this);

        clientBean = UserInfoUtils.getClientInfo(this);

        storehouseId = getIntent().getStringExtra("storehouseId");
        deliveryTime = getIntent().getStringExtra("deliveryTime");
        note = getIntent().getStringExtra("note");
        //订单复制参数
        beanCopy = (GoodsBean) getIntent().getSerializableExtra("beanCopy");
        // 1 普通 2 处理 3 退货 4 换货 5 还货
        if (beanCopy!=null){
            List<GoodsBean> listCopy = beanCopy.getListData();
            for (int i = 0; i<listCopy.size();i++){

                if ("".equals(listCopy.get(i).getGgs_stock()) || listCopy.get(i).getGgs_stock() == null) {
                    listCopy.get(i).setStock_nameref("无库存");
                } else {
                    BigDecimal bdStock = new BigDecimal(listCopy.get(i).getGgs_stock());
                    BigDecimal bdUnitNum = new BigDecimal(listCopy.get(i).getGgp_unit_num());

                    if ("1".equals(listCopy.get(i).getGgp_unit_num())) {
                        String stockStr = listCopy.get(i).getGgs_stock() + listCopy.get(i).getGg_unit_min_nameref();
                        listCopy.get(i).setStock_nameref("库存" + stockStr);
                    } else {
                        //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
                        //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
                        // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
                        BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);

                        String stockStr = results[0].toBigInteger() + listCopy.get(i).getGg_unit_max_nameref() +
                                results[1] + listCopy.get(i).getGg_unit_min_nameref();
                        listCopy.get(i).setStock_nameref("库存" + stockStr);
                    }
                }

                listCopy.get(i).setMaxPrice(listCopy.get(i).getMax_price());
                listCopy.get(i).setMinPrice(listCopy.get(i).getMin_price());
                listCopy.get(i).setMaxNum(listCopy.get(i).getMax_num());
                listCopy.get(i).setMinNum(listCopy.get(i).getMin_num());
                listCopy.get(i).setSaleType(listCopy.get(i).getSaletype());
                listCopy.get(i).setSaleTypeName(listCopy.get(i).getSaletype_nameref());
                listCopy.get(i).setSaleTypeDiscount(listCopy.get(i).getSaletype_discount());

                BigDecimal bdMaxPrice;
                BigDecimal bdMaxNum;
                BigDecimal bdMinPrice;
                BigDecimal bdMinNum;
                BigDecimal bdSumPrice = null;

                if (TextUtils.isEmpty(listCopy.get(i).getMax_price())) {
                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMaxPrice = new BigDecimal(listCopy.get(i).getMax_price());
                }

                if (TextUtils.isEmpty(listCopy.get(i).getMax_num())) {
                    bdMaxNum = new BigDecimal(0);
                } else {
                    bdMaxNum = new BigDecimal(listCopy.get(i).getMax_num());
                }

                if (TextUtils.isEmpty(listCopy.get(i).getMin_price())) {
                    bdMinPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMinPrice = new BigDecimal(listCopy.get(i).getMin_price());
                }
                if (TextUtils.isEmpty(listCopy.get(i).getMin_num())) {
                    bdMinNum = new BigDecimal(0);
                } else {
                    bdMinNum = new BigDecimal(listCopy.get(i).getMin_num());
                }

                if ("1".equals(listCopy.get(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                    String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    listCopy.get(i).setTotalPrice(sumPrice);
                } else {
                    bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
                    listCopy.get(i).setTotalPrice(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            }


            switch (beanCopy.getOm_ordertype()){
                case 1:
                    listGoodsOrder = listCopy;
                    break;
                case 2:
                    listProcessOrder = listCopy;
                    break;
                case 3:
                    listRefundOrder = listCopy;
                    break;
                case 4:
                    listExchangeOrder = listCopy;
                    break;
            }
        }

//        fragmentEdit = new OrderGoodsEditFragment();
        fragmentEdit = new OrderGoodsEditFragment();
        fragmentSubmit = new OrderGoodsSubmitFragment();

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
                    if (listGoodsOrder.size() == 0 && listProcessOrder.size() == 0 &&
                            listExchangeOrder.size() == 0 && listRefundOrder.size() == 0) {
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
                    fragmentEdit.refreshData(fragmentSubmit.indexOrder);
                }
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

    public void toSubmitFragment() {
        if (!fragmentEdit.addGoods()) {
            return;
        }
        index = 1;
        mViewpagerCommon.setCurrentItem(index);
        fragmentSubmit.refreshData(fragmentEdit.indexOrder);
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        if (mViewpagerCommon.getCurrentItem() == 0) {
            if (listGoodsOrder.size() == 0 && listProcessOrder.size() == 0 &&
                    listExchangeOrder.size() == 0 && listRefundOrder.size() == 0) {
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
            fragmentEdit.refreshData(fragmentSubmit.indexOrder);
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
                            fragmentEdit.addGoods(bean);
                        } else {
                            showToast("没有对应的商品");
                        }
                    } else {
                        if (bean.getGgp_id() != null && !"".equals(bean.getGgp_id())) {
                            fragmentSubmit.addGoods(bean);
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

}
