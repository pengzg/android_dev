package com.xdjd.distribution.bean;

import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by juck on 15/12/26.
 */
public class Location {

    public String time;
    public int errorCode;
    public float latitude;
    public float longitude;
    public float radius;

    //GPS 成功
    public float speed;            //单位:公里每小时
    public int satellite;
    public double height;           //单位:米
    public float direction;        // 单位度
    public String addr;             //地址
    public String describe;        //定位成功的类型
    public String city;          // 城市

    //网络定位成功
    public int operationers;     //获取运营商信息

    //离线定位成功
    //
    public String locationdescribe; //位置语义化信息
    //poi
    public List<Poi> listPoi ;      //p.getId() + " " + p.getName() + " " + p.getRank()
}
