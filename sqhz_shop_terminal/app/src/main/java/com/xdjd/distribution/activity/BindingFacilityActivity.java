package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.FacilityListingAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.EquipmentBean;
import com.xdjd.distribution.bean.MemberBean;
import com.xdjd.distribution.bean.RelatedDetailBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.BindingFacilityEvent;
import com.xdjd.distribution.event.MemberEvent;
import com.xdjd.distribution.popup.EquipmentListingPopup;
import com.xdjd.distribution.popup.MemberListingPopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BindingFacilityActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.ll_add_facility)
    LinearLayout mLlAddFacility;
    @BindView(R.id.lv_facility)
    NoScrollListView mLvFacility;
    @BindView(R.id.btn_binding)
    Button mBtnBinding;
    @BindView(R.id.tv_member)
    TextView mTvMember;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.tv_contact_name)
    TextView mTvContactName;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.ll_member)
    LinearLayout mLlMember;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.ll_customer_info)
    LinearLayout mLlCustomerInfo;
    @BindView(R.id.ll_customer)
    LinearLayout mLlCustomer;

    //核销员选择列表
    private MemberListingPopup popupMember;
    //设备选择列表
    private EquipmentListingPopup popupFacility;

    private FacilityListingAdapter adapterFacility;
    //核销员bean
    private MemberBean beanMember = null;
    private MemberBean beanMemberOld = null;
    private Intent intent;

    private ClientBean mClientBean;

    private List<EquipmentBean> listEquipmentOld = new ArrayList<>();//原有的设备列表
    private List<EquipmentBean> listEquipmentNew = new ArrayList<>();//设备列表编辑容器

