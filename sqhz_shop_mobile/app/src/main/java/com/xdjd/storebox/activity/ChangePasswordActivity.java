package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.utils.UserInfoUtils;
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

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.old_password)
    EditText oldPassword;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.save_new_password)
    Button saveNewPassword;
    private String old_pwd;
    private String new_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("修改密码");
    }

    @OnClick({R.id.old_password, R.id.new_password, R.id.save_new_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.old_password:
                break;
            case R.id.new_password:
                break;
            case R.id.save_new_password:
                old_pwd = oldPassword.getText().toString();
                new_pwd = newPassword.getText().toString();
                ModifyPassword(UserInfoUtils.getId(ChangePasswordActivity.this),new_pwd,old_pwd);
                break;
        }
    }

//  用户修改密码
    private void ModifyPassword(String uid,String newPwd,String oldPwd){
        if(oldPwd.length() < 6 || oldPwd.length() > 14){
            showToast("原始密码格式输入错误！");
            /*DialogUtil.showCustomDialog(this, "原始密码输入格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }
        if(newPwd.length() < 6 || newPwd.length() > 14){
            showToast("新密码格式输入错误！");
            /*DialogUtil.showCustomDialog(this, "新密码输入格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }
        AsyncHttpUtil<String>httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    Log.e("tag", obj.getString("repMsg"));

                    if (obj.getString("repCode").equals("00")) {
                        showToast(obj.getString("repMsg"));
                        Intent intent = new Intent(ChangePasswordActivity.this,PersonInfoActivity.class);
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
        httpUtil.post(M_Url.ModifyPwd,L_RequestParams.modifyPassword(uid,newPwd,oldPwd),true);

    }
}
