package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.OrderRetureAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.GoodsStatusBean;
import com.xdjd.distribution.popup.GoodsStatusPopup;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderReturnActivity extends BaseActivity implements OrderRetureAdapter.OrderRetureListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_order_return_goods)
    ListView mLvOrderReturnGoods;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;

    private OrderRetureAdapter adapter;
    private List<GoodsBean> listOrderReturnGoods;

    /**
     * 商品类型-- 1 正 2 临 3残 4过
     */
    private GoodsStatusPopup goodsStatusPopup;
    private List<GoodsStatusBean> listGoodsStatus;

    @Override
    protected int getContentView() {
        return R.layout.activity_order_return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("订单退货");

        adapter = new OrderRetureAdapter(this);
        mLvOrderReturnGoods.setAdapter(adapter);
    }

    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                DialogUtil.showCustomDialog(this, "提示", "是否进行退货提交?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {

                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    /**
     * 获取商品退货类型数据列表
     */
    private void getGoodsStatusList(final boolean isDialog) {
        AsyncHttpUtil<GoodsStatusBean> httpUtil = new AsyncHttpUtil<>(this, GoodsStatusBean.class, new IUpdateUI<GoodsStatusBean>() {
            @Override
            public void updata(GoodsStatusBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listGoodsStatus = jsonBean.getListData();
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getGoodsStatusList, L_RequestParams.
                getGoodsStatusList(), true);
    }

    @Override
    public void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.getItem(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.getItem(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onReturnStatus(int i, ImageView ivReturnStatus, LinearLayout llReturn) {
        if (goodsStatusPopup == null) {
            initGoodsStatusPopup(llReturn.getWidth());
        }
        showPwGoodsStatus(llReturn);
    }

    @Override
    public void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        adapter.getItem(i).setMaxNum(num);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    @Override
    public void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        adapter.getItem(i).setMinNum(num);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    private void goodsStatus(int code, ImageView mIvGoodsStatus) {
        switch (code) {
            case Comon.GOODS_STATUS:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status);
                break;
            case Comon.GOODS_STATUS_C:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status_cc);
                break;
            case Comon.GOODS_STATUS_G:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status_gq);
                break;
            case Comon.GOODS_STATUS_L:
                mIvGoodsStatus.setImageResource(R.mipmap.goods_status_lq);
                break;
        }
    }

    /**
     * 计算所有订单状态的价格
     */
    private String allAmountMoney() {
        BigDecimal sum = BigDecimal.ZERO;

        if (listOrderReturnGoods.size() > 0) {
            for (GoodsBean bean : listOrderReturnGoods) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        return sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 数量添加计算
     *
     * @param et
     */
    private void plusCalculation(int i, EditText et, TextView tvSumPrice) {
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
    private void minusCalculation(int i, EditText et, TextView tvSumPrice) {
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
    private void calculatePrice(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格

        if (TextUtils.isEmpty(adapter.getItem(i).getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapter.getItem(i).getMaxPrice());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapter.getItem(i).getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapter.getItem(i).getMinPrice());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        if ("1".equals(adapter.getItem(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).setTotalPrice(sumPrice);
        }

        mTvTotalPrice.setText(allAmountMoney());
    }

    /**
     * 初始化商品退货类型popup 商品状态 1 正 2 临 3残 4过
     */
    private void initGoodsStatusPopup(int width) {
        goodsStatusPopup = new GoodsStatusPopup(this, width, new GoodsStatusPopup.OnGoodsStatusListener() {
            @Override
            public void onGoodsStatus(int position) {
                //                goodsStatusBean = listGoodsStatus.get(position);
                //                goodsStatus(goodsStatusBean.getCode());
                goodsStatusPopup.dismiss();
            }
        });
    }

    /**
     * 显示商品类型
     */
    private void showPwGoodsStatus(LinearLayout linearLayout) {
        goodsStatusPopup.setData(listGoodsStatus);
        // 显示窗口
        goodsStatusPopup.showAsDropDown(linearLayout, -UIUtils.dp2px(8), 0);
    }

}
