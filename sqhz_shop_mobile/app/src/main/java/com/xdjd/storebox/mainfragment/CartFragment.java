package com.xdjd.storebox.mainfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.ActionActivity;
import com.xdjd.storebox.activity.ActionActivityGroup;
import com.xdjd.storebox.activity.CartActivity;
import com.xdjd.storebox.activity.ConfirmOrderActivity;
import com.xdjd.storebox.activity.GoodsDetailActivity;
import com.xdjd.storebox.adapter.CartNewAdapter;
import com.xdjd.storebox.adapter.CartAdapter;
import com.xdjd.storebox.adapter.listener.CartListener;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.BaseBean;
import com.xdjd.storebox.bean.CartActionBean;
import com.xdjd.storebox.bean.CartBean;
import com.xdjd.storebox.bean.CartGoodsListBean;
import com.xdjd.storebox.bean.CartListBean;
import com.xdjd.storebox.bean.GoodsCartBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.event.ConfirmOrderEvent;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2017/2/24.
 */

public class CartFragment extends BaseFragment implements CartListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.cart_listview)
    ListView mCartListview;
    @BindView(R.id.iv_all)
    ImageView mIvAll;
    @BindView(R.id.rl_all)
    RelativeLayout mRlAll;
    @BindView(R.id.sum_price)
    TextView mSumPrice;
    @BindView(R.id.collect_tv)
    TextView mCollectTv;
    @BindView(R.id.enter_tv)
    TextView mEnterTv;
    @BindView(R.id.start_price)
    TextView mStartPrice;
    @BindView(R.id.spell_ll)
    LinearLayout mSpellLl;
    @BindView(R.id.hint_rl)
    RelativeLayout mHintRl;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private VaryViewHelper mVaryViewHelper = null;

    private List<CartActionBean> list = new ArrayList<>();
    private CartBean bean = new CartBean();

    private ClearCartListener mClearCartListener = new ClearCartListener();

    private Intent intent;

    private CartNewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).
                inflate(R.layout.activity_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        mTitleBar.setTitle("购物车");
        mTitleBar.setRightTextColor(R.color.text_212121);
        mTitleBar.setRightText("编辑");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleBar.getRightText().toString().equals(getString(R.string.cart_edit))) {
                    mTitleBar.setRightText("完成");
                    CartNewAdapter.isSuccess = true;
                    mTitleBar.setRightText02("清空购物车");
                    mTitleBar.setRightTextColor02(R.color.text_212121);
                    mTitleBar.setRightTextClickListener02(mClearCartListener);
                } else {
                    CartNewAdapter.isSuccess = false;
                    mTitleBar.setRightText(getString(R.string.cart_edit));
                    mTitleBar.setRightText02Gone();
                }
                adapter.notifyDataSetChanged();

                if (CartNewAdapter.isSuccess) {
                    mEnterTv.setText("删除");
                    mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));

                    mCollectTv.setVisibility(View.VISIBLE);
                } else {
                    mCollectTv.setVisibility(View.GONE);
                    BigDecimal amount = new BigDecimal(bean.getTotalAmount());
                    BigDecimal freeAmount = new BigDecimal(bean.getFreeAmount());
                    if (freeAmount.doubleValue() > amount.doubleValue()) {
                        mEnterTv.setText("¥" + bean.getFreeAmount() + "起送");
                        mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_c2c2c2));
                    } else {
                        mEnterTv.setText("结算(" + bean.getCartNum() + ")");
                        mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));
                    }
                }
            }
        });

        mVaryViewHelper = new VaryViewHelper(mLlMain);

        //        adapter = new CartAdapter(this);
        //        mCartListview.setAdapter(adapter);

        adapter = new CartNewAdapter(this);
        mCartListview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCartList(PublicFinal.FIRST, false);

        if (CartNewAdapter.isSuccess) {
            mTitleBar.setRightText("完成");
            CartNewAdapter.isSuccess = true;
            mTitleBar.setRightText02("清空购物车");
            mTitleBar.setRightTextColor02(R.color.text_212121);
            mTitleBar.setRightTextClickListener02(mClearCartListener);
        } else {
            CartNewAdapter.isSuccess = false;
            mTitleBar.setRightText02Gone();
            if (null == list || list.size() == 0) {
                mTitleBar.setRightTextViewVisibility(View.GONE);
            } else {
                mTitleBar.setRightText(getString(R.string.cart_edit));
            }
        }

        if (CartNewAdapter.isSuccess) {
            mEnterTv.setText("删除");
            mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));

            mCollectTv.setVisibility(View.VISIBLE);
        } else {
            mCollectTv.setVisibility(View.GONE);
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal freeAmount = BigDecimal.ZERO;

            if (bean.getTotalAmount() != null && !"".equals(bean.getTotalAmount())) {
                amount = new BigDecimal(bean.getTotalAmount());
            }

            if (bean.getFreeAmount() != null && !"".equals(bean.getFreeAmount())) {
                freeAmount = new BigDecimal(bean.getFreeAmount());
            }

            if (freeAmount.doubleValue() > amount.doubleValue()) {
                mEnterTv.setText("¥" + bean.getFreeAmount() + "起送");
                mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_c2c2c2));
            } else {
                mEnterTv.setText("结算(" + bean.getCartNum() + ")");
                mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));
            }
        }
    }

    @OnClick({R.id.spell_ll, R.id.rl_all, R.id.collect_tv, R.id.enter_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.spell_ll:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("currentTab", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                /**
                 * enter_anim:要跳转的界面开始的动画
                 * exit_anim:当前界面销毁的动画
                 */
                //                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.rl_all:
                cartAll();
                break;
            case R.id.collect_tv:
                cartCollect();
                break;
            case R.id.enter_tv:
                submitOrder();
                break;
        }
    }

    private void cartAll() {
        if (mIvAll.isSelected()) {//全部没有选中
            mIvAll.setSelected(false);

            for (int i = 0; i < list.size(); i++) {

                if ("5".equals(list.get(i).getActivityType())){
                    list.get(i).setIsChild(0);
                    continue;
                }

                for (int j = 0; j < list.get(i).getDataList().size(); j++) {

                    list.get(i).getDataList().get(j).setIsChild(0);
                }
            }
        } else {//全部选中商品
            mIvAll.setSelected(true);

            for (int i = 0; i < list.size(); i++) {

                if ("5".equals(list.get(i).getActivityType())){
                    list.get(i).setIsChild(1);
                    continue;
                }

                for (int j = 0; j < list.get(i).getDataList().size(); j++) {

                    list.get(i).getDataList().get(j).setIsChild(1);
                }
            }
        }
        calculateCartPrice();
        adapter.notifyDataSetChanged();
    }

    /**
     * 购物车移入收藏方法
     */
    public void cartCollect() {
        int num = 0;

        StringBuilder goodsCartId = new StringBuilder();
        final StringBuilder favoriteIds = new StringBuilder();

        for (int k = 0; k < list.size(); k++) {
            for (int z = 0; z < list.get(k).getDataList().size(); z++) {
                if (list.get(k).getDataList().get(z).getIsChild() == 1) {
                    goodsCartId.append(list.get(k).getDataList().get(z).getC_id() + ",");
                    favoriteIds.append(list.get(k).getDataList().get(z).getGgp_id() + ",");
                    num++;
                }
            }
        }

        if (goodsCartId.length() > 0) {
            goodsCartId.deleteCharAt(goodsCartId.length() - 1);
            favoriteIds.deleteCharAt(favoriteIds.length() - 1);
        } else {
            //showToast("还没选择要结算的商品");
            return;
        }

        final String goodsStr = goodsCartId.toString();
        final String favoriteStr = favoriteIds.toString();

        DialogUtil.showCustomDialog(getActivity(), null, "当前选中的" + num + "种商品关注成功后,将从购物车删除,请您确认", "移入收藏",
                "我再想想", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        addFavoriteBatch(favoriteStr, goodsStr);
                    }

                    @Override
                    public void no() {
                    }
                });
    }

    @Override
    public void cartAction(int i) {//商品的活动跳转
        if (list.get(i).getActivityType().equals("5")) {
            intent = new Intent(getActivity(), ActionActivityGroup.class);
            intent.putExtra("activityId", list.get(i).getActivityId());
        } else {
            intent = new Intent(getActivity(), ActionActivity.class);
            intent.putExtra("activityId", list.get(i).getActivityId());
        }
        startActivity(intent);
    }

    @Override
    public void cartPlus(int i) {
        BigDecimal actionNum;
        if (list.get(i).getActivityNum()!=null && list.get(i).getActivityNum().length()>0){
            actionNum = new BigDecimal(list.get(i).getActivityNum());
        }else{
            actionNum = BigDecimal.ZERO;
        }
        int num = actionNum.intValue();
        modifyCart(++num,i);
    }

    @Override
    public void cartMinus(int i) {
        BigDecimal actionNum;
        if (list.get(i).getActivityNum()!=null && list.get(i).getActivityNum().length()>0){
            actionNum = new BigDecimal(list.get(i).getActivityNum());
        }else{
            actionNum = BigDecimal.ZERO;
        }
        int num = actionNum.intValue();
        modifyCart(--num,i);
    }

    @Override
    public void cartEdit(int i) {
    }

    @Override
    public void cartLeft(int i) {
        //是否选中,1是选中, 其他未选中
        if (list.get(i).getIsChild() == 0) {
            list.get(i).setIsChild(1);
            for (int k = 0;k<list.get(i).getDataList().size();k++){
                list.get(i).getDataList().get(k).setIsChild(1);
            }
        } else if (list.get(i).getIsChild() == 1) {
            list.get(i).setIsChild(0);
            for (int k = 0;k<list.get(i).getDataList().size();k++){
                list.get(i).getDataList().get(k).setIsChild(0);
            }
        }

        isAllGoods();
        calculateCartPrice();
        adapter.notifyDataSetChanged();
    }

    /**
     * 判断购物车商品是否全选
     */
    private void isAllGoods(){
        boolean all = true;
        for (int l = 0; l < list.size(); l++) {
            if ("5".equals(list.get(l).getActivityType()) && list.get(l).getIsChild()!=1){
                all = false;
                break;
            }
            for (int z = 0; z < list.get(l).getDataList().size(); z++) {
                if (list.get(l).getDataList().get(z).getIsChild() != 1) {
                    all = false;
                    break;
                }
            }
        }

        if (all) {
            mIvAll.setSelected(true);
        } else {
            mIvAll.setSelected(false);
        }
    }

    /*修改套餐购物车数量*/
    private  void modifyCart(final int num, final int i){
        LogUtils.e("modifyCart","num:"+num);
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if(bean.getRepCode().equals("00")){
                    showToast(bean.getRepMsg());
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    if (num == 0){
                        list.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                    loadCartList(PublicFinal.TWO,true);
                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.editCartGoodNum,L_RequestParams.editCart(UserInfoUtils.getId(getActivity()),"", String.valueOf(num),
                UserInfoUtils.getStoreHouseId(getActivity()),list.get(i).getActivityId()),false);
    }

    /**
     * 提交商品,生成订单
     */
    private void submitOrder() {
        if (!mTitleBar.getRightText().toString().equals(getString(R.string.cart_edit))) {
            int num = 0;
            StringBuilder goodsCartId = new StringBuilder();

            for (int i = 0; i < list.size(); i++) {
                for (int k = 0; k < list.get(i).getDataList().size(); k++) {
                    if (list.get(i).getDataList().get(k).getIsChild() == 1) {
                        goodsCartId.append(list.get(i).getDataList().get(k).getC_id() + ",");
                        num++;
                    }
                }
            }

            if (goodsCartId.length() > 0) {
                goodsCartId.deleteCharAt(goodsCartId.length() - 1);
            } else {
                //showToast("还没选择要结算的商品");
                return;
            }

            final String str = goodsCartId.toString();

            DialogUtil.showCustomDialog(getActivity(), null, "确认要删除这" + num + "中商品吗?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                    deleteCart(str);
                }

                @Override
                public void no() {
                }
            });
        } else {
            BigDecimal goodsTotalAmount;
            if (TextUtils.isEmpty(mSumPrice.getText())) {
                goodsTotalAmount = BigDecimal.ZERO;
            } else {
                goodsTotalAmount = new BigDecimal(mSumPrice.getText().toString());
            }

            BigDecimal freeAmount = BigDecimal.ZERO;
            if (bean.getFreeAmount()!=null && !"".equals(bean.getFreeAmount())){
                freeAmount = new BigDecimal(bean.getFreeAmount());
            }
            if (freeAmount.compareTo(BigDecimal.ZERO) == 1){
                //只有起订金额大于0时才做限制
                if (goodsTotalAmount.compareTo(freeAmount) == -1) {
                    showToast("商品总价还没有达到起订金额");
                    return;
                }
            }
            
            StringBuilder goodsCartId = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                for (int k = 0; k < list.get(i).getDataList().size(); k++) {
                    if (list.get(i).getDataList().get(k).getIsChild() == 1) {
                        goodsCartId.append(list.get(i).getDataList().get(k).getC_id() + ",");
                    }
                }
            }

            if (goodsCartId.length() > 0) {
                goodsCartId.deleteCharAt(goodsCartId.length() - 1);
            } else {
                showToast("还没选择要结算的商品");
                return;
            }

            Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
            intent.putExtra("cartIds", goodsCartId.toString());
            startActivity(intent);
        }
    }

    /**
     * 清空购物车回调
     */
    class ClearCartListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (null == list || list.size() == 0) {
                return;
            } else {
                clearCart();
            }
        }
    }

    private void loadCartList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean cartBean) {
                if (cartBean.getRepCode().equals("00")) {
                    mVaryViewHelper.showDataView();

                    bean = cartBean;

                    EventBus.getDefault().post(new CartEvent(cartBean.getCartNum(), cartBean.getTotalAmount()));
                    list.clear();
                    list = bean.getCartData().getActivityList();
                    LogUtils.e("总共几个活动", cartBean.getCartData().getActivityList().size() + "--");
                    if (null == list || list.size() == 0) {
                        mVaryViewHelper.showEmptyCartView("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("currentTab", 1);
                                startActivity(intent);
                                /**
                                 * enter_anim:要跳转的界面开始的动画
                                 * exit_anim:当前界面销毁的动画
                                 */
                                //                                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                            }
                        });
                        //mLayoutCartEmptyMain.setVisibility(View.VISIBLE);

                        //没有商品时,隐藏按钮
                        CartNewAdapter.isSuccess = false;
                        mTitleBar.setRightText(getString(R.string.cart_edit));
                        mTitleBar.setRightText02Gone();
                        mTitleBar.setRightTextViewVisibility(View.GONE);
                    } else {
                        if (CartNewAdapter.isSuccess) {
                            mTitleBar.setRightText("完成");
                            mTitleBar.setRightText02("清空购物车");
                            mTitleBar.setRightTextColor02(R.color.text_212121);
                            mTitleBar.setRightTextClickListener02(mClearCartListener);
                        } else {
                            mTitleBar.setRightText(getString(R.string.cart_edit));
                            mTitleBar.setRightText02Gone();
                        }
                    }

                    adapter.setData(list);
                    mIvAll.setSelected(false);
                    mSumPrice.setText("0.00");
                    calculateCartPrice();
                } else if ("30".equals(cartBean.getRepCode())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab", 4);
                    startActivity(intent);
                    showToast(cartBean.getRepMsg());
                } else {
                    showToast(cartBean.getRepMsg());
                    mVaryViewHelper.showErrorView(new onErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(new onErrorListener());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.cartList, L_RequestParams.getCartList(UserInfoUtils.getId(getActivity()),UserInfoUtils.getStoreHouseId(getActivity())), isDialog);
    }

    /**
     * 加载失败点击事件
     */
    class onErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadCartList(PublicFinal.FIRST, false);
        }
    }

    @Override
    public void cartChildDelete(int i, int j) {
        CartGoodsListBean bean = list.get(i).getDataList().get(j);

        alterCart(true, i, j, bean.getGgp_id(), bean.getC_id(), "0");
    }

    @Override
    public void cartPlus(int i, int j) {
        CartGoodsListBean bean = list.get(i).getDataList().get(j);
        int plusNum = CartUtil.countPlusNum(bean.getC_goods_num(), bean.getGps_min_num(),
                bean.getGps_add_num(), bean.getGoods_stock());

        if (plusNum == CartUtil.Three){
            showToast("库存不足,少买点吧!");
            return;
        }

        if (plusNum == CartUtil.Two) {
            showToast("数量超出库存范围");
            return;
        } else {
            alterCart(false, i, j, bean.getGgp_id(), bean.getC_id(),
                    String.valueOf(plusNum));
        }
    }

    @Override
    public void cartMinus(int i, int j) {
        CartGoodsListBean bean = list.get(i).getDataList().get(j);
        if (!bean.getC_goods_num().equals("0")) {
            int minusNum = CartUtil.countMinusNum(bean.getC_goods_num(), bean.getGps_min_num(),
                    bean.getGps_add_num(), bean.getGoods_stock());
            alterCart(false, i, j, bean.getGgp_id(), bean.getC_id(),
                    String.valueOf(minusNum));
        }
    }

    @Override
    public void cartEdit(final int i, final int j) {
        final CartGoodsListBean bean = list.get(i).getDataList().get(j);
        DialogUtil.showEditCartNum(getActivity(), "加入购物车数量", "确定", "取消", bean.getC_goods_num(),
                bean.getGps_min_num(), bean.getGps_add_num(), bean.getGoods_stock(),bean.getGgp_goods_type(), new DialogUtil.MyCustomDialogListener() {
                    @Override
                    public void ok(String str) {
                        alterCart(false, i, j, bean.getGgp_id(), bean.getC_id(), str);
                    }

                    @Override
                    public void no() {
                    }
                });
    }

    @Override
    public void cartLeft(int i, int j) {
        LogUtils.e("cartLeft", "i:" + i + "--j:" + j);
        if (list.get(i).getDataList().get(j).getIsChild() == 0) {
            list.get(i).getDataList().get(j).setIsChild(1);
        } else if (list.get(i).getDataList().get(j).getIsChild() == 1) {
            list.get(i).getDataList().get(j).setIsChild(0);
        }

        boolean all = true;
        for (int l = 0; l < list.size(); l++) {
            for (int z = 0; z < list.get(l).getDataList().size(); z++) {
                if (list.get(l).getDataList().get(z).getIsChild() != 1) {
                    all = false;
                }
            }
        }

        if (all) {
            mIvAll.setSelected(true);
        } else {
            mIvAll.setSelected(false);
        }
        calculateCartPrice();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void cartChildItem(int i, int j) {
        /*CartGoodsListBean bean = list.get(i).getActivityList().get(j).getDataList().get(k);
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("gpId", bean.getGpId());
        intent.putExtra("cartNum", bean.getGoodsNum());
        startActivity(intent);*/
    }

    /**
     * 点击购物车计算当前价格方法
     * @param cartIds
     */
    /*private void getSingleAmount(String cartIds){
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if ("00".equals(bean.getRepCode())){
                }else{
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
            }
            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getSingleAmount,L_RequestParams.
                getSingleAmount(UserInfoUtils.getId(this),cartIds),true);
    }*/

    /**
     * 删除购物车商品
     */
    private void deleteCart(final String cartIds) {
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean jsonBean) {
                if (jsonBean.getRepCode().equals("00")) {

                    EventBus.getDefault().post(new CartEvent("", jsonBean.getTotalAmount()));
                    /*String[] strCartIds = cartIds.split(",");

                    for (int i = 0; i < list.size(); i++) {

                        for (int k = 0; k < strCartIds.length; k++) {

                            if (list.get(i).getDataList().get(k).getC_id().equals(strCartIds[k])) {
                                list.get(i).getDataList().remove(k);
                            }
                        }

                        if (list.get(i).getDataList().size() == 0) {
                            list.remove(i);
                        }
                    }*/

                    if (list.size() == 0) {
                        CartAdapter.isSuccess = false;
                        mTitleBar.setRightText(getString(R.string.cart_edit));
                        mTitleBar.setRightText02Gone();
                        mTitleBar.setRightTextViewVisibility(View.GONE);
                        mVaryViewHelper.showEmptyCartView("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("currentTab", 1);
                                startActivity(intent);
                               /* *
                                * enter_anim:要跳转的界面开始的动画
                                * exit_anim:当前界面销毁的动画
                                */
                                //                                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                            }
                        });
                    } else {
                        //计算选中商品的总价格
                        calculateCartPrice();
                    }

                    loadCartList(PublicFinal.FIRST, false);
                    adapter.notifyDataSetChanged();
                } else if ("30".equals(jsonBean.getRepCode())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab", 4);
                    startActivity(intent);
                    showToast(jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.deleteCart, L_RequestParams.getDeleteCart(
                UserInfoUtils.getId(getActivity()), cartIds, UserInfoUtils.getCenterShopId(getActivity()),UserInfoUtils.getStoreHouseId(getActivity())), true);
    }

    /**
     * 清空购物车
     */
    public void clearCart() {
        AsyncHttpUtil<CartBean> httpCleart = new AsyncHttpUtil<>(getActivity(), CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    loadCartList(PublicFinal.TWO, true);
                } else if ("30".equals(jsonBean.getRepCode())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab", 4);
                    startActivity(intent);
                    showToast(jsonBean.getRepMsg());
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
        httpCleart.post(M_Url.clearCart, L_RequestParams.
                getCleartCart(UserInfoUtils.getId(getActivity()), UserInfoUtils.getCenterShopId(getActivity()),UserInfoUtils.getStoreHouseId(getActivity())), true);
    }

    /**
     * 购物车商品移入收藏夹
     */
    private void addFavoriteBatch(String favoriteids, String cartIds) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean bean) {
                if (bean.getRepCode().equals("00")) {
//                    showToast(bean.getRepMsg());

                    clearCart();
                } else if ("30".equals(bean.getRepCode())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab", 4);
                    startActivity(intent);
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.addFavoriteBatch, L_RequestParams.addFavoriteBatch(UserInfoUtils.getId(getActivity()),
                "1", favoriteids, "1"), true);
    }

    /**
     * 修改购物车数量
     *
     * @param ggpId    商品价格id
     * @param gp_id
     * @param goodsNum
     */
    private void alterCart(final boolean isDelete, final int i, final int j,
                           final String ggpId, String gp_id, final String goodsNum) {
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (isDelete) {
                        //如果是删除商品
                        list.get(i).getDataList().remove(j);
                    } else {
                        if (bean.getC_goods_num().equals("0")) {
                            //如果本商品在购物车中的数量减到 0 了
                            list.get(i).getDataList().remove(j);
                        } else {
                            list.get(i).getDataList().get(j).setC_goods_num(bean.getC_goods_num());
                            list.get(i).getDataList().get(j).setC_goods_amount(bean.getC_goods_amount());
                        }
                    }

                    if (list.get(i).getDataList().size() == 0) {
                        list.remove(i);
                    }

                    if (list.size() == 0) {
                        CartAdapter.isSuccess = false;
                        mTitleBar.setRightText(getString(R.string.cart_edit));
                        mTitleBar.setRightText02Gone();
                        mTitleBar.setRightTextViewVisibility(View.GONE);
                        mVaryViewHelper.showEmptyCartView("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("currentTab", 1);
                                startActivity(intent);
                                /**
                                 * enter_anim:要跳转的界面开始的动画
                                 * exit_anim:当前界面销毁的动画
                                 */
                                //                                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                            }
                        });
                    } else {
                        //计算选中商品的总价格
                        calculateCartPrice();
                    }
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    //                    EventBus.getDefault().post(new GoodsNumEvent(ggpId, goodsNum));
                    adapter.notifyDataSetChanged();
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
        httpUtil.post(M_Url.editCartGoodNum, L_RequestParams.editCart(UserInfoUtils.getId(getActivity()), ggpId, goodsNum,
                UserInfoUtils.getStoreHouseId(getActivity()),""), false);
    }

    /**
     * 计算购物车价格
     */
    private void calculateCartPrice() {
        BigDecimal goodsTotalAmount = BigDecimal.ZERO;
        BigDecimal goodsTotalNum = BigDecimal.ZERO;

        for (int i = 0; i < list.size(); i++) {

            for (int k = 0; k < list.get(i).getDataList().size(); k++) {

                if (list.get(i).getDataList().get(k).getIsChild() == 1) {
                    BigDecimal goodsAmount = new BigDecimal(list.get(i).getDataList().get(k).getC_goods_amount());
                    goodsTotalAmount = goodsTotalAmount.add(goodsAmount);

                    BigDecimal goodsNum = new BigDecimal(list.get(i).getDataList().get(k).getC_goods_num());
                    goodsTotalNum = goodsTotalNum.add(goodsNum);
                }
            }

            bean.setTotalAmount(goodsTotalAmount.toString());
            bean.setCartNum(goodsTotalNum.toString());
        }

        if (CartNewAdapter.isSuccess) {
            mEnterTv.setText("删除");
            mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));

            mCollectTv.setVisibility(View.VISIBLE);
        } else {
            mCollectTv.setVisibility(View.GONE);
            BigDecimal amount = new BigDecimal(bean.getTotalAmount());
            BigDecimal freeAmount = new BigDecimal(bean.getFreeAmount());
            if (freeAmount.doubleValue() > amount.doubleValue()) {
                mEnterTv.setText("¥" + bean.getFreeAmount() + "起送");
                mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_c2c2c2));
            } else {
                mEnterTv.setText("结算(" + bean.getCartNum() + ")");
                mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));
            }
        }

        mSumPrice.setText(goodsTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        CartNewAdapter.isSuccess = false;
    }

}
