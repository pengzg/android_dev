package com.xdjd.distribution.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.PHDistributionAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.PHTaskBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.DistributionEvent;
import com.xdjd.distribution.event.OrderRejectedEvent;
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
 *     time   : 2017/5/26
 *     desc   : 配送任务商品列表操作界面
 *     version: 1.0
 * </pre>
 */

public class PHDistributionActivity extends BaseActivity implements PHDistributionAdapter.PHDistributionListener {

    @BindView(R.id.left_layout)
    RelativeLayout mLeftLayout;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_right_search)
    RelativeLayout mRlRightSearch;
    @BindView(R.id.rl_right_qr)
    RelativeLayout mRlRightQr;
    @BindView(R.id.right_image)
    ImageView mRightImage;
    @BindView(R.id.lv_distribution_order)
    ListView mLvDistributionOrder;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;

    private int index = 0;

    /**
     * 订单选中的商品列表
     */
    public List<PHTaskBean> listOrder = new ArrayList<>();

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private String title = "";
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

    private PHDistributionAdapter adapter;
    private VaryViewHelper helper = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_ph_distribution;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        super.initData();
        bean = (CustomerTaskBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            title = bean.getCc_name();
        }

        userBean = UserInfoUtils.getUser(this);

        mTitle.setText(title);
        mRlRightQr.setVisibility(View.VISIBLE);
        //        mRlRightSearch.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(R.mipmap.route_white);

        clientBean = UserInfoUtils.getClientInfo(this);

        helper = new VaryViewHelper(mLvDistributionOrder);

        adapter = new PHDistributionAdapter(this, userBean);
        mLvDistributionOrder.setAdapter(adapter);
        getTaskPhDetail();
    }

    /**
     * 查询订单列表
     */
    private void getTaskPhDetail() {
        helper.showLoadingView();
        AsyncHttpUtil<PHTaskBean> httpUtil = new AsyncHttpUtil<>(this, PHTaskBean.class, new IUpdateUI<PHTaskBean>() {
            @Override
            public void updata(PHTaskBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listOrder = jsonBean.getOrderList();
                    adapter.setData(listOrder);
                    if (listOrder!=null && listOrder.size()>0){
                        helper.showDataView();
                    }else{
                        helper.showEmptyView();
                    }
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
        httpUtil.post(M_Url.getTaskPhDetail, L_RequestParams.getTaskPhDetail(bean.getCc_id(),
                salesmanid, ""), false);
    }

    public class MyErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            getTaskPhDetail();
        }
    }

    @OnClick({R.id.left_layout, R.id.rl_right_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.rl_right_qr://百度地图导航
                if (bean == null) {
                    showToast("客户没有维护位置信息,请先维护客户位置信息!");
                    return;
                }

                //地图导航
                boolean baidu = isAvilible(PHDistributionActivity.this, Comon.MY_BAIDU_PACKAGE);
                if (baidu) {
                    DialogUtil.showCustomDialog(PHDistributionActivity.this, "提示", "是否开启百度地图导航?", "是", "否", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            Intent intent;
                            intent = new Intent();
                            intent.setData(Uri.parse("baidumap://map/navi?location=" + bean.getCc_latitude() +
                                    "," + bean.getCc_longitude()));
                            startActivity(intent);
                        }

                        @Override
                        public void no() {
                        }
                    });
                } else {
                    showToast("请安装百度地图软件");
                }
                break;
        }
    }

    private boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void noDistribution(final int i) {
        DialogUtil.showCustomDialog(this, "暂不配送", UIUtils.getString(R.string.no_distribution),
                "暂不配送", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        adapter.list.remove(i);
                        adapter.notifyDataSetInvalidated();
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
//        if ("2".equals(adapter.list.get(i).getFlag())) {//是否允许修改数量 1 允许 2 不允许
//            DialogUtil.showCustomDialog(this, "提示", "参与活动的订单商品不允许修改商品数量", "确定", null, null);
//            return;
//        }

        DialogUtil.showCustomDialog(this, "整单清零", UIUtils.getString(R.string.whole_single_reset),
                "整单清零", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        PHTaskBean bean = adapter.list.get(i);
                        bean.setOa_order_amount("0.00");
                        for (PHTaskBean goodsBean : bean.getDetailList()) {
                            if ("2".equals(goodsBean.getOad_goodstype())) {//getOd_goodstype商品类型 1 普通 2 赠品
                                continue;
                            }
                            goodsBean.setOad_goods_num_max("0");
                            goodsBean.setOad_goods_num_min("0");
                            goodsBean.setOad_real_total("0.00");
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
        Intent intent = new Intent(this, NewRolloutGoodsSubmitActivity.class);
        PHTaskBean bean = null;
        bean = listOrder.get(i);
        intent.putExtra("amount", bean.getOa_order_amount());
        intent.putExtra("orderType",5);
        intent.putExtra("listOrderGoods", (Serializable) bean);//任务单参数
        intent.putExtra("listIndex", i);//集合下标
        intent.putExtra("taskCustomer", this.bean);//客户信息
        startActivity(intent);
    }

    @Override
    public void onOrderRejected(final int i) {
        DialogUtil.showCustomDialog(this, "提示", "是否拒收这笔订单?",
                "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        cancelApplyOrder(i);
                    }

                    @Override
                    public void no() {

                    }
                });
    }

    @Override
    public void onMaxPlus(int i, int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        plusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_max(mEtMaxNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMaxMinus(int i, int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        minusCalculation(i, mEtMaxNum, tvSumPrice);
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_max(mEtMaxNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMaxNum, mLlMaxLeft, mRlMax, mIvMax);
    }

    @Override
    public void onMinPlus(int i, int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        plusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_min(mEtMinNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onMinMinus(int i, int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        minusCalculation(i, mEtMinNum, tvSumPrice);
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_min(mEtMinNum.getText().toString());
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
        AnimUtils.setTranslateAnimation(mEtMinNum, mLlMinLeft, mRlMin, mIvMin);
    }

    @Override
    public void onClearNum(int i, int k, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        mEtMaxNum.setText("0");
        mEtMinNum.setText("0");
        tvSumPrice.setText("0.00");
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_max("0");
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_min("0");
        adapter.list.get(i).getDetailList().get(k).setOad_real_total("0.00");
        amountMoney(i, mTvOrderAmount);
    }

    @Override
    public void onMaxEtNum(int i, int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_max(newStr);
        calculatePrice(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
    }

    @Override
    public void onMinEtNum(int i, int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount) {
        String newStr = num.replaceFirst("^0*", "");
        adapter.list.get(i).getDetailList().get(k).setOad_goods_num_min(newStr);
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

        adapter.list.get(i).getDetailList().get(k).setOad_price_max(bdMaxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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

        adapter.list.get(i).getDetailList().get(k).setOad_price_min(bdMinPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        calculatePrice(i, k, mEtMaxPrice, mEtMinPrice, mEtMaxNum, mEtMinNum, tvSumPrice, mTvOrderAmount);
    }

    /**
     * 铺货任务取消
     * @param i
     */
    private void cancelApplyOrder(final int i) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    adapter.list.remove(i);
                    adapter.notifyDataSetChanged();
                    if (adapter.list == null || adapter.list.size() == 0) {//如果订单没有,发送消息清楚客户列表信息
                        EventBus.getDefault().post(new OrderRejectedEvent());
                        helper.showEmptyView();//显示没有数据提示
                    }
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.cancelApplyOrder, L_RequestParams.
                cancelApplyOrder(adapter.list.get(i).getOa_id(),
                        bean.getCc_id()), true);
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
        BigDecimal totalAmount = new BigDecimal(adapter.getItem(i).getDetailList().get(k).getOad_real_total());

        bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));

        if (bdSumPrice.compareTo(BigDecimal.ZERO) == 0) {
            tvSumPrice.setText("0.00");
            adapter.getItem(i).getDetailList().get(k).setOad_real_total("0.00");
        } else {
            if (totalAmount.compareTo(BigDecimal.ZERO) == -1) {
                tvSumPrice.setText(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getDetailList().get(k).
                        setOad_real_total(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                tvSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getDetailList().get(k).
                        setOad_real_total(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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

        if (TextUtils.isEmpty(adapter.getItem(i).getDetailList().get(k).getOad_price_max())) {
            bdMaxPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMaxPrice = new BigDecimal(adapter.getItem(i).getDetailList().get(k).getOad_price_max());
        }

        if (TextUtils.isEmpty(mEtMaxNum.getText())) {
            bdMaxNum = new BigDecimal(0);
        } else {
            bdMaxNum = new BigDecimal(mEtMaxNum.getText().toString());
        }

        if (TextUtils.isEmpty(adapter.getItem(i).getDetailList().get(k).getOad_price_min())) {
            bdMinPrice = new BigDecimal(BigInteger.ZERO);
        } else {
            bdMinPrice = new BigDecimal(adapter.getItem(i).getDetailList().get(k).getOad_price_min());
        }
        if (TextUtils.isEmpty(mEtMinNum.getText())) {
            bdMinNum = new BigDecimal(0);
        } else {
            bdMinNum = new BigDecimal(mEtMinNum.getText().toString());
        }

        if ("1".equals(adapter.getItem(i).getDetailList().get(k).getOad_unit_num())) {//大小单位换算比1==1,只显示小单位
            bdSumPrice = bdMinPrice.multiply(bdMinNum);
        } else {
            bdSumPrice = bdMaxPrice.multiply(bdMaxNum).add(bdMinPrice.multiply(bdMinNum));
        }

        //订单的总金额
        BigDecimal totalAmount = new BigDecimal(adapter.getItem(i).getDetailList().get(k).getOad_real_total());

        if (bdSumPrice.compareTo(BigDecimal.ZERO) == 0) {//单件商品总价格==0
            String sumPrice = bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            tvSumPrice.setText(sumPrice);
            adapter.getItem(i).getDetailList().get(k).setOad_real_total(sumPrice);
        } else {
            if (totalAmount.compareTo(BigDecimal.ZERO) == -1) {
                tvSumPrice.setText(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getDetailList().get(k).
                        setOad_real_total(bdSumPrice.multiply(fOne).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                tvSumPrice.setText(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                adapter.getItem(i).getDetailList().get(k).
                        setOad_real_total(bdSumPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }

        amountMoney(i, mTvOrderAmount);
    }

    /**
     * 计算这一订单的总价格
     */
    private void amountMoney(int i, TextView mTvOrderAmount) {
        if (adapter.list.get(i).getDetailList().size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            LogUtils.e("bdSumPrice", adapter.list.get(i).getOa_order_amount());
            BigDecimal orderAmount = new BigDecimal(adapter.list.get(i).getOa_order_amount());

            for (PHTaskBean bean : adapter.list.get(i).getDetailList()) {
                BigDecimal bdPrice = new BigDecimal(bean.getOad_real_total());
                sum = sum.add(bdPrice);
            }
            mTvOrderAmount.setText(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            mTvOrderAmount.setText("0.00");
        }
        adapter.list.get(i).setOa_order_amount(mTvOrderAmount.getText().toString());
    }


    public void onEventMainThread(DistributionEvent event) {
        int listIndex = event.getListIndex();

        listOrder.remove(listIndex);
        adapter.setData(listOrder);

        //没有数据直接跳转到主界面
        if (listOrder.size() == 0) {
            Intent intent = new Intent(this, RolloutGoodsActivity.class);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //        if (mViewpagerCommon.getCurrentItem() == 0) {
        //            if (listGoodsOrder.size() == 0 && listProcessOrder.size() == 0 &&
        //                    listExchangeOrder.size() == 0 && listRefundOrder.size() == 0) {
        //                finish();
        //            } else {
        //                DialogUtil.showCustomDialog(this, "提示", "确定放弃编辑退出?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
        //                    @Override
        //                    public void ok() {
        //                        finish();
        //                    }
        //
        //                    @Override
        //                    public void no() {
        //                    }
        //                });
        //            }
        //        } else {
        //            index = 0;
        //            mViewpagerCommon.setCurrentItem(0);
        //        }
    }

    /**
     * 二维码查询
     *
     * @param search_code
     */
    private void getGoodsDetail(String search_code) {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(this, GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (index == 0) {
                        //                        fragmentEdit.addGoods(bean);
                    } else {
                        //                        fragmentSubmit.addGoods(bean);
                    }
                } else {
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.getGoodsDetail, L_RequestParams.getGoodsDetail(
                userBean.getUserId(), bean.getCc_id(), search_code, ""), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Comon.QR_GOODS_REQUEST_CODE) {
            switch (resultCode) {
                case Comon.QR_GOODS_RESULT_CODE:
                    String result = data.getStringExtra("result");
                    if (result == null || result.equals("")) {
                    } else {
                        getGoodsDetail(result);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

}
