package com.xdjd.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xdjd.distribution.bean.Location;

/**
 * Created by juck on 15/12/21.
 * 1.拿到instance
 * 2.初始化locationclient
 * 3.启动
 * 4.获取到这个变化
 */
public class LocationInfoUtil {
    private LocationClient locationClient;
    public BDLocationListener myListener = new MyLocationListener();

    private LocationInfoUtil(){}
    private static LocationInfoUtil locationInstance = new LocationInfoUtil();
    public static LocationInfoUtil getLocationInfo(){
        return locationInstance;
    }

    public  void initLocationor(){
        if (locationClient==null) {
            //声明LocationClient类
            locationClient = new LocationClient(UIUtils.getContext());
            //注册监听函数
            locationClient.registerLocationListener(myListener);
        }
        initLocation();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span=1000;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);
        locationClient.setLocOption(option);
    }
    public void startLocation(){
        locationClient.start();
    }

    public void stopLocation(){
        if (locationInstance!=null){
            locationClient.stop();
        }

    }

    public LocationListener locationListener;

    public interface LocationListener{
         void getLocationSuccess(Location location);
         void getLocationError(Location location);
    }
    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            Location locationData = new Location();
            //
            locationData.time = location.getTime();
            locationData.errorCode = location.getLocType();
            locationData.latitude = (float) location.getLatitude();
            locationData.longitude = (float) location.getLongitude();
            locationData.radius = location.getRadius();
            locationData.city = location.getCity();
            LogUtils.e("location","--------当前的城市:"+locationData.city);
            //
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                //
                locationData.speed = location.getSpeed();
                locationData.satellite = location.getSatelliteNumber();
                locationData.height = location.getAltitude();
                locationData.direction = location.getDirection();
                locationData.addr = location.getAddrStr();

                locationData.describe = "gps定位成功";
                //
                locationData.listPoi = location.getPoiList();
                locationData.locationdescribe = location.getLocationDescribe();
                if (locationListener!=null){
                    locationListener.getLocationSuccess(locationData);
                }

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                //
                locationData.addr = location.getAddrStr();
                LogUtils.e("location",location.getAddrStr()+"");
                locationData.operationers = location.getOperators();
                //
                locationData.listPoi = location.getPoiList();
                locationData.locationdescribe = location.getLocationDescribe();
                locationData.describe ="网络定位成功";
                if (locationListener!=null){
                    locationListener.getLocationSuccess(locationData);
                }
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                locationData.describe ="离线定位成功";
                if (locationListener!=null){
                    locationListener.getLocationSuccess(locationData);
                }
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                locationData.describe ="定位失败";
                if (locationListener!=null){
                    locationListener.getLocationError(locationData);
                }
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                locationData.describe ="定位失败";
                if (locationListener!=null){
                    locationListener.getLocationError(locationData);
                }
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                locationData.describe ="定位失败";
                if (locationListener!=null){
                    locationListener.getLocationError(locationData);
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }
}
