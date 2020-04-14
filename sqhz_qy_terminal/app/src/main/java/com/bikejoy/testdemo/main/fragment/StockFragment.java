package com.bikejoy.testdemo.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.event.StockUpdateListEvent;

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
 *     time   : 2018/4/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class StockFragment extends BaseFragment {
    Unbinder unbinder;

    @BindView(R.id.my_coupon_convert_tv)
    TextView mMyCouponConvertTv;
    @BindView(R.id.rl_scan)
    RelativeLayout mRlScan;
    @BindView(R.id.lv_list)
    ListView mLvList;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.ll_purchase_search)
    LinearLayout mLlPurchaseSearch;
    @BindView(R.id.ll_purchase_apply)
    LinearLayout mLlPurchaseApply;
    @BindView(R.id.ll_allot_search)
    LinearLayout mLlAllotSearch;
    @BindView(R.id.ll_allot_apply)
    LinearLayout mLlAllotApply;

    private CartStockAdapter adapter;
    private List<ProductStoreBean> list = new ArrayList();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper = null;
    private MyErrorListener mErrorListener;

    private OnStoreEmptyListener emptyListener;

    private UserBean mUserBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stock_control, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mEtSearch.setHint("请输入商品名称");
        mUserBean = UserInfoUtils.getUser(getActivity());

        adapter = new CartStockAdapter();
        adapter.setData(list);
        mLvList.setAdapter(adapter);

        mVaryViewHelper = new VaryViewHelper(mRefreshLayout);
        mErrorListener = new MyErrorListener();

        emptyListener = new OnStoreEmptyListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull  RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                dataGridStore(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                dataGridStore(PublicFinal.TWO, true);
            }
        });

        if (mUserBean.getCarid()!=null && mUserBean.getCarid().length()>0){
            dataGridStore(PublicFinal.FIRST, false);
        }else{
            mVaryViewHelper.showEmptyView("没有分配车仓库,请联系后台管理员",emptyListener);
        }
    }

    public class OnStoreEmptyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
           /* page = 1;
            mFlag = 1;
            list.clear();
            adapter.notifyDataSetChanged();
            dataGridStore(PublicFinal.TWO, false);*/
        }
    }

    @OnClick({R.id.rl_scan, R.id.ll_search,R.id.ll_purchase_search, R.id.ll_purchase_apply, R.id.ll_allot_search, R.id.ll_allot_apply})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_scan:
                startActivity(StorehouseDetailActivity.class);
                break;
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                dataGridStore(PublicFinal.FIRST, false);
                break;
            case R.id.ll_purchase_search:
                intent = new Intent(getActivity(),ErpSearchActivity.class);
                intent.putExtra("type","101");
                startActivity(intent);
                break;
            case R.id.ll_purchase_apply://入库
                startActivity(PurchaseApplyActivity.class);
                break;
            case R.id.ll_allot_search:
                intent = new Intent(getActivity(),ErpSearchActivity.class);
                intent.putExtra("type","201");
                startActivity(intent);
                break;
            case R.id.ll_allot_apply://调拨
                if (mUserBean.getCarid()!=null && mUserBean.getCarid().length()>0){
                    startActivity(AllotApplyActivity.class);
                }else{
                    DialogUtil.showCustomDialog(getActivity(),"提示",
                            "请联系后台管理员添加车仓库","确定",null,null);
                }
                break;
        }
    }

    /**
     * 获取车仓库商品列表
     */
    private void dataGridStore(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ProductStoreBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ProductStoreBean.class, new IUpdateUI<ProductStoreBean>() {
            @Override
            public void updata(ProductStoreBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    if (jsonBean.getData().getRows() != null && jsonBean.getData().getRows().size() > 0) {
                        list.addAll(jsonBean.getData().getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView("仓库中没有商品");
                        } else {
                            page--;
                            showToast("数据全部加载完毕");
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
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
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
            }
        });
        httpUtil.post(M_Url.dataGridStore, L_RequestParams.dataGridStore(UserInfoUtils.getUser(getActivity()).getCarid(),
                page + "",mEtSearch.getText().toString(),"1"), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            dataGridStore(PublicFinal.FIRST, false);
        }
    }

    public void onEventMainThread(StockUpdateListEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        dataGridStore(PublicFinal.FIRST, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
