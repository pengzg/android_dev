package com.bikejoy.testdemo.popup;

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

import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.view.LastInputEditText;
import com.bikejoy.testdemo.R;

import java.math.BigDecimal;
import java.math.BigInteger;

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
    private ProductSkuBean beanProduct;

    /**
     * @param context
     * @param type     1.入库 2.调拨
     * @param listener
     */
    public EditCartNumPopupWindow(final Context context, int type, OnEditCartNumListener listener) {
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

        holder.mTvTypeName.setText("入库申请");
        holder.mTvAddCart.setText("加入入库单");

        holder.mLlStock.setVisibility(View.INVISIBLE);

        holder.mIvPlus.setOnClickListener(this);
        holder.mIvMinus.setOnClickListener(this);
        holder.mTvAddCart.setOnClickListener(this);
        holder.mLlClose.setOnClickListener(this);

        holder.mEtNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.mEtNum.addTextChangedListener(textMaxWatcher);
                } else {
                    holder.mEtNum.removeTextChangedListener(textMaxWatcher);
                }
            }
        });

        holder.mEtPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    holder.mEtPrice.addTextChangedListener(priceWatcher);
                } else {
                    holder.mEtPrice.removeTextChangedListener(priceWatcher);
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
            String newStr = holder.mEtNum.getText().toString().replaceFirst("^0*", "");
            beanProduct.setNum(newStr);
        }
    };


    //优惠
    private TextWatcher priceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    holder.mEtPrice.setText(s);
                    holder.mEtPrice.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                holder.mEtPrice.setText(s);
                holder.mEtPrice.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    holder.mEtPrice.setText(s.subSequence(1, 2));
                    holder.mEtPrice.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public void showAddCart(ProductSkuBean bean, RelativeLayout mLlBottom) {
        this.beanProduct = bean;
        //        beanProduct.setNum("0");
        holder.mTvGoodsName.setText(beanProduct.getPm_title());
        holder.mTvStock.setText(beanProduct.getStocknum());

        holder.mEtNum.setText(beanProduct.getNum());
        holder.mEtPrice.setText(beanProduct.getPs_price());

        this.showAtLocation(mLlBottom,
                Gravity.RIGHT | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        holder.mTvAddCart.setOnClickListener(this);
        translateAnimIn(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_plus:
                plusCalculation(holder.mEtNum);
                break;
            case R.id.iv_minus:
                minusCalculation(holder.mEtNum);
                break;
            case R.id.tv_add_cart:
                LogUtils.e("商品信息", beanProduct.toString());

                BigDecimal bgPrice;
                BigDecimal bgNum;//商品数量

                if ("".equals(holder.mEtNum.getText().toString()) || holder.mEtNum == null) {
                    bgNum = new BigDecimal(BigInteger.ZERO);
                } else {
                    bgNum = new BigDecimal(holder.mEtNum.getText().toString());
                }

                if (bgNum.compareTo(BigDecimal.ZERO) == 0) {
                    UIUtils.Toast("入库数量不能为0");
                    return;
                }

                String price = holder.mEtPrice.getText().toString();
                if (price!=null && price.length()>0){
                    bgPrice = new BigDecimal(price);
                }else{
                    bgPrice = BigDecimal.ZERO;
                }
                price = bgPrice.setScale(2).toString();

                beanProduct.setSubtotal(bgNum.multiply(bgPrice).setScale(2).toString());
                beanProduct.setPrice(price);
                beanProduct.setNum(holder.mEtNum.getText().toString());
                listener.editCart((ProductSkuBean) beanProduct.clone());
                view.setOnClickListener(null);
                translateAnimOut(this.view, this);
                break;
            case R.id.ll_close:
                translateAnimOut(this.view, this);
                break;
        }
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
        et.setSelection(et.getText().length());
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
        et.setSelection(et.getText().length());
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
        void editCart(ProductSkuBean beanProduct);
    }


    class ViewHolder {
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_stock)
        TextView mTvStock;
        @BindView(R.id.ll_stock)
        LinearLayout mLlStock;
        @BindView(R.id.ll_close)
        LinearLayout mLlClose;
        @BindView(R.id.tv_type_name)
        TextView mTvTypeName;
        @BindView(R.id.iv_minus)
        ImageView mIvMinus;
        @BindView(R.id.et_num)
        EditText mEtNum;
        @BindView(R.id.ll_left)
        LinearLayout mLlLeft;
        @BindView(R.id.iv_plus)
        ImageView mIvPlus;
        @BindView(R.id.unit_nameref)
        TextView mUnitNameref;
        @BindView(R.id.tv_add_cart)
        TextView mTvAddCart;
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;
        @BindView(R.id.et_price)
        LastInputEditText mEtPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
