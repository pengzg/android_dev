package com.xdjd.steward.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.steward.bean.CustomerLocationBean;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.clusterutil.clustering.Cluster;
import com.xdjd.utils.clusterutil.clustering.ClusterItem;
import com.xdjd.utils.clusterutil.clustering.ClusterManager;
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
 *     desc   : 客户地图fragment
 *     version: 1.0
 * </pre>
 */

public class CustomerMapFragment extends BaseFragment implements BaiduMap.OnMapLoadedCallback {

    @BindView(R.id.mv_shop)
    MapView mMvShop;
    @BindView(R.id.ll_my_location)
    LinearLayout mLlMyLocation;
    @BindView(R.id.cv_my_location)
    CardView mCvMyLocation;
    @BindView(R.id.tv_all)
    TextView mTvAll;
    @BindView(R.id.ll_all)
    LinearLayout mLlAll;
    @BindView(R.id.cv_all)
    CardView mCvAll;
    @BindView(R.id.iv_center)
    ImageView mIvCenter;
    @BindView(R.id.iv_update_near)
    ImageView mIvUpdateNear;
    @BindView(R.id.ll_update_near)
    LinearLayout mLlUpdateNear;
    @BindView(R.id.cv_update_near)
    CardView mCvUpdateNear;

    private BaiduMap mBaiduMap = null;
    private LatLng llCenter = null;
    private int mapZoom = 16;//地图显示级别
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private String latitude = "";//纬度
    private String longtitude = "";//经度

    private MapStatus ms;

    private List<CustomerLocationBean> listAllLocation;//客户位置列表
    private List<CustomerLocationBean> listNearLocation;//客户位置列表
    private ClusterManager<MyItem> mClusterManager;

    private int centerPadingBottom;

    private boolean isAllCustomer = false;//是否加载全部客户

    private ObjectAnimator rotationAnimation;//属性动画

    private List<MyItem> myAllItems = new ArrayList<MyItem>();//全部客户的集合
    private List<MyItem> myNearItems = new ArrayList<MyItem>();//附近客户的集合

    LinearLayout llPopup;//地图显示的布局
    TextView tvName;

