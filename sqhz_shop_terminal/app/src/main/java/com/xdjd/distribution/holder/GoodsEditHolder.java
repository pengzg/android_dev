package com.xdjd.distribution.holder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseHolder;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.UIUtils;

import butterknife.BindView;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsEditHolder extends BaseHolder {


    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.tv_sale_type_name)
    TextView mTvSaleTypeName;
    @BindView(R.id.tv_stock)
    TextView mTvStock;
    @BindView(R.id.iv_max_minus)
    ImageView mIvMaxMinus;
    @BindView(R.id.et_max_num)
    EditText mEtMaxNum;
    @BindView(R.id.ll_max_left)
    LinearLayout mLlMaxLeft;
    @BindView(R.id.iv_max_plus)
    ImageView mIvMaxPlus;
    @BindView(R.id.rl_max)
    RelativeLayout mRlMax;
    @BindView(R.id.unit_max_nameref)
    TextView mUnitMaxNameref;
    @BindView(R.id.ll_max_num)
    LinearLayout mLlMaxNum;
    @BindView(R.id.et_max_price)
    EditText mEtMaxPrice;
    @BindView(R.id.rl_max_price)
    RelativeLayout mRlMaxPrice;
    @BindView(R.id.iv_min_minus)
    ImageView mIvMinMinus;
    @BindView(R.id.et_min_num)
    EditText mEtMinNum;
    @BindView(R.id.ll_min_left)
    LinearLayout mLlMinLeft;
    @BindView(R.id.iv_min_plus)
    ImageView mIvMinPlus;
    @BindView(R.id.rl_min)
    RelativeLayout mRlMin;
    @BindView(R.id.unit_min_nameref)
    TextView mUnitMinNameref;
    @BindView(R.id.ll_min_num)
    LinearLayout mLlMinNum;
    @BindView(R.id.et_min_price)
    EditText mEtMinPrice;
    @BindView(R.id.rl_min_price)
    RelativeLayout mRlMinPrice;
    @BindView(R.id.et_sum_price)
    EditText mEtSumPrice;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.check_all_goods)
    CheckBox mCheckAllGoods;
    @BindView(R.id.ll_all_goods)
    LinearLayout mLlAllGoods;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    private Context mContext;

    public GoodsEditHolder(Context context) {
        mContext = context;
    }

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(),R.layout.holder_goods_edit, null);
        return view;
    }

    @Override
    protected void refreshUI(Object data) {
        setTextWatcher(mEtMaxNum);
        setTextWatcher(mEtMinNum);
        setTextWatcher(mEtMaxPrice);
        setTextWatcher(mEtMinPrice);
    }

    private void setTextWatcher(final EditText et) {
        //是否有焦点
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et.setSelectAllOnFocus(true);
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
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
//            calculatePrice();
        }
    };

    /**
     * 点击其他的地方的时候判断商品输入信息是否符合添加条件
     *
     * @return false是商品填写的添加信息不通过
     */
