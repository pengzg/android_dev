package com.xdjd.distribution.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.xdjd.distribution.activity.ApplyOrderSettlementActivity;
import com.xdjd.distribution.activity.CommissionSalesActivity;
import com.xdjd.distribution.adapter.RefundAdapter;
import com.xdjd.distribution.adapter.recycleradapter.GoodsOrderSubmitAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
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

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CommissionSumbitFragment extends BaseFragment implements GoodsOrderSubmitAdapter.GoodsOrderSubmitListener {

    @BindView(R.id.lv_order_declare_goods)
    ListView mLvOrderDeclareGoods;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private View view;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    private UserBean userBean;

    /**
     * 仓库id
     */
    public String storehouseId = "";

    /**
     * 选中的商品
     */
    public List<GoodsBean> listSelectGoods = new ArrayList<>();
    private CommissionSalesActivity context;
    private CharacterParser characterParser;
    private GoodsOrderSubmitAdapter adapterSubmitAdapter;

    /**
     * 订单编号
     */
    private String orderCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_submit_commission, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData() {
        listSelectGoods = context.listCommissionGoods;
        updateAdapter();
        mTvTotalPrice.setText(allAmountMoney());
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;

        clientBean = UserInfoUtils.getClientInfo(getActivity());
        storehouseId = getActivity().getIntent().getStringExtra("storehouseId");

        context = (CommissionSalesActivity) getActivity();

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        listSelectGoods = context.listCommissionGoods;

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterSubmitAdapter = new GoodsOrderSubmitAdapter(getActivity(),listSelectGoods);
        adapterSubmitAdapter.setListener(this);
        mRecycler.setAdapter(adapterSubmitAdapter);
        //添加Android自带的分割线
        mRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        //添加自定义分割线
//        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
//        mRecycler.addItemDecoration(divider);
    }

    /**
     * 根据订单状态刷新选中商品
     */
    private void updateAdapter() {
        adapterSubmitAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                BigDecimal sum = BigDecimal.ZERO;
                if (listSelectGoods.size() > 0) {
                    for (GoodsBean bean : listSelectGoods) {
                        BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                        sum = sum.add(bdPrice);
                    }
                }
                if (sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() <= 0) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请录入要提交的产品信息!", "确定", null, null);
                    return;
                }

                Intent intent = new Intent(getActivity(), ApplyOrderSettlementActivity.class);
                intent.putExtra("list", (Serializable) listSelectGoods);
                intent.putExtra("storehouseId", context.storehouseId);
                intent.putExtra("businesstype", context.businesstype);
                intent.putExtra("amount", sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                intent.putExtra("customerId", clientBean.getCc_id());
                intent.putExtra("customer_name", clientBean.getCc_name());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onConnectionFinish() {
        super.onConnectionFinish();
        intentActivity();
    }

    private void intentActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finishActivity();
    }

    /**
     * 根据库存计算商品大单位数量和小单位数量
     *
     * @param bean
     */
    private void calculateGoods(GoodsBean bean) {
        BigDecimal bigStore;
        if (bean.getGgs_stock() != null && !"".equals(bean.getGgs_stock())) {
            bigStore = new BigDecimal(bean.getGgs_stock());
        } else {
            bigStore = new BigDecimal(BigInteger.ZERO);
        }

        if ("1".equals(bean.getGgp_unit_num())) {
            bean.setMaxNum("0");
            bean.setMinNum(bigStore.toString());
        } else {
            BigDecimal bigUnitNum = new BigDecimal(bean.getGgp_unit_num());

            BigDecimal[] results = bigStore.divideAndRemainder(bigUnitNum);
            bean.setMaxNum(String.valueOf(results[0].intValue()));
            bean.setMinNum(String.valueOf(results[1].intValue()));
        }

        bean.setMaxPrice(bean.getMax_price());
        bean.setMinPrice(bean.getMin_price());

        calculatePrice(bean);
    }

    /**
     * 根据计算单件商品总价格
     */
    private void calculatePrice(GoodsBean beanGoods) {
        BigDecimal bdMaxPrice;
        BigDecimal bdMaxNum;

        BigDecimal bdMinPrice;
        BigDecimal bdMinNum;

        BigDecimal bdSumPrice = BigDecimal.ZERO;


        if (TextUtils.isEmpty(beanGoods.getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(beanGoods.getMaxPrice());
        }

        if (TextUtils.isEmpty(beanGoods.getMaxNum())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(beanGoods.getMaxNum());
        }

        if (TextUtils.isEmpty(beanGoods.getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(beanGoods.getMinPrice());
        }
        if (TextUtils.isEmpty(beanGoods.getMinNum())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(beanGoods.getMinNum());
        }

        if ("1".equals(beanGoods.getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            beanGoods.setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            beanGoods.setTotalPrice(sumPrice);
        }
    }

    /**
     * 计算所有订单状态的价格
     */
    private String allAmountMoney() {
        listSelectGoods = context.listCommissionGoods;

        BigDecimal sum = BigDecimal.ZERO;
        if (listSelectGoods.size() > 0) {
            for (GoodsBean bean : listSelectGoods) {
                BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                sum = sum.add(bdPrice);
            }
        }

        return sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    public void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMaxNum, tvSumPrice);
//        adapter.getItem(i).setMaxNum(mEtMaxNum.getText().toString());
        adapterSubmitAdapter.list.get(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMaxNum, tvSumPrice);
        adapterSubmitAdapter.list.get(i).setMaxNum(mEtMaxNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        plusCalculation(i, mEtMinNum, tvSumPrice);
        adapterSubmitAdapter.list.get(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        minusCalculation(i, mEtMinNum, tvSumPrice);
        adapterSubmitAdapter.list.get(i).setMinNum(mEtMinNum.getText().toString());
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        mEtMaxNum.setText("0");
        mEtMinNum.setText("0");
        tvSumPrice.setText("¥0.00元");
        adapterSubmitAdapter.list.get(i).setMaxNum("0");
        adapterSubmitAdapter.list.get(i).setMinNum("0");
        adapterSubmitAdapter.list.get(i).setTotalPrice("0.00");
        mTvTotalPrice.setText(allAmountMoney());
    }

    @Override
    public void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        adapterSubmitAdapter.list.get(i).setMaxNum(num);
        calculatePrice(i, mEtMaxNum, mEtMinNum, tvSumPrice);
    }

    @Override
    public void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
        adapterSubmitAdapter.list.get(i).setMinNum(num);
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

        if (TextUtils.isEmpty(adapterSubmitAdapter.list.get(i).getMaxPrice())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapterSubmitAdapter.list.get(i).getMaxPrice());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapterSubmitAdapter.list.get(i).getMinPrice())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapterSubmitAdapter.list.get(i).getMinPrice());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        if ("1".equals(adapterSubmitAdapter.list.get(i).getGgp_unit_num())) {//大小单位换算比1==1,只显示小单位
            String sumPrice = bdMinPrice.multiply(bdMinNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapterSubmitAdapter.list.get(i).setTotalPrice(sumPrice);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
            String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapterSubmitAdapter.list.get(i).setTotalPrice(sumPrice);
        }

        mTvTotalPrice.setText(allAmountMoney());
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
            list = listSelectGoods;
            for (GoodsBean sortModel : list) {
                String name = sortModel.getGg_title();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }

            adapterSubmitAdapter.setData(filterDateList);
        }
    }

    /**
     * 二维码扫描商品结果
     *
     * @param bean
     */
    public void addGoods(GoodsBean bean) {
        List<GoodsBean> filterDateList = new ArrayList<GoodsBean>();

        List<GoodsBean> list = null;
        list = listSelectGoods;
        for (GoodsBean sortModel : list) {
            if (bean.getGgp_id().equals(sortModel.getGgp_id())) {
                filterDateList.add(sortModel);
            }
        }

        if (filterDateList.size() == 0) {
            adapterSubmitAdapter.setData(list);
        } else {
            adapterSubmitAdapter.setData(filterDateList);
        }
    }

}
