package com.xdjd.distribution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.RolloutGoodsDetailActivity;
import com.xdjd.distribution.adapter.RolloutGoodsOrderAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsOrderFragment extends BaseFragment implements ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener {
    Unbinder unbinder;
    @BindView(R.id.elv_order_goods)
    AnimatedExpandableListView mElvOrderGoods;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_clear)
    LinearLayout mLlClear;
    @BindView(R.id.tv_search)
    TextView mTvSearch;

    private RolloutGoodsOrderAdapter adapterOrder;
    private List<PHOrderDetailBean> list = new ArrayList<>();
    //初始的商品列表
    public List<PHOrderDetailBean> listIntrinsicGoods = new ArrayList<>();
    private String customerId;

    private boolean isUnfold = true;//是否全部展开

    private RolloutGoodsDetailActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rollout_goods_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        activity = (RolloutGoodsDetailActivity) getActivity();
        adapterOrder = new RolloutGoodsOrderAdapter();

        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mEtSearch.setSelection(mEtSearch.getText().length());
                    mEtSearch.addTextChangedListener(mTextWatcher);
                    if (mEtSearch.getText().length() > 0) {
                        mLlClear.setVisibility(View.VISIBLE);
                    } else {
                        mLlClear.setVisibility(View.GONE);
                    }
                } else {
                    mEtSearch.removeTextChangedListener(mTextWatcher);
                }
            }
        });

        mElvOrderGoods.setAdapter(adapterOrder);
        mElvOrderGoods.setOnGroupExpandListener(this);
        mElvOrderGoods.setOnChildClickListener(this);
        adapterOrder.setData(list);
        customerId = getActivity().getIntent().getStringExtra("customerId");
        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mEtSearch.hasFocus()) {
            mEtSearch.setFocusable(false);
            mEtSearch.setFocusable(true);
            mEtSearch.setFocusableInTouchMode(true);
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                mLlClear.setVisibility(View.VISIBLE);
            } else {
                mLlClear.setVisibility(View.GONE);
            }
        }
    };

    public void unfold(EaseTitleBar titleBar) {
        if (isUnfold) {
            isUnfold = false;
            titleBar.setRightText(UIUtils.getString(R.string.pack_up_all_order));
        } else {
            isUnfold = true;
            titleBar.setRightText(UIUtils.getString(R.string.unfold_all_order));
        }
        //遍历所有group,将所有项设置成默认展开
        int groupCount = list.size();
        for (int i = 0; i < groupCount; i++) {
            if (isUnfold) {
                mElvOrderGoods.collapseGroup(i);
            } else {
                mElvOrderGoods.expandGroup(i);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        return false;
    }

    @Override
    public void onGroupExpand(int i) {

    }

    private void loadData() {
        AsyncHttpUtil<PHOrderDetailBean> httpUtil = new AsyncHttpUtil<>(getActivity(), PHOrderDetailBean.class, new IUpdateUI<PHOrderDetailBean>() {
            @Override
            public void updata(PHOrderDetailBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        listIntrinsicGoods.addAll(bean.getListData());
                        adapterOrder.setData(list);
                    }
                } else {
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.phOrderDetailList, L_RequestParams.phOrderDetailList(customerId, "Y"), true);
    }

    @OnClick({R.id.ll_clear, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search://搜索商品
                list.clear();
                adapterOrder.notifyDataSetChanged();
                if (TextUtils.isEmpty(mEtSearch.getText())) {
                    list.addAll(listIntrinsicGoods);
                } else {
                    for (int i = 0; i < listIntrinsicGoods.size(); i++) {
                        List<PHOrderDetailBean> list = new ArrayList<>();
                        for (PHOrderDetailBean bean : listIntrinsicGoods.get(i).getListGoodsData()) {
                            if (bean.getGoodsName().indexOf(mEtSearch.getText().toString()) != -1) {
                                list.add(bean);
                            }
                        }
                        if (list.size() > 0) {
                            PHOrderDetailBean bean = (PHOrderDetailBean) listIntrinsicGoods.get(i).clone();
                            bean.setListGoodsData(list);
                            this.list.add(bean);
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    mElvOrderGoods.expandGroup(i);
                }
                isUnfold = false;
                activity.getTitleBar().setRightText(UIUtils.getString(R.string.pack_up_all_order));
                adapterOrder.notifyDataSetChanged();
                break;
            case R.id.ll_clear:
                mEtSearch.setText("");
                list.clear();
                list.addAll(listIntrinsicGoods);
                adapterOrder.notifyDataSetChanged();
                for (int i = 0; i < list.size(); i++) {
                    mElvOrderGoods.expandGroup(i);
                }
                isUnfold = false;
                activity.getTitleBar().setRightText(UIUtils.getString(R.string.pack_up_all_order));
                break;
        }
    }
}
