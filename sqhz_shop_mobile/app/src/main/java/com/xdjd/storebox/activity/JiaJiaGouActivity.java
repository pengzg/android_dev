package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.JiaJiaGouAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.JiaJiaGouBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 加价购商品列表
 * Created by lijipei on 2016/12/16.
 */

public class JiaJiaGouActivity extends BaseActivity implements JiaJiaGouAdapter.JiaJiaGouListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.jiajiagou_listview)
    NoScrollListView mJiajiagouListview;
    @BindView(R.id.scroll)
    ScrollView mScroll;
    @BindView(R.id.cancel_tv)
    TextView mCancelTv;
    @BindView(R.id.enter_tv)
    TextView mEnterTv;

    private List<JiaJiaGouBean> list = new ArrayList<>();
    private JiaJiaGouAdapter adapter;

    private VaryViewHelper mViewHelper = null;

    private String amount; //购物车应付金额
    private String goodsAmount; //选择的加价购商品价格,如果没选应为0

    private String giftgoodsid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiajiagou);
        ButterKnife.bind(this);
        initView();
        loadData(PublicFinal.FIRST);
    }

    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("换购商品");

        amount = getIntent().getStringExtra("amount");
        goodsAmount = getIntent().getStringExtra("goodsAmount");
        giftgoodsid = getIntent().getStringExtra("giftgoodsid");

        BigDecimal bigAmount = new BigDecimal(amount);
        BigDecimal bigGoodsAmount = new BigDecimal(goodsAmount);
        BigDecimal bignum3 = bigAmount.subtract(bigGoodsAmount);

        mViewHelper = new VaryViewHelper(mJiajiagouListview);
        adapter = new JiaJiaGouAdapter(this, bignum3);
        mJiajiagouListview.setAdapter(adapter);
    }

    private void loadData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<JiaJiaGouBean> httpUtil = new AsyncHttpUtil<>(this, JiaJiaGouBean.class,
                new IUpdateUI<JiaJiaGouBean>() {
                    @Override
                    public void updata(JiaJiaGouBean bean) {
                        if (bean.getRepCode().equals("00")) {
                            if (null != bean.getListData() && bean.getListData().size() > 0) {
                                list.addAll(bean.getListData());
                                adapter.setData(list,giftgoodsid);
                            } else {
                            }
                            adapter.notifyDataSetChanged();
                            mViewHelper.showDataView();
                        } else {
                            showToast(bean.getRepMsg());
                        }
                    }

                    @Override
                    public void sendFail(ExceptionType s) {
                        mViewHelper.showErrorView(new OnErrorListener());
                    }

                    @Override
                    public void finish() {
                    }
                });
        httpUtil.post(M_Url.getPromotionGoods, L_RequestParams.getPromotionGoods(
                UserInfoUtils.getId(this)), false);
    }

    @Override
    public void checkGoods(int i) {
//        if (list.get(i).getType() == 0){
//            list.get(i).setType(1);
//        }else{
//            list.get(i).setType(0);
//        }

        if (list.get(i).getWpg_id().equals(giftgoodsid)){
            giftgoodsid = "";
        }else{
            giftgoodsid = list.get(i).getWpg_id();
        }

//        for (int k=0;k<list.size();k++){
//            if (k==i){
//                continue;
//            }
//            list.get(k).setType(0);
//        }

        adapter.setGiftgoodsid(giftgoodsid);
        //adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.cancel_tv, R.id.enter_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                setResult(3000);
                finish();
                break;
            case R.id.enter_tv:
//                if (giftgoodsid == null||giftgoodsid.equals("")){
//                    showToast("还没有选择商品");
//                    return;
//                }

                Intent intent = new Intent();
                intent.putExtra("giftgoodsid",giftgoodsid);
                setResult(2000,intent);
                finish();
                break;
        }
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST);
        }
    }
}
