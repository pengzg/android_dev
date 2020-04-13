package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.MessageAdapter;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 消息界面
 * Created by lijipei on 2016/12/14.
 */

public class MessageActivity extends BaseActivity implements MessageAdapter.MessageListener{

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    PullToRefreshListView mPullList;

    private VaryViewHelper mViewHelper = null;

    private MessageAdapter adapter;
    private List<MessageBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
        initView();
        initData();
    }

    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("消息");
        mViewHelper = new VaryViewHelper(mPullList);
    }

    protected void initData(){
        mPullList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        initRefresh(mPullList);

        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(PublicFinal.FIRST);
            }
        });

        adapter = new MessageAdapter(this);
        mPullList.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    private void loadData(int isFirst){
        if (PublicFinal.FIRST == isFirst){
            mViewHelper.showLoadingView();
        }

        AsyncHttpUtil<MessageBean> httpUtil = new AsyncHttpUtil<>(this, MessageBean.class, new IUpdateUI<MessageBean>() {
            @Override
            public void updata(MessageBean beanMessage) {
                if ("00".equals(beanMessage.getRepCode())){
                    mViewHelper.showDataView();
                    list = beanMessage.getListData();
                    adapter.setData(list);
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
        httpUtil.post(M_Url.querySummeryMessage, L_RequestParams.
                querySummeryMessage(UserInfoUtils.getId(this),UserInfoUtils.getCenterShopId(this)),false);
    }

    /**
     * 错误回调接口
     */
    public class OnErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST);
        }
    }

    @Override
    public void itemListener(int position) {
        Intent intent;
        //1 系统消息 2 活动促销 3 物流消息
        if ("1".equals(list.get(position).getMessage_type())){
            //系统通知消息
            intent = new Intent(this,NotificationMessageActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }else if ("2".equals(list.get(position).getMessage_type())){
            //活动消息
            intent = new Intent(this,MessageActionActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }else if ("3".equals(list.get(position).getMessage_type())){
            //物流消息
            intent = new Intent(this,MessageLogisticsActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }

    //订阅事件FirstEvent
    public  void onEventMainThread(MessageEvent event){
        if (event.getPosition() == -1)
            return;

        if (!"".equals(list.get(event.getPosition()).getMessage_unread_num()) &&
                list.get(event.getPosition()).getMessage_unread_num()!=null){
            list.get(event.getPosition()).setMessage_unread_num("0");
//            adapter.notifyDataSetChanged();
            adapter.setData(list);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

}
