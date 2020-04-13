package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/13
 *     desc   : 设置中奖码activity
 *     version: 1.0
 * </pre>
 */

public class WinningCodeActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_winning_password)
    TextView mTvWinningPassword;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_code);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
    }

    private void initData() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("核销密码");

        loadData();
    }

    private void loadData() {
        AsyncHttpUtil<WinningCodeBean> httpUtil = new AsyncHttpUtil<>(this, WinningCodeBean.class, new IUpdateUI<WinningCodeBean>() {
            @Override
            public void updata(WinningCodeBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getMmu_pwd() == null || jsonStr.getMmu_pwd().length() == 0){
                        mBtnEdit.setText("添加核销密码");
                    }else{
                        mTvWinningPassword.setText(jsonStr.getMmu_pwd());
                        mBtnEdit.setText("编辑核销密码");
                    }

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
        httpUtil.post(M_Url.queryHxPwd, L_RequestParams.queryHxPwd(UserInfoUtils.getId(this)), true);
    }

    @OnClick({R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                startActivity(WinningCodeEditActivity.class);
                break;
        }
    }

    public void onEventMainThread(WinningCodeEvent event) {
        if (event.getWinningCode() == null || event.getWinningCode().length() == 0){
            mBtnEdit.setText("添加核销密码");
        }else{
            mTvWinningPassword.setText(event.getWinningCode());
            mBtnEdit.setText("编辑核销密码");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
