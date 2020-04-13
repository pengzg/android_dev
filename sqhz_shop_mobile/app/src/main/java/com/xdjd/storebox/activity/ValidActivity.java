package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ActionGoodsAdapter;
import com.xdjd.storebox.adapter.listener.PurchaseGoodsListener;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ActionBean;
import com.xdjd.storebox.bean.CartBean;
import com.xdjd.storebox.bean.GoodsBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.event.GoodsNumEvent;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.CartUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
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
import com.xdjd.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 临期界面
 * Created by lijipei on 2016/11/28.
 */

public class ValidActivity extends BaseActivity implements PurchaseGoodsListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.action_gridview)
    NoScrollGridView mActionGridview;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.title_ll)
    LinearLayout mTitleLl;
    @BindView(R.id.purchase_cart_tv)
    TextView mPurchaseCartTv;
    @BindView(R.id.purchase_cart_rl)
    RelativeLayout mPurchaseCartRl;
    private ActionGoodsAdapter adapter;
    private List<ActionBean> list = new ArrayList<>();

    private String activityId;//活动id
    private VaryViewHelper mViewHelper = null;

    private int pageNo = 1;
    private int mFlag = 0;
    private int type;

    private String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid);
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
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        title = getIntent().getStringExtra("title");
        mTitleBar.setTitle(title == null ? "临期惠" : title);
        if(type != 0){
            mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    go_main();
                }
            });
        }

        mViewHelper = new VaryViewHelper(mPullScroll);

        activityId = getIntent().getStringExtra("activityId");

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);

        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
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
        });

        adapter = new ActionGoodsAdapter(this);
        mActionGridview.setAdapter(adapter);
        //loadData(PublicFinal.FIRST);
        loadCartPriceNum();
    }


    private void loadData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ActionBean> httpUtil = new AsyncHttpUtil<>(this, ActionBean.class, new IUpdateUI<ActionBean>() {
            @Override
            public void updata(ActionBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        adapter.setData(list);
                        mViewHelper.showDataView();
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.pull_up_remind));
                            pageNo--;
                            mViewHelper.showDataView();
                        }else if (mFlag == 0 || mFlag == 1){
                            mViewHelper.showEmptyView();
                        }
                    }
                    adapter.notifyDataSetChanged();

                } else if (bean.getRepCode().equals("99")){
                    mViewHelper.showErrorView(new OnErrorListener());
                }else{
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mViewHelper.showErrorView(new OnErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getExpireGoodsList, L_RequestParams.getExpireGoodsList(
                UserInfoUtils.getId(this), String.valueOf(pageNo)), false);
    }

    @OnClick(R.id.purchase_cart_rl)
    public void onClick() {
        startActivity(CartActivity.class);
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST);
            loadCartPriceNum();
        }
    }

    /**
     * 获取购物车数量和总价格
     */
    private void loadCartPriceNum() {
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    mPurchaseCartTv.setText(bean.getTotalAmount());
                } else if ("30".equals(bean.getRepCode())){
                    Intent intent = new Intent(ValidActivity.this, MainActivity.class);
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

    @Override
    public void plusCart(int i, RelativeLayout rl) {
       /* LogUtils.e("tag", list.get(i).getCartnum() + "--" + list.get(i).getGp_minnum() + "--" +
                list.get(i).getGp_addnum() + "--" + list.get(i).getGoodsStock());
        int plusNum = CartUtil.countPlusNum(list.get(i).getCartnum(), list.get(i).getGp_minnum(),
                list.get(i).getGp_addnum(), list.get(i).getGoodsStock());
        if (plusNum == CartUtil.First) {
            showToast("数量有误");
            return;
        }
        if (plusNum == CartUtil.Two) {
            showToast("数量超出库存范围");
            return;
        } else {
            alterCart(i, list.get(i).getGoodsId(), list.get(i).getGpId(),
                    String.valueOf(plusNum), list.get(i).getSupplierid(), rl);
        }*/
    }

    @Override
    public void minusCart(int i, RelativeLayout rl) {
        /*if (!list.get(i).getCartnum().equals("0")) {
            int minusNum = CartUtil.countMinusNum(list.get(i).getCartnum(), list.get(i).getGp_minnum(),
                    list.get(i).getGp_addnum(), list.get(i).getGoodsStock());
            alterCart(i, list.get(i).getGoodsId(), list.get(i).getGpId(),
                    String.valueOf(minusNum), list.get(i).getSupplierid(), rl);
        }*/
    }

    @Override
    public void editGoodsCartNumNew(int i, RelativeLayout rl, GoodsBean bean) {

    }

    @Override
    public void editGoodCartNumActicon(int i, RelativeLayout rl, ActionBean actionBean) {

    }

    @Override
    public void editGoodsCartNum(final int i, final RelativeLayout rl,String ggpId) {
       /* DialogUtil.showEditCartNum(this, "加入购物车数量", "确定", "取消", list.get(i).getCartnum(),
                list.get(i).getGp_minnum(),list.get(i).getGp_addnum(), list.get(i).getGoodsStock(), new DialogUtil.MyCustomDialogListener() {
                    @Override
                    public void ok(String str) {
                        alterCart(i, list.get(i).getGoodsId(), list.get(i).getGpId(),
                                str, list.get(i).getSupplierid(), rl);
                    }

                    @Override
                    public void no() {
                    }
                });*/
    }

    @Override
    public void itemGoods(int position) {
        Intent intent = new Intent();
        intent.setClass(this, GoodsDetailActivity.class);
        intent.putExtra("gpId", list.get(position).getGgp_id());
        //intent.putExtra("cartNum", list.get(position).getCartnum());
        startActivity(intent);
    }

    /**
     * 修改购物车数量
     *
     * @param goodsId
     * @param goodsNum
     * @param supplierid
     */
    private void alterCart(final int position, final String goodsId, String gp_id, final String goodsNum, String supplierid, final RelativeLayout rl) {
        AsyncHttpUtil<CartBean> httpAddCart = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    Animation inFromLeft = null;
                    if (rl.getChildAt(0).getVisibility() == View.GONE && !goodsNum.equals("0")) {
                        rl.getChildAt(0).setVisibility(View.VISIBLE);
                        LinearLayout ll = (LinearLayout) rl.getChildAt(0);
                        TextView tv = (TextView) ll.getChildAt(1);
                        tv.setText(goodsNum);

                        inFromLeft = new TranslateAnimation(rl.getChildAt(0).getWidth(), 0, 0, 0);
                        inFromLeft.setDuration(400);
                        inFromLeft.setFillAfter(true);
                        rl.getChildAt(0).startAnimation(inFromLeft);
                    } else if (rl.getChildAt(0).getVisibility() == View.VISIBLE && goodsNum.equals("0")) {
                        LinearLayout ll = (LinearLayout) rl.getChildAt(0);
                        TextView tv = (TextView) ll.getChildAt(1);
                        tv.setText(goodsNum);

                        inFromLeft = new TranslateAnimation(0, rl.getChildAt(0).getWidth(), 0, 0);
                        inFromLeft.setDuration(400);
                        inFromLeft.setInterpolator(new AccelerateInterpolator());
                        rl.getChildAt(0).startAnimation(inFromLeft);
                        rl.getChildAt(0).setVisibility(View.GONE);
                    }

                    if (null != inFromLeft) {
                        inFromLeft.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //list.get(position).setCartnum(goodsNum);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    } else {
                        //list.get(position).setCartnum(goodsNum);

                        LinearLayout ll = (LinearLayout) rl.getChildAt(0);
                        TextView tv = (TextView) ll.getChildAt(1);
                        tv.setText(goodsNum);
                    }
                    mPurchaseCartTv.setText(bean.getTotalAmount());
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    EventBus.getDefault().post(new GoodsNumEvent(goodsId,goodsNum));
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
        /*httpAddCart.post(M_Url.addCart, L_RequestParams.
                alterCart(UserInfoUtils.getId(this), goodsId, gp_id, goodsNum, supplierid,list.get(position).getActivityId(),UserInfoUtils.getCenterShopId(this)), true, "请求中");
    */
    }

    /**
     * 接收购物车数量和价格
     *
     * @param event
     */
    public void onEventMainThread(CartEvent event) {
        mPurchaseCartTv.setText(event.getTotalAmount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
