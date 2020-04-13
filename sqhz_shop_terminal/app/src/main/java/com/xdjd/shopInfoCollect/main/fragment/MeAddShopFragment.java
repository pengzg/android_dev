package com.xdjd.shopInfoCollect.main.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.NearbyShopAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.shopInfoCollect.activity.CollectClientActivity;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/9/28.
 */

public class MeAddShopFragment extends BaseFragment implements ItemOnListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_no)
    TextView mTvNo;
    @BindView(R.id.tv_been)
    TextView mTvBeen;
    private View view;
    private VaryViewHelper varyViewHelper = null;
    private NearbyShopAdapter adapter;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<NearbyShopBean> list = new ArrayList<NearbyShopBean>();
    private String searchKey = "";
    private String checkStatus = "1";
    private NearbyShopBean bean = new NearbyShopBean();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me_add_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        varyViewHelper = new VaryViewHelper(listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(listview);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO);
            }
        });
        adapter = new NearbyShopAdapter(this);
        listview.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
        editListener(etSearch);
    }

    /*文本框获取焦点和文本内容监听*/
    private void editListener(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                } else {
                    searchKey = "";
                    list.clear();
                    loadData(PublicFinal.TWO);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && editText.getText().length() > 0) {//获得焦点
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    Log.e("Focus", "F");
                } else {//失去焦点
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItem(int position) {
        if (checkStatus.equals("1")) {
            bean.setCcl_name(list.get(position).getCcl_name());
            bean.setCcl_contacts_name(list.get(position).getCcl_contacts_name());
            bean.setCcl_contacts_mobile(list.get(position).getCcl_contacts_mobile());
            bean.setCcl_id(list.get(position).getCcl_id());
            bean.setCcl_img(list.get(position).getCcl_img());
            Intent intent = new Intent(getActivity(), CollectClientActivity.class);
            intent.putExtra("bean", bean);
            startActivity(intent);
        }
    }

    @OnClick({R.id.ll_search,R.id.tv_no,R.id.tv_been})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                Log.e("搜索：", "Y");
                searchKey = etSearch.getText().toString();
                if (!"".equals(searchKey) && null != searchKey) {
                    list.clear();
                    loadData(PublicFinal.FIRST);
                } else {
                    showToast("搜索内容为空！");
                }
                break;
            case R.id.tv_no:
                mTvNo.setTextColor(UIUtils.getColor(R.color.white));
                mTvBeen.setTextColor(UIUtils.getColor(R.color.text_gray));
                checkStatus = "1";
                moveAnimation(mTvNo);
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.FIRST);
                break;
            case R.id.tv_been:
                mTvBeen.setTextColor(UIUtils.getColor(R.color.white));
                mTvNo.setTextColor(UIUtils.getColor(R.color.text_gray));
                checkStatus = "2";
                moveAnimation(mTvBeen);
                list.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.FIRST);
                break;
        }
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.setDuration(400).start();
    }

    /*已采集店铺*/
    private void loadData(int isFirst) {
        if (isFirst == PublicFinal.FIRST) {
            varyViewHelper.showLoadingView();
        }
        AsyncHttpUtil<NearbyShopBean> httpUtil = new AsyncHttpUtil<>(getActivity(), NearbyShopBean.class, new IUpdateUI<NearbyShopBean>() {
            @Override
            public void updata(NearbyShopBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getDataList() && bean.getDataList().size() > 0) {
                        if (mFlag == 0 || mFlag == 1) {
                        }
                        list.addAll(bean.getDataList());
                        adapter.setData(list, 3);
                        adapter.notifyDataSetChanged();
                        varyViewHelper.showDataView();
                    } else {
                        if (mFlag == 2) {
                            showToast("没有更多数据了！");
                            pageNo--;
                        } else {
                            varyViewHelper.showEmptyView("还没有采集店铺！");
                        }
                        adapter.notifyDataSetChanged();
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
                listview.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCollectShop, L_RequestParams.getCollectShop(UserInfoUtils.getId(getActivity()), "", "",
                searchKey, String.valueOf(pageNo), "10", "3", checkStatus), false);
    }
}
