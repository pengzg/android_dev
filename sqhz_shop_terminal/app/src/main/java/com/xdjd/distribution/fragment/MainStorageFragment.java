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
import com.xdjd.distribution.adapter.MainStorageAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.MainStockBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.UserInfoUtils;
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

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MainStorageFragment extends BaseFragment {

    @BindView(R.id.lv_main_storage)
    NoScrollListView mLvMainStorage;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private View view;

    private MainStorageAdapter adapter;

    private int mFlag = 0;
    private int page = 1;

    public List<MainStockBean> list = new ArrayList<>();

    private UserBean userBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_storage, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getMainStockGoods("",false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                page++;
                getMainStockGoods("",false);
            }
        });

        adapter = new MainStorageAdapter();
        mLvMainStorage.setAdapter(adapter);
        adapter.setData(list);

        getMainStockGoods("",true);
    }

    private void getMainStockGoods(String searchKey,boolean isDialog) {
        AsyncHttpUtil<MainStockBean> httpUtil = new AsyncHttpUtil<>(getActivity(), MainStockBean.class, new IUpdateUI<MainStockBean>() {
            @Override
            public void updata(MainStockBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        adapter.setData(list);
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                        } else {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getMainStockGoods, L_RequestParams.
                getMainStockGoods(userBean.getUserId(), String.valueOf(page), searchKey), isDialog);
    }

    public void searchGoods(String s) {
        page = 1;
        list.clear();
        mFlag = 1;
        adapter.notifyDataSetInvalidated();
        getMainStockGoods(s,true);
    }
}
