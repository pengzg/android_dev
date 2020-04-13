package com.xdjd.distribution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.CustomerInventoryActivity;
import com.xdjd.distribution.adapter.GoodsBuyListingAdapter;
import com.xdjd.distribution.adapter.GoodsCategotyAdapter;
import com.xdjd.distribution.adapter.GoodsListAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.EditTextUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/10
 *     desc   : 订单申报 -- 商品编辑界面
 *     version: 1.0
 * </pre>
 */

public class InventoryEditFragment extends BaseFragment implements GoodsListAdapter.GoodsListListener, ItemOnListener,
        ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener {


    @BindView(R.id.horizontal_scroll)
    HorizontalScrollView mHorizontalScroll;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.ll_horizontal)
    LinearLayout mLlHorizontal;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.et_max_num)
    EditText mEtMaxNum;
    @BindView(R.id.unit_max_nameref)
    TextView mUnitMaxNameref;
    @BindView(R.id.ll_max_num)
    LinearLayout mLlMaxNum;
    @BindView(R.id.et_max_price)
    EditText mEtMaxPrice;
    @BindView(R.id.rl_max_price)
    RelativeLayout mRlMaxPrice;
    @BindView(R.id.et_min_num)
    EditText mEtMinNum;
    @BindView(R.id.unit_min_nameref)
    TextView mUnitMinNameref;
    @BindView(R.id.ll_min_num)
    LinearLayout mLlMinNum;
    @BindView(R.id.et_min_price)
    EditText mEtMinPrice;
    @BindView(R.id.rl_min_price)
    RelativeLayout mRlMinPrice;
    @BindView(R.id.et_sum_price)
    EditText mEtSumPrice;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_complete)
    Button mBtnComplete;
    @BindView(R.id.lv_goods_buy_listing)
    ListView mLvGoodsBuyListing;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.elv_goods_category)
    AnimatedExpandableListView mElvGoodsCategory;
    @BindView(R.id.iv_max_minus)
    ImageView mIvMaxMinus;
    @BindView(R.id.ll_max_left)
    LinearLayout mLlMaxLeft;
    @BindView(R.id.iv_max_plus)
    ImageView mIvMaxPlus;
    @BindView(R.id.rl_max)
    RelativeLayout mRlMax;
    @BindView(R.id.iv_min_minus)
    ImageView mIvMinMinus;
    @BindView(R.id.ll_min_left)
    LinearLayout mLlMinLeft;
    @BindView(R.id.iv_min_plus)
    ImageView mIvMinPlus;
    @BindView(R.id.rl_min)
    RelativeLayout mRlMin;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.rl_calendar)
    RelativeLayout mRlCalendar;
    @BindView(R.id.et_display)
    EditText mEtDisplay;
    @BindView(R.id.et_duitou)
    EditText mEtDuitou;
    @BindView(R.id.check_all_goods)
    CheckBox mCheckAllGoods;
    @BindView(R.id.ll_all_goods)
    LinearLayout mLlAllGoods;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private View view;

    private CustomerInventoryActivity context;

    /**
     * 一二级分类集合
     */
    private List<GoodsCategoryBean> listCategory = new ArrayList<>();

    /**
     * 商品列表集合
     */
    private List<GoodsBean> listGoods = new ArrayList<>();

    /**
     * 客户盘点
     */
    public List<GoodsBean> listInventry = new ArrayList<>();

    /**
     * 商品类别adapter
     */
    private GoodsCategotyAdapter adapterGoodsCategory;


    private GoodsListAdapter adapterGoods;
    private GoodsBuyListingAdapter adapterBuyList;

    /**
     * 选中商品的bean
     */
    private GoodsBean beanGoods;
    private UserBean userBean;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    /**
     * 销售类型bean
     */
    private SaleTypeBean saleTypeBean = null;

    /**
     * 选中分类的code
     */
    private String categoryCode;
    /**
     * 商品类别type--4重点  5推荐 6我常买 7促销
     */
    private String categoryType = "";

    /**
     * 商品页数
     */
    private int page = 1;
    private int mFlag = 0;

    //日历选择
    private TimePickerUtil pvTime;

    private EditText editText;

    private CustomerInventoryActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_inventory, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mActivity = (CustomerInventoryActivity) getActivity();

        userBean = UserInfoUtils.getUser(getActivity());
        clientBean = UserInfoUtils.getClientInfo(getActivity());

        context = (CustomerInventoryActivity) getActivity();
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mLlHorizontal.getLayoutParams();
        layoutParams.width = width + (width / 3) * 1;
        mLlHorizontal.setLayoutParams(layoutParams);

        listInventry = context.listInventry;

        adapterGoodsCategory = new GoodsCategotyAdapter();
        mElvGoodsCategory.setAdapter(adapterGoodsCategory);
        mElvGoodsCategory.setOnGroupExpandListener(this);
        mElvGoodsCategory.setOnChildClickListener(this);
        mElvGoodsCategory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //设置扩展动画
                if (mElvGoodsCategory.isGroupExpanded(groupPosition)) {
                    mElvGoodsCategory.collapseGroupWithAnimation(groupPosition);
                } else {
                    mElvGoodsCategory.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        mTvSubmit.setText("提交");

        adapterGoods = new GoodsListAdapter(this);
        mLvGoods.setAdapter(adapterGoods);

        adapterBuyList = new GoodsBuyListingAdapter(this);
        mLvGoodsBuyListing.setAdapter(adapterBuyList);

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setRightLayout(false);
        initRefresh(mPullScroll);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                listGoods.clear();
                adapterGoods.notifyDataSetInvalidated();
                getGoodsList("");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getGoodsList("");
            }
        });

        pvTime = new TimePickerUtil();
        pvTime.initTimePicker(getActivity(), new TimePickerUtil.OnTimePickerOneListener() {
            @Override
            public void onDateStr(String dateStr) {
                mTvDate.setText(dateStr);
            }
        });

        queryGsCategory();
        setTextWatcher(mEtMaxNum);
        setTextWatcher(mEtMinNum);
        setTextWatcher(mEtMaxPrice);
        setTextWatcher(mEtMinPrice);
        mEtSumPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEtSumPrice.setSelectAllOnFocus(true);
                    mEtSumPrice.addTextChangedListener(totalPriceWatcher);
                } else {
                    mEtSumPrice.removeTextChangedListener(totalPriceWatcher);
                }
            }
        });

    }

    private void setTextWatcher(final EditText et) {
        //是否有焦点
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et.setSelectAllOnFocus(true);
                    editText = et;
                    et.addTextChangedListener(textWatcher);
                } else {
                    et.removeTextChangedListener(textWatcher);
                }
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (beanGoods == null)
                return;

            BigDecimal bdMaxPrice;
            BigDecimal bdMaxNum;

            BigDecimal bdMinPrice;
            BigDecimal bdMinNum;

            BigDecimal bdUnitNum = null;

            if (TextUtils.isEmpty(beanGoods.getGgp_unit_num())) {
                bdUnitNum = new BigDecimal("1");
            } else {
                bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());
            }

            if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
                bdMaxPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
            }

            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
            }

            if (TextUtils.isEmpty(mEtMinPrice.getText())) {
                bdMinPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
            }
            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
            }

            if (editText == mEtMaxPrice) {
                String minPrice = bdMaxPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtMinPrice.setText(minPrice);
            } else if (editText == mEtMinPrice) {
                if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                    mEtMaxPrice.setText(editable.toString());
                } else {
                    String maxPrice = bdUnitNum.multiply(bdMinPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                }
            }
            calculatePrice();
        }
    };

    /**
     * 合计金额监听事件
     */
    private TextWatcher totalPriceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtSumPrice.setText(s);
                    mEtSumPrice.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtSumPrice.setText(s);
                mEtSumPrice.setSelection(2);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            priceMatrixing();
        }
    };

    @OnClick({R.id.iv_max_minus, R.id.iv_max_plus, R.id.iv_min_minus, R.id.iv_min_plus, R.id.btn_cancel,
            R.id.btn_complete, R.id.tv_submit, R.id.rl_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_max_minus://大单位减商品数量
                notSelectGoodsHint();
                minusCalculation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
                break;
            case R.id.iv_max_plus:
                notSelectGoodsHint();
                plusCalculation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
                break;
            case R.id.iv_min_minus://小单位减商品数量
                notSelectGoodsHint();
                minusCalculation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
                break;
            case R.id.iv_min_plus:
                notSelectGoodsHint();
                plusCalculation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
                break;
            case R.id.btn_cancel:
                if (beanGoods != null) { //已有选中的商品时
                    if (!"".equals(beanGoods.getIndex()) && beanGoods.getIndex() != null) {
                        showToast(beanGoods.getGg_title() + "删除成功");
                        adapterBuyList.list.remove(Integer.parseInt(beanGoods.getIndex()));
                        adapterBuyList.setIndex(-1);
                        adapterBuyList.notifyDataSetChanged();
                    }
                    beanGoods = null;
                    addBuyGoods(beanGoods);
                }

                clearGoodsEdit();
                beanGoods = null;
                break;
            case R.id.btn_complete:
                notSelectGoodsHint();
                if (beanGoods != null && (TextUtils.isEmpty(mEtMaxNum.getText()) || "0".equals(mEtMaxNum.getText().toString()))
                        && (TextUtils.isEmpty(mEtMinNum.getText()) || "0".equals(mEtMinNum.getText().toString()))) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请输入产品数量!", "确定", null, null);
                    return;
                }
                if (addGoods()) {
                    adapterBuyList.notifyDataSetChanged();
                }
                break;
            case R.id.tv_submit:
                context.toApplyFragment();
                break;
            case R.id.rl_calendar://选择日历
                if (beanGoods != null) {
                    if (!TextUtils.isEmpty(mTvDate.getText())) {
                        pvTime.showTimePicker(mActivity.getLlMain(), mTvDate.getText().toString());
                    } else {
                        pvTime.showTimePicker(mActivity.getLlMain(),DateUtils.getDate2());
                    }
                }
                break;
        }
    }

    /**
     * 没有选中商品操作提示
     */
    private void notSelectGoodsHint() {
        if (beanGoods == null)
            DialogUtil.showCustomDialog(getActivity(), "提示", "您还没有选择商品,请选择商品后再进行操作!", "确定", null, null);
    }

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(EditText et, LinearLayout llLeft, RelativeLayout rlMain, ImageView ivPlus) {
        if (beanGoods == null) {
            return;
        }
        BigDecimal bd;
        if (TextUtils.isEmpty(et.getText())) {
            bd = new BigDecimal(0);
        } else {
            bd = new BigDecimal(et.getText().toString());
        }
        final String numStr = (bd.intValue() + 1) + "";
        et.setText(numStr);
        AnimUtils.setTranslateAnimation(et, llLeft, rlMain, ivPlus);
        if (et.hasFocus()) {//有焦点
            et.setSelection(String.valueOf((bd.intValue() + 1)).length());
        } else {
            //没有焦点
            calculatePrice();
        }
    }

    /**
     * 数量减少计算
     *
     * @param et
     */
    private void minusCalculation(final EditText et, LinearLayout llLeft, RelativeLayout rlMain, ImageView ivPlus) {
        if (beanGoods == null) {
            return;
        }
        BigDecimal bd;
        final String numStr;
        if (TextUtils.isEmpty(et.getText())) {
            return;
        } else {
            bd = new BigDecimal(et.getText().toString());
            int num = bd.intValue();
            if (num == 0) {
                numStr = "0";
            } else if (num - 1 == 0) {
                numStr = "0";
            } else {
                numStr = num - 1 + "";
            }

            et.setText(numStr);
            AnimUtils.setTranslateAnimation(et, llLeft, rlMain, ivPlus);
            if (et.hasFocus()) {
                et.setSelection(numStr.length());
            } else {
                calculatePrice();
            }
        }
    }

    /**
     * 获取分类
     */
    private void queryGsCategory() {
        AsyncHttpUtil<GoodsCategoryBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCategoryBean.class, new IUpdateUI<GoodsCategoryBean>() {
            @Override
            public void updata(GoodsCategoryBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listCategory = jsonBean.getListData();

                        adapterGoodsCategory.setData(listCategory);
                    } else {
                        listCategory = new ArrayList<>();
                    }
                    GoodsCategoryBean cateTwo = new GoodsCategoryBean();
                    cateTwo.setGc_id("00");
                    cateTwo.setGc_name("查询结果");
                    List<GoodsCategoryBean> listCateTwo = new ArrayList<>();
                    listCateTwo.add(cateTwo);

                    GoodsCategoryBean cateOne = new GoodsCategoryBean();
                    cateOne.setGc_name("扫码/查询");
                    cateOne.setGc_id("00");
                    cateOne.setSecondCategoryList(listCateTwo);
                    listCategory.add(0, cateOne);

                    if (listCategory != null && listCategory.size() > 0) {
                        onGroupExpand(1);
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.queryGsCategory, L_RequestParams.
                queryGsCategory(userBean.getUserId(), clientBean.getCc_id(), "", "1", ""), true);
    }

    /**
     * 获取商品列表
     */
    private void getGoodsList(String searchKey) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listGoods.addAll(jsonBean.getListData());
                        adapterGoods.setData(listGoods);
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getGoodsList, L_RequestParams.
                getGoodsList(userBean.getUserId(), clientBean.getCc_id(),
                        "", searchKey, categoryCode, "", "4", String.valueOf(page), "",categoryType), true);
    }

    /**
     * 价格换算
     */
    private void priceMatrixing() {
        if (beanGoods != null) {
            BigDecimal bdMaxPrice;
            BigDecimal bdMaxNum;

            BigDecimal bdMinPrice;
            BigDecimal bdMinNum;

            BigDecimal bdSumPrice = null;
            BigDecimal bdUnitNum = null;//换算单位

            BigDecimal bdSumNum = null;

            if (TextUtils.isEmpty(mEtSumPrice.getText()) ||
                    !Validation.isNumericAndExceedZero(mEtSumPrice.getText().toString())) {
                mEtMinPrice.setText("0");
                mEtMaxPrice.setText("0");
                return;
            }

            if (TextUtils.isEmpty(beanGoods.getGgp_unit_num())) {
                bdUnitNum = new BigDecimal("1");
            } else {
                bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());
            }

            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
            }

            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
            }

            bdSumPrice = new BigDecimal(mEtSumPrice.getText().toString());//总价格

            if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                //如果小单位数量>0
                if (bdMinNum.compareTo(BigDecimal.ZERO) == 1) {
                    String minPrice = bdSumPrice.divide(bdMinNum, 2, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();

                    mEtMaxPrice.setText(minPrice);
                    mEtMinPrice.setText(minPrice);
                } else {//小单位数量==0
                    mEtMaxPrice.setText(sumPrice);
                    mEtMinPrice.setText(sumPrice);
                }
            } else {
                //如果大单位数量>0,小单数量==0
                if (bdMaxNum.compareTo(BigDecimal.ZERO) == 1 && bdMinNum.compareTo(BigDecimal.ZERO) == 0) {
                    BigDecimal maxPrice = bdSumPrice.divide(bdMaxNum).setScale(2, BigDecimal.ROUND_HALF_UP);
                    String minPrice = maxPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice.toString());
                    mEtMinPrice.setText(minPrice);
                } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 && bdMinNum.compareTo(BigDecimal.ZERO) == 0) {//如果大单位数量==0,小单数量==0
                    //以大单位价格为首
                    String maxPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    String minPrice = bdSumPrice.divide(bdUnitNum, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                    mEtMinPrice.setText(minPrice);
                } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 0 && bdMinNum.compareTo(BigDecimal.ZERO) == 1) {//如果大单位数量==0,小单数量>0
                    String maxPrice = bdSumPrice.divide(bdMinNum, 2, BigDecimal.ROUND_HALF_UP).multiply(bdUnitNum)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    String minPrice = bdSumPrice.divide(bdMinNum, 2, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                    mEtMinPrice.setText(minPrice);
                } else if (bdMaxNum.compareTo(BigDecimal.ZERO) == 1 && bdMinNum.compareTo(BigDecimal.ZERO) == 1) {//如果大单位数量>0,小单数量>0
                    //将数量先换算成小单位在进行价格计算
                    bdSumNum = bdUnitNum.multiply(bdMaxNum).add(bdMinNum);
                    String maxPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP).multiply(bdUnitNum)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    String minPrice = bdSumPrice.divide(bdSumNum, 2, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    mEtMaxPrice.setText(maxPrice);
                    mEtMinPrice.setText(minPrice);
                }
            }
        }
    }

    /**
     * 保证listview只展开一项
     *
     * @param groupPosition
     */
    @Override
    public void onGroupExpand(int groupPosition) {
        if (addGoods()) {
            adapterGoodsCategory.index = groupPosition;
            adapterGoodsCategory.index2 = 0;
        } else {
            return;
        }

        for (int i = 0; i < listCategory.size(); i++) {
            if (i != groupPosition) {
                mElvGoodsCategory.collapseGroup(i);
            }
        }

        listGoods.clear();
        adapterGoods.notifyDataSetChanged();
        if (listCategory.get(groupPosition).getSecondCategoryList() != null &&
                listCategory.get(groupPosition).getSecondCategoryList().size() > 0) {
            onChildClick(mElvGoodsCategory, mElvGoodsCategory.getChildAt(groupPosition),
                    groupPosition, 0, adapterGoodsCategory.getChildId(groupPosition, 0));
        }else{
            categoryType = listCategory.get(groupPosition).getType();
            if (categoryType == null || "".equals(categoryType)){
                return;
            }
            //4重点  5推荐 6我常买 7促销
            if ("4".equals(categoryType) || "5".equals(categoryType)|| "6".equals(categoryType)
                    || "7".equals(categoryType)){
                categoryCode = listCategory.get(groupPosition).getGc_code();
                getGoodsList("");
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

        if (addGoods()) {
            adapterGoodsCategory.index2 = i1;
            adapterGoodsCategory.notifyDataSetChanged();
        } else {
            return false;
        }

        mFlag = 1;
        page = 1;
        listGoods.clear();
        adapterGoods.notifyDataSetChanged();

        List<GoodsCategoryBean> secondCategoryList =
                listCategory.get(i).getSecondCategoryList();
        if (!"00".equals(secondCategoryList.get(i1).getGc_id())) {
            categoryCode = secondCategoryList.get(i1).getGc_code();
            categoryType = secondCategoryList.get(i1).getType();
            //00代表扫码/查询
            getGoodsList("");
        } else {
            categoryCode = "";
        }

        return false;
    }

    @Override
    public void onItemGoods(int i) {
        adapterBuyList.setIndex(-1);
        if (addGoods()) {
            adapterGoods.setIndex(i);
        } else {
            return;
        }
        mHorizontalScroll.scrollTo(mHorizontalScroll.getWidth(), 0);

        beanGoods = (GoodsBean) listGoods.get(i).clone();

        if (!"".equals(beanGoods.getSaleTypeDiscount()) && beanGoods.getSaleTypeDiscount() != null) {
            BigDecimal maxPrice;
            BigDecimal minPrice;
            BigDecimal discount = new BigDecimal(beanGoods.getSaleTypeDiscount());//销售类型折扣
            //根据销售类型计算价格
            if (beanGoods.getMax_price() == null || "".equals(beanGoods.getMax_price())) {
                //如果是0,就不需要计算了
                mEtMaxPrice.setText(beanGoods.getMax_price());
            } else {
                maxPrice = new BigDecimal(beanGoods.getMax_price());
                BigDecimal price = maxPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                mEtMaxPrice.setText(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

            if (beanGoods.getMin_price() == null || "".equals(beanGoods.getMin_price())) {
                //如果是0,就不需要计算了
                mEtMinPrice.setText(beanGoods.getMin_price());
            } else {
                minPrice = new BigDecimal(beanGoods.getMin_price());
                BigDecimal price = minPrice.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
                mEtMinPrice.setText(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

        } else {
            mEtMaxPrice.setText(beanGoods.getMax_price());
            mEtMinPrice.setText(beanGoods.getMin_price());
        }

        EditTextUtil.setEditSelection(mEtMaxPrice);
        EditTextUtil.setEditSelection(mEtMinPrice);

        updateEditLayout();

        mTvGoodsName.setText(beanGoods.getGg_title());
        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());
        mTvDate.setText(DateUtils.getDate2());
    }

    @Override
    public void onItem(int position) {
        adapterGoods.setIndex(-1);
        GoodsBean itemBean = adapterBuyList.getItem(position);

        if (addGoods()) {
            adapterBuyList.setIndex(position);
        } else {
            return;
        }
        //已选中的商品
        beanGoods = itemBean;
        beanGoods.setIndex(String.valueOf(position));

        updateEditLayout();

        mTvGoodsName.setText(beanGoods.getGg_title());

        mEtMaxNum.setText(beanGoods.getMaxNum());
        mEtMinNum.setText(beanGoods.getMinNum());

        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        mEtMaxPrice.setText(beanGoods.getMaxPrice());
        mEtMinPrice.setText(beanGoods.getMinPrice());

        mEtSumPrice.setText(beanGoods.getTotalPrice());
        mEtRemarks.setText(beanGoods.getRemarks());

        mTvDate.setText(beanGoods.getInventory_date());
        mEtDisplay.setText(beanGoods.getDisplay_quantity());
        mEtDuitou.setText(beanGoods.getDuitou_quantity());

        EditTextUtil.setEditSelection(mEtMaxNum);
        EditTextUtil.setEditSelection(mEtMinNum);
        EditTextUtil.setEditSelection(mEtMaxPrice);
        EditTextUtil.setEditSelection(mEtMinPrice);
        EditTextUtil.setEditSelection(mEtSumPrice);
        EditTextUtil.setEditSelection(mEtRemarks);

        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
    }

    /**
     * 刷新
     */
    private void updateEditLayout() {
        //如果大小单位名称相等,隐藏大单位
        if (beanGoods.getGgp_unit_num().equals("1")) {
            mLlMaxNum.setVisibility(View.GONE);
            mRlMaxPrice.setVisibility(View.GONE);
        } else {//都显示
            mLlMaxNum.setVisibility(View.VISIBLE);
            mRlMaxPrice.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击其他的地方的时候判断商品输入信息是否符合添加条件
     *
     * @return false是商品填写的添加信息不通过
     */
    public boolean addGoods() {
        /*//1 普通 2 处理 3 退货 4 换货*/
        if (beanGoods != null) { //已有选中的商品时
            if ((TextUtils.isEmpty(mEtMaxNum.getText()) || "0".equals(mEtMaxNum.getText().toString()))
                    && (TextUtils.isEmpty(mEtMinNum.getText()) || "0".equals(mEtMinNum.getText().toString()))) {
                //最大和最小都未空时可以选择
                clearGoodsEdit();

                if (!"".equals(beanGoods.getIndex()) && beanGoods.getIndex() != null) {
                    showToast(beanGoods.getGg_title() + "删除成功");
                    adapterBuyList.removeItem(Integer.parseInt(beanGoods.getIndex()));
                    beanGoods = null;
                    addBuyGoods(beanGoods);
                }
                return true;
            }

            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                beanGoods.setMaxNum("0");
            } else {
                String str = mEtMaxNum.getText().toString();
                String newStr = str.replaceFirst("^0*", "");

                beanGoods.setMaxNum(newStr.equals("") ? "0" : newStr);
            }

            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                beanGoods.setMinNum("0");
            } else {
                String str = mEtMinNum.getText().toString();
                String newStr = str.replaceFirst("^0*", "");

                beanGoods.setMinNum(newStr.equals("") ? "0" : newStr);
            }

            BigDecimal maxPrice;
            if (TextUtils.isEmpty(mEtMaxPrice.getText()) || "".equals(mEtMaxPrice.getText().toString())) {
                maxPrice = BigDecimal.ZERO;
            } else {
                maxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
            }

            BigDecimal minPrice;
            if (TextUtils.isEmpty(mEtMinPrice.getText()) || "".equals(mEtMinPrice.getText().toString())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(mEtMinPrice.getText().toString());
            }

            BigDecimal totalPrice = BigDecimal.ZERO;
            if (!TextUtils.isEmpty(mEtSumPrice.getText().toString()) && !"".equals(mEtSumPrice.getText().toString())) {
                totalPrice = new BigDecimal(mEtSumPrice.getText().toString());
            }

            beanGoods.setMaxPrice(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            beanGoods.setMinPrice(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            beanGoods.setTotalPrice(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            beanGoods.setRemarks(mEtRemarks.getText().toString());
            beanGoods.setDisplay_quantity(mEtDisplay.getText().toString());//陈列
            beanGoods.setDuitou_quantity(mEtDuitou.getText().toString());//堆头
            beanGoods.setInventory_date(mTvDate.getText().toString());

            if (saleTypeBean != null) {
                beanGoods.setSaleTypeDiscount(saleTypeBean.getSp_discount());
            }

            if ("".equals(beanGoods.getIndex()) || beanGoods.getIndex() == null) {
                showToast(beanGoods.getGg_title() + "添加成功");
                addBuyGoods(beanGoods);
            } else {
                for (int i = 0; i < listInventry.size(); i++) {
                    if (beanGoods.getIndex().equals(String.valueOf(i))) {
                        updateBuyGoods(beanGoods, i);
                        break;
                    }
                }
                showToast(beanGoods.getGg_title() + "修改成功");
                LogUtils.e("tag", "修改的商品信息:" + beanGoods.toString());
            }
            adapterBuyList.setData(listInventry);
            beanGoods = null;
            clearGoodsEdit();
        } else {
            clearGoodsEdit();
        }
        return true;
    }

    /**
     * 根据选择订单状态,刷新添加商品信息
     */
    private void updateBuyGoods(GoodsBean beanGoods, int i) {
        listInventry.set(i, beanGoods);
        amountMoney(listInventry);
    }

    /**
     * 根据选择订单状态,添加商品
     */
    private void addBuyGoods(GoodsBean beanGoods) {
        int index = -1;
        BigDecimal maxNum;
        BigDecimal minNum;
        BigDecimal goodsMaxNum;
        BigDecimal goodsMinNum;

        if (beanGoods != null) {
            /*for (int i = 0; i < listInventry.size(); i++) {
                if (listInventry.get(i).getGgp_id().equals(beanGoods.getGgp_id())) {
                    maxNum = new BigDecimal(listInventry.get(i).getMaxNum());
                    minNum = new BigDecimal(listInventry.get(i).getMinNum());

                    goodsMaxNum = new BigDecimal(beanGoods.getMaxNum());
                    goodsMinNum = new BigDecimal(beanGoods.getMinNum());

                    listInventry.get(i).setMaxNum(maxNum.add(goodsMaxNum).toString());
                    listInventry.get(i).setMinNum(minNum.add(goodsMinNum).toString());
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                listInventry.add(beanGoods);
            }*/

            listInventry.add(beanGoods);
        }
        amountMoney(listInventry);
    }

    /**
     * 根据购买数量计算商品价格
     */
    private void calculatePrice() {
        if (beanGoods != null) {
            BigDecimal bdMaxPrice;
            BigDecimal bdMaxNum;

            BigDecimal bdMinPrice;
            BigDecimal bdMinNum;

            BigDecimal bdSumPrice = null;

            if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
                bdMaxPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
            }

            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
            }

            if (TextUtils.isEmpty(mEtMinPrice.getText())) {
                bdMinPrice = new BigDecimal(BigInteger.ZERO);
            } else {
                bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
            }
            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
            }

            if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
                String sunPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                mEtSumPrice.setText(sunPrice);
            } else {
                bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
                mEtSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }
    }

    /**
     * 计算选中商品列表的价格
     */
    private void amountMoney(List<GoodsBean> list) {
        if (list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (GoodsBean bean : list) {
                BigDecimal bdPrice = BigDecimal.ZERO;
                if (!TextUtils.isEmpty(bean.getTotalPrice()) && !"".equals(bean.getTotalPrice())) {
                    bdPrice = new BigDecimal(bean.getTotalPrice());
                }

                sum = sum.add(bdPrice);
            }
            mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvTotalPrice.setText("0.00");
        }

        mTvSelectedNum.setText("已选(" + list.size() + "件)");
    }

    /**
     * 清空商品编辑框
     */
    private void clearGoodsEdit() {
        mTvGoodsName.setText("");
        mTvDate.setText("");

        mEtMaxNum.setText("");
        mEtMaxPrice.setText("");
        mEtMinNum.setText("");
        mEtMinPrice.setText("");
        mEtSumPrice.setText("");

        mEtDisplay.setText("");
        mEtDuitou.setText("");

        mEtRemarks.setText("");

        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
    }

    /**
     * 条件查询
     *
     * @param s
     */
    public void searchGoods(String s) {
        onGroupExpand(0);
        categoryCode = "";
        mFlag = 1;
        page = 1;
        getGoodsList(s);
    }

    /**
     * 二维码扫描搜索商品结果
     *
     * @param bean
     */
    public void addGoods(GoodsBean bean) {
        onGroupExpand(0);
        listGoods.clear();
        listGoods.add(bean);
        adapterGoods.setData(listGoods);
    }
}
