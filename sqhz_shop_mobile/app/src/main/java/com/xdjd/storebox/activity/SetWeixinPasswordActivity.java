package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.Select_showPassword;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/8.
 */

public class SetWeixinPasswordActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.weixin_mobile)
    EditText weixinMobile;
    @BindView(R.id.weixin_smscode)
    EditText weixinSmscode;
    @BindView(R.id.weixin_get_smscode)
    CheckBox weixinGetSmscode;
    @BindView(R.id.weixin_new_pwd)
    EditText weixinNewPwd;
    @BindView(R.id.weixin_check_box)
    CheckBox weixinCheckBox;
    @BindView(R.id.weixin_ok)
    Button weixinOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_weixin_pwd);
        ButterKnife.bind(this);
        titleBar.setTitle("设置微信登录密码");
        titleBar.leftBack(this);
        weixinOk.setSelected(true);
    }

    @OnClick({R.id.weixin_get_smscode, R.id.weixin_check_box, R.id.weixin_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weixin_get_smscode:/*获取验证码*/
                if(Validation.isPhoneNum(weixinMobile.getText().toString())) {
                    GetWeinxinSmsCode(weixinMobile.getText().toString());
                }else{
                    showToast("手机号输入格式有误！");
                    weixinGetSmscode.setChecked(false);
                }

                break;
            case R.id.weixin_check_box:/*是否显示密码*/
                Select_showPassword select_showPassword = new Select_showPassword();
                if (weixinCheckBox.isChecked()) {
                    weixinCheckBox.setChecked(true);
                    select_showPassword.showPassword(weixinNewPwd);
                    //select_showPassword.hidePassword(weixinNewPwd);
                } else {
                    weixinCheckBox.setChecked(false);
                    select_showPassword.hidePassword(weixinNewPwd);
                    //select_showPassword.showPassword(weixinNewPwd);
                }
                break;
            case R.id.weixin_ok:
                SetWeixinLoginPassword(weixinMobile.getText().toString(),weixinSmscode.getText().toString(),weixinNewPwd.getText().toString());
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
            weixinGetSmscode.setEnabled(true);
            weixinGetSmscode.setText("获取验证码");
            weixinGetSmscode.setChecked(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            weixinGetSmscode.setChecked(true);
            weixinGetSmscode.setEnabled(false);
            weixinGetSmscode.setText("(" + millisUntilFinished / 1000 + ")秒后重试");
        }
    }

    /*微信登录密码设置*/
    private void SetWeixinLoginPassword(String phoneNum,String smsCode,String newPassword){
        /*手机号输入验证*/
        if(!Validation.isPhoneNum(phoneNum)){
            showToast("手机号格式错误！");
           /* DialogUtil.showCustomDialog(this, "手机号格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }
        /*验证码输入验证*/
        if(smsCode.length() < 4){
            showToast("验证码格式错误！");
            /*DialogUtil.showCustomDialog(this, "验证码格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }
        /*新密码输入验证*/
        if(newPassword.length() < 6 || newPassword.length() > 14){
            showToast("新密码格式错误！");
            /*DialogUtil.showCustomDialog(this, "密码为空或输入格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
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
                org.json.JSONObject obj;
                try {
                    obj = new org.json.JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        showToast(obj.getString("repMsg"));
                        UserInfoUtils.setId(SetWeixinPasswordActivity.this ,obj.getString("userId"));
                        Intent intent = new Intent(SetWeixinPasswordActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish_weixin();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e("tag", "异常");
                }
            }
            @Override
            public void sendFail(ExceptionType s) { showToast(s.getDetail());}
            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.SetWinxinLoginPassword, L_RequestParams.SetWeixinLoginPwd(phoneNum,smsCode,newPassword),true);
    }

    /*获取短信验证码*/
    private void GetWeinxinSmsCode(String phoneNum){
        if(!Validation.isPhoneNum(phoneNum)){
            DialogUtil.showCustomDialog(this, "手机号格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });

            return;
        }
        MyCount myCount = new MyCount(30000, 1000);
        myCount.start();
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                org.json.JSONObject obj;
                try {
                    obj = new org.json.JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    Log.e("tag", obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
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
        httpUtil.post(M_Url.Getmsmcode, L_RequestParams.getMsmcode(this,phoneNum,"7","3"), true);
    }
    private void finish_weixin(){
        finish();
    }
}
