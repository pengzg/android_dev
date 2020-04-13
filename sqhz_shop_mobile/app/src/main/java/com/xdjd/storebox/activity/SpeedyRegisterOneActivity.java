package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.BaseBean;
import com.xdjd.utils.EditUtil;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 快速注册第一步
 * Created by lijipei on 2017/2/10.
 */

public class SpeedyRegisterOneActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.speedy_register_Tel)
    EditText mSpeedyRegisterTel;
    @BindView(R.id.phone_del)
    LinearLayout mPhoneDel;
    @BindView(R.id.speedy_register_invite)
    EditText mSpeedyRegisterInvite;
    @BindView(R.id.invite_del)
    LinearLayout mInviteDel;
    @BindView(R.id.speedy_zc_next)
    Button mSpeedyZcNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedy_register_one);
        ButterKnife.bind(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("快速注册");
        initData();
    }

    protected void initData() {
        mSpeedyZcNext.setSelected(true);
        EditUtil.editListener(mSpeedyRegisterTel, mPhoneDel);
        EditUtil.editListener(mSpeedyRegisterInvite, mInviteDel);
    }


    @OnClick({R.id.phone_del, R.id.invite_del, R.id.speedy_zc_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_del:
                mSpeedyRegisterTel.setText("");
                break;
            case R.id.invite_del:
                mSpeedyRegisterInvite.setText("");
                break;
            case R.id.speedy_zc_next:
                speedyRegisterNext();
                break;
        }
    }

    private void speedyRegisterNext(){

        if (mSpeedyRegisterTel.getText().toString().equals("")) {
            showToast("请输入手机号");
            return;
        }
        if (!Validation.isPhoneNum(mSpeedyRegisterTel.getText().toString())) {
            showToast("手机号格式错误");
            return;
        }
        //判断邀请码
        if (mSpeedyRegisterInvite.getText().toString().isEmpty()) {
            showToast("请输入邀请码！");
            return;
        }

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean bean) {
                if ("00".equals(bean.getRepCode())){
                    Intent intent = new Intent(SpeedyRegisterOneActivity.this,SpeedyRegisterTwoActivity.class);
                    intent.putExtra("tel",mSpeedyRegisterTel.getText().toString());
                    intent.putExtra("invite",mSpeedyRegisterInvite.getText().toString());
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
        httpUtil.post(M_Url.checkMobileAndSpread, L_RequestParams.
                checkMobileAndSpread(mSpeedyRegisterTel.getText().toString(),mSpeedyRegisterInvite.getText().toString()),true);
    }
}
