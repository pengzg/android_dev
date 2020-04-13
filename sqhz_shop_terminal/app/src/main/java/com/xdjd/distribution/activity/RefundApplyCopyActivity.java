package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RefundGoodsListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.AddRefundBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.RefundRequireBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.SearchPopup;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.toast.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/22
 *     desc   : 退货申请
 *     version: 1.0
 * </pre>
 */

public class RefundApplyCopyActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_refund_apply)
    ListView mLvRefundApply;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_select_store)
    TextView mTvSelectStore;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.ll_all_select)
    LinearLayout mLlAllSelect;
    @BindView(R.id.tv_selected_num)
    TextView mTvSelectedNum;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.cb_normal)
    CheckBox mCbNormal;
    @BindView(R.id.cb_lq)
    CheckBox mCbLq;
    @BindView(R.id.cb_cc)
    CheckBox mCbCc;
    @BindView(R.id.cb_gq)
    CheckBox mCbGq;

    private UserBean userBean;

    /**
     * 退货申请
     */
    public List<GoodsBean> listRefund = new ArrayList<>();
    /**
     * 退货商品列表adapter
     */
    private RefundGoodsListAdapter adapter;

    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;
    private List<StorehouseBean> listStore;//仓库列表数据
    private String storehouseId;//仓库id
    private String storehouseName;//仓库名称

    /**
     * 搜索弹框
     */
    private SearchPopup popupSearch;

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply_copy;
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
        userBean = UserInfoUtils.getUser(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("退货申请");
        mTitleBar.setRightImageResource(R.mipmap.search);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupSeaarh();
            }
        });

        adapter = new RefundGoodsListAdapter();
        mLvRefundApply.setAdapter(adapter);
        adapter.setData(listRefund);

        mLvRefundApply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listRefund.get(i).getIsSelect() == 0) {
                    listRefund.get(i).setIsSelect(1);
                } else {
                    listRefund.get(i).setIsSelect(0);
                }

                boolean isAll = true;
                for (GoodsBean bean : listRefund) {
                    if (1 == bean.getIsSelect()) {
                        isAll = false;
                        break;
                    }
                }
                if (isAll) {
                    mIvIcon.setSelected(true);
                } else {
                    mIvIcon.setSelected(false);
                }

                adapter.notifyDataSetChanged();
                calculateNum();
            }
        });

        queryStorehouseList(false);
        getAllGoodsList("");
        initStorePopup();

        mLlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupSearch();
            }
        });
    }

    /**
     * 加载车库存所有商品
     */
    private void getAllGoodsList(String searchKey) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mCbNormal.setChecked(true);
                    mCbGq.setChecked(true);
                    mCbLq.setChecked(true);
                    mCbCc.setChecked(true);

                    listRefund.clear();
                    listRefund.addAll(jsonBean.getListData());
                    adapter.setData(listRefund);
                    mIvIcon.setSelected(true);
                    calculateNum();
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
        httpUtil.post(M_Url.getAllGoodsList, L_RequestParams.
                getAllGoodsList(searchKey), true);
    }

    @OnClick({R.id.ll_all_select, R.id.tv_submit, R.id.tv_select_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_store:
                if (listStore == null || listStore.size() == 0) {
                    queryStorehouseList(true);
                } else {
                    showPwStore();
                }
                break;
            case R.id.ll_all_select:
                if (mIvIcon.isSelected()) {
                    mIvIcon.setSelected(false);
                    for (int i = 0; i < listRefund.size(); i++) {
                        listRefund.get(i).setIsSelect(1);
                    }
                } else {
                    mIvIcon.setSelected(true);
                    for (int i = 0; i < listRefund.size(); i++) {
                        listRefund.get(i).setIsSelect(0);
                    }
                }
                adapter.notifyDataSetChanged();
                calculateNum();
                break;
            case R.id.tv_submit:
                if (storehouseId == null || storehouseId.length() == 0){
                    showToast("请选择退货仓库!");
                    return;
                }

                if (listRefund == null || listRefund.size() == 0) {
                    showToast("没有退货的商品!");
                    return;
                }

                if (!mCbNormal.isChecked() && !mCbCc.isChecked() && !mCbLq.isChecked() && !mCbGq.isChecked()) {
                    showToast("请选择退货的商品类型!");
                    return;
                }

                BigDecimal normalTotalNum = BigDecimal.ZERO;//正常品总数量
                BigDecimal ccTotalNum = BigDecimal.ZERO;//残次
                BigDecimal lqTotalNum = BigDecimal.ZERO;//临期
                BigDecimal gqTotalNum = BigDecimal.ZERO;//过期

                for (GoodsBean bean:listRefund){//计算退货商品类型总数量
                    if (bean.getIsSelect() == 1)continue;
                    if (mCbNormal.isChecked()){
                        BigDecimal normalNum;
                        if (bean.getGgs_stock() == null || bean.getGgs_stock().length() == 0){
                            normalNum = BigDecimal.ZERO;
                        }else{
                            normalNum = new BigDecimal(bean.getGgs_stock());
                        }
                        normalTotalNum = normalTotalNum.add(normalNum);
                    }
                    if (mCbCc.isChecked()){//残次
                        BigDecimal ccNum;
                        if (bean.getGgs_stock_cc() == null || bean.getGgs_stock_cc().length() == 0){
                            ccNum = BigDecimal.ZERO;
                        }else{
                            ccNum = new BigDecimal(bean.getGgs_stock());
                        }
                        ccTotalNum = ccTotalNum.add(ccNum);
                    }
                    if (mCbLq.isChecked()){//临期
                        BigDecimal lqNum;
                        if (bean.getGgs_stock_cc() == null || bean.getGgs_stock_cc().length() == 0){
                            lqNum = BigDecimal.ZERO;
                        }else{
                            lqNum = new BigDecimal(bean.getGgs_stock());
                        }
                        lqTotalNum = lqTotalNum.add(lqNum);
                    }
                    if (mCbGq.isChecked()){//过期
                        BigDecimal gqNum;
                        if (bean.getGgs_stock_cc() == null || bean.getGgs_stock_cc().length() == 0){
                            gqNum = BigDecimal.ZERO;
                        }else{
                            gqNum = new BigDecimal(bean.getGgs_stock());
                        }
                        gqTotalNum = gqTotalNum.add(gqNum);
                    }
                }

                BigDecimal goodsTotal = normalTotalNum.add(ccTotalNum).add(gqTotalNum).add(lqTotalNum);
                if (goodsTotal.compareTo(BigDecimal.ZERO) == 0){
                    DialogUtil.showCustomDialog(this,"提示","没有可退库存数量,选中的退货商品数量是0!","确定",null,null);
                    return;
                }

                List<AddRefundBean> listAddRefund = new ArrayList<>();

                BigDecimal sum = BigDecimal.ZERO;
                for (GoodsBean bean : listRefund) {
                    if (0 != bean.getIsSelect()) {
                        continue;
                    }

                    AddRefundBean strBean = new AddRefundBean();
                    strBean.setEii_price_id(bean.getGgp_id());
                    strBean.setEii_goodsId(bean.getGgp_goodsid());
                    strBean.setEii_goods_price_max(bean.getMax_price());
                    strBean.setEii_goods_price_min(bean.getMin_price());
                    strBean.setEii_goods_name(bean.getGg_title());
                    //            strBean.setEii_goods_amount(bean.getTotalPrice());
                    strBean.setEii_selltype_id(bean.getSaleType());

                    if (mCbNormal.isChecked()) {
                        strBean.setEii_goods_quantity(bean.getGgs_stock());
                    }
                    if (mCbLq.isChecked()) {
                        strBean.setEii_goods_quantity_lq(bean.getGgs_stock_lq());
                    }
                    if (mCbCc.isChecked()) {
                        strBean.setEii_goods_quantity_cc(bean.getGgs_stock_cc());
                    }
                    if (mCbGq.isChecked()) {
                        strBean.setEii_goods_quantity_gq(bean.getGgs_stock_gq());
                    }

                    BigDecimal bdPrice = new BigDecimal(bean.getTotalPrice());
                    sum = sum.add(bdPrice);

                    listAddRefund.add(strBean);
                }

                if (listAddRefund.size() == 0){
                    DialogUtil.showCustomDialog(this,"提示","请选择退货的商品","确定",null,null);
                    return;
                }

                final String addRefundStr = JsonUtils.toJSONString(listAddRefund);

                final BigDecimal finalSum = sum;
                DialogUtil.showCustomDialog(this, "提示", "是否提交退货申请?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        createOrder(addRefundStr, finalSum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }

                    @Override
                    public void no() {
                    }
                });
                break;
        }
    }

    /**
     * 生成订单接口
     */
    private void createOrder(String addRefundStr,String sum) {

        AsyncHttpUtil<RefundRequireBean> httpUtil = new AsyncHttpUtil<>(this, RefundRequireBean.class, new IUpdateUI<RefundRequireBean>() {
            @Override
            public void updata(RefundRequireBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    intentActivity();
                    ToastUtils.showToastInCenterSuccess(RefundApplyCopyActivity.this,jsonBean.getRepMsg());
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
        httpUtil.post(M_Url.addRefundByGoodsStatus, L_RequestParams.
                addRefundByGoodsStatus(userBean.getUserId(), storehouseId,
                        sum, addRefundStr), true);
    }

    private void intentActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finishActivity();
    }

    /**
     * 计算选中的商品数量
     */
    private void calculateNum() {
        int num = 0;
        for (GoodsBean bean : listRefund) {
            if (0 == bean.getIsSelect()) {
                num++;
            }
        }
        mTvSelectedNum.setText("已选" + num + "件商品");
    }

    /**
     * 获取仓库列表接口
     */
    private void queryStorehouseList(final boolean isDialog) {
        AsyncHttpUtil<StorehouseBean> httpUtil = new AsyncHttpUtil<>(this, StorehouseBean.class, new IUpdateUI<StorehouseBean>() {
            @Override
            public void updata(StorehouseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listStore = jsonBean.getListData();
                    if (listStore != null && listStore.size() > 0) {
                        if (isDialog) {
                            showPwStore();
                        } else {
                            storehouseId = listStore.get(0).getBs_id();
                            storehouseName = listStore.get(0).getBs_name();
                            mTvSelectStore.setText(storehouseName);
                        }
                    } else {
                        showToast("没有仓库数据");
                    }
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
        httpUtil.post(M_Url.queryStorehouseList,
                L_RequestParams.queryStorehouseList("", "", "2"), isDialog);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                storehouseId = listStore.get(position).getBs_id();
                storehouseName = listStore.get(position).getBs_name();
                mTvSelectStore.setText(listStore.get(position).getBs_name());
                popupStore.dismiss();
            }
        });
    }

    /**
     * 显示仓库popup
     */
    private void showPwStore() {
        popupStore.setData(listStore);
        popupStore.setId(storehouseId);
        // 显示窗口
        popupStore.showAtLocation(mLlMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    /**
     * 初始化搜索弹框
     */
    private void initPopupSearch() {
        popupSearch = new SearchPopup(this, mLlMain, new SearchPopup.ItemOnListener() {
            @Override
            public void onItemSearch(String searchStr) {
                listRefund.clear();
                adapter.notifyDataSetChanged();
                getAllGoodsList(searchStr);
            }
        });
    }

    private void showPopupSeaarh() {
        popupSearch.setSearchStr("");
        popupSearch.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
        popupSearch.showPopup();
    }

}
