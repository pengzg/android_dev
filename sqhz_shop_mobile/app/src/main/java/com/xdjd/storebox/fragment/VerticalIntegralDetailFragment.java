package com.xdjd.storebox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.IntegralConfirmOrderActivity;
import com.xdjd.storebox.activity.IntegralGoodsDetailActivity;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.IntegralGoodsDetailBean;
import com.xdjd.storebox.holder.GoodsDetailImageHolderView;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.popup.IntegralExchangePopupWindow;
import com.xdjd.view.verticalslide.CustScrollView;
import com.xdjd.view.verticalslide.DragLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class VerticalIntegralDetailFragment extends BaseFragment implements View.OnClickListener, IntegralExchangePopupWindow.orderConfirmListener {
    @BindView(R.id.custScrollView)
    CustScrollView custScrollView;

    @BindView(R.id.goods_detail_lunbo)
    ConvenientBanner goodsDetailLunbo;
    @BindView(R.id.goods_title)
    TextView goodsTitle;
    @BindView(R.id.integrate_price)
    TextView integratePrice;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.good_stock)
    TextView mGoodStock;
    private DragLayout draglayout;
    private TextView mExchange;
    private TextView mIntegralPrice;

    private VaryViewHelper mVaryViewHelper = null;
    private int gpId;//商品id
    private int  goodStock;//商品库存数
    public String goodsDesc = "";
    private List<Integer> localImages;
    private IntegralExchangePopupWindow exchangePopupWindow;
    private IntegralGoodsDetailActivity activity;
    private String goodsIntegralPrice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vertical_integral_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        draglayout = (DragLayout) getActivity().findViewById(R.id.draglayout);
        mExchange = (TextView) getActivity().findViewById(R.id.tv_exchange);
        mIntegralPrice = (TextView)getActivity().findViewById(R.id.integral_price);

        activity = (IntegralGoodsDetailActivity) getActivity();
        gpId = activity.getGpId();
        goodStock = activity.getgStock();
        mVaryViewHelper = new VaryViewHelper(draglayout);
        mExchange.setOnClickListener(this);
        //loadData(PublicFinal.TWO);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(PublicFinal.TWO);
    }

    /*加载数据*/
    private void loadData(int isFirst) {
        if (PublicFinal.FIRST == isFirst) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<IntegralGoodsDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(), IntegralGoodsDetailBean.class,
                new IUpdateUI<IntegralGoodsDetailBean>() {
                    @Override
                    public void updata(IntegralGoodsDetailBean bean) {
                        if (bean.getRepCode().equals("00")) {
                            goodStock = bean.getWig_stock();//商品库存
                            goodsDesc = bean.getGoods_content();//商品描述
                            activity.getFragmentWeb().initView();
                           mGoodStock.setText(String.valueOf(goodStock));
                            goodsTitle.setText(bean.getGoods_tittle());//商品名称
                            integratePrice.setText(bean.getIntegrate_price());//商品积分价格
                            mIntegralPrice.setText(bean.getIntegrate_price());
                            goodsPrice.setText(bean.getGoods_price());//商品市场价格
                            goodsIntegralPrice = bean.getIntegrate_price();
                            goodsDetailLunbo.setPages(new CBViewHolderCreator<GoodsDetailImageHolderView>() {
                                @Override
                                public GoodsDetailImageHolderView createHolder() {
                                    return new GoodsDetailImageHolderView();
                                }
                            }, bean.getImageList())//设置两个点图片作为翻页指示器，不设置则没有指示器，可// 以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                                    .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                                            R.drawable.ic_page_indicator_focused})
                                    //设置指示器的方向
                                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                        } else {
                            showToast(bean.getRepMsg());
                        }
                    }

                    @Override
                    public void sendFail(ExceptionType s) {
                        showToast(s.getDetail());
                    }

                    @Override
                    public void finish() {

                    }
                });
        httpUtil.post(M_Url.getIntegralGoodsDetail, L_RequestParams.GetIntegralGoodsDetail(UserInfoUtils.getId(getActivity()), String.valueOf(gpId)), false);
    }

    /**
     * 添加轮播图本地图片
     *//*
    private void setLocalImages() {
        localImages = new ArrayList<>();
        localImages.add(R.drawable.welcome_01);
        localImages.add(R.drawable.welcome_02);
        localImages.add(R.drawable.welcome_03);
    }

    private void initView() {
        setLocalImages();
        goodsDetailLunbo.setPages(new CBViewHolderCreator<LocalImageHolderView>() {//从本地加载轮播图片
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }


        }, localImages).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                *//*.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        startActivity(LunBoClickActivity.class);
                    }
                });*//*
    }*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_exchange://兑换按钮
                //showToast("hai");
                exchangePopupWindow = new IntegralExchangePopupWindow(getContext(), this, goodStock,goodsIntegralPrice);
                exchangePopupWindow.showAtLocation(custScrollView, Gravity.CENTER, 0, 0);
                break;
        }
    }

    @Override
    public void orderConfirm(int good_num) {
        Intent intent = new Intent(getActivity(), IntegralConfirmOrderActivity.class);
        intent.putExtra("wig_id", gpId);
        intent.putExtra("goodsNum", good_num);
        startActivity(intent);
    }
}
