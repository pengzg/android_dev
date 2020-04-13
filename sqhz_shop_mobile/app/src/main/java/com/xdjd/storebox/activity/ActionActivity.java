package com.xdjd.storebox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ActionGoodsAdapter;
import com.xdjd.storebox.adapter.listener.PurchaseGoodsListener;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ActionBean;
import com.xdjd.storebox.bean.CartBean;
import com.xdjd.storebox.bean.GoodsBean;
import com.xdjd.storebox.bean.GoodsCartBean;
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
import com.xdjd.view.popup.EditCartNumPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 活动界面
 * Created by lijipei on 2016/11/28.
 */

public class ActionActivity extends BaseActivity implements PurchaseGoodsListener,EditCartNumPopupWindow.editCartNumListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.action_img)
    ImageView mActionImg;
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

    /**
     * 购物车加减popup
     */
    private EditCartNumPopupWindow editCartPopup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
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
    private void selectEditCartPwScreen(){
        editCartPopup = new EditCartNumPopupWindow(this,this);
    }
    private void initView() {
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("活动");
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
        adapter.setData(list);
        loadCartPriceNum();
        selectEditCartPwScreen();//pop
    }


    private void loadData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ActionBean> httpUtil = new AsyncHttpUtil<>(this, ActionBean.class, new IUpdateUI<ActionBean>() {
            @Override
            public void updata(ActionBean bean) {
                if (bean.getRepCode().equals("00")) {

                    if (!"".equals(bean.getActivityCover()) || bean.getActivityCover() != null){
                        Glide.with(ActionActivity.this).load(bean.getActivityCover()).
                                placeholder(R.color.image_gray).into(mActionImg);
                    }

                    if (null != bean.getListData() && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        mViewHelper.showDataView();
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.pull_up_remind));
                            pageNo--;
                        }else if (mFlag == 0 || mFlag == 1) {
                            mViewHelper.showEmptyView("该活动商品已下架！");
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
        httpUtil.post(M_Url.getActivityGoods, L_RequestParams.getActivityGoods(
                UserInfoUtils.getId(this), activityId, String.valueOf(pageNo),UserInfoUtils.getStoreHouseId(this)), false);
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
                    Intent intent = new Intent(ActionActivity.this, MainActivity.class);
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
    }

    @Override
    public void minusCart(int i, RelativeLayout rl) {
    }

    @Override
    public void editGoodsCartNumNew(int i, RelativeLayout rl, GoodsBean bean) {
    }

    @Override
    public void editGoodsCartNum(final int i, final RelativeLayout rl,String ggpId) {
    }

    @Override
    public void editGoodCartNumActicon(int i, RelativeLayout rl, ActionBean actionBean) {
        queryGooodsCartNum(i,actionBean,rl);
    }

    @Override
    public void itemGoods(int position) {
        Intent intent = new Intent();
        intent.setClass(this, GoodsDetailActivity.class);
        intent.putExtra("gpId", list.get(position).getGgp_id());
        intent.putExtra("gpsId","");//价格方案id
        //intent.putExtra("cartNum", list.get(position).getCartnum());
        startActivity(intent);
    }

    @Override
    public void editCart(String num, String ggpId, String min_num) {
        if( num.equals(" ")){
            showToast("请输入订货数量！");
        }else{
                modifyCart(num,ggpId);
        }
    }

    /**
     * 查询商品在购物车中的数量
     * @param i
     * @param
     */
    private void queryGooodsCartNum(final int i, final ActionBean actionBean, final RelativeLayout rl){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(this, GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    editCartPopup.showPwScreenAction(rl,bean.getC_goods_num(),bean.getIsFavorite(),actionBean);
                }else{
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
        httpUtil.post(M_Url.queryGoodsNums,L_RequestParams.queryGoodsCarNum(UserInfoUtils.getId(this),actionBean.getGgp_id()
        ,UserInfoUtils.getStoreHouseId(this),""),false);
    }

    /**
     * 修改购物车
     * @param num
     * @param ggpId
     */
    private  void modifyCart(String num,String ggpId){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(ActionActivity.this, GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    showToast("已成功添加至购物车！");
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    editCartPopup.dismiss();
                }else{
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
        httpUtil.post(M_Url.editCartGoodNum,L_RequestParams.editCart(UserInfoUtils.getId(this),ggpId,num,UserInfoUtils.getStoreHouseId(this),""),false);
    }



    /**
     * 接收购物车数量和价格
     *
     * @param event
     */
    @SuppressWarnings("JavaDoc")
    public void onEventMainThread(CartEvent event) {
        mPurchaseCartTv.setText(event.getTotalAmount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
