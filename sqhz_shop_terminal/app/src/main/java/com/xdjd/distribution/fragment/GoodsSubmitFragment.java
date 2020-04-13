package com.xdjd.distribution.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.xdjd.distribution.activity.OrderDeclareActivity;
import com.xdjd.distribution.activity.OrderSettlementActivity;
import com.xdjd.distribution.adapter.OrderDeclareGoodsAdapter;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.event.OrderDeclareEvent;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.quickindex.CharacterParser;

import java.io.Serializable;
import java.math.BigDecimal;
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
 *     desc   : 订单申报 -- 商品提交界面
 *     version: 1.0
 * </pre>
 */

public class GoodsSubmitFragment extends BaseFragment implements OrderDeclareGoodsAdapter.OrderDeclareGoodsListener {

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
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_amount_desc)
    TextView mTvAmountDesc;

    private OrderDeclareGoodsAdapter adapter;
    private List<String> list = new ArrayList<>();

    private View view;

    private GoodsEditFragment fragmentEdit;

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
     * 接口有数量和价格的数据
     */
    private List<GoodsBean> listCopy = new ArrayList<>();

    public List<GoodsBean> listGoodsOrderCopy = new ArrayList<>();
    public List<GoodsBean> listProcessOrderCopy = new ArrayList<>();
    public List<GoodsBean> listExchangeOrderCopy = new ArrayList<>();
    public List<GoodsBean> listRefundOrderCopy = new ArrayList<>();

    private OrderDeclareActivity context;

    private CharacterParser characterParser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods_submit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 4;

        adapter = new OrderDeclareGoodsAdapter(this);
        mLvOrderDeclareGoods.setAdapter(adapter);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        context = (OrderDeclareActivity) getActivity();
        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;

        mTvAmountDesc.setVisibility(View.VISIBLE);

        selectTab();
        updateTabNum();
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

        if (listProcessOrder.size() > 0) {
            for (GoodsBean bean : listProcessOrder) {
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

        mTvTotalPrice.setText(xAmount.subtract(tAmount).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        mTvAmountDesc.setText("销:" + xAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                " -退:"+tAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
    }

    /**
     * 更新数据
     */
    public void refreshData(GoodsEditFragment fragmentEdit,int index) {
        this.fragmentEdit = fragmentEdit;

        indexOrder = index;

        listGoodsOrder = context.listGoodsOrder;
        listProcessOrder = context.listProcessOrder;
        listExchangeOrder = context.listExchangeOrder;
        listRefundOrder = context.listRefundOrder;
        selectTab();

        updateTabNum();
    }

    @OnClick({R.id.tv_order_form, R.id.tv_process_sheet_form, R.id.tv_exchange_form, R.id.tv_refund_form, R.id.tv_submit})
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
            case R.id.tv_submit:
                if (allAmountMoney() <= 0) {
                    stopSubmit("");
                    return;
                }

                if (removeZeroPriceGoods()) {
                    return;
                }

                List<GoodsBean> listGoods = null;
                switch (indexOrder) {
                    case BaseConfig.OrderType1:
                        listGoods = context.listGoodsOrder;
                        break;
                    case BaseConfig.OrderType2:
                        listGoods = context.listProcessOrder;
                        break;
                    case BaseConfig.OrderType4:
                        listGoods = context.listExchangeOrder;
                        break;
                    case BaseConfig.OrderType3:
                        listGoods = context.listRefundOrder;
                        break;
                }

                listCopy.clear();
                for (int i = 0; i < listGoods.size(); i++) {
                    BigDecimal bdPrice = new BigDecimal(listGoods.get(i).getTotalPrice());
                    if (bdPrice.doubleValue() > 0) {
                        listCopy.add(listGoods.get(i));
                    }
                }

                BigDecimal sum = BigDecimal.ZERO;
                for (GoodsBean bean : listGoods) {
                    BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                    sum = sum.add(bdPrice);
                }

                Intent intent = new Intent(getActivity(), OrderSettlementActivity.class);
                intent.putExtra("list", (Serializable) listCopy);
                intent.putExtra("om_ordertype", indexOrder);
                intent.putExtra("storehouseId", context.storehouseId);
                intent.putExtra("deliveryTime", context.deliveryTime);
                intent.putExtra("note", context.note);
                intent.putExtra("indexOrder",indexOrder);
                startActivity(intent);
                break;
        }
    }

    public void onEventMainThread(OrderDeclareEvent event) {
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
        DialogUtil.showCustomDialog(getActivity(), "注意", "请录入"+message+"要申报的产品信息!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
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
        AnimUtils.setTranslateAnimation(mEtMaxNum,mLlMaxLeft,mRlMax, mIvMax);
    }

    @Override
    public void onMaxMinus(int i,RelativeLayout mRlMax, LinearLayout mLlMaxLeft,ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum,mLlMaxLeft,mRlMax, mIvMax);
    }

    @Override
    public void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin,EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.getItem(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum,mLlMinLeft,mRlMin, mIvMin);
    }

    @Override
    public void onMinMinus(int i,RelativeLayout mRlMin, LinearLayout mLlMinLeft,ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.getItem(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum,mLlMinLeft,mRlMin, mIvMin);
    }

    @Override
    public void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        mEtMaxNum.setText("0");
        mEtMinNum.setText("0");
        tvSumPrice.setText("¥0.00元");
        adapter.getItem(i).setMaxNum("0");
        adapter.getItem(i).setMinNum("0");
        adapter.getItem(i).setTotalPrice("0.00");
