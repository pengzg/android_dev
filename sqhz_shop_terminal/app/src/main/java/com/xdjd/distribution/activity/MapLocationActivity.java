package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.event.MapPositionEvent;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.overlayutil.PoiOverlay;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2017/6/26.
 */

public class MapLocationActivity extends BaseActivity implements BaiduMap.OnMapStatusChangeListener,
        OnGetGeoCoderResultListener,OnGetSuggestionResultListener,OnGetPoiSearchResultListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.mapview)
    MapView mMapview;
    @BindView(R.id.center_img)
    ImageView mCenterImg;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.searchkey)
    AutoCompleteTextView mSearchkey;

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    boolean isFirstLoc = true; // 是否首次定位

    int radius = 600;

    /**
     * 纬度
     */
    private String latitude = "";
    /**
     * 经度
     */
    private String longtitude = "";

    /**
     * 定位后的地址
     */
    private String address = "";

    /**
     * 城市
     */
    private String city = " ";

    private List<String> suggest;

    private ArrayAdapter<String> sugAdapter = null;
    private SuggestionSearch mSuggestionSearch = null;

    private PoiSearch mPoiSearch = null;

    private int loadIndex = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                if (mCenterImg.getVisibility() == View.GONE) {
                    mCenterImg.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_map_location;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("地址定位");
        mTitleBar.setRightText("确定");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("address", mTvAddress.getText().toString());
                intent.putExtra("latitude", latitude);
                intent.putExtra("longtitude", longtitude);
                setResult(Comon.QR_GOODS_RESULT_CODE, intent);
                finish();
                EventBus.getDefault().post(new MapPositionEvent());
                showToast("位置选择成功!");
            }
        });

        //        String address = getIntent().getStringExtra("address");
        //        mTvAddress.setText(address);
        //        latitude = getIntent().getStringExtra("latitude");
        //        longtitude = getIntent().getStringExtra("longtitude");

        mBaiduMap = mMapview.getMap();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap.setOnMapStatusChangeListener(this);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        sugAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
        mSearchkey.setAdapter(sugAdapter);
        mSearchkey.setThreshold(1);

        mSearchkey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCenterImg.setVisibility(View.GONE);

                String keystr = mSearchkey.getText().toString();
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(" ").keyword(keystr).pageNum(loadIndex));

//                String citystr = editCity.getText().toString();
//                String keystr = keyWorldsView.getText().toString();
//                mPoiSearch.searchInCity((new PoiCitySearchOption())
//                        .city(citystr).keyword(keystr).pageNum(loadIndex));
            }
        });

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        mSearchkey.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(" "));
            }
        });

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        latitude = String.valueOf(mapStatus.target.latitude);
        longtitude = String.valueOf(mapStatus.target.longitude);

        LogUtils.e("jingwei", latitude + "--" + longtitude);

        LatLng ptCenter = new LatLng((Float.valueOf((float) mapStatus.target.latitude)), (Float.valueOf((float) mapStatus.target.longitude)));
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        mMapview.onPause();

        mLocClient.unRegisterLocationListener(myListener); //注销掉监听
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapview.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        LogUtils.e("geoCodeResult",geoCodeResult.getAddress());
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
//        mBaiduMap.clear();
        mTvAddress.setText(result.getAddress());
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
            for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<String>(MapLocationActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
        mSearchkey.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     * @param result
     */
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MapLocationActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MapLocationActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * 对周边检索的范围进行绘制
     * @param center
     * @param radius
     */
    public void showNearbyArea(LatLng center, int radius) {
//        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
//                .fromResource(R.mipmap.icon_geo);
//        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
//        mBaiduMap.addOverlay(ooMarker);

        OverlayOptions ooCircle = new CircleOptions().fillColor( 0xCCCCCC00 )
                .center(center).stroke(new Stroke(5, 0xFFFF00FF ))
                .radius(radius);
        mBaiduMap.addOverlay(ooCircle);
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * @param result
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this,result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
            longtitude = result.getLocation().longitude+"";
            latitude = result.getLocation().latitude+"";
            mTvAddress.setText(result.getAddress());
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
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
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            latitude = String.valueOf(location.getLatitude());
            longtitude = String.valueOf(location.getLongitude());

            LatLng ptCenter = new LatLng((Float.valueOf((float) location.getLatitude())),
                    (Float.valueOf((float) location.getLongitude())));
            // 反Geo搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ptCenter));

            mLocClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }
}