//    public boolean addGoods() {
//        /*//1 普通 2 处理 3 退货 4 换货*/
//        if (beanGoods != null) { //已有选中的商品时
//            if ((TextUtils.isEmpty(mEtMaxNum.getText()) || "0".equals(mEtMaxNum.getText().toString()))
//                    && (TextUtils.isEmpty(mEtMinNum.getText()) || "0".equals(mEtMinNum.getText().toString()))) {
//                //最大和最小都未空时可以选择
//                clearGoodsEdit();
//
//                if (!"".equals(beanGoods.getIndex()) && beanGoods.getIndex() != null) {
//                    showToast(beanGoods.getGg_title() + "删除成功");
//                    adapterBuyList.list.remove(Integer.parseInt(beanGoods.getIndex()));
//                    adapterBuyList.notifyDataSetChanged();
//                }
//                beanGoods = null;
//                addBuyGoods(beanGoods);
//                return true;
//            }
//
//            if (TextUtils.isEmpty(mTvSaleTypeName.getText()) || "".equals(beanGoods.getSaleType())) {
//                showToast("请选择销售类型");
//                return false;
//            }
//
//            //1 都有 2 小单位 3 大单位
//            if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
//
//                //商品单价不能为空
//                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "0".equals(mEtMaxPrice.getText().toString()) ||
//                        !Validation.isNumericAndExceedZero(mEtMaxPrice.getText().toString())) {
//                    showToast("商品单价必须大于0");
//                    return false;
//                }
//
//                //商品单价不能为空
//                if (TextUtils.isEmpty(mEtMinPrice.getText()) || "0".equals(mEtMinPrice.getText().toString()) ||
//                        !Validation.isNumericAndExceedZero(mEtMinPrice.getText().toString())) {
//                    showToast("商品单价必须大于0");
//                    return false;
//                }
//
//            } else if ("2".equals(beanGoods.getUnit_type())) {
//
//                //商品单价不能为空
//                if (TextUtils.isEmpty(mEtMinPrice.getText()) || "0".equals(mEtMinPrice.getText().toString()) ||
//                        !Validation.isNumericAndExceedZero(mEtMinPrice.getText().toString())) {
//                    showToast("商品单价必须大于0");
//                    return false;
//                }
//
//            } else if ("3".equals(beanGoods.getUnit_type())) {
//
//                //商品单价不能为空
//                if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "0".equals(mEtMaxPrice.getText().toString()) ||
//                        !Validation.isNumericAndExceedZero(mEtMaxPrice.getText().toString())) {
//                    showToast("商品单价必须大于0");
//                    return false;
//                }
//            }
//            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
//                beanGoods.setMaxNum("0");
//            } else {
//                String str = mEtMaxNum.getText().toString();
//                String newStr = str.replaceFirst("^0*", "");
//
//                beanGoods.setMaxNum(newStr.equals("") ? "0" : newStr);
//            }
//
//            if (TextUtils.isEmpty(mEtMinNum.getText())) {
//                beanGoods.setMinNum("0");
//            } else {
//                String str = mEtMinNum.getText().toString();
//                String newStr = str.replaceFirst("^0*", "");
//
//                beanGoods.setMinNum(newStr.equals("") ? "0" : newStr);
//            }
//
//            beanGoods.setMaxPrice(mEtMaxPrice.getText().toString());
//            beanGoods.setMinPrice(mEtMinPrice.getText().toString());
//            beanGoods.setTotalPrice(mEtSumPrice.getText().toString());
//            beanGoods.setRemarks(mEtRemarks.getText().toString());
//            beanGoods.setStock_nameref(mTvStock.getText().toString());
//            beanGoods.setSaleTypeName(mTvSaleTypeName.getText().toString());
//
//            if (saleTypeBean != null) {
//                beanGoods.setSaleTypeDiscount(saleTypeBean.getSp_discount());
//            }
//
//            if ("".equals(beanGoods.getIndex()) || beanGoods.getIndex() == null) {
//                showToast(beanGoods.getGg_title() + "添加成功");
//                addBuyGoods(beanGoods);
//            } else {
//                switch (indexOrder) {
//                    case BaseConfig.OrderType1:
//                        for (int i = 0; i < listGoodsOrder.size(); i++) {
//                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
//                                updateBuyGoods(beanGoods, i);
//                                break;
//                            }
//                        }
//                        break;
//                    case BaseConfig.OrderType2:
//                        for (int i = 0; i < listProcessOrder.size(); i++) {
//                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
//                                updateBuyGoods(beanGoods, i);
//                                break;
//                            }
//                        }
//                        break;
//                    case BaseConfig.OrderType3:
//                        for (int i = 0; i < listRefundOrder.size(); i++) {
//                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
//                                updateBuyGoods(beanGoods, i);
//                                break;
//                            }
//                        }
//                        break;
//                    case BaseConfig.OrderType4:
//                        for (int i = 0; i < listExchangeOrder.size(); i++) {
//                            if (beanGoods.getIndex().equals(String.valueOf(i))) {
//                                updateBuyGoods(beanGoods, i);
//                                break;
//                            }
//                        }
//                        break;
//                }
//                showToast(beanGoods.getGg_title() + "修改成功");
//                LogUtils.e("tag", "修改的商品信息:" + beanGoods.toString());
//            }
//
//            beanGoods = null;
//            clearGoodsEdit();
//        } else {
//            clearGoodsEdit();
//        }
//        return true;
//    }
//
//    @OnClick({R.id.iv_max_minus,R.id.iv_max_plus,R.id.iv_min_minus,R.id.iv_min_plus,R.id.btn_complete,R.id.btn_cancel})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_max_minus://大单位减商品数量
//                minusCalculation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
//                break;
//            case R.id.iv_max_plus:
//                plusCalculation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
//                break;
//            case R.id.iv_min_minus://小单位减商品数量
//                minusCalculation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
//                break;
//            case R.id.iv_min_plus:
//                plusCalculation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
//                break;
//    case R.id.btn_cancel:
//    clearGoodsEdit();
//    beanGoods = null;
//    break;
//    case R.id.btn_complete:
//            if (beanGoods != null && (TextUtils.isEmpty(mEtMaxNum.getText()) || "0".equals(mEtMaxNum.getText().toString()))
//            && (TextUtils.isEmpty(mEtMinNum.getText()) || "0".equals(mEtMinNum.getText().toString()))) {
//        DialogUtil.showCustomDialog(getActivity(), "注意", "请输入产品数量!", "确定", null, null);
//        return;
//    }
//    if (addGoods()) {
//        adapterBuyList.notifyDataSetChanged();
//    }
//    break;
//        }
//    }
//
//    /**
//     * 数量添加计算
//     *
//     * @param et
//     */
//    private void plusCalculation(EditText et, LinearLayout llLeft, RelativeLayout rlMain, ImageView ivPlus) {
//        if (beanGoods == null) {
//            return;
//        }
//        BigDecimal bd;
//        if (TextUtils.isEmpty(et.getText())) {
//            bd = new BigDecimal(0);
//        } else {
//            bd = new BigDecimal(et.getText().toString());
//        }
//        final String numStr = (bd.intValue() + 1) + "";
//        et.setText(numStr);
//        AnimationUtil.setTranslateAnimation(et, llLeft, rlMain, ivPlus);
//        if (et.hasFocus()) {//有焦点
//            et.setSelection(String.valueOf((bd.intValue() + 1)).length());
//        } else {
//            //没有焦点
//            calculatePrice();
//        }
//    }
//
//    /**
//     * 数量减少计算
//     *
//     * @param et
//     */
//    private void minusCalculation(final EditText et, LinearLayout llLeft, RelativeLayout rlMain, ImageView ivPlus) {
//        if (beanGoods == null) {
//            return;
//        }
//        BigDecimal bd;
//        final String numStr;
//        if (TextUtils.isEmpty(et.getText())) {
//            return;
//        } else {
//            bd = new BigDecimal(et.getText().toString());
//            int num = bd.intValue();
//            if (num == 0) {
//                numStr = "0";
//            } else if (num - 1 == 0) {
//                numStr = "0";
//            } else {
//                numStr = num - 1 + "";
//            }
//
//            et.setText(numStr);
//            AnimationUtil.setTranslateAnimation(et, llLeft, rlMain, ivPlus);
//            if (et.hasFocus()) {
//                et.setSelection(numStr.length());
//            } else {
//                calculatePrice();
//            }
//        }
//    }
//
//    /**
//     * 根据购买数量计算商品价格
//     */
//    private void calculatePrice() {
//        if (beanGoods != null) {
//            BigDecimal bdMaxPrice;
//            BigDecimal bdMaxNum;
//
//            BigDecimal bdMinPrice;
//            BigDecimal bdMinNum;
//
//            BigDecimal bdSumPrice = null;
//
//            //1 都有 2 小单位 3 大单位
//            if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
//                if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
//                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
//                } else {
//                    bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
//                }
//
//                if (TextUtils.isEmpty(mEtMaxNum.getText())) {
//                    bdMaxNum = new BigDecimal(0);
//                } else {
//                    bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
//                }
//
//                if (TextUtils.isEmpty(mEtMinPrice.getText())) {
//                    bdMinPrice = new BigDecimal(BigInteger.ZERO);
//                } else {
//                    bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
//                }
//                if (TextUtils.isEmpty(mEtMinNum.getText())) {
//                    bdMinNum = new BigDecimal(0);
//                } else {
//                    bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
//                }
//
//                bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
//            } else if ("2".equals(beanGoods.getUnit_type())) {
//                if (TextUtils.isEmpty(mEtMinPrice.getText())) {
//                    bdMinPrice = new BigDecimal(BigInteger.ZERO);
//                } else {
//                    bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
//                }
//
//                if (TextUtils.isEmpty(mEtMinNum.getText())) {
//                    bdMinNum = new BigDecimal(0);
//                } else {
//                    bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
//                }
//
//                bdSumPrice = bdMinPrice.multiply(bdMinNum);
//            } else if ("3".equals(beanGoods.getUnit_type())) {
//                if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
//                    bdMaxPrice = new BigDecimal(BigInteger.ZERO);
//                } else {
//                    bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
//                }
//                if (TextUtils.isEmpty(mEtMaxNum.getText())) {
//                    bdMaxNum = new BigDecimal(0);
//                } else {
//                    bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
//                }
//
//                bdSumPrice = bdMaxPrice.multiply(bdMaxNum).setScale(2, BigDecimal.ROUND_HALF_UP);
//            }
//            if (bdSumPrice != null && bdSumPrice.doubleValue() > 0) {
//                mEtSumPrice.setText(bdSumPrice.toString());
//            } else {
//                mEtSumPrice.setText("");
//            }
//        }
//    }
//
//    /**
//     * 刷新
//     */
//    private void updateEditLayout() {
//        //1 都有 2 小单位 3 大单位
//        if ("1".equals(beanGoods.getUnit_type()) || "".equals(beanGoods.getUnit_type()) || null == beanGoods.getUnit_type()) {
//            mLlMaxNum.setVisibility(View.VISIBLE);
//            mRlMaxPrice.setVisibility(View.VISIBLE);
//            mLlMinNum.setVisibility(View.VISIBLE);
//            mRlMinPrice.setVisibility(View.VISIBLE);
//        } else if ("2".equals(beanGoods.getUnit_type())) {
//            mLlMaxNum.setVisibility(View.GONE);
//            mRlMaxPrice.setVisibility(View.GONE);
//            mLlMinNum.setVisibility(View.VISIBLE);
//            mRlMinPrice.setVisibility(View.VISIBLE);
//        } else if ("3".equals(beanGoods.getUnit_type())) {
//            mLlMaxNum.setVisibility(View.VISIBLE);
//            mRlMaxPrice.setVisibility(View.VISIBLE);
//            mLlMinNum.setVisibility(View.GONE);
//            mRlMinPrice.setVisibility(View.GONE);
//        }
//    }

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

        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
    }
}
