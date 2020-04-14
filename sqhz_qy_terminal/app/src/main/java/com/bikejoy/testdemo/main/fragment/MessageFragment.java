package com.bikejoy.testdemo.main.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.PublicFinal;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.base.Common;
import com.bikejoy.testdemo.base.MessageBean;
import com.bikejoy.testdemo.event.UpdateMessageNumEvent;
import com.bikejoy.testdemo.main.MainActivity;

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
 *     time   : 2019/1/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.lv_pull)
    ListView mLvPull;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.tv_sign_read)
    TextView mTvSignRead;

    private VaryViewHelper mVaryViewHelper;
    private MyErrorListener mErrorListener;

    private List<MessageBean> list = new ArrayList<>();
    private MessageAdapter adapter;

    private int page = 1;
    private int mFlag = 0;

    private String readFlag = "0";

    private UserBean userBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 2;

        userBean = UserInfoUtils.getUser(getActivity());

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
                queryMessageList(PublicFinal.TWO, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                queryMessageList(PublicFinal.TWO, true);
            }
        });

        adapter = new MessageAdapter();
        adapter.setData(list);
        adapter.setListener(new MessageAdapter.OnMessageListener() {
            @Override
            public void onItem(int i) {
                MessageBean bean = list.get(i);
                Intent intent;
                if ("1".equals(bean.getMp_jump_model())) {
                    //1 h5 2 活动 3 商品 4 消息详情 5订单6配送任务
                    switch (bean.getMp_jump_type()) {
                        case "5":
                            intent = new Intent(getActivity(), OrderDetailActivity.class);
                            intent.putExtra("orderId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;
                        case "6":
                            intent = new Intent(getActivity(), DeliveryOrderDetailActivity.class);
                            intent.putExtra("odmId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;
                        case "7":
                            intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        //8柜子警告  9 柜子故障
                        case "8":
                            intent = new Intent(getActivity(), StorehouseThreeDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("storeId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;
                        case "9":
                            intent = new Intent(getActivity(), StorehouseThreeDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("storeId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;
                        case "10"://电子券取水
                            intent = new Intent(getActivity(), GetWaterRecordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("recordId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;
                        case "11"://小程序送水申请
                            startActivity(TicketSendApplyListActivity.class);
                            break;
                        case "12"://套餐审核推送
                            startActivity(PackageApplyListActivity.class);
                            break;
                        // 13 主管开发领用   14开发员开发领用  15办公领用
                        case "13":
                            intent = new Intent(getActivity(), PackageRelationListActivity.class);
                            intent.putExtra("sprType", "2");//开发主管
                            startActivity(intent);
                            break;
                        case "14"://14开发员开发领用
                            intent = new Intent(getActivity(), PackageApplyListActivity.class);
                            startActivity(intent);
                            break;
                        case "15":
                            intent = new Intent(getActivity(), PackageApplyListActivity.class);
                            if (Common.ROLE3002.equals(userBean.getRoleCode())) { //开发主管时才传递参数
                                intent.putExtra("queryType", "2");
                            }
                            startActivity(intent);
                            break;
                        case "16"://提现
                            intent = new Intent(getActivity(), CashApplyDetailActivity.class);
                            intent.putExtra("gcaId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;
                        case "17"://发票
                            intent = new Intent(getActivity(), InvoiceDetailActivity.class);
                            intent.putExtra("oilId", bean.getMp_jump_value());
                            startActivity(intent);
                            break;

                    }
                }

                if ("0".equals(bean.getMp_read_flag())) {//未读消息
                    readMessage(bean.getMp_id());
                }
            }
        });
        mLvPull.setAdapter(adapter);

        queryMessageList(PublicFinal.FIRST, false);
    }

    private void queryMessageList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<MessageBean> httpUtil = new AsyncHttpUtil<>(getActivity(), MessageBean.class, new IUpdateUI<MessageBean>() {
            @Override
            public void updata(MessageBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    MessageBean bean = jsonBean.getData();
                    if (bean.getRows() != null && bean.getRows().size() > 0) {
                        list.addAll(bean.getRows());
                        adapter.setData(list);
                        mVaryViewHelper.showDataView();
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            UIUtils.Toast("暂无数据");
                            mVaryViewHelper.showEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    page = 1;
                                    mFlag = 1;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    queryMessageList(PublicFinal.FIRST, false);
                                }
                            });
                        } else {
                            page--;
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                    }
                    if ("0".equals(readFlag)) {
                        mTv1.setText("未读消息(" + bean.getTotal() + ")");
                        if ("0".equals(bean.getTotal())){
                            mTvSignRead.setVisibility(View.GONE);
                        }else{
                            mTvSignRead.setVisibility(View.VISIBLE);
                        }
                        EventBus.getDefault().post(new UpdateMessageNumEvent(bean.getTotal()));
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
        httpUtil.post(M_Url.queryMessageList, L_RequestParams.queryMessageList(readFlag, page + ""), isDialog);
    }

    @OnClick({R.id.tv1, R.id.tv2,R.id.tv_sign_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                readFlag = "0";
                mTvSignRead.setVisibility(View.VISIBLE);
                moveAnimation(0);
                break;
            case R.id.tv2:
                readFlag = "1";
                mTvSignRead.setVisibility(View.GONE);
                moveAnimation(1);
                break;
            case R.id.tv_sign_read://标记全部已读
                DialogUtil.showCustomDialog(getActivity(), "提示", "是否将全部标记为已读?",
                        "标记", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        readMessageAll();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 2
                                * index).setDuration(300).start();

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        queryMessageList(PublicFinal.FIRST, false);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryMessageList(PublicFinal.FIRST, false);
        }
    }


    private void readMessage(String messageids) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryMessageList(PublicFinal.FIRST, false);
                } else {
                    UIUtils.Toast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.readMessage, L_RequestParams.readMessage(messageids), true);
    }

    /**
     * 标记全部已读
     */
    private void readMessageAll() {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    showToast(jsonBean.getDesc());
                    page = 1;
                    mFlag = 1;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    queryMessageList(PublicFinal.FIRST, false);
                } else {
                    showToast(jsonBean.getDesc());
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
        httpUtil.post(M_Url.readMessageAll, L_RequestParams.readMessageAll(userBean.getWorkid()), true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
