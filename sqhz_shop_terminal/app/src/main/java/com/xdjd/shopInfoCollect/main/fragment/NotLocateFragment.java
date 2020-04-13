package com.xdjd.shopInfoCollect.main.fragment;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.NearbyShopAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.shopInfoCollect.activity.CollectClientActivity;
import com.xdjd.utils.PublicFinal;
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

public class NotLocateFragment extends BaseFragment implements ItemOnListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    private View view;
    private VaryViewHelper varyViewHelper = null;
    private NearbyShopAdapter adapter;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<NearbyShopBean> list = new ArrayList<NearbyShopBean>();
    private String searchKey="";
    private NearbyShopBean bean = new NearbyShopBean();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notlocate_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        varyViewHelper = new VaryViewHelper(listview);
        initRefresh(listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
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
        adapter = new NearbyShopAdapter(this) ;
        listview.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
        editListener(etSearch);
    }

    @Override
    public void onItem(int position) {
        bean.setCcl_name(list.get(position).getCcl_name());
        bean.setCcl_contacts_name(list.get(position).getCcl_contacts_name());
        bean.setCcl_contacts_mobile(list.get(position).getCcl_contacts_mobile());
        bean.setCcl_id(list.get(position).getCcl_id());
        bean.setCcl_img(list.get(position).getCcl_img());
        Intent intent = new Intent(getActivity(), CollectClientActivity.class);
        intent.putExtra("bean",bean);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                }else{
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
                    Log.e("Focus","F");
                } else {//失去焦点
                }
            }
        });
    }

    @OnClick(R.id.ll_search)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_search:
                searchKey = etSearch.getText().toString();
                if(!"".equals(searchKey)&&null != searchKey){
                    list.clear();
                    loadData(PublicFinal.FIRST);
                }else{
                    showToast("搜索内容为空！");
                }
                break;
        }
    }

    /*未定位店铺*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            varyViewHelper.showLoadingView();
        }
        AsyncHttpUtil<NearbyShopBean> httpUtil = new AsyncHttpUtil<>(getActivity(), NearbyShopBean.class, new IUpdateUI<NearbyShopBean>() {
            @Override
            public void updata(NearbyShopBean bean) {
                if (bean.getRepCode().equals("00")){
                    if (null!=bean.getDataList() && bean.getDataList().size()>0){
                        if (mFlag == 0 || mFlag == 1){
                        }
                        list.addAll(bean.getDataList());
                        adapter.setData(list,2);
                        adapter.notifyDataSetChanged();
                        varyViewHelper.showDataView();
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了！");
                            pageNo--;
                        }else{
                            varyViewHelper.showEmptyView("还没有未定位店铺！");
                        }
                    }
                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                varyViewHelper.showErrorView(s.getDetail(),new OnErrorListener());
            }
            @Override
            public void finish() {
                listview.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCollectShop, L_RequestParams.getCollectShop(UserInfoUtils.getId(getActivity()),"","",
                searchKey,String.valueOf(pageNo),"10","2",""),false);
    }

    public class OnErrorListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            loadData(PublicFinal.FIRST);
        }
    }
}
