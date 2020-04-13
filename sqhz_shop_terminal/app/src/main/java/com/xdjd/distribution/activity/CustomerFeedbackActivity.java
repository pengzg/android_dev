package com.xdjd.distribution.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.message.PushAgent;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.CustomerFeedbackAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.FeedbackBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.ListenerInputView;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerFeedbackActivity extends BaseActivity implements CustomerFeedbackAdapter.OnCustomerFeedbackListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.listner_inputview)
    ListenerInputView mListnerInputview;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;


    private int page = 1;
    private int mFlag = 0;

    List<FeedbackBean> list = new ArrayList<>();

    /**
     * 客户反馈adapter
     */
    private CustomerFeedbackAdapter adapter;

    // 发布弹出视图控制
    private CommentPopupWindow commentWindow;
    private EditText comment_input;
    private TextView comment_send;
    private int commentIndex = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_feedback;
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
        mTitleBar.setTitle("客户意见反馈");

        // 实例化弹出框
        commentWindow = new CommentPopupWindow(this);

        adapter = new CustomerFeedbackAdapter(this);
        mLvNoScroll.setAdapter(adapter);
        adapter.setData(list);

        mListnerInputview
                .setOnKeyBoardStateChangeListener(new ListenerInputView.OnKeyBoardStateChangeListener() {

                    @Override
                    public void OnKeyBoardState(int state) {
                        switch (state) {
                            // 开启
                            case 1:
                                //                                showToast("输入法显示了");
                                break;
                            // 关闭
                            case 0:
                                //                                showToast("输入法关闭");
                                commentWindow.dismiss();
                                break;

                            default:
                                break;
                        }

                    }
                });

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page=1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetInvalidated();
                loadDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadDate();
            }
        });

    }

    private void loadDate() {
        AsyncHttpUtil<FeedbackBean> httpUtil = new AsyncHttpUtil<>(this, FeedbackBean.class,
                new IUpdateUI<FeedbackBean>() {
                    @Override
                    public void updata(FeedbackBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            if (jsonStr.getListData() != null && jsonStr.getListData().size() > 0) {
                                list.addAll(jsonStr.getListData());
                                adapter.notifyDataSetInvalidated();
                            } else {
                                if (mFlag == 2) {
                                    showToast(UIUtils.getString(R.string.on_pull_remind));
                                    if (page > 1)
                                        page--;
                                }
                            }
                        } else {
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
        httpUtil.post(M_Url.getFeedbackList, L_RequestParams.getFeedbackList(String.valueOf(page)), true);
    }

    private void reply(String cfId,String note) {
        AsyncHttpUtil<FeedbackBean> httpUtil = new AsyncHttpUtil<>(this, FeedbackBean.class,
                new IUpdateUI<FeedbackBean>() {
                    @Override
                    public void updata(FeedbackBean jsonStr) {
                        if ("00".equals(jsonStr.getRepCode())) {
                            showToast(jsonStr.getRepMsg());
                            list.clear();
                            adapter.notifyDataSetInvalidated();
                            page = 1;
                            mFlag = 1;
                            loadDate();
                        } else {
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
        httpUtil.post(M_Url.updateFeedbackNote, L_RequestParams.updateFeedbackNote(cfId,note), true);
    }

    @Override
    public void onReply(int i) {
        commentIndex = i;
        // 显示窗口
        commentWindow
                .showAtLocation(
                        mLlMain,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
        InputMethodManager imm = (InputMethodManager) CustomerFeedbackActivity.this.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 评论窗口
     */
    public class CommentPopupWindow extends PopupWindow {
        private View mMenuView;

        public CommentPopupWindow(Activity context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.popup_comment, null);
            // 实例化控件
            comment_input = (EditText) mMenuView
                    .findViewById(R.id.popup_comment_editText_comment);
            comment_send = (TextView) mMenuView
                    .findViewById(R.id.popup_comment_textView_send);
            comment_input.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    if ("".equals(comment_input.getText().toString())) {
                        comment_send.setTextColor(getResources().getColor(
                                R.color.text_gray));
                    } else {
                        comment_send.setTextColor(getResources().getColor(
                                R.color.text_black_212121));
                    }
                }
            });

            // 设置控件监听
            comment_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reply("",comment_input.getText().toString());
                }
            });
            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            mMenuView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = mMenuView.findViewById(
                            R.id.popup_comment_linearLayout_inputview).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });

        }
    }
}
