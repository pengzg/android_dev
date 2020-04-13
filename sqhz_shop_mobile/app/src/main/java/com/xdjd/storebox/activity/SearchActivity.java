package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.GoodsAdapter;
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
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.utils.runtimepermissions.PermissionUtils;
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
 * 搜索商品界面
 * Created by lijipei on 2016/12/12.
 */

public class SearchActivity extends BaseActivity implements PurchaseGoodsListener,EditCartNumPopupWindow.editCartNumListener{
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_ll)
    LinearLayout mSearchLl;
    @BindView(R.id.search_btn)
    Button mSearchBtn;
    @BindView(R.id.search_gridview)
    NoScrollGridView mSearchGridview;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.purchase_cart_tv)
    TextView mPurchaseCartTv;
    @BindView(R.id.purchase_cart_rl)
    RelativeLayout mPurchaseCartRl;
    @BindView(R.id.search_del)
    LinearLayout mSearchDel;

    private VaryViewHelper mViewHelper = null;

    private int pageNo = 1;
    private int mFlag = 0;

    /**
     * 商品数据列表
     */
    List<GoodsBean> list = new ArrayList<>();
    private GoodsAdapter adapter;

    private String result;//扫描的结果

    /**
     * 购物车加减popup
     */
    private EditCartNumPopupWindow editCartPopup;
    private void selectEditCartPwScreen(){
        editCartPopup = new EditCartNumPopupWindow(this,this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        loadCartPriceNum();
        selectEditCartPwScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (list.size() != 0) {
            mFlag = 1;
            pageNo = 1;
            list.clear();
            adapter.notifyDataSetChanged();
            loadData(PublicFinal.FIRST, false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        result = intent.getStringExtra("result");
        if (result != null && !result.equals("")) {
            mSearchEdit.setText(result);
            mSearchEdit.setSelection(mSearchEdit.getText().length());//光标移动到文本行

            loadData(PublicFinal.FIRST, true);
        }else{
            mSearchDel.setVisibility(View.GONE);
        }
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("搜索商品");
        mTitleBar.setRightImageResource(R.drawable.icon_scan_red);
        mTitleBar.showRightImage();
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.requstActivityCamera(SearchActivity.this, 1000 , new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent1 = new Intent(SearchActivity.this, CaptureActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                    }
                    @Override
                    public void onDilogCancal() {}
                });
            }
        });

        mViewHelper = new VaryViewHelper(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                list.clear();
                loadData(PublicFinal.TWO, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO, false);
            }
        });

        adapter = new GoodsAdapter(this);
        mSearchGridview.setAdapter(adapter);

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mSearchDel.setVisibility(View.VISIBLE);
                } else {
                    mSearchDel.setVisibility(View.GONE);
                    list.clear();
                    adapter.notifyDataSetChanged();
                    mViewHelper.showDataView();
                }
            }
        });

        result = getIntent().getStringExtra("result");
        if (result != null && !result.equals("")) {
            mSearchEdit.setText(result);
            mSearchEdit.setSelection(mSearchEdit.getText().length());//光标移动到文本行

            loadData(PublicFinal.FIRST, true);
        }else{
            mSearchDel.setVisibility(View.GONE);
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
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
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

    @OnClick({R.id.search_ll, R.id.search_btn, R.id.purchase_cart_rl,R.id.search_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_ll:
                break;
            case R.id.search_btn:
                if (mSearchEdit.getText().length() == 0) {
                    showToast("搜索条件不能为空");
                    return;
                }
                list.clear();
                pageNo = 1;
                mFlag = 0;
                loadData(PublicFinal.FIRST, true);
                break;
            case R.id.purchase_cart_rl:
                startActivity(CartActivity.class);
                break;
            case R.id.search_del://删除搜索的内容
                mSearchEdit.setText("");
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    private void loadData(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getListData() && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        adapter.setData(list);
                        mViewHelper.showDataView();
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.pull_up_remind));
                            pageNo--;
                        } else if (mFlag == 0 || mFlag == 1) {
                            showToast("没有搜索结果！");
                            mViewHelper.showEmptyView("没有搜索结果");
                        }
                    }
                    adapter.notifyDataSetChanged();
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.searchGoodsList, L_RequestParams.
                searchGoodsList(UserInfoUtils.getId(this), String.valueOf(pageNo),
                        mSearchEdit.getText().toString(),UserInfoUtils.getStoreHouseId(this)), isDialog);
    }

    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST, false);
            loadCartPriceNum();
        }
    }

    @Override
    public void plusCart(int i, RelativeLayout rl) {
    }

    @Override
    public void minusCart(int i, RelativeLayout rl) {
    }

    @Override
    public void editGoodsCartNumNew(int i, RelativeLayout rl, GoodsBean bean) {
        queryGooodsCartNum(i,bean,rl);
    }

    @Override
    public void editGoodCartNumActicon(int i, RelativeLayout rl, ActionBean actionBean) {
    }

    @Override
    public void editGoodsCartNum(final int i, final RelativeLayout rl,String ggpId) {
    }

    @Override
    public void editCart(String num, String ggpId, String min_num) {
        if( num.equals(" ")){
            showToast("请输入订货数量！");
        }else{
            modifyCart(num,ggpId);
        }
    }

    @Override
    public void itemGoods(int position) {
        Intent intent = new Intent();
        intent.setClass(this, GoodsDetailActivity.class);
        intent.putExtra("gpId", list.get(position).getGgp_id());
        intent.putExtra("cartNum", list.get(position).getCartnum());
        startActivityForResult(intent, 1100);
    }

    /**
     * 查询商品在购物车中的数量
     * @param i
     * @param
     */
    private void queryGooodsCartNum(final int i, final GoodsBean goodsBean, final RelativeLayout rl){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(this, GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    editCartPopup.showPwScreen(rl,bean.getC_goods_num(),bean.getIsFavorite(),goodsBean);
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
        httpUtil.post(M_Url.queryGoodsNums,L_RequestParams.queryGoodsCarNum(UserInfoUtils.getId(this),goodsBean.getGgp_id(),UserInfoUtils.getStoreHouseId(this),""),false);
    }

    /**
     * 修改购物车
     * @param num
     * @param ggpId
     */
    private  void modifyCart(String num,String ggpId){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(SearchActivity.this, GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    // showToast(bean.getRepMsg());
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
    public void onEventMainThread(CartEvent event) {
        mPurchaseCartTv.setText(event.getTotalAmount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
