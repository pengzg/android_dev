package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.CustomTitleView;
import com.xdjd.view.EaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ForgotPwdActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    @BindView(R.id.forgot_userTel)
    EditText forgotUserTel;
    @BindView(R.id.forgot_imagecode)
    EditText forgotImagecode;
    @BindView(R.id._password_custom)
    CustomTitleView PasswordCustom;
    @BindView(R.id.forgot_psd_next)
    Button forgotPsdNext;
    @BindView(R.id.mobile_del)
    LinearLayout mobileDel;

    private int editStart;
    private int editEnd;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passwrod);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("找回密码");
        forgotPsdNext.setEnabled(false);
        if (forgotUserTel .getText().length() > 0) {
            mobileDel.setVisibility(View.VISIBLE);
        } else {
          mobileDel.setVisibility(View.GONE);
        }
        forgotUserTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                   mobileDel.setVisibility(View.VISIBLE);
                } else {
                   mobileDel.setVisibility(View.GONE);
                }
            }
        });
        forgotUserTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b &&  forgotUserTel.getText().length() > 0) {//获得焦点
                    mobileDel.setVisibility(View.VISIBLE);
                    //editText.setSelection(editText.getText().length());
                    Log.e("Focus","F");
                } else {//失去焦点
                    mobileDel.setVisibility(View.GONE);
                }
            }
        });
        forgotImagecode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 4) {
                    forgotPsdNext.setEnabled(true);
                    forgotPsdNext.setSelected(true);
                } else {
                    forgotPsdNext.setEnabled(false);
                    forgotPsdNext.setSelected(false);
                }
            }
        });
    }

    @OnClick({R.id.forgot_psd_next,R.id.mobile_del})
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.forgot_psd_next:
            CheckInputForgotInfo(forgotUserTel.getText().toString(), forgotImagecode.getText().toString());break;
            case R.id.mobile_del:forgotUserTel.setText("");break;
        }
    }

    /*忘记密码手机号验证码判断*/
    private void CheckInputForgotInfo(String phoneNum, String checkCode) {
        //判断手机号
        if (!Validation.isPhoneNum(phoneNum)) {
            showToast("手机号格式错误！");
            return;
        }
        /*判断验证码输入*/
        if (!(PasswordCustom.mTitleText.equalsIgnoreCase(checkCode))) {
            showToast("验证码输入有误！");
            return;
        }
        /*手机号是否为有效手机号*/
        //接口请求，成功，跳转页面
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    if (obj.getString("repCode").equals("00")) {
                        Intent intent = new Intent(ForgotPwdActivity.this, ForgotPwdActivity_next.class);
                        intent.putExtra("Tel", forgotUserTel.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        showToast(obj.getString("repMsg"));
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
        httpUtil.post(M_Url.CheckPhoneNum, L_RequestParams.CheckForgotPhoneNum(phoneNum), true);
    }
}
