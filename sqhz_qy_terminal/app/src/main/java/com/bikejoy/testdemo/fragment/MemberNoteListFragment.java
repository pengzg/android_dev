package com.bikejoy.testdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.event.UpdateNoteEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/1/25
 *     desc   : 会员记录列表
 *     version: 1.0
 * </pre>
 */

@SuppressLint("ValidFragment")
public class MemberNoteListFragment extends BaseFragment {

    @BindView(R.id.lv_list)
    ListView mLvList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.tv_add_note)
    TextView mTvAddNote;

    private int page = 1;
    private int mFlag = 0;
    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    private NoteListAdapter adapter;
    private List<NoteBean> list = new ArrayList<>();

    private MemberBean member;//传递过来的客户信息

    public MemberNoteListFragment(MemberBean member) {
        this.member = member;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_note, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mVaryViewHelper = new VaryViewHelper(mRefreshLayout);
        mErrorListener = new MyErrorListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                queryNoteList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                queryNoteList(PublicFinal.TWO, true);
            }
        });

        adapter = new NoteListAdapter(getActivity());
        adapter.setData(list);
        mLvList.setAdapter(adapter);

        queryNoteList(PublicFinal.FIRST, false);
    }

    /**
     * 获取客户列表
     */
    private void queryNoteList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<NoteBean> httpUtil = new AsyncHttpUtil<>(getActivity(), NoteBean.class, new IUpdateUI<NoteBean>() {
            @Override
            public void updata(NoteBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    NoteBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                            mVaryViewHelper.showEmptyView();
                        } else {
                            page--;
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getDesc(), mErrorListener);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(), mErrorListener);
            }

            @Override
            public void finish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.setNoMoreData(false);
                }
            }
        });
        httpUtil.post(M_Url.queryNoteList, L_RequestParams.queryNoteList(member.getMb_id(), page + "","",""), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryNoteList(PublicFinal.FIRST, false);
        }
    }

    @OnClick({R.id.tv_add_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_note:
                Intent intent = new Intent(getActivity(),AddNoteActivity.class);
                intent.putExtra("mbId",member.getMb_id());
                startActivity(intent);
//                DialogUtil.showAddNoteDialog(getActivity(), "添加拜访信息", "请输入拜访消息", "添加", "取消", new DialogUtil.MyCustomDialogListener() {
//                    @Override
//                    public void ok(Dialog dialog, String str) {
//                        if (str==null || str.length()==0){
//                            showToast("请输入拜访信息");
//                        }else{
//                            dialog.dismiss();
//                            addNote(str);
//                        }
//                    }
//                    @Override
//                    public void no() {
//                    }
//                });
                break;
        }
    }

    /**
     * 添加拜访信息
     * @param msg
     */
    private void addNote(String msg) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("200".equals(jsonBean.getCode())){
                    showToast(jsonBean.getData().toString());
                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryNoteList(PublicFinal.TWO, false);
                }else{
                    showToast(jsonBean.getData().toString());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.addNote, L_RequestParams.addNote(member.getMb_id(), msg,"",""), true);
    }

    public void onEventMainThread(UpdateNoteEvent event) {
        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryNoteList(PublicFinal.TWO, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
