package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单成功接口
 * Created by lijipei on 2016/12/8.
 */

public class OrderSuccessActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
//    @BindView(R.id.order_code)
//    TextView mOrderCode;
    @BindView(R.id.to_main)
    Button mToMain;
    @BindView(R.id.to_order_detail)
    Button mToOrderDetail;

    private String payAmount;
    //private String orderCode;
    private String OrderId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
//        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OrderSuccessActivity.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("currentTab",0);
//                startActivity(intent);
//            }
//        });
        mTitleBar.setTitle("订单提交成功");

        //orderCode = getIntent().getStringExtra("orderCode");
        payAmount = getIntent().getStringExtra("payAmount");
        OrderId = getIntent().getStringExtra("OrderId");

//        mOrderCode.setText("您的订单号:"+orderCode);
    }

    @OnClick({R.id.to_order_detail, R.id.to_main})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.to_order_detail:
//                intent = new Intent(this,OrderDetailActivity.class);
//                intent.putExtra("orderId",OrderId);
//                startActivity(intent);
                startActivity(MyOrderActivity.class);
                finish();
                break;
            case R.id.to_main:
                intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("currentTab",0);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this,MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
    }
}
