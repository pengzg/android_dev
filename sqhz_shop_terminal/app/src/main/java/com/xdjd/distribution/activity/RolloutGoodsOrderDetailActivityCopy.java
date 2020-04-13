package com.xdjd.distribution.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.RolloutGoodsDetailAdapter;
import com.xdjd.distribution.adapter.RolloutGoodsOrderDetailAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.distribution.bean.RolloutGoodsOrderBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.bluetooth.BluetoothUtil;
import com.xdjd.utils.bluetooth.Prints;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.EaseTitleBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/22.
 */

public class RolloutGoodsOrderDetailActivityCopy extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    PullToRefreshListView mPullList;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.tv_order_code)
    TextView mTvOrderCode;
    @BindView(R.id.tv_order_amount)
    TextView mTvOrderAmount;
    @BindView(R.id.ll_order_amount)
    LinearLayout mLlOrderAmount;
    @BindView(R.id.elv_order_goods)
    AnimatedExpandableListView mElvOrderGoods;
    @BindView(R.id.tv_order_title)
    TextView mTvOrderTitle;
    @BindView(R.id.tv_order_amount_title)
    TextView mTvOrderAmountTitle;

    private int type;// 1:铺货单详情 2：铺货销售单详情 3：撤货单详情
    private String orderId;

    private int page = 1;
    private int mFlag = 0;

    RolloutGoodsOrderDetailAdapter adapter;
    private List<PHOrderDetailBean> list = new ArrayList<>();
    //铺货、销售数据集合
    private List<PHOrderDetailBean> listTwo = new ArrayList<>();
    //销售和撤货adapter
    private RolloutGoodsDetailAdapter adapterTwo;

    private ClientBean mClientBean;
    private UserBean mUserBean;

    private RolloutGoodsOrderBean bean;
    private List<BluetoothDevice> printerDevices;

    private PHOrderDetailBean detailBean;

    BigDecimal skAmount;//刷卡
    BigDecimal xjAmount;//现金
    BigDecimal yeAmount;//余额

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.SUCCESS) {
            } else if (msg.what == PublicFinal.ERROR) {
                showToast(UIUtils.getString(R.string.ly_null_error));
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.copy_activity_rollout_goods_order_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setRightImageResource(R.mipmap.dyjsz);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BluetoothUtil.isBluetoothOn())//如果没有打开蓝牙就去打开蓝牙
                    BluetoothUtil.turnOnBluetooth();
                switch (type) {
                    case 1:
                        if (list != null && list.size() > 0) {
                            connectBluetooth();
                        }
                        break;
                    case 2:
                    case 3:
                        if (listTwo != null && listTwo.size() > 0) {
                            connectBluetooth();
                        }
                        break;
                }
            }
        });

        type = getIntent().getIntExtra("type", 1);
        orderId = getIntent().getStringExtra("orderId");
        bean = (RolloutGoodsOrderBean) getIntent().getSerializableExtra("bean");

        switch (type) {
            case 1:
                mTitleBar.setTitle("铺货单详情");
                mPullList.setVisibility(View.VISIBLE);
                mElvOrderGoods.setVisibility(View.GONE);

                mTvOrderTitle.setText("铺货单号");
                mTvOrderAmountTitle.setText("铺货金额");
                break;
            case 2:
                mTitleBar.setTitle("销售单详情");
                mPullList.setVisibility(View.GONE);
                mElvOrderGoods.setVisibility(View.VISIBLE);

                mTvOrderTitle.setText("订单单号");
                mTvOrderAmountTitle.setText("订单金额");
                break;
            case 3:
                mTitleBar.setTitle("撤货单详情");
                mLlOrderAmount.setVisibility(View.GONE);
                mPullList.setVisibility(View.GONE);
                mElvOrderGoods.setVisibility(View.VISIBLE);

                mTvOrderTitle.setText("撤货单号");
                mTvOrderAmountTitle.setText("订单金额");
                break;
        }

        mTvCustomerName.setText(bean.getShopName());
        mTvOrderCode.setText(bean.getOrderCode());
        mTvOrderAmount.setText(bean.getTotalAmount());

        mUserBean = UserInfoUtils.getUser(this);
        mClientBean = UserInfoUtils.getClientInfo(this);

        adapter = new RolloutGoodsOrderDetailAdapter(list, type);
        initRefresh(mPullList);
        mPullList.setMode(PullToRefreshBase.Mode.DISABLED);
        mPullList.setAdapter(adapter);
        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryPhOrderDetailList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryPhOrderDetailList();
            }
        });

        adapterTwo = new RolloutGoodsDetailAdapter(type);
        mElvOrderGoods.setAdapter(adapterTwo);
        adapterTwo.setData(listTwo);
        mElvOrderGoods.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        mPullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i = i - 1;//刷新listview会添加头部,下标所以减1
            }
        });

        queryPhOrderDetailList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillAdapter();
    }

    /**
     * 从所有已配对设备中找出打印设备并显示
     */
    private void fillAdapter() {
        //推荐使用 BluetoothUtil.getPairedPrinterDevices()
        printerDevices = BluetoothUtil.getPairedDevices();
    }

    private void queryPhOrderDetailList() {
        AsyncHttpUtil<PHOrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, PHOrderDetailBean.class,
                new IUpdateUI<PHOrderDetailBean>() {
                    @Override
                    public void updata(PHOrderDetailBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            if (jsonStr.getList() != null && jsonStr.getList().size() > 0) {
                                detailBean = jsonStr.getList().get(0);
                            }
                            if (jsonStr.getList() != null && jsonStr.getList().size() > 0) {
                                if (type == 1) {
                                    list = jsonStr.getList().get(0).getListData();
                                    adapter.setData(list);
                                } else {
                                    listTwo = jsonStr.getList();
                                    adapterTwo.setData(listTwo);
                                    for (int i = 0; i < listTwo.size(); i++) {
                                        mElvOrderGoods.expandGroup(i);
                                    }
                                }
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
                        mPullList.onRefreshComplete();
                    }
                });
        httpUtil.post(M_Url.queryPhOrderDetailList, L_RequestParams.queryPhOrderDetailList(orderId, String.valueOf(type)), true);
    }

    /**
     * 连接蓝牙打印机
     */
    private void connectBluetooth() {
        if (printerDevices == null || printerDevices.size() == 0) {
            showToast(UIUtils.getString(R.string.ly_null_error));
        } else {

            String deviceAddress = UserInfoUtils.getDeviceAddress(this);

            for (int i = 0; i < printerDevices.size(); i++) {
                if (deviceAddress.equals(printerDevices.get(i).getAddress())) {
                    if (printerDevices.get(i) != null) {
                        super.connectDevice(printerDevices.get(i), TASK_TYPE_PRINT);
                    } else {
                        showToast(UIUtils.getString(R.string.ly_null_error));
                    }
                    return;
                }
            }
            showToast(UIUtils.getString(R.string.ly_null_error));
        }
    }

    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                if (socket == null) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
                }

                String time = StringUtils.getDate();

                ClientBean clientBean = new ClientBean();
                clientBean.setCc_name(bean.getShopName());
                clientBean.setCc_contacts_name(bean.getCustomer_name());
                clientBean.setCc_contacts_mobile(bean.getCustomer_phone());
                clientBean.setCc_address(bean.getCustomer_address());
                boolean isSuccess = false;
                switch (type) {
                    case 1://铺货
                        isSuccess = Prints.printOrder(socket, 12, clientBean, "铺货单", time, bean.getOrderCode(),
                                mUserBean.getBud_name(),
                                mUserBean.getMobile(), list, bean.getTotalAmount(), /*mCheckPrintGoodscode.isChecked() ? true :*/ false, bean.getRemark());
                        break;
                    case 2://销售
                        if (detailBean == null) {
                            detailBean = new PHOrderDetailBean();
                        }
                        initAmount();
                        String sfAmount = xjAmount.add(skAmount).add(yeAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        isSuccess = Prints.printOrder(socket, 13, clientBean, "铺货销售单", time, bean.getOrderCode(),
                                mUserBean.getBud_name(),
                                mUserBean.getMobile(), listTwo, bean.getTotalAmount(),
                                detailBean.getSkAmount(), detailBean.getYhAmount(), detailBean.getXjAmount(), detailBean.getYeAmount(),
                                detailBean.getYsAmount(), sfAmount, /*mCheckPrintGoodscode.isChecked() ? true :*/ false, null, bean.getRemark());
                        break;
                    case 3://撤货
                        isSuccess = Prints.printOrder(socket, 14, clientBean, "铺货撤货单", time, bean.getOrderCode(),
                                mUserBean.getBud_name(),
                                mUserBean.getMobile(), listTwo, bean.getTotalAmount(), /*mCheckPrintGoodscode.isChecked() ? true :*/ false, bean.getRemark());
                        break;
                }
                if (!isSuccess) {
                    mHandler.sendEmptyMessage(PublicFinal.ERROR);
                    return;
                }
                finishActivity();
                break;
            case TASK_TYPE_CONNECT:
                break;
        }
    }

    private void initAmount() {
        if (TextUtils.isEmpty(detailBean.getSkAmount())) {
            skAmount = new BigDecimal("0");
        } else {
            skAmount = new BigDecimal(detailBean.getSkAmount());
        }

        if (TextUtils.isEmpty(detailBean.getXjAmount())) {
            xjAmount = new BigDecimal("0");
        } else {
            xjAmount = new BigDecimal(detailBean.getXjAmount());
        }

        if (TextUtils.isEmpty(detailBean.getYeAmount())) {
            yeAmount = new BigDecimal("0");
        } else {
            yeAmount = new BigDecimal(detailBean.getYeAmount());
        }

    }
}
