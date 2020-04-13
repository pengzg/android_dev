package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.mob.MobSDK;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.RecordsBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.bean.UserSignBean;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.distribution.dao.RecordsDao;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.RecordsPopup;
import com.xdjd.distribution.wxapi.LoginApi;
import com.xdjd.distribution.wxapi.OnLoginListener;
import com.xdjd.distribution.wxapi.Tool;
import com.xdjd.distribution.wxapi.UserInfo;
import com.xdjd.shopInfoCollect.main.ShopCollectMainActivity;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.EditTextUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.winningrecord.main.RecordMainActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LoginActivity extends BaseActivity implements RecordsPopup.ItemOnListener, LocationListener {

    @BindView(R.id.account_et)
    EditText mAccountEt;
    @BindView(R.id.password_et)
    EditText mPasswordEt;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.rl_account_del)
    RelativeLayout mRlAccountDel;
    @BindView(R.id.rl_password_del)
    RelativeLayout mRlPasswordDel;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.iv_eye)
    ImageView mIvEye;
    @BindView(R.id.ll_eye)
    LinearLayout mLlEye;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.ll_arrow)
    LinearLayout mLlArrow;
    @BindView(R.id.ll_account_right)
    LinearLayout mLlAccountRight;
    @BindView(R.id.login_weixin)
    ImageView mLoginWeixin;
    @BindView(R.id.forgot_pwd)
    LinearLayout forgotPwd;

    private LineDao lineDao;
    private RecordsDao recordDao;

    private AccountTextWatcher listenerAccount = new AccountTextWatcher();
    private PasswordTextWatcher listenerPassword = new PasswordTextWatcher();

    private static boolean isShowPassword = false;
    //记录历史账号popup
    private RecordsPopup mRecordsPopup;
    //历史账号list
    private List<RecordsBean> listRecord = null;

    //--------友盟推送代码--------
    PushAgent mPushAgent;

    private Platform pfWX;
    private String unionId;


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

    private int mCode = 1;//1是正常登录,2是微信登录

    private LocationService locationService;
    private MyLocationUtil locationUtil;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disProgressDialog(LoginActivity.this);
            if (msg.what == 1) {
                login(mAccountEt.getText().toString(), mPasswordEt.getText().toString());
            } else if (msg.what == 2) {
                Thrid_login(pfWX.getName());
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.setTitle("登  录");
        mTitleBar.setRightText("连接设置");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SystemActivity.class);
            }
        });

        MobSDK.init(getApplicationContext());
        pfWX = new Wechat();

        mAccountEt.setText(UserInfoUtils.getAccount(this));
        mAccountEt.setSelection(UserInfoUtils.getAccount(this).length());
        mPasswordEt.setText(UserInfoUtils.getPassword(this));
        mPasswordEt.setSelection(UserInfoUtils.getPassword(this).length());

        lineDao = new LineDao(this);
        recordDao = new RecordsDao(this);

        EditTextUtil.setEditTextInhibitInputSpace(mAccountEt);
        EditTextUtil.setEditTextInhibitInputSpace(mPasswordEt);

        mAccountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {//获得焦点
                    if (mAccountEt.getText().length() > 0) {
                        mRlAccountDel.setVisibility(View.VISIBLE);
                        mAccountEt.setSelection(mAccountEt.getText().length());
                    } else {
                        mRlAccountDel.setVisibility(View.GONE);
                    }
                    mAccountEt.addTextChangedListener(listenerAccount);
                } else {//失去焦点
                    mRlAccountDel.setVisibility(View.GONE);
                    mAccountEt.removeTextChangedListener(listenerAccount);
                }
            }
        });

        mPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {//获得焦点
                    if (mPasswordEt.getText().length() > 0) {
                        mRlPasswordDel.setVisibility(View.VISIBLE);
                        mPasswordEt.setSelection(mPasswordEt.getText().length());
                    } else {
                        mRlPasswordDel.setVisibility(View.GONE);
                    }
                    mPasswordEt.addTextChangedListener(listenerPassword);
                } else {//失去焦点
                    mRlPasswordDel.setVisibility(View.GONE);
                    mPasswordEt.removeTextChangedListener(listenerPassword);
                }
            }
        });

        mLlAccountRight.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlAccountRight.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopup();
            }
        });

        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);
        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    public void click(View v) {
        startActivity(RecordMainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lineDao.destroy();
        recordDao.destroy();
    }

    @Override
    public void onBackPressed() {
        AppManager.getInstance().finishAllActivity();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(getPackageName());

        //        super.onBackPressed();
    }

    @OnClick({R.id.rl_account_del, R.id.rl_password_del, R.id.login_btn, R.id.ll_main,
            R.id.ll_eye, R.id.ll_arrow, R.id.login_weixin, R.id.forgot_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_account_del:
                mAccountEt.setText("");
                break;
            case R.id.rl_password_del:
                mPasswordEt.setText("");
                break;
            case R.id.login_btn:
                final String loginName = mAccountEt.getText().toString();
                final String password = mPasswordEt.getText().toString();

                if (loginName == null || "".equals(loginName)) {
                    return;
                }

                if (password == null || "".equals(password)) {
                    return;
                }

                //请求位置权限
                PermissionUtils.requstActivityLocation(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        /*if (locationUtil != null && locationService != null) {
                            locationService.unregisterListener(locationUtil); //注销掉监听
                            locationService.stop();
                        }*/


                        if (locationService.client.isStarted()) {
                        } else {
                            if (locationUtil == null){
                                locationUtil = new MyLocationUtil();
                            }
                            locationService.registerListener(locationUtil);
                            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                        }

                        mCode = 1;//正常登录
                        showProgressDialog(LoginActivity.this);
                        locationService.registerListener(locationUtil);
                        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                        locationService.start();// 定位SDK
                    }

                    @Override
                    public void onDilogCancal() {
                    }
                });

                //                login(loginName, password);
                break;
            case R.id.ll_main:
                hideKeyboard();
                break;
            case R.id.ll_eye://显示隐藏密码
                isShowPassword = !isShowPassword;
                showPassword();
                break;
            case R.id.ll_arrow:
                try {
                    if (listRecord == null || listRecord.size() == 0) {
                        listRecord = recordDao.getRecordsList();
                    }
                } catch (Exception e) {
                    LogUtils.e("listRecord", e.toString());
                }

                if (listRecord != null && listRecord.size() > 0) {
                    showPopup();
                    ObjectAnimator invisToVis = ObjectAnimator.ofFloat(mIvArrow, "rotationX",
                            0f, 180f);
                    invisToVis.setDuration(300);
                    invisToVis.start();
                }
                break;
            case R.id.login_weixin:
                if (!Tool.canGetUserInfo(pfWX)) {
                    showToast("请安装微信客户端");
                    return;
                }
                mCode = 2;//微信登录
                showProgressDialog(this);
                locationService.registerListener(locationUtil);
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                locationService.start();// 定位SDK
                //                Thrid_login(pfWX.getName());
                break;
            case R.id.forgot_pwd:
                Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
                startActivityForResult(intent, Comon.FORGOT_PWD_REQUEST_CODE);
                break;
        }
    }

    private void showPassword() {
        if (isShowPassword) {
            mIvEye.setBackgroundResource(R.mipmap.i_login_eye_press);
            mPasswordEt.setInputType(0x90);
            mPasswordEt.setSelection(mPasswordEt.getText().length());
        } else {
            mIvEye.setBackgroundResource(R.mipmap.i_login_eye_normal);
            mPasswordEt.setInputType(0x81);
            mPasswordEt.setSelection(mPasswordEt.getText().length());
        }
    }

    private void login(final String loginName, final String password) {
        AsyncHttpUtil<UserBean> httpUtil = new AsyncHttpUtil<>(LoginActivity.this, UserBean.class, new IUpdateUI<UserBean>() {

            @Override
            public void updata(UserBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    //存储签名
                    UserInfoUtils.setSign(LoginActivity.this, bean.getSign());
                    //登录日期
                    UserInfoUtils.setLoginDate(LoginActivity.this, StringUtils.getDate2());
                    UserInfoUtils.setUnionId(LoginActivity.this, bean.getUnionId());

                    if (!loginName.equals(UserInfoUtils.getAccount(LoginActivity.this)) &&
                            !UserInfoUtils.getAccount(LoginActivity.this).equals("")) {
                        //重置登录客户信息
                        UserInfoUtils.setClientInfo(LoginActivity.this, null);
                    }

                    //将安全欠款和客户余额等字段重置
                    UserInfoUtils.setCustomerBalance(LoginActivity.this, null);
                    UserInfoUtils.setAfterAmount(LoginActivity.this, null);
                    UserInfoUtils.setSafetyArrearsNum(LoginActivity.this, null);

                    //设置默认签到距离
                    UserInfoUtils.setSignDistance(LoginActivity.this, bean.getSignDistance());

                    addUserSign(bean);
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
        httpUtil.post(M_Url.login, L_RequestParams.getLogin(loginName, password, longtitude, latitude, address), true);
    }

    /**
     * 客户签到
     *
     * @param bean
     */
    private void addUserSign(final UserBean bean) {
        AsyncHttpUtil<UserSignBean> httpUtil = new AsyncHttpUtil<>(LoginActivity.this, UserSignBean.class, new IUpdateUI<UserSignBean>() {

            @Override
            public void updata(UserSignBean jsonBean) {
                if (jsonBean!=null && "00".equals(jsonBean.getRepCode())) {
                    //签到id
                    UserInfoUtils.setCudId(LoginActivity.this, jsonBean.getCud_id());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                loginDateSet(bean);
            }
        });
        httpUtil.post(M_Url.addUserSign, L_RequestParams.addUserSign(bean.getUserId(), "", "", "1"), true);
    }

    /**
     * 登录参数设置
     *
     * @param bean
     */
    private void loginDateSet(UserBean bean) {
        RecordsBean recordsBean = new RecordsBean();
        recordsBean.setAccount(mAccountEt.getText().toString());
        recordsBean.setPassword(mPasswordEt.getText().toString());
        recordsBean.setLogin_time(StringUtils.getDate());

        if (!recordDao.isHasRecord(mAccountEt.getText().toString())) {
            recordDao.insert(recordsBean);
        } else {
            recordDao.update(recordsBean);
        }

        //只有业务人员才会绑定推送
        if (!bean.getSu_usertype().equals("5") && bean.getBud_id() != null && !"".equals(bean.getBud_id())) {
            mPushAgent = PushAgent.getInstance(this);
            mPushAgent.addAlias(bean.getBud_id(), BaseConfig.Alias_Type, new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean b, String s) {
                }
            });
        }

        UserInfoUtils.setLoginState(LoginActivity.this, "1");
        UserInfoUtils.setUser(LoginActivity.this, bean);//存储用户配置信息

//        if (bean.getLineList() != null && bean.getLineList().size() > 0) {
            //设置线路信息
//            lineDao.batchInsert(bean.getLineList());//添加线路信息
            //如果不是同一个用户,从新设置默认线路信息
            if (!UserInfoUtils.getId(this).equals(bean.getUserId())) {
                UserInfoUtils.setLineId(LoginActivity.this, "");
                UserInfoUtils.setLineName(LoginActivity.this, "");
                lineDao.deleteAllLine();
            } else {
                //LogUtils.e("lineSetting","是同一个账户");
                //如果是同一个账号登录,且线路以前没有设置过
//                if ("".equals(UserInfoUtils.getLineId(LoginActivity.this))) {
//                    UserInfoUtils.setLineId(LoginActivity.this, bean.getLineList().get(0).getBl_id());
//                    UserInfoUtils.setLineName(LoginActivity.this, bean.getLineList().get(0).getBl_name());
//                }
            }
//        }

        UserInfoUtils.setId(LoginActivity.this, bean.getUserId());//开放这个参数,有的地方会用到
        UserInfoUtils.setAccount(LoginActivity.this, mAccountEt.getText().toString());//账号
        UserInfoUtils.setPassword(LoginActivity.this, mPasswordEt.getText().toString());//密码

        UserInfoUtils.setOrgid(LoginActivity.this, bean.getOrgid());

        UserInfoUtils.setBudId(LoginActivity.this, bean.getBud_id());//员工id

        UserInfoUtils.setTicketmsgHead(LoginActivity.this, bean.getTicketmsg1());//打印表头信息
        UserInfoUtils.setTicketmsgBottom(LoginActivity.this, bean.getTicketmsg2());//打印表尾信息
        UserInfoUtils.setRefundMode(LoginActivity.this, bean.getRefundMode());

        showToast(bean.getRepMsg());

        if (BaseConfig.userTypeAdministrator.equals(bean.getSu_usertype())) {
            startActivity(StewardActivity.class);
        } else if (BaseConfig.collectMan.equals(bean.getSu_usertype())) {//采集员
            //            startActivity(ShopInfoCollectActivity.class);
            startActivity(ShopCollectMainActivity.class);
        } else {
            startActivity(MainActivity.class);
        }
        finishActivity();
    }

    //第三方登录
    private void Thrid_login(String platfromName) {
        Log.e("login:", "yyy");
        LoginApi api = new LoginApi();
        api.setPlatform(platfromName);
        api.setOnLoginListener(new OnLoginListener() {
            @Override
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                //请求接口
                Log.e("微信参数", platform + "******" + res.toString());
                String key = "unionid";
                unionId = res.get(key).toString();//键值对获取值
                Log.e("unionid", res.get(key).toString());
                isPass();
                return false;
            }

            @Override
            public boolean onRegister(UserInfo info) {
                return false;
            }
        });
        api.login(this);
    }

    /*微信登录接口*/
    private boolean isPass() {
        AsyncHttpUtil<UserBean> httpUtil = new AsyncHttpUtil<>(this, UserBean.class, new IUpdateUI<UserBean>() {
            @Override
            public void updata(final UserBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    //存储签名
                    UserInfoUtils.setSign(LoginActivity.this, bean.getSign());
                    //登录日期
                    UserInfoUtils.setLoginDate(LoginActivity.this, StringUtils.getDate2());
                    UserInfoUtils.setUnionId(LoginActivity.this, bean.getUnionId());

                    if (!mAccountEt.getText().toString().equals(UserInfoUtils.getAccount(LoginActivity.this)) &&
                            !UserInfoUtils.getAccount(LoginActivity.this).equals("")) {
                        //重置登录客户信息
                        UserInfoUtils.setClientInfo(LoginActivity.this, null);
                    }
                    //将安全欠款和客户余额等字段重置
                    UserInfoUtils.setCustomerBalance(LoginActivity.this, null);
                    UserInfoUtils.setAfterAmount(LoginActivity.this, null);
                    UserInfoUtils.setSafetyArrearsNum(LoginActivity.this, null);
                    //设置默认签到距离
                    UserInfoUtils.setSignDistance(LoginActivity.this, bean.getSignDistance());

                    addUserSign(bean);
                } else {
                    DialogUtil.showCustomDialog(LoginActivity.this, "提示",
                            "您还没有绑定账号,请先登录账号后在系统设置中进行绑定微信!", "确定", null, null);
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
        httpUtil.post(M_Url.thirdPartyLogin, L_RequestParams.thirdPartyLogin(unionId), true);
        return true;
    }

    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();
        mHandler.sendEmptyMessage(mCode);
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop();
    }

    @Override
    public void locationError(final BDLocation location) {
        mHandler.sendEmptyMessage(mCode);
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop();
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public class AccountTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                mRlAccountDel.setVisibility(View.VISIBLE);
            } else {
                mRlAccountDel.setVisibility(View.GONE);
            }
        }
    }

    public class PasswordTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                mRlPasswordDel.setVisibility(View.VISIBLE);
            } else {
                mRlPasswordDel.setVisibility(View.GONE);
            }
        }
    }

    private void initPopup() {
        mRecordsPopup = new RecordsPopup(this, this, mLlAccountRight, new RecordsPopup.OnDismissPopListener() {
            @Override
            public void onDismiss() {
                ObjectAnimator visToInvis = ObjectAnimator.ofFloat(mIvArrow, "rotationX", 180f, 0f);
                visToInvis.setDuration(200);
                visToInvis.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mRecordsPopup.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                visToInvis.start();
            }
        });
    }

    private void showPopup() {
        mRecordsPopup.setData(listRecord);
        mRecordsPopup.showAsDropDown(mLlAccountRight, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onItem(int i) {
        RecordsBean bean = listRecord.get(i);
        mAccountEt.setText(bean.getAccount());
        mPasswordEt.setText(bean.getPassword());

        mAccountEt.setSelection(bean.getAccount().length());

        mRecordsPopup.dismiss();
    }

    @Override
    public void onItemDel(final int i) {
        DialogUtil.showCustomDialog(this, "提示", "是否删除这条账号的记录?", "删除", "取消", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                recordDao.delete(listRecord.get(i).getId());
                listRecord.remove(i);
                mRecordsPopup.setData(listRecord);

                if (listRecord == null || listRecord.size() == 0) {
                    mRecordsPopup.dismiss();
                }
            }

            @Override
            public void no() {
            }
        });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
