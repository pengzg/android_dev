package com.xdjd.storebox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.LoginBean;
import com.xdjd.storebox.bean.ResetPwdBean;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.Select_showPassword;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ForgotPwdActivity_next extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.forgot_smscode)
    EditText forgotSmscode;
    @BindView(R.id.forgot_new_pwd)
    EditText forgotNewPwd;
    @BindView(R.id.check_box3)
    CheckBox checkBox3;
    @BindView(R.id.reset_ok)
    Button resetOk;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.fotgot_get_smscode)
    CheckBox fotgotGetSmscode;
    private String userTel;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_next);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("找回密码");
        resetOk.setSelected(true);
        Intent intent = getIntent();
        userTel = intent.getStringExtra("Tel");
        phone.setText(userTel);
    }

    /*找回密码，新密码设置请求*/
    private void setForgot_pwd_next(Context context, final String userTel, String msmCode, String newCode) {
        //验证码和密码判断
        if (forgotNewPwd.getText().length() < 6 || forgotNewPwd.getText().length() > 14) {
            showToast("密码长度不足！");
            return;
        }
        AsyncHttpUtil<LoginBean> httpUtil = new AsyncHttpUtil<LoginBean>(this, LoginBean.class, new IUpdateUI<LoginBean>() {
            @Override
            public void updata(LoginBean bean) {
                if(bean.getRepCode().equals("00")){
                    bean.setMobilePhone(userTel);
                    bean.setPassword(forgotNewPwd.getText().toString());
                    Intent intent = new Intent(ForgotPwdActivity_next.this,ResetPwdsuceedActivity.class);
                    intent.putExtra("bean",bean);
                    startActivity(intent);
                }else{
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
        httpUtil.post(M_Url.ForgotPwd, L_RequestParams.getForgotPwd(this,UserInfoUtils.getId(this), newCode, msmCode, userTel), true);
    }

    @OnClick({R.id.check_box3, R.id.reset_ok,R.id.fotgot_get_smscode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_box3:
                Select_showPassword select_showPassword = new Select_showPassword();
                if (checkBox3.isChecked()) {
                    checkBox3.setChecked(true);
                    select_showPassword.showPassword(forgotNewPwd);
                } else {
                    checkBox3.setChecked(false);
                    select_showPassword.hidePassword(forgotNewPwd);
                }
                break;
            case R.id.fotgot_get_smscode://获取验证码
                setForgot_pwd(ForgotPwdActivity_next.this,userTel);
                MyCount mc = new MyCount(30000, 1000);
                mc.start();
                break;
            case R.id.reset_ok:
                Log.e("forgotTel", userTel);
                setForgot_pwd_next(this, userTel, forgotSmscode.getText().toString(), forgotNewPwd.getText().toString());
                break;
        }
    }
    /* 定义一个倒计时的内部类 */
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            fotgotGetSmscode.setEnabled(true);
            fotgotGetSmscode.setText("获取验证码");
            fotgotGetSmscode.setChecked(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            fotgotGetSmscode.setChecked(true);
            fotgotGetSmscode.setEnabled(false);
            fotgotGetSmscode.setText("(" + millisUntilFinished / 1000 + ")秒后重试");
        }
    }


    /*获取验证码*/
    private void setForgot_pwd(Context context, final String userTel) {
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    Log.e("tag", obj.getString("repMsg"));
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
        httpUtil.post(M_Url.Getmsmcode, L_RequestParams.getMsmcode(this, userTel, "3", "3"), true);
    }

}
