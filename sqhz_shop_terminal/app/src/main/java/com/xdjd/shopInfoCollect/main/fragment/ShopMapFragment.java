package com.xdjd.shopInfoCollect.main.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/9
 *     desc   : 店铺地图fragment
 *     version: 1.0
 * </pre>
 */

public class ShopMapFragment extends BaseFragment {

    @BindView(R.id.mv_shop)
    MapView mMvShop;
    @BindView(R.id.iv_center)
    ImageView mIvCenter;
    @BindView(R.id.ll_my_location)
    LinearLayout mLlMyLocation;

    private BaiduMap mBaiduMap = null;
    private LatLng llCenter = null;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private String latitude = "";//纬度
    private String longtitude = "";//经度

    private int centerPadingBottom;

    private List<NearbyShopBean> listNear;//附近客户列表

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initView();
        initMap();
    }

    private void initView() {
        initCenter();
    }

    private void initMap() {
        mBaiduMap = mMvShop.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        //        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                showToast("第"+marker.getZIndex()+"个");
                return false;
            }
        });
    }

    private void initCenter() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.needle, options);
            centerPadingBottom = UIUtils.dp2px(bitmap.getHeight() - 8) / 2;
        } catch (Exception e) {
            centerPadingBottom = UIUtils.dp2px(33) / 2;
        }
        mIvCenter.setPadding(0, 0, 0, centerPadingBottom);
    }

    /**
     * 附近店铺数据
     */
    private void loadNearByShop() {
        AsyncHttpUtil<NearbyShopBean> httpUtil = new AsyncHttpUtil<>(getActivity(), NearbyShopBean.class, new IUpdateUI<NearbyShopBean>() {
            @Override
            public void updata(NearbyShopBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getDataList() && bean.getDataList().size() > 0) {
                        listNear = bean.getDataList();
                        updateNearMapShop();
                    }else{
                        showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.getCollectShop, L_RequestParams.getCollectShop(UserInfoUtils.getId(getActivity()), longtitude, latitude,
                "", "1", "20", "1", ""), false);
    }

    private void updateNearMapShop(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < listNear.size(); i++) {
            NearbyShopBean bean1 = listNear.get(i);
            LatLng llStaff;
            Marker marker;

            //添加位置信息接口
            LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.popup_shop_info, null);
            TextView tvName = (TextView) llPopup.findViewById(R.id.tv_shop_name);
            tvName.setText(bean1.getCcl_name());

            BitmapDescriptor bd = BitmapDescriptorFactory
                    .fromView(llPopup);
            llStaff = new LatLng(Double.parseDouble(bean1.getCcl_latitude()),
                    Double.parseDouble(bean1.getCcl_longitude()));
            builder = builder.include(llStaff);

            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(bd);
            MarkerOptions mo = new MarkerOptions().position(llStaff).icons(giflist)
                    .zIndex(i).period(10);
            marker = (Marker) mBaiduMap.addOverlay(mo);
        }

//        LatLngBounds latlngBounds = builder.build();
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMvShop.getWidth(), mMvShop.getHeight());
//        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onPause() {
        mMvShop.onPause();

        mLocClient.unRegisterLocationListener(myListener); //注销掉监听
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        mMvShop.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMvShop.onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.ll_my_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_my_location:
                if (mLocClient != null && mLocClient.isStarted()) {
                    mLocClient.registerLocationListener(myListener);
                }else{
                    // 定位初始化
                    mLocClient = new LocationClient(getActivity());
                    mLocClient.registerLocationListener(myListener);
                    LocationClientOption option = new LocationClientOption();
                    option.setOpenGps(true); // 打开gps
                    option.setCoorType("bd09ll"); // 设置坐标类型
                    option.setScanSpan(1000);
                    mLocClient.setLocOption(option);
                }
                mLocClient.start();
                break;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMvShop == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            llCenter = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(llCenter).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            mLocClient.stop();

            loadNearByShop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }
}
