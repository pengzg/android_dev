package com.xdjd.distribution.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.BindingFacilityActivity;
import com.xdjd.distribution.activity.BindingFacilityShopActivity;
import com.xdjd.distribution.activity.CaptureActivity;
import com.xdjd.distribution.activity.CashPrizesRecordAtivity;
import com.xdjd.distribution.activity.CusInventoryQueryActivity;
import com.xdjd.distribution.activity.CustomerInventoryActivity;
import com.xdjd.distribution.activity.DisplayFeeActivity;
import com.xdjd.distribution.activity.DisplayFeeQueryActivity;
import com.xdjd.distribution.activity.DistributionWarehouseActivity;
import com.xdjd.distribution.activity.InventoryManagementActivity;
import com.xdjd.distribution.activity.OpenMallActivity;
import com.xdjd.distribution.activity.OrderGoodsSearchActivity;
import com.xdjd.distribution.activity.OrderSearchActivity;
import com.xdjd.distribution.activity.OutboundQueryActivity;
import com.xdjd.distribution.activity.ReceiptPaymentQueryActivity;
import com.xdjd.distribution.activity.ReceivableSearchActivity;
import com.xdjd.distribution.activity.RefundApplyCopyActivity;
import com.xdjd.distribution.activity.RequireGoodsStoreActivity;
import com.xdjd.distribution.activity.ShopAuditActivity;
import com.xdjd.distribution.activity.VisitingAlarmActivity;
import com.xdjd.distribution.activity.VisitingListActivity;
import com.xdjd.distribution.activity.VoucherReprintActivity;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin
 */
public class OrdersFragment extends BaseFragment {

    @BindView(R.id.ll_receipt_payment_search)
    LinearLayout mLlReceiptPaymentSearch;
    @BindView(R.id.ll_refund_apply)
    LinearLayout mLlRefundApply;
    @BindView(R.id.ll_enquiry_apply)
    LinearLayout mLlEnquiryApply;
    @BindView(R.id.ll_order_search)
    LinearLayout mLlOrderSearch;
    @BindView(R.id.ll_outbound_query)
    LinearLayout mLlOutboundQuery;
    @BindView(R.id.rl_place_order_search)
    RelativeLayout mRlPlaceOrderSearch;
    @BindView(R.id.ll_voucher_reprint)
    LinearLayout mLlVoucherReprint;
    @BindView(R.id.ll_visiting_alarm)
    LinearLayout mLlVisitingAlarm;
    @BindView(R.id.ll_visiting_list)
    LinearLayout mLlVisitingList;
    @BindView(R.id.ll_khpd)
    LinearLayout mLlKhpd;
    @BindView(R.id.ll_khpd_query)
    LinearLayout mLlKhpdQuery;
    @BindView(R.id.ll_receivable)
    LinearLayout mLlReceivable;
    @BindView(R.id.ll_inventory_management)
    LinearLayout mLlInventoryManagement;
    @BindView(R.id.ll_distribution_warehouse)
    LinearLayout mLlDistributionWarehouse;
    @BindView(R.id.ll_order_goods_search)
    LinearLayout mLlOrderGoodsSearch;
    @BindView(R.id.ll_cancel_after_verification)
    LinearLayout mLlCancelAfterVerification;
    @BindView(R.id.ll_shop_winning_record)
    LinearLayout mLlShopWinningRecord;
    @BindView(R.id.ll_scan_prize)
    LinearLayout mLlScanPrize;
    @BindView(R.id.ll_winning_record)
    LinearLayout mLlWinningRecord;
    @BindView(R.id.ll_binding_facility)
    LinearLayout mLlBindingFacility;
    @BindView(R.id.ll_binding_shop)
    LinearLayout mLlBindingShop;
    @BindView(R.id.ll_display_fee)
    LinearLayout mLlDisplayFee;
    @BindView(R.id.ll_display_fee_query)
    LinearLayout mLlDisplayFeeQuery;

    private UserBean userBean;
    private MainActivity mMainActivity;

    private ClientBean clientBean;
    Intent intent;

