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
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ChangePhoneNumActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.modify_phoneNum)
    EditText modifyPhoneNum;
    @BindView(R.id.get_smscode)
    Button getSmscode;
    @BindView(R.id.input_smscode)
    EditText inputSmscode;
    @BindView(R.id.save_new_phonNum)
    Button saveNewPhonNum;
    @BindView(R.id.mobile_del)
    LinearLayout mobileDel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_num);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("修改手机");
        if (modifyPhoneNum.getText().length() > 0) {
            mobileDel.setVisibility(View.VISIBLE);
        } else {
            mobileDel.setVisibility(View.GONE);
        }
        modifyPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mobileDel.setVisibility(View.VISIBLE);
                } else {
                    mobileDel.setVisibility(View.GONE);
                }
            }
        });
        modifyPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b &&  modifyPhoneNum.getText().length() > 0) {//获得焦点
                    mobileDel.setVisibility(View.VISIBLE);
                    Log.e("Focus","F");
                } else {//失去焦点
                    mobileDel.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.modify_phoneNum, R.id.get_smscode, R.id.input_smscode, R.id.save_new_phonNum,R.id.mobile_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_phoneNum:
                break;
            case R.id.get_smscode://修改手机号获取验证码
                UserInfoGetSmscode(modifyPhoneNum.getText().toString(), "4", "3");//类型手机
                break;
            case R.id.input_smscode:
                break;
            case R.id.save_new_phonNum://修改用户手机号
                ModifyPhoneNum(UserInfoUtils.getId(this), modifyPhoneNum.getText().toString(), inputSmscode.getText().toString());
                break;
            case R.id.mobile_del:
                modifyPhoneNum.setText("");break;
            default :break;
        }
    }

    /*获取验证码*/
    private void UserInfoGetSmscode(String msmTel, String msmType, String registerType) {
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
                        //Log.e("tag", obj.getString("repMsg"));
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

    /*修改手机号*/
    private void ModifyPhoneNum(String uid, String phoneNum, String smscode) {
        if (!Validation.isPhoneNum(phoneNum)) {
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
        if (smscode.length() < 4) {
            showToast("验证码格式错误！");
            /*DialogUtil.showCustomDialog(this, "验证码输入格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
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
                    Log.e("tag", obj.getString("repMsg"));

                    if (obj.getString("repCode").equals("00")) {
                        Intent intent = new Intent(ChangePhoneNumActivity.this, PersonInfoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
        httpUtil.post(M_Url.ModifyMobile, L_RequestParams.ModifyMobile(uid, phoneNum, smscode), true);
    }
}
