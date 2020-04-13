package com.xdjd.shopInfoCollect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.ChangePasswordActivity;
import com.xdjd.distribution.activity.LoginActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/9/29.
 */

public class SetUpActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.exit)
    TextView exit;
    @BindView(R.id.ll_modify_pwd)
    LinearLayout llModifyPwd;

    @Override
    protected int getContentView() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initData() {
        super.initData();
        titleBar.leftBack(this);
        titleBar.setTitle("设置");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.exit,R.id.ll_modify_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_modify_pwd:
                Intent intent = new Intent(SetUpActivity.this,ChangePasswordActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.exit:
                DialogUtil.showCustomDialog(this, "确定退出登录?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        //--------友盟推送代码--------
                        PushAgent mPushAgent = PushAgent.getInstance(SetUpActivity.this);
                        //退出时解除别名的绑定
                        mPushAgent.removeAlias(UserInfoUtils.getId(SetUpActivity.this), BaseConfig.Alias_Type,
                                new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String s) {
                                    }
                                });
                        //删除所有标签
                        mPushAgent.getTagManager().reset(new TagManager.TCallBack() {
                            @Override
                            public void onMessage(boolean b, ITagManager.Result result) {

                            }
                        });

                        AppManager.getInstance().finishAllActivity();
                        UserInfoUtils.setId(SetUpActivity.this, "0");
                        UserInfoUtils.setLoginState(SetUpActivity.this, "0");
                        Intent intent3 = new Intent(SetUpActivity.this, LoginActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        //finish_info();
                        finish();
                    }

                    @Override
                    public void no() {
                    }
                });
                break;
        }
    }
}
