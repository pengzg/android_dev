package com.xdjd.storebox.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.mob.MobSDK;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.GoodsBean;
import com.xdjd.storebox.bean.LoginBean;
import com.xdjd.storebox.bean.RecordsBean;
import com.xdjd.storebox.dao.RecordsDao;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.storebox.wxapi.LoginApi;
import com.xdjd.storebox.wxapi.OnLoginListener;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.NetUtils;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.tool.Tool;
import com.xdjd.utils.tool.UserInfo;
import com.xdjd.utils.tool.YesOrNoConnectWX;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.Select_showPassword;
import com.xdjd.view.popup.EditCartNumPopupWindow;
import com.xdjd.view.popup.RecordsPopup;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * Created by Administrator on 2016/11/18.
 */

public class LoginActivity extends BaseActivity implements RecordsPopup.ItemOnListener{
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.forgot_psd)
    LinearLayout forgotPsd;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.login_userTel)
    EditText loginUserTel;
    @BindView(R.id.login_userPwd)
    EditText loginUserPwd;
    @BindView(R.id.login_weixin)
    ImageView loginWeixin;
    @BindView(R.id.check_box1)
    CheckBox checkBox1;
    @BindView(R.id.login_userTel_del)
    LinearLayout mLoginUserTelDel;
    @BindView(R.id.login_pwd_del)
    LinearLayout loginPwdDel;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.ll_arrow)
    LinearLayout mLlArrow;
    @BindView(R.id.ll_recored)
    LinearLayout mLlRecored;

    private Platform pfWX;
    private String name;//第三方登录的名字
    private Platform pf;
    private String unionId;
    private RecordsDao recordDao;
    //记录历史账号popup
    private RecordsPopup mRecordsPopup;
    //历史账号list
    private List<RecordsBean> listRecord = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobSDK.init(getApplicationContext());
        pfWX = new Wechat();
        ButterKnife.bind(this);
        titleBar.setTitle("账户登录");
        titleBar.setRightText("设置");
        titleBar.setRightTextColor(R.color.black);
        titleBar.setRightTextDipSize(8,10);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SetupActivity.class);
            }
        });
        login.setSelected(true);
        loginUserTel.setSelection(loginUserTel.getText().length());//光标移动到文本行
        editListener(loginUserTel, mLoginUserTelDel);
        editListener(loginUserPwd, loginPwdDel);
        recordDao = new RecordsDao(this);
        listRecord = recordDao.getRecordsList();

        mLlRecored.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlRecored.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopup();
            }
        });
    }

    /*文本框获取焦点和文本内容监听*/
    private void editListener(final EditText editText, final LinearLayout linearLayout) {
        if (editText.getText().length() > 0) {
            linearLayout.setVisibility(View.INVISIBLE);
            editText.setSelection(editText.getText().length());
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    Log.e("change", "c");
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && editText.getText().length() > 0) {//获得焦点
                    linearLayout.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    Log.e("Focus", "F");
                } else {//失去焦点
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initPopup() {
        mRecordsPopup = new RecordsPopup(this, this, mLlRecored, new RecordsPopup.OnDismissPopListener() {
            @Override
            public void onDismiss() {
                ObjectAnimator visToInvis = ObjectAnimator.ofFloat(mIvArrow, "rotationX", 180f, 0f);
                visToInvis.setDuration(200);
                visToInvis.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {}
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mRecordsPopup.dismiss();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {}
                });
                visToInvis.start();
            }
        });
       mRecordsPopup.setData(listRecord);
    }

    private void showPopup() {
        mRecordsPopup.showAsDropDown(mLlRecored, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onItem(int i) {
        RecordsBean bean = listRecord.get(i);
        loginUserTel.setText(bean.getAccount());
        loginUserPwd.setText(bean.getPassword());
        loginUserTel.setSelection(bean.getAccount().length());
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

    @OnClick({R.id.forgot_psd, R.id.login, R.id.check_box1, R.id.login_userTel_del,
            R.id.login_weixin, R.id.login_pwd_del,R.id.ll_arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_psd://忘记密码
                Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.login://登录
                setLogin(loginUserTel.getText().toString(), loginUserPwd.getText().toString());
                break;
            case R.id.check_box1://显示密码
                Select_showPassword select_showPassword = new Select_showPassword();
                if (checkBox1.isChecked()) {
                    checkBox1.setChecked(true);
                    select_showPassword.showPassword(loginUserPwd);
                } else {
                    checkBox1.setChecked(false);
                    select_showPassword.hidePassword(loginUserPwd);
                }
                break;
            case R.id.login_userTel_del://删除账号输入框内容
                loginUserTel.setText("");
                break;
            case R.id.login_pwd_del:
                loginUserPwd.setText("");
                break;
            case R.id.login_weixin:
                Log.e("微信：","点击");
                name = pfWX.getName();
                if (!Tool.canGetUserInfo(pfWX)) {
                    showToast("请安装微信客户端");
                    return;
                }
                Thrid_login(name);
                break;
            case R.id.ll_arrow:
                if (listRecord != null && listRecord.size() > 0) {
                    showPopup();
                    ObjectAnimator invisToVis = ObjectAnimator.ofFloat(mIvArrow, "rotationX",
                            0f, 180f);
                    invisToVis.setDuration(300);
                    invisToVis.start();
                }
                break;
            default:
                break;
        }
    }

    //登录
    private void setLogin(final String userID, final String userPWD) {
        //网络是否可用
        if (!NetUtils.isConnected(this)) {
            showToast("您的网络不可用！");
            return;
        }
        //验证手机号
        if (!Validation.isPhoneNum(userID)) {
            showToast("手机号格式错误！");
            return;
        }
        //验证密码
        if (!Validation.isPassword(userPWD)) {
            showToast("请输入6-14位数字或字母组合！");
            return;
        }
        AsyncHttpUtil<LoginBean> httpUtil = new AsyncHttpUtil<>(this, LoginBean.class, new IUpdateUI<LoginBean>() {
            @Override
            public void updata(final LoginBean bean) {
                if (bean.getRepCode().equals("00")) {
                    //存储用户信息编码
                    /*UserInfoUtils.setId(LoginActivity.this, bean.getUser());
                    UserInfoUtils.setSpreadName(LoginActivity.this, bean.getSpread_name());
                    UserInfoUtils.setSpreadMobile(LoginActivity.this, bean.getSpreadMobile());
                    UserInfoUtils.setCenterShopName(LoginActivity.this, bean.getCenterShopName());
                    UserInfoUtils.setCenterShopId(LoginActivity.this, bean.getCenter_shopid());*/
                    //友盟统计,统计账号登录
                    //MobclickAgent.onProfileSignIn(bean.getUser());

                    /*if (!"".equals(bean.getUserShopId()) || bean.getUserShopId() != null) {
                        UserInfoUtils.setUserShopId(LoginActivity.this, bean.getUserShopId());
                    }
                    if (bean.getUnionId().toString().isEmpty()) {
                        YesOrNoConnectWX.ConnectFlag = 0;
                    } else {
                        YesOrNoConnectWX.ConnectFlag = 1;//绑定微信
                        Log.e("微信flag", String.valueOf(YesOrNoConnectWX.ConnectFlag));
                    }*/

                    if (bean.getListData() == null || bean.getListData().size() == 0){
                        showToast("您还没有关联的订货公司!");
                        return;
                    }

                    if (bean.getListData().size() == 1){
                        UserInfoUtils.setId(LoginActivity.this,bean.getListData().get(0).getUserId());//用户id
                        UserInfoUtils.setOrgPhone(LoginActivity.this,bean.getListData().get(0).getOrgid_mobile());//公司电话
                        UserInfoUtils.setStoreHouseId(LoginActivity.this,bean.getListData().get(0).getStorehouseid());//发货仓库id
                        UserInfoUtils.setLoginName(LoginActivity.this,userID);//登录账号
                        UserInfoUtils.setLoginPwd(LoginActivity.this,userPWD);//登录密码
                        UserInfoUtils.setCompanyId(LoginActivity.this,bean.getListData().get(0).getOrgid());//公司id
                        UserInfoUtils.setChangeCompanyFlag(LoginActivity.this,"1");
                        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finishLogin();
                    }else{
                        DialogUtil.showDialogList(LoginActivity.this, "选择进货公司", bean.getListData(), new DialogUtil.MyCustomDialogListener4() {
                            @Override
                            public void item(int i) {
                                UserInfoUtils.setId(LoginActivity.this,bean.getListData().get(i).getUserId());//用户id
                                UserInfoUtils.setOrgPhone(LoginActivity.this,bean.getListData().get(i).getOrgid_mobile());//公司电话
                                UserInfoUtils.setStoreHouseId(LoginActivity.this,bean.getListData().get(i).getStorehouseid());//发货仓库id
                                UserInfoUtils.setLoginName(LoginActivity.this,userID);//登录账号
                                UserInfoUtils.setLoginPwd(LoginActivity.this,userPWD);//登录密码
                                UserInfoUtils.setCompanyId(LoginActivity.this,bean.getListData().get(i).getOrgid());//公司id
                                UserInfoUtils.setChangeCompanyFlag(LoginActivity.this,"0");
                                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
                                finishLogin();
                            }
                        });
                    }

                } else {
                    String repMsg = bean.getRepMsg();
                    if (repMsg.length() > 10) {
                        DialogUtil.showSureDialog(LoginActivity.this, repMsg, "确  定", new DialogUtil.MyCustomDialogListener3() {
                            @Override
                            public void sure() {
                            }
                        });
                    } else {
                        showToast(repMsg);
                    }
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }
            @Override
            public void finish() {
                RecordsBean recordsBean = new RecordsBean();
                recordsBean.setAccount(loginUserTel.getText().toString());
                recordsBean.setPassword(loginUserPwd.getText().toString());

                if (!recordDao.isHasRecord(loginUserTel.getText().toString())) {
                    LogUtils.e("账号是否存在", "不存在");
                    recordDao.insert(recordsBean);
                } else {
                    LogUtils.e("账号是否存在", "存在");
                }
            }
        });
        httpUtil.post(M_Url.login, L_RequestParams.getLogin(this, userID, userPWD), true);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //        finish();
        //        System.exit(0);
    }


    //第三方登录
    private void Thrid_login(String platfromName) {
        LoginApi api = new LoginApi();
        api.setPlatform(platfromName);
        api.setOnLoginListener(new OnLoginListener() {
            @Override
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                String key = "unionid";
                unionId = res.get(key).toString();
                isPass(platform);
                return false;
            }
            @Override
            public boolean onRegister(com.xdjd.storebox.wxapi.UserInfo info) {
                return false;
            }
        });
        api.login(this);
    }

    /*微信登录接口*/
   private boolean isPass(String platform) {
        pf = ShareSDK.getPlatform(platform);
        AsyncHttpUtil<LoginBean> httpUtil = new AsyncHttpUtil<>(this, LoginBean.class, new IUpdateUI<LoginBean>() {
            @Override
            public void updata(final LoginBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    YesOrNoConnectWX.ConnectFlag = 1;
                    //*UserInfoUtils.setId(LoginActivity.this, bean.getUser());
                    //UserInfoUtils.setCenterShopId(LoginActivity.this, bean.getCenter_shopid());

                    //友盟统计,统计账号登录
                    //MobclickAgent.onProfileSignIn("WX",bean.getUser());

//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finishLogin();

                    if (bean.getListData().size() == 1){
                        UserInfoUtils.setId(LoginActivity.this,bean.getListData().get(0).getUserId());//用户id
                        UserInfoUtils.setOrgPhone(LoginActivity.this,bean.getListData().get(0).getOrgid_mobile());//公司电话
                        UserInfoUtils.setStoreHouseId(LoginActivity.this,bean.getListData().get(0).getStorehouseid());//发货仓库id
                        /*UserInfoUtils.setBsIsNegative(LoginActivity.this,bean.getListData().get(0).getBs_is_negative());//是否负库存
                        UserInfoUtils.setBsIsVirtual(LoginActivity.this,bean.getListData().get(0).getBs_is_virtual());//是否虚拟库存*/
                        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finishLogin();
                    }else{
                        DialogUtil.showDialogList(LoginActivity.this, "选择进货公司", bean.getListData(), new DialogUtil.MyCustomDialogListener4() {
                            @Override
                            public void item(int i) {
                                UserInfoUtils.setId(LoginActivity.this,bean.getListData().get(i).getUserId());//用户id
                                UserInfoUtils.setOrgPhone(LoginActivity.this,bean.getListData().get(i).getOrgid_mobile());//公司电话
                                UserInfoUtils.setStoreHouseId(LoginActivity.this,bean.getListData().get(0).getStorehouseid());//发货仓库id
                               /* UserInfoUtils.setBsIsNegative(LoginActivity.this,bean.getListData().get(0).getBs_is_negative());//是否负库存
                                UserInfoUtils.setBsIsVirtual(LoginActivity.this,bean.getListData().get(0).getBs_is_virtual());//是否虚拟库存*/
                                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
                                finishLogin();
                            }
                        });
                    }

                } else {
                    //跳注册界面
//                    YesOrNoConnectWX.ConnectFlag = 0;
//                    Intent intent = new Intent(LoginActivity.this, ZhuceActivity.class);
//                    intent.putExtra("unionid", unionId);//传unionid给注册界面
//                    intent.putExtra("flag", "1");
//                    startActivity(intent);
                    showToast("您还没有绑定相关账号!");
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
        httpUtil.post(M_Url.WeixinBinding, L_RequestParams.WeixinLogin(unionId), true);
        return true;
    }

    private void finishLogin() {
        finish();
    }

    private long exitTime;

    // 点击两次退出（2秒内）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                showToast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish_all();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finish_all() {
        AppManager.getInstance().finishAllActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordDao.destroy();
    }
}
