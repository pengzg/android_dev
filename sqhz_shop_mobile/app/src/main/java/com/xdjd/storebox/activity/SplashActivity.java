package com.xdjd.storebox.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.AdBean;
import com.xdjd.storebox.bean.UrlBean;
import com.xdjd.storebox.dao.UrlDao;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.util.List;

/**
 * 闪屏页
 * Created by lijipei on 2016/11/30.
 * freestyle_hong 添加广告页
 */

public class SplashActivity extends BaseActivity{
    private AdBean adBean ;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UrlDao mUrlDao;
    public List<UrlBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        mUrlDao = new UrlDao(this);
        list = mUrlDao.queryList();

        if(UserInfoUtils.getWelcome_btn(SplashActivity.this).equals("1") && !UserInfoUtils.getId(SplashActivity.this).equals("0")){
            //如果不是第一次登陆且是登陆过
            ReqAdvertise();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((list == null || list.size() == 0) && UserInfoUtils.getDomainName(getBaseContext()).equals("0")) {
                    startActivity(SystemConfigurationActivity.class);
                } else {
                    if (list != null && list.size() > 0) {
                        //添加数据库中的域名地址
                        UserInfoUtils.setDomainName(SplashActivity.this, list.get(0).getDomain_name());
                    }
                if (UserInfoUtils.getId(SplashActivity.this).equals("0")){
                    if(!UserInfoUtils.getWelcome_btn(SplashActivity.this).equals("1")){
                        startActivity(WelcomeActivity.class);//第一次进欢迎页
                    }else{
                        startActivity(LoginActivity.class);
                    }
                }else{
                    if (!TextUtils.isEmpty(preferences.getString("gesture", ""))) {
                        if(UserInfoUtils.getLock(SplashActivity.this).equals("1")){
                            Intent intent=new Intent(SplashActivity.this,SetGestureActivity.class);
                            intent.putExtra("activityNum",0);
                            startActivity(intent);
                        }else{
                            if(adBean == null){
                                startActivity(MainActivity.class);
                            }else{
                                Intent intent = new Intent(SplashActivity.this,AdverActivity.class);
                                intent.putExtra("adBean",adBean);
                                startActivity(intent);
                            }
                        }
                    }else{
                        if(adBean == null){
                            startActivity(MainActivity.class);
                        }else{
                            Intent intent = new Intent(SplashActivity.this,AdverActivity.class);
                            intent.putExtra("adBean",adBean);
                            startActivity(intent);
                        }
                    }
                }
                finish();
            }
            }
        },2000);
    }

    /*请求广告接口*/
    private void ReqAdvertise(){
        AsyncHttpUtil<AdBean> httpUtil = new AsyncHttpUtil<>(this, AdBean.class, new IUpdateUI<AdBean>() {
            @Override
            public void updata(AdBean bean) {
                if(bean.getRepCode().equals("00")){
                    adBean = new AdBean();
                    adBean.setAi_cover(bean.getAi_cover()) ;
                    adBean.setAi_type(bean.getAi_type());
                    adBean.setAi_type_value(bean.getAi_type_value());
                    adBean.setAi_title(bean.getAi_title());
//                    Log.e("cover",adBean.getAi_cover());
//                    Log.e("type",String.valueOf(adBean.getAi_type()));
//                    Log.e("typeValue",adBean.getAi_type_value());
                }else{
                    //根据情况来跳转
                    //startActivity(MainActivity.class);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.Advertise, L_RequestParams.Advertise("1",UserInfoUtils.getCenterShopId(this)),false);
    }
}
