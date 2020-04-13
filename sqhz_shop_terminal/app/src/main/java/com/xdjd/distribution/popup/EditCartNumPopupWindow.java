package com.xdjd.distribution.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.TagAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 筛选popup
 * Created by lijipei on 2016/12/9.
 */

public class EditCartNumPopupWindow extends PopupWindow implements View.OnClickListener {

    private View view;
    private Context context;
    private ViewHolder holder;
    private OnEditCartNumListener listener;
    //商品信息
    private GoodsBean beanGoods;
    //商品类型
    List<SaleTypeBean> listSaleType;

    private TagAdapter<String> mSizeTagAdapter;

    public EditCartNumPopupWindow(final Context context, OnEditCartNumListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_pw_editcartnum, null);
        holder = new ViewHolder(view);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.mLlMain.getLayoutParams();
        lp.width = (width / 5) * 4;
        holder.mLlMain.setLayoutParams(lp);

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int width = holder.mLlMain.getLeft();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x < width) {
                        translateAnimOut(view, EditCartNumPopupWindow.this);
                    }
                }
                return true;
            }
        });

        mSizeTagAdapter = new TagAdapter<>(context);
        holder.mFlowLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        holder.mFlowLayout.setAdapter(mSizeTagAdapter);
        holder.mFlowLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        beanGoods.setSaleType(listSaleType.get(i).getSp_code());
                        beanGoods.setSaleTypeName(listSaleType.get(i).getSp_name());
                        beanGoods.setSaleTypeDiscount(listSaleType.get(i).getSp_discount());

