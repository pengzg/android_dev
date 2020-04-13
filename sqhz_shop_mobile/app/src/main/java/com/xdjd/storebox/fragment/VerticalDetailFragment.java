package com.xdjd.storebox.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.ActionActivity;
import com.xdjd.storebox.activity.CartActivity;
import com.xdjd.storebox.activity.GoodsDetailActivity;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.BaseBean;
import com.xdjd.storebox.bean.GoodsCartBean;
import com.xdjd.storebox.bean.GoodsDetailBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.holder.GoodsDetailImageHolderView;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.verticalslide.CustScrollView;
import com.xdjd.view.verticalslide.DragLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 商品详情fragment
 */
public class VerticalDetailFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.goods_detail_lunbo)
    ConvenientBanner mGoodsDetailLunbo;
    @BindView(R.id.collect_iv)
    ImageView mCollectIv;
    @BindView(R.id.collect_tv)
    TextView collectTv;
    @BindView(R.id.collect_ll)
    LinearLayout collectLl;
    @BindView(R.id.goods_standard)
    TextView goodsStandard;
    @BindView(R.id.goods_stock)
    TextView goodsStock;
    @BindView(R.id.detail_ll01)
    LinearLayout detailLl01;
    @BindView(R.id.line02)
    View line02;
    @BindView(R.id.goods_limit)
    TextView goodsLimit;
    @BindView(R.id.goods_valid_date)
    TextView goodsValidDate;
    @BindView(R.id.detail_ll02)
    LinearLayout detailLl02;
    @BindView(R.id.line03)
    View line03;
    @BindView(R.id.goods_start_num)
    TextView goodsStartNum;
    @BindView(R.id.goods_delivery_mode)
    TextView goodsDeliveryMode;
    @BindView(R.id.detail_ll03)
    LinearLayout detailLl03;
    @BindView(R.id.action_name)
    TextView actionName;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_ll)
    LinearLayout actionLl;
    @BindView(R.id.action_list)
    NoScrollListView actionList;
    @BindView(R.id.action_main_ll)
    LinearLayout actionMainLl;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.tv_pull_desc)
    TextView tvPullDesc;
    @BindView(R.id.custScrollView)
    CustScrollView custScrollView;
    @BindView(R.id.price_min)
    TextView priceMin;
    @BindView(R.id.price_max)
    TextView priceMax;
    @BindView(R.id.tv_retailPrice)
    TextView tvRetailPrice;
    @BindView(R.id.ll_ratailPrice)
    LinearLayout llRatailPrice;
    @BindView(R.id.goods_name)
    TextView goodsName;


    private DragLayout draglayout;
    private ImageView mGoods_plus_iv;//加号
    private LinearLayout mGoods_minus_ll;
    private ImageView mGoods_minus_iv;//减号
    private TextView goodNum;//商品数量
    private TextView mGoods_detail_cart;//进入购物车
    private TextView stock_out;//补货中

    private VaryViewHelper mVaryViewHelper = null;
    private GoodsDetailBean bean = new GoodsDetailBean();
    private GoodsDetailActivity activity;

    private String gpId;//商品id
    private String gpsId;//价格方案id
    public String goodsDesc = "";
    Boolean plusBtnFlag;//为true弹出加减框，false加减数量
    private String add_num;  //增量
    private String min_num; //最小起订量
    private String stock_num; //库存
    private String goods_type;
    private Integer editGoodNum;
    private String activityId;
    private String activityType;//活动类型   5-组合促销

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vertical_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        draglayout = (DragLayout) getActivity().findViewById(R.id.draglayout);
        mGoods_detail_cart = (TextView) getActivity().findViewById(R.id.goods_detail_cart);
        mGoods_plus_iv = (ImageView) getActivity().findViewById(R.id.goods_plus_iv);
        mGoods_minus_ll = (LinearLayout) getActivity().findViewById(R.id.goods_minus_ll);
        mGoods_minus_iv = (ImageView) getActivity().findViewById(R.id.goods_minus_iv);
        goodNum = (TextView) getActivity().findViewById(R.id.goods_buy_num);
        stock_out = (TextView)getActivity().findViewById(R.id.stockout_tv);

        mGoods_plus_iv.setOnClickListener(this);
        mGoods_minus_iv.setOnClickListener(this);
        mGoods_detail_cart.setOnClickListener(this);
        goodNum.setOnClickListener(this);

        activity = (GoodsDetailActivity) getActivity();
        gpId = getArguments().getString("gpId");
        gpsId = getArguments().getString("gpsId");
        mVaryViewHelper = new VaryViewHelper(draglayout);
        loadData(PublicFinal.FIRST);
        actionMainLl.setVisibility(View.GONE);//活动描述
        detailLl02.setVisibility(View.GONE);
        line03.setVisibility(View.GONE);
        mGoods_plus_iv.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        queryGoodInCartNum(UserInfoUtils.getId(getActivity()), gpId);
    }

    /**
     * 商品详情
     *
     * @param isFirst
     */
    private void loadData(int isFirst) {
        if (PublicFinal.FIRST == isFirst) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<GoodsDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(),
                GoodsDetailBean.class, new IUpdateUI<GoodsDetailBean>() {
            @Override
            public void updata(GoodsDetailBean jsonBean) {
                if (jsonBean.getRepCode().equals("00")) {
                    if(!jsonBean.getGoods_stock().equals("0")&&jsonBean.getGoods_stock()!=null){
                        mGoods_minus_ll.setVisibility(View.VISIBLE);
                        mGoods_plus_iv.setVisibility(View.VISIBLE);
                        stock_out.setVisibility(View.GONE);
                    }else{
                        mGoods_minus_ll.setVisibility(View.GONE);
                        mGoods_plus_iv.setVisibility(View.GONE);
                        stock_out.setVisibility(View.VISIBLE);//显示补货中
                    }
                    bean = jsonBean;
                    mVaryViewHelper.showDataView();
                    goodsDesc = bean.getGoods_content();

                    activity.getFragmentWeb().initView();


                    mGoodsDetailLunbo.setPages(new CBViewHolderCreator<GoodsDetailImageHolderView>() {
                        @Override
                        public GoodsDetailImageHolderView createHolder() {
                            return new GoodsDetailImageHolderView();
                        }
                    }, bean.getBpa_path_list())//设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                            .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                                    R.drawable.ic_page_indicator_focused})
                            //设置指示器的方向
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

                    if ("1".equals(bean.getIsFavorite())) {
                        mCollectIv.setImageResource(R.drawable.collect_true);
                        collectTv.setText("已收藏");
                    } else {
                        mCollectIv.setImageResource(R.drawable.collect_false);
                        collectTv.setText("收藏");
                    }

                    goodsName.setText(bean.getGg_title());
                    priceMin.setText("¥"+bean.getGps_price_min()+"/"+bean.getGg_unit_min_nameref());
                    if(!bean.getGps_price_min().equals(bean.getGps_price_max())){
                        priceMax.setVisibility(View.VISIBLE);
                        priceMax.setText("¥"+bean.getGps_price_max()+"/"+bean.getGg_unit_max_nameref());
                    }else{
                        priceMax.setVisibility(View.GONE);
                    }
                    goodsStandard.setText("规格参数：" + bean.getGg_model());
                    if(bean.getGgp_goods_type().equals("1")){
                        goodsStock.setText("库存：" + bean.getGoods_stock() + " " + bean.getGg_unit_min_nameref().toString());
                    }else{
                        goodsStock.setText("");
                    }
                    if (bean.getGps_min_num() == null || bean.getGps_min_num().equals("0")) {
                        goodsStartNum.setText("起订数量：" + "1");
                        min_num = "1";
                    } else {
                        goodsStartNum.setText("起订数量：" + bean.getGps_min_num());
                        min_num = bean.getGps_min_num();
                    }
                    if(jsonBean.getActivityId() != null && !jsonBean.getActivityId().equals("")){
                        actionMainLl.setVisibility(View.VISIBLE);
                        if(jsonBean.getActivityDesc() != null && !jsonBean.getActivityDesc().equals("")){
                            actionTitle.setVisibility(View.VISIBLE);
                            actionTitle.setText(jsonBean.getActivityDesc());
                        }else{
                            actionTitle.setVisibility(View.GONE);
                        }
                        actionName.setText(jsonBean.getActivityName());
                        activityId = jsonBean.getActivityId();
                        activityType = jsonBean.getActivityType();
                    }else{
                        actionMainLl.setVisibility(View.GONE);
                    }
                    add_num = bean.getGps_add_num();
                    stock_num = bean.getGoods_stock();
                    goods_type = bean.getGgp_goods_type();
                    queryGoodInCartNum(UserInfoUtils.getId(getActivity()), gpId);
                }else{
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadData(PublicFinal.FIRST);
                    }
                });
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getGoodsDetail, L_RequestParams.
                getGoodsDetail(UserInfoUtils.getId(getActivity()), gpsId, gpId,UserInfoUtils.getStoreHouseId(getActivity())), false);
    }

    @OnClick({R.id.collect_ll, R.id.action_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_detail_cart:
                startActivity(CartActivity.class);
                break;
            case R.id.goods_plus_iv:
                mGoods_minus_ll.setVisibility(View.VISIBLE);
                editPlus();
                modifyCart(String.valueOf(editGoodNum), gpId);
                break;
            case R.id.goods_minus_iv:
                editMinus();
                modifyCart(String.valueOf(editGoodNum), gpId);
                if (editGoodNum == 0) {
                    mGoods_minus_ll.setVisibility(View.GONE);
                }
                break;
            case R.id.goods_buy_num:
                DialogUtil.showEditCartNum(getActivity(), "加入购物车数量", "确定", "取消", goodNum.getText().toString(),
                        min_num,add_num, stock_num, goods_type,new DialogUtil.MyCustomDialogListener() {
                            @Override
                            public void ok(String str) {
                                modifyCart(str,gpId);
                            }

                            @Override
                            public void no() {
                            }
                        });
                break;
            case R.id.sure_tv:
                break;
            case R.id.collect_ll:
                collectGoods(bean.getIsFavorite().equals("1") ? "2" : "1");
                break;
            case R.id.action_ll://跳转活动
                if(!activityType.equals("5")){
                    Intent intent = new Intent(getActivity(), ActionActivity.class);
                    intent.putExtra("activityId",activityId);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 查询商品在购物车数量
     *
     * @param userId
     * @param ggp_id
     */
    private void queryGoodInCartNum(String userId, String ggp_id) {
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    goodNum.setText(bean.getC_goods_num());
                } else {
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {}
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.queryGoodsNums, L_RequestParams.queryGoodsCarNum(userId, ggp_id,UserInfoUtils.getStoreHouseId(getActivity()),""), false);
    }

    private void editPlus() {
        int sum1;
        if (goodNum.getText().toString().equals(" ")) {
            sum1 = 0;
        } else {
            sum1 = Integer.valueOf(goodNum.getText().toString());
        }
        if (((sum1 == Integer.valueOf(stock_num)) || (sum1 > Integer.valueOf(stock_num)))
                && goods_type.equals("1")) {
            if(Integer.valueOf(stock_num) > 0){
                sum1 = Integer.valueOf(stock_num);
            }
            else{
                sum1 = sum1+0;
            }
            UIUtils.Toast("库存不足,少买点吧!");
        } else {
            if (add_num.equals("0")) {
                sum1 = sum1 + 1;
            } else {
                sum1 = sum1 + Integer.valueOf(add_num);
            }
            if(sum1 < Integer.valueOf(min_num)){
                sum1 = Integer.valueOf(min_num);
            }
            if(sum1 > Integer.valueOf(stock_num)&&goods_type.equals("1")){
                UIUtils.Toast("库存不足,少买点吧!");
                return;
            }
        }
        editGoodNum = sum1;
    }

    private void editMinus() {
        int sum;
        if (goodNum.getText().toString().equals(" ")) {
            return;
        } else {
            sum = Integer.valueOf(goodNum.getText().toString());
        }
        if (sum == 0) {
            sum = 0;
        } else {
            if (add_num.equals("0")) {
                sum = sum - 1;
            } else {
                sum = sum - Integer.valueOf(add_num);
            }
        }
        if (min_num.equals("0")) {
            editGoodNum = sum;
        } else {
            if (sum == 0 || sum < Integer.valueOf(min_num)) {
                sum = Integer.valueOf("0");
            }
            editGoodNum = sum;
        }
    }

    /**
     * 修改购物车
     */
    private void modifyCart(String num, String ggpId) {
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    goodNum.setText(bean.getC_goods_num());
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                } else {
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {}
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.editCartGoodNum, L_RequestParams.editCart(UserInfoUtils.getId(getActivity()), ggpId, num,UserInfoUtils.getStoreHouseId(getActivity()),""), false);
    }


    /**
     * 收藏商品
     */
    private void collectGoods(final String isFavorite) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if (jsonBean.getRepCode().equals("00")) {
                    if (isFavorite.equals("1")) {
                        bean.setIsFavorite(isFavorite);
                        mCollectIv.setImageResource(R.drawable.collect_true);
                        collectTv.setText("已收藏");
                        //弹性动画
                        ObjectAnimator oo = ObjectAnimator.ofFloat(
                                mCollectIv, "ylx", 0.5f, 1.5f, 1)
                               .setDuration(300);
                        oo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(
                                    ValueAnimator animation) {
                                float f = (Float) animation
                                        .getAnimatedValue();
                                mCollectIv.setScaleX(f);
                                mCollectIv.setScaleY(f);
                            }
                        });
                        oo.setInterpolator(new BounceInterpolator());
                        oo.start();// 执行动画
                    } else {
                        bean.setIsFavorite(isFavorite);
                        mCollectIv.setImageResource(R.drawable.collect_false);
                        collectTv.setText("收藏");
                        //弹性动画
                        ObjectAnimator oo = ObjectAnimator.ofFloat(
                                mCollectIv, "ylx", 1, 1.5f, 1)
                                .setDuration(300);
                        oo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(
                                    ValueAnimator animation) {
                                float f = (Float) animation
                                        .getAnimatedValue();
                                mCollectIv.setScaleX(f);
                                mCollectIv.setScaleY(f);
                            }
                        });
                        oo.setInterpolator(new BounceInterpolator());
                        oo.start();// 执行动画
                    }

                    activity.isCancel = true;
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
        httpUtil.post(M_Url.addFavoriteBatch, L_RequestParams.addFavoriteBatch(UserInfoUtils.getId(getActivity()), "1", gpId, isFavorite), false);
    }


}
