package com.xdjd.distribution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.VisitingAlarmActivity;
import com.xdjd.distribution.adapter.VisitingAlarmAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.bean.VisitingAlarmBean;
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
 *     time   : 2017/6/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VisitingAlarmFragment extends BaseFragment {

    @BindView(R.id.lv_visiting)
    NoScrollListView mLvVisiting;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;

    private int mFlag = 0;
    private int page = 1;

    private UserBean userBean;

    private List<VisitingAlarmBean> list = new ArrayList<>();
    private VisitingAlarmAdapter adapter;

    /**
     * 业务员
     */
    private String salesid = "";

    private VisitingAlarmActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visiting_alarm, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        activity = (VisitingAlarmActivity) getActivity();
        userBean = UserInfoUtils.getUser(getActivity());

        adapter = new VisitingAlarmAdapter(true);
        mLvVisiting.setAdapter(adapter);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);

        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetInvalidated();

                getCsAlarmList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;

                getCsAlarmList();
            }

        });

        getCsAlarmList();
    }

    private void getCsAlarmList() {
        AsyncHttpUtil<VisitingAlarmBean> httpUtil = new AsyncHttpUtil<>(getActivity(), VisitingAlarmBean.class, new IUpdateUI<VisitingAlarmBean>() {
            @Override
            public void updata(VisitingAlarmBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        list.addAll(jsonBean.getDataList());
                        adapter.setData(list);
                    } else {
                        if (mFlag == 2) {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                    mTvTotalNum.setText("拜访提醒:"+jsonBean.getTotal());
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCsAlarmList, L_RequestParams.getCsAlarmList(userBean.getUserId(), String.valueOf(page), "",
                "2", activity.salesid, String.valueOf(activity.phType)), true);
    }


    public void updateDate(String salesid) {
        this.salesid = salesid;
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetInvalidated();

        getCsAlarmList();
    }

    public void updatePhType(int phType) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetInvalidated();

        getCsAlarmList();
    }

}
