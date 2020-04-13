package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RolloutGoodsSellCartAdapter;
import com.xdjd.distribution.adapter.RolloutGoodsSellCopyAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.RolloutGoodsFinishEvent;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.EaseTitleBar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/11
 *     desc   : 去铺货界面
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsSellActivity extends BaseActivity implements ExpandableListView.OnChildClickListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    PullToRefreshExpandableListView mPullScroll;
    @BindView(R.id.ll_clear_cart)
    LinearLayout mLlClearCart;
    @BindView(R.id.ll_cart)
    LinearLayout mLlCart;
    @BindView(R.id.rl_cart_listing)
    RelativeLayout mRlCartListing;
    @BindView(R.id.lv_rollout_cart)
    AnimatedExpandableListView mLvRolloutCart;
    @BindView(R.id.rl_cart)
    RelativeLayout mRlCart;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_cart_num)
    TextView mTvCartNum;
    @BindView(R.id.shell_layout)
    RelativeLayout mShellLayout;
    @BindView(R.id.iv_cart)
    ImageView mIvCart;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.ll_bottom_left)
    LinearLayout mLlBottomLeft;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    private UserBean userBean;

    //销售搜索出来的订单商品list
    public List<PHOrderDetailBean> listGoods = new ArrayList<>();
    //初始的商品列表
    public List<PHOrderDetailBean> listIntrinsicGoods = new ArrayList<>();
    private RolloutGoodsSellCopyAdapter adapter;
    private RolloutGoodsSellCartAdapter adapterAddGoods;
    //已添加的商品
    public List<PHOrderDetailBean> listAddGoods = new ArrayList<>();

    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;

    private VaryViewHelper mVaryViewHelper;
    private boolean isUnfold = true;//是否全部展开

    @Override
    protected int getContentView() {
        return R.layout.activity_rollout_goods_sell;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mTitleBar.setTitle("铺货销售");
        mTitleBar.leftBack(this);
        mTitleBar.setRightText(UIUtils.getString(R.string.unfold_all_order));
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnfold) {
                    isUnfold = false;
                    mTitleBar.setRightText(UIUtils.getString(R.string.pack_up_all_order));
                } else {
                    isUnfold = true;
                    mTitleBar.setRightText(UIUtils.getString(R.string.unfold_all_order));
                }
                //遍历所有group,将所有项设置成默认展开
                int groupCount = listGoods.size();
                for (int i = 0; i < groupCount; i++) {
                    if (isUnfold) {
                        mPullScroll.getRefreshableView().collapseGroup(i);
                    } else {
                        mPullScroll.getRefreshableView().expandGroup(i);
                    }
                }
            }
        });

        clientBean = UserInfoUtils.getClientInfo(this);
        userBean = UserInfoUtils.getUser(this);

        adapter = new RolloutGoodsSellCopyAdapter();
        mPullScroll.getRefreshableView().setAdapter(adapter);
        mPullScroll.getRefreshableView().setOnChildClickListener(this);

        mPullScroll.getRefreshableView().setGroupIndicator(null);//将一级菜单头部的图标去掉
        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.DISABLED);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {

            }
        });

        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mEtSearch.setSelection(mEtSearch.getText().length());
                    mEtSearch.addTextChangedListener(mTextWatcher);
                    if (mEtSearch.getText().length() > 0) {
                        mLlClear.setVisibility(View.VISIBLE);
                    } else {
                        mLlClear.setVisibility(View.GONE);
                    }
                } else {
                    mEtSearch.removeTextChangedListener(mTextWatcher);
                }
            }
        });

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mLlCart.getLayoutParams();
        lp.topMargin = (height / 4);
        mLlCart.setLayoutParams(lp);

        mVaryViewHelper = new VaryViewHelper(mPullScroll);

        initCartGoodsAdapter();
        initListener();
        phOrderDetailList();
    }

    private void initCartGoodsAdapter() {
        adapterAddGoods = new RolloutGoodsSellCartAdapter();
        mLvRolloutCart.setAdapter(adapterAddGoods);
        adapterAddGoods.setData(listAddGoods);
        adapterAddGoods.setOnChildListerer(new RolloutGoodsSellCartAdapter.OnChildListerer() {
            @Override
            public void onMaxPlus(int i, int i1, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum) {
                PHOrderDetailBean bean = listAddGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, true, null))
                    return;
                plusCalculation(i, mEtMaxNum);
                adapterAddGoods.getChild(i, i1).setMaxNum(mEtMaxNum.getText().toString());
                calculateCartPrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
            }

            @Override
            public void onMaxMinus(int i, int i1, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum) {
                minusCalculation(i, mEtMaxNum);
                adapterAddGoods.getChild(i, i1).setMaxNum(mEtMaxNum.getText().toString());
                calculateCartPrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
            }

            @Override
            public void onMinPlus(int i, int i1, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum) {
                PHOrderDetailBean bean = listAddGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, false, null))
                    return;
                plusCalculation(i, mEtMinNum);
                adapterAddGoods.getChild(i, i1).setMinNum(mEtMinNum.getText().toString());
                calculateCartPrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
            }

            @Override
            public void onMinMinus(int i, int i1, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum) {
                minusCalculation(i, mEtMinNum);
                adapterAddGoods.getChild(i, i1).setMinNum(mEtMinNum.getText().toString());
                calculateCartPrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
            }

            @Override
            public void onMaxEtNum(int i, int i1, String num, EditText mEtMaxNum, EditText mEtMinNum, String beforeStr) {
                String newStr = num.replaceFirst("^0*", "");
                PHOrderDetailBean bean = listAddGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, true, newStr)) {
                    //先去焦点,再进行显示
                    resetFocusable(mEtMaxNum, beforeStr);
                    return;
                }
                adapterAddGoods.getChild(i, i1).setMaxNum(newStr);
                calculateCartPrice(i, i1, mEtMaxNum, mEtMinNum);
            }

            @Override
            public void onMinEtNum(int i, int i1, String num, EditText mEtMaxNum, EditText mEtMinNum, String beforeStr) {
                String newStr = num.replaceFirst("^0*", "");
                PHOrderDetailBean bean = listAddGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, false, newStr)) {
                    resetFocusable(mEtMinNum, beforeStr);
                    return;
                }
                adapterAddGoods.getChild(i, i1).setMinNum(newStr);
                calculateCartPrice(i, i1, mEtMaxNum, mEtMinNum);
            }
        });
    }

    private void phOrderDetailList() {
        mVaryViewHelper.showLoadingView();
        AsyncHttpUtil<PHOrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, PHOrderDetailBean.class, new IUpdateUI<PHOrderDetailBean>() {
            @Override
            public void updata(PHOrderDetailBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        mVaryViewHelper.showDataView();
                        listIntrinsicGoods.addAll(jsonBean.getListData());
                        listGoods.addAll(jsonBean.getListData());
                        adapter.setData(listGoods);
                    } else {
                        mVaryViewHelper.showEmptyView();
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getRepMsg(), new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(), new MyErrorListener());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.phOrderDetailList, L_RequestParams.
                phOrderDetailList(clientBean.getCc_id(), "N"), false);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phOrderDetailList();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                mLlClear.setVisibility(View.VISIBLE);
            } else {
                mLlClear.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        /*final LinearLayout itemShowAddnum = (LinearLayout) v.findViewById(R.id.item_show_addnum);
        RelativeLayout rlAddNum = (RelativeLayout) v.findViewById(R.id.rl_add_cartnum);
        final ImageView ivMaxPlus,ivMaxMinus,ivMinPlus,ivMinMinus;
        ivMaxPlus= (ImageView) v.findViewById(R.id.iv_min_plus);
        ivMaxMinus= (ImageView) v.findViewById(R.id.iv_max_minus);
        ivMinPlus= (ImageView) v.findViewById(R.id.iv_min_plus);
        ivMinMinus= (ImageView) v.findViewById(R.id.iv_min_minus);
        rlAddNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemShowAddnum.getVisibility() == View.GONE) {
                    itemShowAddnum.setVisibility(View.VISIBLE);
                    Animation inFromLeft = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, +1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    inFromLeft.setDuration(200);
                    inFromLeft.setInterpolator(new AccelerateInterpolator());
                    itemShowAddnum.startAnimation(inFromLeft);
                } else {
                    Animation inFromLeft = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, +1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    inFromLeft.setDuration(200);
                    inFromLeft.setInterpolator(new AccelerateInterpolator());
                    itemShowAddnum.startAnimation(inFromLeft);
                    itemShowAddnum.setVisibility(View.GONE);
                }
            }
        });
        ivMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCarAnimation1(ivMaxPlus);
            }
        });*/
        return false;
    }

    private void initListener() {
        adapter.setOnChildListerer(new RolloutGoodsSellCopyAdapter.OnChildListerer() {
            @Override
            public void onMaxPlus(int i, int i1, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum) {
                PHOrderDetailBean bean = listGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, true, null))
                    return;
                plusCalculation(i, mEtMaxNum);
                adapter.getChild(i, i1).setMaxNum(mEtMaxNum.getText().toString());
                calculatePrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
            }

            @Override
            public void onMaxMinus(int i, int i1, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum) {
                minusCalculation(i, mEtMaxNum);
                adapter.getChild(i, i1).setMaxNum(mEtMaxNum.getText().toString());
                calculatePrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
            }

            @Override
            public void onMinPlus(int i, int i1, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum) {
                PHOrderDetailBean bean = listGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, false, null))
                    return;
                plusCalculation(i, mEtMinNum);
                adapter.getChild(i, i1).setMinNum(mEtMinNum.getText().toString());
                calculatePrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
            }

            @Override
            public void onMinMinus(int i, int i1, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum) {
                minusCalculation(i, mEtMinNum);
                adapter.getChild(i, i1).setMinNum(mEtMinNum.getText().toString());
                calculatePrice(i, i1, mEtMaxNum, mEtMinNum);
                AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
            }

            @Override
            public void onMaxEtNum(int i, int i1, String num, EditText mEtMaxNum, EditText mEtMinNum, String beforeStr) {
                String newStr = num.replaceFirst("^0*", "");
                PHOrderDetailBean bean = listGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, true, newStr)) {
                    //先去焦点,再进行显示
                    resetFocusable(mEtMaxNum, beforeStr);
                    return;
                }
                adapter.getChild(i, i1).setMaxNum(newStr);
                calculatePrice(i, i1, mEtMaxNum, mEtMinNum);
            }

            @Override
            public void onMinEtNum(int i, int i1, String num, EditText mEtMaxNum, EditText mEtMinNum, String beforeStr) {
                String newStr = num.replaceFirst("^0*", "");
                PHOrderDetailBean bean = listGoods.get(i).getListGoodsData().get(i1);
                if (isWarningNum(bean, false, newStr)) {
                    resetFocusable(mEtMinNum, beforeStr);
                    return;
                }
                adapter.getChild(i, i1).setMinNum(newStr);
                calculatePrice(i, i1, mEtMaxNum, mEtMinNum);
            }
        });
    }

    /**
     * 失去焦点
     *
     * @param et
     * @param str
     */
    private void resetFocusable(EditText et, String str) {
        et.setFocusable(false);
        if (str != null) {
            et.setText(str);
            et.setSelection(et.getText().length());
        }
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.findFocus();
    }

    /**
     * 是否超出商品未销售预警数量
     *
     * @param isMaxNum 是否是大单位--true:是单位,false:小单位
     * @param editNum  编辑框输入的价格,如果没有编辑则传入null
     */
    private boolean isWarningNum(PHOrderDetailBean bean, boolean isMaxNum, String editNum) {
        BigDecimal bdMaxNum;
        BigDecimal bdMinNum;
        BigDecimal bdUnitNum = null;
        BigDecimal residueNum;//未销售数量
        BigDecimal etNum;//编辑款传入的价格

        if (TextUtils.isEmpty(bean.getResidueNum())) {
            residueNum = BigDecimal.ZERO;
        } else {
            residueNum = new BigDecimal(bean.getResidueNum());
        }

        if (TextUtils.isEmpty(bean.getUnitNum())) {
            bdUnitNum = new BigDecimal("1");
        } else {
            bdUnitNum = new BigDecimal(bean.getUnitNum());
        }
        if (TextUtils.isEmpty(bean.getMaxNum())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(bean.getMaxNum());
        }
        if (TextUtils.isEmpty(bean.getMinNum())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(bean.getMinNum());
        }

        if (editNum == null) {
            //没有使用编辑框编辑价格
            if (isMaxNum) {
                bdMaxNum = bdMaxNum.add(BigDecimal.ONE);
            } else {
                bdMinNum = bdMinNum.add(BigDecimal.ONE);
            }
        } else {
            if (TextUtils.isEmpty(editNum)) {
                etNum = BigDecimal.ZERO;
            } else {
                etNum = new BigDecimal(editNum);
            }
            if (isMaxNum) {
                bdMaxNum = etNum;
            } else {
                bdMinNum = etNum;
            }
        }

        //计算商品数量
        BigDecimal totalNum = bdMaxNum.multiply(bdUnitNum).add(bdMinNum);
        if (totalNum.compareTo(residueNum) == 1) {
            showToast("已超出未销售商品数量");
            return true;
        }
        return false;
    }

    @OnClick({R.id.ll_clear_cart, R.id.rl_cart, R.id.rl_cart_listing, R.id.tv_submit, R.id.tv_search, R.id.ll_clear,R.id.ll_bottom_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_clear_cart://清空购物车
                DialogUtil.showCustomDialog(this, "提示", "是否清空已选商品?", "清空", "取消",
                        new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                listAddGoods.clear();
                                adapterAddGoods.notifyDataSetChanged();
                                mRlCartListing.setVisibility(View.GONE);
                                for (PHOrderDetailBean bean : listGoods) {
                                    for (int k = 0; k < bean.getListGoodsData().size(); k++) {
                                        bean.getListGoodsData().get(k).setMinNum("");
                                        bean.getListGoodsData().get(k).setMaxNum("");
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                mTvCartNum.setText("0");
                                mTvTotalPrice.setText("0.00");
                            }

                            @Override
                            public void no() {
                            }
                        });
                break;
            case R.id.ll_bottom_left:
            case R.id.rl_cart:
                if (listAddGoods.size() > 0) {
                    if (mRlCartListing.getVisibility() == View.VISIBLE) {
                        mRlCartListing.setVisibility(View.GONE);
                        return;
                    }
                    mRlCartListing.setVisibility(View.VISIBLE);
                    adapterAddGoods.notifyDataSetInvalidated();
                    int groupCount = listAddGoods.size();
                    for (int i = 0; i < groupCount; i++) {
                        if (mLvRolloutCart.isGroupExpanded(groupCount)) {
                            mLvRolloutCart.collapseGroup(i);
                        } else {
                            mLvRolloutCart.expandGroup(i);
                        }
                    }
                }
                break;
            case R.id.rl_cart_listing:
                mRlCartListing.setVisibility(View.GONE);
                break;
            case R.id.tv_submit:
                if (listAddGoods.size() == 0) {
                    showToast("请先添加销售的商品");
                    return;
                }
                Intent intent = new Intent(this, RolloutGoodsSubmitActivity.class);
                intent.putExtra("orderType", 2);//销售
                intent.putExtra("listSaleGoods", (Serializable) listAddGoods);
                startActivity(intent);
                break;
            case R.id.tv_search://搜索商品
                listGoods.clear();
                adapter.notifyDataSetChanged();
                if (TextUtils.isEmpty(mEtSearch.getText())) {
                    listGoods.addAll(listIntrinsicGoods);
                } else {
                    for (int i = 0; i < listIntrinsicGoods.size(); i++) {
                        List<PHOrderDetailBean> list = new ArrayList<>();
                        for (PHOrderDetailBean bean : listIntrinsicGoods.get(i).getListGoodsData()) {
                            if (bean.getGoodsName().indexOf(mEtSearch.getText().toString()) != -1) {
                                list.add(bean);
                            }
                        }
                        if (list.size() > 0) {
                            PHOrderDetailBean bean = (PHOrderDetailBean) listIntrinsicGoods.get(i).clone();
                            bean.setListGoodsData(list);
                            listGoods.add(bean);
                        }
                    }
                }
                for (int i = 0; i < listGoods.size(); i++) {
                    mPullScroll.getRefreshableView().expandGroup(i);
                }
                isUnfold = false;
                mTitleBar.setRightText(UIUtils.getString(R.string.pack_up_all_order));
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_clear:
                mEtSearch.setText("");
                listGoods.clear();
                listGoods.addAll(listIntrinsicGoods);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < listGoods.size(); i++) {
                    mPullScroll.getRefreshableView().expandGroup(i);
                }
                isUnfold = false;
                mTitleBar.setRightText(UIUtils.getString(R.string.pack_up_all_order));
                break;
        }
    }


    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(int i, EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        et.setText((bd.intValue() + 1) + "");

    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private void minusCalculation(int i, EditText et) {
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            if (num == 0) {
                et.setText("0");
            } else if (num - 1 == 0) {
                et.setText("0");
            } else {
                et.setText(num - 1 + "");
            }
        }
    }

    /**
     * 计算adapter中item商品价格
     */
    private void calculatePrice(int i, int i1, EditText mEtMaxNum, EditText mEtMinNum) {
        BigDecimal bdMaxPrice = null;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice = null;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格
        BigDecimal discount;//销售类型折扣率

        //Goodstype:1普通  2赠品
        if (adapter.getChild(i, i1).getGoodstype() != null && "1".equals(adapter.getChild(i, i1).getGoodstype()) ) {
            discount = BigDecimal.ONE;//销售类型折扣
        }else{
            discount = BigDecimal.ZERO;
        }

        if (TextUtils.isEmpty(adapter.getChild(i, i1).getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapter.getChild(i, i1).getMaxPrice());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapter.getChild(i, i1).getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapter.getChild(i, i1).getMinPrice());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        //计算折扣率
        bdMaxPrice = bdMaxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
        bdMinPrice = bdMinPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);

        String sumPrice;
        if ("1".equals(adapter.getChild(i, i1).getUnitNum())) {//大小单位换算比1==1,只显示小单位
            bdSumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP);
            sumPrice = bdSumPrice.toString();
            //                        adapter.getChild(i,i1).setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            //            adapter.getChild(i,i1).setTotalPrice(sumPrice);
        }

        if (bdMaxNum.compareTo(BigDecimal.ZERO) == 1 ||
                bdMinNum.compareTo(BigDecimal.ZERO) == 1) {
            boolean isOneOrder = false;//是否在已存在的订单中
            boolean isExist = false;//购物车中是否已经存在这件商品
            for (int l = 0; l < listAddGoods.size(); l++) {
                //判断是否在同一个订单
                if (listGoods.get(i).getOrderCode().equals(listAddGoods.get(l).getOrderCode())) {
                    isOneOrder = true;

                    List<PHOrderDetailBean> listGoodsData = listAddGoods.get(l).getListGoodsData();
                    for (int k = 0; k < listGoodsData.size(); k++) {
                        if (listGoodsData.get(k).getOad_id().equals(listGoods.get(i).getListGoodsData().get(i1).getOad_id())) {
                            isExist = true;
                            //                            listAddGoods.get(l).getListGoodsData().get(k).setNewMaxPrice(bdMaxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            //                            listAddGoods.get(l).getListGoodsData().get(k).setNewMinPrice(bdMinPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            listAddGoods.get(l).getListGoodsData().get(k).setMaxNum(bdMaxNum.toString());
                            listAddGoods.get(l).getListGoodsData().get(k).setMinNum(bdMinNum.toString());
                            listAddGoods.get(l).getListGoodsData().get(k).setTotalPrice(sumPrice);
                            break;
                        }
                    }

                    if (!isExist) {
                        PHOrderDetailBean beanGoods = (PHOrderDetailBean) listGoods.get(i).getListGoodsData().get(i1).clone();
                        beanGoods.setNewMaxPrice(bdMaxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        beanGoods.setNewMinPrice(bdMinPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        beanGoods.setMaxNum(bdMaxNum.toString());
                        beanGoods.setMinNum(bdMinNum.toString());
                        beanGoods.setTotalPrice(sumPrice);
                        listAddGoods.get(l).getListGoodsData().add(beanGoods);
                        break;
                    }
                }
            }

            if (!isOneOrder) {//如果商品订单不存在
                //订单bean
                PHOrderDetailBean beanOrder = new PHOrderDetailBean();
                beanOrder.setOrderCode(listGoods.get(i).getOrderCode());
                beanOrder.setTotalAmount(listGoods.get(i).getTotalAmount());
                beanOrder.setAddTime(listGoods.get(i).getAddTime());
                //商品列表list
                List<PHOrderDetailBean> listGoodsData = new ArrayList<>();
                PHOrderDetailBean beanGoods = (PHOrderDetailBean) listGoods.get(i).getListGoodsData().get(i1).clone();
                beanGoods.setNewMaxPrice(bdMaxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                beanGoods.setNewMinPrice(bdMinPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                beanGoods.setMaxNum(bdMaxNum.toString());
                beanGoods.setMinNum(bdMinNum.toString());
                beanGoods.setTotalPrice(sumPrice);
                listGoodsData.add(beanGoods);
                beanOrder.setListGoodsData(listGoodsData);

                listAddGoods.add(beanOrder);
            }
        } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 &&
                bdMinNum.compareTo(BigDecimal.ZERO) == 0) {

            for (int l = 0; l < listAddGoods.size(); l++) {
                //判断是否在同一个订单
                if (listGoods.get(i).getOrderCode().equals(listAddGoods.get(l).getOrderCode())) {
                    List<PHOrderDetailBean> listGoodsData = listAddGoods.get(l).getListGoodsData();
                    for (int k = 0; k < listGoodsData.size(); k++) {
                        if (listGoodsData.get(k).getOad_id().equals(listGoods.get(i).getListGoodsData().get(i1).getOad_id())) {
                            listAddGoods.get(l).getListGoodsData().remove(k);
                            if (listAddGoods.get(l).getListGoodsData().size() == 0) {
                                listAddGoods.remove(l);
                                //                                adapter.notifyDataSetInvalidated();
                            }
                            break;
                        }
                    }
                }
            }
        }
        priceAndNum();
    }

    /**
     * 计算购物车adapter中item商品价格
     */
    private void calculateCartPrice(int i, int i1, EditText mEtMaxNum, EditText mEtMinNum) {
        BigDecimal bdMaxPrice = null;
        BigDecimal bdMaxNum;
        BigDecimal bdMinPrice = null;
        BigDecimal bdMinNum;
        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格
        BigDecimal discount;//销售类型折扣率

        //Goodstype:1普通  2赠品
        if (adapterAddGoods.getChild(i, i1).getGoodstype() != null && "1".equals(adapterAddGoods.getChild(i, i1).getGoodstype()) ) {
            discount = BigDecimal.ONE;//销售类型折扣
        }else{
            discount = BigDecimal.ZERO;
        }

        if (TextUtils.isEmpty(adapterAddGoods.getChild(i, i1).getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapterAddGoods.getChild(i, i1).getMaxPrice());
        }
        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }
        if (TextUtils.isEmpty(adapterAddGoods.getChild(i, i1).getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapterAddGoods.getChild(i, i1).getMinPrice());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        //计算折扣率
        bdMaxPrice = bdMaxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
        bdMinPrice = bdMinPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);

        String sumPrice;
        if ("1".equals(adapterAddGoods.getChild(i, i1).getUnitNum())) {//大小单位换算比1==1,只显示小单位
            bdSumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP);
            sumPrice = bdSumPrice.toString();
            adapterAddGoods.getChild(i, i1).setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            adapterAddGoods.getChild(i, i1).setTotalPrice(sumPrice);
        }

        if (bdMaxNum.compareTo(BigDecimal.ZERO) == 1 ||
                bdMinNum.compareTo(BigDecimal.ZERO) == 1) {
            for (int l = 0; l < listGoods.size(); l++) {
                //判断是否在同一个订单
                if (listAddGoods.get(i).getOrderCode().equals(listGoods.get(l).getOrderCode())) {
                    List<PHOrderDetailBean> listGoodsData = listGoods.get(l).getListGoodsData();
                    for (int k = 0; k < listGoodsData.size(); k++) {
                        if (listGoodsData.get(k).getOad_id().equals(listAddGoods.get(i).getListGoodsData().get(i1).getOad_id())) {
                            listGoods.get(l).getListGoodsData().get(k).setMaxNum(bdMaxNum.toString());
                            listGoods.get(l).getListGoodsData().get(k).setMinNum(bdMinNum.toString());
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 &&
                bdMinNum.compareTo(BigDecimal.ZERO) == 0) {
            for (int l = 0; l < listGoods.size(); l++) {
                //判断是否在同一个订单
                if (listAddGoods.get(i).getOrderCode().equals(listGoods.get(l).getOrderCode())) {
                    List<PHOrderDetailBean> listGoodsData = listGoods.get(l).getListGoodsData();
                    for (int k = 0; k < listGoodsData.size(); k++) {
                        if (listGoodsData.get(k).getOad_id().equals(listAddGoods.get(i).getListGoodsData().get(i1).getOad_id())) {
                            listGoods.get(l).getListGoodsData().get(k).setMaxNum("");
                            listGoods.get(l).getListGoodsData().get(k).setMinNum("");
                            adapter.notifyDataSetChanged();
                            listAddGoods.get(i).getListGoodsData().remove(i1);
                            break;
                        }
                    }
                }
            }

            if (listAddGoods.get(i).getListGoodsData().size() == 0) {
                listAddGoods.remove(i);
            }
            if (listAddGoods.size() == 0) {
                mRlCartListing.setVisibility(View.GONE);
            }
            adapterAddGoods.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
        priceAndNum();
    }

    private void priceAndNum() {
        int goodsNum = 0;
        BigDecimal sum1 = BigDecimal.ZERO;
        if (listAddGoods.size() > 0) {
            for (PHOrderDetailBean bean : listAddGoods) {
                for (PHOrderDetailBean bean1 : bean.getListGoodsData()) {
                    BigDecimal bdPrice = new BigDecimal(bean1.getTotalPrice());
                    sum1 = sum1.add(bdPrice);
                }

                goodsNum += bean.getListGoodsData().size();
            }
            mTvTotalPrice.setText(sum1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvTotalPrice.setText("0.00");
        }
        mTvCartNum.setText(goodsNum + "");
    }

    private void addToCarAnimation1(ImageView goodsImg) {
        final ImageView animImg = new ImageView(this);
        animImg.setImageDrawable(goodsImg.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        mShellLayout.addView(animImg, params);
        //
        final int shellLocation[] = new int[2];
        mShellLayout.getLocationInWindow(shellLocation);
        int animImgLocation[] = new int[2];
        goodsImg.getLocationInWindow(animImgLocation);
        int carLocation[] = new int[2];
        mTvCartNum.getLocationInWindow(carLocation);
        //
        float startX = animImgLocation[0] - shellLocation[0] + goodsImg.getWidth() / 2;
        float startY = animImgLocation[1] - shellLocation[1] + goodsImg.getHeight() / 2;

        float endX = carLocation[0] - shellLocation[0] + mTvCartNum.getWidth() / 5;
        float endY = carLocation[1] - shellLocation[1];

        //控制点，控制贝塞尔曲线
        float ctrlX = (startX + endX) / 2;
        float ctrlY = startY - 100;

        Log.e("num", "-------->" + ctrlX + " " + startY + " " + ctrlY + " " + endY);

        Path path = new Path();
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线
        path.quadTo(ctrlX, ctrlY, endX, endY);
        mPathMeasure = new PathMeasure(path, false);

        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(animImg, "scaleX", 1, 0.5f, 0.2f);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(animImg, "scaleY", 1, 0.5f, 0.2f);


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                animImg.setTranslationX(mCurrentPosition[0]);
                animImg.setTranslationY(mCurrentPosition[1]);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTvCartNum.setText("99+");

                mShellLayout.removeView(animImg);
                shopCarAnim1();

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(scaleXanim, scaleYanim, valueAnimator);
        animatorSet.start();
    }

    private void shopCarAnim1() {
        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(mIvCart, "scaleX", 1.3f, 1.2f, 1);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(mIvCart, "scaleY", 1.3f, 1.2f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(scaleXanim, scaleYanim);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RolloutGoodsFinishEvent event) {//销毁界面
        finishActivity();
    }
}
