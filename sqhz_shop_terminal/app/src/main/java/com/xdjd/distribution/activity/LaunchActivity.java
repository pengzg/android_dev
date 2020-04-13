package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.UrlBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.dao.UrlDao;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.shopInfoCollect.main.ShopCollectMainActivity;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UserInfoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/3/15.
 */

public class LaunchActivity extends BaseActivity {

    //    @BindView(R.id.iv_welcome)
    //    ImageView mIvWelcome;

    private UrlDao mUrlDao;
    public List<UrlBean> list;

    /*private static final int GOTO_MAIN_ACTIVITY = 0;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    skip();
                    break;
                default:
                    break;
            }
        };
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {
        super.initData();

        mUrlDao = new UrlDao(this);
        //每次进店将客户余额设置为空
        list = mUrlDao.queryList();
        //        UserInfoUtils.setDomainName(LaunchActivity.this, urlBean.getDomain_name());
        //        mHandler.sendEmptyMessageAtTime(GOTO_MAIN_ACTIVITY, 6000);//3秒跳转
        skip();
    }

    private void skip() {
        if ((list == null || list.size() == 0) && UserInfoUtils.getDomainName(this).equals("0")) {
            startActivity(SystemConfigurationActivity.class);
        } else {
            if (list != null && list.size() > 0) {
                //添加数据库中的域名地址
                UserInfoUtils.setDomainName(LaunchActivity.this, list.get(0).getDomain_name());
            }

            if ("0".equals(UserInfoUtils.getLoginState(this))) {
                startActivity(LoginActivity.class);
            } else if ("1".equals(UserInfoUtils.getLoginState(this))) {
                //如果日期不等于今天日期也需要从新登录
                if (!UserInfoUtils.getLoginDate(this).equals(StringUtils.getDate2())) {
                    UserInfoUtils.setLoginState(this, "0");
                    //如果签到了店铺,首页刷新签到客户信息
                    UserInfoUtils.setUpdateShop(this,"1");
                    startActivity(LoginActivity.class);
                } else {
                    if (SystemUtil.getVersionCode(this) != UserInfoUtils.getNowVersion(this)) {
                        //如果版本号不和本地的相等直接跳转登录界面,并且记录当前版本号
                        UserInfoUtils.setNowVersion(this, SystemUtil.getVersionCode(this));
                        startActivity(LoginActivity.class);
                        finish();
                        return;
                    }

                    UserBean userBean = UserInfoUtils.getUser(this);
                    if (userBean.getSu_usertype().equals(BaseConfig.userTypeAdministrator)) {
                        startActivity(StewardActivity.class);
                    } else if (userBean.getSu_usertype().equals(BaseConfig.collectMan)) {
                        //                        startActivity(ShopInfoCollectActivity.class);
                        startActivity(ShopCollectMainActivity.class);
                    } else {
                        if (UserInfoUtils.getLock(this).equals("1")) {
                            Intent intent = new Intent(LaunchActivity.this, SetGestureActivity.class);
                            intent.putExtra("activityNum", 0);
                            startActivity(intent);
                        } else {
                            startActivity(MainActivity.class);
                        }
                    }
                }
            } else {
                if (SystemUtil.getVersionCode(this) != UserInfoUtils.getNowVersion(this)) {
                    //如果版本号不和本地的相等直接跳转登录界面,并且记录当前版本号
                    UserInfoUtils.setNowVersion(this, SystemUtil.getVersionCode(this));
                    startActivity(LoginActivity.class);
                    finish();
                    return;
                }

                UserBean userBean = UserInfoUtils.getUser(this);
                String userType;
                userType = userBean.getSu_usertype();

                if (BaseConfig.userTypeAdministrator.equals(userType)) {
                    startActivity(StewardActivity.class);
                } else if (BaseConfig.collectMan.equals(userType)) {
                    //                    startActivity(ShopInfoCollectActivity.class);
                    startActivity(ShopCollectMainActivity.class);
                } else {
                    UserBean userBean2 = UserInfoUtils.getUser(this);
                    if (userBean2.getSu_usertype().equals(BaseConfig.userTypeAdministrator)) {
                        startActivity(StewardActivity.class);
                    } else if (BaseConfig.collectMan.equals(userType)) {
                        //                        startActivity(ShopInfoCollectActivity.class);
                        startActivity(ShopCollectMainActivity.class);
                    } else {
                        startActivity(MainActivity.class);
                    }
                }
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrlDao.destroy();
    }

}
