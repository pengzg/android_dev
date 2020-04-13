package com.xdjd.storebox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.IntegralDetailAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.IntegralDetailBean;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class HaveUsedIntegralFragment extends BaseFragment {
    @BindView(R.id.lv_integral)
    PullToRefreshListView lvIntegral;
    private IntegralDetailAdapter adapter;
    private VaryViewHelper mVaryViewHelper = null;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<IntegralDetailBean> list = new ArrayList<>();

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mVaryViewHelper = new VaryViewHelper(lvIntegral);
        initView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_integral, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /*上下拉刷新数据*/
    private void initView(){
        lvIntegral.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(lvIntegral);
        lvIntegral.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO);
            }
        });
        adapter = new IntegralDetailAdapter() ;
        lvIntegral.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    /*获取积分明细*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<IntegralDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(), IntegralDetailBean.class, new IUpdateUI<IntegralDetailBean>() {
            @Override
            public void updata(IntegralDetailBean bean) {
                if (bean.getRepCode().equals("00")){
                    if (null!= bean.getListData() && bean.getListData().size() > 0){
                        if (mFlag == 0 || mFlag == 1){
                        }
                        list.addAll(bean.getListData());
                        adapter.setData(list) ;
                        adapter.notifyDataSetChanged();
                        mVaryViewHelper.showDataView();
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了！");
                            pageNo--;
                        }else{
                            mVaryViewHelper.showEmptyView("还没有已使用积分");
                        }
                    }

                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }
            @Override
            public void finish() {
                lvIntegral.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getIntegralDetail, L_RequestParams.GetIntegralDetailList(
                UserInfoUtils.getId(getActivity()),String.valueOf(pageNo),"10","2") ,false);
    }
}
