package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ActionMessageAdapter;
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
 * 活动消息界面
 */
public class MessageActionActivity extends BaseActivity implements ActionMessageAdapter.ActionMessageListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.pull_list)
    PullToRefreshListView mPullList;
    @BindView(R.id.activity_action_message)
    LinearLayout mActivityActionMessage;

    private ActionMessageAdapter adapter;
    private List<MessageBean> list;

    private VaryViewHelper mViewHelper = null;

    private int mFlag = 0;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_message);
        ButterKnife.bind(this);

        initData();
        loadDate(PublicFinal.FIRST,false);
    }

    protected void initData() {
        mTitleBar.setTitle("活动消息");
        mTitleBar.leftBack(this);

        mViewHelper = new VaryViewHelper(mPullList);
        list = new ArrayList<>();

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

        adapter = new ActionMessageAdapter(this);
        mPullList.setAdapter(adapter);
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
                        "2",UserInfoUtils.getCenterShopId(this)),isBoolean);
    }

    @Override
    public void itemListener(int position) {
        Intent intent;
//        intent.putExtra("activityId", list.get(position).getMessage_jump_value());
//        startActivity(intent);

        if (list.get(position).getMessage_jump_type().equals("1")){
            intent = new Intent(this,CommonWebActivity.class);
            intent.putExtra("title",list.get(position).getMessage_title());
            intent.putExtra("url",list.get(position).getMessage_jump_value());
            startActivity(intent);
        }else if (list.get(position).getMessage_jump_type().equals("2")){
            if ("1".equals(list.get(position).getMessage_jump_model())){//商品详情
                intent = new Intent(this,GoodsDetailActivity.class);
                intent.putExtra("gpId",list.get(position).getMessage_jump_value());
                startActivity(intent);
            }else if ("2".equals(list.get(position).getMessage_jump_model())){//活动商品界面
                intent = new Intent(this,ActionActivity.class);
                intent.putExtra("activityId",list.get(position).getMessage_jump_value());
                startActivity(intent);
            }else if ("3".equals(list.get(position).getMessage_jump_model())){//分类界面
                intent = new Intent(this,GoodsCategoryActivity.class);
                intent.putExtra("whp_id",list.get(position).getMessage_jump_value());
                startActivity(intent);
            }else if ("4".equals(list.get(position).getMessage_jump_model())){//订单详情
                intent = new Intent(this,OrderDetailActivity.class);
                intent.putExtra("orderId",list.get(position).getMessage_jump_value());
                startActivity(intent);
            }
        }
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
