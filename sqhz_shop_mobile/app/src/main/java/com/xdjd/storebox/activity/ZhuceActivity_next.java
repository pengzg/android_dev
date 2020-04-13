package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.base.BaseConfig;
import com.xdjd.storebox.bean.RegisterInfoBean;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.operation.Register_Params;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.Select_showPassword;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/18.
 */

public class ZhuceActivity_next extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    Register_Params bean;
    @BindView(R.id.new_register_pwd)
    EditText newRegisterPwd;

    @BindView(R.id.new_register_smscode)
    EditText newRegisterSmscode;
    @BindView(R.id.zc_login)
    Button zcLogin;
    @BindView(R.id.view_userTel)
    TextView viewUserTel;
    @BindView(R.id.check_box2)
    CheckBox checkBox2;
    @BindView(R.id.get_smscode)
    CheckBox getSmscode;
    @BindView(R.id.password_del)
    LinearLayout passwordDel;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppManager.getInstance().addActivity(this);
        setContentView(R.layout.new_user_register_next);
        ButterKnife.bind(this);
        titleBar.setTitle("注册");
        titleBar.leftBack(this);
        zcLogin.setSelected(true);
        bean = (Register_Params) getIntent().getSerializableExtra("bean");//序列化接口
        viewUserTel.setText(bean.getUserTel());//得到注册输入手机号
        editListener(newRegisterPwd, passwordDel);
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

    @OnClick({R.id.zc_login, R.id.check_box2, R.id.get_smscode, R.id.password_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_box2:
                Select_showPassword select_showPassword = new Select_showPassword();
                if (checkBox2.isChecked()) {
                    checkBox2.setChecked(true);
                    select_showPassword.showPassword(newRegisterPwd);
                    //select_showPassword.hidePassword(newRegisterPwd);
                } else {
                    checkBox2.setChecked(false);
                    select_showPassword.hidePassword(newRegisterPwd);
                    //select_showPassword.showPassword(newRegisterPwd);
                }
                break;
            case R.id.zc_login:
                //判断是否为空
                bean.setPassword(newRegisterPwd.getText().toString());
                bean.setSmscode(newRegisterSmscode.getText().toString());
                setRegister(bean);//注册
                break;
            case R.id.get_smscode:
                //获取注册验证码
                MyCount mc = new MyCount(30000, 1000);
                mc.start();
                if (bean.getUserId().equals(" "))
                    setMsmCode_agin(bean.getUserTel(), "1", "3");
                else
                    setMsmCode_agin(bean.getUserTel(), "8", "3");
                break;
            case R.id.password_del:
                newRegisterPwd.setText("");
                break;
            default:
                break;
        }
    }

    //注册
    private void setRegister(Register_Params registerParams) {
        //判断密码
        if (!Validation.isPassword(newRegisterPwd.getText().toString())) {
            showToast("请输入6-14位数字或字母组合！");
            /*DialogUtil.showCustomDialog(this, "请输入6-14位数字或字母组合!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }//判断验证码
        if (newRegisterSmscode.getText().toString().isEmpty()) {
            showToast("请输入验证码！");
            /*DialogUtil.showCustomDialog(this, "请输入验证码!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }
        AsyncHttpUtil<RegisterInfoBean> httpUtil = new AsyncHttpUtil<>(this, RegisterInfoBean.class, new IUpdateUI<RegisterInfoBean>() {

            public void updata(RegisterInfoBean bean) {
                showToast(bean.getRepMsg());
                if ("00".equals(bean.getRepCode())) {
                    //Log.e("USERID", bean.getUser());
                    if ("1".equals(bean.getFlag())) {//首页
                        UserInfoUtils.setId(ZhuceActivity_next.this, bean.getUser());
                        //友盟统计,统计账号登录
                        MobclickAgent.onProfileSignIn(bean.getUser());

                        Intent intent = new Intent(ZhuceActivity_next.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        AppManager.getInstance().finishAllActivity();
                        //ActivityCollector.finishAll();
                    } else {//审核页面

                        if (!"".equals(bean.getUser())) {
                            PushAgent mPushAgent = PushAgent.getInstance(ZhuceActivity_next.this);
                            //添加友盟推送的Alias（支持一个alias对应多个devicetoken）：
                            mPushAgent.addAlias(UserInfoUtils.getId(ZhuceActivity_next.this), BaseConfig.Alias_Type, new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean b, String s) {
                                }
                            });
                        }

                        Intent intent = new Intent(ZhuceActivity_next.this, ZhuceActivity_succeed.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }

            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            public void finish() {
            }
        });
        httpUtil.post(M_Url.Register, L_RequestParams.getRegister(this, bean), true);
    }

    /* 定义一个倒计时的内部类 */
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //getSmscode.setBackgroundResource(R.drawable.background_code );
            //getSmscode.setSelected(false);
            getSmscode.setEnabled(true);
            getSmscode.setText("获取验证码");
            //getSmscode.setClickable(true);
            getSmscode.setChecked(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getSmscode.setChecked(true);
            getSmscode.setEnabled(false);
            getSmscode.setText("(" + millisUntilFinished / 1000 + ")秒后重试");
            //getSmscode.setBackgroundResource(R.drawable.background );
        }
    }

    //请求重发验证码
    private void setMsmCode_agin(String msmTel, String msmType, String registerType) {
        //判断手机号格式
        if (!Validation.isPhoneNum(msmTel)) {
            showToast("手机号格式错误！");
            /*DialogUtil.showCustomDialog(this, "手机号格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }

        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                //Log.e("tag",s);
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    //Log.e("tag", obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        showToast(obj.getString("repMsg"));
                        Log.e("tag", obj.getString("repMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "异常");
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
        httpUtil.post(M_Url.Getmsmcode, L_RequestParams.getMsmcode(this, msmTel, msmType, registerType), true);

    }

}
