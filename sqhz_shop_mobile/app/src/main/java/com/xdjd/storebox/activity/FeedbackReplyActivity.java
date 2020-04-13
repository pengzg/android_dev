package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.FeedbackReplayAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.FeedbackBean;
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

/**
 * Created by freestyle_hong on 2017/1/16.
 */

public class FeedbackReplyActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.list_view)
    ListView listView;
    private FeedbackReplayAdapter adapter;
    private List<FeedbackBean> mList = new ArrayList<>();
    private VaryViewHelper mVaryViewHelper = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_reply);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("我的反馈");
        mVaryViewHelper = new VaryViewHelper(listView);
        adapter = new FeedbackReplayAdapter();
        listView.setAdapter(adapter);
        GetFeedbackList(UserInfoUtils.getId(this),"1");
    }

    /*反馈回复*/
    private void GetFeedbackList(String uid,String pageNo){
        AsyncHttpUtil<FeedbackBean> httpUtil = new AsyncHttpUtil<>(this, FeedbackBean.class, new IUpdateUI<FeedbackBean>() {
            @Override
            public void updata(FeedbackBean bean) {
                if(bean.getRepCode().equals("00")){
                    if(null != bean.getListData() && bean.getListData().size() > 0){
                        mList.addAll(bean.getListData());
                        adapter.setData(mList);
                    }else{
                        mVaryViewHelper.showEmptyView();
                    }
                    adapter.notifyDataSetChanged();
                    mVaryViewHelper.showDataView();
                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());}

            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.MeFeedbackList,L_RequestParams.MeFeedbackList(uid,pageNo,"500"),false);
    }
}
