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

import com.bikejoy.utils.UIUtils;
import com.bikejoy.testdemo.R;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 筛选popup
 * Created by lijipei on 2016/12/9.
 */

public class EditCartNumPopupWindow2 extends PopupWindow implements View.OnClickListener {

    private View view;
    private Context context;
    private ViewHolder holder;
    private OnEditCartNumListener listener;
    //商品信息
    private ProductStoreBean beanProduct;

    /**
     * @param context
     * @param type     1.采购 2.调拨  3.报损单
     * @param listener
     */
    public EditCartNumPopupWindow2(final Context context, int type, OnEditCartNumListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_pw_editcartnum2, null);
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
                        translateAnimOut(view, EditCartNumPopupWindow2.this);
                    }
                }
                return true;
            }
        });

        if (3==type){
            holder.mTvTypeName.setText("报损申请");
            holder.mTvAddCart.setText("加入报损单");
        }else {
            holder.mTvTypeName.setText("调拨申请");
            holder.mTvAddCart.setText("加入调拨单");
        }

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

    public void showAddCart(ProductStoreBean bean, RelativeLayout mLlBottom) {
        this.beanProduct = bean;
        //        beanProduct.setNum("0");
        holder.mTvGoodsName.setText(beanProduct.getPss_productid_nameref());
        holder.mTvStock.setText(beanProduct.getPss_stocknum());

        holder.mEtNum.setText(beanProduct.getNum());
        holder.mTvPrice.setText(beanProduct.getPs_price());

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
                //                beanProduct.setNum(holder.mEtNum.getText().toString());
                break;
            case R.id.iv_minus:
                minusCalculation(holder.mEtNum);
                //                beanProduct.setNum(holder.mEtNum.getText().toString());
                break;
            case R.id.tv_add_cart:
                //                LogUtils.e("商品信息", beanProduct.toString());
                BigDecimal storeNum;//库存数量
                BigDecimal bgNum;//商品数量
                BigDecimal bgPrice;

                if ("".equals(beanProduct.getPss_stocknum()) || beanProduct.getPss_stocknum() == null) {
                    storeNum = new BigDecimal(BigInteger.ZERO);
                } else {
                    storeNum = new BigDecimal(beanProduct.getPss_stocknum());
                }
                if ("".equals(holder.mEtNum.getText().toString()) || holder.mEtNum == null) {
                    bgNum = new BigDecimal(BigInteger.ZERO);
                } else {
                    bgNum = new BigDecimal(holder.mEtNum.getText().toString());
                }
                if (bgNum.compareTo(storeNum) == 1) {
                    UIUtils.Toast("调拨数量已超出库存数量");
                    return;
                } else if (bgNum.compareTo(BigDecimal.ZERO) == 0) {
                    UIUtils.Toast("调拨数量不能为0");
                    return;
                }

                String price = beanProduct.getPs_price();
                if (price!=null && price.length()>0){
                    bgPrice = new BigDecimal(price);
                }else{
                    bgPrice = BigDecimal.ZERO;
                }

                beanProduct.setSubtotal(bgNum.multiply(bgPrice).setScale(2).toString());
                beanProduct.setNum(holder.mEtNum.getText().toString());
                listener.editCart((ProductStoreBean) beanProduct.clone());
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
            translateAnimOut(view, EditCartNumPopupWindow2.this);
            return;
        }
    }

    public interface OnEditCartNumListener {
        void editCart(ProductStoreBean beanProduct);
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
        @BindView(R.id.tv_price)
        TextView mTvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
