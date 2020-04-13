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

public class ChangePersonNameActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.save_nickname)
    Button saveNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_personname);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("修改昵称");
        Intent intent = getIntent();
        nickname.setText(intent.getStringExtra("nickName"));
        nickname.setSelection(nickname.getText().length());
    }

    @OnClick({R.id.nickname, R.id.save_nickname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nickname:
                break;
            case R.id.save_nickname:
                ModifyNickName(UserInfoUtils.getId(this),nickname.getText().toString());
                break;
        }
    }
    /*修改昵称*/
    private void ModifyNickName(String uid,String nickName){
        if(nickName.equals("")){
            showToast("昵称为空！");
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
                        Intent intent = new Intent(ChangePersonNameActivity.this,PersonInfoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        showToast(obj.getString("repMsg"));
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
            public void finish() {}
        });
        httpUtil.post(M_Url.ModifyuserInfo , L_RequestParams.ModifyUserInfo(uid,nickName," "," "), true);
    }
}
