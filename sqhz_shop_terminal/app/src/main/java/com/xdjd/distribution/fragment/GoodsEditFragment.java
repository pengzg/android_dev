package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.OrderDeclareActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.holder.GoodsBuyListHolder;
import com.xdjd.distribution.holder.GoodsCategoryHolder;
import com.xdjd.distribution.holder.GoodsEditHolder;
import com.xdjd.distribution.holder.GoodsListHolder;
import com.xdjd.distribution.popup.SaleTypePopup;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/10
 *     desc   : 订单申报 -- 商品编辑界面
 *     version: 1.0
 * </pre>
 */

public class GoodsEditFragment extends BaseFragment {

    @BindView(R.id.tv_order_form)
    TextView mTvOrderForm;
    @BindView(R.id.tv_process_sheet_form)
    TextView mTvProcessSheetForm;
    @BindView(R.id.tv_exchange_form)
    TextView mTvExchangeForm;
    @BindView(R.id.tv_refund_form)
    TextView mTvRefundForm;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.fl_goods_category)
    FrameLayout mFlGoodsCategory;
    @BindView(R.id.fl_goods_list)
    FrameLayout mFlGoodsList;
    @BindView(R.id.fl_goods_buy_list)
    FrameLayout mFlGoodsBuyList;
    @BindView(R.id.ll_horizontal)
    LinearLayout mLlHorizontal;
    @BindView(R.id.horizontal_scroll)
    HorizontalScrollView mHorizontalScroll;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_amount_desc)
    TextView mTvAmountDesc;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.fl_goods_edit)
    FrameLayout mFlGoodsEdit;
    @BindView(R.id.v_line)
    View mVLine;
    private View view;

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
     * 选中商品的bean
     */
    private GoodsBean beanGoods;

    /**
     * 销售类型popup
     */
    private SaleTypePopup saleTypePopup;

    private List<SaleTypeBean> listSaleType;

    /**
     * 销售类型bean
     */
    private SaleTypeBean saleTypeBean = null;

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

    private OrderDeclareActivity context;

    /**
     * 选中分类的code
     */
    private String categoryCode;

    /**
     * 商品分类holder
     */
    private GoodsCategoryHolder goodsCategoryHolder;
    /**
     * 商品列表holder
     */
    private GoodsListHolder goodsListHolder;
    /**
     * 商品编辑holder
     */
    private GoodsEditHolder goodsEditHolder;
    /**
     * 已选中的商品holder
     */
    private GoodsBuyListHolder goodsBuyListHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData(int index) {
        indexOrder = index;

        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;

        showBeenDataTab();

        //        clearGoodsEdit();
        mHorizontalScroll.scrollTo(0, 0);
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 4;

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mLlHorizontal.getLayoutParams();
        layoutParams.width = width + (width / 3) * 1;
        mLlHorizontal.setLayoutParams(layoutParams);

        context = (OrderDeclareActivity) getActivity();
        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;

        storehouseId = getActivity().getIntent().getStringExtra("storehouseId");
        deliveryTime = getActivity().getIntent().getStringExtra("deliveryTime");
        note = getActivity().getIntent().getStringExtra("note");

        clientBean = UserInfoUtils.getClientInfo(getActivity());

        indexOrder = BaseConfig.OrderType1;
        selectTab();

        //商品分类holder
        goodsCategoryHolder = new GoodsCategoryHolder(getActivity());
        mFlGoodsCategory.addView(goodsCategoryHolder.getRootView());

        //商品列表holder
        goodsListHolder = new GoodsListHolder(getActivity());
        mFlGoodsList.addView(goodsListHolder.getRootView());

        //商品编辑框holder
        goodsEditHolder = new GoodsEditHolder(getActivity());
        mFlGoodsEdit.addView(goodsEditHolder.getRootView());

        //已选中的商品
        goodsBuyListHolder = new GoodsBuyListHolder(getActivity());
        mFlGoodsBuyList.addView(goodsBuyListHolder.getRootView());

        //        initSaleTypePopup(width);
        getSaleTypeList(false);

        goodsCategoryHolder.setData(null);

        /*mEtSumPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEtSumPrice.setSelectAllOnFocus(true);
                    mEtSumPrice.addTextChangedListener(totalPriceWatcher);
                } else {
                    mEtSumPrice.removeTextChangedListener(totalPriceWatcher);
                }
            }
        });*/
    }

    /**
     * 显示有数据的列表
     */
    private void showBeenDataTab() {
        selectTab();
    }

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
            //            priceMatrixing();
        }
    };

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form,
            /*R.id.tv_sale_type_name,*/ R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            //1 普通 2 处理 3 退货 4 换货 5 还货
           /* case R.id.tv_order_form://订单
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
                break;*/
           /* case R.id.tv_sale_type_name://选择销售类型
                if (beanGoods != null) {
                    if (listSaleType != null && listSaleType.size() > 0) {
                        //                        showPwSaleType();
                    } else {
                        getSaleTypeList(true);
                    }
                }
                break;*/
            case R.id.tv_submit:
                context.toSubmitFragment();
                break;
        }
    }

   /* public void addGoods(GoodsBean bean) {
        onGroupExpand(0);
        listGoods.clear();
        listGoods.add(bean);
        adapterGoods.setData(listGoods);
    }

    *//**
     * 根据条件搜索
     *//*
    public void searchGoods(String searchKey) {
        onGroupExpand(0);
        categoryCode = "";
        mFlag = 1;
        page = 1;
        getGoodsList(searchKey);
    }*/

    /**
     * 根据选择订单状态,添加商品
     */
    private void addBuyGoods(GoodsBean beanGoods) {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                if (beanGoods != null)
                    listGoodsOrder.add(beanGoods);
                amountMoney(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                if (beanGoods != null)
                    listProcessOrder.add(beanGoods);
                amountMoney(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                if (beanGoods != null)
                    listRefundOrder.add(beanGoods);
                amountMoney(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                if (beanGoods != null)
                    listExchangeOrder.add(beanGoods);
                amountMoney(listExchangeOrder);
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
        }
    }

    /**
     * 根据订单状态刷新选中商品
     */
    /*private void updateBuyAdapter() {
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
    }*/

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
                        //                        showPwSaleType();
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
    /*private void initSaleTypePopup(int width) {
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
        //        mTvSaleTypeName.setText("");
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
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvProcessSheetForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listProcessOrder.size() + "件)");
                amountMoney(context.listProcessOrder);
                moveAnimation(1);
                alterWidth(mTvProcessSheetForm);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvRefundForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listRefundOrder.size() + "件)");
                amountMoney(context.listRefundOrder);
                moveAnimation(3);
                alterWidth(mTvRefundForm);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvExchangeForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                amountMoney(context.listExchangeOrder);
                moveAnimation(2);
                alterWidth(mTvExchangeForm);
                break;
        }
        //        isChangBJ();
        //        updateBuyAdapter();
        //        listGoods.clear();
        //        adapterGoods.notifyDataSetChanged();
        //        queryGsCategory();
        getSaleTypeList(false);
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
    }

    /**
     * 根据TextView的宽度修改线的宽度
     * @param tv
     */
    private void alterWidth(TextView tv){
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
                                * index).setDuration(200).start();
    }

    public boolean addGoods() {
        return true;
    }

    public void addGoods(GoodsBean bean) {
    }

    public void searchGoods(String searchStr) {
    }
}
