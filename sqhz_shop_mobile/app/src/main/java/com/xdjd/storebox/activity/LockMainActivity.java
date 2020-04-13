package com.xdjd.storebox.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LockMainActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.switch_btn)
    CheckBox  switchBtn;
    @BindView(R.id.modify_btn)
    TextView modifyBtn;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_main);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("设置密码锁");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();


        if (TextUtils.isEmpty(preferences.getString("gesture",""))){
            modifyBtn.setText("设置密码");
        }else{
            modifyBtn.setText("修改密码");
        }
        if(UserInfoUtils.getLock(LockMainActivity.this).equals("0")){
            switchBtn.setSelected(false);
            modifyBtn.setVisibility(View.GONE);
        } else {
            switchBtn.setSelected(true);
            modifyBtn.setVisibility(View.VISIBLE);
        }

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    UserInfoUtils.setLock(LockMainActivity.this,"0");
                    switchBtn.setSelected(false) ;
                }else{
                    UserInfoUtils.setLock(LockMainActivity.this,"1");
                    switchBtn.setSelected(true) ;
                    modifyBtn.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @OnClick({R.id.modify_btn,R.id.switch_btn})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.modify_btn:
                editor.remove("gesture");//修改时都要清空缓存
                editor.commit();
                Intent intent = new Intent(LockMainActivity.this, SetGestureActivity.class);
                intent.putExtra("activityNum", 1);
                startActivity(intent);break;
            case R.id.switch_btn :
                if(switchBtn.isChecked()){
                    modifyBtn.setVisibility(View.VISIBLE);

                }else{
                    modifyBtn.setVisibility(View.GONE);
                }
                break;
        }

    }


}
