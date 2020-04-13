package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.IntegralConfirmBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/2/27.
 */

public class IntegralConfirmOrderActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.add_address_ll)
    LinearLayout mAddAddressLl;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.tel_tv)
    TextView mTelTv;
    @BindView(R.id.address_tv)
    TextView mAddressTv;
    @BindView(R.id.goods_img)
    ImageView mGoodsImg;
    @BindView(R.id.goods_name)
    TextView mGoodsName;
    @BindView(R.id.goods_integral_num)
    TextView mGoodsIntegralNum;
    @BindView(R.id.goods_market_price)
    TextView mGoodsMarketPrice;
    @BindView(R.id.integral_reality_num)
    TextView mIntegralRealityNum;
    @BindView(R.id.pay_btn)
    Button mPayBtn;
    @BindView(R.id.linear)
    LinearLayout mLinear;
    @BindView(R.id.goods_market_price_ll)
    LinearLayout mGoodsMarketPriceLl;
    @BindView(R.id.address_ll)
    LinearLayout mAddressLl;
    @BindView(R.id.goods_num_tv)
    TextView mGoodsNumTv;

    private int wig_id;//积分商品id
    private int goodsNum;//商品数量

    private String addressId = "";//地址id

    private VaryViewHelper mVaryViewHelper = null;

    private IntegralConfirmBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_confirm_order);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData() {
        mTitleBar.setTitle("积分订单确认");
        mTitleBar.leftBack(this);

        mVaryViewHelper = new VaryViewHelper(mLinear);

        wig_id = getIntent().getIntExtra("wig_id", 0);
        goodsNum = getIntent().getIntExtra("goodsNum", 0);

        loadData(PublicFinal.FIRST, false);
    }

    @OnClick({R.id.add_address_ll, R.id.pay_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address_ll:
                Intent intent = new Intent(this, Address_main.class);
                intent.putExtra("addressId", addressId);
                intent.putExtra("isConfirm", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.pay_btn://提交订单
                DialogUtil.showCustomDialog(this, "确定兑换此商品", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        confirmOrder();
                    }

                    @Override
                    public void no() {
                    }
                });
                break;
        }
    }

    /**
     * 获取积分商品订单生成详情
     */
    private void loadData(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<IntegralConfirmBean> httpUtil = new AsyncHttpUtil<>(this, IntegralConfirmBean.class,
                new IUpdateUI<IntegralConfirmBean>() {
                    @Override
                    public void updata(IntegralConfirmBean jsonBean) {
                        if (jsonBean.getRepCode().equals("00")) {
                            bean = jsonBean;
                            mNameTv.setText("姓名:" + bean.getUserName());
                            mTelTv.setText("手机号:" + bean.getMobile());
                            mAddressTv.setText("地址:" + bean.getAddress());

                            if (("".equals(bean.getUserName()) || null == bean.getUserName()) &&
                                    (("".equals(bean.getAddress()) || null == bean.getAddress()))) {
                                mAddressLl.setVisibility(View.GONE);
                            } else {
                                mAddressLl.setVisibility(View.VISIBLE);
                            }

                            Glide.with(IntegralConfirmOrderActivity.this).load(bean.getGoodsImage()).
                                    placeholder(R.color.image_gray).into(mGoodsImg);
                            mGoodsName.setText(bean.getGoodsName());
                            mGoodsIntegralNum.setText(bean.getIntegrate_price());
                            if (bean.getGp_wholesale_price() == null || "".equals(bean.getGp_wholesale_price())) {
                                mGoodsMarketPriceLl.setVisibility(View.GONE);
                            } else {
                                mGoodsMarketPriceLl.setVisibility(View.VISIBLE);
                                mGoodsMarketPrice.setText(bean.getGp_wholesale_price() + "元");
                            }

                            mGoodsNumTv.setText("x"+bean.getGoodsNums());
                            mIntegralRealityNum.setText(bean.getTotal_price());

                            mVaryViewHelper.showDataView();
                        }else{
                            showToast(jsonBean.getRepMsg());
                            finishActivity();
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
        httpUtil.post(M_Url.createIntegralOrderDetail, L_RequestParams.createIntegralOrderDetail(
                UserInfoUtils.getId(this), String.valueOf(wig_id), String.valueOf(goodsNum), addressId), isDialog);
    }

    /**
     * 加载失败点击事件
     */
    class onErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadData(PublicFinal.FIRST, false);
        }
    }

    /**
     * 确认订单
     */
    private void confirmOrder() {
        AsyncHttpUtil<IntegralConfirmBean> httpUtil = new AsyncHttpUtil<>(this, IntegralConfirmBean.class,
                new IUpdateUI<IntegralConfirmBean>() {
                    @Override
                    public void updata(IntegralConfirmBean jsonBean) {
                        if (jsonBean.getRepCode().equals("00")) {
                            finishActivity();
                            startActivity(IntegralOrderActivity.class);
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
        httpUtil.post(M_Url.createIntegralOrder, L_RequestParams.createIntegralOrder(
                UserInfoUtils.getId(this), String.valueOf(wig_id), String.valueOf(goodsNum), addressId), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1000:
                boolean isYes = data.getBooleanExtra("isYes", false);
                if (isYes) {//有地址
                    addressId = data.getStringExtra("addressId");
                } else {//没有地址
                    addressId = data.getStringExtra("addressId");
                }
                LogUtils.e("addressId", addressId);
                loadData(PublicFinal.FIRST, false);
                break;
        }
    }
}
