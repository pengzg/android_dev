package com.xdjd.steward.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.DisplayFeeSettlementAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.DisplayInDetailBean;
import com.xdjd.distribution.bean.DisplayListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/22.
 */

public class DisplayFeeStatisticalDetailActivity extends BaseActivity {


    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.tv_order_title)
    TextView mTvOrderTitle;
    @BindView(R.id.tv_order_code)
    TextView mTvOrderCode;
    @BindView(R.id.tv_order_amount_title)
    TextView mTvOrderAmountTitle;
    @BindView(R.id.tv_order_amount)
    TextView mTvOrderAmount;
    @BindView(R.id.ll_order_amount)
    LinearLayout mLlOrderAmount;
    @BindView(R.id.tv_salesman_name)
    TextView mTvSalesmanName;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.pull_list)
    ListView mPullList;

    private String orderId;
    private int page = 1;
    private int mFlag = 0;

    private DisplayFeeSettlementAdapter adapter;
    private List<DisplayInDetailBean> list = new ArrayList<>();

    private ClientBean mClientBean;
    private UserBean mUserBean;

    private DisplayListBean bean;
    private List<BluetoothDevice> printerDevices;

    private DisplayInDetailBean detailBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_display_fee_statistical_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);

        orderId = getIntent().getStringExtra("orderId");
        bean = (DisplayListBean) getIntent().getSerializableExtra("bean");

        mTitleBar.setTitle("返陈列详情");

        mUserBean = UserInfoUtils.getUser(this);
        mClientBean = UserInfoUtils.getClientInfo(this);

        mTvSalesmanName.setText(bean.getEim_salesid_nameref());

        adapter = new DisplayFeeSettlementAdapter(2);
        mPullList.setAdapter(adapter);

        getDisplayInDetail();
    }

    private void getDisplayInDetail() {
        AsyncHttpUtil<DisplayInDetailBean> httpUtil = new AsyncHttpUtil<>(this, DisplayInDetailBean.class,
                new IUpdateUI<DisplayInDetailBean>() {
                    @Override
                    public void updata(DisplayInDetailBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            detailBean = jsonStr;

                            mTvCustomerName.setText(detailBean.getCustomerName());
                            mTvOrderCode.setText(detailBean.getEim_code());
                            mTvOrderAmount.setText(detailBean.getAmount());

                            if (detailBean.getDataList() != null && detailBean.getDataList().size() > 0) {
                                list = detailBean.getDataList();
                                adapter.setListDetail(list);
                            } else {
                                showToast(UIUtils.getString(R.string.on_pull_remind));
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
        httpUtil.post(M_Url.getDisplayInDetail, L_RequestParams.getDisplayInDetail(bean.getEim_customerid(), orderId), true);
    }

}
