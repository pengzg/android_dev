package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.DistributionActivity;
import com.xdjd.distribution.adapter.GoodsBuyListingAdapter;
import com.xdjd.distribution.adapter.GoodsListAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.SaleTypePopup;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

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
 *     time   : 2017/5/10
 *     desc   : 配送任务 -- 商品编辑界面
 *     version: 1.0
 * </pre>
 */

public class DistributionEditFragment extends BaseFragment implements GoodsListAdapter.GoodsListListener, ItemOnListener {

    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.lv_goods_buy_listing)
    ListView mLvGoodsBuyListing;
    @BindView(R.id.ll_horizontal)
    LinearLayout mLlHorizontal;
    @BindView(R.id.horizontal_scroll)
    HorizontalScrollView mHorizontalScroll;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.tv_sale_type_name)
    TextView mTvSaleTypeName;
    @BindView(R.id.tv_stock)
    TextView mTvStock;
    @BindView(R.id.unit_max_nameref)
    TextView mUnitMaxNameref;
    @BindView(R.id.unit_min_nameref)
    TextView mUnitMinNameref;
    @BindView(R.id.et_max_price)
    EditText mEtMaxPrice;
    @BindView(R.id.et_min_price)
    EditText mEtMinPrice;
    @BindView(R.id.et_sum_price)
    EditText mEtSumPrice;
    @BindView(R.id.et_max_num)
    EditText mEtMaxNum;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.tv_order_form)
    TextView mTvOrderForm;
    @BindView(R.id.tv_process_sheet_form)
    TextView mTvProcessSheetForm;
    @BindView(R.id.tv_exchange_form)
    TextView mTvExchangeForm;
    @BindView(R.id.tv_refund_form)
    TextView mTvRefundForm;
    @BindView(R.id.ll_max_num)
    LinearLayout mLlMaxNum;
    @BindView(R.id.rl_max_price)
    RelativeLayout mRlMaxPrice;
    @BindView(R.id.ll_min_num)
    LinearLayout mLlMinNum;
    @BindView(R.id.rl_min_price)
    RelativeLayout mRlMinPrice;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_complete)
    Button mBtnComplete;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.iv_max_minus)
    ImageView mIvMaxMinus;
    @BindView(R.id.ll_max_left)
    LinearLayout mLlMaxLeft;
    @BindView(R.id.iv_max_plus)
    ImageView mIvMaxPlus;
    @BindView(R.id.rl_max)
    RelativeLayout mRlMax;
    @BindView(R.id.iv_min_minus)
    ImageView mIvMinMinus;
    @BindView(R.id.ll_min_left)
    LinearLayout mLlMinLeft;
    @BindView(R.id.iv_min_plus)
    ImageView mIvMinPlus;
    @BindView(R.id.rl_min)
    RelativeLayout mRlMin;
    @BindView(R.id.et_min_num)
    EditText mEtMinNum;
    @BindView(R.id.tv_amount_desc)
    TextView mTvAmountDesc;

    private View view;

    private GoodsListAdapter adapterGoods;
    private GoodsBuyListingAdapter adapterBuyList;

    /**
     * 发货时间
     */
    public String deliveryTime;

    /**
     * 备注
     */
    public String note;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    public int indexOrder = 1;

    /**
     * 一二级分类集合
     */
    private List<GoodsCategoryBean> listCategory = new ArrayList<>();

    /**
     * 商品列表集合
     */
    private List<GoodsBean> listGoods = new ArrayList<>();

    /**
     * 选中商品的bean
     */
    private GoodsBean beanGoods;
    /**
     * 销售类型popup
     */
    private SaleTypePopup saleTypePopup;

    private List<SaleTypeBean> listSaleType;

    /**
     * 销售类型id
     */
    private String saleTypeId = "";
    /**
     * 销售类型名称
     */
    private String saleTypeName;

    /**
     * 订单选中的商品列表
     */
    public List<GoodsBean> listGoodsOrder;

    /**
     * 处理单选中的商品列表
     */
    public List<GoodsBean> listProcessOrder;

    /**
     * 换货单选中的商品列表
     */
    public List<GoodsBean> listExchangeOrder;

    /**
     * 退货单选中的商品列表
     */
    public List<GoodsBean> listRefundOrder;

    private DistributionActivity context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_goods_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData() {
        //        indexOrder = BaseConfig.OrderType1;

        //        listGoodsOrder = context.listGoodsOrder;
        //        listProcessOrder = context.listProcessOrder;
        //        listExchangeOrder = context.listExchangeOrder;
        //        listRefundOrder = context.listRefundOrder;

        //        selectTab();
        //
        //        clearGoodsEdit();
        //        mHorizontalScroll.scrollTo(0, 0);
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mLlHorizontal.getLayoutParams();
        layoutParams.width = width + (width / 3) * 2;
        mLlHorizontal.setLayoutParams(layoutParams);

        userBean = UserInfoUtils.getUser(getActivity());

        context = (DistributionActivity) getActivity();
        //        listGoodsOrder = context.listGoodsOrder;
        //        listProcessOrder = context.listProcessOrder;
        //        listExchangeOrder = context.listExchangeOrder;
        //        listRefundOrder = context.listRefundOrder;

        //        clientBean = UserInfoUtils.getClientInfo(getActivity());
        //
        //        adapterGoods = new GoodsListAdapter(this);
        //        mLvGoods.setAdapter(adapterGoods);
        //
        //        adapterBuyList = new GoodsBuyListingAdapter(this);
        //        mLvGoodsBuyListing.setAdapter(adapterBuyList);
        //
        //        indexOrder = BaseConfig.OrderType1;
        //        selectTab();
        //
        //        queryGsCategory();
        //        initSaleTypePopup(width);
        //        getSaleTypeList(false);
        //
        //        setTextWatcher(mEtMaxNum);
        //        setTextWatcher(mEtMinNum);
        //        setTextWatcher(mEtMaxPrice);
        //        setTextWatcher(mEtMinPrice);
        //        mEtSumPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        //            @Override
        //            public void onFocusChange(View view, boolean hasFocus) {
        //                if (hasFocus) {
        //                    Log.e("abc", "et1获取到焦点了。。。。。。");
        //                    mEtSumPrice.addTextChangedListener(totalPriceWatcher);
        //                } else {
        //                    Log.e("abc", "et1失去焦点了。。。。。。");
        //                    mEtSumPrice.removeTextChangedListener(totalPriceWatcher);
        //                }
        //            }
        //        });
    }


    private void setTextWatcher(final EditText et) {
        //是否有焦点
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("abc", "et1获取到焦点了。。。。。。");
                    et.addTextChangedListener(textWatcher);
                } else {
                    Log.e("abc", "et1失去焦点了。。。。。。");
                    et.removeTextChangedListener(textWatcher);
                }
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            calculatePrice();
        }
    };

    /**
     * 合计金额监听事件
     */
    private TextWatcher totalPriceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            priceMatrixing();
        }
    };

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form,
            R.id.tv_sale_type_name, R.id.iv_max_minus, R.id.iv_max_plus, R.id.iv_min_minus, R.id.iv_min_plus, R.id.btn_cancel, R.id.btn_complete, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            //1 普通 2 处理 3 退货 4 换货 5 还货
            case R.id.tv_order_form://订单
                if (!addGoods()) {
                    return;
                }
                indexOrder = 1;
                selectTab();
                break;
            case R.id.tv_process_sheet_form://处理单
                if (!addGoods()) {
                    return;
                }
                indexOrder = 2;
                selectTab();
                break;
            case R.id.tv_exchange_form://换货单
                if (!addGoods()) {
                    return;
                }
                indexOrder = 4;
                selectTab();
                break;
            case R.id.tv_refund_form://退货单
                if (!addGoods()) {
                    return;
                }
                indexOrder = 3;
                selectTab();
                break;
            case R.id.tv_sale_type_name://选择销售类型
                if (beanGoods != null) {
                    if (listSaleType != null && listSaleType.size() > 0) {
                        showPwSaleType();
                    } else {
                        getSaleTypeList(true);
                    }
                }
                break;
            case R.id.iv_max_minus://大单位减商品数量
                minusCalculation(mEtMaxNum);
                break;
            case R.id.iv_max_plus:
                plusCalculation(mEtMaxNum);
                break;
            case R.id.iv_min_minus://小单位减商品数量
                minusCalculation(mEtMinNum);
                break;
            case R.id.iv_min_plus:
                plusCalculation(mEtMinNum);
                break;
            case R.id.btn_cancel:
                clearGoodsEdit();
                beanGoods = null;
                break;
            case R.id.btn_complete:
                if (addGoods()) {
                    adapterBuyList.notifyDataSetChanged();
                }
                break;
            case R.id.tv_submit:
                context.toSubmitFragment();
                break;
        }
    }

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(EditText et) {
        if (beanGoods == null) {
            return;
        }
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        et.setText((bd.intValue() + 1) + "");
        if (et.hasFocus()) {//有焦点
            et.setSelection(String.valueOf((bd.intValue() + 1)).length());
        } else {
            //没有焦点
            calculatePrice();
        }
    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private void minusCalculation(EditText et) {
        if (beanGoods == null) {
            return;
        }
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            String numStr;
            if (num == 0) {
                numStr = "0";
            } else if (num - 1 == 0) {
                numStr = "0";
            } else {
                numStr = num - 1 + "";
            }
            et.setText(numStr);
            if (et.hasFocus()) {
                et.setSelection(numStr.length());
            } else {
                calculatePrice();
            }
        }
    }

    /**
     * 获取分类
     */
    private void queryGsCategory() {
        AsyncHttpUtil<GoodsCategoryBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCategoryBean.class, new IUpdateUI<GoodsCategoryBean>() {
            @Override
            public void updata(GoodsCategoryBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listCategory = jsonBean.getListData();
                    } else {
                        listCategory = new ArrayList<>();
                    }
                    GoodsCategoryBean cateTwo = new GoodsCategoryBean();
                    cateTwo.setGc_id("00");
                    cateTwo.setGc_name("查询结果");
                    List<GoodsCategoryBean> listCateTwo = new ArrayList<>();
                    listCateTwo.add(cateTwo);

                    GoodsCategoryBean cateOne = new GoodsCategoryBean();
                    cateOne.setGc_name("扫码/查询");
                    cateOne.setGc_id("00");
                    cateOne.setSecondCategoryList(listCateTwo);
                    listCategory.add(0, cateOne);

                    if (listCategory != null && listCategory.size() > 0) {
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
        httpUtil.post(M_Url.queryGsCategory, L_RequestParams.
                queryGsCategory(userBean.getUserId(), clientBean.getCc_id(), String.valueOf(indexOrder), "1", ""), true);
    }

    /**
     * 获取商品列表
     */
    private void getGoodsList(String gg_category_code_like, String searchKey) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listGoods = jsonBean.getListData();
                    adapterGoods.setData(listGoods);
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
        httpUtil.post(M_Url.getGoodsList, L_RequestParams.
                getGoodsList(userBean.getUserId(), clientBean.getCc_id(),
                        String.valueOf(indexOrder), searchKey, gg_category_code_like, "", "1", "1","",""), true);
    }

    public void addGoods(GoodsBean bean) {
        listGoods.clear();
        listGoods.add(bean);
        adapterGoods.setData(listGoods);
    }

    /**
     * 根据条件搜索
     */
    public void searchGoods(String searchKey) {
        getGoodsList("", searchKey);
    }

    @Override
    public void onItemGoods(int i) {
        adapterBuyList.setIndex(-1);
        if (addGoods()) {
            adapterGoods.setIndex(i);
        } else {
            return;
        }
        mHorizontalScroll.scrollTo(mHorizontalScroll.getWidth(), 0);

        beanGoods = (GoodsBean) listGoods.get(i).clone();

        if (listSaleType != null && listSaleType.size() > 0) {
            saleTypeName = listSaleType.get(0).getSp_name();
            saleTypeId = listSaleType.get(0).getSp_code();
            beanGoods.setSaleTypeName(saleTypeName);
            beanGoods.setSaleType(saleTypeId);
        }

        updateEditLayout();

        mTvSaleTypeName.setText(beanGoods.getSaleTypeName());

        mTvGoodsName.setText(beanGoods.getGg_title());
        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        mEtMaxPrice.setText(beanGoods.getMax_price());
        mEtMinPrice.setText(beanGoods.getMin_price());

        if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null) {
            mTvStock.setText("无库存");
        } else {
            BigDecimal bdStock = new BigDecimal(beanGoods.getGgs_stock());
            BigDecimal bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

            //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
            //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
            // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
            BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);

            String stockStr = results[0].toBigInteger() + beanGoods.getGg_unit_max_nameref() + results[1] + beanGoods.getGg_unit_min_nameref();
            mTvStock.setText("库存" + stockStr);
        }
    }

    @Override
    public void onItem(int position) {
        adapterGoods.setIndex(-1);

        LogUtils.e("tag", adapterBuyList.getCount() + "--");

        if (addGoods()) {
            adapterBuyList.setIndex(position);
        } else {
            return;
        }
        LogUtils.e("tag", position + "--");
        //已选中的商品
        beanGoods = adapterBuyList.getItem(position);
        beanGoods.setIndex(String.valueOf(position));

        updateEditLayout();

        mTvSaleTypeName.setText(beanGoods.getSaleTypeName());//销售类型

        mTvGoodsName.setText(beanGoods.getGg_title());

        mEtMaxNum.setText(beanGoods.getMaxNum());
        mEtMinNum.setText(beanGoods.getMinNum());

        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        mEtMaxPrice.setText(beanGoods.getMaxPrice());
        mEtMinPrice.setText(beanGoods.getMinPrice());

        mEtSumPrice.setText(beanGoods.getTotalPrice());
        mEtRemarks.setText(beanGoods.getRemarks());

        if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null) {
            mTvStock.setText("无库存");
        } else {
            BigDecimal bdStock = new BigDecimal(beanGoods.getGgs_stock());
            BigDecimal bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

            //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
            //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
            // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
            BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);

            String stockStr = results[0].toBigInteger() + beanGoods.getGg_unit_max_nameref() + results[1] + beanGoods.getGg_unit_min_nameref();
            mTvStock.setText("库存" + stockStr);
        }
    }

    /**
     * 刷新
     */
    private void updateEditLayout() {
        //1 都有 2 小单位 3 大单位
        if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
            mLlMaxNum.setVisibility(View.VISIBLE);
            mRlMaxPrice.setVisibility(View.VISIBLE);
            mLlMinNum.setVisibility(View.VISIBLE);
            mRlMinPrice.setVisibility(View.VISIBLE);
        } else if ("2".equals(beanGoods.getUnit_type())) {
            mLlMaxNum.setVisibility(View.GONE);
            mRlMaxPrice.setVisibility(View.GONE);
            mLlMinNum.setVisibility(View.VISIBLE);
            mRlMinPrice.setVisibility(View.VISIBLE);
        } else if ("3".equals(beanGoods.getUnit_type())) {
            mLlMaxNum.setVisibility(View.VISIBLE);
            mRlMaxPrice.setVisibility(View.VISIBLE);
            mLlMinNum.setVisibility(View.GONE);
            mRlMinPrice.setVisibility(View.GONE);
        }
    }

    /**
     * 点击其他的地方的时候判断商品输入信息是否符合添加条件
     *
     * @return false是商品填写的添加信息不通过
     */
    public boolean addGoods() {
        /*//1 普通 2 处理 3 退货 4 换货*/
        if (beanGoods != null) { //已有选中的商品时

            if ((TextUtils.isEmpty(mEtMaxNum.getText()) || "0".equals(mEtMaxNum.getText().toString()))
                    && (TextUtils.isEmpty(mEtMinNum.getText()) || "0".equals(mEtMinNum.getText().toString()))) {
                //最大和最小都未空时可以选择
                clearGoodsEdit();

                if (!"".equals(beanGoods.getIndex()) && beanGoods.getIndex() != null) {
                    showToast(beanGoods.getGg_title() + "删除成功");
                    adapterBuyList.removeItem(Integer.parseInt(beanGoods.getIndex()));
                    beanGoods = null;
                }
                return true;
            }

            if (TextUtils.isEmpty(mTvSaleTypeName.getText()) || "".equals(beanGoods.getSaleType())) {
                showToast("请选择销售类型");
                return false;
            }

            //1 都有 2 小单位 3 大单位
            if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {

                //商品单价不能为空
                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "0".equals(mEtMaxPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMaxPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }

                //商品单价不能为空
                if (TextUtils.isEmpty(mEtMinPrice.getText()) || "0".equals(mEtMinPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMinPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }

            } else if ("2".equals(beanGoods.getUnit_type())) {

                //商品单价不能为空
                if (TextUtils.isEmpty(mEtMinPrice.getText()) || "0".equals(mEtMinPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMinPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }

            } else if ("3".equals(beanGoods.getUnit_type())) {

                //商品单价不能为空
                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "0".equals(mEtMaxPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMaxPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }
            }
            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                beanGoods.setMaxNum("0");
            } else {
                String str = mEtMaxNum.getText().toString();
                String newStr = str.replaceFirst("^0*", "");

                beanGoods.setMaxNum(newStr);
            }

            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                beanGoods.setMinNum("0");
            } else {
                String str = mEtMinNum.getText().toString();
                String newStr = str.replaceFirst("^0*", "");

                beanGoods.setMinNum(newStr);
            }

            beanGoods.setMaxPrice(mEtMaxPrice.getText().toString());
            beanGoods.setMinPrice(mEtMinPrice.getText().toString());
            beanGoods.setTotalPrice(mEtSumPrice.getText().toString());
            beanGoods.setRemarks(mEtRemarks.getText().toString());
            beanGoods.setStock_nameref(mTvStock.getText().toString());
            beanGoods.setSaleTypeName(mTvSaleTypeName.getText().toString());

            if ("".equals(beanGoods.getIndex()) || beanGoods.getIndex() == null) {
                addBuyGoods(beanGoods);
                showToast(beanGoods.getGg_title() + "添加成功");
                LogUtils.e("tag", "add选中的商品信息:" + beanGoods.toString());
            } else {
                switch (indexOrder) {
                    case BaseConfig.OrderType1:
                        for (int i = 0; i < listGoodsOrder.size(); i++) {
                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
                                updateBuyGoods(beanGoods, i);
                                break;
                            }
                        }
                        break;
                    case BaseConfig.OrderType2:
                        for (int i = 0; i < listProcessOrder.size(); i++) {
                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
                                updateBuyGoods(beanGoods, i);
                                break;
                            }
                        }
                        break;
                    case BaseConfig.OrderType3:
                        for (int i = 0; i < listRefundOrder.size(); i++) {
                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
                                updateBuyGoods(beanGoods, i);
                                break;
                            }
                        }
                        break;
                    case BaseConfig.OrderType4:
                        for (int i = 0; i < listExchangeOrder.size(); i++) {
                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
                                updateBuyGoods(beanGoods, i);
                                break;
                            }
                        }
                        break;
                }
                showToast(beanGoods.getGg_title() + "修改成功");
                LogUtils.e("tag", "修改的商品信息:" + beanGoods.toString());
            }

            beanGoods = null;
            clearGoodsEdit();
        } else {
            clearGoodsEdit();
        }
        return true;
    }

    /**
     * 根据选择订单状态,添加商品
     */
    private void addBuyGoods(GoodsBean beanGoods) {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                listGoodsOrder.add(beanGoods);
                amountMoney(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                listProcessOrder.add(beanGoods);
                amountMoney(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                listRefundOrder.add(beanGoods);
                amountMoney(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                listExchangeOrder.add(beanGoods);
                amountMoney(listExchangeOrder);
                break;
        }
        LogUtils.e("tag", "选中的商品:" + listGoodsOrder.size());
    }

    /**
     * 根据选择订单状态,刷新添加商品信息
     */
    private void updateBuyGoods(GoodsBean beanGoods, int i) {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                listGoodsOrder.set(i, beanGoods);
                amountMoney(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                listProcessOrder.set(i, beanGoods);
                amountMoney(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                listRefundOrder.set(i, beanGoods);
                amountMoney(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                listExchangeOrder.set(i, beanGoods);
                amountMoney(listExchangeOrder);
                break;
        }

        LogUtils.e("tag", "选中的商品:" + listGoodsOrder.size());
    }

    /**
     * 根据订单状态刷新选中商品
     */
    private void updateBuyAdapter() {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                adapterBuyList.setData(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                adapterBuyList.setData(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                adapterBuyList.setData(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                adapterBuyList.setData(listExchangeOrder);
                break;
        }
    }

    /**
     * 清空商品编辑框
     */
    private void clearGoodsEdit() {
        mTvGoodsName.setText("");
        mTvSaleTypeName.setText("");

        mTvStock.setText("库存");

        mEtMaxNum.setText("");
        mEtMaxPrice.setText("");
        mEtMinNum.setText("");
        mEtMinPrice.setText("");
        mEtSumPrice.setText("");

        mEtRemarks.setText("");
    }

    /**
     * 根据商品获取商品销售类型
     */
    private void getSaleTypeList(final boolean isDialog) {
        AsyncHttpUtil<SaleTypeBean> httpUtil = new AsyncHttpUtil<>(getActivity(), SaleTypeBean.class, new IUpdateUI<SaleTypeBean>() {
            @Override
            public void updata(SaleTypeBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSaleType = jsonBean.getListData();
                    if (isDialog) {
                        showPwSaleType();
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
                getSaleTypeList(userBean.getUserId(),"", clientBean.getCc_id()), true);
    }

    /**
     * 初始化销售类型popup
     */
    private void initSaleTypePopup(int width) {
        saleTypePopup = new SaleTypePopup(getActivity(), width, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                saleTypeId = listSaleType.get(position).getSp_code();
                saleTypeName = listSaleType.get(position).getSp_name();
                mTvSaleTypeName.setText(listSaleType.get(position).getSp_name());
                beanGoods.setSaleType(saleTypeId);
                beanGoods.setSaleTypeName(saleTypeName);
                saleTypePopup.dismiss();
            }
        });
    }

    /**
     * 显示销售类型popup
     */
    private void showPwSaleType() {
        saleTypePopup.setData(listSaleType);
        saleTypePopup.setId(saleTypeId);
        // 显示窗口
        saleTypePopup.showAsDropDown(mTvSaleTypeName, -UIUtils.dp2px(8), 0);
    }

    /**
     * 根据购买数量计算商品价格
     */
    private void calculatePrice() {
        if (beanGoods != null) {
            BigDecimal bdMaxPrice;
            BigDecimal bdMaxNum;

            BigDecimal bdMinPrice;
            BigDecimal bdMinNum;

            BigDecimal bdSumPrice = null;

            //1 都有 2 小单位 3 大单位
            if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
                if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
                }

                if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                    bdMaxNum = new BigDecimal(0);
                } else {
                    bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
                }

                if (TextUtils.isEmpty(mEtMinPrice.getText())) {
                    bdMinPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
                }
                if (TextUtils.isEmpty(mEtMinNum.getText())) {
                    bdMinNum = new BigDecimal(0);
                } else {
                    bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
                }

                bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            } else if ("2".equals(beanGoods.getUnit_type())) {
                if (TextUtils.isEmpty(mEtMinPrice.getText())) {
                    bdMinPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
                }

                if (TextUtils.isEmpty(mEtMinNum.getText())) {
                    bdMinNum = new BigDecimal(0);
                } else {
                    bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
                }

                bdSumPrice = bdMinPrice.multiply(bdMinNum);
            } else if ("3".equals(beanGoods.getUnit_type())) {
                if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
                } else {
                    bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
                }
                if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                    bdMaxNum = new BigDecimal(0);
                } else {
                    bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
                }

                bdSumPrice = bdMaxPrice.multiply(bdMaxNum).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (bdSumPrice != null && bdSumPrice.doubleValue() > 0) {
                mEtSumPrice.setText(bdSumPrice.toString());
            } else {
                mEtSumPrice.setText("");
            }
        }
    }

    /**
     * 价格换算
     */
    private void priceMatrixing() {
        if (beanGoods != null) {
            BigDecimal bdMaxPrice;
            BigDecimal bdMaxNum;

            BigDecimal bdMinPrice;
            BigDecimal bdMinNum;

            BigDecimal bdSumPrice = null;
            BigDecimal bdUnitNum = null;//换算单位

            BigDecimal bdSumNum = null;

            //1 都有 2 小单位 3 大单位
            if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
                if (TextUtils.isEmpty(mEtSumPrice.getText()) ||
                        !Validation.isNumericAndExceedZero(mEtSumPrice.getText().toString())) {
                    mEtMaxPrice.setText("0");
                    mEtMinPrice.setText("0");
                } else {
                    bdSumPrice = new BigDecimal(mEtSumPrice.getText().toString());//总价格
                    //根据总价格计算大单位和小单的单价
                    bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

                    if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                        bdMaxNum = new BigDecimal(0);
                    } else {
                        bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
                    }

                    if (TextUtils.isEmpty(mEtMinNum.getText())) {
                        bdMinNum = new BigDecimal(0);
                    } else {
                        bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
                    }
                    //通过大单位与小单的换算参数获取总的小单位数量
                    bdSumNum = bdUnitNum.multiply(bdMaxNum).add(bdMinNum);
                    LogUtils.e("tag", "bdSumNum:" + bdSumNum.toString());
                    if (bdSumNum.intValue() == 0) {
                        return;
                    } else {
                        LogUtils.e("tag", "bdSumPrice:" + bdSumPrice.toString() + "--bdSumNum:" + bdSumNum.toString());

                        bdMinPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP);
                        LogUtils.e("tag", "bdMinPrice:" + bdMinPrice.toString());
                        mEtMinPrice.setText(bdMinPrice.toString());

                        bdMaxPrice = bdMinPrice.multiply(bdUnitNum);
                        LogUtils.e("tag", "bdMaxPrice:" + bdMaxPrice.toString());
                        mEtMaxPrice.setText(bdMaxPrice.toString());
                    }
                }
            } else if ("2".equals(beanGoods.getUnit_type())) {

                if (TextUtils.isEmpty(mEtSumPrice.getText()) ||
                        !Validation.isNumericAndExceedZero(mEtSumPrice.getText().toString())) {
                    mEtMinPrice.setText("0");
                } else {
                    bdSumPrice = new BigDecimal(mEtSumPrice.getText().toString());//总价格

                    if (TextUtils.isEmpty(mEtMinNum.getText())) {
                        bdMinNum = new BigDecimal(0);
                    } else {
                        bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
                    }

                    bdSumNum = bdMinNum;
                    if (bdSumNum.intValue() == 0) {
                        return;
                    } else {
                        bdMinPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP);
                        mEtMinPrice.setText(bdMinPrice.toString());
                    }
                }
            } else if ("3".equals(beanGoods.getUnit_type())) {
                if (TextUtils.isEmpty(mEtSumPrice.getText()) ||
                        !Validation.isNumericAndExceedZero(mEtSumPrice.getText().toString())) {
                    mEtMaxPrice.setText("0");
                } else {
                    bdSumPrice = new BigDecimal(mEtSumPrice.getText().toString());//总价格
                    //根据总价格计算大单位和小单的单价
                    bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

                    if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                        bdMaxNum = new BigDecimal(0);
                    } else {
                        bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
                    }

                    bdSumNum = bdMaxNum;
                    if (bdSumNum.intValue() == 0) {
                        return;
                    } else {
                        bdMaxPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP);
                        mEtMaxPrice.setText(bdMaxPrice.toString());
                    }
                }
            }
        }
    }

    /**
     * 计算选中商品列表的价格
     */
    private void amountMoney(List<GoodsBean> list) {
        if (list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (GoodsBean bean : list) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
            mTvTotalPrice.setText("¥:" + sum.setScale(2).doubleValue() + "元");
        } else {
            mTvTotalPrice.setText("¥:0.00元");
        }

        mTvSelectedNum.setText("已选(" + list.size() + "件)");
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvSelectedNum.setText("已选(" + context.listGoodsOrder.size() + "件)");
                amountMoney(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvSelectedNum.setText("已选(" + context.listProcessOrder.size() + "件)");
                amountMoney(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvSelectedNum.setText("已选(" + context.listRefundOrder.size() + "件)");
                amountMoney(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                amountMoney(listExchangeOrder);
                break;
        }
        updateBuyAdapter();
        listGoods.clear();
        adapterGoods.notifyDataSetChanged();
        queryGsCategory();
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    }

}
