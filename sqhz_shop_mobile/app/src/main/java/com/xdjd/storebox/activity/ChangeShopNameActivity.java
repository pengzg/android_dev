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

public class ChangeShopNameActivity extends BaseActivity {
    @BindView(R.id.shop_name)
    EditText shopName;
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.save_shopname)
    Button saveShopname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_shopname);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("修改店铺名称");
        Intent intent = getIntent();
        shopName.setText(intent.getStringExtra("shopName"));
        shopName.setSelection(shopName.getText().length());
    }

    @OnClick({R.id.shop_name, R.id.save_shopname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_name :
                break;
            case R.id.save_shopname:
                ModifyShopName(UserInfoUtils.getId(this),shopName.getText().toString());

                break;
        }
    }
    /*修改店铺名称*/
    private void ModifyShopName(String uid,String shopName){
        /*判断输入为空*/
        if(shopName.equals("")){
            showToast("店铺名为空！");
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
                        showToast(obj.getString("repMsg"));
                        Intent intent = new Intent(ChangeShopNameActivity.this,PersonInfoActivity.class);
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
            public void finish() {}
        });
        httpUtil.post(M_Url.ModifyuserInfo , L_RequestParams.ModifyUserInfo(uid," "," ",shopName), true);
    }
}
