package com.xdjd.winningrecord.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.MemberBean;
import com.xdjd.steward.listener.AnimListenerBuilder;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.winningrecord.adapter.UserListingAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/6
 *     desc   : 用户列表
 *     version: 1.0
 * </pre>
 */

public class UserListingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.rv_user)
    RecyclerView mRvUser;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.tv_page_count)
    TextView mTvPageCount;
    @BindView(R.id.tv_user_total_num)
    TextView mTvUserTotalNum;

    private UserListingAdapter adapterUser;

    private int page = 1;

    private int PAGE_PER = 1;//总共多少页
    private int PAGE_SIZE = 20;//每页的数量
    private int COUNT = 0;//总的条数,默认是0

    private List<MemberBean> list;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_listing;
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
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("用户列表");

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(UIUtils.getColor(R.color.color_blue));

        mRvUser.setLayoutManager(new LinearLayoutManager(this));
        adapterUser = new UserListingAdapter(list);
        adapterUser.setOnLoadMoreListener(this, mRvUser);
        adapterUser.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapterUser.setContext(this);
        mRvUser.setAdapter(adapterUser);

        final AnimListenerBuilder builder = new AnimListenerBuilder();
        mRvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int page;

                    if (((LinearLayoutManager) layoutManager).getChildCount() == 0) {
                        page = 0;
                        PAGE_PER = 0;
                    } else {
                        if (lastVisiblePosition % PAGE_SIZE == 0) {
                            page = lastVisiblePosition / PAGE_SIZE;
                        } else {
                            page = lastVisiblePosition / PAGE_SIZE + 1;
                        }
                    }

                    mTvPageCount.setText(page + "/" + PAGE_PER);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                builder.setNewState(newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && builder.isAnimFinish()) {
                    //如果是IDLE状态，并且显示动画执行完毕，再执行隐藏动画，避免出现动画闪烁
                    //如果快速简短滑动，可能导致出现IDLE状态，但是动画未执行完成。因此无法执行隐藏动画。所以保存当前newState，在onAnimationEnd中增加判断。
                    AnimUtils.hide(mTvPageCount);
                } else if (mTvPageCount.getVisibility() != View.VISIBLE) {
                    AnimUtils.show(mTvPageCount, builder.build());
                }
            }
        });

        list = new ArrayList<>();

        getMemberList();
    }

    @OnClick({R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mSwipeLayout.setEnabled(false);
                adapterUser.setEnableLoadMore(false);
                list.clear();
                adapterUser.getData().clear();
                adapterUser.notifyDataSetChanged();
                getMemberList();
                break;
        }
    }

    /**
     * 获取核销员列表接口
     */
    private void getMemberList() {
        AsyncHttpUtil<MemberBean> httpUtil = new AsyncHttpUtil<>(this, MemberBean.class, new IUpdateUI<MemberBean>() {
            @Override
            public void updata(MemberBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    COUNT = jsonBean.getNum();
                    if (COUNT % PAGE_SIZE == 0) {
                        PAGE_PER = COUNT / PAGE_SIZE;
                    } else {
                        PAGE_PER = COUNT / PAGE_SIZE + 1;
                    }

                    mTvUserTotalNum.setText("用户总数量:" + jsonBean.getNum());
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        adapterUser.addData(jsonBean.getListData());
                        adapterUser.loadMoreComplete();
                    } else {
                        page--;
                        if (page == 0) {
                            page = 1;
                            adapterUser.getData().clear();
                            adapterUser.notifyDataSetChanged();
                        } else {
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            adapterUser.loadMoreEnd(false);
                        }
                    }
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
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(true);
                adapterUser.setEnableLoadMore(true);
            }
        });
        httpUtil.post(M_Url.getMemberList, L_RequestParams.getMemberList(page + "", mEtSearch.getText().toString(), "1"), true);
    }

    @Override
    public void onRefresh() {
        // 下拉刷新
        mSwipeLayout.setEnabled(false);
        adapterUser.setEnableLoadMore(false);
        adapterUser.getData().clear();
        list.clear();
        page = 1;
        adapterUser.notifyDataSetChanged();
        getMemberList();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeLayout.setEnabled(false);
        if (adapterUser.getData().size() < PAGE_SIZE) {
            //第一页数据就小于pageSize的时候
            adapterUser.loadMoreEnd(false);
            mSwipeLayout.setEnabled(true);
        } else {
            page++;
            getMemberList();
        }
    }
}
