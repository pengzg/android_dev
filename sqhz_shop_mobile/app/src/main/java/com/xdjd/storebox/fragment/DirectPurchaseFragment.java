package com.xdjd.storebox.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.GoodsDetailActivity;
import com.xdjd.storebox.adapter.ClassifyTwoAdapter;
import com.xdjd.storebox.adapter.PurchaseGoodsAdapter;
import com.xdjd.storebox.adapter.listener.PurchaseGoodsListener;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.ActionBean;
import com.xdjd.storebox.bean.CartBean;
import com.xdjd.storebox.bean.GoodClassifyBean;
import com.xdjd.storebox.bean.GoodClassifyListBean;
import com.xdjd.storebox.bean.GoodsBean;
import com.xdjd.storebox.bean.GoodsCartBean;
import com.xdjd.storebox.bean.ScreenBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.event.GoodsNumEvent;
import com.xdjd.storebox.event.PurchaseEvent;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.CartUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.NoScrollGridView;
import com.xdjd.view.popup.EditCartNumPopupWindow;
import com.xdjd.view.popup.ScreenPopupWindow;
import com.xdjd.view.popup.SynthesizePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 预售进货fragment
 * Created by lijipei on 2016/11/18.
 */

public class DirectPurchaseFragment extends BaseFragment implements ClassifyTwoAdapter.ClassifyTwoListener,
        PurchaseGoodsListener, ScreenPopupWindow.ScreenPopupLisenter,EditCartNumPopupWindow.editCartNumListener{

    @BindView(R.id.classify01_title_ll)
    LinearLayout mClassify01TitleLl;
    @BindView(R.id.classify02_left_lv)
    ListView mClassify02LeftLv;
    @BindView(R.id.switch_ll)
    LinearLayout mSwitchLl;
    @BindView(R.id.synthesize_ll)
    LinearLayout mSynthesizeLl;
    @BindView(R.id.sales_volume_ll)
    LinearLayout mSalesVolumeLl;
    @BindView(R.id.price_ll)
    LinearLayout mPriceLl;
    @BindView(R.id.screen_ll)
    LinearLayout mScreenLl;
    @BindView(R.id.right_ll)
    LinearLayout mRightLl;
    @BindView(R.id.switch_iv)
    ImageView mSwitchIv;
    @BindView(R.id.synthesize_iv)
    ImageView mSynthesizeIv;
    @BindView(R.id.price_up_iv)
    ImageView mPriceUpIv;
    @BindView(R.id.price_next_iv)
    ImageView mPriceNextIv;
    @BindView(R.id.classify01_title_scroll)
    HorizontalScrollView mClassify01TitleScroll;
    @BindView(R.id.goods_gv)
    NoScrollGridView mGoodsGv;
    @BindView(R.id.fragment_unify_id)
    LinearLayout mFragmentUnifyId;
    @BindView(R.id.synthesize_tv)
    TextView mSynthesizeTv;
    @BindView(R.id.sales_volume_tv)
    TextView mSalesVolumeTv;
    @BindView(R.id.price_tv)
    TextView mPriceTv;
    @BindView(R.id.screen_tv)
    TextView mScreenTv;
    @BindView(R.id.screen_iv)
    ImageView mScreenIv;
    @BindView(R.id.goods_pull_scroll)
    PullToRefreshScrollView mGoodsPullScroll;

    private VaryViewHelper mVaryViewHelper;
    //private List<String> synthesizeList = new ArrayList<>();//综合排序，新品，人气

    DisplayMetrics metric;

    MainActivity main;

    private int oneTitleIndex = 0;
    /**
     * 一二分类总集合
     */
    private GoodClassifyBean classBean;
    /**
     * 二级分类集合
     */
    List<GoodClassifyListBean> twoClassifyList = new ArrayList<>();
    /**
     * 二级分类adapter
     */
    private ClassifyTwoAdapter mTwoAdapter;
    /**
     * 二级分类选中的type
     */
    private  String categoryType;
    /**
     * 二级分类选中的id
     */
    private String categoryId = "";
    /**
     * 二级分类选中的code
     */
    private String categoryCode = "";

    /**
     * 记录上次商品点击二级分类id
     */
    private String sxCategoryId = "";

    /**
     * 商品数据列表
     */
    List<GoodsBean> mGoodsBeanList = new ArrayList<>();
    /**
     * 商品数据列表adapter
     */
    private PurchaseGoodsAdapter mGoodsAdapter;
    /**
     * 商品列表页数
     */
    private int pageNo = 1;
    /**
     * 刷新的状态
     */
    private int mFlag = 0;

    /**
     * 条件type:默认综合
     */
    private int conditionType;

    public String strBrandId;//	品牌	N	多个用英文逗号分隔   4,5,6
    public String strPriceId;//	价格	N
    public String strCategoryId;//	三级分类 	N	多个用英文逗号分隔   4,5,6

    /**
     * 品牌
     */
    List<ScreenBean> screenBrandList = new ArrayList<>();
    /**
     * 价格
     */
    List<ScreenBean> screenPriceList = new ArrayList<>();
    /**
     * 筛选
     */
    List<ScreenBean> screenCategoryList = new ArrayList<>();

    /**
     * 采购fragment
     */
    //PurchaseFragment purchaseFragment;
    private Intent intent = new Intent();

    /**
     * 综合popupwindow
     */
    private SynthesizePopupWindow syPopup;

    /**
     * 筛选popup
     */
    private ScreenPopupWindow scPopup;
    /**
     * 购物车加减popup
     */
    private EditCartNumPopupWindow editCartPopup;
    private void selectEditCartPwScreen(){
        editCartPopup = new EditCartNumPopupWindow(getActivity(),this);
    }
    /**
     * 是否走onstart方法
     */
    private boolean isStart = false;


//    public DirectPurchaseFragment(PurchaseFragment purchaseFragment) {
//        this.purchaseFragment = purchaseFragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direct, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
       main = (MainActivity) getActivity();
        metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        EventBus.getDefault().register(this);
        initView();
        selectPwSynthesize();
        selectPwScreen();
        selectEditCartPwScreen();
        loadCartPriceNum();
        loadClassifyData(PublicFinal.FIRST);
    }

    private void initView() {
        mVaryViewHelper = new VaryViewHelper(mFragmentUnifyId);

        mGoodsPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mGoodsPullScroll);
        mGoodsPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                mGoodsBeanList.clear();
                loadGoodsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadGoodsList();
            }
        });

        mTwoAdapter = new ClassifyTwoAdapter(this);
        mClassify02LeftLv.setAdapter(mTwoAdapter);
        mTwoAdapter.setIndex(0);

        mGoodsAdapter = new PurchaseGoodsAdapter(mSwitchIv.isSelected() ? 1 : 2, this);
        mGoodsGv.setNumColumns(mSwitchIv.isSelected() ? 1 : 2);
        mGoodsGv.setAdapter(mGoodsAdapter);
        mGoodsAdapter.setData(mGoodsBeanList);

        //首次进来默认是综合条件
        conditionType = PublicFinal.sx1;
        mSynthesizeTv.setSelected(true);
        mSynthesizeIv.setImageResource(R.drawable.index_next02);
    }

    /**
     * 获取购物车数量和总价格
     */
    private void loadCartPriceNum() {
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    main.getPurchase().setCartTotalAmount(bean.getTotalAmount());
                } else if ("30".equals(bean.getRepCode())){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
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
        httpUtil.post(M_Url.cartInfo, L_RequestParams.getCartInfo(UserInfoUtils.getId(getActivity()),UserInfoUtils.getCenterShopId(getActivity()),
                UserInfoUtils.getStoreHouseId(getActivity())), false);
    }

    /**
     * 获取分类数据
     */
    private void loadClassifyData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<GoodClassifyBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodClassifyBean.class, new IUpdateUI<GoodClassifyBean>() {
            @Override
            public void updata(GoodClassifyBean classigyBean) {
                if (classigyBean.getRepCode().equals("00")) {
                    mVaryViewHelper.showDataView();
                    classBean = classigyBean;
                    if(classBean.getListData() != null && classBean.getListData().size() != 0){
                        for (int i = 0; i < classBean.getListData().size(); i++) {
                            initTitleView(i);
                        }
                        move(oneTitleIndex);
                    }else{
                        showToast("该公司还没有可售的商品！");
                    }
                } else {
                    showToast(classigyBean.getRepMsg());
                    mVaryViewHelper.showErrorView(new onClassErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                mVaryViewHelper.showErrorView(new onClassErrorListener());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.GoodsCategory, L_RequestParams.
                getGoodsCategory(UserInfoUtils.getId(getActivity()),"2",UserInfoUtils.getStoreHouseId(getActivity())), false);
    }

    /**
     * 加载失败点击事件
     */
    class onClassErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadClassifyData(PublicFinal.FIRST);
            loadCartPriceNum();
        }
    }

    /**
     * 初始化title分类布局
     *
     * @param a
     */
    private void initTitleView(final int a) {
        TextView v = (TextView) LayoutInflater.from(getActivity()).inflate(
                R.layout.item_classify_tv, mClassify01TitleLl, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mClassify01TitleLl.getChildCount(); i++) {
                    mClassify01TitleLl.getChildAt(i).setSelected(false);
                }
                oneTitleIndex = a;
                //mClassify01TitleLl.getChildAt(a).setSelected(true);
                move(a);
            }
        });
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        v.setLayoutParams(params);

        mClassify01TitleLl.addView(v);
        v.setText(classBean.getListData().get(a).getGcName());
    }

    /**
     * 设置一级分类选中状态
     *
     * @param index
     */
    private void move(int index) {
        mClassify01TitleScroll.smoothScrollTo((index - 2)
                * getResources().getDisplayMetrics().widthPixels / 5, 0);
        mClassify01TitleLl.getChildAt(index).setSelected(true);
        setTwoClassifyList(index);
    }

    /**
     * 二级分类请求数据
     *
     * @param oneIndex
     */
    private void setTwoClassifyList(int oneIndex) {
        twoClassifyList = classBean.getListData().get(oneIndex).getSubList();
        mTwoAdapter.setData(twoClassifyList);
        pageNo = 1;
        mFlag = 0;
        //清楚商品数据
        mGoodsBeanList.clear();
        mGoodsAdapter.notifyDataSetChanged();
        if (classBean.getListData().get(oneIndex).getSubList() != null &&
                classBean.getListData().get(oneIndex).getSubList().size() != 0) {
            categoryType = twoClassifyList.get(0).getBackType();
            this.categoryId = twoClassifyList.get(0).getGcId();
            this.categoryCode = twoClassifyList.get(0).getGcCode();
            itemClassifyTwo(0);
        } else {
            //没有二级分类信息
            /*categoryType = classBean.getListData().get(oneIndex).getBackType();
            this.categoryId = classBean.getListData().get(oneIndex).getGcId();
            this.categoryCode = twoClassifyList.get(0).getGcCode();
            //this.categoryCode = "";
            loadGoodsList();*/
            //showToast("该公司还没有可售的商品！");
        }
    }

    @Override
    public void itemClassifyTwo(int position) {
        //筛选字段
        strBrandId = "";
        strPriceId = "";
        strCategoryId = "";
        //将筛选按钮设为未选中状态
        mScreenIv.setSelected(false);
        mScreenTv.setSelected(false);

        mTwoAdapter.setIndex(position);
        mGoodsBeanList.clear();
        categoryType = twoClassifyList.get(position).getBackType();
        this.categoryId = twoClassifyList.get(position).getGcId();
        this.categoryCode = twoClassifyList.get(0).getGcCode();
        loadGoodsList();
    }

    /**
     * 更新商品列表
     */
    public void updateGoodsList(){
        pageNo = 1;
        mFlag = 1;
        mGoodsBeanList.clear();
        mClassify01TitleLl.removeAllViews();
        loadClassifyData(PublicFinal.FIRST);
        loadCartPriceNum();
    }

    /**
     * 加载商品列表
     */
    private void loadGoodsList() {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean goodsBean) {
                if (goodsBean.getRepCode().equals("00")) {
                    if (goodsBean.getListData().size()!=0 &&
                            goodsBean.getListData()!=null){
                        mGoodsBeanList.addAll(goodsBean.getListData());
                    }else{
                        if (mFlag == 2){
                            pageNo--;
                            showToast(UIUtils.getString(R.string.pull_up_remind));
                        }else{
                            showToast("该公司还没有可售的商品！");
                        }
                    }
                    mGoodsAdapter.notifyDataSetChanged();
                } else {
                    showToast(goodsBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mGoodsPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.GoodsList, L_RequestParams.getGoodsList(UserInfoUtils.getId(getActivity()),categoryType,
                categoryCode, String.valueOf(pageNo), "2",String.valueOf(conditionType), strBrandId,
                strPriceId, strCategoryId,UserInfoUtils.getStoreHouseId(getActivity())), true);
    }

    @OnClick({R.id.switch_ll, R.id.synthesize_ll, R.id.sales_volume_ll, R.id.price_ll, R.id.screen_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_ll://单双行商品列表展示切换
                if (mSwitchIv.isSelected()) {
                    setColumns(2);
                } else {
                    mGoodsGv.setHorizontalSpacing(0);
                    mGoodsGv.setVerticalSpacing(10);
                    setColumns(1);
                }
                break;
            case R.id.synthesize_ll://综合
                if (conditionType != PublicFinal.sx1 && conditionType != PublicFinal.sx5 &&
                        conditionType != PublicFinal.sx6) {
                    conditionType = syPopup.index;
                    setType(conditionType);
                } else {
                    showPwSynthesize();
                }
                break;
            case R.id.sales_volume_ll://销量
                conditionType = PublicFinal.sx2;
                setType(conditionType);
                break;
            case R.id.price_ll: //价格
                if (conditionType != PublicFinal.sx3 && conditionType != PublicFinal.sx4) {
                    conditionType = PublicFinal.sx3;
                    setType(conditionType);
                } else {
                    if (conditionType == PublicFinal.sx3) {
                        conditionType = PublicFinal.sx4;
                        setType(conditionType);
                    } else {
                        conditionType = PublicFinal.sx3;
                        setType(conditionType);
                    }
                }
                break;
            case R.id.screen_ll://筛选
                if (categoryId != null && !categoryId.equals("") &&
                        sxCategoryId.equals(categoryId)) {
                    showPwScreen();
                } else {
                    loadPwScreen(categoryId);
                }
                break;
        }
    }

    @Override
    public void plusCart(int i, final RelativeLayout rl) {
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
    public void editCart(String editNum, String ggpId, String min_num) {
        if( editNum.equals(" ")){
            showToast("请输入订货数量！");
        }else{
            if(editNum.compareTo(min_num)<0){
                //showToast("不能小于起订数量！");
            }
            if(editNum.equals("0")){
                modifyCart(editNum,ggpId);
                editCartPopup.clearNum();
            }else{
                modifyCart(editNum,ggpId);
            }
        }
    }

    @Override
    public void itemGoods(int position) {
        intent.setClass(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("gpId", mGoodsBeanList.get(position).getGgp_id());
        intent.putExtra("cartNum", mGoodsBeanList.get(position).getCartnum());
        startActivity(intent);
    }

    /**
     * 查询商品在购物车中的数量
     * @param i
     * @param
     */
    private void queryGooodsCartNum(final int i, final GoodsBean goodBean, final RelativeLayout rl){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    editCartPopup.showPwScreen(rl,bean.getC_goods_num(),bean.getIsFavorite(),goodBean);
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
        httpUtil.post(M_Url.queryGoodsNums,L_RequestParams.queryGoodsCarNum(UserInfoUtils.getId(getActivity()),goodBean.getGgp_id(),UserInfoUtils.getStoreHouseId(getActivity()),""),false);
    }

    /**
     * 修改购物车
     */
    private  void modifyCart(String num,String ggpId){
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    showToast("已成功添加至购物车！");
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    editCartPopup.dissPopupWindow();
                }else {
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {}
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.editCartGoodNum,L_RequestParams.editCart(UserInfoUtils.getId(getActivity()),ggpId,num,UserInfoUtils.getStoreHouseId(getActivity()),""),false);
    }


    /**
     * 修改Gridview的列数
     *
     * @param position
     */
    private void setColumns(int position) {
        mSwitchIv.setSelected(position == 1 ? true : false);
        mGoodsGv.setNumColumns(position);
        mGoodsAdapter.setColumns(position);
    }

    /**
     * 请求筛选参数
     *
     * @param categoryId
     */
    private void loadPwScreen(final String categoryId) {
        strBrandId = "";
        strPriceId = "";
        strCategoryId = "";
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean goodsBean) {
                if (goodsBean.getRepCode().equals("00")) {
                    sxCategoryId = categoryId;
                    screenBrandList = goodsBean.getBrandListData();
                    screenPriceList = goodsBean.getPriceListData();
                    screenCategoryList = goodsBean.getCategoryListData();
                    //显示筛选popup
                    showPwScreen();
                } else {
                    showToast(goodsBean.getRepMsg());
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
        httpUtil.post(M_Url.SearchList, L_RequestParams.
                getSearchList(UserInfoUtils.getId(getActivity()), categoryId), false);
    }

    /**
     * 初始化综合popup
     */
    private void selectPwSynthesize() {
        syPopup = new SynthesizePopupWindow(getActivity(), PublicFinal.sx1,
                new SynthesizePopupWindow.synthesizePopupLisenter() {
                    @Override
                    public void synthesizeType(int position, String title) {
                        mSynthesizeTv.setText(title);
                        conditionType = position;
                        setType(conditionType);
                    }
                });
    }

    /**
     * 显示综合popup
     */
    private void showPwSynthesize() {
        if (Build.VERSION.SDK_INT < 24) {
            syPopup.showAsDropDown(mSynthesizeLl, 0, 0);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            mSynthesizeLl.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            syPopup.showAtLocation(mSynthesizeLl, Gravity.NO_GRAVITY, 0, y + mSynthesizeLl.getHeight());
        }
    }

    /**
     * 初始化-筛选PopupWindow
     */
    private void selectPwScreen() {

        scPopup = new ScreenPopupWindow(getActivity(), this);
    }

    /**
     * 显示筛选popup
     */
    private void showPwScreen() {
        scPopup.showPwScreen(mRightLl, screenBrandList, screenPriceList, screenCategoryList);
    }

    @Override
    public void screenType(String strBrandId, String strPriceId, String strCategoryId) {
        LogUtils.e("筛选参数", "strBrandId:" + strBrandId + "--strPriceId:" +
                strPriceId + "--strCategoryId:" + strCategoryId);
        if (this.strBrandId.equals(strBrandId) && this.strPriceId.equals(strPriceId)
                && this.strCategoryId.equals(strCategoryId)) {
        } else {
            this.strBrandId = strBrandId;
            this.strPriceId = strPriceId;
            this.strCategoryId = strCategoryId;
            if (!strBrandId.equals("") || !strPriceId.equals("") || !strCategoryId.equals("")) {
                //重新加载数据
                mScreenIv.setSelected(true);
                mScreenTv.setSelected(true);
            } else {
                //将筛选按钮设为未选中状态
                mScreenIv.setSelected(false);
                mScreenTv.setSelected(false);
            }
            mGoodsBeanList.clear();
            pageNo = 1;
            mFlag = 1;
            //当筛选条件改变时请求山商品数据
            loadGoodsList();
        }
    }

    /**
     * 设置选中商品条件的文字和图片样式,并重新请求商品列表
     *
     * @param type
     */
    public void setType(int type) {
        mGoodsBeanList.clear();
        mGoodsAdapter.notifyDataSetChanged();
        loadGoodsList();
        switch (type) {
            case PublicFinal.sx2://销量
                mSynthesizeTv.setSelected(false);
                mSynthesizeIv.setImageResource(R.drawable.index_next01);
                mSalesVolumeTv.setSelected(true);
                mPriceNextIv.setImageResource(R.drawable.index_next01);
                mPriceUpIv.setImageResource(R.drawable.index_up01);
                mPriceTv.setSelected(false);
                break;
            case PublicFinal.sx3://价格升序
                mSynthesizeTv.setSelected(false);
                mSynthesizeIv.setImageResource(R.drawable.index_next01);
                mSalesVolumeTv.setSelected(false);
                mPriceNextIv.setImageResource(R.drawable.index_next01);
                mPriceUpIv.setImageResource(R.drawable.index_up02);
                mPriceTv.setSelected(true);
                break;
            case PublicFinal.sx4://价格降序
                mSynthesizeTv.setSelected(false);
                mSynthesizeIv.setImageResource(R.drawable.index_next01);
                mSalesVolumeTv.setSelected(false);
                mPriceNextIv.setImageResource(R.drawable.index_next02);
                mPriceUpIv.setImageResource(R.drawable.index_up01);
                mPriceTv.setSelected(true);
                break;
            case PublicFinal.sx1://综合
            case PublicFinal.sx5:
            case PublicFinal.sx6:
                mSynthesizeTv.setSelected(true);
                mSynthesizeIv.setImageResource(R.drawable.index_next02);
                mSalesVolumeTv.setSelected(false);
                mPriceNextIv.setSelected(false);
                mPriceUpIv.setSelected(false);
                mPriceNextIv.setImageResource(R.drawable.index_next01);
                mPriceUpIv.setImageResource(R.drawable.index_up01);
                mPriceTv.setSelected(false);
                break;
        }
    }

    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim_in);
        view.startAnimation(animation);
    }

    /**
     * PopupWindow消失的动画
     */
    private void translateAnimOut(View view, final PopupWindow pw) {
        //setTab(PublicFinal.ALLDEFAULT);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim_out);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pw.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 其他界面点击加减购物车,刷新商品购物车数量
     * @param event
     */
    public void onEventMainThread(GoodsNumEvent event) {
        boolean isRefresh = false;
        for (int i=0;i<mGoodsBeanList.size();i++){
            if (mGoodsBeanList.get(i).getGg_id().equals(event.getGoodsId())){
                mGoodsBeanList.get(i).setCartnum(event.getCartNum());
                isRefresh = true;
            }
        }

        if (isRefresh){
            mGoodsAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(PurchaseEvent event) {
        mFlag = 1;
        pageNo = 1;
        mGoodsBeanList.clear();
        loadGoodsList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
