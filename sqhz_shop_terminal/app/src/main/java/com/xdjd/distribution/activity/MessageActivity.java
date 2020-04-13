package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.MessageAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.MessageBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.UserInfoUtils;
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

/**
 * Created by lijipei on 2017/7/19.
 */

public class MessageActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_list)
    NoScrollListView mLvList;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;

    private MessageAdapter adapter;

    private boolean isMessage;//true:是消息列表

    private int page = 1;
    private int mFlag = 0;

    private List<MessageBean> list = new ArrayList<>();

    UserBean userBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMessage) {
                    Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    finishActivity();
                }
            }
        });
        mTitleBar.setTitle("消息");

        isMessage = getIntent().getBooleanExtra("isMessage", false);

        userBean = UserInfoUtils.getUser(this);

        adapter = new MessageAdapter();
        mLvList.setAdapter(adapter);

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                page++;
                loadDate();
            }
        });

        loadDate();

        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                readMessage(list.get(i));
            }
        });
    }

    private void loadDate() {
        AsyncHttpUtil<MessageBean> httpUtil = new AsyncHttpUtil<>(this, MessageBean.class, new IUpdateUI<MessageBean>() {
            @Override
            public void updata(MessageBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        list.addAll(jsonBean.getListData());
                        adapter.setData(list);
                    } else {
                        if (mFlag == 2) {
                            showToast("没有更多消息了!");
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
        httpUtil.post(M_Url.queryUserMessage, L_RequestParams.queryUserMessage(userBean.getBud_id(), String.valueOf(page)), true);
    }

    private void readMessage(final MessageBean bean) {
        final AsyncHttpUtil<MessageBean> httpUtil = new AsyncHttpUtil<>(this, MessageBean.class, new IUpdateUI<MessageBean>() {
            @Override
            public void updata(MessageBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                } else {
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                if ("5".equals(bean.getMp_jump_type())) {//跳转订单详情
                    Intent intent = new Intent(MessageActivity.this,OrderDetailActivity.class);
                    intent.putExtra("om_id",bean.getMp_jump_value());
                    startActivity(intent);
                } else {
                    startActivity(DistributionTaskActivity.class);
                }
            }
        });
        httpUtil.post(M_Url.readMessage, L_RequestParams.readMessage("1", userBean.getBud_id(), bean.getMp_id()), true);
    }

    @Override
    public void onBackPressed() {
        if (isMessage) {
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            finishActivity();
        }
        //        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFlag = 1;
        page = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        loadDate();
    }
}