    //客户店铺
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_gcoding);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initCenter();
        initMap();
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

        mBaiduMap.setOnMapLoadedCallback(this);
        //初始化刷新图标的动画
        rotationAnimation = ObjectAnimator.ofFloat(mIvUpdateNear, "rotation", 0f, 360f);

        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(getActivity(), mBaiduMap);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                //                Toast.makeText(CartLocationActivity.this,
                //                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                //                Toast.makeText(CartLocationActivity.this,
                //                        "点击单个Item", Toast.LENGTH_SHORT).show();
                String name;
                if (isAllCustomer){
                    name = listAllLocation.get(item.getIndex()).getCc_name();
                }else{
                    name = listNearLocation.get(item.getIndex()).getCc_name();
                }
                initCustomerInfo(item.getPosition(),name);
                return false;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //                showToast("第"+marker.getZIndex()+"个");
                return false;
            }
        });

        llPopup = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.popup_shop_info, null);
        tvName = (TextView) llPopup.findViewById(R.id.tv_shop_name);
    }

    private void initCustomerInfo(LatLng ll, String name) {
        try {
            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(bd);
            MarkerOptions ooD = new MarkerOptions().position(ll).icons(giflist)
                    .zIndex(0).period(10);
            Marker marker = (Marker) (mBaiduMap.addOverlay(ooD));

//            MapStatusUpdate u = MapStatusUpdateFactory
//                    .newLatLng(ll);
//            mBaiduMap.setMapStatus(u);

            LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.popup_shop_info, null);
            TextView tvName = (TextView) llPopup.findViewById(R.id.tv_shop_name);
            ImageView imageView = (ImageView) llPopup.findViewById(R.id.iv_img);
            imageView.setVisibility(View.INVISIBLE);
            tvName.setText(name);

            LatLng latLng = marker.getPosition();
            InfoWindow mInfoWindow = new InfoWindow(llPopup, latLng, -0);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } catch (Exception e) {
            LogUtils.e("异常", e.toString());
        }
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
     * 客户位置接口
     *
     * @param type 1:附近
     */
    private void loadCustomerLocation(String type) {
        AsyncHttpUtil<CustomerLocationBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CustomerLocationBean.class, new IUpdateUI<CustomerLocationBean>() {
            @Override
            public void updata(CustomerLocationBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getDataList() && bean.getDataList().size() > 0) {
                        if (isAllCustomer) {
                            listAllLocation = bean.getDataList();
                            addMarkers(listAllLocation);//全部客户
                        } else {
                            listNearLocation = bean.getDataList();
                            addNear(listNearLocation);//附近客户
                        }
                    } else {
                        mClusterManager.clearItems();
                        mBaiduMap.clear();

                        showToast("没有客户位置信息");
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
        httpUtil.post(M_Url.getGlyNearCustomerList, L_RequestParams.getGlyNearCustomerList(longtitude, latitude,
                "", "1", "9999", type), false);
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers(List<CustomerLocationBean> listAllLocation) {
        mBaiduMap.clear();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

        mClusterManager.clearItems();
        if (myAllItems == null) {
            myAllItems = new ArrayList<MyItem>();
        }

        if (myAllItems.size() == 0) {//如果没有数据是采取创建对象添加
            for (int i = listAllLocation.size() - 1; i >= 0; i--) {
                LatLng ll = new LatLng(listAllLocation.get(i).getCc_latitude(), listAllLocation.get(i).getCc_longitude());
                MyItem myItem = new MyItem();
                myItem.setPosition(ll);
                myItem.setIndex(i);
                myAllItems.add(myItem);
            }
        }

        mClusterManager.addItems(myAllItems);

        ms = new MapStatus.Builder().target(llCenter).zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    /**
     * 添加附近客户
     */
    private void addNear(List<CustomerLocationBean> listNearLocation) {
        mBaiduMap.clear();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        mClusterManager.clearItems();

        if (myNearItems == null) {
            myNearItems = new ArrayList<>();
        }
        myNearItems.clear();

        for (int i = listNearLocation.size() - 1; i >= 0; i--) {
            LatLng ll = new LatLng(listNearLocation.get(i).getCc_latitude(), listNearLocation.get(i).getCc_longitude());
            MyItem myItem = new MyItem();
            myItem.setPosition(ll);
            myItem.setIndex(i);
            myNearItems.add(myItem);
        }

        mClusterManager.addItems(myNearItems);

        ms = new MapStatus.Builder().target(llCenter).zoom(16).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

    }

    @OnClick({R.id.ll_my_location, R.id.ll_update_near, R.id.ll_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_my_location://自己附近的客户
                mClusterManager.clearItems();
                mBaiduMap.clear();

                ms = new MapStatus.Builder().target(llCenter).zoom(15).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

                mapZoom = 16;
                mTvAll.setTextColor(UIUtils.getColor(R.color.text_gray));
                isAllCustomer = false;

                initLocation();
                break;
            case R.id.ll_update_near://刷新地图中心的附近客户
                mapZoom = 16;
                isAllCustomer = false;
                mTvAll.setTextColor(UIUtils.getColor(R.color.text_gray));

                mBaiduMap.clear();

                MapStatus ms = mBaiduMap.getMapStatus();
                llCenter = ms.target;
                longtitude = String.valueOf(llCenter.longitude);
                latitude = String.valueOf(llCenter.latitude);

                rotationAnimation.setRepeatCount(2);
                rotationAnimation.setDuration(400);
                rotationAnimation.start();

                ms = new MapStatus.Builder().target(llCenter).zoom(15).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

                mClusterManager.clearItems();

                loadCustomerLocation("1");
                break;
            case R.id.ll_all://加载全部客户
                mapZoom = 9;
                isAllCustomer = true;
                mTvAll.setTextColor(UIUtils.getColor(R.color.text_blue));
                mBaiduMap.clear();
                initLocation();
                break;
        }
    }

    private void initLocation() {
        try {
            if (mLocClient != null && mLocClient.isStarted()) {
                mLocClient.registerLocationListener(myListener);
            } else {
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
        } catch (Exception e) {
            LogUtils.e("initLocation", e.toString());
        }
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private int mIndex;

        //        public MyItem(LatLng latLng, int index) {
        //            mPosition = latLng;
        //            mIndex = index;
        //        }

        public int getIndex() {
            return mIndex;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public void setIndex(int index) {
            mIndex = index;
        }

        public void setPosition(LatLng position) {
            mPosition = position;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            //添加位置信息接口
//            LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.popup_shop_info, null);
//            TextView tvName = (TextView) llPopup.findViewById(R.id.tv_shop_name);
//            if (isAllCustomer){//全部客户
//                tvName.setText(listAllLocation.get(getIndex()).getCc_name());
//            }else{//附近客户
//                try{
//                    tvName.setText(listNearLocation.get(getIndex()).getCc_name());
//                }catch(Exception e){
//                    LogUtils.e("listNearLocation异常",getIndex()+"--size:"+listNearLocation.size());
//                }
//            }
            return BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding);
        }
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

            longtitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());

            mLocClient.stop();

            if (isAllCustomer) {//全部
                if (listAllLocation != null && listAllLocation.size() > 0) {
                    addMarkers(listAllLocation);
                } else {
                    loadCustomerLocation("2");
                }
            } else {
                loadCustomerLocation("1");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().target(llCenter).zoom(mapZoom).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }
}
