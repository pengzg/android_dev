package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.GoodsImageAdapter;
import com.xdjd.storebox.adapter.PayTypeAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ConfirmOrderBean;
import com.xdjd.storebox.bean.CreateOrderBean;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.xdjd.storebox.bean.PayMethods;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.event.ConfirmOrderEvent;
import com.xdjd.storebox.event.PurchaseEvent;
import com.xdjd.storebox.main.MainActivity;
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
import com.xdjd.view.NoScrollGridView;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 确认订单activity
 * Created by lijipei on 2016/12/1.
 */

public class ConfirmOrderActivity extends BaseActivity implements PayTypeAdapter.OnPayClickListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.enter_tv)
    TextView mEnterTv;
    @BindView(R.id.pay_type_listview)
    NoScrollListView mPayTypeListview;
    @BindView(R.id.confirm_order_ll)
    LinearLayout mConfirmOrderLl;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.phone_tv)
    TextView mPhoneTv;
    @BindView(R.id.address_tv)
    TextView mAddressTv;
    @BindView(R.id.usable_coupon_num_tv)
    TextView mUsableCouponNumTv;
    @BindView(R.id.goods_total_num)
    TextView mGoodsTotalNum;
    @BindView(R.id.goods_total_price)
    TextView mGoodsTotalPrice;
    @BindView(R.id.discount_amount)
    TextView mDiscountAmount;
    @BindView(R.id.freight_tv)
    TextView mFreightTv;
    @BindView(R.id.note_et)
    EditText mNoteEt;
    @BindView(R.id.amount_tv)
    TextView mAmountTv;
    @BindView(R.id.coupon_ll)
    LinearLayout mCouponLl;
    @BindView(R.id.address_false)
    TextView mAddressFalse;
    @BindView(R.id.address_true)
    LinearLayout mAddressTrue;
    @BindView(R.id.my_address_ll)
    LinearLayout mMyAddressLl;
    @BindView(R.id.goods_list_ll)
    LinearLayout mGoodsListLl;
    @BindView(R.id.goods_order_gridview)
    NoScrollGridView mGoodsOrderGridview;
    @BindView(R.id.goods_list_ll_child)
    LinearLayout mGoodsListLlChild;
    @BindView(R.id.jiajiagou_tv)
    TextView mJiajiagouTv;
    @BindView(R.id.action_goods_name)
    TextView mActionGoodsName;
    @BindView(R.id.action_goods_price)
    TextView mActionGoodsPrice;
    @BindView(R.id.action_goods_ll)
    LinearLayout mActionGoodsLl;
    @BindView(R.id.jiajiagou_parent_ll)
    LinearLayout mJiajiagouParentLl;
    @BindView(R.id.confirm_order_main)
    LinearLayout mConfirmOrderMain;
    @BindView(R.id.pay_method_desc)
    TextView mPayMethodDesc;
    //    @BindView(R.id.jiajiagou_listview)
    //    NoScrollListView mJiajiagouListview;
    //    @BindView(R.id.jiajiagou_listview_ll)
    //    LinearLayout mJiajiagouListviewLl;
    //    @BindView(R.id.jiajiagou_ll)
    //    LinearLayout mJiajiagouLl;
    //    @BindView(R.id.jiajiagou_parent_ll)
    //    LinearLayout mJiajiagouParentLl;


    private String cartIds;

    private PayTypeAdapter adapterPayType;
    //private ConfirmOrderAdapter adapter;
    private GoodsImageAdapter adapter;

    private ConfirmOrderBean bean;
    private List<PayMethods> payList;
    private List<OrderGoodsDetailBean> dataList;

    private String payMethod = "";//支付方式
    private String couponId = "";//优惠券id

    private String addressId = "";//地址id

    private String giftgoodsid = "";//加价购商品id

    private Intent intent;

    private VaryViewHelper mVaryViewHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        mVaryViewHelper = new VaryViewHelper(mConfirmOrderLl);
        initView();
        loadData(PublicFinal.FIRST, false);
    }

    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("付款结算");
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClose();
            }
        });

        mEnterTv.setEnabled(false);

        cartIds = getIntent().getStringExtra("cartIds");

        adapterPayType = new PayTypeAdapter(this);
        mPayTypeListview.setAdapter(adapterPayType);

        adapter = new GoodsImageAdapter();
        mGoodsOrderGridview.setAdapter(adapter);
        //mGoodsOrderListview.setAdapter(adapter);

        mGoodsOrderGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ConfirmOrderActivity.this, GoodsListActivity.class);
                intent.putExtra("cartIds", cartIds);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.enter_tv, R.id.confirm_order_main, R.id.coupon_ll, R.id.my_address_ll,
            R.id.goods_list_ll, R.id.goods_list_ll_child, R.id.jiajiagou_ll,R.id.action_goods_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_tv:
                payMethod = "";
                for (int i = 0; i < payList.size(); i++) {
                    if (payList.get(i).getIsDefault().equals("1")) {
                        payMethod = payList.get(i).getPayId();
                    }
                }
                if (payMethod.equals("")) {
                    showToast("请选择支付方式");
                    return;
                }
                createOrder();
                break;
            case R.id.confirm_order_main:
                break;
            case R.id.coupon_ll://选择可用优惠券
                intent = new Intent(this, CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.my_address_ll:
                showToast("请联系业务员修改收货地址!");
//                intent = new Intent(this, Address_main.class);
//                intent.putExtra("addressId", bean.getAddressId());
//                intent.putExtra("isConfirm", true);
//                startActivityForResult(intent, 100);
                break;
            case R.id.goods_list_ll_child:
            case R.id.goods_list_ll: //跳转商品列表页
                Intent intent = new Intent(this, GoodsListActivity.class);
                intent.putExtra("cartIds", cartIds);
                startActivity(intent);
                break;
            case R.id.jiajiagou_ll://加价购
                Intent intent1 = new Intent(this, JiaJiaGouActivity.class);
                intent1.putExtra("amount", bean.getAmount());
                if (bean.getJiajiagouGoods() == null) {
                    intent1.putExtra("goodsAmount", "0");
                } else {
                    intent1.putExtra("goodsAmount", bean.getJiajiagouGoods().getGoodsAmount());
                }
                intent1.putExtra("giftgoodsid",giftgoodsid);
                startActivityForResult(intent1, 2000);
                break;
            case R.id.action_goods_ll:
                Intent intent2 = new Intent(this, JiaJiaGouActivity.class);
                intent2.putExtra("amount", bean.getAmount());
                if (bean.getJiajiagouGoods() == null) {
                    intent2.putExtra("goodsAmount", "0");
                } else {
                    intent2.putExtra("goodsAmount", bean.getJiajiagouGoods().getGoodsAmount());
                }
                intent2.putExtra("giftgoodsid",giftgoodsid);
                startActivityForResult(intent2, 2000);
                break;
        }
    }

    private void loadData(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ConfirmOrderBean> httpUtil = new AsyncHttpUtil<>(this, ConfirmOrderBean.class,
                new IUpdateUI<ConfirmOrderBean>() {
                    @Override
                    public void updata(ConfirmOrderBean jsonBean) {
                        if (jsonBean.getRepCode().equals("00")) {
                            bean = jsonBean;
                            payList = bean.getPayMethods();
                            dataList = bean.getDataList();
                            setData();
                            mVaryViewHelper.showDataView();
                        }else if ("30".equals(jsonBean.getRepCode())){
                            Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("currentTab",4);
                            startActivity(intent);
                            showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.bulidSettlementOrder, L_RequestParams.bulidSettlementOrder(
                UserInfoUtils.getId(this), cartIds, payMethod, couponId, addressId, giftgoodsid,UserInfoUtils.getStoreHouseId(this)), isDialog);
    }

    @Override
    public void onPayClick(int index) {
        for (int i = 0; i < payList.size(); i++) {
            payList.get(i).setIsDefault("0");
        }
        payList.get(index).setIsDefault("1");
        adapterPayType.notifyDataSetChanged();
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
     * 设置数据
     */
    private void setData() {
        if (bean.getAddressId()==null && (bean.getAddress()==null || "".equals(bean.getAddress()) )){
            mAddressFalse.setVisibility(View.VISIBLE);
            mAddressTrue.setVisibility(View.GONE);
        }else{
            mAddressFalse.setVisibility(View.GONE);
            mAddressTrue.setVisibility(View.VISIBLE);
        }

        mNameTv.setText(bean.getReceiverName());
        mPhoneTv.setText(bean.getMobile());
        mAddressTv.setText(bean.getAddress());

        /*if (bean.getJiajiagouName().equals("") || bean.getJiajiagouName() == null) {
            mJiajiagouParentLl.setVisibility(View.GONE);
        } else {
            mJiajiagouParentLl.setVisibility(View.VISIBLE);
            mJiajiagouTv.setText(bean.getJiajiagouName());

            if (bean.getJiajiagouGoods() != null) {
                mActionGoodsLl.setVisibility(View.VISIBLE);

                mActionGoodsName.setText(bean.getJiajiagouGoods().getGoodsName());
                mActionGoodsPrice.setText("¥" + bean.getJiajiagouGoods().getGoodsAmount());
            } else {
                mActionGoodsLl.setVisibility(View.GONE);
            }
        }*/

        mGoodsTotalNum.setText("共" + bean.getGoodsNum() + "件");
        mGoodsTotalPrice.setText("¥" + bean.getGoodsAmount());
        mDiscountAmount.setText("¥" + bean.getDiscountAmount());
        mFreightTv.setText("¥" + bean.getFreightAmount());

        mAmountTv.setText("¥" + bean.getAmount());

        //选择支付方式优惠字段
        if (bean.getPayMethodDesc() == null || bean.getPayMethodDesc().equals("")){
            mPayMethodDesc.setText("");
        }else{
            mPayMethodDesc.setText(bean.getPayMethodDesc());
        }

        adapterPayType.setData(payList);
        adapter.setData(dataList);
        //加载成功后才可点击
        mEnterTv.setEnabled(true);
    }

    private void createOrder() {
        if (mAddressTrue.getVisibility() == View.GONE){
            showToast("请选择收货地址");
            return;
        }

        AsyncHttpUtil<CreateOrderBean> httpUtil = new AsyncHttpUtil<>(this,
                CreateOrderBean.class, new IUpdateUI<CreateOrderBean>() {
            @Override
            public void updata(CreateOrderBean jsonBean) {
                if (jsonBean.getRepCode().equals("00")) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, OrderSuccessActivity.class);
                    //intent.putExtra("orderCode", jsonBean.getOrderCode());
                    intent.putExtra("payAmount", jsonBean.getPayAmount());
                    intent.putExtra("OrderId", jsonBean.getOrderId());
                    startActivity(intent);

                    //获得所有提交的商品id
                    List<String> listStr = new ArrayList<>();
                    for (int i=0;i<dataList.size();i++){
                       // listStr.add(dataList.get(i).getGoodsId());
                    }

                    EventBus.getDefault().post(new CartEvent(jsonBean.getCartNum(),
                            jsonBean.getCartTotalAmount()));
                    EventBus.getDefault().post(new PurchaseEvent());
                    EventBus.getDefault().post(new ConfirmOrderEvent(listStr));

                    finishActivity();
                }else if (jsonBean.getRepCode().equals("02")){
                    //跳到添加地址页面
                    showToast(jsonBean.getRepMsg());
                }else if ("30".equals(bean.getRepCode())){
                    Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab",4);
                    startActivity(intent);
                    showToast(bean.getRepMsg());
                }else{
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
        httpUtil.post(M_Url.createOrder, L_RequestParams.createOrder(UserInfoUtils.getId(this), cartIds, payMethod,
                addressId, mNoteEt.getText().toString(), couponId, giftgoodsid,UserInfoUtils.getCenterShopId(this),UserInfoUtils.getStoreHouseId(this)), true);
    }

    /**
     * 是否退出结算单
     */
    private void isClose() {
        DialogUtil.showCustomDialog(this, null, "是否退出购物车", "去意已决", "再想想", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                finish();
            }

            @Override
            public void no() {
            }
        });
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        isClose();
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
            case 2000://选择加价购商品返回的商品id
                giftgoodsid = data.getStringExtra("giftgoodsid");
                loadData(PublicFinal.FIRST, false);
                break;
            case 3000:// 取消换购商品
                giftgoodsid = "";
                loadData(PublicFinal.FIRST, false);
//                mActionGoodsLl.setVisibility(View.GONE);
                break;
        }
    }
}
