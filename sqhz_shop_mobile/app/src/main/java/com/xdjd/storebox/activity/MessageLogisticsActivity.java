package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.LogisticsMessageAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.MessageBean;
import com.xdjd.storebox.event.MessageEvent;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 物流信息消息界面
 */
public class MessageLogisticsActivity extends BaseActivity implements LogisticsMessageAdapter.LogisticsMessageListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    PullToRefreshListView mPullList;
    @BindView(R.id.activity_action_message)
    LinearLayout mActivityActionMessage;

    private LogisticsMessageAdapter adapter;
    private List<MessageBean> list;

    private VaryViewHelper mViewHelper = null;

    private int mFlag = 0;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_message);
        ButterKnife.bind(this);

        initData();
        loadDate(PublicFinal.FIRST,false);
    }

    protected void initData() {
        mTitleBar.setTitle("物流消息");
        mTitleBar.leftBack(this);

        list = new ArrayList<>();
        mViewHelper = new VaryViewHelper(mPullList);

        mPullList.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullList);

        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                loadDate(PublicFinal.TWO,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                page++;
                loadDate(PublicFinal.TWO,false);
            }
        });

        adapter = new LogisticsMessageAdapter(this);
        mPullList.setAdapter(adapter);
    }

    @Override
    public void itemListener(int position) {
        Intent intent = new Intent(this,OrderDetailActivity.class);
        intent.putExtra("orderId",list.get(position).getMessage_jump_value());
        startActivity(intent);
    }

    private void loadDate(int isFirst,boolean isBoolean){
        if (PublicFinal.FIRST == isFirst){
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<MessageBean> httpUtil = new AsyncHttpUtil<>(this, MessageBean.class, new IUpdateUI<MessageBean>() {
            @Override
            public void updata(MessageBean beanMessage) {
                if ("00".equals(beanMessage.getRepCode())){
                    mViewHelper.showDataView();
                    if (mFlag == 0){
                        EventBus.getDefault().post(new MessageEvent(getIntent().getIntExtra("position",-1)));
                    }
                    if (beanMessage.getListData() == null || beanMessage.getListData().size() == 0){
                        if (mFlag == 0 || mFlag == 1){
                            mViewHelper.showEmptyView("暂时没有活动消息~");
                        }else if (mFlag == 2){
                            //showToast(beanMessage.getRepMsg());
                            page--;
                        }
                    }else{
                        list.addAll(beanMessage.getListData());
                        adapter.setData(list);
                    }
                }else{
                    showToast(beanMessage.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mViewHelper.showErrorView(new OnErrorListener());
            }

            @Override
            public void finish() {
                mPullList.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryMessageList,
                L_RequestParams.queryMessageList(UserInfoUtils.getId(this),String.valueOf(page),
                        "3",UserInfoUtils.getCenterShopId(this)),isBoolean);
    }

    /**
     * 错误回调接口
     */
    public class OnErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            loadDate(PublicFinal.FIRST,false);
        }
    }
}
