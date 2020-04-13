package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ForGotPwdBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/8/24.
 */

public class ForgotPwdActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.forgot_userTel)
    EditText forgotUserTel;
    @BindView(R.id.sms_code)
    EditText smsCode;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.mobile_del)
    LinearLayout mobileDel;
    @BindView(R.id.sms_btn)
    TextView smsBtn;
    @BindView(R.id.enter_btn)
    Button enterBtn;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.ll_eye)
    LinearLayout llEye;

    private static boolean isShowPassword = false;
    @Override
    protected int getContentView() {
        return R.layout.forgot_passwrod;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBar.leftBack(this);
        titleBar.setTitle("找回密码");
        smsBtn.setEnabled(false);
        editListener(forgotUserTel, mobileDel);
    }

    /*文本框获取焦点和文本内容监听*/
    private void editListener(final EditText editText, final LinearLayout linearLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    if (Validation.isPhoneNum(forgotUserTel.getText().toString())) {
                        enableSmsBtn();
                    } else {
                        disableSmsBtn();
                    }
                } else {
                    linearLayout.setVisibility(View.GONE);
                    disableSmsBtn();
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

    @OnClick({R.id.mobile_del, R.id.sms_btn, R.id.enter_btn,R.id.ll_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mobile_del:
                forgotUserTel.setText("");
                break;
            case R.id.sms_btn:
                if(!Validation.isPhoneNum(forgotUserTel.getText().toString())){
                    showToast("手机号格式错误！");
                    return;
                }else{
                    getSmsCode();
                }
                break;
            case R.id.enter_btn:
                resetPassword(forgotUserTel.getText().toString(), smsCode.getText().toString(), newPassword.getText().toString());
                break;
            case R.id.ll_eye:
                isShowPassword = !isShowPassword;
                showPassword();
                break;
        }
    }

    private void showPassword() {
        if (isShowPassword) {
            ivEye.setBackgroundResource(R.mipmap.i_login_eye_press);
            newPassword.setInputType(0x90);
            newPassword.setSelection(newPassword.getText().length());
        } else {
            ivEye.setBackgroundResource(R.mipmap.i_login_eye_normal);
            newPassword.setInputType(0x81);
            newPassword.setSelection(newPassword.getText().length());
        }
    }
    private void enableSmsBtn(){
        smsBtn.setText("获取验证码");
        smsBtn.setTextColor(UIUtils.getColor(R.color.color_blue));
        smsBtn.setEnabled(true);
    }

    private void disableSmsBtn(){
        smsBtn.setEnabled(false);
        smsBtn.setText("获取验证码");
        smsBtn.setTextColor(UIUtils.getColor(R.color.line_afafaf));
    }

    class MyCounter extends CountDownTimer {
        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            smsBtn.setText("(" + millisUntilFinished / 1000 + ")" + "秒后重试");
            smsBtn.setTextColor(UIUtils.getColor(R.color.line_afafaf));
            smsBtn.setEnabled(false);
        }

        @Override
        public void onFinish() {
            if(Validation.isPhoneNum(forgotUserTel.getText().toString())){
                enableSmsBtn();
            }else{
               disableSmsBtn();
            }
        }
    }

    /*获取验证码*/
    private void getSmsCode() {
        if(!Validation.isPhoneNum(forgotUserTel.getText().toString())){
            showToast("手机号格式错误！");
            return;
        }
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(ForgotPwdActivity.this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean bean) {
                if (bean.getRepCode().equals("00")) {
                    MyCounter mc = new MyCounter(30000, 1000);
                    mc.start();
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
        httpUtil.post(M_Url.sendSms, L_RequestParams.sendSms(ForgotPwdActivity.this, "",
                forgotUserTel.getText().toString(),"2"), false);
    }

    /*重置密码*/
    private void resetPassword(String mobile, String sms_code, String new_pwd) {
        AsyncHttpUtil<ForGotPwdBean> httpUtil = new AsyncHttpUtil<>(ForgotPwdActivity.this, ForGotPwdBean.class, new IUpdateUI<ForGotPwdBean>() {
            @Override
            public void updata(ForGotPwdBean bean) {
                if (bean.getRepCode().equals("00")) {
                    showToast("重置密码成功！");
                    Intent intent = new Intent(ForgotPwdActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.resetPwd, L_RequestParams.resetPwd(mobile, sms_code, new_pwd), false);
    }
}
