package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.shopInfoCollect.main.ShopCollectMainActivity;
import com.xdjd.utils.EditTextUtil;
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
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.rl_old_del)
    RelativeLayout mRlOldDel;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.rl_new_del)
    RelativeLayout mRlNewDel;
    @BindView(R.id.et_again_password)
    EditText mEtAgainPassword;
    @BindView(R.id.rl_again_del)
    RelativeLayout mRlAgainDel;
    @BindView(R.id.btn_sure)
    Button mBtnSure;

    private MyTextWatcher listenerAccount = new MyTextWatcher();
    private Integer type;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("修改密码");
        type = getIntent().getIntExtra("type",0);
        EditTextUtil.setEditTextInhibitInputSpace(mEtOldPassword);
        EditTextUtil.setEditTextInhibitInputSpace(mEtNewPassword);
        EditTextUtil.setEditTextInhibitInputSpace(mEtAgainPassword);

        setImgClearEdit(mEtOldPassword, mRlOldDel);
        setImgClearEdit(mEtNewPassword, mRlNewDel);
        setImgClearEdit(mEtAgainPassword, mRlAgainDel);
    }

    private void changePassword() {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    /*if(type == 0){
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ChangePasswordActivity.this, ShopCollectMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }*/
                    finishActivity();
                }
                showToast(jsonBean.getRepMsg());
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.updatePwd, L_RequestParams.updatePwd(mEtOldPassword.getText().toString(), mEtNewPassword.getText().toString()), true);
    }

    private void setImgClearEdit(final EditText et, final View v) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {//获得焦点
                    if (et.getText().length() > 0) {
                        v.setVisibility(View.VISIBLE);
                        et.setSelection(et.getText().length());
                    } else {
                        v.setVisibility(View.GONE);
                    }
                    listenerAccount.setRlView(v);
                    et.addTextChangedListener(listenerAccount);
                } else {//失去焦点
                    v.setVisibility(View.GONE);
                    et.removeTextChangedListener(listenerAccount);
                }
            }
        });
    }

    @OnClick({R.id.rl_old_del, R.id.rl_new_del, R.id.rl_again_del, R.id.btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_old_del:
                mEtOldPassword.setText("");
                break;
            case R.id.rl_new_del:
                mEtNewPassword.setText("");
                break;
            case R.id.rl_again_del:
                mEtAgainPassword.setText("");
                break;
            case R.id.btn_sure:
                String oldPwd = mEtOldPassword.getText().toString();
                String newPwd = mEtNewPassword.getText().toString();
                String new_pwdAgain = mEtAgainPassword.getText().toString();

                if (TextUtils.isEmpty(mEtOldPassword.getText())) {
                    showToast("请输入旧密码!");
                    return;
                }

                if (oldPwd.length() < 6 || oldPwd.length() > 14) {
                    showToast("旧密码格式错误,请输入6-14位数字或字母！");
                    return;
                }

                if (TextUtils.isEmpty(mEtOldPassword.getText())) {
                    showToast("请输入新密码!");
                    return;
                }

                if (newPwd.length() < 6 || newPwd.length() > 14) {
                    showToast("新oldPwd密码格式错误,请输入6-14位数字或字母！");
                    return;
                }

                if (TextUtils.isEmpty(mEtAgainPassword.getText())) {
                    showToast("请再次输入密码!");
                    return;
                }

                if (!newPwd.equals(new_pwdAgain)) {
                    showToast("两次输入密码不一致！");
                    return;
                }
                changePassword();
                break;
        }
    }

    public class MyTextWatcher implements TextWatcher {

        private View view;

        public void setRlView(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

}
