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
import com.xdjd.distribution.activity.OrderSettlementActivity;
import com.xdjd.distribution.activity.SalesOutboundActivity;
import com.xdjd.distribution.adapter.SalesOutboundAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderListBean;
import com.xdjd.distribution.event.SalesOutboundEvent;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.quickindex.CharacterParser;

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
 *     time   : 2017/5/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SalesOutboundSumbitFragment extends BaseFragment implements SalesOutboundAdapter.SalesOutboundListener {

    @BindView(R.id.lv_order_declare_goods)
    ListView mLvOrderDeclareGoods;
    @BindView(R.id.tv_order_form)
    TextView mTvOrderForm;
    @BindView(R.id.tv_process_sheet_form)
    TextView mTvProcessSheetForm;
    @BindView(R.id.tv_exchange_form)
    TextView mTvExchangeForm;
    @BindView(R.id.tv_refund_form)
    TextView mTvRefundForm;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_amount_desc)
    TextView mTvAmountDesc;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_give_back_form)
    TextView mTvGiveBackForm;

    private SalesOutboundAdapter adapter;
    private View view;
    private SalesOutboundEditFragment fragmentEdit;

    public int indexOrder = 1;
    /**
     * 订单选中的商品列表
     */
    public List<GoodsBean> listGoodsOrder;

    /**
     * 处理单选中的商品列表
     */
    public List<GoodsBean> listProcessOrder;

    /**
     * 换货单选中的商品列表
     */
    public List<GoodsBean> listExchangeOrder;

    /**
     * 退货单选中的商品列表
     */
    public List<GoodsBean> listRefundOrder;
    /**
     * 还货单选中的商品列表
     */
    public List<GoodsBean> listGiveBack;

    private SalesOutboundActivity context;

    private CharacterParser characterParser;

    private ClientBean mClientBean;

    /**
     * 接口有数量和价格的数据
     */
    private List<GoodsBean> listCopy = new ArrayList<>();

    //组装数据集合
    List<OrderListBean> listAssemble = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales_outbound_sumbit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 4;

        adapter = new SalesOutboundAdapter(this);
        mLvOrderDeclareGoods.setAdapter(adapter);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        mClientBean = UserInfoUtils.getClientInfo(getActivity());

        context = (SalesOutboundActivity) getActivity();
        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;
        listGiveBack = context.listGiveBack;

        indexOrder = BaseConfig.OrderType1;

        mTvAmountDesc.setVisibility(View.VISIBLE);
        selectTab();
        updateTabNum();
    }

    /**
     * 更新数据
     */
    public void refreshData(SalesOutboundEditFragment fragmentEdit) {
        this.fragmentEdit = fragmentEdit;

        indexOrder = fragmentEdit.indexOrder;

        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;
        listGiveBack = context.listGiveBack;

        updateTabNum();
        selectOrderIndex();
    }

    /**
     * 计算总价格描述--销: 退:
     */
    private void totalPrice() {
        BigDecimal xAmount = BigDecimal.ZERO;//销售价格
        BigDecimal tAmount = BigDecimal.ZERO;//退货价格
        if (listGoodsOrder.size() > 0) {
            for (GoodsBean bean : listGoodsOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                xAmount = xAmount.add(bdPrice);
            }
        }

        if (listRefundOrder.size() > 0) {
            for (GoodsBean bean : listRefundOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                tAmount = tAmount.add(bdPrice);
            }
        }

        mTvTotalPrice.setText(xAmount.subtract(tAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        mTvAmountDesc.setText("销:" + xAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                " 退:-" + tAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    /**
     * 先显示有数据的列表
     */
    private void selectOrderIndex() {
        if (listGoodsOrder != null && listGoodsOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType1;
        } else if (listProcessOrder != null && listProcessOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType2;
        } else if (listExchangeOrder != null && listExchangeOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType4;
        } else if (listRefundOrder != null && listRefundOrder.size() > 0) {
            indexOrder = BaseConfig.OrderType3;
        }

        selectTab();
    }

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form, R.id.tv_submit, R.id.tv_give_back_form})
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
            case R.id.tv_submit:
                submitOrder();
                break;
        }
    }

    public void onEventMainThread(SalesOutboundEvent event) {
        int orderType = event.getOrderType();
        switch (orderType) {
            case BaseConfig.OrderType1:
                context.listGoodsOrder.clear();
                listGoodsOrder = context.listGoodsOrder;
                break;
            case BaseConfig.OrderType2:
                context.listProcessOrder.clear();
                listProcessOrder = context.listProcessOrder;
                break;
            case BaseConfig.OrderType4:
                context.listExchangeOrder.clear();
                listExchangeOrder = context.listExchangeOrder;
                break;
            case BaseConfig.OrderType3:
                context.listRefundOrder.clear();
                listRefundOrder = context.listRefundOrder;
                break;
        }
        updateAdapter();
        updateTabNum();

        //判断那种状态下的订单集合有数据
        if (listGoodsOrder.size() > 0) {
            onClick(mTvOrderForm);
        } else if (listProcessOrder.size() > 0) {
            onClick(mTvProcessSheetForm);
        } else if (listExchangeOrder.size() > 0) {
            onClick(mTvExchangeForm);
        } else if (listRefundOrder.size() > 0) {
            onClick(mTvRefundForm);
        }

        //没有数据直接跳转到主界面
        if (listGoodsOrder.size() == 0 && listProcessOrder.size() == 0 && listExchangeOrder.size() == 0
                && listRefundOrder.size() == 0) {
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
     * 停止提交提示
     */
    private void stopSubmit(String message) {
        DialogUtil.showCustomDialog(getActivity(), "注意", "请录入" + message + "要申报的产品信息!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {

            }

            @Override
            public void no() {

            }
        });
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
    public void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        mEtMaxNum.setText("0");
        mEtMinNum.setText("0");
        tvSumPrice.setText("0.00元");
        adapter.getItem(i).setMaxNum("0");
        adapter.getItem(i).setMinNum("0");
        adapter.getItem(i).setTotalPrice("0.00");
        amountMoney(adapter.list);
        totalPrice();
    }

    @Override
    public void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.getItem(i).setMaxNum(newStr);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    @Override
    public void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.getItem(i).setMinNum(newStr);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
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
        BigDecimal bdMaxPrice = null;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice = null;
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
        amountMoney(adapter.list);
        totalPrice();
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
    }

    public void searchGoods(String s) {
        filterData(s);
    }

    /**
     * 根据条件进行模糊查询
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GoodsBean> filterDateList = new ArrayList<GoodsBean>();

        if (TextUtils.isEmpty(filterStr)) {
            updateAdapter();
        } else {
            filterDateList.clear();
            List<GoodsBean> list = null;
            switch (indexOrder) {
                case BaseConfig.OrderType1:
                    list = listGoodsOrder;
                    break;
                case BaseConfig.OrderType2:
                    list = listProcessOrder;
                    break;
                case BaseConfig.OrderType3:
                    list = listRefundOrder;
                    break;
                case BaseConfig.OrderType4:
                    list = listExchangeOrder;
                    break;
            }
            for (GoodsBean sortModel : list) {
                String name = sortModel.getGg_title();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }

            adapter.setData(filterDateList);
            if (filterDateList.size() == 0) {
                //			tvNofriends.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addGoods(GoodsBean bean) {
        List<GoodsBean> filterDateList = new ArrayList<GoodsBean>();

        List<GoodsBean> list = null;
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                list = context.listGoodsOrder;
                break;
            case BaseConfig.OrderType2:
                list = context.listProcessOrder;
                break;
            case BaseConfig.OrderType3:
                list = context.listRefundOrder;
                break;
            case BaseConfig.OrderType4:
                list = context.listExchangeOrder;
                break;
        }
        for (GoodsBean sortModel : list) {
            if (bean.getGgp_id().equals(sortModel.getGgp_id())) {
                filterDateList.add(sortModel);
            }
        }

        if (filterDateList.size() == 0) {
            adapter.setData(list);
        } else {
            adapter.setData(filterDateList);
        }
    }

    /**
     * 计算所有订单状态的价格
     */
    private double allAmountMoney() {
        BigDecimal sum = BigDecimal.ZERO;
        if (listGoodsOrder.size() > 0) {
            for (GoodsBean bean : listGoodsOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        if (listProcessOrder.size() > 0) {
            for (GoodsBean bean : listProcessOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        if (listExchangeOrder.size() > 0) {
            for (GoodsBean bean : listExchangeOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        if (listRefundOrder.size() > 0) {
            for (GoodsBean bean : listRefundOrder) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        if (listGiveBack.size() > 0) {
            for (GoodsBean bean : listGiveBack) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }
        return sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 组装提交商品集合数据和判断商品总数量是否为空
     */
    private void submitOrder() {
        if (allAmountMoney() <= 0) {
            stopSubmit("");
            return;
        }

        listCopy.clear();
        listAssemble.clear();

        BigDecimal totalNum = BigDecimal.ZERO;//商品总数量

        BigDecimal maxNum;
        BigDecimal minNum;

        BigDecimal goodsOrderNum = null;//销售商品总数量
        BigDecimal processOrderNum = null;//处理商品总数量
        BigDecimal exchangeOrderNum = null;//换货单商品总数量
        BigDecimal refundOrderNum = null;//退货单商品总数量
        BigDecimal giveBackNum = null;//还货单商品总数量

        //1 销售 2 处理 3 退货 4 换货
        if (listGoodsOrder.size() > 0) {//销售
            for (GoodsBean bean : listGoodsOrder) {
                if (bean.getMaxNum() == null || bean.getMaxNum().length() == 0) {
                    maxNum = BigDecimal.ZERO;
                } else {
                    maxNum = new BigDecimal(bean.getMaxNum());
                }

                if (bean.getMinNum() == null || bean.getMinNum().length() == 0) {
                    minNum = BigDecimal.ZERO;
                } else {
                    minNum = new BigDecimal(bean.getMinNum());
                }

                //换算单位数量
                BigDecimal bgUnitNum = new BigDecimal(bean.getGgp_unit_num());
                goodsOrderNum = maxNum.multiply(bgUnitNum).add(minNum);

                totalNum = totalNum.add(goodsOrderNum);
            }
        }

        if (listProcessOrder.size() > 0) {//处理
            for (GoodsBean bean : listProcessOrder) {
                if (bean.getMaxNum() == null || bean.getMaxNum().length() == 0) {
                    maxNum = BigDecimal.ZERO;
                } else {
                    maxNum = new BigDecimal(bean.getMaxNum());
                }

                if (bean.getMinNum() == null || bean.getMinNum().length() == 0) {
                    minNum = BigDecimal.ZERO;
                } else {
                    minNum = new BigDecimal(bean.getMinNum());
                }

                //换算单位数量
                BigDecimal bgUnitNum = new BigDecimal(bean.getGgp_unit_num());
                processOrderNum = maxNum.multiply(bgUnitNum).add(minNum);

                totalNum = totalNum.add(processOrderNum);
            }
        }

        if (listExchangeOrder.size() > 0) {//换货
            for (GoodsBean bean : listExchangeOrder) {
                if (bean.getMaxNum() == null || bean.getMaxNum().length() == 0) {
                    maxNum = BigDecimal.ZERO;
                } else {
                    maxNum = new BigDecimal(bean.getMaxNum());
                }

                if (bean.getMinNum() == null || bean.getMinNum().length() == 0) {
                    minNum = BigDecimal.ZERO;
                } else {
                    minNum = new BigDecimal(bean.getMinNum());
                }
                //换算单位数量
                BigDecimal bgUnitNum = new BigDecimal(bean.getGgp_unit_num());
                exchangeOrderNum = maxNum.multiply(bgUnitNum).add(minNum);

                totalNum = totalNum.add(exchangeOrderNum);
            }
        }

        if (listRefundOrder.size() > 0) {//退货
            for (GoodsBean bean : listRefundOrder) {
                if (bean.getMaxNum() == null || bean.getMaxNum().length() == 0) {
                    maxNum = BigDecimal.ZERO;
                } else {
                    maxNum = new BigDecimal(bean.getMaxNum());
                }

                if (bean.getMinNum() == null || bean.getMinNum().length() == 0) {
                    minNum = BigDecimal.ZERO;
                } else {
                    minNum = new BigDecimal(bean.getMinNum());
                }

                //换算单位数量
                BigDecimal bgUnitNum = new BigDecimal(bean.getGgp_unit_num());
                refundOrderNum = maxNum.multiply(bgUnitNum).add(minNum);

                totalNum = totalNum.add(refundOrderNum);
            }
        }

        if (listGiveBack.size() > 0) {//还货
            for (GoodsBean bean : listGiveBack) {
                if ("".equals(bean.getMaxNum()) || bean.getMaxNum() == null) {
                    maxNum = new BigDecimal(BigInteger.ZERO);
                } else {
                    maxNum = new BigDecimal(bean.getMaxNum());
                }

                if ("".equals(bean.getMinNum()) || bean.getMinNum() == null) {
                    minNum = new BigDecimal(BigInteger.ZERO);
                } else {
                    minNum = new BigDecimal(bean.getMinNum());
                }
                //换算单位数量
                BigDecimal bgUnitNum = new BigDecimal(bean.getGgp_unit_num());

                if (bean.getGgs_stock() == null || bean.getGgs_stock() == "") {
                    bean.setGgs_stock("0");
                }

                BigDecimal bgStock = new BigDecimal(bean.getGgs_stock());//库存数量
                BigDecimal bgNum = maxNum.multiply(bgUnitNum);//大单位换算成小单位总数量
                BigDecimal bgSumNum = bgNum.add(minNum);//大小单位数量总和

                if (bgStock.compareTo(bgSumNum) == -1 && indexOrder != BaseConfig.OrderType3) {
                    //判断是否超出库存数量
                    DialogUtil.showCustomDialog(getActivity(), "注意", bean.getGg_title() + "库存数量不足", "确定", null, null);
                    return;
                }

                //还货剩余数量计算
                BigDecimal surplusNum = new BigDecimal(bean.getOrder_surplus_num());
                if (bgSumNum.compareTo(surplusNum) == 1) {
                    DialogUtil.showCustomDialog(getActivity(), "注意",
                            bean.getGg_title() + ",订货" + bean.getOrder_surplus_num_str() + ",不足出库量", "确定", null, null);
                    return;
                }
                giveBackNum = maxNum.multiply(bgUnitNum).add(minNum);

                totalNum = totalNum.add(giveBackNum);
            }
        }

        if (totalNum.intValue() == 0 || totalNum.intValue() < 0) {
            stopSubmit("订单");
            return;
        }

        if (goodsOrderNum != null && goodsOrderNum.compareTo(BigDecimal.ZERO) == 1) {
            OrderListBean bean1 = new OrderListBean();
            bean1.setOrderType(BaseConfig.OrderType1);
            bean1.setListData(listGoodsOrder);
            listAssemble.add(bean1);
        }

        if (exchangeOrderNum !=null && exchangeOrderNum.compareTo(BigDecimal.ZERO) == 1) {
            OrderListBean bean2 = new OrderListBean();
            bean2.setOrderType(BaseConfig.OrderType4);
            bean2.setListData(listExchangeOrder);
            listAssemble.add(bean2);
        }

        if (refundOrderNum != null && refundOrderNum.compareTo(BigDecimal.ZERO) == 1) {
            OrderListBean bean3 = new OrderListBean();
            bean3.setOrderType(BaseConfig.OrderType3);
            bean3.setListData(listRefundOrder);
            listAssemble.add(bean3);
        }

        if (giveBackNum !=null && giveBackNum.compareTo(BigDecimal.ZERO) == 1) {
            OrderListBean bean5 = new OrderListBean();
            bean5.setOrderType(BaseConfig.OrderType5);
            bean5.setListData(listGiveBack);
            listAssemble.add(bean5);
        }

        Intent intent = new Intent(getActivity(), OrderSettlementActivity.class);
        intent.putExtra("list", (Serializable) listAssemble);
        intent.putExtra("storehouseId", context.storehouseId);//选择的仓库id
        intent.putExtra("deliveryTime", context.deliveryTime);//发货时间
        intent.putExtra("note", context.note);//备注信息
        intent.putExtra("indexOrder", indexOrder);
        intent.putExtra("orderType", 2);
        startActivity(intent);
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
                mTvSelectedNum.setText("已选(" + context.listGoodsOrder.size() + "件)");

                amountMoney(context.listGoodsOrder);
                moveAnimation(0);
                alterWidth(mTvOrderForm);
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvProcessSheetForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listProcessOrder.size() + "件)");
                amountMoney(context.listProcessOrder);
                moveAnimation(1);
                alterWidth(mTvProcessSheetForm);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvRefundForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listRefundOrder.size() + "件)");
                amountMoney(context.listRefundOrder);
                moveAnimation(3);
                alterWidth(mTvRefundForm);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvExchangeForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
                amountMoney(context.listExchangeOrder);
                moveAnimation(2);
                alterWidth(mTvExchangeForm);
                break;
            case BaseConfig.OrderType5:
                mTvGiveBackForm.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvGiveBackForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvGiveBackForm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                mTvSelectedNum.setText("已选(" + context.listGiveBack.size() + "件)");
                amountMoney(context.listGiveBack);
                moveAnimation(1);
                alterWidth(mTvGiveBackForm);
                break;
        }
        totalPrice();
        updateAdapter();
    }

    /**
     * 计算选中商品列表的价格
     */
    private void amountMoney(List<GoodsBean> list) {
        if (list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (GoodsBean bean : list) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
            mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvTotalPrice.setText("0.00");
        }

        mTvSelectedNum.setText("已选(" + list.size() + "件)");
    }

    /**
     * 跟新tab显示选中商品数量
     */
    private void updateTabNum() {
        if (listGoodsOrder.size() > 0) {
            mTvOrderForm.setText("销售(" + listGoodsOrder.size() + ")");
        } else {
            mTvOrderForm.setText("销售");
        }

        if (context.listProcessOrder.size() > 0) {
            mTvProcessSheetForm.setText("处理单(" + context.listProcessOrder.size() + ")");
        } else {
            mTvProcessSheetForm.setText("处理单");
        }

        if (context.listRefundOrder.size() > 0) {
            mTvRefundForm.setText("退货单(" + context.listRefundOrder.size() + ")");
        } else {
            mTvRefundForm.setText("退货单");
        }

        if (context.listExchangeOrder.size() > 0) {
            mTvExchangeForm.setText("换货单(" + context.listExchangeOrder.size() + ")");
        } else {
            mTvExchangeForm.setText("换货单");
        }

        if (context.listGiveBack.size() > 0) {
            mTvGiveBackForm.setText("还货单(" + context.listGiveBack.size() + ")");
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
