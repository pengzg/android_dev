package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.LoginBean;
import com.xdjd.storebox.bean.ResetPwdBean;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ResetPwdsuceedActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.reset_ok)
    Button resetOk;
    private LoginBean bean;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_passwrod_succeed);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("重置成功");
        resetOk.setSelected(true);
        bean = (LoginBean) getIntent().getSerializableExtra("bean");
    }

    @OnClick(R.id.reset_ok)
    public void onClick() {
        if(bean.getListData().size() == 1){
            UserInfoUtils.setId(ResetPwdsuceedActivity.this,bean.getListData().get(0).getUserId());//用户id
            UserInfoUtils.setOrgPhone(ResetPwdsuceedActivity.this,bean.getListData().get(0).getOrgid_mobile());//公司电话
            UserInfoUtils.setStoreHouseId(ResetPwdsuceedActivity.this,bean.getListData().get(0).getStorehouseid());//发货仓库id
            UserInfoUtils.setLoginName(ResetPwdsuceedActivity.this,bean.getMobilePhone());//登录账号
            UserInfoUtils.setLoginPwd(ResetPwdsuceedActivity.this,bean.getPassword());//登录密码
            UserInfoUtils.setCompanyId(ResetPwdsuceedActivity.this,bean.getListData().get(0).getOrgid());//公司id
            UserInfoUtils.setChangeCompanyFlag(ResetPwdsuceedActivity.this,"1");
            Intent intent = new Intent(ResetPwdsuceedActivity.this ,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);//重置成功返回主页界面
            finish_reset();
        }else{
            DialogUtil.showDialogList(ResetPwdsuceedActivity.this, "选择进货公司", bean.getListData(), new DialogUtil.MyCustomDialogListener4() {
                @Override
                public void item(int i) {
                    UserInfoUtils.setId(ResetPwdsuceedActivity.this,bean.getListData().get(i).getUserId());//用户id
                    UserInfoUtils.setOrgPhone(ResetPwdsuceedActivity.this,bean.getListData().get(i).getOrgid_mobile());//公司电话
                    UserInfoUtils.setStoreHouseId(ResetPwdsuceedActivity.this,bean.getListData().get(i).getStorehouseid());//发货仓库id
                    UserInfoUtils.setLoginName(ResetPwdsuceedActivity.this,bean.getMobilePhone());//登录账号
                    UserInfoUtils.setLoginPwd(ResetPwdsuceedActivity.this,bean.getPassword());//登录密码
                    UserInfoUtils.setCompanyId(ResetPwdsuceedActivity.this,bean.getListData().get(i).getOrgid());//公司id
                    UserInfoUtils.setChangeCompanyFlag(ResetPwdsuceedActivity.this,"0");
                    Intent intent = new Intent(ResetPwdsuceedActivity.this ,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);//重置成功返回主页界面
                    finish_reset();
                }
            });
        }

    }
    private void finish_reset()
    {
        finish();
    }
}
