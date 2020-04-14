package com.bikejoy.testdemo.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.view.MyGridLayoutManager;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.base.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/1/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CRMFragment extends BaseFragment {

    @BindView(R.id.rv_professional)
    RecyclerView mRvProfessional;
    Unbinder unbinder;
    @BindView(R.id.rv_storehouse)
    RecyclerView mRvStorehouse;
    @BindView(R.id.rv_container)
    RecyclerView mRvContainer;
    @BindView(R.id.rv_member)
    RecyclerView mRvMember;
    @BindView(R.id.ll_storehouse)
    LinearLayout mLlStorehouse;
    @BindView(R.id.rv_exploit)
    RecyclerView mRvExploit;
    @BindView(R.id.ll_member)
    LinearLayout mLlMember;
    @BindView(R.id.ll_exploit)
    LinearLayout mLlExploit;
    @BindView(R.id.ll_pro)
    LinearLayout mLlPro;


    private GridTypeAdapter adapterProfessional;
    private List<GridBean> listPro;

    private GridTypeAdapter adapterStorehouse;
    private List<GridBean> listStorehouse;
    //柜子
    private GridTypeAdapter adapterContainer;
    private List<GridBean> listContainer;
    //客户
    private GridTypeAdapter adapterMember;
    private List<GridBean> listMember;
    //市场开发
    private GridTypeAdapter adapterExploit;
    private List<GridBean> listExploit;

    private UserBean mUserBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crm, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mUserBean = UserInfoUtils.getUser(getActivity());
        initData();

        adapterProfessional = new GridTypeAdapter();
        adapterProfessional.setList(listPro);
        adapterProfessional.setListener(new GridTypeAdapter.OnGridTypeListener() {
            @Override
            public void onItem(String type) {
                typeListener(type);
            }
        });

        adapterStorehouse = new GridTypeAdapter();
        adapterStorehouse.setList(listStorehouse);
        adapterStorehouse.setListener(new GridTypeAdapter.OnGridTypeListener() {
            @Override
            public void onItem(String type) {
                typeListener(type);
            }
        });

        adapterContainer = new GridTypeAdapter();
        adapterContainer.setList(listContainer);
        adapterContainer.setListener(new GridTypeAdapter.OnGridTypeListener() {
            @Override
            public void onItem(String type) {
                typeListener(type);
            }
        });

        adapterMember = new GridTypeAdapter();
        adapterMember.setList(listMember);
        adapterMember.setListener(new GridTypeAdapter.OnGridTypeListener() {
            @Override
            public void onItem(String type) {
                typeListener(type);
            }
        });

        adapterExploit = new GridTypeAdapter();
        adapterExploit.setList(listExploit);
        adapterExploit.setListener(new GridTypeAdapter.OnGridTypeListener() {
            @Override
            public void onItem(String type) {
                typeListener(type);
            }
        });

        MyGridLayoutManager noScrollLayoutManagr = new MyGridLayoutManager(getActivity(), 5);
        noScrollLayoutManagr.setScrollEnabled(false);
        mRvProfessional.setLayoutManager(noScrollLayoutManagr);
        mRvProfessional.setAdapter(adapterProfessional);

        MyGridLayoutManager noScrollLayoutManagr02 = new MyGridLayoutManager(getActivity(), 5);
        noScrollLayoutManagr02.setScrollEnabled(false);
        mRvStorehouse.setLayoutManager(noScrollLayoutManagr02);
        mRvStorehouse.setAdapter(adapterStorehouse);

        //        MyGridLayoutManager noScrollLayoutManagr03 = new MyGridLayoutManager(getActivity(), 5);
        //        noScrollLayoutManagr03.setScrollEnabled(false);
        //        mRvContainer.setLayoutManager(noScrollLayoutManagr03);
        //        mRvContainer.setAdapter(adapterContainer);

        MyGridLayoutManager noScrollLayoutManagr04 = new MyGridLayoutManager(getActivity(), 5);
        noScrollLayoutManagr04.setScrollEnabled(false);
        mRvMember.setLayoutManager(noScrollLayoutManagr04);
        mRvMember.setAdapter(adapterMember);

        MyGridLayoutManager noScrollLayoutManagr05 = new MyGridLayoutManager(getActivity(), 5);
        noScrollLayoutManagr05.setScrollEnabled(false);
        mRvExploit.setLayoutManager(noScrollLayoutManagr05);
        mRvExploit.setAdapter(adapterExploit);
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (mUserBean.getRoleCode()) {
            case Common.ROLE3002://主管时才请求
                getToCheckNum();
                getPackageApplyList();
                break;
            case Common.ROLE3004://调度员
                queryOrderList();
                queryTicketSendApplyList();
                break;
            case Common.ROLE3003://配送员
                queryDeliveryList();
                break;
            case Common.ROLE3006://库存管理
                getPackageApplyList();
                break;
        }
    }

    private void initData() {

        listPro = new ArrayList<>();
        listStorehouse = new ArrayList<>();
        listMember = new ArrayList<>();
        listExploit = new ArrayList<>();

        // 企业端用户角色
        // 客户开发3001   客户开发主管3002  配送员3003
        // 调度员3004   配送站3005   库管人员3006
        // 采购员3007  财务人员权限3008  总经理3009
        // 设备运维权限3010  管理员权限3011
        switch (mUserBean.getRoleCode()) {
            case Common.ROLE3001://开发员
                listMember.add(new GridBean(R.mipmap.icon_member, "我的客户", Common.typeMember));
                listMember.add(new GridBean(R.mipmap.icon_note, "拜访记录", Common.typeNote));
                listMember.add(new GridBean(R.mipmap.icon_customer_pool, "客户池", Common.typeCustomerPool));
                listMember.add(new GridBean(R.mipmap.icon_apply_member, "我的申请记录", Common.typeApplyMemberList));

                listExploit.add(new GridBean(R.mipmap.icon_share_xcx, "分享小程序", Common.typeShareRegister));
                listExploit.add(new GridBean(R.mipmap.icon_package_relation, "分享套餐", Common.typePackageRelationList));
                //listExploit.add(new GridBean(R.mipmap.icon_package_relation_store, "领用记录", Common.typePackageRelationStore));
                listExploit.add(new GridBean(R.mipmap.icon_my_apply_action, "我的申请", Common.typeApplyPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_office_recipients, "办公领用", Common.typeOfficeOfRecipients));
                //listExploit.add(new GridBean(R.mipmap.icon_action_list, "申请开发列表", Common.typePackageList));

                mLlPro.setVisibility(View.GONE);
                mLlStorehouse.setVisibility(View.GONE);
                break;
            case Common.ROLE3002://客户开发主管
                listStorehouse.add(new GridBean(R.mipmap.icon_card_store, "车仓库", Common.typeCardStock));

                listMember.add(new GridBean(R.mipmap.icon_member, "我的客户", Common.typeMember));
                listMember.add(new GridBean(R.mipmap.icon_to_audit, "待审核列表", Common.typeToAudit));
                listMember.add(new GridBean(R.mipmap.icon_note, "拜访记录", Common.typeNote));
                listMember.add(new GridBean(R.mipmap.icon_customer_pool, "客户池", Common.typeCustomerPool));
                listMember.add(new GridBean(R.mipmap.icon_agent, "合伙人列表", Common.typeBaseAgent));

                listExploit.add(new GridBean(R.mipmap.icon_share_xcx, "分享小程序", Common.typeShareRegister));
                listExploit.add(new GridBean(R.mipmap.icon_package_relation, "分享套餐", Common.typePackageRelationList));
                listExploit.add(new GridBean(R.mipmap.icon_package_relation_store, "开发领用记录", Common.typePackageRelationStore));
                listExploit.add(new GridBean(R.mipmap.icon_my_apply_action, "待审核", Common.typeApplyPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_apply_action_list, "我的申请", Common.typeApplyLeaderPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_office_recipients, "办公领用", Common.typeOfficeOfRecipients));
                //listExploit.add(new GridBean(R.mipmap.icon_action_list, "申请开发列表", Common.typePackageList));

                mLlPro.setVisibility(View.GONE);
                //mLlStorehouse.setVisibility(View.GONE);
                break;
            case Common.ROLE3003://配送员
                listPro.add(new GridBean(R.mipmap.icon_distr_task, "配送任务", Common.typeDistrTask));

                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_breakage, "商品报损", Common.typeBreakageApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_breakage_search, "报损查询", Common.typeBreakageSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_card_store, "车仓库", Common.typeCardStock));
                listStorehouse.add(new GridBean(R.mipmap.icon_record, "送货记录", Common.typeDeliveryWaterSearch));

                mLlExploit.setVisibility(View.GONE);
                mLlMember.setVisibility(View.GONE);
                break;
            case Common.ROLE3004://调度员
                listPro.add(new GridBean(R.mipmap.icon_send_the_goods, "待发货订单", Common.typeSendTheGoods));
                listPro.add(new GridBean(R.mipmap.icon_send_apply, "配送申请单", Common.typeTicketSendApplyList));

                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_delivery_produc, "配货清单", Common.typeDeliveryProducAndNum));

                mLlExploit.setVisibility(View.GONE);
                mLlMember.setVisibility(View.GONE);
                break;
            case Common.ROLE3005://配送站
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_delivery_produc, "配货清单", Common.typeDeliveryProducAndNum));

                mLlPro.setVisibility(View.GONE);
                mLlExploit.setVisibility(View.GONE);
                mLlMember.setVisibility(View.GONE);
                break;
            case Common.ROLE3006://库管人员
                listPro.add(new GridBean(R.mipmap.icon_order, "订单", Common.typeOrder));
                listPro.add(new GridBean(R.mipmap.icon_send_the_goods, "待发货订单", Common.typeSendTheGoods));
                listPro.add(new GridBean(R.mipmap.icon_send_apply, "配送申请单", Common.typeTicketSendApplyList));

                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_apply, "入库申请", Common.typePurchaseApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_search, "入库查询", Common.typePurchaseSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_breakage, "商品报损", Common.typeBreakageApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_breakage_search, "报损查询", Common.typeBreakageSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_delivery_produc, "配货清单", Common.typeDeliveryProducAndNum));
                listStorehouse.add(new GridBean(R.mipmap.icon_stock_inquiry, "库存查询", Common.typeStockInquiry));


                listMember.add(new GridBean(R.mipmap.icon_note, "拜访记录", Common.typeNote));
                listMember.add(new GridBean(R.mipmap.icon_ticket, "客户水票", Common.typeTicketList));

                listExploit.add(new GridBean(R.mipmap.icon_my_apply_action, "待审核", Common.typeApplyPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_office_recipients, "办公领用", Common.typeOfficeOfRecipients));
                break;
            case Common.ROLE3007://采购员
                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_apply, "入库申请", Common.typePurchaseApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_search, "入库查询", Common.typePurchaseSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));

                mLlPro.setVisibility(View.GONE);
                //mLlExploit.setVisibility(View.GONE);
                listExploit.add(new GridBean(R.mipmap.icon_apply_action_list, "我的申请", Common.typeApplyLeaderPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_office_recipients, "办公领用", Common.typeOfficeOfRecipients));

                mLlMember.setVisibility(View.GONE);
                break;
            case Common.ROLE3008:
            case Common.ROLE3009:
            case Common.ROLE3011:
                listPro.add(new GridBean(R.mipmap.icon_order, "订单", Common.typeOrder));

                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_apply, "入库申请", Common.typePurchaseApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_search, "入库查询", Common.typePurchaseSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));

                listMember.add(new GridBean(R.mipmap.icon_member, "我的客户", Common.typeMember));
                listMember.add(new GridBean(R.mipmap.icon_note, "拜访记录", Common.typeNote));
                listMember.add(new GridBean(R.mipmap.icon_ticket, "客户水票", Common.typeTicketList));
                listMember.add(new GridBean(R.mipmap.icon_all_member, "全部客户", Common.typeAllMember));
                listMember.add(new GridBean(R.mipmap.icon_agent, "合伙人列表", Common.typeBaseAgent));
                listMember.add(new GridBean(R.mipmap.icon_agent_amount, "合伙人资金汇总", Common.typeQueryAgentAmountData));
                listMember.add(new GridBean(R.mipmap.icon_agent_amount, "合伙人战绩汇总", Common.typeQueryProfitRecommendListData));

                //mLlExploit.setVisibility(View.GONE);
                listExploit.add(new GridBean(R.mipmap.icon_apply_action_list, "我的申请", Common.typeApplyLeaderPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_office_recipients, "办公领用", Common.typeOfficeOfRecipients));
                listExploit.add(new GridBean(R.mipmap.icon_cash, "提现列表", Common.typeCashApply));
                listExploit.add(new GridBean(R.mipmap.icon_invoice, "发票申请列表", Common.typeInvoice));
                break;
            case Common.ROLE3010://设备运维
                break;
            default:
                listPro.add(new GridBean(R.mipmap.icon_order, "订单", Common.typeOrder));
                listPro.add(new GridBean(R.mipmap.icon_send_the_goods, "待发货订单", Common.typeSendTheGoods));
                listPro.add(new GridBean(R.mipmap.icon_send_apply, "配送申请单", Common.typeTicketSendApplyList));

                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_apply, "入库申请", Common.typePurchaseApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_purchase_search, "入库查询", Common.typePurchaseSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
                listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_card_store, "车仓库", Common.typeCardStock));
                listStorehouse.add(new GridBean(R.mipmap.icon_record, "送货记录", Common.typeDeliveryWaterSearch));
                listStorehouse.add(new GridBean(R.mipmap.icon_delivery_produc, "配货清单", Common.typeDeliveryProducAndNum));

                listMember.add(new GridBean(R.mipmap.icon_member, "我的客户", Common.typeMember));
                listMember.add(new GridBean(R.mipmap.icon_note, "拜访记录", Common.typeNote));
                listMember.add(new GridBean(R.mipmap.icon_ticket, "客户水票", Common.typeTicketList));
                listMember.add(new GridBean(R.mipmap.icon_customer_pool, "客户池", Common.typeCustomerPool));
                listMember.add(new GridBean(R.mipmap.icon_apply_member, "我的申请记录", Common.typeApplyMemberList));
                //                listMember.add(new GridBean(R.mipmap.icon_add_community, "添加写字楼", Common.typeAddBuilding));
                //                listMember.add(new GridBean(R.mipmap.icon_community, "写字楼列表", Common.typeBuildingList));


                listExploit.add(new GridBean(R.mipmap.icon_share_xcx, "分享小程序", Common.typeShareRegister));
                listExploit.add(new GridBean(R.mipmap.icon_package_relation, "分享套餐", Common.typePackageRelationList));
                listExploit.add(new GridBean(R.mipmap.icon_my_apply_action, "申请套餐记录", Common.typeApplyPackageList));
                listExploit.add(new GridBean(R.mipmap.icon_office_recipients, "办公领用", Common.typeOfficeOfRecipients));
                break;
        }

        //        listPro.add(new GridBean(R.mipmap.icon_order, "订单", Common.typeOrder));
        //        listPro.add(new GridBean(R.mipmap.icon_send_the_goods, "待发货订单", Common.typeSendTheGoods));
        //        listPro.add(new GridBean(R.mipmap.icon_distr_task, "配送任务", Common.typeDistrTask));
        //        listPro.add(new GridBean(R.mipmap.icon_send_apply, "配送申请单", Common.typeTicketSendApplyList));
        //
        //        listStorehouse.add(new GridBean(R.mipmap.icon_purchase_apply, "入库申请", Common.typePurchaseApply));
        //        listStorehouse.add(new GridBean(R.mipmap.icon_purchase_search, "入库查询", Common.typePurchaseSearch));
        //        listStorehouse.add(new GridBean(R.mipmap.icon_allot_apply, "调拨申请", Common.typeAllotApply));
        //        listStorehouse.add(new GridBean(R.mipmap.icon_allot_search, "调拨查询", Common.typeAllotSearch));
        //        listStorehouse.add(new GridBean(R.mipmap.icon_card_store, "车仓库", Common.typeCardStock));
        //        listStorehouse.add(new GridBean(R.mipmap.icon_record, "送货记录", Common.typeDeliveryWaterSearch));
        //        listStorehouse.add(new GridBean(R.mipmap.icon_card_store, "配货清单", Common.typeDeliveryProducAndNum));

        //        listContainer = new ArrayList<>();
        //        listContainer.add(new GridBean(R.mipmap.icon_store, "柜子", Common.typeStock));
        //        listContainer.add(new GridBean(R.mipmap.icon_stock_sales, "柜子销量", Common.typeStockSales));
        //        listContainer.add(new GridBean(R.mipmap.icon_log, "柜子日志", Common.typeStockLog));
        //        listContainer.add(new GridBean(R.mipmap.icon_alert, "警告日志", Common.typeAlertLog));
        //        listContainer.add(new GridBean(R.mipmap.icon_water_record, "取水记录", Common.typeWaterRecord));

        //        listMember.add(new GridBean(R.mipmap.icon_member, "会员", Common.typeMember));
        //        listMember.add(new GridBean(R.mipmap.icon_note, "拜访记录", Common.typeNote));
        //        listMember.add(new GridBean(R.mipmap.icon_member_gift, "赠送记录", Common.typeMemberGift));
        //        listMember.add(new GridBean(R.mipmap.icon_ticket, "客户水票", Common.typeTicketList));
        //        listMember.add(new GridBean(R.mipmap.icon_add_community, "添加写字楼", Common.typeAddBuilding));
        //        listMember.add(new GridBean(R.mipmap.icon_community, "写字楼列表", Common.typeBuildingList));
    }

    private void typeListener(String type) {
        Intent intent;
        switch (type) {
            case Common.typeOrder:
                startActivity(OrderActivity.class);
                break;
            case Common.typeDistrTask:
                startActivity(DeliveryTaskActivity.class);
                break;
            case Common.typeGroupOrder:
                startActivity(OrderGroupActivity.class);
                break;
            case Common.typeCounpon:
                startActivity(CouponActivity.class);
                break;
            case Common.typeNote:
                startActivity(MemberNoteActivity.class);
                break;
            case Common.typeStockControl:
                startActivity(StockControlActivity.class);
                break;
            case Common.typeStock:
                startActivity(StorehouseActivity.class);
                break;
            case Common.typeMember:
                startActivity(MemberActivity.class);
                break;
            case Common.typeStockSales:
                startActivity(StoreOperateListActivity.class);
                break;
            case Common.typeStockLog://柜子日志
                startActivity(MonitoringLogActivity.class);
                break;
            case Common.typeAlertLog://柜子警告日志列表
                startActivity(AlertLogActivity.class);
                break;
            case Common.typeTicketList:
                startActivity(TicketListActivity.class);
                break;
            case Common.typeWaterRecord://取水记录
                startActivity(GetWaterRecordActivity.class);
                break;
            case Common.typeMemberGift://水票赠送记录
                startActivity(MemberGiftRecordActivity.class);
                break;
            case Common.typeCardStock://车仓库
                startActivity(CardStockActivity.class);
                break;
            case Common.typePurchaseApply://入库申请
                startActivity(PurchaseApplyActivity.class);
                break;
            case Common.typePurchaseSearch://入库查询
                intent = new Intent(getActivity(), ErpSearchActivity.class);
                intent.putExtra("type", "101");
                startActivity(intent);
                break;
            case Common.typeAllotApply://调拨申请
                //                if (mUserBean.getCarid() != null && mUserBean.getCarid().length() > 0) {
                startActivity(AllotApplyActivity.class);
                //                } else {
                //                    DialogUtil.showCustomDialog(getActivity(), "提示",
                //                            "请联系后台管理员添加车仓库", "确定", null, null);
                //                }
                break;
            case Common.typeAllotSearch://调拨查询
                intent = new Intent(getActivity(), ErpSearchActivity.class);
                intent.putExtra("type", "201");
                startActivity(intent);
                break;
            case Common.typeAddBuilding:
                startActivity(AddAndUpdateCommunityActivity.class);
                break;
            case Common.typeBuildingList:
                startActivity(CommunityListActivity.class);
                break;
            case Common.typeTicketSendApplyList://配送申请单
                startActivity(TicketSendApplyListActivity.class);
                break;
            case Common.typeDeliveryProducAndNum://配货清单
                startActivity(DeliveryProducAndNumActivity.class);
                break;
            case Common.typeSendTheGoods://跳转待发货订单界面
                intent = new Intent(getActivity(), SendTheGoodsActivity.class);
                startActivity(intent);
                break;
            case Common.typeDeliveryWaterSearch://配送货记录
                startActivity(DeliveryWaterSearchActivity.class);
                break;
            case Common.typeCustomerPool://客户池
                startActivity(PoolCustomerActivity.class);
                break;
            case Common.typePackageList://套餐列表(申请开发列表)
                startActivity(PackageListActivity.class);
                break;
            case Common.typeApplyPackageList://申请中套餐列表
                intent = new Intent(getActivity(), PackageApplyListActivity.class);
                if (Common.ROLE3002.equals(mUserBean.getRoleCode())) { //开发主管时才传递参数
                    intent.putExtra("queryType", "2");
                }
                startActivity(intent);
                break;
            case Common.typeApplyLeaderPackageList:
                intent = new Intent(getActivity(), MyPackageApplyListActivity.class);
                intent.putExtra("queryType", "1");
                startActivity(intent);
                break;
            case Common.typePackageRelationList://已申请套餐列表
                startActivity(PackageRelationListActivity.class);
                break;
            case Common.typeShareRegister://分享小程序
                DialogUtil.showCustomDialog(getActivity(), "提示", "是否进行分享小程序?", "分享",
                        "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                getShareRegisterVO();
                            }

                            @Override
                            public void no() {

                            }
                        });
                break;
            case Common.typeApplyMemberList://我的客户申请记录
                startActivity(MyApplyMemberListActivity.class);
                break;
            case Common.typeToAudit:
                startActivity(ToAuditActivity.class);
                break;
            case Common.typeBreakageApply://商品报损
                if (Common.ROLE3003.equals(mUserBean.getRoleCode()) && TextUtils.isEmpty(mUserBean.getCarid())) { //配送员
                    DialogUtil.showCustomDialog(getActivity(), "提示",
                            "您没有车仓库,无法报损", "确定", null, null);
                } else {
                    startActivity(BreakageApplyActivity.class);
                }
                break;
            case Common.typeBreakageSearch://报损查询
                intent = new Intent(getActivity(), ErpSearchActivity.class);
                intent.putExtra("type", "301");
                intent.putExtra("stockType", "30103");
                startActivity(intent);
                break;
            case Common.typePackageRelationStore://开发领用记录
                intent = new Intent(getActivity(), PackageRelationListActivity.class);
                if (Common.ROLE3002.equals(mUserBean.getRoleCode())) {
                    intent.putExtra("sprType", "2");//开发主管
                } else {
                    intent.putExtra("sprType", "3");//开发员
                }
                startActivity(intent);
                break;
            case Common.typeAllMember://全部客户
                startActivity(AllMemberActivity.class);
                break;
            case Common.typeBaseAgent://全部金牌合伙人列表
                startActivity(AgentListActivity.class);
                break;
            case Common.typeStockInquiry://库存查询
                intent = new Intent(getActivity(), StockInquiryActivity.class);
                startActivity(intent);
                break;
            case Common.typeOfficeOfRecipients://办公领用
                intent = new Intent(getActivity(), AddProductLeaderApplyActivity.class);
                intent.putExtra("spaType", "4");
                startActivity(intent);
                break;
            case Common.typeQueryAgentAmountData://合伙人资金汇总
                startActivity(AgentAmountDataActivity.class);
                break;
            case Common.typeCashApply://提现列表
                startActivity(CashApplyListActivity.class);
                break;
            case Common.typeInvoice://开票列表
                startActivity(InvoiceListActivity.class);
                break;
            case Common.typeQueryProfitRecommendListData://战绩
                startActivity(GlProfitRecommendListAcitivity.class);
                break;
        }
    }

    /**
     * 分享小程序
     */
    private void getShareRegisterVO() {
        AsyncHttpUtil<SharePromotionBean> httpUtil = new AsyncHttpUtil<>(getActivity(), SharePromotionBean.class,
                new IUpdateUI<SharePromotionBean>() {
                    @Override
                    public void updata(SharePromotionBean jsonBean) {
                        if ("200".equals(jsonBean.getCode())) {
                            SharePromotionBean bean = jsonBean.getData();
                            Platform.ShareParams params = new Platform.ShareParams();
                            params.setWxUserName(bean.getGid());  //小程序原始ID
                            params.setWxPath(bean.getPath());//分享小程序页面路径
                            params.setTitle(bean.getTitle());
                            params.setText(bean.getDescription());
                            params.setImageUrl(bean.getImg());
                            params.setUrl(bean.getWebpageUrl());

                            params.setShareType(Platform.SHARE_WXMINIPROGRAM);//分享小程序类型

                            int miniType = 0;//// 正式版:0，测试版:1，体验版:2
                            try {
                                miniType = Integer.parseInt(bean.getMiniprogramType());
                            } catch (Exception e) {
                                miniType = 0;
                            }
                            params.setWxMiniProgramType(miniType);

                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                            wechat.share(params);

                            wechat.setPlatformActionListener(new PlatformActionListener() {
                                @Override
                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                    showToast("分享成功");
                                }

                                @Override
                                public void onError(Platform platform, int i, Throwable throwable) {
                                    showToast(throwable.toString());
                                }

                                @Override
                                public void onCancel(Platform platform, int i) {

                                }
                            });
                        } else {
                            showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.getShareRegisterVO, L_RequestParams.getShareRegisterVO(), true);
    }

    /**
     * 获取待审核数量
     */
    private void getToCheckNum() {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class,
                new IUpdateUI<BaseBean>() {
                    @Override
                    public void updata(BaseBean jsonBean) {
                        if ("200".equals(jsonBean.getCode())) {
                            for (GridBean bean : listMember) {
                                if (Common.typeToAudit.equals(bean.getType())) {
                                    String num = jsonBean.getData().toString();
                                    if (!TextUtils.isEmpty(num) && !"0".equals(num)) {
                                        bean.setTotalNum(num);
                                    } else {
                                        bean.setTotalNum("");
                                    }
                                    break;
                                }
                            }
                            adapterMember.setList(listMember);
                        } else {
                            //showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.getToCheckNum, L_RequestParams.getToCheckNum(), true);
    }

    /**
     * 获取待审核列表
     */
    private void getPackageApplyList() {
        AsyncHttpUtil<PackageApplyListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), PackageApplyListBean.class, new
                IUpdateUI<PackageApplyListBean>() {
                    @Override
                    public void updata(PackageApplyListBean jsonBean) {
                        if ("200".equals(jsonBean.getCode())) {
                            PackageApplyListBean bean = jsonBean.getData();

                            for (GridBean beanGrid : listExploit) {
                                if (Common.typeApplyPackageList.equals(beanGrid.getType())) {

                                    if (!TextUtils.isEmpty(bean.getTotal()) && !"0".equals(bean.getTotal())) {
                                        beanGrid.setTotalNum(bean.getTotal());
                                    } else {
                                        beanGrid.setTotalNum("");
                                    }
                                    break;
                                }
                            }
                            adapterExploit.setList(listExploit);
                        } else {
                            //showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.getPackageApplyList, L_RequestParams.getPackageApplyListTotal("1", "1", "0"), false);
    }

    /**
     * 获取配送订单列表
     */
    private void queryOrderList() {
        AsyncHttpUtil<OrderInfo> httpUtil = new AsyncHttpUtil<>(getActivity(), OrderInfo.class, new IUpdateUI<OrderInfo>() {
            @Override
            public void updata(OrderInfo jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    OrderInfo bean = jsonBean.getData();
                    for (GridBean beanGrid : listPro) {
                        if (Common.typeSendTheGoods.equals(beanGrid.getType())) {
                            if (!TextUtils.isEmpty(bean.getTotal()) && !"0".equals(bean.getTotal())) {
                                beanGrid.setTotalNum(bean.getTotal());
                            } else {
                                beanGrid.setTotalNum("");
                            }
                            break;
                        }
                    }
                    adapterProfessional.setList(listPro);
                } else {
                    //showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.queryOrderList, L_RequestParams.queryOrderListQueryTotal("", "",
                "2", "", "", "1", "", "", "",
                "", "", "1", "1", "0"), false);
    }

    /**
     * 获取仓库商品列表
     */
    private void queryTicketSendApplyList() {
        AsyncHttpUtil<TicketSendApplyBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TicketSendApplyBean.class,
                new IUpdateUI<TicketSendApplyBean>() {
                    @Override
                    public void updata(TicketSendApplyBean jsonBean) {
                        if ("200".equals(jsonBean.getCode())) {
                            TicketSendApplyBean bean = jsonBean.getData();

                            for (GridBean beanGrid : listPro) {
                                if (Common.typeTicketSendApplyList.equals(beanGrid.getType())) {

                                    if (!TextUtils.isEmpty(bean.getTotal()) && !"0".equals(bean.getTotal())) {
                                        beanGrid.setTotalNum(bean.getTotal());
                                    } else {
                                        beanGrid.setTotalNum("");
                                    }
                                    break;
                                }
                            }
                            adapterProfessional.setList(listPro);
                        } else {
                            //showToast(jsonBean.getDesc());
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

        httpUtil.post(M_Url.queryTicketSendApplyList, L_RequestParams.queryTicketSendApplyListTotal(
                "1", "1", "", "0"), false);
    }

    /**
     * 获取配送订单总数量
     */
    private void queryDeliveryList() {
        AsyncHttpUtil<DeliveryMainAndDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(),
                DeliveryMainAndDetailBean.class, new IUpdateUI<DeliveryMainAndDetailBean>() {
            @Override
            public void updata(DeliveryMainAndDetailBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    DeliveryMainAndDetailBean bean = jsonBean.getData();

                    for (GridBean beanGrid : listPro) {
                        if (Common.typeDistrTask.equals(beanGrid.getType())) {
                            if (!TextUtils.isEmpty(bean.getTotal()) && !"0".equals(bean.getTotal())) {
                                beanGrid.setTotalNum(bean.getTotal());
                            } else {
                                beanGrid.setTotalNum("");
                            }
                            break;
                        }
                    }
                    adapterProfessional.setList(listPro);
                } else {
                    //showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.queryDeliveryList, L_RequestParams.queryDeliveryList(UserInfoUtils.getId(getActivity()),
                "3", "1", "0", mUserBean.getUsertype()), false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
