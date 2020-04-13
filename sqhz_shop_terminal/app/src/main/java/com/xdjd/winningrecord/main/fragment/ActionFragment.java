package com.xdjd.winningrecord.main.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.calendarview.StringUtil;
import com.xdjd.winningrecord.activity.ActionDetailActivity;
import com.xdjd.winningrecord.adapter.PrizeActionAdapter;
import com.xdjd.winningrecord.bean.ActivityListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lijipei on 2017/11/2.
 * 活动界面
 */

public class ActionFragment extends BaseFragment {

    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_no_start)
    TextView mTvNoStart;
    @BindView(R.id.tv_underway)
    TextView mTvUnderway;
    @BindView(R.id.tv_end)
    TextView mTvEnd;
    Unbinder unbinder;

    private View view;

    private int currentTab = 0; // 当前Tab页面索引
    private String mActionType = "3";//2 暂存状态 3 进行中 4 已结束

    private PrizeActionAdapter adapter;
    List<ActivityListBean> list = new ArrayList<>();

    private int page = 1;
    private int flag = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        adapter = new PrizeActionAdapter();
        mLvNoScroll.setAdapter(adapter);
        adapter.setData(list);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                flag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadData();
            }
        });

        mLvNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if("2".equals(mActionType)){
                    showToast("活动尚未开始!");
                    return;
                }
                Intent intent = new Intent(getActivity(),ActionDetailActivity.class);
                intent.putExtra("activityId",list.get(i).getMm_id());
                intent.putExtra("startDate",DateUtils.getStringFormat(list.get(i).getMm_startime()));
                intent.putExtra("endDate",DateUtils.getStringFormat(list.get(i).getMm_endtime()));
                startActivity(intent);
            }
        });

        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_no_start, R.id.tv_underway, R.id.tv_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_underway://进行中
                currentTab = 0;
                mActionType = "3";
                moveAnimation(mTvUnderway);
                selectTvAction();
                break;
            case R.id.tv_no_start://未发布
                currentTab = 1;
                mActionType = "2";
                moveAnimation(mTvNoStart);
                selectTvAction();
                break;
            case R.id.tv_end://已结束
                currentTab = 2;
                mActionType = "4";
                moveAnimation(mTvEnd);
                selectTvAction();
                break;
        }
    }

    private void loadData(){
        AsyncHttpUtil<ActivityListBean> httpUtil = new AsyncHttpUtil<>(getActivity(),ActivityListBean.class , new IUpdateUI<ActivityListBean>() {
            @Override
            public void updata(ActivityListBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())){
                    if (jsonStr.getListData()!=null && jsonStr.getListData().size()>0){
                        list.addAll(jsonStr.getListData());
                        adapter.notifyDataSetChanged();
                    }else{
                        if (flag == 2){
                            page--;
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                }else{
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.getActivityList, G_RequestParams.getActivityList(String.valueOf(page),"",
                mActionType),true);
    }

    private void selectTvAction(){
        mTvEnd.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvNoStart.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvUnderway.setTextColor(UIUtils.getColor(R.color.text_gray));
        switch(currentTab){
            case 0://进行中
                mTvUnderway.setTextColor(UIUtils.getColor(R.color.white));
                break;
            case 1://未发布
                mTvNoStart.setTextColor(UIUtils.getColor(R.color.white));
                break;
            case 2://已结束
                mTvEnd.setTextColor(UIUtils.getColor(R.color.white));
                break;
        }

        page = 1;
        flag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        loadData();
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.setDuration(400).start();
    }

}
