package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.SalesOutboundActivity;
import com.xdjd.distribution.adapter.GoodsBuyListingAdapter;
import com.xdjd.distribution.adapter.GoodsCategotyAdapter;
import com.xdjd.distribution.adapter.GoodsListAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.GoodsStatusBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.GoodsStatusPopup;
import com.xdjd.distribution.popup.SaleTypePopup;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.EditTextUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;

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
 *     time   : 2017/5/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SalesOutboundEditFragment extends BaseFragment implements GoodsListAdapter.GoodsListListener, ItemOnListener,
        ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener {

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
    @BindView(R.id.et_min_num)
    EditText mEtMinNum;
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
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.elv_goods_category)
    AnimatedExpandableListView mElvGoodsCategory;
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
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_give_back_form)
    TextView mTvGiveBackForm;
    @BindView(R.id.iv_goods_status)
    ImageView mIvGoodsStatus;
    @BindView(R.id.ll_goods_status)
    RelativeLayout mLlGoodsStatus;

    private View view;

    /**
     * 商品类别adapter
     */
    private GoodsCategotyAdapter adapterGoodsCategory;
    private GoodsListAdapter adapterGoods;
    private GoodsBuyListingAdapter adapterBuyList;

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
     * 销售类型bean
     */
    private SaleTypeBean saleTypeBean = null;

    /**
     * 销售类型popup
     */
    private SaleTypePopup saleTypePopup;
    private List<SaleTypeBean> listSaleType;

    /**
     * 商品类型-- 1 正 2 临 3残 4过
     */
    private GoodsStatusPopup goodsStatusPopup;
    private List<GoodsStatusBean> listGoodsStatus;
    //商品类型bean
    private GoodsStatusBean goodsStatusBean;

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

    /**
     * 还货单
     */
    public List<GoodsBean> listGiveBack;

    private SalesOutboundActivity context;

    /**
     * 选中分类的code
     */
    private String categoryCode;
    /**
     * 选中分类的日期
     */
    private String orderDate;
    /**
     * 商品类别type--4重点  5推荐 6我常买 7促销
     */
    private String categoryType = "";

    /**
     * 商品页数
     */
    private int page = 1;
    private int mFlag = 0;

    private EditText editText;

    /**
     * 订单复制参数
     */
    private GoodsBean beanCopy;

    private String orderGoodsId;//订货订单id
    private String orderGoodsGoodsId;//订货订单商品id

    /**
     * 还货订单code
     */
    private String applyOrderCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_sales_outbound, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData(int index) {
        indexOrder = index;

        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;
        listGiveBack = context.listGiveBack;

        selectTab();

        clearGoodsEdit();
        mHorizontalScroll.scrollTo(0, 0);
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 4;

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mLlHorizontal.getLayoutParams();
        layoutParams.width = width + (width / 3) * 1;
        mLlHorizontal.setLayoutParams(layoutParams);

        context = (SalesOutboundActivity) getActivity();
        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;
        listGiveBack = context.listGiveBack;

        indexOrder = BaseConfig.OrderType1;

        storehouseId = getActivity().getIntent().getStringExtra("storehouseId");
        deliveryTime = getActivity().getIntent().getStringExtra("deliveryTime");
        note = getActivity().getIntent().getStringExtra("note");

        orderGoodsId = getActivity().getIntent().getStringExtra("orderGoodsId");
        orderGoodsGoodsId = getActivity().getIntent().getStringExtra("orderGoodsGoodsId");

        clientBean = UserInfoUtils.getClientInfo(getActivity());

        adapterGoodsCategory = new GoodsCategotyAdapter();
        mElvGoodsCategory.setAdapter(adapterGoodsCategory);
        mElvGoodsCategory.setOnGroupExpandListener(this);
        mElvGoodsCategory.setOnChildClickListener(this);
        mElvGoodsCategory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //设置扩展动画
                if (mElvGoodsCategory.isGroupExpanded(groupPosition)) {
                    mElvGoodsCategory.collapseGroupWithAnimation(groupPosition);
                } else {
                    mElvGoodsCategory.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        adapterGoods = new GoodsListAdapter(this);
        mLvGoods.setAdapter(adapterGoods);

        adapterBuyList = new GoodsBuyListingAdapter(this);
        mLvGoodsBuyListing.setAdapter(adapterBuyList);

        beanCopy = (GoodsBean) getActivity().getIntent().getSerializableExtra("beanCopy");
        if (beanCopy != null) {
            indexOrder = beanCopy.getOm_ordertype();
        }

        if (orderGoodsId!=null && orderGoodsId.length()>0){//从客户信息卡跳转过来的还货
            indexOrder = BaseConfig.OrderType5;
        }

        selectTab();

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setRightLayout(false);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                listGoods.clear();
                adapterGoods.notifyDataSetInvalidated();
                if (indexOrder == BaseConfig.OrderType5) {
                    getGoodsGivebackList("");
                } else {
                    getGoodsList("");
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                if (indexOrder == BaseConfig.OrderType5) {
                    getGoodsGivebackList("");
                } else {
                    getGoodsList("");
                }
            }
        });

        /*if (indexOrder ==   BaseConfig.OrderType5){//客户信息卡过来的还货操作
            queryGivebackCategory();
        }else{
            queryGsCategory();
        }*/

        initSaleTypePopup(width);
        getSaleTypeList(false);

        initGoodsStatusPopup(width);
        getGoodsStatusList(false);

        setTextWatcher(mEtMaxNum);
        setTextWatcher(mEtMinNum);
        setTextWatcher(mEtMaxPrice);
        setTextWatcher(mEtMinPrice);
        mEtSumPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEtSumPrice.setSelectAllOnFocus(true);
                    mEtSumPrice.addTextChangedListener(totalPriceWatcher);
                } else {
                    mEtSumPrice.removeTextChangedListener(totalPriceWatcher);
                }
            }
        });
    }


    private void setTextWatcher(final EditText et) {
        //是否有焦点
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et.setSelectAllOnFocus(true);
                    editText = et;
                    et.addTextChangedListener(textWatcher);
                } else {
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
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }

            /*if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }*/
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (beanGoods == null)
                return;

            BigDecimal bdMaxPrice;
            BigDecimal bdMaxNum;

            BigDecimal bdMinPrice;
            BigDecimal bdMinNum;

            BigDecimal bdUnitNum = null;

            if (TextUtils.isEmpty(beanGoods.getGgp_unit_num())) {
                bdUnitNum = new BigDecimal("1");
            } else {
                bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());
            }

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

            if (editText == mEtMaxPrice) {
                String minPrice = bdMaxPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtMinPrice.setText(minPrice);
            } else if (editText == mEtMinPrice) {
                if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                    mEtMaxPrice.setText(editable.toString());
                } else {
                    String maxPrice = bdUnitNum.multiply(bdMinPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                }
            }

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
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtSumPrice.setText(s);
                    mEtSumPrice.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtSumPrice.setText(s);
                mEtSumPrice.setSelection(2);
            }

            /*if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEtSumPrice.setText(s.subSequence(0, 1));
                    mEtSumPrice.setSelection(1);
                    return;
                }
            }*/
        }

        @Override
        public void afterTextChanged(Editable editable) {
            priceMatrixing();
        }
    };

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form,
            R.id.tv_sale_type_name, R.id.iv_max_minus, R.id.iv_max_plus, R.id.iv_min_minus, R.id.iv_min_plus, R.id.btn_cancel,
            R.id.btn_complete, R.id.tv_submit, R.id.tv_give_back_form, R.id.ll_goods_status})
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
                break;
            case R.id.tv_give_back_form://还货单
                if (!addGoods()) {
                    return;
                }
                indexOrder = 5;
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
            case R.id.ll_goods_status://选择商品类型
                if (beanGoods != null) {
                    if (listGoodsStatus != null && listGoodsStatus.size() > 0) {
                        showPwGoodsStatus();
                    } else {
                        getGoodsStatusList(true);
                    }
                }
                break;
            case R.id.iv_max_minus://大单位减商品数量
                notSelectGoodsHint();
                minusCalculation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
                break;
            case R.id.iv_max_plus:
                notSelectGoodsHint();
                plusCalculation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
                break;
            case R.id.iv_min_minus://小单位减商品数量
                notSelectGoodsHint();
                minusCalculation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
                break;
            case R.id.iv_min_plus:
                notSelectGoodsHint();
                plusCalculation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
                break;
            case R.id.btn_cancel:
                if (beanGoods != null) { //已有选中的商品时
                    if (!"".equals(beanGoods.getIndex()) && beanGoods.getIndex() != null) {
                        showToast(beanGoods.getGg_title() + "删除成功");
                        adapterBuyList.list.remove(Integer.parseInt(beanGoods.getIndex()));
                        adapterBuyList.setIndex(-1);
                        adapterBuyList.notifyDataSetChanged();
                    }
                    beanGoods = null;
                    addBuyGoods(beanGoods);
                }

                clearGoodsEdit();
                beanGoods = null;
                break;
            case R.id.btn_complete:
                notSelectGoodsHint();
                if (beanGoods != null && (TextUtils.isEmpty(mEtMaxNum.getText()) || "0".equals(mEtMaxNum.getText().toString()))
                        && (TextUtils.isEmpty(mEtMinNum.getText()) || "0".equals(mEtMinNum.getText().toString()))) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请输入产品数量!", "确定", null, null);
                    return;
                }
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
     * 没有选中商品操作提示
     */
    private void notSelectGoodsHint() {
        if (beanGoods == null)
            DialogUtil.showCustomDialog(getActivity(), "提示", "您还没有选择商品,请选择商品后再进行操作!", "确定", null, null);
    }

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(EditText et, LinearLayout llLeft, RelativeLayout rlMain, ImageView ivPlus) {
        if (beanGoods == null) {
            return;
        }
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        final String numStr = (bd.intValue() + 1) + "";
        et.setText(numStr);
        AnimUtils.setTranslateAnimation(et, llLeft, rlMain, ivPlus);
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
    private void minusCalculation(final EditText et, LinearLayout llLeft, RelativeLayout rlMain, ImageView ivPlus) {
        if (beanGoods == null) {
            return;
        }
        BigDecimal bd;
        final String numStr;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            if (num == 0) {
                numStr = "0";
            } else if (num - 1 == 0) {
                numStr = "0";
            } else {
                numStr = num - 1 + "";
            }

            et.setText(numStr);
            AnimUtils.setTranslateAnimation(et, llLeft, rlMain, ivPlus);
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

                        adapterGoodsCategory.setData(listCategory);
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
                        onGroupExpand(0);
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
        String from;
        if (indexOrder == BaseConfig.OrderType3) {
            from = "1";
        } else {
            from = "2";
        }
        httpUtil.post(M_Url.queryGsCategory, L_RequestParams.
                queryGsCategory(userBean.getUserId(), clientBean.getCc_id(), String.valueOf(indexOrder), from, ""), true);
    }

    /**
     * 获取商品列表
     */
    private void getGoodsList(String searchKey) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listGoods.addAll(jsonBean.getListData());
                        adapterGoods.setData(listGoods);
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getGoodsList, L_RequestParams.
                getGoodsList(userBean.getUserId(), clientBean.getCc_id(),
                        String.valueOf(indexOrder), searchKey, categoryCode, storehouseId, "2", String.valueOf(page), orderDate, categoryType), true);
    }


    /**
     * 获取还货分类
     */
    private void queryGivebackCategory() {
        AsyncHttpUtil<GoodsCategoryBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCategoryBean.class, new IUpdateUI<GoodsCategoryBean>() {
            @Override
            public void updata(GoodsCategoryBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listCategory = jsonBean.getListData();

                        adapterGoodsCategory.setData(listCategory);
                    } else {
                        listCategory = new ArrayList<>();
                    }

                    if (listCategory != null && listCategory.size() > 0) {

                        if (orderGoodsId!=null && orderGoodsId.length()>0){//客户信息卡过来的还货操作

                            for (int i=0;i<listCategory.size();i++){
                                for (int k=0;k<listCategory.get(i).getSecondCategoryList().size();k++){
                                    if (orderGoodsId.equals(listCategory.get(i).getSecondCategoryList().get(k).getOa_id())){
                                        mElvGoodsCategory.expandGroupWithAnimation(i);
                                        orderGoodsId = null;
                                        break;
                                    }
                                }
                            }

                        }else{
                            onGroupExpand(0);
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

            }
        });
        httpUtil.post(M_Url.queryApplyOrderGoods, L_RequestParams.
                queryApplyOrderGoods(getActivity(), clientBean.getCc_id(), "2"), true);
    }

    /**
     * 获取还货商品列表
     */
    private void getGoodsGivebackList(String searchKey) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listGoods.addAll(jsonBean.getListData());
                        adapterGoods.setData(listGoods, true,applyOrderCode);
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryApplyOrderGoodsDetail, L_RequestParams.
                queryApplyOrderGoodsDetail(userBean.getUserId(), clientBean.getCc_id(),
                        String.valueOf(indexOrder), searchKey, categoryCode), true);
    }

    public void addGoods(GoodsBean bean) {
        onGroupExpand(0);
        listGoods.clear();
        listGoods.add(bean);
        adapterGoods.setData(listGoods);
    }

    /**
     * 根据条件搜索
     */
    public void searchGoods(String searchKey) {
        onGroupExpand(0);
        categoryCode = "";
        orderDate = "";
        mFlag = 1;
        page = 1;
        if (indexOrder == BaseConfig.OrderType5) {
            getGoodsGivebackList(searchKey);
        } else {
            getGoodsList(searchKey);
        }
    }

    /**
     * 保证listview只展开一项
     *
     * @param groupPosition
     */
    @Override
    public void onGroupExpand(int groupPosition) {
        Log.e("xxx", "onGroupExpand>>" + groupPosition);

        if (addGoods()) {
            adapterGoodsCategory.index = groupPosition;
            adapterGoodsCategory.index2 = 0;
        } else {
            return;
        }

        for (int i = 0; i < listCategory.size(); i++) {
            if (i != groupPosition) {
                mElvGoodsCategory.collapseGroup(i);
            }
        }

        categoryCode = "";

        listGoods.clear();
        adapterGoods.notifyDataSetChanged();
        if (listCategory.get(groupPosition).getSecondCategoryList() != null &&
                listCategory.get(groupPosition).getSecondCategoryList().size() > 0) {

            if (orderGoodsId!=null && orderGoodsId.length()>0){//客户信息卡过来的还货操作
                for (int i=0;i<listCategory.size();i++){
                    for (int k=0;k<listCategory.get(i).getSecondCategoryList().size();k++){
                        if (orderGoodsId.equals(listCategory.get(i).getSecondCategoryList().get(k).getOa_id())){

                            adapterGoodsCategory.index2 = k;

                            onChildClick(mElvGoodsCategory, mElvGoodsCategory.getChildAt(groupPosition),
                                    i, k, adapterGoodsCategory.getChildId(groupPosition, k));
                            orderGoodsId = null;
                            break;
                        }
                    }
                }
            }else{
                onChildClick(mElvGoodsCategory, mElvGoodsCategory.getChildAt(groupPosition),
                        groupPosition, 0, adapterGoodsCategory.getChildId(groupPosition, 0));
            }
        } else {
            categoryType = listCategory.get(groupPosition).getType();
            if (categoryType == null || "".equals(categoryType)) {
                return;
            }
            //4重点  5推荐 6我常买 7促销
            if ("4".equals(categoryType) || "5".equals(categoryType) || "6".equals(categoryType)
                    || "7".equals(categoryType)) {
                orderDate = "";
                categoryCode = listCategory.get(groupPosition).getGc_code();
                getGoodsList("");
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

        if (addGoods()) {
            adapterGoodsCategory.index2 = i1;
            adapterGoodsCategory.notifyDataSetChanged();
        } else {
            return false;
        }

        mFlag = 1;
        page = 1;
        listGoods.clear();
        adapterGoods.notifyDataSetChanged();

        List<GoodsCategoryBean> secondCategoryList =
                listCategory.get(i).getSecondCategoryList();
        if (!"00".equals(secondCategoryList.get(i1).getGc_id())) {

            if (indexOrder == BaseConfig.OrderType5) {//还货商品列表
                orderDate = "";
                categoryCode = secondCategoryList.get(i1).getOa_id();
                categoryType = "";
                applyOrderCode = secondCategoryList.get(i1).getOa_applycode();

                getGoodsGivebackList("");
            } else {
                if ("3".equals(secondCategoryList.get(i1).getType())) {
                    orderDate = secondCategoryList.get(i1).getGc_name();
                    categoryCode = "";
                } else {
                    orderDate = "";
                    categoryCode = secondCategoryList.get(i1).getGc_code();
                }
                categoryType = secondCategoryList.get(i1).getType();
                //00代表扫码/查询
                getGoodsList("");
            }
        } else {
            categoryCode = "";
            orderDate = "";
        }

        return false;
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
            saleTypeBean = listSaleType.get(0);

            beanGoods.setSaleTypeName(saleTypeBean.getSp_name());
            beanGoods.setSaleType(saleTypeBean.getSp_code());
            beanGoods.setSaleTypeDiscount(saleTypeBean.getSp_discount());
        }

        if (listGoodsStatus != null && listGoodsStatus.size() > 0) {//设置商品类型
            goodsStatusBean = listGoodsStatus.get(0);

            beanGoods.setGoodsStatus(goodsStatusBean.getCode());
            goodsStatus(goodsStatusBean.getCode());
        }

        if (!"".equals(beanGoods.getSaleTypeDiscount()) && beanGoods.getSaleTypeDiscount() != null) {
            BigDecimal maxPrice;
            BigDecimal minPrice;
            BigDecimal discount = new BigDecimal(beanGoods.getSaleTypeDiscount());//销售类型折扣
            //根据销售类型计算价格
            if (beanGoods.getMax_price() == null || "".equals(beanGoods.getMax_price())) {
                //如果是0,就不需要计算了
                mEtMaxPrice.setText(beanGoods.getMax_price());
            } else {
                maxPrice = new BigDecimal(beanGoods.getMax_price());
                BigDecimal price = maxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                mEtMaxPrice.setText(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

            if (beanGoods.getMin_price() == null || "".equals(beanGoods.getMin_price())) {
                //如果是0,就不需要计算了
                mEtMinPrice.setText(beanGoods.getMin_price());
            } else {
                minPrice = new BigDecimal(beanGoods.getMin_price());
                BigDecimal price = minPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                mEtMinPrice.setText(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

        } else {
            mEtMaxPrice.setText(beanGoods.getMax_price());
            mEtMinPrice.setText(beanGoods.getMin_price());
        }

        EditTextUtil.setEditSelection(mEtMaxPrice);
        EditTextUtil.setEditSelection(mEtMinPrice);

        updateEditLayout();

        //设置商品单位基准值,单位基准：1 小单位，2 大单位
        if (beanGoods.getGgp_unit_num().equals("1")) {
            mEtMinNum.setText("1");
            mEtMinPrice.setText(beanGoods.getMin_price());
            mEtSumPrice.setText(beanGoods.getMin_price());
        } else {//都显示
            if (beanGoods.getGgp_unit_type().equals("1")) {
                mEtMinNum.setText("1");
                mEtMinPrice.setText(beanGoods.getMin_price());
                mEtSumPrice.setText(beanGoods.getMin_price());
            } else {
                mEtMaxNum.setText("1");
                mEtMaxPrice.setText(beanGoods.getMax_price());
                mEtSumPrice.setText(beanGoods.getMax_price());
            }
        }
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);

        mTvSaleTypeName.setText(beanGoods.getSaleTypeName());

        if (indexOrder == BaseConfig.OrderType5){//如果是还货商品
            if (applyOrderCode==null){
                mTvGoodsName.setText(beanGoods.getGg_title());
            }else{
                mTvGoodsName.setText(beanGoods.getGg_title() + "[" +applyOrderCode + "]");
            }
            beanGoods.setOa_id(categoryCode);
            beanGoods.setOa_applycode(applyOrderCode);
        }else{
            mTvGoodsName.setText(beanGoods.getGg_title());
        }

        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null) {
            mTvStock.setText("无库存");
        } else {
            BigDecimal bdStock = new BigDecimal(beanGoods.getGgs_stock());
            BigDecimal bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

            if ("1".equals(beanGoods.getGgp_unit_num())) {
                String stockStr = beanGoods.getGgs_stock() + beanGoods.getGg_unit_min_nameref();
                mTvStock.setText("库存" + stockStr);
            } else {
                //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
                //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
                // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
                BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);

                String stockStr = results[0].toBigInteger() + beanGoods.getGg_unit_max_nameref() + results[1] + beanGoods.getGg_unit_min_nameref();
                mTvStock.setText("库存" + stockStr);
            }
        }
    }

    @Override
    public void onItem(int position) {
        adapterGoods.setIndex(-1);

        GoodsBean itemBean = adapterBuyList.getItem(position);
        if (beanGoods != null) {
            if (addGoods()) {
                adapterBuyList.setIndex(position);
            } else {
                return;
            }
        } else {
            adapterBuyList.setIndex(position);
        }
        if (adapterBuyList.getCount() == 0) {
            return;
        }

        //已选中的商品
        beanGoods = itemBean;
        beanGoods.setIndex(String.valueOf(position));

        updateEditLayout();

        mTvSaleTypeName.setText(beanGoods.getSaleTypeName());//销售类型
        goodsStatus(beanGoods.getGoodsStatus());

        if (indexOrder == BaseConfig.OrderType5) {//如果是还货商品
            mTvGoodsName.setText(beanGoods.getGg_title() + "[" + applyOrderCode + "]");
        }else{
            mTvGoodsName.setText(beanGoods.getGg_title());
        }

        mEtMaxNum.setText(beanGoods.getMaxNum());
        mEtMinNum.setText(beanGoods.getMinNum());

        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        mEtMaxPrice.setText(beanGoods.getMaxPrice());
        mEtMinPrice.setText(beanGoods.getMinPrice());

        mEtSumPrice.setText(beanGoods.getTotalPrice());
        mEtRemarks.setText(beanGoods.getRemarks());

        EditTextUtil.setEditSelection(mEtMaxNum);
        EditTextUtil.setEditSelection(mEtMinNum);
        EditTextUtil.setEditSelection(mEtMaxPrice);
        EditTextUtil.setEditSelection(mEtMinPrice);
        EditTextUtil.setEditSelection(mEtSumPrice);
        EditTextUtil.setEditSelection(mEtRemarks);

        if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null) {
            mTvStock.setText("无库存");
        } else {
            BigDecimal bdStock = new BigDecimal(beanGoods.getGgs_stock());
            BigDecimal bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

            if ("1".equals(beanGoods.getGgp_unit_num())) {
                String stockStr = beanGoods.getGgs_stock() + beanGoods.getGg_unit_min_nameref();
                mTvStock.setText("库存" + stockStr);
            } else {
                //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
                //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
                // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
                BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);

                String stockStr = results[0].toBigInteger() + beanGoods.getGg_unit_max_nameref() + results[1] + beanGoods.getGg_unit_min_nameref();
                mTvStock.setText("库存" + stockStr);
            }
        }

        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
    }

    /**
     * 刷新
     */
    private void updateEditLayout() {
        //如果大小单位换算比==1,隐藏大单位
        if (beanGoods.getGgp_unit_num().equals("1")) {
            mLlMaxNum.setVisibility(View.GONE);
            mRlMaxPrice.setVisibility(View.GONE);
        } else {//都显示
            mLlMaxNum.setVisibility(View.VISIBLE);
            mRlMaxPrice.setVisibility(View.VISIBLE);
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
                    addBuyGoods(beanGoods);
                }
                return true;
            }

            if (TextUtils.isEmpty(mTvSaleTypeName.getText()) || "".equals(beanGoods.getSaleType())) {
                showToast("请选择销售类型");
                return false;
            }

            if (indexOrder == BaseConfig.OrderType4 || indexOrder == BaseConfig.OrderType3) {//退货或换货时必须选择商品类型
                if (goodsStatusBean == null) {
                    showToast("请选择商品类型");
                    return false;
                }
            }

            /*if ("1".equals(beanGoods.getGgp_unit_num())) {//小单位
                //商品单价不能为空
                if (TextUtils.isEmpty(mEtMinPrice.getText()) || "0".equals(mEtMinPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMinPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }
            } else {
                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "0".equals(mEtMaxPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMaxPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }

                if (TextUtils.isEmpty(mEtMinPrice.getText()) || "0".equals(mEtMinPrice.getText().toString()) ||
                        !Validation.isNumericAndExceedZero(mEtMinPrice.getText().toString())) {
                    showToast("商品单价必须大于0");
                    return false;
                }
            }*/

            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                beanGoods.setMaxNum("0");
            } else {
                String str = mEtMaxNum.getText().toString();
                String newStr = str.replaceFirst("^0*", "");

                beanGoods.setMaxNum(newStr.equals("") ? "0" : newStr);
            }

            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                beanGoods.setMinNum("0");
            } else {
                String str = mEtMinNum.getText().toString();
                String newStr = str.replaceFirst("^0*", "");
                newStr = newStr.equals("") ? "0" : newStr;

                beanGoods.setMinNum(newStr);
            }

            BigDecimal bgMaxNum;//大单位数量
            BigDecimal bgMinNum;//小单位数量
            if ("".equals(beanGoods.getMaxNum()) || beanGoods.getMaxNum() == null) {
                bgMaxNum = new BigDecimal(BigInteger.ZERO);
            } else {
                bgMaxNum = new BigDecimal(beanGoods.getMaxNum());
            }

            if ("".equals(beanGoods.getMinNum()) || beanGoods.getMinNum() == null) {
                bgMinNum = new BigDecimal(BigInteger.ZERO);
            } else {
                bgMinNum = new BigDecimal(beanGoods.getMinNum());
            }
            //换算单位数量
            BigDecimal bgUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

            if (beanGoods.getGgs_stock() == null || beanGoods.getGgs_stock() == "") {
                beanGoods.setGgs_stock("0");
            }

            BigDecimal bgStock = new BigDecimal(beanGoods.getGgs_stock());

            BigDecimal bgNum = bgMaxNum.multiply(bgUnitNum);//大单位换算成小单位总数量
            BigDecimal bgSumNum = bgNum.add(bgMinNum);//大小单位数量总和

            if (bgStock.compareTo(bgSumNum) == -1 && indexOrder != BaseConfig.OrderType3) {
                //判断是否超出库存数量
                showToast("商品库存数量不足!");
                return false;
            }

            //还货剩余数量计算
            if (indexOrder == BaseConfig.OrderType5) {//还货
                BigDecimal surplusNum = new BigDecimal(beanGoods.getOrder_surplus_num());
                if (bgSumNum.compareTo(surplusNum) == 1) {
                    DialogUtil.showCustomDialog(getActivity(), "注意",
                            beanGoods.getGg_title() + "--" + beanGoods.getOrder_surplus_num_str() + ",已超出剩余商品数量", "确定", null, null);
                    return false;
                }

                if ("".equals(beanGoods.getIndex()) || beanGoods.getIndex() == null) {//是添加商品时
                    for (int i = 0; i < listGiveBack.size(); i++) {
                        //如果有相通的商品
                        if (listGiveBack.get(i).getOa_id().equals(beanGoods.getOa_id())
                                && listGiveBack.get(i).getGgp_id().equals(beanGoods.getGgp_id())) {

                            BigDecimal maxNum;
                            BigDecimal minNum;

                            maxNum = new BigDecimal(listGiveBack.get(i).getMaxNum());
                            minNum = new BigDecimal(listGiveBack.get(i).getMinNum());

                            if (maxNum.multiply(bgUnitNum).add(minNum).add(bgSumNum).compareTo(surplusNum) == 1) {
                                DialogUtil.showCustomDialog(getActivity(), "注意",
                                        beanGoods.getGg_title() + "--" + beanGoods.getOrder_surplus_num_str() + ",已超出剩余商品数量", "确定", null, null);
                                return false;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            if (!"1".equals(beanGoods.getGgp_unit_num())) {
                BigDecimal[] result = bgSumNum.divideAndRemainder(bgUnitNum);
                beanGoods.setMaxNum(String.valueOf(result[0]));
                beanGoods.setMinNum(String.valueOf(result[1]));
            }

            BigDecimal maxPrice;
            if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "".equals(mEtMaxPrice.getText().toString())) {
                maxPrice = BigDecimal.ZERO;
            } else {
                maxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
            }

            BigDecimal minPrice;
            if (TextUtils.isEmpty(mEtMinPrice.getText()) || "".equals(mEtMinPrice.getText().toString())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(mEtMinPrice.getText().toString());
            }

            BigDecimal sumPrice;
            if (TextUtils.isEmpty(mEtSumPrice.getText()) || "".equals(mEtSumPrice.getText().toString())) {
                sumPrice = BigDecimal.ZERO;
            } else {
                sumPrice = new BigDecimal(mEtSumPrice.getText().toString());
            }

            beanGoods.setMaxPrice(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            beanGoods.setMinPrice(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            beanGoods.setTotalPrice(sumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            beanGoods.setRemarks(mEtRemarks.getText().toString());
            beanGoods.setStock_nameref(mTvStock.getText().toString());
            beanGoods.setSaleTypeName(mTvSaleTypeName.getText().toString());
            beanGoods.setSource_id(beanGoods.getOad_id());

            if (saleTypeBean != null) {
                beanGoods.setSaleTypeDiscount(saleTypeBean.getSp_discount());
            }

            if ("".equals(beanGoods.getIndex()) || beanGoods.getIndex() == null) {
                showToast(beanGoods.getGg_title() + "添加成功");
                addBuyGoods(beanGoods);
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
                    case BaseConfig.OrderType5:
                        for (int i = 0; i < listGiveBack.size(); i++) {
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
        int index = -1;
        BigDecimal maxNum;
        BigDecimal minNum;
        BigDecimal goodsMaxNum;
        BigDecimal goodsMinNum;

        BigDecimal maxPrice = null;
        BigDecimal minPrice = null;

        if (beanGoods != null) {
            if (TextUtils.isEmpty(beanGoods.getMaxPrice()) || "".equals(beanGoods.getMaxPrice())) {
                maxPrice = BigDecimal.ZERO;
            } else {
                maxPrice = new BigDecimal(beanGoods.getMaxPrice());
            }

            if (TextUtils.isEmpty(beanGoods.getMinPrice()) || "".equals(beanGoods.getMinPrice())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(beanGoods.getMinPrice());
            }
        }

        switch (indexOrder) {
            case BaseConfig.OrderType1:
                if (beanGoods != null) {
                    for (int i = 0; i < listGoodsOrder.size(); i++) {
                        if (listGoodsOrder.get(i).getGgp_id().equals(beanGoods.getGgp_id())) {
                            maxNum = new BigDecimal(listGoodsOrder.get(i).getMaxNum());
                            minNum = new BigDecimal(listGoodsOrder.get(i).getMinNum());

                            goodsMaxNum = new BigDecimal(beanGoods.getMaxNum());
                            goodsMinNum = new BigDecimal(beanGoods.getMinNum());

                            //新增商品加上已有商品数量
                            maxNum = maxNum.add(goodsMaxNum);
                            minNum = minNum.add(goodsMinNum);
                            //将新的数量设置进列表
                            listGoodsOrder.get(i).setMaxNum(maxNum.toString());
                            listGoodsOrder.get(i).setMinNum(minNum.toString());
                            //将新的价格添加进商品
                            listGoodsOrder.get(i).setMaxPrice(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            listGoodsOrder.get(i).setMinPrice(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                            String totalPrice = maxNum.multiply(maxPrice).add(minNum.multiply(minPrice))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                            listGoodsOrder.get(i).setTotalPrice(totalPrice);

                            index = i;
                            break;
                        }
                    }
                    if (index == -1) {
                        listGoodsOrder.add(beanGoods);
                    }
                }
                amountMoney(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                if (beanGoods != null) {
                    for (int i = 0; i < listProcessOrder.size(); i++) {
                        if (listProcessOrder.get(i).getGgp_id().equals(beanGoods.getGgp_id())) {
                            maxNum = new BigDecimal(listProcessOrder.get(i).getMaxNum());
                            minNum = new BigDecimal(listProcessOrder.get(i).getMinNum());

                            goodsMaxNum = new BigDecimal(beanGoods.getMaxNum());
                            goodsMinNum = new BigDecimal(beanGoods.getMinNum());

                            //新增商品加上已有商品数量
                            maxNum = maxNum.add(goodsMaxNum);
                            minNum = minNum.add(goodsMinNum);
                            //将新的数量设置进列表
                            listProcessOrder.get(i).setMaxNum(maxNum.toString());
                            listProcessOrder.get(i).setMinNum(minNum.toString());
                            //将新的价格添加进商品
                            listProcessOrder.get(i).setMaxPrice(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            listProcessOrder.get(i).setMinPrice(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                            String totalPrice = maxNum.multiply(maxPrice).add(minNum.multiply(minPrice))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                            listProcessOrder.get(i).setTotalPrice(totalPrice);
                            index = i;
                            break;
                        }
                    }
                    if (index == -1) {
                        listProcessOrder.add(beanGoods);
                    }
                }
                amountMoney(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                if (beanGoods != null) {
                    if (goodsStatusBean != null && (indexOrder == BaseConfig.OrderType3 ||
                            indexOrder == BaseConfig.OrderType4)) {
                        beanGoods.setGoodsStatus(goodsStatusBean.getCode());
                    } else {
                        beanGoods.setGoodsStatus(0);
                    }
                    listRefundOrder.add(beanGoods);
                }
                amountMoney(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                if (beanGoods != null) {
                    if (goodsStatusBean != null && (indexOrder == BaseConfig.OrderType3 ||
                            indexOrder == BaseConfig.OrderType4)) {
                        beanGoods.setGoodsStatus(goodsStatusBean.getCode());
                    } else {
                        beanGoods.setGoodsStatus(0);
                    }
                    listExchangeOrder.add(beanGoods);
                }
                amountMoney(listExchangeOrder);
                break;
            case BaseConfig.OrderType5:
                if (beanGoods != null) {
                    for (int i = 0; i < listGiveBack.size(); i++) {
                        if (listGiveBack.get(i).getOa_id().equals(beanGoods.getOa_id())
                                && listGiveBack.get(i).getGgp_id().equals(beanGoods.getGgp_id())) {
                            maxNum = new BigDecimal(listGiveBack.get(i).getMaxNum());
                            minNum = new BigDecimal(listGiveBack.get(i).getMinNum());

                            goodsMaxNum = new BigDecimal(beanGoods.getMaxNum());
                            goodsMinNum = new BigDecimal(beanGoods.getMinNum());

                            //新增商品加上已有商品数量
                            maxNum = maxNum.add(goodsMaxNum);
                            minNum = minNum.add(goodsMinNum);
                            //将新的数量设置进列表
                            listGiveBack.get(i).setMaxNum(maxNum.toString());
                            listGiveBack.get(i).setMinNum(minNum.toString());
                            //将新的价格添加进商品
                            listGiveBack.get(i).setMaxPrice(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            listGiveBack.get(i).setMinPrice(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                            String totalPrice = maxNum.multiply(maxPrice).add(minNum.multiply(minPrice))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                            listGiveBack.get(i).setTotalPrice(totalPrice);
                            index = i;
                            break;
                        }
                    }

                    if (index == -1) {
                        listGiveBack.add(beanGoods);
                    }
                }
                amountMoney(listGiveBack);
                break;
        }
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
            case BaseConfig.OrderType5:
                listGiveBack.set(i, beanGoods);
                amountMoney(listGiveBack);
                break;
        }
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
            case BaseConfig.OrderType5:
                adapterBuyList.setData(listGiveBack);
                break;
        }
    }

    /**
     * 清空商品编辑框
     */
    private void clearGoodsEdit() {
        mTvGoodsName.setText("");
        mTvSaleTypeName.setText("");
        mIvGoodsStatus.setImageResource(R.color.transparent);

        mTvStock.setText("库存");

        mEtMaxNum.setText("");
        mEtMaxPrice.setText("");
        mEtMinNum.setText("");
        mEtMinPrice.setText("");
        mEtSumPrice.setText("");

        mEtRemarks.setText("");

        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
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
                getSaleTypeList(userBean.getUserId(), String.valueOf(indexOrder), clientBean.getCc_id()), true);
    }

    /**
     * 初始化销售类型popup
     */
    private void initSaleTypePopup(int width) {
        saleTypePopup = new SaleTypePopup(getActivity(), width, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                saleTypeBean = listSaleType.get(position);

                mTvSaleTypeName.setText(listSaleType.get(position).getSp_name());
                beanGoods.setSaleType(saleTypeBean.getSp_code());
                beanGoods.setSaleTypeName(saleTypeBean.getSp_name());
                beanGoods.setSaleTypeDiscount(saleTypeBean.getSp_discount());

                if (!"".equals(beanGoods.getSaleTypeDiscount()) && beanGoods.getSaleTypeDiscount() != null) {
                    BigDecimal maxPrice;
                    BigDecimal minPrice;
                    BigDecimal discount = new BigDecimal(beanGoods.getSaleTypeDiscount());//销售类型折扣
                    //根据销售类型计算价格
                    if (beanGoods.getMax_price() == null || "".equals(beanGoods.getMax_price())) {
                        //如果是0,就不需要计算了
                        mEtMaxPrice.setText(beanGoods.getMax_price());
                    } else {
                        maxPrice = new BigDecimal(beanGoods.getMax_price());
                        BigDecimal price = maxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        mEtMaxPrice.setText(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }

                    if (beanGoods.getMin_price() == null || "".equals(beanGoods.getMin_price())) {
                        //如果是0,就不需要计算了
                        mEtMinPrice.setText(beanGoods.getMin_price());
                    } else {
                        minPrice = new BigDecimal(beanGoods.getMin_price());
                        BigDecimal price = minPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        mEtMinPrice.setText(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }

                } else {
                    mEtMaxPrice.setText(beanGoods.getMax_price());
                    mEtMinPrice.setText(beanGoods.getMin_price());
                }

                calculatePrice();
                saleTypePopup.dismiss();
            }
        });
    }

    /**
     * 显示销售类型popup
     */
    private void showPwSaleType() {
        saleTypePopup.setData(listSaleType);
        // 显示窗口
        saleTypePopup.showAsDropDown(mTvSaleTypeName, -UIUtils.dp2px(8), 0);
    }

    /**
     * 获取商品退货类型数据列表
     */
    private void getGoodsStatusList(final boolean isDialog) {
        AsyncHttpUtil<GoodsStatusBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsStatusBean.class, new IUpdateUI<GoodsStatusBean>() {
            @Override
            public void updata(GoodsStatusBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listGoodsStatus = jsonBean.getListData();
                    if (isDialog) {
                        showPwGoodsStatus();
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
        httpUtil.post(M_Url.getGoodsStatusList, L_RequestParams.
                getGoodsStatusList(), true);
    }

    /**
     * 初始化商品退货类型popup 商品状态 1 正 2 临 3残 4过
     */
    private void initGoodsStatusPopup(int width) {
        goodsStatusPopup = new GoodsStatusPopup(getActivity(), width, new GoodsStatusPopup.OnGoodsStatusListener() {
            @Override
            public void onGoodsStatus(int position) {
                goodsStatusBean = listGoodsStatus.get(position);

                beanGoods.setGoodsStatus(listGoodsStatus.get(position).getCode());
                goodsStatus(goodsStatusBean.getCode());

                goodsStatusPopup.dismiss();
            }
        });
    }

    /**
     * 显示商品类型
     */
    private void showPwGoodsStatus() {
        goodsStatusPopup.setData(listGoodsStatus);
        // 显示窗口
        goodsStatusPopup.showAsDropDown(mIvGoodsStatus, -UIUtils.dp2px(8), 0);
    }

    private void goodsStatus(int code) {
        switch (code) {
            case Comon.GOODS_STATUS:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status);
                break;
            case Comon.GOODS_STATUS_C:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status_cc);
                break;
            case Comon.GOODS_STATUS_G:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status_gq);
                break;
            case Comon.GOODS_STATUS_L:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status_lq);
                break;
        }
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

            if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                String sunPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtSumPrice.setText(sunPrice);
            } else {
                bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
                mEtSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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

            if (TextUtils.isEmpty(mEtSumPrice.getText()) ||
                    !Validation.isNumericAndExceedZero(mEtSumPrice.getText().toString())) {
                mEtMinPrice.setText("0");
                mEtMaxPrice.setText("0");
                return;
            }

            if (TextUtils.isEmpty(beanGoods.getGgp_unit_num())) {
                bdUnitNum = new BigDecimal("1");
            } else {
                bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());
            }

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

            bdSumPrice = new BigDecimal(mEtSumPrice.getText().toString());//总价格

            if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                //如果小单位数量>0
                if (bdMinNum.compareTo(BigDecimal.ZERO) == 1) {
                    String minPrice = bdSumPrice.divide(bdMinNum, 2, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();

                    mEtMaxPrice.setText(minPrice);
                    mEtMinPrice.setText(minPrice);
                } else {//小单位数量==0
                    mEtMaxPrice.setText(sumPrice);
                    mEtMinPrice.setText(sumPrice);
                }
            } else {
                //如果大单位数量>0,小单数量==0
                if (bdMaxNum.compareTo(BigDecimal.ZERO) == 1 && bdMinNum.compareTo(BigDecimal.ZERO) == 0) {
                    BigDecimal maxPrice = bdSumPrice.divide(bdMaxNum,2, BigDecimal.ROUND_HALF_UP);
                    String minPrice = maxPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice.toString());
                    mEtMinPrice.setText(minPrice);
                } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 && bdMinNum.compareTo(BigDecimal.ZERO) == 0) {//如果大单位数量==0,小单数量==0
                    //以大单位价格为首
                    String maxPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    String minPrice = bdSumPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                    mEtMinPrice.setText(minPrice);
                } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 && bdMinNum.compareTo(BigDecimal.ZERO) == 1) {//如果大单位数量==0,小单数量>0
                    String maxPrice = bdSumPrice.divide(bdMinNum, 2, BigDecimal.ROUND_HALF_UP).multiply(bdUnitNum)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    String minPrice = bdSumPrice.divide(bdMinNum, 2, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                    mEtMinPrice.setText(minPrice);
                } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 1 && bdMinNum.compareTo(BigDecimal.ZERO) == 1) {//如果大单位数量>0,小单数量>0
                    //将数量先换算成小单位在进行价格计算
                    bdSumNum = bdUnitNum.multiply(bdMaxNum).add(bdMinNum);
                    String maxPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP).multiply(bdUnitNum)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    String minPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                    mEtMinPrice.setText(minPrice);
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
            mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvTotalPrice.setText("0.00");
        }

        mTvSelectedNum.setText("已选(" + list.size() + "件)");
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        saleTypeBean = null;
        mTvSaleTypeName.setText("");
        restoreTab();
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                mTvOrderForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvOrderForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listGoodsOrder.size() + "件)");

                amountMoney(context.listGoodsOrder);
                moveAnimation(0);
                alterWidth(mTvOrderForm);
                mLlGoodsStatus.setVisibility(View.GONE);
                goodsStatusBean = null;
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvProcessSheetForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listProcessOrder.size() + "件)");
                amountMoney(context.listProcessOrder);
                moveAnimation(1);
                alterWidth(mTvProcessSheetForm);
                mLlGoodsStatus.setVisibility(View.GONE);
                goodsStatusBean = null;
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvRefundForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listRefundOrder.size() + "件)");
                amountMoney(context.listRefundOrder);
                moveAnimation(3);
                alterWidth(mTvRefundForm);
                mLlGoodsStatus.setVisibility(View.VISIBLE);
                if (listGoodsStatus != null && listGoodsStatus.size() > 0) {
                    goodsStatusBean = listGoodsStatus.get(0);
                    //                    goodsStatus(goodsStatusBean.getCode());
                } else {
                    goodsStatusBean = null;
                }
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvExchangeForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                amountMoney(context.listExchangeOrder);
                moveAnimation(2);
                alterWidth(mTvExchangeForm);
                mLlGoodsStatus.setVisibility(View.VISIBLE);
                if (listGoodsStatus != null && listGoodsStatus.size() > 0) {
                    goodsStatusBean = listGoodsStatus.get(0);
                    //                    goodsStatus(goodsStatusBean.getCode());
                } else {
                    goodsStatusBean = null;
                }
                break;
            case BaseConfig.OrderType5:
                mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvGiveBackForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listGiveBack.size() + "件)");
                amountMoney(context.listGiveBack);
                moveAnimation(1);
                alterWidth(mTvGiveBackForm);
                mLlGoodsStatus.setVisibility(View.GONE);
                goodsStatusBean = null;
                break;
        }
        isChangBJ();
        updateBuyAdapter();

        listCategory.clear();
        adapterGoodsCategory.notifyDataSetChanged();

        listGoods.clear();
        adapterGoods.notifyDataSetChanged();

        switch (indexOrder) {
            case BaseConfig.OrderType1:
            case BaseConfig.OrderType2:
            case BaseConfig.OrderType3:
            case BaseConfig.OrderType4:
                mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
                queryGsCategory();
                break;
            case BaseConfig.OrderType5://还货
                mPullScroll.setMode(PullToRefreshBase.Mode.DISABLED);
                queryGivebackCategory();
                break;
        }

        getSaleTypeList(false);
    }

    /**
     * 是否可以改变价格
     */
    private void isChangBJ() {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
            case BaseConfig.OrderType2:
            case BaseConfig.OrderType4:
                //正常商品是否可以变价
                if (userBean.getIsChangPrice().equals("2")) {
                    mEtMaxPrice.setEnabled(false);
                    mEtMinPrice.setEnabled(false);
                    mEtSumPrice.setEnabled(false);
                } else {
                    mEtMaxPrice.setEnabled(true);
                    mEtMinPrice.setEnabled(true);
                    mEtSumPrice.setEnabled(true);
                }
                break;
            case BaseConfig.OrderType3:
                //退货订单是否可以变价
                if (userBean.getIsChangeThPrice().equals("2")) {
                    mEtMaxPrice.setEnabled(false);
                    mEtMinPrice.setEnabled(false);
                    mEtSumPrice.setEnabled(false);
                } else {
                    mEtMaxPrice.setEnabled(true);
                    mEtMinPrice.setEnabled(true);
                    mEtSumPrice.setEnabled(true);
                }
                break;
            case BaseConfig.OrderType5://还货商品不可以变价
                mEtMaxPrice.setEnabled(false);
                mEtMinPrice.setEnabled(false);
                mEtSumPrice.setEnabled(false);
                break;
        }
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvOrderForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvProcessSheetForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvExchangeForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvRefundForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvGiveBackForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 4
                                * index).setDuration(300).start();
    }

}
