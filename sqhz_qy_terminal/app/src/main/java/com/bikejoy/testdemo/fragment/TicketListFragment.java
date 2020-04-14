package com.bikejoy.testdemo.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.base.Common;
import com.bikejoy.testdemo.event.CreateOrderSuccessEvent;
import com.bikejoy.testdemo.popup.SelectStorePopup;
import com.bikejoy.testdemo.popup.TicketCouponListPopup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

@SuppressLint("ValidFragment")
public class TicketListFragment extends BaseFragment {

    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;
    @BindView(R.id.tv_donor_ticket)
    TextView mTvDonorTicket;

    private VaryViewHelper helper;

    private List<TicketListBean> list = new ArrayList<>();
    private TicketListAdapter adapter;

    Unbinder unbinder;
    private final int indexType;//订单类型

    private int page = 1;
    private int mFlag = 0;

    private MemberBean customer;//传递过来的客户信息
    private UserBean userBean;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;
    //仓库列表
    private List<StoreListBean> listStore;
    //仓库code
    private String storeCode;
    private String odId;

    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;
    private MemberInformationActivity activity;

    private TicketCouponListPopup ticketListPopup;

    public TicketListFragment(int indexType, MemberBean customer) {
        this.indexType = indexType;
        this.customer = customer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        activity = (MemberInformationActivity) getActivity();

        userBean = UserInfoUtils.getUser(getActivity());

        EventBus.getDefault().register(this);

        if (Common.ROLE3009.equals(userBean.getRoleCode()) ||
                Common.ROLE3011.equals(userBean.getRoleCode())){
            mTvDonorTicket.setVisibility(View.VISIBLE);
        }else{
            mTvDonorTicket.setVisibility(View.GONE);
        }

        mVaryViewHelper = new VaryViewHelper(mLvPull);
        mErrorListener = new MyErrorListener();

        initRefresh(mLvPull);
        mLvPull.setMode(PullToRefreshBase.Mode.BOTH);
        mLvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryTicketListByMember(PublicFinal.FIRST, false, "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryTicketListByMember(PublicFinal.TWO, true, "");
            }
        });

        adapter = new TicketListAdapter();
        adapter.setData(list);
        mLvPull.setAdapter(adapter);
        adapter.setShowMember(false);
        adapter.setListener(new TicketListAdapter.OnTicketListListener() {
            @Override
            public void onIndex(int i) {
                Intent intent = new Intent(getActivity(), TicketVerificationActivity.class);
                intent.putExtra("ticketid", list.get(i).getTm_id());
                startActivity(intent);
            }

            @Override
            public void onCheckTicket(final int i) {
                if (TextUtils.isEmpty(userBean.getCarid())) {
                    showToast("没有车仓库无法核销水票");
                    return;
                }
                DialogUtil.showEditNumDialog(getActivity(), "水票核销", "请输入核销水票数量", "1", list.get(i).getTm_available_num(), "已超出可用水票数量",
                        "核销", "取消", new DialogUtil.MyCustomDialogListener() {
                            @Override
                            public void ok(Dialog dialog, final String num) {
                                DialogUtil.showCustomDialog(getActivity(), "提示", "是否核销" + num + "张水票?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                                    @Override
                                    public void ok() {
                                        verificationTicket(list.get(i).getTm_id(), num);
                                    }

                                    @Override
                                    public void no() {

                                    }
                                });
                            }

                            @Override
                            public void no() {
                            }
                        });
            }
        });

        initTicketPopup();
        queryTicketListByMember(PublicFinal.FIRST, false, "");
    }

    /**
     * 获取订单列表
     */
    private void queryTicketListByMember(int isFirst, boolean isDialog, String searchStr) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<TicketListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TicketListBean.class, new IUpdateUI<TicketListBean>() {
            @Override
            public void updata(TicketListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    TicketListBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        List<TicketListBean> list1 = bean.getRows();
                        for (TicketListBean bean1 : list1) {
                            BigDecimal total;
                            BigDecimal available;
                            if (!TextUtils.isEmpty(bean1.getTm_total_num())) {
                                total = new BigDecimal(bean1.getTm_total_num());
                            } else {
                                total = BigDecimal.ZERO;
                            }
                            if (!TextUtils.isEmpty(bean1.getTm_available_num())) {
                                available = new BigDecimal(bean1.getTm_available_num());
                            } else {
                                available = BigDecimal.ZERO;
                            }
                            bean1.setUse_num(total.subtract(available).toString());
                        }
                        list.addAll(list1);
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    queryTicketListByMember(PublicFinal.FIRST, false, "");
                                }
                            });
                        } else {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getDesc(), mErrorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(), mErrorListener);
            }

            @Override
            public void finish() {
                if (mLvPull != null) {
                    mLvPull.onRefreshComplete();
                }
            }
        });
        httpUtil.post(M_Url.queryTicketListByMember, L_RequestParams.
                queryTicketListByMember(customer.getMb_id(), page + ""), isDialog);
    }

    @OnClick({R.id.tv_donor_ticket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_donor_ticket:
                showTicketPopup();
                break;
        }
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryTicketListByMember(PublicFinal.FIRST, false, "");
        }
    }

    /**
     * 核销水票功能
     *
     * @param ticketMainId
     * @param num
     */
    private void verificationTicket(String ticketMainId, String num) {
        AsyncHttpUtil<TicketListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TicketListBean.class, new IUpdateUI<TicketListBean>() {
            @Override
            public void updata(TicketListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    showToast("核销成功");

                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryTicketListByMember(PublicFinal.FIRST, false, "");
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
        httpUtil.post(M_Url.verificationTicket, L_RequestParams.
                verificationTicket(userBean.getCarid(), ticketMainId, num), true);
    }


    public void onEventMainThread(CreateOrderSuccessEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryTicketListByMember(PublicFinal.TWO, true, "");
    }

    public void searchOrder(String searchStr) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryTicketListByMember(PublicFinal.FIRST, false, searchStr);
    }

    /**
     * 初始化优惠券popup
     */
    private void initTicketPopup() {
        ticketListPopup = new TicketCouponListPopup(getActivity(), new TicketCouponListPopup.OnTicketListener() {
            @Override
            public void onItemTicket(TicketListBean ticketBean, String num) {
                sendWaterTicket(ticketBean, num);
            }
        });
    }

    private void showTicketPopup() {
        //显示popup
        if (ticketListPopup.isShowing()) {
            ticketListPopup.dismiss();
            return;
        }
        ticketListPopup.showPopup(getView(), ticketListPopup, userBean);
    }

    private void sendWaterTicket(TicketListBean ticketBean, String num) {
        AsyncHttpUtil<TicketListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), TicketListBean.class, new IUpdateUI<TicketListBean>() {
            @Override
            public void updata(TicketListBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    UIUtils.Toast(jsonBean.getDesc());
                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryTicketListByMember(PublicFinal.TWO, true, "");
                    ticketListPopup.dismiss();
                } else {
                    UIUtils.Toast(jsonBean.getDesc());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.sendWaterTicket, L_RequestParams.sendWaterTicket(customer.getMb_id(), ticketBean.getTm_id(), userBean.getUserid(), num), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
