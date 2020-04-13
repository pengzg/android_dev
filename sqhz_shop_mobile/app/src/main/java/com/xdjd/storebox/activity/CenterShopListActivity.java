package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.xdjd.storebox.App;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.CenterShopListAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.CenterShopListBean;
import com.xdjd.storebox.service.LocationService;
import com.xdjd.utils.LogUtils;
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
 * Created by lijipei on 2017/3/13.
 */

public class CenterShopListActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.center_shop_list)
    ListView mCenterShopList;
    @BindView(R.id.address_tv)
    TextView mAddressTv;
    @BindView(R.id.refresh_ll)
    LinearLayout mRefreshLl;
    @BindView(R.id.refresh_iv)
    ImageView mRefreshIv;

    private CenterShopListAdapter adapter;

    private LocationService locationService;

    private String address;
    private int REFRESH_ADDRESS = 01;//更新地址显示
    private int REFRESH_LIST = 02;//更新地址列表
    private int REFRESH_SUCDESS = 03;
    private int REFRESH_FAILED = 04;
    private boolean is_animotion = false;
    private List<CenterShopListBean> list = new ArrayList<>();

    private VaryViewHelper mViewHelper;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH_ADDRESS){
                mAddressTv.setText("当前位置:"+address);
            }else if (msg.what == REFRESH_LIST){
                adapter.setData(list);
            }else{
                if (is_animotion){
                    mRefreshIv.clearAnimation();
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_shop);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("选择中心仓");

        mCenterShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CenterShopListActivity.this,ZhuceActivity.class);
                intent.putExtra("name",list.get(i).getMs_name());
                intent.putExtra("id",list.get(i).getMs_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        adapter = new CenterShopListAdapter();
        mCenterShopList.setAdapter(adapter);
    }

    @OnClick({R.id.refresh_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_ll:
                list.clear();
                adapter.notifyDataSetChanged();

                locationService.start();
                Animation operatingAnim = AnimationUtils.loadAnimation(this,
                        R.anim.rotate_anim_refresh);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                if (operatingAnim != null) {
                    mRefreshIv.startAnimation(operatingAnim);
                    is_animotion = true;
                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRefreshIv.clearAnimation();
//                    }
//                }, 1500);
                break;
        }
    }

    /**
     * 定位成功
     */
    private void successLocation(BDLocation location) {
        address = location.getAddrStr();
        mHandler.sendEmptyMessage(REFRESH_ADDRESS);
        Log.e("tag",location.getAddrStr());

        AsyncHttpUtil<CenterShopListBean> httpUtil = new AsyncHttpUtil<>(this, CenterShopListBean.class, new IUpdateUI<CenterShopListBean>() {
            @Override
            public void updata(CenterShopListBean bean) {
                if ("00".equals(bean.getRepCode())){
                    list.clear();
                    list.addAll(bean.getDataList());
                    mHandler.sendEmptyMessage(REFRESH_LIST);
                }else{
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getCenterShopList, L_RequestParams.
                getCenterShopList(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude())), false);
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }


    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            if (mRefreshIv)
//            mRefreshIv.clearAnimation();
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    //                    sb.append("gps定位成功");
                    successLocation(location);
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    //                    sb.append("网络定位成功");
                    successLocation(location);
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("离线定位成功，离线定位结果也是有效的");
                    successLocation(location);
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    showToast("定位失败,请重新定位!");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    showToast("定位失败,请重新定位!");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    showToast("定位失败,请检查手机是否处于飞行模式!");
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                LogUtils.e("loc", sb.toString());
            } else {
                showToast("定位失败,请重新定位!");
            }

            locationService.stop();
            mHandler.sendEmptyMessage(REFRESH_SUCDESS);

        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };


}
