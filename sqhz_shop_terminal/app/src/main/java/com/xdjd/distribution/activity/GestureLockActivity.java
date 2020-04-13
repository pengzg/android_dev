package com.xdjd.distribution.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.ToggleButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/8/7.
 */

public class GestureLockActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.btn_on_off)
    ToggleButton btnOnOff;
    @BindView(R.id.ll_on_off)
    LinearLayout llOnOff;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    Boolean flag;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected int getContentView() {
        return R.layout.activity_system_setup_gesture;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CollectorActivity.addActivity(this);
    }

    @Override
    protected void initData() {
        super.initData();
        titleBar.leftBack(this);
        titleBar.setTitle("设置密码");
        btnEdit.setVisibility(View.INVISIBLE);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        if(UserInfoUtils.getLock(this).equals("0")){
            btnOnOff.setToggleOff();
            btnEdit.setVisibility(View.GONE);
            flag = false;
        }else{
            btnOnOff.setToggleOn();
            btnEdit.setVisibility(View.VISIBLE);
            flag = true;
            //btnEdit.setText("修改密码");
        }
        llOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @OnClick({R.id.title_bar, R.id.btn_on_off, R.id.ll_on_off, R.id.btn_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar:
                break;
            case R.id.btn_on_off:
                Log.e("lockValue", UserInfoUtils.getLock(this));
                if(!flag){
                    btnOnOff.setToggleOn();
                    btnEdit.setVisibility(View.VISIBLE);
                    flag = true;
                }else{
                    btnOnOff.setToggleOff();
                    btnEdit.setVisibility(View.GONE);
                    flag = false;
                    UserInfoUtils.setLock(this,"0");
                }
                break;
            case R.id.ll_on_off:

                break;
            case R.id.btn_edit:
                editor.remove("gesture");//修改时都要清空缓存
                editor.commit();
                Intent intent = new Intent(GestureLockActivity.this,SetGestureActivity.class);
                intent.putExtra("activityNum",1);
                startActivity(intent);
                break;
        }
    }
}
