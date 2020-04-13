package com.xdjd.storebox.mainfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.NetSingleAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 网单fragment
 * Created by lijipei on 2016/11/16.
 */

public class NetSingleFragment extends BaseFragment {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
//    @BindView(R.id.order_rl)
//    RelativeLayout mOrderRl;
//    @BindView(R.id.tv_0)
//    TextView mTv0;
//    @BindView(R.id.tv_1)
//    TextView mTv1;
//    @BindView(R.id.tv_2)
//    TextView mTv2;
//    @BindView(R.id.tv_3)
//    TextView mTv3;
//    @BindView(R.id.line)
//    RelativeLayout mLine;
//    @BindView(R.id.viewPager)
//    ViewPager mViewPager;
//    @BindView(R.id.pull_net_single_scroll)
//    PullToRefreshScrollView mPullNetSingleScroll;
//    @BindView(R.id.net_single_listview)
//    NoScrollListView mNetSingleListview;
    @BindView(R.id.net_single_ll)
    LinearLayout mNetSingleLl;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_ll)
    LinearLayout mSearchLl;
    @BindView(R.id.search_btn)
    Button mSearchBtn;

    private TextView[] tvs;
    private int mIndex;

    private int index = 0;

    private NetSingleAdapter adapter;
    private int width;

    private VaryViewHelper mViewHelper = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //width = getResources().getDisplayMetrics().widthPixels / 4;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_single, container, false);
        ButterKnife.bind(this, view);
        //mLine.getLayoutParams().width = width;
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mTitleBar.setTitle("网单");
        initView();
    }

    private void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mOrderRl
//                    .getLayoutParams();
//            params.height = (int) (UIUtils.getStatusBarHeight());
//            mOrderRl.setLayoutParams(params);
//            //mOrderRl.setPadding(0, UIUtils.getStatusBarHeight(), 0, 0);
//        }

                mViewHelper = new VaryViewHelper(mNetSingleLl);
        //        mViewHelper.showEmptyView("暂无数据");

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()>0){

                }else{
                    mViewHelper.showDataView();
                }
            }
        });

        /*tvs = new TextView[]{mTv0, mTv1, mTv2, mTv3};
        tvs[mIndex].setSelected(true);

        mPullNetSingleScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullNetSingleScroll);
        mPullNetSingleScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullNetSingleScroll.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullNetSingleScroll.onRefreshComplete();
            }
        });

        adapter = new NetSingleAdapter();
        mNetSingleListview.setAdapter(adapter);

        mNetSingleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(NetSingleDetailActivity.class);
            }
        });*/
    }

//    @OnClick({R.id.tv_0, R.id.tv_1, R.id.tv_2, R.id.tv_3})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_0:
//                index = 0;
//                break;
//            case R.id.tv_1:
//                index = 1;
//                break;
//            case R.id.tv_2:
//                index = 2;
//                break;
//            case R.id.tv_3:
//                index = 3;
//                break;
//        }
//        moveAnimation(index);
//        for (int i = 0; i < tvs.length; i++) {
//            if (i == index) {
//                tvs[i].setSelected(true);
//            } else {
//                tvs[i].setSelected(false);
//            }
//        }
//    }

//    private void moveAnimation(int index) {
//        ObjectAnimator
//                .ofFloat(
//                        mLine,
//                        "translationX",
//                        mLine.getTranslationX(),
//                        getResources().getDisplayMetrics().widthPixels / 4
//                                * index).setDuration(300).start();
//    }

    @OnClick(R.id.search_btn)
    public void onClick() {
        mViewHelper.showLoadingView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewHelper.showEmptyView("没有所搜到订单信息");
            }
        },1500);
    }
}
