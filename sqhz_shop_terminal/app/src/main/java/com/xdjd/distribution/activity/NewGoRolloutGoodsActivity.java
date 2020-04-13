package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.GoRolloutGoodsCartAdapter;
import com.xdjd.distribution.adapter.NewGoRolloutGoodsAdapter;
import com.xdjd.distribution.adapter.NewGoRolloutGoodsCartAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.RolloutGoodsFinishEvent;
import com.xdjd.distribution.popup.EditCartNumPopupWindow;
import com.xdjd.distribution.popup.GoodsCartPopup;
import com.xdjd.distribution.popup.GoodsCategoryPopup;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

//import com.flipboard.bottomsheet.BottomSheetLayout;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/13
 *     desc   : 去铺货
 *     version: 1.0
 * </pre>
 */

public class NewGoRolloutGoodsActivity extends BaseActivity implements NewGoRolloutGoodsAdapter.GoRolloutGoodsListener {

    @BindView(R.id.lv_goods)
    PullToRefreshListView mLvGoods;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_category)
    TextView mTvCategory;
    @BindView(R.id.ll_select_category)
    LinearLayout mLlSelectCategory;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_cart_num)
    TextView mTvCartNum;
    @BindView(R.id.rl_cart)
    RelativeLayout mRlCart;
    @BindView(R.id.lv_rollout_cart)
    ListView mLvRolloutCart;
    @BindView(R.id.ll_cart)
    LinearLayout mLlCart;
    @BindView(R.id.rl_cart_listing)
    RelativeLayout mRlCartListing;
    @BindView(R.id.ll_clear_cart)
    LinearLayout mLlClearCart;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.ll_bottom_left)
    LinearLayout mLlBottomLeft;
    @BindView(R.id.ll_main)
    RelativeLayout mLlMain;

    //商品类别
    private GoodsCategoryPopup mPwSelectCategory;
    private List<GoodsCategoryBean> listCategory = new ArrayList<>();
    //商品列表
    private List<GoodsBean> listGoods = new ArrayList<>();
    //已选中添加的商品列表
    private List<GoodsBean> listAddGoods = new ArrayList<>();

    //商品列表adapter
    private NewGoRolloutGoodsAdapter adapterGoods;
    //已选商品列表adapter
    private NewGoRolloutGoodsCartAdapter adapterAddGoods;

    /**
     * 商品购物车popup
     */
    private GoodsCartPopup mPwGoodsCart;

    //选择的客户信息
    private ClientBean clientBean;
    private UserBean userBean;
    //商品类别index
    private int firstIndex = 0;
    private int secondIndex = -1;
    private String categoryCode = "";

    private DisplayMetrics dm;

    private int page = 1;
    private int mFlag = 0;

    private BottomSheetLayout bottomSheetLayout;

    private EditCartNumPopupWindow pwEditCartNum;
    //销售类型
    private List<SaleTypeBean> listSaleType;

    @Override
    protected int getContentView() {
        return R.layout.activity_go_rollout_goods;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.setTitle("铺货");
        mTitleBar.leftBack(this);
        EventBus.getDefault().register(this);

        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);

        dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mLlCart.getLayoutParams();
        lp.topMargin = height / 4;
        mLlCart.setLayoutParams(lp);

        clientBean = UserInfoUtils.getClientInfo(this);
        userBean = UserInfoUtils.getUser(this);

        adapterGoods = new NewGoRolloutGoodsAdapter(this);
        mLvGoods.setAdapter(adapterGoods);
        adapterGoods.setData(listGoods);

        adapterAddGoods = new NewGoRolloutGoodsCartAdapter(this,new NewGoRolloutGoodsCartAdapter.GoRolloutGoodsListener() {
            @Override
            public void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                plusCalculation(i, mEtMaxNum);
                adapterAddGoods.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
                calculateCartGoodsPrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
                AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
            }

            @Override
            public void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                minusCalculation(i, mEtMaxNum);
                adapterAddGoods.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
                calculateCartGoodsPrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
                AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
            }

            @Override
            public void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                plusCalculation(i, mEtMinNum);
                adapterAddGoods.getItem(i).setMinNum(mEtMinNum.getText().toString());
                calculateCartGoodsPrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
                AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
            }

            @Override
            public void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                minusCalculation(i, mEtMinNum);
                adapterAddGoods.getItem(i).setMinNum(mEtMinNum.getText().toString());
                calculateCartGoodsPrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
                AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
            }

            @Override
            public void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                String newStr = num.replaceFirst("^0*", "");
                adapterAddGoods.getItem(i).setMaxNum(newStr);
                calculateCartGoodsPrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
            }

            @Override
            public void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                String newStr = num.replaceFirst("^0*", "");
                adapterGoods.getItem(i).setMinNum(newStr);
                calculateCartGoodsPrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
            }

            @Override
            public void onMaxPrice(int i, String editable, EditText mEtMaxPrice, EditText mEtMinPrice, TextView tvSumPrice, TextView mTvMaxPrice, TextView mTvMinPrice) {
                afterTextChanged(mEtMaxPrice, i, editable, mEtMaxPrice, mEtMinPrice, tvSumPrice, mTvMaxPrice, mTvMinPrice);
            }

            @Override
            public void onMinPrice(int i, String editable, EditText mEtMaxPrice, EditText mEtMinPrice, TextView tvSumPrice, TextView mTvMaxPrice, TextView mTvMinPrice) {
                afterTextChanged(mEtMinPrice, i, editable, mEtMaxPrice, mEtMinPrice, tvSumPrice, mTvMaxPrice, mTvMinPrice);
            }
        });
        adapterAddGoods.setData(listAddGoods);
        mLvRolloutCart.setAdapter(adapterAddGoods);

        mLvGoods.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mLvGoods);
        mLvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                page = 1;
                mFlag = 1;
                listGoods.clear();
                adapterGoods.notifyDataSetChanged();
                getGoodsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                page++;
                mFlag = 2;
                getGoodsList();
            }
        });

        initGoodsCategoryPopup();
        initCartPopup();
        initEditCartNum();
        getGoodsList();
        //获取商品类型
        getSaleTypeList(false, null);
    }

    private void afterTextChanged(EditText editText, int i, String editable, EditText mEtMaxPrice, EditText mEtMinPrice, TextView tvSumPrice,
                                  TextView mTvMaxPrice, TextView mTvMinPrice) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice;
        BigDecimal bdUnitNum = null;

        if (TextUtils.isEmpty(listAddGoods.get(i).getGgp_unit_num())) {
            bdUnitNum = new BigDecimal("1");
        } else {
            bdUnitNum = new BigDecimal(listAddGoods.get(i).getGgp_unit_num());
        }

        if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
        }

        if (TextUtils.isEmpty(listAddGoods.get(i).getMaxNum())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(listAddGoods.get(i).getMaxNum());
        }

        if (TextUtils.isEmpty(mEtMinPrice.getText())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
        }
        if (TextUtils.isEmpty(listAddGoods.get(i).getMinNum())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(listAddGoods.get(i).getMinNum());
        }

        if (editText == mEtMaxPrice) {
            String minPrice = bdMaxPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            mEtMinPrice.setText(minPrice);
            bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
        } else if (editText == mEtMinPrice) {
            if ("1".equals(listAddGoods.get(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                mEtMaxPrice.setText(editable.toString());
                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || mEtMaxPrice.getText().length() == 0) {
                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
                }
            } else {
                String maxPrice = bdUnitNum.multiply(bdMinPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtMaxPrice.setText(maxPrice);
                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || mEtMaxPrice.getText().length() == 0) {
                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
                }
            }
        }

        //设置价格
        mTvMaxPrice.setText(mEtMaxPrice.getText().toString());
        mTvMinPrice.setText(mEtMinPrice.getText().toString());
        adapterAddGoods.getItem(i).setMaxPrice(mEtMaxPrice.getText().toString());
        adapterAddGoods.getItem(i).setMinPrice(mEtMinPrice.getText().toString());

        if ("1".equals(adapterAddGoods.getItem(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            bdSumPrice = bdMinPrice.multiply(bdMinNum);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
        }
        adapterAddGoods.getItem(i).setTotalPrice(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        tvSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        calculateCartTotalPrice();
    }

    @OnClick({R.id.ll_select_category, R.id.ll_search, R.id.rl_cart, R.id.ll_bottom_left, R.id.tv_submit,
            R.id.rl_cart_listing, R.id.ll_clear_cart, R.id.ll_clear})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_select_category:
                if (listCategory == null || listCategory.size() == 0) {
                    queryGsCategory();
                } else {
                    showCategoryPopup();
                }
                break;
            case R.id.ll_search:
                intent = new Intent(this, SearchGoodsActivity.class);
                intent.putExtra("searchStr", mTvSearch.getText().toString());
                //                intent.putExtra("hint","商品编码/名称/条形码");
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_bottom_left:
            case R.id.rl_cart:
                if (listAddGoods.size() > 0) {
                    if (mRlCartListing.getVisibility() == View.VISIBLE) {
                        mRlCartListing.setVisibility(View.GONE);
                        return;
                    }
                    adapterAddGoods.notifyDataSetChanged();
                    mRlCartListing.setVisibility(View.VISIBLE);
                }
                //                                showCartPopup();
                //                adapterAddGoods.setData(listAddGoods);
                //                showBottomCart();
                break;
            case R.id.rl_cart_listing:
                mRlCartListing.setVisibility(View.GONE);
                break;
            case R.id.tv_submit:
                if (listAddGoods == null || listAddGoods.size() == 0) {
                    showToast("还没有选择铺货商品");
                    return;
                }

                for (GoodsBean bean : listAddGoods) {
                    if (bean.getMaxPrice() == null || bean.getMaxPrice().equals("")) {
                        bean.setMaxPrice("0.00");
                    }
                    if (bean.getMinPrice() == null || bean.getMinPrice().equals("")) {
                        bean.setMinPrice("0.00");
                    }
                }

                intent = new Intent(this, NewRolloutGoodsSubmitActivity.class);
                intent.putExtra("listRolloutGoods", (Serializable) listAddGoods);
                intent.putExtra("orderType", 1);
                startActivity(intent);
                break;
            case R.id.ll_clear_cart://清空购物车
                DialogUtil.showCustomDialog(this, "提示", "是否清空已选商品?", "清空", "取消",
                        new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                listAddGoods.clear();
                                adapterAddGoods.notifyDataSetChanged();
                                for (GoodsBean bean : listGoods) {
                                    bean.setMinNum("");
                                    bean.setMaxNum("");
                                }
                                adapterGoods.notifyDataSetChanged();
                                mRlCartListing.setVisibility(View.GONE);
                                mTvCartNum.setText("0");
                                mTvTotalPrice.setText("0.00");
                                isHasGoods();
                            }

                            @Override
                            public void no() {
                            }
                        });
                break;
            case R.id.ll_clear:
                mTvSearch.setText("");
                mLlClear.setVisibility(View.GONE);
                page = 1;
                mFlag = 1;
                listGoods.clear();
                adapterGoods.notifyDataSetChanged();
                getGoodsList();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                mTvSearch.setText(data.getStringExtra("searchStr"));
                if (mTvSearch.getText().length() > 0) {
                    mLlClear.setVisibility(View.VISIBLE);
                } else {
                    mLlClear.setVisibility(View.GONE);
                }
                page = 1;
                mFlag = 1;
                listGoods.clear();
                adapterGoods.notifyDataSetChanged();
                getGoodsList();
                break;
        }
    }

    /**
     * 根据商品获取商品销售类型
     */
    private void getSaleTypeList(final boolean isDialog, final GoodsBean bean) {
        AsyncHttpUtil<SaleTypeBean> httpUtil = new AsyncHttpUtil<>(this, SaleTypeBean.class, new IUpdateUI<SaleTypeBean>() {
            @Override
            public void updata(SaleTypeBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSaleType = jsonBean.getListData();
                    pwEditCartNum.setListSaleType(listSaleType);
                    if (isDialog) {
                        showEditCartNum(bean);
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getSaleTypeList, L_RequestParams.
                getSaleTypeList(userBean.getUserId(), "1", clientBean.getCc_id(),"2"), true);
    }

    /**
     * 获取分类
     */
    private void queryGsCategory() {
        AsyncHttpUtil<GoodsCategoryBean> httpUtil = new AsyncHttpUtil<>(this, GoodsCategoryBean.class, new IUpdateUI<GoodsCategoryBean>() {
            @Override
            public void updata(GoodsCategoryBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listCategory = jsonBean.getListData();
                    } else {
                        listCategory = new ArrayList<>();
                    }
                    if (listCategory != null && listCategory.size() > 0) {
                        showCategoryPopup();
                    } else {
                        showToast(jsonBean.getRepMsg());
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.phGsCategory, L_RequestParams.
                phGsCategory(clientBean.getCc_id(), String.valueOf(1),userBean.getSu_storeid()), true);
    }

    /**
     * 获取商品列表
     */
    private void getGoodsList() {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        for (GoodsBean bean : listAddGoods) {
                            for (int k = 0; k < jsonBean.getListData().size(); k++) {
                                //如果购物车中已存在这件商品
                                if (bean.getGgp_goodsid().equals(jsonBean.getListData().get(k).getGgp_goodsid())) {
                                    jsonBean.getListData().get(k).setIsHasCart("Y");
                                    break;
                                }
                            }
                        }
                        listGoods.addAll(jsonBean.getListData());
                        adapterGoods.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {
                mLvGoods.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getGoodsList, L_RequestParams.
                getGoodsList(userBean.getUserId(), clientBean.getCc_id(),
                        String.valueOf(1), mTvSearch.getText().toString(), categoryCode, userBean.getSu_storeid(),
                        "7", String.valueOf(page), "", ""), true);
    }

    /**
     * 初始化商品类别选择popup
     */
    private void initGoodsCategoryPopup() {
        mPwSelectCategory = new GoodsCategoryPopup(this, new GoodsCategoryPopup.OnGoodsCategoryListener() {
            @Override
            public void onItemCategory(int first, int second) {
                firstIndex = first;
                secondIndex = second;
                if (secondIndex == -1) {//没有二级分类
                    mTvCategory.setText(listCategory.get(firstIndex).getGc_name());
                    categoryCode = listCategory.get(firstIndex).getGc_code();
                } else {//有二级分类
                    mTvCategory.setText(listCategory.get(firstIndex).getSecondCategoryList().get(secondIndex).getGc_name());
                    categoryCode = listCategory.get(firstIndex).getSecondCategoryList().get(secondIndex).getGc_code();
                }
                page = 1;
                listGoods.clear();
                mFlag = 1;
                adapterGoods.notifyDataSetChanged();
                getGoodsList();
                mPwSelectCategory.dismiss();
            }
        });
    }

    private void showCategoryPopup() {
        //显示popup
        if (mPwSelectCategory.isShowing()) {
            mPwSelectCategory.dismiss();
            return;
        }
        if (Build.VERSION.SDK_INT < 24) {
            mPwSelectCategory.setData(listCategory);
            mPwSelectCategory.setIndex(firstIndex, secondIndex);
            mPwSelectCategory.showAsDropDown(mLlSelectCategory, 0, 3);
        } else {
            int[] a = new int[2];
            mLlSelectCategory.getLocationInWindow(a);
            mPwSelectCategory.setData(listCategory);
            mPwSelectCategory.setIndex(firstIndex, secondIndex);
            mPwSelectCategory.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, mLlSelectCategory.getHeight() + a[1] + 3);
            mPwSelectCategory.update();
        }
    }


    //购物车popup
    private void initCartPopup() {
        mPwGoodsCart = new GoodsCartPopup(this, dm.heightPixels);
    }

    private void showCartPopup() {
        mPwGoodsCart.showGoodsCartPopup(mLlBottom);
    }

    @Override
    public void onEditCartNum(int i) {
        showEditCartNum((GoodsBean) listGoods.get(i).clone());
    }

    //初始化编辑购物车数量弹框
    private void initEditCartNum() {
        pwEditCartNum = new EditCartNumPopupWindow(this, new EditCartNumPopupWindow.OnEditCartNumListener() {
            @Override
            public void editCart(GoodsBean goodsBean) {
                calculatePrice(goodsBean);
            }
        });
    }

    //显示编辑购物车弹框
    private void showEditCartNum(GoodsBean beanGoods) {
        if (listSaleType == null || listSaleType.size() == 0) {
            getSaleTypeList(true, beanGoods);
        } else {
            pwEditCartNum.showAddCart(beanGoods, mLlMain);
        }
    }

   /* @Override
    public void onShowEditNumAlter(LinearLayout itemShowAddnum) {
        if (itemShowAddnum.getVisibility() == View.GONE) {
            itemShowAddnum.setVisibility(View.VISIBLE);
            Animation inFromLeft = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, +1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            inFromLeft.setDuration(200);
            inFromLeft.setInterpolator(new AccelerateInterpolator());
            itemShowAddnum.startAnimation(inFromLeft);
        } else {
            Animation inFromLeft = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, +1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            inFromLeft.setDuration(200);
            inFromLeft.setInterpolator(new AccelerateInterpolator());
            itemShowAddnum.startAnimation(inFromLeft);
            itemShowAddnum.setVisibility(View.GONE);
        }
    }*/

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(int i, EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        et.setText((bd.intValue() + 1) + "");

    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private void minusCalculation(int i, EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            if (num == 0) {
                et.setText("0");
            } else if (num - 1 == 0) {
                et.setText("0");
            } else {
                et.setText(num - 1 + "");
            }
        }
    }

    /**
     * 计算adapter中item商品价格
     */
    private void calculatePrice(GoodsBean goodsBean) {
        BigDecimal goodsMaxNum;
        BigDecimal goodsMinNum;

        BigDecimal cartMaxNum, cartMinNum;//购物车中商品已有的数量
        BigDecimal cartMaxPrice = null;//购物车中大单位价格
        BigDecimal cartMinPrice = null;//购物车中小单位价格
        BigDecimal goodsSumPrice = BigDecimal.ZERO;//    总价格
        String sumPrice;

        //获取商品的大小单位数量
        if (TextUtils.isEmpty(goodsBean.getMaxNum())) {
            goodsMaxNum = new BigDecimal(0);
        } else {
            goodsMaxNum = new BigDecimal(goodsBean.getMaxNum());
        }
        if (TextUtils.isEmpty(goodsBean.getMinNum())) {
            goodsMinNum = new BigDecimal(0);
        } else {
            goodsMinNum = new BigDecimal(goodsBean.getMinNum());
        }

        if (goodsMaxNum.compareTo(BigDecimal.ZERO) == 0 &&
                goodsMinNum.compareTo(BigDecimal.ZERO) == 0){
            return;
        }

        if (goodsMaxNum.compareTo(BigDecimal.ZERO) == 1 ||
                goodsMinNum.compareTo(BigDecimal.ZERO) == 1) {
            boolean isExist = false;//购物车中是否已经存在这件商品
            for (int k = 0; k < listAddGoods.size(); k++) {

                if (goodsBean.getGgp_id().equals(listAddGoods.get(k).getGgp_id()) &&
                        goodsBean.getSaleType().equals(listAddGoods.get(k).getSaleType())) {//相同销售类型的商品已经在购物车中存在
                    isExist = true;

                    //商品在购物车中的数量
                    if (TextUtils.isEmpty(listAddGoods.get(k).getMaxNum())) {
                        cartMaxNum = new BigDecimal(0);
                    } else {
                        cartMaxNum = new BigDecimal(listAddGoods.get(k).getMaxNum());
                    }
                    if (TextUtils.isEmpty(listAddGoods.get(k).getMinNum())) {
                        cartMinNum = new BigDecimal(0);
                    } else {
                        cartMinNum = new BigDecimal(listAddGoods.get(k).getMinNum());
                    }
                    cartMaxNum = cartMaxNum.add(goodsMaxNum);
                    cartMinNum = cartMinNum.add(goodsMinNum);

                    //购物车中商品的大小单位价格
                    if (TextUtils.isEmpty(listAddGoods.get(k).getMaxPrice())) {
                        cartMaxPrice = new BigDecimal(BigInteger.ZERO);
                    } else {
                        cartMaxPrice = new BigDecimal(listAddGoods.get(k).getMaxPrice());
                    }
                    if (TextUtils.isEmpty(listAddGoods.get(k).getMinPrice())) {
                        cartMinPrice = new BigDecimal(BigInteger.ZERO);
                    } else {
                        cartMinPrice = new BigDecimal(listAddGoods.get(k).getMinPrice());
                    }

                    if ("1".equals(goodsBean.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                        sumPrice = cartMinPrice.multiply(cartMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    } else {
                        goodsSumPrice = cartMaxPrice.multiply(cartMaxNum).add(cartMinPrice.multiply(cartMinNum));
                        sumPrice = goodsSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    }
                    listAddGoods.get(k).setMaxNum(cartMaxNum.toString());//大单位数量
                    listAddGoods.get(k).setMinNum(cartMinNum.toString());//小单位数量
                    listAddGoods.get(k).setTotalPrice(sumPrice);//商品小计
                    break;
                }
            }

            if (!isExist) {//如果商品不存在
                if ("赠品".equals(goodsBean.getSaleTypeName())){
                    goodsBean.setGg_title(goodsBean.getGg_title() + "[" + goodsBean.getSaleTypeName() + "]");
                }else{
                    goodsBean.setGg_title(goodsBean.getGg_title());
                }
                goodsBean.setMaxNum(goodsMaxNum.toString());
                goodsBean.setMinNum(goodsMinNum.toString());
                listAddGoods.add(goodsBean);
            }
        }
        mTvCartNum.setText(listAddGoods.size() + "");
        calculateCartTotalPrice();
    }

    private void calculateCartGoodsPrice(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        BigDecimal bdMaxPrice = null;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice = null;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格
        BigDecimal discount;//销售类型折扣率

        if (!"".equals(adapterAddGoods.getItem(i).getSaleTypeDiscount()) && adapterAddGoods.getItem(i).getSaleTypeDiscount() != null) {
            discount = new BigDecimal(adapterAddGoods.getItem(i).getSaleTypeDiscount());//销售类型折扣
        }else{
            discount = BigDecimal.ONE;
        }

        if (TextUtils.isEmpty(adapterAddGoods.getItem(i).getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapterAddGoods.getItem(i).getMaxPrice());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapterAddGoods.getItem(i).getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapterAddGoods.getItem(i).getMinPrice());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        //计算折扣率
        bdMaxPrice = bdMaxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
        bdMinPrice = bdMinPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);

        String sumPrice;
        if ("1".equals(adapterAddGoods.getItem(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            adapterAddGoods.getItem(i).setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            adapterAddGoods.getItem(i).setTotalPrice(sumPrice);
        }
        tvSumPrice.setText(sumPrice);

        if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 &&
                bdMinNum.compareTo(BigDecimal.ZERO) == 0) {
            //            if (!mEtMaxNum.hasFocus() && !mEtMinNum.hasFocus()){
            listAddGoods.remove(i);
            adapterAddGoods.setData(listAddGoods);
            //            }
        }
        if (listAddGoods.size() == 0) {
            mRlCartListing.setVisibility(View.GONE);
            adapterAddGoods.notifyDataSetChanged();
        }
        calculateCartTotalPrice();
    }

    /**
     * 计算购物车总价格
     */
    private void calculateCartTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        if (listAddGoods.size() > 0) {
            for (GoodsBean bean : listAddGoods) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
            mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvTotalPrice.setText("0.00");
        }
        mTvCartNum.setText(listAddGoods.size() + "");

        isHasGoods();
    }

    //判断列表中商品是否已在购物车中存在
    private void isHasGoods(){
        if (listAddGoods != null && listAddGoods.size() > 0) {
            for (GoodsBean bean:adapterGoods.getList()){
                for (int k = 0; k < listAddGoods.size(); k++){
                    bean.setIsHasCart("N");
                    //如果购物车中已存在这件商品
                    if (bean.getGgp_goodsid().equals(listAddGoods.get(k).getGgp_goodsid())) {
                        bean.setIsHasCart("Y");
                        break;
                    }
                }
            }
            /*for (GoodsBean bean : listAddGoods) {
                for (int k = 0; k < adapterGoods.getList().size(); k++) {
                    //如果购物车中已存在这件商品
                    if (bean.getGgp_goodsid().equals(adapterGoods.getList().get(k).getGgp_goodsid())) {
                        adapterGoods.getList().get(k).setIsHasCart("Y");
                        break;
                    }
                }
            }*/
        } else {
            for (int k = 0; k < adapterGoods.getList().size(); k++) {
                adapterGoods.getList().get(k).setIsHasCart("N");
            }
        }
        adapterGoods.notifyDataSetChanged();
    }

    public void onEventMainThread(RolloutGoodsFinishEvent event) {
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
