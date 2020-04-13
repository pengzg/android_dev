package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsSuccessActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_go_order)
    TextView mTvGoOrder;
    @BindView(R.id.tv_go_rollout_main)
    TextView mTvGoRolloutMain;
    @BindView(R.id.tv_alter)
    TextView mTvAlter;

    private int type;//1.铺货单,2.销售单,3.撤货单,4.返陈列出库成功 ,5.铺货任务

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 1);
        String title;
        switch (type) {
            case 1:
            case 5:
                title = "铺货单提交成功";
                mTvAlter.setText("铺货下单成功");
                mTvGoOrder.setText("查看铺货单");
                break;
            case 2:
                title = "铺货销售单提交成功";
                mTvAlter.setText("铺货销售下单成功");
                mTvGoOrder.setText("查看铺货销售单");
                break;
            case 3:
                title = "撤货单提交成功";
                mTvAlter.setText("撤货下单成功");
                mTvGoOrder.setText("查看撤货单");
                break;
            case 4:
                title = "返陈列出库提交成功";
                mTvAlter.setText("返陈列出库成功");
                mTvGoOrder.setText("查看陈列出库单");
                mTvGoRolloutMain.setText("跳转首页");
                break;
            default:
                title = "订单提交成功";
                break;
        }

        mTitleBar.setTitle(title);
        mTitleBar.setRightText("完成");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (type) {
                    case 4:
                        intent = new Intent(RolloutGoodsSuccessActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    case 5:
                        finishActivity();
                        break;
                    default:
                        intent = new Intent(RolloutGoodsSuccessActivity.this, RolloutGoodsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    @OnClick({R.id.tv_go_order, R.id.tv_go_rollout_main})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_go_order:
                switch (type) {
                    case 4:
                        intent = new Intent(RolloutGoodsSuccessActivity.this, DisplayFeeQueryActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        if (type==5){
                            type=1;
                        }
                        intent = new Intent(RolloutGoodsSuccessActivity.this, RolloutGoodsOrderActivity.class);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        break;
                }
                finishActivity();
                break;
            case R.id.tv_go_rollout_main:
                switch (type) {
                    case 4:
                        intent = new Intent(RolloutGoodsSuccessActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    default:
                        intent = new Intent(RolloutGoodsSuccessActivity.this, RolloutGoodsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                }
                finishActivity();
                break;
        }
    }
}