    HomeFragment homeFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());
        mMainActivity = (MainActivity) getActivity();
        clientBean = UserInfoUtils.getClientInfo(getActivity());

        homeFragment = (HomeFragment) mMainActivity.mFragments.get(0);
    }

    @OnClick({R.id.ll_order_search, R.id.ll_outbound_query, R.id.ll_receipt_payment_search,
            R.id.rl_place_order_search, R.id.ll_enquiry_apply, R.id.ll_refund_apply, R.id.ll_voucher_reprint,
            R.id.ll_visiting_alarm, R.id.ll_visiting_list, R.id.ll_khpd, R.id.ll_khpd_query, R.id.ll_receivable, R.id.ll_inventory_management
            , R.id.ll_distribution_warehouse, R.id.ll_order_goods_search, R.id.ll_cancel_after_verification, R.id.ll_shop_winning_record,
            R.id.ll_scan_prize, R.id.ll_winning_record, R.id.ll_binding_facility, R.id.ll_binding_shop, R.id.ll_display_fee, R.id.ll_display_fee_query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_order_search:
                //                if (BaseConfig.userTypeDistribution.equals(userBean.getSu_usertype())) {
                //                    DialogUtil.showCustomDialog(getActivity(), "提示", "您的用户为配送功能,不包含车销出库、订单查询等功能。", "确定", null, null);
                //                    return;
                //                }
                //                if ("".equals(userBean.getSu_storeid())) {
                //                    noCarHint();
                //                    return;
                //                }
                startActivity(OrderSearchActivity.class);
                break;
            case R.id.ll_order_goods_search://订货查询
                startActivity(OrderGoodsSearchActivity.class);
                break;
            case R.id.ll_outbound_query:
                if ("".equals(userBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                startActivity(OutboundQueryActivity.class);
                break;
            case R.id.ll_receipt_payment_search:
                startActivity(ReceiptPaymentQueryActivity.class);
                break;
            case R.id.rl_place_order_search:
                break;
            case R.id.ll_enquiry_apply:
                if ("".equals(userBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                if (userBean.getBud_customerid() == null || "".equals(userBean.getBud_customerid())) {
                    noCustomerid();
                    return;
                }
                startActivity(RequireGoodsStoreActivity.class);
                break;
            case R.id.ll_refund_apply: //退货申请
                if ("".equals(userBean.getSu_storeid())) {
                    noCarHint();
                    return;
                }
                if (userBean.getBud_customerid() == null || "".equals(userBean.getBud_customerid())) {
                    noCustomerid();
                    return;
                }
                //                startActivity(RefundStoreActivity.class);//旧的退货方式
                startActivity(RefundApplyCopyActivity.class);
                break;
            case R.id.ll_voucher_reprint:
                startActivity(VoucherReprintActivity.class);
                break;
            case R.id.ll_visiting_alarm://拜访提醒
                startActivity(VisitingAlarmActivity.class);
                break;
            case R.id.ll_visiting_list://拜访明细
                startActivity(VisitingListActivity.class);
                break;
            case R.id.ll_khpd://客户盘点
                if (homeFragment.clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请先刷客户档案或选择客户...", "确定", null, null);
                } else {
                    startActivity(CustomerInventoryActivity.class);
                }
                break;
            case R.id.ll_khpd_query://客户盘点查询
                startActivity(CusInventoryQueryActivity.class);
                break;
            case R.id.ll_receivable:
                startActivity(ReceivableSearchActivity.class);
                break;
            case R.id.ll_inventory_management://库存管理
                startActivity(InventoryManagementActivity.class);
                break;
            case R.id.ll_distribution_warehouse:
                if (homeFragment.clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请先刷客户档案或选择客户...", "确定", null, null);
                } else {
                    startActivity(DistributionWarehouseActivity.class);
                }
                break;
            case R.id.ll_cancel_after_verification://店铺结算汇总
                if (homeFragment.clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请先刷客户档案或选择客户...", "确定", null, null);
                } else {
                    startActivity(ShopAuditActivity.class);
                }
                break;
            case R.id.ll_shop_winning_record://店铺中奖核销记录列表
                //                startActivity(ShopWinningRecordActivity.class);
                break;
            case R.id.ll_scan_prize://扫码核销
                intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("isPrize", true);
                intent.putExtra("titleStr", "扫码核销");
                startActivity(intent);
                break;
            case R.id.ll_winning_record://核销记录
                intent = new Intent(getActivity(), CashPrizesRecordAtivity.class);
                intent.putExtra("title", "核销详情");
                startActivity(intent);
                break;
            case R.id.ll_binding_facility://跳转绑定设备列表
                //                startActivity(BindingFacilityShopActivity.class);

                if (homeFragment.clientBean == null) {
                    startActivity(BindingFacilityActivity.class);
                } else {
                    if ("Y".equals(homeFragment.clientBean.getCc_isaccount())) {//已经开通商城
                        startActivity(BindingFacilityActivity.class);
                    } else {
                        DialogUtil.showCustomDialog(getActivity(), "提示", "请先去开通商城,再进行设备绑定!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                startActivity(OpenMallActivity.class);
                            }

                            @Override
                            public void no() {

                            }
                        });
                    }
                }
                break;
            case R.id.ll_binding_shop://已绑设备店铺列表
                Intent intent = new Intent(getActivity(), BindingFacilityShopActivity.class);
                intent.putExtra("isOrderJump", true);
                startActivity(intent);
                break;
            case R.id.ll_display_fee://陈列申请
                if (homeFragment.clientBean == null) {
                    DialogUtil.showCustomDialog(getActivity(), "注意", "请先刷客户档案或选择客户...", "确定", null, null);
                } else {
                    if ("".equals(userBean.getSu_storeid())) {
                        noCarHint();
                        return;
                    }
                    startActivity(DisplayFeeActivity.class);
                }
                break;
            case R.id.ll_display_fee_query://陈列查询
                startActivity(DisplayFeeQueryActivity.class);
                break;
        }
    }

    /**
     * 没有车仓库是提示弹框
     */
    private void noCarHint() {
        DialogUtil.showCustomDialog(getActivity(), "提示", "请联系后台文员进行车仓库关联", "确定", null, null);
    }

    /**
     * 没有关联的业务员id
     */
    private void noCustomerid() {
        DialogUtil.showCustomDialog(getActivity(), "提示", "请联系管理员创建业务员客户!", "确定", null, null);
    }
}
