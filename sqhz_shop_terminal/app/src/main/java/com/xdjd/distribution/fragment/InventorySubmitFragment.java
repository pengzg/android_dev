package com.xdjd.distribution.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.xdjd.distribution.activity.CustomerInventoryActivity;
import com.xdjd.distribution.activity.InventorySubmitActivity;
import com.xdjd.distribution.adapter.InventoryAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.UserBean;
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
 *     time   : 2017/5/10
 *     desc   : 订单申报 -- 商品提交界面
 *     version: 1.0
 * </pre>
 */

public class InventorySubmitFragment extends BaseFragment implements InventoryAdapter.InventoryListener {

    @BindView(R.id.lv_order_declare_goods)
    ListView mLvOrderDeclareGoods;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;

    private View view;

    /**
     * 要货申请
     */
    public List<GoodsBean> listInventry = new ArrayList<>();

    private InventoryEditFragment editFragment;

    private CustomerInventoryActivity context;

    private InventoryAdapter adapter;

    private CharacterParser characterParser;

    private UserBean userBean;

    /**
     * 订单编号
     */
    private String orderCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_submit_inventory, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void refreshData(InventoryEditFragment editFragment) {
        this.editFragment = editFragment;

        listInventry = context.listInventry;

        mTvSelectedNum.setText("已选(" + context.listInventry.size() + "件)");
        adapter.setData(listInventry);

        allAmountMoney();
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;

        context = (CustomerInventoryActivity) getActivity();

        adapter = new InventoryAdapter(this);
        mLvOrderDeclareGoods.setAdapter(adapter);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        listInventry = context.listInventry;

        mTvSelectedNum.setText("已选(" + context.listInventry.size() + "件)");
    }

    @OnClick({R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                removeZeroPriceGoods();
                break;
        }
    }

    /**
     * 删除价格为0的商品
     */
    private void removeZeroPriceGoods() {
       /* if (listInventry.size() > 0) {
            for (int i = 0; i < listInventry.size(); i++) {
                BigDecimal bdPrice = new BigDecimal(listInventry.get(i).getTotalPrice());
                if (bdPrice.doubleValue() == 0) {
                    listInventry.remove(i);
                }
            }
        }*/

        if (listInventry == null || listInventry.size() == 0) {
            DialogUtil.showCustomDialog(getActivity(), "注意", "请录入要申报的产品信息!", "确定", null, null);
            return;
        }
        createOrder(listInventry);
    }


    /**
     * 生成订单接口
     */
    private void createOrder(List<GoodsBean> list) {
        Intent intent = new Intent(getActivity(), InventorySubmitActivity.class);
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("totalPrice", mTvTotalPrice.getText().toString());
        intent.putExtra("pathList",context.pathList);
        startActivity(intent);
    }


    /**
     * 计算所有订单状态的价格
     */
    private void allAmountMoney() {
        listInventry = context.listInventry;

        BigDecimal sum = BigDecimal.ZERO;
        if (listInventry.size() > 0) {
            for (GoodsBean bean : listInventry) {
                BigDecimal bdPrice = BigDecimal.ZERO;
                if (!TextUtils.isEmpty(bean.getTotalPrice()) && !"".equals(bean.getTotalPrice())){
                    bdPrice = new BigDecimal(bean.getTotalPrice());
                }
                sum = sum.add(bdPrice);
            }
        }

        mTvTotalPrice.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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
        allAmountMoney();
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

    @Override
    public void onDiaplayNum(int i, String num) {
        adapter.getItem(i).setDisplay_quantity(num);
    }

    @Override
    public void onDuitouNum(int i, String num) {
        adapter.getItem(i).setDuitou_quantity(num);
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

        allAmountMoney();
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
            adapter.setData(listInventry);
        } else {
            filterDateList.clear();
            List<GoodsBean> list = null;
            list = listInventry;
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

    /**
     * 二维码扫描商品结果
     *
     * @param bean
     */
    public void addGoods(GoodsBean bean) {
        List<GoodsBean> filterDateList = new ArrayList<GoodsBean>();

        List<GoodsBean> list = null;
        list = listInventry;
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
}
