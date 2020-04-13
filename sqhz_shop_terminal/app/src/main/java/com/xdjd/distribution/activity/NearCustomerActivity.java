package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
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
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.clusterutil.clustering.Cluster;
import com.xdjd.utils.clusterutil.clustering.ClusterItem;
import com.xdjd.utils.clusterutil.clustering.ClusterManager;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/13
 *     desc   : 业务员附近客户activity
 *     version: 1.0
 * </pre>
 */

public class NearCustomerActivity extends BaseActivity implements BaiduMap.OnMapLoadedCallback {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.mapview)
    MapView mMapview;
    @BindView(R.id.iv_center)
    ImageView mIvCenter;
    @BindView(R.id.ll_my_location)
    LinearLayout mLlMyLocation;
    @BindView(R.id.cv_my_location)
    CardView mCvMyLocation;
    @BindView(R.id.iv_update_near)
    ImageView mIvUpdateNear;
    @BindView(R.id.ll_update_near)
    LinearLayout mLlUpdateNear;
    @BindView(R.id.cv_update_near)
    CardView mCvUpdateNear;
    @BindView(R.id.cv_add_client)
    CardView mCvAddClient;

    private BaiduMap mBaiduMap;

    private int mapZoom = 19;//地图显示级别

    private String latitude = "";//纬度
    private String longtitude = "";//经度

    private MapStatus ms;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private LatLng llCenter = null;

    private ClusterManager<MyItem> mClusterManager;

    private int centerPadingBottom;
    private ObjectAnimator rotationAnimation;//属性动画

    private List<NearbyShopBean> listCustomer;//客户位置数据集合

    //公司位置标注
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_geo);

    LinearLayout llPopup;
    TextView tvName;

    List<MyItem> items;//点集合集合

    @Override
    protected int getContentView() {
        return R.layout.activity_near_customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("附近客户");

       /* mTitleBar.setRightImageResource(R.mipmap.add_client);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        items = new ArrayList<>();

        initCenter();
        initMap();
    }

    private void initMap() {
        mBaiduMap = mMapview.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        initLocation();

        mBaiduMap.setOnMapLoadedCallback(this);
        //初始化刷新图标的动画
        rotationAnimation = ObjectAnimator.ofFloat(mIvUpdateNear, "rotation", 0f, 360f);

        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);

        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(final MyItem item) {
                //                initOverlay(item.getPosition(),listCustomer.get(item.getIndex()));
                DialogUtil.showCustomDialog(NearCustomerActivity.this, "提示", "是否更新--" +
                        listCustomer.get(item.getIndex()).getCcl_name() + "--店铺信息", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        Intent intent = new Intent(NearCustomerActivity.this, AddClientActivity.class);
                        intent.putExtra("locationId", listCustomer.get(item.getIndex()).getCcl_id());
                        startActivity(intent);
                    }

                    @Override
                    public void no() {
                    }
                });
                return false;
            }
        });

        llPopup = (LinearLayout) LayoutInflater.from(NearCustomerActivity.this).inflate(R.layout.popup_shop_info, null);
        tvName = (TextView) llPopup.findViewById(R.id.tv_shop_name);
    }

    /*private void initOverlay(LatLng ll,NearbyShopBean bean) {
        try {
            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(bd);
            MarkerOptions ooD = new MarkerOptions().position(ll).icons(giflist)
                    .zIndex(0).period(10);
            Marker marker = (Marker) (mBaiduMap.addOverlay(ooD));

            MapStatusUpdate u = MapStatusUpdateFactory
                    .newLatLng(ll);
            mBaiduMap.setMapStatus(u);

            LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_map_shop_info, null);
            TextView tvName = (TextView) llPopup.findViewById(R.id.tv_shop_name);
            ImageView ivHead = (ImageView) llPopup.findViewById(R.id.cs_image);
            TextView tvUpdate = (TextView) llPopup.findViewById(R.id.tv_update);
            tvUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            tvName.setText(bean.getCcl_name());
            if (bean.getCcl_img()!=null && bean.getCcl_img().length() > 0){
                Glide.with(this).load(bean.getCcl_img()).asBitmap().into(ivHead);
            }

            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            LatLng latLng = marker.getPosition();
            InfoWindow mInfoWindow = new InfoWindow(llPopup, latLng, -107);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } catch (Exception e) {
            LogUtils.e("异常",e.toString());
        }
    }*/

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

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().target(llCenter).zoom(mapZoom).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    private void initLocation() {
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.registerLocationListener(myListener);
        } else {
            // 定位初始化
            mLocClient = new LocationClient(this);
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
        }
        mLocClient.start();
    }

    @OnClick({R.id.ll_my_location, R.id.ll_update_near,R.id.cv_add_client})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_my_location:
                ms = new MapStatus.Builder().target(llCenter).zoom(18).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

                mapZoom = 19;

                initLocation();
                break;
            case R.id.ll_update_near:
                MapStatus ms = mBaiduMap.getMapStatus();
                llCenter = ms.target;
                longtitude = String.valueOf(llCenter.longitude);
                latitude = String.valueOf(llCenter.latitude);

                rotationAnimation.setRepeatCount(2);
                rotationAnimation.setDuration(400);
                rotationAnimation.start();

                ms = new MapStatus.Builder().target(llCenter).zoom(18).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

                mapZoom = 19;

                mClusterManager.clearItems();

                getNearbyShop();
                break;
            case R.id.cv_add_client://添加客户
                startActivity(AddClientActivity.class);
                break;
        }
    }

    /*获取附近店铺列表*/
    private void getNearbyShop() {
        AsyncHttpUtil<NearbyShopBean> httpUtil = new AsyncHttpUtil<>(NearCustomerActivity.this, NearbyShopBean.class, new IUpdateUI<NearbyShopBean>() {
            @Override
            public void updata(NearbyShopBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (null != bean.getDataList() && bean.getDataList().size() > 0) {
                        listCustomer = bean.getDataList();
                        addMarkers();//全部客户
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
        httpUtil.post(M_Url.getNearbyShop, L_RequestParams.getNearbyShop(longtitude, latitude,
                "", "1", "999", "1"), false);
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        mBaiduMap.clear();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

        mClusterManager.clearItems();
        if (items == null) {
            items = new ArrayList<MyItem>();
        }
        items.clear();
        for (int i = listCustomer.size() - 1; i >= 0; i--) {
            LatLng ll = new LatLng(Double.valueOf(listCustomer.get(i).getCcl_latitude()),
                    Double.valueOf(listCustomer.get(i).getCcl_longitude()));
            MyItem myItem = new MyItem();
            myItem.setIndex(i);
            myItem.setPosition(ll);
            myItem.setLocationId(listCustomer.get(i).getCcl_id());
            items.add(myItem);
        }

        mClusterManager.addItems(items);

        ms = new MapStatus.Builder().target(llCenter).zoom(19).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapview == null) {
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

            getNearbyShop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private int mIndex;
        private String locationId;

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public int getIndex() {
            return mIndex;
        }

        /*public MyItem(LatLng latLng, int index) {
            mPosition = latLng;
            mIndex = index;
        }*/

        public void setIndex(int index) {
            mIndex = index;
        }

        public void setPosition(LatLng position) {
            mPosition = position;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {

            tvName.setText(listCustomer.get(mIndex).getCcl_name());
            return BitmapDescriptorFactory
                    .fromView(llPopup);
        }
    }

    @Override
    public void onPause() {
        mMapview.onPause();

        mLocClient.unRegisterLocationListener(myListener); //注销掉监听
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        mMapview.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mBaiduMap.clear();
        mMapview.onDestroy();
        super.onDestroy();
    }
}
