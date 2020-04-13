package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ActionGroupGoodsAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ActionBean;
import com.xdjd.storebox.bean.CartBean;
import com.xdjd.storebox.bean.GoodsCartBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by freestyle_hong on 2017/9/13.
 */

public class ActionActivityGroup extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.action_name)
    TextView actionName;
    @BindView(R.id.action_price)
    TextView actionPrice;
    @BindView(R.id.action_discount)
    TextView actionDiscount;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.groupList)
    NoScrollListView groupList;
    @BindView(R.id.pull_scroll)
    ScrollView pullScroll;
    @BindView(R.id.title_ll)
    LinearLayout titleLl;
    @BindView(R.id.purchase_cart_tv)
    TextView purchaseCartTv;
    @BindView(R.id.purchase_cart_rl)
    RelativeLayout purchaseCartRl;
    @BindView(R.id.join_cart)
    Button joinCart;


    private VaryViewHelper varyViewHelper;
    private ActionGroupGoodsAdapter adapter;
    private List<ActionBean> list = new ArrayList<>();

    private int pageNo = 1;
    private int mFlag = 0;
    private int type;
    private String activityId;//活动id
    private String groupNum;//组合套餐在购物车数量
    private int goodsNum;
    private String ggp_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_group);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type",0);
        initView();
    }

    private void go_main(){
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(type != 0)
            go_main();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFlag = 1;
        pageNo = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        loadData(PublicFinal.FIRST);
        //loadCartPriceNum();
    }

    private void initView() {
        titleBar.leftBack(this);
        titleBar.setTitle("组合促销");
        joinCart.setSelected(true);
        activityId = getIntent().getStringExtra("activityId");
        varyViewHelper = new VaryViewHelper(pullScroll);
       /* pullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(pullScroll);*/
        /*pullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
               mFlag = 1;
                pageNo = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO);
            }
        });*/
        adapter = new ActionGroupGoodsAdapter();
        groupList.setAdapter(adapter);
        adapter.setData(list);
        loadCartPriceNum();
    }

    private void loadData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            varyViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ActionBean> httpUtil = new AsyncHttpUtil<>(this, ActionBean.class, new IUpdateUI<ActionBean>() {
            @Override
            public void updata(ActionBean bean) {
                if (bean.getRepCode().equals("00")) {
                    actionName.setText(bean.getActivityName());//名称
                    actionPrice.setText("¥"+bean.getActivityPrice());//活动价格
                    actionDiscount.setBackgroundResource(R.drawable.shape_screen_item);
                    actionDiscount.setText("立减¥"+bean.getActivityDiscountPrice());//优惠价格
                    if (null != bean.getListData() && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        ggp_id = bean.getListData().get(0).getGgp_id();
                        //queryGoodInCartNum(UserInfoUtils.getId(ActionActivityGroup.this),bean.getListData().get(0).getGgp_id());
                        varyViewHelper.showDataView();
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.pull_up_remind));
                            pageNo--;
                        }else if (mFlag == 0 || mFlag == 1) {
                            varyViewHelper.showEmptyView("该活动商品已下架！");
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (bean.getRepCode().equals("99")){
                   // varyViewHelper.showErrorView(new ActionActivityGroup().OnErrorListener());
                }else{
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                //varyViewHelper.showErrorView(new ActionActivity.OnErrorListener());
            }

            @Override
            public void finish() {
               // pullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getActivityGoods, L_RequestParams.getActivityGoods(
                UserInfoUtils.getId(this), activityId, "1",UserInfoUtils.getStoreHouseId(this)), false);
    }

    /**
     * 获取购物车数量和总价格
     */
    private void loadCartPriceNum() {
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    purchaseCartTv.setText(bean.getTotalAmount());
                } else if ("30".equals(bean.getRepCode())){
                    Intent intent = new Intent(ActionActivityGroup.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab",4);
                    startActivity(intent);
                    showToast(bean.getRepMsg());
                }else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.cartInfo, L_RequestParams.getCartInfo(UserInfoUtils.getId(this),UserInfoUtils.getCenterShopId(this),
                UserInfoUtils.getStoreHouseId(this)), false);
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST);
           loadCartPriceNum();
        }
    }

    @OnClick({R.id.join_cart,R.id.purchase_cart_rl})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.join_cart:
                queryGoodInCartNum(UserInfoUtils.getId(ActionActivityGroup.this),ggp_id);

                break;
            case R.id.purchase_cart_rl:
                startActivity(CartActivity.class);
                break;
        }
    }

   /*修改购物车*/
    private  void modifyCart(String num,String ggpId){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(ActionActivityGroup.this, GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    showToast("已成功添加至购物车！");
                    loadCartPriceNum();
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {}
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.editCartGoodNum,L_RequestParams.editCart(UserInfoUtils.getId(this),ggpId,num,UserInfoUtils.getStoreHouseId(this),activityId),false);
    }
    /**
     * 查询组合商品在购物车数量
     *
     * @param userId
     * @param ggp_id
     */
    private void queryGoodInCartNum(String userId, String ggp_id) {
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(ActionActivityGroup.this, GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    groupNum = bean.getC_goods_num();
                    goodsNum = Integer.valueOf(groupNum)+1;
                    modifyCart(String.valueOf(goodsNum),"");
                } else {
                    groupNum = "0";
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {}
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.queryGoodsNums, L_RequestParams.queryGoodsCarNum(userId, ggp_id,UserInfoUtils.getStoreHouseId(this),activityId), false);
    }

}
