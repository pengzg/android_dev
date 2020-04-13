package com.xdjd.storebox.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.PromoteUsersAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.PromoteUserBean;
import com.xdjd.utils.DialogUtil;
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
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/1/3.
 */

public class PromoteUsersActivity extends BaseActivity implements PromoteUsersAdapter.userListener {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.user_list)
    PullToRefreshListView userList;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_del)
    LinearLayout mSearchDel;
    @BindView(R.id.search_ll)
    LinearLayout mSearchLl;
    @BindView(R.id.search_btn)
    Button mSearchBtn;
    @BindView(R.id.search_main_ll)
    LinearLayout mSearchMainLl;
    private PromoteUsersAdapter adapter;
    private VaryViewHelper mVaryViewHelper = null;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<PromoteUserBean> list = new ArrayList<PromoteUserBean>();

    private String searchKey = "";//	搜索关键词

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_users);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("推广用户");
        titleBar.setRightText("搜索");
        titleBar.setRightTextColor(R.color.text_212121);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchMainLl.getVisibility() == View.GONE) {
                    mSearchMainLl.setVisibility(View.VISIBLE);
                    titleBar.setRightText("取消");
                } else {
                    mSearchMainLl.setVisibility(View.GONE);
                    titleBar.setRightText("搜索");
                    searchKey = "";

                    mFlag = 1;
                    pageNo = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    loadData(PublicFinal.FIRST,false);
                }
            }
        });

        mVaryViewHelper = new VaryViewHelper(userList);
        adapter = new PromoteUsersAdapter(this, pageNo);
        userList.setAdapter(adapter);
        initView();
    }

    /*上下拉刷新数据*/
    private void initView() {
        userList.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(userList);
        userList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO,false);
            }
        });
        adapter = new PromoteUsersAdapter(this, pageNo);
        userList.setAdapter(adapter);
        loadData(PublicFinal.FIRST,false);

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mSearchDel.setVisibility(View.VISIBLE);
                } else {
                    mSearchDel.setVisibility(View.GONE);
                }
            }
        });
    }


    /*全部订单请求接口*/
    private void loadData(int isFirst,boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<PromoteUserBean> httpUtil = new AsyncHttpUtil<>(this, PromoteUserBean.class, new IUpdateUI<PromoteUserBean>() {
            @Override
            public void updata(PromoteUserBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getDataList() && bean.getDataList().size() > 0) {
                        list.addAll(bean.getDataList());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 2) {
                            showToast("没有更多数据了！");
                            pageNo--;
                            //mVaryViewHelper.showDataView();
                        } else if (mFlag == 0 || mFlag == 1) {
                            if ("".equals(searchKey) || searchKey == null){
                                mVaryViewHelper.showEmptyView("还没有推广用户,再接再厉吧!");
                            }else if (titleBar.getRightText().equals("取消")){
                                mVaryViewHelper.showEmptyView("没有搜索结果!");
                                showToast("没有搜索结果!");
                            }

                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(new onErrorListener());
            }

            @Override
            public void finish() {
                userList.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.PromoteUserList, L_RequestParams.PromoteUser(
                UserInfoUtils.getId(PromoteUsersActivity.this), searchKey, pageNo), isDialog);
    }

    @OnClick({R.id.search_del, R.id.search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_del:
                mSearchEdit.setText("");
                break;
            case R.id.search_btn:
                if (mSearchEdit.getText().length() == 0 || "".equals(mSearchEdit.getText())) {
                    showToast("请输入搜索条件");
                } else {
                    searchKey = mSearchEdit.getText().toString();

                    mFlag = 1;
                    pageNo = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    loadData(PublicFinal.FIRST,false);
                }
                break;
        }
    }

    /**
     * 加载失败点击事件
     */
    class onErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadData(PublicFinal.FIRST,false);
        }
    }

    @Override
    public void dialPhone(final String mobile) {
        DialogUtil.showCustomDialog(this, "拨打电话?", mobile, "拨打", "取消", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void no() {

            }
        });
    }

}
