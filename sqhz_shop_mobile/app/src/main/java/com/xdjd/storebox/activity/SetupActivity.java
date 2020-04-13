package com.xdjd.storebox.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.UrlBean;
import com.xdjd.storebox.dao.UrlDao;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.cache.CacheTools;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.ToggleButton;
import com.xdjd.view.popup.SelectUrlPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestylehong on 2016/12/20.
 */

public class SetupActivity extends BaseActivity implements SelectUrlPopup.ItemOnListener{
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.about_box)
    LinearLayout aboutBox;
    @BindView(R.id.image2)
    ToggleButton image2;
    @BindView(R.id.clear_cache)
    LinearLayout clearCache;
    @BindView(R.id.cache_num)
    TextView cacheNum;
    @BindView(R.id.lock_l)
    LinearLayout lockL;
    @BindView(R.id.lock_status)
    TextView lockStatus;
    @BindView(R.id.tb_is_select)
    ToggleButton mTbIsSelect;
    @BindView(R.id.tv_url)
    TextView mTvUrl;
    @BindView(R.id.rl_url)
    RelativeLayout mRlUrl;
    @BindView(R.id.ipLine)
    View mIpLine;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private UrlDao mUrlDao;
    public List<UrlBean> list;
    private UrlBean bean = new UrlBean();

    private SelectUrlPopup mUrlPopup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        titleBar.setTitle("设置");
        titleBar.leftBack(this);
        cacheNum.setText(CacheTools.getHttpCacheSize(SetupActivity.this));
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if (UserInfoUtils.getWifi(this)) {
            image2.setToggleOn();
        } else {
            image2.setToggleOff();
        }
        image2.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (!UserInfoUtils.getWifi(SetupActivity.this)) {
                    image2.setToggleOn();
                    UserInfoUtils.setWifi(SetupActivity.this, true);
                } else {
                    image2.setToggleOff();
                    UserInfoUtils.setWifi(SetupActivity.this, false);
                }
            }
        });

        mUrlDao = new UrlDao(this);
        list = mUrlDao.queryList();
        if (list != null && list.size() > 0) {
            bean = list.get(0);
        } else {
            bean = new UrlBean();
        }
        mTvUrl.setText(bean.getDomain_name() == null ? UserInfoUtils.getDomainName(this) : bean.getDomain_name());
        if (UserInfoUtils.getIsAutomaticIp(this)) {
            mRlUrl.setVisibility(View.GONE);
            mTbIsSelect.setToggleOn();
            mIpLine.setVisibility(View.VISIBLE);
        } else {
            mRlUrl.setVisibility(View.VISIBLE);
            mTbIsSelect.setToggleOff();
            mIpLine.setVisibility(View.GONE);
        }

        mTbIsSelect.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    mRlUrl.setVisibility(View.GONE);
                    UserInfoUtils.setIsAutomaticIp(SetupActivity.this, true);
                    mIpLine.setVisibility(View.VISIBLE);
                } else {
                    mRlUrl.setVisibility(View.VISIBLE);
                    UserInfoUtils.setIsAutomaticIp(SetupActivity.this, false);
                    mIpLine.setVisibility(View.GONE);
                }
            }
        });
        initPopup();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(preferences.getString("gesture", ""))) {
            if (UserInfoUtils.getLock(SetupActivity.this).equals("1"))
                lockStatus.setText("已开启");
            else {
                lockStatus.setText("未开启");
            }
        } else {
            lockStatus.setText("未开启");
        }
    }

    Runnable myRun = new Runnable() {
        @Override
        public void run() {
            CacheTools.clearAppCache(SetupActivity.this);
            Message message = Message.obtain();
            message.what = 100;
            handler.sendMessage(message);
        }
    };

    @OnClick({R.id.clear_cache, R.id.about_box, R.id.lock_l,R.id.rl_url})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_cache:
                if (cacheNum.getText().toString().equalsIgnoreCase("0KB")) {
                    showToast("清除完成");
                } else {
                    DialogUtil.showCustomDialog(this, "清除缓存", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            new Thread(myRun).start();
                        }

                        @Override
                        public void no() {
                        }
                    });
                }
                break;
            case R.id.about_box:
                startActivity(AboutCommunityBoxActivity.class);
                break;
            case R.id.lock_l:
                Intent intent = new Intent(SetupActivity.this, LockMainActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_url:
                DialogUtil.showCustomDialog(this, "提示", "是否重置域名地址?重置域名会先退出程序,再次启动程序时才能进行修改域名!", "确定", "取消",
                        new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                UserInfoUtils.clearAll(SetupActivity.this);
                                UserInfoUtils.setDomainName(SetupActivity.this, "0");
                                mUrlDao.delete();

                                LogUtils.e("域名", mUrlDao.queryList().size() + "--");
                                AppManager.getInstance().finishAllActivity();

                                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                am.killBackgroundProcesses(getPackageName());
                                AppManager.getInstance().finishAllActivity();
                                //终止当前正在运行的Java虚拟机，导致程序终止
                                System.exit(0);
                            }
                            @Override
                            public void no() {}
                        });
                break;
        }
    }

    private void initPopup() {
        mUrlPopup = new SelectUrlPopup(this, this);
        mUrlPopup.setData(list);
    }

    private void showPopup() {
        mUrlPopup.showAsDropDown(mRlUrl, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrlDao.destroy();
    }

    @Override
    public void onItem(int i) {
        mTvUrl.setText(list.get(i).getDomain_name());
        //mTvIp.setText(list.get(i).getIp());
        bean = list.get(i);
        mUrlPopup.dismiss();
    }

    private void finish_setup() {
        finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showToast("清除缓存成功");
            cacheNum.setText(CacheTools.getHttpCacheSize(SetupActivity.this));
        }
    };

}
