package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.GoodsDetailBean;
import com.xdjd.storebox.fragment.VerticalDetailFragment;
import com.xdjd.storebox.fragment.VerticalWebFragment;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.verticalslide.DragLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2016/11/28.
 */

public class GoodsDetailActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;



    private String gpId;//商品价格id
    private String gpsId;//商品价格方案id
    private String cartNum;//当前商品在购物车当中的数量

    private GoodsDetailBean bean = new GoodsDetailBean();

    private Intent intent;
    public boolean isCancel = false;
    private int type;


    private static final int HIDE_TAB = 0;
    private static final int SHOW_TAB = 1;
    private VerticalDetailFragment fragmentDetail;
    private VerticalWebFragment fragmentWeb;
    private DragLayout draglayout;
    private TextView mTextViewPullDesc;
    Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HIDE_TAB:
                    hideTab();
                    break;
                case SHOW_TAB:
                    showTab();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type",0);
        initView();
    }

    private void go_main(){
        startActivity(MainActivity.class);
        finish();
    }


    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == 0)
                    isCollect();
                else
                    go_main();
            }
        });
        mTitleBar.setTitle("商品详情");

        mTitleBar.setRightImageResource(R.drawable.to_homepage);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsDetailActivity.this, MainActivity.class);
                intent.putExtra("currentTab", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        gpId = getIntent().getStringExtra("gpId");
        gpsId = getIntent().getStringExtra("gpsId");

        fragmentDetail = new VerticalDetailFragment();
        fragmentWeb = new VerticalWebFragment();

        Bundle bundle = new Bundle();
        bundle.putString("gpId",gpId);
        bundle.putString("gpsId",gpsId);
        fragmentDetail.setArguments(bundle);

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
        //将“super.onSaveInstanceState(outState);”注释掉，
        // 让其不再保存Fragment的状态，达到其随着绑定Activity一起被回收的效果。
    }

    private void showTab() {
        findViewById(R.id.iv_arrow).setBackgroundResource(R.mipmap.down_arrow);
        ((TextView) findViewById(R.id.tv_pull_desc)).setText("下拉查看图文详情");
    }

    private void hideTab() {
        findViewById(R.id.iv_arrow).setBackgroundResource(R.mipmap.up_arrow);
        ((TextView) findViewById(R.id.tv_pull_desc)).setText("上拉关闭图文详情");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(type == 0)
            isCollect();
        else
            go_main();
    }

    /**
     * 判断是否走收藏商品接口
     */
    private void isCollect() {
        intent = new Intent();
        intent.putExtra("isCancel", isCancel);
        setResult(1100, intent);
        finish();
    }

    public VerticalDetailFragment getFragmentDetail() {
        return fragmentDetail;
    }

    public VerticalWebFragment getFragmentWeb() {
        return fragmentWeb;
    }

}
