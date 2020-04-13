package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.WinningCodeBean;
import com.xdjd.storebox.event.WinningCodeEvent;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class WinningCodeEditActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_winning_code)
    EditText mEtWinningCode;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_code_edit);
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("编辑核销密码");
    }

    private void setUpHxPwd() {
        if (TextUtils.isEmpty(mEtWinningCode.getText())){
            showToast("请输入核销密码!");
            return;
        }
        if (mEtWinningCode.getText().length() < 6){
            showToast("核销密码必须等于或大于6位数");
            return;
        }
        AsyncHttpUtil<WinningCodeBean> httpUtil = new AsyncHttpUtil<>(this, WinningCodeBean.class, new IUpdateUI<WinningCodeBean>() {
            @Override
            public void updata(WinningCodeBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(WinningCodeEditActivity.this,jsonStr.getRepMsg());
                    EventBus.getDefault().post(new WinningCodeEvent(mEtWinningCode.getText().toString()));
                    finishActivity();
                } else {
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.setUpHxPwd, L_RequestParams.setUpHxPwd(UserInfoUtils.getId(this),mEtWinningCode.getText().toString()), true);
    }

    @OnClick({R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                setUpHxPwd();
                break;
        }
    }
}
