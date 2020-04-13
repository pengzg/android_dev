package com.xdjd.distribution.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.GoodsListAdapter;
import com.xdjd.distribution.base.BaseHolder;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/10
 *     desc   : 商品列表holder
 *     version: 1.0
 * </pre>
 */

public class GoodsListHolder extends BaseHolder implements GoodsListAdapter.GoodsListListener{

    @BindView(R.id.lv_goods)
    NoScrollListView mLvGoods;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private Context mContext;

    private GoodsListAdapter adapterGoods;

    /**
     * 商品页数
     */
    public int page = 1;
    public int mFlag = 0;

    /**
     * 商品列表集合
     */
    private List<GoodsBean> listGoods = new ArrayList<>();

    public GoodsListHolder(Context context) {
        mContext = context;
    }

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(),R.layout.holder_goods_list, null);
        return view;
    }

    @Override
    protected void refreshUI(Object data) {

        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setRightLayout(false);
//        initRefresh(mPullScroll);
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

        adapterGoods = new GoodsListAdapter(this);
        mLvGoods.setAdapter(adapterGoods);
    }

    /**
     * 获取商品列表
     */
    private void getGoodsList(String searchKey) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>((Activity) mContext, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listGoods.addAll(jsonBean.getListData());
                        adapterGoods.setData(listGoods);
                    } else {
                        if (mFlag == 2) {
                            page--;
                            UIUtils.Toast("没有更多数据了");
                        }
                    }
                } else {
                    UIUtils.Toast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
//        httpUtil.post(M_Url.getGoodsList, L_RequestParams.
//                getGoodsList(userBean.getUserId(), clientBean.getCc_id(),
//                        String.valueOf(indexOrder), searchKey, categoryCode, storehouseId, "1", String.valueOf(page)), true);
    }

    @Override
    public void onItemGoods(int i) {
       /* adapterBuyList.setIndex(-1);
        if (addGoods()) {
            adapterGoods.setIndex(i);
        } else {
            return;
        }
        mHorizontalScroll.scrollTo(mHorizontalScroll.getWidth(), 0);

        beanGoods = (GoodsBean) listGoods.get(i).clone();

        if (listSaleType != null && listSaleType.size() > 0) {//设置销售类型
            saleTypeBean = listSaleType.get(0);

            beanGoods.setSaleTypeName(saleTypeBean.getSp_name());
            beanGoods.setSaleType(saleTypeBean.getSp_code());
            beanGoods.setSaleTypeDiscount(saleTypeBean.getSp_discount());
        }

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

        updateEditLayout();

        mTvSaleTypeName.setText(beanGoods.getSaleTypeName());

        mTvGoodsName.setText(beanGoods.getGg_title());
        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());

        if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null) {
            mTvStock.setText("无库存");
        } else {
            BigDecimal bdStock = new BigDecimal(beanGoods.getGgs_stock());
            BigDecimal bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());

            //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
            //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
            // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
            BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);

            String stockStr = results[0].toBigInteger() + beanGoods.getGg_unit_max_nameref() + results[1] + beanGoods.getGg_unit_min_nameref();
            mTvStock.setText("库存" + stockStr);
        }*/
    }
}
