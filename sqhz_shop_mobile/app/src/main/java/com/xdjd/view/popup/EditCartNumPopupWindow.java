package com.xdjd.view.popup;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.serializer.IntegerCodec;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.ActionBean;
import com.xdjd.storebox.bean.BaseBean;
import com.xdjd.storebox.bean.GoodsBean;
import com.xdjd.storebox.bean.GoodsDetailBean;
import com.xdjd.storebox.holder.GoodsDetailImageHolderView;
import com.xdjd.utils.CartUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.zhy.autolayout.utils.AutoUtils;

import java.math.BigDecimal;


/**
 * 筛选popup
 * Created by lijipei on 2016/12/9.
 */

public class EditCartNumPopupWindow extends PopupWindow {
    private View view;
    private TextView viewLeft;
    private Context context;
    private TextView goodNum;
    private ImageView goodImage,ivCollection;
    private TextView goodName;
    private TextView goodMinNum;
    private TextView goodPrice;
    private TextView goodStock;
    private TextView goodModle;
    private TextView goodUnit;
    private TextView unit;
    private LinearLayout minusLL;
    private LinearLayout plusPlus;
    private TextView sureTv;
    private LinearLayout llStock;
    private View stockLine;
    private String min_num;//起订量
    private  String add_num;//增量
    private  String stock_num;//库存
    private String goods_type;//1:普通 2：预售
    private String editNum;//加减后的商品数量
    private String ggpId;//商品价格id
    editCartNumListener listener;
    //private ConvenientBanner lunBoImage;

    private String isFavorite = "";//	是否收藏 	1收藏 2取消收藏

    public EditCartNumPopupWindow(final Context context, final editCartNumListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = LayoutInflater.from(context).
                inflate(R.layout.pw_editcartnum_layout, null);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);//调用输入键盘
        AutoUtils.auto(view);
        this.setContentView(view);

        goodNum = (TextView)view.findViewById(R.id.good_num);
        goodImage = (ImageView)view.findViewById(R.id.good_path);
        goodName = (TextView)view.findViewById(R.id.good_name);
        goodPrice = (TextView)view.findViewById(R.id.good_price);
        goodStock = (TextView)view.findViewById(R.id.good_stock);
        llStock = (LinearLayout)view.findViewById(R.id.ll_stock);
        stockLine = (View)view.findViewById(R.id.stock_line);
        goodModle = (TextView)view.findViewById(R.id.good_modle);
        goodUnit = (TextView)view.findViewById(R.id.good_unit);
        goodMinNum = (TextView)view.findViewById(R.id.good_min_num);
        ivCollection = (ImageView) view.findViewById(R.id.iv_collection);
        unit = (TextView)view.findViewById(R.id.unit);
        viewLeft = (TextView)view.findViewById(R.id.left_view);
        sureTv = (TextView)view.findViewById(R.id.sure_tv);
        minusLL = (LinearLayout)view.findViewById(R.id.ll_minus);
        plusPlus  = (LinearLayout)view.findViewById(R.id.ll_plus);
        //lunBoImage = (ConvenientBanner)view.findViewById(R.id.goods_detail_lunbo);

        viewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    translateAnimOut(view, EditCartNumPopupWindow.this);
                    return;
                }
            }
        });
        goodNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showEditCartNum(context, "加入购物车数量", "确定", "取消", goodNum.getText().toString(),
                        min_num,add_num, stock_num,goods_type, new DialogUtil.MyCustomDialogListener() {
                            @Override
                            public void ok(String str) {
                                goodNum.setText(str);
                            }

                            @Override
                            public void no() {
                            }
                        });
            }
        });
        minusLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum = 0;
                if(!goodNum.getText().toString().equals("")){
                    sum = Integer.valueOf(goodNum.getText().toString());
                }else{
                    return;
                }
                if(sum == 0){
                    sum = 0;
                }else{
                    if(add_num.equals("0")){
                        sum = sum - 1;
                    }else{
                        sum = sum - Integer.valueOf(add_num);
                    }
                }
                if(min_num.equals("0")){
                    if(sum > Integer.valueOf(stock_num)){
                        sum = Integer.valueOf(stock_num);
                    }
                    goodNum.setText(String.valueOf(sum));
                }else{
                    if(sum > Integer.valueOf(stock_num)){
                        sum = Integer.valueOf(stock_num);
                    }
                    if(sum == 0 ){
                        sum = 0;
                    }else if(sum < Integer.valueOf(min_num)){
                        sum = 0;
                    }
                    goodNum.setText(String.valueOf(sum));
                }
            }
        });
        plusPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum = 0;
                if(goodNum.getText().toString().equals("")){
                   sum = 0;
                }else{
                 sum = Integer.valueOf(goodNum.getText().toString());
                }
                if(((sum == Integer.valueOf(stock_num)) || (sum > Integer.valueOf(stock_num)))
                        && goods_type.equals("1")){
                    if(Integer.valueOf(stock_num) > 0) {
                        sum = Integer.valueOf(stock_num);
                    }
                    else{
                            sum = sum+0;
                    }
                    UIUtils.Toast("库存不足,少买点吧!");
                }else{
                    if(add_num.equals("0")){
                        sum = sum + 1;
                    }else{
                        sum = sum + Integer.valueOf(add_num);
                    }
                    if(sum < Integer.valueOf(min_num)){
                        sum = Integer.valueOf(min_num);
                    }
                    if((sum > Integer.valueOf(stock_num))&&goods_type.equals("1")){
                        UIUtils.Toast("库存不足,少买点吧!");
                        return;
                    }
                }
                goodNum.setText(String.valueOf(sum));
            }
        });
        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNum = goodNum.getText().toString();
                if (editNum.equals("")) {
                    UIUtils.Toast("请输入订货数量！");
                    return;
                }else if((Integer.valueOf(editNum) > Integer.valueOf(stock_num))&&goods_type.equals("1")){
                    UIUtils.Toast("库存不足,少买点吧!");
                    return;
                }else{
                    if(Integer.valueOf(editNum) < Integer.valueOf(min_num) && Integer.valueOf(editNum)!= 0){
                        UIUtils.Toast("不能小于起订数量！");
                        return;
                    }else{
                        listener.editCart(editNum,ggpId,min_num);
                    }
                }

            }
        });

        ivCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectGoods(isFavorite.equals("1") ? "2" : "1");
            }
        });

    }

    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_in);
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

    public void dissPopupWindow(){
        if (isShowing()) {
            translateAnimOut(view, EditCartNumPopupWindow.this);
            return;
        }
    }
    public void clearNum(){
        goodNum.setText(" ");
    }

    /**
     * 显示筛选popup
     */
    public void showPwScreen(View v, String num,String isFavorite, GoodsBean bean) {
        this.showAtLocation(v,
                Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
        translateAnimIn(view);
        if(null == num || num.equals("0")){
            num = "0";
        }
        this.isFavorite = isFavorite;
        ggpId = bean.getGgp_id();
        if(bean.getGps_min_num().equals("0")){
            min_num = "1";
        }else {
            min_num = bean.getGps_min_num();//起订量
        }
        add_num = bean.getGps_add_num();//增量
        stock_num = bean.getGoods_stock();//商品库存
        goods_type = bean.getGgp_goods_type();//商品类型
        goodNum.setText(num);
       // goodNum.setSelection(goodNum.getText().length());
        if(bean.getBpa_path() != null&& !"".equals(bean.getBpa_path())){
           Glide.with(view.getContext()).load(bean.getBpa_path()).into(goodImage);
        }else{
            goodImage.setImageResource(R.drawable.default_pic);
        }
        goodName.setText(bean.getGg_title());
        goodPrice.setText("¥"+bean.getGps_price_min()+"/"+bean.getGg_unit_min_nameref());
        if(bean.getGgp_goods_type().equals("1")){
            llStock.setVisibility(View.VISIBLE);
            stockLine.setVisibility(View.VISIBLE);
            goodStock.setText(bean.getGoods_stock()+" "+bean.getGg_unit_min_nameref());
        }else{
            llStock.setVisibility(View.GONE);
            stockLine.setVisibility(View.GONE);
        }
        goodModle.setText(bean.getGg_model());
        goodUnit.setText(bean.getGg_unit_min_nameref());
        goodMinNum.setText(min_num);
        unit.setText(bean.getGg_unit_min_nameref());

        if ("1".equals(this.isFavorite)) {
            ivCollection.setImageResource(R.drawable.collect_true);
        } else {
            ivCollection.setImageResource(R.drawable.collect_false);
        }
    }

    /**
     * 显示筛选popup  活动跳pop
     */
    public void showPwScreenAction(View v, String num,String isFavorite, ActionBean bean) {
        this.showAtLocation(v,
                Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
        translateAnimIn(view);
        if(null == num || num.equals("0")){
            num = "0";
        }
        this.isFavorite = isFavorite;
        ggpId = bean.getGgp_id();
        //goodCartNum = num;//在购物车数量
        if(bean.getGps_min_num().equals("0")){
            min_num = "1";
        }else {
            min_num = bean.getGps_min_num();//起订量
        }
        add_num = bean.getGps_add_num();//增量
        stock_num = bean.getGoods_stock();
        goods_type = bean.getGgp_goods_type();//商品类型
        goodNum.setText(num);
        //goodNum.setSelection(goodNum.getText().length());
        if(bean.getBpa_path() != null && !"".equals(bean.getBpa_path())){
            Glide.with(view.getContext()).load(bean.getBpa_path()).into(goodImage);
        }else{
            goodImage.setImageResource(R.drawable.default_pic);
        }
        goodName.setText(bean.getGg_title());
        goodPrice.setText("¥"+bean.getGps_price_min()+"/"+bean.getGg_unit_min_nameref());
        if(bean.getGgp_goods_type().equals("1")){
            llStock.setVisibility(View.VISIBLE);
            stockLine.setVisibility(View.VISIBLE);
            goodStock.setText(bean.getGoods_stock()+" "+bean.getGg_unit_min_nameref());
        }else{
            llStock.setVisibility(View.GONE);
            stockLine.setVisibility(View.GONE);
        }
        goodStock.setText(bean.getGoods_stock()+" "+bean.getGg_unit_min_nameref());
        goodModle.setText(bean.getGg_model());
        goodUnit.setText(bean.getGg_unit_min_nameref());
        goodMinNum.setText(min_num);
        unit.setText(bean.getGg_unit_min_nameref());
        if ("1".equals(this.isFavorite)) {
            ivCollection.setImageResource(R.drawable.collect_true);
        } else {
            ivCollection.setImageResource(R.drawable.collect_false);
        }
    }
    public interface editCartNumListener{
        void editCart(String editNum,String ggpId,String  min_num);
    }


    private GoodsDetailBean bean = new GoodsDetailBean();

    /**
     * 收藏商品
     */
    private void collectGoods(final String isfavorite) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>((Activity) context, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if (jsonBean.getRepCode().equals("00")) {
                    isFavorite = isfavorite;
                    if (isfavorite.equals("1")) {
                        ivCollection.setImageResource(R.drawable.collect_true);
                        //弹性动画
                        ObjectAnimator oo = ObjectAnimator.ofFloat(
                                ivCollection, "ylx", 0.5f, 1.5f, 1)
                                .setDuration(300);
                        oo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(
                                    ValueAnimator animation) {
                                float f = (Float) animation
                                        .getAnimatedValue();
                                ivCollection.setScaleX(f);
                                ivCollection.setScaleY(f);
                            }
                        });
                        oo.setInterpolator(new BounceInterpolator());
                        oo.start();// 执行动画
                    } else {
                        ivCollection.setImageResource(R.drawable.collect_false);
                        //弹性动画
                        ObjectAnimator oo = ObjectAnimator.ofFloat(
                                ivCollection, "ylx", 1, 1.5f, 1)
                                .setDuration(300);
                        oo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(
                                    ValueAnimator animation) {
                                float f = (Float) animation
                                        .getAnimatedValue();
                                ivCollection.setScaleX(f);
                                ivCollection.setScaleY(f);
                            }
                        });
                        oo.setInterpolator(new BounceInterpolator());
                        oo.start();// 执行动画
                    }
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.addFavoriteBatch, L_RequestParams.addFavoriteBatch(UserInfoUtils.getId(context), "1", ggpId, isfavorite), false);
    }
}
