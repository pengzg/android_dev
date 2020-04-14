package com.bikejoy.testdemo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.event.UpdateCouponListEvent;
import com.bikejoy.testdemo.popup.CouponListPopup;

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
public class CouponMemberFragment extends BaseFragment {

    @BindView(R.id.lv_pull)
    PullToRefreshListView mLvPull;
    @BindView(R.id.tv_donor_coupon)
    TextView mTvDonorCoupon;

    private CouponMemberAdapter adapter;
    private List<CouponBean> list = new ArrayList<>();

    Unbinder unbinder;
    private final int indexType;//订单类型

    private int page = 1;
    private int mFlag = 0;

    private MemberBean customer;//传递过来的客户信息

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    private MemberInformationActivity activity;

    private CouponListPopup couponListPopup;

    public CouponMemberFragment(int indexType, MemberBean customer) {
        this.indexType = indexType;
        this.customer = customer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_coupon, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        activity = (MemberInformationActivity) getActivity();

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
                queryCouponList(PublicFinal.FIRST, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                mFlag = 2;
                queryCouponList(PublicFinal.TWO, true);
            }
        });

        adapter = new CouponMemberAdapter();
        adapter.setData(list);
        mLvPull.setAdapter(adapter);

        queryCouponList(PublicFinal.FIRST, false);
        initCouponPopup();
    }

    /**
     * 获取优惠券列表
     */
    private void queryCouponList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<CouponBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CouponBean.class, new IUpdateUI<CouponBean>() {
            @Override
            public void updata(CouponBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    CouponBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    queryCouponList(PublicFinal.FIRST, false);
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
        httpUtil.post(M_Url.queryCouponList, L_RequestParams.queryCouponList(customer.getMb_id(), "2",page+""), isDialog);
    }

    @OnClick({R.id.tv_donor_coupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_donor_coupon:
                showCouponPopup();
                break;
        }
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryCouponList(PublicFinal.FIRST, false);
        }
    }

    /**
     * 初始化优惠券popup
     */
    private void initCouponPopup() {
        couponListPopup = new CouponListPopup(getActivity(), new CouponListPopup.OnCouponListener() {
            @Override
            public void onItemCoupon(CouponBean couponBean,String num) {
                sendCouponToMember(couponBean,num);
            }
        });
    }

    private void showCouponPopup() {
        //显示popup
        if (couponListPopup.isShowing()) {
            couponListPopup.dismiss();
            return;
        }
        couponListPopup.showPopup(getView(),couponListPopup,customer);
    }

    private void sendCouponToMember(CouponBean couponBean,String num) {
        AsyncHttpUtil<CouponBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CouponBean.class, new IUpdateUI<CouponBean>() {
            @Override
            public void updata(CouponBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    UIUtils.Toast(jsonBean.getDesc());
                    //刷新优惠券列表
                    EventBus.getDefault().post(new UpdateCouponListEvent());
                    couponListPopup.dismiss();
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
        httpUtil.post(M_Url.sendCouponToMember, L_RequestParams.sendCouponToMember(couponBean.getCb_id(),customer.getMb_id(),couponBean.getCb_shopid(),num), true);
    }

    public void onEventMainThread(UpdateCouponListEvent event) {
//        page = 1;
//        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryCouponList(PublicFinal.FIRST, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

}