//        amountMoney(adapter.list);
        totalPrice();
    }

    @Override
    public void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.getItem(i).setMaxNum(newStr);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    @Override
    public void onMinEtNum(int i,String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
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
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;//    总价格

        //1 都有 2 小单位 3 大单位
        if ("1".equals(adapter.getItem(i).getUnit_type()) || "".equals(adapter.getItem(i).getUnit_type()) || null == adapter.getItem(i).getUnit_type()) {
            bdMaxPrice = new BigDecimal(adapter.getItem(i).getMaxPrice());
            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
            }

            bdMinPrice = new BigDecimal(adapter.getItem(i).getMinPrice());
            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
            }

            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
        } else if ("2".equals(adapter.getItem(i).getUnit_type())) {
            bdMinPrice = new BigDecimal(adapter.getItem(i).getMinPrice());
            if (TextUtils.isEmpty(mEtMinNum.getText())) {
                bdMinNum = new BigDecimal(0);
            } else {
                bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
            }

            bdSumPrice = bdMinPrice.multiply(bdMinNum);
        } else if ("3".equals(adapter.getItem(i).getUnit_type())) {
            bdMaxPrice = new BigDecimal(adapter.getItem(i).getMaxPrice());
            if (TextUtils.isEmpty(mEtMaxNum.getText())) {
                bdMaxNum = new BigDecimal(0);
            } else {
                bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
            }

            bdSumPrice = bdMaxPrice.multiply(bdMaxNum);
        }
        if (bdSumPrice.compareTo(BigDecimal.ZERO) == 0) {
            tvSumPrice.setText("0.00");
            adapter.getItem(i).setTotalPrice("0.00");
        } else {
            tvSumPrice.setText(bdSumPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            adapter.getItem(i).setTotalPrice(bdSumPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }

//        amountMoney(adapter.list);
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
     * 计算选中商品列表的价格
     */
//    private void amountMoney(List<GoodsBean> list) {
//        if (list.size() > 0) {
//            BigDecimal sum = BigDecimal.ZERO;
//            for (GoodsBean bean : list) {
//                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
//                sum = sum.add(bdPrice);
//            }
//            mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//        } else {
//            mTvTotalPrice.setText("0.00");
//        }
//    }

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
        return sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 删除价格为0的商品
     */
    private boolean removeZeroPriceGoods() {
        BigDecimal sum = BigDecimal.ZERO;
        //1 销售 2 处理 3 退货 4 换货
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                if (listGoodsOrder.size() > 0) {
                    for (GoodsBean bean : listGoodsOrder) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }
                }

                if (listGoodsOrder.size() > 0 && sum.doubleValue() > 0) {
                    //                    for (int i = 0; i<listGoodsOrder.size();i++) {
                    //                        BigDecimal bdPrice = new BigDecimal(listGoodsOrder.get(i).getTotalPrice());
                    //                        if (bdPrice.doubleValue() == 0){
                    //                            listGoodsOrder.remove(i);
                    //                        }
                    //                    }
                } else {
                    stopSubmit("销售单");
                    return true;
                }
                break;
            case BaseConfig.OrderType2:
                if (listProcessOrder.size() > 0) {
                    for (GoodsBean bean : listProcessOrder) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }
                }

                if (listProcessOrder.size() > 0 && sum.doubleValue() > 0) {
                    //                    for (int i = 0; i<listProcessOrder.size();i++) {
                    //                        BigDecimal bdPrice = new BigDecimal(listProcessOrder.get(i).getTotalPrice());
                    //                        if (bdPrice.doubleValue() == 0){
                    //                            listProcessOrder.remove(i);
                    //                        }
                    //                    }
                } else {
                    stopSubmit("处理单");
                    return true;
                }
                break;
            case BaseConfig.OrderType4:
                if (listExchangeOrder.size() > 0) {
                    for (GoodsBean bean : listExchangeOrder) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }
                }

                if (listExchangeOrder.size() > 0 && sum.doubleValue() > 0) {
                    //                    for (int i = 0; i<listExchangeOrder.size();i++) {
                    //                        BigDecimal bdPrice = new BigDecimal(listExchangeOrder.get(i).getTotalPrice());
                    //                        if (bdPrice.doubleValue() == 0){
                    //                            listExchangeOrder.remove(i);
                    //                        }
                    //                    }
                } else {
                    stopSubmit("换货单");
                    return true;
                }
                break;
            case BaseConfig.OrderType3:
                if (listRefundOrder.size() > 0) {
                    for (GoodsBean bean : listRefundOrder) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }
                }

                if (listRefundOrder.size() > 0 && sum.doubleValue() > 0) {
                    //                    for (int i = 0; i<listRefundOrder.size();i++) {
                    //                        BigDecimal bdPrice = new BigDecimal(listRefundOrder.get(i).getTotalPrice());
                    //                        if (bdPrice.doubleValue() == 0){
                    //                            listRefundOrder.remove(i);
                    //                        }
                    //                    }
                } else {
                    stopSubmit("退货单");
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (indexOrder) {
            case BaseConfig.OrderType1:
                mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(0);
                mTvSelectedNum.setText("已选(" + context.listGoodsOrder.size() + "件)");
//                amountMoney(context.listGoodsOrder);
                break;
            case BaseConfig.OrderType2:
                mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(1);
                mTvSelectedNum.setText("已选(" + context.listProcessOrder.size() + "件)");
//                amountMoney(context.listProcessOrder);
                break;
            case BaseConfig.OrderType3:
                mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(3);
                mTvSelectedNum.setText("已选(" + context.listRefundOrder.size() + "件)");
//                amountMoney(context.listRefundOrder);
                break;
            case BaseConfig.OrderType4:
                mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                moveAnimation(2);
                mTvSelectedNum.setText("已选(" + context.listExchangeOrder.size() + "件)");
//                amountMoney(context.listExchangeOrder);
                break;
        }
        totalPrice();
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
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvOrderForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvOrderForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvProcessSheetForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvProcessSheetForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvExchangeForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvExchangeForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        mTvRefundForm.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvRefundForm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 4
                                * index).setDuration(200).start();
    }

}
