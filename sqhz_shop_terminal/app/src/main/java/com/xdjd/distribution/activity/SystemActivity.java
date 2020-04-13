package com.xdjd.distribution.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.MobSDK;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.UrlBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.dao.UrlDao;
import com.xdjd.distribution.popup.SelectUrlPopup;
import com.xdjd.distribution.wxapi.LoginApi;
import com.xdjd.distribution.wxapi.OnLoginListener;
import com.xdjd.distribution.wxapi.Tool;
import com.xdjd.distribution.wxapi.UserInfo;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by lijipei on 2017/5/30.
 */

public class SystemActivity extends BaseActivity implements SelectUrlPopup.ItemOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tb_is_select)
    ToggleButton mTbIsSelect;
    @BindView(R.id.tv_url)
    TextView mTvUrl;
    @BindView(R.id.rl_url)
    RelativeLayout mRlUrl;
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;
    @BindView(R.id.tv_ip)
    TextView mTvIp;
    @BindView(R.id.ll_change_password)
    LinearLayout mLlChangePassword;
    @BindView(R.id.ll_setup_gesture)
    LinearLayout mLlSetupGesture;
    @BindView(R.id.lockStatus)
    TextView lockStatus;
    @BindView(R.id.ll_bind_wx)
    LinearLayout mLlBindWx;
    @BindView(R.id.tv_wx_bind)
    TextView mTvWxBind;
    @BindView(R.id.ll_switch_account)
    LinearLayout mLlSwitchAccount;

    private UrlDao mUrlDao;
    public List<UrlBean> list;
    private UrlBean bean = new UrlBean();

    private SelectUrlPopup mUrlPopup;

    private Platform pfWX;
    private String unionId = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_system;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserInfoUtils.getLock(this).equals("1")) {
            lockStatus.setText("已开启");
        } else {
            lockStatus.setText("未开启");
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("系统");

        MobSDK.init(getApplicationContext());
        pfWX = new Wechat();

        mUrlDao = new UrlDao(this);
        list = mUrlDao.queryList();
        if (list != null && list.size() > 0) {
            bean = list.get(0);
        } else {
            bean = new UrlBean();
        }

        mTvUrl.setText(bean.getDomain_name() == null ? UserInfoUtils.getDomainName(this) : bean.getDomain_name());
        mTvVersionName.setText("v" + SystemUtil.getVersion(this));
        mTvIp.setText(bean.getIp());

        if (UserInfoUtils.getIsAutomaticIp(this)) {
            mRlUrl.setVisibility(View.GONE);
            mTbIsSelect.setToggleOn();
        } else {
            mRlUrl.setVisibility(View.VISIBLE);
            mTbIsSelect.setToggleOff();
        }

        mTbIsSelect.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    mRlUrl.setVisibility(View.GONE);
                    UserInfoUtils.setIsAutomaticIp(SystemActivity.this, true);
                } else {
                    mRlUrl.setVisibility(View.VISIBLE);
                    UserInfoUtils.setIsAutomaticIp(SystemActivity.this, false);
                }
            }
        });

        if (UserInfoUtils.getLoginState(this).equals("0")) {
            mLlChangePassword.setVisibility(View.GONE);
            mLlSetupGesture.setVisibility(View.GONE);
            mLlSwitchAccount.setVisibility(View.GONE);
            //            mLlBindWx.setVisibility(View.GONE);
        } else {
            mLlChangePassword.setVisibility(View.VISIBLE);
            mLlSetupGesture.setVisibility(View.VISIBLE);
            mLlSwitchAccount.setVisibility(View.VISIBLE);
            //            mLlBindWx.setVisibility(View.VISIBLE);

           /* if (UserInfoUtils.getUnionId(this) == null || "".equals(UserInfoUtils.getUnionId(this))){
                mTvWxBind.setText("绑定微信");
            }else{
                mTvWxBind.setText("已绑定");
            }*/
        }

        initPopup();
    }

    @OnClick({R.id.rl_url, R.id.ll_setup_gesture, R.id.ll_bind_wx,R.id.ll_switch_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_url:
                //                showPopup();
                DialogUtil.showCustomDialog(this, "提示", "是否重置域名地址?重置域名会先退出程序,再次启动程序时才能进行修改域名!", "确定", "取消",
                        new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {

                                UserBean userBean = UserInfoUtils.getUser(SystemActivity.this);
                                if (userBean != null && !userBean.getSu_usertype().equals("5")
                                        && userBean.getBud_id() != null && !"".equals(userBean.getBud_id())) {

                                    PushAgent mPushAgent = PushAgent.getInstance(SystemActivity.this);
                                    //退出时解除别名的绑定
                                    mPushAgent.removeAlias(UserInfoUtils.getBudId(SystemActivity.this), BaseConfig.Alias_Type,
                                            new UTrack.ICallBack() {
                                                @Override
                                                public void onMessage(boolean isSuccess, String s) {
                                                }
                                            });
                                }

                                UserInfoUtils.clearAll(SystemActivity.this);
                                UserInfoUtils.setDomainName(SystemActivity.this, "0");
                                mUrlDao.delete();

                                LogUtils.e("域名", mUrlDao.queryList().size() + "--");
                                AppManager.getInstance().finishAllActivity();

                                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                am.killBackgroundProcesses(getPackageName());
                                AppManager.getInstance().finishAllActivity();
                                //终止当前正在运行的Java虚拟机，导致程序终止
                                System.exit(0);

                                //                        Intent intent = new Intent(SystemActivity.this,SystemConfigurationActivity.class);
                                //                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //                        startActivity(intent);
                                //                        finish();
                            }

                            @Override
                            public void no() {

                            }
                        });
                break;
            case R.id.ll_setup_gesture:
                startActivity(GestureLockActivity.class);
                break;
            case R.id.ll_bind_wx://绑定解绑微信
                String bind_type = "";// Y	1绑定 2解绑
                if (UserInfoUtils.getUnionId(this) == null || "".equals(UserInfoUtils.getUnionId(this))) {
                    //如果没有unionId
                    bind_type = "1";
                    ConnectWX(bind_type);
                } else {
                    bind_type = "2";
                    DialogUtil.showCustomDialog(this, "提示", "是否解除微信绑定?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            isPass("2");
                        }

                        @Override
                        public void no() {

                        }
                    });
                }
                break;
            case R.id.ll_switch_account://切换账号

                ClientBean clientBean = UserInfoUtils.getClientInfo(this);
                UserBean userBean = UserInfoUtils.getUser(this);
                String userType;
                userType = userBean.getSu_usertype();
                if(clientBean!=null && !BaseConfig.userTypeAdministrator.equals(userType)){
                    DialogUtil.showCustomDialog(this, "提示", "请离店后 再切换账号!", "确定", null,null);
                    return;
                }

                DialogUtil.showCustomDialog(this, "提示", "是否切换账号", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        AppManager.getInstance().finishAllActivity();
                        UserInfoUtils.setLoginState(SystemActivity.this,"0");
                        Intent intent = new Intent(SystemActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(LoginActivity.class);
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    /*绑定微信*/
    private void ConnectWX(final String bind_type) {
        if (!Tool.canGetUserInfo(pfWX)) {
            showToast("请安装微信客户端");
            return;
        }
        LoginApi api = new LoginApi();
        api.setPlatform(pfWX.getName());
        api.setOnLoginListener(new OnLoginListener() {
            @Override
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                String key = "unionid";
                unionId = res.get(key).toString();
                isPass(bind_type);
                return false;
            }

            @Override
            public boolean onRegister(UserInfo info) {
                return false;
            }
        });
        api.login(this);

    }

    private void isPass(final String bind_type) {
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    //                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        if ("1".equals(bind_type)) {
                            //如果没有unionId
                            UserInfoUtils.setUnionId(SystemActivity.this, unionId);
                            mTvWxBind.setText("已绑定");
                            showToast("绑定成功!");
                        } else {
                            UserInfoUtils.setUnionId(SystemActivity.this, "");
                            mTvWxBind.setText("绑定微信");
                            showToast("解绑成功!");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

        httpUtil.post(M_Url.bindWx, L_RequestParams.bindWx(bind_type, unionId), true);
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
        mTvIp.setText(list.get(i).getIp());
        bean = list.get(i);
        mUrlPopup.dismiss();
    }

    /**
     * 修改密码
     *
     * @param v
     */
    public void changePassword(View v) {
        startActivity(ChangePasswordActivity.class);
    }
}
