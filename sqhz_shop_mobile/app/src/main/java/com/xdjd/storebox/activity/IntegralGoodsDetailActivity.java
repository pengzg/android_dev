package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.fragment.VerticalIntegralDetailFragment;
import com.xdjd.storebox.fragment.VerticalIntegralGoodsWebFragment;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.verticalslide.DragLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class IntegralGoodsDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    private int gpId;//商品id
    private int gStock;//商品库存
    private Intent intent;
    public boolean isCancel = false;
    private static final int HIDE_TAB = 0;
    private static final int SHOW_TAB = 1;
    private VerticalIntegralDetailFragment fragmentDetail;
    private VerticalIntegralGoodsWebFragment fragmentWeb;
    private DragLayout draglayout;
    Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HIDE_TAB:
                    //hideTab();
                    showTab();
                    break;
                case SHOW_TAB:
                    hideTab();
                    //showTab();
                    break;
            }
        }
    };

    public int getGpId() {
        return gpId;
    }

    public void setGpId(int gpId) {
        this.gpId = gpId;
    }

    public int getgStock() {
        return gStock;
    }

    public void setgStock(int gStock) {
        this.gStock = gStock;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_goods_detail);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("商品详情");
        gpId = getIntent().getIntExtra("gpId",0);
        gStock = getIntent().getIntExtra("gs",0);

        fragmentDetail = new VerticalIntegralDetailFragment();
        fragmentWeb = new VerticalIntegralGoodsWebFragment();
        /*Bundle bundle = new Bundle();
        bundle.putString("gpId",gpId);
        bundle.putInt("gStock",gStock);
        fragmentDetail.setArguments(bundle);*/

        getSupportFragmentManager().beginTransaction()
                .add(R.id.first, fragmentDetail).add(R.id.second, fragmentWeb)
                .commit();

        DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                //fragmentWeb.initView();
                mHandler.sendEmptyMessageDelayed(0,1000);

            }
        };
        draglayout = (DragLayout)findViewById(R.id.draglayout);
        draglayout.setNextPageListener(nextIntf);
        draglayout.setUpPageListener(new DragLayout.ShowUpPageNotifier() {
            @Override
            public void onUpPage() {
                mHandler.sendEmptyMessageDelayed(1,1000);
            }
        });

    }


    private void showTab() {
        findViewById(R.id.iv_arrow).setBackgroundResource(R.mipmap.down_arrow);
        ((TextView) findViewById(R.id.tv_pull_desc)).setText("下拉关闭图文详情");
    }

    private void hideTab() {
        findViewById(R.id.iv_arrow).setBackgroundResource(R.mipmap.up_arrow);
        ((TextView) findViewById(R.id.tv_pull_desc)).setText("上拉查看图文详情");
    }

    public VerticalIntegralDetailFragment getFragmentDetail() {
        return fragmentDetail;
    }
    public void setFragmentDetail(VerticalIntegralDetailFragment fragmentDetail) {
        this.fragmentDetail = fragmentDetail;
    }
    public VerticalIntegralGoodsWebFragment getFragmentWeb() {
        return fragmentWeb;
    }

    public void setFragmentWeb(VerticalIntegralGoodsWebFragment  fragmentWeb) {
        this.fragmentWeb = fragmentWeb;
    }
}
