package com.bikejoy.testdemo.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
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
import com.bikejoy.utils.ToastUtils;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.recycle.VaryViewHelper;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 活动自动开团界面
 *     version: 1.0
 * </pre>
 */

public class ActionAddGroupFragment extends BaseFragment {

    Unbinder unbinder;
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
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private ActionAddGroupAdapter adapter;
    private List<ActionBean> list = new ArrayList<>();

    private int page = 1;
    private int mFlag = 0;

    private VaryViewHelper mVaryViewHelper = null;
    private MyErrorListener mErrorListener;
    private String omState = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_add_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 3;

        mVaryViewHelper = new VaryViewHelper(mRefreshLayout);
        mErrorListener = new MyErrorListener();

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                productPromotionMainDataGrid(PublicFinal.FIRST, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                mFlag = 2;
                productPromotionMainDataGrid(PublicFinal.TWO, true);
            }
        });


        adapter = new ActionAddGroupAdapter();
        adapter.setData(list);
        adapter.setListener(new ActionAddGroupAdapter.OnActionAddGroupListListener() {
            @Override
            public void onAddActionGroup(final int i) {
                DialogUtil.showCustomDialog(getActivity(), "提示", "确认自动开团吗?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        insertGroupAuto(list.get(i).getPpm_id());
                    }

                    @Override
                    public void no() {
                    }
                });
            }
        });
        mLvPull.setAdapter(adapter);

        productPromotionMainDataGrid(PublicFinal.FIRST, false);
    }

    /**
     * 获取活动订单列表
     */
    private void productPromotionMainDataGrid(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<ActionBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ActionBean.class, new IUpdateUI<ActionBean>() {
            @Override
            public void updata(ActionBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    ActionBean bean = jsonBean.getData();
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
                            showToast("数据全部加载完毕");
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
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
            }
        });
        httpUtil.post(M_Url.productPromotionMainDataGrid, L_RequestParams.productPromotionMainDataGrid("3", omState,
                "Y", page + ""), isDialog);
    }

    public class MyErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            productPromotionMainDataGrid(PublicFinal.FIRST, false);
        }
    }

    private void insertGroupAuto(String sourceId) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("200".equals(jsonBean.getCode())) {
                    //                    showToast(jsonBean.getDesc());
                    ToastUtils.showToastInCenterSuccess(getActivity(), "已开团成功,可在小程序端查看");
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
        httpUtil.post(M_Url.insertGroupAuto, L_RequestParams.insertGroupAuto("2", sourceId), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                omState = "1";//商品促销
                moveAnimation(0);
                alterWidth(mTv1);
                break;
            case R.id.tv2://套餐促销
                omState = "4";
                moveAnimation(1);
                alterWidth(mTv2);
                break;
            case R.id.tv3://团购促销
                omState = "5";
                moveAnimation(2);
                alterWidth(mTv3);
                break;
        }
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 3
                                * index).setDuration(300).start();

        page = 1;
        mFlag = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        productPromotionMainDataGrid(PublicFinal.FIRST, false);
    }

}
