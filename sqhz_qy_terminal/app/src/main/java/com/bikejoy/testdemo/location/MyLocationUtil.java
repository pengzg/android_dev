package com.bikejoy.testdemo.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MyLocationUtil implements BDLocationListener {

    public LocationListener listener;

    public void setListener(LocationListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null != location && location.getLocType() != BDLocation.TypeServerError) {
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                //sb.append("gps定位成功");
                if (listener != null) {
                    listener.locationSuccess(location);
                }
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                //sb.append("网络定位成功");
                if (listener != null) {
                    listener.locationSuccess(location);
                }
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                //sb.append("离线定位成功，离线定位结果也是有效的");
                if (listener != null) {
                    listener.locationSuccess(location);
                }
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                if (listener != null) {
                    listener.locationError(location);
                }
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //sb.append("网络不同导致定位失败，请检查网络是否通畅");
                if (listener != null) {
                    listener.locationError(location);
                }
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                if (listener != null) {
                    listener.locationError(location);
                }
            }
        } else {
            if (listener != null) {
                listener.locationError(location);
            }
        }
    }

}
