package com.xdjd.view.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.utils.StringFormatUtil;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/2/24.
 */

public class IntegralExchangePopupWindow extends PopupWindow implements View.OnClickListener{

    private TextView cancel;
    private TextView tvExchange;
    private LinearLayout minuxL;
    private LinearLayout addL;
    private TextView goodNum;
    private TextView tv_price;
    private View view;
    private orderConfirmListener listener;

    private int goodStock;//库存数
    private String price;//积分单价
    private int good_num = 1;//兑换商品数

    public IntegralExchangePopupWindow(Context context,orderConfirmListener listener,int stock,String price) {
        super(context);
        this.goodStock = stock;//商品库存数
        this.price = price;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_integral_exchange, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());

        this.listener = listener;
        //点击外部消失
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        AutoUtils.auto(view);
        this.setContentView(view);

        cancel = (TextView)view.findViewById(R.id.cancel);//取消
        tvExchange = (TextView)view.findViewById(R.id.tv_exchange);//兑换
        minuxL = (LinearLayout)view.findViewById(R.id.num_minux);//减按钮
        addL = (LinearLayout)view.findViewById(R.id.num_add);//加按钮
        goodNum = (TextView)view.findViewById(R.id.good_num);//要兑换商品数
        tv_price = (TextView)view.findViewById(R.id.price);//积分价格

        cancel.setOnClickListener(this);
        tvExchange.setOnClickListener(this);
        minuxL.setOnClickListener(this);
        addL.setOnClickListener(this);
        goodNum.setText("1");//默认兑换数量为1
        tv_price.setText(price);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.tv_exchange:
                listener.orderConfirm(good_num);
                this.dismiss();
                break;
            case R.id.num_minux:
               good_num --;
                if(good_num >= 1){
                    goodNum.setText(String.valueOf(good_num));
                }else{
                    good_num = 1;
                }
                break;
            case R.id.num_add:
                good_num++;
                if(good_num <= goodStock){
                    goodNum.setText(String.valueOf(good_num));
                }else{
                    good_num = goodStock;
                }
                break;
        }
    }
    /*订单确认*/
    public interface orderConfirmListener{
        void orderConfirm(int good_num);
    }
}
