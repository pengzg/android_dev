package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.MemberListingAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.MemberBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.MemberEvent;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MemberListingActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;

    private int page = 1;
    private int mFlag = 0;

    private List<MemberBean> list;
    private MemberListingAdapter adapter;

    private MemberBean beanMember;

    @Override
    protected int getContentView() {
        return R.layout.activity_member_listing;
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
        mTitleBar.setTitle("选择核销员");

        mEtSearch.setHint("按昵称查询");

        beanMember = (MemberBean) getIntent().getSerializableExtra("beanMember");

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetInvalidated();
                getMemberList(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getMemberList(false);
            }
        });

        list = new ArrayList<>();

        adapter = new MemberListingAdapter(new ItemOnListener() {
            @Override
            public void onItem(int position) {
                EventBus.getDefault().post(new MemberEvent(list.get(position)));
                finishActivity();
            }
        });
        mLvNoScroll.setAdapter(adapter);
        if (beanMember != null) {
            adapter.setId(beanMember.getMb_id());
        }
        adapter.setData(list);

        getMemberList(true);
    }

    /**
     * 获取核销员列表接口
     */
    private void getMemberList(boolean isDialog) {
        AsyncHttpUtil<MemberBean> httpUtil = new AsyncHttpUtil<>(this, MemberBean.class, new IUpdateUI<MemberBean>() {
            @Override
            public void updata(MemberBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                        adapter.notifyDataSetInvalidated();
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            page--;
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
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getMemberList, L_RequestParams.getMemberList(page + "", mEtSearch.getText().toString(),"2"), isDialog);
    }

    @OnClick({R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetInvalidated();
                getMemberList(true);
                break;
        }
    }
}