//    private List<EquipmentBean> listNoEquipment;//未关联设备列表

    private String addIdStr = "";//添加的店铺id,用,好分割
    private String delIdsStr = "";//删除的设备id
    private String mb_id = "";//核销员id

    @Override
    protected int getContentView() {
        return R.layout.activity_binding_facility;
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
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("店铺绑定设备");
        mTitleBar.setRightText("已绑店铺");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BindingFacilityShopActivity.class);
            }
        });

        adapterFacility = new FacilityListingAdapter(new ItemOnListener() {
            @Override
            public void onItem(final int position) {
                DialogUtil.showCustomDialog(BindingFacilityActivity.this, "提示", "确定删除这个设备?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        listEquipmentNew.remove(position);
                        adapterFacility.notifyDataSetInvalidated();
                    }

                    @Override
                    public void no() {

                    }
                });
            }
        });
        mLvFacility.setAdapter(adapterFacility);

        mClientBean = (ClientBean) getIntent().getSerializableExtra("clientBean");
        if (mClientBean==null){//如果不是从订单主页跳转到绑定设备店铺列表跳转过来的,判断是不是已经签到店铺
            mClientBean = UserInfoUtils.getClientInfo(this);
        }

        if (mClientBean == null) {
            mLlCustomer.setVisibility(View.GONE);
            mTvHint.setVisibility(View.VISIBLE);
        } else {
            mLlCustomer.setVisibility(View.VISIBLE);
            mTvHint.setVisibility(View.GONE);
            mTvShopName.setText("店铺名称:" + mClientBean.getCc_name());
            mTvContactName.setText("联系人:" + mClientBean.getCc_contacts_name());
            mTvMobile.setText("联系电话:" + mClientBean.getCc_contacts_mobile());

            getEquipmentRelatedDetail();
        }

        initPopupFacility();
    }

    /**
     * 获取店铺的关联详情
     */
    private void getEquipmentRelatedDetail() {
        AsyncHttpUtil<RelatedDetailBean> httpUtil = new AsyncHttpUtil<>(this, RelatedDetailBean.class, new IUpdateUI<RelatedDetailBean>() {
            @Override
            public void updata(RelatedDetailBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listEquipmentOld.clear();
                    listEquipmentNew.clear();
                    listEquipmentOld.addAll(jsonBean.getDataList());//关联的设备列表
                    listEquipmentNew.addAll(jsonBean.getDataList());//关联的设备列表

//                    listNoEquipment = jsonBean.getListData();

                    if (jsonBean.getMmu_memberid() != null) {
                        mLlMember.setVisibility(View.VISIBLE);

                        beanMember = new MemberBean();
                        beanMember.setMb_id(jsonBean.getMmu_memberid());
                        beanMember.setMb_name(jsonBean.getMmu_memberid_nameref());

                        mTvMember.setText(jsonBean.getMmu_memberid_nameref());

                        beanMemberOld = new MemberBean();
                        beanMemberOld.setMb_id(jsonBean.getMmu_memberid());
                        beanMemberOld.setMb_name(jsonBean.getMmu_memberid_nameref());
                    } else {
                        mLlMember.setVisibility(View.GONE);
                    }

                    adapterFacility.setData(listEquipmentNew);
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
        httpUtil.post(M_Url.getEquipmentRelatedDetail, L_RequestParams.getEquipmentRelatedDetail("1", mClientBean.getCc_id()), true);
    }

    @OnClick({R.id.ll_add_facility, R.id.btn_binding, R.id.tv_member, R.id.ll_customer_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_customer_info:
                startActivity(CustomerSelectListingActivity.class);
                break;
            case R.id.ll_add_facility:
                if (mClientBean == null) {
                    showToast("请先选择店铺!");
                    return;
                }
                showPopupFacility();
                break;
            case R.id.btn_binding:
                if (mClientBean == null) {
                    showToast("请先选择店铺!");
                    return;
                }

                addIdStr = "";
                delIdsStr = "";
                //添加增加的设备id
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listEquipmentNew.size(); i++) {
                    sb.append(listEquipmentNew.get(i).getMe_id());
                    if (i != listEquipmentNew.size() - 1) {
                        sb.append(",");
                    }
                }
                addIdStr = sb.toString();

                //添加删除的设备id
                StringBuilder sbDel = new StringBuilder();
                for (int i = 0; i < listEquipmentOld.size(); i++) {//添加删除的设备
                    boolean isHas = false;//是否有这个,默认没有
                    for (int k = 0; k < listEquipmentNew.size(); k++) {
                        if (listEquipmentOld.get(i).getMe_id().equals(listEquipmentNew.get(k).getMe_id())) {
                            isHas = true;
                        }
                    }
                    if (!isHas) {
                        sbDel.append(listEquipmentOld.get(i).getMe_id());
                        sbDel.append(",");
                    }
                }
                if (sbDel.length() > 0) {
                    sbDel.deleteCharAt(sbDel.length() - 1);
                }
                delIdsStr = sbDel.toString();

                DialogUtil.showCustomDialog(this, "提示", "是否绑定店铺?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        relatedEqpAndMangerUser();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
            /*case R.id.tv_member://添加核销员
                if (beanMemberOld != null && !"".equals(beanMemberOld.getMb_id())) {
                    DialogUtil.showCustomDialog(this, "提示", "已经绑定过核销员,如需更换请联系后台人员!", "确定", null, null);
                    return;
                }
                //                showPopupMember();
                intent = new Intent(BindingFacilityActivity.this, MemberListingActivity.class);
                intent.putExtra("beanMember", beanMember);
                startActivity(intent);
                break;*/
        }
    }

    /*private void initPopupMember() {
        popupMember = new MemberListingPopup(this, getResources().getDisplayMetrics().heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
            }
        }, new MemberListingPopup.MemberSearchListener() {
            @Override
            public void onSearch(String searchStr) {
            }
        });
    }
    private void showPopupMember() {
        popupMember.showAtLocation(mLlMain, Gravity.BOTTOM,0,0);
    }*/

    private void initPopupFacility() {
        popupFacility = new EquipmentListingPopup(this, getResources().getDisplayMetrics().heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                EquipmentBean bean = popupFacility.listEquipment.get(position);
                for (EquipmentBean bean1 : listEquipmentNew) {
                    if (bean1.getMe_id().equals(bean.getMe_id())) {
                        showToast("该设备在设备列表中已存在!");
                        return;
                    }
                }

                listEquipmentNew.add(popupFacility.listEquipment.get(position));
                adapterFacility.setData(listEquipmentNew);
                popupFacility.dismiss();
            }
        }, new EquipmentListingPopup.FacilitySearchListener() {
            @Override
            public void onSearch(String searchStr) {

            }
        });
    }

    private void showPopupFacility() {
        popupFacility.setClientBean(mClientBean);
        popupFacility.showPopup();
        popupFacility.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
    }

    private void relatedEqpAndMangerUser() {

        AsyncHttpUtil<MemberBean> httpUtil = new AsyncHttpUtil<>(this, MemberBean.class, new IUpdateUI<MemberBean>() {
            @Override
            public void updata(MemberBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(BindingFacilityActivity.this, mClientBean.getCc_name()+"绑定"+jsonBean.getRepMsg());
                    finishActivity();
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
        httpUtil.post(M_Url.relatedEqpAndMangerUser, L_RequestParams.relatedEqpAndMangerUser(addIdStr, delIdsStr, "", mClientBean), true);
    }

    public void onEventMainThread(MemberEvent event) {
        beanMember = event.getBean();
        mTvMember.setText(beanMember.getMb_name());
    }

    public void onEventMainThread(BindingFacilityEvent event) {
        mClientBean = event.getClientBean();
        mLlCustomer.setVisibility(View.VISIBLE);
        mTvHint.setVisibility(View.GONE);

        mTvShopName.setText("店铺名称:" + mClientBean.getCc_name());
        mTvContactName.setText("联系人:" + mClientBean.getCc_contacts_name());
        mTvMobile.setText("联系电话:" + mClientBean.getCc_contacts_mobile());

        listEquipmentNew.clear();
        listEquipmentOld.clear();

        adapterFacility.notifyDataSetInvalidated();

        getEquipmentRelatedDetail();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
