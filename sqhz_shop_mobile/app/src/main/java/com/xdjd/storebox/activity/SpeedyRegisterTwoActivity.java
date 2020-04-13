package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.LoginBean;
import com.xdjd.storebox.main.MainActivity;
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
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeedyRegisterTwoActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.view_userTel)
    TextView mViewUserTel;
    @BindView(R.id.new_register_pwd)
    EditText mNewRegisterPwd;
    @BindView(R.id.password_del)
    LinearLayout mPasswordDel;
    @BindView(R.id.check_box2)
    CheckBox mCheckBox2;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.new_register_smscode)
    EditText mNewRegisterSmscode;
    @BindView(R.id.get_smscode)
    CheckBox mGetSmscode;
    @BindView(R.id.zc_login)
    Button mZcLogin;
    @BindView(R.id.activity_speedy_register)
    LinearLayout mActivitySpeedyRegister;


    private String tel; //手机号
    private String invite;//邀请码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedy_register_two);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData() {
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpeedyRegisterTwoActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        mTitleBar.setTitle("快速注册");

        tel = getIntent().getStringExtra("tel");
        invite = getIntent().getStringExtra("invite");

        mViewUserTel.setText(tel);
        mZcLogin.setSelected(true);

        editListener(mNewRegisterPwd, mPasswordDel);
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

    @OnClick({R.id.password_del, R.id.check_box2, R.id.get_smscode, R.id.zc_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_del:
                mNewRegisterPwd.setText("");
                break;
            case R.id.check_box2:
                Select_showPassword select_showPassword = new Select_showPassword();
                if (mCheckBox2.isChecked()) {
                    mCheckBox2.setChecked(true);
                    select_showPassword.showPassword(mNewRegisterPwd);
                } else {
                    mCheckBox2.setChecked(false);
                    select_showPassword.hidePassword(mNewRegisterPwd);
                }
                break;
            case R.id.get_smscode:
                //获取注册验证码
                setMsmCode_agin("1", "3");
                break;
            case R.id.zc_login:
                requestSpeedyRegister();
                break;
        }
    }

    /**
     * 请求快速注册接口
     */
    private void requestSpeedyRegister() {
        //判断密码
        if (!Validation.isPassword(mNewRegisterPwd.getText().toString())) {
            showToast("请输入6-14位数字或字母组合！");
            return;
        }//判断验证码
        if (mNewRegisterSmscode.getText().toString().isEmpty()) {
            showToast("请输入验证码！");
            return;
        }

        AsyncHttpUtil<LoginBean> httpUtil = new AsyncHttpUtil<>(this, LoginBean.class, new IUpdateUI<LoginBean>() {
            @Override
            public void updata(LoginBean bean) {
                if ("00".equals(bean.getRepCode())){
                    UserInfoUtils.setId(SpeedyRegisterTwoActivity.this,bean.getUserId());

                    //友盟统计,统计账号登录
                    MobclickAgent.onProfileSignIn(bean.getUserId());

                    Intent intent = new Intent(SpeedyRegisterTwoActivity.this,MainActivity.class);
                    intent.putExtra("isSpeedy",true);
                    startActivity(intent);
                }else{
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.quickReg, L_RequestParams.getQuickReg(tel,
                mNewRegisterSmscode.getText().toString(), mNewRegisterPwd.getText().toString(), invite), true);
    }

    //请求重发验证码
    private void setMsmCode_agin(String msmType, String registerType) {
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        MyCount mc = new MyCount(30000, 1000);
                        mc.start();

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
        httpUtil.post(M_Url.Getmsmcode, L_RequestParams.getMsmcode(this, tel, msmType, registerType), true);

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
            mGetSmscode.setEnabled(true);
            mGetSmscode.setText("获取验证码");
            //getSmscode.setClickable(true);
            mGetSmscode.setChecked(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetSmscode.setChecked(true);
            mGetSmscode.setEnabled(false);
            mGetSmscode.setText("(" + millisUntilFinished / 1000 + ")秒后重试");
            //getSmscode.setBackgroundResource(R.drawable.background );
        }
    }
}
