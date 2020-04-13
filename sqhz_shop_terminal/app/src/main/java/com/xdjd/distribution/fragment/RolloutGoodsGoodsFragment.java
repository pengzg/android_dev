package com.xdjd.distribution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.RolloutGoodsDetailActivity;
import com.xdjd.distribution.adapter.RolloutGoodsGoodsAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.PHGoodsDetailListBean;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsGoodsFragment extends BaseFragment {


    @BindView(R.id.lv_noscroll)
    NoScrollListView mLvNoscroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    Unbinder unbinder;
    private RolloutGoodsGoodsAdapter adapterGoods;
    private List<PHGoodsDetailListBean> list = new ArrayList<>();
    private int page = 1;
    private int mFlag = 0;
    private String customerId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rollout_goods_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);
        customerId = getActivity().getIntent().getStringExtra("customerId");
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullScroll.onRefreshComplete();
                mFlag = 1;
                page = 1;

                list.clear();
                adapterGoods.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullScroll.onRefreshComplete();
                page++;
                mFlag = 2;
                loadData();
            }
        });

        adapterGoods = new RolloutGoodsGoodsAdapter();
        mLvNoscroll.setAdapter(adapterGoods);
        loadData();
    }

    private void loadData(){
        AsyncHttpUtil httpUtil = new AsyncHttpUtil<>(getActivity(), PHGoodsDetailListBean.class, new IUpdateUI<PHGoodsDetailListBean>() {
            @Override
            public void updata(PHGoodsDetailListBean bean) {
              if("00".equals(bean.getRepCode())){
                  if(bean.getListData() != null && bean.getListData().size() > 0){
                      list.addAll(bean.getListData());
                      adapterGoods.setData(list);
                  }else{
                      if(mFlag == 2){
                          page--;
                          showToast("没有更多数据了");
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

            }
        });
        httpUtil.post(M_Url.phGoodsDetailList, L_RequestParams.phGoodsDetailList(String.valueOf(page),customerId,"0","","",""),true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
