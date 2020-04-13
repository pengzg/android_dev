package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/18.
 */

public class ZhuceActivity_succeed extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.zc_enter)
    Button zcEnter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppManager.getInstance().addActivity(this);
        setContentView(R.layout.new_user_register_succeed);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("注册成功");
        zcEnter.setSelected(true);
    }

    @OnClick(R.id.zc_enter)
    public void onClick() {
        Intent intent = new Intent(ZhuceActivity_succeed.this,LoginActivity.class) ;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
