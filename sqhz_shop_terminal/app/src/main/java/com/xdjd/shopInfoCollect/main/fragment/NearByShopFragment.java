package com.xdjd.shopInfoCollect.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.NearbyShopAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.utils.MyLocationUtil;
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

public class NearByShopFragment extends BaseFragment implements ItemOnListener,LocationListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.locate)
    TextView locate;
    private View view;
    private  VaryViewHelper mVaryViewHelper = null;
    private int pageNo = 1;
    private int mFlag = 0;
    private List<NearbyShopBean> list = new ArrayList<NearbyShopBean>();
    private NearbyShopAdapter adapter;
    //纬度
    private String latitude = "";
    //经度
    private String longtitude = "";
    private LocationService locationService;

    private MyLocationUtil locationUtil;
    private String address = "";
    private String searchKey="";
    private Boolean locateFlag = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                if(!locateFlag){
                    loadData(PublicFinal.FIRST);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nearby_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);
        // -----------location config ------------
        locationService = ((App) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initView();
    }
    private void initView(){
        mVaryViewHelper = new VaryViewHelper(listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(listview);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                locateFlag = true;
                locationService.start();// 定位SDK
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
        editListener(etSearch);
    }

    @Override
    public void onItem(int position) {

    }

    @Override
    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();
        mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        locationService.stop();
    }

    @Override
    public void locationError(BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        locationService.stop(); //停止定位服务
    }

    @Override
    public void onPause() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
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

    @OnClick({R.id.ll_search,R.id.locate})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.locate:
                break;
            case R.id.ll_search:
                Log.e("搜索：","Y");
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

    /*附近店铺数据*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<NearbyShopBean> httpUtil = new AsyncHttpUtil<>(getActivity(), NearbyShopBean.class, new IUpdateUI<NearbyShopBean>() {
            @Override
            public void updata(NearbyShopBean bean) {
                if (bean.getRepCode().equals("00")){
                    if (null!=bean.getDataList() && bean.getDataList().size()>0){
                        if (mFlag == 0 || mFlag == 1){
                        }
                        list.addAll(bean.getDataList());
                        adapter.setData(list,1);
                        adapter.notifyDataSetChanged();
                        mVaryViewHelper.showDataView();
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了！");
                            pageNo--;
                        }else{
                            mVaryViewHelper.showEmptyView("还没有附近店铺，快去采集吧！");
                        }
                    }
                }else{
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
        httpUtil.post(M_Url.getCollectShop,L_RequestParams.getCollectShop(UserInfoUtils.getId(getActivity()),longtitude,latitude,
                searchKey,String.valueOf(pageNo),"10","1",""),false);
    }
}