//                        beanGoods.setMaxPrice(beanGoods.getMax_price());
//                        beanGoods.setMinPrice(beanGoods.getMin_price());

                        /*if (!"".equals(beanGoods.getSaleTypeDiscount()) && beanGoods.getSaleTypeDiscount() != null) {
                            BigDecimal maxPrice;
                            BigDecimal minPrice;
                            BigDecimal discount = new BigDecimal(beanGoods.getSaleTypeDiscount());//销售类型折扣
                            //根据销售类型计算价格
                            if (beanGoods.getMax_price() == null || "".equals(beanGoods.getMax_price())) {
                                //如果是0,就不需要计算了
                                //                                mEtMaxPrice.setText(beanGoods.getMax_price());
                            } else {
                                maxPrice = new BigDecimal(beanGoods.getMax_price());
                                BigDecimal price = maxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                                beanGoods.setMaxPrice(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            }

                            if (beanGoods.getMin_price() == null || "".equals(beanGoods.getMin_price())) {
                                //如果是0,就不需要计算了
                                //                                mEtMinPrice.setText(beanGoods.getMin_price());
                            } else {
                                minPrice = new BigDecimal(beanGoods.getMin_price());
                                BigDecimal price = minPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                                beanGoods.setMinPrice(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            }

                            if ("1".equals(beanGoods.getGgp_unit_num())) {
                                holder.mTvMaxUnit.setText("");
                                holder.mTvMinPrice.setText(beanGoods.getMinPrice());
                                holder.mTvMinUnit.setText("元/" + beanGoods.getGg_unit_min_nameref());
                            } else {
                                holder.mTvMaxPrice.setText(beanGoods.getMaxPrice());//大单位价格
                                holder.mTvMinPrice.setText(beanGoods.getMinPrice());//小单位价格
                                holder.mTvMaxUnit.setText("元/" + beanGoods.getGg_unit_max_nameref());
                                holder.mTvMinUnit.setText("元/" + beanGoods.getGg_unit_min_nameref());
                            }

                        } else {
                            beanGoods.setMaxPrice(beanGoods.getMax_price());
                            beanGoods.setMinPrice(beanGoods.getMin_price());
                        }*/

                        calculateCartGoodsPrice();
                    }
                } else {
                }
            }
        });

        holder.mIvMaxPlus.setOnClickListener(this);
        holder.mIvMaxMinus.setOnClickListener(this);
        holder.mIvMinPlus.setOnClickListener(this);
        holder.mIvMinMinus.setOnClickListener(this);
        holder.mTvAddCart.setOnClickListener(this);
        holder.mLlClose.setOnClickListener(this);

        holder.mEtMaxNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.mEtMaxNum.addTextChangedListener(textMaxWatcher);
                } else {
                    holder.mEtMaxNum.removeTextChangedListener(textMaxWatcher);
                }
            }
        });

        holder.mEtMinNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.mEtMinNum.addTextChangedListener(textMinWatcher);
                } else {
                    holder.mEtMinNum.removeTextChangedListener(textMinWatcher);
                }
            }
        });
    }

    private TextWatcher textMaxWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String newStr = holder.mEtMaxNum.getText().toString().replaceFirst("^0*", "");
            beanGoods.setMaxNum(newStr);
            calculateCartGoodsPrice();
        }
    };

    private TextWatcher textMinWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String newStr = holder.mEtMinNum.getText().toString().replaceFirst("^0*", "");
            beanGoods.setMinNum(newStr);
            calculateCartGoodsPrice();
        }
    };

    public void showAddCart(GoodsBean bean, RelativeLayout mLlBottom) {
        this.beanGoods = bean;
        //设置价格数据
        beanGoods.setMaxPrice(beanGoods.getMax_price());
        beanGoods.setMinPrice(beanGoods.getMin_price());
        beanGoods.setMaxNum("0");
        beanGoods.setMinNum("0");

        if (listSaleType != null && listSaleType.size() > 0) {
            beanGoods.setSaleType(listSaleType.get(0).getSp_code());
            beanGoods.setSaleTypeName(listSaleType.get(0).getSp_name());
            beanGoods.setSaleTypeDiscount(listSaleType.get(0).getSp_discount());

            mSizeTagAdapter.onlyAddAll(listSaleType);
        } else {
            beanGoods.setSaleType("");
        }

        holder.mTvGoodsName.setText(beanGoods.getGg_title());
        String stockStr = UnitCalculateUtils.unitStr(beanGoods.getGgp_unit_num(), beanGoods.getGgs_stock(),
                beanGoods.getGg_unit_max_nameref(), beanGoods.getGg_unit_min_nameref());
        holder.mTvStock.setText(stockStr);
        holder.mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        holder.mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        holder.mEtMaxNum.setText(beanGoods.getMaxNum());
        holder.mEtMinNum.setText(beanGoods.getMinNum());

        //如果大小单位换算比==1隐藏大单位
        if ("1".equals(beanGoods.getGgp_unit_num())) {
            holder.mLlMaxNum.setVisibility(View.GONE);
            holder.mLlMax.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinPrice.setText(beanGoods.getMin_price());
            holder.mTvMinUnit.setText("元/" + beanGoods.getGg_unit_min_nameref());
        } else {
            holder.mLlMaxNum.setVisibility(View.VISIBLE);
            holder.mLlMax.setVisibility(View.VISIBLE);
            holder.mTvMaxPrice.setText(beanGoods.getMax_price());//大单位价格
            holder.mTvMinPrice.setText(beanGoods.getMin_price());//小单位价格
            holder.mTvMaxUnit.setText("元/" + beanGoods.getGg_unit_max_nameref());
            holder.mTvMinUnit.setText("元/" + beanGoods.getGg_unit_min_nameref());
        }
        this.showAtLocation(mLlBottom,
                Gravity.RIGHT | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        holder.mTvAddCart.setOnClickListener(this);
        translateAnimIn(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_max_plus:
                plusCalculation(holder.mEtMaxNum);
                beanGoods.setMaxNum(holder.mEtMaxNum.getText().toString());
                calculateCartGoodsPrice();
                break;
            case R.id.iv_max_minus:
                minusCalculation(holder.mEtMaxNum);
                beanGoods.setMaxNum(holder.mEtMaxNum.getText().toString());
                calculateCartGoodsPrice();
                break;
            case R.id.iv_min_plus:
                plusCalculation(holder.mEtMinNum);
                beanGoods.setMinNum(holder.mEtMinNum.getText().toString());
                calculateCartGoodsPrice();
                break;
            case R.id.iv_min_minus:
                minusCalculation(holder.mEtMinNum);
                beanGoods.setMinNum(holder.mEtMinNum.getText().toString());
                calculateCartGoodsPrice();
                break;
            case R.id.tv_add_cart:
                LogUtils.e("商品信息",beanGoods.toString());

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

                BigDecimal bgStock;
                if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null){
                    bgStock = BigDecimal.ZERO;
                }else{
                    bgStock = new BigDecimal(beanGoods.getGgs_stock());
                }

                BigDecimal bgNum = bgMaxNum.multiply(bgUnitNum);//大单位换算成小单位总数量
                BigDecimal bgSumNum = bgNum.add(bgMinNum);//大小单位数量总和

                if (bgStock.compareTo(bgSumNum) == -1) {
                    //判断是否超出库存数量
                    UIUtils.Toast("商品库存数量不足!");
                    return;
                }

                listener.editCart(beanGoods);
                view.setOnClickListener(null);
                translateAnimOut(this.view, this);
                break;
            case R.id.ll_close:
                translateAnimOut(this.view, this);
                break;
        }
    }

    public void setListSaleType(List<SaleTypeBean> listSaleType) {
        this.listSaleType = listSaleType;
        mSizeTagAdapter.onlyAddAll(listSaleType);
    }


    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(EditText et) {
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
    private void minusCalculation(EditText et) {
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

    private void calculateCartGoodsPrice() {
        BigDecimal bdMaxPrice = null;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice = null;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice;//    总价格
        BigDecimal discount;//销售类型折扣率

        if (!"".equals(beanGoods.getSaleTypeDiscount()) && beanGoods.getSaleTypeDiscount() != null) {
            discount = new BigDecimal(beanGoods.getSaleTypeDiscount());//销售类型折扣
        }else{
            discount = BigDecimal.ONE;
        }

        if (TextUtils.isEmpty(beanGoods.getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(beanGoods.getMaxPrice());
        }

        if (TextUtils.isEmpty(beanGoods.getMaxNum())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(beanGoods.getMaxNum());
        }

        if (TextUtils.isEmpty(beanGoods.getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(beanGoods.getMinPrice());
        }
        if (TextUtils.isEmpty(beanGoods.getMinNum())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(beanGoods.getMinNum());
        }

        //计算折扣率
        bdMaxPrice = bdMaxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
        bdMinPrice = bdMinPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);

        String sumPrice;
        if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            beanGoods.setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            beanGoods.setTotalPrice(sumPrice);
        }
        beanGoods.setTotalPrice(sumPrice);
    }


    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_in_right);
        view.startAnimation(animation);
    }

    /**
     * PopupWindow消失的动画
     */
    private void translateAnimOut(View view, final PopupWindow pw) {
        //setTab(PublicFinal.ALLDEFAULT);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_out);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void dissPopupWindow() {
        if (isShowing()) {
            translateAnimOut(view, EditCartNumPopupWindow.this);
            return;
        }
    }

    public interface OnEditCartNumListener {
        void editCart(GoodsBean goodsBean);
    }


    class ViewHolder {
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_stock)
        TextView mTvStock;
        @BindView(R.id.ll_stock)
        LinearLayout mLlStock;
        @BindView(R.id.tv_max_price)
        TextView mTvMaxPrice;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.ll_max)
        LinearLayout mLlMax;
        @BindView(R.id.tv_min_price)
        TextView mTvMinPrice;
        @BindView(R.id.tv_min_unit)
        TextView mTvMinUnit;
        @BindView(R.id.ll_min)
        LinearLayout mLlMin;
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
        @BindView(R.id.tv_add_cart)
        TextView mTvAddCart;
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;
        @BindView(R.id.flow_layout)
        FlowTagLayout mFlowLayout;
        @BindView(R.id.ll_close)
        LinearLayout mLlClose;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
