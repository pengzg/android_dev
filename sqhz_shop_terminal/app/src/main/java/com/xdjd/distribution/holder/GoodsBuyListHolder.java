package com.xdjd.distribution.holder;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.GoodsBuyListingAdapter;
import com.xdjd.distribution.base.BaseHolder;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import butterknife.BindView;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsBuyListHolder extends BaseHolder  implements ItemOnListener {

    @BindView(R.id.lv_goods_buy_listing)
    ListView mLvGoodsBuyListing;

    private Context mContext;
    private GoodsBuyListingAdapter adapterBuyList;

    public GoodsBuyListHolder(Context context) {
        mContext = context;
    }

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(),R.layout.holder_goods_buy_list, null);
        return view;
    }

    @Override
    protected void refreshUI(Object data) {
        adapterBuyList = new GoodsBuyListingAdapter(this);
        mLvGoodsBuyListing.setAdapter(adapterBuyList);
    }

    @Override
    public void onItem(int position) {
//        adapterGoods.setIndex(-1);
//        GoodsBean itemBean = adapterBuyList.getItem(position);
//
//        if (addGoods()) {
//            adapterBuyList.setIndex(position);
//        } else {
//            return;
//        }
//        //已选中的商品
//        beanGoods = itemBean;
//        beanGoods.setIndex(String.valueOf(position));
//
//        updateEditLayout();
//
//        mTvSaleTypeName.setText(beanGoods.getSaleTypeName());//销售类型
//
//        mTvGoodsName.setText(beanGoods.getGg_title());
//
//        mEtMaxNum.setText(beanGoods.getMaxNum());
//        mEtMinNum.setText(beanGoods.getMinNum());
//
//        mUnitMaxNameref.setText(beanGoods.getGg_unit_max_nameref());
//        mUnitMinNameref.setText(beanGoods.getGg_unit_min_nameref());
//
//        mEtMaxPrice.setText(beanGoods.getMaxPrice());
//        mEtMinPrice.setText(beanGoods.getMinPrice());
//
//        mEtSumPrice.setText(beanGoods.getTotalPrice());
//        mEtRemarks.setText(beanGoods.getRemarks());
//
//        if ("".equals(beanGoods.getGgs_stock()) || beanGoods.getGgs_stock() == null) {
//            mTvStock.setText("无库存");
//        } else {
//            BigDecimal bdStock = new BigDecimal(beanGoods.getGgs_stock());
//            BigDecimal bdUnitNum = new BigDecimal(beanGoods.getGgp_unit_num());
//
//            //            BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
//            //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
//            // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
//            BigDecimal[] results = bdStock.divideAndRemainder(bdUnitNum);
//
//            String stockStr = results[0].toBigInteger() + beanGoods.getGg_unit_max_nameref() + results[1] + beanGoods.getGg_unit_min_nameref();
//            mTvStock.setText("库存" + stockStr);
//        }
//
//        AnimationUtil.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMaxPlus);
//        AnimationUtil.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMinPlus);
    }
}
