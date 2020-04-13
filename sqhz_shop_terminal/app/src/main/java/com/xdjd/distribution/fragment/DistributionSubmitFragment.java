package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.DistributionActivity;
import com.xdjd.distribution.activity.OrderSettlementActivity;
import com.xdjd.distribution.adapter.DistributionAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.DistributionGoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.DistributionEvent;
import com.xdjd.distribution.event.OrderRejectedEvent;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.quickindex.CharacterParser;
import com.xdjd.utils.recycle.VaryViewHelper;

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
 *     time   : 2017/5/10
 *     desc   : 配送任务 -- 商品提交界面
 *     version: 1.0
 * </pre>
 */

public class DistributionSubmitFragment extends BaseFragment implements DistributionAdapter.DistributionListener {

    @BindView(R.id.tv_order_form)
    TextView mTvOrderForm;
    @BindView(R.id.tv_process_sheet_form)
    TextView mTvProcessSheetForm;
    @BindView(R.id.tv_exchange_form)
    TextView mTvExchangeForm;
    @BindView(R.id.tv_refund_form)
    TextView mTvRefundForm;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.lv_distribution_order)
    ListView mLvDistributionOrder;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_give_back_form)
    TextView mTvGiveBackForm;

    private DistributionAdapter adapter;
    private List<String> list = new ArrayList<>();

    private View view;

    public int indexOrder = 1;

    /**
     * 订单选中的商品列表
     */
    public List<OrderBean> listGoodsOrder;

    /**
     * 处理单选中的商品列表
     */
    public List<OrderBean> listProcessOrder;

    /**
     * 换货单选中的商品列表
     */
    public List<OrderBean> listExchangeOrder;

    /**
     * 退货单选中的商品列表
     */
    public List<OrderBean> listRefundOrder;

    /**
     * 还货单选中的商品列表
     */
    public List<OrderBean> listGiveBack;

    private DistributionActivity context;
    private CharacterParser characterParser;

    /**
     * 客户id
     */
    private String customerId;
    /**
     * 客户店名
     */
    private String customer_name;
    /**
     * 业务员id
     */
    private String salesmanid;
    private CustomerTaskBean bean;

    private UserBean userBean;

    /**
     * 如果订单价格为负数,计算时使用这个参数
     */
    private BigDecimal fOne = new BigDecimal("-1");

    private VaryViewHelper helper = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_distribution_submit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 4;

        userBean = UserInfoUtils.getUser(getActivity());

        helper = new VaryViewHelper(mLvDistributionOrder);

        adapter = new DistributionAdapter(this, userBean);
        mLvDistributionOrder.setAdapter(adapter);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        bean = (CustomerTaskBean) getActivity().getIntent().getSerializableExtra("bean");
        if (bean != null) {
            customerId = bean.getCc_id();
            customer_name = bean.getCc_name();
            salesmanid = bean.getCc_salesmanid();
        }

        context = (DistributionActivity) getActivity();

        indexOrder = BaseConfig.OrderType1;
        selectTab();

        queryOrderGrid();
    }

    /**
     * 查询订单列表
     */
    private void queryOrderGrid() {
        helper.showLoadingView();
        AsyncHttpUtil<OrderBean> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderBean.class, new IUpdateUI<OrderBean>() {
            @Override
            public void updata(OrderBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listGoodsOrder = jsonBean.getOrderList1();
                    listProcessOrder = jsonBean.getOrderList2();
                    listExchangeOrder = jsonBean.getOrderList4();
                    listRefundOrder = jsonBean.getOrderList3();
                    listGiveBack = jsonBean.getOrderList5();

                    showBeenDataTab();
                    updateTabNum();
                } else {
                    helper.showErrorView(jsonBean.getRepMsg(),new MyErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                helper.showErrorView(s.getDetail(),new MyErrorListener());
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getTaskDetail, L_RequestParams.getTaskDetail(userBean.getUserId(),
                customerId, salesmanid, ""), true);
    }

    public class MyErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            queryOrderGrid();
        }
    }

    /**
     * 显示有数据的列表
     */
    private void showBeenDataTab() {
        if (listGoodsOrder != null && listGoodsOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType1;
        } else if (listProcessOrder != null && listProcessOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType2;
        } else if (listExchangeOrder != null && listExchangeOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType4;
        } else if (listRefundOrder != null && listRefundOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType3;
        } else if (listGiveBack != null && listGiveBack.size() > 0) {
            indexOrder = BaseConfig.OrderType5;
        }

        selectTab();
    }

    /**
     * 更新数据
     */
    public void refreshData() {
        indexOrder = BaseConfig.OrderType1;

        updateAdapter();
        updateTabNum();
        selectTab();
    }

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form,R.id.tv_give_back_form})
    public void onClick(View view) {
        switch (view.getId()) {
            //1 普通 2 处理 3 退货 4 换货 5 还货
            case R.id.tv_order_form://订单
                indexOrder = 1;
                selectTab();
                break;
            case R.id.tv_process_sheet_form://处理单
                indexOrder = 2;
                selectTab();
                break;
            case R.id.tv_exchange_form://换货单
                indexOrder = 4;
                selectTab();
                break;
            case R.id.tv_refund_form://退货单
                indexOrder = 3;
                selectTab();
                break;
            case R.id.tv_give_back_form://还货单
                indexOrder = 5;
                selectTab();
                break;
        }
    }

    @Override
    public void noDistribution(final int i) {
        DialogUtil.showCustomDialog(getActivity(), "暂不配送", UIUtils.getString(R.string.no_distribution),
                "暂不配送", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        adapter.list.remove(i);
                        adapter.notifyDataSetChanged();
                        updateTabNum();
                        if (adapter.list == null || adapter.list.size() == 0){
                            helper.showEmptyView();//显示没有数据提示
                        }
                    }

                    @Override
                    public void no() {

                    }
                });
    }

    @Override
    public void clearZero(final int i) {
        if ("2".equals(adapter.list.get(i).getFlag())){//是否允许修改数量 1 允许 2 不允许
            DialogUtil.showCustomDialog(getActivity(),"提示","参与活动的订单商品不允许修改商品数量","确定",null,null);
            return;
        }

        DialogUtil.showCustomDialog(getActivity(), "整单清零", UIUtils.getString(R.string.whole_single_reset),
                "整单清零", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        OrderBean bean = adapter.list.get(i);
                        bean.setOm_order_amount("0.00");
                        for (DistributionGoodsBean goodsBean : bean.getOrderDetailVoList()) {
                            if ("2".equals(goodsBean.getOd_goodstype())) {//getOd_goodstype商品类型 1 普通 2 赠品
                                continue;
                            }
                            goodsBean.setOd_goods_num_max("0");
                            goodsBean.setOd_goods_num_min("0");
                            goodsBean.setOd_real_total("0.00");
                        }
                        adapter.notifyDataSetInvalidated();
                    }

                    @Override
                    public void no() {

                    }
                });
    }

    @Override
    public void onSubmitOrder(int i) {
        Intent intent = new Intent(getActivity(), OrderSettlementActivity.class);
        OrderBean bean = null;
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                bean = listGoodsOrder.get(i);
                intent.putExtra("amount", bean.getOm_order_amount());
                break;
            case BaseConfig.OrderType2:
                bean = listProcessOrder.get(i);
                intent.putExtra("amount", bean.getOm_order_amount());
                break;
            case BaseConfig.OrderType4:
                bean = listExchangeOrder.get(i);
                intent.putExtra("amount", "0.00");
                break;
            case BaseConfig.OrderType3:
                bean = listRefundOrder.get(i);
                BigDecimal orderAmount = new BigDecimal(bean.getOm_order_amount());
                if (orderAmount.compareTo(BigDecimal.ZERO) == 1) {
                    intent.putExtra("amount",
                            orderAmount.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    intent.putExtra("amount", bean.getOm_order_amount());
                }
                break;
            case BaseConfig.OrderType5:
                bean = listGiveBack.get(i);
                intent.putExtra("amount", "0.00");
                break;
        }

        intent.putExtra("listOrderGoods", (Serializable) bean);
        intent.putExtra("indexOrder", indexOrder);
        intent.putExtra("orderType", 3);
        intent.putExtra("listIndex", i);//集合下标
        intent.putExtra("taskCustomer",this.bean);
        startActivity(intent);
    }

    @Override
    public void onOrderRejected(final int i) {
        DialogUtil.showCustomDialog(getActivity(), "提示", "是否拒收这笔订单?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                updateRejectionOrder(i);
            }

            @Override
            public void no() {

            }
        });
    }

    @Override
    public void onMaxPlus(int i, int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        plusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_max(mEtMaxNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMaxMinus(int i, int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        minusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_max(mEtMaxNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMinPlus(int i, int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        plusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_min(mEtMinNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onMinMinus(int i, int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        minusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_min(mEtMinNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onClearNum(int i, int k, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        mEtMaxNum.setText("0");
        mEtMinNum.setText("0");
        tvSumPrice.setText("0.00");
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_max("0");
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_min("0");
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_real_total("0.00");
        amountMoney(i, mTvOrderAmount);
    }

    @Override
    public void onMaxEtNum(int i, int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_max(newStr);
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
    }

    @Override
    public void onMinEtNum(int i, int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_goods_num_min(newStr);
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
    }

    @Override
    public void onMaxPriceChild(int i, int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        if (".".equals(mEtMaxPrice.getText().toString())) {
            return;
        }
        BigDecimal bdMaxPrice;
        if (TextUtils.isEmpty(mEtMaxPrice.getText())) {
            bdMaxPrice = BigDecimal.ZERO;
        } else {
            bdMaxPrice = new BigDecimal(mEtMaxPrice.getText().toString());
        }

        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_price_max(bdMaxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        calculatePrice(i, k, mEtMaxPrice, mEtMinPrice, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
    }

    @Override
    public void onMinPriceChild(int i, int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        if (".".equals(mEtMinPrice.getText().toString())) {
            return;
        }

        BigDecimal bdMinPrice;
        if (TextUtils.isEmpty(mEtMinPrice.getText())) {
            bdMinPrice = BigDecimal.ZERO;
        } else {
            bdMinPrice = new BigDecimal(mEtMinPrice.getText().toString());
        }

        adapter.list.get(i).getOrderDetailVoList().get(k).setOd_price_min(bdMinPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        calculatePrice(i, k, mEtMaxPrice, mEtMinPrice, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
    }

    private void updateRejectionOrder(final int i) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    adapter.list.remove(i);
                    adapter.notifyDataSetChanged();
                    updateTabNum();
                    if (adapter.list == null || adapter.list.size() == 0) {//如果订单没有,发送消息清楚客户列表信息
                        EventBus.getDefault().post(new OrderRejectedEvent());
                        helper.showEmptyView();//显示没有数据提示
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.updateRejectionOrder, L_RequestParams.
                updateRejectionOrder(getActivity(), adapter.list.get(i).getOm_id(), adapter.list.get(i).getOm_version()), true);
    }

    /**
     * 计算adapter中item商品价格
     */
    private void calculatePrice(int i, int k, EditText maxPrice, EditText minPice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格

        if (TextUtils.isEmpty(maxPrice.getText())) {
            bdMaxPrice = BigDecimal.ZERO;
        } else {
            bdMaxPrice = new BigDecimal(maxPrice.getText().toString());
        }

        if (TextUtils.isEmpty(minPice.getText())) {
            bdMinPrice = BigDecimal.ZERO;
        } else {
            bdMinPrice = new BigDecimal(minPice.getText().toString());
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

        //订单的总金额
        BigDecimal totalAmount = new BigDecimal(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_real_total());

        bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));

        if (bdSumPrice.compareTo(BigDecimal.ZERO) == 0) {
            tvSumPrice.setText("0.00");
            adapter.getItem(i).getOrderDetailVoList().get(k).setOd_real_total("0.00");
        } else {
            if (totalAmount.compareTo(BigDecimal.ZERO) == -1) {
                tvSumPrice.setText(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getOrderDetailVoList().get(k).
                        setOd_real_total(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                tvSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getOrderDetailVoList().get(k).
                        setOd_real_total(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }

        amountMoney(i, mTvOrderAmount);
    }

    /**
     * 计算adapter中item商品价格
     */
    private void calculatePrice(int i, int k, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格

        if (TextUtils.isEmpty(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_price_max())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_price_max());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_price_min())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_price_min());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        if ("1".equals(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_unit_num())) {//大小单位换算比1==1,只显示小单位
            bdSumPrice = bdMinPrice.multiply(bdMinNum);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
        }

        //订单的总金额
        BigDecimal totalAmount = new BigDecimal(adapter.getItem(i).getOrderDetailVoList().get(k).getOd_real_total());

        if (bdSumPrice.compareTo(BigDecimal.ZERO) == 0) {//单件商品总价格==0
            String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).getOrderDetailVoList().get(k).setOd_real_total(sumPrice);
        } else {
            if (totalAmount.compareTo(BigDecimal.ZERO) == -1) {
                tvSumPrice.setText(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getOrderDetailVoList().get(k).
                        setOd_real_total(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                tvSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getOrderDetailVoList().get(k).
                        setOd_real_total(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }

        amountMoney(i, mTvOrderAmount);
    }

    /**
     * 计算这一订单的总价格
     */
    private void amountMoney(int i, TextView mTvOrderAmount) {
        if (adapter.list.get(i).getOrderDetailVoList().size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            LogUtils.e("bdSumPrice", adapter.list.get(i).getOm_order_amount());
            BigDecimal orderAmount = new BigDecimal(adapter.list.get(i).getOm_order_amount());

            for (DistributionGoodsBean bean : adapter.list.get(i).getOrderDetailVoList()) {
                BigDecimal bdPrice = new BigDecimal(bean.getOd_real_total());
                sum = sum.add(bdPrice);
            }
            if (indexOrder == BaseConfig.OrderType3) {
                if (orderAmount.compareTo(BigDecimal.ZERO) == 0 || orderAmount.compareTo(BigDecimal.ZERO) == 1) {
                    mTvOrderAmount.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    if (sum.compareTo(BigDecimal.ZERO) == -1 || sum.compareTo(BigDecimal.ZERO) == 0) {
                        mTvOrderAmount.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        mTvOrderAmount.setText("-" + sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
            } else {
                mTvOrderAmount.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        } else {
            mTvOrderAmount.setText("0.00");
        }
        adapter.list.get(i).setOm_order_amount(mTvOrderAmount.getText().toString());
    }

    public void onEventMainThread(DistributionEvent event) {
        int orderType = event.getOrderType();
        int listIndex = event.getListIndex();

        indexOrder = orderType;
        switch (orderType) {
            case BaseConfig.OrderType1:
                listGoodsOrder.remove(listIndex);
                break;
            case BaseConfig.OrderType2:
                listProcessOrder.remove(listIndex);
                break;
            case BaseConfig.OrderType4:
                listExchangeOrder.remove(listIndex);
                break;
            case BaseConfig.OrderType3:
                listRefundOrder.remove(listIndex);
                break;
            case BaseConfig.OrderType5:
                listGiveBack.remove(listIndex);
                break;
        }
        updateAdapter();
        updateTabNum();

        //判断那种状态下的订单集合有数据
        if (listGoodsOrder.size() > 0 && orderType == BaseConfig.OrderType1) {
            onClick(mTvOrderForm);
        } else if (listProcessOrder.size() > 0 && orderType == BaseConfig.OrderType2) {
            onClick(mTvProcessSheetForm);
        } else if (listExchangeOrder.size() > 0 && orderType == BaseConfig.OrderType4) {
            onClick(mTvExchangeForm);
        } else if (listRefundOrder.size() > 0 && orderType == BaseConfig.OrderType3) {
            onClick(mTvRefundForm);
        } else if (listGiveBack.size() > 0 && orderType == BaseConfig.OrderType3){
            onClick(mTvGiveBackForm);
        }

        //没有数据直接跳转到主界面
        if (listGoodsOrder.size() == 0 && listProcessOrder.size() == 0 && listExchangeOrder.size() == 0
                && listRefundOrder.size() == 0 && listGiveBack.size() == 0) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finishActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                et.setText("");
            } else if (num - 1 == 0) {
                et.setText("");
            } else {
                et.setText(num - 1 + "");
            }
        }
    }

    /**
     * 根据订单状态刷新选中商品
     */
    private void updateAdapter() {
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                adapter.setData(listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                adapter.setData(listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                adapter.setData(listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                adapter.setData(listExchangeOrder);
                break;
            case BaseConfig.OrderType5:
                adapter.setData(listGiveBack);
                break;
        }
        if (adapter.list==null || adapter.list.size()==0){
            helper.showEmptyView();
        }else{
            helper.showDataView();
        }
    }

    /**
     * 计算选中商品列表的价格
     */
    private void amountMoney(List<OrderBean> list) {
        if (list != null && list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (OrderBean bean : list) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
            mTvTotalPrice.setText("¥:" + sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
        } else {
            mTvTotalPrice.setText("¥:0.00元");
        }
    }

    /**
     * 计算所有订单状态的价格
     */
    private double allAmountMoney() {
        BigDecimal sum = BigDecimal.ZERO;
        if (listGoodsOrder.size() > 0) {
            for (OrderBean bean : listGoodsOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
        }

        if (listProcessOrder.size() > 0) {
            for (OrderBean bean : listProcessOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
        }

        if (listExchangeOrder.size() > 0) {
            for (OrderBean bean : listExchangeOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
        }

        if (listRefundOrder.size() > 0) {
            for (OrderBean bean : listRefundOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
        }

        if (listGiveBack.size() > 0) {
            for (OrderBean bean : listGiveBack) {
                BigDecimal bdPrice = new BigDecimal(bean.getOm_order_amount());
                sum = sum.add(bdPrice);
            }
        }
        return sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                mTvOrderForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvOrderForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                amountMoney(listGoodsOrder);
                moveAnimation(0);
                alterWidth(mTvOrderForm);
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvProcessSheetForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                amountMoney(listProcessOrder);
                moveAnimation(1);
                alterWidth(mTvProcessSheetForm);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvRefundForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                amountMoney(listRefundOrder);
                moveAnimation(3);
                alterWidth(mTvRefundForm);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvExchangeForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                amountMoney(listExchangeOrder);
                moveAnimation(2);
                alterWidth(mTvExchangeForm);
                break;
            case BaseConfig.OrderType5:
                mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvGiveBackForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                amountMoney(listGiveBack);
                moveAnimation(1);
                alterWidth(mTvGiveBackForm);
                break;
        }
        updateAdapter();
    }

    /**
     * 跟新tab显示选中商品数量
     */
    private void updateTabNum() {
        if (listGoodsOrder.size() > 0) {
            mTvOrderForm.setText("订单(" + listGoodsOrder.size() + ")");
        } else {
            mTvOrderForm.setText("订单");
        }

        if (listProcessOrder.size() > 0) {
            mTvProcessSheetForm.setText("处理单(" + listProcessOrder.size() + ")");
        } else {
            mTvProcessSheetForm.setText("处理单");
        }

        if (listRefundOrder.size() > 0) {
            mTvRefundForm.setText("退货单(" + listRefundOrder.size() + ")");
        } else {
            mTvRefundForm.setText("退货单");
        }

        if (listExchangeOrder.size() > 0) {
            mTvExchangeForm.setText("换货单(" + listExchangeOrder.size() + ")");
        } else {
            mTvExchangeForm.setText("换货单");
        }

        if (listGiveBack.size() > 0) {
            mTvGiveBackForm.setText("还货单(" + listGiveBack.size() + ")");
        } else {
            mTvGiveBackForm.setText("还货单");
        }

    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvOrderForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvProcessSheetForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvExchangeForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvRefundForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvGiveBackForm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 4
                                * index).setDuration(300).start();
    }

}
